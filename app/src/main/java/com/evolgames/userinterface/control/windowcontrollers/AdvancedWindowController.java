package com.evolgames.userinterface.control.windowcontrollers;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.windows.AdvancedWindow;

public class AdvancedWindowController<W extends AdvancedWindow<?>> extends Controller {

    protected W window;
    protected KeyboardController keyboardController;
    private TextField<?> selectedTextField;

    public TextField<?> getSelectedTextField() {
        return selectedTextField;
    }

    public void setWindow(W window) {
        this.window = window;
    }

    public void onFoldButtonClicked() {
        showWindowBody();
    }

    public void onFoldButtonReleased() {
        hideWindowBody();
    }

    private boolean isElementPartOf(Element e, WindowPartIdentifier windowPartIdentifier) {
        return e.getWindowPartIdentifier() == windowPartIdentifier;
    }

    protected void setBodyOnly() {
        for (Element e : window.getContents())
            if (!isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY))
                e.setVisible(false);
    }

    private void hideWindowBody() {
        for (Element e : window.getContents())
            if (isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY) || isElementPartOf(e, WindowPartIdentifier.WINDOW_MID_PADDING))
                e.setVisible(false);
    }

    private void showWindowBody() {
        for (Element e : window.getContents())
            if (isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY) || isElementPartOf(e, WindowPartIdentifier.WINDOW_MID_PADDING))
                e.setVisible(true);
    }

    public void onCloseButtonClicked() {
        closeWindow();
    }

    public void closeWindow() {
        window.setVisible(false);
    }

    public void openWindow() {
        if (window != null) {
            window.setVisible(true);
        }
    }

    @Override
    public void init() {

    }

    public void unfold() {
        hideWindowBody();
        disableFoldButton();
    }

    public void fold() {
        showWindowBody();
        enableFoldButton();

    }

    private void disableFoldButton() {
        window.disableFoldButton();
    }

    private void disableCloseButton() {
        window.disableCloseButton();
    }

    private void enableFoldButton() {
        window.enableFoldButton();

    }

    private void enableCloseButton() {
        window.enableCloseButton();
    }


    protected void onTextFieldTapped(TextField<?> pTextField) {
        if (selectedTextField != null) onTextFieldReleased(selectedTextField);
        selectedTextField = pTextField;
        TextFieldBehavior<?> textFieldBehavior = (TextFieldBehavior<?>) pTextField.getBehavior();
        if (textFieldBehavior.isResetText()) textFieldBehavior.setTextString("");
        textFieldBehavior.setSelected(true);
        keyboardController.bindWithTextField(pTextField);
        keyboardController.getKeyboard().setCurrentType(textFieldBehavior.getKeyboardType());

        keyboardController.openKeyboard(pTextField.getAbsoluteX(), pTextField.getAbsoluteY(), selectedTextField.getWidth(), selectedTextField.getHeight());

    }

    protected void onTextFieldReleased(TextField<?> textField) {
        selectedTextField = null;
        TextFieldBehavior<?> textFieldBehavior = (TextFieldBehavior<?>) textField.getBehavior();
        if (textFieldBehavior.getValidator() == null || textFieldBehavior.validate()) {
            textFieldBehavior.setLastValidTextString(textField.getTextString());
            if (textFieldBehavior.getReleaseAction() != null)
                textFieldBehavior.getReleaseAction().performAction();
        } else {
            textFieldBehavior.setText(textFieldBehavior.getLastValidTextString());
        }
        textFieldBehavior.setSelected(false);
        if (keyboardController != null) {
            keyboardController.unbindTextField();
            keyboardController.closeKeyboard();
        }
    }
}
