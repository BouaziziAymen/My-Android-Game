package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.OneLevelSectionLayout;

public abstract class AbstractOneLevelSectionedAdvancedWindow<Primary extends Element,Secondary extends Element> extends AbstractSectionedAdvancedWindow<OneLevelSectionLayout<Primary,Secondary>> {
     public AbstractOneLevelSectionedAdvancedWindow(float pX, float pY, int pRows, int pColumns, LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, pRows, pColumns, controller);
    }




    public abstract Primary createPrimary(int primaryKey);
    public abstract Secondary createSecondary(int primaryKey, int secondaryKey);
    public Primary addPrimary(int primaryKey, boolean isActive) {
        Primary primaryField = createPrimary(primaryKey);
        layout.addPrimary(primaryKey, primaryField, isActive);
        return primaryField;
    }
    protected Secondary addSecondary(int primaryKey, int secondaryKey){
        Secondary secondary = createSecondary(primaryKey,secondaryKey);
        layout.addSecondary(primaryKey,secondaryKey,secondary);
        return secondary;
    }


    public void addPrimary(Primary primaryField,int primaryKey, boolean isActive) {
        layout.addPrimary(primaryKey, primaryField, isActive);
    }
    protected void addSecondary(Secondary secondaryField,int primaryKey, int secondaryKey){
        layout.addSecondary(primaryKey,secondaryKey,secondaryField);
    }



}
