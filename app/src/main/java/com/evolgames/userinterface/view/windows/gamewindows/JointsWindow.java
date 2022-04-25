package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.ZeroLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractZeroLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.jointswindow.JointField;

public class JointsWindow extends AbstractZeroLevelSectionedAdvancedWindow<JointField> {
    private final JointWindowController jointWindowController;

    public JointsWindow(float pX, float pY, JointWindowController controller) {
        super(pX, pY, 6, 8, controller);
        jointWindowController = controller;
        Text text = new Text("Joints:",2);
        text.setPadding(5);
        layout.addDummySection(text);
        createScroller();
        layout.updateLayout();




    }

    @Override
    public JointField createPrimary(int primaryKey) {
        return new JointField(primaryKey, jointWindowController);
    }


    @Override
    protected ZeroLevelSectionLayout<JointField> createLayout() {
        return new ZeroLevelSectionLayout<>(12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }
}