package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.windowcontrollers.ThreeLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.windows.gamewindows.SettingsWindow;

public class SettingsWindowController extends ThreeLevelSectionedAdvancedWindowController<SettingsWindow, SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>> {
    protected UserInterface userInterface;
    protected ProperModel model;
    protected Properties tempProperty;

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void onCancelSettings() {
        if (tempProperty != null)
            model.setProperty(tempProperty);
        closeWindow();
    }

    void onModelUpdated(ProperModel model) {
        this.model = model;
        if (model.getProperty() != null)
            tempProperty = model.getProperty().getCopy();
    }

    public void onSubmitSettings() {
        if (getSelectedTextField() != null) {
            TextFieldBehavior<?> textFieldBehavior = getSelectedTextField().getBehavior();
            textFieldBehavior.release();
        }
        closeWindow();
    }

}



