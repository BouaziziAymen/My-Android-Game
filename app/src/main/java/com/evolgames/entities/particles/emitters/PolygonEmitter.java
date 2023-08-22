package com.evolgames.entities.particles.emitters;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;

import java.util.ArrayList;
import java.util.List;

import is.kul.learningandengine.helpers.Predicate;

public abstract class PolygonEmitter  extends BaseParticleEmitter {


    protected List<CoatingBlock> coatingBlocks;
    protected CoatingBlock[] associatedCoatingBlocks;
    protected float[] trianglesData;
    protected float[] trianglesAccumulatedWeight;
    protected float fullArea;
    protected int selectedTriangle;
    private float coverageRatio;
    final protected Predicate<CoatingBlock> coatingBlockPredicate;

    public PolygonEmitter(List<CoatingBlock> coatingBlockList,Predicate<CoatingBlock> predicate) {
        super(0, 0);
        this.coatingBlockPredicate = predicate;
        this.coatingBlocks = coatingBlockList;
        for (CoatingBlock coatingBlock : coatingBlockList) {
            fullArea += coatingBlock.getArea();
        }
        setupData();
        computeTrianglesData();
        calculateWeights();
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
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                associatedCoatingBlocks[counter++] = coatingBlock;
            }
        }
    }


    protected void calculateWeights() {
        float coveredArea = 0;
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                if (coatingBlockPredicate.evaluate(coatingBlock)) {
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


    protected void computeTrianglesData() {
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
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
            if (coatingBlockPredicate.evaluate(associatedCoatingBlocks[i])) {
                if (randomFloat < trianglesAccumulatedWeight[i]) {
                    selectedTriangle = i;
                    break;
                }
            }
        }
    }

    @Override
    public void getPositionOffset(float[] pOffset) {
        selectTriangle();
        float x1 = trianglesData[6 * selectedTriangle];
        float y1 = trianglesData[6 * selectedTriangle + 1];
        float x2 = trianglesData[6 * selectedTriangle + 2];
        float y2 = trianglesData[6 * selectedTriangle + 3];
        float x3 = trianglesData[6 * selectedTriangle + 4];
        float y3 = trianglesData[6 * selectedTriangle + 5];

        GeometryUtils.generateRandomPointInTriangle(pOffset, x1, y1, x2, y2, x3, y3);
    }

    public float getCoverageRatio() {
        return coverageRatio;
    }

    public CoatingBlock getActiveCoatingBlock() {
        return associatedCoatingBlocks[selectedTriangle];
    }
}
