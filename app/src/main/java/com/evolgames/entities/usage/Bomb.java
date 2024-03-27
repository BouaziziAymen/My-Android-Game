package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.properties.usage.BombUsageProperties;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Bomb extends Use {

    protected boolean alive = true;
    private List<BombInfo> bombInfoList;
    private int safetyJointId;

    private boolean hasSafety;

    @SuppressWarnings("unused")
    public Bomb() {
    }

    public Bomb(UsageModel<?> usageModel, boolean mirrored) {
        BombUsageProperties properties = (BombUsageProperties) usageModel.getProperties();
        this.bombInfoList =
                properties.getBombModelList().stream()
                        .map(m -> m.toBombInfo(mirrored))
                        .collect(Collectors.toList());
        this.safetyJointId = properties.getSafetyJoint();
        if(this.safetyJointId!=-1){
            hasSafety = true;
        }
    }

    public void setHasSafety(boolean hasSafety) {
        this.hasSafety = hasSafety;
    }

    protected abstract boolean isCountDone();


    private void detonate(WorldFacade worldFacade) {
        for (BombInfo bombInfo : bombInfoList) {
            Body body = bombInfo.getCarrierEntity().getBody();
            if(body == null){
                return;
            }
            Vector2 pos =
                    bombInfo
                            .getBombPosition();
            Vector2 worldPos = body.getWorldPoint(pos).cpy();

            worldFacade.createExplosion(
                    bombInfo.getCarrierEntity(),
                    worldPos.x,
                    worldPos.y,
                    bombInfo.getFireRatio(),
                    bombInfo.getSmokeRatio(),
                    bombInfo.getSparkRatio(),
                    60f * bombInfo.getParticles(),
                    bombInfo.getForce(),
                    bombInfo.getHeat(),
                    bombInfo.getSpeed(), 1f, 0f);
        }
        this.alive = false;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (!this.alive) {
            return;
        }
        if (isCountDone()) {
            detonate(worldFacade);
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        if (!isActive()) {
            return Collections.singletonList(PlayerSpecialAction.Trigger);
        }
        return null;
    }
    public void onTriggered(WorldFacade worldFacade) {
        active = true;
        if(this.safetyJointId!=-1) {
            this.removeSafety(worldFacade);
            this.hasSafety = false;
        }
    }


    public List<BombInfo> getBombInfoList() {
        return bombInfoList;
    }

    protected void removeSafety(WorldFacade worldFacade) {
        if (safetyJointId == -1) {
            return;
        }
        Set<Joint> jointSet = new HashSet<>();
        for (BombInfo bombInfo : bombInfoList) {
            GameEntity carrierEntity = bombInfo.getCarrierEntity();
            if(carrierEntity.getBody()==null){continue;}
            carrierEntity.getBody().getJointList().forEach(jointEdge -> {
                Joint joint = jointEdge.joint;
                JointBlock jointBlock = (JointBlock) joint.getUserData();
                if (jointBlock.getJointId() == safetyJointId && !jointSet.contains(joint)) {
                    jointSet.add(joint);
                    Invoker.addJointDestructionCommand(carrierEntity.getParentGroup(), joint);
                    GameEntity entity1 = (GameEntity) joint.getBodyA().getUserData();
                    GameEntity entity2 = (GameEntity) joint.getBodyB().getUserData();
                    if(carrierEntity==entity1){
                        entity2.getBody().setLinearVelocity(carrierEntity.isMirrored()?-5f:5f,10f);
                    } else {
                        entity1.getBody().setLinearVelocity(carrierEntity.isMirrored()?-5f:5f,10f);
                    }
                    worldFacade.addNonCollidingPair(entity1, entity2);
                }
            });
        }
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        this.getBombInfoList().forEach(bombInfo -> {
            bombInfo.getBombPosition().set(GeometryUtils.mirrorPoint(bombInfo.getBombPosition()));
        });
    }

    protected boolean hasSafety(){
        return hasSafety;
    }
}
