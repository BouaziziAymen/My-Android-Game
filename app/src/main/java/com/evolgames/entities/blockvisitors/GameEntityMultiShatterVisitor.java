package com.evolgames.entities.blockvisitors;

import static com.evolgames.physics.PhysicsConstants.MINIMUM_STABLE_SPLINTER_AREA;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.physics.WorldFacade;

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
      MultiShatterVisitor blockShatterVisitor =
          new MultiShatterVisitor(
              impacts.stream()
                  .filter(impactData -> impactData.getImpactedBlock() == layerBlock)
                  .collect(Collectors.toList()),
              worldFacade,
              gameEntity);
        blockShatterVisitor.visitTheElement(layerBlock);

      if (blockShatterVisitor.isShatterPerformed()) {
        shatterPerformed = true;
        iterator.remove();
      } else {
        float energy =
            (float)
                impacts.stream()
                    .filter(e -> e.getImpactedBlock() == layerBlock)
                    .mapToDouble(ImpactData::getImpactImpulse)
                    .sum();
        LayerProperties properties = layerBlock.getProperties();
        float ratio =
            (float) Math.min(0.3f, 0.002f * energy / (Math.pow(properties.getTenacity(), 6)));
        float newTenacity = properties.getTenacity() * (1f - ratio);
        properties.setTenacity(newTenacity);
      }
      this.splintersBlocks.addAll(blockShatterVisitor.getSplintersBlocks());
    }
  }
}
