package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.sections.Section;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.SectionLayout;

public abstract class AbstractSectionedAdvancedWindow<T extends SectionLayout<?,?>> extends AbstractLinearLayoutAdvancedWindow<T> {

    AbstractSectionedAdvancedWindow(float pX, float pY, int rows, int columns, LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, controller);


    }






}
