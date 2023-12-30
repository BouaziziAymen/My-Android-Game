package com.evolgames.entities.usage;

import com.evolgames.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

public class ImpactBomb extends Bomb{

  private float countDown;
  private float minImpact;
  private boolean isImpacted;

    @SuppressWarnings("Unused")
    public ImpactBomb() {
    }
    public ImpactBomb(UsageModel<?> usageModel) {
        super(usageModel);
        ImpactBombUsageProperties properties = (ImpactBombUsageProperties) usageModel.getProperties();
        this.countDown = properties.getDelay();
        this.minImpact = properties.getMinImpact();
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        super.onStep(deltaTime, worldFacade);
        if (isImpacted&&alive) {
            countDown -= deltaTime;
        }
    }

    @Override
    protected boolean isBombOn() {
        return isImpacted&&countDown<0;
    }

    public void setImpacted(boolean impacted) {
        isImpacted = impacted;
    }

    public float getMinImpact() {
        return minImpact;
    }

    @Override
    public PlayerSpecialAction getAction() {
        return null;
    }
}
