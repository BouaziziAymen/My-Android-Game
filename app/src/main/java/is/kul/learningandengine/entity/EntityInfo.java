package is.kul.learningandengine.entity;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

public class EntityInfo {
public float[] trianglesData;
public Color[] colors;
public int[] layersVertexCount;
public int vertexCount;
public ArrayList<Integer> INDEXES;
public ArrayList<ArrayList<Vector2>> vertices;
public void setElements(ArrayList<ArrayList<Vector2>> vertices, ArrayList<Integer> indexes){
	this.vertices = vertices;
    INDEXES = indexes;
}

public EntityInfo(float[] vertexData){
    trianglesData = vertexData;
    this.colors = new Color[]{new Color(1f,1f,1f,1f)};
    this.layersVertexCount = new int[]{vertexData.length/3};
    vertexCount = vertexData.length/3;
}
public EntityInfo(float[] vertexData,int[] layersVertexCount,Color[] colors){
    trianglesData = vertexData;
	this.colors = colors;
	this.layersVertexCount = layersVertexCount;
    vertexCount = vertexData.length/3;
}

}
