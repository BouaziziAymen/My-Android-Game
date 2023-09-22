package com.evolgames.entities.usage;

import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.hand.Hand;

import org.andengine.input.touch.TouchEvent;

public class Slasher extends Use {

    private final GameEntity slashingEntity;
    private final WorldFacade worldFacade;

    public Slasher(GameEntity gameEntity, WorldFacade worldFacade) {
       this.slashingEntity = gameEntity;
       this.worldFacade = worldFacade;
    }

    @Override
    public void onStep(float deltaTime) {

    }

    @Override
    public float getUIWidth() {
        return 0;
    }

    @Override
    public void updateUIPosition(int row, int offset) {

    }

    @Override
    public void showUI() {

    }

    @Override
    public void hideUI() {

    }

    @Override
    public int getUseId() {
        return 5;
    }

}
