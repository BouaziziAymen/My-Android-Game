package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.basics.Text;

public class SimpleTitleField extends SimplePrimary<Text> {
    public SimpleTitleField(String title) {
        super(0, new Text(title,2));
    }
}
