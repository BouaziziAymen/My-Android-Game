package com.evolgames.dollmutilationgame.physics.entities.explosions;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blockvisitors.ImpactData;

import java.util.List;

@FunctionalInterface
public interface ImpactInterface {
    void processImpacts(GameEntity gameEntity, List<ImpactData> impacts);
}
