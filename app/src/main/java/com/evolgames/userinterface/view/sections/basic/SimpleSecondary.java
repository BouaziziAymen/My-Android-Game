package com.evolgames.userinterface.view.sections.basic;

import com.evolgames.userinterface.view.basics.Element;

public class SimpleSecondary<T extends Element> extends SimplePrimary<T> {
  private final int secondaryKey;

  public SimpleSecondary(int primaryKey, int secondaryKey, T element) {
    super(primaryKey, element);
    this.secondaryKey = secondaryKey;
  }

  public int getSecondaryKey() {
    return secondaryKey;
  }
}
