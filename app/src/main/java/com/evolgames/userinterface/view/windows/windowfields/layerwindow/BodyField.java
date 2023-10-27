package com.evolgames.userinterface.view.windows.windowfields.layerwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.sections.basic.PrimaryLinearLayout;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class BodyField extends PrimaryLinearLayout {

    private final ButtonWithText<LayerWindowController> mBodyControl;
    private final Button<LayerWindowController> bodyRemoveButton;
    private final Button<LayerWindowController> addLayerButton;
    private final Button<LayerWindowController> bodyOptionsButton;
    private boolean visibleFields;

    public BodyField(int bodyFieldKey, LayerWindowController controller) {
        super(bodyFieldKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mBodyControl = new ButtonWithText<>("", 2,ResourceManager.getInstance().mainButtonTextureRegion, Button.ButtonType.Selector,true);


        mBodyControl.setBehavior(new ButtonBehavior<LayerWindowController>(controller,mBodyControl) {
            @Override
            public void informControllerButtonClicked() {
                controller.onPrimaryButtonClicked(BodyField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onPrimaryButtonReleased(BodyField.this);
            }
        });


       addLayerButton = new Button<>(ResourceManager.getInstance().addTextureRegion, Button.ButtonType.OneClick,true);

        addLayerButton.setPadding(5f);
        addLayerButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,addLayerButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.onAddLayerButtonCLicked(BodyField.this);
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });

        bodyRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);


        bodyRemoveButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,bodyRemoveButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBodyRemoveButtonReleased(BodyField.this);
            }
        });


        bodyOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);

        bodyOptionsButton.setBehavior(new ButtonBehavior<LayerWindowController>(controller,bodyOptionsButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onBodySettingsButtonReleased(BodyField.this);
            }
        });
        addToLayout(addLayerButton);
        addToLayout(mBodyControl);
        addToLayout(bodyOptionsButton);
        addToLayout(bodyRemoveButton);

        setPadding(5f);
        hideFields();
    }


    public ButtonWithText<?> getBodyControl() {
        return mBodyControl;
    }
    public void setText(String text){
        mBodyControl.setTitle(text);
    }


    public void showFields() {
        this.visibleFields = true;
        this.bodyOptionsButton.setVisible(true);
        this.bodyRemoveButton.setVisible(true);
        this.addLayerButton.setVisible(true);
    }

    public void hideFields() {
        this.visibleFields = false;
        this.bodyOptionsButton.setVisible(false);
        this.bodyRemoveButton.setVisible(false);
        this.addLayerButton.setVisible(false);
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
