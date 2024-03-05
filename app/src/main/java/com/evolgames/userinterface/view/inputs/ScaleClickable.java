package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.ScaleClickableBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.view.inputs.bounds.Bounds;
import com.evolgames.userinterface.view.inputs.bounds.RectangularBounds;

import org.andengine.opengl.texture.region.ITextureRegion;

public class ScaleClickable<C extends Controller> extends ClickableImage<C, ScaleClickableBehavior<C>> {
    private final ITextureRegion region;

    public ScaleClickable(float x, float y, ITextureRegion textureRegion, C controller, RectangularBounds bounds, Action action) {
        super(x,y,textureRegion, false);
        setBehavior(new ScaleClickableBehavior<>(controller, this,action));
        setBounds(bounds);
        this.region = textureRegion;
    }
    public void setImageScale(float pScaleX, float pScaleY) {
        image.setScale(pScaleX,pScaleY);
        float dx = (pScaleX-1)*region.getWidth();
        float dy =  (pScaleY-1)*region.getHeight();
        image.setLowerBottomX(-dx/2);
        image.setLowerBottomY(-dy/2);
    }

}