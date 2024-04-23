package com.evolgames.scenes;


import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.scenes.PlayScene.pause;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.properties.BodyUsageCategory;
import com.evolgames.entities.properties.usage.BowProperties;
import com.evolgames.entities.properties.usage.MotorControlProperties;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.entities.usage.Bow;
import com.evolgames.entities.usage.Drag;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.Heavy;
import com.evolgames.entities.usage.ImpactBomb;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.Missile;
import com.evolgames.entities.usage.MotorControl;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.helpers.FruitSizeGenerator;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.ToolUtils;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class PhysicsScene extends AbstractScene {

    protected final WorldFacade worldFacade;
    protected final List<GameGroup> gameGroups = new CopyOnWriteArrayList<>();
    protected Hand hand;
    protected RagDoll ragdoll;
    protected int step;

    public PhysicsScene(Camera pCamera, SceneType sceneName) {
        super(pCamera, sceneName);
        this.worldFacade = new WorldFacade(this);
        BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
        GameEntityFactory.getInstance().create(this);
        BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
        Invoker.setScene(this);
    }

    private static void setupSpecialModels(ArrayList<BodyModel> bodies) {
        List<DragModel> dragModels =
                bodies.stream()
                        .map(BodyModel::getDragModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        List<BombModel> bombModels =
                bodies.stream()
                        .map(BodyModel::getBombModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        List<ProjectileModel> projectileModels =
                bodies.stream()
                        .map(BodyModel::getProjectileModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        dragModels.forEach(
                dragModel ->
                        dragModel.setDraggedEntity(
                                bodies.stream()
                                        .filter(e -> e.getBodyId() == dragModel.getBodyId())
                                        .findAny()
                                        .orElseThrow(() -> new RuntimeException("Body not found!"))
                                        .getGameEntity()));
        projectileModels.forEach(
                projectileModel ->
                        projectileModel.setMuzzleEntity(
                                bodies.stream()
                                        .filter(e -> e.getBodyId() == projectileModel.getBodyId())
                                        .findAny()
                                        .orElseThrow(() -> new RuntimeException("Body not found!"))
                                        .getGameEntity()));
        bombModels.forEach(
                bombModel ->
                        bombModel.setCarrierEntity(
                                bodies.stream()
                                        .filter(e -> e.getBodyId() == bombModel.getBodyId())
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
    }

    public GameGroup createTool(ToolModel toolModel, final boolean mirrored) {
        ArrayList<BodyModel> bodies = toolModel.getBodies();
        ArrayList<JointModel> joints = toolModel.getJoints();
        List<GameEntity> gameEntities = new CopyOnWriteArrayList<>();
        if(toolModel.getToolCategory()== ItemCategory.PRODUCE){
            ToolUtils.scaleTool(toolModel, FruitSizeGenerator.generateSize());
        }
        //validate here:

        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getLayers().size() == 0) {
                continue;
            }
            Vector2 center = bodyModel.calculateBodyCenter();
            ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(bodyModel.getLayers(), center);
            if (blocks.size() == 0) {
                continue;
            }

            if (mirrored) {
                for (LayerBlock layerBlock : blocks) {
                    layerBlock.mirror();
                }
            }
            Init _init = bodyModel.getInit();
            BodyInit bodyInit = _init.getBodyInit();
            GameEntity gameEntity =
                    GameEntityFactory.getInstance()
                            .createGameEntity(
                                    (_init.getX() + center.x - 400) / 32f,
                                    (_init.getY() + center.y - 240) / 32f,
                                    0,
                                    mirrored,
                                    bodyInit,
                                    blocks,
                                    BodyDef.BodyType.DynamicBody,
                                    toolModel.getProperties().getToolName());
            gameEntities.add(gameEntity);
            gameEntity.setCenter(center);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setZIndex(bodyModel.getProperties().getZIndex());
        }
        bodies.removeIf(e->e.getGameEntity()==null);
        // Create game group
        GameGroup gameGroup = new GameGroup(GroupType.OTHER, gameEntities);
        this.addGameGroup(gameGroup);
        // Handle usage
        setupSpecialModels(bodies);
        // Create joints
        List<JointBlock> mainJointBlocks = new ArrayList<>();
        for (JointModel jointModel : joints) {
            JointBlock mainJointBlock = createJointFromModel(jointModel, mirrored);
            mainJointBlocks.add(mainJointBlock);
        }
        setupUsages(bodies, mainJointBlocks,this, mirrored);

        this.sortChildren();

        return gameGroup;
    }



    public void setupUsages(ArrayList<BodyModel> bodies, List<JointBlock> mainJointBlocks, PhysicsScene physicsScene, boolean mirrored) {
        bodies.forEach(bodyModel -> {
            bodyModel.getDragModels().forEach(dragModel -> {
                Drag drag = new Drag(dragModel, mirrored);
                bodyModel.getGameEntity().getUseList().add(drag);
            });
            bodyModel.getUsageModels()
                    .forEach(
                            e -> {
                                switch (e.getType()) {
                                    case HEAVY:
                                        Heavy heavy = new Heavy();
                                        bodyModel.getGameEntity().getUseList().add(heavy);
                                        break;
                                    case SHOOTER:
                                    case SHOOTER_CONTINUOUS:
                                        boolean isHeavy = bodyModel.getUsageModels().stream().anyMatch(u -> u.getType() == BodyUsageCategory.HEAVY);
                                        Shooter shooter = new Shooter(e, physicsScene, isHeavy, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(shooter);
                                        break;
                                    case BOW:
                                        BowProperties bowProperties = (BowProperties) e.getProperties();
                                        if (bowProperties.getUpper() != null && bowProperties.getMiddle() != null && bowProperties.getLower() != null) {
                                            bowProperties.getUpper().sub(bodyModel.calculateBodyCenter());
                                            bowProperties.getMiddle().sub(bodyModel.calculateBodyCenter());
                                            bowProperties.getLower().sub(bodyModel.calculateBodyCenter());
                                        }
                                        Bow bow = new Bow(e, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(bow);
                                        break;
                                    case TIME_BOMB:
                                        TimeBomb timeBomb = new TimeBomb(e, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(timeBomb);
                                        break;
                                    case IMPACT_BOMB:
                                        ImpactBomb impactBomb = new ImpactBomb(e, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(impactBomb);
                                        break;
                                    case SLASHER:
                                        Slasher slasher = new Slasher();
                                        bodyModel.getGameEntity().getUseList().add(slasher);
                                        break;
                                    case BLUNT:
                                        Smasher smasher = new Smasher();
                                        bodyModel.getGameEntity().getUseList().add(smasher);
                                        break;
                                    case STABBER:
                                        Stabber stabber = new Stabber();
                                        bodyModel.getGameEntity().getUseList().add(stabber);
                                        break;
                                    case THROWING:
                                        Throw throwable = new Throw();
                                        bodyModel.getGameEntity().getUseList().add(throwable);
                                        break;
                                    case FLAME_THROWER:
                                        FlameThrower flameThrower = new FlameThrower(e, physicsScene, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(flameThrower);
                                        break;
                                    case ROCKET:
                                        Rocket rocket = new Rocket(e, physicsScene, mirrored);
                                        rocket.setRocketBodyGameEntity(bodyModel.getGameEntity());
                                        bodyModel.getGameEntity().getUseList().add(rocket);
                                        break;
                                    case MISSILE:
                                        Missile missile = new Missile(e, physicsScene, mirrored);
                                        missile.setRocketBodyGameEntity(bodyModel.getGameEntity());
                                        bodyModel.getGameEntity().getUseList().add(missile);
                                        break;
                                    case LIQUID_CONTAINER:
                                        LiquidContainer liquidContainer = new LiquidContainer(e, physicsScene, mirrored);
                                        bodyModel.getGameEntity().getUseList().add(liquidContainer);
                                        break;
                                    case ROCKET_LAUNCHER:
                                        RocketLauncher rocketLauncher = new RocketLauncher(e, physicsScene.getWorldFacade(), mirrored);
                                        bodyModel.getGameEntity().getUseList().add(rocketLauncher);
                                        break;
                                    case MOTOR_CONTROL:
                                       MotorControlProperties motorControlProperties = (MotorControlProperties) e.getProperties();
                                        List<JointBlock> jointBlocks = mainJointBlocks.stream().filter(j -> j!=null&&motorControlProperties.getJointIds().contains(j.getJointId())).collect(Collectors.toList());
                                        MotorControl motorControl = new MotorControl(e, jointBlocks,mirrored);
                                        bodyModel.getGameEntity().getUseList().add(motorControl);
                                        break;
                                }

                            });
        });
    }

    protected void destroyGroups() {
        for (GameGroup gameGroup : this.getGameGroups()) {
            Invoker.addGroupDestructionCommand(gameGroup);
        }
        this.hand = null;
    }
    public JointBlock createJointFromModel(JointModel jointModel, boolean mirrored) {
        BodyModel bodyModel1 = jointModel.getBodyModel1();
        BodyModel bodyModel2 = jointModel.getBodyModel2();
        if (bodyModel1 == null || bodyModel2 == null) {
            return null;
        }
        GameEntity entity1 = bodyModel1.getGameEntity();
        GameEntity entity2 = bodyModel2.getGameEntity();

        if (entity1 == null || entity2 == null) {
            return null;
        }
        jointModel.translate();
        JointDef jointDef = jointModel.createJointDef(mirrored);

        return getWorldFacade().addJointToCreate(jointDef, entity1, entity2, jointModel.getJointId(),mirrored);
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
        destroyGroups();
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

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setMouseJoint(MouseJoint joint, GameEntity gameEntity, MouseJointDef jointDef) {
        if (hand != null) {
            Objects.requireNonNull(hand)
                    .setMouseJoint(joint, jointDef, gameEntity);
        }
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
        this.worldFacade.onStep();
        Invoker.onStep();
        for (GameGroup gameGroup : getGameGroups()) {
            gameGroup.onStep(pSecondsElapsed);
        }
    }

    public GameGroup createItemFromFile(String name, float x, float y, boolean assets, boolean mirrored) {
        ToolModel toolModel = loadToolModel(name, false, assets);
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(x, y).filter(OBJECT, OBJECT).build()));
        return createTool(toolModel, mirrored);
    }

    public GameGroup createItem(ToolModel toolModel, boolean mirrored) {
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(400f, 240f).filter(OBJECT, OBJECT).build()));
        return createTool(toolModel, mirrored);
    }

    public void createItemFromFile(String name, boolean assets, boolean mirrored) {
        createItem(loadToolModel(name, false, assets), mirrored);
    }

    public GameGroup createItem(float x, float y, float angle, ToolModel toolModel, boolean mirrored) {
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(x, y).angle(angle).filter(OBJECT, OBJECT).build()));
        return createTool(toolModel, mirrored);
    }


    protected void createRagDoll(float x, float y) {
        this.ragdoll = GameEntityFactory.getInstance().createRagdoll(x / 32f, y / 32f);
    }


}
