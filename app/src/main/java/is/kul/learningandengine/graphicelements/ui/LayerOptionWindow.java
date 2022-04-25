package is.kul.learningandengine.graphicelements.ui;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.factory.Material;
import is.kul.learningandengine.factory.MaterialFactory;
import is.kul.learningandengine.graphicelements.CollisionOption;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;


public class LayerOptionWindow extends ScrollableOrganizedWindow {
int layerID;
int mNumber;
private final Button done;
private final Button refuse;
LayerProperty property;
public void setCurrentLayer(LayerProperty property){
	this.property = property;
    this.primaries.clear();
    this.secondaries.clear();
    this.container.detachChildren();


    this.layerID = property.ID;
    this.addPrimaryElement(new NameElement(property.layerName));
    this.addPrimaryElement(new ColorButtonElement(property.getColor()));

    this.addPrimaryElement("Material");
    this.addPrimaryElement("General Properties");
    this.addPrimaryElement("Category");
    this.addPrimaryElement("Collides With");
    this.addSecondaryElement(3,new TextedNumericInput("DENSITY   ",0,0,2.35f,0.001f, 800f,NumType.FLOAT));
    this.addSecondaryElement(3,new TextedSelector(property.getRestitution(),1, 20, 1, "BOUNCINESS", 0, 0, 0, 1f, 4));
    this.addSecondaryElement(3,new TextedSelector(property.getFriction(),2, 20, 1, "FRICTION  ", 0, 0, 0, 1f, 5));





int mat = property.getMaterialNumber();
    this.loadMaterialButtons(mat);
	 loadCollisionButtons(property.getCollisionList());
    loadCategoryButtons(property.getOrder());

 ((TextedNumericInput) this.secondaries.get(3).get(0)).numericInput.setFloatValue(property.getDensity());

    setVisible(true);
    createLayout();
	
}
 private ColorButtonElement getColorButton(){
	return (ColorButtonElement) this.primaries.get(1);
}
void loadMaterialButtons(int a){
	for(int i=0;i<MaterialFactory.getInstance().materials.size();i++){
		SimpleSecondaryElement element;
        this.addSecondaryElement(2,element = new SimpleSecondaryElement(MaterialFactory.getInstance().materials.get(i).name,i));

		if(i==a)element.hightlight();

	}

}
short getSelectedCategory(){
    for(Entity s:secondaries.get(4))

        if(s instanceof SimpleSecondaryElement){
            SimpleSecondaryElement se = (SimpleSecondaryElement)s;
            if(se.isPressed())return (short)(se.ID);
    }

    return 0;
}

	short[] getMaskCategories(){
	int k = 0;
		for(Entity s:secondaries.get(5))

			if(s instanceof SimpleSecondaryElement){
				SimpleSecondaryElement se = (SimpleSecondaryElement)s;
				if(se.isPressed())k++;
			}
			short[] list = new short[k];
		k = 0;
		for(Entity s:secondaries.get(5)) {

			if (s instanceof SimpleSecondaryElement) {
				SimpleSecondaryElement se = (SimpleSecondaryElement) s;
				if (se.isPressed()) {
					list[k] = (short) (se.ID);
					k++;
				}
			}

		}

		return list;
	}
    void loadCategoryButtons(short a){
        UserInterface parent = (UserInterface)this.getParent();
        ArrayList<CategoryElement> categories = parent.getCategories();
        for(int i=0;i<categories.size();i++) {
            CategoryElement category = categories.get(i);
            SimpleSecondaryElement element;
            this.addSecondaryElement(4, element = new SimpleSecondaryElement(category.getName(), category.ID));
if(a==category.ID)element.hightlight();
        }

    }


	void loadCollisionButtons(short[] list){
		UserInterface parent = (UserInterface)this.getParent();
		ArrayList<CategoryElement> categories = parent.getCategories();
		for(int i=0;i<categories.size();i++) {
			CategoryElement category = categories.get(i);
			SimpleSecondaryElement element;
			this.addSecondaryElement(5, element = new SimpleSecondaryElement(category.getName(), category.ID));
for(int j=0;j<list.length;j++)if(category.ID==list[j])element.hightlight();
		}

	}





	public LayerOptionWindow(float X, float Y) {
		super(X, Y, 4, 6, ResourceManager.getInstance().vbom, true);


        this.primaries = new ArrayList<Entity>();
        this.secondaries = new ArrayList<ArrayList<Entity>>();
	
	VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;

        attachChild(this.done = new Button(-46,-3*64,
				ResourceManager.getInstance().validateBigTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointOptionsDoneButton));

        attachChild(this.refuse = new Button(46,-3*64,
				ResourceManager.getInstance().removeBigTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointOptionCancelButton));


        setVisible(false);
	}

	
	public	void addPrimaryElement(String title){

        this.primaries.add(new LayerOptionElement(title));
        this.secondaries.add(new ArrayList<Entity>());
	}
	

