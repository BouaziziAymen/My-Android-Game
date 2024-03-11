package com.evolgames.activity;

import com.evolgames.activity.components.PlayUIFragment;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.List;

public interface INativeUIController {
    void onItemButtonPressed(ItemMetaData itemMetaData);
    void onHomeButtonPressed();

    void onTouchHoldButtonSwitched(PlayUIFragment.TouchHoldState touchHoldState);

    void onUsagesUpdated(List<PlayerSpecialAction> usageList, PlayerSpecialAction selectedAction);
}
