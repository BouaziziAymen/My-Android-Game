package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.control.windowcontrollers.ThreeLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.TextField;
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
    protected ProperModel<P> model;
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
        closeWindow();
    }

    @Override
    protected void onTextFieldTapped(TextField<?> pTextField) {
        super.onTextFieldTapped(pTextField);
        window.getPanel().getAcceptButton().updateState(Button.State.DISABLED);
        window.getPanel().getCloseButton().updateState(Button.State.DISABLED);
    }

    @Override
    protected void onTextFieldReleased(TextField<?> textField) {
        super.onTextFieldReleased(textField);
        window.getPanel().getAcceptButton().updateState(Button.State.NORMAL);
        window.getPanel().getCloseButton().updateState(Button.State.NORMAL);
    }
}
