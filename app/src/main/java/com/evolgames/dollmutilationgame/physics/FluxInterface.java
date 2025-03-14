package com.evolgames.dollmutilationgame.physics;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

@FunctionalInterface
public interface FluxInterface {
    void computeFluxEffect(
            LayerBlock block,
            GameEntity entity,
            Vector2 direction,
            Vector2 source,
            Vector2 target,
            float angle);
}
