package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.BodyProperty;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.scene.UISignal;


public class LayersWindow extends ScrollableOrganizedWindow {
private final ArrayList<BodyElement> bodies;
private final ArrayList<ArrayList<LayerElement>> layers;
private final ArrayList<ArrayList<ArrayList<DecorationElement>>> decorations;

private int selectedPrimaryIndex;
private int selectedSecondaryIndex;
private int selectedThirdIndex;

private final LayerWindowMainElement main;

public AtomicInteger bodyId = new AtomicInteger();
public int getSelectedPrimaryIndex(){
    this.selectedPrimaryIndex = -1;
	for(BodyElement body: this.bodies)if(body.isPressed()){
        this.selectedPrimaryIndex = this.bodies.indexOf(body);
	break;
	}
	return this.selectedPrimaryIndex;
}

public int getSelectedSecondaryIndex(){
    this.selectedSecondaryIndex = -1;
	for(LayerElement layer: this.layers.get(this.selectedPrimaryIndex))if(layer.isPressed()){
        this.selectedSecondaryIndex = this.layers.get(this.selectedPrimaryIndex).indexOf(layer);
		break;
	}
	return this.selectedSecondaryIndex;
}

public int getSelectedThirdIndex(){
    this.selectedThirdIndex = -1;
	for(DecorationElement decoration: this.decorations.get(this.selectedPrimaryIndex).get(this.selectedSecondaryIndex))if(decoration.isPressed()){
        selectedThirdIndex = this.decorations.get(this.selectedPrimaryIndex).get(this.selectedSecondaryIndex).indexOf(decoration);
		break;
	}
	return this.selectedThirdIndex;
}
public void updateCurrentLayerName(){
    UserInterface ui = (UserInterface)this.getParent();
	ui.toolsWindow.updateCutLayerName(layers.get(selectedPrimaryIndex).get(selectedSecondaryIndex).name);
}

public LayersWindow(float x, float y){
	super(x, y, 4, 5, ResourceManager.getInstance().vbom,true);

    this.bodies = new ArrayList<BodyElement>();
    this.layers = new ArrayList<ArrayList<LayerElement>>();
    this.decorations = new ArrayList<ArrayList<ArrayList<DecorationElement>>>();
    this.main = new LayerWindowMainElement("Bodies");

    this.addBody(bodyId.getAndIncrement());
    selectedPrimaryIndex = 0;
    selectedSecondaryIndex = 0;
    addLayer(0, this.bodies.get(0).layerId.getAndIncrement());
    this.layers.get(this.selectedPrimaryIndex).get(0).hightlight();


    this.createLayout();
}
private BodyElement addBody(int bodyID){
	BodyElement bodyelement = new BodyElement(bodyID);
    this.bodies.add(bodyelement);
	ArrayList<LayerElement> bodyLayers = new ArrayList<LayerElement>();
	ArrayList<ArrayList<DecorationElement>> bodyDecorations = new ArrayList<ArrayList<DecorationElement>>();
    this.decorations.add(bodyDecorations);
    this.layers.add(bodyLayers);

	return bodyelement;

}
private LayerElement addLayer(int bodyIndex, int layerID){
	LayerElement layerelement = new LayerElement(this.bodies.get(bodyIndex).bodyID,layerID);
    this.layers.get(bodyIndex).add(layerelement);
    this.decorations.get(bodyIndex).add(new ArrayList<DecorationElement>());
	return layerelement;
}

private DecorationElement addDecoration(int bodyIndex,int layerIndex, int decorID) {
	for(DecorationElement e: this.decorations.get(this.selectedPrimaryIndex).get(this.selectedSecondaryIndex))
		if(e.decorationID!=decorID)e.unhighlight();
	String name = (decorID==1000)?"Cutting Shape":"Decoration"+decorID;
	DecorationElement decorationElement =
            new DecorationElement(this.bodies.get(bodyIndex).bodyID, this.layers.get(bodyIndex).get(layerIndex).layerID,decorID,name);
    this.decorations.get(bodyIndex).get(layerIndex).add(decorationElement);
	decorationElement.hightlight();
	return decorationElement;

}

void createLayout(){
	int k = 1;
	Button firstbody = this.bodies.get(0);
	firstbody.changeState(Button.State.PRESSED);
    this.bodies.get(0).text.setColor(0f,1f,0f);
    this.selectedPrimaryIndex = 0;

	TextedButton firstlayer = this.layers.get(0).get(0);
	firstlayer.hightlight();


    this.main.setActive(true);
    attachChild(this.main,0,0);

	for(int i = 0; i< this.layers.size(); i++){
        attachChild(this.bodies.get(i),k,0);
		for(int j = 0; j< this.layers.get(i).size(); j++){
			if(i== this.selectedPrimaryIndex){
                this.layers.get(i).get(j).setActive(true);
			k++;
			}

			else this.layers.get(i).get(j).setActive(false);
            attachChild(this.layers.get(i).get(j),k,3);
			for(int d = 0; d< this.decorations.get(i).get(j).size(); d++){
				if(j== this.selectedSecondaryIndex){
                    this.decorations.get(i).get(j).get(d).setActive(true);
					k++;
				}
                attachChild(this.decorations.get(i).get(j).get(d),k,4);
			}



		}

		k++;

	}

	for(int i = 0; i< this.layers.size(); i++){

		for(int j = 0; j< this.layers.get(i).size(); j++){
			if(j==0) this.layers.get(i).get(0).getUp().changeState(Button.State.DISABLED);
			else this.layers.get(i).get(j).getUp().changeState(Button.State.NORMAL);

			if(j== this.layers.get(i).size()-1) this.layers.get(i).get(j).getDown().changeState(Button.State.DISABLED);
			else this.layers.get(i).get(j).getDown().changeState(Button.State.NORMAL);

		}
	}

    this.scroll.update(k);
    container.setY(this.scroll.getScrollValue());
    this.container.update();
}

void updateLayout(){

	for(int i = 0; i< this.layers.size(); i++){

		for(int j = 0; j< this.layers.get(i).size(); j++){
			if(j==0) this.layers.get(i).get(0).getUp().changeState(Button.State.DISABLED);
			else this.layers.get(i).get(j).getUp().changeState(Button.State.NORMAL);

			if(j== this.layers.get(i).size()-1) this.layers.get(i).get(j).getDown().changeState(Button.State.DISABLED);
			else this.layers.get(i).get(j).getDown().changeState(Button.State.NORMAL);

		}
	}


	int k = 1;
	for(int i = 0; i< this.layers.size(); i++){

		if(container.containsEntity(this.bodies.get(i)))
            moveChild(this.bodies.get(i),k);
			else
            attachChild(this.bodies.get(i),k,0);



		for(int j = 0; j< this.layers.get(i).size(); j++){
			if(i== this.selectedPrimaryIndex){
                this.layers.get(i).get(j).setActive(true);
			k++;
			if(container.containsEntity(this.layers.get(i).get(j)))
                moveChild(this.layers.get(i).get(j),k);
			else
                attachChild(this.layers.get(i).get(j),k,3);


			for(int d = 0; d< this.decorations.get(i).get(j).size(); d++){
				if(j == this.selectedSecondaryIndex){
                    this.decorations.get(i).get(j).get(d).setActive(true);
					k++;
					if(container.containsEntity(this.decorations.get(i).get(j).get(d)))
                        moveChild(this.decorations.get(i).get(j).get(d),k);
					else
                        attachChild(this.decorations.get(i).get(j).get(d),k,4);

				} else this.decorations.get(i).get(j).get(d).setActive(false);

			}
			}
			else {
                this.layers.get(i).get(j).setActive(false);
			for(DecorationElement decoration: this.decorations.get(i).get(j))decoration.setActive(false);

			}


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

	UserInterface ui = (UserInterface) getParent();


	if(signal.signalName == UISignal.SignalName.AddBodyButton&&signal.event == UISignal.Event.Clicked){
		BodyElement bodyElement = addBody(bodyId.getAndIncrement());
		bodyElement.hightlight();
		for(BodyElement body: this.bodies)if(body!=bodyElement)body.unhighlight();
        getSelectedPrimaryIndex();
        addLayer(bodyElement.bodyID, bodyElement.layerId.getAndIncrement());
        this.layers.get(this.selectedPrimaryIndex).get(0).hightlight();
        getSelectedSecondaryIndex();
		if(selectedSecondaryIndex !=-1)
            getSelectedThirdIndex();
        updateLayout();
		ui.out_signal_addBody(bodyElement.bodyID);
		ui.update();

	}

	if(signal.signalName == UISignal.SignalName.BodyElementMain&&signal.event == UISignal.Event.Clicked){
		BodyElement bodyElement = (BodyElement) signal.source;
		bodyElement.hightlight();
		for(BodyElement body: this.bodies)if(body!=bodyElement)body.unhighlight();

        getSelectedPrimaryIndex();
        getSelectedSecondaryIndex();
		if(selectedSecondaryIndex !=-1)
            getSelectedThirdIndex();
        updateLayout();
		ui.out_signal_selectBody(bodyElement.bodyID);
		if(selectedSecondaryIndex !=-1)
		ui.out_signal_selectLayer(this.layers.get(selectedPrimaryIndex).get(this.selectedSecondaryIndex).layerID);
		this.updateCurrentLayerName();
		ui.update();
	}
	if(signal.signalName == UISignal.SignalName.BodyElementMain&&signal.event == UISignal.Event.Unclicked){
		BodyElement bodyElement = (BodyElement) signal.source;
		bodyElement.unhighlight();

        selectedPrimaryIndex = -1;
        selectedSecondaryIndex = -1;
        selectedThirdIndex = -1;
        updateLayout();
		ui.out_signal_selectBody(-1);

	}


	if(signal.signalName == UISignal.SignalName.LayerButtonMain&&signal.event == UISignal.Event.Clicked){
		LayerElement layerElement = (LayerElement) signal.source;
		for(LayerElement layer: this.layers.get(this.selectedPrimaryIndex))if(layer!=layerElement)layer.unhighlight();
		layerElement.hightlight();
        getSelectedSecondaryIndex();
		for(DecorationElement decoration: this.decorations.get(this.selectedPrimaryIndex).get(this.selectedSecondaryIndex))decoration.unhighlight();
        selectedThirdIndex = -1;
        updateLayout();
		ui.out_signal_selectLayer(layerElement.layerID);
		ui.out_signal_updateDecaledLayers(getMoveableLayers());
		this.updateCurrentLayerName();
		ui.update();
	}
	if(signal.signalName == UISignal.SignalName.LayerButtonMain&&signal.event == UISignal.Event.Unclicked){
		LayerElement layerElement = (LayerElement) signal.source;
		layerElement.unhighlight();
        selectedSecondaryIndex = -1;
        selectedThirdIndex = -1;
        updateLayout();
		ui.out_signal_selectLayer(-1);
		ui.update();
	}
	if(signal.signalName == UISignal.SignalName.BodyOptionButton&&signal.event == UISignal.Event.Clicked){
		BodyElement element = (BodyElement) signal.source;
		BodyProperty bproperty =ui.scene.model.getBody(element.bodyID).getProperties();
		ui.getBodyOptionWindow().setCurrentBody(bproperty);
	}

	if(signal.signalName == UISignal.SignalName.BodyElementAddLayerButton&&signal.event == UISignal.Event.Clicked){
		BodyElement bodyElement = (BodyElement) signal.source;
		bodyElement.hightlight();
		for(BodyElement body: this.bodies)if(body!=bodyElement)body.unhighlight();
        getSelectedPrimaryIndex();
		LayerElement layerElement = addLayer(selectedPrimaryIndex, bodyElement.layerId.getAndIncrement());
		for(LayerElement layer: this.layers.get(this.selectedPrimaryIndex))if(layer!=layerElement)layer.unhighlight();
		layerElement.hightlight();
        getSelectedSecondaryIndex();
        getSelectedThirdIndex();
        updateLayout();
		ui.out_signal_selectBody(bodyElement.bodyID);
		ui.out_signal_addLayer(layerElement.layerID);
		this.updateCurrentLayerName();
		ui.update();
	}


	if(signal.signalName == UISignal.SignalName.LayerButtonAddDecoration&&signal.event == UISignal.Event.Clicked){
		LayerElement layerElement = (LayerElement) signal.source;
		for(LayerElement layer: this.layers.get(this.selectedPrimaryIndex))if(layer!=layerElement)layer.unhighlight();
		layerElement.hightlight();
        getSelectedSecondaryIndex();
        getSelectedThirdIndex();
		//ADD THE DOCORATION BUTTON
		DecorationElement decorationElement = addDecoration(selectedPrimaryIndex, selectedSecondaryIndex, layerElement.decorationId.getAndIncrement());


        updateLayout();
		ui.out_signal_selectLayer(layerElement.layerID);
		ui.out_signal_addDecoration(decorationElement.decorationID);
		ui.toolsWindow.update();

	}




	if(signal.signalName== UISignal.SignalName.LayerButtonUpArrow&&signal.event==UISignal.Event.Unclicked)
		{

		int order = this.layers.get(selectedPrimaryIndex).indexOf(signal.source);
		int po = order-1;



		Collections.swap(this.layers.get(selectedPrimaryIndex), order, po);
            this.updateLayout();

	ui.out_signal_swapLayers(order,po);
		}


	if(signal.signalName== UISignal.SignalName.LayerButtonDownArrow&&signal.event==UISignal.Event.Unclicked)
	{

	int order = this.layers.get(selectedPrimaryIndex).indexOf(signal.source);
	int no = order+1;



	Collections.swap(this.layers.get(selectedPrimaryIndex), order, no);
        this.updateLayout();

ui.out_signal_swapLayers(order,no);

	}

	if(signal.signalName== UISignal.SignalName.LayerButtonRemove&&signal.event == UISignal.Event.Clicked&&!signal.confirmed){
ui.in_signal_setPrompt(signal,this);
	}


	if(signal.signalName== UISignal.SignalName.LayerButtonRemove&&signal.event == UISignal.Event.Clicked&&signal.confirmed){
		int layerIndex = this.layers.get(this.selectedPrimaryIndex).indexOf(signal.source);
        layers.get(selectedPrimaryIndex).remove(signal.source);
        this.container.detachChild(signal.source);
		LayerElement element = (LayerElement) signal.source;

		for(DecorationElement e: this.decorations.get(this.selectedPrimaryIndex).get(layerIndex)) this.container.detachChild(e);
        decorations.get(this.selectedPrimaryIndex).remove(layerIndex);
        selectedSecondaryIndex = -1;
        selectedThirdIndex = -1;
        updateLayout();
        ui.out_signal_removeLayer(element.layerID);
	}


	if(signal.signalName== UISignal.SignalName.DecorationButtonRemove&&signal.event == UISignal.Event.Clicked&&!signal.confirmed){
		ui.in_signal_setPrompt(signal,this);
	}


    if(signal.signalName== UISignal.SignalName.DecorationButtonRemove&&signal.event == UISignal.Event.Clicked&&signal.confirmed){
        int decorationIndex = this.decorations.get(this.selectedPrimaryIndex).get(selectedSecondaryIndex).indexOf(signal.source);
        decorations.get(selectedPrimaryIndex).get(selectedSecondaryIndex).remove(signal.source);
        this.container.detachChild(signal.source);
        DecorationElement element = (DecorationElement) signal.source;

        selectedThirdIndex = -1;
        updateLayout();
        ui.out_signal_removeDecoration(element.decorationID);
    }


	if(signal.signalName== UISignal.SignalName.DecorationButtonOptions&&signal.event==UISignal.Event.Clicked){
		int decorationID = ((DecorationElement)signal.source).decorationID;
Color decColor = ui.in_signal_GetColorOfDecoration(decorationID);

		ui.in_signal_OpenColorSelector(decColor,this);

	}






    if(signal.signalName== UISignal.SignalName.LayerButtonOptions&&signal.event == UISignal.Event.Clicked)
	{
	LayerElement element = (LayerElement) signal.source;
	LayerProperty lproperty =ui.scene.model.getLayer(element.layerID).properties;
	ui.getLayerOptionWindow().setCurrentLayer(lproperty);
	}

	if(signal.signalName== UISignal.SignalName.LayerButtonDecale)
	{

ui.out_signal_updateDecaledLayers(getMoveableLayers());
	}


	if(signal.signalName== UISignal.SignalName.BodyElementDecaleButton)
	{
		BodyElement element = (BodyElement) signal.source;
		int index = this.bodies.indexOf(element);
		if(signal.event==UISignal.Event.Clicked){


	for(int i = 0; i< this.layers.get(index).size(); i++){
		LayerElement e = this.layers.get(index).get(i);
		e.getDecale().click();

	}
	for(BodyElement body: this.bodies){
		if(body!=element){
		body.getDecale().unclick();

		int index1 = this.bodies.indexOf(body);
		for(int i = 0; i< this.layers.get(index1).size(); i++){
			LayerElement e = this.layers.get(index1).get(i);
			e.getDecale().unclick();

		}

		}
	}


	}

		if(signal.event==UISignal.Event.Unclicked){


				for(int i = 0; i< this.layers.get(index).size(); i++){
					LayerElement e = this.layers.get(index).get(i);
					e.getDecale().unclick();

				}
		}

		ui.out_signal_updateDecaledLayers(getMoveableLayers());
	}










	if(signal.signalName == UISignal.SignalName.DecorationButtonMain&&signal.event == UISignal.Event.Clicked){
		DecorationElement decorationElement = (DecorationElement) signal.source;
		decorationElement.hightlight();
		for(DecorationElement e: this.decorations.get(this.selectedPrimaryIndex).get(this.selectedSecondaryIndex))if(e!=decorationElement)e.unhighlight();
        getSelectedThirdIndex();
        updateLayout();
		ui.out_signal_selectDecoration(decorationElement.decorationID);
	}
	if(signal.signalName == UISignal.SignalName.DecorationButtonMain&&signal.event == UISignal.Event.Unclicked){
		DecorationElement decorationElement = (DecorationElement) signal.source;
		decorationElement.unhighlight();
        selectedThirdIndex = -1;
		ui.out_signal_selectDecoration(-1);
	}


	if(signal.signalName== UISignal.SignalName.Scroller){
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

public ArrayList<BodyElement> getBodies() {
	// TODO Auto-generated method stub
	return this.bodies;
}

public void clickLayer(int layerID) {
	for(LayerElement element: this.layers.get(this.selectedPrimaryIndex))if(element.layerID==layerID){element.hightlight();break;}
    getSelectedSecondaryIndex();
    updateLayout();
}


private int[] getMoveableLayers(){
	int[] indexes = new int[100];
	int k = 0;
	for(int i = 0; i< this.layers.get(this.selectedPrimaryIndex).size(); i++){
	if(this.layers.get(this.selectedPrimaryIndex).get(i).getDecale().isPressed()){
		indexes[k] = this.layers.get(this.selectedPrimaryIndex).get(i).layerID;
		k++;
	}
	}
	indexes = Arrays.copyOf(indexes, k);

	return indexes;

}
int getBodyIndexFromID(int bodyID){
    for(BodyElement b:bodies)if(b.bodyID==bodyID)return bodies.indexOf(b);
    return -1;
}

    int getLayerIndexFromID(int bodyIndex,int layerID){

        for(LayerElement l:layers.get(bodyIndex))if(l.layerID==layerID)return layers.get(bodyIndex).indexOf(l);
        return -1;
    }

    int getDecorationIndexFromID(int bodyIndex,int layerIndex,int decorationID){
        for(DecorationElement d:decorations.get(bodyIndex).get(layerIndex))if(d.decorationID==decorationID)return decorations.get(bodyIndex).get(layerIndex).indexOf(d);
        return -1;
    }

    public int addLayerFromOutside(int activeBodyID) {
		int result = bodies.get(activeBodyID).layerId.getAndIncrement();
		LayerElement element;

		layers.get(activeBodyID).add(element = new LayerElement(activeBodyID,result));
		decorations.get(activeBodyID).add(new ArrayList<DecorationElement>());
		element.hightlight();
		for(LayerElement layer:layers.get(activeBodyID))
			if(layer!=element)layer.unhighlight();

		this.updateLayout();
		return result;
    }


	public void removeLayerFromOutside(int bodyID, int layerID) {
	int bodyIndex = this.getBodyIndexFromID(bodyID);
	int layerIndex = this.getLayerIndexFromID(bodyIndex,layerID);

				this.container.detachChild(layers.get(bodyIndex).get(layerIndex));
				layers.get(bodyIndex).remove(layerIndex);
		ArrayList<DecorationElement> decors = decorations.get(bodyIndex).get(layerIndex);
		for(DecorationElement d:decors)this.container.detachChild(d);
		decors.clear();
				this.updateLayout();

	}


	public int addDecorationFromOutside(int bodyID, int layerID) {
	int bodyIndex = this.getBodyIndexFromID(bodyID);
	int layerIndex = this.getLayerIndexFromID(bodyIndex,layerID);
	LayerElement layer = layers.get(bodyIndex).get(layerIndex);
	int decorationID = layer.decorationId.get();
	this.addDecoration(bodyIndex, layerIndex,layer.decorationId.getAndIncrement());
	this.updateLayout();
	return decorationID;
	}
	public void addCuttingDecorationFromOutside(int bodyID, int layerID) {
		int bodyIndex = this.getBodyIndexFromID(bodyID);
		int layerIndex = this.getLayerIndexFromID(bodyIndex,layerID);
		this.addDecoration(bodyIndex, layerIndex,1000);
		this.updateLayout();
	}


	public void removeDecorationFromOutside(int bodyID, int layerID, int decorationID) {
		int bodyIndex = this.getBodyIndexFromID(bodyID);
		int layerIndex = this.getLayerIndexFromID(bodyIndex,layerID);
		int decorationIndex = this.getDecorationIndexFromID(bodyIndex,layerIndex,decorationID);
		this.container.detachChild(decorations.get(bodyIndex).get(layerIndex).get(decorationIndex));
		decorations.get(bodyIndex).get(layerIndex).remove(decorationIndex);
		this.updateLayout();

	}
}
