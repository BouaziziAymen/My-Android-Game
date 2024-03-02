package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.entities.blockvisitors.utilities.ToolUtils;
import com.evolgames.entities.properties.BodyProperties;
import com.evolgames.entities.properties.usage.BombUsageProperties;
import com.evolgames.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.entities.properties.usage.FuzeBombUsageProperties;
import com.evolgames.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.entities.properties.usage.MissileProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.entities.properties.usage.RocketProperties;
import com.evolgames.entities.properties.usage.ShooterProperties;
import com.evolgames.entities.properties.usage.SlashProperties;
import com.evolgames.entities.properties.usage.SmashProperties;
import com.evolgames.entities.properties.usage.StabProperties;
import com.evolgames.entities.properties.usage.ThrowProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SecondarySectionField;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BodySettingsWindowController extends SettingsWindowController<BodyProperties> {

    private final AlphaNumericValidator bodyNameValidator = new AlphaNumericValidator(8, 5);
    private final NumericValidator gunReloadTimeValidator =
            new NumericValidator(false, false, 0, 1000, 3, 0);
    private final NumericValidator rocketFuelValidator =
            new NumericValidator(false, false, 0, 1000, 3, 0);
    private final NumericValidator roundsValidator =
            new NumericValidator(false, false, 0, 1000, 3, 0);
    private final NumericValidator bombDelayValidator =
            new NumericValidator(false, true, 0.01f, 1000f, 3, 2);
    private final NumericValidator bombMinImpactValidator =
            new NumericValidator(false, true, 0.01f, 9999f, 4, 2);
    private final Hashtable<String, ButtonWithText<ProjectileOptionController>> missileButtonsTable;
    private LayerWindowController layerWindowController;
    private TextField<BodySettingsWindowController> bodyNameTextField;
    // Automatic
    // Manual
    private TextField<BodySettingsWindowController> rangedManualReloadTimeTextField;
    // Semi Automatic
    private Quantity<BodySettingsWindowController> fireRateQuantityField;
    private Quantity<BodySettingsWindowController> slashSpeedQuantityField;
    private BodyModel bodyModel;
    private EditorUserInterface editorUserInterface;
    private TextField<BodySettingsWindowController> roundsTextField;
    private TextField<BodySettingsWindowController> bombDelayTextField;
    private TextField<BodySettingsWindowController> bombMinImpactTextField;
    private Quantity<BodySettingsWindowController> rocketPowerQuantityField;
    private Quantity<BodySettingsWindowController> missileControlQuantityField;
    private TextField<BodySettingsWindowController> rocketFuelTextField;

    public BodySettingsWindowController() {
        this.missileButtonsTable = new Hashtable<>();
    }

    public void setLayerWindowController(LayerWindowController layerWindowController) {
        this.layerWindowController = layerWindowController;
    }

    @Override
    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    void onModelUpdated(ProperModel<BodyProperties> model) {
        super.onModelUpdated(model);
        setBodyName(model.getModelName());
        this.bodyModel = (BodyModel) model;

        createCategorySection();
        updateUsageSettings();
        for (BodyUsageCategory usageCategory :
                this.bodyModel.getUsageModels().stream()
                        .map(UsageModel::getType)
                        .collect(Collectors.toList())) {
            updateUsageCategoryFields(usageCategory);
        }
        updateScroller();
    }

  private void updateUsageCategoryFields(BodyUsageCategory usageCategory) {
    switch (usageCategory) {
      case SHOOTER:
        UsageModel<ShooterProperties> manualUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        ShooterProperties manualProps = manualUsageModel.getProperties();
        setReloadTime(manualProps.getReloadTime());
        setNumberOfRounds(manualProps.getNumberOfRounds());
        break;
      case SHOOTER_CONTINUOUS:
        UsageModel<ContinuousShooterProperties> automaticUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        ContinuousShooterProperties autoProps = automaticUsageModel.getProperties();
        setFireRate(autoProps.getFireRate());
        setNumberOfRounds(autoProps.getNumberOfRounds());
        setReloadTime(autoProps.getReloadTime());
        break;
      case TIME_BOMB:
        UsageModel<TimeBombUsageProperties> timeBombUsagePropertiesUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        TimeBombUsageProperties timeBombUsageProperties =
            timeBombUsagePropertiesUsageModel.getProperties();
        setBombDelay(timeBombUsageProperties.getDelay());
        break;
      case FUZE_BOMB:
        UsageModel<FuzeBombUsageProperties> fuzeBombUsagePropertiesUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        FuzeBombUsageProperties fuzeBombUsageProperties =
            fuzeBombUsagePropertiesUsageModel.getProperties();
        setBombDelay(fuzeBombUsageProperties.getDelay());
        break;
      case IMPACT_BOMB:
        UsageModel<ImpactBombUsageProperties> impactBombUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        ImpactBombUsageProperties impactBombUsageProperties = impactBombUsageModel.getProperties();
        setBombDelay(impactBombUsageProperties.getDelay());
        setBombMinImpact(impactBombUsageProperties.getMinImpact());
        break;
      case SLASHER:
        UsageModel<SlashProperties> slashPropertiesUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        SlashProperties slashUsageProperties = slashPropertiesUsageModel.getProperties();
        // setSlashSpeed(slashUsageProperties.getSpeed());
        break;
      case BLUNT:
        break;
      case STABBER:
        break;
      case THROWING:
        break;
      case FLAME_THROWER:
        break;
      case ROCKET:
        UsageModel<RocketProperties> rocketPropertiesUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        RocketProperties rocketProperties = rocketPropertiesUsageModel.getProperties();
        setRocketPower(rocketProperties.getPower());
        setFuel(rocketProperties.getFuel());
        break;
      case MISSILE:
        UsageModel<MissileProperties> missilePropertiesUsageModel =
            this.bodyModel.getUsageModel(usageCategory);
        MissileProperties missileProperties = missilePropertiesUsageModel.getProperties();
        setRocketPower(missileProperties.getPower());
        setFuel(missileProperties.getFuel());
        setMissileControl(missileProperties.getControl());
        break;
      case LIQUID_CONTAINER:
        UsageModel<LiquidContainerProperties> liquidContainerPropertiesUsageModel =
                this.bodyModel.getUsageModel(usageCategory);
        LiquidContainerProperties liquidContainerProperties = liquidContainerPropertiesUsageModel.getProperties();

                break;
            case ROCKET_LAUNCHER:
                UsageModel<RocketLauncherProperties> rocketLauncherPropertiesUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                RocketLauncherProperties rocketLauncherProperties = rocketLauncherPropertiesUsageModel.getProperties();
                setReloadTime(rocketLauncherProperties.getReloadTime());
                break;
        }
    }

    private void createNumberOfRoundsTextField(
            int primaryKey, int secondaryKey, RangedProperties rangedProperties) {
        TitledTextField<BodySettingsWindowController> roundsField =
                new TitledTextField<>("Rounds:", 5, 5, 76);
        roundsTextField = roundsField.getAttachment();

        roundsField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                roundsField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                roundsValidator,
                                true) {
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
        SimpleSecondary<FieldWithError> roundsElement =
                new SimpleSecondary<>(primaryKey, secondaryKey, titleRoundsFieldWithError);
        window.addSecondary(roundsElement);
        roundsTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            int rounds = Integer.parseInt(roundsTextField.getTextString());
                            rangedProperties.setNumberOfRounds(rounds);
                        });
    }

    private void setSlashSpeed(float speed) {
        slashSpeedQuantityField.updateRatio(speed);
    }

    private void setFireRate(float fireRate) {
        fireRateQuantityField.updateRatio(fireRate);
    }

    private void setRocketPower(float power) {
        rocketPowerQuantityField.updateRatio(power);
    }

    private void setMissileControl(float control) {
        missileControlQuantityField.updateRatio(control);
    }


    private void setReloadTime(float reloadTime) {
        rangedManualReloadTimeTextField
                .getBehavior()
                .setTextValidated("" + Float.valueOf(reloadTime).intValue());
    }

    private void setFuel(int fuel) {
        rocketFuelTextField.getBehavior().setTextValidated("" + fuel);
    }

    private void setBombDelay(float delay) {
        bombDelayTextField.getBehavior().setTextValidated("" + delay);
    }

    private void setBombMinImpact(float delay) {
        bombMinImpactTextField.getBehavior().setTextValidated("" + delay);
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
        SectionField<BodySettingsWindowController> sectionField =
                new SectionField<>(
                        1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<BodySettingsWindowController> bodyNameField =
                new TitledTextField<>("Body Name:", 10);
        bodyNameTextField = bodyNameField.getAttachment();

        bodyNameTextField.setBehavior(
                new TextFieldBehavior<BodySettingsWindowController>(
                        this, bodyNameTextField, Keyboard.KeyboardType.AlphaNumeric, bodyNameValidator) {
                    @Override
                    protected void informControllerTextFieldTapped() {
                        BodySettingsWindowController.super.onTextFieldTapped(bodyNameTextField);
                    }

                    @Override
                    protected void informControllerTextFieldReleased() {
                        BodySettingsWindowController.super.onTextFieldReleased(bodyNameTextField);
                    }
                });
        bodyNameTextField
                .getBehavior()
                .setReleaseAction(() -> model.setModelName(bodyNameTextField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(bodyNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);

        createCategorySection();
        updateUsageSettings();

        updateLayout();
        window.createScroller();
    }

    private void createCategorySection() {

        if (window.getLayout().getPrimaries().size() >= 2) {
            window.getLayout().removePrimary(2);
        }
        SectionField<BodySettingsWindowController> categorySection =
                new SectionField<>(
                        2, "Category", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(categorySection);
        if (this.bodyModel != null) {
            BodyModel.allCategories.forEach(
                    e ->
                            this.createCategoryCheckBox(
                                    e, this.bodyModel.getUsageModels().stream().anyMatch(s -> s.getType() == e)));
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
                List<BodyUsageCategory> bodyCategories =
                        this.bodyModel.getUsageModels().stream()
                                .map(UsageModel::getType)
                                .collect(Collectors.toList());
                if (bodyCategories.contains(bodyUsageCategory)) {
                    int primaryId = BodyModel.allCategories.indexOf(bodyUsageCategory) + 3;
                    SectionField<BodySettingsWindowController> bodyUsageField =
                            new SectionField<>(
                                    primaryId,
                                    bodyUsageCategory.getOptionsTitle(),
                                    ResourceManager.getInstance().mainButtonTextureRegion,
                                    this);
                    window.addPrimary(bodyUsageField);
                    switch (bodyUsageCategory) {
                        case SHOOTER:
                            ShooterProperties shooterProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.SHOOTER);

                            createReloadTimeField(primaryId, 1, shooterProperties);
                            createNumberOfRoundsTextField(primaryId, 2, shooterProperties);
                            createProjectilesField(primaryId, 3, shooterProperties);
                            break;
                        case SHOOTER_CONTINUOUS:
                            ContinuousShooterProperties automaticProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.SHOOTER_CONTINUOUS);
                            createReloadTimeField(primaryId, 0, automaticProperties);
                            createFireRateField(
                                    primaryId,
                                    1,
                                    () ->
                                            fireRateQuantityField
                                                    .getBehavior()
                                                    .setChangeAction(
                                                            () ->
                                                                    automaticProperties.setFireRate(
                                                                            fireRateQuantityField.getRatio())));
                            createNumberOfRoundsTextField(primaryId, 2, automaticProperties);
                            createProjectilesField(primaryId, 3, automaticProperties);
                            break;
                        case TIME_BOMB:
                            TimeBombUsageProperties timeBombProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.TIME_BOMB);
                            createTimeBombDelayField(primaryId, 1, timeBombProperties);
                            createBombsField(primaryId, 2, timeBombProperties);
                            break;
                        case FUZE_BOMB:
                            FuzeBombUsageProperties fuzeBombProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.FUZE_BOMB);
                            createTimeBombDelayField(primaryId, 1, fuzeBombProperties);
                            createBombsField(primaryId, 2, fuzeBombProperties);
                            break;
                        case IMPACT_BOMB:
                            ImpactBombUsageProperties impactBombProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.IMPACT_BOMB);
                            createTimeBombDelayField(primaryId, 1, impactBombProperties);
                            createBombMinImpactField(primaryId, 2, impactBombProperties);
                            createBombsField(primaryId, 3, impactBombProperties);
                            break;
                        case SLASHER:
                            SlashProperties slasherProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.SLASHER);
                            // createSlashSpeedField(primaryId,1,()->
                            //        slashSpeedQuantityField.getBehavior().setChangeAction(() ->
                            // slasherProperties.setSpeed(slashSpeedQuantityField.getRatio())));
                            break;
                        case BLUNT:
                            break;
                        case STABBER:
                            break;
                        case THROWING:
                            break;
                        case FLAME_THROWER:
                            FlameThrowerProperties flameThrowerProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.FLAME_THROWER);
                            createFlameThrowerFireSourceField(primaryId, 1, flameThrowerProperties);
                            break;
                        case ROCKET:
                            RocketProperties rocketProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.ROCKET);
                            createRocketFireSourceField(primaryId, 0, rocketProperties);
                            createRocketPowerField(
                                    primaryId,
                                    1,
                                    () ->
                                            rocketPowerQuantityField
                                                    .getBehavior()
                                                    .setChangeAction(
                                                            () ->
                                                                    rocketProperties.setPower(rocketPowerQuantityField.getRatio())));
                            createRocketFuelField(primaryId, 2, rocketProperties);
                            break;
                        case MISSILE:
                            MissileProperties missileProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.MISSILE);
                            createRocketFireSourceField(primaryId, 0, missileProperties);
                            createRocketPowerField(
                                    primaryId,
                                    1,
                                    () ->
                                            rocketPowerQuantityField
                                                    .getBehavior()
                                                    .setChangeAction(
                                                            () ->
                                                                    missileProperties.setPower(rocketPowerQuantityField.getRatio())));
                            createMissileControlField(
                                    primaryId,
                                    2,
                                    () ->
                                            missileControlQuantityField
                                                    .getBehavior()
                                                    .setChangeAction(
                                                            () ->
                                                                    missileProperties.setControl(rocketPowerQuantityField.getRatio())));
                            createRocketFuelField(primaryId, 3, missileProperties);
                            break;
                        case LIQUID_CONTAINER:
                            LiquidContainerProperties liquidContainerProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.LIQUID_CONTAINER);
                            createLiquidSourceField(primaryId, 0, liquidContainerProperties);
                            break;
                        case ROCKET_LAUNCHER:
                            RocketLauncherProperties rocketLauncherProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.ROCKET_LAUNCHER);
                            createReloadTimeField(primaryId, 0, rocketLauncherProperties);
                            createProjectilesField(primaryId, 1, rocketLauncherProperties);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + bodyUsageCategory);
                    }
                }
            }
        }
        updateLayout();
    }

    private void createSlashSpeedField(int primaryId, int secondaryId, Runnable action) {
        TitledQuantity<BodySettingsWindowController> titledSlashSpeedQuantity =
                new TitledQuantity<>("Speed:", 8, "b", 5, 50);
        slashSpeedQuantityField = titledSlashSpeedQuantity.getAttachment();
        titledSlashSpeedQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, slashSpeedQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError slashSpeedFieldWithError = new FieldWithError(titledSlashSpeedQuantity);
        SimpleSecondary<FieldWithError> slashSpeedElement =
                new SimpleSecondary<>(primaryId, secondaryId, slashSpeedFieldWithError);
        window.addSecondary(slashSpeedElement);
        action.run();
    }

    private void createFireRateField(int primaryId, int secondaryId, Runnable action) {
        TitledQuantity<BodySettingsWindowController> titledFireRateQuantity =
                new TitledQuantity<>("Fire Rate:", 16, "r", 5, 50);
        fireRateQuantityField = titledFireRateQuantity.getAttachment();
        titledFireRateQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, fireRateQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError fireRateFieldWithError = new FieldWithError(titledFireRateQuantity);
        SimpleSecondary<FieldWithError> rangedFireRateElement =
                new SimpleSecondary<>(primaryId, secondaryId, fireRateFieldWithError);
        window.addSecondary(rangedFireRateElement);
        action.run();
    }

    private void createRocketPowerField(int primaryId, int secondaryId, Runnable action) {
        TitledQuantity<BodySettingsWindowController> titledMotorPowerQuantity =
                new TitledQuantity<>("Power:", 16, "r", 5, 50);
        rocketPowerQuantityField = titledMotorPowerQuantity.getAttachment();
        titledMotorPowerQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, rocketPowerQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError rocketPowerFieldWithError = new FieldWithError(titledMotorPowerQuantity);
        SimpleSecondary<FieldWithError> rocketPowerElement =
                new SimpleSecondary<>(primaryId, secondaryId, rocketPowerFieldWithError);
        window.addSecondary(rocketPowerElement);
        action.run();
    }

    private void createMissileControlField(int primaryId, int secondaryId, Runnable action) {
        TitledQuantity<BodySettingsWindowController> titledMissileControlQuantity =
                new TitledQuantity<>("Control:", 16, "r", 5, 50);
        missileControlQuantityField = titledMissileControlQuantity.getAttachment();
        titledMissileControlQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, missileControlQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError missileControlFieldWithError = new FieldWithError(titledMissileControlQuantity);
        SimpleSecondary<FieldWithError> missileControlElement =
                new SimpleSecondary<>(primaryId, secondaryId, missileControlFieldWithError);
        window.addSecondary(missileControlElement);
        action.run();
    }

    private void createRocketFuelField(
            int primaryId, int secondaryId, RocketProperties rocketProperties) {
        TitledTextField<BodySettingsWindowController> rocketFuelField =
                new TitledTextField<>("Fuel:", 6, 5, 80);
        rocketFuelTextField = rocketFuelField.getAttachment();

        rocketFuelField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                rocketFuelTextField,
                                Keyboard.KeyboardType.Numeric,
                                rocketFuelValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(rocketFuelTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(rocketFuelTextField);
                            }
                        });

        FieldWithError rocketFuelFieldWithError = new FieldWithError(rocketFuelField);
        SimpleSecondary<FieldWithError> rocketFuelElement =
                new SimpleSecondary<>(primaryId, secondaryId, rocketFuelFieldWithError);
        window.addSecondary(rocketFuelElement);

        rocketFuelTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            int fuel = Integer.parseInt(rocketFuelTextField.getTextString());
                            assert rocketProperties != null;
                            rocketProperties.setFuel(fuel);
                        });
    }

    private void createReloadTimeField(
            int primaryId, int secondaryId, RangedProperties rangedProperties) {
        TitledTextField<BodySettingsWindowController> reloadTimeField =
                new TitledTextField<>("Reload Time:", 6, 5, 80);
        rangedManualReloadTimeTextField = reloadTimeField.getAttachment();

        reloadTimeField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                rangedManualReloadTimeTextField,
                                Keyboard.KeyboardType.Numeric,
                                gunReloadTimeValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(
                                        rangedManualReloadTimeTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(
                                        rangedManualReloadTimeTextField);
                            }
                        });

        FieldWithError reloadTimeFieldWithError = new FieldWithError(reloadTimeField);
        SimpleSecondary<FieldWithError> fireRateElement =
                new SimpleSecondary<>(primaryId, secondaryId, reloadTimeFieldWithError);
        window.addSecondary(fireRateElement);

        rangedManualReloadTimeTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float reloadTime = Integer.parseInt(rangedManualReloadTimeTextField.getTextString());
                            assert rangedProperties != null;
                            rangedProperties.setReloadTime(reloadTime);
                        });
    }

    private void createTimeBombDelayField(
            int primaryId, int secondaryId, BombUsageProperties bombUsageProperties) {
        TitledTextField<BodySettingsWindowController> bombDelayField =
                new TitledTextField<>("Delay:", 6, 5, 80);
        bombDelayTextField = bombDelayField.getAttachment();

        bombDelayField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this, bombDelayTextField, Keyboard.KeyboardType.Numeric, bombDelayValidator, true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(bombDelayTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(bombDelayTextField);
                            }
                        });

        FieldWithError delayFieldWithError = new FieldWithError(bombDelayField);
        SimpleSecondary<FieldWithError> delayElement =
                new SimpleSecondary<>(primaryId, secondaryId, delayFieldWithError);
        window.addSecondary(delayElement);

        bombDelayTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float delay = Float.parseFloat(bombDelayTextField.getTextString());
                            assert bombUsageProperties != null;
                            bombUsageProperties.setDelay(delay);
                        });
    }

    private void createBombMinImpactField(
            int primaryId, int secondaryId, ImpactBombUsageProperties bombUsageProperties) {
        TitledTextField<BodySettingsWindowController> minImpactField =
                new TitledTextField<>("Min Impact:", 6, 5, 80);
        bombMinImpactTextField = minImpactField.getAttachment();

        minImpactField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                bombMinImpactTextField,
                                Keyboard.KeyboardType.Numeric,
                                bombMinImpactValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(bombMinImpactTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(bombMinImpactTextField);
                            }
                        });

        FieldWithError minImpactFieldWithError = new FieldWithError(minImpactField);
        SimpleSecondary<FieldWithError> impactElement =
                new SimpleSecondary<>(primaryId, secondaryId, minImpactFieldWithError);
        window.addSecondary(impactElement);

        bombDelayTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float minImpact = Float.parseFloat(bombMinImpactTextField.getTextString());
                            assert bombUsageProperties != null;
                            bombUsageProperties.setMinImpact(minImpact);
                        });
    }

    private void createProjectilesField(
            int primaryId, int secondaryId, RangedProperties rangedProperties) {
        SecondarySectionField<BodySettingsWindowController> projectileField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        "Projectiles",
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(projectileField);
        List<ProjectileModel> projectiles =
                editorUserInterface.getToolModel().getBodies().stream()
                        .map(BodyModel::getProjectileModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        for (ProjectileModel projectileModel : projectiles) {
            createProjectileButton(primaryId, secondaryId, projectileModel, rangedProperties);
        }
    }

    private void createFlameThrowerFireSourceField(
            int primaryId, int secondaryId, FlameThrowerProperties flameThrowerProperties) {
        SecondarySectionField<BodySettingsWindowController> fireSourcesField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        "Fire Sources",
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(fireSourcesField);
        List<FireSourceModel> fireSources =
                editorUserInterface.getToolModel().getBodies().stream()
                        .map(BodyModel::getFireSourceModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        for (FireSourceModel fireSourceModel : fireSources) {
            createFlameThrowerFireSourceButton(
                    primaryId, secondaryId, fireSourceModel, flameThrowerProperties);
        }
    }

    private void createFlameThrowerFireSourceButton(
            int primaryKey,
            int secondaryKey,
            FireSourceModel fireSourceModel,
            FlameThrowerProperties flameThrowerProperties) {
        ButtonWithText<BodySettingsWindowController> fireSourceButton =
                new ButtonWithText<>(
                        fireSourceModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> fireSourceField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, fireSourceModel.getFireSourceId(), fireSourceButton);
        window.addTertiary(fireSourceField);
        fireSourceButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, fireSourceButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        flameThrowerProperties.getFireSources().add(fireSourceModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        flameThrowerProperties.getFireSources().remove(fireSourceModel);
                    }
                });
        if (flameThrowerProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
            fireSourceButton.updateState(Button.State.PRESSED);
        }
    }

    private void createLiquidSourceField(
            int primaryId, int secondaryId, LiquidContainerProperties liquidContainerProperties) {
        SecondarySectionField<BodySettingsWindowController> liquidSourcesField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        "Liquid Sources",
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(liquidSourcesField);
        List<LiquidSourceModel> liquidSourceModelList =
                editorUserInterface.getToolModel().getBodies().stream()
                        .map(BodyModel::getLiquidSourceModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        for (LiquidSourceModel liquidSourceModel : liquidSourceModelList) {
            createLiquidSourceButton(
                    primaryId, secondaryId, liquidSourceModel, liquidContainerProperties);
        }
    }

    private void createLiquidSourceButton(
            int primaryKey,
            int secondaryKey,
            LiquidSourceModel liquidSourceModel,
            LiquidContainerProperties liquidContainerProperties) {
        ButtonWithText<BodySettingsWindowController> liquidSourceButton =
                new ButtonWithText<>(
                        liquidSourceModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> liquidField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, liquidSourceModel.getLiquidSourceId(), liquidSourceButton);
        window.addTertiary(liquidField);
        liquidSourceButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, liquidSourceButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        liquidContainerProperties.getLiquidSourceModelList().add(liquidSourceModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        liquidContainerProperties.getLiquidSourceModelList().remove(liquidSourceModel);
                    }
                });
        if (liquidContainerProperties.getLiquidSourceIds().contains(liquidSourceModel.getLiquidSourceId())) {
            liquidSourceButton.updateState(Button.State.PRESSED);
        }
    }


    private void createProjectileButton(
            int primaryKey,
            int secondaryKey,
            ProjectileModel projectileModel,
            RangedProperties rangedProperties) {
        ButtonWithText<BodySettingsWindowController> projectileButton =
                new ButtonWithText<>(
                        projectileModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> projectileField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, projectileModel.getProjectileId(), projectileButton);
        window.addTertiary(projectileField);
        projectileButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, projectileButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        rangedProperties.getProjectileModelList().add(projectileModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        rangedProperties.getProjectileModelList().remove(projectileModel);
                    }
                });
        if (rangedProperties.getProjectileIds().contains(projectileModel.getProjectileId())) {
            projectileButton.updateState(Button.State.PRESSED);
        }
    }

    private void onCategoryButtonPressed(SimpleSecondary<?> categoryField, BodyUsageCategory e) {
        super.onSecondaryButtonClicked(categoryField);
        int size = window.getLayout().getSecondariesSize(2);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
            if (categoryField != other
                    && BodyModel.allCategories.get(other.getSecondaryKey()).getGroup() == e.getGroup()) {
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
        onLayoutChanged();
    }

    private void onCategoryButtonReleased(SimpleSecondary<?> categoryField, BodyUsageCategory e) {
        super.onSecondaryButtonClicked(categoryField);
        onUsageRemoved(e);
        updateUsageSettings();
    }

    public void onUsageAdded(BodyUsageCategory e) {
        if (e == BodyUsageCategory.SHOOTER_CONTINUOUS || e == BodyUsageCategory.SHOOTER) {
            UsageModel<RangedProperties> usage = new UsageModel<>("", e);
            RangedProperties properties = usage.getProperties();
            properties.setProjectileIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.TIME_BOMB
                || e == BodyUsageCategory.FUZE_BOMB
                || e == BodyUsageCategory.IMPACT_BOMB) {
            UsageModel<BombUsageProperties> usage = new UsageModel<>("", e);
            BombUsageProperties properties = usage.getProperties();
            properties.setBombIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.SLASHER) {
            UsageModel<SlashProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.STABBER) {
            UsageModel<StabProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.BLUNT) {
            UsageModel<SmashProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
        }

        if (e == BodyUsageCategory.THROWING) {
            UsageModel<ThrowProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
        }

        if (e == BodyUsageCategory.FLAME_THROWER) {
            UsageModel<FlameThrowerProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
            FlameThrowerProperties properties = usage.getProperties();
            properties.setFireSourceIds(new ArrayList<>());
        }
        if (e == BodyUsageCategory.ROCKET) {
            UsageModel<RocketProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
            RocketProperties properties = usage.getProperties();
            properties.setFireSourceModelListIds(new ArrayList<>());
        }
        if (e == BodyUsageCategory.MISSILE) {
            UsageModel<MissileProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
            MissileProperties properties = usage.getProperties();
            properties.setFireSourceModelListIds(new ArrayList<>());
        }
        if (e == BodyUsageCategory.LIQUID_CONTAINER) {
            UsageModel<LiquidContainerProperties> usage = new UsageModel<>("", e);
            LiquidContainerProperties properties = usage.getProperties();
            properties.setLiquidSourceIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.ROCKET_LAUNCHER) {
            UsageModel<RocketLauncherProperties> usage = new UsageModel<>("", e);
            RocketLauncherProperties properties = usage.getProperties();
            properties.setProjectileIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
    }

    public void onUsageRemoved(BodyUsageCategory bodyUsageCategory) {
        bodyModel.getUsageModels().removeIf(e -> e.getType() == bodyUsageCategory);
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

    private void createCategoryCheckBox(BodyUsageCategory bodyUsageCategory, boolean pressed) {
        TitledButton<BodySettingsWindowController> categoryCheckbox =
                new TitledButton<>(
                        bodyUsageCategory.getName(),
                        ResourceManager.getInstance().checkBoxTextureRegion,
                        Button.ButtonType.Selector,
                        5f,
                        true);

        SimpleSecondary<TitledButton<BodySettingsWindowController>> categoryField =
                new SimpleSecondary<>(
                        2, BodyModel.allCategories.indexOf(bodyUsageCategory), categoryCheckbox);
        window.addSecondary(categoryField);
        categoryCheckbox
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<BodySettingsWindowController>(
                                this, categoryCheckbox.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                onCategoryButtonPressed(categoryField, bodyUsageCategory);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                onCategoryButtonReleased(categoryField, bodyUsageCategory);
                            }
                        });
        if (pressed) {
            categoryCheckbox.getAttachment().updateState(Button.State.PRESSED);
        }
    }

    private void createBombsField(
            int primaryId, int secondaryId, BombUsageProperties bombUsageProperties) {
        SecondarySectionField<BodySettingsWindowController> projectileField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        "Bombs",
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(projectileField);
        List<BombModel> bombModelList =
                editorUserInterface.getToolModel().getBodies().stream()
                        .map(BodyModel::getBombModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        for (BombModel bombModel : bombModelList) {
            createBombButton(primaryId, secondaryId, bombModel, bombUsageProperties);
        }
    }

    private void createBombButton(
            int primaryKey,
            int secondaryKey,
            BombModel bombModel,
            BombUsageProperties bombUsageProperties) {
        ButtonWithText<BodySettingsWindowController> bombButton =
                new ButtonWithText<>(
                        bombModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> bombField =
                new SimpleTertiary<>(primaryKey, secondaryKey, bombModel.getBombId(), bombButton);
        window.addTertiary(bombField);
        bombButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, bombButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        bombUsageProperties.getBombModelList().add(bombModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        bombUsageProperties.getBombModelList().remove(bombModel);
                    }
                });
        if (bombUsageProperties.getBombModelList().stream()
                .anyMatch(b -> bombModel.getBombId() == b.getBombId())) {
            bombButton.updateState(Button.State.PRESSED);
        }
    }

    private void createRocketFireSourceField(
            int primaryId, int secondaryId, RocketProperties rocketProperties) {
        SecondarySectionField<BodySettingsWindowController> fireSourcesField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        "Fire Sources",
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(fireSourcesField);
        List<FireSourceModel> fireSources =
                editorUserInterface.getToolModel().getBodies().stream()
                        .map(BodyModel::getFireSourceModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        for (FireSourceModel fireSourceModel : fireSources) {
            createRocketFireSourceButton(primaryId, secondaryId, fireSourceModel, rocketProperties);
        }
    }

    private void createRocketFireSourceButton(
            int primaryKey,
            int secondaryKey,
            FireSourceModel fireSourceModel,
            RocketProperties rocketProperties) {
        ButtonWithText<BodySettingsWindowController> fireSourceButton =
                new ButtonWithText<>(
                        fireSourceModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> fireSourceField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, fireSourceModel.getFireSourceId(), fireSourceButton);
        window.addTertiary(fireSourceField);
        fireSourceButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, fireSourceButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        rocketProperties.getFireSourceModelList().add(fireSourceModel);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        rocketProperties.getFireSourceModelList().remove(fireSourceModel);
                    }
                });
        if (rocketProperties.getFireSourceModelListIds().contains(fireSourceModel.getFireSourceId())) {
            fireSourceButton.updateState(Button.State.PRESSED);
        }
    }
}
