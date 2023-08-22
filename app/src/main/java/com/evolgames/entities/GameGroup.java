package com.evolgames.entities;

import com.evolgames.entities.commandtemplate.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class GameGroup {
    private final ArrayList<GameEntity> entities;
    private final ArrayList<Command> commands;
    private final boolean living;

    public boolean isLiving() {
        return living;
    }

    public GameGroup(boolean living, GameEntity ... groupOfEntities){
        entities = new ArrayList<>();
        commands = new ArrayList<>();
        entities.addAll(Arrays.asList(groupOfEntities));
        for(GameEntity entity:groupOfEntities) {
            entity.setParentGroup(this);
        }
        this.living = living;
    }
    public GameGroup(GameEntity ... groupOfEntities){
       this(false,groupOfEntities);
    }
    public GameGroup(ArrayList<GameEntity> groupOfEntities){
        entities = groupOfEntities;
        commands = new ArrayList<>();
        for(GameEntity entity:groupOfEntities) {
            entity.setParentGroup(this);
        }
        living = false;
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


    public void onStep(float timeStep) {
        List<GameEntity> clone = new ArrayList<>(entities);
        for (GameEntity entity : clone) {
            entity.onStep(timeStep);
        }
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
