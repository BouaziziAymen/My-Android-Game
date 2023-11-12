package com.evolgames.entities.serialization;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.PhysicsScene;

import java.util.ArrayList;
import java.util.List;

public class GameGroupSerializer {
  List<GameEntitySerializer> gameEntitySerializerList;
  GroupType groupType;

  @SuppressWarnings("unused")
  GameGroupSerializer() {}

  GameGroupSerializer(GameGroup gameGroup) {
    this.groupType = gameGroup.getGroupType();
    gameEntitySerializerList = new ArrayList<>();
    for (GameEntity gameEntity : gameGroup.getGameEntities()) {
      gameEntitySerializerList.add(new GameEntitySerializer(gameEntity));
    }
  }

  private void afterLoad() {
    gameEntitySerializerList.forEach(GameEntitySerializer::afterLoad);
  }

  public GameGroup create(PhysicsScene<?> scene) {
    this.afterLoad();
    GameGroup gameGroup = null;
    switch (groupType){
      case GROUND:
        gameGroup = new GameGroup(groupType);
        scene.getWorldFacade().setGroundGroup(gameGroup);
        break;
      case DOLL:
        gameGroup = new RagDoll(scene);
        break;
      case OTHER:
        gameGroup = new GameGroup(groupType);
        break;
    }

    for (GameEntitySerializer gameEntitySerializer : gameEntitySerializerList) {
      GameEntity gameEntity = gameEntitySerializer.create();
      gameEntitySerializer.afterCreate(gameEntity);
      gameEntity.setScene(scene);
      gameGroup.addGameEntity(gameEntity);
      if(groupType==GroupType.DOLL){
        if (gameEntity.getType()==SpecialEntityType.Head&&GeometryUtils.isPointInPolygon(0, 9, gameEntity.getBlocks().get(0).getVertices())) {
          gameEntity.setType(SpecialEntityType.Head);
          ((RagDoll)gameGroup).setHead(gameEntity);
        }
      }
    }
    return gameGroup;
  }

  @SuppressWarnings("unused")
  public void setGameEntitySerializerList(List<GameEntitySerializer> gameEntitySerializerList) {
    this.gameEntitySerializerList = gameEntitySerializerList;
  }
}
