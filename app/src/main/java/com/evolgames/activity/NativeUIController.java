package com.evolgames.activity;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.scenes.MainScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.entities.hand.PlayerAction;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.scenes.entities.SceneType;

import java.util.List;

public class NativeUIController implements INativeUIController{
    private final MainScene mainScene;
    private final GameActivity gameActivity;

    NativeUIController(MainScene menuScene, GameActivity gameActivity){
        this.mainScene = menuScene;
        this.gameActivity = gameActivity;
    }

    @Override
    public void onItemButtonPressed(ItemMetaData itemMetaData) {
        ((PlayScene) mainScene.getChildScene()).createItem(itemMetaData.getFileName(),true);
    }

    @Override
    public void onHomeButtonPressed() {
        gameActivity.runOnUpdateThread(()->{
            ((PlayScene)mainScene.getChildScene()).onPause();
            resetUI();
            mainScene.goToScene(SceneType.MENU);
        });

    }

    @Override
    public void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState) {
        PlayScene playScene = ((PlayScene)mainScene.getChildScene());
        playScene.setPlayerAction(touchHoldState== PlayUIFragment.TouchHoldState.HOLD? PlayerAction.Hold:PlayerAction.Drag);
    }

    @Override
    public void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selected) {
          PlayUIFragment playUIFragment = gameActivity.getGameUIFragment();
    playUIFragment.setOptionsList(usageList,selected);
    }

    @Override
    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {
        PlayScene playScene = ((PlayScene)mainScene.getChildScene());
        playScene.onOptionSelected(playerSpecialAction);
    }
    @Override
    public void resetUI(){
        gameActivity.runOnUiThread(()->{
            switch(gameActivity.getInstalledUI()){
                case GAME:
                    PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
                    gameUIFragment.reset();
                    break;
                case EDITOR:
                    break;
                case MENU:
                    break;
            }
        });
    }
}
