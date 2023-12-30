package com.evolgames.entities.usage;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.properties.usage.MissileProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

public class Missile extends Rocket {

  private float control;
  private float steerValue;

  @SuppressWarnings("unused")
  public Missile() {}

  public Missile(UsageModel<?> usageModel, PhysicsScene<?> physicsScene) {
    super(usageModel, physicsScene);
    MissileProperties missileProperties = ((MissileProperties) usageModel.getProperties());
    this.control = missileProperties.getControl();
  }

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {
    super.onStep(deltaTime, worldFacade);
    if (rocketBodyEntity != null) {
      Body body = rocketBodyEntity.getBody();
      if (body != null) {
        steer();
      }
      }
  }
  @Override
  public PlayerSpecialAction getAction() {
    return PlayerSpecialAction.Missile;
  }

  private void steer() {
    float steer =
            this.steerValue
                    * this.control
                    * rocketBodyEntity.getBody().getLinearVelocity().len()
                    / 30f;
    if (Math.abs(steer) > Math.PI/60f) {
      this.rocketBodyEntity.getBody().setAngularVelocity(steer);
    }
  }

  public void setSteerValue(float steerValue) {
    this.steerValue = steerValue;
  }
}
