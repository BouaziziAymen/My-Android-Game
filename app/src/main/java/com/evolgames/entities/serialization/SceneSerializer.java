package com.evolgames.entities.serialization;

import android.util.Log;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.PhysicsScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SceneSerializer {

  private List<GameGroupSerializer> gameGroupSerializers;
  private WorldFacadeSerializer worldFacadeSerializer;

  @SuppressWarnings("unused")
  SceneSerializer() {}

  SceneSerializer(AbstractScene<?>  abstractScene) {
    gameGroupSerializers = new ArrayList<>();
    if(abstractScene instanceof PhysicsScene){
      PhysicsScene<?> physicsScene = (PhysicsScene<?>)abstractScene;
      physicsScene.getGameGroups().forEach(gameGroup -> gameGroupSerializers.add(new GameGroupSerializer(gameGroup)));
      worldFacadeSerializer = new WorldFacadeSerializer(physicsScene.getWorldFacade());
    }
  }

void create(PhysicsScene<?> scene) {
    List<GameGroup> gameGroups = new ArrayList<>();
    for (GameGroupSerializer gameGroupSerializer : gameGroupSerializers) {
      GameGroup gameGroup = gameGroupSerializer.create(scene);
      gameGroups.add(gameGroup);
    }
    scene.getGameGroups().addAll(gameGroups);
    List<TimedCommand> timedCommands = this.worldFacadeSerializer.timedCommandList;
    scene.getWorldFacade().getTimedCommands().addAll(timedCommands);
  }

  void afterCreate(PhysicsScene<?> scene) {
    Map<String, List<JointBlock>> jointBlockMap = new HashMap<>();
    for (GameGroup gameGroup : scene.getGameGroups()) {
      for (GameEntity gameEntity : gameGroup.getGameEntities()) {

        for (LayerBlock layerBlock : gameEntity.getBlocks()) {
          for (AssociatedBlock<?, ?> associatedBlock : layerBlock.getAssociatedBlocks()) {
            if (associatedBlock instanceof JointBlock) {
              JointBlock jointBlock = (JointBlock) associatedBlock;
              if (!jointBlockMap.containsKey(jointBlock.getJointUniqueId())) {
                List<JointBlock> list = new ArrayList<>();
                list.add(jointBlock);
                jointBlockMap.put(jointBlock.getJointUniqueId(), list);
                } else {
                List<JointBlock> list = jointBlockMap.get(jointBlock.getJointUniqueId());
                list.add(jointBlock);
              }
            }
          }
        }
      }
    }
    Log.e("Joint",""+jointBlockMap.values().size());
    for (List<JointBlock> pair : jointBlockMap.values()) {
      JointBlock jointBlock = pair.get(0);
      GameEntity entity1 = GameEntitySerializer.entities.get(jointBlock.getUniqueId1());
      GameEntity entity2 = GameEntitySerializer.entities.get(jointBlock.getUniqueId2());
      if (entity1 == null || entity2 == null) {
        Log.e("Joint","one entity is null");
        continue;
      }
      JointInfo jointInfo = jointBlock.getJointInfo();
      if (pair.size() == 2) {
        Log.e("Joint","create joint");
        JointCreationCommand jointCreationCommand =
            scene
                .getWorldFacade()
                .addJointToCreate(
                    jointInfo.getJointDef(), entity1, entity2, pair.get(0), pair.get(1));
        pair.forEach(jb->jb.setCommand(jointCreationCommand));
      }
    }
    worldFacadeSerializer.afterCreate(scene);
  }
}
