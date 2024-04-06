package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GameEntityMultiShatterVisitor extends BreakVisitor<GameEntity> {

    private final List<ImpactData> impacts;
    private final WorldFacade worldFacade;

    public GameEntityMultiShatterVisitor(List<ImpactData> impacts, WorldFacade worldFacade) {
        this.impacts = impacts;
        this.worldFacade = worldFacade;
    }

    @Override
    public void visitTheElement(GameEntity gameEntity) {
        Iterator<LayerBlock> iterator = new ArrayList<>(gameEntity.getBlocks()).iterator();
        while (iterator.hasNext()) {
            LayerBlock layerBlock = iterator.next();
            List<ImpactData> layerImpacts = impacts.stream()
                    .filter(impactData -> impactData.getImpactedBlock() == layerBlock)
                    .collect(Collectors.toList());
            List<Vector2> localCenters = impacts.stream().map(ImpactData::getLocalImpactPoint).collect(Collectors.toList());
            Vector2 localCenter = GeometryUtils.calculateCenterScatter(localCenters);
            float impactEnergy = (float) layerImpacts.stream().mapToDouble(ImpactData::getImpactImpulse).sum();
            ShatterVisitor blockShatterVisitor =
                    new ShatterVisitor(impactEnergy
                            , localCenter,
                            worldFacade,
                            gameEntity);
            blockShatterVisitor.visitTheElement(layerBlock);

            if (blockShatterVisitor.isShatterPerformed()) {
                shatterPerformed = true;
                iterator.remove();
            } else {
                applyStrain(layerBlock);
            }
            this.splintersBlocks.addAll(blockShatterVisitor.getSplintersBlocks());
        }
    }

    private void applyStrain(LayerBlock layerBlock) {
        float energy =
                (float)
                        impacts.stream()
                                .filter(e -> e.getImpactedBlock() == layerBlock)
                                .mapToDouble(ImpactData::getImpactImpulse)
                                .sum();
        LayerProperties properties = layerBlock.getProperties();
        float ratio =
                (float) Math.min(0.1f, 0.002f * energy / (Math.pow(layerBlock.getTenacity(), 10)));
        float newTenacity = layerBlock.getTenacity() * (1f - ratio);
        properties.setTenacity(newTenacity);
    }
}
