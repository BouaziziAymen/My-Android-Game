package com.evolgames.dollmutilationgame.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TopographyData {

    private final float[][] data;
    private final GameEntity[] entities;
    private final LayerBlock[] blocks;
    private final Vector2 base;
    private int length = 0;
    private boolean center;

    public TopographyData(int n, Vector2 base) {
        this.data = new float[n][2];
        this.base = base.cpy();
        this.entities = new GameEntity[n];
        this.blocks = new LayerBlock[n];
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public float[][] getData() {
        return data;
    }

    public int getLength() {
        return length;
    }

    public GameEntity[] getEntities() {
        return entities;
    }

    public void add(float begin, float end, GameEntity entity, LayerBlock layerBlock) {
        if (begin <= end) {
            data[length][0] = begin;
            data[length][1] = end;
        } else {
            data[length][1] = begin;
            data[length][0] = end;
        }
        entities[length] = entity;
        blocks[length] = layerBlock;
        length++;
    }

    public float getDensityValue() {
        float value = 0;
        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            float sup = data[i][1];
            float density = blocks[i].getProperties().getDensity();
            value += density * (sup - inf);
        }
        return value;
    }

    public int getSize() {
        return length;
    }

    public Float getImpulseForAdvance(
            float advance, float dL, float sharpness, float penetratorHardness) {
        float energy = 0;
        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            float sup = data[i][1];
            float hardness = blocks[i].getProperties().getHardness();
            float density = blocks[i].getProperties().getDensity();
            if (advance >= inf) {
                float hardnessFactor = hardness / penetratorHardness;
                float s = 1.1f - sharpness;
                float h = (float) Math.pow(hardnessFactor, 4);
                float xAdvance = (advance <= sup) ? advance - inf : sup - inf;
                energy += xAdvance * density * h * dL * PhysicsConstants.PENETRATION_CONSTANT * Math.pow(s, 2);
            }
        }
        return energy;
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

    public List<GameEntity> findReachedEntities(
            TopographyData penetratorTopographyData, float advance) {
        HashSet<GameEntity> gameEntities = new HashSet<>();
        for (int j = 0; j < penetratorTopographyData.length; j++) {
            for (int i = 0; i < length; i++) {
                float INF = data[i][0];
                float sup = penetratorTopographyData.getData()[j][1] + advance;
                if (sup > INF) {
                    gameEntities.add(this.entities[i]);
                }
            }
        }
        return new ArrayList<>(gameEntities);
    }

    public List<Overlap> findOverlaps(TopographyData penetratorTopographyData, float advance) {

        List<Overlap> overlaps = new ArrayList<>();
        for (int j = 0; j < penetratorTopographyData.length; j++) {
            for (int i = 0; i < length; i++) {
                float INF = data[i][0];
                float SUP = data[i][1];
                float density = penetratorTopographyData.getBlocks()[j].getProperties().getDensity();
                float inf = penetratorTopographyData.getData()[j][0] + advance;
                float sup = penetratorTopographyData.getData()[j][1] + advance;
                float lowerBoundOfOverlap = Math.max(inf, INF);
                float upperBoundOfOverlap = Math.min(sup, SUP);
                if (lowerBoundOfOverlap <= upperBoundOfOverlap - 0.05) {
                    // Overlap exists
                    overlaps.add(
                            new Overlap(this.entities[i], density * (upperBoundOfOverlap - lowerBoundOfOverlap)));
                }
            }
        }
        return overlaps;
    }

    public boolean doesOverlap(TopographyData penetratorTopographyData, float advance) {
        for (int j = 0; j < penetratorTopographyData.length; j++) {
            for (int i = 0; i < length; i++) {
                float INF = data[i][0];
                float SUP = data[i][1];
                float inf = penetratorTopographyData.getData()[j][0] + advance;
                float sup = penetratorTopographyData.getData()[j][1] + advance;
                if ((inf > INF && inf < SUP) || (sup > INF && sup < SUP)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Vector2 getBase() {
        return base;
    }

    public LayerBlock[] getBlocks() {
        return blocks;
    }

    public Float getFirstContact(GameEntity overlappedEntity) {
        Float inf = null;
        for (int i = 0; i < length; i++) {
            GameEntity gameEntity = entities[i];
           if(gameEntity==overlappedEntity){
              if(inf==null||data[i][0]<inf){
                  inf = data[i][0];
              }
           }
        }
        return inf;
    }

    public static class Overlap {
        GameEntity gameEntity;
        float value;

        public Overlap(GameEntity gameEntity, float value) {
            this.gameEntity = gameEntity;
            this.value = value;
        }

        public GameEntity getGameEntity() {
            return gameEntity;
        }

        public void setGameEntity(GameEntity gameEntity) {
            this.gameEntity = gameEntity;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }
}
