package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class AmmoField extends ItemField {
    public AmmoField(int primaryKey, int secondaryKey, ItemWindowController controller) {
        super(primaryKey, secondaryKey, controller);
        Button<ItemWindowController> itemRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);

        itemRemoveButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, itemRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onAmmoRemoveButtonClicked(AmmoField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onAmmoRemoveButtonReleased(AmmoField.this);
            }
        });
        this.setHeight(itemRemoveButton.getHeight());


        Button<ItemWindowController> targetOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(targetOptionsButton);
        addToLayout(itemRemoveButton);
        targetOptionsButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, targetOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onAmmoButtonReleased(AmmoField.this);
            }
        });

    }

}
