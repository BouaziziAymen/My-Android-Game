package com.evolgames.dollmutilationgame.entities.pools;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.pool.GenericPool;

public class ColorModifierPool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    private static final GenericPool<ColorModifier> POOL =
            new GenericPool<ColorModifier>() {
                @Override
                protected ColorModifier onAllocatePoolItem() {
                    return new ColorModifier();
                }
            };
    // ===========================================================
    // Fields
    // ===========================================================
    public static int recyledTimes;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public static ColorModifier obtain(
            final float pDuration, final Color pFromColor, final Color pToColor) {
        ColorModifier colorModifier = POOL.obtainPoolItem();
        colorModifier.reset(
                pDuration,
                pFromColor.getRed(),
                pToColor.getRed(),
                pFromColor.getGreen(),
                pToColor.getGreen(),
                pFromColor.getBlue(),
                pToColor.getBlue());
        return colorModifier;
    }

    public static void recycle(final ColorModifier colorModifier) {

        POOL.recyclePoolItem(colorModifier);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
