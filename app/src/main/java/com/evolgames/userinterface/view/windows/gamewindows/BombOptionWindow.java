package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BombOptionController;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class BombOptionWindow extends SettingsWindow {
    public BombOptionWindow(float pX, float pY, BombOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Bomb Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        Panel mPanel = new Panel(0, -64, 4, true, true);

        mPanel
                .getCloseButton()
                .setBehavior(
                        new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getCloseButton()) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onCancelSettings();
                            }
                        });

        mPanel
                .getAcceptButton()
                .setBehavior(
                        new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getAcceptButton()) {
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
