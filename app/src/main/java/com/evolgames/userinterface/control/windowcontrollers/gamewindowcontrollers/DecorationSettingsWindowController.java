package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import android.util.Log;
import com.evolgames.entities.properties.ColoredProperties;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.TextFieldValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.windows.windowfields.ColorSlot;
import com.evolgames.userinterface.view.windows.windowfields.TitledField;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;
import org.andengine.util.adt.color.Color;

public class DecorationSettingsWindowController
    extends SettingsWindowController<DecorationProperties> {
  private final TextFieldValidator decorationNameValidator = new AlphaNumericValidator(16, 5);
  private TextField<DecorationSettingsWindowController> decorationNameTextField;
    private ColorSlot colorSlotForDecoration;
  private ColorSelectorWindowController colorSelectorController;

  public void setColorSelectorController(ColorSelectorWindowController colorSelectorController) {
    this.colorSelectorController = colorSelectorController;
  }

  @Override
  public void init() {
    TitledTextField<DecorationSettingsWindowController> decorationNameField =
        new TitledTextField<>("Decoration Name:", 12);
    decorationNameTextField = decorationNameField.getAttachment();

    decorationNameTextField.setBehavior(
        new TextFieldBehavior<DecorationSettingsWindowController>(
            this,
            decorationNameTextField,
            Keyboard.KeyboardType.AlphaNumeric,
            decorationNameValidator) {
          @Override
          protected void informControllerTextFieldTapped() {
            DecorationSettingsWindowController.super.onTextFieldTapped(decorationNameTextField);
          }

          @Override
          protected void informControllerTextFieldReleased() {
            DecorationSettingsWindowController.super.onTextFieldReleased(decorationNameTextField);
          }
        });
    decorationNameTextField
        .getBehavior()
        .setReleaseAction(
            new Action() {
              @Override
              public void performAction() {
                model.setModelName(decorationNameTextField.getTextString());
              }
            });

    SimplePrimary<TitledTextField<?>> secondaryElement1 =
        new SimplePrimary<>(0, decorationNameField);
    window.addPrimary(secondaryElement1);
    // create the color selection field

    colorSlotForDecoration = new ColorSlot();
    Button<DecorationSettingsWindowController> colorSelectionButton =
        new Button<>(
            ResourceManager.getInstance().smallButtonTextureRegion,
            Button.ButtonType.OneClick,
            true);
    LinearLayout linearLayout = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
    linearLayout.addToLayout(colorSelectionButton);
    linearLayout.addToLayout(colorSlotForDecoration);
    TitledField<LinearLayout> colorSelectionField =
        new TitledField<>("Select Color:", linearLayout);
    colorSelectionButton.setBehavior(
        new ButtonBehavior<DecorationSettingsWindowController>(this, colorSelectionButton) {
          @Override
          public void informControllerButtonClicked() {}

          @Override
          public void informControllerButtonReleased() {
              ColoredProperties props = (ColoredProperties) model.getProperties();
            if (colorSelectorController != null) {
              colorSelectorController.bindProperties(
               props.getDefaultColor(),props.getColorSquareId());
              colorSelectorController.setAcceptAction(
                  new Action() {
                    @Override
                    public void performAction() {
                      setDecorationColorSlot();
                    }
                  });
              Log.e("testing", "open Window");
              colorSelectorController.openWindow();
            }
          }
        });
    SimplePrimary<TitledField> secondaryElement2 = new SimplePrimary<>(2, colorSelectionField);
    window.addPrimary(secondaryElement2);
    updateLayout();
  }

  private void setDecorationColorSlot() {
    Color color = ((ColoredProperties) model.getProperties()).getDefaultColor();
    colorSlotForDecoration.setColor(color.getRed(), color.getGreen(), color.getBlue());
  }

  @Override
  void onModelUpdated(ProperModel<DecorationProperties> model) {
    super.onModelUpdated(model);
    setDecorationColorSlot();
    setDecorationName(model.getModelName());
  }

  private void setDecorationName(String layerName) {
    decorationNameTextField.getBehavior().setTextValidated(layerName);
  }

  @Override
  public void onCancelSettings() {
    super.onCancelSettings();
  }

  @Override
  public void onSubmitSettings() {
    super.onSubmitSettings();
    editorUserInterface.getToolModel().updateMesh();
  }
}
