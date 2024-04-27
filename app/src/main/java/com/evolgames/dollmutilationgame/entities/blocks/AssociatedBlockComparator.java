package com.evolgames.dollmutilationgame.entities.blocks;

import java.util.Comparator;

public class AssociatedBlockComparator implements Comparator<Block<?, ?>> {
    @Override
    public int compare(Block o1, Block o2) {
        if (o1 instanceof DecorationBlock && o2 instanceof CoatingBlock) return -1;
        if (o2 instanceof DecorationBlock && o1 instanceof CoatingBlock) return 1;
        return 0;
    }
}
