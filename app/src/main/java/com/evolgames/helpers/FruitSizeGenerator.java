package com.evolgames.helpers;

import java.util.Random;

public class FruitSizeGenerator {
    static Random random = new Random();
    private static final float MIN_SIZE = 0.5f;
    private static final float MAX_SIZE = 1.5f;
    private static final float MEAN = 1.0f; // Scale factor 1 is the mean
    private static final float STD_DEV = 0.05f; // Standard deviation

    public static float generateSize() {
        float u1 = random.nextFloat(); // uniform(0,1) random doubles
        float u2 = random.nextFloat();
        float randStdNormal = (float) (Math.sqrt(-2.0 * Math.log(u1)) * Math.sin(2.0 * Math.PI * u2)); // random normal(0,1)
        float size = MEAN + STD_DEV * randStdNormal; // random normal(mean,stdDev^2)
        // Ensure size is within bounds
        size = Math.max(MIN_SIZE, Math.min(MAX_SIZE, size));

        return size;
    }
}