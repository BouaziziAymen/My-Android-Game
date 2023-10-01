package com.evolgames.entities.usage;

import com.evolgames.scenes.PlayerSpecialAction;

public class Throw extends Use{
    @Override
    public void onStep(float deltaTime) {

    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Throw;
    }
}
