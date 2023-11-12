package com.evolgames.userinterface.model.toolmodels;

public enum ProjectileTriggerType {
  MANUAL(0),
  SEMI_AUTOMATIC(1),
  AUTOMATIC(2);

  private final int value;

  ProjectileTriggerType(final int newValue) {
    value = newValue;
  }

  public static ProjectileTriggerType getFromValue(int v) {
    if (v == 0) return MANUAL;
    if (v == 1) return SEMI_AUTOMATIC;
    if (v == 2) return AUTOMATIC;
    return null;
  }

  public int getValue() {
    return value;
  }
}
