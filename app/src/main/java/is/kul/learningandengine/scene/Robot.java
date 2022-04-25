package is.kul.learningandengine.scene;

import is.kul.learningandengine.helpers.Utils;

import com.badlogic.gdx.math.Vector2;

public class Robot {
float x,y;
float minx,maxx;
float miny,maxy;

private void changeSpeed(){
    this.x = this.minx + (float)Math.random()*(this.maxx - this.minx);
    this.y = this.miny + (float)Math.random()*(this.maxy - this.miny);
}
public Robot(float minx, float maxx, float miny, float maxy){

this.minx = minx;
this.maxx = maxx;
this.miny = miny;
this.maxy = maxy;
    this.changeSpeed();


}
public void update(){
    this.changeSpeed();
}

}
