package com.evolgames.dollmutilationgame.entities.particles.pools;

import com.evolgames.dollmutilationgame.activity.ResourceManager;

import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.pool.GenericPool;

public class SparkSpritePool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static final GenericPool<UncoloredSprite> POOL =
            new GenericPool<UncoloredSprite>() {
                @Override
                protected UncoloredSprite onAllocatePoolItem() {
                    return new UncoloredSprite(
                            0, 0, ResourceManager.getInstance().dotParticle, ResourceManager.getInstance().vbom);
                }
            };

    public static UncoloredSprite obtain(float pX, float pY) {
        UncoloredSprite particle = SparkSpritePool.POOL.obtainPoolItem();
        particle.reset();
        particle.setPosition(pX, pY);
        particle.getUserData();
        return particle;
    }

    public static void recycle(UncoloredSprite spark) {
        SparkSpritePool.POOL.recyclePoolItem(spark);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
