package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.behaviors.ClickableBehavior;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Container;

import org.andengine.input.touch.TouchEvent;


public class ClickableContainer<C extends Controller, B extends ClickableBehavior<C>> extends Container implements Touchable {

    protected B behavior;
    private boolean enabled = true;


    public ClickableContainer(float pX, float pY) {
        super(pX, pY);

    }


    public B getBehavior() {
        return behavior;
    }

    public void setBehavior(B behavior) {
        this.behavior = behavior;
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent, boolean touched) {
        if (!isEnabled()||behavior==null) return false;
        return behavior.processTouch(pTouchEvent,touched);
    }

    protected boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



}
