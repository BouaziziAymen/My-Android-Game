package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyModel extends OutlineModel<BodyProperties> {
    public static final ArrayList<BodyUsageCategory> allCategories = new ArrayList<>(EnumSet.allOf(BodyUsageCategory.class));
    private final AtomicInteger layerCounter = new AtomicInteger();
    private final int bodyId;
    private final ArrayList<LayerModel> layers;
    private final ArrayList<ProjectileModel> projectiles;
    private final ArrayList<CasingModel> ammoModels;
    private GameEntity gameEntity;
    private final List<UsageModel<?>> usageModels;
    @SuppressWarnings("unchecked")
    public <T extends Properties> T getUsageModelProperties(BodyUsageCategory bodyUsageCategory){
        return ((UsageModel<T>)usageModels.stream().filter(e->e.getType()==bodyUsageCategory).findFirst().get()).getProperties();
    }

    @SuppressWarnings("unchecked")
    public <T extends Properties> UsageModel<T> getUsageModel(BodyUsageCategory bodyUsageCategory){
        return ((UsageModel<T>)usageModels.stream().filter(e->e.getType()==bodyUsageCategory).findFirst().get());
    }
    public BodyModel(int bodyId) {
        super("Body" + bodyId);
        this.bodyId = bodyId;
        layers = new ArrayList<>();
        projectiles = new ArrayList<>();
        ammoModels = new ArrayList<>();
        properties = new BodyProperties();
        usageModels = new ArrayList<>();
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
        return layerModel;
    }

    LayerModel getLayerModelById(int layerId) {
        for (LayerModel layer : layers) {
            if (layer.getLayerId() == layerId) return layer;
        }
        return null;
    }
    public CasingModel getAmmoModelById(int ammoId) {
        for (CasingModel ammoModel : ammoModels) {
            if (ammoModel.getCasingId() == ammoId) return ammoModel;
        }
        return null;
    }

    public int getBodyId() {
        return bodyId;
    }


    public ArrayList<LayerModel> getLayers() {
        return layers;
    }


    DecorationModel createNewDecoration(int layerId) {
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

    public List<CasingModel> getAmmoModels() {
        return ammoModels;
    }

    public List<UsageModel<?>> getUsageModels() {
        return usageModels;
    }

    public ArrayList<ProjectileModel> getProjectiles() {
        return projectiles;
    }
}
