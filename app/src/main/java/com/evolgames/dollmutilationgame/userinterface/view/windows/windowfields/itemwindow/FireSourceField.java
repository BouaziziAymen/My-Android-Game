package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;

public class FireSourceField extends ItemField {

    private final Button<ItemWindowController> fireSourceOptionsButton;
    private final Button<ItemWindowController> itemRemoveButton;

    public FireSourceField(int primaryKey, int modelId, ItemWindowController controller) {
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
                        controller.onFireSourceRemoveButtonClicked(FireSourceField.this);
                    }
                });
        this.setHeight(itemRemoveButton.getHeight());

        fireSourceOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(fireSourceOptionsButton);
        addToLayout(itemRemoveButton);
        fireSourceOptionsButton.setBehavior(
                new ButtonBehavior<ItemWindowController>(controller, fireSourceOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onFireSourceOptionClicked(FireSourceField.this);
                    }
                });
    }

    @Override
    public void showFields() {
        super.showFields();
        this.itemRemoveButton.setVisible(true);
        this.fireSourceOptionsButton.setVisible(true);
        this.itemRemoveButton.setGone(false);
        this.fireSourceOptionsButton.setGone(false);
    }

    @Override
    public void hideFields() {
        super.hideFields();
        this.itemRemoveButton.setGone(true);
        this.fireSourceOptionsButton.setGone(true);
    }
}
