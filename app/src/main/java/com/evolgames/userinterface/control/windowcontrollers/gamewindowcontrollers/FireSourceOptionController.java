package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.FireSourceProperties;
import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;

public class FireSourceOptionController extends SettingsWindowController<FireSourceProperties> {

  private ItemWindowController itemWindowController;
  private FireSourceProperties fireSourceProperties;
  private Quantity<FireSourceOptionController> fireRatioQuantityField;
  private Quantity<FireSourceOptionController> smokeRatioQuantityField;
  private Quantity<FireSourceOptionController> sparkRatioQuantityField;
  private Quantity<FireSourceOptionController> speedQuantityField;
  private Quantity<FireSourceOptionController> particlesQuantityField;
  private Quantity<FireSourceOptionController> heatQuantityField;
  private Quantity<FireSourceOptionController> inPartSizeQuantityField;
  private Quantity<FireSourceOptionController> finPartSizeQuantityField;

  public void setItemWindowController(ItemWindowController itemWindowController) {
    this.itemWindowController = itemWindowController;
  }

  Quantity<FireSourceOptionController> createQuantity(
      String title,
      int primaryKey,
      int secondaryKey,
      int length,
      float minX,
      String key,
      FireSourceOptionController.FloatConsumer consumer) {

    TitledQuantity<FireSourceOptionController> ratioQuantity =
        new TitledQuantity<>(title, length, key, 1, minX);

    Quantity<FireSourceOptionController> quantityField = ratioQuantity.getAttachment();
    ratioQuantity
        .getAttachment()
        .setBehavior(
            new QuantityBehavior<FireSourceOptionController>(this, quantityField) {
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
    SectionField<FireSourceOptionController> explosiveSettingsSection =
        new SectionField<>(
            1, "Fire Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
    window.addPrimary(explosiveSettingsSection);

    particlesQuantityField =
        this.createQuantity(
            "Particles:", 1, 1, 10, 60, "b", (q) -> fireSourceProperties.setParticles(q));
    speedQuantityField =
        this.createQuantity("Speed:", 1, 2, 10, 40, "b", (q) -> fireSourceProperties.setSpeedRatio(q));

    heatQuantityField =
        this.createQuantity("Heat:", 1, 3, 10, 40, "r", (q) -> fireSourceProperties.setHeatRatio(q));
    fireRatioQuantityField =
        this.createQuantity(
            "Fire:", 1, 4, 10, 40, "r", (q) -> fireSourceProperties.setFireRatio(q));
    smokeRatioQuantityField =
        this.createQuantity(
            "Smoke:", 1, 5, 10, 50, "t", (q) -> fireSourceProperties.setSmokeRatio(q));
    sparkRatioQuantityField =
        this.createQuantity(
            "Sparks:", 1, 6, 10, 50, "g", (q) -> fireSourceProperties.setSparkRatio(q));

    inPartSizeQuantityField =
            this.createQuantity(
                    "In. Part. Size:", 1, 7, 10, 50, "g", (q) -> fireSourceProperties.setInFirePartSize(q));

    finPartSizeQuantityField =
            this.createQuantity(
                    "Fin. Part. Size:", 1, 8, 10, 50, "g", (q) -> fireSourceProperties.setFinFirePartSize(q));

    window.createScroller();
    updateLayout();
    window.setVisible(false);
  }

  @Override
  void onModelUpdated(ProperModel<FireSourceProperties> model) {
    super.onModelUpdated(model);
    if (model == null) {
      return;
    }
    this.fireSourceProperties = model.getProperties();
    this.setFireRatio(fireSourceProperties.getFireRatio());
    this.setSmokeRatio(fireSourceProperties.getSmokeRatio());
    this.setSparkRatio(fireSourceProperties.getSparkRatio());
    this.setParticlesRatio(fireSourceProperties.getParticles());
    this.setSpeedRatio(fireSourceProperties.getSpeedRatio());
    this.setHeatRatio(fireSourceProperties.getHeatRatio());
    this.setInPartSizeRatio(fireSourceProperties.getInFirePartSize());
    this.setFinPartSizeRatio(fireSourceProperties.getFinFirePartSize());
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

  private void setInPartSizeRatio(float ratio) {
    inPartSizeQuantityField.updateRatio(ratio);
  }
  private void setFinPartSizeRatio(float ratio) {
    finPartSizeQuantityField.updateRatio(ratio);
  }
  @FunctionalInterface
  public interface FloatConsumer {
    void accept(float value);
  }
}
