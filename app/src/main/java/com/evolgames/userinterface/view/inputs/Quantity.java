package com.evolgames.userinterface.view.inputs;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.Temporal;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import java.util.ArrayList;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Quantity<C extends AdvancedWindowController<?>>
    extends InputField<C, QuantityBehavior<C>> implements Temporal {
  private final String key;
  private final LinearLayout mFront;
  private int mNumberOfStrokes;

  public Quantity(float pX, float pY, int mLength, String key) {
    this(pX, pY, mLength, key, 0);
  }

  public Quantity(float pX, float pY, int mLength, String key, float ratio) {
    this(pX, pY, mLength, key, ratio, false, false);
  }

  public Quantity(
      float pX, float pY, int mLength, String key, float ratio, boolean left, boolean right) {
    super(pX, pY, mLength, left, right);
    this.key = key;
    mFront = new LinearLayout(left ? 27 : 5, 5, LinearLayout.Direction.Horizontal, -1);
    addElement(mFront);
    updateRatio(ratio);
  }

  public Quantity(int mLength, String key) {
    this(0, 0, mLength, key);
  }

  public void updateRatio(float ratio) {
    if (ratio < 0 || ratio > 1) {
      throw new IllegalArgumentException("The ratio of quantity should be between 0 and 1 :"+ratio);
    }
    int newNumberOfStrokes = (int) Math.ceil(ratio * inputLength);
    if (newNumberOfStrokes != mNumberOfStrokes) {
      mNumberOfStrokes = newNumberOfStrokes;
      createFront();
      if (getBehavior() != null) {
        getBehavior().onViewUpdated();
      }
    }
  }

  public float getRatio() {
    return mNumberOfStrokes / (float) inputLength;
  }

  private void createFront() {
    mFront.updateLayout();
    if (mNumberOfStrokes == 0) {
      return;
    }
    ArrayList<ITextureRegion> frontRegions = ResourceManager.getInstance().quantity.get(key);
    mFront.addToLayout(new Image(frontRegions.get(0)));
    for (int i = 1; i < mNumberOfStrokes - 1; i++) {
      mFront.addToLayout(new Image(frontRegions.get(1)));
    }
    mFront.addToLayout(new Image(frontRegions.get(2)));
  }

  @Override
  public void onStep() {
    if (getBehavior() != null) {
      getBehavior().onStep();
    }
  }
}
