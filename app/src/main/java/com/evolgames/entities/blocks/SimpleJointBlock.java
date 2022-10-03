package com.evolgames.entities.blocks;

import com.evolgames.entities.cut.Cut;

public class SimpleJointBlock extends JointBlock{

    private SimpleJointBlock twin;

    @Override
    protected JointBlock createChildBlock() {
        return new SimpleJointBlock();
    }

    @Override
    public void performCut(Cut cut) {}

    public void setTwin(SimpleJointBlock twin) {
        this.twin = twin;
    }
}
