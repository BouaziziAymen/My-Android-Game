package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.blocks.BlockA;
import java.util.HashSet;

public class BlockQueryCallBack implements QueryCallback {
    private HashSet<BlockA> blocks = new HashSet<>();

    public void reset(){
        blocks.clear();
    }
    @Override
    public boolean reportFixture(Fixture fixture)
    {

        BlockA block = (BlockA) fixture.getUserData();
        if(block!=null)
        blocks.add(block);
        return true;
    }

    public HashSet<BlockA> getBlocks() {
        return blocks;
    }
}
