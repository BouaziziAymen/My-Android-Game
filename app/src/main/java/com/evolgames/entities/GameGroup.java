package com.evolgames.entities;

import java.util.ArrayList;
import java.util.Arrays;

public class GameGroup {
    private ArrayList<GameEntity> entities;
    public GameGroup(GameEntity ... groupOfEntities){
        entities = new ArrayList<>();
        entities.addAll(Arrays.asList(groupOfEntities));
        for(GameEntity entity:groupOfEntities)
            entity.setParentGroup(this);

    }
    public GameGroup(ArrayList<GameEntity> groupOfEntities){
        entities = groupOfEntities;
        for(GameEntity entity:groupOfEntities)
            entity.setParentGroup(this);

    }

    public void addGameEntity(GameEntity entity) {
        entities.add(entity);
        entity.setParentGroup(this);
    }
    public GameEntity getGameEntityByIndex(int index){
        return entities.get(index);
    }

    public ArrayList<GameEntity> getGameEntities() {
        return entities;
    }


    public void update() {
        for (GameEntity entity : entities) entity.update();
    }
}
