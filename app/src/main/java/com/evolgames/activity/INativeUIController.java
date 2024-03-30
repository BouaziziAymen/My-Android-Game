package com.evolgames.activity;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.helpers.ItemMetaData;

import java.util.List;

public interface INativeUIController {
    void onItemButtonPressed(ItemMetaData itemMetaData);

    void onHomeButtonPressed();

    void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState);

    void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selectedAction);

    void onOptionSelected(PlayerSpecialAction playerSpecialAction);

    void resetTouchHold();

    void resetSelectButton();

    void resetUI();

    void onMirrorButtonClicked();

    void onEditorClicked();

    void onProceedToEdit(String itemNameText);

    void onProceedToCreate(String itemNameText, String itemTypeText, String itemTemplateText);

    void onItemSaved();
}
