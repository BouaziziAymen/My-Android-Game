package com.evolgames.activity;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.entities.hand.PlayerAction;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.gameengine.BuildConfig;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.helpers.XmlHelper;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.MainScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ItemCategory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NativeUIController implements INativeUIController {
    private  MainScene mainScene;
    private final GameActivity gameActivity;

    NativeUIController( GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setMainScene(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    @Override
    public void onItemButtonPressed(ItemMetaData itemMetaData) {
        ((PlayScene) mainScene.getChildScene()).createItemFromFile(itemMetaData.getFileName(), !itemMetaData.isUserCreated(), true);
    }

    @Override
    public void onHomeButtonPressed() {
        gameActivity.runOnUpdateThread(() -> {
            ((AbstractScene<?>) mainScene.getChildScene()).onPause();
            resetUI();
            mainScene.goToScene(SceneType.MENU);
        });

    }

    @Override
    public void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState) {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.setPlayerAction(touchHoldState == PlayUIFragment.TouchHoldState.HOLD ? PlayerAction.Hold : PlayerAction.Drag);
    }

    @Override
    public void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selected) {
        PlayUIFragment playUIFragment = gameActivity.getGameUIFragment();
        playUIFragment.setOptionsList(usageList, selected);
    }

    @Override
    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.onOptionSelected(playerSpecialAction);
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
        PlayScene playScene = ((PlayScene) mainScene.getChildScene());
        playScene.onMirrorButtonClicked();
    }

    @Override
    public void onEditorClicked() {
        gameActivity.showCreateItemDialog();
    }

    @Override
    public void onProceedToEdit(String itemNameText) {
        gameActivity.runOnUpdateThread(() -> {
            Optional<ItemMetaData> res = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                    .filter(e -> e.getName().equalsIgnoreCase(itemNameText)).findFirst();
            if (res.isPresent()) {
                ItemMetaData itemMetaData = new ItemMetaData();
                itemMetaData.setFileName(res.get().getFileName());
                itemMetaData.setUserCreated(res.get().isUserCreated());
                itemMetaData.setToolName(BuildConfig.DEBUG?res.get().getName():"Anonymous");
                itemMetaData.setItemCategory(res.get().getItemCategory());
                ResourceManager.getInstance().setEditorItem(itemMetaData);
                mainScene.saveStringToPreferences("saved_tool_filename",XmlHelper.convertToXmlFormat(itemMetaData.getName()));
                mainScene.goToScene(SceneType.EDITOR);
            }
        });
    }

    @Override
    public void onProceedToCreate(String itemNameText, String itemTypeText, String itemTemplateText) {
        gameActivity.runOnUpdateThread(() -> {
            ItemMetaData newItemMetaData = new ItemMetaData();
            newItemMetaData.setToolName(itemNameText);
            newItemMetaData.setItemCategory(ItemCategory.fromName(itemTypeText));
            if(!itemTemplateText.isEmpty()) {
                Optional<ItemMetaData> res = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                        .filter(e -> e.getName().equalsIgnoreCase(itemTemplateText)).findFirst();
                res.ifPresent(itemMetaData -> newItemMetaData.setTemplateFilename(res.get().getFileName()));
                newItemMetaData.setUserCreated(res.get().isUserCreated());
            }
            ResourceManager.getInstance().setEditorItem(newItemMetaData);
            mainScene.saveStringToPreferences("saved_tool_filename",  XmlHelper.convertToXmlFormat(newItemMetaData.getName()) );
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
        ResourceManager.getInstance().setItemsMap(map);
    }

    @Override
    public void onItemSaved() {
     this.fillItemsMap();
    }
}
