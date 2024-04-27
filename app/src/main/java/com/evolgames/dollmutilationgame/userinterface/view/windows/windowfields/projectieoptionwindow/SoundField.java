package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.projectieoptionwindow;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;

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
                        controller.onSecondaryButtonReleased(SoundField.this);
                    }
                });
    }

    public ButtonWithText getSoundFieldControl() {
        return main;
    }
}
