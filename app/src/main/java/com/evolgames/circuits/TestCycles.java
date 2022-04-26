package com.evolgames.circuits;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;


/**
 * Testfile for elementary cycle search.
 *
 * @author Frank Meyer
 */
public class TestCycles {


    public static void main(String[] args) {


        int num_vertices = 4;

        Vector2 nodes[] = new Vector2[num_vertices];
        boolean adjMatrix[][] = new boolean[num_vertices][num_vertices];


			nodes[0] = new Vector2(1, 1);
			nodes[1] = new Vector2(0, 1);
			nodes[2] = new Vector2(0, 0);
			nodes[3] = new Vector2(1, 0);


        adjMatrix[0][1] = true;
        adjMatrix[1][0] = true;

        adjMatrix[1][2] = true;
        adjMatrix[2][1] = true;

        adjMatrix[2][3] = true;
        adjMatrix[3][2] = true;

		adjMatrix[0][3] = true;
		adjMatrix[3][0] = true;


		adjMatrix[0][2] = true;
		adjMatrix[2][0] = true;


		ArrayList<Vector2> result = compute(adjMatrix, nodes);
	//	System.out.println(Arrays.toString(result.toArray()));
    }


    public static ArrayList<Vector2> compute(boolean[][] adjMatrix, Vector2[] nodes) {
        ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjMatrix, nodes);
        List cycles = ecs.getElementaryCycles();
        ArrayList<ArrayList<Vector2>> list = new ArrayList<>();
        for (int i = 0; i < cycles.size(); i++) {
            List cycle = (List) cycles.get(i);
            ArrayList set = new ArrayList<>(cycle);

            if (set.size() >= 3) list.add(set);
        }
        Collections.sort(list,(l1,l2)-> Float.compare(GeometryUtils.getArea(l1),GeometryUtils.getArea(l2)));
        Collections.reverse(list);
        if(list.size()>0)
        return list.get(0);
        else return null;
    }
}
