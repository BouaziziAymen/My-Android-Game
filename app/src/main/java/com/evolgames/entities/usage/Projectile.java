package com.evolgames.entities.usage;

import com.evolgames.scenes.PlayerSpecialAction;

public class Projectile extends Use{
    @Override
    public void onStep(float deltaTime) {
this.active = true;
    }


    @Override
    public PlayerSpecialAction getAction() {
        return null;
    }
}
