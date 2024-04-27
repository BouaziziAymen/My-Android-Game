package com.evolgames.dollmutilationgame.userinterface.model;

import com.evolgames.dollmutilationgame.entities.properties.Properties;

public abstract class ProperModel<T extends Properties> extends Model {
    protected T properties;

    public ProperModel(String name) {
        super(name);
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T property) {
        properties = property;
    }
    public void setProperties(Object property) {
        properties = (T) property;
    }
}
