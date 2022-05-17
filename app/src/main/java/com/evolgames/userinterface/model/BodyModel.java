package com.evolgames.userinterface.model;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.shapes.BodyOutlineShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BodyModel extends ProperModel {
    private final AtomicInteger layerCounter = new AtomicInteger();
    private final int bodyId;
    private final ArrayList<LayerPointsModel> layers;
    private GameEntity gameEntity;
    private final ArrayList<ProjectileModel> projectiles;
    private final ArrayList<HandModel> hands;
    public static final ArrayList<BodyCategory> allCategories = new ArrayList<>(Arrays.asList(BodyCategory.ARMOR,BodyCategory.MELEE,BodyCategory.PROJECTILE));
    private BodyCategory category;
    private BodyOutlineShape bodyOutlineShape;

    public void setCategory(BodyCategory category) {
        this.category = category;
    }

    public void setBodyOutlineShape(BodyOutlineShape bodyOutlineShape) {
        this.bodyOutlineShape = bodyOutlineShape;
    }

    public void onChildLayerShapeUpdated(int layerId) {
        bodyOutlineShape.onModelUpdated();
    }

    public enum BodyCategory{
        PROJECTILE,ARMOR, MELEE;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }


    public BodyModel(int bodyId){
        super("Body"+bodyId);
        this.bodyId = bodyId;
        layers = new ArrayList<>();
        projectiles = new ArrayList<>();
        hands = new ArrayList<>();
        properties = new Properties();
        properties.setProperties(new float[]{});
    }

    void swapLayers(int index1,int index2){
       Collections.swap(layers,index1,index2);
    }

    LayerPointsModel createLayer(){
        LayerPointsModel layerModel = new LayerPointsModel(bodyId,layerCounter.getAndIncrement(),this);
        layers.add(layerModel);
        System.out.println("Creating layer:"+layerCounter.get());
        return layerModel;
    }

    LayerPointsModel getLayerModelById(int layerId){
        for(LayerPointsModel layer:layers){
            if(layer.getLayerId()==layerId)return layer;
        }
        return null;
    }
    public int getBodyId() {
        return bodyId;
    }

    public ArrayList<LayerPointsModel> getLayers() {
        return layers;
    }


     DecorationPointsModel createNewDecroation(int layerId){
       return Objects.requireNonNull(getLayerModelById(layerId)).createNewDecroation();
    }


    public String toString(){
        String s = "Body"+ bodyId +": \n";
        for(int i=0;i<layers.size();i++){
            s+=layers.get(i).toString()+"\n";
        }
        return s;
    }

     void removeLayer(int layerId) {
        layers.remove(getLayerModelById(layerId));
    }

    public DecorationPointsModel removeDecoration(int layerId, int decorationId) {
        return getLayerModelById(layerId).removeDecoration(decorationId);
    }


    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }


    public void initLayerCounter(int layerMaxId) {
        layerCounter.set(layerMaxId);
    }

    public int getLayersCounterValue() {
        return layerCounter.get();
    }

    public ArrayList<ProjectileModel> getProjectiles() {
        return projectiles;
    }

    public ArrayList<HandModel> getHands() {
        return hands;
    }

    public List<List<Vector2>> getPolygons(){
        List<List<Vector2>> polygons = new ArrayList<>();
         layers.forEach(l->{if(l.getOutlinePoints()!=null)polygons.add(Arrays.asList(l.getOutlinePoints()));});
         return polygons;
    }

    public void select(){
       select(Colors.palette1_gold);
    }
    public void select(Color color){
        if(bodyOutlineShape!=null) {
            bodyOutlineShape.select(color);
        }
    }
    public void deselect(){
      Log.e("selection","deselect :"+bodyId);
        if(bodyOutlineShape!=null) {
            bodyOutlineShape.deselect();
            Log.e("selection","shape");
        }
    }
    public boolean hasSelectedLayer(){
        for(LayerPointsModel layerPointsModel: layers){
            if(layerPointsModel.getPointsShape().isSelected()){
                return true;
            }
        }
        return false;
    }

    public BodyOutlineShape getBodyOutlineShape() {
        return bodyOutlineShape;
    }
}
