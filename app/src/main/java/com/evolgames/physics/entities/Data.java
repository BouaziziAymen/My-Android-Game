package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.PhysicsConstants;

import java.util.Arrays;

public class Data {

    private int length = 0;
    private final float[][] data;
    private float delta;
    private final GameEntity[] entities;
    private final LayerBlock[] blocks;
    private final Vector2 base;

    public Data(int n,Vector2 base) {
        data = new float[n][3];
        this.base = base;
        entities = new GameEntity[n];
        blocks = new LayerBlock[n];
    }

    public int getLength() {
        return length;
    }

    public float[][] getData() {
        return data;
    }

    public GameEntity[] getEntities() {
        return entities;
    }

    public float getDelta() {
        return delta;
    }

    public void add(float begin, float end, float weight, GameEntity entity, LayerBlock layerBlock) {
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
        length++;

    }

    public int getSize() {
        return length;
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
        delta = getMax() - getMin();
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
