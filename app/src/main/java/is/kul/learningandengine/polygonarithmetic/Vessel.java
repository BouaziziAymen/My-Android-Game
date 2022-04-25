package is.kul.learningandengine.polygonarithmetic;

import org.andengine.entity.Entity;
import com.badlogic.gdx.math.Vector2;

public class Vessel {
enum POSITIONTYPE{
	INPOINT,INCOMPASS;
}
POSITIONTYPE posType;
POLARITY polarity;
DIRECTION direction;
Compass start, last;
int r;

public Vector2 Position;
private int max1;
private int max2;
Cutter cutter;
public Vessel(Compass start, Cutter cutter){
	this.cutter = cutter;
	this.start = start;
    this.polarity = POLARITY.YIN;
	takeCompass(start);
	this.Position = start.point;
	max1 = cutter.YANGS.size();
	max2 = cutter.YINS.size();
		
	this.posType=POSITIONTYPE.INPOINT;
	
	while(enabled)
		this.update();
}
boolean ok = true;
void update(){
if(this.posType==POSITIONTYPE.INCOMPASS){
	this.posType = POSITIONTYPE.INPOINT;
	this.Position =  last.point;
	cutter.addPoint(Position);
	if(last==start){enabled = false;return;}
	
} else{
	

	
	Compass newCompass = cutter.isAtCompass(polarity, direction, r, last);
	if(newCompass!=null){

	if(this.polarity==POLARITY.YANG)Position = cutter.YANGS.get(r);
	else if (this.polarity==POLARITY.YIN)Position = cutter.YINS.get(r);
	takeCompass(cutter.isAtCompass(polarity, direction, r, last));	
	cutter.addPoint(Position);
	return;
	
	}
	
	if(this.polarity==POLARITY.YANG){
		Position = cutter.YANGS.get(r);
		cutter.addPoint(Position);
		if(direction ==DIRECTION.POSITIVE)
		r= (r==max1-1)?0:r+1;
		else 
		r= (r==0)?max1-1:r-1;	
			
		
	} else 
	if(this.polarity==POLARITY.YIN){
		Position = cutter.YINS.get(r);
		cutter.addPoint(Position);
		if(direction ==DIRECTION.POSITIVE)
		r= (r==max2-1)?0:r+1;
		else 
		r= (r==0)?max2-1:r-1;	
	}
	
}
	
	
}


void takeCompass(Compass compass){
	if(compass==null)return;
	
	
	compass.desactivate();
	last = compass;
	posType = POSITIONTYPE.INCOMPASS;
	if(polarity ==POLARITY.YANG){
	this.polarity = POLARITY.YIN;	
	if(compass.POSITIVE_YIN_OPEN){
		this.direction = DIRECTION.POSITIVE;
		r= compass.POSITION.EAST;
	} else 
	if(compass.NEGATIVE_YIN_OPEN){
		this.direction = DIRECTION.NEGATIVE;
		r= compass.POSITION.WEST;
	}
	
		
	} else 
	
	if(polarity==POLARITY.YIN){
	this.polarity = POLARITY.YANG;
	if(compass.POSITIVE_YANG_OPEN){
		this.direction = DIRECTION.POSITIVE;
		r= compass.POSITION.NORTH;
	} else 
	if(compass.NEGATIVE_YANG_OPEN){
		this.direction = DIRECTION.NEGATIVE;
		r= compass.POSITION.SOUTH;
	}
		
	}

}
public boolean enabled = true;



}
