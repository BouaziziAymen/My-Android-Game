package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.TertiaryLinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;

public class DecorationField extends TertiaryLinearLayout {

    private final ButtonWithText<LayerWindowController> mDecorationControl;
    private final Button<LayerWindowController> decorationRemoveButton;
    private final Button<LayerWindowController> decorationOptionsButton;
    private final Button<LayerWindowController> decorationShowHideButton;
    private boolean visibleFields;

    public DecorationField(
            int bodyFieldKey,
            int layerFieldKey,
            int decorationFieldKey,
            LayerWindowController controller) {
        super(bodyFieldKey, layerFieldKey, decorationFieldKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mDecorationControl =
                new ButtonWithText<>(
                        "",
                        2,
                        ResourceManager.getInstance().largeLampTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        addToLayout(mDecorationControl);

        mDecorationControl.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, mDecorationControl) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onTertiaryButtonClicked(DecorationField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onTertiaryButtonReleased(DecorationField.this);
                    }
                });
        decorationOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(decorationOptionsButton);
        decorationOptionsButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, decorationOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onDecorationSettingsButtonReleased(DecorationField.this);
                    }
                });

        decorationRemoveButton =
                new Button<>(
                        ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(decorationRemoveButton);
        decorationRemoveButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, decorationRemoveButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onDecorationRemoveButtonReleased(DecorationField.this);
                    }
                });

        decorationShowHideButton =
                new Button<>(
                        ResourceManager.getInstance().showHideTextureRegion, Button.ButtonType.Selector, true);
        addToLayout(decorationShowHideButton);
        decorationShowHideButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, decorationShowHideButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onDecorationShowHideButtonClicked(DecorationField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onDecorationShowHideButtonReleased(DecorationField.this);
                    }
                });
        decorationShowHideButton.updateState(Button.State.PRESSED);

        this.setHeight(decorationRemoveButton.getHeight());
    }

    public void setText(String text) {
        mDecorationControl.setTitle(text);
    }

    public ButtonWithText<?> getDecorationControl() {
        return mDecorationControl;
    }

    public void showFields() {
        this.visibleFields = true;
        this.decorationOptionsButton.setVisible(true);
        this.decorationRemoveButton.setVisible(true);
        this.decorationShowHideButton.setVisible(true);

        this.decorationOptionsButton.setGone(false);
        this.decorationRemoveButton.setGone(false);
        this.decorationShowHideButton.setGone(false);
    }

    public void hideFields() {
        this.visibleFields = false;
        this.decorationOptionsButton.setGone(true);
        this.decorationRemoveButton.setGone(true);
        this.decorationShowHideButton.setGone(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            if (visibleFields) {
                this.showFields();
            } else {
                this.hideFields();
            }
        }
    }
}
