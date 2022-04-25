package com.evolgames.userinterface.view.windows.windowfields.jointswindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.sections.basic.PrimaryLinearLayout;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

public class JointField extends PrimaryLinearLayout {

    private final ButtonWithText<JointWindowController> mBodyControl;

    public JointField(int bodyFieldKey, JointWindowController controller) {
        super(bodyFieldKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mBodyControl = new ButtonWithText<>("Joint"+bodyFieldKey, 2,ResourceManager.getInstance().mainButtonTextureRegion, Button.ButtonType.Selector,true);
        addToLayout(mBodyControl);

        mBodyControl.setBehavior(new ButtonBehavior<JointWindowController>(controller,mBodyControl) {
            @Override
            public void informControllerButtonClicked() {
            getController().onPrimaryButtonClicked(JointField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onPrimaryButtonReleased(JointField.this);
            }
        });




        Button<JointWindowController> jointOptionsButton = new Button<>(ResourceManager.getInstance().smallOptionsTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(jointOptionsButton);
        jointOptionsButton.setBehavior(new ButtonBehavior<JointWindowController>(controller,jointOptionsButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                getController().onOptionButtonReleased(JointField.this);
            }
        });


        jointOptionsButton.setPadding(5f);




        Button<JointWindowController> jointRemoveButton = new Button<>(ResourceManager.getInstance().removeTextureRegion,Button.ButtonType.OneClick,true);
        addToLayout(jointRemoveButton);
        jointRemoveButton.setBehavior(new ButtonBehavior<JointWindowController>(controller,jointRemoveButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
              getController().onRemoveButtonReleased(JointField.this);
            }
        });
        setPadding(5f);
    }


    public ButtonWithText getBodyControl() {
        return mBodyControl;
    }

}
