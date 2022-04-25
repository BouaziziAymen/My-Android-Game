package is.kul.learningandengine.basics;

import java.util.ArrayList;

import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.World;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class Grain implements Comparable<Grain>{
int nx,ny;

public Vector2 position;

public boolean isnull;
boolean dead;

public int weight;

float value;


public boolean main;

public boolean isNode;

public boolean burned;
private int id;





public boolean onFire;

public boolean intersectionPoint;

public float temperature;

public boolean tempUpdated;

private Block block;

public boolean used;

public boolean isBorder;

public boolean inter;



public Grain(int i, int j, float x, float y, Block block) {
    this.position = new Vector2(x,y);
    this.nx = i;
    this.ny = j;
    setBlock(block);

    energyRatio = 1f;
}

public Grain(Vector2 point) {
    this.position = point;
    isNode = true;
}

@Override
public String toString(){
	//return "("+nx+","+ny+"){"+id+"}";
	return "("+ this.block.grid.allGrains.indexOf(this)+")";
	//return "("+String.format("%.0f",position.x)+","+String.format("%.0f", position.y)+")";
	//return "("+position.x+","+position.y+")";
}


public String toString2(){
	//return "("+id+")";
	return "("+String.format("%.0f", this.position.x)+","+String.format("%.0f", this.position.y)+")";
	//return "("+position.x+","+position.y+")";
}


@Override
public int compareTo(Grain another) {
	return value >another.value ?1:-1;
}

public float distance(Vector2 pc) {
	// TODO Auto-generated method stub
	return position.dst(pc);
}

public void setValue(float value) {
	this.value = value;
	
}
float energy = 1000;
float energyRatio = 1f;
public void updateGrain() {
	if(this.onFire && this.energy >=60) this.energy -=60;

    this.energyRatio = this.energy /1000;
    this.burned = true;
	
	if(this.temperature >500&&!this.onFire){


        this.onFire = true;
        this.getBlock().createFire(this);}
	if(this.temperature <500& this.onFire){
        onFire = false;}
if(isBorder &&Math.abs(this.temperature -World.AMBIENT_TEMPERATURE)>10) this.temperature +=Math.signum(World.AMBIENT_TEMPERATURE- this.temperature)*10;
	
}

public Block getBlock() {
	return this.block;
}

public void setBlock(Block block) {
	this.block = block;
}

public void copyValues(Grain nearest) {
    temperature = nearest.temperature;
    onFire = nearest.onFire;
	
}

public void applyHeat(float heat) {

if(heat>0&& this.temperature +heat<1600 ||heat<0) this.temperature +=heat;
	
	
}


public int getId() {
	return this.id;
}

public void setId(int id) {
	this.id = id;
}

public Color getColor() {

	return this.block.color;
}

public Color getColorResult() {
ArrayList<Color> list = new ArrayList<>();

if(burned)list.add(new Color(0.11f, 0.17f, 0.21f,1f- energyRatio));
	if(temperature >1200)list.add(new Color(1f,0.4f,0f,(this.temperature -1200)/50f*0.1f));
if(temperature >1200)list.add(new Color(1f,0.4f,0f,1f));
Color r = getColor();
for(Color c:list){
	 r = Utils.blendColors(r, c);
}
return r;

}
public int getLayers() {
	int r = 0;
	if(burned)r+=1;
	if(temperature >1200)r+=10;
    this.layersN = r;
	return r;
}
int layersN;

public boolean hasLayers() {
	if(burned)return true;
    return temperature > 1200;
}

}
