package com.evolgames.physics;

public class CollisionConstants {


    public static final short DOLL_CATEGORY = 0x1;
    public static final short GUN_CATEGORY = 0x2;
    public static final short PROJECTILE_CATEGORY = 0x4;
    public static final short CASING_CATEGORY = 0x10;
    public static final short OBJECTS_FRONT_CATEGORY = 0x20;
    public static final short OBJECTS_MIDDLE_CATEGORY = 0x40;
    public static final short OBJECTS_BACK_CATEGORY = 0x80;
    private static final short OBJECTS_CATEGORY = OBJECTS_FRONT_CATEGORY|OBJECTS_MIDDLE_CATEGORY|OBJECTS_BACK_CATEGORY;
    public static final short DOLL_MASK = -1;
    public static final short GUN_MASK = DOLL_CATEGORY|OBJECTS_CATEGORY|GUN_CATEGORY;
    public static final short PROJECTILE_MASK = DOLL_CATEGORY|OBJECTS_MIDDLE_CATEGORY|CASING_CATEGORY;
    public static final short CASING_MASK = DOLL_CATEGORY|OBJECTS_CATEGORY|CASING_CATEGORY|PROJECTILE_CATEGORY;

}
