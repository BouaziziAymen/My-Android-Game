package com.evolgames.physics;

import android.hardware.SensorManager;

public class PhysicsConstants {
    public static final float TENACITY_FACTOR =10f;
    public static final float HEAT_CONSTANT = 100f;
    public static final float RADIANCE_CONSTANT = 10000;
    public static final float MINIMUM_SPLINTER_AREA = 5;
    public static final float PENETRATION_CONSTANT = 50000f;
    public static float grain_spacing = 16f;
    public static float gravity = - 3 * SensorManager.GRAVITY_EARTH;
    public static float ambient_temperature = 60;
}
