package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;

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
    Vector2 modelEnd = model.getLocalAnchorB();
    this.updateEnd(modelEnd.x, modelEnd.y);
    model.setJointShape(this);
  }
}
