package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;

import java.util.ArrayDeque;
import java.util.Iterator;

public class ShatterVisitor extends BreakVisitor<LayerBlock> {
    private final Vector2 localImpactPoint;
    private final WorldFacade worldFacade;
    private final GameEntity gameEntity;
    private float availableEnergy;
    private boolean isError = false;


    public ShatterVisitor(float energy, Vector2 localPoint, WorldFacade worldFacade, GameEntity gameEntity) {
        super();
        this.availableEnergy = energy;
        this.localImpactPoint = localPoint;
        this.worldFacade = worldFacade;
        this.gameEntity = gameEntity;

    }

    private void processBlock(LayerBlock layerBlock) {
        if (layerBlock.getBlockArea() < PhysicsConstants.MINIMUM_SPLINTER_AREA) {
            float totalRatio = 0;
            for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
                float ratio = (float) (1 - coatingBlock.getProperties().getBurnRatio() / 1.1f);
                totalRatio += ratio * coatingBlock.getArea() / layerBlock.getBlockArea();
            }
            totalRatio /= layerBlock.getBlockGrid().getCoatingBlocks().size();
            float pulverizationEnergy = PhysicsConstants.TENACITY_FACTOR * layerBlock.getProperties().getTenacity() * PhysicsConstants.PULVERIZATION_CONSTANT * totalRatio * layerBlock.getBlockArea();
            if (availableEnergy > pulverizationEnergy) {
                layerBlock.setAborted(true);
                layerBlock.getBlockGrid().getCoatingBlocks().forEach(coatingBlock -> coatingBlock.setPulverized(true));
                worldFacade.pulverizeBlock(layerBlock,gameEntity);
                shatterPerformed = true;
                availableEnergy -= pulverizationEnergy;
            }

        } else {
            ShatterData data = BlockUtils.applyCut(layerBlock, localImpactPoint);
            if (data == null) {
                isError = true;
                return;
            }
            if (availableEnergy - data.getDestructionEnergy() >= 0) {

                if (!data.isNonValid()) {
                    shatterPerformed = true;
                    layerBlock.performCut(data.getDestructionCut());
                }
                availableEnergy -= data.getDestructionEnergy();
            }
        }
    }

    @Override
    public void visitTheElement(LayerBlock cutBlock) {
        ArrayDeque<LayerBlock> deque = new ArrayDeque<>();
        deque.push(cutBlock);
        while (!deque.isEmpty()) {
            LayerBlock current = deque.peek();
            if (current == null) {
                break;
            }
            if (current.isNotAborted()) {
                this.processBlock(current); //visit
            }

            if (this.isExhausted() || this.isError()) {
                break;
            }
            for (LayerBlock child : current.getChildren()) {
                if (child.isNotAborted()) {
                    deque.addLast(child);
                }
            }

            System.out.println("---- looop ----");
            deque.pop();
        }


        Iterator<LayerBlock> iterator = cutBlock.createIterator();
        while (iterator.hasNext()) {
            LayerBlock bl = iterator.next();
            if (bl.isNotAborted()) {
                splintersBlocks.add(bl);
            } else {
                abortedBlocks.add(bl);
            }
        }

    }

    public boolean isExhausted() {
        return availableEnergy <= 0;
    }

    public boolean isError() {
        return isError;
    }
}
