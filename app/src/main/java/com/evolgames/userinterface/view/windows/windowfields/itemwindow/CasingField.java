package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class CasingField extends ItemField {
  private final Button<ItemWindowController> ammoOptionsButton;
  private final Button<ItemWindowController> itemRemoveButton;

  public CasingField(int primaryKey, int modelId, ItemWindowController controller) {
    super(primaryKey, modelId, controller);
    itemRemoveButton =
        new Button<>(
            ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);

    itemRemoveButton.setBehavior(
        new ButtonBehavior<ItemWindowController>(controller, itemRemoveButton) {
          @Override
          public void informControllerButtonClicked() {}

          @Override
          public void informControllerButtonReleased() {
            controller.onAmmoRemoveButtonReleased(CasingField.this);
          }
        });
    this.setHeight(itemRemoveButton.getHeight());

    ammoOptionsButton =
        new Button<>(
            ResourceManager.getInstance().smallOptionsTextureRegion,
            Button.ButtonType.OneClick,
            true);
    addToLayout(ammoOptionsButton);
    addToLayout(itemRemoveButton);
    ammoOptionsButton.setBehavior(
        new ButtonBehavior<ItemWindowController>(controller, ammoOptionsButton) {
          @Override
          public void informControllerButtonClicked() {}

          @Override
          public void informControllerButtonReleased() {
            controller.onAmmoOptionButtonReleased(CasingField.this);
          }
        });
  }

  @Override
  public void showFields() {
    super.showFields();
    this.itemRemoveButton.setVisible(true);
    this.ammoOptionsButton.setVisible(true);
  }

  @Override
  public void hideFields() {
    super.hideFields();
    this.itemRemoveButton.setVisible(false);
    this.ammoOptionsButton.setVisible(false);
  }
}
