package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class BombField extends ItemField {
    private final Button<ItemWindowController> itemRemoveButton;
    private final Button<ItemWindowController> bombOptionsButton;

    public BombField(int primaryKey, int modelId, ItemWindowController controller) {
        super(primaryKey, modelId, controller);
        itemRemoveButton =
                new Button<>(
                        ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);

        itemRemoveButton.setBehavior(
                new ButtonBehavior<ItemWindowController>(controller, itemRemoveButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onBombRemoveButtonReleased(BombField.this);
                    }
                });
        this.setHeight(itemRemoveButton.getHeight());

        bombOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(bombOptionsButton);
        addToLayout(itemRemoveButton);
        bombOptionsButton.setBehavior(
                new ButtonBehavior<ItemWindowController>(controller, bombOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onBombOptionsButtonReleased(BombField.this);
                    }
                });
    }

    @Override
    public void showFields() {
        super.showFields();
        this.itemRemoveButton.setVisible(true);
        this.bombOptionsButton.setVisible(true);
        this.itemRemoveButton.setGone(false);
        this.bombOptionsButton.setGone(false);
    }

    @Override
    public void hideFields() {
        super.hideFields();
        this.itemRemoveButton.setGone(true);
        this.bombOptionsButton.setGone(true);
    }
}
