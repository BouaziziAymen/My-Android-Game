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

public class SettingsWindowController<P extends Properties> extends ThreeLevelSectionedAdvancedWindowController<SettingsWindow, SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>> {
    protected UserInterface userInterface;
    protected ProperModel model;
    protected P tempProperty;

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void onCancelSettings() {
        if (tempProperty != null){
            model.setProperties(tempProperty);
        }
        closeWindow();
    }

    void onModelUpdated(ProperModel<P> model) {
        this.model = model;
        if (model!=null && model.getProperties() != null) {
            tempProperty = (P) model.getProperties().copy();
        }
    }

    public void onSubmitSettings() {
        if (getSelectedTextField() != null) {
            TextFieldBehavior<?> textFieldBehavior = getSelectedTextField().getBehavior();
            textFieldBehavior.release();
        }
        closeWindow();
    }

}



