package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ZeroLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractZeroLevelSectionedAdvancedWindow;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.jointswindow.JointField;
import com.evolgames.gameengine.R;

public class JointsWindow extends AbstractZeroLevelSectionedAdvancedWindow<JointField> {
    private final JointWindowController jointWindowController;

    public JointsWindow(float pX, float pY, JointWindowController controller) {
        super(pX, pY, 6, 8, true, controller);
        jointWindowController = controller;
        Text text = new Text(ResourceManager.getInstance().getString(R.string.joints_title), 2);
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
        return new ZeroLevelSectionLayout<>(
                12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }
}
