package com.evolgames.dollmutilationgame.activity;

import com.evolgames.dollmutilationgame.activity.components.PlayUIFragment;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.helpers.XmlHelper;
import com.evolgames.dollmutilationgame.scenes.AbstractScene;
import com.evolgames.dollmutilationgame.scenes.MainScene;
import com.evolgames.dollmutilationgame.scenes.PlayScene;
import com.evolgames.dollmutilationgame.entities.hand.PlayerAction;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;
import com.evolgames.dollmutilationgame.userinterface.model.ItemCategory;

import org.andengine.audio.sound.Sound;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NativeUIController implements INativeUIController {
    public static final String LATEST = "Latest Item";
    private final GameActivity gameActivity;
    private MainScene mainScene;

    NativeUIController(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setMainScene(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    @Override
    public void onItemButtonPressed(ItemMetaData itemMetaData) {
         ResourceManager.getInstance().setSelectedItemMetaData(itemMetaData);
        ((PlayScene) mainScene.getChildScene()).setPlayerAction(PlayerAction.Create);
    }

    public void onItemButtonReleased() {
        ResourceManager.getInstance().setSelectedItemMetaData(null);
        ((PlayScene) mainScene.getChildScene()).stopCreating();
    }

    @Override
    public void onHomeButtonPressed() {
        resetUI();
        ResourceManager.getInstance().motorSounds.forEach(Sound::stop);
        ResourceManager.getInstance().clockSound.pause();
        gameActivity.runOnUpdateThread(() -> {
            ((AbstractScene) mainScene.getChildScene()).onPause();
            mainScene.goToScene(SceneType.MENU);
        });
    }

    @Override
    public void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState) {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        switch (touchHoldState) {
            case TOUCH:
                playScene.setPlayerAction(PlayerAction.Drag);
                break;
            case SELECT:
                playScene.setPlayerAction(PlayerAction.Select);
                break;
            case HOLD:
                playScene.setPlayerAction(PlayerAction.Hold);
                break;
        }
    }

    @Override
    public void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selected, boolean usesActive) {
        gameActivity.runOnUiThread(() -> {
            PlayUIFragment playUIFragment = gameActivity.getGameUIFragment();
            playUIFragment.setOptionsList(usageList, selected);
        });
    }

    @Override
    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.onOptionSelected(playerSpecialAction);
    }

    @Override
    public void resetTouchHold() {
        gameActivity.runOnUiThread(() -> {
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.reset();
        });
    }

    @Override
    public void resetSelectButton() {
        gameActivity.runOnUiThread(() -> {
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.resetSelectButton();
        });
    }

    @Override
    public void resetUI() {
        gameActivity.runOnUiThread(() -> {
            switch (gameActivity.getInstalledUI()) {
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

    @Override
    public void onMirrorButtonClicked() {
        gameActivity.runOnUpdateThread(() -> {
            PlayScene playScene = ((PlayScene) mainScene.getChildScene());
            playScene.onMirrorButtonClicked();
        });
    }

    @Override
    public void onEditorClicked() {
        gameActivity.showCreateItemDialog();
    }

    @Override
    public void onProceedToEdit(String itemNameText, boolean continueLatest) {
        gameActivity.runOnUpdateThread(() -> {
            final String item;
            if(continueLatest){
                item = gameActivity.loadStringFromPreferences(LATEST);
            } else {
                item = itemNameText;
            }
            Optional<ItemMetaData> res = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                    .filter(e -> e.getName().equalsIgnoreCase(item)).findFirst();
            if (res.isPresent()) {
                ItemMetaData itemMetaData = new ItemMetaData();
                itemMetaData.setFileName(res.get().getFileName());
                itemMetaData.setUserCreated(res.get().isUserCreated());
                itemMetaData.setToolName(res.get().getName());
                itemMetaData.setItemCategory(res.get().getItemCategory());
                ResourceManager.getInstance().setEditorItem(itemMetaData);
                ResourceManager.getInstance().activity.saveStringToPreferences("saved_tool_filename", XmlHelper.convertToXmlFormat(itemMetaData.getName()));
                mainScene.goToScene(SceneType.EDITOR);
            } else {
                ResourceManager.getInstance().activity.installMenuUi();
                goToScene(SceneType.MENU);
            }
        });
    }

    @Override
    public void onProceedToCreate(String itemNameText, String itemTypeText, String itemTemplateText) {
        gameActivity.runOnUpdateThread(() -> {
            gameActivity.saveStringToPreferences(LATEST,itemNameText);
            ItemMetaData newItemMetaData = new ItemMetaData();
            newItemMetaData.setToolName(itemNameText);
            newItemMetaData.setItemCategory(ItemCategory.fromName(itemTypeText));
            if (itemTemplateText!=null&&!itemTemplateText.isEmpty()) {
                Optional<ItemMetaData> res = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                        .filter(e -> e.getName().equalsIgnoreCase(itemTemplateText)).findFirst();
                res.ifPresent(itemMetaData -> newItemMetaData.setTemplateFilename(res.get().getFileName()));
                newItemMetaData.setUserCreated(res.get().isUserCreated());
            }
            newItemMetaData.setToolName(itemNameText);
            ResourceManager.getInstance().setEditorItem(newItemMetaData);
            ResourceManager.getInstance().activity.saveStringToPreferences("saved_tool_filename", XmlHelper.convertToXmlFormat(newItemMetaData.getName()));
            mainScene.goToScene(SceneType.EDITOR);
        });
    }

    void fillItemsMap() {
        Map<ItemCategory, List<ItemMetaData>> map = new HashMap<>();
        for (ItemCategory cat : ItemCategory.values()) {
            map.put(cat, new ArrayList<>());
        }
        XmlHelper helper = new XmlHelper(gameActivity);
        helper.fillItemsMapFromAssets(map);
        helper.fillItemsMapFromInternalStorage(map);
        map.values().forEach(list -> list.sort(Comparator.comparing(ItemMetaData::getName)));
        ResourceManager.getInstance().fillItemNamesTranslation();
        ResourceManager.getInstance().setItemsMap(map);
        for (List<ItemMetaData> list : map.values()) {
            for(ItemMetaData itemMetaData:list){
                Integer itemTranslatedNameId = ResourceManager.getInstance().getTranslatedItemStringId(itemMetaData.getName());
              if(itemTranslatedNameId!=-1) {
                  itemMetaData.setDisplayName(gameActivity.getString(itemTranslatedNameId));
              } else {
                  itemMetaData.setDisplayName(itemMetaData.getName());
              }
            }
            list.sort(Comparator.comparing(ItemMetaData::getDisplayName));
        }
    }

    @Override
    public void onItemSaved() {
        this.fillItemsMap();
    }

    public void onTrackButtonClicked() {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        if(playScene.getHand().hasSelectedEntity()){
            playScene.chaseEntity(playScene.getHand().getSelectedEntity(),false);
        }
        playScene.setChaseActive(true);
        showHint(R.string.camera_tracking_hint,HintType.HINT);
    }

    public void onTrackButtonReleased() {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.setChaseActive(false);
        playScene.releaseChasedEntity();
    }

    public void onUsesClicked() {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.setUsesActive(true);
        playScene.setEffectsActive(false);
    }

    public void onUsesReleased() {

    }

    public void onEffectsClicked() {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.setEffectsActive(true);
        playScene.setUsesActive(false);
        playScene.onUsagesUpdated();
    }

    public void onEffectsReleased() {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.setEffectsActive(false);
        playScene.setUsesActive(true);
        playScene.onUsagesUpdated();
    }

    public void onItemCreated() {
        gameActivity.runOnUiThread(() -> {
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.resetWeaponsButtonAndClose();
            gameUIFragment.resetClickedItem();
        });
    }

    public void onHelpPressed() {
        gameActivity.showHelpDialog();
    }

    public void onRefreshButtonClicked() {
            PlayScene playScene = ((PlayScene) mainScene.getChildScene());
            if(playScene!=null) {
                playScene.refresh();
            }
    }

    public void resetCameraButton() {
        gameActivity.runOnUiThread(() -> {
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.resetCameraButton();
        });
    }

    public PlayerAction getHoldDragState() {
        PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
       return gameUIFragment.getHoldState();
    }

    public void onRefresh() {
        ((AbstractScene) mainScene.getChildScene()).onPause();
        mainScene.goToScene(SceneType.PLAY);
    }

    public void goToScene(SceneType sceneType) {
        mainScene.goToScene(sceneType);
    }

    public enum HintType{
        WARNING, HINT
    }
    public void showHint(int hintString, HintType hintType){
        if(!ResourceManager.getInstance().isHints()&&hintType!=HintType.WARNING){
            return;
        }
        gameActivity.runOnUiThread(() -> {
            int hintIconId = 0;
            switch (hintType){
                case WARNING:
                    hintIconId = R.drawable.warning_icon;
                    break;
                case HINT:
                    hintIconId = R.drawable.lightbulb_icon;
                    break;
            }
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.showHint(gameActivity.getString(hintString),hintIconId);
        });
    }

    public void showHint(String text, HintType hintType){
        if(!ResourceManager.getInstance().isHints()&&hintType!=HintType.WARNING){
            return;
        }
        gameActivity.runOnUiThread(() -> {
            int hintIconId = 0;
            switch (hintType){
                case WARNING:
                    hintIconId = R.drawable.warning_icon;
                    break;
                case HINT:
                    hintIconId = R.drawable.lightbulb_icon;
                    break;
            }
            PlayUIFragment gameUIFragment = gameActivity.getGameUIFragment();
            gameUIFragment.showHint(text,hintIconId);
        });
    }
}