	public	void addPrimaryElement(Entity entity){
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
		if(this.primaries.get(i) instanceof ColorButtonElement)
            attachChild2(this.primaries.get(i),k,20f);
		else
            attachChild(this.primaries.get(i),k,0);
		for(int j = 0; j< this.secondaries.get(i).size(); j++){
			if(i== this.selectedPrimaryIndex){
				((LayoutElement) this.secondaries.get(i).get(j)).setActive(true);
			k++;
			}
		
			else ((LayoutElement) this.secondaries.get(i).get(j)).setActive(false);
            attachChild(this.secondaries.get(i).get(j),k,0);
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
			if(i== this.selectedPrimaryIndex){
				((LayoutElement) this.secondaries.get(i).get(j)).setActive(true);
			k++;
			if(container.containsEntity(this.secondaries.get(i).get(j)))
                moveChild(this.secondaries.get(i).get(j),k);
			else
                attachChild(this.secondaries.get(i).get(j),k,0);
			}
			else ((LayoutElement) this.secondaries.get(i).get(j)).setActive(false);
		}
		
		k++;
		
	}


    this.scroll.update(k);
    container.setY(this.scroll.getScrollValue());
    this.container.update();
    Log.e("signal","layout");
    ((UIHandler)this.getParent()).diffuse(new UISignal(SignalName.Updated));
}









@Override
public void diffuse(UISignal signal) {
	super.diffuse(signal);

	if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){


        done.setVisible(true);
        refuse.setVisible(true);
	}

if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){

    this.done.setVisible(false);
    this.refuse.setVisible(false);
	}





	UserInterface	ui = (UserInterface) getParent();






	if(signal.signalName== SignalName.ColorButton&&signal.event==UISignal.Event.Clicked){
		for(Entity bb: this.primaries)if(bb!=signal.source&&bb instanceof Button)
			((TextedButton) bb).unhighlight();
		ui.in_signal_OpenColorSelector(this.property.getColor(),this);

	}


	if(signal.signalName== SignalName.LayerOptionButtonMain&&signal.event==UISignal.Event.Clicked){
		for(Entity bb: this.primaries)if(bb!=signal.source&&bb instanceof TextedButton)
			((TextedButton) bb).unhighlight();
        this.selectedPrimaryIndex = this.primaries.indexOf(signal.source);


        this.updateLayout();
	}

	if(signal.signalName== SignalName.SimpleSecondaryButtonMain&&signal.event==UISignal.Event.Unclicked){
		SimpleSecondaryElement sselement = (SimpleSecondaryElement)signal.source;
		sselement.unhighlight();
	}
	if(signal.signalName== SignalName.SimpleSecondaryButtonMain&&signal.event==UISignal.Event.Clicked){
        if(this.selectedPrimaryIndex ==4||this.selectedPrimaryIndex ==5){
            SimpleSecondaryElement sselement = (SimpleSecondaryElement)signal.source;
            sselement.hightlight();
            if(this.selectedPrimaryIndex ==4)
            for(Entity bb: this.secondaries.get(this.selectedPrimaryIndex)){
                SimpleSecondaryElement element = (SimpleSecondaryElement)bb;
                if(element!=signal.source)	element.unhighlight();
            }

        }

		if(this.selectedPrimaryIndex ==2){
			SimpleSecondaryElement sselement = (SimpleSecondaryElement)signal.source;
			sselement.hightlight();
            this.mNumber = ((SimpleSecondaryElement)signal.source).ID;
		for(Entity bb: this.secondaries.get(this.selectedPrimaryIndex)){
			SimpleSecondaryElement element = (SimpleSecondaryElement)bb;
			if(element!=signal.source)	element.unhighlight();
		}

	Material material =	MaterialFactory.getInstance().materials.get(this.mNumber);
	((TextedNumericInput) this.secondaries.get(3).get(0)).numericInput.setFloatValue(material.DENSITY);
	((TextedSelector) this.secondaries.get(3).get(1)).setValue(material.RESTITUTION);
	((TextedSelector) this.secondaries.get(3).get(2)).setValue(material.FRICTION);
            this.property.setColor(material.color);
		}
	}




	if(signal.signalName== SignalName.LayerOptionButtonMain&&signal.event==UISignal.Event.Unclicked){
        this.selectedPrimaryIndex = -1;
        updateLayout();


		}





	if(signal.signalName== SignalName.JointOptionsDoneButton&&signal.event==UISignal.Event.Clicked){
	//GET ALL PROPERTIES FROM WINDOW

	float density=	((TextedNumericInput) this.secondaries.get(3).get(0)).numericInput.getFloatValue();
	float bounciness=	((TextedSelector) this.secondaries.get(3).get(1)).getValue();
	float friction=	((TextedSelector) this.secondaries.get(3).get(2)).getValue();
		float hardness=	MaterialFactory.getInstance().getMaterial(this.mNumber).HARDNESS;
		float[] properties = {density,bounciness,friction,hardness};

        CollisionOption cOption = this.property.getCollisionOptions();
        cOption.setCategory(this.getSelectedCategory());
		cOption.collidesWith(this.getMaskCategories());

       this.property.layerName = (String) ((NameElement)this.primaries.get(0)).alphanumericInput.getText();
        this.property.setProperties(properties);

        setVisible(false);


		ui.out_signal_updateProperty(this.property);
		
	}

	
	
	
	
	if(signal.signalName== SignalName.Scroller){
        container.setY(this.scroll.getScrollValue());
        this.container.update();
		}
	
}
@Override
    public void updateColor(Color color) {
        getColorButton().updateColor(color);
    }


@Override
public boolean OnTouchScene(TouchEvent touch) {

	boolean touched = false;
	if(isVisible()){
    if(super.OnTouchScene(touch)) {
        touched = true;

    }
	if(this.done.OnTouchScene(touch))touched=true;
	if(this.scroll.OnTouchScene(touch))touched=true;
	
	}
return touched;
}
}
