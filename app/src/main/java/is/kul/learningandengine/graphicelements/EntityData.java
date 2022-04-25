package is.kul.learningandengine.graphicelements;

import java.util.ArrayList;

import org.andengine.entity.Entity;

public class EntityData {
public ArrayList<Entity> entities;
public int[] indexes;
public EntityData(ArrayList<Entity> entities, int[] indexes){
	this.entities = entities;
	this.indexes = indexes;
}

}
