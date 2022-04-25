package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.sections.basic.TertiaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

public class DecorationField1 extends TertiaryLinearLayout {

    private final ButtonWithText<LayerWindowController> mDecorationControl;


    public DecorationField1(int bodyFieldKey, int layerFieldKey, int decorationFieldKey, LayerWindowController controller) {
        super(bodyFieldKey,layerFieldKey,decorationFieldKey,4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mDecorationControl = new ButtonWithText<>("Decoration", 2, ResourceManager.getInstance().largeLampTextureRegion,Button.ButtonType.Selector,true);
        addToLayout(mDecorationControl);

        mDecorationControl.setBehavior(new ButtonBehavior<LayerWindowController>(controller,mDecorationControl) {
            @Override
            public void informControllerButtonClicked() {
                controller.onTertiaryButtonClicked(DecorationField1.this);
            }
            @Override
            public void informControllerButtonReleased() {
                controller.onTertiaryButtonReleased(DecorationField1.this);
            }
        });
        Button<LayerWindowController> decorationOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(decorationOptionsButton);
        decorationOptionsButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,decorationOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onDecorationSettingsButtonClicked(DecorationField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onDecorationSettingsButtonReleased(DecorationField1.this);
            }
        });





        Button<LayerWindowController> decorationRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(decorationRemoveButton);
        decorationRemoveButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,decorationRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onDecorationRemoveButtonClicked(DecorationField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onDecorationRemoveButtonReleased(DecorationField1.this);
            }
        });
        this.setHeight(decorationRemoveButton.getHeight());
    }


    public ButtonWithText getDecorationControl() {
        return mDecorationControl;
    }

}
