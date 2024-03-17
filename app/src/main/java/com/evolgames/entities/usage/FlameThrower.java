package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.Collections;
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

    public FlameThrower(UsageModel<?> usageModel, PhysicsScene<?> physicsScene, boolean mirrored) {
        this.fireSourceInfoList =
                ((FlameThrowerProperties) usageModel.getProperties()).getFireSources().stream()
                        .map(fireSourceModel -> fireSourceModel.toFireSourceInfo(mirrored))
                        .collect(Collectors.toList());
        createFireSources(physicsScene.getWorldFacade());
    }

    public List<FireSourceInfo> getFireSourceInfoList() {
        return fireSourceInfoList;
    }

    public void onTriggerPulled() {
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
        return Collections.singletonList(PlayerSpecialAction.Fire);
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {

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
                                Vector2 nor = new Vector2(-dir.y, dir.x);
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
