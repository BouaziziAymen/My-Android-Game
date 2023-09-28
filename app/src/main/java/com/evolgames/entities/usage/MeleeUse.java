package com.evolgames.entities.usage;

import com.evolgames.entities.GameEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class MeleeUse extends Use{
    private final List<GameEntity> targetGameEntities = new ArrayList<>();

    public  List<GameEntity> getTargetGameEntities() {
        return targetGameEntities;
    }

}
