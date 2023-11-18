package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.MenuScene;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.MainMenuController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import org.andengine.input.touch.TouchEvent;

public class MenuUserInterface extends UserInterface<MenuScene> {
  public MenuUserInterface(MenuScene scene) {
    super(scene);
    MainMenuController mainMenuController = new MainMenuController();
    ButtonWithText<MainMenuController> playButton =
        new ButtonWithText<>(
            400,
            240,
            "Play",
            1,
            ResourceManager.getInstance().mainButtonTextureRegion,
            Button.ButtonType.OneClick,
            true);
    ButtonWithText<MainMenuController> editorButton =
        new ButtonWithText<>(
            400,
            200,
            "Editor",
            1,
            ResourceManager.getInstance().mainButtonTextureRegion,
            Button.ButtonType.OneClick,
            true);


    playButton.setBehavior(new ButtonBehavior<MainMenuController>(mainMenuController,playButton) {
      @Override
      public void informControllerButtonClicked() {
          scene.goToScene(SceneType.PLAY);
      }

      @Override
      public void informControllerButtonReleased() {

      }
    });


    editorButton.setBehavior(new ButtonBehavior<MainMenuController>(mainMenuController,editorButton) {
      @Override
      public void informControllerButtonClicked() {
        scene.goToScene(SceneType.EDITOR);
      }

      @Override
      public void informControllerButtonReleased() {

      }
    });

    addElement(playButton);
    addElement(editorButton);
  }

  @Override
  public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
    return super.onTouchHud(pTouchEvent, isTouched);
  }

  @Override
  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {}
}
