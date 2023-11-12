package com.evolgames.userinterface.sections;

import com.evolgames.userinterface.view.basics.Element;

public class Section<Primary extends Element> {
  private boolean isActive;
  private final int sectionKey;
  private Primary primary;
  private boolean isDummy;
  private Element dummyElement;

  public Section(int key, Primary primary, boolean isActive) {
    this.sectionKey = key;
    this.primary = primary;
    this.isActive = isActive;
  }

  public Section(Element dummyElement) {
    this.isDummy = true;
    this.dummyElement = dummyElement;
    sectionKey = -1;
  }

  public int getSectionKey() {
    return sectionKey;
  }

  public boolean isDummy() {
    return isDummy;
  }

  public Element getDummyElement() {
    return dummyElement;
  }

  public Primary getPrimary() {

    return primary;
  }

  public void setPrimary(Primary primary) {
    this.primary = primary;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }
}
