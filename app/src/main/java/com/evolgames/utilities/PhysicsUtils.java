package com.evolgames.utilities;

import com.evolgames.entities.blocks.CoatingBlock;

public class PhysicsUtils {

    public static void transferHeatByConduction(
            float density1,
            float density2,
            float length,
            CoatingBlock g1,
            CoatingBlock g2,
            float heat_conductivity1,
            float heat_conductivity2,
            float heatResistance1,
            float heatResistance2) {
        float Conductivity =
                g1.getTemperature() < g2.getTemperature() ? heat_conductivity1 : heat_conductivity2;
        double crossSection = length * 1;
        double deltaT = g1.getTemperature() - g2.getTemperature();
        float minStep = Math.min(g1.getStep(), g2.getStep());
        double Q = Conductivity * crossSection * deltaT * minStep / 32f;
        double deltaT1 = -Q / (density1 * heatResistance1) * Math.random();
        double deltaT2 = Q / (density2 * heatResistance2) * Math.random();
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
        double deltaTemperature = tempDifference / (specificHeat * 10000f);

        if (solidCoatingBlock.getTemperature() + deltaTemperature > 0) {
            solidCoatingBlock.applyDeltaTemperature(deltaTemperature);
        } else {
            solidCoatingBlock.setTemperature(0);
        }
    }
}
