package com.evolgames.userinterface.view.visitor;


import com.evolgames.userinterface.view.basics.Element;

public class ShadeVisitBehavior extends VisitBehavior {
    ShadeAction shadeAction = ShadeAction.Show;
    private Element excepted;

    public void setShadeAction(ShadeAction shadeAction) {
        this.shadeAction = shadeAction;
    }

    public void setExcepted(Element excepted) {
        this.excepted = excepted;
    }

    @Override
    protected void visitElement(Element e) {
        if (e != excepted) {
            switch (shadeAction) {
                case Show:
                    e.setShaded(false);
                    break;
                case Hide:
                    e.setShaded(true);
                    break;
            }
        }
    }

    @Override
    protected boolean forkCondition(Element e) {
        return e != excepted;
    }

    @Override
    protected boolean carryOnCondition() {
        return true;
    }

    public enum ShadeAction {
        Show,
        Hide
    }
}
