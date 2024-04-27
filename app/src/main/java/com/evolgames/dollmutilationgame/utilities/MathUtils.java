package com.evolgames.dollmutilationgame.utilities;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.Random;

/**
 * Utility and fast math functions.
 *
 * <p>Thanks to Riven on JavaGaming.org for the basis of sin/cos/atan2/floor/ceil.
 *
 * @author Nathan Sweet
 */
public class MathUtils {
    public static final float PI = 3.1415927f;
    public static final float radiansToDegrees = 180f / MathUtils.PI;
    public static final float degreesToRadians = MathUtils.PI / 180;
    private static final int SIN_BITS = 13; // Adjust for accuracy.
    private static final int SIN_MASK = ~(-1 << MathUtils.SIN_BITS);
    private static final int SIN_COUNT = MathUtils.SIN_MASK + 1;
    private static final float degToIndex = MathUtils.SIN_COUNT / MathUtils.degFull;
    private static final float radFull = MathUtils.PI * 2;
    private static final float radToIndex = MathUtils.SIN_COUNT / MathUtils.radFull;
    private static final float degFull = 360;
    private static final int ATAN2_BITS = 7; // Adjust for accuracy.
    private static final int ATAN2_BITS2 = MathUtils.ATAN2_BITS << 1;
    private static final int ATAN2_MASK = ~(-1 << MathUtils.ATAN2_BITS2);
    private static final int ATAN2_COUNT = MathUtils.ATAN2_MASK + 1;
    static final int ATAN2_DIM = (int) Math.sqrt(MathUtils.ATAN2_COUNT);
    private static final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (MathUtils.ATAN2_DIM - 1);
    private static final int BIG_ENOUGH_INT = 16 * 1024;
    private static final double BIG_ENOUGH_FLOOR = MathUtils.BIG_ENOUGH_INT;
    private static final double CEIL = 0.9999999;

    // ---
    private static final double BIG_ENOUGH_CEIL =
            NumberUtils.longBitsToDouble(NumberUtils.doubleToLongBits(MathUtils.BIG_ENOUGH_INT + 1) - 1);
    private static final double BIG_ENOUGH_ROUND = MathUtils.BIG_ENOUGH_INT + 0.5f;
    public static Random random = new Random();

