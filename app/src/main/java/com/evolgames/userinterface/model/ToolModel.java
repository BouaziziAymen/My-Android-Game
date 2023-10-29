package com.evolgames.userinterface.model;

import static com.evolgames.physics.CollisionConstants.GUN_CATEGORY;
import static com.evolgames.physics.CollisionConstants.GUN_MASK;

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
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.entities.usage.Trigger;
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
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ToolModel extends ProperModel<ToolProperties> implements Serializable {
    private final GameScene scene;
    private final AtomicInteger bodyCounter = new AtomicInteger();
    private final AtomicInteger jointCounter = new AtomicInteger();
    private final AtomicInteger projectileCounter = new AtomicInteger();
    private final AtomicInteger ammoCounter = new AtomicInteger();
    private final AtomicInteger bombCounter = new AtomicInteger();
    private final ArrayList<BodyModel> bodies;
    private final ArrayList<JointModel> joints;
    private ItemCategory toolCategory;


    public ToolModel(GameScene gameScene, int toolId) {
        super("Tool" + toolId);
        scene = gameScene;
        bodies = new ArrayList<>();
        joints = new ArrayList<>();

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
        getBodyModelById(bodyId).getProjectileModels().add(projectileModel);
        return projectileModel;
    }

    public JointModel createJointModel(JointShape jointShape, JointDef jointDef) {
        int jointId = jointCounter.getAndIncrement();
        JointModel jointModel = new JointModel(jointId, jointDef, jointShape);
        joints.add(jointModel);
        return jointModel;
    }

    public void removeJoint(int jointId) {
        JointModel jointModel = null;
        for (JointModel j : joints) {
            if (j.getJointId() == jointId) {
                jointModel = j;
                break;
            }
        }
        if (jointModel != null) {
            joints.remove(jointModel);
        }
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
        return Objects.requireNonNull(getBodyById(bodyId)).createNewDecoration(layerId);
    }

    public void removeBody(int bodyId) {
        bodies.remove(getBodyById(bodyId));
    }

    public void removeLayer(int bodyId, int layerId) {
        Objects.requireNonNull(getBodyById(bodyId)).removeLayer(layerId);
    }
    public void removeAmmo(int bodyId, int ammoId) {
        BodyModel body = getBodyModelById(bodyId);
        body.getCasingModels().remove(body.getAmmoModelById(ammoId));
    }

    public DecorationModel removeDecoration(int bodyId, int layerId, int decorationId) {
        return Objects.requireNonNull(getBodyById(bodyId)).removeDecoration(layerId, decorationId);
    }

    public DecorationModel getDecorationModelById(int primaryKey, int secondaryKey, int tertiaryKey) {
        return getLayerModelById(primaryKey, secondaryKey).getDecorationById(tertiaryKey);
    }

    private final ArrayList<MosaicMesh> meshes = new ArrayList<>();

    public void updateMesh() {
        for (MosaicMesh mesh : meshes){
            mesh.detachSelf();
        }
        meshes.clear();
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getLayers().size() == 0) continue;
            if(bodyModel.getLayers().stream().allMatch(e->e.getPoints().size()<3))continue;
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
            BodyInit bodyInit = new BulletInit(new TransformInit(new BodyInitImpl(GUN_CATEGORY,GUN_MASK),center.x / 32F, center.y / 32F, 0),false);
            GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(center.x / 32F, center.y / 32F, 0, bodyInit,blocks, BodyDef.BodyType.DynamicBody, "weapon");
            gameEntities.add(gameEntity);
            bodyModel.setGameEntity(gameEntity);
            gameEntity.setCenter(center);
            bodyModel.getBombModels().forEach(bombModel -> bombModel.setGameEntity(gameEntity));
            bodyModel.getProjectileModels().forEach(p->{
                ProjectileProperties properties = p.getProperties();
                if(properties.getFireRatio()>=0.1f||properties.getSmokeRatio()>=0.1f||properties.getSparkRatio()>=0.1f){
                   Vector2 end = properties.getProjectileEnd();
                   Vector2 dir = end.cpy().sub(properties.getProjectileOrigin()).nor();
                   Vector2 nor = new Vector2(-dir.y,dir.x);
                   Vector2 e = end.cpy().sub(gameEntity.getCenter());
                   float extent = ToolUtils.getAxisExtent(p.getMissileModel(),nor)/2f;
                   ExplosiveParticleWrapper fireSource = scene.getWorldFacade().createFireSource(gameEntity,e.cpy().sub(extent*nor.x,extent*nor.y),e.cpy().add(extent*nor.x,extent*nor.y), PhysicsConstants.getProjectileVelocity(properties.getMuzzleVelocity())/10f,properties.getFireRatio(),properties.getSmokeRatio(),properties.getSparkRatio(),0.1f,2000);
                   fireSource.setSpawnEnabled(false);
                   p.setFireSource(fireSource);
               }
            });
        }
        // Handle usage
        List<ProjectileModel> projectileModels = bodies.stream().map(BodyModel::getProjectileModels).flatMap(Collection::stream).collect(Collectors.toList());
        projectileModels.forEach(projectileModel -> projectileModel.setMuzzleEntity(bodies.stream().filter(e -> e.getBodyId() == projectileModel.getBodyId()).findAny().orElseThrow(() -> new RuntimeException("Body not found!")).getGameEntity()));

        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.RANGED_MANUAL||e.getType()==BodyUsageCategory.RANGED_SEMI_AUTOMATIC||e.getType()==BodyUsageCategory.RANGED_AUTOMATIC).forEach(e->{
            Trigger trigger = new Trigger(e);
            usageBodyModel.getGameEntity().getUseList().add(trigger);
        }));


        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.TIME_BOMB).forEach(e->{
            TimeBomb timeBomb = new TimeBomb(e,scene.getWorldFacade());
            usageBodyModel.getGameEntity().getUseList().add(timeBomb);
        }));
        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.SLASHER).forEach(e->{
            Slasher slasher = new Slasher();
            usageBodyModel.getGameEntity().getUseList().add(slasher);
        }));
        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.STABBER).forEach(e->{
            Stabber stabber = new Stabber();
            usageBodyModel.getGameEntity().getUseList().add(stabber);
        }));
        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.BLUNT).forEach(e->{
            Smasher smasher = new Smasher();
            usageBodyModel.getGameEntity().getUseList().add(smasher);
        }));
        bodies.forEach(usageBodyModel -> usageBodyModel.getUsageModels().stream().filter(e->e.getType()==BodyUsageCategory.THROWING).forEach(e->{
            Throw throwable = new Throw();
            usageBodyModel.getGameEntity().getUseList().add(throwable);
        }));

        //Create joints
        for (JointModel jointModel : this.joints) {
            createJointFromModel(jointModel);
        }
        // Create game group
        GameGroup gameGroup = new GameGroup(gameEntities);
        scene.addGameGroup(gameGroup);
        scene.sortChildren();
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
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getBodyId() == bodyId){
                return bodyModel;
            }
        }
        return null;
    }

    public CasingModel getAmmoById(int primaryKey, int secondaryKey) {
        return getBodyModelById(primaryKey).getCasingModels().stream().filter(e -> e.getCasingId() == secondaryKey).findAny().orElse(null);
    }
    public ProjectileModel getProjectileById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getProjectileModels().stream().filter(e -> e.getProjectileId() == modelId).findFirst().orElse(null);
    }

    public BombModel getBombById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getBombModels().stream().filter(e -> e.getBombId() == modelId).findAny().orElse(null);
    }

    public void removeProjectile(int primaryKey, int modelId) {
        getBodyModelById(primaryKey).getProjectileModels().removeIf(e -> e.getProjectileId() == modelId);
    }
    public void removeBomb(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getBombModels().removeIf(e -> e.getBombId() == secondaryKey);
    }


    public ItemCategory getToolCategory() {
        return toolCategory;
    }

    public void setToolCategory(ItemCategory toolCategory) {
        this.toolCategory = toolCategory;
    }


    public AtomicInteger getProjectileCounter() {
        return projectileCounter;
    }

    public AtomicInteger getAmmoCounter() {
        return ammoCounter;
    }

    public AtomicInteger getBodyCounter() {
        return bodyCounter;
    }

    public AtomicInteger getJointCounter() {
        return jointCounter;
    }

    public AtomicInteger getBombCounter() {
        return bombCounter;
    }


    public CasingModel createNewAmmo(CasingShape ammoShape, int bodyId) {
        int ammoId = ammoCounter.getAndIncrement();
        CasingModel ammoModel = new CasingModel(bodyId, ammoId, ammoShape);
        getBodyModelById(bodyId).getCasingModels().add(ammoModel);
        return ammoModel;
    }



    public BombModel createNewBomb(BombShape bombShape, int bodyId) {
       int bombId = bombCounter.getAndIncrement();
       BombModel bombModel = new BombModel(bodyId,bombId,bombShape);
        getBodyModelById(bodyId).getBombModels().add(bombModel);
        bombShape.bindModel(bombModel);
        return  bombModel;
    }

}

