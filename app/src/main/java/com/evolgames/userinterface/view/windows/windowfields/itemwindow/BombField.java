package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class BombField extends ItemField {
    public BombField(int primaryKey, int secondaryKey, ItemWindowController controller) {
        super(primaryKey, secondaryKey, controller);
        Button<ItemWindowController> itemRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);

        itemRemoveButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, itemRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onBombRemoveButtonClicked(BombField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBombRemoveButtonReleased(BombField.this);
            }
        });
        this.setHeight(itemRemoveButton.getHeight());


        Button<ItemWindowController> bombOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(bombOptionsButton);
        addToLayout(itemRemoveButton);
        bombOptionsButton.setBehavior(new ButtonBehavior<ItemWindowController>(controller, bombOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBombOptionsButtonReleased(BombField.this);
            }
        });

    }

}
