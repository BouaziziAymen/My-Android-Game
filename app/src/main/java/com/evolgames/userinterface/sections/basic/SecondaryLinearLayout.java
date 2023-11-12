package com.evolgames.userinterface.sections.basic;

public class SecondaryLinearLayout extends PrimaryLinearLayout implements SecondaryInterface {
  private final int secondaryKey;

  public SecondaryLinearLayout(int primaryKey, int secondaryKey, float margin) {
    super(primaryKey, margin);
    this.secondaryKey = secondaryKey;
  }

  @Override
  public int getSecondaryKey() {
    return secondaryKey;
  }
}
