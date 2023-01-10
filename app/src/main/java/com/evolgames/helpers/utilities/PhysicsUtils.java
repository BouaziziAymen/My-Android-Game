package com.evolgames.helpers.utilities;

import com.evolgames.entities.blocks.CoatingBlock;

public class PhysicsUtils {

    public static void transferHeatByConduction(float density1, float density2, float length, CoatingBlock g1, CoatingBlock g2, float heat_conductivity1, float heat_conductivity2, float specific_heat1, float specific_heat2){
        float Conductivity = g1.getTemperature()<g2.getTemperature() ?heat_conductivity1:heat_conductivity2;
       // float d = Math.min(thickness2, thickness1);
        double crossSection = length * 1;
        double deltaT = g1.getTemperature()-g2.getTemperature();
        float minStep = Math.min(g1.getStep(),g2.getStep());
        double Q = Conductivity * crossSection* deltaT*minStep/32f;
        double deltaT1 = -Q /(density1 * specific_heat1);
        double deltaT2 = Q /(density2 * specific_heat2);
        if(g1.getTemperature()+deltaT1>0) {
            g1.applyDeltaTemperature(deltaT1);
        }
        else {
            g1.setTemperature(0);
        }
        if(g2.getTemperature()+deltaT2>0) {
            g2.applyDeltaTemperature(deltaT2);
        }
        else {
            g2.setTemperature(0);
        }
    }


    public static void transferHeatByConvection(float heat_conductivity,float specificHeat,float density, double gas_temperature, CoatingBlock solidCoatingBlock){
        double deltaT = gas_temperature-solidCoatingBlock.getTemperature();
        double Q = heat_conductivity * deltaT;
        double deltaT1 = Q/(density*specificHeat);
       if(solidCoatingBlock.getTemperature()+deltaT1>0)
       solidCoatingBlock.applyDeltaTemperature(deltaT1);
       else solidCoatingBlock.setTemperature(0);
    }


}
