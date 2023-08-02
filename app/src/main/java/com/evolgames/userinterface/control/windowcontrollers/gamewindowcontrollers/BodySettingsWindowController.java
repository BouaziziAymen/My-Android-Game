package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.properties.usage.AutomaticProperties;
import com.evolgames.entities.properties.usage.ManualProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.SemiAutomaticProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
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
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BodySettingsWindowController extends SettingsWindowController<BodyProperties> {

    private final LayerWindowController layerWindowController;
    private TextField<BodySettingsWindowController> bodyNameTextField;
    private final AlphaNumericValidator bodyNameValidator = new AlphaNumericValidator(8, 5);
    private final NumericValidator gunReloadTimeValidator = new NumericValidator(false, false, 0, 1000, 3, 0);
   //Manual
    private TextField<BodySettingsWindowController> rangedManualReloadTimeTextField;
    //Automatic
    private Quantity<BodySettingsWindowController> rangedAutomaticFireRateQuantity;
    //Semi Automatic
    private Quantity<BodySettingsWindowController> rangedSemAutomaticFireRateQuantity;


    private BodyModel bodyModel;
    private UserInterface userInterface;


    public BodySettingsWindowController(LayerWindowController layerWindowController, KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
        this.layerWindowController = layerWindowController;
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    void onModelUpdated(ProperModel<BodyProperties> model) {
        super.onModelUpdated(model);
        setBodyName(model.getModelName());
        this.bodyModel = (BodyModel) model;

        createCategorySection();
        updateUsageSettings();
        for(BodyUsageCategory usageCategory:this.bodyModel.getUsageModels().stream().map(UsageModel::getType).collect(Collectors.toList())){
            switch (usageCategory){
                case RANGED_MANUAL:
                    UsageModel<ManualProperties> manualUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                    ManualProperties manualProps =  manualUsageModel.getProperties();
                    setReloadTime(manualProps.getReloadTime());
                    break;
                case RANGED_SEMI_AUTOMATIC:
                    UsageModel<SemiAutomaticProperties> semiAutomaticUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                    SemiAutomaticProperties semAutoProps = semiAutomaticUsageModel.getProperties();
                    setSemAutoFireRate(semAutoProps.getFireRate());
                    break;
                case RANGED_AUTOMATIC:
                    UsageModel<AutomaticProperties> automaticUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                    AutomaticProperties autoProps = automaticUsageModel.getProperties();
                    setAutoFireRate(autoProps.getFireRate());
                    break;
            }
        }

    }

    private void setAutoFireRate(float fireRate) {
        rangedAutomaticFireRateQuantity.updateRatio(fireRate);
    }

    private void setSemAutoFireRate(float fireRate) {
        rangedSemAutomaticFireRateQuantity.updateRatio(fireRate);
    }

    private void setReloadTime(float reloadTime) {
        rangedManualReloadTimeTextField.getBehavior().setTextValidated(""+Float.valueOf(reloadTime).intValue());
    }

    private void setBodyName(String bodyName) {
        bodyNameTextField.getBehavior().setTextValidated(bodyName);
    }


    @Override
    public void init() {
        window.setVisible(false);
        SectionField<BodySettingsWindowController> sectionField = new SectionField<>(1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<BodySettingsWindowController> bodyNameField = new TitledTextField<>("Body Name:", 10);
        bodyNameTextField = bodyNameField.getAttachment();

        bodyNameTextField.setBehavior(new TextFieldBehavior<BodySettingsWindowController>(this, bodyNameTextField, Keyboard.KeyboardType.AlphaNumeric, bodyNameValidator) {
            @Override
            protected void informControllerTextFieldTapped() {
                BodySettingsWindowController.super.onTextFieldTapped(bodyNameTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                BodySettingsWindowController.super.onTextFieldReleased(bodyNameTextField);
            }
        });
        bodyNameTextField.getBehavior().setReleaseAction(() -> model.setModelName(bodyNameTextField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(bodyNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);

        createCategorySection();

        updateUsageSettings();


        window.createScroller();
        window.getLayout().updateLayout();
    }

    private void createCategorySection() {

        if (window.getLayout().getPrimaries().size() >= 2) {
            window.getLayout().removePrimary(2);
        }
        SectionField<BodySettingsWindowController> categorySection = new SectionField<>(2, "Category", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(categorySection);
        if (this.bodyModel != null) {
            BodyModel.allCategories.forEach(e -> this.createCategoryCheckBox(e, this.bodyModel.getUsageModels().stream().anyMatch(s->s.getType()==e)));
        }
    }

    private void updateUsageSettings() {
        for (SimplePrimary<?> p : window.getLayout().getPrimaries()) {
            if (p.getPrimaryKey() >= 3) {
                window.getLayout().removePrimary(p.getPrimaryKey());
            }
        }

        if (bodyModel != null) {
            for (BodyUsageCategory bodyUsageCategory : BodyModel.allCategories) {
                List<BodyUsageCategory> bodyCategories = this.bodyModel.getUsageModels().stream().map(UsageModel::getType).collect(Collectors.toList());
                if (bodyCategories.contains(bodyUsageCategory)) {
                    int primaryId = BodyModel.allCategories.indexOf(bodyUsageCategory) + 3;
                    SectionField<BodySettingsWindowController> bodyUsageField = new SectionField<>(primaryId, bodyUsageCategory.getOptionsTitle(), ResourceManager.getInstance().mainButtonTextureRegion, this);
                    window.addPrimary(bodyUsageField);
                    switch (bodyUsageCategory) {
                        case RANGED_MANUAL:
                            ManualProperties manualProperties = bodyModel.getUsageModelProperties(BodyUsageCategory.RANGED_MANUAL);

                            TitledTextField<BodySettingsWindowController> reloadTimeField = new TitledTextField<>("Reload Time:", 6, 5, 80);
                            rangedManualReloadTimeTextField = reloadTimeField.getAttachment();

                            reloadTimeField.getAttachment().setBehavior(new TextFieldBehavior<BodySettingsWindowController>(this, rangedManualReloadTimeTextField, Keyboard.KeyboardType.Numeric, gunReloadTimeValidator, true) {
                                @Override
                                protected void informControllerTextFieldTapped() {
                                    BodySettingsWindowController.super.onTextFieldTapped(rangedManualReloadTimeTextField);
                                }

                                @Override
                                protected void informControllerTextFieldReleased() {
                                    BodySettingsWindowController.super.onTextFieldReleased(rangedManualReloadTimeTextField);
                                }
                            });

                            FieldWithError reloadTimeFieldWithError = new FieldWithError(reloadTimeField);
                            SimpleSecondary<FieldWithError> fireRateElement = new SimpleSecondary<>(primaryId, 1, reloadTimeFieldWithError);
                            window.addSecondary(fireRateElement);

                            rangedManualReloadTimeTextField.getBehavior().setReleaseAction(() -> {
                                float reloadTime = Integer.parseInt(rangedManualReloadTimeTextField.getTextString());
                                assert manualProperties != null;
                                manualProperties.setReloadTime(reloadTime);
                            });

                            createProjectilesField(primaryId, 2,manualProperties);
                            break;
                        case RANGED_SEMI_AUTOMATIC:
                            SemiAutomaticProperties semiAutomaticProperties =bodyModel.getUsageModelProperties(BodyUsageCategory.RANGED_SEMI_AUTOMATIC);
                            TitledQuantity<BodySettingsWindowController> titledFireRateQuantity = new TitledQuantity<>("Fire Rate:", 16, "r", 5, 50);
                            rangedSemAutomaticFireRateQuantity = titledFireRateQuantity.getAttachment();
                            titledFireRateQuantity.getAttachment().setBehavior(new QuantityBehavior<BodySettingsWindowController>(this, rangedSemAutomaticFireRateQuantity) {
                                @Override
                                public void informControllerQuantityUpdated(Quantity<?> quantity) {

                                }
                            });

                            FieldWithError muzzleVelocityFieldWithError = new FieldWithError(titledFireRateQuantity);
                            SimpleSecondary<FieldWithError> rangedAutomaticFireRateElement = new SimpleSecondary<>(primaryId, 1, muzzleVelocityFieldWithError);
                            window.addSecondary(rangedAutomaticFireRateElement);
                            rangedSemAutomaticFireRateQuantity.getBehavior().setChangeAction(() ->semiAutomaticProperties.setFireRate(rangedSemAutomaticFireRateQuantity.getRatio()));
                            createProjectilesField(primaryId,2, semiAutomaticProperties);
                            break;
                        case RANGED_AUTOMATIC:
                            AutomaticProperties automaticProperties = bodyModel.getUsageModelProperties(BodyUsageCategory.RANGED_AUTOMATIC);
                            TitledQuantity<BodySettingsWindowController> titledFireRateQuantity_ = new TitledQuantity<>("Fire Rate:", 16, "r", 5, 50);
                            rangedAutomaticFireRateQuantity = titledFireRateQuantity_.getAttachment();
                            titledFireRateQuantity_.getAttachment().setBehavior(new QuantityBehavior<BodySettingsWindowController>(this, rangedAutomaticFireRateQuantity) {
                                @Override
                                public void informControllerQuantityUpdated(Quantity<?> quantity) {

                                }
                            });

                            FieldWithError muzzleVelocityFieldWithError_ = new FieldWithError(titledFireRateQuantity_);
                            SimpleSecondary<FieldWithError> rangedAutomaticFireRateElement_ = new SimpleSecondary<>(primaryId, 1, muzzleVelocityFieldWithError_);
                            window.addSecondary(rangedAutomaticFireRateElement_);
                            rangedAutomaticFireRateQuantity.getBehavior().setChangeAction(() ->automaticProperties.setFireRate(rangedAutomaticFireRateQuantity.getRatio()));

                            createProjectilesField(primaryId,2, automaticProperties);
                            break;
                    }
                }
            }
        }
        window.getLayout().updateLayout();
    }

    private void createProjectilesField(int primaryId,int secondaryId, RangedProperties rangedProperties) {
        SecondarySectionField<BodySettingsWindowController> projectileField = new SecondarySectionField<>(primaryId,secondaryId, "Projectiles", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addSecondary(projectileField);
        List<ProjectileModel> projectiles = userInterface.getToolModel().getBodies().stream().map(BodyModel::getProjectiles).flatMap(Collection::stream).collect(Collectors.toList());
        for(ProjectileModel projectileModel:projectiles){
            createProjectileButton(primaryId,secondaryId,projectileModel, rangedProperties);
        }
    }

    private void createProjectileButton(int primaryKey, int secondaryKey, ProjectileModel projectileModel, RangedProperties rangedProperties){
        ButtonWithText<BodySettingsWindowController> projectileButton = new ButtonWithText<>
                (projectileModel.getModelName(), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);
        SimpleTertiary<ButtonWithText<?>> projectileField = new SimpleTertiary<>(primaryKey, secondaryKey, projectileModel.getProjectileId(), projectileButton);
        window.addTertiary(projectileField);
        projectileButton.setBehavior(new ButtonBehavior<BodySettingsWindowController>(this, projectileButton) {
            @Override
            public void informControllerButtonClicked() {
                onProjectileButtonClicked(projectileButton);
                rangedProperties.getProjectileModelList().add(projectileModel);
            }

            @Override
            public void informControllerButtonReleased() {
                rangedProperties.getProjectileModelList().remove(projectileModel);
            }
        });
        if(rangedProperties.getProjectileIds().contains(projectileModel.getProjectileId())){
            projectileButton.updateState(Button.State.PRESSED);
        }
    }

    private void onProjectileButtonClicked(ButtonWithText<?> projectileButton) {

    }

    private void onCategoryButtonPressed(SimpleSecondary<?> categoryField, BodyUsageCategory e) {
        super.onSecondaryButtonClicked(categoryField);
        int size = window.getLayout().getSecondariesSize(2);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
            if (categoryField != other && BodyModel.allCategories.get(other.getSecondaryKey()).getGroup() == e.getGroup()) {
                Element main = other.getMain();
                if (main instanceof TitledButton) {
                    BodyUsageCategory otherUsage = BodyModel.allCategories.get(other.getSecondaryKey());
                    ((TitledButton<?>) main).getAttachment().updateState(Button.State.NORMAL);
                    onUsageRemoved(otherUsage);
                }
            }
        }
        onUsageAdded(e);
        updateUsageSettings();
    }

    private void onCategoryButtonReleased(SimpleSecondary<?> categoryField, BodyUsageCategory e) {
        super.onSecondaryButtonClicked(categoryField);
        onUsageRemoved(e);
        updateUsageSettings();
    }

    public void onUsageAdded(BodyUsageCategory e) {
        if(e==BodyUsageCategory.RANGED_AUTOMATIC||e==BodyUsageCategory.RANGED_MANUAL||e==BodyUsageCategory.RANGED_SEMI_AUTOMATIC) {
            UsageModel<RangedProperties> usage = new UsageModel<>("", e);
            RangedProperties properties = usage.getProperties();
            properties.setProjectileIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }

    }

    public void onUsageRemoved(BodyUsageCategory bodyUsageCategory) {
        bodyModel.getUsageModels().removeIf(e->e.getType()==bodyUsageCategory);
    }


    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        layerWindowController.onResume();
        layerWindowController.changeBodyName(model.getModelName(), bodyModel.getBodyId());
    }
    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        layerWindowController.onResume();
    }

    private void createCategoryCheckBox(BodyUsageCategory bodyUsageCategory,boolean pressed) {
        TitledButton<BodySettingsWindowController> categoryCheckbox = new TitledButton<>
                (bodyUsageCategory.getName(), ResourceManager.getInstance().checkBoxTextureRegion, Button.ButtonType.Selector, 5f, true);

        SimpleSecondary<TitledButton<BodySettingsWindowController>> categoryField = new SimpleSecondary<>(2, BodyModel.allCategories.indexOf(bodyUsageCategory), categoryCheckbox);
        window.addSecondary(categoryField);
        categoryCheckbox.getAttachment().setBehavior(new ButtonBehavior<BodySettingsWindowController>(this, categoryCheckbox.getAttachment()) {
            @Override
            public void informControllerButtonClicked() {
                onCategoryButtonPressed(categoryField, bodyUsageCategory);
            }

            @Override
            public void informControllerButtonReleased() {
                onCategoryButtonReleased(categoryField, bodyUsageCategory);
            }
        });
        if(pressed){
            categoryCheckbox.getAttachment().updateState(Button.State.PRESSED);
        }
    }

}
