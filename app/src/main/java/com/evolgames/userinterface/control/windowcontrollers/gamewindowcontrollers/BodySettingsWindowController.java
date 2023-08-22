package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.properties.BodyProperties;
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
    private final NumericValidator roundsValidator = new NumericValidator(false, false, 0, 1000, 3, 0);
    //Manual
    private TextField<BodySettingsWindowController> rangedManualReloadTimeTextField;
    //Automatic

    //Semi Automatic
    private Quantity<BodySettingsWindowController> fireRateQuantityField;


    private BodyModel bodyModel;
    private UserInterface userInterface;
    private TextField<BodySettingsWindowController> roundsTextField;


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
            updateUsageCategoryFields(usageCategory);
        }

    }

    private void updateUsageCategoryFields(BodyUsageCategory usageCategory) {
        switch (usageCategory){
            case RANGED_MANUAL:
                UsageModel<ManualProperties> manualUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                ManualProperties manualProps =  manualUsageModel.getProperties();
                setReloadTime(manualProps.getReloadTime());
                setNumberOfRounds(manualProps.getNumberOfRounds());
                break;
            case RANGED_SEMI_AUTOMATIC:
                UsageModel<SemiAutomaticProperties> semiAutomaticUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                SemiAutomaticProperties semAutoProps = semiAutomaticUsageModel.getProperties();
                setFireRate(semAutoProps.getFireRate());
                setNumberOfRounds(semAutoProps.getNumberOfRounds());
                setReloadTime(semAutoProps.getReloadTime());
                break;
            case RANGED_AUTOMATIC:
                UsageModel<AutomaticProperties> automaticUsageModel =  this.bodyModel.getUsageModel(usageCategory);
                AutomaticProperties autoProps = automaticUsageModel.getProperties();
                setFireRate(autoProps.getFireRate());
                setNumberOfRounds(autoProps.getNumberOfRounds());
                setReloadTime(autoProps.getReloadTime());
                break;
        }
    }

    private void createNumberOfRoundsTextField(int primaryKey, int secondaryKey, RangedProperties rangedProperties){
        TitledTextField<BodySettingsWindowController> roundsField = new TitledTextField<>("Rounds:", 5, 5, 76);
        roundsTextField = roundsField.getAttachment();

        roundsField.getAttachment().setBehavior(new TextFieldBehavior<BodySettingsWindowController>(this, roundsField.getAttachment(), Keyboard.KeyboardType.Numeric, roundsValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                BodySettingsWindowController.super.onTextFieldTapped(roundsTextField);
            }
            @Override
            protected void informControllerTextFieldReleased() {
                BodySettingsWindowController.super.onTextFieldReleased(roundsTextField);
            }
        });
        FieldWithError titleRoundsFieldWithError = new FieldWithError(roundsField);
        SimpleSecondary<FieldWithError> roundsElement = new SimpleSecondary<>(primaryKey, secondaryKey, titleRoundsFieldWithError);
        window.addSecondary(roundsElement);
        roundsTextField.getBehavior().setReleaseAction(() -> {
            int rounds = Integer.parseInt(roundsTextField.getTextString());
            rangedProperties.setNumberOfRounds(rounds);
        });
    }


    private void setFireRate(float fireRate) {
        fireRateQuantityField.updateRatio(fireRate);
    }

    private void setReloadTime(float reloadTime) {
        rangedManualReloadTimeTextField.getBehavior().setTextValidated(""+Float.valueOf(reloadTime).intValue());
    }
    private void setNumberOfRounds(int rounds) {
        roundsTextField.getBehavior().setTextValidated("" + rounds);
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

                            createReloadTimeField(primaryId, 1,manualProperties);
                            createNumberOfRoundsTextField(primaryId,2,manualProperties);
                            createProjectilesField(primaryId, 3,manualProperties);
                            break;
                        case RANGED_SEMI_AUTOMATIC:
                            SemiAutomaticProperties semiAutomaticProperties =bodyModel.getUsageModelProperties(BodyUsageCategory.RANGED_SEMI_AUTOMATIC);
                            createReloadTimeField(primaryId, 0,semiAutomaticProperties);
                            createFireRateField(primaryId, 1,()->
                                    fireRateQuantityField.getBehavior().setChangeAction(() -> semiAutomaticProperties.setFireRate(fireRateQuantityField.getRatio())));
                            createNumberOfRoundsTextField(primaryId,2,semiAutomaticProperties);
                            createProjectilesField(primaryId,3, semiAutomaticProperties);
                            break;
                        case RANGED_AUTOMATIC:
                            AutomaticProperties automaticProperties = bodyModel.getUsageModelProperties(BodyUsageCategory.RANGED_AUTOMATIC);
                            createReloadTimeField(primaryId, 1,automaticProperties);
                            createFireRateField(primaryId, 1,()->
                                    fireRateQuantityField.getBehavior().setChangeAction(() -> automaticProperties.setFireRate(fireRateQuantityField.getRatio())));
                            createNumberOfRoundsTextField(primaryId,2,automaticProperties);
                            createProjectilesField(primaryId,3, automaticProperties);
                            break;
                    }
                }
            }
        }
        window.getLayout().updateLayout();
    }

    private void createFireRateField(int primaryId, int secondaryId,Runnable action) {
        TitledQuantity<BodySettingsWindowController> titledFireRateQuantity = new TitledQuantity<>("Fire Rate:", 16, "r", 5, 50);
        fireRateQuantityField = titledFireRateQuantity.getAttachment();
        titledFireRateQuantity.getAttachment().setBehavior(new QuantityBehavior<BodySettingsWindowController>(this, fireRateQuantityField) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });

        FieldWithError fireRateFieldWithError = new FieldWithError(titledFireRateQuantity);
        SimpleSecondary<FieldWithError> rangedFireRateElement = new SimpleSecondary<>(primaryId, secondaryId, fireRateFieldWithError);
        window.addSecondary(rangedFireRateElement);
        action.run();
    }

    private void createReloadTimeField(int primaryId,int secondaryId, RangedProperties rangedProperties) {
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
        SimpleSecondary<FieldWithError> fireRateElement = new SimpleSecondary<>(primaryId, secondaryId, reloadTimeFieldWithError);
        window.addSecondary(fireRateElement);

        rangedManualReloadTimeTextField.getBehavior().setReleaseAction(() -> {
            float reloadTime = Integer.parseInt(rangedManualReloadTimeTextField.getTextString());
            assert rangedProperties != null;
            rangedProperties.setReloadTime(reloadTime);
        });
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
        updateUsageCategoryFields(e);
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
