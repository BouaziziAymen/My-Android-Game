package is.kul.learningandengine.factory;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class VerticesFactory {
	 
	   
	
	
	public static ArrayList<Vector2> createPolygon(int n,float teta0, float w, float h,float x, float y){

		float teta = teta0;
		ArrayList<Vector2> points = new ArrayList<Vector2>();
		
		for(int i=0;i<n;i++){
		Vector2 v = new Vector2(x+ w*(float)Math.cos(teta),y+h*(float)Math.sin(teta));
	    teta+=2*Math.PI/n;
	    points.add(v);
		}
		return points;	

	}


	
	
	
public static ArrayList<Vector2> createRectangle( float w, float h){
	
float hw = w/2;
float hh = h/2;
ArrayList<Vector2> list = new ArrayList<Vector2>();
list.add(new Vector2( -hw, -hh));
list.add(new Vector2( +hw, -hh));
list.add(new Vector2( +hw,+hh));
list.add(new Vector2(-hw,+hh));



return list;
}



public static ArrayList<Vector2>  createShape4( float H1, float H2, float R1, float R2){
	ArrayList<Vector2> list = new ArrayList<Vector2>();

float H = H1 + H2;
    float y1 =  H/2;
    float y2 = -H/2;
    float y3 = -H/2 + H2;
	float teta = 0;
	for(int i=0;i<5;i++){


		list.add(new Vector2( R1*(float)Math.cos(teta),y1+R1*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	list.add(new Vector2( -R1,y3));
	
	
	for(int i=0;i<5;i++){


		list.add(new Vector2( R2*(float)Math.cos(teta),y2+R2*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	list.add(new Vector2( R1,y3));

return list;
}









public static ArrayList<Vector2>  createShape5( float H1, float H2, float L, float R1, float R2){
	ArrayList<Vector2> list = new ArrayList<Vector2>();

float H = H1 + H2;
    float y1 =  H/2;
    float y2 = -H/2;
    float y3 = -H/2 + H2;
	float teta = 0;
	for(int i=0;i<5;i++){


		list.add(new Vector2( R1*(float)Math.cos(teta),y1+R1*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	list.add(new Vector2( -L,y3));
	
	
	for(int i=0;i<5;i++){


		list.add(new Vector2( R2*(float)Math.cos(teta),y2+R2*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	list.add(new Vector2( L,y3));

return list;
}







public static ArrayList<Vector2>  createShape1( float H, float R1, float R2){
	ArrayList<Vector2> list = new ArrayList<Vector2>();


    float y1 = H/2;
    float y2 = -H/2;
	float teta = 0;
	for(int i=0;i<5;i++){


		list.add(new Vector2( R1*(float)Math.cos(teta),y1+R1*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	
	for(int i=0;i<5;i++){


		list.add(new Vector2( R2*(float)Math.cos(teta),y2+R2*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}

return list;
}

public static ArrayList<Vector2>  createShape2( float H, float R1, float R2){
	ArrayList<Vector2> list = new ArrayList<Vector2>();


    float y1 = H/2;
    float y2 = -H/2;
	float teta = 0;
	for(int i=0;i<5;i++){


		list.add(new Vector2( R1*(float)Math.cos(teta),y1+R1*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}
	
	list.add(new Vector2( -R2,y2));
	list.add(new Vector2( 0,y2-R2));
	list.add(new Vector2( R2,y2));

return list;
}



public static List<Vector2> createRectangleTriangles( float w, float h,float x, float y){
	
float hw = w/2;
float hh = h/2;
List<Vector2> list = new ArrayList<Vector2>();
list.add(new Vector2( -hw, -hh).add(x, y));
list.add(new Vector2( +hw, -hh).add(x, y));
list.add(new Vector2( +hw,+hh).add(x, y));
list.add(new Vector2(-hw,+hh).add(x, y));
return list;
}


public static ArrayList<Vector2> createDistorted( float w1,float w2, float h,float x, float y){
	
float hw1 = w1/2;
float hw2 = w2/2;
float hh = h/2;
ArrayList<Vector2> list = new ArrayList<Vector2>();
list.add(new Vector2( -hw2, -hh).add(x, y));
list.add(new Vector2( +hw2, -hh).add(x, y));
list.add(new Vector2( +hw1,+hh).add(x, y));
list.add(new Vector2(-hw1,+hh).add(x, y));
return list;
}


public static ArrayList<ArrayList<Vector2>> createDistorted2( float w1,float w2, float h,float x, float y, float ratio){
	
	ArrayList<ArrayList<Vector2>> list = new 	ArrayList<ArrayList<Vector2>>();
float gamma = ratio * (-w1+w2)/2;
float w3 = w1+2*gamma;
ArrayList<Vector2> list1 = VerticesFactory.createDistorted(  w1, w3,  h*ratio, x,  y+h*(1-ratio)/2);
ArrayList<Vector2> list2 = VerticesFactory.createDistorted(  w3, w2,  h*(1-ratio), x,  y-h*ratio/2);
list.add(list1);
list.add(list2);

return list;
}


public static ArrayList<Vector2> createPoly() {
	ArrayList<Vector2> list = new ArrayList<Vector2>();
	list.add(new Vector2( 0,0));
	list.add(new Vector2( 100, 100));
	list.add(new Vector2( -200,100));
	list.add(new Vector2(-300,50));
	list.add(new Vector2(-200,0));

	return list;
}
public static ArrayList<Vector2> createPoly2() {
	ArrayList<Vector2> list = new ArrayList<Vector2>();

	list.add(new Vector2(100,25));
	list.add(new Vector2( 25,25));
	list.add(new Vector2( 25,75));
	list.add(new Vector2( 100,75));
	list.add(new Vector2(100,100));
	list.add(new Vector2(0,100));
	list.add(new Vector2(0,0));
	list.add(new Vector2( 100,0));
	return list;
}


public static ArrayList<Vector2> createPolygon(float x,float y,float w, float h,int n){

ArrayList<Vector2> list = new ArrayList<Vector2>();

	float teta = 0;
	for(int i=0;i<n;i++){


		list.add(new Vector2(x+ w*(float)Math.cos(teta),y+h*(float)Math.sin(teta)));
teta+=2*Math.PI/n;

	}
	return list;
}

	public static ArrayList<Vector2> createEgg(float x,float y,float l, float a, float b,int n){

		ArrayList<Vector2> list = new ArrayList<Vector2>();

		float teta = 0;
		for(int i=0;i<n;i++){

float cos = (float)Math.cos(teta);
float sin = (float)Math.sin(teta);
			list.add(new Vector2(y+(a+b*cos)*sin,x+ l*cos+(a+b*cos)*cos));
			teta+=2*Math.PI/n;

		}
		return list;
	}



public static ArrayList<Vector2> createShape6(float R, float h){

ArrayList<Vector2> list = new ArrayList<Vector2>();

	float teta = (float) (-Math.PI/2);
	for(int i=0;i<5;i++){


		list.add(new Vector2(R*(float)Math.cos(teta),R*(float)Math.sin(teta)));
teta+=Math.PI/5;

	}

	list.add(new Vector2(-h,R));
	
	return list;
}





public static ArrayList<Vector2> createSquare(float side) {
	ArrayList<Vector2> list = new ArrayList<Vector2>();
float hside = side/2;
	
	
	
	list.add(new Vector2(-hside,hside));
	list.add(new Vector2(-hside,-hside));
	list.add(new Vector2(hside,-hside));
	list.add(new Vector2(hside,hside));
	return list;
}
}
