package com.evolgames.userinterface.view.inputs;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.layouts.BoxLayout;

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

  public void addColor(final float pRed, final float pGreen, final float pBlue) {

    Button<ColorSelectorWindowController> square =
        new Button<>(
            ResourceManager.getInstance().squareTextureRegion, Button.ButtonType.Selector, true);
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
  }

  public BoxLayout getColorsLayout() {
    return colorsLayout;
  }

  public void removeColor(Button selectedSlot) {
    colorsLayout.removeElement(selectedSlot);
  }
}
