package com.evolgames.userinterface.model;

public class Model {
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    private String modelName;
    public Model(String name){
        this.modelName = name;
    }
}
