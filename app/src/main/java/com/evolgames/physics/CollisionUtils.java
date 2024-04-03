package com.evolgames.physics;

public class CollisionUtils {

    public static final short DOLL_CATEGORY = 0x1;
    public static final short OBJECTS_FRONT_CATEGORY = 0x20;
    public static final short OBJECTS_MIDDLE_CATEGORY = 0x40;
    public static final short OBJECTS_BACK_CATEGORY = 0x80;
    public static final short OBJECT = OBJECTS_BACK_CATEGORY | OBJECTS_MIDDLE_CATEGORY | OBJECTS_FRONT_CATEGORY;
    public static final short ALL_MASK = -1;
    private static short groupIndex = 2;

    public static synchronized short groupIndex() {
        return (short) -(groupIndex++);
    }
}
