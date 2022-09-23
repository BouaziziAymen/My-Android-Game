package com.evolgames.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GameGroup {
    private final ArrayList<GameEntity> entities;
    private final HashSet<GameEntity> addedEntities;
    public GameGroup(GameEntity ... groupOfEntities){
        entities = new ArrayList<>();
        addedEntities = new HashSet<>();
        entities.addAll(Arrays.asList(groupOfEntities));
        for(GameEntity entity:groupOfEntities)
            entity.setParentGroup(this);
    }
    public GameGroup(ArrayList<GameEntity> groupOfEntities){
        entities = groupOfEntities;
        addedEntities = new HashSet<>();
        for(GameEntity entity:groupOfEntities)
            entity.setParentGroup(this);
    }

    public void addGameEntity(GameEntity entity) {
        addedEntities.add(entity);
        entity.setParentGroup(this);
    }
    public GameEntity getGameEntityByIndex(int index){
        return entities.get(index);
    }

    public ArrayList<GameEntity> getGameEntities() {
        return entities;
    }


    public void onStep(float timeStep) {
        for (GameEntity entity : entities) entity.onStep(timeStep);
        entities.addAll(addedEntities);
        addedEntities.clear();
    }
}
