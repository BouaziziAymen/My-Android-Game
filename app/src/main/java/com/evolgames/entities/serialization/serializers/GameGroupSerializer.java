package com.evolgames.entities.serialization.serializers;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.basics.SpecialEntityType;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.List;

public class GameGroupSerializer {
    List<GameEntitySerializer> gameEntitySerializerList;
    GroupType groupType;

    @SuppressWarnings("unused")
    GameGroupSerializer() {
    }

    public GameGroupSerializer(GameGroup gameGroup) {
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
        switch (groupType) {
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
            if (groupType == GroupType.DOLL) {
                if (gameEntity.getType() == SpecialEntityType.Head && GeometryUtils.isPointInPolygon(0, 9, gameEntity.getBlocks().get(0).getVertices())) {
                    gameEntity.setType(SpecialEntityType.Head);
                    ((RagDoll) gameGroup).setHead(gameEntity);
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
