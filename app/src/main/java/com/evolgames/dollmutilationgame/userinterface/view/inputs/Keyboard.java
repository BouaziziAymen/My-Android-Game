package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.userinterface.control.KeyboardController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Container;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;

import java.util.ArrayList;

public class Keyboard extends Container {
    private static final float BUTTON_SIDE = 26f;
    private final KeyboardController mController;
    private final Container numericButtons;
    private final Container alphaNumericButtons;
    private final ArrayList<KeyboardButton> buttons = new ArrayList<>();
    private KeyboardType currentType;

    public Keyboard(float pX, float pY, KeyboardController controller) {
        super(pX, pY);
        numericButtons = new Container();
        alphaNumericButtons = new Container();
        mController = controller;
        load(KeyboardType.AlphaNumeric, alphaNumericButtons);
        load(KeyboardType.Numeric, numericButtons);
        addElement(alphaNumericButtons);
        addElement(numericButtons);
        setCurrentType(KeyboardType.Numeric);
        setVisible(false);
    }

    @Override
    public float getWidth() {
        if (currentType == KeyboardType.Numeric) return numericButtons.getWidth();
        else return alphaNumericButtons.getWidth();
    }

    @Override
    public float getHeight() {
        if (currentType == KeyboardType.Numeric) return numericButtons.getHeight();
        else return alphaNumericButtons.getHeight();
    }

    public void setCurrentType(KeyboardType currentType) {
        this.currentType = currentType;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            if (currentType == KeyboardType.Numeric) {
                numericButtons.setVisible(true);
                alphaNumericButtons.setVisible(false);
            } else {
                numericButtons.setVisible(false);
                alphaNumericButtons.setVisible(true);
            }
        } else {
            numericButtons.setVisible(false);
            alphaNumericButtons.setVisible(false);
        }
    }

    public void load(KeyboardType keyboardType, Container container) {
        float width = 0;
        float height = 0;
        String[] strings =
                keyboardType == KeyboardType.AlphaNumeric
                        ? new String[]{"0123456789", "azertyuiop", "qsdfghjklm", "#wxcvbn@", "._&+-"}
                        : new String[]{"123", "456", "789", ".0-", "@"};

        float y = BUTTON_SIDE * 4;
        for (int j = 0; j < strings.length; j++) {
            LinearLayout lineLayout =
                    new LinearLayout(
                            j == 4
                                    ? keyboardType == KeyboardType.AlphaNumeric
                                    ? 1.5f * BUTTON_SIDE
                                    : 0.5f * BUTTON_SIDE
                                    : 0,
                            y,
                            LinearLayout.Direction.Horizontal,
                            -1);
            for (int i = 0; i < strings[j].length(); i++) {
                Button.ButtonType type = Button.ButtonType.OneClick;
                int n;
                char character = strings[j].charAt(i);
                if (character == '&') {
                    n = 3;
                } // SPACE
                else if (character == '@') {
                    n = 4;
                } // DELETE
                else if (character == '#') {
                    n = 4;
                    type = Button.ButtonType.Selector;
                } // SHIFT
                else {
                    if (i == 0 || character == 'w' || character == '+') n = 0;
                    else if (i == strings[j].length() - 1 || character == 'n' || character == ',')
                        n = 2;
                    else n = 1;
                }

                KeyboardButton button = new KeyboardButton(character, n, type);
                ButtonBehavior<KeyboardController> buttonBehavior =
                        new ButtonBehavior<KeyboardController>(mController, button) {
                            @Override
                            public void informControllerButtonClicked() {
                                if (mController.isBound())
                                    switch (button.getKeyboardButtonType()) {
                                        case CHARACTER:
                                            mController.onCharacterTyped(button.getChar());
                                            break;
                                        case SPACE:
                                            mController.onSpaceClicked();
                                            break;
                                        case DELETE:
                                            mController.onDeleteClicked();
                                            break;
                                        case SHIFT:
                                            mController.onShiftClicked();
                                            break;
                                    }
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                if (mController.isBound())
                                    switch (button.getKeyboardButtonType()) {
                                        case CHARACTER:
                                            break;
                                        case SPACE:
                                            break;
                                        case DELETE:
                                            break;
                                        case SHIFT:
                                            mController.onShiftReleased();
                                            break;
                                    }
                            }
                        };
                button.setBehavior(buttonBehavior);
                lineLayout.addToLayout(button);
                buttons.add(button);
            }
            container.addElement(lineLayout);
            if (lineLayout.getWidth() > width) {
                width = lineLayout.getWidth();
            }
            height += lineLayout.getHeight();
            y -= (BUTTON_SIDE - 1);
        }
        container.setWidth(width);
        container.setHeight(height);
    }

    public ArrayList<KeyboardButton> getButtons() {
        return buttons;
    }

    public enum KeyboardType {
        Numeric,
        AlphaNumeric
    }
}
