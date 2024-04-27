package com.evolgames.dollmutilationgame.entities.properties;

import androidx.annotation.NonNull;

public abstract class Properties implements Cloneable {
    @NonNull
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
