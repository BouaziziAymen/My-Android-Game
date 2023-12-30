package com.evolgames.userinterface.view.windows.windowfields.projectieoptionwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class SoundField extends SimpleTertiary<ButtonWithText<ProjectileOptionController>> {

  public SoundField(
      String soundName,
      int bodyFieldKey,
      int layerFieldKey,
      int decorationFieldKey,
      ProjectileOptionController controller) {
    super(
        bodyFieldKey,
        layerFieldKey,
        decorationFieldKey,
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
            controller.onTertiaryButtonClicked(SoundField.this);
          }

          @Override
          public void informControllerButtonReleased() {
            controller.onTertiaryButtonReleased(SoundField.this);
          }
        });
  }

  public ButtonWithText getSoundFieldControl() {
    return main;
  }
}
