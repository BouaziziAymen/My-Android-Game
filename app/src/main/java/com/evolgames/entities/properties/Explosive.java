package com.evolgames.entities.properties;

public enum Explosive {
    OTHER("Other", 0f, 0f, 0f),
    POWDER("Black Powder", 0.7f, 0.2f, 0.1f),
    SMOKELESS_POWDER("Smokeless Powder", 0.7f, 0f, 0.3f);
    final String name;
    final float fireRatio;
    final float smokeRatio;
    final float SparkRatio;

    Explosive(String name, float fireRatio, float smokeRatio, float sparkRatio) {
        this.name = name;
        this.fireRatio = fireRatio;
        this.smokeRatio = smokeRatio;
        SparkRatio = sparkRatio;
    }

    public String getName() {
        return name;
    }

    public float getFireRatio() {
        return fireRatio;
    }

    public float getSmokeRatio() {
        return smokeRatio;
    }

    public float getSparkRatio() {
        return SparkRatio;
    }
}
