package com.evolgames.userinterface.model;

public enum ItemCategory {
    MELEE("Melee"),GUN("Gun"),PROJECTILE("Projectile"), ROCKET("Rocket");
    public final String name;
    ItemCategory(String name){
        this.name = name;
    }
}
