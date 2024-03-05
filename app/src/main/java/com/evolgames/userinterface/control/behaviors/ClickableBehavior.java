package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.Controller;

import org.andengine.input.touch.TouchEvent;

public abstract class ClickableBehavior<C extends Controller> extends Behavior<C> {

    public ClickableBehavior(C controller) {
        super(controller);
    }

    public abstract boolean processTouch(TouchEvent touchEvent);
}
