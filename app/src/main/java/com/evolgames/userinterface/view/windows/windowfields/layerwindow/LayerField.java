package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.userinterface.view.sections.basic.SecondaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class LayerField extends SecondaryLinearLayout {
    private final ButtonWithText<LayerWindowController> mLayerControl;
    private final Button<LayerWindowController> upArrowButton;
    private final Button<LayerWindowController> downArrowButton;
    private final Button<LayerWindowController> layerOptionsButton;
    private final Button<LayerWindowController> layerAddDecorationButton;
    private final Button<LayerWindowController> layerRemoveButton;
    private final Button<LayerWindowController> layerShowHideButton;
    private boolean visibleFields = false;

    public LayerField(int bodyFieldKey, int layerFieldKey, LayerWindowController controller) {
        super(bodyFieldKey, layerFieldKey, 4);

        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);

        mLayerControl =
                new ButtonWithText<>(
                        "",
                        2,
                        ResourceManager.getInstance().layerButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        RectangularBounds layerButtonBounds = (RectangularBounds) mLayerControl.getBounds();
        layerButtonBounds.setWidth(63);
        layerButtonBounds.setShiftX(28);

        this.setHeight(mLayerControl.getHeight());

        addToLayout(mLayerControl);
        mLayerControl.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, mLayerControl) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onSecondaryButtonClicked(LayerField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onSecondaryButtonReleased(LayerField.this);
                    }
                });

        float ARROW_WIDTH = 14f;
        float ARROW_HEIGHT = 16f;
        upArrowButton =
                new Button<>(
                        105 - ARROW_HEIGHT / 2,
                        mLayerControl.getHeight() / 2 - ARROW_WIDTH / 2,
                        ResourceManager.getInstance().upButtonTextureRegions.get(0),
                        Button.ButtonType.OneClick,
                        true);
        addElement(upArrowButton);
        upArrowButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, upArrowButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onUpArrowButtonClicked(LayerField.this);
                    }
                });

        downArrowButton =
                new Button<>(
                        13 - 8,
                        mLayerControl.getHeight() / 2 - 7,
                        ResourceManager.getInstance().downButtonTextureRegions.get(0),
                        Button.ButtonType.OneClick,
                        true);
        addElement(downArrowButton);

        downArrowButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, downArrowButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onDownArrowButtonClicked(LayerField.this);
                    }
                });

        layerAddDecorationButton =
                new Button<>(
                        ResourceManager.getInstance().addTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(layerAddDecorationButton);
        layerAddDecorationButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, layerAddDecorationButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onLayerAddDecorationClicked(LayerField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });

        layerOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(layerOptionsButton);
        layerOptionsButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, layerOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onLayerSettingsButtonReleased(LayerField.this);
                    }
                });


        layerRemoveButton =
                new Button<>(
                        ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(layerRemoveButton);
        layerRemoveButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, layerRemoveButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onLayerRemoveButtonReleased(LayerField.this);
                    }
                });

        layerShowHideButton =
                new Button<>(
                        ResourceManager.getInstance().showHideTextureRegion, Button.ButtonType.Selector, true);
        addToLayout(layerShowHideButton);
        layerShowHideButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, layerShowHideButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onLayerShowHideButtonClicked(LayerField.this);
                    }
                    @Override
                    public void informControllerButtonReleased() {
                        controller.onLayerShowHideButtonReleased(LayerField.this);
                    }
                });
        layerShowHideButton.updateState(Button.State.PRESSED);
        setPadding(3f);
        hideFields();
    }

    public ButtonWithText<?> getLayerControl() {
        return mLayerControl;
    }

    public Button<LayerWindowController> getArrowUpButton() {
        return upArrowButton;
    }

    public Button<LayerWindowController> getArrowDownButton() {
        return downArrowButton;
    }

    public void setText(String text) {
        mLayerControl.setTitle(text);
    }

    public void showFields() {
        this.visibleFields = true;
        this.layerOptionsButton.setVisible(true);
        this.layerRemoveButton.setVisible(true);
        this.layerAddDecorationButton.setVisible(true);
        this.layerShowHideButton.setVisible(true);
    }

    public void hideFields() {
        this.visibleFields = false;
        this.layerOptionsButton.setVisible(false);
        this.layerRemoveButton.setVisible(false);
        this.layerAddDecorationButton.setVisible(false);
        this.layerShowHideButton.setVisible(false);
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
