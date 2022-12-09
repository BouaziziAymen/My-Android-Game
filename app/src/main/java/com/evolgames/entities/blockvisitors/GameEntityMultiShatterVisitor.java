package com.evolgames.entities.blockvisitors;

import static com.evolgames.physics.WorldFacade.removeLiquidSource;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.FreshCut;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GameEntityMultiShatterVisitor extends BreakVisitor<GameEntity> {

    private final List<ImpactData> impacts;
    public GameEntityMultiShatterVisitor(List<ImpactData> impacts) {
        this.impacts = impacts;
    }

    @Override
    public void visitTheElement(GameEntity gameEntity) {
        Iterator<LayerBlock> iterator = gameEntity.getBlocks().iterator();
        while(iterator.hasNext()) {
            LayerBlock layerBlock = iterator.next();
            MultiShatterVisitor blockShatterVisitor = new MultiShatterVisitor(impacts.stream().filter(impactData ->
                    impactData.getImpactedBlock() == layerBlock).collect(Collectors.toList()));
            blockShatterVisitor.visitTheElement(layerBlock);
            if (blockShatterVisitor.isShatterPerformed()) {
                shatterPerformed = true;
                iterator.remove();
                onBlockShattered(layerBlock);
            }
            this.splintersBlocks.addAll(blockShatterVisitor.getSplintersBlocks());
        }
    }
    private void onBlockShattered(LayerBlock block){
        for (FreshCut freshCut : block.getFreshCuts()) {
            removeLiquidSource(freshCut.getLiquidParticleWrapper());
        }
    }
}
