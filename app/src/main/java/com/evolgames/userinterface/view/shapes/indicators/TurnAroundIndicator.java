package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.view.inputs.controllers.Movable;

public abstract class TurnAroundIndicator extends FixedLengthArrowShape implements Movable {

  protected TurnAroundIndicator(Vector2 begin, EditorScene scene, float length, int size) {
    super(begin, scene, length, size);
  }

  public abstract void onTurnAroundCommand(float dA);
}
