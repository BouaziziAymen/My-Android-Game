package is.kul.learningandengine.scene;

import java.util.ArrayList;
import java.util.Arrays;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.Utils;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class Slash extends Entity {

public Slash(Vector2 start){
    P0 = start;
    this.line = new Line(start.x, start.y, start.x, start.y,1, ResourceManager.getInstance().vbom);
    attachChild(this.line);
    this.points = new ArrayList<Vector2>();
    this.points.add(this.P0);

    this.PP = start;
    this.tx = start.x;
    this.ty = start.y;

}

ArrayList<Vector2> points;
Line line;
float time;
float tx, ty;

Vector2 P, PP,P0;
public void updateTouch(TouchEvent event){

    tx = event.getX();
    ty = event.getY();
if(event.isActionUp()){
    this.points.add(new Vector2(this.tx, this.ty));
	//Log.e("points", Arrays.toString(points.toArray()));
}


}

ArrayList<Vector2> getPoints(){
	for(Vector2 p: this.points)p.mul(1/32f);
	return this.points;
}

@Override
protected void onManagedUpdate(float pSecondsElapsed) {
super.onManagedUpdate(pSecondsElapsed);
    this.time += pSecondsElapsed;
if(this.time >1/60f){
    step();
    this.time = this.time - 1/60f;
}
}



void step(){


    this.P = new Vector2(this.tx, this.ty);

    this.line.setPosition(this.P0.x, this.P0.y, this.tx, this.ty);


}
}
