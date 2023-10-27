package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.sections.basic.TertiaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

public class DecorationField extends TertiaryLinearLayout {

    private final ButtonWithText<LayerWindowController> mDecorationControl;
    private final Button<LayerWindowController> decorationRemoveButton;
    private final Button<LayerWindowController> decorationOptionsButton;
    private boolean visibleFields;


    public DecorationField(int bodyFieldKey, int layerFieldKey, int decorationFieldKey, LayerWindowController controller) {
        super(bodyFieldKey,layerFieldKey,decorationFieldKey,4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mDecorationControl = new ButtonWithText<>("", 2, ResourceManager.getInstance().largeLampTextureRegion,Button.ButtonType.Selector,true);
        addToLayout(mDecorationControl);

        mDecorationControl.setBehavior(new ButtonBehavior<LayerWindowController>(controller,mDecorationControl) {
            @Override
            public void informControllerButtonClicked() {
                controller.onTertiaryButtonClicked(DecorationField.this);
            }
            @Override
            public void informControllerButtonReleased() {
                controller.onTertiaryButtonReleased(DecorationField.this);
            }
        });
        decorationOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(decorationOptionsButton);
        decorationOptionsButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,decorationOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onDecorationSettingsButtonReleased(DecorationField.this);
            }
        });





        decorationRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(decorationRemoveButton);
        decorationRemoveButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,decorationRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onDecorationRemoveButtonReleased(DecorationField.this);
            }
        });
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
    }

    public void hideFields() {
        this.visibleFields = false;
        this.decorationOptionsButton.setVisible(false);
        this.decorationRemoveButton.setVisible(false);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible){
            if(visibleFields){
                this.showFields();
            } else {
                this.hideFields();
            }
        }
    }

}
