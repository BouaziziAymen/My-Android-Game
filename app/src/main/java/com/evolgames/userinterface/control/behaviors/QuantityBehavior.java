package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Quantity;
import org.andengine.input.touch.TouchEvent;

public abstract class QuantityBehavior<C extends AdvancedWindowController<?>>
    extends ErrorClickableBehavior<C> {
  private final Quantity<C> quantity;
  private boolean touched;
  private int pointerId;
  private Action changeAction;
  private Condition condition;

  public QuantityBehavior(C controller, Quantity<C> quantity) {
    super(controller, quantity);
    this.quantity = quantity;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  public void setChangeAction(Action changeAction) {
    this.changeAction = changeAction;
  }

  public abstract void informControllerQuantityUpdated(Quantity<?> quantity);

  @Override
  public boolean processTouch(TouchEvent touchEvent) {
    if (touchEvent.isActionDown())
      if (quantity.isInBounds(touchEvent.getX(), touchEvent.getY())) {
        this.touched = true;
        pointerId = touchEvent.getPointerID();
      }
    if (touchEvent.getPointerID() == pointerId) {
      if (touchEvent.isActionUp() || touchEvent.isActionOutside() || touchEvent.isActionCancel())
        this.touched = false;

      if (this.touched) {
        float ratio =
            (touchEvent.getX() - (quantity.getAbsoluteX() + (quantity.isLeftSlot() ? 27 : 5)))
                / (quantity.getWidth()
                    - (quantity.isLeftSlot() ? 27 : 5)
                    - (quantity.isRightSlot() ? 27 : 5));
        if (ratio > 1) {
          ratio = 1;
        }
        if (ratio < 0) {
          ratio = 0;
        }
        boolean isCondition = (condition != null) && condition.isCondition(ratio);
        if (condition == null || isCondition) {
          quantity.updateRatio(ratio);
          informControllerQuantityUpdated(quantity);
          if (changeAction != null) {
            changeAction.performAction();
          }
          return true;
        } else {
          if (quantity.getErrorDisplay() != null&&!showError) {
            showError = true;
            quantity.getErrorDisplay().showError(condition.getError());
            updateWindowLayout();
          }
        }
      }
    }
    return false;
  }
}
