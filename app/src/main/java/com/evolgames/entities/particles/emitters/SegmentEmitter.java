package com.evolgames.entities.particles.emitters;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import org.andengine.util.math.MathUtils;

public class SegmentEmitter extends DataEmitter {

  private final float beginX;
  private final float beginY;
  private final float endX;
  private final float endY;

  public SegmentEmitter(float[] data) {
    super((data[0] + data[2]) / 2, (data[1] + data[3]) / 2, data);
    this.beginX = data[0];
    this.beginY = data[1];
    this.endX = data[2];
    this.endY = data[3];
  }

  @Override
  public void getPositionOffset(float[] pOffset) {
    float rand = MathUtils.RANDOM.nextFloat();
    if (data[2] - data[0] != 0.0) {
      float slope = (data[3] - data[1]) / (data[2] - data[0]);
      pOffset[VERTEX_INDEX_X] = this.data[0] + rand * (this.data[2] - data[0]);
      pOffset[VERTEX_INDEX_Y] = this.data[1] + slope * rand * (this.data[2] - data[0]);
    } else {
      pOffset[VERTEX_INDEX_X] = this.data[0];
      pOffset[VERTEX_INDEX_Y] = this.data[1] + rand * (this.data[3] - data[1]);
    }
  }

  @Override
  protected void prepareData() {
    data[0] = beginX;
    data[1] = beginY;
    data[2] = endX;
    data[3] = endY;
  }
}
