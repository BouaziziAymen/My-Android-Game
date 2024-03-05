package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class ProjectileField extends ItemField {
  private final Button<ItemWindowController> itemRemoveButton;
  private final Button<ItemWindowController> projectileOptionsButton;

  public ProjectileField(int primaryKey, int modelId, ItemWindowController controller) {
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
            controller.onProjectileRemoveButtonReleased(ProjectileField.this);
          }
        });
    this.setHeight(itemRemoveButton.getHeight());

    projectileOptionsButton =
        new Button<>(
            ResourceManager.getInstance().smallOptionsTextureRegion,
            Button.ButtonType.OneClick,
            true);
    addToLayout(projectileOptionsButton);
    addToLayout(itemRemoveButton);
    projectileOptionsButton.setBehavior(
        new ButtonBehavior<ItemWindowController>(controller, projectileOptionsButton) {
          @Override
          public void informControllerButtonClicked() {}

          @Override
          public void informControllerButtonReleased() {
            controller.onProjectileSettingsButtonReleased(ProjectileField.this);
          }
        });
  }

  @Override
  public void showFields() {
    super.showFields();
    this.itemRemoveButton.setVisible(true);
    this.projectileOptionsButton.setVisible(true);
  }

  @Override
  public void hideFields() {
    super.hideFields();
    this.itemRemoveButton.setVisible(false);
    this.projectileOptionsButton.setVisible(false);
  }
}
