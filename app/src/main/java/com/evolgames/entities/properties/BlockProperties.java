package com.evolgames.entities.properties;

public abstract class BlockProperties extends PropertiesWithColor {

    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public abstract BlockProperties getCopy();

}
