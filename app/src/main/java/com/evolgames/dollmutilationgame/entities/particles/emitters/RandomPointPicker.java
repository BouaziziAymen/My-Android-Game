package com.evolgames.dollmutilationgame.entities.particles.emitters;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import org.andengine.util.adt.transformation.Transformation;

import java.util.List;
import java.util.function.Predicate;

public class RandomPointPicker {
    private final Predicate<CoatingBlock> coatingBlockPredicate;
    private final List<CoatingBlock> coatingBlocks;
    private final float fullArea;
    private CoatingBlock[] associatedCoatingBlocks;
    private float[] trianglesData;
    private float[] trianglesAccumulatedWeight;
    private float coverageRatio;
    private int selectedTriangle;

    RandomPointPicker(List<CoatingBlock> coatingBlockList, Predicate<CoatingBlock> predicate) {
        this.coatingBlocks = coatingBlockList;
        this.coatingBlockPredicate = predicate;
        float area = 0;
        for (CoatingBlock coatingBlock : coatingBlockList) {
            area += coatingBlock.getArea();
        }
        this.fullArea = area;
        setupData();
        computeTrianglesData();
        calculateWeights();
    }

    protected void calculateWeights() {
        float coveredArea = 0;
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            List<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                if (coatingBlockPredicate.test(coatingBlock)) {
                    Vector2 p1 = vertices.get(i);
                    Vector2 p2 = vertices.get(i + 1);
                    Vector2 p3 = vertices.get(i + 2);
                    float area = GeometryUtils.calculateTriangleArea(p1, p2, p3);
                    coveredArea += area;
                    trianglesAccumulatedWeight[counter] = coveredArea;
                }
                counter++;
            }
        }
        for (int i = 0; i < trianglesAccumulatedWeight.length; i++) {
            trianglesAccumulatedWeight[i] /= coveredArea;
        }

        coverageRatio = coveredArea / fullArea;
    }

    protected void setupData() {
        int numberOfVertices = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            numberOfVertices += coatingBlock.getTriangles().size();
        }
        trianglesData = new float[numberOfVertices * 2];
        int size = numberOfVertices / 3;
        trianglesAccumulatedWeight = new float[size];
        associatedCoatingBlocks = new CoatingBlock[size];
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            List<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                associatedCoatingBlocks[counter++] = coatingBlock;
            }
        }
    }

    protected void computeTrianglesData() {
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            List<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i++) {
                Vector2 vector2 = vertices.get(i);
                int baseIndex = counter * 2;
                trianglesData[baseIndex] = vector2.x;
                trianglesData[baseIndex + 1] = vector2.y;
                counter++;
            }
        }
    }

    private void selectTriangle() {
        float randomFloat = GeometryUtils.random.nextFloat();
        for (int i = 0; i < trianglesAccumulatedWeight.length; i++) {
            if (coatingBlockPredicate.test(associatedCoatingBlocks[i])) {
                if (randomFloat < trianglesAccumulatedWeight[i]) {
                    selectedTriangle = i;
                    break;
                }
            }
        }
    }

    public void getRandomPoint(float[] point) {
        selectTriangle();
        float x1 = trianglesData[6 * selectedTriangle];
        float y1 = trianglesData[6 * selectedTriangle + 1];
        float x2 = trianglesData[6 * selectedTriangle + 2];
        float y2 = trianglesData[6 * selectedTriangle + 3];
        float x3 = trianglesData[6 * selectedTriangle + 4];
        float y3 = trianglesData[6 * selectedTriangle + 5];
        GeometryUtils.generateRandomPointInTriangle(point, x1, y1, x2, y2, x3, y3);
    }

    public float getCoverageRatio() {
        return coverageRatio;
    }

    public CoatingBlock getActiveCoatingBlock() {
        return associatedCoatingBlocks[selectedTriangle];
    }

    public void transformData(Transformation transformation) {
        transformation.transform(trianglesData);
    }
}
