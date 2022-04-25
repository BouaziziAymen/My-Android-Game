package com.evolgames.userinterface.model;

import com.evolgames.entities.properties.BlockProperties;
import com.evolgames.entities.properties.Properties;

public abstract class ProperModel extends Model {
    protected Properties properties;

    public ProperModel(String name){
        super(name);
    }

    public Properties getProperty() {
        return properties;
    }

    public void setProperty(Properties property) {
        properties = property;
    }
}
