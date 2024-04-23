package com.evolgames.entities.serialization.serializers;

import static com.evolgames.entities.blocks.JointBlock.Position.A;
import static com.evolgames.entities.blocks.JointBlock.Position.B;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.PlayerAction;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.entities.serialization.infos.JointInfo;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.entities.usage.Bomb;
import com.evolgames.entities.usage.Bow;
import com.evolgames.entities.usage.Drag;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.MeleeUse;
import com.evolgames.entities.usage.MotorControl;
import com.evolgames.entities.usage.Muzzle;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Use;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;

import org.andengine.engine.camera.SmoothCamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SceneSerializer {

    private List<GameGroupSerializer> gameGroupSerializers;
    public static Map<String, JointBlock> mainJointBlocks = new HashMap<>();
    private Hand hand;
    private WorldFacadeSerializer worldFacadeSerializer;
    private PlayerAction playerAction;
    private PlayerSpecialAction playerSpecialAction;

    private String grabbedEntityUniqueId;
    private String selectedEntityUniqueId;

    private float camX, camY;


    @SuppressWarnings("unused")
    SceneSerializer() {
    }

    public SceneSerializer(AbstractScene abstractScene) {
        this.gameGroupSerializers = new ArrayList<>();
        if (abstractScene instanceof PhysicsScene) {
            PhysicsScene physicsScene = (PhysicsScene) abstractScene;
            physicsScene
                    .getGameGroups()
                    .forEach(gameGroup -> gameGroupSerializers.add(new GameGroupSerializer(gameGroup)));
            this.worldFacadeSerializer = new WorldFacadeSerializer(physicsScene.getWorldFacade());
        }
        if (abstractScene instanceof PhysicsScene) {
            PhysicsScene physicsScene = (PhysicsScene) abstractScene;
            this.hand = physicsScene.getHand();
        }
        if (abstractScene instanceof PlayScene) {
            PlayScene playScene = (PlayScene) abstractScene;
            this.playerAction = playScene.getPlayerAction();
            this.playerSpecialAction = playScene.getSpecialAction();
            if(playScene.getHand()!=null&&playScene.getHand().getGrabbedEntity()!=null) {
                this.grabbedEntityUniqueId = playScene.getHand().getGrabbedEntity().getUniqueID();
            }
            if(playScene.getHand()!=null&&playScene.getHand().getSelectedEntity()!=null) {
                this.selectedEntityUniqueId = playScene.getHand().getSelectedEntity().getUniqueID();
            }
        }
        this.camX = abstractScene.getCamera().getCenterX();
        this.camY = abstractScene.getCamera().getCenterY();
    }

    private static void reloadRocket(PhysicsScene scene, Rocket rocket) {
        for (FireSourceInfo fireSourceInfo : rocket.getFireSourceInfoList()) {
            fireSourceInfo.setMuzzleEntity(GameEntitySerializer.entities.get(fireSourceInfo.getMuzzleEntityUniqueId()));
        }
        rocket.setRocketBodyGameEntity(Objects.requireNonNull(GameEntitySerializer.entities.get(rocket.getRocketBodyEntityUniqueId())));
        rocket.createFireSources(scene.getWorldFacade());
    }

    private static void resetMuzzles(List<ProjectileInfo> list) {
        for (ProjectileInfo projectileInfo : list) {
            GameEntity muzzleEntity = GameEntitySerializer.entities.get(projectileInfo.getMuzzleEntityUniqueId());
            if (muzzleEntity != null) {
                Muzzle muzzle = muzzleEntity.getUseList().stream().filter(e -> e instanceof Muzzle)
                        .map(e -> (Muzzle) e).filter(e -> Objects.equals(e.getProjectileInfoUniqueId(), projectileInfo.getProjectileInfoUniqueId()))
                        .findFirst().orElse(null);
                if (muzzle != null) {
                    muzzle.setMuzzleEntity(muzzleEntity);
                    projectileInfo.setMuzzle(muzzle);
                    muzzle.setProjectileInfo(projectileInfo);
                }
            }
        }
    }

    public void create(PhysicsScene scene) {
        List<GameGroup> gameGroups = new ArrayList<>();
        for (GameGroupSerializer gameGroupSerializer : gameGroupSerializers) {
            GameGroup gameGroup = gameGroupSerializer.create(scene);
            gameGroups.add(gameGroup);
        }
        scene.getGameGroups().addAll(gameGroups);
        List<TimedCommand> timedCommands = this.worldFacadeSerializer.timedCommandList;
        scene.getWorldFacade().getTimedCommands().addAll(timedCommands);
        if (scene instanceof PlayScene) {
            if (hand != null) {
                scene.setHand(this.hand);
                hand.getHandControlStack().forEach(e -> e.setHand(hand));
                hand.setPlayScene((PlayScene) scene);
            }
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
                    stabber.setHand(this.hand);
                }
                if (use instanceof Smasher) {
                    Smasher smasher = (Smasher) use;
                    smasher.setHand(Objects.requireNonNull(this.hand));
                }
            }
        }
    }

    public void afterCreate(PhysicsScene scene) {
        //reset the non colliding pairs
        this.worldFacadeSerializer
                .getNonCollidingPairs()
                .forEach(
                        p -> {
                            GameEntity entity1 = GameEntitySerializer.entities.get(p.first);
                            GameEntity entity2 = GameEntitySerializer.entities.get(p.second);
                            scene.getWorldFacade().addNonCollidingPair(entity1, entity2);
                        });
        //reset the joint blocks
        Map<String, List<JointBlock>> jointBlockMap = new HashMap<>();
        for (GameGroup gameGroup : scene.getGameGroups()) {
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
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
                                assert list != null;
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
                mainJointBlocks.put(mainBlock.getJointUniqueId(),mainBlock);
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


        for(GameGroup gameGroup:scene.getGameGroups()){
            for(GameEntity gameEntity:gameGroup.getEntities()){
                reloadGameEntityUses(scene, gameEntity);
            }
        }
        worldFacadeSerializer.afterCreate(scene);
        if(scene instanceof PlayScene){
            PlayScene playScene = (PlayScene) scene;
            if(playScene.getHand()!=null) {
                if(this.grabbedEntityUniqueId!=null) {
                    playScene.getHand().setGrabbedEntity(GameEntitySerializer.entities.get(this.grabbedEntityUniqueId));
                }
                if(this.selectedEntityUniqueId!=null) {
                    playScene.getHand().setSelectedEntity(GameEntitySerializer.entities.get(this.selectedEntityUniqueId));
                }
            }
            playScene.onUsagesUpdated();
            playScene.setPlayerAction(this.playerAction);
            playScene.onOptionSelected(this.playerSpecialAction);
        }
        ((SmoothCamera)ResourceManager.getInstance().firstCamera).setCenterDirect(camX,camY);
        scene.sortChildren();
    }

    private void reloadGameEntityUses(PhysicsScene scene, GameEntity gameEntity) {
        if (gameEntity.hasUsage(Shooter.class)) {
            Shooter shooter = gameEntity.getUsage(Shooter.class);
            shooter.getProjectileInfoList().forEach(projectileInfo -> projectileInfo.setUpdatedMuzzle(true));
            resetMuzzles(shooter.getProjectileInfoList());
            shooter.fillMissileModels();
            shooter.createFireSources(scene.getWorldFacade());
        }
        if (gameEntity.hasUsage(RocketLauncher.class)) {
            RocketLauncher rocketLauncher = gameEntity.getUsage(RocketLauncher.class);
            resetMuzzles(rocketLauncher.getProjectileInfoList());
            Map<ProjectileInfo, Rocket> rocketsMap = new HashMap<>();
            rocketLauncher.getProjectileInfoList().forEach(projectileInfo -> {
                GameEntity rocketEntity = GameEntitySerializer.entities.get(projectileInfo.getRocketEntityUniqueId());
                if (rocketEntity != null) {
                    Rocket rocket = rocketEntity.getUsage(Rocket.class);
                    rocket.setRocketBodyGameEntity(rocketEntity);
                    rocketsMap.put(projectileInfo, rocket);
                }
            });
            rocketLauncher.setRockets(rocketsMap);
            rocketLauncher.createFireSources(scene.getWorldFacade());
        }

        if (gameEntity.hasUsage(Bow.class)) {
            Bow bow = gameEntity.getUsage(Bow.class);
            resetMuzzles(bow.getProjectileInfoList());
            Map<ProjectileInfo, GameGroup> arrowsMap = new HashMap<>();
            bow.getProjectileInfoList().forEach(projectileInfo -> {
                GameGroup arrowGroup = GameGroupSerializer.groups.get(projectileInfo.getArrowGroupUniqueId());
                if (arrowGroup != null) {
                    arrowsMap.put(projectileInfo, arrowGroup);
                }
            });
            bow.setArrows(arrowsMap);
            bow.drawBowstring();
        }


        if (gameEntity.hasUsage(LiquidContainer.class)) {
            LiquidContainer liquidContainer = gameEntity.getUsage(LiquidContainer.class);
            liquidContainer.getLiquidSourceInfoList().forEach(
                    liquidSourceInfo -> {
                        liquidSourceInfo.setSealEntity(GameEntitySerializer.entities.get(liquidSourceInfo.getSealEntityUniqueId()));
                    });

            liquidContainer.getLiquidSourceInfoList().forEach(
                    liquidSourceInfo -> {
                        liquidSourceInfo.setContainerEntity(GameEntitySerializer.entities.get(liquidSourceInfo.getContainerEntityUniqueId()));
                    });

            liquidContainer.createLiquidSources(scene.getWorldFacade());
        }
        if (gameEntity.hasUsage(Drag.class)) {
            Drag drag = gameEntity.getUsage(Drag.class);
            drag.getDragInfo().setDraggedEntity(GameEntitySerializer.entities.get(drag.getDragInfo().getDraggedEntityUniqueId()));
        }
        if (gameEntity.hasUsage(Rocket.class)) {
            Rocket rocket = gameEntity.getUsage(Rocket.class);
            reloadRocket(scene, rocket);
        }
        if (gameEntity.hasUsage(FlameThrower.class)) {
            FlameThrower flameThrower = gameEntity.getUsage(FlameThrower.class);
            for (FireSourceInfo fireSourceInfo : flameThrower.getFireSourceInfoList()) {
                fireSourceInfo.setMuzzleEntity(
                        GameEntitySerializer.entities.get(fireSourceInfo.getMuzzleEntityUniqueId()));
            }
            flameThrower.createFireSources(scene.getWorldFacade());
        }
        if (gameEntity.hasUsage(Bomb.class)) {
            Bomb bomb = gameEntity.getUsage(Bomb.class);
            for (BombInfo bombInfo : bomb.getBombInfoList()) {
                bombInfo.setCarrierEntity(
                        GameEntitySerializer.entities.get(bombInfo.getCarrierEntityUniqueId()));
            }
        }
        if (gameEntity.hasUsage(MotorControl.class)) {
            MotorControl motorControl = gameEntity.getUsage(MotorControl.class);
            List<JointBlock> jointBlocks = motorControl.getJointBlockUniqueIds().stream().map(e -> SceneSerializer.mainJointBlocks.get(e)).collect(Collectors.toList());
           motorControl.setJointBlocks(jointBlocks);
        }
    }
}
