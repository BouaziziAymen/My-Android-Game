package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.shapes.OutlineShape;

public abstract class OutlineModel<T extends Properties> extends ProperModel<T>{
    protected OutlineShape<?> outlineShape;
    protected Vector2[] outlinePoints;
    private boolean isSelected;

    public OutlineModel(String name) {
        super(name);
    }

    public Vector2[] getOutlinePoints(){
        return outlinePoints;
    }

    public void setOutlinePoints(Vector2[] points){
        this.outlinePoints = points;
    }

    public abstract void updateOutlinePoints();

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
