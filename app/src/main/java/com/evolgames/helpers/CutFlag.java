package com.evolgames.helpers;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class CutFlag implements Comparable<CutFlag> {
  private int i;
  private int ni;
  private Vector2 v;
  private float value;

  public CutFlag(Vector2 v, int i, int ni) {
    this.v = v;
    this.i = i;
    this.ni = ni;
    ArrayList list;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public int getNi() {
    return ni;
  }

  public void setNi(int ni) {
    this.ni = ni;
  }

  public Vector2 getV() {
    return v;
  }

  public void setV(Vector2 v) {
    this.v = v;
  }

  @Override
  public int compareTo(CutFlag another) {
    return (this.value >= another.value) ? 1 : -1;
  }

  public String toString() {
    return v.toString();
  }
}
