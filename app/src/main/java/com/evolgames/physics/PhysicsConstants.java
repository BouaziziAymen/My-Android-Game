package com.evolgames.physics;

import android.hardware.SensorManager;

public class PhysicsConstants {
    public static final float TENACITY_FACTOR =20f;
    public static final float HEAT_CONSTANT = 100f;
    public static final float MINIMUM_SPLINTER_AREA = 12;
    public static final float MINIMUM_STABLE_SPLINTER_AREA = 25;
    public static final float PENETRATION_CONSTANT = 100f;
    public static final float PULVERIZATION_CONSTANT = 3000000.41f;
    public static final int FLUX_PRECISION = 10;
    public static final float BACKOFF = 0.05f;
    public static final int PRECISION = 50;
    public static final float BLEEDING_CONSTANT = 1;
    public static final float LIQUID_DENSITY_CONSTANT = 1000;
    public static float grain_spacing = 16f;
    public static float gravity = - 3 * SensorManager.GRAVITY_EARTH;
    public static float ambient_temperature = 60;
}
