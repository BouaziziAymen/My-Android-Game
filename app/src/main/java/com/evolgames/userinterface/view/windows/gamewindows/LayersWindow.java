package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.TwoLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractTwoLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.BodyField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField;

public class LayersWindow
        extends AbstractTwoLevelSectionedAdvancedWindow<BodyField, LayerField, DecorationField> {
    private final LayerWindowController layerWindowController;

    public LayersWindow(float pX, float pY, LayerWindowController controller) {
        super(pX, pY, 8, 9, true, controller);
        layerWindowController = controller;
        Text text = new Text("Bodies:", 2);
        LinearLayout mainField = new LinearLayout(LinearLayout.Direction.Horizontal, 5f);
        mainField.addToLayout(text);

        Button<LayerWindowController> addBodyButton =
                new Button<>(
                        ResourceManager.getInstance().addTextureRegion, Button.ButtonType.OneClick, true);
        addBodyButton.setBehavior(
                new ButtonBehavior<LayerWindowController>(controller, addBodyButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        controller.onAddBodyButtonClicked();
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });
        addBodyButton.setLowerBottomY(-addBodyButton.getHeight() / 2);
        mainField.addToLayout(addBodyButton);
        mainField.setHeight(addBodyButton.getHeight());
        layout.addDummySection(mainField);
        mainField.setPadding(15f);
        createScroller();
    }

    @Override
    protected TwoLevelSectionLayout<BodyField, LayerField, DecorationField> createLayout() {
        return new TwoLevelSectionLayout<>(
                12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
    }

    public BodyField addBodyField(int bodyFieldKey, boolean isActive) {
        return addPrimary(bodyFieldKey, isActive);
    }

    public LayerField addLayerField(int bodyFieldKey, int layerFieldKey) {
        return addSecondary(bodyFieldKey, layerFieldKey);
    }

    public DecorationField addDecorationField(
            int bodyFieldKey, int layerFieldKey, int decorationFieldKey) {
        return addTetiary(bodyFieldKey, layerFieldKey, decorationFieldKey);
    }

    @Override
    public BodyField createPrimary(int primaryKey) {
        return new BodyField(primaryKey, layerWindowController);
    }

    @Override
    public LayerField createSecondary(int primaryKey, int secondaryKey) {
        return new LayerField(primaryKey, secondaryKey, layerWindowController);
    }

    @Override
    public DecorationField createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
        return new DecorationField(primaryKey, secondaryKey, tertiaryKey, layerWindowController);
    }
}
