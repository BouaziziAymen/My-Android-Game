package com.evolgames.pools;

import com.evolgames.entities.blocks.BlockA;
import org.andengine.util.adt.pool.GenericPool;

public class BlockPool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
public static int recyledTimes;
    private static final GenericPool<BlockA> POOL = new GenericPool<BlockA>() {
        @Override
        protected BlockA onAllocatePoolItem() {
            return new BlockA();
        }
    };

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public static BlockA obtain() {
        return POOL.obtainPoolItem();
    }



    public static void recycle(final BlockA recycledBlock) {
        recyledTimes++;
        recycledBlock.getChildren().clear();
        recycledBlock.setGameEntity(null);
        if(recycledBlock.isDead()){
            recycledBlock.recycleSelf();
            recycledBlock.setDead(false);
        }
        if(recycledBlock.getFreshCuts()!=null)
            recycledBlock.getFreshCuts().clear();
        if(recycledBlock.getAssociatedBlocks()!=null)
        recycledBlock.getAssociatedBlocks().clear();
        if(recycledBlock.getFreshCuts()!=null)
        recycledBlock.getFreshCuts().clear();
        if(recycledBlock.getKeys()!=null)
        recycledBlock.getKeys().clear();
        POOL.recyclePoolItem(recycledBlock);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}

