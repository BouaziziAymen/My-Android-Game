package com.evolgames.dollmutilationgame.entities.pools;

import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

import org.andengine.util.adt.pool.GenericPool;

public class BlockPool {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================

    private static final GenericPool<LayerBlock> POOL =
            new GenericPool<LayerBlock>() {
                @Override
                protected LayerBlock onAllocatePoolItem() {
                    return new LayerBlock();
                }
            };
    // ===========================================================
    // Fields
    // ===========================================================
    public static int recyledTimes;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    public static LayerBlock obtain() {
        return POOL.obtainPoolItem();
    }

    public static void recycle(final LayerBlock recycledBlock) {
        recyledTimes++;
        recycledBlock.getChildren().clear();
        if (recycledBlock.isDead()) {
            recycledBlock.recycleSelf();
            recycledBlock.setDead(false);
        }
        if (recycledBlock.getFreshCuts() != null) {
            recycledBlock.getFreshCuts().clear();
        }
        if (recycledBlock.getAssociatedBlocks() != null) {
            recycledBlock.getAssociatedBlocks().clear();
        }
        if (recycledBlock.getFreshCuts() != null) {
            recycledBlock.getFreshCuts().clear();
        }
        POOL.recyclePoolItem(recycledBlock);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
