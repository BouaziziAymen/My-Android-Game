package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.dollmutilationgame.entities.serialization.infos.BombInfo;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

import java.util.List;

public class ImpactBomb extends Bomb {

    private List<Integer> sensitiveLayers;
    private float countDown;
    private float minImpact;
    private boolean isImpacted;

    @SuppressWarnings("unused")
    public ImpactBomb() {
    }

    public ImpactBomb(UsageModel<?> usageModel, boolean mirrored) {
        super(usageModel, mirrored);
        ImpactBombUsageProperties properties = (ImpactBombUsageProperties) usageModel.getProperties();
        this.countDown = properties.getDelay();
        this.minImpact = properties.getMinImpact();
        this.sensitiveLayers = properties.getSensitiveLayers();
    }

    public List<Integer> getSensitiveLayers() {
        return sensitiveLayers;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        super.onStep(deltaTime, worldFacade);
        if (isImpacted && alive) {
            countDown -= deltaTime;
        }
    }

    @Override
    protected boolean isCountDone() {
        return isImpacted && countDown <= 0;
    }

    private void setImpacted() {
        isImpacted = true;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        super.dynamicMirror(physicsScene);
    }


    @Override
    public boolean isActive() {
        if (hasSafety()) {
            return super.isActive();
        }
        return true;
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        for (BombInfo bombInfo : getBombInfoList()) {
            bombInfo.setCarrierEntity(heir);
        }
        return ratio > 0.5f;
    }

    public void onImpact(float impulse, int id, WorldFacade worldFacade) {
        if(sensitiveLayers.contains(id)) {
            if (impulse > PhysicsConstants.BOMB_IMPACT_FACTOR * this.minImpact) {
                this.setImpacted();
                onStep(1/60f,worldFacade);
            }
        }
    }
}
