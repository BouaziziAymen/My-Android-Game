package com.evolgames.dollmutilationgame.userinterface.view.windows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds.CircularBounds;

public class AdvancedWindow<C extends AdvancedWindowController> extends Window {
    private static final float SLOT1_SHIFT = 18f;
    private static final float SLOT2_SHIFT = 18f;
    private static final float SLOT3_SHIFT = 14f;
    private static final float SLOT_VERTICAL_SHIFT = 14.5f;
    private static final float FOLD_UNFOLD_BUTTON_RAY = 12f;
    private final C controller;
    private Button<AdvancedWindowController<?>> foldButton;
    private Button<AdvancedWindowController<?>> closeButton;

    public AdvancedWindow(
            float pX, float pY, int rows, int columns, C controller, boolean hasPadding) {
        super(pX, pY, rows, Math.max(columns, 2), hasPadding, 2);
        // adding the buttons
        controller.setWindow(this);
        this.controller = controller;
        if (hasPadding) {
            foldButton =
                    new Button<>(
                            ResourceManager.getInstance().windowFoldTextureRegion,
                            Button.ButtonType.Selector,
                            false);
            ButtonBehavior<AdvancedWindowController<?>> foldButtonBehavior =
                    new ButtonBehavior<AdvancedWindowController<?>>(controller, foldButton) {

                        @Override
                        public void informControllerButtonClicked() {
                            controller.onFoldButtonClicked();
                        }

                        @Override
                        public void informControllerButtonReleased() {
                            controller.onFoldButtonReleased();
                        }
                    };

            addToSlot(foldButton, 0);
            foldButton.setBehavior(foldButtonBehavior);
            foldButton.setState(Button.State.PRESSED);
            foldButton.setBounds(new CircularBounds(foldButton, FOLD_UNFOLD_BUTTON_RAY));
            foldButton.setId(7);
            closeButton =
                    new Button<>(
                            ResourceManager.getInstance().windowCloseTextureRegion,
                            Button.ButtonType.OneClick,
                            false);
            addToSlot(closeButton, 1);
            ButtonBehavior<AdvancedWindowController<?>> closeButtonBehavior =
                    new ButtonBehavior<AdvancedWindowController<?>>(controller, closeButton) {
                        @Override
                        public void informControllerButtonClicked() {
                        }

                        @Override
                        public void informControllerButtonReleased() {
                            controller.onCloseButtonClicked();
                        }
                    };
            closeButton.setBehavior(closeButtonBehavior);
            closeButton.setBounds(new CircularBounds(closeButton, FOLD_UNFOLD_BUTTON_RAY));
            closeButton.setDepth(20);
            foldButton.setDepth(20);
        }
    }

    private void addToSlot(Button<?> button, int slotNumber) {
        if (slotNumber >= mNumSlots) return;
        float dx;
        if (slotNumber == 0 && mNumSlots == mColumns) dx = SLOT1_SHIFT;
        else if (slotNumber == mNumSlots - 1) dx = SLOT3_SHIFT;
        else dx = SLOT2_SHIFT;
        float x = getSlotXPositions()[slotNumber] + dx - button.getWidth() / 2;
        float y = mRows * TILE_SIDE - mRows + SLOT_VERTICAL_SHIFT - button.getHeight() / 2;
        button.setPosition(x, y);
        addElement(button);
    }

    public C getController() {
        return controller;
    }

    public void disableFoldButton() {
        foldButton.updateState(Button.State.DISABLED);
    }

    public void disableCloseButton() {
        closeButton.updateState(Button.State.DISABLED);
    }

    public void enableFoldButton() {
        foldButton.updateState(Button.State.PRESSED);
    }

    public void enableCloseButton() {
        closeButton.updateState(Button.State.NORMAL);
    }
}
