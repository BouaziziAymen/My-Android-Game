package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;

import java.util.List;

public abstract class Use {

    protected boolean active;

    public abstract void onStep(float deltaTime, WorldFacade worldFacade);

    public abstract List<PlayerSpecialAction> getActions();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract void dynamicMirror(PhysicsScene physicsScene);

    public void onAfterMirror(PhysicsScene scene) {

    }

    public abstract boolean inheritedBy(GameEntity biggestSplinter, float ratio);
}
