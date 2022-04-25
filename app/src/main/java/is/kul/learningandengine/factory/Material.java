package is.kul.learningandengine.factory;

import org.andengine.util.adt.color.Color;

public class Material {
public float DENSITY;
public float FRICTION;
public float RESTITUTION;
	public float HARDNESS;
public Color color;
public String name;
public int INDEX;
public Material(String name, float density, float friction, float restitution, Color color, int index, float hardness){
    DENSITY =  density * 4;
    FRICTION = friction;
    RESTITUTION = restitution;
	this.color = color;
	this.name = name;
    INDEX = index;
    HARDNESS = hardness;

}
}
