package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.properties.CasingProperties;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.RotationQuantity;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledRotationQuantity;

public class CasingOptionController extends SettingsWindowController<CasingProperties> {

    private ItemWindowController itemWindowController;
    private CasingModel ammoModel;
    private TitledRotationQuantity<CasingOptionController> titledRotationQuantity;
    private Quantity<CasingOptionController> ejectionSpeedQuantity;

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    @Override
    void onModelUpdated(ProperModel<CasingProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }

        this.ammoModel = (CasingModel) model;
        setRotationSpeed(ammoModel.getAmmoProperties().getRotationSpeed());
        setLinearSpeed(ammoModel.getAmmoProperties().getLinearSpeed());
        this.titledRotationQuantity
                .getAttachment()
                .setClockwise(ammoModel.getAmmoProperties().isRotationOrientation());
    }

    @Override
    public void init() {
        super.init();
        SectionField<CasingOptionController> generalSettingsSection =
                new SectionField<>(
                        1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(generalSettingsSection);
        onPrimaryButtonClicked(generalSettingsSection);

        titledRotationQuantity = new TitledRotationQuantity<>("Ang Velocity", 10, "g", 10f, 80f, this);
        titledRotationQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<CasingOptionController>(
                                this, titledRotationQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                                float ratio = quantity.getRatio();
                                ammoModel.getAmmoProperties().setRotationSpeed(ratio);
                            }
                        });
        titledRotationQuantity
                .getAttachment()
                .setAnticlockwiseButtonsAction(
                        () -> {
                            ammoModel.getAmmoProperties().setRotationOrientation(false);
                        });
        titledRotationQuantity
                .getAttachment()
                .setClockwiseButtonsAction(
                        () -> {
                            ammoModel.getAmmoProperties().setRotationOrientation(true);
                        });
        SimpleSecondary<TitledRotationQuantity<?>> rotationQuantitySimpleSecondary =
                new SimpleSecondary<>(1, 1, titledRotationQuantity);
        window.addSecondary(rotationQuantitySimpleSecondary);

        TitledQuantity<CasingOptionController> titledEjectionSpeedQuantity =
                new TitledQuantity<>("Lin. Velocity:", 10, "b", 5, 85);
        ejectionSpeedQuantity = titledEjectionSpeedQuantity.getAttachment();
        titledEjectionSpeedQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<CasingOptionController>(
                                this, titledEjectionSpeedQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<CasingOptionController>> bouncinessElement =
                new SimpleSecondary<>(1, 2, titledEjectionSpeedQuantity);
        window.addSecondary(bouncinessElement);
        ejectionSpeedQuantity
                .getBehavior()
                .setChangeAction(
                        () -> ammoModel.getAmmoProperties().setLinearSpeed(ejectionSpeedQuantity.getRatio()));

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        itemWindowController.onResume();
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        itemWindowController.onResume();
        // ProjectileModel projectileModel = (ProjectileModel) model;
        //  userInterface.getItemWindowController().changeItemName(model.getModelName(),
        // projectileModel.getBodyId(), projectileModel.getProjectileId());
    }

    private void setRotationSpeed(float speed) {
        titledRotationQuantity.getAttachment().updateRatio(speed);
    }

    private void setLinearSpeed(float speed) {
        ejectionSpeedQuantity.updateRatio(speed);
    }
}
