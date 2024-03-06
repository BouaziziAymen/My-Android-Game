package com.evolgames.activity;

import com.evolgames.helpers.ItemMetaData;
import com.evolgames.scenes.MainScene;
import com.evolgames.scenes.MenuScene;
import com.evolgames.scenes.PlayScene;

public class NativeUIController implements INativeUIController{
    private final MainScene menuScene;

    NativeUIController(MainScene menuScene){
        this.menuScene = menuScene;
    }

    @Override
    public void onItemButtonPressed(ItemMetaData itemMetaData) {
        ((PlayScene)menuScene.getChildScene()).createItem(itemMetaData.getFileName(),true);
    }
}
