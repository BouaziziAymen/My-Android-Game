package is.kul.learningandengine.basics;

import is.kul.learningandengine.MyConstants;
import is.kul.learningandengine.scene.World;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class BlockGrid {


public ArrayList<Grain> borderGrains;
public ArrayList<Grain> allGrains;
ArrayList<Vector2> VERTICES;
public Vector2 startup;


ArrayList<Grain> sideGrains;
public HashSet<Grain> reservoir;
public Grain[][] generateTemperatureArray(){
	int minnx = Integer.MAX_VALUE,minny=Integer.MAX_VALUE,maxnx=Integer.MIN_VALUE,maxny=Integer.MIN_VALUE;
	for(Grain g: this.allGrains){
		if(g.nx<minnx)minnx = g.nx;
		if(g.nx>maxnx)maxnx = g.nx;
		if(g.ny<minny)minny = g.ny;
		if(g.ny>maxny)maxny = g.ny;
	}
	int nxl = maxnx - minnx + 1;
	int nyl = maxny - minny + 1;
	
Grain[][] array = new Grain[nxl][nyl];	
for(Grain g: this.allGrains){
	array[g.nx - minnx][g.ny - minny] = g;
}

return array;
}






public BlockGrid(ArrayList<Grain> grains, ArrayList<Vector2> sides , Vector2 startup){
    allGrains = grains;
    this.reservoir = new HashSet<Grain>();
for(Grain g:grains) this.reservoir.add(g);
    this.borderGrains = new ArrayList<Grain>();
    VERTICES = sides;
this.startup = startup;
for(Grain g:grains){
if(g.nx%2==0&&g.ny%2==0)g.main = true;

if(this.isBorderGrain(g)){
    this.borderGrains.add(g);g.isBorder = true;}


}


}
boolean isBorderGrain(Grain g){
	if(!this.testGrain(g.nx+1,g.ny))return true;
	if(!this.testGrain(g.nx+1,g.ny-1))return true;
	if(!this.testGrain(g.nx+1,g.ny+1))return true;
	if(!this.testGrain(g.nx-1,g.ny))return true;
	if(!this.testGrain(g.nx-1,g.ny-1))return true;
	if(!this.testGrain(g.nx-1,g.ny+1))return true;
	if(!this.testGrain(g.nx,g.ny-1))return true;
    return !this.testGrain(g.nx, g.ny + 1);
}

boolean testGrain(int nx, int ny){
	
	for(Grain grain: this.allGrains){
		if(grain.nx==nx&&grain.ny==ny)return true;
	}
		return false;
}
Grain extractGrain(int nx, int ny){
	for(Grain grain: this.allGrains)
		if(grain.nx==nx&&grain.ny==ny)return grain;
	return null;
}


Grain getGrain(int nx, int ny){
	for(Grain grain: this.allGrains)
		if(grain.nx==nx&&grain.ny==ny)return grain;
	Grain nullGrain= new Grain(nx, ny, this.startup.x+32*nx, this.startup.y+32*ny,null);
	nullGrain.isnull = true;
	
	return nullGrain;
}

public HashSet<Grain> getNeighborsClean(Grain grain){
	HashSet<Grain> result = new HashSet<Grain>();
	int nx = grain.nx;
	int ny = grain.ny;
Grain g = extractGrain(nx, ny+1);
	    if(g!=null)result.add(g);
	  g = extractGrain(nx, ny-1);

	    if(g!=null)result.add(g);
	  g = extractGrain(nx+1, ny);
	    if(g!=null)result.add(g);
	  g = extractGrain(nx-1, ny);
	    if(g!=null)result.add(g);

return result;
}


public HashSet<Grain> getNeighbors(Grain grain){
	HashSet<Grain> result = new HashSet<Grain>();
	int nx = grain.nx;
	int ny = grain.ny;
Grain g = extractGrain(nx, ny+1);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx, ny-1);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
  	  g = extractGrain(nx+1, ny+1);
  	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx-1, ny+1);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx+1, ny-1);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx-1, ny-1);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx+1, ny);
	    if(g!=null)if(!g.tempUpdated)result.add(g);
	  g = extractGrain(nx-1, ny);
	    if(g!=null)if(!g.tempUpdated)result.add(g);

return result;
}





public void resetTempValves() {

}


public void innerHeatTransfer() {
	//HEAT INTERACTION BETWEEN GRAINS
for(Grain g: allGrains){
	HashSet<Grain> group = getNeighbors(g);
	for(Grain gg:group){
	
	World.transferHeat(1, 1, 1, 1, g, gg, 6f, 6f, 100f, 100f);

	}
}
}



public void innerHeatTransfer(Grain grain) {
	//HEAT INTERACTION BETWEEN GRAINS

	HashSet<Grain> group = getNeighbors(grain);
	for(Grain gg:group){
	
	World.transferHeat(1, 1, 1, 1, grain, gg, 6f, 6f, 100f, 100f);

	
}






}






public Grain getNearestGrain(Vector2 center) {
	float distance = Float.MAX_VALUE;
	Grain result = null;
	for(Grain g: this.allGrains){
		float d = g.distance(center);
		if(d<distance){distance = d;result = g;}
	}
	return result;
	
}
public Grain getNearestBorderGrain(Vector2 center) {
	float distance = Float.MAX_VALUE;
	Grain result = null;
	for(Grain g: this.borderGrains){
		float d = g.distance(center);
		if(d<distance){distance = d;result = g;}
	}
	return result;
	
}






public HashSet<Grain> getNeighborsCleanFull(Grain grain) {

		HashSet<Grain> result = new HashSet<Grain>();
		int nx = grain.nx;
		int ny = grain.ny;
	Grain g = extractGrain(nx, ny+1);
		    if(g!=null)result.add(g);
		  g = extractGrain(nx, ny-1);
		    if(g!=null)result.add(g);
	  	  g = extractGrain(nx+1, ny+1);
	  	    if(g!=null)result.add(g);
		  g = extractGrain(nx-1, ny+1);
		    if(g!=null)result.add(g);
		  g = extractGrain(nx+1, ny-1);
		    if(g!=null)result.add(g);
		  g = extractGrain(nx-1, ny-1);
		    if(g!=null)result.add(g);
		  g = extractGrain(nx+1, ny);
		    if(g!=null)result.add(g);
		  g = extractGrain(nx-1, ny);
		    if(g!=null)result.add(g);

	return result;
	

}








}
