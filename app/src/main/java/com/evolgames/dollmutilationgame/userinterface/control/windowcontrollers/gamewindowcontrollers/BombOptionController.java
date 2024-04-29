package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Quantity;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.dollmutilationgame.entities.properties.BombProperties;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.validators.NumericValidator;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;

public class BombOptionController extends SettingsWindowController<BombProperties> {

    private final NumericValidator forceValidator = new NumericValidator(false, true, 0, 100000, 5, 2);
    private ItemWindowController itemWindowController;
    private BombProperties bombProperties;
    private Quantity<BombOptionController> fireRatioQuantityField;
    private Quantity<BombOptionController> smokeRatioQuantityField;
    private Quantity<BombOptionController> sparkRatioQuantityField;
    private Quantity<BombOptionController> speedQuantityField;
    private Quantity<BombOptionController> particlesQuantityField;
    private Quantity<BombOptionController> heatQuantityField;
    private TextField<BombOptionController> forceTextField;

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    Quantity<BombOptionController> createQuantity(
            String title,
            int primaryKey,
            int secondaryKey,
            int length,
            float minX,
            String key,
            FloatConsumer consumer) {

        TitledQuantity<BombOptionController> ratioQuantity =
                new TitledQuantity<>(title, length, key, 1, true);

        Quantity<BombOptionController> quantityField = ratioQuantity.getAttachment();
        ratioQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BombOptionController>(this, quantityField) {
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
        SectionField<BombOptionController> explosiveSettingsSection =
                new SectionField<>(
                        1, ResourceManager.getInstance().getString(R.string.explosive_settings_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSettingsSection);

        particlesQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.particle_density_title), 1, 1, 10, 60, "b", (q) -> bombProperties.setParticles(q));
        speedQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.particle_speed_title), 1, 2, 10, 40, "b", (q) -> bombProperties.setSpeed(q));

        TitledTextField<BombOptionController> forceField = new TitledTextField<>(ResourceManager.getInstance().getString(R.string.explosion_force), 8, 5);
        forceTextField = forceField.getAttachment();
        forceField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BombOptionController>(
                                this,
                                forceField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                forceValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BombOptionController.super.onTextFieldTapped(forceTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BombOptionController.super.onTextFieldReleased(forceTextField);
                            }
                        });
        FieldWithError forceFieldWithError = new FieldWithError(forceField);
        SimpleSecondary<FieldWithError> forceElement = new SimpleSecondary<>(1, 3, forceFieldWithError);
        window.addSecondary(forceElement);
        forceTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float force = Float.parseFloat(forceTextField.getTextString());
                            bombProperties.setForce(force);
                        });

        heatQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.explosion_heat_title), 1, 4, 10, 40, "r", (q) -> bombProperties.setHeat(q));
        fireRatioQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.fire_title), 1, 5, 10, 40, "r", (q) -> bombProperties.setFireRatio(q));
        smokeRatioQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.smoke_title), 1, 6, 10, 50, "t", (q) -> bombProperties.setSmokeRatio(q));
        sparkRatioQuantityField =
                this.createQuantity(ResourceManager.getInstance().getString(R.string.sparks_title), 1, 7, 10, 50, "g", (q) -> bombProperties.setSparkRatio(q));

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    @Override
    void onModelUpdated(ProperModel<BombProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }
        this.bombProperties = model.getProperties();
        this.setFireRatio(bombProperties.getFireRatio());
        this.setSmokeRatio(bombProperties.getSmokeRatio());
        this.setSparkRatio(bombProperties.getSparkRatio());
        this.setParticlesRatio(bombProperties.getParticles());
        this.setSpeedRatio(bombProperties.getSpeed());
        this.setForceRatio(bombProperties.getForce());
        this.setHeatRatio(bombProperties.getHeat());
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

    private void setFireRatio(float fireRatio) {
        fireRatioQuantityField.updateRatio(fireRatio);
    }

    private void setSmokeRatio(float smokeRatio) {
        smokeRatioQuantityField.updateRatio(smokeRatio);
    }

    private void setSparkRatio(float sparkRatio) {
        sparkRatioQuantityField.updateRatio(sparkRatio);
    }

    private void setParticlesRatio(float particlesRatio) {
        particlesQuantityField.updateRatio(particlesRatio);
    }

    private void setSpeedRatio(float speedRatio) {
        speedQuantityField.updateRatio(speedRatio);
    }

    private void setHeatRatio(float heatRatio) {
        heatQuantityField.updateRatio(heatRatio);
    }

    private void setForceRatio(float forceRatio) {
        forceTextField.getBehavior().setTextValidated(String.valueOf(forceRatio));
    }

    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);
    }
}
