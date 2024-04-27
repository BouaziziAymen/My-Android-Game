package com.evolgames.dollmutilationgame.physics;

public class CollisionUtils {

    public static final short DOLL_CATEGORY = 0x1;
    public static final short OBJECTS_FRONT_CATEGORY = 0x20;
    public static final short OBJECTS_MIDDLE_CATEGORY = 0x40;
    public static final short OBJECTS_BACK_CATEGORY = 0x80;
    public static final short OBJECT = OBJECTS_BACK_CATEGORY | OBJECTS_MIDDLE_CATEGORY | OBJECTS_FRONT_CATEGORY;
    public static final short ALL_MASK = -1;
    public static final short DOLL_PART_GROUP_INDEX = -1;
    public static final short THROWN_ENTITY_GROUP_INDEX = -2;
    private static short groupIndex = 10;

    public static synchronized short groupIndex() {
        return (short) -(groupIndex++);
    }
}
