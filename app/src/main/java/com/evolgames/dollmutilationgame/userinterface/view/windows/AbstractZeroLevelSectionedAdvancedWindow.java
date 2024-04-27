package com.evolgames.dollmutilationgame.userinterface.view.windows;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ZeroLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

public abstract class AbstractZeroLevelSectionedAdvancedWindow<Primary extends Element>
        extends AbstractSectionedAdvancedWindow<ZeroLevelSectionLayout<Primary>> {
    public AbstractZeroLevelSectionedAdvancedWindow(
            float pX,
            float pY,
            int rows,
            int columns,
            boolean hasPadding,
            LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, hasPadding, controller);
    }

    public Primary addPrimary(int primaryKey, boolean isActive) {
        Primary primaryField = createPrimary(primaryKey);
        layout.addPrimary(primaryKey, primaryField, isActive);
        return primaryField;
    }

    public abstract Primary createPrimary(int primaryKey);
}
