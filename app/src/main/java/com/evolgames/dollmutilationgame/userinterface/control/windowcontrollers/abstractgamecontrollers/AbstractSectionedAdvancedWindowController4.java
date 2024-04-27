package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController4<
        W extends AbstractLinearLayoutAdvancedWindow<?>,
        Primary extends Element,
        Secondary extends Element,
        Tertiary extends Element,
        Quaternary extends Element>
        extends AbstractSectionedAdvancedWindowController3<W, Primary, Secondary, Tertiary> {

    public abstract void onQuaternaryButtonClicked(Quaternary tertiary);

    public abstract void onQuaternaryButtonReleased(Quaternary tertiary);

    public abstract void onQuaternaryAdded(Quaternary tertiary);
}
