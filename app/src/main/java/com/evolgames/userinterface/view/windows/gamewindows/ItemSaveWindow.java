package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class ItemSaveWindow extends SettingsWindow {

    private final Panel panel;

    public ItemSaveWindow(float pX, float pY, ItemSaveWindowController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Saving Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        panel = new Panel(0, -64, 4, true, true);

        panel
                .getCloseButton()
                .setBehavior(
                        new ButtonBehavior<AdvancedWindowController<?>>(controller, panel.getCloseButton()) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onCancelSettings();
                            }
                        });

        panel
                .getAcceptButton()
                .setBehavior(
                        new ButtonBehavior<AdvancedWindowController<?>>(controller, panel.getAcceptButton()) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onSubmitSettings();
                            }
                        });

        panel.setLowerBottomX(getWidth() / 2 - panel.getWidth() / 2);
        addElement(panel);
        setVisible(false);
    }

    public Panel getPanel() {
        return panel;
    }
}
