package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LayerModel extends PointsModel<LayerProperties> {
    private static final float MINIMAL_DISTANCE_BETWEEN_VERTICES = 1f;
    private final BodyModel bodyModel;
    private final AtomicInteger decorationCounter = new AtomicInteger();
    private final ArrayList<DecorationModel> decorations;
    private final int bodyId;
    private final int layerId;
    private LayerField field;

    public LayerModel(int bodyId, int layerId, String layerName, LayerProperties properties, BodyModel bodyModel) {
        super(layerName);
        this.bodyId = bodyId;
        this.layerId = layerId;
        this.decorations = new ArrayList<>();
        this.properties = properties;
        this.bodyModel = bodyModel;
    }

    LayerModel(int bodyId, int layerId, BodyModel bodyModel) {
        super("Layer" + layerId);
        this.bodyId = bodyId;
        this.layerId = layerId;
        this.decorations = new ArrayList<>();
        this.properties = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0));
        this.properties.setLayerName("Layer" + layerId);
        this.bodyModel = bodyModel;
    }

    @Override
    public String toString() {
        return "Layer:" + bodyId + "/" + getLayerId() + "/" + Arrays.toString(getPoints().toArray());
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getLayerId() {
        return layerId;
    }

    private DecorationModel getDecorationModelById(int decorationId) {
        for (DecorationModel decoration : decorations) {
            if (decoration.getDecorationId() == decorationId) {
                return decoration;
            }
        }
        return null;
    }

    DecorationModel createDecoration() {
        DecorationModel decorationModel = new DecorationModel(this, decorationCounter.getAndIncrement());
        decorations.add(decorationModel);
        return decorationModel;
    }


    DecorationModel removeDecoration(int decorationId) {
        DecorationModel decorationModel = getDecorationModelById(decorationId);
        decorations.remove(decorationModel);
        return decorationModel;
    }


    @Override
    public boolean test(Vector2 movedPoint, float dx, float dy) {

        for (Vector2 p : getPoints())
            if (p != movedPoint)
                if (p.dst(movedPoint.x + dx, movedPoint.y + dy) < MINIMAL_DISTANCE_BETWEEN_VERTICES)
                    return false;
        float x = movedPoint.x;
        float y = movedPoint.y;
        movedPoint.set(x + dx, y + dy);
        boolean test = true;
        for (DecorationModel decorationModel : decorations) {
            if (!decorationModel.test(decorationModel.getPoints())) {
                test = false;
                break;
            }
        }
        movedPoint.set(x, y);
        return test;
    }

    @Override
    public boolean test(float x, float y) {
        for (Vector2 p : getPoints()) {
            if (p.dst(x, y) < MINIMAL_DISTANCE_BETWEEN_VERTICES) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean test(List<Vector2> newPoints) {
        Vector2[] originalPoints = getOutlinePoints();
        setOutlinePoints(newPoints.toArray(new Vector2[0]));
        for (DecorationModel decorationModel : decorations) {
            if (!decorationModel.test(decorationModel.getPoints())) {
                setOutlinePoints(originalPoints);
                return false;
            }
        }

        return true;
    }

    DecorationModel getDecorationById(int tertiaryKey) {
        for (DecorationModel model : decorations)
            if (model.getDecorationId() == tertiaryKey) return model;
        return null;
    }



    public ArrayList<PointsShape> getPointsShapes() {
        ArrayList<PointsShape> result = new ArrayList<>();
        for (DecorationModel model : decorations) {
            result.add(model.getPointsShape());
        }
        result.add(getPointsShape());
        return result;
    }

    public ArrayList<DecorationModel> getDecorations() {
        return decorations;
    }

    public BodyModel getBodyModel() {
        return bodyModel;
    }



    public AtomicInteger getDecorationCounter() {
        return decorationCounter;
    }
    
    @Override
    public void updateOutlinePoints() {
        super.updateOutlinePoints();
        bodyModel.onChildLayerOutlineUpdated(layerId);
    }

    public void setField(LayerField field) {
        this.field = field;
    }

    public LayerField getField() {
        return field;
    }

}
