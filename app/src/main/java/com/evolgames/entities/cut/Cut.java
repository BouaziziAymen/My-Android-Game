package com.evolgames.entities.cut;

import android.support.annotation.NonNull;
import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.BlockUtils;
import java.util.List;

public class Cut implements Comparable<Cut> {
  private final Vector2 lower1;
  private final Vector2 higher1;
  private final Vector2 lower2;
  private final Vector2 higher2;
  private final float length;
  public Vector2 center;
  private Vector2 p1;
  private Vector2 p2;
  private Vector2 copy1;
  private Vector2 copy2;

  public Cut(Vector2 p1, Vector2 p2, Vector2 l1, Vector2 h1, Vector2 l2, Vector2 h2) {
    this.p1 = p1;
    this.p2 = p2;
    this.lower1 = l1;
    this.higher1 = h1;
    this.lower2 = l2;
    this.higher2 = h2;
    this.length = p1.dst(p2);
  }

  public Vector2 getLower1() {
    return lower1;
  }

  public Vector2 getHigher1() {
    return higher1;
  }

  public Vector2 getLower2() {
    return lower2;
  }

  public Vector2 getHigher2() {
    return higher2;
  }

  @NonNull
  public String toString() {
    return this.p1 + "/" + this.p2 + "(" + center + ")";
  }

  public Vector2 getP1() {
    return this.p1;
  }

  public void setP1(Vector2 p1) {
    this.p1 = p1;
  }

  public Vector2 getP2() {
    return this.p2;
  }

  public void setP2(Vector2 p2) {
    this.p2 = p2;
  }

  @Override
  public int compareTo(Cut another) {
    if (getLength() < another.getLength()) {
      return -1;
    } else {
      return 1;
    }
  }

  public float getLength() {
    return this.length;
  }

  public boolean isValid(List<Vector2> vertices) {
    int a = vertices.indexOf(getP1());
    int b = vertices.indexOf(getP2());
    if (BlockUtils.areNeighbors(a, b, vertices.size())) return false;
    if (getP1() == getP2()) return false;
    if (getHigher2() == getP1() || getLower2() == getP1()) return false;
    if (getHigher1() == getP2() || getLower1() == getP2()) return false;
    if (getLower1() == getLower2()) return false;
    return getHigher1() != getHigher2();
  }

  public Vector2 getP2Brother() {
    return copy2;
  }

  public void setP2Brother(Vector2 copy2) {
    this.copy2 = copy2;
  }

  public Vector2 getP1Brother() {
    return copy1;
  }

  public void setP1Brother(Vector2 copy1) {
    this.copy1 = copy1;
  }
}
