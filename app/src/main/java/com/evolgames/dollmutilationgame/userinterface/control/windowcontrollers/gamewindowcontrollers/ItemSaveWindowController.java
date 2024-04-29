package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.dollmutilationgame.entities.properties.ToolProperties;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;

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
        }, ResourceManager.getInstance().getString(R.string.name_already_exists_validation_message));
        TitledTextField<ItemSaveWindowController> titledTextField = new TitledTextField<>(ResourceManager.getInstance().getString(R.string.item_name_title), 20,0);
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
