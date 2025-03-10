package com.evolgames.dollmutilationgame.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.actions.ControllerAction;

public class ReferencePointImage extends PointImage {
    private ControllerAction controllerAction;

    public ReferencePointImage(Vector2 point, ControllerAction controllerAction) {
        super(ResourceManager.getInstance().centeredDiskTextureRegion, point);
        this.controllerAction = controllerAction;
    }

    public ReferencePointImage(Vector2 point) {
        super(ResourceManager.getInstance().centeredDiskTextureRegion, point);
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        super.onControllerMoved(dx, dy);
        if (controllerAction != null) {
            controllerAction.performAction(dx, dy);
        }
    }
}
