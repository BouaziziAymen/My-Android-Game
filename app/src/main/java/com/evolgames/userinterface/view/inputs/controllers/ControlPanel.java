package com.evolgames.userinterface.view.inputs.controllers;

import com.evolgames.scenes.GameScene;

import org.andengine.engine.camera.Camera;

import java.util.ArrayList;
import java.util.Objects;

public class ControlPanel {
    private final ArrayList<ControlElement> registeredElements;
    private final Camera camera;
    private final GameScene scene;

    public ControlPanel(GameScene scene) {
        this.camera = scene.getMainCamera();
        this.scene = scene;
        this.registeredElements = new ArrayList<>();
    }

    public ControlElement getControlElementByID(int ID) {
        for (ControlElement e : registeredElements) if (e.getID() == ID) return e;
        return null;
    }

    public void addControlElements(ControlElement... elements) {
        for (ControlElement e : elements) {
            if (!registeredElements.contains(e)) {
                switch (e.getType()) {
                    case AnalogController: {
                        float x = 800 - 64f / 2;
                        float y = 64f / 2;
                        AnalogController control = new AnalogController(x, y, this.camera, 0.1f, createAnalogListener());
                        control.setUserData(e);
                        e.setAssociate(control);
                        registeredElements.add(e);
                        scene.setChildScene(control);
                        control.setVisible(false);
                    }
                    break;

                    case DigitalController: {
                        float x = 800 - 64f / 2f;
                        float y = 64f / 2f;
                        DigitalController control = new DigitalController(x, y, this.camera, 0.1f, false, createBaseListener());
                       // control.setType(Controller.Type.XYD);
                        control.setUserData(e);
                        e.setAssociate(control);
                        registeredElements.add(e);
                        scene.setChildScene(control);
                        control.setVisible(false);

                    }
                    break;
                }
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
            public void onControlReleased(AnalogController pAnalogOnScreenControl){
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
        if(controlElement==null)return;
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

    public void showControlElement(int ID) {
        //scene.setChildScene((MyAnalogOnScreenControl) this.getControlElementByID(0).getAssociate());
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(true);
        ((AnalogController)item.getAssociate()).setTouchEnabled(true);
    }

    public void hideControlElement(int ID) {
        //scene.clearChildScene();
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(false);
        ((AnalogController)item.getAssociate()).setTouchEnabled(false);
    }
}
