package com.evolgames.scenes;


import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.scenes.PlayScene.pause;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.GeometryUtils;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class PhysicsScene<T extends UserInterface<?>> extends AbstractScene<T> {

    protected final WorldFacade worldFacade;
    protected final List<GameGroup> gameGroups = new CopyOnWriteArrayList<>();
    protected Hand hand = null;
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

    private static void setupModels(ArrayList<BodyModel> bodies) {
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
                                    (_init.getX() + center.x - 400) / 32F,
                                    (_init.getY() + center.y - 240) / 32F,
                                   0,
                                    mirrored,
                                    bodyInit,
                                    blocks,
                                    BodyDef.BodyType.DynamicBody,
                                    toolModel.getProperties().getToolName());
            gameEntities.add(gameEntity);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setCenter(center);
        }
        // Handle usage
        setupModels(bodies);
        bodies.forEach(b -> b.setupUsages(this,b.getGameEntity().getCenter(), mirrored));
        // Create game group
        GameGroup gameGroup = new GameGroup(GroupType.OTHER, gameEntities);
        this.addGameGroup(gameGroup);
        this.sortChildren();
        // Create joints
        for (JointModel jointModel : joints) {
            createJointFromModel(jointModel, mirrored);
        }
        //   GameEntity gameEntity = gameGroup.getGameEntityByIndex(0);
        // getWorldFacade().applyLiquidStain(gameEntity, 40, -7, gameEntity.getBlocks().get(1), Color.RED, 0f, 0, false);
        //gameEntity.redrawStains();

        return gameGroup;
    }

    public void createJointFromModel(JointModel jointModel, boolean mirrored) {
        BodyModel bodyModel1 = jointModel.getBodyModel1();
        BodyModel bodyModel2 = jointModel.getBodyModel2();
        if(bodyModel1==null||bodyModel2==null){
            return;
        }
        GameEntity entity1 = bodyModel1.getGameEntity();
        GameEntity entity2 = bodyModel2.getGameEntity();

        if (entity1 == null || entity2 == null) {
            return;
        }
        JointDef jointDef = jointModel.createJointDef(entity1.getCenter(), entity2.getCenter(), mirrored);

        getWorldFacade().addJointToCreate(jointDef, entity1, entity2,jointModel.getJointId());
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

    public void onDestroyMouseJoint(MouseJoint j) {
        if (hand != null) {
            hand.onMouseJointDestroyed();
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
        this.worldFacade.onStep(pSecondsElapsed);
        Invoker.onStep();
        for (GameGroup gameGroup : getGameGroups()) {
            gameGroup.onStep(pSecondsElapsed);
        }
    }

    public GameGroup createItemFromFile(String name, float x, float y, boolean assets, boolean mirrored) {
       ToolModel toolModel = loadToolModel(name, false, assets);
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(x,y).filter(OBJECT,OBJECT).build()));
        return createTool(toolModel, mirrored);
    }

    public GameGroup createItem(ToolModel toolModel, boolean mirrored) {
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(400f,240f).filter(OBJECT,OBJECT).build()));
        return createTool(toolModel, mirrored);
    }

    public void createItemFromFile(String name, boolean assets, boolean mirrored) {
        createItem(loadToolModel(name, false, assets), mirrored);
    }

    public GameGroup createItem(float x, float y, float angle, ToolModel toolModel, boolean mirrored) {
        toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit( new Init.Builder(x,y).angle(angle).filter(OBJECT,OBJECT).build()));
        return createTool(toolModel, mirrored);
    }


    protected void createRagDoll(float x, float y) {
        this.ragdoll = GameEntityFactory.getInstance().createRagdoll(x / 32f, y / 32f);
    }


}
