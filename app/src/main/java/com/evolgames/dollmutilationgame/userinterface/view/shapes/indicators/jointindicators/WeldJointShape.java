package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;
import com.evolgames.dollmutilationgame.scenes.EditorScene;

public class WeldJointShape extends JointShape {
    public WeldJointShape(EditorScene scene, Vector2 begin) {
        super(
                scene.getUserInterface(),
                begin,
                scene,
                ResourceManager.getInstance().emptySquareTextureRegion);
    }

    public void bindModel(JointModel model) {
        this.model = model;
        Vector2 modelEnd = model.getProperties().getLocalAnchorB();
        this.updateEnd(modelEnd.x, modelEnd.y);
        model.setJointShape(this);
    }
}
