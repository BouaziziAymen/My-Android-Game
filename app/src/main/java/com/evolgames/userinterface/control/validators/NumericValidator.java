package com.evolgames.userinterface.control.validators;

import android.util.Log;

import com.evolgames.gameengine.R;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.view.inputs.Keyboard;

public class NumericValidator extends TextFieldValidator {
    private final boolean hasLengthLimit;
    private final float lowerLimit;
    private final float upperLimit;
    private float value;
    private final String regex;
    private Condition validationCondition;

    public boolean isHasLengthLimit() {
        return hasLengthLimit;
    }

    public NumericValidator(boolean hasNegative, boolean hasFraction, float lowerLimit, float upperLimit, int baseNumber, int fractionNumber) {
        super(0);
        hasLengthLimit = false;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.inputType = Keyboard.KeyboardType.Numeric;
        regex = "^(" + (hasNegative ? "[-]?" : "") + "\\d{0," + baseNumber + "}" + (hasFraction ? "\\.?\\d{0," + fractionNumber + "}" : "") + ")$";
    }

    public void setValidationCondition(Condition validationCondition) {
        this.validationCondition = validationCondition;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean checkText(String string) {
        if (hasLengthLimit) {
            if (string.length() > length){
                errorMessage = ResourceManager.getInstance().getString(R.string.validator_too_long);
                return false;
            }
        }


        boolean result = string.matches(regex);
        if(!result){
            errorMessage = ResourceManager.getInstance().getString(R.string.validator_invalid_number);
        }
        return result;
    }

    @Override
    public boolean validate(String string) {
        try {
            value = Float.parseFloat(string);
            if (value > upperLimit || value < lowerLimit) {
                errorMessage = ResourceManager.getInstance().activity.getString(R.string.validator_float_range, lowerLimit, upperLimit);
                return false;
            }
            if (validationCondition != null) return validationCondition.isCondition(value);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

}
