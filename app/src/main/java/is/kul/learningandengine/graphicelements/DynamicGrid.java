package is.kul.learningandengine.graphicelements;

import java.util.ArrayList;

import is.kul.learningandengine.GameActivity;


import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

import android.graphics.Point;
import android.util.Log;



public class DynamicGrid extends Entity{
	public ArrayList<Point> points;
	public DynamicGrid(VertexBufferObjectManager vbom){
		super();
			
		
		int N = GameActivity.CAMERA_WIDTH/2;
		
int M = GameActivity.CAMERA_HEIGHT/2;


        this.points = new ArrayList<Point>();
			for(int order1=0;order1<=N;order1++){
				int x = order1 * 2;
				for(int order2=0;order2<=M;order2++){
				int y = order2 * 2;
                    this.points.add(new Point(x,y));
				}
			}
			for(int order=0;order<=N;order++){
		
				
			float x = 2*order;
					
			Line line = new Line(x, 0, x, GameActivity.CAMERA_HEIGHT, this.getThickness( order), vbom);
			line.setTag(this.getScale(order));
			line.setColor(this.getColor(order));

                attachChild(line);
				
		}
			
		

			
			for(int order=0;order<=M;order++){
		
				
				
				float y = 2*order;
					
			Line line = new Line(0, y, GameActivity.CAMERA_WIDTH, y, this.getThickness( order), vbom);
			line.setTag(this.getScale(order));
			line.setColor(this.getColor(order));

                attachChild(line);
				
			}


        update(1, 0, GameActivity.CAMERA_WIDTH, 0, GameActivity.CAMERA_HEIGHT);
		
	}
	int getScale(int order){
		if(order%16==0)return 1;
		if(order%8==0)return 2;
		if(order%4==0)return 4;
		if(order%2==0)return 8;
		else return 16;
	}
	
	public int getK(float scale){
		if(scale>=16)return 2;
		if(scale>8)return 4;
		if(scale>4)return 8;
		if(scale>2)return 16;
		else return 32;
	}

	private Color getColor(int order){
		if(order%16==0)return new Color(1f,1f,1f);
		if(order%8==0)return new Color(0f,1f,1f);
		if(order%4==0)return new Color(1f,0f,1f);
		if(order%2==0)return new Color(1,1f,0f);
		else return new Color(1f,0f,0f);
	}
	private float getThickness(int order){
		if(order%16==0)return 5f;
		if(order%8==0)return 4;
		if(order%4==0)return 3;
		if(order%2==0)return 2;
		else return 1;
		}
		float scale = 1;
	public float getScale(){
		return scale;
	}
public void update(float scale,float xmin, float xmax, float ymin, float ymax){
	this.scale = scale;
for(IEntity entity: mChildren){
	if(entity instanceof Line){
	Line line= 	(Line)entity;
	if(line.getX1()==line.getX2()){
		if(line.getX1()>xmax||line.getX1()<xmin)line.setVisible(false);else line.setVisible(true);
	}
	
	if(line.getY1()==line.getY2()){
		if(line.getY1()>ymax||line.getY1()<ymin)line.setVisible(false);else line.setVisible(true);
	}
	
	if(scale>=line.getTag())line.setVisible(true);else line.setVisible(false);
	}
}
}


}
