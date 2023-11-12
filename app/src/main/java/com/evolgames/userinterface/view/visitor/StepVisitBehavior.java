package com.evolgames.userinterface.view.visitor;

import com.evolgames.userinterface.view.Temporal;
import com.evolgames.userinterface.view.basics.Element;

public class StepVisitBehavior extends VisitBehavior {
  @Override
  protected void visitElement(Element e) {
    if (e instanceof Temporal) {
      ((Temporal) e).onStep();
    }
  }

  @Override
  protected boolean forkCondition(Element e) {
    return e.isVisible();
  }

  @Override
  protected boolean carryOnCondition() {
    return true;
  }
}
