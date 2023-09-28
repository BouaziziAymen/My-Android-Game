package com.evolgames.userinterface.model;

public enum BodyUsageCategory {
  RANGED_MANUAL("Ranged manual","Ranged Options",6,1), RANGED_SEMI_AUTOMATIC("Ranged sem. automatic","Ranged Options",6,1),
    RANGED_AUTOMATIC("Ranged automatic","Ranged Options",6,1),
    TIME_BOMB("Time Bomb","Bomb Options",7,2),
    FUZE_BOMB("Fuze Bomb","Bomb Options",7,2),
    IMPACT_BOMB("Impact Bomb","Bomb Options",7,2),
    SLASHER("Slashing","Slashing Options",2,3),
    BLUNT("Blunt","Blunt Options",3,4),
    STABBER("Stabbing","Stabbing Options",4,5),
    THROWING("Throwing","Throwing Options",5,6);
    final String name;
    final int group;
    private final String optionsTitle;
    private final int regionIndex;

    BodyUsageCategory(String name,String optionsTitle, int index,int group){
        this.name = name;
        this.group = group;
        this.optionsTitle = optionsTitle;
        this.regionIndex = index;
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