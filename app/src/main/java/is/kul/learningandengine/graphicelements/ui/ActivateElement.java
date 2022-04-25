package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;

public class ActivateElement extends  TextedButton {

    public int ID;

    public ActivateElement(String name,int id,ButtonType type) {
        super(name, ResourceManager.getInstance().simpleButtonTextureRegion, type, UISignal.SignalName.Activate);
        ID = id;

    }






}
