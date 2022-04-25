package com.evolgames.userinterface.view.visitor;

import com.evolgames.userinterface.view.basics.Element;

public abstract class VisitBehavior {
    protected abstract void visitElement(Element e);
    protected abstract boolean forkCondition(Element e);
    protected abstract boolean carryOnCondition();
}
