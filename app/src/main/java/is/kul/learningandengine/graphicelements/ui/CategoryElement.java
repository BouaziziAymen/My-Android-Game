package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class CategoryElement  extends NameElement {

    private  Button remove;


    public int ID;


public String getName(){
    return this.alphanumericInput.getText();
}
    public CategoryElement(int ID) {
        super("Category "+ID);
        this.ID = ID;

        VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;


        this.remove = new Button(ResourceManager.getInstance().bodyButtonTextureRegion.getWidth()+ResourceManager.getInstance().smallOptionsTextureRegion.getWidth()+ResourceManager.getInstance().removeTextureRegion.getWidth()/2+20, getHeight()/2,
                ResourceManager.getInstance().removeTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.CollisionButtonRemove);


        attachChild(this.remove);


        // TODO Auto-generated constructor stub
    }

    public CategoryElement(String string ,int ID) {
        super(string);
        this.ID = ID;

        VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;



        // TODO Auto-generated constructor stub
    }





    @Override
    public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

        boolean touched = false;
        if(isVisible()){

            if(super.OnTouchScene(pSceneTouchEvent))touched=true;

if(remove!=null)
            if(this.remove.OnTouchScene(pSceneTouchEvent))touched=true;

        }
        return touched;
    }


}


