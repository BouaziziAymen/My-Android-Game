package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;

import java.util.List;

public class MultiShatterVisitor extends BreakVisitor<LayerBlock> {

    private final List<ImpactData> impacts;

    public MultiShatterVisitor(List<ImpactData> impacts) {
        super();
        this.impacts = impacts;
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
            ShatterVisitor shatterVisitor = new ShatterVisitor(energy, point);
            shatterVisitor.visitTheElement(nearest);
            if(shatterVisitor.isShatterPerformed()){
                shatterPerformed = true;
            }
            splintersBlocks.addAll(shatterVisitor.getSplintersBlocks());
        }
    }

}