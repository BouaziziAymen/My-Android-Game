package com.evolgames.entities.usage;

import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.physics.CollisionUtils.OBJECTS_MIDDLE_CATEGORY;
import static com.evolgames.physics.PhysicsConstants.getProjectileVelocity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.EntityWithBody;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.usage.BowProperties;
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

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;
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
    private final transient Entity bowString = new Entity();
    private List<ProjectileInfo> projectileInfoList;
    private float reloadTime;
    private boolean loading = false;
    private float loadingTimer;
    private transient Map<ProjectileInfo, GameGroup> arrows;
    private boolean loaded;
    private Vector2 upper, lower, middle;

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
        this.upper = bowProperties.getUpper();
        this.middle = bowProperties.getMiddle();
        this.lower = bowProperties.getLower();
        if (mirrored) {
            mirrorPoints();
        }
        drawBowstring();
    }

    public void drawBowstring() {
        GameEntity bow = projectileInfoList.stream().findFirst().get().getMuzzleEntity();
        if (bowString.hasParent()) {
            bowString.detachSelf();
        }
        bow.getMesh().attachChild(bowString);
        bowString.detachChildren();
        if(upper!=null&&lower!=null&&middle!=null) {
            if (loaded) {
                Line line1 = new Line(upper.x, upper.y, middle.x, middle.y, 1f, ResourceManager.getInstance().vbom);
                line1.setColor(Color.WHITE);
                Line line2 = new Line(middle.x, middle.y, lower.x, lower.y, 1f, ResourceManager.getInstance().vbom);
                line2.setColor(Color.WHITE);
                bowString.attachChild(line1);
                bowString.attachChild(line2);
            } else {
                Line line = new Line(upper.x, upper.y, lower.x, lower.y, 1f, ResourceManager.getInstance().vbom);
                line.setColor(Color.WHITE);
                bowString.attachChild(line);
            }
        }
    }

    public void mirrorPoints() {
        if (upper != null && lower != null && middle != null) {
            upper.set(GeometryUtils.mirrorPoint(upper));
            middle.set(GeometryUtils.mirrorPoint(middle));
            lower.set(GeometryUtils.mirrorPoint(lower));
        }
    }

    public void setArrows(Map<ProjectileInfo, GameGroup> arrows) {
        this.arrows = arrows;
    }

    public List<ProjectileInfo> getProjectileInfoList() {
        return projectileInfoList;
    }


    public void onArrowsReleased() {
        for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
            fire(i);
        }
        this.loading = true;
        this.loaded = false;
       startReloading();
        drawBowstring();
    }
    public void startReloading(){
        this.loading = true;
        this.loadingTimer = 0;
    }

    public boolean isLoaded() {
        return loaded;
    }


    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                this.loading = false;
                this.loadingTimer = 0;
                this.loaded = true;
                loadArrows(worldFacade);
                ((PlayScene) worldFacade.getPhysicsScene()).onUsagesUpdated();
            }
        }
    }

    private void loadArrows(WorldFacade worldFacade) {
        this.arrows = new HashMap<>();
        boolean allBodiesReady = projectileInfoList.stream().map(ProjectileInfo::getMuzzleEntity).allMatch(entity -> entity != null && entity.getBody() != null);
        if (allBodiesReady) {
            for (ProjectileInfo projectileInfo : projectileInfoList) {
                if (projectileInfo.getMuzzleEntity().getBody() != null) {
                    this.createArrow(projectileInfo, worldFacade.getPhysicsScene());
                }
            }
            this.loaded = true;
            drawBowstring();
        }
    }


    private void fire(int index) {
        ProjectileInfo projectileInfo = this.projectileInfoList.get(index);

        GameGroup arrowGroup = Objects.requireNonNull(arrows.get(projectileInfo));
        GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();
        if (projectileInfo.getMuzzleEntity().getBody() == null || arrowGroup.getGameEntityByIndex(0).getBody() == null) {
            return;
        }

        muzzleEntity.getBody().getJointList().forEach(jointEdge -> {
            Body bodyA = jointEdge.joint.getBodyA();
            Body bodyB = jointEdge.joint.getBodyB();
            if (bodyA == muzzleEntity.getBody() && arrowGroup.getEntities().stream().map(EntityWithBody::getBody).anyMatch(e -> e == bodyB)) {
                Invoker.addJointDestructionCommand(muzzleEntity.getParentGroup(), jointEdge.joint);
            }
        });


        Vector2 begin = projectileInfo.getProjectileOrigin();
        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 endProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(end)
                        .cpy();
        Vector2 beginProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(begin)
                        .cpy();
        Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
        float muzzleVelocity = getProjectileVelocity(projectileInfo.getMuzzleVelocity());
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        arrowGroup.getGameEntityByIndex(0).getBody().setLinearVelocity(muzzleVelocityVector);
        //ResourceManager.getInstance().firstCamera.setChaseEntity(arrowGroup.getGameEntityByIndex(0).getMesh());
        computeRecoil(projectileInfo, arrowGroup, muzzleEntity);

    }

    private void computeRecoil(ProjectileInfo projectileInfo, GameGroup arrowGroup, GameEntity muzzleEntity) {
        Vector2 endProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileEnd().cpy().mul(1 / 32f))
                        .cpy();
        Vector2 beginProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(projectileInfo.getProjectileOrigin().cpy().mul(1 / 32f))
                        .cpy();
        Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
        float muzzleVelocity = projectileInfo.getMuzzleVelocity();
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        Vector2 impulse =
                directionProjected.cpy().nor().mul(arrowGroup.getMass()).mul(-projectileInfo.getRecoil() * muzzleVelocityVector.len());
        muzzleEntity.getBody().applyLinearImpulse(impulse, beginProjected);
    }

    private void createArrow(ProjectileInfo projectileInfo, PhysicsScene<?> physicsScene) {

        ToolModel arrowModel;
        try {
            arrowModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile(), projectileInfo.isAssetsMissile());
        } catch (PersistenceException | IOException | ParserConfigurationException |
                 SAXException e) {
            return;
        }
        GameEntity bowBodyEntity = projectileInfo.getMuzzleEntity();
        Vector2 begin = projectileInfo.getProjectileOrigin();
        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 localDir = end.cpy().sub(projectileInfo.getProjectileOrigin()).nor();
        Vector2 worldDir = bowBodyEntity.getBody().getWorldVector(localDir);
        float worldAngle = GeometryUtils.calculateAngleRadians(worldDir.x, worldDir.y);
        Filter projectileFilter = new Filter();
        Vector2 worldEnd = bowBodyEntity.getBody().getWorldPoint(end).cpy().mul(32f);
        projectileFilter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.maskBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.groupIndex = bowBodyEntity.getGroupIndex();


        for(int i=0;i<arrowModel.getBodies().size();i++){
            BodyModel bodyModel = arrowModel.getBodies().get(i);
            if(i==0){
                //this is the main arrow body
                bodyModel.setInit(new Init.Builder(worldEnd.x, worldEnd.y).filter(OBJECT,OBJECT).angle(worldAngle).isBullet(true).build());
            } else {
                bodyModel.setInit(new Init.Builder(worldEnd.x, worldEnd.y).filter(OBJECT,OBJECT).angle(worldAngle).build());
            }
        }
        JointModel jointModel = new JointModel(arrowModel.getJointCounter().getAndIncrement(), JointDef.JointType.WeldJoint);


        GameGroup arrowGroup = physicsScene.createTool(arrowModel, bowBodyEntity.isMirrored());
        GameEntity arrowEntity = arrowGroup.getGameEntityByIndex(0);
        BodyModel bodyModel1 = new BodyModel(0);
        BodyModel bodyModel2 = new BodyModel(1);
        jointModel.setBodyModel1(bodyModel1);
        jointModel.setBodyModel2(bodyModel2);

        jointModel.getLocalAnchorA().set(begin
                .cpy().mul(32f).add(bowBodyEntity.getCenter()));
        jointModel.getLocalAnchorB().set(arrowEntity.getCenter());
        float angle = (float) (GeometryUtils.calculateAngleRadians(localDir.x, localDir.y) + (!bowBodyEntity.isMirrored() ? Math.PI : 0));
        jointModel.setReferenceAngle(angle);

        Projectile projectile = new Projectile(ProjectileType.SHARP_WEAPON);
        projectile.setActive(true);
        arrowEntity.getUseList().add(projectile);

        bodyModel1.setGameEntity(bowBodyEntity);
        bodyModel2.setGameEntity(arrowEntity);
        physicsScene.createJointFromModel(jointModel, false);

        this.arrows.put(projectileInfo, arrowGroup);
        arrowEntity.setZIndex(bowBodyEntity.getMesh().getZIndex() + 1);

        for (GameEntity arrowPart : arrowGroup.getEntities()) {
            physicsScene.getWorldFacade().addNonCollidingPair(arrowPart, bowBodyEntity);
        }
        physicsScene.sortChildren();
        projectileInfo.setArrowGroupUniqueId(arrowGroup.getUniqueID());
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        if (!loading) {
            return Collections.singletonList(PlayerSpecialAction.Shoot_Arrow);
        } else return null;
    }


    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        projectileInfoList.forEach(projectileInfo -> {
            projectileInfo.getProjectileOrigin().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileOrigin()));
            projectileInfo.getProjectileEnd().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileEnd()));
        });
        mirrorPoints();

    }

    @Override
    public void onAfterMirror(PhysicsScene<?> scene) {
        super.onAfterMirror(scene);
        drawBowstring();
    }

    public Map<ProjectileInfo, GameGroup> getArrows() {
        return arrows;
    }

    public void onBowReleased() {
        if(arrows!=null) {
            this.arrows.clear();
        }
        this.loaded = false;
        this.loading = false;
        this.loadingTimer = 0f;
        drawBowstring();
    }

    public boolean isLoading() {
        return this.loading;
    }
}