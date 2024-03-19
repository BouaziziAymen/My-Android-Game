package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.view.inputs.Button;

public class DragField extends ItemField {

    private final Button<ItemWindowController> dragOptionsButton;
    private final Button<ItemWindowController> itemRemoveButton;

    public DragField(int primaryKey, int modelId, ItemWindowController controller) {
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
                        controller.onDragRemoveButtonClicked(DragField.this);
                    }
                });
        this.setHeight(itemRemoveButton.getHeight());

        dragOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(dragOptionsButton);
        addToLayout(itemRemoveButton);
        dragOptionsButton.setBehavior(
                new ButtonBehavior<ItemWindowController>(controller, dragOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onDragOptionClicked(DragField.this);
                    }
                });
    }

    @Override
    public void showFields() {
        super.showFields();
        this.itemRemoveButton.setVisible(true);
        this.dragOptionsButton.setVisible(true);
        this.itemRemoveButton.setGone(false);
        this.dragOptionsButton.setGone(false);
    }

    @Override
    public void hideFields() {
        super.hideFields();
        this.itemRemoveButton.setGone(true);
        this.dragOptionsButton.setGone(true);
    }
}
