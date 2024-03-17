package com.evolgames.userinterface.view.inputs.bounds;

import com.evolgames.userinterface.view.basics.Element;

public class RectangularBounds extends Bounds {
    private float width;
    private float height;
    private float shiftX;
    private float shiftY;

    public RectangularBounds(
            Element element, float pWidth, float pHeight, float shiftX, float shiftY) {
        super(element);
        this.width = pWidth;
        this.height = pHeight;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public RectangularBounds(Element element, float pWidth, float pHeight) {
        super(element);
        this.width = pWidth;
        this.height = pHeight;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public boolean isInBounds(float pX, float pY) {
        float s1x = element.getAbsoluteX() + shiftX;
        float s1y = element.getAbsoluteY() + shiftY;
        float s2x = s1x + width;
        float s2y = s1y + height;

        return pX > s1x && pX < s2x && pY > s1y && pY < s2y;
    }

    public void setShiftX(float shiftX) {
        this.shiftX = shiftX;
    }

    public void setShiftY(float shiftY) {
        this.shiftY = shiftY;
    }
}
