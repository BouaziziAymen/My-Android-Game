package com.evolgames.dollmutilationgame.userinterface.view.windows;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ThreeLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

public abstract class AbstractThreeLevelSectionedAdvancedWindow<
        Primary extends Element,
        Secondary extends Element,
        Tertiary extends Element,
        Quaternary extends Element>
        extends AbstractSectionedAdvancedWindow<
        ThreeLevelSectionLayout<Primary, Secondary, Tertiary, Quaternary>> {
    public AbstractThreeLevelSectionedAdvancedWindow(
            float pX,
            float pY,
            int rows,
            int columns,
            boolean hasPadding,
            LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, hasPadding, controller);
    }

    public abstract Primary createPrimary(int primaryKey);

    public abstract Secondary createSecondary(int primaryKey, int secondaryKey);

    public abstract Tertiary createTertiary(int primaryKey, int secondaryKey, int tertiaryKey);

    public abstract Quaternary createQuaternary(
            int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey);

    Primary addPrimary(int primaryKey, boolean isActive) {
        Primary primaryField = createPrimary(primaryKey);
        layout.addPrimary(primaryKey, primaryField, isActive);
        getController().onLayoutChanged();
        return primaryField;
    }

    Secondary addSecondary(int primaryKey, int secondaryKey) {
        Secondary secondary = createSecondary(primaryKey, secondaryKey);
        layout.addSecondary(primaryKey, secondaryKey, secondary);
        return secondary;
    }

    Tertiary addTetiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        Tertiary tertiary = createTertiary(primaryKey, secondaryKey, tertiaryKey);
        layout.addTertiary(primaryKey, secondaryKey, tertiaryKey, tertiary);
        return tertiary;
    }

    Quaternary addQuaternary(int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey) {
        Quaternary quaternary = createQuaternary(primaryKey, secondaryKey, tertiaryKey, quaternaryKey);
        layout.addQuaternary(primaryKey, secondaryKey, tertiaryKey, quaternaryKey, quaternary);
        return quaternary;
    }
}
