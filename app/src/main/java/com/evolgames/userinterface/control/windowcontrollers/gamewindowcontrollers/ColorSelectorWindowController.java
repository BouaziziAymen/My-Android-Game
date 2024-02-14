package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.ColorPanelProperties;
import com.evolgames.entities.properties.ColoredProperties;
import com.evolgames.entities.properties.SquareProperties;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ColorPanel;
import com.evolgames.userinterface.view.inputs.ColorSelector;
import com.evolgames.userinterface.view.windows.gamewindows.ColorSelectorWindow;

import org.andengine.entity.primitive.Mesh;
import org.andengine.util.adt.color.Color;

public class ColorSelectorWindowController
    extends LinearLayoutAdvancedWindowController<ColorSelectorWindow> {
  private final EditorUserInterface editorUserInterface;
  private Color copy;
  private Button selectedSlot;
  private ColoredProperties selectedColoredProperties;
  private Action acceptAction;
  private  ColorPanelProperties colorPanelProperties;

  public ColorSelectorWindowController(EditorUserInterface editorUserInterface) {
    this.editorUserInterface = editorUserInterface;
  }

  public void setColorPanelProperties(ColorPanelProperties colorPanelProperties) {
    this.colorPanelProperties = colorPanelProperties;
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

  public void onColorSlotClicked(Button<?> slot) {
    this.selectedSlot = slot;
    this.selectedColoredProperties.setColorSquareId(slot.getId());
    ColorSelector colorSelector = window.getSelector();
    colorSelector.getRemoveColorButton().updateState(Button.State.NORMAL);
    ColorPanel panel = window.getPanel();
    for (Element square : panel.getColorsLayout().getContents()) {
      if (square != slot) ((Button<?>) square).updateState(Button.State.NORMAL);
    }
    colorSelector.setRed(slot.getRed());
    colorSelector.setGreen(slot.getGreen());
    colorSelector.setBlue(slot.getBlue());
    colorSelector.setAlpha(1f);
    colorSelector.updateColorRGB();
    updateSelectors();
    onColorUpdated();
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
    Color color = window.getSelector().getMesh().getColor();
    updateMeshColor(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    // update the selected color
    if (selectedColoredProperties != null) {
      selectedColoredProperties.getDefaultColor().set(window.getSelector().getMesh().getColor());
    }
    // update the scene mesh color
    editorUserInterface.getToolModel().updateMesh();
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
  public void addColorToPanel(Color color) {
    window
            .getPanel()
            .addColor(color.getRed(), color.getGreen(), color.getBlue());
  }
  public void addColorToPanel() {
    ColorSelector colorSelector = window.getSelector();
    Button<ColorSelectorWindowController> square = window
            .getPanel()
            .addColor(colorSelector.getRed(), colorSelector.getGreen(), colorSelector.getBlue());
    colorPanelProperties.getSquarePropertiesList().add(new SquareProperties(square.getId(),new Color(colorSelector.getRed(), colorSelector.getGreen(), colorSelector.getBlue())));
  }

  public void updateMeshColor(float red, float green, float blue, float alpha) {
    Mesh mesh = window.getSelector().getMesh();
    mesh.setColor(red, green, blue);
    mesh.setAlpha(alpha);
  }
  public void removeColorFromPanel() {
    window.getPanel().removeColor(selectedSlot);
    onColorSlotReleased(selectedSlot);
  }

  public void onColorSlotReleased(Button square) {
    selectedSlot = null;
    this.colorPanelProperties.getSquarePropertiesList().removeIf(e->e.getSquareId()==square.getId());
    this.selectedColoredProperties.setColorSquareId(-1);
    window.getSelector().getRemoveColorButton().updateState(Button.State.DISABLED);
    ColorSelector colorSelector = window.getSelector();
    colorSelector.setRed(1f);
    colorSelector.setGreen(1f);
    colorSelector.setBlue(1f);
    colorSelector.setAlpha(1f);
    colorSelector.updateColorRGB();
    updateSelectors();
    onColorUpdated();
  }

  public void bindProperties(ColoredProperties coloredProperties) {
    this.selectedColoredProperties = coloredProperties;
    copy = new Color(coloredProperties.getDefaultColor());
    window.getSelector().getMesh().setColor(selectedColoredProperties.getDefaultColor());
    if(coloredProperties.getColorSquareId()!=-1) {
      Button<?> slot = window.getPanel().getColorSlotById(coloredProperties.getColorSquareId());
      onColorSlotClicked(slot);
      slot.updateState(Button.State.PRESSED);
    }
  }

  public void onAccept() {
    if (acceptAction != null) {
      acceptAction.performAction();
    }
    closeWindow();
  }

  public void onRefuse() {
    selectedColoredProperties.getDefaultColor().set(copy);
    if (editorUserInterface.getToolModel() != null) {
      editorUserInterface.getToolModel().updateMesh();
    }
    closeWindow();
  }
}
