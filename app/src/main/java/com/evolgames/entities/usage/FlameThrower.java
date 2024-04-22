package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlameThrower extends Use {
    private List<FireSourceInfo> fireSourceInfoList;
    transient private HashMap<FireSourceInfo, ExplosiveParticleWrapper> flameThrowerInfFireSourceMap;
    private boolean on;


    @SuppressWarnings("unused")
    public FlameThrower() {
    }

    public FlameThrower(UsageModel<?> usageModel, PhysicsScene physicsScene, boolean mirrored) {
        this.fireSourceInfoList =
                ((FlameThrowerProperties) usageModel.getProperties()).getFireSources().stream()
                        .map(fireSourceModel -> fireSourceModel.toFireSourceInfo(mirrored))
                        .collect(Collectors.toList());
        createFireSources(physicsScene.getWorldFacade());
    }

    public List<FireSourceInfo> getFireSourceInfoList() {
        return fireSourceInfoList;
    }

    public void onTriggerPulled(float x, float y) {
        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().fireWhoosh,1f,3,x,y);
        on = true;
        for (int i = 0, projectileInfoListSize = fireSourceInfoList.size();
             i < projectileInfoListSize;
             i++) {
            FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
            if (flameThrowerInfFireSourceMap.containsKey(fireSourceInfo)) {
                Objects.requireNonNull(flameThrowerInfFireSourceMap.get(fireSourceInfo)).setSpawnEnabled(true);
            }
        }
    }

    public void onTriggerReleased() {
        on = false;
        for (int i = 0, projectileInfoListSize = fireSourceInfoList.size();
             i < projectileInfoListSize;
             i++) {
            FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
            if (flameThrowerInfFireSourceMap.containsKey(fireSourceInfo)) {
                Objects.requireNonNull(flameThrowerInfFireSourceMap.get(fireSourceInfo)).setSpawnEnabled(false);
            }
        }
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.None);
        list.add(PlayerSpecialAction.FireLight);
        return list;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        fireSourceInfoList.forEach(fireSourceInfo -> {
            fireSourceInfo.getFireSourceOrigin().set(GeometryUtils.mirrorPoint(fireSourceInfo.getFireSourceOrigin()));
            fireSourceInfo.getFireDirection().x = -fireSourceInfo.getFireDirection().x;
        });
        createFireSources(physicsScene.getWorldFacade());
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        this.flameThrowerInfFireSourceMap.values().forEach(ExplosiveParticleWrapper::detach);
        if (ratio < 0.9f) {
            return false;
        }
        this.fireSourceInfoList.forEach(fireSourceInfo -> fireSourceInfo.setMuzzleEntity(heir));
        return true;
    }

    public void createFireSources(WorldFacade worldFacade) {
        this.flameThrowerInfFireSourceMap = new HashMap<>();
        this.fireSourceInfoList
                .forEach(
                        p -> {
                            if (p.getFireRatio() >= 0.1f
                                    || p.getSmokeRatio() >= 0.1f
                                    || p.getSparkRatio() >= 0.1f) {

                                Vector2 dir = p.getFireDirection();
                                float ny = dir.x;
                                float nx = -dir.y;
                                Vector2 nor = new Vector2(nx, ny);
                                Vector2 e = p.getFireSourceOrigin().cpy().mul(32f);
                                float axisExtent = p.getExtent();
                                ExplosiveParticleWrapper fireSource =
                                        worldFacade
                                                .createFireSource(
                                                        p.getMuzzleEntity(),
                                                        e.cpy().sub(axisExtent * nor.x, axisExtent * nor.y),
                                                        e.cpy().add(axisExtent * nor.x, axisExtent * nor.y),
                                                        PhysicsConstants.getParticleVelocity(p.getSpeedRatio()),
                                                        p.getFireRatio(),
                                                        p.getSmokeRatio(),
                                                        p.getSparkRatio(),
                                                        p.getParticles(),
                                                        p.getHeat(), p.getInFirePartSize(), p.getFinFirePartSize());
                                fireSource.setSpawnEnabled(this.on);
                                this.flameThrowerInfFireSourceMap.put(p, fireSource);
                            }
                        });
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
