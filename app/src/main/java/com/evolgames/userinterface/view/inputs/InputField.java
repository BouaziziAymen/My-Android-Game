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

    protected int inputLength;
    private final LinearLayout layout;
    private final boolean leftSlot;
    private final boolean rightSlot;
    public InputField(float pX, float pY,int mLength){
      this(pX,pY,mLength,false,false);
    }
    public InputField(float pX, float pY,int mLength, boolean leftSlot, boolean rightSlot){
        super(pX,pY);
        this.inputLength = mLength;
        this.layout = new LinearLayout(LinearLayout.Direction.Horizontal,-1);
        this.rightSlot = rightSlot;
        this.leftSlot = leftSlot;
        addElement(layout);
        createBase(leftSlot,rightSlot);
    }

    public boolean isLeftSlot() {
        return leftSlot;
    }

    public boolean isRightSlot() {
        return rightSlot;
    }

    private void createBase(boolean left, boolean right) {
        ArrayList<ITextureRegion> baseRegions = ResourceManager.getInstance().quantity.get("a");
        ArrayList<ITextureRegion> altBaseRegions = ResourceManager.getInstance().quantity.get("c");
        Image image = new Image(0,0,left?altBaseRegions.get(0):baseRegions.get(0));
        layout.addToLayout(image);
        for(int i = 0; i< inputLength -2; i++){
            image = new Image(0,0,baseRegions.get(1));
            layout.addToLayout(image);
        }
        image = new Image(0,0,right?altBaseRegions.get(2):baseRegions.get(2));
        layout.addToLayout(image);
        setWidth(layout.getWidth());
        setHeight(layout.getHeight());
        RectangularBounds bounds = new RectangularBounds(this,getWidth()-(right?27:0)-(left?27:0),getHeight(),left?27:0,0);
        setBounds(bounds);
    }
}
