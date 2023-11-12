package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointsFreshCut extends FreshCut {
  private final List<CutPoint> points;

  public PointsFreshCut(List<CutPoint> pointsData, float length, int limit, Vector2 velocity) {
    super(length, limit, velocity);
    this.points = pointsData;
  }

  public List<CutPoint> getPoints() {
    return points;
  }
}
