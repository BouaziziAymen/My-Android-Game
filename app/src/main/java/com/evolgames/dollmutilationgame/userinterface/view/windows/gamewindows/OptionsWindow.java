package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Dummy;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.TwoLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractTwoLevelSectionedAdvancedWindow;
import com.evolgames.dollmutilationgame.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.dollmutilationgame.userinterface.view.ShiftText;

public class OptionsWindow
        extends AbstractTwoLevelSectionedAdvancedWindow<
        SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>> {

    private ShiftText<OptionsWindowController> shiftingText;

    public OptionsWindow(float pX, float pY, OptionsWindowController controller) {
        super(pX, pY, 5, 8, false, controller);
        Element mainField = new Dummy();
        layout.addDummySection(mainField);
        mainField.setPadding(5);

        addShiftingText(controller);
        controller.init();
        this.setDepth(10);
    }

    private void addShiftingText(OptionsWindowController controller) {
        shiftingText =
                new ShiftText<>(8, 32 * 5 - 12 - 5, 8, controller);
        addElement(shiftingText);
        shiftingText.setDepth(3);
        shiftingText.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
    }

    public void setShiftingText(String text){
        shiftingText.setText(text);
    }
    @Override
    protected TwoLevelSectionLayout<SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>>
    createLayout() {
        return new TwoLevelSectionLayout<>(
                12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }

    @Override
    public SimplePrimary createPrimary(int primaryKey) {
        return null;
    }

    @Override
    public SimpleSecondary createSecondary(int primaryKey, int secondaryKey) {
        return null;
    }

    @Override
    public SimpleTertiary createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        return null;
    }

    public void addPrimary(SimplePrimary primaryInterface) {
        getLayout().addPrimary(primaryInterface.getPrimaryKey(), primaryInterface, false);
    }

    public void removePrimary(int primaryKey) {
        getLayout().removePrimary(primaryKey);
    }

    public void addSecondary(SimpleSecondary secondary) {
        getLayout().addSecondary(secondary.getPrimaryKey(), secondary.getSecondaryKey(), secondary);
    }
}
