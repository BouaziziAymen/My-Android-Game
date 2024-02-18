package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.usage.RocketProperties;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Rocket extends Use {
  public static final int FORCE_FACTOR = 10000;
  int time = 0;
  private List<FireSourceInfo> fireSourceInfoList;
  private transient HashMap<FireSourceInfo, ExplosiveParticleWrapper> rocketFireSourceInfMap;
  private boolean on;
  protected transient GameEntity rocketBodyEntity;
  private String rocketBodyEntityUniqueId;
  private float fuel;
  private float power;

  @SuppressWarnings("unused")
  public Rocket() {}

  public Rocket(UsageModel<?> usageModel, PhysicsScene<?> physicsScene) {
    RocketProperties rocketProperties = ((RocketProperties) usageModel.getProperties());
    this.fireSourceInfoList =
            rocketProperties
            .getFireSourceModelList().stream()
                .map(FireSourceModel::toFireSourceInfo)
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

  public void onLaunch(float angle) {
    this.on = true;
    Body body = rocketBodyEntity.getBody();
    body.setTransform(body.getPosition(), angle);
    ResourceManager.getInstance().firstCamera.setChaseEntity(rocketBodyEntity.getMesh());
    for (int i = 0, projectileInfoListSize = fireSourceInfoList.size();
        i < projectileInfoListSize;
        i++) {
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
      this.fuel-=deltaTime;
      for (int i = 0, projectileInfoListSize = fireSourceInfoList.size();
          i < projectileInfoListSize;
          i++) {
        FireSourceInfo fireSourceInfo = this.fireSourceInfoList.get(i);
        Vector2 v = fireSourceInfo.getFireDirection();
        if (rocketBodyEntity != null) {
          Body body = rocketBodyEntity.getBody();
          if (body != null) {
            if (fuel > 0) {
              // rocketBodyEntity.getBody().setAngularDamping(3f);
              Vector2 pos = fireSourceInfo.getFireSourceOrigin();
              Vector2 lp = body.getLocalPoint(new Vector2(pos.x / 32F, pos.y / 32F)).cpy();
              Vector2 lv = body.getWorldVector(v).cpy();
              body.applyForce(lv.cpy().mul(-FORCE_FACTOR * power), body.getWorldCenter());
            } else {
              this.rocketFireSourceInfMap.get(fireSourceInfo).setSpawnEnabled(false);
            }
          }
        }
        }
    }
  }

  @Override
  public List<PlayerSpecialAction> getActions() {
    return Collections.singletonList(PlayerSpecialAction.Rocket);
  }

  public void createFireSources(WorldFacade worldFacade) {
    this.rocketFireSourceInfMap = new HashMap<>();
    this.fireSourceInfoList.forEach(
        p -> {
          if (p.getFireRatio() >= 0.1f || p.getSmokeRatio() >= 0.1f || p.getSparkRatio() >= 0.1f) {

            Vector2 dir = p.getFireDirection();
            Vector2 nor = new Vector2(-dir.y, dir.x);
            Vector2 e = p.getFireSourceOrigin().cpy().sub(p.getMuzzleEntity().getCenter());
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
}
