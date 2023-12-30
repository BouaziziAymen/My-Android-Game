package com.evolgames.userinterface.view.sections;

import com.evolgames.userinterface.view.basics.Element;

public class ZeroLevelSection<Primary extends Element> extends Section<Primary> {

  public ZeroLevelSection(int sectionKey, Primary primary) {

    super(sectionKey, primary, false);
  }

  public ZeroLevelSection(Element e) {
    super(e);
  }
}
