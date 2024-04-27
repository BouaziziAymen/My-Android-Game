package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;

public class SpecialPointField extends ItemField {
    private final Button<ItemWindowController> itemRemoveButton;

    public SpecialPointField(int primaryKey, int modelId, ItemWindowController controller) {
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
                        controller.onSpecialPointRemoveButtonReleased(SpecialPointField.this);
                    }
                });
        this.setHeight(itemRemoveButton.getHeight());
        addToLayout(itemRemoveButton);
    }

    @Override
    public void showFields() {
        super.showFields();
        this.itemRemoveButton.setVisible(true);
        this.itemRemoveButton.setGone(false);
    }

    @Override
    public void hideFields() {
        super.hideFields();
        this.itemRemoveButton.setGone(true);
    }
}
