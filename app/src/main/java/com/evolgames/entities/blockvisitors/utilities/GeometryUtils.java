package com.evolgames.entities.blockvisitors.utilities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.caliper.Caliper;
import com.evolgames.entities.caliper.Polygon;
import com.evolgames.entities.caliper.Rectangle;
import com.evolgames.helpers.CutFlag;
import com.evolgames.helpers.Hull;
import com.evolgames.helpers.VectorComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.transformation.Transformation;

public class GeometryUtils {
  public static final float TO_DEGREES = 1 / (float) Math.PI * 180;
  public static Hull hullFinder = new Hull();
  public static Random random = new Random();
  public static Transformation transformation = new Transformation();

  public static float dst(float x1, float y1, float x2, float y2) {
    float x_d = x1 - x2;
    float y_d = y1 - y2;
    return (float) Math.sqrt(x_d * x_d + y_d * y_d);
  }

  public static Vector2 imageMirror(Vector2 mirror, Vector2 vector) {
    float angle = (float) (-Math.atan2(mirror.y, mirror.x) + Math.atan2(vector.y, vector.x));
    return GeometryUtils.rotatedVectorRad(vector, -2 * angle);
  }

  public static float calculateTriangleArea(Vector2 A, Vector2 B, Vector2 C) {
    float area = (A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y)) / 2.0f;
    return Math.abs(area);
  }

  public static void rotate(Vector2 point, float angle, Vector2 begin) {
    Vector2 u = new Vector2(point.x - begin.x, point.y - begin.y);
    Utils.rotateVector2Rad(u, angle);
    float newX = u.x + begin.x;
    float newY = u.y + begin.y;
    point.set(newX, newY);
  }

  public static List<Vector2> mirrorPoints(List<Vector2> points, Vector2 begin, Vector2 end) {
    List<Vector2> mirroredPoints = new ArrayList<>();
    Vector2 direction = end.cpy().sub(begin);
    for (int i = 0; i < points.size(); i++) {
      Vector2 v = points.get(i).cpy().sub(begin);
      Vector2 mirroredPoint = begin.cpy().add(GeometryUtils.imageMirror(direction, v));
      mirroredPoints.add(mirroredPoint);
    }
    return mirroredPoints;
  }

  public static boolean IsClockwise(List<Vector2> vertices) {

    double sum = 0;
    for (int i = 0; i < vertices.size(); i++) {
      Vector2 v1 = vertices.get(i);
      Vector2 v2 = vertices.get((i + 1) % vertices.size());
      sum += (v2.x - v1.x) * (v2.y + v1.y);
    }
    return sum < 0;
  }

  public static float projection(Vector2 point, Vector2 center, Vector2 eVector) {
    return (point.x - center.x) * eVector.x + (point.y - center.y) * eVector.y;
  }

  public static boolean isPointInPolygon(Vector2 point, List<Vector2> points) {

    int i, j, size = points.size();
    boolean c = false;

    for (i = 0, j = size - 1; i < size; j = i++) {
      if (points.get(i).y >= point.y != points.get(j).y >= point.y
          && point.x
              <= (points.get(j).x - points.get(i).x)
                      * (point.y - points.get(i).y)
                      / (points.get(j).y - points.get(i).y)
                  + points.get(i).x) c = !c;
    }

    return c;
  }
  public static boolean isPointInPolygon(float x, float y,Vector2[] points) {

    int i, j, size = points.length;
    boolean c = false;

    for (i = 0, j = size - 1; i < size; j = i++) {
      if (points[i].y >= y != points[j].y >= y
              && x
              <= (points[j].x - points[i].x)
              * (y - points[i].y)
              / (points[j].y - points[i].y)
              + points[i].x) c = !c;
    }

    return c;
  }

  public static boolean isPointInPolygon(float x, float y, List<Vector2> points) {

    int i, j, size = points.size();
    boolean c = false;

    for (i = 0, j = size - 1; i < size; j = i++) {
      if (points.get(i).y >= y != points.get(j).y >= y
          && x
              <= (points.get(j).x - points.get(i).x)
                      * (y - points.get(i).y)
                      / (points.get(j).y - points.get(i).y)
                  + points.get(i).x) c = !c;
    }

    return c;
  }

  private static Vector2 lineIntersectPoint(
      double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {

    double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

    if (denominator == 0.0) { // Lines are parallel.
      return null;
    }
    double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
    double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;
    if (ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f) {
      // Get the intersection point.
      return Vector2Pool.obtain((float) (x1 + ua * (x2 - x1)), (float) (y1 + ua * (y2 - y1)));
    }

    return null;
  }

  public static Vector2 lineIntersectPoint(Vector2 s1, Vector2 e1, Vector2 s2, Vector2 e2) {
    return lineIntersectPoint(s1.x, s1.y, e1.x, e1.y, s2.x, s2.y, e2.x, e2.y);
  }

  public static boolean isPointOnLineSegment(Vector2 pt1, Vector2 pt2, Vector2 pt, float epsilon) {
    if (pt.x - Math.max(pt1.x, pt2.x) > epsilon
        || Math.min(pt1.x, pt2.x) - pt.x > epsilon
        || pt.y - Math.max(pt1.y, pt2.y) > epsilon
        || Math.min(pt1.y, pt2.y) - pt.y > epsilon) return false;

    if (Math.abs(pt2.x - pt1.x) < epsilon)
      return Math.abs(pt1.x - pt.x) < epsilon || Math.abs(pt2.x - pt.x) < epsilon;
    if (Math.abs(pt2.y - pt1.y) < epsilon)
      return Math.abs(pt1.y - pt.y) < epsilon || Math.abs(pt2.y - pt.y) < epsilon;

    double x = pt1.x + (pt.y - pt1.y) * (pt2.x - pt1.x) / (pt2.y - pt1.y);
    double y = pt1.y + (pt.x - pt1.x) * (pt2.y - pt1.y) / (pt2.x - pt1.x);

    return Math.abs(pt.x - x) < epsilon || Math.abs(pt.y - y) < epsilon;
  }

  public static void generateRandomPointInTriangle(
      float[] point, float Ax, float Ay, float Bx, float By, float Cx, float Cy) {
    float r1 = random.nextFloat();
    float r2 = random.nextFloat();
    float sqrtR1 = (float) Math.sqrt((r1));
    float Px = (1 - sqrtR1) * Ax + (sqrtR1 * (1 - r2)) * Bx + (sqrtR1 * r2) * Cx;
    float Py = (1 - sqrtR1) * Ay + (sqrtR1 * (1 - r2)) * By + (sqrtR1 * r2) * Cy;
    point[0] = Px;
    point[1] = Py;
  }

  @SuppressWarnings("unused")
  public static Vector2 generateRandomPointInTriangle(Vector2 A, Vector2 B, Vector2 C) {
    float r1 = random.nextFloat();
    float r2 = random.nextFloat();
    float sqrtR1 = (float) Math.sqrt((r1));
    float Px = (1 - sqrtR1) * A.x + (sqrtR1 * (1 - r2)) * B.x + (sqrtR1 * r2) * C.x;
    float Py = (1 - sqrtR1) * A.y + (sqrtR1 * (1 - r2)) * B.y + (sqrtR1 * r2) * C.y;
    return new Vector2(Px, Py);
  }

  static CutFlag getIntersectionData(List<Vector2> Vertices, Vector2 head, Vector2 next) {
    ArrayList<CutFlag> candidates = new ArrayList<>();
    for (int i = 0; i < Vertices.size(); i++) {
      int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
      Vector2 v1 = Vertices.get(i);
      Vector2 v2 = Vertices.get(ni);
      Vector2 u = Vector2Pool.obtain(v1.x - v2.x, v1.y - v2.y).nor();
      Vector2 V1 = Vector2Pool.obtain(v1.x + u.x, v1.y + u.y);
      Vector2 V2 = Vector2Pool.obtain(v2.x - u.x, v2.y - u.y);
      Vector2 intersection = lineIntersectPoint(head, next, V1, V2);
      if (intersection != null && isPointOnLineSegment(v1, v2, intersection, 0.01f)) {
        CutFlag flag = new CutFlag(intersection, i, ni);
        flag.setValue(head.dst(intersection));
        candidates.add(flag);
      }
      Vector2Pool.recycle(u);
      Vector2Pool.recycle(V1);
      Vector2Pool.recycle(V2);
    }

    Collections.sort(candidates);
    if (candidates.size() > 0) return candidates.get(0);
    else return null;
  }

  public static void rotateVectorDeg(Vector2 v, float angle) {
    float r = (float) (angle / 360 * 2 * Math.PI);

    float rx = (float) (v.x * Math.cos(r) - v.y * Math.sin(r));
    float ry = (float) (v.x * Math.sin(r) + v.y * Math.cos(r));
    v.x = rx;
    v.y = ry;
  }

  public static void rotateVectorRad(Vector2 v, float r) {

    float rx = (float) (v.x * Math.cos(r) - v.y * Math.sin(r));
    float ry = (float) (v.x * Math.sin(r) + v.y * Math.cos(r));
    v.x = rx;
    v.y = ry;
  }
  public static Vector2 rotatedVectorRad(Vector2 v, float r) {

    float rx = (float) (v.x * Math.cos(r) - v.y * Math.sin(r));
    float ry = (float) (v.x * Math.sin(r) + v.y * Math.cos(r));
    return new Vector2(rx,ry);
  }

  public static float getArea(List<Vector2> Vertices) {

    float A = 0;
    for (int i = 0; i < Vertices.size(); i++) {
      int ni = i == Vertices.size() - 1 ? 0 : i + 1;
      A += Vertices.get(i).x * Vertices.get(ni).y - Vertices.get(ni).x * Vertices.get(i).y;
    }
    A = A / 2;
    if (A >= 0) return A;
    else return -A;
  }

  public static ArrayList<Vector2> generateRandomSpecialConvexPolygon(
      int n, float x, float y, float L) {
    ArrayList<Vector2> polygon;
    do {
      polygon = generateRandomConvexPolygon(L, x, y, n);
    } while (!testPolygon(polygon));
    return polygon;
  }

  private static ArrayList<Vector2> generateRandomConvexPolygon(
      float L, float xC, float yC, int n) {
    // Generate two lists of random X and Y coordinates
    ArrayList<Float> xPool = new ArrayList<>(n);
    ArrayList<Float> yPool = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      xPool.add(Utils.RAND.nextFloat() * L + xC);
      yPool.add(Utils.RAND.nextFloat() * L + yC);
    }

    // Sort them
    Collections.sort(xPool);
    Collections.sort(yPool);

    // Isolate the extreme points
    Float minX = xPool.get(0);
    Float maxX = xPool.get(n - 1);
    Float minY = yPool.get(0);
    Float maxY = yPool.get(n - 1);

    // Divide the interior points into two chains & Extract the vector components
    ArrayList<Float> xVec = new ArrayList<>(n);
    ArrayList<Float> yVec = new ArrayList<>(n);

    float lastTop = minX, lastBot = minX;

    for (int i = 1; i < n - 1; i++) {
      float X = xPool.get(i);

      if (Utils.RAND.nextBoolean()) {
        xVec.add(X - lastTop);
        lastTop = X;
      } else {
        xVec.add(lastBot - X);
        lastBot = X;
      }
    }

    xVec.add(maxX - lastTop);
    xVec.add(lastBot - maxX);

    float lastLeft = minY, lastRight = minY;

    for (int i = 1; i < n - 1; i++) {
      float Y = yPool.get(i);

      if (Utils.RAND.nextBoolean()) {
        yVec.add(Y - lastLeft);
        lastLeft = Y;
      } else {
        yVec.add(lastRight - Y);
        lastRight = Y;
      }
    }

    yVec.add(maxY - lastLeft);
    yVec.add(lastRight - maxY);

    // Randomly CutFlag up the X- and Y-components
    Collections.shuffle(yVec);

    // Combine the paired up components into vectors
    ArrayList<Vector2> vec = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      vec.add(Vector2Pool.obtain(xVec.get(i), yVec.get(i)));
    }

    // Sort the vectors by angle
    vec.sort(new VectorComparator());

    // Lay them end-to-end
    float x = 0, y = 0;
    float minPolygonX = 0;
    float minPolygonY = 0;
    ArrayList<Vector2> points = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      points.add(Vector2Pool.obtain(x, y));

      x += vec.get(i).x;
      y += vec.get(i).y;

      minPolygonX = Math.min(minPolygonX, x);
      minPolygonY = Math.min(minPolygonY, y);
    }

    // Move the polygon to the original min and max coordinates
    float xShift = minX - minPolygonX;
    float yShift = minY - minPolygonY;

    for (int i = 0; i < n; i++) {
      Vector2 p = points.get(i);
      points.set(i, Vector2Pool.obtain(p.x + xShift, p.y + yShift));
    }

    return points;
  }

  private static boolean testPolygon(ArrayList<Vector2> polygon) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      if (polygon.get(i).dst(polygon.get(ni)) < 2) return false;
    }
    return true;
  }

  public static Vector2 calculateCenter(List<List<Vector2>> lists) {
    List<Vector2> list = new ArrayList<>();
    for (List<Vector2> vector2List : lists) {
      list.addAll(vector2List);
    }
    if (list.size() < 3) {
      return null;
    }
    Vector2[] vertices = GeometryUtils.hullFinder.findConvexHull(list.toArray(new Vector2[0]));
    if (vertices.length < 3) {
      return null;
    }
    Polygon polygon = new Polygon(Arrays.asList(vertices));
    Rectangle rectangle = Caliper.minimumBox(polygon);
    return rectangle.getCenter();
  }

  public static float calculateShortestDirectedDistance(float angle1, float angle2) {
    // Normalize angles to be between 0 and 360 degrees
    angle1 = normalizeAngle(angle1);
    angle2 = normalizeAngle(angle2);

    // Calculate the absolute difference
    float absoluteDifference = Math.abs(angle1 - angle2);

    // Calculate clockwise distance
    float clockwiseDistance = (angle1 > angle2) ? 360f - absoluteDifference : absoluteDifference;

    // Calculate counterclockwise distance
    float counterclockwiseDistance = 360f - clockwiseDistance;

    // Determine the shortest directed distance
    return clockwiseDistance < counterclockwiseDistance
        ? clockwiseDistance
        : -counterclockwiseDistance;
  }

  public static float calculateAngleRad(float x, float y) {
    // Calculate the angle in radians
    float angleRadians = (float) Math.atan2(y, x);

    // Convert the angle from radians to degrees
    float angleDegrees = (float) Math.toDegrees(angleRadians);

    // Ensure the angle is positive and within the range [0, 360)
    if (angleDegrees < 0) {
      angleDegrees += 360.0;
    }

    return angleDegrees;
  }

  public static float calculateAngle(float x, float y) {
    // Calculate the angle in radians
    float angleRadians = (float) Math.atan2(y, x);

    // Convert the angle from radians to degrees
    float angleDegrees = (float) Math.toDegrees(angleRadians);

    // Ensure the angle is positive and within the range [0, 360)
    if (angleDegrees < 0) {
      angleDegrees += 360.0;
    }

    return angleDegrees;
  }

  public static float normalizeAngle(float angle) {
    angle = angle % 360f;
    if (angle < 0) {
      angle += 360.0;
    }
    return angle;
  }

  public static Vector2 calculateCentroid(List<Vector2> vertices) {
    float x = 0;
    float y = 0;
    int pointCount = vertices.size();
    for (int i = 0; i < pointCount; i++) {
      final Vector2 point = vertices.get(i);
      x += point.x;
      y += point.y;
    }

    x = x / pointCount;
    y = y / pointCount;

    return Vector2Pool.obtain(x, y);
  }

  public static Vector2 calculateCentroid(float[] verticesData) {
    float x = 0;
    float y = 0;
    int pointCount = verticesData.length / 2;
    for (int i = 0; i < pointCount; i++) {
      final float pointX = verticesData[2 * i];
      final float pointY = verticesData[2 * i + 1];
      x += pointX;
      y += pointY;
    }

    x = x / pointCount;
    y = y / pointCount;

    return Vector2Pool.obtain(x, y);
  }

  public static Vector2 calculateCentroid(List<Vector2> vertices, int offset, int count) {
    float x = 0;
    float y = 0;
    for (int i = offset; i < offset + count; i++) {
      final Vector2 point = vertices.get(i);
      x += point.x;
      y += point.y;
    }

    x = x / count;
    y = y / count;

    return Vector2Pool.obtain(x, y);
  }

  @SuppressWarnings("unused")
  private static ArrayList<Vector2> generateRandomConvexPolygon(int n) {
    // Generate two lists of random X and Y coordinates
    ArrayList<Float> xPool = new ArrayList<>(n);
    ArrayList<Float> yPool = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      xPool.add(Utils.RAND.nextFloat() * 40 + 10);
      yPool.add(Utils.RAND.nextFloat() * 40 + 10);
    }

    // Sort them
    Collections.sort(xPool);
    Collections.sort(yPool);

    // Isolate the extreme points
    Float minX = xPool.get(0);
    Float maxX = xPool.get(n - 1);
    Float minY = yPool.get(0);
    Float maxY = yPool.get(n - 1);

    // Divide the interior points into two chains & Extract the vector components
    ArrayList<Float> xVec = new ArrayList<>(n);
    ArrayList<Float> yVec = new ArrayList<>(n);

    float lastTop = minX, lastBot = minX;

    for (int i = 1; i < n - 1; i++) {
      float x = xPool.get(i);

      if (Utils.RAND.nextBoolean()) {
        xVec.add(x - lastTop);
        lastTop = x;
      } else {
        xVec.add(lastBot - x);
        lastBot = x;
      }
    }

    xVec.add(maxX - lastTop);
    xVec.add(lastBot - maxX);

    float lastLeft = minY, lastRight = minY;

    for (int i = 1; i < n - 1; i++) {
      float y = yPool.get(i);

      if (Utils.RAND.nextBoolean()) {
        yVec.add(y - lastLeft);
        lastLeft = y;
      } else {
        yVec.add(lastRight - y);
        lastRight = y;
      }
    }

    yVec.add(maxY - lastLeft);
    yVec.add(lastRight - maxY);

    // Randomly CutFlag up the X- and Y-components
    Collections.shuffle(yVec);

    // Combine the paired up components into vectors
    ArrayList<Vector2> vec = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      vec.add(Vector2Pool.obtain(xVec.get(i), yVec.get(i)));
    }

    // Sort the vectors by angle
    vec.sort(new VectorComparator());

    // Lay them end-to-end
    float x = 0, y = 0;
    float minPolygonX = 0;
    float minPolygonY = 0;
    ArrayList<Vector2> points = new ArrayList<>(n);

    for (int i = 0; i < n; i++) {
      points.add(Vector2Pool.obtain(x, y));

      x += vec.get(i).x;
      y += vec.get(i).y;

      minPolygonX = Math.min(minPolygonX, x);
      minPolygonY = Math.min(minPolygonY, y);
    }

    // Move the polygon to the original min and max coordinates
    float xShift = minX - minPolygonX;
    float yShift = minY - minPolygonY;

    for (int i = 0; i < n; i++) {
      Vector2 p = points.get(i);
      points.set(i, Vector2Pool.obtain(p.x + xShift, p.y + yShift));
    }

    return points;
  }

  public static boolean doLayersIntersect(List<Vector2> layer1, List<Vector2> layer2) {

    for (Vector2 point : layer1) {
      if (isPointInPolygon(point, layer2)) {
        return true;
      }
    }
    for (Vector2 point : layer2) {
      if (isPointInPolygon(point, layer1)) {
        return true;
      }
    }

    for (int i = 0; i < layer1.size(); i++) {

      int ni = i == layer1.size() - 1 ? 0 : i + 1;
      Vector2 p1 = layer1.get(i);
      Vector2 q1 = layer1.get(ni);
      for (int j = 0; j < layer2.size(); j++) {
        int nj = j == layer2.size() - 1 ? 0 : j + 1;
        Vector2 p2 = layer2.get(j);
        Vector2 q2 = layer2.get(nj);

        if (lineIntersect(p1.x, p1.y, q1.x, q1.y, p2.x, p2.y, q2.x, q2.y)) {
          return true;
        }
      }
    }

    return false;
  }

  private static boolean lineIntersect(
      float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
    double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
    if (denominator == 0.0) { // Lines are parallel.
      return false;
    }
    double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
    double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;
    return ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f;
  }

  public static Vector2 calculateProjection(Vector2 point, List<Vector2> vertices) {
    if (GeometryUtils.isPointInPolygon(point, vertices)) {
      return point.cpy();
    }
    Vector2 result = null;
    float minDis = Float.POSITIVE_INFINITY;
    for (int i = 0; i < vertices.size(); i++) {
      int ni = i == vertices.size() - 1 ? 0 : i + 1;
      Vector2 p1 = vertices.get(i).cpy();
      Vector2 p2 = vertices.get(ni).cpy();
      Vector2 proj = GeometryUtils.calculateProjection(p1, p2, point);
      Vector2 p =
          isPointOnLineSegment(p1, p2, proj, 1f) ? proj : proj.dst(p1) < proj.dst(p2) ? p1 : p2;
      float dis = p.dst(point);
      if (dis < minDis) {
        minDis = dis;
        result = p;
      }
    }
    return result;
  }

  public static Vector2 calculateProjection(Vector2 startPoint, Vector2 endPoint, Vector2 point) {
    // Calculate the vector representing the line segment
    float lineVectorX = endPoint.x - startPoint.x;
    float lineVectorY = endPoint.y - startPoint.y;

    // Calculate the vector from the line segment's start point to the given point
    float pointVectorX = point.x - startPoint.x;
    float pointVectorY = point.y - startPoint.y;

    // Calculate the dot product of the two vectors
    float dotProduct = (pointVectorX * lineVectorX) + (pointVectorY * lineVectorY);

    // Calculate the magnitude squared of the line segment vector
    float lineMagnitudeSquared = (lineVectorX * lineVectorX) + (lineVectorY * lineVectorY);

    // Calculate the projection factor
    float projectionFactor = dotProduct / lineMagnitudeSquared;

    // Calculate the projection vector
    float projectionX = startPoint.x + (projectionFactor * lineVectorX);
    float projectionY = startPoint.y + (projectionFactor * lineVectorY);

    // Create and return the projection point
    return new Vector2(projectionX, projectionY);
  }
}
