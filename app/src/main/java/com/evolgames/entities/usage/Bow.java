package com.evolgames.entities.usage;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.basics.EntityWithBody;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.usage.BowProperties;
import com.evolgames.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.WorldFacade;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class Bow extends Use {
    private List<ProjectileInfo> projectileInfoList;
    private float reloadTime;
    private boolean loading = false;
    private float loadingTimer;
    private transient Map<ProjectileInfo, GameGroup> arrows;
    private boolean initialized = false;


    @SuppressWarnings("unused")
    public Bow() {
    }

    public Bow(UsageModel<?> rangedUsageModel, boolean mirrored) {
        BowProperties bowProperties = (BowProperties) rangedUsageModel.getProperties();
        this.projectileInfoList =
                bowProperties.getProjectileModelList().stream()
                        .map(m -> m.toProjectileInfo(mirrored))
                        .collect(Collectors.toList());
        this.reloadTime = bowProperties.getReloadTime();
    }

    public void setArrows(Map<ProjectileInfo, GameGroup> arrows) {
        this.arrows = arrows;
    }

    public List<ProjectileInfo> getProjectileInfoList() {
        return projectileInfoList;
    }


    public void onArrowsReleased() {
        if (loading) {
            return;
        }
        this.loading = true;
        this.loadingTimer = 0;
        for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
            fire(i);
        }
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (!initialized) {
            loadArrows(worldFacade);
        }

        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                this.loading = false;
                loadArrows(worldFacade);
                ((PlayScene) worldFacade.getPhysicsScene()).onUsagesUpdated();
            }
        }
    }

    private void loadArrows(WorldFacade worldFacade) {
        this.arrows = new HashMap<>();
        boolean allBodiesReady = projectileInfoList.stream().map(ProjectileInfo::getMuzzleEntity).allMatch(entity -> entity != null && entity.getBody() != null);
        if (allBodiesReady) {
            initialized = true;
            for (ProjectileInfo projectileInfo : projectileInfoList) {
                if (projectileInfo.getMuzzleEntity().getBody() != null) {
                    this.createArrow(projectileInfo, worldFacade.getPhysicsScene());
                }
            }
        }
    }


    private void fire(int index) {
        ProjectileInfo projectileInfo = this.projectileInfoList.get(index);

        GameGroup arrowGroup = Objects.requireNonNull(arrows.get(projectileInfo));

        GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();
        muzzleEntity.getBody().getJointList().forEach(jointEdge -> {
            Body bodyA = jointEdge.joint.getBodyA();
            Body bodyB = jointEdge.joint.getBodyB();
            if (bodyA == muzzleEntity.getBody() && arrowGroup.getEntities().stream().map(EntityWithBody::getBody).anyMatch(e -> e == bodyB)) {
                Invoker.addJointDestructionCommand(muzzleEntity.getParentGroup(), jointEdge.joint);
            }
        });
        computeRecoil(projectileInfo, arrowGroup, muzzleEntity);

    }

    private void computeRecoil(ProjectileInfo projectileInfo, GameGroup arrowGroup, GameEntity muzzleEntity) {
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
        float muzzleVelocity = projectileInfo.getMuzzleVelocity();
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        Vector2 impulse =
                directionProjected.cpy().nor().mul(arrowGroup.getMass()).mul(-projectileInfo.getRecoil() * muzzleVelocityVector.len());
        muzzleEntity.getBody().applyLinearImpulse(impulse, beginProjected);
    }

    private void createArrow(ProjectileInfo projectileInfo, PhysicsScene<?> physicsScene) {
        int index = projectileInfoList.indexOf(projectileInfo);
        ToolModel arrowModel;
        try {
            arrowModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile(),projectileInfo.isAssetsMissile());
        } catch (PersistenceException | IOException | ParserConfigurationException |
                 SAXException e) {
            return;
        }
        GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();

        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 localDir = end.cpy().sub(projectileInfo.getProjectileOrigin()).nor();
        Vector2 worldDir = muzzleEntity.getBody().getWorldVector(localDir);
        float worldAngle = GeometryUtils.calculateAngleRadians(worldDir.x, worldDir.y);
        Filter projectileFilter = new Filter();
        Vector2 worldEnd = muzzleEntity.getBody().getWorldPoint(end).cpy().mul(32f);
        projectileFilter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.maskBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.groupIndex = muzzleEntity.getGroupIndex();

        arrowModel.getBodies().forEach(bodyModel -> bodyModel.setBullet(true));
        JointModel jointModel = new JointModel(arrowModel.getJointCounter().getAndIncrement(), JointDef.JointType.WeldJoint);


        GameGroup arrowGroup = physicsScene.createItem(worldEnd.x, worldEnd.y, worldAngle, arrowModel, muzzleEntity.isMirrored());
        GameEntity arrowEntity = arrowGroup.getGameEntityByIndex(0);
        BodyModel bodyModel1 = new BodyModel(0);
        BodyModel bodyModel2 = new BodyModel(1);
        jointModel.setBodyModel1(bodyModel1);
        jointModel.setBodyModel2(bodyModel2);
        jointModel.getLocalAnchorA().set(end.cpy().mul(32f).add(muzzleEntity.getCenter()));
        jointModel.getLocalAnchorB().set(arrowEntity.getCenter());
        float angle = (float) (GeometryUtils.calculateAngleRadians(localDir.x, localDir.y) + (!muzzleEntity.isMirrored() ? Math.PI : 0));
        jointModel.setReferenceAngle(angle);

        bodyModel1.setGameEntity(muzzleEntity);
        bodyModel2.setGameEntity(arrowEntity);
        physicsScene.createJointFromModel(jointModel, false);
        this.arrows.put(projectileInfo, arrowGroup);
        arrowEntity.setZIndex(muzzleEntity.getMesh().getZIndex() - 1);
        physicsScene.getWorldFacade().addNonCollidingPair(arrowEntity, muzzleEntity);
        //projectileInfo.setRocketEntityUniqueId(arrowEntity.getUniqueID());
        physicsScene.sortChildren();
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        if (!loading) {
            return Collections.singletonList(PlayerSpecialAction.Trigger);
        } else return null;
    }


    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        projectileInfoList.forEach(projectileInfo -> {
            projectileInfo.getProjectileOrigin().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileOrigin()));
            projectileInfo.getProjectileEnd().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileEnd()));
        });
    }
}