package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;

public class SimpleTitleField extends SimplePrimary<Text> {
    public SimpleTitleField(String title) {
        super(0, new Text(title, 2));
    }
}
