package com.evolgames.entities.particles;

import android.util.Log;

import com.evolgames.gameengine.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.pool.GenericPool;

public class LiquidParticlePool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static final GenericPool<UncoloredSprite> POOL = new GenericPool<UncoloredSprite>() {

        @Override
        protected UncoloredSprite onAllocatePoolItem() {
            return new UncoloredSprite(0,0, ResourceManager.getInstance().liquidParticle,ResourceManager.getInstance().vbom);
        }

    };


    public static UncoloredSprite obtain(float pX, float pY) {
        UncoloredSprite uncoloredSprite = LiquidParticlePool.POOL.obtainPoolItem();
        uncoloredSprite.setPosition(pX, pY);
        return uncoloredSprite;
    }

    public static void recycle(UncoloredSprite uncoloredSprite) {
        LiquidParticlePool.POOL.recyclePoolItem(uncoloredSprite);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
