package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.WorldFacade;

import java.util.List;

public class MultiShatterVisitor extends BreakVisitor<LayerBlock> {

    private final List<ImpactData> impacts;
    private final WorldFacade worldFacade;
    private final GameEntity gameEntity;

    public MultiShatterVisitor(List<ImpactData> impacts, WorldFacade worldFacade, GameEntity gameEntity) {
        super();
        this.impacts = impacts;
        this.worldFacade = worldFacade;
        this.gameEntity = gameEntity;
    }

    private LayerBlock getNearestBlock(Vector2 point, List<LayerBlock> blocks) {
        float minDistance = Float.MAX_VALUE;
        LayerBlock result = null;
        for (LayerBlock layerBlock : blocks) {
            CoatingBlock nearest = layerBlock.getBlockGrid().getNearestCoatingBlockSimple(point);
            float distance = nearest.distance(point);
            if (distance < minDistance) {
                result = layerBlock;
                minDistance = distance;
            }
        }
        return result;
    }

    @Override
    public void visitTheElement(LayerBlock layerBlock) {
        splintersBlocks.add(layerBlock);
        for (int i = 0; i < this.impacts.size(); i++) {
            Vector2 point = this.impacts.get(i).getLocalImpactPoint();
            float energy = this.impacts.get(i).getImpactEnergy();
            LayerBlock nearest = getNearestBlock(point, splintersBlocks);
            splintersBlocks.remove(nearest);
            ShatterVisitor shatterVisitor = new ShatterVisitor(energy, point, worldFacade, gameEntity);
            if(nearest!=null) {
                shatterVisitor.visitTheElement(nearest);
                if (shatterVisitor.isShatterPerformed()) {
                    shatterPerformed = true;
                }
                splintersBlocks.addAll(shatterVisitor.getSplintersBlocks());
            }
        }
    }

}