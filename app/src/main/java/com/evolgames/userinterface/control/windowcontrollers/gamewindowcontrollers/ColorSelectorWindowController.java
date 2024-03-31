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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ColorSelectorWindowController
        extends LinearLayoutAdvancedWindowController<ColorSelectorWindow> {
    private final EditorUserInterface editorUserInterface;
    private final List<Button<?>> squareList = new ArrayList<>();
    private final AtomicInteger colorSlotCounter = new AtomicInteger();
    private ColoredProperties copy;
    private Button<ColorSelectorWindowController> selectedSlot;
    private ColoredProperties coloredProperties;
    private Action acceptAction;
    private ColorPanelProperties colorPanelProperties;

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
        this.editorUserInterface.undoShade();
        window.getSelector().getMesh().setVisible(false);
    }

    @Override
    public void openWindow() {
        super.openWindow();
        window.getSelector().getMesh().setVisible(true);
    }

    public void onColorSlotClicked(Button<?> slot) {
        ColorSelector colorSelector = window.getSelector();
        colorSelector.getRemoveColorButton().updateState(Button.State.NORMAL);
        ColorPanel panel = window.getPanel();
        for (Element square : panel.getColorsLayout().getContents()) {
            if (square != slot) {
                ((Button<?>) square).updateState(Button.State.NORMAL);
            }
        }
        coloredProperties.setColorSquareId(slot.getId());
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
        // update the selected color
        if (coloredProperties != null) {
            coloredProperties.getDefaultColor().set(window.getSelector().getMesh().getColor());
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

    public void addColorToPanel(Color color, int id, boolean addToProperties) {
        Button<ColorSelectorWindowController> square = window
                .getPanel()
                .addColor(color.getRed(), color.getGreen(), color.getBlue());
        square.setId(id);
        squareList.add(square);
        if (addToProperties) {
            colorPanelProperties.getSquarePropertiesList().add(new SquareProperties(id, new Color(color.getRed(), color.getGreen(), color.getBlue())));
        }
    }

    public void addColorToPanel(Color color, boolean addToProperties) {
        int id = colorSlotCounter.getAndIncrement();
        addColorToPanel(color, id, addToProperties);
    }

    public void addSelectorColorToPanel() {
        ColorSelector colorSelector = window.getSelector();
        addColorToPanel(new Color(colorSelector.getRed(), colorSelector.getGreen(), colorSelector.getBlue()), true);
    }

    public void updateMeshColor(float red, float green, float blue, float alpha) {
        Mesh mesh = window.getSelector().getMesh();
        mesh.setColor(red, green, blue);
        mesh.setAlpha(alpha);
    }

    public void removeColorFromPanel() {
        window.getPanel().removeColor(selectedSlot);
        this.colorPanelProperties.getSquarePropertiesList().removeIf(e -> e.getSquareId() == selectedSlot.getId());
        this.squareList.removeIf(e -> e.getId() == selectedSlot.getId());
        onColorSlotReleased(selectedSlot);
    }

    public void onColorSlotReleased(Button<?> square) {
        if (coloredProperties.getColorSquareId() == square.getId()) {
            coloredProperties.setColorSquareId(-1);
        }
        selectedSlot = null;
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
        this.editorUserInterface.shade(window);
        this.coloredProperties = coloredProperties;
        copy = (ColoredProperties) coloredProperties.clone();
        window.getSelector().getMesh().setColor(coloredProperties.getDefaultColor());
        if (coloredProperties.getColorSquareId() != -1) {
            Button<?> slot = window.getPanel().getColorSlotById(coloredProperties.getColorSquareId());
            if (slot != null) {
                onColorSlotClicked(slot);
                slot.updateState(Button.State.PRESSED);
            }
        } else {
            resetSlots();
        }
    }

    public void resetSlots() {
        squareList.forEach(square -> {
            square.updateState(Button.State.NORMAL);
        });
    }

    public void onAccept() {
        if (acceptAction != null) {
            acceptAction.performAction();
        }
        closeWindow();
    }

    public void onRefuse() {
        coloredProperties.getDefaultColor().set(copy.getDefaultColor());
        coloredProperties.setColorSquareId(copy.getColorSquareId());
        if (editorUserInterface.getToolModel() != null) {
            editorUserInterface.getToolModel().updateMesh();
        }
        closeWindow();
    }

    public AtomicInteger getColorSlotCounter() {
        return colorSlotCounter;
    }
}
