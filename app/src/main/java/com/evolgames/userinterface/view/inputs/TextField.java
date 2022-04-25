package com.evolgames.userinterface.view.inputs;

import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.basics.Text;

public class TextField<C extends AdvancedWindowController<?>> extends InputField<C, TextFieldBehavior<C>> {
    private Text mText;

    // private Text errorText;

    public TextField(int mLength) {
        this(0, 0, mLength);
    }

    public TextField(float pX, float pY, int mLength) {
        super(pX, pY, mLength);
        mText = new Text(10, 0, "", 2);

        //errorText = new Text(10,mText.getLowerBottomY()-mText.getHeight()/2,"error",2);
        //errorText.setColor(1,0,0);
        mText.setLowerBottomY(getHeight() / 2);
        // setHeight(mText.getHeight()+errorText.getHeight()+20);

        //errorText.setLowerBottomY(mText.getLowerBottomY()-errorText.getHeight()-10);
        addElement(mText);
        //addElement(errorText);
    }

    @Override
    public boolean isTemporal() {
        return true;
    }

    public Text getText() {
        return mText;
    }


    void validate(){
        getBehavior().validate();
    }
    @Override
    public void onStep() {
        getBehavior().onStep();
    }

    public String getTextString() {
        return getBehavior().getTextString();
    }

}
