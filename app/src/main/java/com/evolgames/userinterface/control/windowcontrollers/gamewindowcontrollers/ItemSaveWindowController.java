package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.ItemCategory;
import com.evolgames.entities.factories.ItemCategoryFactory;
import com.evolgames.entities.properties.ToolProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import java.util.ArrayList;

public class ItemSaveWindowController extends SettingsWindowController<ToolProperties> {

  private final AlphaNumericValidator itemNameValidator = new AlphaNumericValidator(12, 3);
  private TextField<ItemSaveWindowController> titleTextField;

  @Override
  public void init() {
    super.init();
    window.setVisible(false);
    TitledTextField<ItemSaveWindowController> titledTextField = new TitledTextField<>("Title", 15);
    this.titleTextField = titledTextField.getAttachment();
    titleTextField.setBehavior(
        new TextFieldBehavior<ItemSaveWindowController>(
            this,
            titledTextField.getAttachment(),
            Keyboard.KeyboardType.AlphaNumeric,
            itemNameValidator,
            true) {
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
        .setReleaseAction(() -> model.setModelName(titleTextField.getTextString()));

    SectionField<ItemSaveWindowController> categorySection =
        new SectionField<>(
            1, "Category:", ResourceManager.getInstance().mainButtonTextureRegion, this);
    window.addPrimary(categorySection);
    ArrayList<ItemCategory> categories = ItemCategoryFactory.getInstance().itemCategories;
    for (int i = 0; i < categories.size(); i++) {
      ItemCategory itemCategory = categories.get(i);
      ButtonWithText<ItemSaveWindowController> categoryButton =
          new ButtonWithText<>(
              itemCategory.getCategoryName(),
              2,
              ResourceManager.getInstance().simpleButtonTextureRegion,
              Button.ButtonType.Selector,
              true);
      SimpleSecondary<ButtonWithText<ItemSaveWindowController>> categoryField =
          new SimpleSecondary<>(1, i, categoryButton);
      window.addSecondary(categoryField);
      categoryButton.setBehavior(
          new ButtonBehavior<ItemSaveWindowController>(this, categoryButton) {
            @Override
            public void informControllerButtonClicked() {
              ItemSaveWindowController.this.onCategoryButtonClicked(categoryField);
            }

            @Override
            public void informControllerButtonReleased() {}
          });
    }
    updateLayout();
    window.createScroller();
  }

  private void onCategoryButtonClicked(
      SimpleSecondary<ButtonWithText<ItemSaveWindowController>> categoryField) {
    super.onSecondaryButtonClicked(categoryField);
    int size = window.getLayout().getSecondariesSize(1);
    for (int i = 0; i < size; i++) {
      SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(1, i);
      if (categoryField != other) {
        Element main = other.getMain();
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
        }
      }
    }
    if (model != null) {
      ItemCategory category =
          ItemCategoryFactory.getInstance().getItemCategoryByIndex(categoryField.getSecondaryKey());
      ((ToolModel) model).setToolCategory(category);
    }
  }

  @Override
  public void onCancelSettings() {
    super.onCancelSettings();
  }

  @Override
  public void onModelUpdated(ProperModel<ToolProperties> model) {
    super.onModelUpdated(model);
    titleTextField.getBehavior().setTextValidated(model.getModelName());
  }

  @Override
  public void onSubmitSettings() {
    super.onSubmitSettings();
    ItemCategory toolCategory = ((ToolModel) model).getToolCategory();
    if (toolCategory == null){
      return;
    }
    this.editorUserInterface.saveToolModel();

  }
}
