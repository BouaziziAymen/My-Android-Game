package com.evolgames.activity;

import static com.evolgames.scenes.EditorScene.EDITOR_FILE;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.entities.hand.PlayerAction;
import com.evolgames.entities.hand.PlayerSpecialAction;
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
    public static final String EDITOR_FILE_FROM_ASSETS = "EditorFromAssets";
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
                ResourceManager.getInstance().setEditorItem(res.get());
                ItemMetaData itemMetaData = res.get();
                itemMetaData.setTemplateFilename(itemMetaData.getFileName());
                mainScene.saveStringToPreferences(EDITOR_FILE, itemMetaData.getFileName());
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
            Optional<ItemMetaData> res = ResourceManager.getInstance().getItemsMap().values().stream().flatMap(List::stream)
                    .filter(e -> e.getName().equalsIgnoreCase(itemTemplateText)).findFirst();
            res.ifPresent(itemMetaData -> newItemMetaData.setTemplateFilename(itemMetaData.getFileName()));
            ResourceManager.getInstance().setEditorItem(newItemMetaData);
            mainScene.saveStringToPreferences(EDITOR_FILE, newItemMetaData.getFileName());
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
