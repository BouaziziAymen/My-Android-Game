package com.evolgames.entities;

import com.evolgames.entities.commandtemplate.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GameGroup {
    private final ArrayList<GameEntity> entities;
    private final HashSet<GameEntity> addedEntities;
    private final ArrayList<Command> commands;
    public GameGroup(GameEntity ... groupOfEntities){
        entities = new ArrayList<>();
        addedEntities = new HashSet<>();
        commands = new ArrayList<>();
        entities.addAll(Arrays.asList(groupOfEntities));
        for(GameEntity entity:groupOfEntities)
            entity.setParentGroup(this);
    }
    public GameGroup(ArrayList<GameEntity> groupOfEntities){
        entities = groupOfEntities;
        addedEntities = new HashSet<>();
        commands = new ArrayList<>();
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
        for (GameEntity entity : entities){
            entity.onStep(timeStep);
        }
        entities.addAll(addedEntities);
        addedEntities.clear();
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
