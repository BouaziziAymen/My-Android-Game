package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.PhysicsConstants;

import java.util.Arrays;

public class Data {

    private int length = 0;
    private final float[][] data;
    private final GameEntity[] entities;
    private final LayerBlock[] blocks;
    private final Vector2 base;

    public Data(int n,Vector2 base) {
        this.data = new float[n][4];
        this.base = base;
        this.entities = new GameEntity[n];
        this.blocks = new LayerBlock[n];
    }


    public float[][] getData() {
        return data;
    }

    public GameEntity[] getEntities() {
        return entities;
    }

    public void add(float begin, float end, float weight,float sharpness, GameEntity entity, LayerBlock layerBlock) {
        if (begin <= end) {
            data[length][0] = begin;
            data[length][1] = end;
        } else {
            data[length][1] = begin;
            data[length][0] = end;
        }
        entities[length] = entity;
        blocks[length] = layerBlock;
        data[length][2] = weight;
        data[length][3] = sharpness;
        length++;

    }

    public int getSize() {
        return length;
    }

    public float getAverageSharpnessForAdvance(float advance) {
        float sharpness = 0;
        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            float sup = data[i][1];
            if (advance >= inf) {
                float xAdvance = (advance <= sup) ? advance - inf : sup - inf;
                sharpness += xAdvance * data[i][3];
            }
        }
        return sharpness/advance;
    }
    public float getEnergyForAdvance(float advance, float dL) {
        float energy = 0;
        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            float sup = data[i][1];
            if (advance >= inf) {
                float xAdvance = (advance <= sup) ? advance - inf : sup - inf;
                energy += xAdvance * data[i][2] * PhysicsConstants.PENETRATION_CONSTANT * dL;
            }
        }
        return energy;
    }

    public String toString() {
        String result = length + ":";
        for (int i = 0; i < length; i++)
            result += Arrays.toString(data[i]);
        return result;
    }

    public float getMax(GameEntity entity) {
        float result = -Float.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if (entities[i] == entity) {
                float sup = data[i][1];
                if (result < sup) result = sup;
            }
        }
        return result;
    }


    public float getMax() {
        float result = -Float.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            float sup = data[i][1];
            if (result < sup) result = sup;
        }
        return result;
    }


    public float getMin() {
        float result = Float.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            if (result > inf) result = inf;
        }
        return result;
    }

    public float getMin(GameEntity entity) {
        float result = Float.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if (entities[i] == entity) {
                float inf = data[i][0];
                if (result > inf) result = inf;
            }
        }
        return result;
    }


    public void findDelta() {
        float delta = getMax() - getMin();
    }


    public boolean isOverlapped(float inf, float sup, float advance, int index) {
        float INF = data[index][0];
        float SUP = data[index][1];
        float i = inf + advance;
        float s = sup + advance;
        if (i > INF && i < SUP) return true;
        return s > INF && s < SUP;
    }

    public Vector2 getBase() {
        return base;
    }

    public LayerBlock[] getBlocks() {
        return blocks;
    }
}
