package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.OneLevelGameWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;

public class SecondaryButtonField extends SecondaryLinearLayout {
    private final ButtonWithText<OneLevelGameWindowController> buttonControl;

    public SecondaryButtonField(
            int primaryKey, int secondaryKey, OneLevelGameWindowController controller) {
        super(primaryKey, secondaryKey, 4);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
        buttonControl =
                new ButtonWithText<>(
                        "",
                        2,
                        ResourceManager.getInstance().largeLampTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        addToLayout(buttonControl);

        buttonControl.setBehavior(
                new ButtonBehavior<OneLevelGameWindowController>(controller, buttonControl) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onSecondaryButtonClicked(SecondaryButtonField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onSecondaryButtonReleased(SecondaryButtonField.this);
                    }
                });
    }

    public ButtonWithText<OneLevelGameWindowController> getControl() {
        return buttonControl;
    }

    public void setText(String name) {
        buttonControl.setTitle(name);
    }
}
