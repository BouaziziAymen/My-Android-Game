package com.evolgames.userinterface.model;

public enum BodyUsageCategory {
  RANGED_MANUAL("Ranged manual","Ranged Options", 1), RANGED_SEMI_AUTOMATIC("Ranged sem. automatic","Ranged Options", 1),
    RANGED_AUTOMATIC("Ranged automatic","Ranged Options", 1),
    TIME_BOMB("Time Bomb","Bomb Options", 2),
    FUZE_BOMB("Fuze Bomb","Bomb Options", 2),
    IMPACT_BOMB("Impact Bomb","Bomb Options", 2),
    SLASHER("Slashing","Slashing Options", 3),
    BLUNT("Blunt","Blunt Options", 4),
    STABBER("Stabbing","Stabbing Options", 5),
    THROWING("Throwing","Throwing Options", 6);
    final String name;
    final int group;
    private final String optionsTitle;

    BodyUsageCategory(String name, String optionsTitle, int group){
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