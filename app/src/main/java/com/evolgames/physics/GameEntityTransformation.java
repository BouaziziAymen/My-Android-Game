package com.evolgames.physics;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;

import java.util.ArrayList;

public class GameEntityTransformation {
    private Vector2 position1;
    private Vector2 position2;
    private float rotation1;
    private float rotation2;

    public GameEntityTransformation(Vector2 position1, Vector2 position2, float rotation1, float rotation2) {
        this.position1 = position1;
        this.position2 = position2;
        this.rotation1 = rotation1;
        this.rotation2 = rotation2;
    }

    public static ArrayList<Vector2> interpolate(GameEntityTransformation transformation, ArrayList<Vector2> vertices, boolean regular) {
        if (!regular)
            return interpolate(transformation.position1, transformation.rotation1, transformation.position2, transformation.rotation2, vertices);
        else
            return interpolate(transformation.position2, transformation.rotation2, transformation.position1, transformation.rotation1, vertices);
    }

    public static ArrayList<Vector2> interpolate(Vector2 position1, float rotation1, Vector2 position2, float rotation2, ArrayList<Vector2> vertices) {
        float[] verticesLocalRealData = new float[2 * vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            verticesLocalRealData[2 * i] = vertices.get(i).x / 32f;
            verticesLocalRealData[2 * i + 1] = vertices.get(i).y / 32f;
        }

        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(position1.x, position1.y);
        GeometryUtils.transformation.preRotate(rotation1 * GeometryUtils.TO_DEGREES);
        GeometryUtils.transformation.transform(verticesLocalRealData);
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preRotate(-rotation2 * GeometryUtils.TO_DEGREES);
        GeometryUtils.transformation.preTranslate(-position2.x, -position2.y);

        GeometryUtils.transformation.transform(verticesLocalRealData);
        ArrayList<Vector2> result = new ArrayList<>();
        for (int i = 0; i < verticesLocalRealData.length; i += 2) {
            result.add(new Vector2(verticesLocalRealData[i], verticesLocalRealData[i + 1]));
        }
        return result;
    }


    public static ArrayList<Vector2> interpolateScene(GameEntityTransformation transformation, ArrayList<Vector2> vertices, boolean regular) {
        if (!regular)
            return interpolateScene(transformation.position1, transformation.rotation1, transformation.position2, transformation.rotation2, vertices);
        else
            return interpolateScene(transformation.position2, transformation.rotation2, transformation.position1, transformation.rotation1, vertices);
    }

    public static ArrayList<Vector2> interpolateScene(Vector2 position1, float rotation1, Vector2 position2, float rotation2, ArrayList<Vector2> vertices) {
        float[] verticesLocalRealData = new float[2 * vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            verticesLocalRealData[2 * i] = vertices.get(i).x / 32f;
            verticesLocalRealData[2 * i + 1] = vertices.get(i).y / 32f;
        }

        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(position1.x, position1.y);
        GeometryUtils.transformation.preRotate(rotation1 * GeometryUtils.TO_DEGREES);
        GeometryUtils.transformation.transform(verticesLocalRealData);
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preRotate(-rotation2 * GeometryUtils.TO_DEGREES);
        GeometryUtils.transformation.preTranslate(-position2.x, -position2.y);

        GeometryUtils.transformation.transform(verticesLocalRealData);
        ArrayList<Vector2> result = new ArrayList<>();
        for (int i = 0; i < verticesLocalRealData.length; i += 2) {
            result.add(new Vector2(verticesLocalRealData[i] * 32f, verticesLocalRealData[i + 1] * 32f));
        }
        return result;
    }
}
