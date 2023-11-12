package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import android.util.Log;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ColorPanel;
import com.evolgames.userinterface.view.inputs.ColorSelector;
import com.evolgames.userinterface.view.windows.gamewindows.ColorSelectorWindow;
import org.andengine.util.adt.color.Color;

public class ColorSelectorWindowController
    extends LinearLayoutAdvancedWindowController<ColorSelectorWindow> {
  private final EditorUserInterface editorUserInterface;
  private final Color copy = new Color(0, 0, 0, 0);
  private Button selectedSlot;
  private Color selectedColor;
  private Action acceptAction;

  public ColorSelectorWindowController(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  public void setAcceptAction(Action acceptAction) {
    this.acceptAction = acceptAction;
  }

  @Override
  public void closeWindow() {
    super.closeWindow();
    window.getSelector().getMesh().setVisible(false);
  }

  @Override
  public void openWindow() {
    super.openWindow();
    window.getSelector().getMesh().setVisible(true);
  }

  public void onColorSlotClicked(Button slot) {
    selectedSlot = slot;

    ColorSelector colorSelector = window.getSelector();
    colorSelector.getRemoveColorButton().updateState(Button.State.NORMAL);
    ColorPanel panel = window.getPanel();
    for (Element square : panel.getColorsLayout().getContents())
      if (square != slot) ((Button) square).updateState(Button.State.NORMAL);
    colorSelector.setRed(slot.getRed());
    colorSelector.setGreen(slot.getGreen());
    colorSelector.setBlue(slot.getBlue());
    colorSelector.setAlpha(1f);
    colorSelector.updateColorRGB();
    updateSelectors();
  }

  private void updateSelectors() {
    ColorSelector colorSelector = window.getSelector();
    window.getAlphaSelector().setRatio(colorSelector.getAlpha());
    window.getValueSelector().setRatio(colorSelector.getValue());
  }

  public void onHueAndSaturationUpdated() {
    ColorSelector colorSelector = window.getSelector();
    colorSelector.updateColorHSV();
    updateSelectors();
    onColorUpdated();
  }

  private void onColorUpdated() {
    updateSelectorSlotColor();
    // update the selected color
    if (selectedColor != null) {
      selectedColor.set(window.getSelector().getMesh().getColor());
    }
    // update the scene mesh color
    Log.e("material", "tool model address:" + editorUserInterface.getToolModel().hashCode());
    editorUserInterface.getToolModel().updateMesh();
  }

  private void updateSelectorSlotColor() {
    window.getSelector().updateMeshColor();
  }

  public void onOpacityUpdated(float ratio) {
    ColorSelector colorSelector = window.getSelector();
    colorSelector.setAlpha(ratio);
    onColorUpdated();
  }

  public void onValueUpdated(float ratio) {
    ColorSelector colorSelector = window.getSelector();
    colorSelector.setValue(ratio);
    colorSelector.updateColorHSV();
    onColorUpdated();
  }

  public void addColorToPanel() {
    ColorSelector colorSelector = window.getSelector();
    window
        .getPanel()
        .addColor(colorSelector.getRed(), colorSelector.getGreen(), colorSelector.getBlue());
  }

  public void removeColorFromPanel() {
    window.getPanel().removeColor(selectedSlot);
    onColorSlotReleased(selectedSlot);
  }

  public void onColorSlotReleased(Button square) {
    selectedSlot = null;
    window.getSelector().getRemoveColorButton().updateState(Button.State.DISABLED);
  }

  private void setSelectedColor(Color selectedColor) {

    this.selectedColor = selectedColor;
    window.getSelector().getMesh().setColor(selectedColor);
  }

  public void bindToColor(Color colorToChange) {
    copy.set(colorToChange);
    setSelectedColor(colorToChange);
  }

  public void onAccept() {
    if (acceptAction != null) {
      acceptAction.performAction();
    }
    closeWindow();
  }

  public void onRefuse() {
    selectedColor.set(copy);
    if (editorUserInterface.getToolModel() != null) {
      editorUserInterface.getToolModel().updateMesh();
    }
    closeWindow();
  }
}
