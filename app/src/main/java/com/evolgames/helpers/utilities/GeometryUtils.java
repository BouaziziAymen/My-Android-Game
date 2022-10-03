package com.evolgames.helpers.utilities;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.caliper.Caliper;
import com.evolgames.caliper.Polygon;
import com.evolgames.helpers.CutFlag;
import com.evolgames.helpers.Hull;
import com.evolgames.helpers.VectorComparator;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.transformation.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import is.kul.learningandengine.helpers.MathUtils;

public class GeometryUtils {
    public static final float TO_DEGREES = 1 / (float) Math.PI * 180;
    public static Hull hullFinder = new Hull();
    public static Random random = new Random();

    public static float dst(float x1, float y1, float x2, float y2) {
        float x_d = x1 - x2;
        float y_d = y1 - y2;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public static Transformation transformation = new Transformation();

    public static Vector2 imageMirror(Vector2 mirror, Vector2 vector) {
        float angle = (float) (-Math.atan2(mirror.y, mirror.x) + Math.atan2(vector.y, vector.x));
        return MathUtils.rotateZ(vector, -2 * angle);
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

    public static Vector2 mirrorPoint(Vector2 point, Vector2 begin, Vector2 end) {
        Vector2 direction = end.cpy().sub(begin);
        Vector2 v = point.cpy().sub(begin);
        return begin.cpy().add(GeometryUtils.imageMirror(direction, v));
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

    public static boolean IsClockwise(ArrayList<Vector2> vertices) {


        double sum = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vector2 v1 = vertices.get(i);
            Vector2 v2 = vertices.get((i + 1) % vertices.size());
            sum += (v2.x - v1.x) * (v2.y + v1.y);
        }
        return sum < 0;
    }

    private static Vector2 midPoint(float p1x, float p1y, float p2x, float p2y) {
        float mx = (p1x + p2x) / 2;
        float my = (p1y + p2y) / 2;
        return Vector2Pool.obtain(mx, my);
    }

    private static Vector2 FindLineCircleIntersection(
            float cx, float cy, float radius,
            Vector2 point1, Vector2 point2) {
        float dx, dy, A, B, C, det;

        dx = point2.x - point1.x;
        dy = point2.y - point1.y;

        A = dx * dx + dy * dy;
        B = 2 * (dx * (point1.x - cx) + dy * (point1.y - cy));
        C = (point1.x - cx) * (point1.x - cx) +
                (point1.y - cy) * (point1.y - cy) -
                radius * radius;

        det = B * B - 4 * A * C;
        if (A <= 0.0000001 || det < 0) {
            // No real solutions.

            return null;
        } else if (det == 0) {
            // One solution.
            float t1 = -B / (2 * A);

            return Vector2Pool.obtain(point1.x + t1 * dx, point1.y + t1 * dy);

        } else {
            // Two solutions.
            float t1 = (float) ((-B + Math.sqrt(det)) / (2 * A));

            float t2 = (float) ((-B - Math.sqrt(det)) / (2 * A));

            return midPoint(point1.x + t1 * dx, point1.y + t1 * dy, point1.x + t2 * dx, point1.y + t2 * dy);
        }
    }

    public static float projection(Vector2 point, Vector2 center, Vector2 eVector) {
        return (point.x - center.x) * eVector.x + (point.y - center.y) * eVector.y;
    }

    public static Vector2 getIntersectionWithPolygon(float x, float y, ArrayList<Vector2> points) {
        for (int i = 0; i < points.size(); i++) {
            int ni = (i == points.size() - 1) ? 0 : i + 1;
            Vector2 inter = FindLineCircleIntersection(x, y, 5, points.get(i), points.get(ni));
            if (inter != null) return inter;
        }
        return null;
    }


    public static boolean PointInPolygon(Vector2 point, ArrayList<Vector2> points) {

        int i, j, nvert = points.size();
        boolean c = false;

        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (points.get(i).y >= point.y != points.get(j).y >= point.y &&
                    point.x <= (points.get(j).x - points.get(i).x) * (point.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x
            )
                c = !c;
        }

        return c;
    }

    public static boolean PointInPolygon(float x, float y, ArrayList<Vector2> points) {

        int i, j, nvert = points.size();
        boolean c = false;

        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            if (points.get(i).y >= y != points.get(j).y >= y &&
                    x <= (points.get(j).x - points.get(i).x) * (y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x
            )
                c = !c;
        }

        return c;
    }

    private static Vector2 lineIntersectPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

        if (denom == 0.0) { // Lines are parallel.
            return null;
        }
        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f) {
            // Get the intersection point.
            return Vector2Pool.obtain((float) (x1 + ua * (x2 - x1)), (float) (y1 + ua * (y2 - y1)));
        }

        return null;
    }

