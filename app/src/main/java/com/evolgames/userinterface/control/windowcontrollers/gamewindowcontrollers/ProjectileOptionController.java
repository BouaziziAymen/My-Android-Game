package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.factories.ItemCategoryFactory;
import com.evolgames.gameengine.GameSound;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
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
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProjectileOptionController extends SettingsWindowController<ProjectileProperties> {
    private final GameScene gameScene;
    private final AlphaNumericValidator projectileNameValidator = new AlphaNumericValidator(8, 5);
    private final NumericValidator energyValidator = new NumericValidator(false, true, 0, 100000, 5, 1);
    private final NumericValidator fireRateValidator = new NumericValidator(false, true, 0, 1000, 3, 1);
    private final ToolModel toolModel;
    private final Hashtable<String, ToolModel> missileTable;
    private final Hashtable<String, ButtonWithText<ProjectileOptionController>> missileButtonsTable;
    private TextField<ProjectileOptionController> projectileNameField;
    private TextField<ProjectileOptionController> energyTextField;
    private TextField<ProjectileOptionController> fireRateTextField;
    private Quantity<ProjectileOptionController> recoilQuantity;
    private ProjectileProperties projectileProperties;

    public ProjectileOptionController(GameScene gameScene, KeyboardController keyboardController, ToolModel toolModel) {
        this.keyboardController = keyboardController;
        this.toolModel = toolModel;
        this.gameScene = gameScene;
        this.missileTable = new Hashtable<>();
        this.missileButtonsTable = new Hashtable<>();
    }

    private void resetLayout() {
        for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
            window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
        }
    }


    public void updateMissileSelectionFields() {
        if (model == null) return;
        if (window.getLayout().getPrimariesSize() >= 1) {
            window.getLayout().removePrimary(0);
        }
        SectionField<ProjectileOptionController> bodyASection = new SectionField<>(0, "Projectile Body:", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyASection);

        List<String> missileFilesList = ToolUtils.getToolNamesByCategory(gameScene.getActivity(), "c0");
        AtomicInteger missileCounter = new AtomicInteger();
        missileFilesList.forEach(f -> createProjectileToolButton(f, missileCounter.getAndIncrement()));
        this.onUpdated();
    }

    private void createProjectileToolButton(String fileName, int missileId) {
        ButtonWithText<ProjectileOptionController> missileButton = new ButtonWithText<>(fileName.substring(3,fileName.length()-4), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);
        SimpleSecondary<ButtonWithText<ProjectileOptionController>> missileField = new SimpleSecondary<>(0, missileId, missileButton);
        window.addSecondary(missileField);
        missileButtonsTable.put(fileName, missileButton);
        missileButton.setBehavior(new ButtonBehavior<ProjectileOptionController>(this, missileButton) {
            @Override
            public void informControllerButtonClicked() {
                ProjectileModel projectileModel = ((ProjectileModel) ProjectileOptionController.this.model);
                ToolModel missileModel;
                if (!missileTable.containsKey(fileName)) {
                    missileModel = ToolUtils.getProjectileModel(fileName);
                    missileTable.put(fileName, missileModel);
                } else {
                    missileModel = missileTable.get(fileName);
                }

                projectileModel.setMissileModel(missileModel);
                onProjectileBodyButtonClicked(missileField);
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });
    }


    void onProjectileBodyButtonClicked(SimpleSecondary<?> materialButton) {
        int primaryKey = materialButton.getPrimaryKey();
        int secondaryKey = materialButton.getSecondaryKey();
        for (int i = 0; i < window.getLayout().getSecondariesSize(primaryKey); i++) {
            SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(primaryKey, i);
            if (element.getSecondaryKey() != secondaryKey) {
                Element main = element.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
    }

    public void onUpdated() {
        window.getLayout().updateLayout();
        onLayoutChanged();
    }


    @Override
    void onModelUpdated(ProperModel<ProjectileProperties> model) {
        super.onModelUpdated(model);
        if (model == null) {
            return;
        }
        ProjectileModel projectileModel = (ProjectileModel) model;
        updateMissileSelectionFields();
        this.projectileProperties = model.getProperties();
        setBody(projectileModel.getBodyId());
        setProjectileName(this.model.getModelName());
        setFireRate(this.projectileProperties.getFireRate());
        setEnergy(this.projectileProperties.getMuzzleVelocity());
        setRecoil(this.projectileProperties.getRecoil());
        setFireSound(this.projectileProperties.getFireSound());
        setProjectileTriggerType(this.projectileProperties.getProjectileTriggerType());
        setProjectileName(projectileModel.getModelName());
        if(projectileModel.getMissileModel()!=null) {
            setMissileName(ItemCategoryFactory.getInstance().getItemCategoryByIndex(0).getPrefix()+"_" +projectileModel.getMissileModel().getModelName()+".xml");
        }
    }

    private void setBody(int bodyId) {

    }

    private void setProjectileTriggerType(ProjectileTriggerType projectileTriggerType) {
        if (projectileTriggerType != null) {
            SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(2, projectileTriggerType.getValue());
            ((ButtonWithText<?>) element.getMain()).updateState(Button.State.PRESSED);
        }
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
        projectileProperties.setProjectileTriggerType(projectileTriggerType);
    }

    @Override
    public void onTertiaryButtonClicked(SimpleTertiary<?> simpleTertiary) {
        super.onTertiaryButtonClicked(simpleTertiary);
        int primaryKey = simpleTertiary.getPrimaryKey();
        int secondaryKey = simpleTertiary.getSecondaryKey();
        if (simpleTertiary.getPrimaryKey() == 1 && simpleTertiary.getSecondaryKey() == 1) {
            ResourceManager.getInstance().gunshotSounds.get(simpleTertiary.getTertiaryKey()).getSoundList().get(0).play();
            projectileProperties.setFireSound(simpleTertiary.getTertiaryKey());
            for (int i = 0; i < window.getLayout().getTertiariesSize(primaryKey, secondaryKey); i++) {
                SimpleTertiary<?> element = window.getLayout().getTertiaryByIndex(primaryKey, secondaryKey, i);
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
        updateMissileSelectionFields();
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
        SecondarySectionField<ProjectileOptionController> projectileShotSoundSection = new SecondarySectionField<>(1, 1, "Shot Sound", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addSecondary(projectileShotSoundSection);
        for (int i = 0; i < sounds.size(); i++) {
            GameSound gameSound = sounds.get(i);
            SoundField soundField = new SoundField(gameSound.getTitle(), 1, 1, i, this);
            window.addTertiary(soundField);
        }


        SectionField<ProjectileOptionController> typeSection = new SectionField<>(2, "Type", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(typeSection);
        for (int i = 0; i < 3; i++) {
            ProjectileTriggerType projectileTriggerType = ProjectileTriggerType.getFromValue(i);
            assert projectileTriggerType != null;
            String title;
            switch (projectileTriggerType) {
                case MANUAL:
                    title = "Manual";
                    break;
                case SEMI_AUTOMATIC:
                    title = "Semi Automatic";
                    break;
                case AUTOMATIC:
                    title = "Automatic";
                    break;
                default:
                    title = "";
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
            int energy = Integer.parseInt(energyTextField.getTextString());
            projectileProperties.setMuzzleVelocity(energy);
        });


        TitledTextField<ProjectileOptionController> fireRateField = new TitledTextField<>("Rate:", 6, 5, 50);
        fireRateTextField = fireRateField.getAttachment();

        fireRateField.getAttachment().setBehavior(new TextFieldBehavior<ProjectileOptionController>(this, fireRateTextField, Keyboard.KeyboardType.Numeric, fireRateValidator, true) {
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
            int rate = Integer.parseInt(fireRateTextField.getTextString());
            projectileProperties.setFireRate(rate);
        });


        TitledQuantity<ProjectileOptionController> titledRecoilQuantity = new TitledQuantity<>("Recoil:", 10, 3, 5, 50);
        recoilQuantity = titledRecoilQuantity.getAttachment();
        titledRecoilQuantity.getAttachment().setBehavior(new QuantityBehavior<ProjectileOptionController>(this, recoilQuantity) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<?>> recoilElement = new SimpleSecondary<>(3, 3, titledRecoilQuantity);
        window.addSecondary(recoilElement);
        recoilQuantity.getBehavior().setChangeAction(() -> projectileProperties.setRecoil(recoilQuantity.getRatio()));
        window.createScroller();
        window.getLayout().updateLayout();
        window.setVisible(false);
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        ProjectileModel projectileModel = (ProjectileModel) model;
        userInterface.getItemWindowController().changeItemName(model.getModelName(), projectileModel.getBodyId(), projectileModel.getProjectileId());
    }

    private void setProjectileName(String name) {
        projectileNameField.getBehavior().setTextValidated(name);
    }

    private void setFireRate(int fireRate) {
        fireRateTextField.getBehavior().setTextValidated("" + fireRate);
    }

    private void setEnergy(float energy) {
        energyTextField.getBehavior().setTextValidated("" + Math.floor(energy));
    }

    private void setRecoil(float recoil) {
        recoilQuantity.updateRatio(recoil);
    }

    private void setFireSound(int fireSound) {

    }

    private void setMissileName(String fileName) {
        ButtonWithText<ProjectileOptionController> selectedButton = missileButtonsTable.get(fileName);
        assert (selectedButton != null);
        missileButtonsTable.forEach((key, value) -> value.release());
        selectedButton.click();
    }
}