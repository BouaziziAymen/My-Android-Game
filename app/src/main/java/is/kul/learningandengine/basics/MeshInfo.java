package is.kul.learningandengine.basics;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

public class MeshInfo {
	
ArrayList<LayerInfo> layers;
public int[] layersVertexCount;
public Color[] colors;
public void addLayer(int blockID, float[] newBasic,int[] newVertexCount, Color[] newColors){
	LayerInfo info = new LayerInfo(newBasic,newColors,newVertexCount,blockID);
    this.layers.add(info);
	}
    public MeshInfo(ArrayList<LayerInfo> layers){
		this.layers = layers;
        this.updateColors();
        this.updateVerticesCount();
		
		
	}
    
   public float[] getVerticesData(){
	   int count = 0;
    	for(LayerInfo info: this.layers){
    		float[] layerData = info.getData();
    		count+=layerData.length;
    	}
    	float[] result = new float[count];
    	count = 0;
    	
    	for(LayerInfo info: this.layers){
    		float[] layerData = info.getData();
    		for(int i=0;i<layerData.length;i++){
    			result[count+i] = layerData[i];
    		}
    		count += layerData.length;
    	}
    	return result;
    }
   
   
   
   public void updateColors(){
	   int count = 0;
   	for(LayerInfo info: this.layers){
   		Color[] layerColors = info.getColors();
   		count+=layerColors.length;
   	}
   	Color[] result = new Color[count];
   	count = 0;
   	
   	for(LayerInfo info: this.layers){
   		Color[] layerColors = info.getColors();
   		for(int i=0;i<layerColors.length;i++){
   			result[count+i] = layerColors[i];
   		}
   		count += layerColors.length;
   	}
       colors = result;
   }
   public void updateVerticesCount(){
	   int count = 0;
   	for(LayerInfo info: this.layers){
   		int[] layerVertexCount = info.getLayerVertexCount();
   		count+=layerVertexCount.length;
   	}
   	int[] result = new int[count];
   	count = 0;
   	
   	for(LayerInfo info: this.layers){
   		int[] layerVertexCount = info.getLayerVertexCount();
   		for(int i=0;i<layerVertexCount.length;i++){
   			result[count+i] = layerVertexCount[i];
   		}
   		count += layerVertexCount.length;
   	}
       layersVertexCount = result;
   	
   }



public LayerInfo getLayerByID(int blockID) {
for(LayerInfo info: this.layers)if(info.layerID == blockID)return info;
return null;
}
public void updateLayer(int blockID, float[] newBasic,int[] newVertexCount, Color[] newColors){
    getLayerByID(blockID).update(newBasic, newVertexCount, newColors);
}

}
