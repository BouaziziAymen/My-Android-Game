package com.evolgames.helpers.utilities;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.caliper.Caliper;
import com.evolgames.caliper.Polygon;
import com.evolgames.entities.blocks.AssociatedBlockComparator;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.DecorationBlockConcrete;
import com.evolgames.entities.blocks.Polarity;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.entities.joint.JointKey;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.factories.BlockFactory;
import com.evolgames.helpers.CutFlag;
import com.evolgames.helpers.ElementCouple;
import com.evolgames.helpers.UnionFind;
import com.evolgames.physics.PhysicsConstants;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

public class BlockUtils {
    public static float e = 4f;
    public static AssociatedBlockComparator associatedBlockComparator = new AssociatedBlockComparator();

    public static ArrayList<ArrayList<Vector2>> decompose(ArrayList<Vector2> vectors) {
        ArrayList<ArrayList<Vector2>> result = new ArrayList<>();
        int N = vectors.size();
        if (N <= 8) result.add(vectors);
        else {
            ArrayList<Vector2> part1 = new ArrayList<>();
            ArrayList<Vector2> part2 = new ArrayList<>();
            int index = (N - 1) / 2 + 1;

            for (int i = 0; i <= index; i++) part1.add(Vector2Pool.obtain(vectors.get(i)));
            part2.add(Vector2Pool.obtain(vectors.get(index)));
            for (int i = index + 1; i < vectors.size(); i++)
                part2.add(Vector2Pool.obtain(vectors.get(i)));
            part2.add(Vector2Pool.obtain(vectors.get(0)));

            result.addAll(decompose(part1));
            result.addAll(decompose(part2));
        }

        return result;

    }

    public static Pair<ArrayList<Vector2>, ArrayList<Vector2>> splitVerticesSimple(Cut cut, ArrayList<Vector2> vertices) {

        boolean point1_approximated_to_one_of_sides = (cut.getP1() == cut.getLower1() || cut.getP1() == cut.getHigher1());
        boolean point2_approximated_to_one_of_sides = (cut.getP2() == cut.getLower2() || cut.getP2() == cut.getHigher2());


        ArrayList<Vector2> list = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector2 point = vertices.get(i);
            list.add(point);
            if (!point1_approximated_to_one_of_sides) if (point == cut.getLower1()) {
                list.add(cut.getP1());
            }
            if (!point2_approximated_to_one_of_sides) if (point == cut.getLower2()) {
                list.add(cut.getP2());
            }
        }

        ArrayList<Vector2> added = new ArrayList<>(list);
        list.addAll(added);

        ArrayList<Vector2> chunk1 = new ArrayList<>();
        ArrayList<Vector2> chunk2 = new ArrayList<>();
        int begin1 = (list.indexOf(cut.getP1()) == list.size() - 1) ? 0 : list.indexOf(cut.getP1()) + 1;

        HashSet<Vector2> used = new HashSet<>();//for when the cut is approximated to one single point and both chunks contain all

        for (int i = begin1; i < list.size(); i++) {
            Vector2 point = list.get(i);
            if (point == cut.getP2()) break;
            chunk1.add(point);
            used.add(point);
        }
        int begin2 = (list.indexOf(cut.getP2()) == list.size() - 1) ? 0 : list.indexOf(cut.getP2()) + 1;
        for (int i = begin2; i < list.size(); i++) {
            Vector2 point = list.get(i);
            if (point == cut.getP1()) break;
            if (!used.contains(point)) chunk2.add(point);
        }


        chunk1.add(0, cut.getP1());
        chunk1.add(cut.getP2());

        Vector2 copyOfP2 = Vector2Pool.obtain(cut.getP2());
        Vector2 copyOfP1 = Vector2Pool.obtain(cut.getP1());


        chunk2.add(0, copyOfP2);
        chunk2.add(copyOfP1);

        cut.setP1Brother(copyOfP1);
        cut.setP2Brother(copyOfP2);

