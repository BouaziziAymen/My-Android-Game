package com.evolgames.userinterface.view.visitor;

import android.util.Log;

import com.evolgames.userinterface.view.basics.Element;

public class IsUpdatedVisitBehavior extends VisitBehavior {
    private boolean isUpdated;
    @Override
    protected void visitElement(Element e) {
        if(e.isUpdated()) {
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

    public boolean isUpdated(){
        return isUpdated;
    }
    public void setUpdated(boolean updated){
         this.isUpdated = updated;
    }
}
