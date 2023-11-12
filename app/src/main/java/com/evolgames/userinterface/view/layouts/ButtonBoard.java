package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;

public class ButtonBoard extends LinearLayout {
  public ButtonBoard(float pX, float pY, Direction direction, float margin) {
    super(pX, pY, direction, margin);
  }

  public void addToButtonBoard(Button<? extends Controller> button) {
    addToLayout(button);
  }

  @Override
  public void addToLayout(Element element) {
    if (element instanceof Button) super.addToLayout(element);
    else throw new UnsupportedOperationException("Non Button element added to ButtonBoard");
  }

  public Button<? extends Controller> getButtonAtIndex(int index) {
    return (Button<? extends Controller>) layoutElements.get(index);
  }

  public int getSize() {
    return layoutElements.size();
  }
}
