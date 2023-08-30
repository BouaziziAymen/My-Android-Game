package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.BombProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.IntegerValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
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
    private BombModel bombModel;
    private BombProperties bombProperties;
    private Quantity<BombOptionController> fireRatioQuantityField;
    private Quantity<BombOptionController> smokeRatioQuantityField;
    private Quantity<BombOptionController> sparkRatioQuantityField;
    private Quantity<BombOptionController> speedQuantityField;
    private Quantity<BombOptionController> particlesQuantityField;
    private Quantity<BombOptionController> forceQuantityField;
    private Quantity<BombOptionController> heatQuantityField;


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

        particlesQuantityField =this.createQuantity("Particles",1,1,10,70,"b",(q)->bombProperties.setParticles(q));
        speedQuantityField =this.createQuantity("Speed",1,2,10,70,"b",(q)->bombProperties.setSpeed(q));
        forceQuantityField =this.createQuantity("Force",1,3,10,70,"n",(q)->bombProperties.setForce(q));
        heatQuantityField =this.createQuantity("Heat",1,4,10,70,"r",(q)->bombProperties.setHeat(q));
        fireRatioQuantityField = this.createQuantity("Fire",1,5,10,70,"r",(q)->bombProperties.setFireRatio(q));
        smokeRatioQuantityField = this.createQuantity("Smoke",1,6,10,70,"t",(q)->bombProperties.setSmokeRatio(q));
        sparkRatioQuantityField =this.createQuantity("Sparks",1,7,10,70,"g",(q)->bombProperties.setSparkRatio(q));

        window.createScroller();
        window.getLayout().updateLayout();
        window.setVisible(false);
    }

    @Override
    void onModelUpdated(ProperModel<BombProperties> model) {
        super.onModelUpdated(model);
        this.bombModel = (BombModel) model;
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
        forceQuantityField.updateRatio(forceRatio);
    }

}
