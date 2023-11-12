package com.evolgames.entities.blockvisitors;

import com.evolgames.entities.blocks.LayerBlock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class BreakVisitor<T> {
  protected boolean shatterPerformed;
  protected ArrayList<LayerBlock> splintersBlocks;
  protected HashSet<LayerBlock> abortedBlocks;
  public BreakVisitor() {
    this.splintersBlocks = new ArrayList<>();
    this.abortedBlocks = new HashSet<>();
  }

  public abstract void visitTheElement(T element);

  public boolean isShatterPerformed() {
    return shatterPerformed;
  }

  public List<LayerBlock> getSplintersBlocks() {
    return splintersBlocks;
  }

  public HashSet<LayerBlock> getAbortedBlocks() {
    return abortedBlocks;
  }
}
