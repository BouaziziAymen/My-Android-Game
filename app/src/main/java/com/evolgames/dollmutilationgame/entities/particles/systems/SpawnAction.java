package com.evolgames.dollmutilationgame.entities.particles.systems;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.UncoloredSprite;

public interface SpawnAction {
    void run(Particle<UncoloredSprite> particle);
}
