package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class LiquidSourceField extends ItemField {

    private final Button<ItemWindowController> liquidSourceOptionsButton;
    private final Button<ItemWindowController> itemRemoveButton;

    public LiquidSourceField(int primaryKey, int modelId, ItemWindowController controller) {
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
                        controller.onLiquidSourceRemoveButtonClicked(LiquidSourceField.this);
                    }
                });
        this.setHeight(itemRemoveButton.getHeight());

        liquidSourceOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(liquidSourceOptionsButton);
        addToLayout(itemRemoveButton);
        liquidSourceOptionsButton.setBehavior(
                new ButtonBehavior<ItemWindowController>(controller, liquidSourceOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {}

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onLiquidSourceOptionClicked(LiquidSourceField.this);
                    }
                });
    }

    @Override
    public void showFields() {
        super.showFields();
        this.itemRemoveButton.setVisible(true);
        this.liquidSourceOptionsButton.setVisible(true);
    }

    @Override
    public void hideFields() {
        super.hideFields();
        this.itemRemoveButton.setVisible(false);
        this.liquidSourceOptionsButton.setVisible(false);
    }
}
