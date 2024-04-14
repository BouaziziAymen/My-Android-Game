package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.properties.DragProperties;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;

public class DragOptionController extends SettingsWindowController<DragProperties> {

    private ItemWindowController itemWindowController;
    private DragProperties dragProperties;
    private Quantity<DragOptionController> dragRatioQuantityField;
    private SimpleSecondary<TitledButton<DragOptionController>> symmetricalField;

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    Quantity<DragOptionController> createQuantity(
            String title,
            int primaryKey,
            int secondaryKey,
            int length,
            String key,
            DragOptionController.FloatConsumer consumer) {

        TitledQuantity<DragOptionController> ratioQuantity =
                new TitledQuantity<>(title, length, key, 1, true);

        Quantity<DragOptionController> quantityField = ratioQuantity.getAttachment();
        ratioQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<DragOptionController>(this, quantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                consumer.accept(quantity.getRatio());
                            }
                        });

        FieldWithError ratioFieldWithError = new FieldWithError(ratioQuantity);
        SimpleSecondary<FieldWithError> ratioElement =
                new SimpleSecondary<>(primaryKey, secondaryKey, ratioFieldWithError);
        window.addSecondary(ratioElement);

        return quantityField;
    }

    @Override
    public void init() {
        super.init();
        SectionField<DragOptionController> explosiveSettingsSection =
                new SectionField<>(
                        1, ResourceManager.getInstance().getString(R.string.air_drag_settings_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSettingsSection);

        this.dragRatioQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.magnitude_title), 1, 1, 10, "b", (q) -> dragProperties.setMagnitude(q));

        TitledButton<DragOptionController> symmetricalButton =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(R.string.symmetrical_title),
                        ResourceManager.getInstance().onOffTextureRegion,
                        Button.ButtonType.Selector,
                        5f);

        symmetricalField = new SimpleSecondary<>(1, 2, symmetricalButton);
        window.addSecondary(symmetricalField);
        symmetricalButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<DragOptionController>(this, symmetricalButton.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                onSecondaryButtonClicked(symmetricalField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                onSecondaryButtonReleased(symmetricalField);
                            }
                        });
        symmetricalButton.getAttachment().getBehavior().setPushAction(() -> dragProperties.setSymmetrical(true));
        symmetricalButton.getAttachment().getBehavior().setReleaseAction(() -> dragProperties.setSymmetrical(false));

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    @Override
    void onModelUpdated(ProperModel<DragProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }
        this.dragProperties = model.getProperties();
        this.setDragMagnitude(dragProperties.getMagnitude());
        this.setSymmetrical(dragProperties.isSymmetrical());
    }

    private void setSymmetrical(boolean symmetrical) {
        if (symmetrical) {
            symmetricalField.getMain().getAttachment().updateState(Button.State.PRESSED);
            onSecondaryButtonClicked(symmetricalField);
        } else {
            symmetricalField.getMain().getAttachment().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(symmetricalField);
        }
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        itemWindowController.onResume();
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        itemWindowController.onResume();
    }

    private void setDragMagnitude(float dragMagnitude) {
        this.dragRatioQuantityField.updateRatio(dragMagnitude);
    }

    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);
    }
}
