package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.Arrays;
import java.util.Comparator;

import org.andengine.input.touch.TouchEvent;

public class PlayUserInterface extends UserInterface<PlayScene> {

  private final Switcher particularUsageSwitcher;
  private final Switcher generalUsageSwitcher;
  private PlayerSpecialAction[] particularUsages;

  public PlayUserInterface(PlayScene scene) {
    super(scene);

    particularUsageSwitcher =
        new Switcher(
            800f - 72f,
            300,
            ResourceManager.getInstance().particularUsages,
            32f,
            (index) -> scene.setSpecialAction(this.particularUsages[index]));
    particularUsageSwitcher.reset();
    addElement(particularUsageSwitcher);

    generalUsageSwitcher =
        new Switcher(
            800f - 72f,
            300f + 64f,
            ResourceManager.getInstance().generalUsages,
            32f,
            (index) -> scene.setPlayerAction(PlayerAction.values()[index]));
    generalUsageSwitcher.reset(0, 1, 2, 3, 4, 5);
    addElement(generalUsageSwitcher);

    //        Button<DrawButtonBoardController> createButton = new
    // Button<>(ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.OneClick,
    // true);
    //        createButton.setBehavior(new
    // ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, createButton) {
    //            @Override
    //            public void informControllerButtonClicked() {
    //            }
    //
    //            @Override
    //            public void informControllerButtonReleased() {
    //                getToolModel().createTool();
    //            }
    //        });
    //        createButton.setPosition(0, 480 - createButton.getHeight());
    //        addElement(createButton);
    setUpdated(true);
  }

  @Override
  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {}

  @Override
  public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
    return super.onTouchHud(pTouchEvent, isTouched);
  }

  public void updateParticularUsageSwitcher(PlayerSpecialAction[] usages) {
    Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal));
    this.particularUsages = Arrays.copyOf(usages, usages.length);
    Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal).reversed());
    this.particularUsageSwitcher.reset(Arrays.stream(usages).mapToInt(e -> e.iconId).toArray());
  }
  public void updatePlayerSpecialActionOnSwitcher(PlayerSpecialAction playerSpecialAction){
    while (scene.getSpecialAction() != playerSpecialAction) {
      this.particularUsageSwitcher.search();
    }
  }
  public void updatePlayerSpecialActionOnSwitcher(PlayerAction playerAction){
    while (scene.getPlayerAction() != playerAction) {
      this.generalUsageSwitcher.search();
    }
  }

}
