package com.evolgames.utilities;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.physics.PhysicsConstants;

public class PhysicsUtils {

    public static void transferHeatByConduction(
            float length,
            CoatingBlock g1,
            CoatingBlock g2) {
        double crossSection = length * 1;
        double deltaT = g1.getTemperature() - g2.getTemperature();
        float minStep = Math.min(g1.getStep(), g2.getStep());
        double Q = crossSection * deltaT * minStep / PhysicsConstants.HEAT_CONSTANT;
        double deltaT1 = -Q / (g1.getProperties().getHeatResistance()) * Math.random();
        double deltaT2 = Q / (g2.getProperties().getHeatResistance()) * Math.random();

            g1.applyDeltaTemperature(deltaT1);
            g2.applyDeltaTemperature(deltaT2);
    }

    public static void transferHeatByConvection(
            float specificHeat, double gas_temperature, CoatingBlock solidCoatingBlock) {
        double tempDifference = gas_temperature - solidCoatingBlock.getTemperature();

        double deltaTemperature = tempDifference / (PhysicsConstants.HEAT_CONSTANT * specificHeat);

        solidCoatingBlock.applyDeltaTemperature(deltaTemperature);
    }
}
