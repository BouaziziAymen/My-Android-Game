package com.evolgames.helpers.utilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.scenes.EditorScene;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.color.Color;

public class Utils {
  public static final Random RAND = new Random();
  private static final Random rand = new Random();

  public static JointDef copyJointDef(JointDef jointDef) {
    switch (jointDef.type) {
      case Unknown:
        break;
      case RevoluteJoint:
        break;
      case PrismaticJoint:
        break;
      case DistanceJoint:
        break;
      case PulleyJoint:
        break;
      case MouseJoint:
        break;
      case GearJoint:
        break;
      case LineJoint:
        break;
      case WeldJoint:
        WeldJointDef weldJointDef = (WeldJointDef) jointDef;
        WeldJointDef copy = new WeldJointDef();
        copy.collideConnected = weldJointDef.collideConnected;
        copy.localAnchorA.set(weldJointDef.localAnchorA);
        copy.localAnchorB.set(weldJointDef.localAnchorB);
        copy.referenceAngle = weldJointDef.referenceAngle;
        return copy;
      case FrictionJoint:
        break;
    }
    return null;
  }

  public static boolean PointInPolygon(Vector2 point, List<Vector2> points) {

    int i, j, nvert = points.size();
    boolean c = false;

    for (i = 0, j = nvert - 1; i < nvert; j = i++) {
      if (points.get(i).y >= point.y != points.get(j).y >= point.y
          && point.x
              <= (points.get(j).x - points.get(i).x)
                      * (point.y - points.get(i).y)
                      / (points.get(j).y - points.get(i).y)
                  + points.get(i).x) c = !c;
    }

    return c;
  }

  public static Color getRandomColor(float alpha) {

    return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), alpha);
  }

  public static void drawPath(ArrayList<Vector2> path, Color color) {
    for (int i = 0; i < path.size(); i++) {
      int ni = i == path.size() - 1 ? 0 : i + 1;
      EditorScene.plotter.drawLine2(path.get(i), path.get(ni), color, 1);
    }
  }

  public static float shortestDistance(float x1, float y1, float x2, float y2, float x3, float y3) {
    float px = x2 - x1;
    float py = y2 - y1;
    float temp = px * px + py * py;
    float u = ((x3 - x1) * px + (y3 - y1) * py) / temp;
    if (u > 1) {
      u = 1;
    } else if (u < 0) {
      u = 0;
    }
    float x = x1 + u * px;
    float y = y1 + u * py;

    float dx = x - x3;
    float dy = y - y3;
    float dist = (float) Math.sqrt(dx * dx + dy * dy);
    return dist;
  }

  public static void translatePoint(Vector2 point, Vector2 center) {
    point.sub(center);
  }

  public static void translatePoints(List<Vector2> points, Vector2 center) {
    for (Vector2 v : points) {
      v.sub(center);
    }
  }

  public static void translatePoints(List<Vector2> points, float dx, float dy) {
    for (Vector2 v : points) v.sub(dx, dy);
  }

  public static ArrayList<Vector2> translatedPoints(Vector2[] points, Vector2 center) {
    ArrayList<Vector2> layerPoints = new ArrayList<>();
    for (Vector2 v : points) layerPoints.add(v.cpy().sub(center));
    return layerPoints;
  }

  public static Color getColorWithAlpha() {

    return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
  }

  public static Color getRandomColor() {

    return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
  }

  public static Color getColorNonBlack() {

    return new Color(
        rand.nextFloat() * 0.7f + 0.3f,
        rand.nextFloat() * 0.7f + 0.3f,
        rand.nextFloat() * 0.7f + 0.3f);
  }

  public static void rotateVector2Rad(Vector2 v, float angle) {

    float rx = (float) (v.x * Math.cos(angle) - v.y * Math.sin(angle));
    float ry = (float) (v.x * Math.sin(angle) + v.y * Math.cos(angle));
    v.x = rx;
    v.y = ry;
  }

  public static void rotateVector2(Vector2 v, float angle) {
    float r = (float) (angle / 360 * 2 * Math.PI);

    float rx = (float) (v.x * Math.cos(r) - v.y * Math.sin(r));
    float ry = (float) (v.x * Math.sin(r) + v.y * Math.cos(r));
    v.x = rx;
    v.y = ry;
  }

  public static ArrayList<Vector2> generateTest() {
    ArrayList<Vector2> points = new ArrayList<>();
    Vector2 point0 = Vector2Pool.obtain(400, 240);
    Vector2 u = Vector2Pool.obtain(1, 0);
    float angle = (float) Math.random() * 360;
    GeometryUtils.rotateVectorDeg(u, angle);
    Vector2 point1 = point0.cpy().add(u.cpy().mul(12 + (float) Math.random() * 4));
    GeometryUtils.rotateVectorDeg(u, 89 + 2 * (float) Math.random());
    Vector2 point2 = point0.cpy().add(u.cpy().mul(12 + (float) Math.random() * 4));
    points.add(point0);
    points.add(point1);
    points.add(point2);
    return points;
  }

  static boolean successive(int order1, int order2, int n) {
    boolean result;
    result =
        (order1 == n - 1) ? order2 == 0 : order2 == order1 + 1;
    return result;
  }

  public static boolean areNeighbors(int ord1, int ord2, int n) {
    return (successive(ord1, ord2, n) || successive(ord2, ord1, n));
  }

  public static <T> T[] concatWithStream(T[] array1, T[] array2) {
    return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
        .toArray(size -> (T[]) Array.newInstance(array1.getClass().getComponentType(), size));
  }

  public static float[] mapPointsToArray(List<Vector2> points) {
    float[] pointsData = new float[points.size() * 2];
    for (int i = 0; i < points.size(); i++) {
      pointsData[2 * i] = points.get(i).x;
      pointsData[2 * i + 1] = points.get(i).y;
    }
    return pointsData;
  }

  public static float[] mapWeightsToArray(List<Float> list) {
    float accumulated = 0f;
    float[] result = new float[list.size()];
    for (int i = 0; i < list.size(); i++) {
      accumulated += list.get(i);
      result[i] = accumulated;
    }
    for (int i = 0; i < result.length; i++) {
      result[i] /= accumulated;
    }
    return result;
  }
}
