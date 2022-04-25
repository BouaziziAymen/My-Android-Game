package com.evolgames.entities.particles;

import com.evolgames.gameengine.ResourceManager;

import org.andengine.entity.sprite.UncoloredSprite;

import is.kul.learningandengine.particlesystems.FireParticleSystem;


public class Spark extends UncoloredSprite {


    public Spark() {
        super(0, 0, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
    }

}
