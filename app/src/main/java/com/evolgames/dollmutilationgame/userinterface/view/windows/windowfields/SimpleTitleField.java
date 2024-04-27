package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;

public class SimpleTitleField extends SimplePrimary<Text> {
    public SimpleTitleField(String title) {
        super(-1, new Text(title, 2));
    }
}
