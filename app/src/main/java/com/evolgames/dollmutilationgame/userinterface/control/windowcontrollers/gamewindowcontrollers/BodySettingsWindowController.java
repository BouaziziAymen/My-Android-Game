package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.properties.usage.BombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.BowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.HeavyProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MissileProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MotorControlProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RangedProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.SlashProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.SmashProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.StabProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ThrowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.dollmutilationgame.userinterface.control.OutlineController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Quantity;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SecondarySectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.dollmutilationgame.entities.properties.BodyProperties;
import com.evolgames.dollmutilationgame.entities.properties.BodyUsageCategory;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.dollmutilationgame.userinterface.control.validators.NumericValidator;
import com.evolgames.dollmutilationgame.userinterface.model.BodyModel;
import com.evolgames.dollmutilationgame.userinterface.model.LayerModel;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.BombModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BodySettingsWindowController extends SettingsWindowController<BodyProperties> {

    private final AlphaNumericValidator bodyNameValidator = new AlphaNumericValidator(8, 5);
    private final NumericValidator motorPowerValidator =
            new NumericValidator(false, true, 0, 100000, 6, 2);
    private final NumericValidator gunReloadTimeValidator =
            new NumericValidator(false, false, 0, 1000, 3, 0);

    private final NumericValidator zIndexValidator =
            new NumericValidator(true, false, -1000, 1000, 3, 0);
    private final NumericValidator rocketFuelValidator =
            new NumericValidator(false, true, 0, 1000, 3, 1);
    private final NumericValidator roundsValidator =
            new NumericValidator(false, false, 0, 1000, 3, 0);
    private final NumericValidator bombDelayValidator =
            new NumericValidator(false, true, 0.01f, 1000f, 3, 2);
    private final NumericValidator bombMinImpactValidator =
            new NumericValidator(false, true, 0.01f, 9999f, 4, 2);
    private LayerWindowController layerWindowController;
    private TextField<BodySettingsWindowController> bodyNameTextField;
    // Automatic
    // Manual
    private TextField<BodySettingsWindowController> rangedManualReloadTimeTextField;
    private TextField<BodySettingsWindowController> zIndexTextField;
    // Semi Automatic
    private Quantity<BodySettingsWindowController> fireRateQuantityField;
    private Quantity<BodySettingsWindowController> forwardSpeedQuantityField, backwardSpeedQuantityField;
    private BodyModel bodyModel;
    private EditorUserInterface editorUserInterface;
    private TextField<BodySettingsWindowController> roundsTextField;
    private TextField<BodySettingsWindowController> bombDelayTextField;
    private TextField<BodySettingsWindowController> bombMinImpactTextField;
    private Quantity<BodySettingsWindowController> rocketPowerQuantityField;
    private Quantity<BodySettingsWindowController> missileControlQuantityField;
    private TextField<BodySettingsWindowController> rocketFuelTextField;
    private OutlineController outlineController;
    private SecondarySectionField<BodySettingsWindowController> sensitiveLayersField;
    private TextField<BodySettingsWindowController> motorPowerTextField;
    private SimpleSecondary<TitledButton<BodySettingsWindowController>> brakesField;
    private LinkedList<BodyUsageCategory> bodyCategoriesCopy;

    public BodySettingsWindowController() {
    }

    public void setLayerWindowController(LayerWindowController layerWindowController) {
        this.layerWindowController = layerWindowController;
    }

    @Override
    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    private List<Object> propertiesCopyList;
    private BodyProperties bodyPropertiesCopy;
    void onModelUpdated(ProperModel<BodyProperties> model) {
        super.onModelUpdated(model);
        setBodyName(model.getModelName());
        this.bodyModel = (BodyModel) model;
        bodyCategoriesCopy = bodyModel.getUsageModels().stream().map(UsageModel::getType).collect(Collectors.toCollection(LinkedList::new));
        bodyPropertiesCopy = (BodyProperties) model.getProperties().clone();
        propertiesCopyList = new LinkedList<>();
        for(UsageModel<?> usageModel:this.bodyModel.getUsageModels()){
            propertiesCopyList.add(usageModel.getProperties().clone());
        }

        createCategorySection();
        updateUsageSettings();
        for (BodyUsageCategory usageCategory :
                this.bodyModel.getUsageModels().stream()
                        .map(UsageModel::getType)
                        .collect(Collectors.toList())) {
            updateUsageCategoryFields(usageCategory);
        }

        setZIndex(bodyModel.getProperties().getZIndex());
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
            case BOW:
                UsageModel<BowProperties> bowUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                BowProperties bowProps = bowUsageModel.getProperties();
                setReloadTime(bowProps.getReloadTime());
                break;
            case TIME_BOMB:
                UsageModel<TimeBombUsageProperties> timeBombUsagePropertiesUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                TimeBombUsageProperties timeBombUsageProperties =
                        timeBombUsagePropertiesUsageModel.getProperties();
                setBombDelay(timeBombUsageProperties.getDelay());
                break;
            case IMPACT_BOMB:
                UsageModel<ImpactBombUsageProperties> impactBombUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                ImpactBombUsageProperties impactBombUsageProperties = impactBombUsageModel.getProperties();
                setBombDelay(impactBombUsageProperties.getDelay());
                setBombMinImpact(impactBombUsageProperties.getMinImpact());
                break;
            case SLASHER:
                break;
            case MOTOR_CONTROL:
                UsageModel<MotorControlProperties> motorControlPropertiesUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                MotorControlProperties motorControlProperties = motorControlPropertiesUsageModel.getProperties();
                setMotorPower(motorControlProperties.getPower());
                setMotorForwardSpeed(motorControlProperties.getForwardSpeed());
                setMotorBackwardSpeed(motorControlProperties.getBackwardSpeed());
                setMotorBrakes(motorControlProperties.isBrakes());
                break;
            case BLUNT:
                break;
            case STABBER:
                break;
            case THROWING:
                break;
            case FLAME_THROWER:
                UsageModel<FlameThrowerProperties> flameThrowerPropertiesUsageModel =
                        this.bodyModel.getUsageModel(usageCategory);
                FlameThrowerProperties flameThrowerProperties = flameThrowerPropertiesUsageModel.getProperties();
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

    private void setMotorBrakes(boolean brakes) {
        brakesField.getMain().getAttachment().updateState(brakes ? Button.State.PRESSED : Button.State.NORMAL);
    }

    private void createMotorBrakesField(int primaryKey, int secondaryKey, MotorControlProperties motorControlProperties) {
        TitledButton<BodySettingsWindowController> motorBrakesButton =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(R.string.brakes_title),
                        ResourceManager.getInstance().onOffTextureRegion,
                        Button.ButtonType.Selector,
                        5f, true, 1);


        brakesField = new SimpleSecondary<>(primaryKey, secondaryKey, motorBrakesButton);
        window.addSecondary(brakesField);
        if (motorControlProperties.isBrakes()) {
            motorBrakesButton.getAttachment().updateState(Button.State.PRESSED);
        }
        motorBrakesButton.getAttachment().setBehavior(new ButtonBehavior<BodySettingsWindowController>(this,motorBrakesButton.getAttachment()) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
            }
        });
        motorBrakesButton.getAttachment().getBehavior().setPushAction(() -> motorControlProperties.setBrakes(true));
        motorBrakesButton.getAttachment().getBehavior().setReleaseAction(() -> motorControlProperties.setBrakes(false));
    }

    private void createNumberOfRoundsTextField(String title,
                                               int primaryKey, int secondaryKey, RangedProperties rangedProperties) {
        TitledTextField<BodySettingsWindowController> roundsField =
                new TitledTextField<>(title, 5, 5);
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

    private void setFireRate(float fireRate) {
        fireRateQuantityField.updateRatio(fireRate);
    }

    private void setRocketPower(float power) {
        rocketPowerQuantityField.updateRatio(power);
    }

    private void setMissileControl(float control) {
        missileControlQuantityField.updateRatio(control);
    }

    private void setMotorForwardSpeed(float motorForwardSpeed) {
        forwardSpeedQuantityField.updateRatio(motorForwardSpeed);
    }

    private void setMotorBackwardSpeed(float motorBackwardSpeed) {
        backwardSpeedQuantityField.updateRatio(motorBackwardSpeed);
    }

    private void setMotorPower(float power) {
        motorPowerTextField
                .getBehavior()
                .setTextValidated(String.valueOf(Float.valueOf(power)));
    }
    private void setZIndex(int zIndex) {
        zIndexTextField
                .getBehavior()
                .setTextValidated(String.valueOf(Integer.valueOf(zIndex)));
    }
    private void setReloadTime(float reloadTime) {
        rangedManualReloadTimeTextField
                .getBehavior()
                .setTextValidated(String.valueOf(Float.valueOf(reloadTime).intValue()));
    }

    private void setFuel(float fuel) {
        rocketFuelTextField.getBehavior().setTextValidated(String.valueOf(fuel));
    }

    private void setBombDelay(float delay) {
        bombDelayTextField.getBehavior().setTextValidated(String.valueOf(delay));
    }

    private void setBombMinImpact(float delay) {
        bombMinImpactTextField.getBehavior().setTextValidated(String.valueOf(delay));
    }

    private void setNumberOfRounds(int rounds) {
        roundsTextField.getBehavior().setTextValidated(String.valueOf(rounds));
    }

    private void setBodyName(String bodyName) {
        bodyNameTextField.getBehavior().setTextValidated(bodyName);
    }

    @Override
    public void init() {
        window.setVisible(false);
        SectionField<BodySettingsWindowController> sectionField =
                new SectionField<>(
                        1,  ResourceManager.getInstance().getString(R.string.general_settings_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<BodySettingsWindowController> bodyNameField =
                new TitledTextField<>( ResourceManager.getInstance().getString(R.string.body_name_title), 10,0);
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

        createZIndexField(1,1);
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
                        2,  ResourceManager.getInstance().getString(R.string.usage_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
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
                if (bodyCategories.contains(bodyUsageCategory)&&bodyUsageCategory.hasOptions) {
                    int primaryId = BodyModel.allCategories.indexOf(bodyUsageCategory) + 3;
                    SectionField<BodySettingsWindowController> bodyUsageField =
                            new SectionField<>(
                                    primaryId,
                                   ResourceManager.getInstance().getString(bodyUsageCategory.nameStringId),
                                    ResourceManager.getInstance().mainButtonTextureRegion,
                                    this);
                    window.addPrimary(bodyUsageField);
                    switch (bodyUsageCategory) {
                        case SHOOTER:
                            ShooterProperties shooterProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.SHOOTER);

                            createReloadTimeField(primaryId, 1, shooterProperties);
                            createNumberOfRoundsTextField( ResourceManager.getInstance().getString(R.string.rounds_title), primaryId, 2, shooterProperties);
                            createProjectilesField(primaryId, 3, shooterProperties);
                            break;
                        case SHOOTER_CONTINUOUS:
                            ContinuousShooterProperties automaticProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.SHOOTER_CONTINUOUS);
                            createReloadTimeField(primaryId, 0, automaticProperties);
                            createFireRateField(primaryId, 1, automaticProperties);
                            createNumberOfRoundsTextField( ResourceManager.getInstance().getString(R.string.rounds_title), primaryId, 2, automaticProperties);
                            createProjectilesField(primaryId, 3, automaticProperties);
                            break;
                        case BOW:
                            BowProperties bowProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.BOW);
                            createReloadTimeField(primaryId, 1, bowProperties);
                            createProjectilesField(primaryId, 2, bowProperties);
                            break;
                        case TIME_BOMB:
                            TimeBombUsageProperties timeBombProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.TIME_BOMB);
                            createTimeBombDelayField(primaryId, 1, timeBombProperties);
                            createBombsField(primaryId, 2, timeBombProperties);
                            createSafetyJointField(primaryId, 3, timeBombProperties);
                            break;
                        case IMPACT_BOMB:
                            ImpactBombUsageProperties impactBombProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.IMPACT_BOMB);
                            createTimeBombDelayField(primaryId, 1, impactBombProperties);
                            createBombMinImpactField(primaryId, 2, impactBombProperties);
                            createBombsField(primaryId, 3, impactBombProperties);
                            createSafetyJointField(primaryId, 4, impactBombProperties);
                            createSensitiveLayersField(primaryId, 5, impactBombProperties);
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
                                    rocketProperties);
                            createRocketFuelField(primaryId, 2, rocketProperties);
                            break;
                        case MISSILE:
                            MissileProperties missileProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.MISSILE);
                            createRocketFireSourceField(primaryId, 0, missileProperties);
                            createRocketPowerField(
                                    primaryId,
                                    1, missileProperties);
                            createMissileControlField(
                                    primaryId,
                                    2,
                                    missileProperties);
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
                        case HEAVY:
                            break;
                        case MOTOR_CONTROL:
                            MotorControlProperties motorControlProperties =
                                    bodyModel.getUsageModelProperties(BodyUsageCategory.MOTOR_CONTROL);
                            createMotorPowerField(primaryId, 0, motorControlProperties);
                            createMotorForwardSpeedField(primaryId, 1, motorControlProperties);
                            createMotorBackwardSpeedField(primaryId, 2, motorControlProperties);
                            createMotorBrakesField(primaryId, 3, motorControlProperties);
                            createMotorJointsField(primaryId, 4, motorControlProperties);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + bodyUsageCategory);
                    }
                }
            }
        }
        updateLayout();
    }

    private void createFireRateField(int primaryId, int secondaryId, ContinuousShooterProperties shooterProperties) {
        TitledQuantity<BodySettingsWindowController> titledFireRateQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.fire_rate_title), 16, "r", 5, true);
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
        fireRateQuantityField
                .getBehavior()
                .setChangeAction(
                        () -> shooterProperties.setFireRate(
                                fireRateQuantityField.getRatio()));
    }


    private void createMotorForwardSpeedField(int primaryId, int secondaryId, MotorControlProperties motorControlProperties) {
        TitledQuantity<BodySettingsWindowController> titledForwardSpeedField =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.speed_forward), 16, "r", 5, true);
        forwardSpeedQuantityField = titledForwardSpeedField.getAttachment();
        titledForwardSpeedField
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, forwardSpeedQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError forwardSpeedFieldWithError = new FieldWithError(titledForwardSpeedField);
        SimpleSecondary<FieldWithError> forwardSpeedElement =
                new SimpleSecondary<>(primaryId, secondaryId, forwardSpeedFieldWithError);
        window.addSecondary(forwardSpeedElement);
        forwardSpeedQuantityField
                .getBehavior()
                .setChangeAction(
                        () -> motorControlProperties.setForwardSpeed(
                                forwardSpeedQuantityField.getRatio()));
    }

    private void createMotorBackwardSpeedField(int primaryId, int secondaryId, MotorControlProperties motorControlProperties) {
        TitledQuantity<BodySettingsWindowController> titledBackwardSpeedField =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.speed_backward), 16, "b", 5, true);
        backwardSpeedQuantityField = titledBackwardSpeedField.getAttachment();
        titledBackwardSpeedField
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<BodySettingsWindowController>(this, backwardSpeedQuantityField) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });

        FieldWithError backwardSpeedFieldWithError = new FieldWithError(titledBackwardSpeedField);
        SimpleSecondary<FieldWithError> backwardSpeedElement =
                new SimpleSecondary<>(primaryId, secondaryId, backwardSpeedFieldWithError);
        window.addSecondary(backwardSpeedElement);
        backwardSpeedQuantityField
                .getBehavior()
                .setChangeAction(
                        () -> motorControlProperties.setBackwardSpeed(
                                backwardSpeedQuantityField.getRatio()));
    }

    private void createRocketPowerField(int primaryId, int secondaryId, RocketProperties rocketProperties) {
        TitledQuantity<BodySettingsWindowController> titledMotorPowerQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.power_title), 16, "r", 5, true);
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
        rocketPowerQuantityField
                .getBehavior()
                .setChangeAction(
                        () ->
                                rocketProperties.setPower(rocketPowerQuantityField.getRatio()));
    }

    private void createMissileControlField(int primaryId, int secondaryId, MissileProperties missileProperties) {
        TitledQuantity<BodySettingsWindowController> titledMissileControlQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.control_title), 16, "r", 5, true);
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
        missileControlQuantityField
                .getBehavior()
                .setChangeAction(
                        () ->
                                missileProperties.setControl(rocketPowerQuantityField.getRatio()));
    }

    private void createRocketFuelField(
            int primaryId, int secondaryId, RocketProperties rocketProperties) {
        TitledTextField<BodySettingsWindowController> rocketFuelField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.fuel_title), 6, 5);
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
                            float fuel = Float.parseFloat(rocketFuelTextField.getTextString());
                            assert rocketProperties != null;
                            rocketProperties.setFuel(fuel);
                        });
    }

    private void createZIndexField(
            int primaryId, int secondaryId) {
        TitledTextField<BodySettingsWindowController> zIndexField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.z_index_title), 6, 5);
        zIndexTextField = zIndexField.getAttachment();

        zIndexField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                zIndexTextField,
                                Keyboard.KeyboardType.Numeric,
                                zIndexValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(zIndexTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(
                                        zIndexTextField);
                            }
                        });

        FieldWithError zIndexFieldWithError = new FieldWithError(zIndexField);
        SimpleSecondary<FieldWithError> zIndexElement =
                new SimpleSecondary<>(primaryId, secondaryId, zIndexFieldWithError);
        window.addSecondary(zIndexElement);

        zIndexTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            int zIndex = Integer.parseInt(zIndexTextField.getTextString());
                            if(bodyModel!=null) {
                                bodyModel.getProperties().setZIndex(zIndex);
                                editorUserInterface.getToolModel().updateMesh();
                            }
                        });
    }


    private void createReloadTimeField(
            int primaryId, int secondaryId, RangedProperties rangedProperties) {
        TitledTextField<BodySettingsWindowController> reloadTimeField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.reload_time_title), 6, 5);
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


    private void createMotorPowerField(
            int primaryId, int secondaryId, MotorControlProperties motorControlProperties) {
        TitledTextField<BodySettingsWindowController> motorPowerField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.power_hp_title), 6, 5);
        motorPowerTextField = motorPowerField.getAttachment();

        motorPowerField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<BodySettingsWindowController>(
                                this,
                                motorPowerTextField,
                                Keyboard.KeyboardType.Numeric,
                                motorPowerValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                BodySettingsWindowController.super.onTextFieldTapped(
                                        motorPowerTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                BodySettingsWindowController.super.onTextFieldReleased(
                                        motorPowerTextField);
                            }
                        });

        FieldWithError motorPowerFieldWithError = new FieldWithError(motorPowerField);
        SimpleSecondary<FieldWithError> fireRateElement =
                new SimpleSecondary<>(primaryId, secondaryId, motorPowerFieldWithError);
        window.addSecondary(fireRateElement);

        motorPowerTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float motorPower = Float.parseFloat(motorPowerTextField.getTextString());
                            assert motorControlProperties != null;
                            motorControlProperties.setPower(motorPower);
                        });
    }

    private void createTimeBombDelayField(
            int primaryId, int secondaryId, BombUsageProperties bombUsageProperties) {
        TitledTextField<BodySettingsWindowController> bombDelayField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.delay_title), 6, 5);
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
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.min_impact_title), 6, 5);
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

        bombMinImpactTextField
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
        SecondarySectionField<BodySettingsWindowController> projectileField = new SecondarySectionField<>(
                primaryId,
                secondaryId,
                ResourceManager.getInstance().getString(R.string.projectiles_title),
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

    private void createMotorJointsField(
            int primaryId, int secondaryId, MotorControlProperties motorControlProperties) {
        SecondarySectionField<BodySettingsWindowController> motorJointField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        ResourceManager.getInstance().getString(R.string.joint_title),
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(motorJointField);
        List<JointModel> projectiles =
                editorUserInterface.getToolModel().getJoints().stream()
                        .filter(jointModel -> jointModel.getProperties().getJointType() == JointDef.JointType.RevoluteJoint || jointModel.getProperties().getJointType() == JointDef.JointType.PrismaticJoint)
                        .collect(Collectors.toList());
        for (JointModel jointModel : projectiles) {
            createMotorJointButton(primaryId, secondaryId, jointModel, motorControlProperties);
        }
    }

    private void createSafetyJointField(
            int primaryId, int secondaryId, BombUsageProperties bombUsageProperties) {
        SecondarySectionField<BodySettingsWindowController> safetyField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        ResourceManager.getInstance().getString(R.string.safety_title),
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(safetyField);
        List<JointModel> joints =
                editorUserInterface.getToolModel().getJoints().stream().filter(j -> j.getBodyModel1() == this.bodyModel || j.getBodyModel2() == this.bodyModel).collect(Collectors.toList());
        for (JointModel jointModel : joints) {
            createJointButton(primaryId, secondaryId, jointModel, bombUsageProperties);
        }
    }

    private void createFlameThrowerFireSourceField(
            int primaryId, int secondaryId, FlameThrowerProperties flameThrowerProperties) {
        SecondarySectionField<BodySettingsWindowController> flameThrowerFireSourcesField = new SecondarySectionField<>(
                primaryId,
                secondaryId,
                ResourceManager.getInstance().getString(R.string.fire_sources_title),
                ResourceManager.getInstance().mainButtonTextureRegion,
                this);
        window.addSecondary(flameThrowerFireSourcesField);
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
                        if(!flameThrowerProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
                            flameThrowerProperties.getFireSourceIds().add(fireSourceModel.getFireSourceId());
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if (flameThrowerProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
                            flameThrowerProperties.getFireSourceIds().removeIf(e->e==fireSourceModel.getFireSourceId());
                        }
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
                        ResourceManager.getInstance().getString(R.string.liquid_sources_title),
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(liquidSourcesField);
        List<LiquidSourceModel> liquidSourceModelList =
                editorUserInterface.getToolModel().getBodies().stream().filter(b->b.getBodyId()==bodyModel.getBodyId())
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
                        if(!liquidContainerProperties.getLiquidSourceIds().contains(liquidSourceModel.getLiquidSourceId())) {
                            liquidContainerProperties.getLiquidSourceIds().add(liquidSourceModel.getLiquidSourceId());
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if(liquidContainerProperties.getLiquidSourceIds().contains(liquidSourceModel.getLiquidSourceId())) {
                            liquidContainerProperties.getLiquidSourceIds().removeIf(e->e==liquidSourceModel.getLiquidSourceId());
                        }
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
                        if(!rangedProperties.getProjectileIds().contains(projectileModel.getProjectileId())) {
                            rangedProperties.getProjectileIds().add(projectileModel.getProjectileId());
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if(rangedProperties.getProjectileIds().contains(projectileModel.getProjectileId())) {
                            rangedProperties.getProjectileIds().removeIf(e->e == projectileModel.getProjectileId());
                        }
                    }
                });
        if (rangedProperties.getProjectileIds().contains(projectileModel.getProjectileId())) {
            projectileButton.updateState(Button.State.PRESSED);
        }
    }


    private void createMotorJointButton(
            int primaryKey,
            int secondaryKey,
            JointModel jointModel,
            MotorControlProperties motorControlProperties) {
        ButtonWithText<BodySettingsWindowController> jointButton =
                new ButtonWithText<>(
                        jointModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> jointField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, jointModel.getJointId(), jointButton);
        window.addTertiary(jointField);
        jointButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, jointButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        motorControlProperties.getJointIds().add(jointModel.getJointId());
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        motorControlProperties.getJointIds().removeIf(j->j==jointModel.getJointId());
                    }
                });
        if (motorControlProperties.getJointIds().contains(jointModel.getJointId())) {
            jointButton.updateState(Button.State.PRESSED);
        }
    }

    private void createJointButton(
            int primaryKey,
            int secondaryKey,
            JointModel jointModel,
            BombUsageProperties bombUsageProperties) {
        ButtonWithText<BodySettingsWindowController> projectileButton =
                new ButtonWithText<>(
                        jointModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> jointField =
                new SimpleTertiary<>(
                        primaryKey, secondaryKey, jointModel.getJointId(), projectileButton);
        window.addTertiary(jointField);
        projectileButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, projectileButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        bombUsageProperties.setSafetyJoint(jointModel.getJointId());
                        int size = window.getLayout().getTertiariesSize(primaryKey, secondaryKey);
                        for (int i = 0; i < size; i++) {

                            SimpleTertiary<?> other = window.getLayout().getTertiaryByIndex(primaryKey, secondaryKey, i);
                            if (other != jointField) {
                                ((ButtonWithText<?>) other.getMain()).updateState(Button.State.NORMAL);
                            }
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        bombUsageProperties.setSafetyJoint(-1);
                    }
                });
        if (bombUsageProperties.getSafetyJoint() == jointModel.getJointId()) {
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
        for (BodyUsageCategory usageCategory :
                this.bodyModel.getUsageModels().stream()
                        .map(UsageModel::getType)
                        .collect(Collectors.toList())) {
            updateUsageCategoryFields(usageCategory);
        }
        onLayoutChanged();
    }

    private void onCategoryButtonReleased(SimpleSecondary<?> categoryField, BodyUsageCategory e) {
        super.onSecondaryButtonClicked(categoryField);
        onUsageRemoved(e);
        updateUsageSettings();
    }

    public void onUsageAdded(BodyUsageCategory e) {
        if (e == BodyUsageCategory.SHOOTER_CONTINUOUS || e == BodyUsageCategory.SHOOTER || e == BodyUsageCategory.BOW) {
            UsageModel<RangedProperties> usage = new UsageModel<>("", e);
            RangedProperties properties = usage.getProperties();
            properties.setProjectileIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.TIME_BOMB) {
            UsageModel<BombUsageProperties> usage = new UsageModel<>("", e);
            BombUsageProperties properties = usage.getProperties();
            properties.setBombIds(new ArrayList<>());
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.IMPACT_BOMB) {
            UsageModel<ImpactBombUsageProperties> usage = new UsageModel<>("", e);
            ImpactBombUsageProperties properties = usage.getProperties();
            properties.setBombIds(new ArrayList<>());
            properties.setSensitiveLayers(new ArrayList<>());
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
            properties.setFireSourceIds(new ArrayList<>());
        }
        if (e == BodyUsageCategory.MISSILE) {
            UsageModel<MissileProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
            MissileProperties properties = usage.getProperties();
            properties.setFireSourceIds(new ArrayList<>());
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
        if (e == BodyUsageCategory.HEAVY) {
            UsageModel<HeavyProperties> usage = new UsageModel<>("", e);
            bodyModel.getUsageModels().add(usage);
        }
        if (e == BodyUsageCategory.MOTOR_CONTROL) {
            UsageModel<MotorControlProperties> usage = new UsageModel<>("", e);
            MotorControlProperties motorControlProperties = usage.getProperties();
            motorControlProperties.setJointIds(new ArrayList<>());
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
        int i = 0;
        bodyModel.getUsageModels().removeIf(e->!bodyCategoriesCopy.contains(e.getType()));
        bodyModel.setProperties(bodyPropertiesCopy);
        for(Object properties: propertiesCopyList){
            bodyModel.getUsageModels().get(i++).setProperties(properties);
        }
    }

    private void createCategoryCheckBox(BodyUsageCategory bodyUsageCategory, boolean pressed) {
        TitledButton<BodySettingsWindowController> categoryCheckbox =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(bodyUsageCategory.nameStringId),
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
        SecondarySectionField<BodySettingsWindowController> bombsField = new SecondarySectionField<>(
                primaryId,
                secondaryId,
                ResourceManager.getInstance().getString(R.string.bombs_title),
                ResourceManager.getInstance().mainButtonTextureRegion,
                this);
        window.addSecondary(bombsField);
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
                        if(!bombUsageProperties.getBombIds().contains(bombModel.getBombId())) {
                            bombUsageProperties.getBombIds().add(bombModel.getBombId());
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if(bombUsageProperties.getBombIds().contains(bombModel.getBombId())) {
                            bombUsageProperties.getBombIds().removeIf(e->e == bombModel.getBombId());
                        }
                    }
                });
        if (bombUsageProperties.getBombIds().stream()
                .anyMatch(b -> bombModel.getBombId() == b)) {
            bombButton.updateState(Button.State.PRESSED);
        }
    }

    private void createRocketFireSourceField(
            int primaryId, int secondaryId, RocketProperties rocketProperties) {
        SecondarySectionField<BodySettingsWindowController> fireSourcesField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        ResourceManager.getInstance().getString(R.string.fire_sources_title),
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(fireSourcesField);
        List<FireSourceModel> fireSources =
                editorUserInterface.getToolModel().getBodies().stream().filter(b->b.getBodyId()==bodyModel.getBodyId())
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
                        if(!rocketProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
                            rocketProperties.getFireSourceIds().add(fireSourceModel.getFireSourceId());
                        }
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if(rocketProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
                            rocketProperties.getFireSourceIds().removeIf(e->e==fireSourceModel.getFireSourceId());
                        }
                    }
                });
        if (rocketProperties.getFireSourceIds().contains(fireSourceModel.getFireSourceId())) {
            fireSourceButton.updateState(Button.State.PRESSED);
        }
    }


    private void createSensitiveLayersField(
            int primaryId, int secondaryId, ImpactBombUsageProperties bombUsageProperties) {
        sensitiveLayersField =
                new SecondarySectionField<>(
                        primaryId,
                        secondaryId,
                        ResourceManager.getInstance().getString(R.string.sensitive_layers_title),
                        ResourceManager.getInstance().mainButtonTextureRegion,
                        this);
        window.addSecondary(sensitiveLayersField);
        List<LayerModel> layers = new ArrayList<>(bodyModel.getLayers());
        LayerModel allPlaceholder = new LayerModel(bodyModel.getBodyId(), -1, bodyModel);
        allPlaceholder.setModelName(ResourceManager.getInstance().getString(R.string.all_placeholder));
        layers.add(0, allPlaceholder);

        for (LayerModel layerModel : layers) {
            createSensitiveLayerButton(primaryId, secondaryId, layerModel, bombUsageProperties);
        }
    }


    @Override
    public void onSecondaryButtonReleased(SimpleSecondary<?> simpleSecondary) {
        super.onSecondaryButtonReleased(simpleSecondary);
        if (simpleSecondary == sensitiveLayersField) {
            outlineController.onSelectionUpdated(layerWindowController.getSelectedBodyModel(), layerWindowController.getSelectedLayerModel(), layerWindowController.getSelectedDecorationModel());
        }
    }

    private void createSensitiveLayerButton(
            int primaryKey,
            int secondaryKey,
            LayerModel layerModel,
            ImpactBombUsageProperties impactBombUsageProperties) {
        ButtonWithText<BodySettingsWindowController> layerButton =
                new ButtonWithText<>(
                        layerModel.getModelName(),
                        2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        SimpleTertiary<ButtonWithText<?>> layerField =
                new SimpleTertiary<>(primaryKey, secondaryKey, layerModel.getLayerId(), layerButton);
        window.addTertiary(layerField);
        layerButton.setBehavior(
                new ButtonBehavior<BodySettingsWindowController>(this, layerButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        if (layerModel.getLayerId() != -1) {
                            impactBombUsageProperties.getSensitiveLayers().add(layerModel.getLayerId());
                        }

                        if (layerModel.getLayerId() == -1) {
                            impactBombUsageProperties.setSensitiveLayers(bodyModel.getLayers().stream().map(LayerModel::getLayerId).collect(Collectors.toList()));
                            for (int i = 0; i < window.getLayout().getTertiariesSize(primaryKey, secondaryKey); i++) {
                                SimpleTertiary<?> other = window.getLayout().getTertiaryByIndex(primaryKey, secondaryKey, i);
                                ((ButtonWithText<?>) other.getMain()).updateState(Button.State.PRESSED);
                            }
                        }
                        outlineController.onSensitiveLayersChanged(bodyModel, impactBombUsageProperties.getSensitiveLayers());
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if (layerModel.getLayerId() == -1) {
                            impactBombUsageProperties.getSensitiveLayers().clear();
                            for (int i = 0; i < window.getLayout().getTertiariesSize(primaryKey, secondaryKey); i++) {
                                SimpleTertiary<?> other = window.getLayout().getTertiaryByIndex(primaryKey, secondaryKey, i);
                                if (other.getSecondaryKey() != -1) {
                                    ((ButtonWithText<?>) other.getMain()).updateState(Button.State.NORMAL);
                                }
                            }
                        } else if (impactBombUsageProperties.getSensitiveLayers().contains(layerModel.getLayerId())) {
                            impactBombUsageProperties.getSensitiveLayers().removeIf(e -> e == layerModel.getLayerId());
                        }
                        outlineController.onSensitiveLayersChanged(bodyModel, impactBombUsageProperties.getSensitiveLayers());
                    }
                });
        if (impactBombUsageProperties.getSensitiveLayers().contains(layerModel.getLayerId())) {
            layerButton.updateState(Button.State.PRESSED);
        }
    }

    public void setOutlineController(OutlineController outlineController) {
        this.outlineController = outlineController;
    }

}
