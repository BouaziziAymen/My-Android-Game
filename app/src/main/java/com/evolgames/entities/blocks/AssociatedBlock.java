package com.evolgames.entities.blocks;

import android.util.Pair;
import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.BlockUtils;
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
    b1.initialization(list.first, getProperties(), 0);
    T b2 = createChildBlock();
    b2.initialization(list.second, getProperties().copy(), 0);
    addBlock(b1);
    addBlock(b2);
  }
}
