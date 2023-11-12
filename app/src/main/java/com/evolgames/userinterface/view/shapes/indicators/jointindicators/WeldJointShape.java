package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;

public class WeldJointShape extends JointShape {
  public WeldJointShape(EditorScene scene, Vector2 begin) {
    super(
        scene.getUserInterface(),
        begin,
        scene,
        ResourceManager.getInstance().emptySquareTextureRegion);
  }
}
