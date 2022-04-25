package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.andengine.input.touch.TouchEvent;


public class JointsWindow extends ScrollableOrganizedWindow {
ArrayList<JointElement> joints;



private int selectedPrimaryIndex;

private final WindowMainElement main;

public AtomicInteger jointId = new AtomicInteger();


public JointsWindow(float x, float y){
	super(x, y, 4, 5, ResourceManager.getInstance().vbom,true);

    this.joints = new ArrayList<JointElement>();

    this.main = new WindowMainElement("Joints");


    this.createLayout();




}
void addJoint(){
	JointElement button = new JointElement(jointId.get());
    this.joints.add(button);
    updateLayout();
	
}


int addJointFromOutside(){
    addJoint();
return jointId.getAndIncrement();
}

void createLayout(){
	int k = 1;


    this.main.setActive(true);
    attachChild(this.main,0,0);
	
	for(int i = 0; i< this.joints.size(); i++){
        attachChild(this.joints.get(i),k,0);
		
		k++;
		
	}


    this.scroll.update(k);
    container.setY(this.scroll.getScrollValue());
    this.container.update();
}

void updateLayout(){

	
	
	int k = 1;
	for(int i = 0; i< this.joints.size(); i++){
		
		if(container.containsEntity(this.joints.get(i)))
            moveChild(this.joints.get(i),k);
			else
            attachChild(this.joints.get(i),k,0);
		
	
	
		
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
	
	if(signal.signalName== SignalName.JointButtonMain&&signal.event==UISignal.Event.Clicked){
		for(JointElement bb: this.joints)if(bb!=signal.source)
			bb.unhighlight();

		JointElement element = (JointElement)signal.source;
		element.hightlight();
        this.selectedPrimaryIndex = this.joints.indexOf(signal.source);



		ui.out_select_Joint(((JointElement)signal.source).ID);
        this.updateLayout();
	}



	if(signal.signalName== SignalName.JointButtonRemove&&signal.event==UISignal.Event.Clicked&&!signal.confirmed) {
		ui.in_signal_setPrompt(signal, this);
	}

	if(signal.signalName== SignalName.JointButtonRemove&&signal.event==UISignal.Event.Clicked&&signal.confirmed){


        joints.remove(signal.source);
        this.container.detachChild(signal.source);
        updateLayout();
		ui.out_remove_Joint(((JointElement)signal.source).ID);
		
		
	}
	
	
	if(signal.signalName== SignalName.JointButtonOptions){
		JointElement element = (JointElement) signal.source;
		ui.in_signal_start_Joint_Option_Window(element.ID);
		
			
		}

	

	
	if(signal.signalName== SignalName.Scroller){
        container.setY(this.scroll.getScrollValue());
        this.container.update();
		}
	
}




@Override
public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

	boolean touched = false;
	if(isVisible()){
    if(super.OnTouchScene(pSceneTouchEvent))touched=true;
    if(this.window.isVisible())
    if(this.scroll.OnTouchScene(pSceneTouchEvent))touched=true;
	
	
	if(this.window.isVisible())
	for(int i = 0; i< this.window.getChildCount(); i++){
		if(this.window.getChildByIndex(i).contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){touched=true;break;}
	}
	
	for(int i = 0; i< this.padding.getChildCount(); i++){
		if(this.padding.getChildByIndex(i).contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){touched=true;break;}
	}
	}	
	
	
	
	
	
	return touched;
}




	
	
}
