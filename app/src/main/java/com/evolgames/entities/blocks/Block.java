package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.composite.Composite;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.Properties;
import com.evolgames.factories.MeshFactory;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Block<T extends Block<T, P>, P extends Properties> extends Composite<T> {
    protected boolean verticesChanged = false;
    private int id;
    private Properties properties;
    private ArrayList<Vector2> vertices;
    private ArrayList<Vector2> Triangles;
    private boolean Aborted;
    private LayerBlock parent;

    public void performCut(Cut cut) {
        setAborted(true);
    }

    public P getProperties() {
        return (P) properties;
    }

    public void initialization(ArrayList<Vector2> vertices, Properties properties, int id, boolean firstTime) {//Template Pattern
        this.id = id;
        this.vertices = vertices;
        this.properties = properties;
        if (shouldRectify()) {
            rectifyVertices();
        }
        if (shouldCalculateArea()) {
            calculateArea();
        }
        if (shouldCheckShape()) {
            checkShape();
        }
        if (isNotAborted()) {
            if (shouldArrangeVertices()) {
                arrangeVertices();
            }
        }
        particularInitialization(firstTime);
    }

    protected abstract void calculateArea();

    protected abstract boolean shouldCalculateArea();

    protected void particularInitialization(boolean firstTime) {
    }

    protected abstract T createChildBlock();

    protected abstract void checkShape();

    final public int getId() {
        return id;
    }

    final public void setId(int id) {
        this.id = id;
    }

    public boolean isNotAborted() {
        return !Aborted;
    }

    public void setAborted(boolean b) {
        Aborted = b;
    }

    protected abstract boolean shouldRectify();

    protected abstract boolean shouldArrangeVertices();

    protected abstract boolean shouldCheckShape();

    public void computeTriangles() {
        Triangles = MeshFactory.getInstance().triangulate(vertices);
        verticesChanged = false;
    }

    protected void rectifyVertices() {
        BlockUtils.bruteForceRectification(getVertices(), 0.1f);
    }

    private void arrangeVertices() {
        if (!GeometryUtils.IsClockwise(getVertices())){
            Collections.reverse(getVertices());
        }
    }

    final public ArrayList<Vector2> getVertices() {
        return vertices;
    }

    public final void setVertices(ArrayList<Vector2> vertices) {
        this.vertices = vertices;
    }

    public LayerBlock getParent() {
        return parent;
    }

    public void setParent(LayerBlock parent) {
        this.parent = parent;
    }

    public void translate(Vector2 t) {
        Utils.translatePoints(vertices, t);
        computeTriangles();
    }

    public ArrayList<Vector2> getTriangles() {
        if (Triangles == null) computeTriangles();
        return Triangles;
    }

    public float[] getTrianglesData() {
        int size = Triangles.size();
        float[] data = new float[size * 2];
        for (int i = 0; i < size; i++) {
            data[2 * i] = Triangles.get(i).x;
            data[2 * i + 1] = Triangles.get(i).y;
        }
        return data;
    }

    public void recycleSelf() {
        if (true) return;
        for (Vector2 v : getTriangles()) if (!getVertices().contains(v)) Vector2Pool.recycle(v);
        for (Vector2 v : getVertices()) Vector2Pool.recycle(v);
    }
}
