package com.evolgames.physics;

import android.hardware.SensorManager;

public class PhysicsConstants {
    public static final float TENACITY_FACTOR =50f;
    public static final float HEAT_CONSTANT = 100f;
    public static final float MINIMUM_SPLINTER_AREA = 64f;
    public static final float MINIMUM_STABLE_SPLINTER_AREA = 90f;
    public static final float PENETRATION_CONSTANT = 3000000f;
    public static final float PULVERIZATION_CONSTANT = 3.41f;
    public static final int FLUX_PRECISION = 100;
    public static final float BACKOFF = 0.05f;
    public static final float BLEEDING_CONSTANT = 3f;
    public static final float LIQUID_DENSITY_CONSTANT = 100;
    public static final float STAINING_PROBABILITY = 0.1f;
    public static final float MIN_PROJECTILE_VELOCITY = 10f;
    public static final float MAX_PROJECTILE_VELOCITY = 1000f;
    public static final float MIN_FIRE_RATE = 1f;
    public static final float MAX_FIRE_RATE = 10f;
    public static final float GRAIN_SPACING = 20f;
    public static final float PARTICLE_TERMINAL_VELOCITY = 10f;
    public static float gravity = - 3 * SensorManager.GRAVITY_EARTH;
    public static float ambient_temperature = 60;
    public static float getProjectileVelocity(float ratio){
        return MIN_PROJECTILE_VELOCITY + ratio * (MAX_PROJECTILE_VELOCITY-MIN_PROJECTILE_VELOCITY);
    }
    public static float getEffectiveFireRate(float ratio){
        return MIN_FIRE_RATE + ratio * (MAX_FIRE_RATE-MIN_FIRE_RATE);
    }
}
