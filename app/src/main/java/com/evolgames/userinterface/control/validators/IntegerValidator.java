package com.evolgames.userinterface.control.validators;

import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.view.inputs.Keyboard;

public class IntegerValidator extends TextFieldValidator {
    private final boolean hasLengthLimit;
    private final float lowerLimit;
    private final float upperLimit;
    private final String regex;
    private float value;
    private Condition validationCondition;

    public IntegerValidator(float lowerLimit, float upperLimit) {
        super(2);
        hasLengthLimit = false;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.inputType = Keyboard.KeyboardType.Numeric;
        regex = "^(\\d*)$";
    }

    public void setValidationCondition(Condition validationCondition) {
        this.validationCondition = validationCondition;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean checkText(String string) {
        if (hasLengthLimit) if (string.length() > length) return false;
        try {
            float value = Integer.parseInt(string);
            if (value > upperLimit || (value < lowerLimit && string.length() == length))
                return false;
        } catch (Throwable ignored) {
        }
        return string.matches(regex);
    }

    @Override
    public boolean validate(String string) {
        try {
            value = Integer.parseInt(string);
            if (validationCondition != null) return validationCondition.isCondition(value);
            return !(value > upperLimit) && !(value < lowerLimit);
        } catch (Throwable t) {
            return false;
        }
    }
}
