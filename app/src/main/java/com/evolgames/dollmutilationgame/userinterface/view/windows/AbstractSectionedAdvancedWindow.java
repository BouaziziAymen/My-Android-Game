package com.evolgames.dollmutilationgame.userinterface.view.windows;

import com.evolgames.dollmutilationgame.userinterface.view.layouts.SectionLayout;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

public abstract class AbstractSectionedAdvancedWindow<T extends SectionLayout<?, ?>>
        extends AbstractLinearLayoutAdvancedWindow<T> {
    AbstractSectionedAdvancedWindow(
            float pX,
            float pY,
            int rows,
            int columns,
            boolean hasPadding,
            LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, hasPadding, controller);
    }
}
