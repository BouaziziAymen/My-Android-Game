package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;

public class TimeBomb extends Bomb {

    private float countDown;

    @SuppressWarnings("unused")
    public TimeBomb() {
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
    protected void detonate(WorldFacade worldFacade) {
        super.detonate(worldFacade);
            ResourceManager.getInstance().clockSound.pause();
    }

    @Override
    protected void removeSafety(WorldFacade worldFacade) {
        if(ResourceManager.getInstance().isSound()) {
            ResourceManager.getInstance().clockSound.play();
        }
        super.removeSafety(worldFacade);
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
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
