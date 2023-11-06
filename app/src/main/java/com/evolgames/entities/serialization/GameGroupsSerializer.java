package com.evolgames.entities.serialization;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.scenes.GameScene;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameGroupsSerializer {

    private List<GameGroupSerializer> gameGroupSerializers;

    @SuppressWarnings("unused")
    GameGroupsSerializer(){}

    GameGroupsSerializer(List<GameGroup> gameGroups){
        gameGroupSerializers = new ArrayList<>();
        gameGroupSerializers.add(new GameGroupSerializer(gameGroups.get(0)));
        gameGroupSerializers.add(new GameGroupSerializer(gameGroups.get(1)));
        for(GameGroup gameGroup:gameGroups){
           // gameGroupSerializers.add(new GameGroupSerializer(gameGroup));
       }
    }
    List<GameGroup> create(){
        List<GameGroup> gameGroups = new ArrayList<>();
        for(GameGroupSerializer gameGroupSerializer:gameGroupSerializers){
            GameGroup gameGroup = gameGroupSerializer.create();
            gameGroups.add(gameGroup);
        }
        return gameGroups;
    }
    void afterCreate(GameScene gameScene, List<GameGroup> gameGroups){
        HashSet<String> jointUniqueIds = new HashSet<>();
        List<JointBlock> jointBlockList = new ArrayList<>();
        for(GameGroup gameGroup:gameGroups){
        for(GameEntity gameEntity:gameGroup.getGameEntities()){
            for(LayerBlock layerBlock:gameEntity.getBlocks()) {
                for (AssociatedBlock<?, ?> associatedBlock : layerBlock.getAssociatedBlocks()) {
                    if (associatedBlock instanceof JointBlock) {
                        JointBlock jointBlock = (JointBlock) associatedBlock;
                        if (!jointUniqueIds.contains(jointBlock.getJointUniqueId())) {
                            jointUniqueIds.add(jointBlock.getJointUniqueId());
                            jointBlockList.add(jointBlock);
                        }
                    }
                }
            }
            }
        }
        for(JointBlock jointBlock:jointBlockList){
            GameEntity entity1 = GameEntitySerializer.entities.get(jointBlock.getUniqueId1());
            GameEntity entity2 = GameEntitySerializer.entities.get(jointBlock.getUniqueId2());
            if(entity1==null||entity2==null){
                continue;
            }
            JointInfo jointInfo = jointBlock.getJointInfo();
            gameScene.getWorldFacade().addJointToCreate(jointInfo.getJointDef(), entity1, entity2, false);
        }
    }

    public void setGameGroupSerializers(List<GameGroupSerializer> gameGroupSerializers) {
        this.gameGroupSerializers = gameGroupSerializers;
    }
}
