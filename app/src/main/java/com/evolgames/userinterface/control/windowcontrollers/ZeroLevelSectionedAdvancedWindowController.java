package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController1;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.ZeroLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class ZeroLevelSectionedAdvancedWindowController<W extends AbstractSectionedAdvancedWindow<ZeroLevelSectionLayout<Primary>>,Primary extends Element> extends AbstractSectionedAdvancedWindowController1<W,Primary> {
    @Override
    public void onPrimaryButtonClicked(Primary primary) {
        primary.getSection().setActive(true);
        updateLayout();
    }


    @Override
    public void onPrimaryButtonReleased(Primary primary) {
        primary.getSection().setActive(false);
        updateLayout();
    }




    @Override
    public void onPrimaryAdded(Primary primaryField) {
       updateLayout();
        onPrimaryButtonClicked(primaryField);
    }



}
