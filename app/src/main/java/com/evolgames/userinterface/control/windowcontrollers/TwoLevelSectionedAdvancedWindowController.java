package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController3;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class TwoLevelSectionedAdvancedWindowController<
        W extends AbstractSectionedAdvancedWindow<?>,
        Primary extends Element,
        Secondary extends Element,
        Tertiary extends Element>
        extends AbstractSectionedAdvancedWindowController3<W, Primary, Secondary, Tertiary> {

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
    }

    public void onTertiaryButtonClicked(Tertiary tertiary) {
    }

    public void onTertiaryButtonReleased(Tertiary tertiary) {
    }
}
