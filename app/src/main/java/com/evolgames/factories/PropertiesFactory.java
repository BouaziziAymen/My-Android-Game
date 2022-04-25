package com.evolgames.factories;

import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.entities.properties.CollisionOption;
import com.evolgames.entities.properties.Material;

import org.andengine.util.adt.color.Color;

import java.util.Arrays;

public class PropertiesFactory {
    static final PropertiesFactory INSTANCE = new PropertiesFactory();
    public static PropertiesFactory getInstance() {
        return INSTANCE;
    }

    public BlockAProperties createProperties( Material material, Filter filter){

        BlockAProperties properties = new BlockAProperties();
        properties.setJuicy(material.isJuicy());
        properties.setFlammable(material.isFlammable());
        properties.setFilter(filter);



        properties.setProperties(new float[]{
                material.getDensity(),
                material.getRestitution(),
                material.getFriction(),
                material.getTenacity(),
                material.getJuicinessDensity(),
                material.getJuicinessLowerPressure(),
                material.getJuicinessUpperPressure()
        });

        if(material.getJuiceColor()!=null)
         properties.setJuiceColor(new Color(material.getJuiceColor()));
        properties.setDefaultColor(new Color(material.getColor()));
        properties.setCollisionOption(new CollisionOption());
        properties.setIgnitionTemperature(material.getIgnitionTemperature());
        properties.setFlameTemperature(material.getFlameTemperature());
        properties.setChemicalEnergy(material.getEnergy());

        return properties;
    }

    public BlockAProperties createProperties(BlockAProperties original){
        BlockAProperties properties = new BlockAProperties();
        properties.setProperties(Arrays.copyOf(original.getProperties(),original.getProperties().length));
        properties.setDefaultColor(new Color(original.getDefaultColor()));
        properties.setFilter(original.getFilter());
        properties.setCollisionOption(new CollisionOption());
        properties.setOrder(original.getOrder());
        properties.setJuicy(original.isJuicy());
        properties.setFlammable(original.isFlammable());
        if(original.getJuiceColor()!=null)
        properties.setJuiceColor(new Color(original.getJuiceColor()));
        properties.setIgnitionTemperature(original.getIgnitionTemperature());
        properties.setFlameTemperature(original.getFlameTemperature());
        properties.setChemicalEnergy(original.getChemicalEnergy());
        return properties;
    }

}
