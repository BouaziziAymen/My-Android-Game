package com.evolgames.entities.blockvisitors;

import static com.evolgames.physics.PhysicsConstants.MINIMUM_STABLE_SPLINTER_AREA;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.utilities.BlockUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShatterVisitor extends BreakVisitor<LayerBlock> {
    private final Vector2 localImpactPoint;
    private final WorldFacade worldFacade;
    private final GameEntity gameEntity;
    private float availableImpulse;
    private boolean isError = false;

    public ShatterVisitor(
            float impulse, Vector2 localPoint, WorldFacade worldFacade, GameEntity gameEntity) {
        super();
        this.availableImpulse = impulse;
        this.localImpactPoint = localPoint;
        this.worldFacade = worldFacade;
        this.gameEntity = gameEntity;
    }

    private void processBlock(LayerBlock layerBlock, boolean isFirst) {
        float pulverizationImpulse;
        if (layerBlock.getBlockArea() < 10 * MINIMUM_STABLE_SPLINTER_AREA && availableImpulse > (pulverizationImpulse = calculatePulverizationImpulse(layerBlock))) {
            layerBlock.setAborted(true);
            layerBlock
                    .getBlockGrid()
                    .getCoatingBlocks()
                    .forEach(coatingBlock -> coatingBlock.setPulverized(true));
            worldFacade.pulverizeBlock(layerBlock, gameEntity);
            shatterPerformed = true;
            availableImpulse -= pulverizationImpulse;
        } else {
            ShatterData data = BlockUtils.applyCut(layerBlock, localImpactPoint);
            if (data == null) {
                isError = true;
                return;
            }
            if (availableImpulse - data.getDestructionEnergy() >= 0) {

                if (!data.isNonValid() && (isFirst || layerBlock.getBlockArea() >= 5 * MINIMUM_STABLE_SPLINTER_AREA)) {
                    shatterPerformed = true;
                    layerBlock.performCut(data.getDestructionCut());
                }
                availableImpulse -= data.getDestructionEnergy();
            }
        }
    }

    public float calculatePulverizationImpulse(LayerBlock layerBlock) {
        float totalRatio = 0;
        for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
            float ratio = (float) (1.1f - coatingBlock.getProperties().getBurnRatio());
            totalRatio += ratio * coatingBlock.getArea() / layerBlock.getBlockArea();
        }
        totalRatio /= layerBlock.getBlockGrid().getCoatingBlocks().size();
        return PhysicsConstants.TENACITY_FACTOR
                                * layerBlock.getTenacity()
                                * PhysicsConstants.PULVERIZATION_CONSTANT
                                * totalRatio
                                * layerBlock.getBlockArea();
    }

    @Override
    public List<LayerBlock> visitTheElement(LayerBlock cutBlock) {
        ArrayDeque<LayerBlock> deque = new ArrayDeque<>();
        deque.push(cutBlock);
        while (!deque.isEmpty()) {
            LayerBlock current = deque.peek();
            if (current == null) {
                break;
            }
            if (current.isNotAborted()) {
                this.processBlock(current, cutBlock == current); // visit
            }

            if (this.isExhausted() || this.isError()) {
                break;
            }
            for (LayerBlock child : current.getChildren()) {
                if (child.isNotAborted()) {
                    deque.addLast(child);
                }
            }

            deque.pop();
        }
        List<LayerBlock> splinters = new ArrayList<>();
        Iterator<LayerBlock> iterator = cutBlock.createIterator();
        while (iterator.hasNext()) {
            LayerBlock bl = iterator.next();
            if (bl.isNotAborted()) {
                splinters.add(bl);
            } else {
                abortedBlocks.add(bl);
            }
            bl.getChildren().clear();
        }
        return splinters;
    }

    public boolean isExhausted() {
        return availableImpulse <= 0;
    }

    public boolean isError() {
        return isError;
    }
}