    public static Vector2 lineIntersectPoint(Vector2 s1, Vector2 e1, Vector2 s2, Vector2 e2) {
        return lineIntersectPoint(s1.x, s1.y, e1.x, e1.y, s2.x, s2.y, e2.x, e2.y);

    }

    public static boolean doLinesIntersections(Vector2 s1, Vector2 e1, List<Vector2> Vertices) {
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 s2 = Vertices.get(i);
            Vector2 e2 = Vertices.get(ni);
            Vector2 inter = lineIntersectPoint(s1.x, s1.y, e1.x, e1.y, s2.x, s2.y, e2.x, e2.y);
            if (inter != null) return true;
        }
        return false;
    }

    public static List<Vector2> lineIntersections(Vector2 s1, Vector2 e1, List<Vector2> Vertices) {
        List<Vector2> intersections = new ArrayList<>();
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 s2 = Vertices.get(i);
            Vector2 e2 = Vertices.get(ni);
            Vector2 inter = lineIntersectPoint(s1.x, s1.y, e1.x, e1.y, s2.x, s2.y, e2.x, e2.y);
            if (inter != null) intersections.add(inter);
        }
        Collections.sort(intersections, (o1, o2) -> o1.dst(s1) <= o2.dst(s1) ? -1 : 1);
        return intersections;
    }

    public static boolean isOnBorder(Vector2 point, ArrayList<Vector2> Vertices) {
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 v1 = Vertices.get(i);
            Vector2 v2 = Vertices.get(ni);
            if (GeometryUtils.PointOnLineSegment(v1, v2, point, 2f)) return true;
        }
        return false;
    }

    public static boolean isIntersection(List<Vector2> Vertices, Vector2 head, Vector2 next) {
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 v1 = Vertices.get(i);
            Vector2 v2 = Vertices.get(ni);
            Vector2 u = Vector2Pool.obtain(v1.x - v2.x, v1.y - v2.y).nor().mul(0.1f);
            Vector2 V1 = Vector2Pool.obtain(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = Vector2Pool.obtain(v2.x - u.x, v2.y - u.y);

            Vector2 intersection = lineIntersectPoint(head, next, V1, V2);
            if (intersection != null) {
                return true;
            }
            Vector2Pool.recycle(u);
            Vector2Pool.recycle(V1);
            Vector2Pool.recycle(V2);
        }
        return false;
    }

    public static Vector2 getIntersection(List<Vector2> Vertices, Vector2 head, Vector2 next) {
        ArrayList<CutFlag> candidates = new ArrayList<>();
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 v1 = Vertices.get(i);
            Vector2 v2 = Vertices.get(ni);
            Vector2 u = Vector2Pool.obtain(v1.x - v2.x, v1.y - v2.y).nor().mul(0.1f);
            Vector2 V1 = Vector2Pool.obtain(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = Vector2Pool.obtain(v2.x - u.x, v2.y - u.y);

            Vector2 intersection = lineIntersectPoint(head, next, V1, V2);
            if (intersection != null) {
                CutFlag CutFlag = new CutFlag(intersection, i, 0);
                CutFlag.setValue(head.dst(intersection));
                candidates.add(CutFlag);
            }
            Vector2Pool.recycle(u);
            Vector2Pool.recycle(V1);
            Vector2Pool.recycle(V2);
        }
        Collections.sort(candidates);
        if (candidates.size() > 0)
            return candidates.get(0).getV();
        else return null;

    }

    public static Vector2 getOrthogonalProjection(Vector2 A, Vector2 B, Vector2 C) {
        float x1 = A.x, y1 = A.y, x2 = B.x, y2 = B.y, x3 = C.x, y3 = C.y;
        float px = x2 - x1, py = y2 - y1, dAB = px * px + py * py;
        float u = ((x3 - x1) * px + (y3 - y1) * py) / dAB;
        float x = x1 + u * px, y = y1 + u * py;
        return new Vector2(x, y); //this is D
    }

    public static boolean PointOnLineSegment(Vector2 pt1, Vector2 pt2, Vector2 pt, float epsilon) {
        if (pt.x - Math.max(pt1.x, pt2.x) > epsilon ||
                Math.min(pt1.x, pt2.x) - pt.x > epsilon ||
                pt.y - Math.max(pt1.y, pt2.y) > epsilon ||
                Math.min(pt1.y, pt2.y) - pt.y > epsilon)
            return false;

        if (Math.abs(pt2.x - pt1.x) < epsilon)
            return Math.abs(pt1.x - pt.x) < epsilon || Math.abs(pt2.x - pt.x) < epsilon;
        if (Math.abs(pt2.y - pt1.y) < epsilon)
            return Math.abs(pt1.y - pt.y) < epsilon || Math.abs(pt2.y - pt.y) < epsilon;

        double x = pt1.x + (pt.y - pt1.y) * (pt2.x - pt1.x) / (pt2.y - pt1.y);
        double y = pt1.y + (pt.x - pt1.x) * (pt2.y - pt1.y) / (pt2.x - pt1.x);

        return Math.abs(pt.x - x) < epsilon || Math.abs(pt.y - y) < epsilon;
    }

    public static void generateRandomPointInTriangle(float[] point, float Ax, float Ay, float Bx, float By, float Cx, float Cy) {
        float r1 = random.nextFloat();
        float r2 = random.nextFloat();
        float sqrtR1 = (float) Math.sqrt((r1));
        float Px = (1 - sqrtR1) * Ax + (sqrtR1 * (1 - r2)) * Bx + (sqrtR1 * r2) * Cx;
        float Py = (1 - sqrtR1) * Ay + (sqrtR1 * (1 - r2)) * By + (sqrtR1 * r2) * Cy;
        point[0] = Px;
        point[1] = Py;
    }

    public static Vector2 generateRandomPointInTriangle(Vector2 A, Vector2 B, Vector2 C) {
        float r1 = random.nextFloat();
        float r2 = random.nextFloat();
        float sqrtR1 = (float) Math.sqrt((r1));
        float Px = (1 - sqrtR1) * A.x + (sqrtR1 * (1 - r2)) * B.x + (sqrtR1 * r2) * C.x;
        float Py = (1 - sqrtR1) * A.y + (sqrtR1 * (1 - r2)) * B.y + (sqrtR1 * r2) * C.y;
        return new Vector2(Px, Py);
    }

    static CutFlag getIntersectionData(ArrayList<Vector2> Vertices, Vector2 head, Vector2 next) {
        ArrayList<CutFlag> candidates = new ArrayList<>();
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = (i == Vertices.size() - 1) ? 0 : i + 1;
            Vector2 v1 = Vertices.get(i);
            Vector2 v2 = Vertices.get(ni);
            Vector2 u = Vector2Pool.obtain(v1.x - v2.x, v1.y - v2.y).nor();
            Vector2 V1 = Vector2Pool.obtain(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = Vector2Pool.obtain(v2.x - u.x, v2.y - u.y);
            Vector2 intersection = lineIntersectPoint(head, next, V1, V2);
            if (intersection != null && PointOnLineSegment(v1, v2, intersection, 0.01f)) {
                CutFlag flag = new CutFlag(intersection, i, ni);
                flag.setValue(head.dst(intersection));
                candidates.add(flag);
            }
            Vector2Pool.recycle(u);
            Vector2Pool.recycle(V1);
            Vector2Pool.recycle(V2);
        }

        Collections.sort(candidates);
        if (candidates.size() > 0)
            return candidates.get(0);
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

    public static float getArea(ArrayList<Vector2> Vertices) {


        float A = 0;
        for (int i = 0; i < Vertices.size(); i++) {
            int ni = i == Vertices.size() - 1 ? 0 : i + 1;
            A += Vertices.get(i).x * Vertices.get(ni).y - Vertices.get(ni).x * Vertices.get(i).y;
        }
        A = A / 2;
        if (A >= 0) return A;
        else return -A;
    }

    public static ArrayList<Vector2> generateRandomSpecialConvexPolygon(int n) {
        ArrayList<Vector2> polygon;
        do {
            polygon = generateRandomConvexPolygon(n);
        } while (!testPolygon(polygon));
        return polygon;
    }

    private static boolean testPolygon(ArrayList<Vector2> polygon) {
        for (int i = 0; i < polygon.size(); i++) {
            int ni = (i == polygon.size() - 1) ? 0 : i + 1;
            if (polygon.get(i).dst(polygon.get(ni)) < 2) return false;
        }
        return true;
    }

    public static Vector2 calculateCenter(List<List<Vector2>> blocks) {
        List<Vector2> list = new ArrayList<>();
        for (List<Vector2> block : blocks) list.addAll(block);
        Vector2[] vertices = GeometryUtils.hullFinder.findConvexHull(list.toArray(new Vector2[0]));
        if (vertices.length < 3) return null;
        Polygon polygon = new Polygon(Arrays.asList(vertices));
        com.evolgames.caliper.Rectangle rectangle = Caliper.minimumBox(polygon);
        return rectangle.getCenter();
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
        int pointCount = verticesData.length/2;
        for (int i = 0; i < pointCount; i++) {
            final float pointX = verticesData[2*i];
            final float pointY = verticesData[2*i+1];
            x += pointX;
            y += pointY;
        }

        x = x / pointCount;
        y = y / pointCount;

        return Vector2Pool.obtain(x, y);
    }


    public static Vector2 calculateCentroid(ArrayList<Vector2> vertices, int offset, int count) {
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
        Collections.sort(vec, new VectorComparator());

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


    public static boolean doLayersIntersect(ArrayList<Vector2> layer1, ArrayList<Vector2> layer2) {

        for (Vector2 point : layer1) {
            if (PointInPolygon(point, layer2)) {
                return true;
            }
        }
        for (Vector2 point : layer2) {
            if (PointInPolygon(point, layer1)) {
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

    private static boolean lineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0.0) { // Lines are parallel.
            return false;
        }
        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        return ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f;

    }

    public static float distBetweenPointAndPolygon(float x, float y, ArrayList<Vector2> points) {
        if (PointInPolygon(x, y, points)) return 0;
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            int ni = (i == points.size() - 1) ? 0 : i + 1;
            Vector2 P1 = points.get(i);
            Vector2 P2 = points.get(ni);
            float d = distancePointToSegment(x, y, P1.x, P1.y, P2.x, P2.y);
            if (d < distance) {
                distance = d;
            }
        }
        return distance;
    }

    private static float distancePointToSegment(float x, float y, float x1, float y1, float x2, float y2) {

        float A = x - x1;
        float B = y - y1;
        float C = x2 - x1;
        float D = y2 - y1;

        float dot = A * C + B * D;
        float len_sq = C * C + D * D;
        float param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        float xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        float dx = x - xx;
        float dy = y - yy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float distBetweenPointAndLine(float x, float y, float x1, float y1, float x2, float y2) {
        // BlockA - the standalone point (x, y)
        // B - start point of the line segment (x1, y1)
        // C - end point of the line segment (x2, y2)
        // D - the crossing point between line from BlockA to BC

        float AB = distBetween(x, y, x1, y1);
        float BC = distBetween(x1, y1, x2, y2);
        float AC = distBetween(x, y, x2, y2);

        // Heron's formula
        float s = (AB + BC + AC) / 2;
        float area = (float) Math.sqrt(s * (s - AB) * (s - BC) * (s - AC));

        // but also area == (BC * AD) / 2
        // BC * AD == 2 * area
        // AD == (2 * area) / BC
        // TODO: check if BC == 0
        return (2 * area) / BC;
    }

    private static float distBetween(float x, float y, float x1, float y1) {
        float xx = x1 - x;
        float yy = y1 - y;

        return (float) Math.sqrt(xx * xx + yy * yy);
    }

    /**
     * Get projected point P' of P on line e1.
     *
     * @return projected point p.
     */
    private static Vector2 getProjectedPointOnLine(Vector2 v1, Vector2 v2, float x, float y) {
        // get dot product of e1, e2
        Vector2 e1 = Vector2Pool.obtain(v2.x - v1.x, v2.y - v1.y);
        Vector2 e2 = Vector2Pool.obtain(x - v1.x, y - v1.y);
        double valDp = e1.dot(e2);
        // get length of vectors
        double lenLineE1 = Math.sqrt(e1.x * e1.x + e1.y * e1.y);
        double lenLineE2 = Math.sqrt(e2.x * e2.x + e2.y * e2.y);
        double cos = valDp / (lenLineE1 * lenLineE2);
        // length of v1P'
        double projLenOfLine = cos * lenLineE2;
        Vector2 pp = Vector2Pool.obtain((int) (v1.x + (projLenOfLine * e1.x) / lenLineE1),
                (int) (v1.y + (projLenOfLine * e1.y) / lenLineE1));
        return pp;
    }

    private static boolean isProjectedPointOnLineSegment(Vector2 v1, Vector2 v2, Vector2 p) {
        // get dotproduct |e1| * |e2|
        Vector2 e1 = Vector2Pool.obtain(v2.x - v1.x, v2.y - v1.y);
        double recArea = e1.dot(e1);
        // dot product of |e1| * |e2|
        Vector2 e2 = Vector2Pool.obtain(p.x - v1.x, p.y - v1.y);
        double val = e1.dot(e2);
        return (val > 0 && val < recArea);
    }

    private static boolean doPolygonsIntersect(List<Vector2> vertices1, List<Vector2> vertices2) {
        System.out.println("----------------");
        System.out.println(""+vertices2);
        for (int i = 0; i < vertices1.size(); i++) {
            int ni = (i == vertices1.size() - 1) ? 0 : i + 1;
            Vector2 p1 = vertices1.get(i);
            Vector2 p2 = vertices1.get(ni);
            if (GeometryUtils.doLinesIntersections(p1, p2, vertices2)) return true;
        }
        return false;
    }

}
