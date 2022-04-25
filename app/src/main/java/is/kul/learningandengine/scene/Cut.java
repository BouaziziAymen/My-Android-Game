package is.kul.learningandengine.scene;



import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import is.kul.learningandengine.basics.DCGameEntity;
public class Cut implements Comparable<Cut> {
    public int ip2,iq2;
    DCGameEntity entity;
public int blockId;
Vector2 p1,p2;
boolean dead;
public Vector2 x1a,x1b;
public Vector2 x2a,x2b;
private boolean half;
private float value;
	public DCGameEntity getEntity(){return entity;}
public ArrayList<Vector2> inners;
public Cut(Vector2 p1, Vector2 p2,int id){
	this.p1 = p1;
	this.p2 = p2;

    this.setValue(p1.dst(p2));
	if(this.getValue()<1f) setDead(true);
    blockId = id;
    this.inners = new  ArrayList<Vector2> ();

}


public String toString(){
	return this.p1 +"/"+ this.p2 +"ishalf"+half;
}
public Vector2 getP1(){
	return this.p1;
}

public Vector2 getP2(){
	return this.p2;
}

public int getId() {
	return this.blockId;
}

public boolean isDead() {
	// TODO Auto-generated method stub
	return this.dead;
}

public void setDead(boolean b) {
	// TODO Auto-generated method stub
    dead = b;
}


@Override
public int compareTo(Cut another) {
	if(getValue()<another.getValue())return -1; else return 1;
	
}


public float getValue() {
	return this.value;
}


public void setValue(float value) {
	this.value = value;
}


    public boolean isHalf() {
        return half;
    }

    public void setHalf(boolean half) {
        this.half = half;
    }
}
