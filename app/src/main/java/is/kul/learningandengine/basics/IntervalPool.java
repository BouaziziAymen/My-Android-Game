package is.kul.learningandengine.basics;

import com.badlogic.gdx.math.Vector2;

import org.andengine.util.adt.pool.GenericPool;


import org.andengine.util.adt.pool.GenericPool;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ayem on 21/05/2018.
 */

public class IntervalPool {


    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static final GenericPool<Interval> POOL = new GenericPool<Interval>() {
        @Override
        protected Interval onAllocatePoolItem() {
            return new Interval();
        }
    };

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public static Interval obtain() {
        return POOL.obtainPoolItem();
    }



    public static Interval obtain(final float s, final float e,final float h) {
        return POOL.obtainPoolItem().set(s,e,h);
    }

    public static void recycle(final Interval pVector2) {
        POOL.recyclePoolItem(pVector2);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
