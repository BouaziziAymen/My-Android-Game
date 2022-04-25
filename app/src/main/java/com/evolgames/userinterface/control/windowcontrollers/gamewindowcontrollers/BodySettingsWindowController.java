package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

public class BodySettingsWindowController extends SettingsWindowController {

    private final LayerWindowController layerWindowController;
    private TextField<BodySettingsWindowController> bodyNameTextField;
    private AlphaNumericValidator bodyNameValidator = new AlphaNumericValidator(8,5);

    public BodySettingsWindowController(LayerWindowController layerWindowController, UserInterface userInterface, KeyboardController keyboardController) {
        super(userInterface);
        this.keyboardController = keyboardController;
        this.layerWindowController = layerWindowController;
    }

    void onModelUpdated(ProperModel model) {
        super.onModelUpdated(model);
        setBodyName(model.getModelName());
    }

    private void setBodyName(String bodyName) {
        bodyNameTextField.getBehavior().setTextValidated(bodyName);
    }


    @Override
    public void init() {
        window.setVisible(false);
        SectionField<BodySettingsWindowController> sectionField = new SectionField<>(1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<BodySettingsWindowController> bodyNameField = new TitledTextField<>("Body Name:", 10);
        bodyNameTextField = bodyNameField.getAttachment();

        bodyNameTextField.setBehavior(new TextFieldBehavior<BodySettingsWindowController>(this, bodyNameTextField, Keyboard.KeyboardType.AlphaNumeric, bodyNameValidator) {
            @Override
            protected void informControllerTextFieldTapped() {
                BodySettingsWindowController.super.onTextFieldTapped(bodyNameTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                BodySettingsWindowController.super.onTextFieldReleased(bodyNameTextField);
            }
        });
        bodyNameTextField.getBehavior().setReleaseAction(() -> model.setModelName(bodyNameTextField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(bodyNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);

        SectionField<BodySettingsWindowController> categorySection = new SectionField<>(2, "Category", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(categorySection);

        BodyModel.allCategories.forEach(e->{
            ButtonWithText<BodySettingsWindowController> categoryButton = new ButtonWithText<>
                    (e.toString(), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);

            SimpleSecondary<ButtonWithText> categoryField = new SimpleSecondary<>(2, BodyModel.allCategories.indexOf(e), categoryButton);
            window.addSecondary(categoryField);
            categoryButton.setBehavior(new ButtonBehavior<BodySettingsWindowController>(this, categoryButton) {
                @Override
                public void informControllerButtonClicked() {
                    onCategoryButtonPressed(categoryField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });
        });





        window.createScroller();
        window.getLayout().updateLayout();
    }

    private void onCategoryButtonPressed(SimpleSecondary<ButtonWithText> categoryField) {
        super.onSecondaryButtonClicked(categoryField);
        int size = window.getLayout().getSecondariesSize(2);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
            if (categoryField != other) {
                Element main = other.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
        ((BodyModel)model).setCategory(BodyModel.allCategories.get(categoryField.getSecondaryKey()));
    }


    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        layerWindowController.onResume();
        BodyModel bodyModel = (BodyModel) model;
        layerWindowController.changeBodyName(model.getModelName(), bodyModel.getBodyId());
    }

}
