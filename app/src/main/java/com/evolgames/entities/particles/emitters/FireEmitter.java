package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;

public class FireEmitter extends RelativePolygonEmitter {

    public FireEmitter(GameEntity entity) {
        super(entity, CoatingBlock::isOnFire);
    }
}
