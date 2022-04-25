package is.kul.learningandengine.graphicelements.ui;


import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

public class SimpleSecondaryElement extends  TextedButton {
  
   public int ID;
   
int PrimaryID;
    public SimpleSecondaryElement(String name) {
        super(name,ResourceManager.getInstance().simpleButtonTextureRegion,Button.ButtonType.Selector, SignalName.SimpleSecondaryButtonMain);


    }

    public SimpleSecondaryElement(String name,int mNumber) {
		super(name,ResourceManager.getInstance().simpleButtonTextureRegion,Button.ButtonType.Selector, SignalName.SimpleSecondaryButtonMain);
        ID = mNumber;

	}


    public SimpleSecondaryElement(String name, int ID, int ID2) {
		super(name,ResourceManager.getInstance().simpleButtonTextureRegion,Button.ButtonType.Selector, SignalName.SimpleSecondaryButtonMain);
        this.ID = ID;
        PrimaryID = ID2;
       
	
	}

    

	

}
