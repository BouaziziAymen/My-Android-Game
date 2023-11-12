package com.evolgames.userinterface.view.inputs;

import org.andengine.input.touch.TouchEvent;

public interface Touchable {
  boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched);
}
