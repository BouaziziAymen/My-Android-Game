package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.windowcontrollers.ThreeLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.windows.gamewindows.SettingsWindow;

public class SettingsWindowController<P extends Properties>
        extends ThreeLevelSectionedAdvancedWindowController<
        SettingsWindow,
        SimplePrimary<?>,
        SimpleSecondary<?>,
        SimpleTertiary<?>,
        SimpleQuaternary<?>> {
    protected EditorUserInterface editorUserInterface;
    protected ProperModel model;
    protected P tempProperty;

    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    public void onCancelSettings() {
        if (tempProperty != null) {
            model.setProperties(tempProperty);
        }
        closeWindow();
    }

    void onModelUpdated(ProperModel<P> model) {
        this.model = model;
        if (model != null && model.getProperties() != null) {
            tempProperty = (P) model.getProperties().clone();
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
