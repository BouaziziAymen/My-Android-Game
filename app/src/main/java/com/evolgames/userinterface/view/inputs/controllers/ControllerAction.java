package com.evolgames.userinterface.view.inputs.controllers;

public abstract class ControllerAction {
  public abstract void controlMoved(float pX, float pY);

  public abstract void controlClicked();

  public abstract void controlReleased();
}
