package com.evolgames.entities.particles.emitters;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;

import java.util.ArrayList;


public class FireEmitter extends RelativePolygonEmitter {

    public FireEmitter(GameEntity entity) {
        super(entity, CoatingBlock::isOnFire);
    }


    public Object getUserData() {
        return associatedCoatingBlocks[selectedTriangle];
    }

}
