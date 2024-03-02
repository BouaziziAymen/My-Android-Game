package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ClickableBehavior;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;

import org.andengine.input.touch.TouchEvent;

public class ClickableElement<C extends Controller, B extends ClickableBehavior<C>>
        extends Element implements Touchable {

    protected B behavior;
    private boolean enabled = true;

    public ClickableElement(float pX, float pY) {
        super(pX, pY);
    }

    @Override
    public void drawSelf() {

    }

    public B getBehavior() {
        return behavior;
    }

    public void setBehavior(B behavior) {
        this.behavior = behavior;
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        if (!isEnabled() || behavior == null) {
            return false;
        }
        return behavior.processTouch(pTouchEvent);
    }

    protected boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
