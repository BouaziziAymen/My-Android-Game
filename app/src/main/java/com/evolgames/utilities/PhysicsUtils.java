package com.evolgames.utilities;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.physics.PhysicsConstants;

public class PhysicsUtils {

    public static void transferHeatByConduction(
            float density1,
            float density2,
            float length,
            CoatingBlock g1,
            CoatingBlock g2) {
        double crossSection = length * 1;
        double deltaT = g1.getTemperature() - g2.getTemperature();
        float minStep = Math.min(g1.getStep(), g2.getStep());
        double Q = crossSection * deltaT * minStep / PhysicsConstants.HEAT_CONSTANT;
        double deltaT1 = -Q / (density1 * g1.getProperties().getHeatResistance()) * Math.random();
        double deltaT2 = Q / (density2 * g2.getProperties().getHeatResistance()) * Math.random();
        if (g1.getTemperature() + deltaT1 > 0) {
            g1.applyDeltaTemperature(deltaT1);
        } else {
            g1.setTemperature(0);
        }
        if (g2.getTemperature() + deltaT2 > 0) {
            g2.applyDeltaTemperature(deltaT2);
        } else {
            g2.setTemperature(0);
        }
    }

    public static void transferHeatByConvection(
            float specificHeat, double gas_temperature, CoatingBlock solidCoatingBlock) {
        double tempDifference = gas_temperature - solidCoatingBlock.getTemperature();

        double deltaTemperature = tempDifference / (PhysicsConstants.HEAT_CONSTANT * specificHeat);

        solidCoatingBlock.applyDeltaTemperature(deltaTemperature);
    }
}
