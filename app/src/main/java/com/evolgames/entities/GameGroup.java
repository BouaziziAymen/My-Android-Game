package com.evolgames.entities;

import com.evolgames.entities.commandtemplate.commands.Command;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameGroup {

  private final ArrayList<GameEntity> entities;
  private final ArrayList<Command> commands;
  private final GroupType groupType;

  public GameGroup(GroupType groupType, GameEntity... groupOfEntities) {
    this.groupType = groupType;
    entities = new ArrayList<>();
    commands = new ArrayList<>();
    entities.addAll(Arrays.asList(groupOfEntities));
    for (GameEntity entity : groupOfEntities) {
      entity.setParentGroup(this);
    }
  }

  public GameGroup(GroupType groupType,ArrayList<GameEntity> groupOfEntities) {
    this.groupType = groupType;
    entities = groupOfEntities;
    commands = new ArrayList<>();
    for (GameEntity entity : groupOfEntities) {
      entity.setParentGroup(this);
    }
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
