package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.MenuScene;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.MainMenuController;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.shapes.Grid;

import org.andengine.input.touch.TouchEvent;

public class MenuUserInterface extends UserInterface<MenuScene> {
  public MenuUserInterface(MenuScene scene) {
    super(scene);
    Image image = new Image(400-(131f/2),240-(70f/2)-42f,ResourceManager.getInstance().playTextureRegion);
    this.addElement(image);
/*    MainMenuController mainMenuController = new MainMenuController();

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
    addElement(editorButton);*/
setUpdated(true);

  }

  @Override
  public boolean onTouchHud(TouchEvent pTouchEvent) {
    return super.onTouchHud(pTouchEvent);
  }

  @Override
  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {}
}
