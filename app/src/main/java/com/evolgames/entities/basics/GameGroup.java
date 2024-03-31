package com.evolgames.entities.basics;

import com.evolgames.entities.commandtemplate.commands.Command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameGroup {

    private final List<GameEntity> entities;
    private final List<Command> commands;
    private final GroupType groupType;
    private String uniqueID = UUID.randomUUID().toString();

    public GameGroup(GroupType groupType, GameEntity... groupOfEntities) {
        this.groupType = groupType;
        entities = new CopyOnWriteArrayList<>();
        commands = new CopyOnWriteArrayList<>();
        entities.addAll(Arrays.asList(groupOfEntities));
        for (GameEntity entity : groupOfEntities) {
            entity.setParentGroup(this);
        }
    }

    public GameGroup(GroupType groupType, List<GameEntity> groupOfEntities) {
        this.groupType = groupType;
        entities = groupOfEntities;
        commands = new CopyOnWriteArrayList<>();
        for (GameEntity entity : groupOfEntities) {
            entity.setParentGroup(this);
        }
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void addGameEntity(GameEntity entity) {
        entities.add(entity);
        entity.setParentGroup(this);
    }

    public GameEntity getGameEntityByIndex(int index) {
        return entities.get(index);
    }

    public List<GameEntity> getGameEntities() {
        return entities;
    }

    public void onStep(float timeStep) {
        for (GameEntity entity : entities) {
            entity.onStep(timeStep);
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public float getMass() {
        float result = 0;
        for (GameEntity gameEntity : entities) {
            if (gameEntity.isAlive() && gameEntity.getBody() != null) {
                result += gameEntity.getBody().getMass();
            }
        }
        return result;
    }
}
