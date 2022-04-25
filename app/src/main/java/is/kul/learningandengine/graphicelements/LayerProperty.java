package is.kul.learningandengine.graphicelements;

import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.factory.MaterialFactory;

public class LayerProperty {
    CollisionOption collisionOption;
float brittleness = 0.1f;
float tenacity = 500;
public String layerName;	
private int MATERIAL_NUMBER;
public int ID;
private float[] properties;
private Color color;
public float thickness = 1;
public float getBrittleness(){
	return this.brittleness;
}
public float getTenacity(){
	return this.tenacity;
}
public float getDensity(){
	return this.getProperties()[0];
}
public float getRestitution(){
	return this.getProperties()[1];
}
public float getFriction(){
	return this.getProperties()[2];
}
public float getHardness(){return this.getProperties()[3]; }


public LayerProperty(int n, String name, int ID){
    collisionOption = new CollisionOption();
    setProperties(new float[4]);
    layerName = name;
	this.ID = ID;
    color = new Color(MaterialFactory.getInstance().materials.get(n).color);
    this.getProperties()[0] = MaterialFactory.getInstance().materials.get(n).DENSITY;
    this.getProperties()[1] = MaterialFactory.getInstance().materials.get(n).RESTITUTION;
    this.getProperties()[2] = MaterialFactory.getInstance().materials.get(n).FRICTION;
    this.getProperties()[3] = MaterialFactory.getInstance().materials.get(n).HARDNESS;
}

public LayerProperty(int ID) {
    collisionOption = new CollisionOption();
    setProperties(new float[4]);
	this.ID = ID;
    this.getProperties()[0] = MaterialFactory.getInstance().materials.get(ID).DENSITY;
    this.getProperties()[1] = MaterialFactory.getInstance().materials.get(ID).RESTITUTION;
    this.getProperties()[2] = MaterialFactory.getInstance().materials.get(ID).FRICTION;
    this.getProperties()[3] = MaterialFactory.getInstance().materials.get(ID).HARDNESS;
}

public int getMaterialNumber(){
	return this.MATERIAL_NUMBER;
}
public Color getColor() {
	return this.color;
}
public void setColor(Color color) {
	this.color = color;
}

public float[] getProperties() {
	return this.properties;
}
public void setProperties(float[] properties) {this.properties = properties;}


    public CollisionOption getCollisionOptions() {
        return collisionOption;
    }

    public short[] getCollisionList() {
        return this.getCollisionOptions().getCollisionList();
    }

    public short getCatergory() {
        return this.getCollisionOptions().getCategory();
    }

    public short getOrder() {
        return this.getCollisionOptions().getOrder();
    }
}
