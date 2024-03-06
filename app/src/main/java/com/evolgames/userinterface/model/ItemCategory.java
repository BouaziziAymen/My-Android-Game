package com.evolgames.userinterface.model;

public enum ItemCategory {
    SWORD("Swords"),  SPEAR("Spears"),BULLET("Bullets"),KNIFE("Knives"),OTHER("Other"), MACE("Maces"),GRENADE("Grenades"),GUN("Gun"),PROJECTILE("Projectile"), ROCKET("Rocket");
    public final String name;
    ItemCategory(String name){
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
