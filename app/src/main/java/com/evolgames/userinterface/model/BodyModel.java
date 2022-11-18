package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.shapes.BodyShape;
import com.evolgames.userinterface.view.shapes.PointsShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BodyModel extends OutlineModel<BodyProperties> {
    public static final ArrayList<BodyCategory> allCategories = new ArrayList<>(Arrays.asList(BodyCategory.ARMOR, BodyCategory.MELEE, BodyCategory.PROJECTILE));
    private final AtomicInteger layerCounter = new AtomicInteger();
    private final AtomicInteger projectileCounter = new AtomicInteger();
    private final int bodyId;
    private final ArrayList<LayerModel> layers;
    private final ArrayList<ProjectileModel> projectiles;
    private final ArrayList<HandModel> hands;
    private GameEntity gameEntity;
    private BodyCategory category;

    public BodyModel(int bodyId) {
        super("Body" + bodyId);
        this.bodyId = bodyId;
        layers = new ArrayList<>();
        projectiles = new ArrayList<>();
        hands = new ArrayList<>();
        properties = new BodyProperties();
    }

    public void setCategory(BodyCategory category) {
        this.category = category;
    }

    public void onChildLayerOutlineUpdated(int layerId) {
       updateOutlinePoints();
       if(outlineShape!=null) {
           outlineShape.onModelUpdated();
       }
    }
    public void setBodyShape(BodyShape bodyShape) {
        this.outlineShape = bodyShape;
        bodyShape.setOutlineModel(this);
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }

    void swapLayers(int index1, int index2) {
        Collections.swap(layers, index1, index2);
    }

    LayerModel createLayer() {
        LayerModel layerModel = new LayerModel(bodyId, layerCounter.getAndIncrement(), this);
        layers.add(layerModel);
        System.out.println("Creating layer:" + layerCounter.get());
        return layerModel;
    }

    LayerModel getLayerModelById(int layerId) {
        for (LayerModel layer : layers) {
            if (layer.getLayerId() == layerId) return layer;
        }
        return null;
    }

    public int getBodyId() {
        return bodyId;
    }


    public ArrayList<LayerModel> getLayers() {
        return layers;
    }


    DecorationModel createNewDecroation(int layerId) {
        return Objects.requireNonNull(getLayerModelById(layerId)).createDecoration();
    }


    public String toString() {
        String s = "Body" + bodyId + ": \n";
        for (int i = 0; i < layers.size(); i++) {
            s += layers.get(i).toString() + "\n";
        }
        return s;
    }

    void removeLayer(int layerId) {
        layers.remove(getLayerModelById(layerId));
    }

    public DecorationModel removeDecoration(int layerId, int decorationId) {
        return getLayerModelById(layerId).removeDecoration(decorationId);
    }

    public AtomicInteger getLayerCounter() {
        return layerCounter;
    }

    public ArrayList<ProjectileModel> getProjectiles() {
        return projectiles;
    }

    public ArrayList<HandModel> getHands() {
        return hands;
    }

    public List<List<Vector2>> getPolygons() {
        List<List<Vector2>> polygons = new ArrayList<>();
        layers.forEach(layer -> {
            if (layer.getOutlinePoints() != null) {
                polygons.add(Arrays.asList(layer.getOutlinePoints()));
            }
        });
        return polygons;
    }


    public void deselect() {
        if (getSelectedLayer() != null) {
            getSelectedLayer().deselect();
        }
    }

    public LayerModel getSelectedLayer() {
        for (LayerModel layerModel : layers) {
            if (layerModel.isSelected()) {
                return layerModel;
            }
        }
        return null;
    }

    public boolean hasSelectedLayer() {
        for (LayerModel layerModel : layers) {
            if (layerModel.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public AtomicInteger getProjectileCounter() {
        return projectileCounter;
    }

    public Vector2 getCenter() {
        List<List<Vector2>> list = new ArrayList<>();
        for (LayerModel layerModel : layers) {
            list.add(layerModel.getModelPoints());
        }
        return GeometryUtils.calculateCenter(list);
    }

    public void select() {
        if (hasSelectedLayer()) {
            getSelectedLayer().select();
        }
    }

    @Override
    public void updateOutlinePoints() {

        Object[] list = layers.stream().map(PointsModel::getOutlinePoints).filter(Objects::nonNull).collect(Collectors.toList()).stream().flatMap(Arrays::stream).toArray();
        Vector2[] vector2s = Arrays.copyOf(list, list.length, Vector2[].class);
        if(vector2s.length<3){
            setOutlinePoints(new Vector2[0]);
        } else {
            Vector2[] pointsArray = GeometryUtils.hullFinder.findConvexHull(vector2s);
            setOutlinePoints(pointsArray);
        }
    }

    public enum BodyCategory {
        PROJECTILE, ARMOR, MELEE;
    }
}
