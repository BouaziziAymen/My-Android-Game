package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.ItemCategory;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.properties.ToolProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.HandShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolModel extends ProperModel<ToolProperties> implements Serializable {
    private final GameScene scene;
    private final AtomicInteger bodyCounter = new AtomicInteger();
    private final AtomicInteger jointCounter = new AtomicInteger();
    private final AtomicInteger handCounter = new AtomicInteger();
    private final ArrayList<BodyModel> bodies;
    private final ArrayList<JointModel> joints;
    private ItemCategory toolCategory;
    public static BodyModel groundBodyModel;

    public ToolModel(GameScene gameScene, int toolId) {
        super("Tool" + toolId);
        scene = gameScene;
        bodies = new ArrayList<>();

        joints = new ArrayList<>();
        groundBodyModel = new BodyModel(-1);

        groundBodyModel.setModelName("Ground");
    }

    public BodyModel createNewBody() {
        BodyModel bodyModel = new BodyModel(bodyCounter.getAndIncrement());
        bodies.add(bodyModel);
        return bodyModel;
    }

    private BodyModel getBodyById(int bodyId) {
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getBodyId() == bodyId) return bodyModel;
        }
        return null;
    }

    public void swapLayers(int bodyId, int index1, int index2) {
        Objects.requireNonNull(getBodyById(bodyId)).swapLayers(index1, index2);
        updateMesh();
    }

    public ProjectileModel createNewProjectile(ProjectileShape projectileShape, int bodyId) {
        int projectileId = Objects.requireNonNull(getBodyById(bodyId)).getProjectileCounter().getAndIncrement();
        ProjectileModel projectileModel = new ProjectileModel(bodyId, projectileId, projectileShape);
        getBodyModelById(bodyId).getProjectiles().add(projectileModel);
        return projectileModel;
    }

    public JointModel createJointModel(JointShape jointShape, JointDef jointDef) {
        int jointId = jointCounter.getAndIncrement();
        JointModel jointModel = new JointModel(jointId, jointDef, jointShape);
        joints.add(jointModel);
        return jointModel;
    }

    public JointModel removeJoint(int jointId) {
        JointModel jointModel = null;
        for (JointModel j : joints) {
            if (j.getJointId() == jointId) {
                jointModel = j;
                break;
            }
        }
        if (jointModel != null)
            joints.remove(jointModel);
        return jointModel;
    }

    public LayerModel createNewLayer(int bodyId) {
        return Objects.requireNonNull(getBodyById(bodyId), "invalid bodyId").createLayer();
    }

    public LayerModel getLayerModelById(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).getLayerModelById(layerId);
    }

    public ArrayList<BodyModel> getBodies() {
        return bodies;
    }


    public void selectJoint(int jointId) {
        getJointById(jointId).selectJoint();
        for (JointModel jointModel : joints) {
            if (jointId != jointModel.getJointId()){
                jointModel.deselect();
            }
        }

    }

    public JointModel getJointById(int jointId) {
        for (JointModel jointModel : joints) {
            if (jointModel.getJointId() == jointId) {
                return jointModel;
            }
        }
        return null;
    }


    public DecorationModel createNewDecoration(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).createNewDecroation(layerId);
    }

    public void removeBody(int bodyId) {
        bodies.remove(getBodyById(bodyId));
    }

    public void removeLayer(int bodyId, int layerId) {
        System.out.println("Delete "+this.hashCode());
        Objects.requireNonNull(getBodyById(bodyId)).removeLayer(layerId);
    }

    public DecorationModel removeDecoration(int bodyId, int layerId, int decorationId) {
        return Objects.requireNonNull(getBodyById(bodyId)).removeDecoration(layerId, decorationId);
    }

    public DecorationModel getDecorationModelById(int primaryKey, int secondaryKey, int tertiaryKey) {
        return getLayerModelById(primaryKey, secondaryKey).getDecorationById(tertiaryKey);
    }

    public int getNewDecorationId(int primaryKey, int secondaryKey) {
        return getLayerModelById(primaryKey, secondaryKey).getNewDecorationId();
    }

    private final ArrayList<MosaicMesh> meshes = new ArrayList<>();

    public void updateMesh() {
        for (MosaicMesh mesh : meshes) mesh.detachSelf();
        meshes.clear();
        GameScene.plotter2.detachChildren();
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getLayers().size() == 0) continue;
            Vector2 center = GeometryUtils.calculateCentroid(bodyModel.getLayers().get(0).getPoints());
            ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(bodyModel, center);
            MosaicMesh mesh = MeshFactory.getInstance().createMosaicMesh(center.x, center.y, 0, blocks);
            scene.attachChild(mesh);
            meshes.add(mesh);

        }
        scene.sortChildren();

    }

    public void createTool() {
        ArrayList<GameEntity> gameEntities = new ArrayList<>();
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getLayers().size() == 0) {
                continue;
            }
            List<List<Vector2>> list = new ArrayList<>();
            for (LayerModel layerModel : bodyModel.getLayers()) {
                list.add(layerModel.getPoints());
            }

            Vector2 center = GeometryUtils.calculateCenter(list);
            ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(bodyModel, center);
            if (blocks.size() == 0 || center == null){
                return;
            }
            BodyInit bodyInit = new TransformInit(new BodyInitImpl(),center.x / 32F, center.y / 32F, 0);
            GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(center.x / 32F, center.y / 32F, 0, bodyInit,blocks, BodyDef.BodyType.DynamicBody, "created", bodyModel.getProjectiles());
            gameEntities.add(gameEntity);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setCenter(center);

           // createHands(bodyModel, gameEntity);
        }
        GameGroup gameGroup = new GameGroup(gameEntities);
        scene.addGameGroup(gameGroup);
        for (GameEntity entity : gameEntities){
            scene.attachChild(entity.getMesh());
        }
        scene.sortChildren();
        groundBodyModel.setGameEntity(scene.getGround());


        for (JointModel jointModel : joints) {
            createJointFromModel(jointModel);
        }
    }

    private void createHands(BodyModel bodyModel, GameEntity gameEntity) {
        for (HandModel hand : bodyModel.getHands()) {
            Vector2 u1 = hand.getHandShape().getCenter().cpy().sub(gameEntity.getCenter());
            Vector2 u2 = hand.getHandShape().getCenter().cpy().sub(GameEntityFactory.getInstance().hand.getBody().getWorldCenter().cpy().mul(1 / 32f));
            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.localAnchorA.set(u1.mul(1 / 32f));
            revoluteJointDef.localAnchorB.set(u2.mul(0));
            revoluteJointDef.enableLimit = true;
            revoluteJointDef.lowerAngle = 0f;
            revoluteJointDef.upperAngle = 0f;
            Vector2 u = hand.getHandShape().getDir().nor();
            revoluteJointDef.referenceAngle = (float) Math.atan2(-u.y, u.x);
            revoluteJointDef.collideConnected = false;
            //scene.getWorldFacade().addJointToCreate(revoluteJointDef, gameEntity, GameEntityFactory.getInstance().hand);
        }
    }

    private void createJointFromModel(JointModel jointModel) {
        BodyModel bodyModel1 = jointModel.getBodyModel1();
        BodyModel bodyModel2 = jointModel.getBodyModel2();

        GameEntity entity1 = bodyModel1.getGameEntity();
        GameEntity entity2 = bodyModel2.getGameEntity();

        if (entity1 == null || entity2 == null){
            return;
        }
        if(jointModel.getJointShape()!=null) {
            Vector2 u1 = jointModel.getJointShape().getBegin().cpy().sub(entity1.getCenter());
            Vector2 u2 = jointModel.getJointShape().getEnd().cpy().sub(entity2.getCenter());

            if (jointModel.getJointDef() instanceof RevoluteJointDef) {
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointModel.getJointDef();
                revoluteJointDef.localAnchorA.set(u1.mul(1 / 32f));
                revoluteJointDef.localAnchorB.set(u2.mul(1 / 32f));

            } else if (jointModel.getJointDef() instanceof WeldJointDef) {
                WeldJointDef weldJointDef = (WeldJointDef) jointModel.getJointDef();
                weldJointDef.localAnchorA.set(u1.mul(1 / 32f));
                weldJointDef.localAnchorB.set(u2.mul(1 / 32f));
            } else if (jointModel.getJointDef() instanceof DistanceJointDef) {
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointModel.getJointDef();
                distanceJointDef.localAnchorA.set(u1.mul(1 / 32f));
                distanceJointDef.localAnchorB.set(u2.mul(1 / 32f));

            } else if (jointModel.getJointDef() instanceof PrismaticJointDef) {
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointModel.getJointDef();
                prismaticJointDef.localAnchorA.set(u1.mul(1 / 32f));
                prismaticJointDef.localAnchorB.set(u2.mul(1 / 32f));
            }
        }
        scene.getWorldFacade().addJointToCreate(jointModel.getJointDef(), entity1, entity2);
    }


    public void deselectJoint(int jointId) {
        Objects.requireNonNull(getJointById(jointId)).deselect();
    }


    public ArrayList<JointModel> getJoints() {
        return joints;
    }

    public BodyModel getBodyModelById(int bodyId) {
        System.out.println("bodyId:" + bodyId);
        for (BodyModel bodyModel : bodies) if (bodyModel.getBodyId() == bodyId) return bodyModel;
        return null;
    }


    public ProjectileModel getProjectileById(int primaryKey, int secondaryKey) {
        Optional<ProjectileModel> res = getBodyModelById(primaryKey).getProjectiles().stream().filter(e -> e.getProjectileId() == secondaryKey).findFirst();
        return res.orElse(null);
    }

    public HandModel getHandById(int primaryKey, int secondaryKey) {
        Optional<HandModel> res = getBodyModelById(primaryKey).getHands().stream().filter(e -> e.getHandId() == secondaryKey).findFirst();
        return res.orElse(null);
    }

    public void removeProjectile(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getProjectiles().removeIf(e -> e.getProjectileId() == secondaryKey);
    }

    public HandModel createNewHand(HandShape handShape, int bodyId) {
        int handId = handCounter.getAndIncrement();
        HandModel handModel = new HandModel(bodyId, handId, handShape);
        getBodyModelById(bodyId).getHands().add(handModel);
        return handModel;
    }


    public ItemCategory getToolCategory() {
        return toolCategory;
    }

    public void setToolCategory(ItemCategory toolCategory) {
        this.toolCategory = toolCategory;
    }



    public AtomicInteger getBodyCounter() {
        return bodyCounter;
    }

    public AtomicInteger getJointCounter() {
        return jointCounter;
    }

    public int getSelectedBodyId() {
        for(BodyModel bodyModel:bodies){
            if(bodyModel.isSelected()){
                return bodyModel.getBodyId();
            }
        }
        return -1;
    }
}

