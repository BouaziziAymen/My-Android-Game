package com.evolgames.entities.particles.pools;

import com.evolgames.gameengine.ResourceManager;

import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.pool.GenericPool;

public class SmokeSpritePool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static final GenericPool<UncoloredSprite> POOL = new GenericPool<UncoloredSprite>() {
        @Override
        protected UncoloredSprite onAllocatePoolItem() {
            return new UncoloredSprite(0,0,ResourceManager.getInstance().plasmaParticle, ResourceManager.getInstance().vbom);
        }

    };


    public static UncoloredSprite obtain(float pX, float pY) {
        UncoloredSprite particle = SmokeSpritePool.POOL.obtainPoolItem();
        particle.reset();
        particle.setPosition(pX, pY);

        return particle;
    }

    public static void recycle(UncoloredSprite spark) {
        SmokeSpritePool.POOL.recyclePoolItem(spark);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
