package is.kul.learningandengine.graphicelements.ui;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

public class JointOptionWindow extends ScrollableOrganizedWindow {
int jointID;
int mNumber;
private final Button done;
private final UserInterface ui;
JointProperty.Joint_Type type;
JointProperty currentProperty;
public void setCurrentJoint(JointProperty property){
    currentProperty = property;
    this.primaries.clear();
    this.secondaries.clear();
    this.container.detachChildren();

    this.type = property.type;

    this.jointID = property.ID;
    this.addPrimaryElement("Body A");
    this.addPrimaryElement("Body B");
	ArrayList<BodyElement> list = this.ui.in_signal_getBodyElements();
	for(int i=0;i<list.size();i++){
        addSecondaryElement(0, new SimpleSecondaryElement((String)list.get(i).text.getText(),list.get(i).bodyID,0));
        addSecondaryElement(1, new SimpleSecondaryElement((String)list.get(i).text.getText(),list.get(i).bodyID,1));

	}
	for(int i = 0; i< this.secondaries.get(0).size(); i++){
		SimpleSecondaryElement element = (SimpleSecondaryElement) this.secondaries.get(0).get(i);
		if(element.ID==property.bodyA_ID)element.hightlight();
	}

	for(int i = 0; i< this.secondaries.get(1).size(); i++){
		SimpleSecondaryElement element = (SimpleSecondaryElement) this.secondaries.get(1).get(i);
		if(element.ID==property.bodyB_ID)element.hightlight();
	}

	if(this.type == JointProperty.Joint_Type.REVOLUTE){

        addPrimaryElement(new OnoffWindowMainElement("Has Limits"));
	if(property.hasLimits)
		((OnoffWindowMainElement) primaries.get(2)).click();

        this.addSecondaryElement(2,new TextedNumericInput("LOWER ANGLE",50,0, 0,0,360,NumType.FLOAT));
        this.addSecondaryElement(2,new TextedNumericInput("UPPER ANGLE",50,0,  0,0,360,NumType.FLOAT));
	((TextedNumericInput) secondaries.get(2).get(0)).setValue(property.lower_limit);
	((TextedNumericInput) secondaries.get(2).get(1)).setValue(property.upper_limit);

        addPrimaryElement(new OnoffWindowMainElement("Has Motor"));
	if(property.hasMotor)
		((OnoffWindowMainElement) primaries.get(3)).click();

        this.addSecondaryElement(3,new TextedNumericInput("SPEED",50,0,  0,0,100,NumType.FLOAT));
        this.addSecondaryElement(3,new TextedNumericInput("TORQUE",50,0,  0,0,99999,NumType.FLOAT));

	((TextedNumericInput) secondaries.get(3).get(0)).setValue(property.Rotation_Speed);
	((TextedNumericInput) secondaries.get(3).get(1)).setValue(property.Rotation_Torque);

        addPrimaryElement(new OnoffWindowMainElement("Collision"));
	if(property.collideConnected)
		((OnoffWindowMainElement) primaries.get(4)).click();
	}
	else
	if(this.type == JointProperty.Joint_Type.PRISMATIC){

        addPrimaryElement(new OnoffWindowMainElement("Has Limits"));
		if(property.hasLimits)
			((OnoffWindowMainElement) primaries.get(2)).click();


        addPrimaryElement(new OnoffWindowMainElement("Has Lift"));
		if(property.hasMotor)
			((OnoffWindowMainElement) primaries.get(3)).click();

        this.addSecondaryElement(3,new TextedNumericInput("SPEED   ",50,0,  0,0,100,NumType.FLOAT));
        this.addSecondaryElement(3,new TextedNumericInput("FORCE   ",50,0,  0,0,99999,NumType.FLOAT));

		((TextedNumericInput) secondaries.get(3).get(0)).setValue(property.Rotation_Speed);
		((TextedNumericInput) secondaries.get(3).get(1)).setValue(property.Rotation_Torque);

        addPrimaryElement(new OnoffWindowMainElement("Collision"));
		if(property.collideConnected)
			((OnoffWindowMainElement) primaries.get(4)).click();
		}


	else if(this.type ==JointProperty.Joint_Type.DISTANCE)
	{
        this.addPrimaryElement("OPTIONS");

        this.addSecondaryElement(2,new TextedNumericInput("FREQUENCY   ",50,0,  0,0,60,NumType.FLOAT));
        this.addSecondaryElement(2,new TextedNumericInput("DAMPING   ",50,0,  0,0,1,NumType.FLOAT));

		((TextedNumericInput) secondaries.get(2).get(0)).setValue(property.frequency);
		((TextedNumericInput) secondaries.get(2).get(1)).setValue(property.damping);
        addPrimaryElement(new OnoffWindowMainElement("Collision"));
		if(property.collideConnected)
			((OnoffWindowMainElement) primaries.get(3)).click();

	}


    setVisible(true);
    createLayout();

}


