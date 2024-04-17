package com.evolgames.entities.blockvisitors;

import com.evolgames.entities.blocks.LayerBlock;

import java.util.HashSet;
import java.util.List;

public abstract class BreakVisitor<T> {
    protected boolean shatterPerformed;
    protected HashSet<LayerBlock> abortedBlocks;

    public BreakVisitor() {
        this.abortedBlocks = new HashSet<>();
    }

    public abstract List<LayerBlock> visitTheElement(T element);

    public boolean isShatterPerformed() {
        return shatterPerformed;
    }

}
