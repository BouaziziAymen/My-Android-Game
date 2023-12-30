package com.evolgames.userinterface.view.sections;

import com.evolgames.userinterface.view.basics.Element;

import java.util.ArrayList;

public abstract class CompositeSection<
        T extends Section<?>, Primary extends Element, Secondary extends Element>
    extends Section<Primary> {

  protected ArrayList<T> children = new ArrayList<>();

  public CompositeSection(int sectionKey, Primary primary, boolean isActive) {
    super(sectionKey, primary, isActive);
  }

  public CompositeSection(Element dummyElement) {
    super(dummyElement);
  }

  public ArrayList<T> getChildren() {
    return children;
  }
}
