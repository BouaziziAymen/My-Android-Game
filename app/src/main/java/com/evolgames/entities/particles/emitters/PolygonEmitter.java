package com.evolgames.entities.particles.emitters;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.transformation.Transformation;

import java.util.ArrayList;


public class PolygonEmitter extends BaseParticleEmitter {
    private final GameEntity gameEntity;
    private float fullArea;
    private float[] data;
    private Color[] initialColors;
    private int selectedTriangle;
    private ArrayList<CoatingBlock> coatingBlocks;
    private float px;
    private float py;
    private float pr;
    private float[] weights;
    private CoatingBlock[] associatedCoatingBlocks;
    private float coverageRatio;

    public PolygonEmitter(GameEntity entity) {
        super(0, 0);
        coatingBlocks = new ArrayList<>();

        this.gameEntity = entity;
        setupData();
        computeData();
        px = 0;
        py = 0;
        pr = 0;


        fullArea = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            fullArea += coatingBlock.getArea();
        }

    }

    public void update() {
        calculateWeights();
        if (Math.abs(px - gameEntity.getMesh().getX()) > 1 || Math.abs(py - gameEntity.getMesh().getY()) > 1 || Math.abs(pr - gameEntity.getMesh().getRotation()) > 1) {
            computeData();
            Transformation transformation = gameEntity.getMesh().getLocalToSceneTransformation();
            transformation.transform(data);
            px = gameEntity.getMesh().getX();
            py = gameEntity.getMesh().getY();
            pr = gameEntity.getMesh().getRotation();
        }
    }

    private void setupData() {
        for (LayerBlock b : gameEntity.getBlocks()) {
                coatingBlocks.addAll(b.getBlockGrid().getCoatingBlocks());
        }
        int numberOfVertices = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            numberOfVertices += coatingBlock.getTriangles().size();
        }
        data = new float[numberOfVertices * 2];
        int size = numberOfVertices / 3;
        weights = new float[size];
        associatedCoatingBlocks = new CoatingBlock[numberOfVertices / 3];
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                associatedCoatingBlocks[counter++] = coatingBlock;
            }
        }
        initialColors = new Color[size];
        for (int i = 0; i < size; i++) {
            initialColors[i] = associatedCoatingBlocks[i].getProperties().getFlameColor1();

        }

    }

    private void calculateWeights() {
        //  for(int i=0;i<associatedCoatingBlocks.length;i++)
        //    associatedCoatingBlocks[i].setTest(true);


        float totalArea = 0;
        int counter = 0;
        for (CoatingBlock coatingBlock : coatingBlocks) {
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i += 3) {
                if (coatingBlock.isOnFire()) {
                    Vector2 p1 = vertices.get(i);
                    Vector2 p2 = vertices.get(i + 1);
                    Vector2 p3 = vertices.get(i + 2);
                    float area = GeometryUtils.calculateTriangleArea(p1, p2, p3);
                    totalArea += area;
                    weights[counter] = totalArea;
                }
                counter++;
            }
        }
        for (int i = 0; i < weights.length; i++) weights[i] /= totalArea;

        coverageRatio = totalArea / fullArea;

    }


    private void computeData() {
        int counter = 0;

        for (CoatingBlock coatingBlock : coatingBlocks) {
            ArrayList<Vector2> vertices = coatingBlock.getTriangles();
            for (int i = 0; i < vertices.size(); i++) {
                Vector2 vertice = vertices.get(i);
                int baseIndex = counter * 2;
                data[baseIndex] = vertice.x;
                data[baseIndex + 1] = vertice.y;
                counter++;
            }
        }
    }

    private void selectTriangle() {
        float randomFloat = GeometryUtils.random.nextFloat();
        for (int i = 0; i < weights.length; i++) {
            if (associatedCoatingBlocks[i].isOnFire())
                if (randomFloat < weights[i]) {
                    selectedTriangle = i;
                    break;
                }
        }
    }

    @Override
    public void getPositionOffset(float[] pOffset) {
        selectTriangle();
        float x1 = data[6 * selectedTriangle];
        float y1 = data[6 * selectedTriangle + 1];
        float x2 = data[6 * selectedTriangle + 2];
        float y2 = data[6 * selectedTriangle + 3];
        float x3 = data[6 * selectedTriangle + 4];
        float y3 = data[6 * selectedTriangle + 5];

        GeometryUtils.generateRandomPointInTriangle(pOffset, x1, y1, x2, y2, x3, y3);
        //GameScene.plotter2.drawPoint(new Vector2(pOffset[0],pOffset[1]),Color.CYAN,1,0);
    }


    public float getCoverageRatio() {
        return coverageRatio;
    }

    public double getTemperature() {
        return associatedCoatingBlocks[selectedTriangle].getFlameTemperature();
    }
}
