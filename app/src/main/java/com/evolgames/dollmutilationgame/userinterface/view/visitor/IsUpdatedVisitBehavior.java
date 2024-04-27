package com.evolgames.dollmutilationgame.userinterface.view.visitor;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class IsUpdatedVisitBehavior extends VisitBehavior {
    private boolean isUpdated;

    @Override
    protected void visitElement(Element e) {
        if (e.isUpdated()) {
            isUpdated = true;
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

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        this.isUpdated = updated;
    }
}
