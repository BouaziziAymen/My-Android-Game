package com.evolgames.entities.usage;

import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.physics.CollisionUtils.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.Init;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.ToolUtils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
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
    private transient Map<ProjectileInfo, Rocket> rockets;
    private boolean initialized = false;

    private boolean fireOn;
    private int fireCounter;
    private transient Map<ProjectileInfo, ExplosiveParticleWrapper> projInfFireSourceMap;

    @SuppressWarnings("unused")
    public RocketLauncher() {
    }

    public RocketLauncher(UsageModel<?> rangedUsageModel, WorldFacade worldFacade, boolean mirrored) {
        RocketLauncherProperties rocketLauncherProperties = (RocketLauncherProperties) rangedUsageModel.getProperties();
        this.projectileInfoList =
                rocketLauncherProperties.getProjectileModelList().stream()
                        .map(m -> m.toProjectileInfo(mirrored))
                        .collect(Collectors.toList());
        createFireSources(worldFacade);
        this.reloadTime = rocketLauncherProperties.getReloadTime();
    }

    public void setRockets(Map<ProjectileInfo, Rocket> rockets) {
        this.rockets = rockets;
    }

    public List<ProjectileInfo> getProjectileInfoList() {
        return projectileInfoList;
    }


    public void onTriggerPulled(PlayScene playScene) {
        if (loading) {
            return;
        }
        this.loading = true;
        this.loadingTimer = 0;
        for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
            fire(i, playScene);

        }
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (!initialized) {
            loadRockets(worldFacade);
        }
        if (this.fireOn) {
            fireCounter++;
        }
        if (fireCounter > 60) {
            for (ExplosiveParticleWrapper explosiveParticleWrapper : this.projInfFireSourceMap.values()) {
                explosiveParticleWrapper.setSpawnEnabled(false);
            }
            this.fireOn = false;
            fireCounter = 0;
        }

        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                this.loading = false;
                loadRockets(worldFacade);
                ((PlayScene) worldFacade.getPhysicsScene()).onUsagesUpdated();
                // Reload finished
            }
        }
    }

    private void loadRockets(WorldFacade worldFacade) {
        this.rockets = new HashMap<>();
        boolean allBodiesReady = projectileInfoList.stream().map(ProjectileInfo::getMuzzle).allMatch(muzzle -> muzzle != null && muzzle.getTheMuzzleEntity() != null && muzzle.getTheMuzzleEntity().getBody() != null);
        if (allBodiesReady) {
            initialized = true;
            for (ProjectileInfo projectileInfo : projectileInfoList) {
                if (projectileInfo.getMuzzle()!=null&&projectileInfo.getMuzzle().getTheMuzzleEntity().getBody() != null) {
                    this.createRocket(projectileInfo, worldFacade.getPhysicsScene());
                }
            }
        }
    }


    private void fire(int index, PlayScene playScene) {
        ProjectileInfo projectileInfo = this.projectileInfoList.get(index);
        Rocket rocket = rockets.get(projectileInfo);
        if (rocket == null) {
            return;
        }
        GameEntity rocketEntity = rocket.rocketBodyEntity;
        if (rocketEntity.getBody() == null) {
            return;
        }
        if (rocketEntity.hasUsage(Bomb.class)) {
            Bomb bomb = rocketEntity.getUsage(Bomb.class);
            bomb.setHasSafety(false);
        }
        rocketEntity.getParentGroup().getEntities().forEach(entity -> {
            if (entity != rocketEntity) {
                entity.setVisible(true);
                entity.getBody().getFixtureList().forEach(fixture -> fixture.setSensor(true));
            }
        });

        GameEntity muzzleEntity = projectileInfo.getMuzzle().getTheMuzzleEntity();
        if (muzzleEntity == null || muzzleEntity.getBody() == null) {
            return;
        }
        muzzleEntity.getBody().getJointList().forEach(jointEdge -> {
            Body bodyA = jointEdge.joint.getBodyA();
            Body bodyB = jointEdge.joint.getBodyB();
            if (bodyA == muzzleEntity.getBody() && bodyB == rocketEntity.getBody()) {
                Invoker.addJointDestructionCommand(muzzleEntity.getParentGroup(), jointEdge.joint);
            }
        });

        computeRecoil(projectileInfo, rocketEntity, muzzleEntity);

        Objects.requireNonNull(this.rockets.get(projectileInfo)).onLaunch(playScene, false);
        ResourceManager.getInstance().tryPlaySound( ResourceManager.getInstance()
                .getProjectileSound(projectileInfo.getFireSound()).getSound(),1f,3);
        if (projInfFireSourceMap.containsKey(projectileInfo)) {
            Objects.requireNonNull(projInfFireSourceMap.get(projectileInfo)).setSpawnEnabled(true);
            fireOn = true;
        }
    }

    private void computeRecoil(ProjectileInfo projectileInfo, GameEntity rocketEntity, GameEntity muzzleEntity) {
        Vector2 endProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileEnd())
                        .cpy();
        Vector2 beginProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileOrigin())
                        .cpy();
        Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
        float muzzleVelocity = Objects.requireNonNull(rockets.get(projectileInfo)).getPower() * 500;
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);

        Vector2 impulse =
                directionProjected.cpy().nor().mul(rocketEntity.getBody().getMass()).mul(-projectileInfo.getRecoil() * muzzleVelocityVector.len());
        muzzleEntity.getBody().applyLinearImpulse(impulse, beginProjected);
    }

    private void createRocket(ProjectileInfo projectileInfo, PhysicsScene physicsScene) {
        ToolModel rocketModel;
        try {
            rocketModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile(), projectileInfo.isAssetsMissile());
        } catch (PersistenceException | IOException | ParserConfigurationException |
                 SAXException e) {
            return;
        }
        GameEntity muzzleEntity = projectileInfo.getMuzzle().getTheMuzzleEntity();

        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 localDir = end.cpy().sub(projectileInfo.getProjectileOrigin()).nor();
        Vector2 worldDir = muzzleEntity.getBody().getWorldVector(localDir);
        float worldAngle = GeometryUtils.calculateAngleRadians(worldDir.x, worldDir.y);
        Filter projectileFilter = new Filter();
        Vector2 worldEnd = muzzleEntity.getBody().getWorldPoint(end).cpy().mul(32f);
        projectileFilter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.maskBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.groupIndex = muzzleEntity.getGroupIndex();

        for (int i = 0; i < rocketModel.getBodies().size(); i++) {
            BodyModel bodyModel = rocketModel.getBodies().get(i);
            Init init;
            if (i == 0) {
                //this is the main rocket body
                init = new Init.Builder(worldEnd.x, worldEnd.y).filter(OBJECT, OBJECT).angle(worldAngle).isBullet(true).build();
            } else {
                init = new Init.Builder(worldEnd.x, worldEnd.y).bodyIsNotActive(true).filter(OBJECT, OBJECT).angle(worldAngle).build();
            }
            bodyModel.setInit(init);
        }
        JointModel jointModel = new JointModel(rocketModel.getJointCounter().getAndIncrement(), JointDef.JointType.WeldJoint);

        GameGroup rocketGroup = physicsScene.createTool(rocketModel, muzzleEntity.isMirrored());
        muzzleEntity.setZIndex(1);
        rocketGroup.getEntities().forEach(entity -> {
            physicsScene.getWorldFacade().addNonCollidingPair(entity, muzzleEntity);
            entity.setZIndex(muzzleEntity.getZIndex() - 1);
        });

        GameEntity rocketEntity = rocketGroup.getGameEntityByIndex(0);
        BodyModel bodyModel1 = new BodyModel(0);
        BodyModel bodyModel2 = new BodyModel(1);
        jointModel.setBodyModel1(bodyModel1);
        jointModel.setBodyModel2(bodyModel2);
        jointModel.getProperties().getLocalAnchorA().set(end.cpy().mul(32f));
        jointModel.getProperties().getLocalAnchorB().set(new Vector2());
        float angle = (float) (GeometryUtils.calculateAngleRadians(localDir.x, localDir.y) + (!muzzleEntity.isMirrored() ? Math.PI : 0));
        jointModel.getProperties().setReferenceAngle(angle);

        bodyModel1.setGameEntity(muzzleEntity);
        bodyModel2.setGameEntity(rocketEntity);
        bodyModel1.setCenter(new Vector2());
        bodyModel2.setCenter(new Vector2());
        physicsScene.createJointFromModel(jointModel, false);

        Rocket rocket = rocketEntity.getUsage(Rocket.class);
        this.rockets.put(projectileInfo, rocket);

        projectileInfo.setRocketEntityUniqueId(rocketEntity.getUniqueID());


        if (rocketEntity.hasUsage(Bomb.class)) {
            Bomb bomb = rocketEntity.getUsage(Bomb.class);
            bomb.setHasSafety(true);
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.None);
        list.add(PlayerSpecialAction.AimLight);
        if (!loading) {
            list.add(PlayerSpecialAction.Trigger);
        }
        return list;
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
                                float nx = -dir.y;
                                float ny = dir.x;
                                Vector2 nor = new Vector2(nx,ny);
                                Vector2 e = p.getProjectileOrigin().cpy().mul(32f);
                                float axisExtent = 0.1f;
                                ExplosiveParticleWrapper fireSource =
                                        worldFacade
                                                .createFireSource(
                                                        p.getMuzzle().getTheMuzzleEntity(),
                                                        e.cpy().sub(axisExtent * nor.x, axisExtent * nor.y),
                                                        e.cpy().add(axisExtent * nor.x, axisExtent * nor.y),
                                                        -100f,
                                                        p.getFireRatio(),
                                                        p.getSmokeRatio(),
                                                        p.getSparkRatio(),
                                                        1f,
                                                        0.2f, 1f, 0f);
                                fireSource.setSpawnEnabled(false);
                                this.projInfFireSourceMap.put(p, fireSource);
                            }
                        });
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        projectileInfoList.forEach(projectileInfo -> {
            projectileInfo.getProjectileOrigin().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileOrigin()));
            projectileInfo.getProjectileEnd().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileEnd()));
        });
        this.projInfFireSourceMap.values().forEach(
                ExplosiveParticleWrapper::detach
        );

        createFireSources(physicsScene.getWorldFacade());
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        this.projInfFireSourceMap.values().forEach(ExplosiveParticleWrapper::detach);
        if (ratio < 0.9f) {
            return false;
        }
        createFireSources(heir.getScene().getWorldFacade());
        return true;
    }


}
