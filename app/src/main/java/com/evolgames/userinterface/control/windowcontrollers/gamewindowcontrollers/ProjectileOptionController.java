package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.gameengine.GameSound;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SecondarySectionField;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.userinterface.view.windows.windowfields.projectieoptionwindow.SoundField;

import java.util.ArrayList;

public class ProjectileOptionController  extends SettingsWindowController {
    private TextField<ProjectileOptionController> projectileNameField;
    private AlphaNumericValidator projectileNameValidator = new AlphaNumericValidator(8,5);
    private TextField<ProjectileOptionController> energyTextField;
    private NumericValidator energyValidator = new NumericValidator(false, false, 0, 100000, 5, 1);
    private TextField<ProjectileOptionController> fireRateTextField;
    private Quantity<ProjectileOptionController> recoilQuantity;
    private ToolModel toolModel;


    public ProjectileOptionController(KeyboardController keyboardController, ToolModel toolModel) {
        this.keyboardController = keyboardController;
            this.toolModel = toolModel;
        }
        private void resetLayout() {
            for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
                window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
            }
        }
        private void onFirstBodyButtonClicked(BodyModel bodyModel, SimpleSecondary<?> body1Field) {
            super.onSecondaryButtonClicked(body1Field);
            int size = window.getLayout().getSecondariesSize(1);
            for (int i = 0; i < size; i++) {
                SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(1, i);
                if (body1Field != other) {
                    Element main = other.getMain();
                    if (main instanceof ButtonWithText) {
                        ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                    }
                }
            }
        ((ProjectileModel) model).setBodyModel(bodyModel);
    }


    public void updateBodySelectionFields() {
        if(model==null)return;
        if (window.getLayout().getPrimariesSize() >= 1) {
            window.getLayout().removePrimary(0);
        }
        SectionField<ProjectileOptionController> bodyASection = new SectionField<>(0, "Projectile Body:", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyASection);

        ArrayList<BodyModel> bodies = new ArrayList<>(toolModel.getBodies());
        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            if(bodyModel.getBodyId() ==((ProjectileModel)model).getBodyId())continue;
            ButtonWithText<ProjectileOptionController> bodyButton = new ButtonWithText<>(bodyModel.getModelName(), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);
            SimpleSecondary<ButtonWithText<ProjectileOptionController>> bodyField = new SimpleSecondary<>(0, i, bodyButton);
            window.addSecondary(bodyField);
            bodyButton.setBehavior(new ButtonBehavior<ProjectileOptionController>(this, bodyButton) {
                @Override
                    public void informControllerButtonClicked() {
                  //  onFirstBodyButtonClicked(bodyModel, bodyField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });
        }
        onUpdated();
    }

    public void onUpdated() {
        window.getLayout().updateLayout();
        onLayoutChanged();
    }
    void updateProjectileModel(ProjectileModel projectileModel) {
        this.model = projectileModel;
        resetLayout();
        updateBodySelectionFields();
        SectionField<ProjectileOptionController> sectionField = new SectionField<>(1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<ProjectileOptionController> layerNameField = new TitledTextField<>("Projectile Name:", 10);
        projectileNameField = layerNameField.getAttachment();

        projectileNameField.setBehavior(new TextFieldBehavior<ProjectileOptionController>(this, projectileNameField, Keyboard.KeyboardType.AlphaNumeric, projectileNameValidator) {
            @Override
            protected void informControllerTextFieldTapped() {
                ProjectileOptionController.super.onTextFieldTapped(projectileNameField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                ProjectileOptionController.super.onTextFieldReleased(projectileNameField);
            }
        });
        projectileNameField.getBehavior().setReleaseAction(() -> model.setModelName(projectileNameField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(layerNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);


        ArrayList<GameSound> sounds = ResourceManager.getInstance().gunshotSounds;
        SecondarySectionField<ProjectileOptionController> projectileShotSoundSection = new SecondarySectionField<>(1,1, "Shot Sound", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addSecondary(projectileShotSoundSection);
        for (int i = 0; i < sounds.size(); i++) {
            GameSound gameSound = sounds.get(i);
            SoundField soundField = new SoundField(gameSound.getTitle(),1,1,i,this);
            window.addTertiary(soundField);
        }




        SectionField<ProjectileOptionController> typeSection = new SectionField<>(2, "Type", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(typeSection);
        for (int i = 0; i < 3; i++) {
           ProjectileTriggerType projectileTriggerType = ProjectileTriggerType.getFromValue(i);
            assert projectileTriggerType != null;
            String title;
            switch (projectileTriggerType){
                case MANUAL:title = "Manual";
                    break;
                case SEMI_AUTOMATIC:title="Semi Automatic";
                    break;
                case AUTOMATIC:title="Automatic";
                    break;
                default:title="";
                break;
            }
            ButtonWithText<ProjectileOptionController> typeButton = new ButtonWithText<>
                    (title, 3, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);


            SimpleSecondary<ButtonWithText<ProjectileOptionController>> typeField = new SimpleSecondary<>(2, i, typeButton);
            window.addSecondary(typeField);
            typeButton.setBehavior(new ButtonBehavior<ProjectileOptionController>(this, typeButton) {
                @Override
                public void informControllerButtonClicked() {
                    onTypeButtonClicked(typeField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });

        }





        SectionField<ProjectileOptionController> projectileOptionsField = new SectionField<>(3, "Physical Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(projectileOptionsField);

        TitledTextField<ProjectileOptionController> energyField = new TitledTextField<>("Energy:", 6, 5, 50);
        energyTextField = energyField.getAttachment();

        energyField.getAttachment().setBehavior(new TextFieldBehavior<ProjectileOptionController>(this, energyField.getAttachment(), Keyboard.KeyboardType.Numeric, energyValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                ProjectileOptionController.super.onTextFieldTapped(energyTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                ProjectileOptionController.super.onTextFieldReleased(energyTextField);
            }
        });

        FieldWithError energyFieldWithError = new FieldWithError(energyField);
        SimpleSecondary<FieldWithError> energyElement = new SimpleSecondary<>(3, 1, energyFieldWithError);
        window.addSecondary(energyElement);

        energyTextField.getBehavior().setReleaseAction(() -> {
            float energy = Float.parseFloat(energyTextField.getTextString());
            projectileModel.setEnergy(energy);
        });





        TitledTextField<ProjectileOptionController> fireRateField = new TitledTextField<>("Rate:", 3, 5, 50);
        fireRateTextField = fireRateField.getAttachment();

        fireRateField.getAttachment().setBehavior(new TextFieldBehavior<ProjectileOptionController>(this, fireRateTextField, Keyboard.KeyboardType.Numeric, energyValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                ProjectileOptionController.super.onTextFieldTapped(fireRateTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                ProjectileOptionController.super.onTextFieldReleased(fireRateTextField);
            }
        });

        FieldWithError fireRateFieldWithError = new FieldWithError(fireRateField);
        SimpleSecondary<FieldWithError> fireRateElement = new SimpleSecondary<>(3, 2, fireRateFieldWithError);
        window.addSecondary(fireRateElement);

        fireRateTextField.getBehavior().setReleaseAction(() -> {
            float rate = Float.parseFloat(fireRateTextField.getTextString());
            projectileModel.setFireRate(rate);
        });



        TitledQuantity<ProjectileOptionController> titledRecoilQuantity = new TitledQuantity<>("Recoil:", 10, 3, 5, 50);
        recoilQuantity = titledRecoilQuantity.getAttachment();
        titledRecoilQuantity.getAttachment().setBehavior(new QuantityBehavior<ProjectileOptionController>(this,recoilQuantity) {
            @Override
            public void informControllerQuantityUpdated(Quantity quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<?>> recoilElement = new SimpleSecondary<>(3, 3, titledRecoilQuantity);
        window.addSecondary(recoilElement);
        recoilQuantity.getBehavior().setChangeAction(() -> projectileModel.setRecoil(recoilQuantity.getRatio()));

        window.createScroller();
        window.getLayout().updateLayout();
    }

    private void onTypeButtonClicked(SimpleSecondary<ButtonWithText<ProjectileOptionController>> typeField) {
        int primaryKey = typeField.getPrimaryKey();
        int secondaryKey = typeField.getSecondaryKey();
        ProjectileTriggerType projectileTriggerType = ProjectileTriggerType.getFromValue(secondaryKey);
        for (int i = 0; i < window.getLayout().getSecondariesSize(primaryKey); i++) {
            SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(primaryKey, i);
            if (element.getSecondaryKey() != secondaryKey) {
                Element main = element.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
        System.out.println(projectileTriggerType);
        ((ProjectileModel)model).setProjectileTriggerType(projectileTriggerType);
    }

    @Override
    public void onTertiaryButtonClicked(SimpleTertiary<?> simpleTertiary) {
        super.onTertiaryButtonClicked(simpleTertiary);
        ResourceManager.getInstance().gunshotSounds.get(simpleTertiary.getTertiaryKey()).getSoundList().get(0).setVolume(1f);
        ResourceManager.getInstance().gunshotSounds.get(simpleTertiary.getTertiaryKey()).getSoundList().get(0).play();
        System.out.println("Sound----------------------");
        int primaryKey = simpleTertiary.getPrimaryKey();
        int secondaryKey = simpleTertiary.getSecondaryKey();
        if(simpleTertiary.getPrimaryKey()==1&&simpleTertiary.getSecondaryKey()==1){
            for (int i = 0; i < window.getLayout().getTertiariesSize(primaryKey,secondaryKey); i++) {
                SimpleTertiary<?> element = window.getLayout().getTertiaryByIndex(primaryKey,secondaryKey, i);
                if (element.getTertiaryKey() != simpleTertiary.getTertiaryKey()) {
                    Element main = element.getMain();
                    if (main instanceof ButtonWithText) {
                        ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                    }
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        window.setVisible(false);
    }
}