package is.kul.learningandengine.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.util.Log;


import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.Grain;


public class Fire {
private HashSet<Flame> flames;
	Block block;
	private DCGameEntity entity;
	public Fire(Block block, DCGameEntity entity){
		this.block = block;
        setEntity(entity);
        this.setFlames(new HashSet<Flame>());
        update(false);
        this.alive = true;

	
	}
	
	public Fire(Block block, DCGameEntity splinter,HashSet<Flame>flames){
		this.block = block;
        setEntity(splinter);
        setFlames(flames);
		
		
		
	
		
		 for(Flame flame:flames){	
			 
	
	
			 splinter.addHalfChild(flame.getFire());
	
			
			 }

        update(true);

	
	}
	
	

public void step(){
	if(!getEntity().hasParent())for(Flame f: this.getFlames()){
	
		f.getFire().detachSelf();}
	for(Flame f: this.getFlames())f.step();

	

}
	
public void update(boolean direct){

	//Grain[][] array = block.grid.generateTemperatureArray();
	
ArrayList<Flame> newflames = new ArrayList<Flame>();
	
	for( Grain g: this.block.grid.allGrains)
		if(g.onFire&&g.isBorder){
			HashSet<Grain> grains = new HashSet<Grain>();
			boolean ok = true;
			for(Flame f: this.flames)if(f.grains.contains(g)){ok = false;break;}
			grains.add(g);
			if(ok)newflames.add(new Flame(grains, 1, this.entity));
		}

    this.getFlames().addAll(newflames);
	 


		
	 for(Flame flame:newflames){
         this.getEntity().addHalfChild(flame.getFire());
		 GameScene.world.attachChild(flame.getFire());
		
			if(direct)for(int n=0;n<60;n++)flame.getFire().onUpdate(1/60f);
		 }
	

}
public Fire extractFire(Block splinterBlock, DCGameEntity splinter){
	HashSet<Flame> flames = new HashSet<>();
	
	for(Flame f: getFlames()){
		boolean ok = true;
		for(Grain g:f.grains){
			if(!splinterBlock.grid.allGrains.contains(g)){

				ok = false;
				break;
			}
		
		}
		if(ok)flames.add(f);
	}

    getFlames().removeAll(flames);
Fire result= new Fire(splinterBlock, splinter,flames); 

		return result;
	
}
	
public boolean hasIdentical(HashSet<Grain> base){
	for(Flame f: this.getFlames())if(f.isIdentical(base))return true;
	return false;
}


public DCGameEntity getEntity() {
	return this.entity;
}


public void setEntity(DCGameEntity entity) {
	this.entity = entity;
}
public boolean alive;
int count;
boolean counter;
public void setDead() {
    this.count = 0;
    this.counter = true;
	
}

public HashSet<Flame> getFlames() {
	return this.flames;
}

public void setFlames(HashSet<Flame> flames) {
	this.flames = flames;
}

public void setVisible(boolean b) {
for(Flame f: this.flames)f.getFire().setVisible(b);
	
}

}
