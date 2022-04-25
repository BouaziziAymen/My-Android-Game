package is.kul.learningandengine.scene;

import is.kul.learningandengine.basics.DCGameEntity;



import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.particlesystems.FireParticleSystem;

import java.util.HashSet;

import android.util.Log;

public class Flame {
	
private FireParticleSystem fire;
HashSet<Grain> grains;
public boolean visible;
int changeStateCount;
private boolean stateCount;
float getAverageTemp(){
	float average = 0;
	for(Grain g: this.grains)average+=g.temperature;
	return average/ this.grains.size();
}

public void step() {	

	//fire.setVisible(false);
    this.getFire().updateSpeed();

    this.getFire().updateTemp(getAverageTemp(),false);
	
	if(this.stateCount) this.changeStateCount++;
	if(this.changeStateCount >30){
        this.stateCount = false;
        this.changeStateCount = 0;
        changeState();
	}
	
}
public boolean isIdentical(HashSet<Grain> base){
	if(base.size()!= this.grains.size())return false;
    return this.grains.containsAll(base) && base.containsAll(this.grains);
}

public Flame(HashSet<Grain> group,int size, DCGameEntity entity) {
    this.grains = group;
    this.visible = true;

float x = 0,y = 0;
float minx = Float.MAX_VALUE,miny = Float.MAX_VALUE,maxx = Float.MIN_VALUE,maxy = Float.MIN_VALUE;
for(Grain g:group){
	if(g.position.x<minx)minx=g.position.x;
	if(g.position.y<miny)miny=g.position.y;
	if(g.position.x>maxx)maxx=g.position.x;
	if(g.position.y>maxy)maxy=g.position.y;

}
x = minx + (maxx-minx)/2;
y = miny + (maxy-miny)/2;
float average = 0;
for(Grain g:group)average+=g.temperature;
average /= group.size();
    this.setFire(new FireParticleSystem(x,y,0, size, entity, average));
    this.getFire().setZIndex(999999999);

}
public boolean isVisible(){
	return this.visible;
}
public boolean isParentOf(Flame candidate){
    return grains.containsAll(candidate.grains) && !candidate.grains.containsAll(grains);
}

public boolean isChildOf(Flame candidate){
    return candidate.grains.containsAll(grains) && !grains.containsAll(candidate.grains);
}



public void setAsleep(boolean asleep){
    this.visible = !asleep;
    this.stateCount = true;
	
}
public void changeState(){
if(!visible){

    getFire().setVisible(false);
    getFire().setIgnoreUpdate(true);
		
	
	} else {

    getFire().setVisible(true);
    getFire().setIgnoreUpdate(false);
		
	}

}



public boolean test() {
	for(Grain g: this.grains)if(!g.onFire)return false;
	return true;
}

public FireParticleSystem getFire() {
	return this.fire;
}

public void setFire(FireParticleSystem fire) {
	this.fire = fire;
}
}
