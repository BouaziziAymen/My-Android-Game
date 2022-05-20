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
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.DecorationBlockConcrete;
import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.factories.BlockFactory;
import com.evolgames.factories.GameEntityFactory;
import com.evolgames.factories.MeshFactory;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.mesh.mosaic.MosaicMesh;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.HandShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ToolModel extends ProperModel implements Serializable {
    private final GameScene scene;
    private final AtomicInteger bodyCounter = new AtomicInteger();
    private final AtomicInteger jointCounter = new AtomicInteger();
    private final AtomicInteger projectileCounter = new AtomicInteger();
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
        int projectileId = projectileCounter.getAndIncrement();
        ProjectileModel projectileModel = new ProjectileModel(bodyId, projectileId, projectileShape);
        getBodyModelById(bodyId).getProjectiles().add(projectileModel);
        return projectileModel;
    }

    public JointModel createNewJoint(JointShape jointShape, JointDef jointDef) {
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

    public LayerPointsModel createNewLayer(int bodyId) {
        return Objects.requireNonNull(getBodyById(bodyId), "invalid bodyId").createLayer();
    }

    public LayerPointsModel getLayerModelById(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).getLayerModelById(layerId);
    }

    public ArrayList<BodyModel> getBodies() {
        return bodies;
    }

    public void selectJoint(int jointId) {
        getJointById(jointId).selectJoint();
        for (JointModel jointModel : joints)
            if (jointId != jointModel.getJointId()) jointModel.deselect();

    }

    public JointModel getJointById(int jointId) {
        for (JointModel jointModel : joints)
            if (jointModel.getJointId() == jointId) return jointModel;
        return null;
    }


    public DecorationPointsModel createNewDecoration(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).createNewDecroation(layerId);
    }

    public void removeBody(int bodyId) {
        bodies.remove(getBodyById(bodyId));
    }

    public void removeLayer(int bodyId, int layerId) {
        System.out.println("Delete "+this.hashCode());
        Objects.requireNonNull(getBodyById(bodyId)).removeLayer(layerId);
    }

    public DecorationPointsModel removeDecoration(int bodyId, int layerId, int decorationId) {
        return Objects.requireNonNull(getBodyById(bodyId)).removeDecoration(layerId, decorationId);
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Game Entity: \n");
        for (int i = 0; i < bodies.size(); i++) {
            s.append(bodies.get(i).toString()).append("\n");
        }
        return s.toString();
    }


    public DecorationPointsModel getDecorationModelById(int primaryKey, int secondaryKey, int tertiaryKey) {
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
            ArrayList<BlockA> blocks = createBlocks(bodyModel, center);
            if (blocks.size() == 0) continue;


            MosaicMesh mesh = MeshFactory.getInstance().createMosaicMesh(center.x, center.y, 0, blocks);
            scene.attachChild(mesh);
            meshes.add(mesh);

        }
        scene.sortChildren();

    }

    public void initBodyCounter(int bodyMaxId) {
        bodyCounter.set(bodyMaxId);
    }


    private ArrayList<BlockA> createBlocks(BodyModel bodyModel, Vector2 center) {
        ArrayList<BlockA> blocks = new ArrayList<>();
        for (LayerPointsModel layerModel : bodyModel.getLayers()) {
            Vector2[] layerPointsArray = layerModel.getOutlinePoints();
            if (layerPointsArray == null || layerPointsArray.length < 3) continue;

            BlockAProperties layerProperty = (BlockAProperties) layerModel.getProperty();

            ArrayList<Vector2> list = Utils.translatedPoints(layerPointsArray, center);
            BlockA block = BlockFactory.createBlockA(list, layerProperty.getCopy(), layerModel.getLayerId(), bodyModel.getLayers().indexOf(layerModel));
            blocks.add(block);

            for (DecorationPointsModel decorationModel : layerModel.getDecorations()) {
                DecorationBlockConcrete decorationBlock = new DecorationBlockConcrete();
                if (decorationModel.getOutlinePoints() != null) {
                    decorationBlock.initialization(Utils.translatedPoints(decorationModel.getOutlinePoints(), center), (DecorationProperties) decorationModel.getProperty(), decorationModel.getDecorationId(), true);
                    block.addAssociatedBlock(decorationBlock);
                }
            }
        }


        return blocks;
    }

    public void createTool() {
        try {
            ToolUtils.saveToolModel(this, scene.getActivity());
        } catch (IOException e) {
            System.out.println("Error saving tool model.");
        }

        ArrayList<GameEntity> gameEntities = new ArrayList<>();
        for (BodyModel bodyModel : bodies) {
            ArrayList<ArrayList<Vector2>> list = new ArrayList<>();
            for (LayerPointsModel layerModel : bodyModel.getLayers()) {
                list.add(layerModel.getPoints());
            }
            if (list.size() == 0) continue;
            Vector2 center = GeometryUtils.calculateCenter(list);
            ArrayList<BlockA> blocks = createBlocks(bodyModel, center);
            if (blocks.size() == 0) return;
            GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(Objects.requireNonNull(center).x / 32F, center.y / 32F, 0, blocks, BodyDef.BodyType.DynamicBody, "created");
            gameEntities.add(gameEntity);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setCenter(center);

            for (HandModel hand : bodyModel.getHands()) {
                Vector2 u1 = hand.getHandShape().getCenter().cpy().sub(gameEntity.getCenter());
                Vector2 u2 = hand.getHandShape().getCenter().cpy().sub(GameEntityFactory.getInstance().hand.getBody().getWorldCenter().cpy().mul(1 / 32f));
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(u1.mul(1 / 32f));
                revoluteJointDef.localAnchorB.set(u2.mul(0));
                revoluteJointDef.enableLimit = true;
                revoluteJointDef.lowerAngle = (float) 0f;
                revoluteJointDef.upperAngle = (float) 0f;
                Vector2 u = hand.getHandShape().getDir().nor();
                revoluteJointDef.referenceAngle = (float) Math.atan2(-u.y, u.x);
                revoluteJointDef.collideConnected = false;
                scene.getWorldFacade().addJointToCreate(revoluteJointDef, gameEntity, GameEntityFactory.getInstance().hand, true);
            }
        }
        GameGroup gameGroup = new GameGroup(gameEntities);
        scene.addGameGroup(gameGroup);
        for (GameEntity entity : gameEntities) scene.attachChild(entity.getMesh());
        scene.sortChildren();
        groundBodyModel.setGameEntity(scene.getGround());

//creation of joints from model
        for (JointModel jointModel : joints) {
            BodyModel bodyModel1 = jointModel.getBodyModel1();
            BodyModel bodyModel2 = jointModel.getBodyModel2();
            // Log.e("JointModel","creation:"+bodyModel1.getBodyModelName()+"/"+bodyModel2.getBodyModelName());
            GameEntity entity1 = bodyModel1.getGameEntity();
            GameEntity entity2 = bodyModel2.getGameEntity();

            if (entity1 == null || entity2 == null) continue;

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
            scene.getWorldFacade().addJointToCreate(jointModel.getJointDef(), entity1, entity2, true);

        }
    }


    public void deselectJoint(int jointId) {
        Objects.requireNonNull(getJointById(jointId)).deselect();
    }


    public ArrayList<JointModel> getJointModels() {
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

    public int getBodyCount() {
        return bodyCounter.get();
    }

    public void resetSelection() {
        bodies.forEach(BodyModel::deselect);
    }

    public ItemCategory getToolCategory() {
        return toolCategory;
    }

    public void setToolCategory(ItemCategory toolCategory) {
        this.toolCategory = toolCategory;
    }

    public void setLayersOutlinesVisible(boolean b) {
        for(BodyModel bodyModel:bodies){
            for(LayerPointsModel layerPointsModel:bodyModel.getLayers()){
                layerPointsModel.getPointsShape().setVisible(b);
            }
        }
    }
}
