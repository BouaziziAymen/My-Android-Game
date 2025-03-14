package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.KeyboardController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;

public class KeyboardButton extends Button<KeyboardController> {
    private final KeyboardButtonType keyboardButtonType;
    private final Text text;
    private char mChar;

    public KeyboardButton(char c, int n, ButtonType keyboardButtonType) {
        super(ResourceManager.getInstance().keyboardButtons.get(n), keyboardButtonType, true);
        mChar = c;
        if (c == '0') setId(6);
        String string = "";
        if (c == '&') {
            string = "SPACE";
            this.keyboardButtonType = KeyboardButtonType.SPACE;
        } else if (c == '#') {
            string = "SHIFT";
            this.keyboardButtonType = KeyboardButtonType.SHIFT;
        } else if (c == '@') {
            string = "DEL";
            this.keyboardButtonType = KeyboardButtonType.DELETE;
        } else {
            string = String.valueOf(c);
            this.keyboardButtonType = KeyboardButtonType.CHARACTER;
        }
        text = new Text(string, 1);
        text.setPosition(getWidth() / 2 - text.getWidth() / 2, getHeight() / 2);
        addElement(text);
    }

    public Text getText() {
        return text;
    }

    public KeyboardButtonType getKeyboardButtonType() {
        return keyboardButtonType;
    }

    public char getChar() {
        return mChar;
    }

    public void setChar(char character) {
        mChar = character;
    }

    public enum KeyboardButtonType {
        CHARACTER,
        SPACE,
        DELETE,
        SHIFT
    }
}
