package com.evolgames.entities.serialization;

import static com.evolgames.entities.blocks.JointBlock.Position.A;
import static com.evolgames.entities.blocks.JointBlock.Position.B;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.entities.serialization.infos.JointInfo;
import com.evolgames.entities.usage.Drag;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.MeleeUse;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.entities.usage.Use;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SceneSerializer {

  private List<GameGroupSerializer> gameGroupSerializers;
  private HashMap<Integer, Hand> hands;
  private WorldFacadeSerializer worldFacadeSerializer;
  private PlayerAction playerAction;
  private PlayerSpecialAction playerSpecialAction;

  @SuppressWarnings("unused")
  SceneSerializer() {}

  SceneSerializer(AbstractScene<?> abstractScene) {
    this.gameGroupSerializers = new ArrayList<>();
    if (abstractScene instanceof PhysicsScene) {
      PhysicsScene<?> physicsScene = (PhysicsScene<?>) abstractScene;
      physicsScene
          .getGameGroups()
          .forEach(gameGroup -> gameGroupSerializers.add(new GameGroupSerializer(gameGroup)));
      this.worldFacadeSerializer = new WorldFacadeSerializer(physicsScene.getWorldFacade());
    }
    if (abstractScene instanceof PhysicsScene) {
      PhysicsScene<?> physicsScene = (PhysicsScene<?>) abstractScene;
      this.hands = physicsScene.getHands();
    }
    if (abstractScene instanceof PlayScene) {
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
    for (GameEntity gameEntity : GameEntitySerializer.entities.values()) {
      for (Use use : gameEntity.getUseList()) {
        if (use instanceof MeleeUse) {
          MeleeUse meleeUse = (MeleeUse) use;
          meleeUse
              .getTargetGameEntitiesUniqueIds()
              .forEach(
                  entityId ->
                      meleeUse
                          .getTargetGameEntities()
                          .add(GameEntitySerializer.entities.get(entityId)));
        }
        if (use instanceof Stabber) {
          Stabber stabber = (Stabber) use;
          stabber.setHand(this.hands.get(stabber.getHandId()));
        }
        if (use instanceof Smasher) {
          Smasher smasher = (Smasher) use;
          smasher.setHand(this.hands.get(smasher.getHandId()));
        }
      }
    }

    if (scene instanceof PlayScene) {
      PlayScene playScene = (PlayScene) scene;
      playScene.setSavedSpecialAction(this.playerSpecialAction);
      playScene.getUserInterface().updatePlayerSpecialActionOnSwitcher(this.playerAction);
    }
  }

  void afterCreate(PhysicsScene<?> scene) {
    this.worldFacadeSerializer
        .getNonCollidingPairs()
        .forEach(
            p -> {
              GameEntity entity1 = GameEntitySerializer.entities.get(p.first);
              GameEntity entity2 = GameEntitySerializer.entities.get(p.second);
              scene.getWorldFacade().addNonCollidingPair(entity1, entity2);
            });

    Map<String, List<JointBlock>> jointBlockMap = new HashMap<>();
    for (GameGroup gameGroup : scene.getGameGroups()) {
      for (GameEntity gameEntity : gameGroup.getGameEntities()) {
        reloadGameEntityUses(scene, gameEntity);
        for (LayerBlock layerBlock : gameEntity.getBlocks()) {
          for (AssociatedBlock<?, ?> associatedBlock : layerBlock.getAssociatedBlocks()) {
            if (associatedBlock instanceof JointBlock) {
              JointBlock jointBlock = (JointBlock) associatedBlock;
              jointBlock.setEntity(gameEntity);
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
    try {
      for (List<JointBlock> pair : jointBlockMap.values()) {
        if (pair.size() < 2) {
          continue;
        }
        JointBlock mainBlock = pair.get(0);
        mainBlock.setBrother(pair.get(1));
        mainBlock.getBrother().setBrother(mainBlock);

        JointInfo jointInfo = mainBlock.getJointInfo();
        GameEntity entity1 =
            mainBlock.getPosition() == A
                ? mainBlock.getEntity()
                : mainBlock.getBrother().getEntity();
        GameEntity entity2 =
            mainBlock.getPosition() == B
                ? mainBlock.getEntity()
                : mainBlock.getBrother().getEntity();

        if (pair.size() == 2 && mainBlock.isNotAborted()) {
          scene
              .getWorldFacade()
              .addJointToRecreate(
                  mainBlock.getJointDefFromJointInfo(jointInfo), entity1, entity2, mainBlock);
        }
      }
    } catch (Throwable throwable) {
      throw new IllegalStateException("Problem recreate-ing joints." + throwable);
    }
    worldFacadeSerializer.afterCreate(scene);
    scene.sortChildren();
  }

  private void reloadGameEntityUses(PhysicsScene<?> scene, GameEntity gameEntity) {
    if (gameEntity.hasUsage(Shooter.class)) {
      Shooter shooter = gameEntity.getUsage(Shooter.class);
      for (ProjectileInfo projectileInfo : shooter.getProjectileInfoList()) {
        projectileInfo.setMuzzleEntity(
            GameEntitySerializer.entities.get(projectileInfo.getMuzzleEntityUniqueId()));
      }
      shooter.fillMissileModels();
      shooter.createFireSources(scene.getWorldFacade());
    }
    if (gameEntity.hasUsage(Drag.class)) {
      Drag drag = gameEntity.getUsage(Drag.class);
      drag.setDraggedEntity(GameEntitySerializer.entities.get(drag.getDraggedEntityUniqueId()));
    }
    if (gameEntity.hasUsage(Rocket.class)) {
      Rocket rocket = gameEntity.getUsage(Rocket.class);

      for (FireSourceInfo fireSourceInfo : rocket.getFireSourceInfoList()) {
        fireSourceInfo.setMuzzleEntity(
                GameEntitySerializer.entities.get(fireSourceInfo.getMuzzleEntityUniqueId()));
      }
      rocket.setRocketBodyGameEntity(
              Objects.requireNonNull(
                      GameEntitySerializer.entities.get(rocket.getRocketBodyEntityUniqueId())));
      rocket.createFireSources(scene.getWorldFacade());
    }
    if (gameEntity.hasUsage(FlameThrower.class)) {
      FlameThrower flameThrower = gameEntity.getUsage(FlameThrower.class);
      for (FireSourceInfo fireSourceInfo : flameThrower.getFireSourceInfoList()) {
        fireSourceInfo.setMuzzleEntity(
                GameEntitySerializer.entities.get(fireSourceInfo.getMuzzleEntityUniqueId()));
      }
      flameThrower.createFireSources(scene.getWorldFacade());
    }


    if (gameEntity.hasUsage(TimeBomb.class)) {
      TimeBomb timeBomb = gameEntity.getUsage(TimeBomb.class);
      if (timeBomb != null) {
        timeBomb.setGameEntity(
            Objects.requireNonNull(
                GameEntitySerializer.entities.get(timeBomb.getGameEntityUniqueId())));
      }
    }
  }
}