	public JointOptionWindow(float X, float Y, UserInterface ui) {
		super(X, Y, 4, 6, ResourceManager.getInstance().vbom, true);

		this.ui = ui;
        this.primaries = new ArrayList<Entity>();
        this.secondaries = new ArrayList<ArrayList<Entity>>();

	VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;

        attachChild(this.done = new Button(-46,-3*64,
				ResourceManager.getInstance().validateBigTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointOptionsDoneButton));

        attachChild( new Button(46,-3*64,
				ResourceManager.getInstance().removeBigTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointOptionCancelButton));


        setVisible(false);
	}


	public	void addPrimaryElement(String title){
	LayoutElement element;
        this.primaries.add((Entity) (element = new WindowMainElement(title)));
		element.setActive(true);
        this.secondaries.add(new ArrayList<Entity>());
	}


	public	void addPrimaryElement(Entity entity){
		((LayoutElement)entity).setActive(true);
        this.primaries.add(entity);
        this.secondaries.add(new ArrayList<Entity>());
	}
public	void addSecondaryElement(int pindex, Entity secondary){
    this.secondaries.get(pindex).add(secondary);

	}

ArrayList<Entity> primaries;
ArrayList<ArrayList<Entity>> secondaries;
int selectedPrimaryIndex;






void createLayout(){

	int k = 0;






	for(int i = 0; i< this.secondaries.size(); i++){
        attachChild(this.primaries.get(i),k,0);
		for(int j = 0; j< this.secondaries.get(i).size(); j++){

				((LayoutElement) this.secondaries.get(i).get(j)).setActive(true);
			k++;

            attachChild(this.secondaries.get(i).get(j),k,1);
		}

		k++;

	}

    this.scroll.update(k);
    container.setY(this.scroll.getScrollValue());
    this.container.update();
}









void updateLayout(){



	int k = 0;
	for(int i = 0; i< this.secondaries.size(); i++){
        moveChild(this.primaries.get(i),k);


		for(int j = 0; j< this.secondaries.get(i).size(); j++){

				((LayoutElement) this.secondaries.get(i).get(j)).setActive(true);
			k++;
			if(container.containsEntity(this.secondaries.get(i).get(j)))
                moveChild(this.secondaries.get(i).get(j),k);
			else
                attachChild(this.secondaries.get(i).get(j),k,1);


		}

		k++;

	}


    this.scroll.update(k);
    container.setY(this.scroll.getScrollValue());
    this.container.update();

}









@Override
public void diffuse(UISignal signal) {
	super.diffuse(signal);
	UserInterface	ui = (UserInterface) getParent();




	if(signal.signalName== SignalName.JointOptionsDoneButton&&signal.event==UISignal.Event.Clicked){

		int ID1 = 0,ID2 = 0;
		for(int i = 0; i< this.secondaries.get(0).size(); i++){
			if(((SimpleSecondaryElement) this.secondaries.get(0).get(i)).isPressed()){
				ID1 = ((SimpleSecondaryElement) this.secondaries.get(0).get(i)).ID;
				break;
			}
		}

		for(int i = 0; i< this.secondaries.get(1).size(); i++){
			if(((SimpleSecondaryElement) this.secondaries.get(1).get(i)).isPressed()){
				ID2 = ((SimpleSecondaryElement) this.secondaries.get(1).get(i)).ID;
				break;
			}
		}
		if(this.type ==JointProperty.Joint_Type.REVOLUTE){
		boolean hasLimits = ((OnoffWindowMainElement) primaries.get(2)).isOn();
		boolean hasMotor = ((OnoffWindowMainElement) primaries.get(3)).isOn();
		boolean collision = ((OnoffWindowMainElement) primaries.get(4)).isOn();
		float l1 = ((TextedNumericInput) this.secondaries.get(2).get(0)).getValue();
		float l2 = ((TextedNumericInput) this.secondaries.get(2).get(1)).getValue();

		float Nm = ((TextedNumericInput) this.secondaries.get(3).get(0)).getValue();
		float Tm = ((TextedNumericInput) this.secondaries.get(3).get(1)).getValue();


	JointProperty property = new JointProperty(this.jointID,hasMotor,Nm,Tm,hasLimits,l1,l2,collision,ID1,ID2);
	ui.out_set_Joint_Property(property);

		} else	if(this.type ==JointProperty.Joint_Type.PRISMATIC){
			boolean hasLimits = ((OnoffWindowMainElement) primaries.get(2)).isOn();
			boolean hasMotor = ((OnoffWindowMainElement) primaries.get(3)).isOn();
			boolean collision = ((OnoffWindowMainElement) primaries.get(4)).isOn();
			float Nm = ((TextedNumericInput) this.secondaries.get(3).get(0)).getValue();
			float Tm = ((TextedNumericInput) this.secondaries.get(3).get(1)).getValue();
			JointProperty property = new JointProperty(this.jointID,hasMotor,Nm,Tm,hasLimits, this.currentProperty.lower_limit, this.currentProperty.upper_limit,collision,ID1,ID2, this.currentProperty.axe);
			ui.out_set_Joint_Property(property);
		}
		 else if(this.type ==JointProperty.Joint_Type.DISTANCE)
			{

			 float frequency = ((TextedNumericInput) this.secondaries.get(2).get(0)).getValue();
			float damping= ((TextedNumericInput) this.secondaries.get(2).get(1)).getValue();
			boolean collision = ((OnoffWindowMainElement) primaries.get(3)).isOn();
			JointProperty property =  new JointProperty(this.jointID,frequency, damping,collision,ID1,ID2);
			ui.out_set_Joint_Property(property);
			}

		else if(this.type ==JointProperty.Joint_Type.WELD){


			JointProperty property = new JointProperty(this.jointID,ID1,ID2);
			ui.out_set_Joint_Property(property);



		}

        setVisible(false);
	}



	if(signal.signalName== SignalName.SimpleSecondaryButtonMain&&signal.event==UISignal.Event.Clicked){
		SimpleSecondaryElement element = (SimpleSecondaryElement)signal.source;
		element.hightlight();
		for(Entity bb: this.secondaries.get(element.PrimaryID)){
			if(bb!=signal.source)	((SimpleSecondaryElement) bb).unhighlight();
		}

	}
if(signal.signalName== SignalName.SimpleSecondaryButtonMain&&signal.event==UISignal.Event.Unclicked){
	boolean ok = false;
	SimpleSecondaryElement element = (SimpleSecondaryElement)signal.source;
	element.hightlight();
	for(Entity bb: this.secondaries.get(element.PrimaryID)){
		SimpleSecondaryElement simple = (SimpleSecondaryElement) bb;
		if(simple.isPressed()){
			ok = true;break;
		}
	}
	
	if(!ok)((SimpleSecondaryElement) signal.source).hightlight();
	}
	
	
	if(signal.signalName== SignalName.Scroller){
        container.setY(this.scroll.getScrollValue());
        this.container.update();
		}
	
}	



@Override
public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

	if(isVisible()){
		  if(this.window.isVisible()){
	if(this.done.OnTouchScene(pSceneTouchEvent))return true;

  
    if(this.scroll.OnTouchScene(pSceneTouchEvent))return true;
		  }
    if(super.OnTouchScene(pSceneTouchEvent))return true;

	
	}
return false;
}
}
