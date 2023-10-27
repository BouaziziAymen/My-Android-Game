package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;

import org.andengine.util.adt.color.Color;

import java.util.List;

import is.kul.learningandengine.helpers.Utils;

public class DecorationModel extends PointsModel<DecorationProperties> {
    private static final float MINIMAL_DISTANCE_BETWEEN_VERTICES = 4f;
    private final LayerModel layerModel;
    private final int decorationId;
    private DecorationField field;

    public DecorationModel(LayerModel layerModel, int decorationId) {
        super("Decoration" + decorationId);
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
    public boolean test(Vector2 movedPoint, float dx, float dy) {
        boolean inside = Utils.PointInPolygon(movedPoint.x + dx, movedPoint.y + dy, layerModel.getOutlinePoints());
        if (!inside) return false;
        for (Vector2 p : getPoints())
            if (p != movedPoint)
                if (p.dst(movedPoint.x + dx, movedPoint.y + dy) < MINIMAL_DISTANCE_BETWEEN_VERTICES)
                    return false;
        return true;

    }

    @Override
    public boolean test(List<Vector2> points) {
        for (Vector2 p : points) {
            if (!Utils.PointInPolygon(p.x, p.y, layerModel.getOutlinePoints())) return false;
        }

        return true;
    }

    @Override
    public boolean test(float x, float y) {

        boolean inside = Utils.PointInPolygon(x, y, layerModel.getOutlinePoints());
        if (!inside) return false;
        for (Vector2 p : getPoints())
            if (p.dst(x, y) < MINIMAL_DISTANCE_BETWEEN_VERTICES) return false;
        return true;
    }

    public void setField(DecorationField field) {
        this.field = field;
    }

    public DecorationField getField() {
        return field;
    }

    public LayerModel getLayerModel() {
        return layerModel;
    }
}
