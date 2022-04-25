package com.evolgames.userinterface.view.windows.gamewindows;

import android.util.Log;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.ThreeLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractThreeLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class DecorationSettingsWindow  extends SettingsWindow {

    public DecorationSettingsWindow(float pX, float pY, DecorationSettingsWindowController controller) {
        super(pX, pY, 8, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Decoration Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        Panel mPanel = new Panel(0, -64, 4, true, true);

        mPanel.getCloseButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getCloseButton()) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onCancelSettings();
            }
        });


        mPanel.getAcceptButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getAcceptButton()) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onSubmitSettings();

            }
        });
        mPanel.setLowerBottomX(getWidth() / 2 - mPanel.getWidth() / 2);
        addElement(mPanel);


    }
}
