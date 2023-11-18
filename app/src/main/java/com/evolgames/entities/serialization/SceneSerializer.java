package com.evolgames.entities.serialization;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Use;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SceneSerializer {

  private List<GameGroupSerializer> gameGroupSerializers;
  private HashMap<Integer,Hand> hands;
  private WorldFacadeSerializer worldFacadeSerializer;
  private PlayerAction playerAction;
  private PlayerSpecialAction playerSpecialAction;

  @SuppressWarnings("unused")
  SceneSerializer() {}

  SceneSerializer(AbstractScene<?>  abstractScene) {
    this.gameGroupSerializers = new ArrayList<>();
    if(abstractScene instanceof PhysicsScene){
      PhysicsScene<?> physicsScene = (PhysicsScene<?>)abstractScene;
      physicsScene.getGameGroups().forEach(gameGroup -> gameGroupSerializers.add(new GameGroupSerializer(gameGroup)));
      this.worldFacadeSerializer = new WorldFacadeSerializer(physicsScene.getWorldFacade());
    }
    if (abstractScene instanceof PhysicsScene) {
      PhysicsScene<?> physicsScene =(PhysicsScene<?>) abstractScene;
      this.hands = physicsScene.getHands();
    }
    if(abstractScene instanceof PlayScene){
      PlayScene playScene = (PlayScene) abstractScene;
      this.playerAction = playScene.getPlayerAction();
      this.playerSpecialAction = playScene.getSpecialAction();
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
    if (scene instanceof PlayScene) {
      scene.setHands(this.hands);
      this.hands.forEach(
          (integer, hand) -> {
            hand.getHandControlStack().forEach(e -> e.setHand(hand));
            hand.setPlayScene((PlayScene) scene);
          });
    }
    for(GameEntity gameEntity:GameEntitySerializer.entities.values()){
      for(Use use: gameEntity.getUseList()){
        if(use instanceof Stabber){
          Stabber stabber = (Stabber) use;
          stabber.setHand(Objects.requireNonNull(this.hands.get(stabber.getHandId())));
        }
        if(use instanceof Smasher){
          Smasher smasher = (Smasher) use;
          smasher.setHand(Objects.requireNonNull(this.hands.get(smasher.getHandId())));
        }
      }
    }

  if(scene instanceof PlayScene){
    PlayScene playScene = (PlayScene) scene;
    playScene.setSavedSpecialAction(this.playerSpecialAction);
    playScene.getUserInterface().updatePlayerSpecialActionOnSwitcher(this.playerAction);
  }
  }

  void afterCreate(PhysicsScene<?> scene) {
    this.worldFacadeSerializer.getNonCollidingPairs().forEach(p->{
      GameEntity entity1 = GameEntitySerializer.entities.get(p.first);
      GameEntity entity2 = GameEntitySerializer.entities.get(p.second);
      scene.getWorldFacade().addNonCollidingPair(entity1,entity2);
    });

    HashMap<JointBlock,LayerBlock> jointBlockLayerBlockHashMap = new HashMap<>();
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
                jointBlockLayerBlockHashMap.put(jointBlock,layerBlock);
                jointBlockMap.put(jointBlock.getJointUniqueId(), list);
                } else {
                List<JointBlock> list = jointBlockMap.get(jointBlock.getJointUniqueId());
                list.add(jointBlock);
                jointBlockLayerBlockHashMap.put(jointBlock,layerBlock);
              }
            }
          }
        }
      }
    }
    Log.e("Joint",""+jointBlockMap.values().size());
    for (List<JointBlock> pair : jointBlockMap.values()) {
      JointBlock jointBlock1 = pair.get(0);
      GameEntity entity1 = GameEntitySerializer.entities.get(jointBlock1.getJointInfo().getEntity1());
      GameEntity entity2 = GameEntitySerializer.entities.get(jointBlock1.getJointInfo().getEntity2());

      JointInfo jointInfo1 = jointBlock1.getJointInfo();
      jointInfo1.setJointBlock(jointBlock1);
      Log.e("Joint","pair size: "+pair.size());
      if (pair.size() == 2) {
        JointBlock jointBlock2 = pair.get(1);
        Log.e("Joint","create joint");
        JointCreationCommand jointCreationCommand =
            scene
                .getWorldFacade()
                .addJointToCreate(
                    jointInfo1.getJointDef(), entity1, entity2, jointBlock1, jointBlock2);
        jointBlock2.getJointInfo().setJointBlock(jointBlock2);
        pair.forEach(jb->jb.setCommand(jointCreationCommand));
      } else if (jointBlock1.getJointType()== JointDef.JointType.MouseJoint){
        GameEntity ground = scene.getWorldFacade().getGround().getGameEntityByIndex(0);
        JointCreationCommand jointCreationCommand =
                scene
                        .getWorldFacade()
                        .addJointToCreate(
                                jointInfo1.getJointDef(), ground, entity2, null,jointBlock1);
        pair.forEach(jb->jb.setCommand(jointCreationCommand));
      } else {
        jointBlock1.setAborted(true);
        Objects.requireNonNull(jointBlockLayerBlockHashMap.get(jointBlock1)).getAssociatedBlocks().remove(jointBlock1);
       // throw new IllegalStateException("Problem recreate-ing joints");
      }
    }
    worldFacadeSerializer.afterCreate(scene);
  }
}
