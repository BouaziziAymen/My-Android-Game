package com.evolgames.entities.blocks;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.ColoredProperties;
import com.evolgames.helpers.utilities.BlockUtils;

import java.util.ArrayList;

public abstract class InvisibleBlock<T extends InvisibleBlock<T, P>, P extends ColoredProperties> extends Block<T, P> {


    @Override
    protected void checkShape() {
        if (getVertices().size() < 3) {
            setAborted(true);
        } else {
            setAborted(false);
        }
    }

    @Override
    protected boolean rectify() {
        return false;
    }

    @Override
    protected boolean arrange() {
        return true;
    }

    @Override
    protected boolean check() {
        return true;
    }

    @Override
    public void performCut(Cut cut) {
        super.performCut(cut);
        Pair<ArrayList<Vector2>, ArrayList<Vector2>> list = BlockUtils.splitVerticesSimple(cut, getVertices());
        T b1 = createChildBlock();
        b1.initialization(list.first, getProperties(), 0,false);
        T b2 = createChildBlock();
        b2.initialization(list.second, getProperties(), 0,false);
        addBlock(b1);
        addBlock(b2);
    }

}
