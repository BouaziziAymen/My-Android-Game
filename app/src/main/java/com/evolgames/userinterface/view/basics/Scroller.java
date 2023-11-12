package com.evolgames.userinterface.view.basics;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.Touchable;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import org.andengine.input.touch.TouchEvent;

public class Scroller extends LinearLayout implements Touchable {
  public static final float UPPER_WIDTH = 24f;
  private static final float UPPER_HEIGHT = 27f;
  private static final float SPACE_MARGIN = -1f;
  private static final float SLOT_WIDTH = 24f;
  private static final float TRAIL_WIDTH = 10f;
  private static final float TRAIL_HEIGHT = 32f;
  private final Image knob;

  private final LinearLayoutAdvancedWindowController<?> mController;
  private final float effectiveHeight;
  private final Button<LinearLayoutAdvancedWindowController<?>> upArrowButton;
  private final Button<LinearLayoutAdvancedWindowController<?>> downArrowButton;
  private final float visibilityLength;
  private float knobHeight;
  private float mAdvance;
  private boolean isTouched;

  public Scroller(
      float pX,
      float pY,
      int rows,
      LinearLayoutAdvancedWindowController<?> controller,
      float visibilityLength) {
    super(pX, pY, Direction.Vertical, SPACE_MARGIN);
    this.mController = controller;
    this.visibilityLength = visibilityLength;

    effectiveHeight = (TRAIL_HEIGHT + SPACE_MARGIN) * rows;

    Image upper = new Image(-SLOT_WIDTH / 2, 0, ResourceManager.getInstance().upperTextureRegion);
    addToLayout(upper);

    for (int i = 0; i < rows; i++) {
      Image middle =
          new Image(-TRAIL_WIDTH / 2, 0, ResourceManager.getInstance().trailTextureRegion);
      addToLayout(middle);
    }
    Image lower = new Image(-SLOT_WIDTH / 2, 0, ResourceManager.getInstance().lowerTextureRegion);
    addToLayout(lower);

    upArrowButton =
        new Button<>(
            upper.getLowerBottomX() + upper.getWidth() / 2 - 8,
            upper.getLowerBottomY() + 15 - 7,
            ResourceManager.getInstance().upButtonTextureRegions.get(0),
            Button.ButtonType.OneClick,
            true);
    addElement(upArrowButton);
    upArrowButton.setBehavior(
        new ButtonBehavior<LinearLayoutAdvancedWindowController<?>>(controller, upArrowButton) {
          @Override
          public void informControllerButtonClicked() {
            incrementAdvance(-0.1f);
          }

          @Override
          public void informControllerButtonReleased() {}
        });

    downArrowButton =
        new Button<>(
            lower.getLowerBottomX() + lower.getWidth() / 2 - 8,
            lower.getLowerBottomY() + 12 - 7,
            ResourceManager.getInstance().downButtonTextureRegions.get(0),
            Button.ButtonType.OneClick,
            true);
    addElement(downArrowButton);

    downArrowButton.setBehavior(
        new ButtonBehavior<LinearLayoutAdvancedWindowController<?>>(controller, downArrowButton) {
          @Override
          public void informControllerButtonClicked() {
            incrementAdvance(0.1f);
          }

          @Override
          public void informControllerButtonReleased() {}
        });
    downArrowButton.setDepth(-1);
    upArrowButton.setDepth(-1);

    knob = new Image(-TRAIL_WIDTH / 2, 0, ResourceManager.getInstance().scrollerKnobTextureRegion);
    addElement(knob);
    knob.setColor(143 / 255f, 86 / 255f, 59 / 255f);
    knob.setDepth(1);
  }

  private float getEffectiveSup() {
    return getAbsoluteY() - UPPER_HEIGHT;
  }

  private float getEffectiveInf() {
    return getEffectiveSup() - effectiveHeight;
  }

  public void onHeightUpdated(float height) {
    float ratio = visibilityLength / height;
    if (ratio >= 1) {
      upArrowButton.updateState(Button.State.DISABLED);
      downArrowButton.updateState(Button.State.DISABLED);
      knob.setVisible(false);
      knob.setLowerBottomY(-knobHeight - UPPER_HEIGHT);
      updateAdvance();
      mController.onScrolled(mAdvance);
    } else {
      upArrowButton.updateState(Button.State.NORMAL);
      downArrowButton.updateState(Button.State.NORMAL);
      knob.setVisible(true);
      knobHeight = effectiveHeight * ratio;
      updateKnobSize();
      correctKnob();
      updateKnobLowerBottomY();
      updateAdvance();
      mController.onVisibleZoneUpdate();
    }
  }

  private void incrementAdvance(float dA) {
    updateKnobPosition(mAdvance + dA);
    updateAdvance();
    knob.setUpdated(true);
    mController.onScrolled(mAdvance);
  }

  public void setRatio(float ratio) {
    if (ratio >= 1) {

      knob.setVisible(false);
      knob.setLowerBottomY(-knobHeight - UPPER_HEIGHT);
      updateAdvance();
      mController.onScrolled(mAdvance);

    } else {
      knob.setVisible(true);
      knobHeight = effectiveHeight * ratio;
      updateKnobSize();
      correctKnob();
      updateAdvance();
      mController.onScrolled(mAdvance);
    }
  }

  private void updateKnobPosition(float newAdvance) {
    float newY = -newAdvance * effectiveHeight - knobHeight - UPPER_HEIGHT;
    knob.setLowerBottomY(newY);
    correctKnob();
  }

  private void updateAdvance() {
    mAdvance = -(knob.getLowerBottomY() + knobHeight + UPPER_HEIGHT) / effectiveHeight;
  }

  private void updateKnobLowerBottomY() {
    knob.setLowerBottomY(-(mAdvance * effectiveHeight + knobHeight + UPPER_HEIGHT));
  }

  private void updateKnobSize() {
    knob.setScale(1, knobHeight / 100f);
  }

  @Override
  public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
    if (!knob.isVisible()) return false;
    if (pTouchEvent.isActionDown()) {
      if (Math.abs(pTouchEvent.getX() - getAbsoluteX()) < UPPER_WIDTH / 2) {
        if (pTouchEvent.getY() < getEffectiveSup() && pTouchEvent.getY() > getEffectiveInf()) {
          this.isTouched = true;
        }
      }
    }

    if (this.isTouched) {
      if (pTouchEvent.isActionOutside()
          || pTouchEvent.isActionCancel()
          || pTouchEvent.isActionUp()) {
        this.isTouched = false;
        return true;
      }
    }
    if (this.isTouched) {
      float newY = pTouchEvent.getY() - getAbsoluteY() - knobHeight / 2;
      knob.setLowerBottomY(newY);
      correctKnob();
      updateAdvance();
      knob.setUpdated(true);
      mController.onScrolled(mAdvance);
      return true;
    }

    return false;
  }

  private void correctKnob() {
    float newY = knob.getLowerBottomY();
    if (newY > -UPPER_HEIGHT - knobHeight) newY = -knobHeight - UPPER_HEIGHT;
    if (newY < -UPPER_HEIGHT - effectiveHeight) newY = -UPPER_HEIGHT - effectiveHeight;
    knob.setLowerBottomY(newY);
  }
}
