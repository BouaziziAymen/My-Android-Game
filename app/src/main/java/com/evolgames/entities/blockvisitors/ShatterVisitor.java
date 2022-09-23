package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.helpers.utilities.BlockUtils;

public class ShatterVisitor implements Visitor<LayerBlock> {
    private final Vector2 localImpactPoint;
    private float availableEnergy;
    private boolean isError = false;
    private boolean shatterPerformed;

    public ShatterVisitor(float energy, Vector2 localPoint) {
        availableEnergy = energy;
        this.localImpactPoint = localPoint;

    }

    public boolean isShatterPerformed() {
        return shatterPerformed;
    }

    private void setShatterPerformed() {
        this.shatterPerformed = true;
    }

    @Override
    public void visitTheElement(LayerBlock cutBlock) {
        ShatterData data = BlockUtils.shatterData(cutBlock, localImpactPoint);
        if (data == null) {
            isError = true;
            return;
        }
        availableEnergy -= data.getDestructionEnergy();
        if (availableEnergy >= 0 && !data.isNonValid()) {
            System.out.println("Inside visitor shatter");
            setShatterPerformed();
            cutBlock.performCut(data.getDestructionCut());
        }
    }

    public boolean isExhausted() {
        return availableEnergy <= 0;
    }

    public boolean isError() {
        return isError;
    }
}
