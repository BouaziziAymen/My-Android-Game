package is.kul.learningandengine.polygonarithmetic;

import com.badlogic.gdx.math.Vector2;

public class Compass  {
    public Vector2 point;
    
	private boolean active = true;
	public boolean POSITIVE_YANG_OPEN;
	public boolean POSITIVE_YIN_OPEN;
	public boolean NEGATIVE_YANG_OPEN;
	public boolean NEGATIVE_YIN_OPEN;

	public Position POSITION;

	public float yangDistance;
	public float yinDistance;
	
	
    Compass(Vector2 p, boolean pyo,boolean pyio,boolean nyo, boolean nyio,Position position, float yangDistance, Float yinDistance){
this.yangDistance= yangDistance;
this.yinDistance= yinDistance;
    	this.point = p;
    	POSITIVE_YANG_OPEN = pyo;
    	POSITIVE_YIN_OPEN = pyio;
    	NEGATIVE_YANG_OPEN = nyo;
    	NEGATIVE_YIN_OPEN = nyio;
    	this.POSITION = position;
    }

   public String toString(){
	   String string =
	 "("+point.toString()
	 +":yang<"+NEGATIVE_YANG_OPEN+","+POSITIVE_YANG_OPEN+ ">"
	 +":yin<"+NEGATIVE_YIN_OPEN+","+POSITIVE_YIN_OPEN+ ">";
	   
   return string;
   }

public boolean isActive() {
	// TODO Auto-generated method stub
	return active;
}

public void desactivate() {
	active = false;
	
}



   
    
}
