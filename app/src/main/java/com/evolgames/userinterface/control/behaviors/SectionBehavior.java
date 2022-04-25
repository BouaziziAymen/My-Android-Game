package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Element;

import org.andengine.input.touch.TouchEvent;

public class SectionBehavior<C extends Controller> extends ErrorClickableBehavior<C> {
    public SectionBehavior(C controller, Element errorProducer) {
        super(controller, errorProducer);
    }

    @Override
    public boolean processTouch(TouchEvent touchEvent, boolean touched) {
        return false;
    }
}
