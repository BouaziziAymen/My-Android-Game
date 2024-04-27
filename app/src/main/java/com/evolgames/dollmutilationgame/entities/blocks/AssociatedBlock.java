package com.evolgames.dollmutilationgame.entities.blocks;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.cut.Cut;
import com.evolgames.dollmutilationgame.utilities.BlockUtils;
import com.evolgames.dollmutilationgame.entities.properties.Properties;

import java.util.ArrayList;

public abstract class AssociatedBlock<T extends AssociatedBlock<T, P>, P extends Properties>
        extends Block<T, P> {

    @Override
    protected void checkShape() {
        setAborted(getVertices().size() < 3);
    }

    @Override
    protected void rectifyVertices() {
        BlockUtils.bruteForceRectificationDecoration(getVertices());
    }

    @Override
    protected boolean shouldRectify() {
        return true;
    }

    @Override
    protected boolean shouldArrangeVertices() {
        return true;
    }

    @Override
    protected boolean shouldCheckShape() {
        return true;
    }

    @Override
    public void performCut(Cut cut) {
        super.performCut(cut);
        Pair<ArrayList<Vector2>, ArrayList<Vector2>> list =
                BlockUtils.splitVerticesSimple(cut, getVertices());
        T b1 = createChildBlock();
        b1.initialization(list.first, (Properties) getProperties().clone(), 0);
        T b2 = createChildBlock();
        b2.initialization(list.second, (Properties) getProperties().clone(), 0);
        addBlock(b1);
        addBlock(b2);
    }

    public void onStep(GameEntity parent) {
    }
}
