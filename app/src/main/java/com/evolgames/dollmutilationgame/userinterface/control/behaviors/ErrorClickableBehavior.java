package com.evolgames.dollmutilationgame.userinterface.control.behaviors;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;

public abstract class ErrorClickableBehavior<C extends Controller> extends AdvancedClickableBehavior<C> {
    private final Element errorProducer;
    protected boolean showError = false;
    private int errorTimer = 0;

    public ErrorClickableBehavior(C controller, Element errorProducer) {
        super(controller);
        this.errorProducer = errorProducer;
    }

    public void onStep() {
        if (errorProducer.getErrorDisplay() != null) {
            if (showError) {
                errorTimer++;
            }
            if (errorTimer > 120 && showError) {
                showError = false;
                errorTimer = 0;
                errorProducer.getErrorDisplay().hideError();
                updateWindowLayout();
            }
        }
    }
}
