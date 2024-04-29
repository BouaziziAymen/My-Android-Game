package com.evolgames.dollmutilationgame.userinterface.control.validators;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.dollmutilationgame.R;

public class AlphaNumericValidator extends TextFieldValidator {

    private final String regex;
    private final int minLength;

    public AlphaNumericValidator(int pLength, int minLength) {
        super(pLength);
        this.inputType = Keyboard.KeyboardType.AlphaNumeric;
        regex = "^[a-zA-Z_]+[a-zA-Z0-9#+. ]*";
        this.minLength = minLength;
    }

    @Override
    public boolean checkText(String string) {
        if (string == null) return false;
        if (string.length() == 0) return true;
        if (string.length() > length) {
            errorMessage = ResourceManager.getInstance().getString(R.string.validator_too_long);
            return false;
        }
        boolean result = string.matches(regex);
        if (!result) {
            errorMessage = ResourceManager.getInstance().getString(R.string.validator_invalid_name);
        }
        return result;
    }

    @Override
    public boolean validate(String string) {
        boolean valid = (string.length() >= minLength);
        if (!valid) {
            errorMessage = "Too short!";
        } else if (condition != null) {
            if (!condition.test(string)) {
                valid = false;
                errorMessage = conditionMessage;
            }
        }

        return valid;
    }
}
