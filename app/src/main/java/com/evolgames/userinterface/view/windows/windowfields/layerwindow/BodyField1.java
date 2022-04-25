package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.sections.basic.PrimaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

public class BodyField1 extends PrimaryLinearLayout {

    private final ButtonWithText<LayerWindowController> mBodyControl;

    public BodyField1(int bodyFieldKey, LayerWindowController controller) {
        super(bodyFieldKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mBodyControl = new ButtonWithText<>("", 2,ResourceManager.getInstance().mainButtonTextureRegion, Button.ButtonType.Selector,true);
        addToLayout(mBodyControl);


        mBodyControl.setBehavior(new ButtonBehavior<LayerWindowController>(controller,mBodyControl) {
            @Override
            public void informControllerButtonClicked() {
                controller.onPrimaryButtonClicked(BodyField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onPrimaryButtonReleased(BodyField1.this);
            }
        });


        Button<LayerWindowController> addButton = new Button<>(ResourceManager.getInstance().addTextureRegion, Button.ButtonType.OneClick,true);
        addToLayout(addButton);
        addButton.setPadding(5f);
        addButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,addButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onAddLayerButtonCLicked(BodyField1.this);
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });

        Button<LayerWindowController> bodyRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(bodyRemoveButton);
        bodyRemoveButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,bodyRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onBodyRemoveButtonClicked(BodyField1.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBodyRemoveButtonReleased(BodyField1.this);
            }
        });


        Button<LayerWindowController> bodyOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(bodyOptionsButton);
        bodyOptionsButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,bodyOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBodySettingsButtonReleased(BodyField1.this);
            }
        });



        setPadding(5f);
    }


    public ButtonWithText getBodyControl() {
        return mBodyControl;
    }
    public void setText(String text){
        mBodyControl.setTitle(text);
    }

}
