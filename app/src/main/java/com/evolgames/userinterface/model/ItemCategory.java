package com.evolgames.userinterface.model;

public enum ItemCategory {
    SWORD("Swords"), AXE("Axes"), SPEAR("Spears"),PRODUCE("Produce"),
    BULLET("Bullets",true), SHELL("Shells",true), KNIFE("Knives"), OTHER("Other"),
    MACE("Maces"), GRENADE("Grenades"), GUN("Guns"), HEAVY("Heavy Weapons"),
    PROJECTILE("Projectiles",true),ROCKET("Rockets"), ARROW("Arrows",true), BOW("Bows");
    public final String name;
    public final boolean nonCreatable;

    ItemCategory(String name) {
        this.name = name;
        this.nonCreatable = false;
    }
    ItemCategory(String name, boolean nonCreatable) {
        this.name = name;
        this.nonCreatable = nonCreatable;
    }
    public static ItemCategory fromName(String categoryString) {
        for (ItemCategory category : values()) {
            if (category.name().equalsIgnoreCase(categoryString)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No ItemCategory with name: " + categoryString);
    }
}
