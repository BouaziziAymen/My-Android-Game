package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.layouts.SectionLayout;

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
