package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsWindowController;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.ThreeLevelSectionLayout;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.windows.AbstractThreeLevelSectionedAdvancedWindow;

public abstract class SettingsWindow
        extends AbstractThreeLevelSectionedAdvancedWindow<
        SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>> {

    private Panel panel;

    public SettingsWindow(
            float pX,
            float pY,
            int rows,
            int columns,
            LinearLayoutAdvancedWindowController<?> controller) {
        super(pX, pY, rows, columns, false, controller);
    }

    public void addPrimary(SimplePrimary<?> primaryInterface) {
        getLayout().addPrimary(primaryInterface.getPrimaryKey(), primaryInterface, false);
    }

    public void addSecondary(SimpleSecondary<?> secondaryElement) {
        getLayout()
                .addSecondary(
                        secondaryElement.getPrimaryKey(), secondaryElement.getSecondaryKey(), secondaryElement);
    }

    public void addTertiary(SimpleTertiary<?> tertiaryElement) {
        getLayout()
                .addTertiary(
                        tertiaryElement.getPrimaryKey(),
                        tertiaryElement.getSecondaryKey(),
                        tertiaryElement.getTertiaryKey(),
                        tertiaryElement);
    }

    public void addQuaternary(SimpleQuaternary<?> quaternaryElement) {
        getLayout()
                .addQuaternary(
                        quaternaryElement.getPrimaryKey(),
                        quaternaryElement.getSecondaryKey(),
                        quaternaryElement.getTertiaryKey(),
                        quaternaryElement.getQuaternaryKey(),
                        quaternaryElement);
    }

    @Override
    protected ThreeLevelSectionLayout<
            SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>>
    createLayout() {
        return new ThreeLevelSectionLayout<>(
                8, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }

    @Override
    public SimplePrimary<?> createPrimary(int primaryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleSecondary<?> createSecondary(int primaryKey, int secondaryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleTertiary<?> createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleQuaternary<?> createQuaternary(
            int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey) {
        throw new UnsupportedOperationException();
    }

    protected void createPanel(SettingsWindowController<?> controller) {
        this.panel = new Panel(0, -64, 4, true, true);

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
    }

    public Panel getPanel() {
        return panel;
    }
}
