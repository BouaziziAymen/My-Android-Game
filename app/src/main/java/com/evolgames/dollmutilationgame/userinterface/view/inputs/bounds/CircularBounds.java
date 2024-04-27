package com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

import org.andengine.util.math.MathUtils;

public class CircularBounds extends Bounds {
    private final float ray;

    public CircularBounds(Element e, float pRay) {
        super(e);
        this.ray = pRay;
    }

    @Override
    public boolean isInBounds(float pX, float pY) {

        float x = element.getAbsoluteX() + element.getWidth() / 2;
        float y = element.getAbsoluteY() + element.getHeight() / 2;

        float distance = MathUtils.distance(x, y, pX, pY);
        return distance < ray;
    }
}
