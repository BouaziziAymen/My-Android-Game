package com.evolgames.entities.usage;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

public class TimeBomb extends Bomb {

    private float countDown;

    @SuppressWarnings("unused")
    public TimeBomb(UsageModel<?> e) {
    }

    public TimeBomb(UsageModel<?> usageModel, boolean mirrored) {
        super(usageModel, mirrored);
        TimeBombUsageProperties properties = (TimeBombUsageProperties) usageModel.getProperties();
        this.countDown = properties.getDelay();
    }

    @Override
    protected boolean isCountDone() {
        return countDown < 0;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        super.onStep(deltaTime, worldFacade);
        if (active && alive) {
            countDown -= deltaTime;
        }
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        super.dynamicMirror(physicsScene);
    }


    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        if (ratio < 0.9f) {
            return false;
        }
        this.getBombInfoList().forEach(bombInfo -> bombInfo.setCarrierEntity(heir));
        return true;
    }

}
