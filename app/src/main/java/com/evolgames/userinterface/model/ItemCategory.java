package com.evolgames.userinterface.model;

public enum ItemCategory {
    SWORD("Swords"), AXE("Axes"), SPEAR("Spears"),PRODUCE("Produce"),
    BULLET("Bullets"), SHELL("Shells"), KNIFE("Knives"), OTHER("Other"),
    MACE("Maces"), GRENADE("Grenades"), GUN("Guns"), HEAVY("Heavy Weapons"),
    PROJECTILE("Projectiles"),ROCKET("Rockets"), ARROW("Arrows"), BOW("Bows");
    public final String name;

    ItemCategory(String name) {
        this.name = name;
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
