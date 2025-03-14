package com.evolgames.dollmutilationgame.entities.blocks;

import androidx.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.cut.Cut;
import com.evolgames.dollmutilationgame.utilities.BlockUtils;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.entities.composite.Composite;
import com.evolgames.dollmutilationgame.entities.factories.MeshFactory;
import com.evolgames.dollmutilationgame.entities.properties.Properties;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.Collections;
import java.util.List;

public abstract class Block<T extends Block<T, P>, P extends Properties> extends Composite<T> {
    private int id;
    private Properties properties;
    private List<Vector2> vertices;
    private List<Vector2> triangles;
    private boolean aborted;

    public void performCut(Cut cut) {
        setAborted(true);
    }

    public P getProperties() {
        return (P) properties;
    }

    public void initialization(
            List<Vector2> vertices, Properties properties, int id) { // Template Pattern
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
                arrangeVertices(false);
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

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
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
    }

    protected void rectifyVertices() {
        BlockUtils.bruteForceRectification(getVertices(), 0.1f);
    }

    private void arrangeVertices(boolean skipTest) {
        if (skipTest || !GeometryUtils.IsClockwise(getVertices())) {
            Collections.reverse(getVertices());
        }
    }

    public final List<Vector2> getVertices() {
        return vertices;
    }

    public final void setVertices(List<Vector2> vertices) {
        this.vertices = vertices;
    }

    public List<Vector2> getTriangles() {
        if (triangles == null) {
            computeTriangles();
        }
        return triangles;
    }

    public void recycleSelf() {
        for (Vector2 v : getTriangles()) {
            if (!getVertices().contains(v)) {
                Vector2Pool.recycle(v);
            }
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


    public void mirror() {
        this.vertices = GeometryUtils.mirrorPoints(this.vertices);
        arrangeVertices(true);
        if (triangles != null) {
            this.triangles = GeometryUtils.mirrorPoints(this.triangles);
        }
    }
}
