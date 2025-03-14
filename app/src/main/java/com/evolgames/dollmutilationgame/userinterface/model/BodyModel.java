package com.evolgames.dollmutilationgame.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.properties.BodyProperties;
import com.evolgames.dollmutilationgame.entities.properties.BodyUsageCategory;
import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.scenes.Init;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.BombModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.CasingModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.DragModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.SpecialPointModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.layerwindow.BodyField;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyModel extends OutlineModel<BodyProperties> {
    public static final ArrayList<BodyUsageCategory> allCategories =
            new ArrayList<>(EnumSet.allOf(BodyUsageCategory.class));

    static {
        allCategories.remove(BodyUsageCategory.MISSILE);
    }
    private final AtomicInteger layerCounter = new AtomicInteger();
    private final int bodyId;
    private final ArrayList<LayerModel> layers;
    private final ArrayList<ProjectileModel> projectileModels;
    private final ArrayList<CasingModel> casingModels;
    private final ArrayList<DragModel> dragModels;
    private final ArrayList<FireSourceModel> fireSourceModels;
    private final ArrayList<LiquidSourceModel> liquidSourceModels;
    private final List<UsageModel<?>> usageModels;
    private final List<BombModel> bombModels;
    private final List<SpecialPointModel> specialPointModels;
    private GameEntity gameEntity;
    private BodyField field;
    private Init init;

    private Vector2 center;

    public BodyModel(int bodyId) {
        super("Body" + bodyId);
        this.bodyId = bodyId;
        layers = new ArrayList<>();
        projectileModels = new ArrayList<>();
        casingModels = new ArrayList<>();
        properties = new BodyProperties();
        usageModels = new LinkedList<>();
        bombModels = new ArrayList<>();
        dragModels = new ArrayList<>();
        fireSourceModels = new ArrayList<>();
        liquidSourceModels = new ArrayList<>();
        specialPointModels = new ArrayList<>();
    }

    public Init getInit() {
        return init;
    }

    public void setInit(Init init) {
        this.init = init;
    }

    @SuppressWarnings("unchecked")
    public <T extends Properties> T getUsageModelProperties(BodyUsageCategory bodyUsageCategory) {
        return ((UsageModel<T>)
                usageModels.stream().filter(e -> e.getType() == bodyUsageCategory).findFirst().get())
                .getProperties();
    }

    @SuppressWarnings("unchecked")
    public <T extends Properties> UsageModel<T> getUsageModel(BodyUsageCategory bodyUsageCategory) {
        return ((UsageModel<T>)
                usageModels.stream().filter(e -> e.getType() == bodyUsageCategory).findFirst().get());
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

    public CasingModel getCasingModelById(int ammoId) {
        for (CasingModel ammoModel : casingModels) {
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

    void removeLayer(int layerId) {
        layers.remove(getLayerModelById(layerId));
    }

    public DecorationModel removeDecoration(int layerId, int decorationId) {
        return getLayerModelById(layerId).removeDecoration(decorationId);
    }

    public AtomicInteger getLayerCounter() {
        return layerCounter;
    }

    @Override
    public void updateOutlinePoints() {
        throw new UnsupportedOperationException();
    }

    public List<CasingModel> getCasingModels() {
        return casingModels;
    }

    public List<UsageModel<? extends Properties>> getUsageModels() {
        return usageModels;
    }

    public ArrayList<ProjectileModel> getProjectileModels() {
        return projectileModels;
    }

    public List<BombModel> getBombModels() {
        return bombModels;
    }

    public ArrayList<DragModel> getDragModels() {
        return dragModels;
    }

    public BombModel getBombModelById(int bombId) {
        return bombModels.stream()
                .filter(bombModel -> bombModel.getBombId() == bombId)
                .findFirst()
                .orElse(null);
    }

    public BodyField getField() {
        return field;
    }

    public void setField(BodyField bodyField) {
        this.field = bodyField;
    }

    public ArrayList<FireSourceModel> getFireSourceModels() {
        return fireSourceModels;
    }

    public ArrayList<LiquidSourceModel> getLiquidSourceModels() {
        return liquidSourceModels;
    }


    public List<SpecialPointModel> getSpecialPointModels() {
        return specialPointModels;
    }

    public Vector2 calculateBodyCenter() {
        if(this.center==null) {
            List<List<Vector2>> list = new ArrayList<>();
            for (LayerModel layerModel : this.getLayers()) {
                list.add(layerModel.getPoints());
            }
            this.center = GeometryUtils.calculateCenter(list);
        }
        return this.center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public float[] getBounds(float x, float y, float pX, float pY,boolean mirrored) {
        float maxX = 0f;
        float minX = 0f;
        float maxY = 0f;
        float minY = 0f;
            for(LayerModel layerModel:getLayers()){
                for(Vector2 v:layerModel.getPoints()){
                    float dx = v.x - x;
                    float dy = v.y - y;
                    if(dx<minX){minX = dx;}
                    if(dx>maxX){maxX = dx;}
                    if(dy<minY){minY = dy;}
                    if(dy>maxY){maxY = dy;}
                }
            }
        return new float[]{!mirrored?minX+pX:maxX+pX,!mirrored?maxX+pX:minX+pX, pY+minY,pY+maxY};
    }
}
