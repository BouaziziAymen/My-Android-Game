package com.evolgames.entities.blocks;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.Utils;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class CoatingBlock extends AssociatedBlock<CoatingBlock, CoatingProperties> {

    public Vector2 position;

    public float value;
    private boolean isOnFire;
    private boolean isOnFlame;
    private transient HashSet<CoatingBlock> neighbors;
    private float area;
    private float step;
    private boolean pulverized;
    private transient LayerBlock parent;

    public CoatingBlock() {
    }

    @Override
    protected CoatingBlock getThis() {
        return this;
    }


    public void centerCoreCoatingBlock() {
        if (position == null) {
            position = new Vector2();
        }
        position.set(GeometryUtils.calculateCentroid(getVertices()));
    }

    @Override
    public void performCut(Cut cut) {
        setAborted(true);
        Pair<ArrayList<Vector2>, ArrayList<Vector2>> list =
                BlockUtils.splitVerticesSimple(cut, getVertices());
        CoatingBlock b1 = createChildBlock();
        b1.initialization(list.first, getProperties().copy(), getId());
        CoatingBlock b2 = createChildBlock();
        b2.initialization(list.second, getProperties().copy(), getId());

        b1.centerCoreCoatingBlock();
        b2.centerCoreCoatingBlock();
        addBlock(b1);
        addBlock(b2);
    }

    @Override
    protected void calculateArea() {
        area = GeometryUtils.getArea(getVertices());
    }

    @Override
    protected boolean shouldCalculateArea() {
        return true;
    }

    @Override
    protected CoatingBlock createChildBlock() {
        CoatingBlock child = new CoatingBlock();
        child.setStep(step);
        return child;
    }

    @Override
    public void computeTriangles() {
        super.computeTriangles();
        List<Vector2> triangles = getTriangles();
        for (int i = 0; i < triangles.size(); i += 3) {
            Vector2 centroid = GeometryUtils.calculateCentroid(triangles, i, 3);
            for (int j = 0; j < 3; j++) {
                Vector2 v = triangles.get(i + j);
                Vector2 u = Vector2Pool.obtain(v).sub(centroid).nor().mul(0.05f);
                v.add(u);
                Vector2Pool.recycle(u);
            }
        }
    }

    @Override
    public void translate(Vector2 translation, Vector2 worldTranslation) {
        Utils.translatePoints(getVertices(), translation);
        computeTriangles();
        centerCoreCoatingBlock();
    }

    public float getArea() {
        return area;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void applyDeltaTemperature(double delta) {
        getProperties().applyDeltaTemperature(delta);
    }

    public double getTemperature() {
        return getProperties().getTemperature();
    }

    public void setTemperature(double temperature) {
        getProperties().setTemperature(temperature);
    }

    @Override
    public void onStep(GameEntity parent) {
        double temperature = getTemperature();
        if (getProperties().isCombustible()) {
            double ignitionTemperature = getProperties().getIgnitionTemperature();
            double chemicalEnergy = getProperties().getChemicalEnergy();
            double initialChemicalEnergy = getProperties().getInitialChemicalEnergy();

            boolean onFire = (isOnFlame || temperature > ignitionTemperature)
                    && chemicalEnergy >= getProperties().getNonBurnableChemicalEnergy();
            setOnFire(onFire);
            if (isOnFire()) {
                if (chemicalEnergy < getProperties().getNonBurnableChemicalEnergy()) {
                    setOnFire(false);
                }
                double deltaE = -getFlameTemperature() / 1000;
                changeChemicalEnergy(deltaE);

            }
            double ratio = chemicalEnergy / initialChemicalEnergy;
            getProperties().setBurnRatio(1 - ratio);
        }

        getProperties().updateColors();
        parent.getMesh().onColorsUpdated();
    }

    private void applyDeltaChemicalEnergy(double delta) {
        double chemicalEnergy = getProperties().getChemicalEnergy();
        if (chemicalEnergy + delta >= 0) {
            chemicalEnergy += delta;
        }
        getProperties().setChemicalEnergy(chemicalEnergy);
    }

    private void changeChemicalEnergy(double delta) {
        applyDeltaChemicalEnergy(delta);
        for (CoatingBlock neighbor : neighbors) {
            neighbor.applyDeltaChemicalEnergy(delta / 10);
        }
    }

    public float distance(Vector2 point) {
        return point.dst(position);
    }

    public HashSet<CoatingBlock> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashSet<CoatingBlock> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isOnFire() {
        return isOnFire;
    }

    public void setOnFire(boolean onFire) {
        isOnFire = onFire;
    }

    public int getNx() {
        return getProperties().getRow();
    }

    public void setNx(int nx) {
        getProperties().setRow(nx);
    }

    public int getNy() {
        return getProperties().getColumn();
    }

    public void setNy(int ny) {
        getProperties().setColumn(ny);
    }

    public double getFlameTemperature() {
        return getProperties().getFlameTemperature();
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public boolean isPulverized() {
        return pulverized;
    }

    public void setPulverized(boolean pulverized) {
        this.pulverized = pulverized;
    }

    public LayerBlock getParent() {
        return parent;
    }

    public void setParent(LayerBlock parent) {
        this.parent = parent;
    }

    public void onSpark(double sparkTemperature) {
        float layerFlammability = parent.getProperties().getFlammability();
        float flammability = 0f;
        for (AssociatedBlock<?, ?> associatedBlock : parent.getAssociatedBlocks()) {
            if (associatedBlock instanceof StainBlock) {
                StainBlock stain = (StainBlock) associatedBlock;

                if (stain.getProperties().getFlammability() > 0) {
                    float d = this.position.dst(stain.getLocalCenterX(), stain.getLocalCenterY());
                    float liquidFlammability = stain.getProperties().getFlammability();
                    float tempRatio = (float) (sparkTemperature / 10000f);
                    float diff = liquidFlammability - flammability;
                    float delta = tempRatio * diff / (Math.max(1, d * d));
                    if (delta < 0) {
                        flammability = Math.max(0, flammability + delta);
                    } else {
                        flammability = Math.min(liquidFlammability, flammability + delta);
                    }
                }
            }
        }
        flammability = Math.max(flammability, layerFlammability);
        if (Math.random() < flammability) {
            this.isOnFlame = true;
            this.isOnFire = true;
        }
    }

    public void onHeatWave(float heatRatio) {
        if (!(getParent().getProperties().isCombustible() || getParent().getProperties().isFlammable())) {
            return;
        }
        if (Math.random() < 0.5f * heatRatio * heatRatio) {
            this.isOnFlame = true;
            this.isOnFire = true;
        }
    }


    @Override
    public void mirror() {
        super.mirror();
        position.set(GeometryUtils.mirrorPoint(position));
    }

    public void onFrost() {
        applyDeltaTemperature(-0.01f);
    }
}
