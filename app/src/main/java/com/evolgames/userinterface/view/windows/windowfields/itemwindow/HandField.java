package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class HandField extends ItemField {
    public HandField(int primaryKey, int secondaryKey, ItemWindowController controller) {
        super(primaryKey, secondaryKey, controller);

        Button<ItemWindowController> itemRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);

        itemRemoveButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, itemRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onHandRemoveButtonClicked(HandField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onHandRemoveButtonReleased(HandField.this);
            }
        });
        this.setHeight(itemRemoveButton.getHeight());


        Button<ItemWindowController> handOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(handOptionsButton);
        addToLayout(itemRemoveButton);
        handOptionsButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, handOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }
            @Override
            public void informControllerButtonReleased() {
                controller.onHandSettingsButtonReleased(HandField.this);
            }
        });
    }
}
