package com.evolgames.userinterface.view.inputs.controllers;

import com.evolgames.scenes.GameScene;

import org.andengine.engine.camera.Camera;

import java.util.ArrayList;
import java.util.Objects;

public class ControlPanel {
    private ArrayList<ControlElement> registeredElements;
    private Camera camera;
    private GameScene scene;

    public ControlPanel(GameScene scene) {
        this.camera = scene.getMainCamera();
        this.scene = scene;
        registeredElements = new ArrayList<>();

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
                        float x = 800 - 128f / 2;
                        float y = 128f / 2;
                        MyAnalogOnScreenControl control = new MyAnalogOnScreenControl(x, y, this.camera, 0.1f, createAnalogListener());
                        control.setUserData(e);
                        e.setAssociate(control);
                        registeredElements.add(e);
                        control.getControlBase().setScale(0.75f);
                        control.getControlKnob().setScale(0.75f);
                        control.refreshControlKnobPosition();

                        scene.setChildScene(control);
                        control.setVisible(false);
                    }
                    break;

                    case DigitalController: {
                        float x = 800 - 128 / 2;
                        float y = 128 / 2;
                        MyDigitalOnScreenControl control = new MyDigitalOnScreenControl(x, y, this.camera, 0.1f, false, createBaseListener());
                        control.setType(MyBaseOnScreenControl.Type.XYD);
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

    private MyBaseOnScreenControl.IOnScreenControlListener createBaseListener() {

        return (pBaseOnScreenControl, pValueX, pValueY) -> {
            if (Math.abs(pValueX) > 0.05f || Math.abs(pValueY) > 0.05)
                processDigitalControllerChangeSignal(pBaseOnScreenControl, pValueX, pValueY);
        };
    }

    private MyAnalogOnScreenControl.IAnalogOnScreenControlListener createAnalogListener() {

        return new MyAnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlClick(MyAnalogOnScreenControl pAnalogOnScreenControl) {
                processAnalogControllerClickSignal(pAnalogOnScreenControl);
            }
            @Override
            public void onControlReleased(MyAnalogOnScreenControl pAnalogOnScreenControl){
                processAnalogControllerReleaseSignal(pAnalogOnScreenControl);
            }

            @Override
            public void onControlChange(
                    MyBaseOnScreenControl pBaseOnScreenControl,
                    float pValueX, float pValueY) {

                if (Math.abs(pValueX) > 0.05f || Math.abs(pValueY) > 0.05) {
                    processAnalogControllerChangeSignal(pBaseOnScreenControl, pValueX, pValueY);

                }
            }
        };
    }

    private void processAnalogControllerReleaseSignal(MyBaseOnScreenControl source) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        if(controlElement==null)return;
        ControllerAction action = controlElement.getAction();
        action.controlReleased();
    }

    private void processAnalogControllerChangeSignal(MyBaseOnScreenControl source, float valueX, float valueY) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlMoved(valueX, valueY);

    }

    private void processDigitalControllerChangeSignal(MyBaseOnScreenControl source, float valueX, float valueY) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlMoved(valueX, valueY);

    }

    private void processAnalogControllerClickSignal(MyBaseOnScreenControl source) {
        ControlElement controlElement = (ControlElement) source.getUserData();
        ControllerAction action = controlElement.getAction();
        action.controlClicked();

    }

    public void showControlElement(int ID) {
        //scene.setChildScene((MyAnalogOnScreenControl) this.getControlElementByID(0).getAssociate());
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(true);
        ((MyAnalogOnScreenControl)item.getAssociate()).setTouchEnabled(true);
    }

    public void hideControlElement(int ID) {
        //scene.clearChildScene();
        ControlElement item = Objects.requireNonNull(this.getControlElementByID(ID));
        item.setVisible(false);
        ((MyAnalogOnScreenControl)item.getAssociate()).setTouchEnabled(false);
    }
}
