package com.evolgames.entities.serialization;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import java.util.ArrayList;
import java.util.List;

public class GameGroupSerializer {
    List<GameEntitySerializer> gameEntitySerializerList ;

    @SuppressWarnings("unused")
    GameGroupSerializer(){

    }
    GameGroupSerializer(GameGroup gameGroup){
        gameEntitySerializerList = new ArrayList<>();
        for(GameEntity gameEntity:gameGroup.getGameEntities()){
            gameEntitySerializerList.add(new GameEntitySerializer(gameEntity));
        }
    }

     private void afterLoad() {
        gameEntitySerializerList.forEach(GameEntitySerializer::afterLoad);
    }

    public GameGroup create() {
        this.afterLoad();
        GameGroup gameGroup = new GameGroup();
        for (GameEntitySerializer gameEntitySerializer : gameEntitySerializerList) {
            GameEntity gameEntity = gameEntitySerializer.create();
            gameEntitySerializer.afterCreate(gameEntity);
            gameGroup.addGameEntity(gameEntity);
        }
        return gameGroup;
    }

    @SuppressWarnings("unused")
    public void setGameEntitySerializerList(List<GameEntitySerializer> gameEntitySerializerList) {
        this.gameEntitySerializerList = gameEntitySerializerList;
    }
}
