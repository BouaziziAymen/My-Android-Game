package com.evolgames.entities.properties;

public enum BodyUsageCategory {
    SHOOTER("Shooter", "Ranged Options", 1),
    SHOOTER_CONTINUOUS("Shooter continuous", "Ranged Options", 1),
    BOW("Bow", "Bow Options", 1),
    TIME_BOMB("Time Bomb", "Bomb Options", 2),
    FUZE_BOMB("Fuze Bomb", "Bomb Options", 2),
    IMPACT_BOMB("Impact Bomb", "Bomb Options", 2),
    SLASHER("Slashing", "Slashing Options", 3),
    BLUNT("Blunt", "Blunt Options", 4),
    STABBER("Stabbing", "Stabbing Options", 5),
    THROWING("Throwing", "Throwing Options", 6),
    FLAME_THROWER("Flame Thrower", "Flame Thr. Options", 7),
    ROCKET("Rocket", "Rocket. Options", 8),
    MISSILE("Missile", "Missile. Options", 8),
    LIQUID_CONTAINER("Liquid Container", "Container. Options", 9),
    ROCKET_LAUNCHER("Rocket Launcher", "Rock. Launch. Options", 7);
    final String name;
    final int group;
    private final String optionsTitle;

    BodyUsageCategory(String name, String optionsTitle, int group) {
        this.name = name;
        this.group = group;
        this.optionsTitle = optionsTitle;
    }

    public String getName() {
        return name;
    }

    public int getGroup() {
        return group;
    }

    public String getOptionsTitle() {
        return optionsTitle;
    }

    @Override
    public String toString() {
        return name;
    }
}
