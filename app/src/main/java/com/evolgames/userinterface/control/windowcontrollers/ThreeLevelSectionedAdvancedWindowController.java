package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController4;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.ThreeLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class ThreeLevelSectionedAdvancedWindowController<W extends AbstractSectionedAdvancedWindow<ThreeLevelSectionLayout<Primary, Secondary, Tertiary,Quaternary>>, Primary extends Element, Secondary extends Element, Tertiary extends Element,Quaternary extends Element> extends AbstractSectionedAdvancedWindowController4<W, Primary, Secondary,Tertiary,Quaternary> {


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
    public void onSecondaryButtonClicked(Secondary secondary) {
        secondary.getSection().setActive(true);
       updateLayout();
    }

    @Override
    public void onSecondaryButtonReleased(Secondary secondary) {
        secondary.getSection().setActive(false);
       updateLayout();

    }



    @Override
    public void onPrimaryAdded(Primary primaryField) {
       updateLayout();
        onPrimaryButtonClicked(primaryField);
    }

    @Override
    public void onSecondaryAdded(Secondary secondaryField) {
        updateLayout();
        onSecondaryButtonClicked(secondaryField);
    }

    public void onTertiaryAdded(Tertiary tertiary) {
        updateLayout();
        onTertiaryButtonClicked(tertiary);
    }


    public void onTertiaryButtonClicked(Tertiary tertiary) {
        tertiary.getSection().setActive(true);
       updateLayout();
    }


    public void onTertiaryButtonReleased(Tertiary tertiary) {
        tertiary.getSection().setActive(false);
        updateLayout();
    }

    @Override
    public void onQuaternaryButtonClicked(Quaternary quaternary) {

    }

    @Override
    public void onQuaternaryButtonReleased(Quaternary quaternary) {

    }

    @Override
    public void onQuaternaryAdded(Quaternary tertiary) {

    }
}
