package com.evolgames.userinterface.view.inputs;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.view.basics.Text;

public class KeyboardButton extends Button<KeyboardController> {
    private final KeyboardButtonType keyboardButtonType;
    private char mChar;
    private final Text text;

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

    public enum KeyboardButtonType{
    CHARACTER,SPACE,DELETE,SHIFT
    }

    public KeyboardButton(char c, int n, ButtonType keyboardButtonType) {
        super(ResourceManager.getInstance().keyboardButtons.get(n), keyboardButtonType, true);
        mChar = c;
        if(c=='0') setId(6);
        String string="";
        if(c=='&'){string="SPACE";this.keyboardButtonType = KeyboardButtonType.SPACE;}
        else if(c=='#'){string="SHIFT";this.keyboardButtonType = KeyboardButtonType.SHIFT;}
        else if(c=='@'){string="DEL";this.keyboardButtonType = KeyboardButtonType.DELETE;}
        else {string=""+c;this.keyboardButtonType = KeyboardButtonType.CHARACTER;}
        text = new Text(string,1);
        text.setPosition(getWidth()/2-text.getWidth()/2,getHeight()/2);
        addElement(text);
    }

}
