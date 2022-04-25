package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;


public class Slice{
private cutElement first;
private cutElement second;
private Vector2 p1;
private Vector2 p2;
public Fixture fixture;
public Slice(cutElement first, cutElement second, Fixture fixture) {
    setFirst(first);
    setSecond(second);
    setFixture(fixture);
    this.setP1(first.getPoint());
    this.setP2(second.getPoint());
}



public String toString(){
return "["+ this.getFirst()+"|"+ this.getSecond()+"]";
}



public cutElement getFirst() {
	return this.first;
}



public void setFirst(cutElement first) {
	this.first = first;
}



public cutElement getSecond() {
	return this.second;
}



public void setSecond(cutElement second) {
	this.second = second;
}



public Vector2 getP1() {
	return this.p1;
}



public void setP1(Vector2 p1) {
	this.p1 = p1;
}



public Vector2 getP2() {
	return this.p2;
}



public void setP2(Vector2 p2) {
	this.p2 = p2;
}



public Fixture getFixture() {
	return this.fixture;
}



public void setFixture(Fixture fixture) {
	this.fixture = fixture;
}

}