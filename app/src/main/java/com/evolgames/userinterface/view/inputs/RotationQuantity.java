package com.evolgames.userinterface.view.inputs;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;

public class RotationQuantity<C extends AdvancedWindowController<?>> extends Quantity<C> {

  private Button<C> anticlockwiseButton;
  private Button<C> clockwiseButton;
  private Action clockwiseButtonsAction;
  private Action anticlockwiseButtonsAction;

  public RotationQuantity(float pX, float pY, int mLength, String key, float ratio) {
    super(pX, pY, mLength, key, ratio, true, true);
  }

  public RotationQuantity(float pX, float pY, int mLength, String key, C controller) {
    this(pX, pY, mLength, key, 0);
    buildButtons(controller);
  }

  public void setClockwiseButtonsAction(Action clockwiseButtonsAction) {
    this.clockwiseButtonsAction = clockwiseButtonsAction;
  }

  public void setAnticlockwiseButtonsAction(Action anticlockwiseButtonsAction) {
    this.anticlockwiseButtonsAction = anticlockwiseButtonsAction;
  }

  private void buildButtons(C controller) {
    anticlockwiseButton =
        new Button<>(
            getWidth() - 8 - inputLength,
            12 - 7,
            ResourceManager.getInstance().rotationAntiClockTextureRegion,
            Button.ButtonType.Selector,
            true);
    addElement(anticlockwiseButton);
    anticlockwiseButton.setBehavior(
        new ButtonBehavior<C>(controller, anticlockwiseButton) {
          @Override
          public void informControllerButtonClicked() {
            clockwiseButton.updateState(Button.State.NORMAL);
            if (anticlockwiseButtonsAction != null) {
              anticlockwiseButtonsAction.performAction();
            }
          }

          @Override
          public void informControllerButtonReleased() {
            clockwiseButton.updateState(Button.State.PRESSED);
          }
        });

    clockwiseButton =
        new Button<>(
            12 - 8,
            12 - 7,
            ResourceManager.getInstance().rotationClockTextureRegion,
            Button.ButtonType.Selector,
            true);
    addElement(clockwiseButton);

    clockwiseButton.setBehavior(
        new ButtonBehavior<C>(controller, clockwiseButton) {
          @Override
          public void informControllerButtonClicked() {
            anticlockwiseButton.updateState(Button.State.NORMAL);
            if (clockwiseButtonsAction != null) {
              clockwiseButtonsAction.performAction();
            }
          }

          @Override
          public void informControllerButtonReleased() {
            anticlockwiseButton.updateState(Button.State.PRESSED);
          }
        });
  }

  public boolean isClockwise() {
    return clockwiseButton.getState() == Button.State.PRESSED;
  }

  public void setClockwise(boolean clockwise) {
    if (clockwise) {
      clockwiseButton.updateState(Button.State.PRESSED);
      anticlockwiseButton.updateState(Button.State.NORMAL);
    } else {
      clockwiseButton.updateState(Button.State.NORMAL);
      anticlockwiseButton.updateState(Button.State.PRESSED);
    }
  }
}
