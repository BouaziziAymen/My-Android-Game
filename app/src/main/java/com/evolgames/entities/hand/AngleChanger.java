package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;

public class AngleChanger {
  float target;
  GameEntity gameEntity;
  float step;

  AngleChanger(GameEntity entity, float targetAngle) {
    this.gameEntity = entity;
    this.target = targetAngle;
    float rot = gameEntity.getBody().getAngle();
    float dis =
        GeometryUtils.calculateShortestDirectedDistance(
            rot * MathUtils.radiansToDegrees, targetAngle);
    this.step = dis * MathUtils.degreesToRadians / 15f;
    this.gameEntity.getBody().setFixedRotation(true);
  }

  void run() {
    Vector2 position = gameEntity.getBody().getPosition();
    float rot = gameEntity.getBody().getAngle();
    float dis =
        GeometryUtils.calculateShortestDirectedDistance(rot * MathUtils.radiansToDegrees, target);
    if (Math.abs(dis) > 0) {
      gameEntity.getBody().setTransform(position, gameEntity.getBody().getAngle() + step);
    }
  }
}
