package com.evolgames.userinterface.view.inputs.controllers;

import org.andengine.entity.Entity;

public class ControlElement {
  private final Type type;
  private final int ID;
  private ControllerAction action;
  private Entity associate;
  private boolean visible;

  public ControlElement(Type type, int ID, ControllerAction action) {
    this.type = type;
    this.ID = ID;
    this.action = action;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
    if (associate != null) {
      associate.setVisible(visible);
    }
  }

  public int getID() {
    return ID;
  }

  public Entity getAssociate() {
    return associate;
  }

  void setAssociate(Entity associate) {
    this.associate = associate;
  }

  public ControllerAction getAction() {
    return action;
  }

  public void setAction(ControllerAction action) {
    this.action = action;
  }

  public Type getType() {
    return type;
  }

  public enum Type {
    AnalogController,
    DigitalController
  }
}
