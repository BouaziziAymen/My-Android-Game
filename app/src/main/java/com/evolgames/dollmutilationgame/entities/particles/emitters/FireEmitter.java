package com.evolgames.dollmutilationgame.entities.particles.emitters;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;

public class FireEmitter extends RelativePolygonEmitter {

    public FireEmitter(GameEntity entity) {
        super(entity, CoatingBlock::isOnFire);
    }
}
