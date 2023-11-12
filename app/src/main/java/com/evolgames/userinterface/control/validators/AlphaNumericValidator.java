package com.evolgames.userinterface.control.validators;

import com.evolgames.gameengine.R;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.view.inputs.Keyboard;

public class AlphaNumericValidator extends TextFieldValidator {

  private final String regex;
  private final int minLength;

  public AlphaNumericValidator(int pLength, int minLength) {
    super(pLength);
    this.inputType = Keyboard.KeyboardType.AlphaNumeric;
    regex = "^[a-zA-z_]+[a-zA-Z0-9+ ]*";
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
    if (!valid) errorMessage = "Too short!";
    return valid;
  }
}
