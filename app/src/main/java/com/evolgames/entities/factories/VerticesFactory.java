package com.evolgames.entities.factories;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class VerticesFactory {

  public static ArrayList<Vector2> createRectangle(
      float x, float y, float wleft, float wright, float hup, float hdown) {

    ArrayList<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(x - wleft, y - hdown));
    list.add(Vector2Pool.obtain(x + wright, y - hdown));
    list.add(Vector2Pool.obtain(x + wright, y + hup));
    list.add(Vector2Pool.obtain(x - wleft, y + hup));

    return list;
  }

  public static ArrayList<Vector2> createRectangle(float x, float y, float w, float h) {

    float hw = w / 2;
    float hh = h / 2;
    ArrayList<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(x - hw, y - hh));
    list.add(Vector2Pool.obtain(x + hw, y - hh));
    list.add(Vector2Pool.obtain(x + hw, y + hh));
    list.add(Vector2Pool.obtain(x - hw, y + hh));

    return list;
  }

  public static ArrayList<Vector2> createRectangle(float w, float h) {

    float hw = w / 2;
    float hh = h / 2;
    ArrayList<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(-hw, -hh));
    list.add(Vector2Pool.obtain(+hw, -hh));
    list.add(Vector2Pool.obtain(+hw, +hh));
    list.add(Vector2Pool.obtain(-hw, +hh));

    return list;
  }

  public static ArrayList<Vector2> createShape4(float H1, float H2, float R1, float R2) {
    ArrayList<Vector2> list = new ArrayList<>();

    float H = H1 + H2;
    float y1 = H / 2;
    float y2 = -H / 2;
    float y3 = y2 + H2;
    float teta = 0;
    int n = 8;
    for (int i = 0; i < n; i++) {

      list.add(Vector2Pool.obtain(R1 * (float) Math.cos(teta), y1 + R1 * (float) Math.sin(teta)));
      teta += Math.PI / n;
    }
    list.add(Vector2Pool.obtain(-R1, y3));

    for (int i = 0; i < n; i++) {

      list.add(Vector2Pool.obtain(R2 * (float) Math.cos(teta), y2 + R2 * (float) Math.sin(teta)));
      teta += Math.PI / n;
    }
    list.add(Vector2Pool.obtain(R1, y3));

    return list;
  }

  public static ArrayList<Vector2> createShape5(float H1, float H2, float L, float R1, float R2) {
    ArrayList<Vector2> list = new ArrayList<Vector2>();

    float H = H1 + H2;
    float y1 = H / 2;
    float y2 = -H / 2;
    float y3 = -H / 2 + H2;
    float teta = 0;
    int n = 8;
    for (int i = 0; i < n; i++) {

      list.add(Vector2Pool.obtain(R1 * (float) Math.cos(teta), y1 + R1 * (float) Math.sin(teta)));
      teta += Math.PI / n;
    }
    list.add(Vector2Pool.obtain(-L, y3));

    for (int i = 0; i < n; i++) {

      list.add(Vector2Pool.obtain(R2 * (float) Math.cos(teta), y2 + R2 * (float) Math.sin(teta)));
      teta += Math.PI / n;
    }
    list.add(Vector2Pool.obtain(L, y3));

    return list;
  }

  public static ArrayList<Vector2> createShape1(float H, float R1, float R2) {
    ArrayList<Vector2> list = new ArrayList<Vector2>();

    float y1 = H / 2;
    float y2 = -H / 2;
    float teta = 0;
    for (int i = 0; i < 5; i++) {

      list.add(Vector2Pool.obtain(R1 * (float) Math.cos(teta), y1 + R1 * (float) Math.sin(teta)));
      teta += Math.PI / 5;
    }

    for (int i = 0; i < 5; i++) {

      list.add(Vector2Pool.obtain(R2 * (float) Math.cos(teta), y2 + R2 * (float) Math.sin(teta)));
      teta += Math.PI / 5;
    }

    return list;
  }

  public static ArrayList<Vector2> createShape2(float H, float R1, float R2) {
    ArrayList<Vector2> list = new ArrayList<Vector2>();

    float y1 = H / 2;
    float y2 = -H / 2;
    float teta = 0;
    for (int i = 0; i < 5; i++) {

      list.add(Vector2Pool.obtain(R1 * (float) Math.cos(teta), y1 + R1 * (float) Math.sin(teta)));
      teta += Math.PI / 5;
    }

    list.add(Vector2Pool.obtain(-R2, y2));
    list.add(Vector2Pool.obtain(0, y2 - R2));
    list.add(Vector2Pool.obtain(R2, y2));

    return list;
  }

  public static List<Vector2> createRectangleTriangles(float w, float h, float x, float y) {

    float hw = w / 2;
    float hh = h / 2;
    List<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(-hw, -hh).add(x, y));
    list.add(Vector2Pool.obtain(+hw, -hh).add(x, y));
    list.add(Vector2Pool.obtain(+hw, +hh).add(x, y));
    list.add(Vector2Pool.obtain(-hw, +hh).add(x, y));
    return list;
  }

  public static ArrayList<Vector2> createDistorted(float w1, float w2, float h, float x, float y) {

    float hw1 = w1 / 2;
    float hw2 = w2 / 2;
    float hh = h / 2;
    ArrayList<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(-hw2, -hh).add(x, y));
    list.add(Vector2Pool.obtain(+hw2, -hh).add(x, y));
    list.add(Vector2Pool.obtain(+hw1, +hh).add(x, y));
    list.add(Vector2Pool.obtain(-hw1, +hh).add(x, y));
    return list;
  }

  public static ArrayList<ArrayList<Vector2>> createDistorted2(
      float w1, float w2, float h, float x, float y, float ratio) {

    ArrayList<ArrayList<Vector2>> list = new ArrayList<ArrayList<Vector2>>();
    float gamma = ratio * (-w1 + w2) / 2;
    float w3 = w1 + 2 * gamma;
    ArrayList<Vector2> list1 =
        VerticesFactory.createDistorted(w1, w3, h * ratio, x, y + h * (1 - ratio) / 2);
    ArrayList<Vector2> list2 =
        VerticesFactory.createDistorted(w3, w2, h * (1 - ratio), x, y - h * ratio / 2);
    list.add(list1);
    list.add(list2);

    return list;
  }

  public static ArrayList<Vector2> createPoly() {
    ArrayList<Vector2> list = new ArrayList<Vector2>();
    list.add(Vector2Pool.obtain(0, 0));
    list.add(Vector2Pool.obtain(100, 100));
    list.add(Vector2Pool.obtain(-200, 100));
    list.add(Vector2Pool.obtain(-300, 50));
    list.add(Vector2Pool.obtain(-200, 0));

    return list;
  }

  public static ArrayList<Vector2> createPoly2() {
    ArrayList<Vector2> list = new ArrayList<Vector2>();

    list.add(Vector2Pool.obtain(100, 25));
    list.add(Vector2Pool.obtain(25, 25));
    list.add(Vector2Pool.obtain(25, 75));
    list.add(Vector2Pool.obtain(100, 75));
    list.add(Vector2Pool.obtain(100, 100));
    list.add(Vector2Pool.obtain(0, 100));
    list.add(Vector2Pool.obtain(0, 0));
    list.add(Vector2Pool.obtain(100, 0));
    return list;
  }

  public static ArrayList<Vector2> createPolygon(
      float x, float y, float angle, float w, float h, int n) {

    ArrayList<Vector2> list = new ArrayList<>();

    for (int i = 0; i < n; i++) {

      list.add(
          Vector2Pool.obtain(x + w * (float) Math.cos(angle), y + h * (float) Math.sin(angle)));
      angle += 2 * Math.PI / n;
    }
    return list;
  }

  public static ArrayList<Vector2> createPolygon(float x, float y, float w, float h, int n) {

    ArrayList<Vector2> list = new ArrayList<Vector2>();

    float teta = 0;
    for (int i = 0; i < n; i++) {

      list.add(Vector2Pool.obtain(x + w * (float) Math.cos(teta), y + h * (float) Math.sin(teta)));
      teta += 2 * Math.PI / n;
    }
    return list;
  }

  public static ArrayList<Vector2> createEgg(float x, float y, float l, float a, float b, int n) {

    ArrayList<Vector2> list = new ArrayList<Vector2>();

    float teta = 0;
    for (int i = 0; i < n; i++) {

      float cos = (float) Math.cos(teta);
      float sin = (float) Math.sin(teta);
      list.add(Vector2Pool.obtain(y + (a + b * cos) * sin, x + l * cos + (a + b * cos) * cos));
      teta += 2 * Math.PI / n;
    }
    return list;
  }

  public static ArrayList<Vector2> createShape6(float R, float h) {

    ArrayList<Vector2> list = new ArrayList<Vector2>();
    float teta = (float) (Math.PI / 2);
    for (int i = 0; i < 5; i++) {
      list.add(Vector2Pool.obtain(R * (float) Math.cos(teta), R * (float) Math.sin(teta)));
      teta -= Math.PI / 5;
    }

    list.add(Vector2Pool.obtain(-h, -R));

    return list;
  }

  public static ArrayList<Vector2> mirrorShapeX(ArrayList<Vector2> shape) {

    ArrayList<Vector2> list = new ArrayList<>();
    for (Vector2 v : shape) list.add(new Vector2(-v.x, v.y));

    return list;
  }

  public static ArrayList<Vector2> createSquare(float side) {
    ArrayList<Vector2> list = new ArrayList<Vector2>();
    float hside = side / 2;

    list.add(Vector2Pool.obtain(-hside, hside));
    list.add(Vector2Pool.obtain(-hside, -hside));
    list.add(Vector2Pool.obtain(hside, -hside));
    list.add(Vector2Pool.obtain(hside, hside));
    return list;
  }

  public ArrayList<Vector2> createPolygon(int n, float teta0, float w, float h, Vector2 begin) {

    float teta = teta0;
    ArrayList<Vector2> shape = new ArrayList<Vector2>();
    for (int i = 0; i < n; i++) {
      shape.add(
          new Vector2(begin.x + w * (float) Math.cos(teta), begin.y + h * (float) Math.sin(teta)));
      teta += 2 * Math.PI / n;
    }
    return shape;
  }
}
