package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController2;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class OneLevelSectionedAdvancedWindowController<W extends AbstractSectionedAdvancedWindow<?>,Primary extends Element,Secondary extends Element> extends AbstractSectionedAdvancedWindowController2<W,Primary,Secondary> {

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
    public void onSecondaryButtonClicked(Secondary secondary){

    }
    @Override
    public void onSecondaryButtonReleased(Secondary secondary){

    }

    @Override
    public void onPrimaryAdded(Primary bodyField) {

    }

    @Override
    public void onSecondaryAdded(Secondary secondary) {

    }


}
