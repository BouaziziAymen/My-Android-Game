package com.evolgames.physics.entities.explosions;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blockvisitors.ImpactData;

import java.util.List;

@FunctionalInterface
public interface ImpactInterface {
  void processImpacts(GameEntity gameEntity, List<ImpactData> impacts);
}
