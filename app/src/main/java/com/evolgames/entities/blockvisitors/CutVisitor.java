package com.evolgames.entities.blockvisitors;

import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.Cut;
import java.util.ArrayList;

public class CutVisitor implements Visitor<LayerBlock> {

  CutVisitor(ArrayList<Cut> cuts) {}

  @Override
  public void visitTheElement(LayerBlock block) {}
}
