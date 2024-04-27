package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MeleeUse extends Use {
    protected transient final List<GameEntity> targetGameEntities = new ArrayList<>();
    protected List<String> targetGameEntitiesUniqueIds;

    public List<GameEntity> getTargetGameEntities() {
        return targetGameEntities;
    }

    public void prepareToSerialize() {
        targetGameEntitiesUniqueIds = targetGameEntities.stream().map(GameEntity::getUniqueID).collect(Collectors.toList());
    }

    public List<String> getTargetGameEntitiesUniqueIds() {
        return targetGameEntitiesUniqueIds;
    }
}
