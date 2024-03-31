package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.properties.ToolProperties;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSaveWindowController extends SettingsWindowController<ToolProperties> {

    private final AlphaNumericValidator itemNameValidator = new AlphaNumericValidator(20, 2);
    private TextField<ItemSaveWindowController> titleTextField;

    @Override
    public void init() {
        super.init();
        itemNameValidator.setCondition((input) -> {
            List<ItemMetaData> items = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            String[] itemNames = items.stream().map(ItemMetaData::getName).toArray(String[]::new);
            boolean nameExists = Arrays.stream(itemNames).map(String::toLowerCase).anyMatch(e -> e.equals(input.toLowerCase()));
            return !nameExists;
        }, "Name already exists.");
        TitledTextField<ItemSaveWindowController> titledTextField = new TitledTextField<>("Title", 20);
        this.titleTextField = titledTextField.getAttachment();
        titleTextField.setBehavior(
                new TextFieldBehavior<ItemSaveWindowController>(
                        this,
                        titledTextField.getAttachment(),
                        Keyboard.KeyboardType.AlphaNumeric,
                        itemNameValidator,
                        false) {
                    @Override
                    protected void informControllerTextFieldTapped() {
                        ItemSaveWindowController.super.onTextFieldTapped(titledTextField.getAttachment());
                    }

                    @Override
                    protected void informControllerTextFieldReleased() {
                        ItemSaveWindowController.super.onTextFieldReleased(titledTextField.getAttachment());
                    }
                });
        FieldWithError titleFieldWithError = new FieldWithError(titledTextField);
        SimplePrimary<FieldWithError> titleField = new SimplePrimary<>(0, titleFieldWithError);
        window.addPrimary(titleField);
        titleTextField
                .getBehavior()
                .setReleaseAction(() -> ((ToolModel) model).getProperties().setToolName(titleTextField.getTextString()));

        updateLayout();
        window.createScroller();
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
    }

    @Override
    public void onModelUpdated(ProperModel<ToolProperties> model) {
        super.onModelUpdated(model);
        titleTextField.getBehavior().setTextValidated(model.getProperties().getToolName());
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        this.editorUserInterface.saveToolModel();
        ResourceManager.getInstance().activity.getUiController().onItemSaved();
    }
}
