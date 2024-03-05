package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;

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
      Vector2 modelEnd = model.getLocalAnchorB();
      model.setJointShape(this);
      this.updateEnd(modelEnd.x, modelEnd.y);
    }
}
