package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.view.inputs.ColorPanel;
import com.evolgames.userinterface.view.inputs.ColorSelector;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;

public class ColorSelectorWindow extends AbstractLinearLayoutAdvancedWindow<LinearLayout> {


    private final ColorPanel mPanel;

    public ColorSelector getSelector() {
        return selector;
    }

    public TitledQuantity getValueSelector() {
        return valueSelector;
    }

    public TitledQuantity getAlphaSelector() {
        return alphaSelector;
    }

    private final ColorSelector selector;
    private final TitledQuantity<ColorSelectorWindowController> valueSelector;
    private final TitledQuantity<ColorSelectorWindowController> alphaSelector;

    public ColorSelectorWindow(float X, float Y, ColorSelectorWindowController controller) {
        super(X, Y+64, 2, 5,false,controller);
        this.selector = new ColorSelector(0,2*32-2,controller);
        selector.setLowerBottomX(5*32/2-selector.getWidth()/2);

        this.valueSelector = new TitledQuantity("Value:", 10,1,0,52);
        this.alphaSelector = new TitledQuantity("Opacity:", 10,1,0,52);

        valueSelector.setRatio(1f);
        alphaSelector.setRatio(1f);


        valueSelector.getAttachment().setBehavior(new QuantityBehavior<ColorSelectorWindowController>(controller,valueSelector.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity quantity) {
             controller.onValueUpdated(quantity.getRatio());
            }
        });

        alphaSelector.getAttachment().setBehavior(new QuantityBehavior<ColorSelectorWindowController>(controller,alphaSelector.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity quantity) {
             controller.onOpacityUpdated(quantity.getRatio());
            }
        });
        getLayout().addToLayout(valueSelector);
        getLayout().addToLayout(alphaSelector);

        addElement(this.selector);
        selector.setDepth(5);

        mPanel = new ColorPanel(0, -64,controller);

        mPanel.getCloseButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getCloseButton()) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onRefuse();
             controller.closeWindow();
            }
        });



        mPanel.getAcceptButton().setBehavior(new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getAcceptButton()) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                controller.onAccept();
controller.closeWindow();

            }
        });
        mPanel.setLowerBottomX(getWidth()/2- mPanel.getWidth()/2);
        addElement(mPanel);



    }

    @Override
    protected LinearLayout createLayout() {
        return new LinearLayout(12,getLocalVisibilitySup(),LinearLayout.Direction.Vertical);
    }

    public ColorPanel getPanel() {
        return mPanel;
    }
}
