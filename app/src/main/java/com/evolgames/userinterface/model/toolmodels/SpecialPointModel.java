package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.SpecialPointProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.SpecialPointShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.SpecialPointField;

public class SpecialPointModel extends ProperModel<SpecialPointProperties> {

    private final int bodyId;
    private final int pointId;
    private SpecialPointShape specialPointShape;
    private SpecialPointField specialPointField;

    public SpecialPointModel(int bodyId, int pointId) {
        super("Point" + pointId);
        this.bodyId = bodyId;
        this.pointId = pointId;
    }

    public SpecialPointModel(int bodyId, int pointId, SpecialPointShape specialPointShape) {
        super("Point" + pointId);
        this.bodyId = bodyId;
        this.pointId = pointId;
        this.specialPointShape = specialPointShape;
        this.properties = new SpecialPointProperties();
    }

    public SpecialPointShape getSpecialPointShape() {
        return specialPointShape;
    }

    public void setSpecialPointShape(SpecialPointShape specialPointShape) {
        this.specialPointShape = specialPointShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getPointId() {
        return pointId;
    }

    public SpecialPointField getSpecialPointField() {
        return specialPointField;
    }

    public void setSpecialPointField(SpecialPointField bombField) {
        this.specialPointField = bombField;
    }


}
