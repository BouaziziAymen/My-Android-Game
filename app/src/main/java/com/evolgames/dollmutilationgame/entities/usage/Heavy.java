package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

import java.util.List;

public class Heavy extends Use {
    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {

    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return ratio > 0.7f;
    }
}
