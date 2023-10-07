package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.PhysicsConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopographyData {

    private int length = 0;
    private final float[][] data;
    private final GameEntity[] entities;
    private final LayerBlock[] blocks;
    private final Vector2 base;

    public TopographyData(int n, Vector2 base) {
        this.data = new float[n][3];
        this.base = base.cpy();
        this.entities = new GameEntity[n];
        this.blocks = new LayerBlock[n];
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

    public void add(float begin, float end, float hardness, GameEntity entity, LayerBlock layerBlock) {
        if (begin <= end) {
            data[length][0] = begin;
            data[length][1] = end;
        } else {
            data[length][1] = begin;
            data[length][0] = end;
        }
        entities[length] = entity;
        blocks[length] = layerBlock;
        data[length][2] = hardness;
        length++;

    }

    public int getSize() {
        return length;
    }

    public Float getEnergyForAdvance(float advance, float dL, float sharpness, float penetratorHardness) {
        float energy = 0;

        for (int i = 0; i < length; i++) {
            float inf = data[i][0];
            float sup = data[i][1];
            if (advance >= inf) {
                if(penetratorHardness<data[i][2]){
                    return null;
                }
                float s = 1f-sharpness;
                float h = 1/(penetratorHardness - 0.999f*data[i][2]);
                float xAdvance = (advance <= sup) ? advance - inf : sup - inf;
                energy += xAdvance *h* PhysicsConstants.PENETRATION_CONSTANT * dL * Math.pow(s,2);
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


    static class OverlapFlag{
        GameEntity entity;
        float value;

        public OverlapFlag(GameEntity entity, float value) {
            this.entity = entity;
            this.value = value;
        }

        public float getValue() {
            return value;
        }

        public GameEntity getEntity() {
            return entity;
        }
    }

    public List<GameEntity> findReachedEntities(TopographyData penetratorTopographyData, float advance) {
        HashSet<GameEntity> gameEntities = new HashSet<>();
        for(int j = 0; j< penetratorTopographyData.length; j++) {
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

    public List<GameEntity> findOverlappingEntities(TopographyData penetratorTopographyData, float advance) {
        List<OverlapFlag> overlapFlagList = new ArrayList<>();
        for(int j = 0; j< penetratorTopographyData.length; j++) {
            for (int i = 0; i < length; i++) {
                float INF = data[i][0];
                float SUP = data[i][1];
                float inf = penetratorTopographyData.getData()[j][0] + advance;
                float sup = penetratorTopographyData.getData()[j][1] + advance;
                float lowerBoundOfOverlap = Math.max(inf, INF);
                float upperBoundOfOverlap = Math.min(sup, SUP);
                if (lowerBoundOfOverlap <= upperBoundOfOverlap) {
                    // Overlap exists, calculate and return the length
                    overlapFlagList.add(new OverlapFlag(this.entities[i], lowerBoundOfOverlap));
                    overlapFlagList.add(new OverlapFlag(this.entities[i], upperBoundOfOverlap));
                }
            }
        }
       return overlapFlagList.stream().collect(Collectors.groupingBy(OverlapFlag::getEntity))
                .entrySet().stream().filter((e)->{
                    List<OverlapFlag> list = e.getValue();
                    list.sort(Comparator.comparing(OverlapFlag::getValue));
                    float overlap = list.get(list.size()-1).value - list.get(0).value;
                    return overlap>=0.05f;
                }).map(Map.Entry::getKey).distinct().collect(Collectors.toList());
    }

    public boolean doesOverlap(TopographyData penetratorTopographyData, float advance) {
        for(int j = 0; j< penetratorTopographyData.length; j++) {
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
}
