package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.Controller;

public abstract class Behavior<C extends Controller> {
    private C controller;

    Behavior(C controller) {
        this.controller = controller;
    }

    protected C getController() {
        return controller;
    }

    public void setController(C controller) {
        this.controller = controller;
    }

}
