package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.AmmoOptionController;
import com.evolgames.userinterface.model.toolmodels.AmmoModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyModel extends OutlineModel<BodyProperties> {
    public static final ArrayList<BodyCategory> allCategories = new ArrayList<>(Arrays.asList(BodyCategory.ARMOR, BodyCategory.MELEE, BodyCategory.PROJECTILE));
    private final AtomicInteger layerCounter = new AtomicInteger();
    private final AtomicInteger projectileCounter = new AtomicInteger();
    private final AtomicInteger ammoCounter = new AtomicInteger();
    private final int bodyId;
    private final ArrayList<LayerModel> layers;
    private final ArrayList<ProjectileModel> projectiles;
    private final ArrayList<AmmoModel> ammoModels;
    private final ArrayList<HandModel> hands;
    private GameEntity gameEntity;
    private BodyCategory category;

    public BodyModel(int bodyId) {
        super("Body" + bodyId);
        this.bodyId = bodyId;
        layers = new ArrayList<>();
        projectiles = new ArrayList<>();
        ammoModels = new ArrayList<>();
        hands = new ArrayList<>();
        properties = new BodyProperties();
    }

    public void setCategory(BodyCategory category) {
        this.category = category;
    }

    public void onChildLayerOutlineUpdated(int layerId) {
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
    public AmmoModel getAmmoModelById(int ammoId) {
        for (AmmoModel ammoModel : ammoModels) {
            if (ammoModel.getAmmoId() == ammoId) return ammoModel;
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

    public AtomicInteger getProjectileCounter() {
        return projectileCounter;
    }

    public AtomicInteger getAmmoCounter() {
        return ammoCounter;
    }

    public Vector2 getCenter() {
        List<List<Vector2>> list = new ArrayList<>();
        for (LayerModel layerModel : layers) {
            list.add(layerModel.getPoints());
        }
        return GeometryUtils.calculateCenter(list);
    }

    @Override
    public void updateOutlinePoints() {
        throw new UnsupportedOperationException();
    }

    public List<AmmoModel> getAmmoModels() {
        return ammoModels;
    }


    public enum BodyCategory {
        PROJECTILE, ARMOR, MELEE;
    }
}
