package com.evolgames.entities.blocks;

import java.util.Comparator;

public class AssociatedBlockComparator implements Comparator<Block<?,?>> {
    @Override
    public int compare(Block o1, Block o2) {
        if(o1 instanceof DecorationBlockConcrete&&o2 instanceof CoatingBlock)return -1;
        if(o2 instanceof DecorationBlockConcrete&&o1 instanceof CoatingBlock)return 1;
        return 0;
    }
}
