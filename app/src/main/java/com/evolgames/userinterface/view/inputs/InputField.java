package com.evolgames.userinterface.view.inputs;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ClickableBehavior;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.userinterface.view.layouts.LinearLayout;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

public class InputField<C extends Controller,B extends ClickableBehavior<C>> extends  ClickableContainer<C,B> {


    int mLength;
    public InputField(float pX, float pY,int mLength){
        super(pX,pY);
        this.mLength = mLength;
        LinearLayout layout = new LinearLayout(LinearLayout.Direction.Horizontal,-1);
        addElement(layout);

        ArrayList<ITextureRegion> baseRegions = ResourceManager.getInstance().quantity.get(0);
        Image image = new Image(0,0,baseRegions.get(0));
        layout.addToLayout(image);
        for(int i = 0; i< mLength -2; i++){
            image = new Image(0,0,baseRegions.get(1));
            layout.addToLayout(image);
        }
        image = new Image(0,0,baseRegions.get(2));
        layout.addToLayout(image);
        setWidth(layout.getWidth());
        setHeight(layout.getHeight());
        RectangularBounds bounds = new RectangularBounds(this,getWidth(),getHeight());
        setBounds(bounds);
    }
}
