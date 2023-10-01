package com.evolgames.entities.usage;

import com.evolgames.scenes.PlayerSpecialAction;

public class Slasher extends MeleeUse {

    @Override
    public void onStep(float deltaTime) {

    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Slash;
    }

}
