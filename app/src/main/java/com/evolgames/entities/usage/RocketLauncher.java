package com.evolgames.entities.usage;

import static com.evolgames.entities.usage.Rocket.FORCE_FACTOR;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blockvisitors.utilities.GeometryUtils;
import com.evolgames.entities.blockvisitors.utilities.ToolUtils;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class RocketLauncher extends Use {
    private List<ProjectileInfo> projectileInfoList;
    private float reloadTime;
    private boolean loading = false;
    private float loadingTimer;
    transient private List<ToolModel> rocketModels;
    private transient Map<ProjectileInfo, Rocket> rockets;
    private boolean initialized = false;
    private transient Map<ProjectileInfo, ExplosiveParticleWrapper> projInfFireSourceMap;

    @SuppressWarnings("unused")
    public RocketLauncher() {
    }

    public RocketLauncher(UsageModel<?> rangedUsageModel, WorldFacade worldFacade) {
        RocketLauncherProperties rocketLauncherProperties = (RocketLauncherProperties) rangedUsageModel.getProperties();
        this.projectileInfoList =
                rocketLauncherProperties.getProjectileModelList().stream()
                        .map(ProjectileModel::toProjectileInfo)
                        .collect(Collectors.toList());
        fillMissileModels();
        createFireSources(worldFacade);
        this.reloadTime = rocketLauncherProperties.getReloadTime();

    }

    public void setRockets(Map<ProjectileInfo, Rocket> rockets) {
        this.rockets = rockets;
    }

    public List<ProjectileInfo> getProjectileInfoList() {
        return projectileInfoList;
    }

    public void fillMissileModels() {
        this.rocketModels = new ArrayList<>();
        for (ProjectileInfo projectileInfo : this.projectileInfoList) {
            try {
                ToolModel toolModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile());
                rocketModels.add(toolModel);
            } catch (PersistenceException | ParserConfigurationException | SAXException |
                     IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onTriggerPulled() {
        if(loading){return;}
        this.loading = true;
        this.loadingTimer = 0;
        for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
            fire(i);
        }
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (!initialized) {
            loadRockets(worldFacade);
        }
        if(this.projInfFireSourceMap!=null) {
            for (ExplosiveParticleWrapper explosiveParticleWrapper : this.projInfFireSourceMap.values()) {
                explosiveParticleWrapper.setSpawnEnabled(false);
            }
        }
        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                this.loading = false;
                loadRockets(worldFacade);
                // Reload finished
            }
        }
    }

    private void loadRockets(WorldFacade worldFacade) {
        this.rockets = new HashMap<>();
        boolean allBodiesReady = projectileInfoList.stream().map(ProjectileInfo::getMuzzleEntity).allMatch(entity -> entity!=null&&entity.getBody()!=null);
        if (allBodiesReady) {
            initialized = true;
            for (ProjectileInfo projectileInfo : projectileInfoList) {
                if (projectileInfo.getMuzzleEntity().getBody() != null) {
                    this.createRocket(projectileInfo, worldFacade.getPhysicsScene());
                }
            }
        }
    }


    private void fire(int index) {
        ProjectileInfo projectileInfo = this.projectileInfoList.get(index);

        GameEntity rocketEntity = Objects.requireNonNull(rockets.get(projectileInfo)).rocketBodyEntity;
        GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();
        muzzleEntity.getBody().getJointList().forEach(jointEdge -> {
            Body bodyA = jointEdge.joint.getBodyA();
            Body bodyB = jointEdge.joint.getBodyB();
            if (bodyA == muzzleEntity.getBody() && bodyB == rocketEntity.getBody()) {
                Invoker.addJointDestructionCommand(muzzleEntity.getParentGroup(), jointEdge.joint);
            }
        });
        computeRecoil(projectileInfo, rocketEntity, muzzleEntity);

        Objects.requireNonNull(this.rockets.get(projectileInfo)).onLaunch();
        if (projInfFireSourceMap.containsKey(projectileInfo)) {
            projInfFireSourceMap.get(projectileInfo).setSpawnEnabled(true);
        }
    }

    private void computeRecoil(ProjectileInfo projectileInfo, GameEntity rocketEntity, GameEntity muzzleEntity) {
        Vector2 endProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileEnd().cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f))
                        .cpy();
        Vector2 beginProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileOrigin().cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f))
                        .cpy();
        Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
        float muzzleVelocity =  rockets.get(projectileInfo).getPower()*FORCE_FACTOR;
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);

        Vector2 impulse =
                directionProjected.cpy().nor().mul(rocketEntity.getBody().getMass()).mul(-projectileInfo.getRecoil() * muzzleVelocityVector.len());
        muzzleEntity.getBody().applyLinearImpulse(impulse, beginProjected);
    }

    private void createRocket(ProjectileInfo projectileInfo, PhysicsScene<?> physicsScene) {
        int index = projectileInfoList.indexOf(projectileInfo);
        ToolModel rocketModel = rocketModels.get(index);
        GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();

        Vector2 begin = projectileInfo.getProjectileOrigin();
        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 localDir = end.cpy().sub(begin).nor();
        Filter projectileFilter = new Filter();
        projectileFilter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.maskBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.groupIndex = muzzleEntity.getGroupIndex();

        rocketModel.getBodies().forEach(bodyModel -> bodyModel.setBullet(true));
        JointModel jointModel = new JointModel(rocketModel.getJointCounter().getAndIncrement(), JointDef.JointType.WeldJoint);
        GameGroup rocketGroup = physicsScene.createItem(rocketModel);

        BodyModel bodyModel1 = new BodyModel(0);
        BodyModel bodyModel2 = new BodyModel(1);
        jointModel.setBodyModel1(bodyModel1);
        jointModel.setBodyModel2(bodyModel2);
        jointModel.getLocalAnchorA().set(end);
        GameEntity rocketEntity = rocketGroup.getGameEntityByIndex(0);
        jointModel.getLocalAnchorB().set(rocketEntity.getCenter());
        jointModel.setReferenceAngle((float) (GeometryUtils.calculateAngleRadians(localDir.x, localDir.y) - Math.PI / 2));
        bodyModel1.setGameEntity(muzzleEntity);
        bodyModel2.setGameEntity(rocketEntity);
        physicsScene.createJointFromModel(jointModel);
        Rocket rocket = rocketEntity.getUsage(Rocket.class);
        this.rockets.put(projectileInfo, rocket);
        physicsScene.getWorldFacade().addNonCollidingPair(rocketEntity, muzzleEntity);
        projectileInfo.setRocketEntityUniqueId(rocketEntity.getUniqueID());
      /*  ResourceManager.getInstance()
                .gunshotSounds
                .get(projectileInfo.getFireSound())
                .getSoundList()
                .get(0)
                .play();*/
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return Collections.singletonList(PlayerSpecialAction.RocketLauncher);
    }

    public void createFireSources(WorldFacade worldFacade) {
        this.projInfFireSourceMap = new HashMap<>();
        this.projectileInfoList
                .forEach(
                        p -> {
                            if (p.getFireRatio() >= 0.1f
                                    || p.getSmokeRatio() >= 0.1f
                                    || p.getSparkRatio() >= 0.1f) {
                                Vector2 end = p.getProjectileEnd();
                                Vector2 dir = end.cpy().sub(p.getProjectileOrigin()).nor();
                                Vector2 nor = new Vector2(-dir.y, dir.x);
                                Vector2 e = p.getProjectileOrigin().cpy().sub(p.getMuzzleEntity().getCenter());
                                float axisExtent = 0.1f;
                                ExplosiveParticleWrapper fireSource =
                                        worldFacade
                                                .createFireSource(
                                                        p.getMuzzleEntity(),
                                                        e.cpy().sub(axisExtent * nor.x, axisExtent * nor.y),
                                                        e.cpy().add(axisExtent * nor.x, axisExtent * nor.y),
                                                        PhysicsConstants.getProjectileVelocity(-p.getMuzzleVelocity())
                                                                /2f,
                                                        p.getFireRatio(),
                                                        p.getSmokeRatio(),
                                                        p.getSparkRatio(),
                                                        10f,
                                                        0.2f, p.getInFirePartSize(), p.getFinFirePartSize());
                                fireSource.setSpawnEnabled(false);
                                this.projInfFireSourceMap.put(p, fireSource);
                            }
                            ;
                        });
    }

}
