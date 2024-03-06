package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_BACK_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_FRONT_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import static com.evolgames.scenes.PlayScene.pause;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.entities.usage.Drag;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.ImpactBomb;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.Missile;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class PhysicsScene<T extends UserInterface<?>> extends AbstractScene<T> {

    protected final WorldFacade worldFacade;
    protected final List<GameGroup> gameGroups = new CopyOnWriteArrayList<>();
    protected HashMap<Integer, Hand> hands = new HashMap<>();
    protected RagDoll ragdoll;
    protected int step;

    public PhysicsScene(Camera pCamera, SceneType sceneName) {
        super(pCamera, sceneName);
        this.worldFacade = new WorldFacade(this);
        BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
        GameEntityFactory.getInstance().create(this.worldFacade.getPhysicsWorld(), this);
        BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
        Invoker.setScene(this);
    }

    protected GameGroup createTool(ToolModel toolModel, float x, float y) {
        ArrayList<BodyModel> bodies = toolModel.getBodies();
        ArrayList<JointModel> joints = toolModel.getJoints();
        List<GameEntity> gameEntities = new CopyOnWriteArrayList<>();
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getLayers().size() == 0) {
                continue;
            }
            List<List<Vector2>> list = new ArrayList<>();
            for (LayerModel layerModel : bodyModel.getLayers()) {
                list.add(layerModel.getPoints());
            }

            Vector2 center = GeometryUtils.calculateCenter(list);
            ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(bodyModel.getLayers(), center);
            if (blocks.size() == 0 || center == null) {
                continue;
            }
            BodyInit bodyInit = new BulletInit(
                            new TransformInit(
                                    new BodyInitImpl(
                                            (short)
                                                    (OBJECTS_MIDDLE_CATEGORY
                                                            | OBJECTS_BACK_CATEGORY
                                                            | OBJECTS_FRONT_CATEGORY)),
                                    (x+center.x-400) / 32F,
                                    (y+center.y-240) / 32F,
                                    0),
                            bodyModel.isBullet());
            GameEntity gameEntity =
                    GameEntityFactory.getInstance()
                            .createGameEntity(
                                    (x+center.x-400) / 32F,
                                    (y+center.y-240) / 32F,
                                    0,
                                    bodyInit,
                                    blocks,
                                    BodyDef.BodyType.DynamicBody,
                                    "weapon");
            gameEntities.add(gameEntity);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setCenter(center);
            bodyModel
                    .getDragModels()
                    .forEach(
                            e -> {
                                Drag drag = new Drag(e);
                                gameEntity.getUseList().add(drag);
                                drag.setDraggedEntity(gameEntity);
                            });
        }
        // Handle usage
        List<ProjectileModel> projectileModels =
                bodies.stream()
                        .map(BodyModel::getProjectileModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        projectileModels.forEach(
                projectileModel ->
                        projectileModel.setMuzzleEntity(
                                bodies.stream()
                                        .filter(e -> e.getBodyId() == projectileModel.getBodyId())
                                        .findAny()
                                        .orElseThrow(() -> new RuntimeException("Body not found!"))
                                        .getGameEntity()));
        List<FireSourceModel> fireSourceModels =
                bodies.stream()
                        .map(BodyModel::getFireSourceModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        fireSourceModels.forEach(
                fireSourceModel ->
                        fireSourceModel.setMuzzleEntity(
                                bodies.stream()
                                        .filter(e -> e.getBodyId() == fireSourceModel.getBodyId())
                                        .findAny()
                                        .orElseThrow(() -> new RuntimeException("Body not found!"))
                                        .getGameEntity()));

        List<LiquidSourceModel> liquidSourceModels =
                bodies.stream()
                        .map(BodyModel::getLiquidSourceModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        liquidSourceModels.forEach(
                liquidSourceModel -> {
                    liquidSourceModel.setContainerEntity(
                            bodies.stream()
                                    .filter(e -> e.getBodyId() == liquidSourceModel.getBodyId())
                                    .findAny()
                                    .orElseThrow(() -> new RuntimeException("Body not found!"))
                                    .getGameEntity());
                    liquidSourceModel.setSealEntity(
                            bodies.stream()
                                    .filter(e -> e.getBodyId() == liquidSourceModel.getProperties().getSealBodyId())
                                    .findAny()
                                    .orElseThrow(() -> new RuntimeException("Body not found!"))
                                    .getGameEntity());
                });

        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(
                                        e ->
                                                e.getType() == BodyUsageCategory.SHOOTER
                                                        || e.getType() == BodyUsageCategory.SHOOTER_CONTINUOUS)
                                .forEach(
                                        e -> {
                                            Shooter shooter = new Shooter(e, this);
                                            usageBodyModel.getGameEntity().getUseList().add(shooter);
                                        }));

        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(
                                        e ->
                                                e.getType() == BodyUsageCategory.ROCKET_LAUNCHER)
                                .forEach(
                                        e -> {
                                            RocketLauncher rocketLauncher = new RocketLauncher(e, worldFacade);
                                            usageBodyModel.getGameEntity().getUseList().add(rocketLauncher);
                                        }));

        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.TIME_BOMB)
                                .forEach(
                                        e -> {
                                            TimeBomb timeBomb = new TimeBomb(e);
                                            usageBodyModel.getGameEntity().getUseList().add(timeBomb);
                                            timeBomb.setGameEntity(usageBodyModel.getGameEntity());
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.IMPACT_BOMB)
                                .forEach(
                                        e -> {
                                            ImpactBomb impactBomb = new ImpactBomb(e);
                                            usageBodyModel.getGameEntity().getUseList().add(impactBomb);
                                            impactBomb.setGameEntity(usageBodyModel.getGameEntity());
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.SLASHER)
                                .forEach(
                                        e -> {
                                            Slasher slasher = new Slasher();
                                            usageBodyModel.getGameEntity().getUseList().add(slasher);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.STABBER)
                                .forEach(
                                        e -> {
                                            Stabber stabber = new Stabber();
                                            usageBodyModel.getGameEntity().getUseList().add(stabber);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.FLAME_THROWER)
                                .forEach(
                                        e -> {
                                            FlameThrower flameThrower = new FlameThrower(e, this);
                                            usageBodyModel.getGameEntity().getUseList().add(flameThrower);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.BLUNT)
                                .forEach(
                                        e -> {
                                            Smasher smasher = new Smasher();
                                            usageBodyModel.getGameEntity().getUseList().add(smasher);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.LIQUID_CONTAINER)
                                .forEach(
                                        e -> {
                                            LiquidContainer liquidContainer = new LiquidContainer(e, this);
                                            usageBodyModel.getGameEntity().getUseList().add(liquidContainer);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.THROWING)
                                .forEach(
                                        e -> {
                                            Throw throwable = new Throw();
                                            usageBodyModel.getGameEntity().getUseList().add(throwable);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.ROCKET)
                                .forEach(
                                        e -> {
                                            Rocket rocket = new Rocket(e, this);
                                            rocket.setRocketBodyGameEntity(usageBodyModel.getGameEntity());
                                            usageBodyModel.getGameEntity().getUseList().add(rocket);
                                        }));
        bodies.forEach(
                usageBodyModel ->
                        usageBodyModel.getUsageModels().stream()
                                .filter(e -> e.getType() == BodyUsageCategory.MISSILE)
                                .forEach(
                                        e -> {
                                            Missile missile = new Missile(e, this);
                                            missile.setRocketBodyGameEntity(usageBodyModel.getGameEntity());
                                            usageBodyModel.getGameEntity().getUseList().add(missile);
                                        }));

        // Create game group
        GameGroup gameGroup = new GameGroup(GroupType.OTHER, gameEntities);
        this.addGameGroup(gameGroup);
        this.sortChildren();
        // Create joints
        for (JointModel jointModel : joints) {
                createJointFromModel(jointModel);
        }
        return gameGroup;
    }
    public void createJointFromModel(JointModel jointModel) {
        BodyModel bodyModel1 = jointModel.getBodyModel1();
        BodyModel bodyModel2 = jointModel.getBodyModel2();

        GameEntity entity1 = bodyModel1.getGameEntity();
        GameEntity entity2 = bodyModel2.getGameEntity();

        if (entity1 == null || entity2 == null) {
            return;
        }
        JointDef jointDef = jointModel.createJointDef(entity1.getCenter(), entity2.getCenter());

        getWorldFacade().addJointToCreate(jointDef, entity1, entity2);
    }

    public WorldFacade getWorldFacade() {
        return worldFacade;
    }

    public PhysicsWorld getPhysicsWorld() {
        return getWorldFacade().getPhysicsWorld();
    }

    @Override
    public void populate() {
    }

    @Override
    public void detach() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    public List<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public void addGameGroup(GameGroup gameGroup) {
        gameGroups.add(gameGroup);
    }

    public HashMap<Integer, Hand> getHands() {
        return hands;
    }

    public void setHands(HashMap<Integer, Hand> hands) {
        this.hands = hands;
    }

    public void setMouseJoint(MouseJoint joint, GameEntity gameEntity, MouseJointDef jointDef) {
        if (hands.get(gameEntity.getHangedPointerId()) != null) {
            Objects.requireNonNull(hands.get(gameEntity.getHangedPointerId()))
                    .setMouseJoint(joint, jointDef, gameEntity);
        }
    }

    public void onDestroyMouseJoint(MouseJoint j) {
        Optional<Hand> hand = hands.values().stream().filter(e -> e.getMouseJoint() == j).findFirst();
        hand.ifPresent(Hand::onMouseJointDestroyed);
    }

    public GameEntity getGameEntityByUniqueId(String uniqueId) {
        for (GameGroup gameGroup : gameGroups) {
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (gameEntity.getUniqueID().equals(uniqueId)) {
                    return gameEntity;
                }
            }
        }
        return null;
    }
    protected void onManagedUpdate(float pSecondsElapsed) {
        step++;
        if (!pause) {
            super.onManagedUpdate(1 / 60f);
        }
        this.worldFacade.onStep(pSecondsElapsed);
        Invoker.onStep();
        for (GameGroup gameGroup : getGameGroups()) {
            gameGroup.onStep(pSecondsElapsed);
        }
    }
    public GameGroup createItem(String name, float x, float y, boolean assets) {
        return createTool(loadToolModel(name, false, assets), x, y);
    }

    public GameGroup createItem(ToolModel toolModel) {
      return createTool(toolModel,400,240);
    }

    protected void createRagDoll(float x, float y) {
        this.ragdoll = GameEntityFactory.getInstance().createRagdoll(x / 32f, y / 32f);
    }

    public void createItem(String name, boolean assets) {
      createItem(loadToolModel(name,false,assets));
    }
}
