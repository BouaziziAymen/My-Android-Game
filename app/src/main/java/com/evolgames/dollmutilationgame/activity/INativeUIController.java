package com.evolgames.dollmutilationgame.activity;

import com.evolgames.dollmutilationgame.activity.components.PlayUIFragment;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;

import java.util.List;

public interface INativeUIController {
    void onItemButtonPressed(ItemMetaData itemMetaData);

    void onHomeButtonPressed();

    void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState);

    void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selected, boolean usesActive);

    void onOptionSelected(PlayerSpecialAction playerSpecialAction);

    void resetTouchHold();

    void resetSelectButton();

    void resetUI();

    void onMirrorButtonClicked();

    void onEditorClicked();

    void onProceedToEdit(String itemNameText, boolean continueLatest);

    void onProceedToCreate(String itemNameText, String itemTypeText, String itemTemplateText);

    void onItemSaved();
}
