package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.Block;
import com.evolgames.utilities.BlockUtils;
import java.util.ArrayList;
import java.util.List;

public class ClipVisitor implements Visitor<Block<?, ?>> {

  private List<Vector2> clipPath;
  private List<Vector2> result;

  public void setClipPath(ArrayList<Vector2> clipPath) {
    this.clipPath = clipPath;
  }

  @Override
  public void visitTheElement(Block<?, ?> block) {
    result = BlockUtils.applyClip(block.getVertices(), clipPath);
  }

  public List<Vector2> getResult() {
    return result;
  }
}
