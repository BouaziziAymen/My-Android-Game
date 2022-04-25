package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.inputs.TextField;

public class TitledTextField<C extends AdvancedWindowController<?>>  extends TitledField<TextField<C>> {

    public TitledTextField(String titleString, int length) {
        super(titleString,new TextField<>(length));

    }
    public TitledTextField(String titleString, int length,float margin,float minX) {
        super(titleString,new TextField<>(length),margin,minX);
    }

}
