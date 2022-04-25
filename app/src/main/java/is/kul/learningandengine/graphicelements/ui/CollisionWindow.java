package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;

public class CollisionWindow extends ScrollableOrganizedWindow {
    ArrayList<CategoryElement> categories;



    private int selectedPrimaryIndex;

    private final CollisionWindowMainElement main;

    public AtomicInteger categoryId = new AtomicInteger();

public ArrayList<CategoryElement> getCategories(){
    return categories;
}

    public CollisionWindow(float x, float y){
        super(x, y, 4, 5, ResourceManager.getInstance().vbom,true);

        this.categories = new ArrayList<CategoryElement>();

        this.main = new CollisionWindowMainElement("Categories");


        this.createLayout();
        addCategory("Default");
        addCategory("Armor1");
        addCategory("Armor2");
        addCategory("Armor3");
        addCategory("Building");
        addCategory("Doll");
        addCategory("Ground");
        addCategory("Vegetation");
        addCategory("Weapon1");
        addCategory("Weapon2");
        addCategory("Weapon3");




    }
    void addCategory(){
        CategoryElement button = new CategoryElement(categoryId.getAndIncrement());
        this.categories.add(button);
        updateLayout();

    }

    void addCategory(String name){
        CategoryElement button = new CategoryElement(name,categoryId.getAndIncrement());
        this.categories.add(button);
        updateLayout();

    }



    int addCategoryFromOutside(){
        addCategory();
        return categoryId.getAndIncrement();
    }

    void createLayout(){
        int k = 1;


        this.main.setActive(true);
        attachChild(this.main,0,0);

        for(int i = 0; i< this.categories.size(); i++){
            attachChild(this.categories.get(i),k,0);

            k++;

        }


        this.scroll.update(k);
        container.setY(this.scroll.getScrollValue());
        this.container.update();
    }

    void updateLayout(){



        int k = 1;
        for(int i = 0; i< this.categories.size(); i++){

            if(container.containsEntity(this.categories.get(i)))
                moveChild(this.categories.get(i),k);
            else
                attachChild(this.categories.get(i),k,0);




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



        if(signal.signalName== UISignal.SignalName.AddCategoryButton&&signal.event==UISignal.Event.Clicked){
            this.addCategory();
            if(categories.size()==15)main.close();
            //ui.out_select_Joint(((CategoryElement)signal.source).ID);

        }





        if(signal.signalName== UISignal.SignalName.CollisionButtonRemove&&signal.event==UISignal.Event.Unclicked){


            categories.remove(signal.source);
            this.container.detachChild(signal.source);
            updateLayout();
           // ui.out_remove_Joint(((JointElement)signal.source).ID);


        }


        if(signal.signalName== UISignal.SignalName.CollisionButtonOptions){
            CategoryElement element = (CategoryElement) signal.source;
           // ui.in_signal_start_Joint_Option_Window(element.ID);


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






}
