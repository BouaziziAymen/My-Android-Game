package com.evolgames.userinterface.view.inputs;


import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.view.Temporal;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.layouts.LinearLayout;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

public class Quantity<C extends AdvancedWindowController<?>> extends InputField<C,QuantityBehavior<C>> implements Temporal {
    private final int mColorIndex;
    private int mNumberOfStrokes;
    private final LinearLayout mFront;
    public Quantity(float pX, float pY,int mLength, int colorIndex){
       this(pX,pY,mLength,colorIndex,0);
    }

    public Quantity(float pX, float pY,int mLength, int colorIndex,float ratio){
        super(pX,pY,mLength);
        this.mColorIndex = colorIndex;
        mFront = new LinearLayout(13-8,(24-14)/2,LinearLayout.Direction.Horizontal,-1);
        addElement(mFront);
        updateRatio(ratio);

    }

    public Quantity(int mLength, int colorIndex){
       this(0,0,mLength,colorIndex);
    }



    public void updateRatio(float ratio) {
        int newNumberOfStrokes = (int) Math.ceil(ratio*mLength);
        if(newNumberOfStrokes!=mNumberOfStrokes) {
            mNumberOfStrokes = newNumberOfStrokes;
            createFront();
            if(getBehavior()!=null)
            getBehavior().onViewUpdated();
        }
    }
    public float getRatio(){
        return mNumberOfStrokes/(float)mLength;
    }


    private void createFront(){
        mFront.updateLayout();
        if(mNumberOfStrokes==0)return;
        ArrayList<ITextureRegion> frontRegions = ResourceManager.getInstance().quantity.get(mColorIndex);
        mFront.addToLayout(new Image(frontRegions.get(0)));
        for(int i=1;i<mNumberOfStrokes-1;i++){
            mFront.addToLayout(new Image(frontRegions.get(1)));
        }
        mFront.addToLayout(new Image(frontRegions.get(2)));
    }


    @Override
    public void onStep() {
        if(getBehavior()!=null){
            getBehavior().onStep();
        }
    }
}
