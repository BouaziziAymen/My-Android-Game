package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.jointswindow;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.PrimaryLinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;

public class JointField extends PrimaryLinearLayout {

    private final ButtonWithText<JointWindowController> mJointControl;
    private final Button<JointWindowController> jointOptionsButton;
    private final Button<JointWindowController> jointRemoveButton;
    private boolean visibleFields;

    public JointField(int bodyFieldKey, JointWindowController controller) {
        super(bodyFieldKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        mJointControl =
                new ButtonWithText<>(
                        "Joint" + bodyFieldKey,
                        2,
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        addToLayout(mJointControl);

        mJointControl.setBehavior(
                new ButtonBehavior<JointWindowController>(controller, mJointControl) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onPrimaryButtonClicked(JointField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onPrimaryButtonReleased(JointField.this);
                    }
                });

        jointOptionsButton =
                new Button<>(
                        ResourceManager.getInstance().smallOptionsTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        addToLayout(jointOptionsButton);
        jointOptionsButton.setBehavior(
                new ButtonBehavior<JointWindowController>(controller, jointOptionsButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onOptionButtonReleased(JointField.this);
                    }
                });

        jointOptionsButton.setPadding(5f);

        jointRemoveButton =
                new Button<>(
                        ResourceManager.getInstance().removeTextureRegion, Button.ButtonType.OneClick, true);
        addToLayout(jointRemoveButton);
        jointRemoveButton.setBehavior(
                new ButtonBehavior<JointWindowController>(controller, jointRemoveButton) {
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

    public ButtonWithText<?> getJointControl() {
        return mJointControl;
    }

    public void showFields() {
        this.visibleFields = true;
        this.jointOptionsButton.setVisible(true);
        this.jointRemoveButton.setVisible(true);
        this.jointOptionsButton.setGone(false);
        this.jointRemoveButton.setGone(false);
    }

    public void hideFields() {
        this.visibleFields = false;
        this.jointOptionsButton.setGone(true);
        this.jointRemoveButton.setGone(true);
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
