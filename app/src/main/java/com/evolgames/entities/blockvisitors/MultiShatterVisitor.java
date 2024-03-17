package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.WorldFacade;
import com.evolgames.utilities.BlockUtils;

import java.util.List;

public class MultiShatterVisitor extends BreakVisitor<LayerBlock> {

    private final List<ImpactData> impacts;
    private final WorldFacade worldFacade;
    private final GameEntity gameEntity;

    public MultiShatterVisitor(
            List<ImpactData> impacts, WorldFacade worldFacade, GameEntity gameEntity) {
        super();
        this.impacts = impacts;
        this.worldFacade = worldFacade;
        this.gameEntity = gameEntity;
    }

    @Override
    public void visitTheElement(LayerBlock layerBlock) {
        splintersBlocks.add(layerBlock);
        for (int i = 0; i < this.impacts.size(); i++) {
            Vector2 point = this.impacts.get(i).getLocalImpactPoint();
            float energy = this.impacts.get(i).getImpactImpulse();
            LayerBlock nearest = BlockUtils.getNearestBlock(point, splintersBlocks);
            splintersBlocks.remove(nearest);
            ShatterVisitor shatterVisitor = new ShatterVisitor(energy, point, worldFacade, gameEntity);

            if (nearest != null) {
                shatterVisitor.visitTheElement(nearest);
                if (shatterVisitor.isShatterPerformed()) {
                    shatterPerformed = true;
                }
                splintersBlocks.addAll(shatterVisitor.getSplintersBlocks());
            }
        }
    }
}
