package com.evolgames.userinterface.view.basics;

import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Touchable;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Image extends Element implements Limited,Touchable {
    protected ITextureRegion textureRegion;
    private boolean isLimited;
    private float limitY0;
    private float limitY1;
    private float limitX0;
    private float limitX1;

    public Image(ITextureRegion pTextureRegion) {
        this(0,0,pTextureRegion);
    }
    public Image(float pX, float pY, ITextureRegion pTextureRegion) {
        super(pX,pY,pTextureRegion.getWidth(),pTextureRegion.getHeight());
        textureRegion = pTextureRegion;
        this.limitY0 = 0;
        this.limitY1 = pTextureRegion.getHeight();
        this.limitX0 = 0;
        this.limitX1 = pTextureRegion.getWidth();

    }

    @Override
    public void setShaded(boolean shaded) {
        if(shaded!= isShaded())setUpdated(true);
        super.setShaded(shaded);

    }

    float red() {
    if (isShaded()) return 0.3f;
    else return mRed;
}
    float green() {
        if (isShaded()) return 0.3f;
        else return mGreen;
    }
    float blue() {
        if (isShaded()) return 0.3f;
        else return mBlue;
    }

    public void drawImage() {
        if (isLimited) {
            if(limitY0<limitY1) {
                UserInterface.hudBatcher.drawLimited(textureRegion, getAbsoluteX(), getAbsoluteY(), red(), green(), blue(), 1f, limitX0, limitX1, limitY0, limitY1);
            }
        }
        else {
            if (!isScaled()) {
                UserInterface.hudBatcher.draw(textureRegion, getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight(), red(), green(), blue(), 1f);
            }
            else {
                UserInterface.hudBatcher.draw(textureRegion, getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight(), scaleX, scaleY, red(), getBlue(), blue(), 1f);

            }
            }
    }

    @Override
    public void drawSelf() {
drawImage();
    }

    @Override
    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    @Override
    public void setLimitY0(float limitY0) {
        if(limitY0>getHeight())limitY0=getHeight();
        if(limitY0<0)limitY0=0;
        this.limitY0 = limitY0;
    }

    @Override
    public void setLimitY1(float limitY1) {
        if(limitY1>getHeight())limitY1=getHeight();
        if(limitY1<0)limitY1=0;
        this.limitY1 = limitY1;
    }

    public void setLimitX0(float limitX0) {
        if(limitX0>getWidth())limitX0=getWidth();
        if(limitX0<0)limitX0=0;
        this.limitX0 = limitX0;
        setUpdated(true);
    }


    public void setLimitX1(float limitX1) {
        if(limitX1>getWidth())limitX1=getWidth();
        if(limitX1<0)limitX1=0;
        this.limitX1 = limitX1;
        setUpdated(true);
    }
    public void setTextureRegion(ITextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
        return isInBounds(pTouchEvent.getX(),pTouchEvent.getY());
    }
}
