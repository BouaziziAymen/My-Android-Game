package com.evolgames.dollmutilationgame.userinterface.control.validators;


import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.actions.Condition;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.gameengine.R;

public class NumericValidator extends TextFieldValidator {
    private final boolean hasLengthLimit;
    private final float lowerLimit;
    private final float upperLimit;
    private final String regex;
    private float value;
    private Condition validationCondition;

    public NumericValidator(
            boolean hasNegative,
            boolean hasFraction,
            float lowerLimit,
            float upperLimit,
            int baseNumber,
            int fractionNumber) {
        super(0);
        hasLengthLimit = false;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.inputType = Keyboard.KeyboardType.Numeric;
        regex =
                "^("
                        + (hasNegative ? "[-]?" : "")
                        + "\\d{0,"
                        + baseNumber
                        + "}"
                        + (hasFraction ? "\\.?\\d{0," + fractionNumber + "}" : "")
                        + ")$";
    }

    public boolean isHasLengthLimit() {
        return hasLengthLimit;
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
            if (string.length() > length) {
                errorMessage = ResourceManager.getInstance().getString(R.string.validator_too_long);
                return false;
            }
        }

        boolean result = string.matches(regex);
        if (!result) {
            errorMessage = ResourceManager.getInstance().getString(R.string.validator_invalid_number);
        }
        return result;
    }

    @Override
    public boolean validate(String string) {
        try {
            value = Float.parseFloat(string);
            if (value > upperLimit || value < lowerLimit) {
                errorMessage =
                        ResourceManager.getInstance()
                                .activity
                                .getString(R.string.validator_float_range, lowerLimit, upperLimit);
                return false;
            }
            if (validationCondition != null) return validationCondition.isCondition(value);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
