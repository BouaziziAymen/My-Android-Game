package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;

import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;

public class CreationZone {

    private CreationZoneController creationZoneController;
    private boolean touchLocked;

    public boolean isTouchLocked() {
        return touchLocked;
    }

    public CreationZone(CreationZoneController controller){

        this.creationZoneController = controller;
    }
    public Vector2 applyMagnet(float x, float y){

        for(ReferencePointImage referencePointImage:referencePointImageArrayList){
            Vector2 v = referencePointImage.getPoint();
            if(v.dst(x,y)<32)return v;
        }
        return new Vector2(x,y);
    }
    public ArrayList<ReferencePointImage> referencePointImageArrayList = new ArrayList<>();

    public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {
        if(scroll)return;
        Vector2 touch =(creationZoneController.isMagnet())? applyMagnet(pTouchEvent.getX(),pTouchEvent.getY()):new Vector2(pTouchEvent.getX(),pTouchEvent.getY());

        if(pTouchEvent.isActionUp()){
           creationZoneController.onZoneActionUp(touch.x,touch.y);
        }

        if(pTouchEvent.isActionDown()){
            creationZoneController.onZoneActionDown(touch.x,touch.y);
        }

        if(pTouchEvent.isActionMove()){
            creationZoneController.onZoneActionMove(touch.x,touch.y);
        }
    }

    public void setTouchLocked(boolean b) {
        this.touchLocked = b;
    }
}
