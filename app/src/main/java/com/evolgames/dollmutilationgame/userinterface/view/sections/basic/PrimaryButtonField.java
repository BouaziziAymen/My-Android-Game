package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.OneLevelGameWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;

public class PrimaryButtonField extends PrimaryLinearLayout {
    private final ButtonWithText<OneLevelGameWindowController> controlButton;

    public PrimaryButtonField(int primaryKey, OneLevelGameWindowController controller) {
        super(primaryKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        controlButton =
                new ButtonWithText<>(
                        "",
                        2,
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        addToLayout(controlButton);

        controlButton.setBehavior(
                new ButtonBehavior<OneLevelGameWindowController>(controller, controlButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onPrimaryButtonClicked(PrimaryButtonField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onPrimaryButtonReleased(PrimaryButtonField.this);
                    }
                });

        setPadding(5f);
    }

    public ButtonWithText getControl() {
        return controlButton;
    }

    public void setText(String text) {
        controlButton.setTitle(text);
    }
}
