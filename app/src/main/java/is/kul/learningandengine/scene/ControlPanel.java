package is.kul.learningandengine.scene;

import android.util.Log;

import org.andengine.engine.camera.Camera;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.controls.MyAnalogOnScreenControl;
import is.kul.learningandengine.controls.MyBaseOnScreenControl;
import is.kul.learningandengine.controls.MyDigitalOnScreenControl;

public class ControlPanel {
private ArrayList<ControlElement> registeredElements;
private Camera camera;
GameScene scene;
ControlElement getControlElementByID(int ID){
    for(ControlElement e:registeredElements)if(e.getID()==ID)return e;
    return null;
}
    ControlPanel(GameScene scene) {
        this.camera = scene.camera;
        this.scene = scene;
        registeredElements = new ArrayList<ControlElement>();

    }
    public void addControlElements(ControlElement... elements) {
for(ControlElement e:elements){
    if(!registeredElements.contains(e)) {
        switch (e.type){
            case AnalogController: {
                float x = 800 - 128/ 2;
                float y = 128 / 2;
                MyAnalogOnScreenControl control = new MyAnalogOnScreenControl(x, y, this.camera
, 0.1f,
                        createAnalogListener());
                control.setUserData(e);
                e.setAssociate(control);
                registeredElements.add(e);
               scene.setChildScene(control);
               control.setOnControlClickEnabled(false);

                //control.setVisible(false);
            }
            break;

            case DigitalController:
            {
                float x = 800 - 128/ 2;
                float y = 128 / 2;
                MyDigitalOnScreenControl control = new MyDigitalOnScreenControl(x, y, this.camera

                       , 0.1f, false,
                        createBaseListener());
                control.setType(MyBaseOnScreenControl.Type.XYD);
                control.setUserData(e);
                e.setAssociate(control);
                registeredElements.add(e);
                scene.setChildScene(control);
                //control.setVisible(false);

            }
                break;
        }
    }
}
}

    MyBaseOnScreenControl.IOnScreenControlListener createBaseListener() {

    return new MyBaseOnScreenControl.IOnScreenControlListener(){

        @Override
        public void onControlChange(MyBaseOnScreenControl pBaseOnScreenControl, float pValueX, float pValueY) {
            if(Math.abs(pValueX)>0.05f||Math.abs(pValueY)>0.05)
                processDigitalControllerChangeSignal(pBaseOnScreenControl,pValueX,pValueY);
        }
    };
    }

    MyAnalogOnScreenControl.IAnalogOnScreenControlListener createAnalogListener() {
       MyAnalogOnScreenControl.IAnalogOnScreenControlListener listener = new MyAnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(MyAnalogOnScreenControl pAnalogOnScreenControl) {
                processAnalogControllerClickSignal(pAnalogOnScreenControl);
            }
            @Override
            public void onControlChange(
                    MyBaseOnScreenControl pBaseOnScreenControl,
                    float pValueX, float pValueY) {
                if(Math.abs(pValueX)>0.05f||Math.abs(pValueY)>0.05) {

                    processAnalogControllerChangeSignal(pBaseOnScreenControl, pValueX, pValueY);

                }
            }
        };

        return listener;
    }

    void processAnalogControllerChangeSignal(MyBaseOnScreenControl source,float valueX, float valueY){
        ControlElement controlElement = (ControlElement) source.getUserData();
        switch (controlElement.function){

            case ControlMoveableElement:
               // Log.e("move","process:"+valueX+"/"+valueY);
                scene.ControlMoveableElementChange(valueX,valueY);
                break;
        }
    }

    void processDigitalControllerChangeSignal( MyBaseOnScreenControl source,float valueX, float valueY){
        ControlElement controlElement = (ControlElement) source.getUserData();
        switch (controlElement.function){

            case ControlMoveableElement:
                scene.ControlMoveableElementChange(valueX,valueY);
                break;
        }
    }

    void processAnalogControllerClickSignal(MyBaseOnScreenControl source){
        ControlElement controlElement = (ControlElement) source.getUserData();
        switch (controlElement.function){

            case ControlMoveableElement:
                scene.ControlMoveableElementClick();
                break;
        }
    }

    public void showControlElement(int ID) {
    this.getControlElementByID(ID).setVisible(true);
    }
    public void hideControlElement(int ID) {
        this.getControlElementByID(ID).setVisible(false);
    }
}
