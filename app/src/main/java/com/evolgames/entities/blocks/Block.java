package com.evolgames.entities.blocks;

import android.support.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.composite.Composite;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Block<T extends Block<T, P>, P extends Properties> extends Composite<T> {
    protected boolean verticesChanged = false;
    private int id;
    private Properties properties;
    private ArrayList<Vector2> vertices;
    private ArrayList<Vector2> triangles;
    private boolean aborted;
    private LayerBlock parent;

    public void performCut(Cut cut) {
        setAborted(true);
    }

    public P getProperties() {
        return (P) properties;
    }

    public void initialization(ArrayList<Vector2> vertices, Properties properties, int id) {//Template Pattern
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
        specificInitialization();
    }

    protected abstract void calculateArea();

    protected abstract boolean shouldCalculateArea();

    protected void specificInitialization() {
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
        return !aborted;
    }

    public void setAborted(boolean b) {
        aborted = b;
    }

    protected abstract boolean shouldRectify();

    protected abstract boolean shouldArrangeVertices();

    protected abstract boolean shouldCheckShape();

    public void computeTriangles() {
        triangles = MeshFactory.getInstance().triangulate(vertices);
        verticesChanged = false;
    }

    protected void rectifyVertices() {
        BlockUtils.bruteForceRectification(getVertices(), 0.1f);
    }

    private void arrangeVertices() {
        if (!GeometryUtils.IsClockwise(getVertices())) {
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

    public abstract void translate(Vector2 t);

    public ArrayList<Vector2> getTriangles() {
        if (triangles == null) {
            computeTriangles();
        }
        return triangles;
    }

    public float[] getTrianglesData() {
        int size = triangles.size();
        float[] data = new float[size * 2];
        for (int i = 0; i < size; i++) {
            data[2 * i] = triangles.get(i).x;
            data[2 * i + 1] = triangles.get(i).y;
        }
        return data;
    }

    public void recycleSelf() {
        if (true) return;
        for (Vector2 v : getTriangles()) {
            if (!getVertices().contains(v)) Vector2Pool.recycle(v);
        }
        for (Vector2 v : getVertices()) {
            Vector2Pool.recycle(v);
        }
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return vertices.equals(obj);
    }
}
