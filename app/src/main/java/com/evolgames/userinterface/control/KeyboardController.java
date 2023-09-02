package com.evolgames.userinterface.control;

import android.util.Log;

import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.KeyboardButton;
import com.evolgames.userinterface.view.inputs.TextField;

public class KeyboardController extends Controller {
    private TextField<? extends AdvancedWindowController> boundTextField;
    private Keyboard keyboard;


    @Override
    public void init() {
    }

    public void bindWithTextField(TextField<? extends AdvancedWindowController> pText) {
        if (pText == boundTextField) return;
        pText.getBehavior().setSelected(false);
        unbindTextField();
        this.boundTextField = pText;
    }

    private void updateTextString(String newText) {
        boundTextField.getBehavior().setText(newText);
    }

    public void onCharacterTyped(char pChar) {
        String oldTextString = boundTextField.getTextString();
        updateTextString(oldTextString + pChar);

    }

    public void onSpaceClicked() {
        String oldTextString = boundTextField.getTextString();
        updateTextString(oldTextString + " ");

    }

    public void onDeleteClicked() {
        String oldTextString = boundTextField.getTextString();
        if (oldTextString.length() == 0) return;
        updateTextString(oldTextString.substring(0, oldTextString.length() - 1));
    }

    public void onShiftClicked() {
        for (KeyboardButton button : keyboard.getButtons()) {
            if (button.getKeyboardButtonType() == KeyboardButton.KeyboardButtonType.CHARACTER) {
                button.getText().updateText(button.getText().getTextString().toUpperCase());
                button.setChar(Character.toUpperCase(button.getChar()));
            }
        }
    }

    public void onShiftReleased() {
        for (KeyboardButton button : keyboard.getButtons()) {
            if (button.getKeyboardButtonType() == KeyboardButton.KeyboardButtonType.CHARACTER) {
                button.getText().updateText(button.getText().getTextString().toLowerCase());
                button.setChar(Character.toLowerCase(button.getChar()));
            }
        }
    }


    public void unbindTextField() {
        if (!isBound()) return;
        boundTextField = null;
    }

    public boolean isBound() {
        return boundTextField != null;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void openKeyboard(float absoluteX, float absoluteY, float textFieldWidth, float textFieldHeight) {
        float keyboardWidth = keyboard.getWidth();
        float keyboardHeight = keyboard.getHeight();
        Log.e("keyboard", keyboardWidth + "/" + keyboardHeight);
        float x = absoluteX + textFieldWidth / 2 - keyboardWidth / 2;
        float y = absoluteY + textFieldHeight;
        if (x + keyboardWidth > 800) x = 800 - keyboardWidth;
        if (y + keyboardHeight > 480) y = absoluteY - keyboardHeight - 16;
        getKeyboard().setPosition(x, y);
        getKeyboard().setVisible(true);

    }

    public void closeKeyboard() {
        getKeyboard().setVisible(false);
    }
}
