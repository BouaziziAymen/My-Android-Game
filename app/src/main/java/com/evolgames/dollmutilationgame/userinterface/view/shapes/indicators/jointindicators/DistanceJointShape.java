package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;

public class DistanceJointShape extends JointShape {
    public DistanceJointShape(EditorScene scene, Vector2 begin) {
        super(
                scene.getUserInterface(),
                begin,
                scene,
                ResourceManager.getInstance().targetCircleTextureRegion);
    }

    public void bindModel(JointModel model) {
        this.model = model;
        Vector2 modelEnd = model.getProperties().getLocalAnchorB();
        model.setJointShape(this);
        this.updateEnd(modelEnd.x, modelEnd.y);
    }
}