    public static float distance(float x1, float y1, float x2, float y2) {
        float x_d = x1 - x2;
        float y_d = y1 - y2;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public static void rotateVectorByRadianAngle(Vector2 vector, float angle) {
        float x = (float) (vector.x * Math.cos(angle) - vector.y * Math.sin(angle));
        float y = (float) (vector.x * Math.sin(angle) + vector.y * Math.cos(angle));
        vector.set(x, y);
    }

    public static Vector2 getRotatedVectorByRadianAngle(Vector2 vector, float angle) {

        // normalize(vector); // No  need to normalize, vector is already ok...
        float x = (float) (vector.x * Math.cos(angle) - vector.y * Math.sin(angle));

        float y = (float) (vector.x * Math.sin(angle) + vector.y * Math.cos(angle));
        return Vector2Pool.obtain(x, y);
    }

    public static final float sin(float rad) {
        return MathUtils.Sin.table[(int) (rad * MathUtils.radToIndex) & MathUtils.SIN_MASK];
    }

    public static final float cos(float rad) {
        return MathUtils.Cos.table[(int) (rad * MathUtils.radToIndex) & MathUtils.SIN_MASK];
    }

    // ---

    public static final float sinDeg(float deg) {
        return MathUtils.Sin.table[(int) (deg * MathUtils.degToIndex) & MathUtils.SIN_MASK];
    }

    public static final float cosDeg(float deg) {
        return MathUtils.Cos.table[(int) (deg * MathUtils.degToIndex) & MathUtils.SIN_MASK];
    }

    public static final float atan2(float y, float x) {
        float add, mul;
        if (x < 0) {
            if (y < 0) {
                y = -y;
                mul = 1;
            } else mul = -1;
            x = -x;
            add = -MathUtils.PI;
        } else {
            if (y < 0) {
                y = -y;
                mul = -1;
            } else mul = 1;
            add = 0;
        }
        float invDiv = 1 / ((x < y ? y : x) * MathUtils.INV_ATAN2_DIM_MINUS_1);
        int xi = (int) (x * invDiv);
        int yi = (int) (y * invDiv);
        return (MathUtils.Atan2.table[yi * MathUtils.ATAN2_DIM + xi] + add) * mul;
    }

    /**
     * Returns a random number between 0 (inclusive) and the specified value (inclusive).
     */
    public static final int random(int range) {
        return MathUtils.random.nextInt(range + 1);
    }

    /**
     * Returns a random number between start (inclusive) and end (inclusive).
     */
    public static final int random(int start, int end) {
        return start + MathUtils.random.nextInt(end - start + 1);
    }

    public static final boolean randomBoolean() {
        return MathUtils.random.nextBoolean();
    }

    public static final float random() {
        return MathUtils.random.nextFloat();
    }

    // ---

    /**
     * Returns a random number between 0 (inclusive) and the specified value (inclusive).
     */
    public static final float random(float range) {
        return MathUtils.random.nextFloat() * range;
    }

    /**
     * Returns a random number between start (inclusive) and end (inclusive).
     */
    public static final float random(float start, float end) {
        return start + MathUtils.random.nextFloat() * (end - start);
    }

    // ---

    public static int nextPowerOfTwo(int value) {
        if (value == 0) return 1;
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    // ---

    public static short clamp(short value, short min, short max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Returns the largest integer less than or equal to the specified float. This method will only
     * properly floor floats from -(2^14) to (Float.MAX_VALUE - 2^14).
     */
    public static int floor(float x) {
        return (int) (x + MathUtils.BIG_ENOUGH_FLOOR) - MathUtils.BIG_ENOUGH_INT;
    }

    /**
     * Returns the largest integer less than or equal to the specified float. This method will only
     * properly floor floats that are positive. Note this method simply casts the float to int.
     */
    public static int floorPositive(float x) {
        return (int) x;
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float. This method will
     * only properly ceil floats from -(2^14) to (Float.MAX_VALUE - 2^14).
     */
    public static int ceil(float x) {
        return (int) (x + MathUtils.BIG_ENOUGH_CEIL) - MathUtils.BIG_ENOUGH_INT;
    }

    /**
     * Returns the smallest integer greater than or equal to the specified float. This method will
     * only properly ceil floats that are positive.
     */
    public static int ceilPositive(float x) {
        return (int) (x + MathUtils.CEIL);
    }

    /**
     * Returns the closest integer to the specified float. This method will only properly round floats
     * from -(2^14) to (Float.MAX_VALUE - 2^14).
     */
    public static int round(float x) {
        return (int) (x + MathUtils.BIG_ENOUGH_ROUND) - MathUtils.BIG_ENOUGH_INT;
    }

    /**
     * Returns the closest integer to the specified float. This method will only properly round floats
     * that are positive.
     */
    public static int roundPositive(float x) {
        return (int) (x + 0.5f);
    }

    public static float diminishedIncrease(float inputValue, float cap) {
        // Adjust these parameters for desired behavior
        float base = 2.0f; // Base of the logarithm

        // Ensure the input value is non-negative
        if (inputValue < 0) {
            throw new IllegalArgumentException("Input value must be non-negative.");
        }

        // Calculate the increased value using a logarithmic function
        return (float) (Math.min(cap,Math.log(inputValue + 1) / Math.log(base)));
    }

    private static class Sin {
        static final float[] table = new float[MathUtils.SIN_COUNT];

        static {
            for (int i = 0; i < MathUtils.SIN_COUNT; i++)
                Sin.table[i] = (float) Math.sin((i + 0.5f) / MathUtils.SIN_COUNT * MathUtils.radFull);
            for (int i = 0; i < 360; i += 90)
                Sin.table[(int) (i * MathUtils.degToIndex) & MathUtils.SIN_MASK] =
                        (float) Math.sin(i * MathUtils.degreesToRadians);
        }
    }

    private static class Cos {
        static final float[] table = new float[MathUtils.SIN_COUNT];

        static {
            for (int i = 0; i < MathUtils.SIN_COUNT; i++)
                Cos.table[i] = (float) Math.cos((i + 0.5f) / MathUtils.SIN_COUNT * MathUtils.radFull);
            for (int i = 0; i < 360; i += 90)
                Cos.table[(int) (i * MathUtils.degToIndex) & MathUtils.SIN_MASK] =
                        (float) Math.cos(i * MathUtils.degreesToRadians);
        }
    }

    private static class Atan2 {
        static final float[] table = new float[MathUtils.ATAN2_COUNT];

        static {
            for (int i = 0; i < MathUtils.ATAN2_DIM; i++) {
                for (int j = 0; j < MathUtils.ATAN2_DIM; j++) {
                    float x0 = (float) i / MathUtils.ATAN2_DIM;
                    float y0 = (float) j / MathUtils.ATAN2_DIM;
                    Atan2.table[j * MathUtils.ATAN2_DIM + i] = (float) Math.atan2(y0, x0);
                }
            }
        }
    }
}
