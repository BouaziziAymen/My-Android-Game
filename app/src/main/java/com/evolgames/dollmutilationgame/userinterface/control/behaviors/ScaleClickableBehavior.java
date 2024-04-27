package com.evolgames.dollmutilationgame.userinterface.control.behaviors;

import com.evolgames.dollmutilationgame.userinterface.control.behaviors.actions.Action;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ScaleClickable;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;

import org.andengine.input.touch.TouchEvent;

public class ScaleClickableBehavior<C extends Controller> extends ClickableBehavior<C> {
    private final ScaleClickable<C> image;
    private final Action clickAction;


    public ScaleClickableBehavior(C controller, ScaleClickable<C> image, Action clickAction) {
        super(controller);
        this.image = image;
        this.clickAction = clickAction;
    }

    @Override
    public boolean processTouch(TouchEvent touchEvent) {
        boolean isInBounds = image.isInBounds(touchEvent.getX(), touchEvent.getY());
        if (isInBounds) {
            if (touchEvent.isActionDown()) {
                image.setImageScale(1.2f, 1.2f);
            } else if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
                image.setImageScale(1f, 1f);
                if (clickAction != null) {
                    clickAction.performAction();
                }
            }
        } else {
            image.setImageScale(1f, 1f);
        }

        return isInBounds;
    }
}
