package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.blocks.LayerBlock;
import java.util.HashSet;

public class BlockQueryCallBack implements QueryCallback {
    private HashSet<LayerBlock> blocks = new HashSet<>();

    public void reset(){
        blocks.clear();
    }
    @Override
    public boolean reportFixture(Fixture fixture)
    {

        LayerBlock block = (LayerBlock) fixture.getUserData();
        if(block!=null)
        blocks.add(block);
        return true;
    }

    public HashSet<LayerBlock> getBlocks() {
        return blocks;
    }
}
