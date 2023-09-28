package com.evolgames.helpers.utilities;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.caliper.Caliper;
import com.evolgames.entities.caliper.Polygon;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.AssociatedBlockComparator;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.DecorationBlock;
import com.evolgames.entities.blocks.Polarity;
import com.evolgames.entities.caliper.Rectangle;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.PointsFreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.cut.ShatterData;
import com.evolgames.entities.properties.ColoredProperties;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.helpers.CutFlag;
import com.evolgames.helpers.ElementCouple;
import com.evolgames.helpers.UnionFind;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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


    private static Vector2 findTheOne(ArrayList<Vector2> Vertices, Vector2 A, Vector2 B, Vector2 centroid) {
        if (GeometryUtils.PointOnLineSegment(A, B, centroid, 0.01f)) return centroid;
        for (Vector2 v : Vertices) {
            if (v.dst(A) < 0.001f || v.dst(B) < 0.001f) continue;
            Vector2 p = GeometryUtils.lineIntersectPoint(v, centroid, A, B);
            if (p != null && p.dst(v) > 0.01f) return p;
        }
        return null;
    }


    private static Cut projectCutWithoutCorrection(ArrayList<Vector2> vertices, Vector2 first, Vector2 second) {
        if (vertices.size() < 3) {
            return null;
        }
        Vector2 centroid = GeometryUtils.calculateCentroid(vertices);
        Vector2 u = Vector2Pool.obtain(second).sub(first).nor().mul(1000);
        Vector2 center = findTheOne(vertices, first, second, centroid);

        if (center == null) return null;
        Vector2 e1 = Vector2Pool.obtain(center).add(u);
        Vector2 e2 = Vector2Pool.obtain(center).sub(u);

        Cut cut = generateCut(vertices, center, e1, e2);
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
        if (pair1 == null || pair2 == null) {
            return null;
        }
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


    public static float[] getBounds(GameEntity gameEntity, Body body, Vector2 worldCenter, Vector2 worldTangent, Vector2 worldNormal) {

        Vector2 localTangent = obtain(body.getLocalVector(worldTangent)).nor();
        Vector2 localNormal = obtain(body.getLocalVector(worldNormal)).nor();
        Vector2 localCenter = obtain(body.getLocalPoint(worldCenter));
        float infX1 = Float.MAX_VALUE;
        float supX1 = -Float.MAX_VALUE;
        float infY1 = Float.MAX_VALUE;
        float supY1 = -Float.MAX_VALUE;

        for (LayerBlock b : gameEntity.getBlocks()) {
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


    private static Vector2 randomlyRotatedUnitVector() {
        int rot = (int) (Math.random() * 180);
        Vector2 u = Vector2Pool.obtain(1, 0);
        GeometryUtils.rotateVectorDeg(u, rot);
        return u;

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

    public static ArrayList<ArrayList<LayerBlock>> getDivisionGroups(List<LayerBlock> blocks) {
        int blocksSize = blocks.size();
        UnionFind unionFinder = new UnionFind(blocksSize);
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = i + 1; j < blocks.size(); j++) {
                if (blocks.get(i).getId() != blocks.get(j).getId()) {
                    if (blocks.get(i).getPolarity() == Polarity.NEUTRAL || blocks.get(j).getPolarity() == Polarity.NEUTRAL || (blocks.get(i).getPolarity() == blocks.get(j).getPolarity())) {
                        if (GeometryUtils.doLayersIntersect(blocks.get(i).getVertices(), blocks.get(j).getVertices())) {
                            unionFinder.union(i, j);
                        }
                    }
                }
            }
        }

        unionFinder.compute();

        Iterator<HashSet<Integer>> iterator = unionFinder.myDict.values().iterator();
        ArrayList<ArrayList<LayerBlock>> blockGrouping = new ArrayList<>();
        int index = 0;
        while (iterator.hasNext()) {
            blockGrouping.add(new ArrayList<>());
            HashSet<Integer> set = iterator.next();
            for (Integer aSet : set) {
                blockGrouping.get(index).add(blocks.get(aSet));
            }
            index++;
        }
        for (int i = 0; i < blockGrouping.size(); i++) {
            ArrayList<LayerBlock> list = blockGrouping.get(i);
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setId(j);
                list.get(j).setPolarity(Polarity.NEUTRAL);
            }
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
            if (cut == null) {
                continue;
            }
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
        if (cut != null) {
            return cut.getLength();
        } else return 0;

    }


    private static void divideAssociatedBlocks(LayerBlock block1, LayerBlock block2, ArrayList<? extends AssociatedBlock<?, ?>> associatedBlocks, Cut cut) {
        for (AssociatedBlock<?, ?> associatedBlock : associatedBlocks) {
            Cut projectedCut = BlockUtils.projectCutWithoutCorrection(associatedBlock.getVertices(), cut.getP1().cpy(), cut.getP2().cpy());
            Vector2 center = GeometryUtils.calculateCentroid(associatedBlock.getVertices());
            if (projectedCut == null) {
                ArrayList<Vector2> triplet = new ArrayList<>();
                triplet.add(cut.getP1());
                triplet.add(cut.getP2());
                triplet.add(center);
                if (GeometryUtils.IsClockwise(triplet)) {
                    block2.addAssociatedBlock(associatedBlock);
                } else {
                    block1.addAssociatedBlock(associatedBlock);
                }
            } else {
                if (associatedBlock.isNotAborted()) {
                    associatedBlock.performCut(projectedCut);
                }
                if (associatedBlock.getChildren().size() == 0) {
                    continue;
                }
                AssociatedBlock<?, ?> child1 = associatedBlock.getChildren().get(0);
                if (child1.isNotAborted()) {
                    block2.addAssociatedBlock(child1);
                }
                if (associatedBlock.getChildren().size() == 1) {
                    continue;
                }
                AssociatedBlock<?, ?> child2 = associatedBlock.getChildren().get(1);


                if (child2.isNotAborted()) {
                    block1.addAssociatedBlock(child2);
                }

            }
        }

    }


    private static void divideFreshCuts(LayerBlock block1, LayerBlock block2, ArrayList<FreshCut> freshCuts, Cut cut) {
        for (FreshCut fc : freshCuts) {
            if (fc instanceof SegmentFreshCut) {
                SegmentFreshCut sfc = (SegmentFreshCut) fc;
                if(!sfc.isInner()) {
                    boolean divided = false;
                    if ((cut.getLower1() == sfc.first && cut.getHigher1() == sfc.second)) {
                        if (cut.getP1() != sfc.first && cut.getP1() != sfc.second) {
                            float ratio = cut.getP1().dst(sfc.second)/sfc.getLength();
                            SegmentFreshCut halfFreshCut1 = new SegmentFreshCut(cut.getP1(), sfc.second,(int) (sfc.getLimit()*ratio), false);
                            SegmentFreshCut halfFreshCut2 = new SegmentFreshCut(sfc.first, cut.getP1Brother(),(int) (sfc.getLimit()*(1f-ratio)), false);
                            block1.addFreshCut(halfFreshCut1);
                            block2.addFreshCut(halfFreshCut2);
                            divided = true;
                        }
                    } else if ((cut.getLower2() == sfc.first && cut.getHigher2() == sfc.second)) {
                        if (cut.getP2() != sfc.first && cut.getP2() != sfc.second) {
                            float ratio = cut.getP2().dst(sfc.first)/sfc.getLength();
                            SegmentFreshCut halfFreshCut1 = new SegmentFreshCut(sfc.first, cut.getP2(),(int) (sfc.getLimit()*ratio),false);
                            SegmentFreshCut halfFreshCut2 = new SegmentFreshCut(cut.getP2Brother(), sfc.second,(int) (sfc.getLimit()*(1f-ratio)), false);
                            block1.addFreshCut(halfFreshCut1);
                            block2.addFreshCut(halfFreshCut2);
                            divided = true;
                        }
                    }
                    if (!divided) {

                        if (block1.getVertices().contains(sfc.first) && block1.getVertices().contains(sfc.second)) {
                            block1.addFreshCut(sfc);
                        } else if (block2.getVertices().contains(sfc.first) || block2.getVertices().contains(sfc.second)) {
                            if (!block2.getVertices().contains(sfc.first)) {
                                sfc.first = (sfc.first == cut.getP1()) ? cut.getP1Brother() : cut.getP2Brother();
                            }
                            if (!block2.getVertices().contains(sfc.second)) {
                                sfc.second = (sfc.second == cut.getP2()) ? cut.getP2Brother() : cut.getP1Brother();
                            }
                            block2.addFreshCut(sfc);
                        }

                    }
                } else {
                    Vector2 inter = GeometryUtils.lineIntersectPoint(sfc.first,sfc.second,cut.getP1(),cut.getP2());
                    if(inter==null){
                        if(GeometryUtils.PointInPolygon(sfc.first,block1.getVertices())&&GeometryUtils.PointInPolygon(sfc.second,block1.getVertices())){
                           block1.addFreshCut(sfc);
                        } else {
                            block2.addFreshCut(sfc);
                        }
                    } else {
                        Vector2 u = sfc.first.cpy().sub(sfc.second).nor();
                        float dst1 = inter.dst(sfc.first);
                        if(dst1 >4f){
                            float ratio = dst1/sfc.getLength();
                            SegmentFreshCut child1 = new SegmentFreshCut(sfc.first.cpy().sub(u),inter.cpy(),(int) (ratio*sfc.getLimit()), true);
                            if(GeometryUtils.PointInPolygon(child1.first,block1.getVertices())){
                                block1.addFreshCut(child1);
                            } else {
                                block2.addFreshCut(child1);
                            }
                        }
                        float dst2 = inter.dst(sfc.second);
                        if(dst2 >4f){
                            float ratio = dst2/sfc.getLength();
                            SegmentFreshCut child2 = new SegmentFreshCut(inter,sfc.second.cpy().add(u),(int) (ratio*sfc.getLimit()), true);
                            if(GeometryUtils.PointInPolygon(child2.second,block1.getVertices())){
                                block1.addFreshCut(child2);
                            } else {
                                block2.addFreshCut(child2);
                            }
                        }

                    }
                }
            } else if (fc instanceof PointsFreshCut) {
                PointsFreshCut fpc = (PointsFreshCut) fc;
                List<Vector2> newPoints1 = new ArrayList<>();
                List<Vector2> newPoints2 = new ArrayList<>();
                for (Vector2 v : fpc.getPoints()) {
                    ArrayList<Vector2> triplet = new ArrayList<>();
                    triplet.add(cut.getP1());
                    triplet.add(cut.getP2());
                    triplet.add(v);
                    if (GeometryUtils.IsClockwise(triplet)) {
                        newPoints2.add(v);
                    } else {
                        newPoints1.add(v);
                    }
                }
                float ratio = (float)newPoints1.size() / (float)fpc.getPoints().size();
                if (!newPoints1.isEmpty()) {
                    PointsFreshCut child1 = new PointsFreshCut(newPoints1, fpc.getLength(),(int) (ratio*fpc.getLimit()), fpc.getSplashVelocity());
                    block1.addFreshCut(child1);
                }
                if (!newPoints2.isEmpty()) {
                    PointsFreshCut child2 = new PointsFreshCut(newPoints2, fpc.getLength(),(int) ((1-ratio)*fpc.getLimit()), fpc.getSplashVelocity());
                    block2.addFreshCut(child2);
                }
            }
        }

    }
    public static LayerBlock getNearestBlock(Vector2 point, List<LayerBlock> blocks) {
        float minDistance = Float.MAX_VALUE;
        LayerBlock result = null;
        for (LayerBlock layerBlock : blocks) {
            CoatingBlock nearest = layerBlock.getBlockGrid().getNearestCoatingBlockSimple(point);
            if(nearest==null){
                continue;
            }
            float distance = nearest.distance(point);
            if (distance < minDistance) {
                result = layerBlock;
                minDistance = distance;
            }
        }
        return result;
    }
    public static Pair<LayerBlock, LayerBlock> cutLayerBlock(LayerBlock block, Cut cut) {


        Pair<ArrayList<Vector2>, ArrayList<Vector2>> result = splitVerticesSimple(cut, block.getVertices());
        float density = block.getProperties().getJuicinessDensity();
        SegmentFreshCut pair1 = new SegmentFreshCut(cut.getP2(), cut.getP1(), false,density);
        SegmentFreshCut pair2 = new SegmentFreshCut(cut.getP1Brother(), cut.getP2Brother(), false,density);


        LayerBlock block1 = BlockFactory.createLayerBlock(result.first, block.getProperties().copy(), block.getId(), block.getOrder(), false);
       if(pair1.getLength()>6f)
        block1.addFreshCut(pair1);
        block1.setPolarity(Polarity.YIN);
        LayerBlock block2 = BlockFactory.createLayerBlock(result.second, block.getProperties().copy(), block.getId(), block.getOrder(), false);
       if(pair2.getLength()>6f)
        block2.addFreshCut(pair2);
        block2.setPolarity(Polarity.YANG);
        int liquidQuantity = block.getLiquidQuantity();
        block1.setLiquidQuantity((int) (liquidQuantity * block1.getBlockArea() / block.getBlockArea()));
        block2.setLiquidQuantity((int) (liquidQuantity * block2.getBlockArea() / block.getBlockArea()));


        BlockUtils.divideAssociatedBlocks(block1, block2, block.getAssociatedBlocks(), cut);
        block1.refillGrid();
        block2.refillGrid();

        BlockUtils.divideFreshCuts(block1, block2, block.getFreshCuts(), cut);

        return new Pair<>(block1, block2);

    }

    public static void setBodyVertices(LayerBlock block) {
        ArrayList<Vector2> rVertices = BlockUtils.bodyVertices(block);
        block.setBodyVertices(rVertices);
    }


    public static boolean isValid(ArrayList<Vector2> points) {
        return (points.size() >= 3);
    }

    public static ShatterData applyCut(LayerBlock block, Vector2 localImpactPoint) {
        if (block.getBlockGrid() == null) {
            return null;
        }
        CoatingBlock coatingCenter = block.getBlockGrid().getNearestCoatingBlockSimple(localImpactPoint);
        if (coatingCenter == null) {
            return null;
        }

        Vector2 u = BlockUtils.randomlyRotatedUnitVector();
        Vector2 center = coatingCenter.getPosition();

        Vector2 extremity1;
        Vector2 extremity2;
        ArrayList<Cut> candidates = new ArrayList<>();
        int n = 16;
        for (int i = 0; i < n; i++) {
            GeometryUtils.rotateVectorDeg(u, 180f / n);

            extremity1 = Vector2Pool.obtain(center.x + u.x * 200, center.y + u.y * 200);
            extremity2 = Vector2Pool.obtain(center.x - u.x * 200, center.y - u.y * 200);

            Cut cut = BlockUtils.generateCutCorrected(block.getVertices(), center, extremity1, extremity2);
            if (cut != null && cut.isValid(block.getVertices())) candidates.add(cut);
        }
        if (candidates.size() == 0) {
            return new ShatterData();
        }
        Collections.sort(candidates);
        Cut chosen = candidates.get(0);
        float destructionEnergy = 0;
        for (CoatingBlock coatingBlock : block.getBlockGrid().getCoatingBlocks()) {
            float ratio = (float) (1 - coatingBlock.getProperties().getBurnRatio() / 1.1f);
            float value = getInnerValue(coatingBlock.getVertices(), chosen.getP1(), chosen.getP2());
            destructionEnergy += PhysicsConstants.TENACITY_FACTOR * block.getProperties().getTenacity() * ratio * value * value;
        }
        return new ShatterData(chosen, destructionEnergy);
    }

    public static ArrayList<Vector2> bodyVertices(LayerBlock block) {
        ArrayList<Vector2> bodyVertices = new ArrayList<>();
        for (Vector2 v : block.getVertices()) {
            Vector2 bv = Vector2Pool.obtain(v.x / 32f, v.y / 32f);
            bodyVertices.add(bv);
        }
        return bodyVertices;
    }


    public static void computeCoatingBlocks(LayerBlock mainBlock) {

        double initialTemperature = PhysicsConstants.ambient_temperature;
        double initialChemicalEnergy = mainBlock.getProperties().getChemicalEnergy();
        CoatingBlock root = new CoatingBlock();
        ArrayList<Vector2> mainBlockVerticesCopy = new ArrayList<>();
        for (int i = 0; i < mainBlock.getVertices().size(); i++) {
            mainBlockVerticesCopy.add(Vector2Pool.obtain(mainBlock.getVertices().get(i)));
        }

        root.initialization(mainBlockVerticesCopy, new CoatingProperties(0, 0, initialTemperature, 0, initialChemicalEnergy, mainBlock.getProperties()), 0);

        ArrayList<Vector2> vertices = root.getVertices();
        Vector2 center = GeometryUtils.calculateCentroid(vertices);
        for (Vector2 v : vertices) {
            Vector2 u = Vector2Pool.obtain(v.x - center.x, v.y - center.y).nor().mul(0.3f);
            v.add(u);
        }


        Utils.translatePoints(vertices, center);


        Polygon polygon = new Polygon(root.getVertices());
        if(polygon.edgeCount()==0){
            return;
        }
        Rectangle rectangle = Caliper.minimumBox(polygon);

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
        float xLen = X.len();

        float yLen = Y.len();
        final float step = PhysicsConstants.GRAIN_SPACING;
        int xCount = (int) Math.floor(xLen / step);
        int yCount = (int) Math.floor(yLen / step);
        final Vector2 stepX = Vector2Pool.obtain(uX).mul(xLen / (xCount + 1));
        final Vector2 stepY = Vector2Pool.obtain(uY).mul(yLen / (yCount + 1));
        CoatingBlock currentTx = root;
        Vector2 EYBegin = obtain();
        Vector2 EYEnd = obtain();
        root.setStep(step);
        for (int i = 0; i < xCount + 1; i++) {
            if (i < xCount) {
                EXBegin.add(stepX);
                EXEnd.add(stepX);
                Cut cut = findCut(currentTx.getVertices(), EXBegin, EXEnd);
                if (cut == null) {
                    continue;
                }

                currentTx.performCut(cut);
                currentTx.setAborted(true);
            }


            CoatingBlock currenty = (i < xCount) ? (currentTx.getChildren().get(1).isNotAborted() ? currentTx.getChildren().get(1) : currentTx.getChildren().get(0)) : currentTx;

            EYBegin.set(P0.x - uX.x * 5, P0.y - uX.y * 5);
            EYEnd.set(P0.x + X.x + uX.x * 5, P0.y + X.y + uX.y * 5);


            for (int j = 0; j < yCount; j++) {
                EYBegin.add(stepY);
                EYEnd.add(stepY);
                Cut cutY = findCut(currenty.getVertices(), EYBegin, EYEnd);
                if (cutY == null) {
                    continue;
                }
                currenty.performCut(cutY);
                currenty.setAborted(true);

                CoatingBlock child1 = currenty.getChildren().get(0);
                CoatingBlock child2 = currenty.getChildren().get(1);
                child1.setNx(i);
                child1.setNy(j);
                child2.setNx(i);
                child2.setNy(j + 1);

                currenty = (child2.isNotAborted()) ? child2 : child1;
            }


            if (currentTx.getChildren().size() > 0)
                currentTx = currentTx.getChildren().get(0);


        }
        Vector2Pool.recycle(P0);
        Vector2Pool.recycle(PX);
        Vector2Pool.recycle(PY);
        Vector2Pool.recycle(X);
        Vector2Pool.recycle(Y);
        Vector2Pool.recycle(uX);
        Vector2Pool.recycle(uY);
        Vector2Pool.recycle(EXBegin);
        Vector2Pool.recycle(EXEnd);
        Vector2Pool.recycle(EYBegin);
        Vector2Pool.recycle(EYEnd);
        Vector2Pool.recycle(stepX);
        Vector2Pool.recycle(stepY);

        Iterator<CoatingBlock> iterator = root.createIterator();
        boolean error = false;
        int blockId = 0;
        while (iterator.hasNext()) {
            CoatingBlock coatingBlock = iterator.next();
            if (coatingBlock.isNotAborted()) {
                mainBlock.addAssociatedBlock(coatingBlock);
                if (coatingBlock.getArea() > (step * step) + 1) {
                    error = true;
                }
                mainBlock.getBlockGrid().addCoatingBlock(coatingBlock);
                coatingBlock.centerCoreCoatingBlock();
                coatingBlock.setId(blockId++);
            }
        }
        if (error) {
            Log.e("Error", "error coating:" + Arrays.toString(mainBlock.getVertices().toArray()));
        }


    }

    public static Color[] computeColors(ArrayList<LayerBlock> blocks) {
        int colorNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            colorNumber++;
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlock) {
                    colorNumber++;
                }
            }
        }
        Color[] data = new Color[colorNumber];
        colorNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            data[colorNumber++] = layerBlock.getProperties().getDefaultColor();
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) {
                    continue;
                }
                if (b instanceof CoatingBlock || b instanceof DecorationBlock) {
                    if (b instanceof CoatingBlock) {
                        ((CoatingProperties) b.getProperties()).setDefaultColor(new Color(Color.TRANSPARENT));
                    }

                    data[colorNumber++] = ((ColoredProperties) b.getProperties()).getDefaultColor();
                }
            }
        }
        return data;
    }

    public static int[] computeVertexCount(ArrayList<LayerBlock> blocks) {
        int groupNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            groupNumber++;
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlock) {
                    groupNumber++;
                }
            }
        }
        int[] data = new int[groupNumber];
        groupNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            data[groupNumber++] = layerBlock.getTriangles().size();
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                if (b instanceof CoatingBlock || b instanceof DecorationBlock)
                    data[groupNumber++] = b.getTriangles().size();
            }
        }
        return data;
    }

    public static float[] computeData(ArrayList<LayerBlock> blocks) {
        int vertexNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            vertexNumber += layerBlock.getTriangles().size();
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) {
                    continue;
                }
                if (b instanceof CoatingBlock || b instanceof DecorationBlock) {
                    vertexNumber += b.getTriangles().size();
                }
            }
        }
        float[] data = new float[vertexNumber * 3];
        vertexNumber = 0;
        for (LayerBlock layerBlock : blocks) {
            for (Vector2 p : layerBlock.getTriangles()) {
                data[3 * vertexNumber] = p.x;
                data[3 * vertexNumber + 1] = p.y;
                data[3 * vertexNumber + 2] = 0;
                vertexNumber++;
            }
            for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                if (!b.isNotAborted()) continue;
                ArrayList<Vector2> vertices;
                if (b instanceof CoatingBlock) {
                    vertices = b.getTriangles();
                } else if (b instanceof DecorationBlock) {
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

    public static ArrayList<LayerBlock> createBlocks(BodyModel bodyModel) {
        List<List<Vector2>> list = new ArrayList<>();
        for (LayerModel layerModel : bodyModel.getLayers()) {
            list.add(layerModel.getPoints());
        }
        Vector2 center = GeometryUtils.calculateCenter(list);
        return createBlocks(bodyModel, center);
    }

    public static ArrayList<LayerBlock> createBlocks(BodyModel bodyModel, Vector2 center) {
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        for (LayerModel layerModel : bodyModel.getLayers()) {
            Vector2[] layerPointsArray = layerModel.getOutlinePoints();
            if (layerPointsArray == null || layerPointsArray.length < 3) {
                continue;
            }

            LayerProperties layerProperty = layerModel.getProperties();

            ArrayList<Vector2> list = Utils.translatedPoints(layerPointsArray, center);
            LayerBlock block = BlockFactory.createLayerBlock(list, layerProperty.copy(), layerModel.getLayerId(), bodyModel.getLayers().indexOf(layerModel));
            blocks.add(block);

            for (DecorationModel decorationModel : layerModel.getDecorations()) {
                DecorationBlock decorationBlock = new DecorationBlock();
                if (decorationModel.getOutlinePoints() != null) {
                    decorationBlock.initialization(Utils.translatedPoints(decorationModel.getOutlinePoints(), center), decorationModel.getProperties(), decorationModel.getDecorationId());
                    block.addAssociatedBlock(decorationBlock);
                }
            }
        }


        return blocks;
    }
}
