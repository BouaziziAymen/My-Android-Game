package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.BombProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

public class BombOptionController extends SettingsWindowController<BombProperties> {

    private ItemWindowController itemWindowController;
    private BombProperties bombProperties;
    private Quantity<BombOptionController> fireRatioQuantityField;
    private Quantity<BombOptionController> smokeRatioQuantityField;
    private Quantity<BombOptionController> sparkRatioQuantityField;
    private Quantity<BombOptionController> speedQuantityField;
    private Quantity<BombOptionController> particlesQuantityField;
    private Quantity<BombOptionController> heatQuantityField;
    private TextField<BombOptionController> forceTextField;
    private final NumericValidator forceValidator = new NumericValidator(false, true, 0, 10, 2, 2);


    public BombOptionController(KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }
    @FunctionalInterface
    public interface FloatConsumer {
        void accept(float value);
    }
    Quantity<BombOptionController> createQuantity(String title, int primaryKey, int secondaryKey, int length,float minX, String key,FloatConsumer consumer){

        TitledQuantity<BombOptionController> ratioQuantity = new TitledQuantity<>(title, length, key, 1, minX);

        Quantity<BombOptionController> quantityField = ratioQuantity.getAttachment();
        ratioQuantity.getAttachment().setBehavior(new QuantityBehavior<BombOptionController>(this, quantityField) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                consumer.accept(quantity.getRatio());
            }
        });

        FieldWithError ratioFieldWithError = new FieldWithError(ratioQuantity);
        SimpleSecondary<FieldWithError> ratioElement = new SimpleSecondary<>(primaryKey, secondaryKey, ratioFieldWithError);
        window.addSecondary(ratioElement);
        return quantityField;
    }

    @Override
    public void init() {
        super.init();
        SectionField<BombOptionController> explosiveSettingsSection = new SectionField<>(1, "Explosive Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(explosiveSettingsSection);

        particlesQuantityField =this.createQuantity("Particles:",1,1,10,60,"b",(q)->bombProperties.setParticles(q));
        speedQuantityField =this.createQuantity("Speed:",1,2,10,40,"b",(q)->bombProperties.setSpeed(q));

        TitledTextField<BombOptionController> forceField = new TitledTextField<>("Force:", 5, 5, 60);
        forceTextField = forceField.getAttachment();
        forceField.getAttachment().setBehavior(new TextFieldBehavior<BombOptionController>(this, forceField.getAttachment(), Keyboard.KeyboardType.Numeric, forceValidator, true) {
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
        forceTextField.getBehavior().setReleaseAction(() -> {
            float force = Float.parseFloat(forceTextField.getTextString());
            bombProperties.setForce(force);
        });

        heatQuantityField =this.createQuantity("Heat:",1,4,10,40,"r",(q)->bombProperties.setHeat(q));
        fireRatioQuantityField = this.createQuantity("Fire:",1,5,10,40,"r",(q)->bombProperties.setFireRatio(q));
        smokeRatioQuantityField = this.createQuantity("Smoke:",1,6,10,50,"t",(q)->bombProperties.setSmokeRatio(q));
        sparkRatioQuantityField =this.createQuantity("Sparks:",1,7,10,50,"g",(q)->bombProperties.setSparkRatio(q));

        window.createScroller();
        updateLayout();
        window.setVisible(false);
    }

    @Override
    void onModelUpdated(ProperModel<BombProperties> model) {
        super.onModelUpdated(model);
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

}
