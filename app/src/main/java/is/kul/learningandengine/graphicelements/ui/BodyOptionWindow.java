package is.kul.learningandengine.graphicelements.ui;


import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.BodyProperty;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class BodyOptionWindow extends ScrollableOrganizedWindow {
    int bodyID;
    int mNumber;
    private final Button done;
    private final Button refuse;
    BodyProperty property;
    public void setCurrentBody(BodyProperty property){
        this.property = property;
        this.primaries.clear();
        this.secondaries.clear();
        this.container.detachChildren();
        this.bodyID = property.getBodyID();
        this.addPrimaryElement(new NameElement(property.bodyName));
        this.addPrimaryElement("Type");
        this.addPrimaryElement("Forces");
        setVisible(true);
        createLayout();

    }





    public BodyOptionWindow(float X, float Y) {
        super(X, Y, 4, 6, ResourceManager.getInstance().vbom, true);


        this.primaries = new ArrayList<Entity>();
        this.secondaries = new ArrayList<ArrayList<Entity>>();

        VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;

        attachChild(this.done = new Button(-46,-3*64,
                ResourceManager.getInstance().validateBigTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.JointOptionsDoneButton));

        attachChild(this.refuse = new Button(46,-3*64,
                ResourceManager.getInstance().removeBigTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.JointOptionCancelButton));


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

        ((UIHandler)this.getParent()).diffuse(new UISignal(UISignal.SignalName.Updated));
    }









    @Override
    public void diffuse(UISignal signal) {
        super.diffuse(signal);

        if(signal.signalName== UISignal.SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){


            done.setVisible(true);
            refuse.setVisible(true);
        }

        if(signal.signalName== UISignal.SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){

            this.done.setVisible(false);
            this.refuse.setVisible(false);
        }





        UserInterface	ui = (UserInterface) getParent();







        if(signal.signalName== UISignal.SignalName.LayerOptionButtonMain&&signal.event==UISignal.Event.Clicked){
            for(Entity bb: this.primaries)if(bb!=signal.source&&bb instanceof TextedButton)
                ((TextedButton) bb).unhighlight();
            this.selectedPrimaryIndex = this.primaries.indexOf(signal.source);


            this.updateLayout();
        }

        if(signal.signalName== UISignal.SignalName.SimpleSecondaryButtonMain&&signal.event==UISignal.Event.Unclicked){
            SimpleSecondaryElement sselement = (SimpleSecondaryElement)signal.source;
            sselement.unhighlight();
        }





        if(signal.signalName== UISignal.SignalName.JointOptionsDoneButton&&signal.event==UISignal.Event.Unclicked){



        }





        if(signal.signalName== UISignal.SignalName.Scroller){
            container.setY(this.scroll.getScrollValue());
            this.container.update();
        }

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
