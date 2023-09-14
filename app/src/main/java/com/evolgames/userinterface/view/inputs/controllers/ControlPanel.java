package com.evolgames.userinterface.view.inputs.controllers;

import com.evolgames.scenes.GameScene;

import org.andengine.engine.camera.Camera;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ControlPanel {
    private final ArrayList<ControlElement> registeredElements;
    private final Camera camera;
    private final GameScene scene;
    private AtomicInteger controlIdCounter;

    public ControlPanel(GameScene scene) {
        this.camera = scene.getMainCamera();
        this.scene = scene;
        this.registeredElements = new ArrayList<>();
        this.controlIdCounter = new AtomicInteger();
    }

    public ControlElement getControlElementByID(int ID) {
        for (ControlElement e : registeredElements) if (e.getID() == ID) return e;
        return null;
    }

    public void removeControlElement(int id) {
        ControlElement element = getControlElementByID(id);
        element.getAssociate().detachSelf();
        registeredElements.remove(element);
    }
    public ControlElement allocateController(float x, float y,ControlElement.Type type, ControllerAction action){
        boolean found = false;
        for(ControlElement controlElement:registeredElements){
            if(controlElement.getType()==type){
                controlElement.getAssociate().setPosition(x,y);
                controlElement.getAssociate().setUserData(controlElement);
                controlElement.setAction(action);
                showControlElement(controlElement.getID());
                return controlElement;
            }
        }
        ControlElement element = new ControlElement(ControlElement.Type.AnalogController, controlIdCounter.getAndIncrement(), action);
        addControlElement(element,x,y);
        element.getAssociate().setVisible(true);
        showControlElement(element.getID());
        return element;
    }

    private void addControlElement(ControlElement e,float x, float y) {
            if (!registeredElements.contains(e)) {
                switch (e.getType()) {
                    case AnalogController: {
                        AnalogController control = new AnalogController(x, y, this.camera, 0.1f, createAnalogListener());
                        control.setUserData(e);
                        e.setAssociate(control);
                        registeredElements.add(e);
                        scene.setChildScene(control);
                    }
                    break;

                    case DigitalController: {
                        DigitalController control = new DigitalController(x, y, this.camera, 0.1f, false, createBaseListener());
                        control.setUserData(e);
                        e.setAssociate(control);
                        registeredElements.add(e);
                        scene.setChildScene(control);
                    }
                    break;
                }
            }
    }

    private Controller.IOnScreenControlListener createBaseListener() {

        return (pBaseOnScreenControl, pValueX, pValueY) -> {
            if (Math.abs(pValueX) > 0.05f || Math.abs(pValueY) > 0.05)
                processDigitalControllerChangeSignal(pBaseOnScreenControl, pValueX, pValueY);
        };
    }

    private AnalogController.IAnalogOnScreenControlListener createAnalogListener() {

        return new AnalogController.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(AnalogController pAnalogOnScreenControl) {
                processAnalogControllerClickSignal(pAnalogOnScreenControl);
            }

            @Override
            public void onControlReleased(AnalogController pAnalogOnScreenControl) {
                processAnalogControllerReleaseSignal(pAnalogOnScreenControl);
            }

            @Override
            public void onControlChange(
                    Controller pBaseOnScreenControl,
                    float pValueX, float pValueY) {

                if (Math.abs(pValueX) > 0.05f || Math.abs(pValueY) > 0.05) {
                    processAnalogControllerChangeSignal(pBaseOnScreenControl, pValueX, pValueY);

                }
            }
        };
    }

    private void processAnalogControllerReleaseSignal(Controller source) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        if (controlElement == null) return;
        ControllerAction action = controlElement.getAction();
        action.controlReleased();
    }

    private void processAnalogControllerChangeSignal(Controller source, float valueX, float valueY) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlMoved(valueX, valueY);

    }

    private void processDigitalControllerChangeSignal(Controller source, float valueX, float valueY) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlMoved(valueX, valueY);

    }

    private void processAnalogControllerClickSignal(Controller source) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlClicked();

    }

    private void showControlElement(int ID) {
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(true);
        ((AnalogController) item.getAssociate()).setTouchEnabled(true);
    }

    public void hideControlElement(int ID) {
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(false);
        ((AnalogController) item.getAssociate()).setTouchEnabled(false);
    }
}
