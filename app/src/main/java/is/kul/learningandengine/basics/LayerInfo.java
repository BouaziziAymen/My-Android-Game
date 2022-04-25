package is.kul.learningandengine.basics;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

public class LayerInfo {
float[] basic;
Color[] colors;
int[] layerVertexCount;
int layerID = -1;
public LayerInfo(float[] layerData, Color[] colors, int[] layerVertexCount, int ID ){
    basic = layerData;
	this.colors =  colors;
	this.layerVertexCount = layerVertexCount;
    layerID = ID;
}


public float[] getData() {

	
	return this.basic;
}

public Color[]getColors(){

		

	return this.colors;
}
public int[] getLayerVertexCount() {
	
	return this.layerVertexCount;
}


public void update(float[] newBasic, int[] newVertexCount, Color[] newColors) {
    basic = newBasic;
    layerVertexCount = newVertexCount;
    colors = newColors;
	
}

}
