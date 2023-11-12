package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;

public class DistanceJointShape extends JointShape {
  public DistanceJointShape(EditorScene scene, Vector2 begin) {
    super(
        scene.getUserInterface(),
        begin,
        scene,
        ResourceManager.getInstance().targetCircleTextureRegion);
  }
}
