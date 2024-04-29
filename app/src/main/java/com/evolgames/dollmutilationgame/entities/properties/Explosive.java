package com.evolgames.dollmutilationgame.entities.properties;

import com.evolgames.dollmutilationgame.R;

public enum Explosive {
    OTHER(R.string.other, 0f, 0f, 0f),
    POWDER(R.string.black_powder, 0.7f, 0.2f, 0.1f),
    SMOKELESS_POWDER(R.string.smokeless_powder, 0.7f, 0f, 0.3f);
    public final int stringId;
    final float fireRatio;
    final float smokeRatio;
    final float SparkRatio;

    Explosive(int stringId, float fireRatio, float smokeRatio, float sparkRatio) {
        this.stringId = stringId;
        this.fireRatio = fireRatio;
        this.smokeRatio = smokeRatio;
        SparkRatio = sparkRatio;
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
