package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import com.evolgames.utilities.GeometryUtils;

import org.andengine.util.adt.color.Color;

import java.util.List;

public class DecorationModel extends PointsModel<DecorationProperties> {
    private final LayerModel layerModel;
    private final int decorationId;
    private final int bodyId;
    private final int layerId;
    private DecorationField field;

    public DecorationModel(
            int bodyId, int layerId, int decorationId, String decorationName, DecorationProperties properties, LayerModel layerModel) {
        super(decorationName);
        this.bodyId = bodyId;
        this.layerId = layerId;
        this.decorationId = decorationId;
        this.properties = properties;
        this.layerModel = layerModel;
    }

    public DecorationModel(int bodyId, int layerId, int decorationId, LayerModel layerModel) {
        super("Decoration" + decorationId);
        this.bodyId = bodyId;
        this.layerId = layerId;
        this.decorationId = decorationId;
        this.layerModel = layerModel;
        properties = new DecorationProperties(new Color(Color.WHITE));
    }

    public String toString() {
        return "Decoration" + getDecorationId() + ": \n";
    }

    public int getDecorationId() {
        return decorationId;
    }

    @Override
    public boolean testMove(Vector2 movedPoint, float dx, float dy) {
        return GeometryUtils.isPointInPolygon(movedPoint.x + dx, movedPoint.y + dy, layerModel.getOutlinePoints());
    }

    @Override
    public boolean testPoints(List<Vector2> points) {
        for (Vector2 p : points) {
            if (!GeometryUtils.isPointInPolygon(p.x, p.y, layerModel.getOutlinePoints())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean testAdd(float x, float y) {
        return GeometryUtils.isPointInPolygon(x, y, layerModel.getOutlinePoints());
    }

    public DecorationField getField() {
        return field;
    }

    public void setField(DecorationField field) {
        this.field = field;
    }

    public LayerModel getLayerModel() {
        return layerModel;
    }

}
