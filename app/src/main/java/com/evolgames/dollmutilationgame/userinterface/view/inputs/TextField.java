package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.Temporal;

public class TextField<C extends AdvancedWindowController<?>>
        extends InputField<C, TextFieldBehavior<C>> implements Temporal {
    private final Text mText;

    public TextField(int mLength) {
        this(0, 0, mLength);
    }

    public TextField(float pX, float pY, int mLength) {
        super(pX, pY, mLength);
        mText = new Text(10, 0, "", 2);
        mText.setLowerBottomY(getHeight() / 2);
        addElement(mText);
    }

    public Text getText() {
        return mText;
    }

    void validate() {
        getBehavior().validate();
    }

    @Override
    public void onStep() {
        getBehavior().onStep();
    }

    public String getTextString() {
        return getBehavior().getTextString();
    }

    @Override
    public boolean isVisible() {
       for(Element e:this.layout.getContents()){
           if(!e.isVisible()){
               return false;
           }
       }
       return true;
    }
}
