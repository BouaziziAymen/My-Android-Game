package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.helpers.utilities.BlockUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ShatterVisitor extends BreakVisitor<LayerBlock> {
    private final Vector2 localImpactPoint;
    private float availableEnergy;
    private boolean isError = false;


    public ShatterVisitor(float energy, Vector2 localPoint) {
        super();
        availableEnergy = energy;
        this.localImpactPoint = localPoint;

    }

    private void processBlock(LayerBlock layerBlock) {
        ShatterData data = BlockUtils.applyCut(layerBlock, localImpactPoint);
        if (data == null) {
            isError = true;
            return;
        }
        availableEnergy -= data.getDestructionEnergy();
        if (availableEnergy >= 0 && !data.isNonValid()) {
            shatterPerformed = true;
            layerBlock.performCut(data.getDestructionCut());
        }
    }

    @Override
    public void visitTheElement(LayerBlock cutBlock) {
        ArrayDeque<LayerBlock> deque = new ArrayDeque<>();
        deque.push(cutBlock);
        while (!deque.isEmpty()) {
            LayerBlock current = deque.peek();
            if (current == null) {
                break;
            }
            if (current.isNotAborted()) {
                this.processBlock(current); //visit
            }

            if (this.isExhausted() || this.isError()) {
                break;
            }
            for (LayerBlock component : current.getChildren()) {
                if (component.isNotAborted()) deque.addLast(component);
            }
            deque.pop();
        }


        Iterator<LayerBlock> iterator = cutBlock.createIterator();
        while (iterator.hasNext()) {
            LayerBlock bl = iterator.next();
            if (bl.isNotAborted()) {
                splintersBlocks.add(bl);
            } else {
                abortedBlocks.add(bl);
            }
        }

    }

    public boolean isExhausted() {
        return availableEnergy <= 0;
    }

    public boolean isError() {
        return isError;
    }
}
