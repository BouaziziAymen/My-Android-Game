package com.evolgames.physics;

import android.hardware.SensorManager;

public class PhysicsConstants {
  public static final float TENACITY_FACTOR = 50f;
  public static final float MINIMUM_STABLE_SPLINTER_AREA = 36;
  public static final float PENETRATION_CONSTANT = 50000000f;
  public static final float PULVERIZATION_CONSTANT = 3.41f;
  public static final int FLUX_PRECISION = 10;
  public static final float BACKOFF = 0.05f;
  public static final float BLEEDING_CONSTANT = 1f;
  public static final float LIQUID_DENSITY_CONSTANT = 100;
  public static final float STAINING_PROBABILITY = 0.1f;
  public static final float MIN_PROJECTILE_VELOCITY = 10f;
  public static final float MAX_PROJECTILE_VELOCITY = 1000f;
  public static final float MIN_FIRE_RATE = 1f;
  public static final float MAX_FIRE_RATE = 10f;
  public static final float GRAIN_SPACING = 16f;
  public static final float PARTICLE_TERMINAL_VELOCITY = 10f;
  public static float gravity = -3 * SensorManager.GRAVITY_EARTH;
  public static float ambient_temperature = 60;

  public static float getProjectileVelocity(float ratio) {
    return MIN_PROJECTILE_VELOCITY + ratio * (MAX_PROJECTILE_VELOCITY - MIN_PROJECTILE_VELOCITY);
  }

  public static float getProjectileVelocityRatio(float velocity) {
    return (velocity - MIN_PROJECTILE_VELOCITY)
        / (MAX_PROJECTILE_VELOCITY - MIN_PROJECTILE_VELOCITY);
  }

  public static float getEffectiveFireRate(float ratio) {
    return MIN_FIRE_RATE + ratio * (MAX_FIRE_RATE - MIN_FIRE_RATE);
  }

  public static float getTenacityFromRatio(float ratio) {
    return 0.001f + ratio * (8f - 0.001f);
  }

  public static float getTenacityRatio(float tenacity) {
    return (tenacity - 0.001f) / (8f - 0.001f);
  }

    public static float getParticleVelocity(float speedRatio) {
      return (100+1000*speedRatio);
    }

  public static float getFlameTemperature(float heatRatio) {
    return 500+10000*heatRatio;
  }
}
