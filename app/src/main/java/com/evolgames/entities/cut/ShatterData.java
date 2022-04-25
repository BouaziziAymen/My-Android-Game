package com.evolgames.entities.cut;


import com.evolgames.entities.blocks.CoatingBlock;

public class ShatterData {

    private Cut destructionCut;
    private float destructionEnergy;

    public boolean isNonValid() {
        return isNonValid;
    }

    private boolean isNonValid;

    public ShatterData() {
        isNonValid = true;
    }

    public ShatterData(Cut destructionCut, float destructionEnergy) {
        this.destructionCut = destructionCut;
        this.destructionEnergy = destructionEnergy;
    }


    public Cut getDestructionCut() {
        return destructionCut;
    }


    public float getDestructionEnergy() {
        return destructionEnergy;
    }



}
