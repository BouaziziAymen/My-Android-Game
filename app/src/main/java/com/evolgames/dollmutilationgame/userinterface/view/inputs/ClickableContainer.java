package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Container;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ClickableBehavior;

import org.andengine.input.touch.TouchEvent;

public class ClickableContainer<C extends Controller, B extends ClickableBehavior<C>>
        extends Container implements Touchable {

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
