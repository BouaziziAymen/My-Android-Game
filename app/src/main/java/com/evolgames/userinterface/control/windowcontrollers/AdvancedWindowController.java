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
        for (Element e : window.getContents()) {
            if (!isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY)) {
                e.setVisible(false);
            }
        }
    }

    protected void hideWindowBody() {
        for (Element e : window.getContents()) {
            if (isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY) || isElementPartOf(e, WindowPartIdentifier.SCROLLER)|| isElementPartOf(e, WindowPartIdentifier.WINDOW_MID_PADDING)) {
                e.setVisible(false);
            }
        }
    }

    protected void showWindowBody() {
        for (Element e : window.getContents()) {
            if (isElementPartOf(e, WindowPartIdentifier.WINDOW_BODY) || isElementPartOf(e, WindowPartIdentifier.SCROLLER) || isElementPartOf(e, WindowPartIdentifier.WINDOW_MID_PADDING)) {
                e.setVisible(true);
            }
        }
    }

    public void onCloseButtonClicked() {
        closeWindow();
    }

    public void closeWindow() {
        window.setVisible(false);
    }

    public void openWindow() {
        window.setVisible(true);
    }

    @Override
    public void init() {

    }

    public void onResume() {
        fold();
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

    private void enableFoldButton() {
        window.enableFoldButton();

    }

    protected void onTextFieldTapped(TextField<?> pTextField) {
        if (selectedTextField != null) {
            return;
        }
        selectedTextField = pTextField;
        TextFieldBehavior<?> textFieldBehavior = pTextField.getBehavior();
        if (textFieldBehavior.isResetText()) {
            textFieldBehavior.setTextString("");
        }
        textFieldBehavior.setSelected(true);
        keyboardController.bindWithTextField(pTextField);
        keyboardController.getKeyboard().setCurrentType(textFieldBehavior.getKeyboardType());
        keyboardController.openKeyboard(pTextField.getAbsoluteX(), pTextField.getAbsoluteY(), selectedTextField.getWidth(), selectedTextField.getHeight());
    }

    protected void onTextFieldReleased(TextField<?> textField) {
        if (textField != selectedTextField) {
            return;
        }
        TextFieldBehavior<?> textFieldBehavior = textField.getBehavior();
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
        selectedTextField = null;
    }
}
