package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blockvisitors.ClipVisitor;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.utilities.Utils;
import java.util.ArrayList;

public final class DecorationBlock extends AssociatedBlock<DecorationBlock, DecorationProperties> {

  @Override
  protected void calculateArea() {}

  @Override
  protected boolean shouldCalculateArea() {
    return false;
  }

  @Override
  protected DecorationBlock createChildBlock() {
    return new DecorationBlock();
  }

  @Override
  public void translate(Vector2 translationVector) {
    Utils.translatePoints(this.getVertices(), translationVector);
    computeTriangles();
  }

  @Override
  protected DecorationBlock getThis() {
    return this;
  }

  public void applyClip(ArrayList<Vector2> clipPath) {
    ClipVisitor clipVisitor = new ClipVisitor();
    clipVisitor.setClipPath(clipPath);
    clipVisitor.visitTheElement(this);
    setVertices(clipVisitor.getResult());
  }
}
