package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Panel;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.BoxLayout;

public class ColorPanel extends Panel {
    public static int N = 6;
    public static int NUMBER_SQUARES = 8;
    public static float MARGIN = 5f;
    public static float BLOCK_WIDTH = 32f;
    public static float USEFUL_WIDTH_SIDE_BLOCK = 15f;
    public static float BLOCK_HEIGHT = 64f;
    public static float SQUARE_SIDE = 16f;
    private final BoxLayout colorsLayout;
    private final ColorSelectorWindowController mController;

    public ColorPanel(float pX, float pY, ColorSelectorWindowController controller) {
        super(pX, pY, N, true, true);
        float effectiveLength = USEFUL_WIDTH_SIDE_BLOCK * 2 + BLOCK_WIDTH * N - 2 * N;
        float deltax =
                (effectiveLength - NUMBER_SQUARES * SQUARE_SIDE - (NUMBER_SQUARES - 1) * MARGIN) / 2f;
        colorsLayout =
                new BoxLayout(55 + deltax, BLOCK_HEIGHT - SQUARE_SIDE - MARGIN - 9, MARGIN, 2, 8);
        addElement(colorsLayout);

        this.mController = controller;
    }

    public Button<ColorSelectorWindowController> addColor(final float pRed, final float pGreen, final float pBlue) {
        Button<ColorSelectorWindowController> square =
                new Button<>(
                        ResourceManager.getInstance().squareTextureRegion, Button.ButtonType.Selector, false);
        square.setBounds(new RectangularBounds(square, 20, 20));
        colorsLayout.addToLayout(square);
        square.setColor(pRed, pGreen, pBlue);
        square.setBehavior(
                new ButtonBehavior<ColorSelectorWindowController>(mController, square) {
                    @Override
                    public void informControllerButtonClicked() {
                        mController.onColorSlotClicked(square);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        mController.onColorSlotReleased(square);
                    }
                });
        return square;
    }

    public BoxLayout getColorsLayout() {
        return colorsLayout;
    }

    public void removeColor(Button<?> selectedSlot) {
        colorsLayout.removeElement(selectedSlot);
    }

    public Button<?> getColorSlotById(int id) {
        return (Button<?>) colorsLayout.getContents().stream().filter(b -> b.getId() == id).findAny().orElse(null);
    }

}
