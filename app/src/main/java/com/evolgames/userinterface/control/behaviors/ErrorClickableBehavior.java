package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Element;

public abstract class ErrorClickableBehavior<C extends Controller> extends AdvancedClickableBehavior<C> {
  private final Element errorProducer;
  protected boolean showError = false;
  private int errorTimer = 0;

  public ErrorClickableBehavior(C controller, Element errorProducer) {
    super(controller);
    this.errorProducer = errorProducer;
  }

  public void onStep() {
    if (errorProducer.getErrorDisplay() != null) {
      if (showError) errorTimer++;
      if (errorTimer > 120) {
        showError = false;
        errorTimer = 0;
        errorProducer.getErrorDisplay().hideError();
        updateWindowLayout();
      }
    }
  }
}
