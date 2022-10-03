package com.evolgames.entities.particles.pools;

import com.evolgames.entities.particles.systems.Spark;

import org.andengine.util.adt.pool.GenericPool;

public class SparkPool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static final GenericPool<Spark> POOL = new GenericPool<Spark>() {

        @Override
        protected Spark onAllocatePoolItem() {
            return new Spark();
        }

    };



    public static Spark obtain(float pX, float pY) {
        Spark spark = SparkPool.POOL.obtainPoolItem();
        spark.setPosition(pX, pY);
        return spark;
    }

    public static void recycle(Spark spark) {
SparkPool.POOL.recyclePoolItem(spark);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