        return new Pair<>(chunk1, chunk2);
    }

    public static Pair<Pair<ArrayList<Vector2>, ArrayList<Vector2>>, Pair<FreshCut, FreshCut>> splitVertices(Cut cut, ArrayList<Vector2> vertices) {

        Pair<ArrayList<Vector2>, ArrayList<Vector2>> result = splitVerticesSimple(cut, vertices);

        FreshCut pair1 = new FreshCut(cut.getP2(), cut.getP1());
        FreshCut pair2 = new FreshCut(cut.getP1Brother(), cut.getP2Brother());

        return new Pair<>(result, new Pair<>(pair1, pair2));
    }

    private static Vector2 findTheOne(ArrayList<Vector2> Vertices, Vector2 A, Vector2 B, Vector2 centroid) {
        if (GeometryUtils.PointOnLineSegment(A, B, centroid, 0.01f)) return centroid;
        for (Vector2 v : Vertices) {
            if (v.dst(A) < 0.001f || v.dst(B) < 0.001f) continue;
            Vector2 p = GeometryUtils.lineIntersectPoint(v, centroid, A, B);
            if (p != null && p.dst(v) > 0.01f) return p;
        }
        return null;
    }


    private static Cut projectCutWithoutCorrection(ArrayList<Vector2> Vertices, Vector2 first, Vector2 second) {

        Vector2 centroid = GeometryUtils.calculateCentroid(Vertices);
        Vector2 u = Vector2Pool.obtain(second).sub(first).nor().mul(1000);
        Vector2 center = findTheOne(Vertices, first, second, centroid);

        if (center == null) return null;
        Vector2 e1 = Vector2Pool.obtain(center).add(u);
        Vector2 e2 = Vector2Pool.obtain(center).sub(u);

        Cut cut = generateCut(Vertices, center, e1, e2);
        Vector2Pool.recycle(e1);
        Vector2Pool.recycle(e2);
        Vector2Pool.recycle(center);
        return cut;
    }


    private static Cut findCut(ArrayList<Vector2> Vertices, Vector2 firstExtremity, Vector2 secondExtremity) {
        Vector2 centroid = GeometryUtils.calculateCentroid(Vertices);
        Vector2 center = findTheOne(Vertices, firstExtremity, secondExtremity, centroid);
        if (center == null) return null;
        return generateCut(Vertices, center, firstExtremity, secondExtremity);
    }

    private static Vector2 positionCutExtremityOnSide(Vector2 P, Vector2 E1, Vector2 E2) {
        float side = E1.dst(E2);
        float d1 = P.dst(E1);
        float d2 = P.dst(E2);
        if (side >= 2 * e) {//SIDE BIG ENOUGH

            if (d1 <= d2) {
                if (d1 < e) {//PUT X1 AT DISTANCE e FROM E1
                    Vector2 u = Vector2Pool.obtain(E2.x - E1.x, E2.y - E1.y).nor().mul(e);
                    return u.add(E1);
                }
            } else {
                if (d2 < e) {//PUT X1 AT DISTANCE e FROM E2
                    Vector2 u = Vector2Pool.obtain(E1.x - E2.x, E1.y - E2.y).nor().mul(e);
                    return u.add(E2);
                }
            }
        } else {//SIDE TOO SMALL APPROXIMATE TO ONE OF NEIGHBORS

            if (d1 <= d2) {
                return E1;
            } else {
                return E2;
            }
        }
        return P;
    }

    private static Cut generateCut(ArrayList<Vector2> vertices, Vector2 center, Vector2 e1, Vector2 e2) {

        CutFlag pair1, pair2;
        pair1 = GeometryUtils.getIntersectionData(vertices, center, e1);
        pair2 = GeometryUtils.getIntersectionData(vertices, center, e2);
        if (pair1 == null || pair2 == null) return null;
        Vector2 lower1 = vertices.get(pair1.getI());
        Vector2 higher1 = vertices.get(pair1.getNi());
        Vector2 lower2 = vertices.get(pair2.getI());
        Vector2 higher2 = vertices.get(pair2.getNi());

        return new Cut(pair1.getV(), pair2.getV(), lower1, higher1, lower2, higher2);

    }

    private static Cut generateCutCorrected(ArrayList<Vector2> vertices, Vector2 center, Vector2 e1, Vector2 e2) {
        Cut generatedCut = generateCut(vertices, center, e1, e2);
        if (generatedCut != null) return correctCutExtremities(generatedCut);
        return null;
    }

    public static boolean areNeighbors(int i, int j, int n) {
        if (i == -1 || j == -1) return false;
        if (i == j + 1 || j == i + 1) return true;
        return i == 0 && j == n - 1 || j == 0 && i == n - 1;
    }

    private static Cut correctCutExtremities(Cut cut) {
        cut.setOriginalP1(cut.getP1());
        cut.setOriginalP2(cut.getP2());
        Vector2 P1 = positionCutExtremityOnSide(cut.getP1(), cut.getLower1(), cut.getHigher1());
        Vector2 P2 = positionCutExtremityOnSide(cut.getP2(), cut.getLower2(), cut.getHigher2());
        cut.setP1(P1);
        cut.setP2(P2);

        return cut;
    }

    public static Cut correctExtremitiesAndCreateCut(Vector2 p1, Vector2 p2, Vector2 lower1, Vector2 higher1, Vector2 lower2, Vector2 higher2) {
        Cut cut = new Cut(p1, p2, lower1, higher1, lower2, higher2);
        return correctCutExtremities(cut);
    }

    public static void bruteForceRectificationDecoration(ArrayList<Vector2> polygon) {
        boolean repeat;

        do {
            repeat = false;
            Iterator<Vector2> iterator = polygon.iterator();
            while (iterator.hasNext()) {
                int i = polygon.indexOf(iterator.next());
                int pi = (i == 0) ? polygon.size() - 1 : i - 1;


                Vector2 p = polygon.get(i);
                Vector2 pp = polygon.get(pi);

                //using normalized vectors because what matters is the angle as proven by test
                if (Math.abs(p.dst(pp)) < 0.1f) {
                    repeat = true;
                    iterator.remove();
                    break;
                }
            }
        } while (repeat && polygon.size() >= 3);


        do {
            repeat = false;
            Iterator<Vector2> iterator = polygon.iterator();
            while (iterator.hasNext()) {
                int i = polygon.indexOf(iterator.next());
                int pi = (i == 0) ? polygon.size() - 1 : i - 1;
                int ni = (i == polygon.size() - 1) ? 0 : i + 1;

                Vector2 p = polygon.get(i);
                Vector2 pp = polygon.get(pi);
                Vector2 pn = polygon.get(ni);
                if (GeometryUtils.PointOnLineSegment(pn, pp, p, 0.1f)) {
                    repeat = true;
                    iterator.remove();
                    break;
                }
            }
        } while (repeat && polygon.size() >= 3);

    }

    public static void bruteForceRectification(ArrayList<Vector2> polygon, float ceil) {
        boolean repeat;
        do {
            repeat = false;
            Iterator<Vector2> iterator = polygon.iterator();
            while (iterator.hasNext()) {
                int i = polygon.indexOf(iterator.next());
                int pi = (i == 0) ? polygon.size() - 1 : i - 1;
                int ni = (i == polygon.size() - 1) ? 0 : i + 1;

                Vector2 p = polygon.get(i);
                Vector2 pp = polygon.get(pi);
                Vector2 pn = polygon.get(ni);


                if (GeometryUtils.PointOnLineSegment(pn, pp, p, ceil)) {
                    repeat = true;
                    iterator.remove();
                    break;
                }
            }
        } while (repeat && polygon.size() >= 3);
    }


    public static float distance(float x1, float y1, float x2, float y2) {
        float x_d = x1 - x2;
        float y_d = y1 - y2;
        return (float) Math.sqrt(x_d * x_d + y_d * y_d);
    }


    public static float[] getBounds(ArrayList<BlockA> blocks, Body body, Vector2 worldCenter, Vector2 worldTangent, Vector2 worldNormal) {

        Vector2 localTangent = obtain(body.getLocalVector(worldTangent)).nor();
        Vector2 localNormal = obtain(body.getLocalVector(worldNormal)).nor();
        Vector2 localCenter = obtain(body.getLocalPoint(worldCenter));
        float infX1 = Float.MAX_VALUE;
        float supX1 = -Float.MAX_VALUE;
        float infY1 = Float.MAX_VALUE;
        float supY1 = -Float.MAX_VALUE;

        for (BlockA b : blocks) {
            for (Vector2 p : b.getBodyVertices()) {
                float dx = p.x - localCenter.x;
                float dy = p.y - localCenter.y;
                float tproj = dx * localTangent.x + dy * localTangent.y;
                float nproj = dx * localNormal.x + dy * localNormal.y;

                if (tproj < infX1) infX1 = tproj;
                if (tproj > supX1) supX1 = tproj;
                if (nproj < infY1) infY1 = nproj;
                if (nproj > supY1) supY1 = nproj;
            }
        }

        return new float[]{infX1, infY1, supX1, supY1};
    }

    public static float[] getBounds(Body body, BlockA particular, Vector2 worldCenter, Vector2 worldTangent, Vector2 worldNormal) {
        Vector2 localTangent = obtain(body.getLocalVector(worldTangent)).nor();
        Vector2 localNormal = obtain(body.getLocalVector(worldNormal)).nor();
        Vector2 localCenter = obtain(body.getLocalPoint(worldCenter));
        float infX = Float.MAX_VALUE;
        float supX = -Float.MAX_VALUE;
        float infY = Float.MAX_VALUE;
        float supY = -Float.MAX_VALUE;

        for (Vector2 p : particular.getBodyVertices()) {
            float dx = p.x - localCenter.x;
            float dy = p.y - localCenter.y;
            float tproj = dx * localTangent.x + dy * localTangent.y;
            float nproj = dx * localNormal.x + dy * localNormal.y;

            if (tproj < infX) infX = tproj;
            if (tproj > supX) supX = tproj;
            if (nproj < infY) infY = nproj;
            if (nproj > supY) supY = nproj;

        }
        return new float[]{infX, infY, supX, supY};
    }


    private static Vector2 randomlyRotatedUnitVector() {
        int rot = (int) (Math.random() * 180);
        Vector2 u = Vector2Pool.obtain(1, 0);
        GeometryUtils.rotateVectorDeg(u, rot);
        return u;

    }


    private static void divideKeys(BlockA block1, BlockA block2, HashSet<JointKey> keys) {
        for (JointKey key : keys) {
            Vector2 anchor = key.getAnchor();
            float d1 = GeometryUtils.distBetweenPointAndPolygon(anchor.x, anchor.y, block1.getVertices());
            float d2 = GeometryUtils.distBetweenPointAndPolygon(anchor.x, anchor.y, block2.getVertices());
            if (d1 < d2) {
                if (block1.isNotAborted()) block1.addKey(key);
            } else {
                if (block2.isNotAborted()) block2.addKey(key);
            }
        }
    }


    @SuppressWarnings("SameParameterValue")
    private static boolean PointOnLineSegment(Vector2 pt1, Vector2 pt2, Vector2 pt, float epsilon) {
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

    public static ElementCouple<Vector2> getSides(Vector2 p, ArrayList<Vector2> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            int ni = (i == vertices.size() - 1) ? 0 : i + 1;
            Vector2 pi = vertices.get(i);
            Vector2 pni = vertices.get(ni);
            if (PointOnLineSegment(pi, pni, p, 0.01f)) return new ElementCouple<>(pi, pni);

        }
        return null;
    }

    public static ArrayList<ArrayList<BlockA>> getDivisionGroups(ArrayList<BlockA> blocks) {
        int N = blocks.size();
        UnionFind unionFinder = new UnionFind(N);
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = i + 1; j < blocks.size(); j++) {
                if (blocks.get(i).getID() != blocks.get(j).getID())
                    if (blocks.get(i).getPolarity() == Polarity.NEUTRAL || blocks.get(j).getPolarity() == Polarity.NEUTRAL || (blocks.get(i).getPolarity() == blocks.get(j).getPolarity()))
                        if (GeometryUtils.doLayersIntersect(blocks.get(i).getVertices(), blocks.get(j).getVertices()))
                            unionFinder.union(i, j);

            }
        }
        for (BlockA b : blocks) b.setPolarity(Polarity.NEUTRAL);

        unionFinder.compute();

        Iterator<HashSet<Integer>> iterator = unionFinder.myDict.values().iterator();
        ArrayList<ArrayList<BlockA>> blockGrouping = new ArrayList<>();
        int INDEX = 0;
        while (iterator.hasNext()) {
            blockGrouping.add(new ArrayList<>());
            HashSet<Integer> set = iterator.next();
            for (Integer aSet : set) {
                blockGrouping.get(INDEX).add(blocks.get(aSet));
            }
            INDEX++;
        }
        for (int i = 0; i < blockGrouping.size(); i++) {
            ArrayList<BlockA> list = blockGrouping.get(i);
            for (int j = 0; j < list.size(); j++) list.get(j).setID(j);
        }
        return blockGrouping;
    }


    public static ArrayList<Vector2> applyClip(ArrayList<Vector2> vertices, ArrayList<Vector2> clipPath) {
        ArrayList<Vector2> current = vertices;
        for (int i = 0; i < clipPath.size(); i++) {
            int ni = (i == clipPath.size() - 1) ? 0 : i + 1;
            Vector2 p1 = clipPath.get(i);
            Vector2 p2 = clipPath.get(ni);
            Vector2 dir = p2.cpy().sub(p1).nor().mul(1000);
            Cut cut = BlockUtils.projectCutWithoutCorrection(current, p1.cpy().sub(dir), p2.cpy().add(dir));
            if (cut == null) continue;
            Pair<ArrayList<Vector2>, ArrayList<Vector2>> list = BlockUtils.splitVerticesSimple(cut, current);

            current = list.first;
        }
        if (GeometryUtils.PointInPolygon(GeometryUtils.calculateCentroid(current), clipPath))
            return current;
        else
            return null;
    }


    private static float getInnerValue(ArrayList<Vector2> vertices, Vector2 p1, Vector2 p2) {
        Vector2 dir = p2.cpy().sub(p1).nor().mul(1000);
        Cut cut = BlockUtils.projectCutWithoutCorrection(vertices, p1.cpy().sub(dir), p2.cpy().add(dir));
        if (cut != null) return cut.getValue();
        else return 0;

    }


    private static void divideAssociatedBlocks(BlockA block1, BlockA block2, ArrayList<? extends Block<?, ?>> associatedBlocks, Cut cut) {
        for (Block<?, ?> associatedBlock : associatedBlocks) {
            Cut projectedCut = BlockUtils.projectCutWithoutCorrection(associatedBlock.getVertices(), cut.getP1().cpy(), cut.getP2().cpy());


            Vector2 center = GeometryUtils.calculateCentroid(associatedBlock.getVertices());
            if (projectedCut == null) {
                ArrayList<Vector2> triplet = new ArrayList<>();
                triplet.clear();
                triplet.add(cut.getP1());
                triplet.add(cut.getP2());
                triplet.add(center);
                if (GeometryUtils.IsClockwise(triplet))
                    block2.addAssociatedBlock(associatedBlock);
                else block1.addAssociatedBlock(associatedBlock);
            } else {
                if (associatedBlock.isNotAborted())
                    associatedBlock.performCut(projectedCut);
                if (associatedBlock.getChildren().size() == 0) continue;
                Block<?, ?> child1 = associatedBlock.getChildren().get(0);
                if (child1.isNotAborted()) {
                    block2.addAssociatedBlock(child1);
                }
                if (associatedBlock.getChildren().size() == 1) continue;
                Block<?, ?> child2 = associatedBlock.getChildren().get(1);


                if (child2.isNotAborted()) {
                    block1.addAssociatedBlock(child2);
                }

            }
        }

    }


    private static void divideFreshCuts(BlockA block1, BlockA block2, ArrayList<FreshCut> freshCuts, Cut cut) {
        //divide freshcuts
        for (FreshCut freshCut : freshCuts) {
            boolean divided = false;
            if ((cut.getLower1() == freshCut.first && cut.getHigher1() == freshCut.second)) {
                if (cut.getP1() != freshCut.first && cut.getP1() != freshCut.second) {
                    FreshCut halfFreshCut1 = new FreshCut(cut.getP1(), freshCut.second);
                    FreshCut halfFreshCut2 = new FreshCut(freshCut.first, cut.getP1Brother());
                    block1.addFreshCut(halfFreshCut1);
                    block2.addFreshCut(halfFreshCut2);
                    freshCut.setAlive(false);
                    divided = true;
                }

            } else if ((cut.getLower2() == freshCut.first && cut.getHigher2() == freshCut.second)) {
                if (cut.getP2() != freshCut.first && cut.getP2() != freshCut.second) {
                    FreshCut halfFreshCut1 = new FreshCut(freshCut.first, cut.getP2());
                    FreshCut halfFreshCut2 = new FreshCut(cut.getP2Brother(), freshCut.second);

                    block1.addFreshCut(halfFreshCut1);
                    block2.addFreshCut(halfFreshCut2);

                    divided = true;
                    freshCut.setAlive(false);
                }

            }
            if (!divided) {

                if (block1.getVertices().contains(freshCut.first) && block1.getVertices().contains(freshCut.second)) {
                    block1.addFreshCut(freshCut);
                } else if (block2.getVertices().contains(freshCut.first) || block2.getVertices().contains(freshCut.second)) {
                    if (!block2.getVertices().contains(freshCut.first)) {
                        freshCut.first = (freshCut.first == cut.getP1()) ? cut.getP1Brother() : cut.getP2Brother();
                    }
                    if (!block2.getVertices().contains(freshCut.second)) {
                        freshCut.second = (freshCut.second == cut.getP2()) ? cut.getP2Brother() : cut.getP1Brother();
                    }

                    block2.addFreshCut(freshCut);
                }


            }
        }

    }

    public static Pair<BlockA, BlockA> cutBlockA(BlockA block, Cut cut) {
        Pair<Pair<ArrayList<Vector2>, ArrayList<Vector2>>, Pair<FreshCut, FreshCut>> splitResult = BlockUtils.splitVertices(cut, block.getVertices());
        Pair<ArrayList<Vector2>, ArrayList<Vector2>> group = splitResult.first;
        Pair<FreshCut, FreshCut> limits = splitResult.second;


        BlockA block1 = BlockFactory.createBlockA(group.first, block.getProperties().getCopy(), block.getID(), false, block.getOrder());
        block1.addFreshCut(limits.first);
        block1.setPolarity(Polarity.YIN);
        BlockA block2 = BlockFactory.createBlockA(group.second, block.getProperties().getCopy(), block.getID(), false, block.getOrder());
        block2.addFreshCut(limits.second);
        block2.setPolarity(Polarity.YANG);
        int liquidQuantity = block.getLiquidQuantity();
        block1.setLiquidQuantity((int) (liquidQuantity * block1.getArea() / block.getArea()));
        block2.setLiquidQuantity((int) (liquidQuantity * block2.getArea() / block.getArea()));


        BlockUtils.divideKeys(block1, block2, block.getKeys());
        BlockUtils.divideAssociatedBlocks(block1, block2, block.getAssociatedBlocks(), cut);
        block1.refillGrid();
        block2.refillGrid();

        BlockUtils.divideFreshCuts(block1, block2, block.getFreshCuts(), cut);

        return new Pair<>(block1, block2);

    }

    public static void setBodyVertices(BlockA block) {
        ArrayList<Vector2> rVertices = BlockUtils.bodyVertices(block);
        block.setBodyVertices(rVertices);
    }


    public static boolean isValid(ArrayList<Vector2> points) {
      return (points.size()>= 3);
    }
    public static ShatterData shatterData(BlockA block, Vector2 localImpactPoint) {
        if (block.getBlockGrid() == null) return null;
        CoatingBlock coatingCenter = block.getBlockGrid().getNearestCoatingBlockSimple(localImpactPoint);
        //RANDOM ROTATION
        if (coatingCenter == null) return null;

        Vector2 u = BlockUtils.randomlyRotatedUnitVector();
        Vector2 Center = coatingCenter.getPosition();

        Vector2 extremity1;
        Vector2 extremity2;
        ArrayList<Cut> candidates = new ArrayList<>();
        int n = 16;
        for (int i = 0; i < n; i++) {
            GeometryUtils.rotateVectorDeg(u, 180f / n);

            extremity1 = Vector2Pool.obtain(Center.x + u.x * 200, Center.y + u.y * 200);
            extremity2 = Vector2Pool.obtain(Center.x - u.x * 200, Center.y - u.y * 200);

            Cut cut = BlockUtils.generateCutCorrected(block.getVertices(), Center, extremity1, extremity2);
            if (cut != null && cut.isValid(block.getVertices())) candidates.add(cut);
        }
        Collections.sort(candidates);
        if (candidates.size() == 0) return new ShatterData();

        Cut chosen = candidates.get(0);
        float destructionEnergy = 0;
        for (CoatingBlock coatingBlock : block.getBlockGrid().getCoatingBlocks()) {
            float ratio = (float) (1 - coatingBlock.getProperties().getBurnRatio() / 1.1f);
            float value = getInnerValue(coatingBlock.getVertices(), chosen.getP1(), chosen.getP2());
            destructionEnergy += PhysicsConstants.TENACITY_FACTOR * block.getProperties().getTenacity() * ratio * value * value;
        }
        return new ShatterData(chosen, destructionEnergy);
    }

    public static ArrayList<Vector2> bodyVertices(BlockA block) {
        ArrayList<Vector2> bodyVertices = new ArrayList<>();
        for (Vector2 v : block.getVertices()) {
            Vector2 bv = Vector2Pool.obtain(v.x / 32f, v.y / 32f);
            bodyVertices.add(bv);
        }
        return bodyVertices;
    }


    public static void computeCoatingBlocks(BlockA mainBlock) {

        double initialTemperature = PhysicsConstants.ambient_temperature;
        double initialChemicalEnergy = mainBlock.getProperties().getChemicalEnergy();
        CoatingBlock root = new CoatingBlock();
        ArrayList<Vector2> mainBlockVerticesCopy = new ArrayList<>();
        for (int i = 0; i < mainBlock.getVertices().size(); i++)
            mainBlockVerticesCopy.add(Vector2Pool.obtain(mainBlock.getVertices().get(i)));

        root.initialization(mainBlockVerticesCopy, new CoatingProperties(mainBlock.getProperties(), 0, 0, initialTemperature, 0, initialChemicalEnergy), 0, true);

        ArrayList<Vector2> vertices = root.getVertices();
        Vector2 center = GeometryUtils.calculateCentroid(vertices);
        for (Vector2 v : vertices) {
            Vector2 u = Vector2Pool.obtain(v.x - center.x, v.y - center.y).nor().mul(0.3f);
            v.add(u);
        }


        Utils.translatePoints(vertices, center);


        Polygon polygon = new Polygon(root.getVertices());
        com.evolgames.caliper.Rectangle rectangle = Caliper.minimumBox(polygon);

        rectangle.getPoint(0).add(center);
        rectangle.getPoint(1).add(center);
        rectangle.getPoint(2).add(center);
        rectangle.getPoint(3).add(center);

        Utils.translatePoints(vertices, -center.x, -center.y);

        Vector2 P0 = rectangle.getPoint(0);
        Vector2 PX = rectangle.getPoint(1);
        Vector2 PY = rectangle.getPoint(3);


        Vector2 X = Vector2Pool.obtain(PX).sub(P0);
        Vector2 Y = Vector2Pool.obtain(PY).sub(P0);
        Vector2 uX = Vector2Pool.obtain(X).nor();
        Vector2 uY = Vector2Pool.obtain(Y).nor();

        Vector2 EXBegin = Vector2Pool.obtain(P0).sub(uY.x * 5, uY.y * 5);
        Vector2 EXEnd = Vector2Pool.obtain(P0).add(Y).add(uY.x * 5, uY.y * 5);
        float xlen = X.len();

        float ylen = Y.len();
        float len = Math.max(xlen, ylen);
        int n = (int) (Math.floor(len / 32) + 1);
        final float step = 16;
        int xCount = (int) Math.floor(xlen / step);
        int yCount = (int) Math.floor(ylen / step);
        final Vector2 stepX = Vector2Pool.obtain(uX).mul(xlen / (xCount + 1));
        final Vector2 stepY = Vector2Pool.obtain(uY).mul(ylen / (yCount + 1));
        CoatingBlock currentx = root;
        Vector2 EYBegin = obtain();
        Vector2 EYEnd = obtain();
        root.setStep(step);
        for (int i = 0; i < xCount + 1; i++) {
            if (i < xCount) {
                EXBegin.add(stepX);
                EXEnd.add(stepX);
                Cut cut = findCut(currentx.getVertices(), EXBegin, EXEnd);
                if (cut == null) continue;

                currentx.performCut(cut);
                currentx.setAborted(true);
            }


            CoatingBlock currenty = (i < xCount) ? (currentx.getChildren().get(1).isNotAborted() ? currentx.getChildren().get(1) : currentx.getChildren().get(0)) : currentx;

            EYBegin.set(P0.x - uX.x * 5, P0.y - uX.y * 5);
            EYEnd.set(P0.x + X.x + uX.x * 5, P0.y + X.y + uX.y * 5);


            for (int j = 0; j < yCount; j++) {
                EYBegin.add(stepY);
                EYEnd.add(stepY);
                Cut cuty = findCut(currenty.getVertices(), EYBegin, EYEnd);
                if (cuty == null) continue;

                currenty.performCut(cuty);
                currenty.setAborted(true);

                CoatingBlock child1 = currenty.getChildren().get(0);
                CoatingBlock child2 = currenty.getChildren().get(1);
                child1.setNx(i);
                child1.setNy(j);
                child2.setNx(i);
                child2.setNy(j + 1);

                currenty = (child2.isNotAborted()) ? child2 : child1;
            }


            if (currentx.getChildren().size() > 0)
                currentx = currentx.getChildren().get(0);


        }
        Vector2Pool.obtain(P0);
        Vector2Pool.obtain(PX);
        Vector2Pool.obtain(PY);
        Vector2Pool.obtain(X);
        Vector2Pool.obtain(Y);
        Vector2Pool.obtain(uX);
        Vector2Pool.obtain(uY);
        Vector2Pool.obtain(EXBegin);
        Vector2Pool.obtain(EXEnd);
        Vector2Pool.obtain(EYBegin);
        Vector2Pool.obtain(EYEnd);
        Vector2Pool.obtain(stepX);
        Vector2Pool.obtain(stepY);

        Iterator<CoatingBlock> iterator = root.createIterator();
        boolean error = false;
        while (iterator.hasNext()) {
            CoatingBlock bl = iterator.next();
            if (bl.isNotAborted()) {
                mainBlock.addAssociatedBlock(bl);
                if (bl.getArea() > (step * step) + 1) {
                    error = true;


                }
                mainBlock.getBlockGrid().addCoatingBlock(bl);
                bl.centerCoreCoatingBlock();
            }
        }
        if (error)
            Log.e("Error", "error coating:" + Arrays.toString(mainBlock.getVertices().toArray()));


    }

    public static Color[] computeColors(ArrayList<BlockA> blocks) {
        int colorNumber = 0;
        for (BlockA blockA : blocks) {
            colorNumber++;
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlockConcrete) {
                    colorNumber++;
                }
            }
        }
        Color[] data = new Color[colorNumber];
        colorNumber = 0;
        for (BlockA blockA : blocks) {
            data[colorNumber++] = blockA.getProperties().getDefaultColor();
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlockConcrete) {
                    if (b instanceof CoatingBlock)
                        b.getProperties().setDefaultColor(new Color(Color.TRANSPARENT));

                    data[colorNumber++] = b.getProperties().getDefaultColor();
                }
            }
        }
        return data;
    }

    public static int[] computeVertexCount(ArrayList<BlockA> blocks) {
        int groupNumber = 0;
        for (BlockA blockA : blocks) {
            groupNumber++;
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlockConcrete) {
                    groupNumber++;
                }
            }
        }
        int[] data = new int[groupNumber];
        groupNumber = 0;
        for (BlockA blockA : blocks) {
            data[groupNumber++] = blockA.getTriangles().size();
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlockConcrete)
                    data[groupNumber++] = b.getTriangles().size();
            }
        }
        return data;
    }

    public static float[] computeData(ArrayList<BlockA> blocks) {
        int vertexNumber = 0;
        for (BlockA blockA : blocks) {
            vertexNumber += blockA.getTriangles().size();
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlockConcrete) {
                    vertexNumber += b.getTriangles().size();
                }
            }
        }
        float[] data = new float[vertexNumber * 3];
        vertexNumber = 0;
        for (BlockA blockA : blocks) {
            for (Vector2 p : blockA.getTriangles()) {
                data[3 * vertexNumber] = p.x;
                data[3 * vertexNumber + 1] = p.y;
                data[3 * vertexNumber + 2] = 0;
                vertexNumber++;
            }
            for (Block<?, ?> b : blockA.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                ArrayList<Vector2> vertices;
                if (b instanceof CoatingBlock) {
                    vertices = b.getTriangles();
                } else if (b instanceof DecorationBlockConcrete) {
                    vertices = b.getTriangles();
                } else continue;
                for (Vector2 p : vertices) {
                    data[3 * vertexNumber] = p.x;
                    data[3 * vertexNumber + 1] = p.y;
                    data[3 * vertexNumber + 2] = 0;
                    vertexNumber++;
                }
            }
        }
        return data;
    }
}
