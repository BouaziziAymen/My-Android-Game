package is.kul.learningandengine.graphicelements;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.ui.JointProperty;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class JointIndicator extends Entity {
public JointPoint begin;
public JointPoint end;

public DElement[] getPrismaticMain(){
	PrismaticPoint point;
	if(((PrismaticPoint) this.begin).main)point= (PrismaticPoint) this.begin;else point = (PrismaticPoint) this.end;
	 return new DElement[]{point.limit1,point.limit2};
}

        public DElement[] getRevoluteMain(){
            RevolutePoint point;
        if(((RevolutePoint) this.begin).main)point= (RevolutePoint) this.begin;else point = (RevolutePoint) this.end;
        return new DElement[]{point.limit1,point.limit2,point.reference};
    }
float scale;
int ID;
private final Line line;

boolean selected;
public JointProperty.Joint_Type type;

public void setSelected(boolean selected){
	this.selected = selected;
}


public void setGreen(){
    this.begin.setGreen();
    this.end.setGreen();
}

public void setBlue(){
    this.begin.setBlue();
    this.end.setBlue();
}

public void setRed(){
    this.begin.setRed();
    this.end.setRed();
}


public JointIndicator(Vector2 begin, Vector2 end,float scale, int id, JointProperty.Joint_Type type,boolean hasLimits){
    ID = id;
this.type = type;
	this.scale = scale;

	if(type == JointProperty.Joint_Type.REVOLUTE){
	this.begin = new RevolutePoint(begin.x, begin.y, scale);
	this.end = new RevolutePoint(end.x, end.y, scale, true);
        setProperty(new JointProperty(this.ID, false, 0, 0, hasLimits, 0, 0, false,0,0));
	} else if (type ==JointProperty.Joint_Type.WELD){
		this.begin = new WeldPoint(begin.x, begin.y, scale);
		this.end = new WeldPoint(end.x, end.y, scale);
        setProperty(new JointProperty(this.ID,0,0));

	}else if (type ==JointProperty.Joint_Type.DISTANCE){
		this.begin = new DistancePoint(begin.x, begin.y, scale, 0);
		this.end = new DistancePoint(end.x, end.y, scale, 0);
        setProperty(new JointProperty(this.ID,0f,0f,true,0,0));

	}else if (type ==JointProperty.Joint_Type.PRISMATIC){
		this.begin = new PrismaticPoint(begin.x, begin.y, scale);
		this.end = new PrismaticPoint(end.x, end.y, scale, true);
		float upper_limit = ((PrismaticPoint)this.end).getUpperLimit();
        float lower_limit = ((PrismaticPoint)this.end).getLowerLimit();
		Vector2 axe = ((PrismaticPoint)this.end).getAxe();

        setProperty(new JointProperty(this.ID, false, 0,0, hasLimits, lower_limit,upper_limit, false,0,0, axe));

	}


    this.line = new Line(begin.x, begin.y, end.x, end.y, 4, ResourceManager.getInstance().vbom);
    this.line.setColor(0f,1f,0f);
    attachChild(this.line);
    attachChild(this.begin);
    attachChild(this.end);
    setZIndex(1000);




}

int bodyAID, bodyBID;
public int entityA,entityB;
private JointProperty property;
public void updatePrismatic(){
    float upper_limit = ((PrismaticPoint)this.end).getUpperLimit();
    float lower_limit = ((PrismaticPoint)this.end).getLowerLimit();
    this.property.lower_limit = lower_limit;
    this.property.upper_limit= upper_limit;
    this.property.axe =  ((PrismaticPoint) end).getAxe();


}

    public void updateRevolute(){
        float upper_limit = ((RevolutePoint)this.end).getUpperLimit();
        float lower_limit = ((RevolutePoint)this.end).getLowerLimit();

    this.property.lower_limit = lower_limit;
    this.property.upper_limit = upper_limit;


    }

public void updateIndicator(float nx, float ny){

    this.end.setPosition(nx, ny);



    this.updateLine();


}

public void setEntityA(int A) {
    entityA = A;

}

public void setEntityB(int B) {
    entityB = B;

}

public void updateLine() {
    this.line.setPosition(this.begin.getX(), this.begin.getY(), this.end.getX(), this.end.getY());

}


public JointProperty getProperty() {

	return this.property;
}

public void setProperty(JointProperty property) {
	this.property = property;
	if(this.type== JointProperty.Joint_Type.REVOLUTE){
        RevolutePoint point;
        if(((RevolutePoint) this.begin).main)point= (RevolutePoint) this.begin;else point = (RevolutePoint) this.end;
        point.update(this);
    } else if(this.type== JointProperty.Joint_Type.PRISMATIC){
        PrismaticPoint point;
        if(((PrismaticPoint) this.begin).main)point= (PrismaticPoint) this.begin;else point = (PrismaticPoint) this.end;
        point.update(this);
    }
}
public void scale(float scale){
    this.begin.scale(scale);
    this.end.scale(scale);

}

public void updateMainPoints(){
    if(this.type== JointProperty.Joint_Type.REVOLUTE){
        RevolutePoint point;
        if(((RevolutePoint) this.begin).main)point= (RevolutePoint) this.begin;else point = (RevolutePoint) this.end;
        point.update(this);
    } else if(this.type== JointProperty.Joint_Type.PRISMATIC){
        PrismaticPoint point;
        if(((PrismaticPoint) this.begin).main)point= (PrismaticPoint) this.begin;else point = (PrismaticPoint) this.end;
        point.update(this);
    }
}
    public void select() {
    this.setVisible(true);
    selected = true;
    setRed();
    updateMainPoints();
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean hasLimits() {
    return property.hasLimits;
    }

    public void disselect() {
    this.setVisible(false);
        selected = false;
        setGreen();
        updateMainPoints();
    }

    public int getID() {
        return ID;
    }
}

