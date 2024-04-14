package com.evolgames.userinterface.view.windows.windowfields.projectieoptionwindow;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class SoundField extends SimpleSecondary<ButtonWithText<ProjectileOptionController>> {

    public SoundField(
            String soundName,
            int bodyFieldKey,
            int layerFieldKey,
            ProjectileOptionController controller) {
        super(
                bodyFieldKey,
                layerFieldKey,
                new ButtonWithText<>(
                        soundName,
                        2,
                        ResourceManager.getInstance().largeLampTextureRegion,
                        Button.ButtonType.Selector,
                        true));
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);

        main.setBehavior(
                new ButtonBehavior<ProjectileOptionController>(controller, main) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onSecondaryButtonClicked(SoundField.this);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        controller.onSecondaryButtonClicked(SoundField.this);
                    }
                });
    }

    public ButtonWithText getSoundFieldControl() {
        return main;
    }
}
