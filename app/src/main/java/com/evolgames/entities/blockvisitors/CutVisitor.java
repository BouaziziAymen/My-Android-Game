package com.evolgames.entities.blockvisitors;

import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.blocks.BlockA;

import java.util.ArrayList;

public class CutVisitor implements Visitor<BlockA> {

    CutVisitor(ArrayList<Cut> cuts){

    }

    @Override
    public void visitTheElement(BlockA block) {

    }
}
