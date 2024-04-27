package com.evolgames.dollmutilationgame.entities.blockvisitors;

import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.cut.Cut;

import java.util.ArrayList;

public class CutVisitor implements Visitor<LayerBlock> {

    CutVisitor(ArrayList<Cut> cuts) {
    }

    @Override
    public void visitTheElement(LayerBlock block) {
    }
}
