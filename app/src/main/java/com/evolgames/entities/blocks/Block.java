package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.composite.Composite;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.BlockProperties;
import com.evolgames.factories.MeshFactory;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Block<T extends Block<T,P>, P extends BlockProperties> extends Composite<T> {
    private int ID;
    private BlockProperties properties;
    private ArrayList<Vector2> Vertices;
    private ArrayList<Vector2> Triangles;
    private boolean Aborted;


    public void performCut(Cut cut){
        setAborted(true);
    }

    public P getProperties() {
        return (P) properties;
    }

    protected void setProperties(BlockProperties properties) {
        this.properties = properties;
    }

    public void initialization(ArrayList<Vector2> vertices, BlockProperties properties, int id, boolean firstTime) {//Template Pattern
        setID(id);
        setVertices(vertices);
        setProperties(properties);
        if (rectify()) rectifyVertices();
        if(calcArea())calculateArea();
        if (check()) checkShape();
        if (isNotAborted()) {
            if (arrange()) arrangeVertices();
        }
        particularInitialization(firstTime);
    }

    protected boolean verticesChanged = false;
    protected abstract void calculateArea();

    protected abstract boolean calcArea();

    protected void particularInitialization(boolean firstTime){}

    protected abstract T createChildBlock();

    protected abstract void checkShape();

    final public int getID() {
        return ID;
    }

    final public void setID(int id) {
        ID = id;
    }

    public boolean isNotAborted() {
        return !Aborted;
    }

    public void setAborted(boolean b) {
        Aborted = b;
    }

    protected abstract boolean rectify();

    protected abstract boolean arrange();

    protected abstract boolean check();


     public void  computeTriangles(){
        Triangles = MeshFactory.getInstance().triangulate(Vertices);
        verticesChanged = false;
    }
    protected void rectifyVertices() {
        BlockUtils.bruteForceRectification(getVertices(), 0.1f);
    }

    private void arrangeVertices() {
        if (!GeometryUtils.IsClockwise(getVertices())) Collections.reverse(getVertices());
    }

    final public ArrayList<Vector2> getVertices() {
        return Vertices;
    }

    public final void setVertices(ArrayList<Vector2> vertices) {
        this.Vertices = vertices;
    }

    public BlockA getParent() {
        return parent;
    }

    public void setParent(BlockA parent) {
        this.parent = parent;
    }

    private BlockA parent;


    public void translate(Vector2 t) {
            Utils.translatePoints(Vertices,t);
            computeTriangles();
    }

    public ArrayList<Vector2> getTriangles() {
        if(Triangles==null)computeTriangles();
        return Triangles;
    }
    public float[] getTrianglesData(){
        int size = Triangles.size();
        float[] data = new float[size*2];
        for(int i=0;i<size;i++){
            data[2*i] = Triangles.get(i).x;
            data[2*i + 1] = Triangles.get(i).y;
        }
        return data;
    }
    public void recycleSelf() {
        if(true)return;
        for (Vector2 v : getTriangles()) if(!getVertices().contains(v))Vector2Pool.recycle(v);
        for (Vector2 v : getVertices()) Vector2Pool.recycle(v);
    }
}
