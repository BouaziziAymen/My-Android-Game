package is.kul.learningandengine.graphicelements;

import org.andengine.entity.Entity;

import com.badlogic.gdx.math.Vector2;

import is.kul.learningandengine.graphicelements.ui.Coordinated;

public abstract class DElement extends Entity implements Coordinated{
	


public boolean active;
public boolean dead;
abstract void scale(float scale);



	public DElement(float x, float y){
		super(x,y);

        this.active = true;
	}

	public abstract void setBlue();
	public abstract void setGreen();
	public abstract void setRed();

	public void updatePosition(float dX, float dY) {
        setPosition(getX()+dX,getY()+dY);
setModified(true);
	}

	public Vector2 toVector() {
		return new Vector2(getX(), getY());
	}

	public void resetColor() {
        setGreen();
		
	}
	boolean modified;
	@Override
	public void setModified(boolean m){
		 modified = m;
	}
	@Override
	public boolean isModified(){
		return modified;
	}
	@Override
	public String getCoordinates(){
		return "x: "+String.format("%.1f", getX())+",y: "+String.format("%.1f", getY());
	}
}
