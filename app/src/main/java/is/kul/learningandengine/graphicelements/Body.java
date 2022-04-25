package is.kul.learningandengine.graphicelements;

import is.kul.learningandengine.basics.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import org.andengine.entity.Entity;
import org.andengine.util.adt.color.Color;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class Body extends Entity {
Entity layers;
int bodyID;	
int activeLayer;
public int numberOfLayers;
int ZINDEX;
int[] decaledLayerIndexes;
BodyProperty properties;

	public Layer getLayer(int ID) {
		for (int i = 0; i <layers.getChildCount(); i++) {
			Layer layer = (Layer) layers.getChildByIndex(i);
			if (layer.ID == ID) return layer;
		}
		return null;
	}

	public enum BodyState{
	SELECTED,NOTSELECTED, MOVED
}
Body.BodyState state;
public void changeState(Body.BodyState newState){
    state = newState;
	if(state == Body.BodyState.NOTSELECTED) setVisible(false);
	if(state == Body.BodyState.SELECTED) setVisible(true);
	if(state == Body.BodyState.MOVED) setMovableLayers();
}

 int getLastOrder(){
	return this.layers.getChildCount();
 }
	Body(int bodyID){
        this.bodyID = bodyID;
        this.ZINDEX = 999+bodyID;
        this.layers = new Entity();
        state = Body.BodyState.SELECTED;
        attachChild(this.layers);
        this.decaledLayerIndexes = new int[0];
		properties = new BodyProperty("Body "+bodyID,bodyID);
	}
	public BodyProperty getProperties(){
	return properties;
	}

	int getNumberOfActiveLayers(){

		int number = 0;
		for(int i = 0; i< this.layers.getChildCount(); i++){
			Layer layer = (Layer) this.layers.getChildByIndex(i);
			if(layer.isActive())number++;
		}
		return number;
	}

	public ArrayList<Vector2> getCenters() {
		ArrayList<Vector2> result = new ArrayList<Vector2>();

		for(int i = 0; i< this.layers.getChildCount(); i++) {
			Layer layer = (Layer) this.layers.getChildByIndex(i);
			result.addAll(layer.getCenters());
		}
		return result;
	}





	public void setActiveLayer(int index){

        this.activeLayer = index;

		for(int i = 0; i< this.layers.getChildCount(); i++){
			Layer layer = (Layer) this.layers.getChildByIndex(i);

			if(layer.ID==index)
			{
			layer.changeState(Layer.State.SELECTED);

			}
			else {
			layer.changeState(Layer.State.NOTSELECTED);

			}
	}

	}
	public void setMovableLayers() {

		for(int i = 0; i< this.layers.getChildCount(); i++){
			Layer layer = (Layer) this.layers.getChildByIndex(i);
			boolean found = false;
			for(int j = 0; j< this.decaledLayerIndexes.length; j++){

				if(layer.ID== this.decaledLayerIndexes[j]){
					layer.changeState(Layer.State.HIGHLIGHTED);
					found = true;
					break;
				}

			}
			if(!found)layer.changeState(Layer.State.NOTSELECTED);
			}

	}

	public void resetActiveLayer() {
		for(int i = 0; i< this.layers.getChildCount(); i++){
			Layer layer = (Layer) this.layers.getChildByIndex(i);
		layer.setState(Layer.State.NOTSELECTED);
		}
        setActiveLayer(this.activeLayer);

	}
	public void createNewLayer(int ID) {
		numberOfLayers++;
		Layer layer = new Layer(ID, getLastOrder());
		layers.attachChild(layer);
		setActiveLayer(ID);
	}

	public ArrayList<Block> generateBlocks(boolean transparent){
		ArrayList<Block> blocks = new ArrayList<>();
		for(int i = 0; i< this.layers.getChildCount(); i++){
			Layer layer = (Layer) this.layers.getChildByIndex(i);
			if(layer.isValid()) {
				Block block = layer.generateBlock();
				if (transparent)
					if (state == Body.BodyState.NOTSELECTED)
						block.color = new Color(block.color.getRed(), block.color.getGreen(), block.color.getBlue(), 0.1f);
				blocks.add(block);
			}
		}
		Collections.sort(blocks);
		return blocks;
	}
}
