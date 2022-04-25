package is.kul.learningandengine.graphicelements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.helpers.Utils;

public class RevolutePoint extends JointPoint {
DPoint inner,outer;
    RevoluteLimitDPoint limit1,limit2,reference;
    DVector vector1, vector2,refVector;
    DAngle angle;
	boolean main;
	boolean hasLimits;
	public RevolutePoint(float x, float y, float scale) {
		super(x,y);

        attachChild(this.outer = new DPoint(0,0,1,0,DFactory.DrawingType.TYPEJOINT1));
        attachChild(this.inner =new DPoint(0,0,1,0,DFactory.DrawingType.TYPEJOINT2));
        this.scale(scale);
	}


    public void update(JointIndicator ind){
        if(ind.isSelected()&&ind.hasLimits()){
            angle.setVisible(true);
            vector1.setVisible(true);
            vector2.setVisible(true);
            refVector.setVisible(true);
        } else {
            angle.setVisible(false);
            vector1.setVisible(false);
            vector2.setVisible(false);
            refVector.setVisible(false);
        }
    }

    public RevolutePoint(float x, float y, float scale,boolean flag) {
        super(x,y);
        main = true;
        attachChild(this.outer = new DPoint(0,0,1,0,DFactory.DrawingType.TYPEJOINT1));
        attachChild(this.inner =new DPoint(0,0,1,0,DFactory.DrawingType.TYPEJOINT2));
        attachChild(this.vector1 =new DVector(new Vector2(), new Vector2(20,0),Color.BLUE,3));
        attachChild(this.vector2 =new DVector(new Vector2(), new Vector2(30,0),Color.RED,3));
        attachChild(this.refVector =new DVector(new Vector2(),new Vector2(60,0),Color.WHITE,3));
        vector1.attachChild(this.limit1 = new RevoluteLimitDPoint(20,0,1));
        vector2.attachChild(this.limit2 = new RevoluteLimitDPoint(30,0,1));
        refVector.attachChild(this.reference =new RevoluteLimitDPoint(60,0,1));
        angle = new DAngle(0,0,0,0,16,16);
        this.attachChild(angle);
        this.scale(scale);
    }
    public boolean isFirstLimit(RevoluteLimitDPoint limit){
        return limit==limit1;
    }
    public boolean isSecondLimit(RevoluteLimitDPoint limit){
        return limit==limit2;
    }
    public boolean isReferenceLimit(RevoluteLimitDPoint limit){
        return limit==reference;
    }
    float getLowerLimit(){
        return (float)Math.toRadians(getLimit1Angle());
    }
    float getUpperLimit(){ return (float)Math.toRadians(getLimit2Angle()); }
    float getReferenceAngle(){
        return refVector.getRotation();
    }

float getLimit1Angle(){
        return vector1.getRotation()-refVector.getRotation();
    }
float getLimit2Angle(){
        return vector2.getRotation()-refVector.getRotation();
    }

void updateParent(){
    ((JointIndicator)this.getParent()).updateRevolute();
}


    void onUpdateReferenceLimitPosition(float dA){
        float rotRef = refVector.getRotation()-dA;
        refVector.setRotation(rotRef);
        onUpdateFirstLimitPosition(dA);
        onUpdateSecondLimitPosition(dA);
    }
    void onUpdateSecondLimitPosition(float dA){
        float rotUpper = vector2.getRotation();
        float rotLower = vector1.getRotation();
        if(rotUpper-dA<rotLower){dA = rotUpper - rotLower;}
        vector2.setRotation(rotUpper-dA);
        angle.update(vector1.getRotation(),vector2.getRotation());
        updateParent();
    }
    void onUpdateFirstLimitPosition(float dA){
        float rotLower = vector1.getRotation();
        float rotUpper = vector2.getRotation();
        if(rotLower-dA>rotUpper){dA = rotLower-rotUpper;}
        vector1.setRotation(rotLower-dA);
        angle.update(vector1.getRotation(),vector2.getRotation());
        updateParent();
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
    this.setScale(1/scale);

}




}
