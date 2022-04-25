package is.kul.learningandengine.graphicelements;

import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;


public class PrismaticPoint extends JointPoint {

	private final DSquare outer;
	private final DSquare inner;
DVector vector;

PrismaticLimitDPoint limit1, limit2;

boolean main;
boolean isAlongAxis(Vector2 v){
    return this.getAxe().dot(v)>=0;
}
public float getUpperLimit(){
	return this.limit1.toVector().len();
}
public float getLowerLimit(){
    return (isAlongAxis(limit2.toVector()))?this.limit2.toVector().len():-this.limit2.toVector().len();
}
public boolean isFirstLimit(PrismaticLimitDPoint limit){
    return limit==limit1;
}
    public boolean isSecondLimit(PrismaticLimitDPoint limit){
        return limit==limit2;
    }

public void onUpdateFirstLimitPosition(){
    float distance = limit2.toVector().len();

    Vector2 u = limit1.toVector().nor().mul((limit1.toVector().dot(limit2.toVector())<0)?-distance:distance);

    limit2.setPosition(u.x, u.y);
    updateVector();
}
public void onUpdateSecondLimitPosition(float dL){

    if(limit1.toVector().dot(limit2.toVector())>0) {//SAME SIDE
        float L1 = limit1.toVector().len();
        float L2 = limit2.toVector().len();

        if (L2-dL > L1) {
            dL = L2 - L1;
        }
    }
    Vector2 u = limit1.toVector().nor().mul(-dL);
    limit2.setX(limit2.getX()+ u.x);
    limit2.setY(limit2.getY()+ u.y);
    updateVector();

}
public void update(JointIndicator ind){

    if(ind.isSelected()&&ind.hasLimits()){
        vector.setVisible(true);
    } else vector.setVisible(false);
}


public void updateVector(){

    this.vector.update(this.limit2.toVector(), this.limit1.toVector());
	((JointIndicator) getParent()).updatePrismatic();

}

	public PrismaticPoint(float x, float y, float scale) {
		super(x, y);
        attachChild(this.outer = new DSquare(0,0,1,DFactory.DrawingType.TYPEJOINT1));
        attachChild(this.inner =new DSquare(0,0,1,DFactory.DrawingType.TYPEJOINT2));
        this.main = false;
        scale(scale);
	}

	public PrismaticPoint(float x, float y, float scale,boolean b) {
		super(x, y);
        this.main = true;
        attachChild(this.outer = new DSquare(0,0,1,DFactory.DrawingType.TYPEJOINT1));
        attachChild(this.inner =new DSquare(0,0,1,DFactory.DrawingType.TYPEJOINT2));

        attachChild(this.vector =new DVector(new Vector2(0,-20), new Vector2(0,20),Color.PINK,3));
        vector.attachChild(this.limit1 = new PrismaticLimitDPoint(0,20,1));
        vector.attachChild(this.limit2 =new PrismaticLimitDPoint(0,-20,1));
scale(scale);


	}

	

	@Override
	public void setRed(){
        this.inner.setRed();
        this.outer.setRed();
}

@Override
public void setBlue() {
    this.inner.setBlue();
    this.outer.setBlue();
	
}

@Override
public void setGreen() {
    this.inner.setGreen();
    this.outer.setGreen();
	
}
@Override
void scale(float scale) {
    inner.setScale(1/scale);
    outer.setScale(1/scale);
    if(main) {
        limit1.setScale(1 / scale);
        limit2.setScale(1 / scale);
        vector.scale(scale);
    }
}
public Vector2 getAxe() {
	Vector2 v = limit1.toVector().sub(this.limit2.toVector()).nor();
	return  v;
}

}
