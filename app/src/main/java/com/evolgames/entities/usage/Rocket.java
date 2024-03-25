package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.usage.RocketProperties;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Rocket extends Use {
    public static final int FORCE_FACTOR = 10000;
    protected transient GameEntity rocketBodyEntity;
    int time = 0;
    private List<FireSourceInfo> fireSourceInfoList;
    private transient HashMap<FireSourceInfo, ExplosiveParticleWrapper> rocketFireSourceInfMap;
    private boolean on;
    private String rocketBodyEntityUniqueId;
    private float fuel;
    private float power;

    @SuppressWarnings("unused")
    public Rocket() {
    }

    public Rocket(UsageModel<?> usageModel, PhysicsScene<?> physicsScene, boolean mirrored) {
        RocketProperties rocketProperties = ((RocketProperties) usageModel.getProperties());
        this.fireSourceInfoList =
                rocketProperties
                        .getFireSourceModelList().stream()
                        .map(m -> m.toFireSourceInfo(mirrored))
                        .collect(Collectors.toList());
        this.fuel = rocketProperties.getFuel();
        this.power = rocketProperties.getPower();
        createFireSources(physicsScene.getWorldFacade());
    }

    public String getRocketBodyEntityUniqueId() {
        return rocketBodyEntityUniqueId;
    }

    public List<FireSourceInfo> getFireSourceInfoList() {
        return fireSourceInfoList;
    }

    public void onLaunch(float angleRad) {
        this.on = true;
        Body body = rocketBodyEntity.getBody();
        body.setTransform(body.getPosition(), angleRad);

        for (int i = 0, projectileInfoListSize = fireSourceInfoList.size(); i < projectileInfoListSize; i++) {
            FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
            if (rocketFireSourceInfMap.containsKey(fireSourceInfo)) {
                rocketFireSourceInfMap.get(fireSourceInfo).setSpawnEnabled(true);
                rocketFireSourceInfMap.get(fireSourceInfo).setSpawnEnabled(true);
            }
        }
    }

    public void onLaunch() {
        this.on = true;
        //ResourceManager.getInstance().firstCamera.setChaseEntity(rocketBodyEntity.getMesh());
        for (int i = 0, projectileInfoListSize = fireSourceInfoList.size(); i < projectileInfoListSize; i++) {
            FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
            if (rocketFireSourceInfMap.containsKey(fireSourceInfo)) {
                rocketFireSourceInfMap.get(fireSourceInfo).setSpawnEnabled(true);
            }
        }
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        time++;
        if (this.on) {
            this.fuel -= deltaTime;
            int i = 0, projectileInfoListSize = fireSourceInfoList.size();
            while (i < projectileInfoListSize) {
                FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
                Vector2 fireDirection = fireSourceInfo.getFireDirection();
                if (rocketBodyEntity != null) {
                    Body body = rocketBodyEntity.getBody();
                    if (body != null) {
                        if (fuel > 0) {
                            Vector2 worldFireDirection = body.getWorldVector(fireDirection).cpy();
                            body.applyForce(worldFireDirection.cpy().mul((rocketBodyEntity.isMirrored() ? 1 : -1) * FORCE_FACTOR * power), body.getWorldCenter());
                        } else {
                            this.rocketFireSourceInfMap.get(fireSourceInfo).setSpawnEnabled(false);
                        }
                    }
                }
                i++;
            }
        }
    }

    public float getPower() {
        return power;
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        if (isOn()) {
            return Collections.singletonList(PlayerSpecialAction.SwitchOff);
        } else {
            return Collections.singletonList(PlayerSpecialAction.SwitchOn);
        }
    }


    public void createFireSources(WorldFacade worldFacade) {
        this.rocketFireSourceInfMap = new HashMap<>();
        this.fireSourceInfoList.forEach(
                p -> {
                    if (p.getFireRatio() >= 0.1f || p.getSmokeRatio() >= 0.1f || p.getSparkRatio() >= 0.1f) {

                        Vector2 dir = p.getFireDirection();
                        Vector2 nor = new Vector2(-dir.y, dir.x);
                        Vector2 e = p.getFireSourceOrigin().cpy().mul(32f);
                        float axisExtent = p.getExtent();
                        ExplosiveParticleWrapper fireSource =
                                worldFacade.createFireSource(
                                        p.getMuzzleEntity(),
                                        e.cpy().sub(axisExtent * nor.x, axisExtent * nor.y),
                                        e.cpy().add(axisExtent * nor.x, axisExtent * nor.y),
                                        PhysicsConstants.getParticleVelocity(p.getSpeedRatio()),
                                        p.getFireRatio(),
                                        p.getSmokeRatio(),
                                        p.getSparkRatio(),
                                        p.getParticles(),
                                        p.getHeat(),
                                        p.getInFirePartSize(),
                                        p.getFinFirePartSize());
                        fireSource.setSpawnEnabled(this.on);
                        this.rocketFireSourceInfMap.put(p, fireSource);
                    }
                });
    }

    public void setRocketBodyGameEntity(GameEntity gameEntity) {
        this.rocketBodyEntity = gameEntity;
        this.rocketBodyEntityUniqueId = gameEntity.getUniqueID();
    }

    public boolean isOn() {
        return this.on;
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        fireSourceInfoList.forEach(fireSourceInfo -> {
            fireSourceInfo.getFireSourceOrigin().set(GeometryUtils.mirrorPoint(fireSourceInfo.getFireSourceOrigin()));
            fireSourceInfo.getFireDirection().x = -fireSourceInfo.getFireDirection().x;
        });
        this.rocketFireSourceInfMap.values().forEach(
                ExplosiveParticleWrapper::detach
        );
        createFireSources(physicsScene.getWorldFacade());
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        if (ratio < 0.9f) {
            return false;
        }
        this.fireSourceInfoList.forEach(fireSourceInfo -> {
            fireSourceInfo.setMuzzleEntity(heir);
        });

        return true;
    }

}
