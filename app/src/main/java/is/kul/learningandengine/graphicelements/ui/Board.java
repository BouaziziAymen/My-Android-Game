package is.kul.learningandengine.graphicelements.ui;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import com.badlogic.gdx.math.Vector2;

public class Board extends Entity {
	public enum Type {
		Horizental,
		Vertical
	}
	Entity[] entities;
	
	
	Vector2 getElementPosition(int N){
		return new Vector2(this.entities[N].getX(), this.entities[N].getY());
	}
	public Board(float x, float y,int margin, Board.Type type,IEntity... entities ){
    super(x,y);


    this.entities = (Entity[]) entities;

    float width=-margin;
    float height = 0;
		if(type ==Board.Type.Horizental){
			
		float L = 0;	
	
		for(int i=0;i<entities.length;i++){
			width+=entities[i].getWidth()+margin;
		float l = i==0||i==entities.length-1 ?entities[i].getWidth()/2:entities[i].getWidth();
			L+=l;
		}
		L += (entities.length-1)*margin;
		height = entities[0].getHeight();
	
		float d = -L/2;
		for(int i=0;i<entities.length;i++){
			entities[i].setPosition(d+width/2, height/2);
            attachChild(entities[i]);
		if(i<entities.length-1)	 d += entities[i].getWidth()/2+entities[i+1].getWidth()/2+margin;
		}
		
			
		
		
	} else {
		
		float d = 0;	
		
		for(int i=0;i<entities.length;i++)
			height+=entities[i].getHeight()+margin;
		
		d = height/2-entities[0].getHeight()/2;
		
		
		for(int i=0;i<entities.length;i++){
			entities[i].setPosition(0, d+height/2);
            attachChild(entities[i]);
		if(i<entities.length)	 d -= entities[i].getHeight()+margin;
		}
			
		
		
		
	}
        setWidth(width);
        setHeight(height);
	
	}
}
