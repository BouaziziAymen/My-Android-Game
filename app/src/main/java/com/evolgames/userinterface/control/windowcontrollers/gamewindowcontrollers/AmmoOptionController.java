package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.AmmoProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.AmmoModel;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.RotationQuantity;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledRotationQuantity;

public class AmmoOptionController  extends SettingsWindowController<AmmoProperties> {
    private final GameScene gameScene;
    private final ToolModel toolModel;;
    private AmmoModel ammoModel;
    private TitledRotationQuantity<AmmoOptionController> titledRotationQuantity;
    private Quantity<AmmoOptionController> ejectionSpeedQuantity;

    public AmmoOptionController(GameScene gameScene, KeyboardController keyboardController, ToolModel toolModel) {
        this.keyboardController = keyboardController;
        this.toolModel = toolModel;
        this.gameScene = gameScene;
    }




    public void onUpdated() {
        window.getLayout().updateLayout();
        onLayoutChanged();
    }


    @Override
    void onModelUpdated(ProperModel<AmmoProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }

        this.ammoModel = (AmmoModel) model;
        setRotationSpeed(ammoModel.getRotationSpeed());
        this.titledRotationQuantity.getAttachment().setClockwise(ammoModel.getRotationOrientation());
    }



    @Override
    public void init() {
        super.init();
        SectionField<AmmoOptionController> generalSettingsSection = new SectionField<>(1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(generalSettingsSection);
        onPrimaryButtonClicked(generalSettingsSection);

       titledRotationQuantity = new TitledRotationQuantity<>("Ang Velocity", 10, "g", 10f,80f,this);
        titledRotationQuantity.getAttachment().setBehavior(new QuantityBehavior<AmmoOptionController>(this, titledRotationQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                float ratio = ((RotationQuantity<?>)quantity).getRatio();
                ammoModel.setRotationSpeed(ratio);
            }
        });
        titledRotationQuantity.getAttachment().setAnticlockwiseButtonsAction(() -> {
            ammoModel.setRotationOrientation(false);
        });
        titledRotationQuantity.getAttachment().setClockwiseButtonsAction(() -> {
            ammoModel.setRotationOrientation(true);
        });
        SimpleSecondary<TitledRotationQuantity<?>> rotationQuantitySimpleSecondary = new SimpleSecondary<>(1,1, titledRotationQuantity);
        window.addSecondary(rotationQuantitySimpleSecondary);



        TitledQuantity<AmmoOptionController> titledEjectionSpeedQuantity = new TitledQuantity<>("Lin. Velocity:", 10, "b", 5, 85);
        ejectionSpeedQuantity = titledEjectionSpeedQuantity.getAttachment();
        titledEjectionSpeedQuantity.getAttachment().setBehavior(new QuantityBehavior<AmmoOptionController>(this, titledEjectionSpeedQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<AmmoOptionController>> bouncinessElement = new SimpleSecondary<>(1, 2, titledEjectionSpeedQuantity);
        window.addSecondary(bouncinessElement);
        ejectionSpeedQuantity.getBehavior().setChangeAction(()-> ammoModel.setLinearSpeed(ejectionSpeedQuantity.getRatio()));


        window.createScroller();
        window.getLayout().updateLayout();
        window.setVisible(false);
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
       // ProjectileModel projectileModel = (ProjectileModel) model;
      //  userInterface.getItemWindowController().changeItemName(model.getModelName(), projectileModel.getBodyId(), projectileModel.getProjectileId());
    }

    private void setRotationSpeed(float speed) {
        titledRotationQuantity.getAttachment().updateRatio(speed);
    }

    private void setLinearSpeed(float speed) {
        ejectionSpeedQuantity.updateRatio(speed);
    }

}