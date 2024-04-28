package com.evolgames.dollmutilationgame.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.entities.properties.usage.BombUsageProperties;
import com.evolgames.dollmutilationgame.entities.serialization.infos.BombInfo;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.ArrayList;
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
        if (this.safetyJointId != -1) {
            hasSafety = true;
        }
    }

    public void setHasSafety(boolean hasSafety) {
        this.hasSafety = hasSafety;
    }

    protected abstract boolean isCountDone();


    protected void detonate(WorldFacade worldFacade) {
        for (BombInfo bombInfo : bombInfoList) {
            Body body = bombInfo.getCarrierEntity().getBody();
            if (body == null) {
                return;
            }
            Vector2 pos =
                    bombInfo
                            .getBombPosition();
            worldFacade.createExplosion(
                    bombInfo.getCarrierEntity(),
                    pos.x,
                    pos.y,
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
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.None);
        if (!isActive()) {
            list.add(PlayerSpecialAction.Trigger);
        }
        return list;
    }

    public void onTriggered(WorldFacade worldFacade) {
        active = true;
        if (this.safetyJointId != -1) {
            this.removeSafety(worldFacade);
            this.hasSafety = false;
        }
    }


    public List<BombInfo> getBombInfoList() {
        return bombInfoList;
    }

    protected void removeSafety(WorldFacade worldFacade) {
        Set<Joint> jointSet = new HashSet<>();
        boolean soundPlayed = false;
        for (BombInfo bombInfo : bombInfoList) {
            GameEntity carrierEntity = bombInfo.getCarrierEntity();
            if (carrierEntity == null || carrierEntity.getBody() == null) {
                continue;
            }
            if (!soundPlayed) {
                ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().switchSound, 1f, 3, carrierEntity.getX(), carrierEntity.getY());
                soundPlayed = true;
            }
            ArrayList<JointEdge> copy = new ArrayList<>(carrierEntity.getBody().getJointList());
            for (JointEdge jointEdge : copy) {
                Joint joint = jointEdge.joint;
                JointBlock jointBlock = (JointBlock) joint.getUserData();
                if (jointBlock.getJointId() == safetyJointId && !jointSet.contains(joint)) {
                    if (joint.getBodyA() == null || joint.getBodyB() == null) {
                        continue;
                    }
                    jointSet.add(joint);
                    Invoker.addJointDestructionCommand(carrierEntity.getParentGroup(), joint);
                    GameEntity entity1 = (GameEntity) joint.getBodyA().getUserData();
                    GameEntity entity2 = (GameEntity) joint.getBodyB().getUserData();
                    if (carrierEntity == entity1) {
                        entity2.getBody().setLinearVelocity(carrierEntity.isMirrored() ? -5f : 5f, 10f);
                    } else {
                        entity1.getBody().setLinearVelocity(carrierEntity.isMirrored() ? -5f : 5f, 10f);
                    }
                    worldFacade.addNonCollidingPair(entity1, entity2);
                }
            }
        }
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        this.getBombInfoList().forEach(bombInfo -> bombInfo.getBombPosition().set(GeometryUtils.mirrorPoint(bombInfo.getBombPosition())));
    }

    protected boolean hasSafety() {
        return hasSafety;
    }
}
