package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Image;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ClickableBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds.RectangularBounds;

import org.andengine.opengl.texture.region.ITextureRegion;

public class ClickableImage<C extends Controller, B extends ClickableBehavior<C>>
        extends ClickableContainer<C, B> {

    protected final Image image;

    public ClickableImage(ITextureRegion textureRegion, B behavior, boolean createBounds) {
        this(textureRegion, createBounds);
        setBehavior(behavior);
    }

    public ClickableImage(ITextureRegion textureRegion, boolean createBounds) {
        this(0, 0, textureRegion, createBounds);
    }

    public ClickableImage(float pX, float pY, ITextureRegion textureRegion, boolean createBounds) {
        super(pX, pY);
        image = new Image(0, 0, textureRegion);
        addElement(image);
        setWidth(image.getWidth());
        setHeight(image.getHeight());
        if (createBounds) {
            setBounds(new RectangularBounds(this, getWidth(), getHeight()));
        }
    }

    @Override
    public boolean isVisible() {
        return image.isVisible();
    }
}
