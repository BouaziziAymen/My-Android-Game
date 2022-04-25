package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.factories.MaterialFactory;
import com.evolgames.factories.PropertiesFactory;
import com.evolgames.userinterface.view.shapes.PointsShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class LayerPointsModel extends PointsModel {
    private static final float MINIMAL_DISTANCE_BETWEEN_VERTICES = 1f;
    private final BodyModel bodyModel;
    private AtomicInteger decorationCounter = new AtomicInteger();
    private ArrayList<DecorationPointsModel> decorations;
    private int layerId;
    private int bodyId;


    public LayerPointsModel(int bodyId, int layerId, String layerName, BlockAProperties properties, BodyModel bodyModel) {
        super(layerName);
        this.layerId = layerId;
        this.bodyId = bodyId;
        decorations = new ArrayList<>();
        this.properties = properties;
        this.bodyModel = bodyModel;
    }

    LayerPointsModel(int bodyId, int layerId, BodyModel bodyModel) {
        super("Layer" + layerId);
        this.layerId = layerId;
        this.bodyId = bodyId;
        decorations = new ArrayList<>();
        properties = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0), new Filter());
        ((BlockAProperties) properties).setLayerName("Layer" + layerId);
        this.bodyModel = bodyModel;
    }

    @Override
    public String toString() {
        return "Layer:" + bodyId + "/" + layerId + "/" + Arrays.toString(getPoints().toArray());
    }

    public int getBodyId() {
        return bodyId;
    }

    @Override
    public void onShapeUpdated() {
        super.onShapeUpdated();
        bodyModel.onChildLayerShapeUpdated(layerId);
    }

    public int getLayerId() {
        return layerId;
    }

    private DecorationPointsModel getDecorationModelById(int decorationId) {
        for (DecorationPointsModel decoration : decorations) {
            if (decoration.getDecorationId() == decorationId) return decoration;

        }
        return null;
    }

    DecorationPointsModel createNewDecroation() {
        DecorationPointsModel decorationModel = new DecorationPointsModel(this, decorationCounter.getAndIncrement());
        decorations.add(decorationModel);
        return decorationModel;
    }


    DecorationPointsModel removeDecoration(int decorationId) {
        DecorationPointsModel decorationModel = getDecorationModelById(decorationId);
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
        for (DecorationPointsModel decorationModel : decorations) {
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
        for (Vector2 p : getPoints())
            if (p.dst(x, y) < MINIMAL_DISTANCE_BETWEEN_VERTICES) return false;
        return true;
    }


    @Override
    public boolean test(ArrayList<Vector2> newPoints) {
        Vector2[] originalPoints = getOutlinePoints();
        setOutlinePoints(newPoints.toArray(new Vector2[0]));
        for (DecorationPointsModel decorationModel : decorations) {
            if (!decorationModel.test(decorationModel.getPoints())) {
                setOutlinePoints(originalPoints);
                return false;
            }
        }

        return true;
    }

    DecorationPointsModel getDecorationById(int tertiaryKey) {
        for (DecorationPointsModel model : decorations)
            if (model.getDecorationId() == tertiaryKey) return model;
        return null;
    }

    public int getNewDecorationId() {
        return decorationCounter.get();
    }

    public ArrayList<PointsShape> getPointsShapes() {
        ArrayList<PointsShape> result = new ArrayList<>();
        for (DecorationPointsModel model : decorations) result.add(model.getPointsShape());
        result.add(getPointsShape());
        return result;
    }

    public ArrayList<DecorationPointsModel> getDecorations() {
        return decorations;
    }


    public void setDecorationCounter(int value) {
        decorationCounter.set(value);
    }

}
