package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.helpers.Utils;

public class DecorationPointsModel extends PointsModel {
    private static final float MINIMAL_DISTANCE_BETWEEN_VERTICES = 4f;
    private int id;
    private LayerPointsModel layerModel;
    public DecorationPointsModel(LayerPointsModel layerModel, int decorationId){
        super("Decoration"+decorationId);
        this.id = decorationId;
        this.layerModel = layerModel;
        properties = new DecorationProperties(new Color(Color.WHITE));
    }
    public int getDecorationId(){
        return id;
    }
    public String toString(){
        String s = "Decoration"+id+": \n";

        return s;
    }

    @Override
    public boolean test(Vector2 movedPoint, float dx, float dy) {
        boolean inside = Utils.PointInPolygon(movedPoint.x+dx,movedPoint.y+dy,layerModel.getOutlinePoints());
        if(!inside)return false;
        for(Vector2 p:getPoints())if(p!=movedPoint)if(p.dst(movedPoint.x+dx,movedPoint.y+dy)<MINIMAL_DISTANCE_BETWEEN_VERTICES)return false;
        return true;

    }

    @Override
    public boolean test(ArrayList<Vector2> points) {
        for(Vector2 p:points) {
         if(!Utils.PointInPolygon(p.x, p.y, layerModel.getOutlinePoints()))return false;
        }

        return true;
    }

    @Override
    public boolean test(float x, float y) {

        boolean inside = Utils.PointInPolygon(x,y,layerModel.getOutlinePoints());
        if(!inside)return false;
        for(Vector2 p:getPoints())if(p.dst(x,y)<MINIMAL_DISTANCE_BETWEEN_VERTICES)return false;
        return true;
    }
}
