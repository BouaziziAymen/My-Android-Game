package com.evolgames.circuits;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.scenes.GameScene;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PolygonMerger {
    HashSet<Vector2> allNodes = new HashSet<>();

    private Vector2 getNode(Vector2 v) {
        for (Vector2 n : allNodes) {
            if (n.dst(v) < 0.00001f) return n;
        }
        allNodes.add(v);
        return v;
    }

    public ArrayList<Vector2> merge(List<Vector2> vertices1, List<Vector2> vertices2) {
        Set<Pair<Vector2, Vector2>> edges = new HashSet<>();
        allNodes.clear();
        for (int i = 0; i < vertices1.size(); i++) {
            int ni = (i == vertices1.size() - 1) ? 0 : i + 1;
            Vector2 p1 = vertices1.get(i);
            Vector2 p2 = vertices1.get(ni);
            List<Vector2> intersections = GeometryUtils.lineIntersections(p1, p2, vertices2);
            Collections.sort(intersections, (e1, e2) -> {
                float d1 = e1.dst(p1);
                float d2 = e2.dst(p1);
                if (d1 == d2) return 0;
                return d1 < d2 ? -1 : 1;
            });
            intersections.add(0, p1);
            intersections.add(p2);
            boolean p1Inside = Utils.PointInPolygon(p1, vertices2);
            for (int k = 0; k < intersections.size() - 1; k++) {
                int nk = (k == intersections.size() - 1) ? 0 : k + 1;
                if ((p1Inside && k % 2 == 1) || (!p1Inside && k % 2 == 0)) {
                    Pair<Vector2, Vector2> edge = new Pair<>(getNode(intersections.get(k)), getNode(intersections.get(nk)));
                    edges.add(edge);
                }
            }
        }


        for (int i = 0; i < vertices2.size(); i++) {
            int ni = (i == vertices2.size() - 1) ? 0 : i + 1;
            Vector2 p1 = vertices2.get(i);
            Vector2 p2 = vertices2.get(ni);
            List<Vector2> intersections = GeometryUtils.lineIntersections(p1, p2, vertices1);

            intersections.sort((e1, e2) -> {
                float d1 = e1.dst(p1);
                float d2 = e2.dst(p1);
                if (d1 == d2) return 0;
                return d1 < d2 ? -1 : 1;
            });
            intersections.add(0, p1);
            intersections.add(p2);
            boolean p1Inside = Utils.PointInPolygon(p1, vertices1);
            for (int k = 0; k < intersections.size() - 1; k++) {
                int nk = (k == intersections.size() - 1) ? 0 : k + 1;
                if ((p1Inside && k % 2 == 1) || (!p1Inside && k % 2 == 0)) {
                    Pair<Vector2, Vector2> edge = new Pair<>(getNode(intersections.get(k)), getNode(intersections.get(nk)));
                    edges.add(edge);
                }
            }
        }

        for (int i = 0; i < vertices1.size(); i++) {
            int ni = (i == vertices1.size() - 1) ? 0 : i + 1;
            boolean intersection = GeometryUtils.lineIntersections(vertices1.get(i), vertices1.get(ni), vertices2).size() > 0;
            if (!intersection) {
                Pair<Vector2, Vector2> edge = new Pair<>(vertices1.get(i), vertices1.get(ni));
                edges.add(edge);
            }
        }

        for (int i = 0; i < vertices2.size(); i++) {
            int ni = (i == vertices2.size() - 1) ? 0 : i + 1;
            boolean intersection = GeometryUtils.lineIntersections(vertices2.get(i), vertices2.get(ni), vertices1).size() > 0;
            if (!intersection) {
                Pair<Vector2, Vector2> edge = new Pair<>(vertices2.get(i), vertices2.get(ni));
                edges.add(edge);
            }
        }
        List<Vector2> nodes = new ArrayList<>();

        for (Pair<Vector2, Vector2> edge : edges) {
            if (!nodes.contains(edge.first)) nodes.add(edge.first);
            if (!nodes.contains(edge.second)) nodes.add(edge.second);
        }

        for (Vector2 v : nodes) {
            GameScene.plotter2.drawPoint(v, Color.YELLOW, 1f, 3f);
        }
        for (Pair<Vector2, Vector2> edge : edges) {
            GameScene.plotter2.drawLine2(edge.first, edge.second, Color.BLUE, 2);
        }

        Vector2 nodesArray[] = nodes.toArray(new Vector2[0]);
        boolean adjMatrix[][] = new boolean[nodesArray.length][nodesArray.length];
        for (Pair<Vector2, Vector2> edge : edges) {
            int index1 = nodes.indexOf(edge.first);
            int index2 = nodes.indexOf(edge.second);
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
        return TestCycles.compute(adjMatrix, nodesArray);
    }
}
