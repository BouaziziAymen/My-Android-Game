package com.evolgames.dollmutilationgame.userinterface.view.visitor;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.Temporal;

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
