package com.evolgames.userinterface.view.visitor;

import com.evolgames.userinterface.view.basics.Limited;
import com.evolgames.userinterface.view.basics.Element;

public class LimitBehavior extends VisitBehavior {
    private float inf;
    private float sup;

    public void setInf(float inf) {
        this.inf = inf;
    }


    public void setSup(float sup) {
        this.sup = sup;
    }

    @Override
    protected void visitElement(Element e) {
        float y = e.getAbsoluteY();
        if (y + e.getHeight() > sup) {
            if (e instanceof Limited) {
                Limited limited = (Limited) e;
                limited.setLimited(true);
                limited.setLimitY0(0);
                limited.setLimitY1(-y + sup);
            }
        } else if (y < inf) {
            if (e instanceof Limited) {
                Limited limited = (Limited) e;
                limited.setLimited(true);
                limited.setLimitY0(inf - y);
                limited.setLimitY1(e.getHeight());
            }

        } else {
            if (e instanceof Limited) {
                Limited limited = (Limited) e;
                limited.setLimited(false);
            }

        }


    }

    @Override
    protected boolean forkCondition(Element e) {
        return true;
    }

    @Override
    protected boolean carryOnCondition() {
        return true;
    }
}
