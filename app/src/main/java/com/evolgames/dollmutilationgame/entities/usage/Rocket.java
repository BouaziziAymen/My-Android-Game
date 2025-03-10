package com.evolgames.dollmutilationgame.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketProperties;
import com.evolgames.dollmutilationgame.entities.serialization.infos.FireSourceInfo;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.scenes.PlayScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    public Rocket(UsageModel<?> usageModel, PhysicsScene physicsScene, boolean mirrored) {
        RocketProperties rocketProperties = ((RocketProperties) usageModel.getProperties());
        this.fireSourceInfoList =
                rocketProperties
                        .getFireSourceModelList().stream()
                        .map(m -> m.toFireSourceInfo(mirrored))
                        .collect(Collectors.toList());
        this.fuel = rocketProperties.getFuel();
        this.power = rocketProperties.getPower();
        createFireSources(physicsScene.getWorldFacade());
        setActive(true);
    }

    public String getRocketBodyEntityUniqueId() {
        return rocketBodyEntityUniqueId;
    }

    public List<FireSourceInfo> getFireSourceInfoList() {
        return fireSourceInfoList;
    }


    public void onLaunch(PlayScene playScene, boolean withSound) {
        if(this.rocketBodyEntity!=null){
            this.rocketBodyEntity.getUseList().forEach(use -> {
                if(use instanceof TimeBomb){
                    TimeBomb timeBomb = (TimeBomb) use;
                    timeBomb.onTriggered(playScene.getWorldFacade());
                }
            });
        }
        if(playScene.isChaseActive()) {
            playScene.chaseEntity(rocketBodyEntity,true);
        }
        this.on = true;
        for (int i = 0, projectileInfoListSize = fireSourceInfoList.size(); i < projectileInfoListSize; i++) {
            FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
            if (rocketFireSourceInfMap.containsKey(fireSourceInfo)) {
                Objects.requireNonNull(rocketFireSourceInfMap.get(fireSourceInfo)).setSpawnEnabled(true);
            }
        }
        if(withSound) {
            ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().getProjectileSound("missile1").getSound(),1f,4,rocketBodyEntity.getMesh().getX(),rocketBodyEntity.getMesh().getY());
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
                Vector2 moveDirection = fireSourceInfo.getFireDirection().cpy().mul(-1);
                if (rocketBodyEntity != null) {
                    Body body = rocketBodyEntity.getBody();
                    if (body != null) {
                        if (fuel > 0) {
                            Vector2 worldFireDirection = body.getWorldVector(moveDirection).cpy();
                            body.applyForce(worldFireDirection.cpy().mul(FORCE_FACTOR * power), body.getWorldCenter());
                        } else {
                            Objects.requireNonNull(this.rocketFireSourceInfMap.get(fireSourceInfo)).setSpawnEnabled(false);
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
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.None);
        if(isActive()) {
            list.add(PlayerSpecialAction.AimLight);
            if (isOn()) {
                list.add(PlayerSpecialAction.SwitchOff);
            } else {
                list.add(PlayerSpecialAction.SwitchOn);
            }
        }
        return list;
    }


    public void createFireSources(WorldFacade worldFacade) {
        this.rocketFireSourceInfMap = new HashMap<>();
        this.fireSourceInfoList.forEach(
                p -> {
                    if (p.getFireRatio() >= 0.1f || p.getSmokeRatio() >= 0.1f || p.getSparkRatio() >= 0.1f) {

                        Vector2 dir = p.getFireDirection();
                        float nx = -dir.y;
                        float ny = dir.x;
                        Vector2 nor = new Vector2(nx, ny);
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
    public void dynamicMirror(PhysicsScene physicsScene) {
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
        this.rocketFireSourceInfMap.values().forEach(ExplosiveParticleWrapper::detach);
        if (ratio < 0.9f) {
            return false;
        }
        this.rocketBodyEntity = heir;
        this.fireSourceInfoList.forEach(fireSourceInfo -> fireSourceInfo.setMuzzleEntity(heir));
        createFireSources(heir.getScene().getWorldFacade());
        return true;
    }

}
