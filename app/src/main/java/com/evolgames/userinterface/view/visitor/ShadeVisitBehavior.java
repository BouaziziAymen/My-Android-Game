package com.evolgames.userinterface.view.visitor;

import android.util.Log;

import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;

public class ShadeVisitBehavior extends VisitBehavior {
    private Element excepted;
    public enum ShadeAction{Show, Hide}
    ShadeAction shadeAction = ShadeAction.Show;

    public void setShadeAction(ShadeAction shadeAction) {
        this.shadeAction = shadeAction;
    }

    public void setExcepted(Element excepted) {
        this.excepted = excepted;
    }

    @Override
    protected void visitElement(Element e) {
        if(e!=excepted){
            switch (shadeAction){
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
        return e!=excepted;
    }

    @Override
    protected boolean carryOnCondition() {
        return true;
    }
}
