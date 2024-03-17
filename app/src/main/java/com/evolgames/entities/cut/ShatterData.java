package com.evolgames.entities.cut;


public class ShatterData {

    private Cut destructionCut;
    private float destructionEnergy;
    private boolean isNonValid;

    public ShatterData() {
        isNonValid = true;
    }

    public ShatterData(Cut destructionCut, float destructionEnergy) {
        this.destructionCut = destructionCut;
        this.destructionEnergy = destructionEnergy;
    }

    public boolean isNonValid() {
        return isNonValid;
    }

    public Cut getDestructionCut() {
        return destructionCut;
    }

    public float getDestructionEnergy() {
        return destructionEnergy;
    }
}
