package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import android.util.Log;

import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.entities.properties.Material;
import com.evolgames.entities.properties.PropertiesWithColor;
import com.evolgames.factories.MaterialFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.model.LayerPointsModel;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.windows.windowfields.ColorSlot;
import com.evolgames.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.Sub2SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledField;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import org.andengine.util.adt.color.Color;

public class LayerSettingsWindowController extends SettingsWindowController {


    private TextField<LayerSettingsWindowController> flameTemperatureTextField;
    private ColorSlot colorSlotForLayer;
    private ColorSlot colorSlotForJuice;
    private ColorSelectorWindowController colorSelectorController;
    private NumericValidator densityValidator = new NumericValidator(false, true, 0.01f, 999f, 3, 2);
    private NumericValidator ignitionTemperatureValidator = new NumericValidator(false, false, 0, 10000, 4, 1);
    private NumericValidator flameTemperatureValidator = new NumericValidator(false, false, 0, 10000, 4, 1);
    private NumericValidator energyValidator = new NumericValidator(false, false, 0, 100000, 5, 1);
    private AlphaNumericValidator layerNameValidator = new AlphaNumericValidator(8,5);
    private BlockAProperties layerProperty;
    private TextField<LayerSettingsWindowController> layerNameTextField;
    private TextField<LayerSettingsWindowController> densityTextField;
    private Quantity<LayerSettingsWindowController> bouncinessQuantity;
    private Quantity<LayerSettingsWindowController> frictionQuantity;
    private Quantity<LayerSettingsWindowController> hardnessQuantity;
    private LayerWindowController layerWindowController;
    private SimpleSecondary<TitledButton<LayerSettingsWindowController>> jucyField;
    private TextField<LayerSettingsWindowController> ignitionTextField;
    private TextField<LayerSettingsWindowController> energyTextField;
    private SimpleSecondary<TitledButton<LayerSettingsWindowController>> flammableField;

    public LayerSettingsWindowController(LayerWindowController layerWindowController, UserInterface userInterface, KeyboardController keyboardController) {
        super(userInterface);
        this.keyboardController = keyboardController;
        this.layerWindowController = layerWindowController;
    }

    public void setColorSelectorController(ColorSelectorWindowController colorSelectorController) {
        this.colorSelectorController = colorSelectorController;
    }


    private void setLayerColorSlot() {
        Color color = ((PropertiesWithColor) model.getProperty()).getDefaultColor();
        colorSlotForLayer.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    private void setJuiceColorSlot() {
        Color color = ((BlockAProperties) model.getProperty()).getJuiceColor();
        if (color != null)
            colorSlotForJuice.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }


    @Override
    void onModelUpdated(ProperModel model) {
        super.onModelUpdated(model);

        this.layerProperty = (BlockAProperties) model.getProperty();
        setLayerColorSlot();
        setJuiceColorSlot();
        setLayerName(model.getModelName());
        setDensity(layerProperty.getDensity());
        setIgnitionTemperature(layerProperty.getIgnitionTemperature());
        setFlameTemperature(layerProperty.getFlameTemperature());
        setChemicalEnergy(layerProperty.getChemicalEnergy());
        setBounciness(layerProperty.getRestitution());
        setFriction(layerProperty.getFriction());
        setTenacity(layerProperty.getTenacity());
        setJuicy(layerProperty.isJuicy());
        setFlammable(layerProperty.isFlammable());

    }

    @Override
    public void init() {
        window.setVisible(false);
        SectionField<LayerSettingsWindowController> sectionField = new SectionField<>(1, "General Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<LayerSettingsWindowController> layerNameField = new TitledTextField<>("Layer Name:", 10);
        layerNameTextField = layerNameField.getAttachment();

        layerNameTextField.setBehavior(new TextFieldBehavior<LayerSettingsWindowController>(this, layerNameTextField, Keyboard.KeyboardType.AlphaNumeric, layerNameValidator) {
            @Override
            protected void informControllerTextFieldTapped() {
                LayerSettingsWindowController.super.onTextFieldTapped(layerNameTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                LayerSettingsWindowController.super.onTextFieldReleased(layerNameTextField);
            }
        });
        layerNameTextField.getBehavior().setReleaseAction(() -> model.setModelName(layerNameTextField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(layerNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);
//create the color selection field

        colorSlotForLayer = new ColorSlot();
        Button<LayerSettingsWindowController> colorSelectionButton = new Button<>(ResourceManager.getInstance().smallButtonTextureRegion, Button.ButtonType.OneClick, true);
        LinearLayout linearLayout = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
        linearLayout.addToLayout(colorSelectionButton);
        linearLayout.addToLayout(colorSlotForLayer);
        TitledField<LinearLayout> colorSelectionField = new TitledField<>("Select Color:", linearLayout);
        colorSelectionButton.setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, colorSelectionButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                if (colorSelectorController != null) {
                    colorSelectorController.bindToColor(((PropertiesWithColor) model.getProperty()).getDefaultColor());
                    colorSelectorController.setAcceptAction(() -> setLayerColorSlot());
                    colorSelectorController.openWindow();
                }
            }
        });
        SimpleSecondary<TitledField<?>> secondaryElement2 = new SimpleSecondary<>(1, 1, colorSelectionField);
        window.addSecondary(secondaryElement2);


        SectionField<LayerSettingsWindowController> materialsSection = new SectionField<>(2, "Material", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(materialsSection);

        for (int i = 0; i < MaterialFactory.getInstance().materials.size(); i++) {

            ButtonWithText<LayerSettingsWindowController> materialButton = new ButtonWithText<>
                    (this.getMaterialNameByIndex(i), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);


            SimpleSecondary<ButtonWithText<?>> materialField = new SimpleSecondary<>(2, i, materialButton);
            window.addSecondary(materialField);
            materialButton.setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, materialButton) {
                @Override
                public void informControllerButtonClicked() {
                    onMaterialButtonClicked(materialField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });

        }


        SectionField<LayerSettingsWindowController> propertiesSection = new SectionField<>(3, "Properties", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(propertiesSection);
        TitledTextField<LayerSettingsWindowController> densityField = new TitledTextField<>("Density:", 5, 5, 76);
        densityTextField = densityField.getAttachment();

        densityField.getAttachment().setBehavior(new TextFieldBehavior<LayerSettingsWindowController>(this, densityField.getAttachment(), Keyboard.KeyboardType.Numeric, densityValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                LayerSettingsWindowController.super.onTextFieldTapped(densityTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                LayerSettingsWindowController.super.onTextFieldReleased(densityTextField);
            }


        });

        FieldWithError titleDensityFieldWithError = new FieldWithError(densityField);
        SimpleSecondary<FieldWithError> densityElement = new SimpleSecondary<>(3, 0, titleDensityFieldWithError);
        window.addSecondary(densityElement);
        densityTextField.getBehavior().setReleaseAction(() -> {
            float density = Float.parseFloat(densityTextField.getTextString());
            layerProperty.setDensity(density);
        });


        TitledQuantity<LayerSettingsWindowController> titledBouncinessQuantity = new TitledQuantity<>("Bounciness:", 10, 1, 5, 76);
        bouncinessQuantity = titledBouncinessQuantity.getAttachment();
        titledBouncinessQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, titledBouncinessQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<LayerSettingsWindowController>> bouncinessElement = new SimpleSecondary<>(3, 1, titledBouncinessQuantity);
        window.addSecondary(bouncinessElement);


        TitledQuantity<LayerSettingsWindowController> titledFrictionQuantity = new TitledQuantity<>("Friction:", 10, 4, 5, 76);
        frictionQuantity = titledFrictionQuantity.getAttachment();
        titledFrictionQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, titledFrictionQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<?>> frictionElement = new SimpleSecondary<>(3, 2, titledFrictionQuantity);
        window.addSecondary(frictionElement);


        TitledQuantity<LayerSettingsWindowController> titledTenacityQuantity = new TitledQuantity<>("Tenacity:", 10, 5, 5, 76);
        hardnessQuantity = titledTenacityQuantity.getAttachment();
        titledTenacityQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, titledTenacityQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleSecondary<TitledQuantity<?>> tenacityElement = new SimpleSecondary<>(3, 3, titledTenacityQuantity);
        window.addSecondary(tenacityElement);


        TitledButton<LayerSettingsWindowController> juicyButton = new TitledButton<>
                ("Juicy", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);


        jucyField = new SimpleSecondary<>(3, 4, juicyButton);
        window.addSecondary(jucyField);
        juicyButton.getAttachment().setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, juicyButton.getAttachment()) {
            @Override
            public void informControllerButtonClicked() {

                onSecondaryButtonClicked(jucyField);
            }

            @Override
            public void informControllerButtonReleased() {
                onSecondaryButtonReleased(jucyField);

            }
        });
        juicyButton.getAttachment().getBehavior().setPushAction(() -> layerProperty.setJuicy(true));
        juicyButton.getAttachment().getBehavior().setReleaseAction(() -> layerProperty.setJuicy(false));

        Sub2SectionField<LayerSettingsWindowController> juiceSettingsSection = new Sub2SectionField<>(3, 4, 0, "Settings", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addTertiary(juiceSettingsSection);


        colorSlotForJuice = new ColorSlot();
        Button<LayerSettingsWindowController> juiceColorSelectionButton = new Button<>(ResourceManager.getInstance().smallButtonTextureRegion, Button.ButtonType.OneClick, true);
        LinearLayout linearLayout2 = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
        linearLayout2.addToLayout(juiceColorSelectionButton);
        linearLayout2.addToLayout(colorSlotForJuice);
        TitledField<LinearLayout> colorSelectionField2 = new TitledField<>("Select Juice Color:", linearLayout2);
        juiceColorSelectionButton.setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, juiceColorSelectionButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {
                if (colorSelectorController != null) {
                    colorSelectorController.bindToColor(((BlockAProperties) model.getProperty()).getJuiceColor());
                    colorSelectorController.setAcceptAction(() -> setJuiceColorSlot());
                    colorSelectorController.openWindow();
                }
            }
        });
        SimpleQuaternary<TitledField<?>> juiceColorElement = new SimpleQuaternary<>(3, 4, 0, 0, colorSelectionField2);
        window.addQuaternary(juiceColorElement);


        Sub2SectionField<LayerSettingsWindowController> juiceTypeSection = new Sub2SectionField<>(3, 4, 1, "Liquid", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addTertiary(juiceTypeSection);

        for (int i = 0; i < MaterialFactory.getInstance().liquids.size(); i++) {

            ButtonWithText<LayerSettingsWindowController> juiceButton = new ButtonWithText<>
                    (this.getLiquidNameByIndex(i), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);


            SimpleQuaternary<ButtonWithText<?>> juiceField = new SimpleQuaternary<>(3, 4, 1, i, juiceButton);
            window.addQuaternary(juiceField);
            juiceButton.setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, juiceButton) {
                @Override
                public void informControllerButtonClicked() {
                    onJuiceButtonClicked(juiceField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });


        }

        Sub2SectionField<LayerSettingsWindowController> juicePropertiesSection = new Sub2SectionField<>(3, 4, 2, "Properties", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addTertiary(juicePropertiesSection);


        TitledQuantity<LayerSettingsWindowController> juicinessQuantity = new TitledQuantity<>("Juiciness:", 10, 5, 5);
        juicinessQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, juicinessQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleQuaternary<TitledQuantity<?>> juicinessElement = new SimpleQuaternary<>(3, 4, 2, 0, juicinessQuantity);
        window.addQuaternary(juicinessElement);
        juicinessQuantity.getAttachment().getBehavior().setChangeAction(() -> layerProperty.setJuicinessDensity(juicinessQuantity.getAttachment().getRatio()));


        TitledQuantity<LayerSettingsWindowController> lowerRateQuantity = new TitledQuantity<>("Lower Rate:", 10, 5, 5, 75);
        lowerRateQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, lowerRateQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        FieldWithError lowerRateFieldWithError = new FieldWithError(lowerRateQuantity);
        SimpleQuaternary<FieldWithError> lowerRateElement = new SimpleQuaternary<>(3, 4, 2, 1, lowerRateFieldWithError);
        window.addQuaternary(lowerRateElement);
        lowerRateQuantity.getAttachment().getBehavior().setChangeAction(() -> layerProperty.setJuicinessLowerPressure(lowerRateQuantity.getAttachment().getRatio()));

        lowerRateQuantity.getAttachment().getBehavior().setCondition(new Condition() {
            @Override
            public boolean isCondition(float value) {
                return value <= layerProperty.getJuicinessUpperPressure();
            }

            @Override
            public String getError() {
                return "lower rate under upper rate !";
            }
        });


        TitledQuantity<LayerSettingsWindowController> upperRateQuantity = new TitledQuantity<>("Upper Rate:", 10, 5, 5, 75);
        upperRateQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, upperRateQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        FieldWithError upperRateFieldWithError = new FieldWithError(upperRateQuantity);
        SimpleQuaternary<FieldWithError> upperRateElement = new SimpleQuaternary<>(3, 4, 2, 2, upperRateFieldWithError);
        window.addQuaternary(upperRateElement);

        upperRateQuantity.getAttachment().getBehavior().setChangeAction(() -> layerProperty.setJuicinessUpperPressure(upperRateQuantity.getAttachment().getRatio()));
        upperRateQuantity.getAttachment().getBehavior().setCondition(new Condition() {
            @Override
            public boolean isCondition(float value) {
                return value >= layerProperty.getJuicinessLowerPressure();
            }

            @Override
            public String getError() {
                return "upper rate above lower rate";
            }
        });


        TitledButton<LayerSettingsWindowController> juiceCombButton = new TitledButton<>
                ("Combustible:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);


        SimpleTertiary<TitledButton<LayerSettingsWindowController>> juiceCombElement = new SimpleTertiary<>(3, 4, 3, juiceCombButton);
        window.addTertiary(juiceCombElement);


        juiceCombButton.getAttachment().setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, juiceCombButton.getAttachment()) {
            @Override
            public void informControllerButtonClicked() {
                onTertiaryButtonClicked(juiceCombElement);
            }

            @Override
            public void informControllerButtonReleased() {
                onTertiaryButtonReleased(juiceCombElement);

            }
        });


        TitledQuantity<LayerSettingsWindowController> juiceCombEnergyQuantity = new TitledQuantity<>("Energy:", 10, 5, 5);
        juiceCombEnergyQuantity.getAttachment().setBehavior(new QuantityBehavior<LayerSettingsWindowController>(this, juiceCombEnergyQuantity.getAttachment()) {
            @Override
            public void informControllerQuantityUpdated(Quantity<?> quantity) {

            }
        });
        SimpleQuaternary<TitledQuantity<LayerSettingsWindowController>> juicinessCombEnergyElement = new SimpleQuaternary<>(3, 4, 3, 0, juiceCombEnergyQuantity);
        window.addQuaternary(juicinessCombEnergyElement);


        TitledButton<LayerSettingsWindowController> flammableButton = new TitledButton<>
                ("Flammable:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);

        flammableField = new SimpleSecondary<>(3, 5, flammableButton);
        window.addSecondary(flammableField);
        flammableButton.getAttachment().setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, flammableButton.getAttachment()) {
            @Override
            public void informControllerButtonClicked() {

                onSecondaryButtonClicked(flammableField);
            }

            @Override
            public void informControllerButtonReleased() {
                onSecondaryButtonReleased(flammableField);

            }
        });
        flammableButton.getAttachment().getBehavior().setPushAction(() -> layerProperty.setFlammable(true));
        flammableButton.getAttachment().getBehavior().setReleaseAction(() -> layerProperty.setFlammable(false));


        TitledTextField<LayerSettingsWindowController> ignitionTemperatureField = new TitledTextField<>("Ignition Temperature:", 5, 5, 140);
        ignitionTextField = ignitionTemperatureField.getAttachment();
        ignitionTemperatureField.getAttachment().setBehavior(new TextFieldBehavior<LayerSettingsWindowController>(this, ignitionTemperatureField.getAttachment(), Keyboard.KeyboardType.Numeric, ignitionTemperatureValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                LayerSettingsWindowController.super.onTextFieldTapped(ignitionTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                LayerSettingsWindowController.super.onTextFieldReleased(ignitionTextField);
            }


        });

        //SimpleTertiary<TitledTextField> ignitionElement = new SimpleTertiary<>(3, 5, 0, ignitionTemperatureField);

        FieldWithError ignitionFieldWithError = new FieldWithError(ignitionTemperatureField);
        SimpleTertiary<FieldWithError> ignitionElement = new SimpleTertiary<>(3, 5, 0, ignitionFieldWithError);
        window.addTertiary(ignitionElement);
        ignitionTextField.getBehavior().setReleaseAction(() -> {
            float ignitionTemperature = Float.parseFloat(ignitionTextField.getTextString());
            layerProperty.setIgnitionTemperature(ignitionTemperature);
        });


        TitledTextField<LayerSettingsWindowController> energyField = new TitledTextField<>("Energy:", 6, 5, 50);
        energyTextField = energyField.getAttachment();

        energyField.getAttachment().setBehavior(new TextFieldBehavior<LayerSettingsWindowController>(this, energyField.getAttachment(), Keyboard.KeyboardType.Numeric, energyValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                LayerSettingsWindowController.super.onTextFieldTapped(energyTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                LayerSettingsWindowController.super.onTextFieldReleased(energyTextField);
            }


        });

        FieldWithError energyFieldWithError = new FieldWithError(energyField);
        SimpleTertiary<FieldWithError> energyElement = new SimpleTertiary<>(3, 5, 1, energyFieldWithError);
        window.addTertiary(energyElement);

        energyTextField.getBehavior().setReleaseAction(() -> {
            float energy = Float.parseFloat(energyTextField.getTextString());
            layerProperty.setChemicalEnergy(energy);
        });


        TitledTextField<LayerSettingsWindowController> flameTemperatureField = new TitledTextField<>("Flame Temperature:", 5, 5, 128);
        flameTemperatureTextField = flameTemperatureField.getAttachment();

        flameTemperatureField.getAttachment().setBehavior(new TextFieldBehavior<LayerSettingsWindowController>(this, flameTemperatureField.getAttachment(), Keyboard.KeyboardType.Numeric, flameTemperatureValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
                LayerSettingsWindowController.super.onTextFieldTapped(flameTemperatureTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
                LayerSettingsWindowController.super.onTextFieldReleased(flameTemperatureTextField);
            }


        });

        FieldWithError flameTemperatureFieldWithError = new FieldWithError(flameTemperatureField);
        SimpleTertiary<FieldWithError> flameTemperatureElement = new SimpleTertiary<>(3, 5, 2, flameTemperatureFieldWithError);
        window.addTertiary(flameTemperatureElement);
        flameTemperatureTextField.getBehavior().setReleaseAction(() -> {
            float flameTemperature = Float.parseFloat(flameTemperatureTextField.getTextString());
            layerProperty.setFlameTemperature(flameTemperature);
        });


        SectionField<LayerSettingsWindowController> categorySection = new SectionField<>(4, "Category", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(categorySection);


        SectionField<LayerSettingsWindowController> collisionSection = new SectionField<>(5, "Collides With", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(collisionSection);


        window.createScroller();
        window.getLayout().updateLayout();

    }

    private String getMaterialNameByIndex(int i) {
        return MaterialFactory.getInstance().getMaterialByIndex(i).getName();
    }

    private String getLiquidNameByIndex(int i) {
        return MaterialFactory.getInstance().getLiquidByIndex(i).getProperties().getJuiceName();
    }

    private void setLayerName(String layerName) {
        layerNameTextField.getBehavior().setTextValidated(layerName);
    }

    private void setBounciness(float bounciness) {
        bouncinessQuantity.updateRatio(bounciness);
    }

    private void setDensity(float density) {
        densityTextField.getBehavior().setTextValidated("" + density);
    }

    private void setIgnitionTemperature(double ignitionTemperature) {
        ignitionTextField.getBehavior().setTextValidated("" + (long) ignitionTemperature);
    }

    private void setFlameTemperature(double flameTemperature) {
        flameTemperatureTextField.getBehavior().setTextValidated("" + (long) flameTemperature);
    }

    private void setChemicalEnergy(double chemicalEnergy) {
        energyTextField.getBehavior().setTextValidated("" + (long) chemicalEnergy);
    }


    private void setFriction(float friction) {
        frictionQuantity.updateRatio(friction);
    }

    private void setTenacity(float tenacity) {
        hardnessQuantity.updateRatio(tenacity / 10f);
    }

    private void setJuicy(boolean juicy) {
        if (juicy) {
            jucyField.getMain().getAttachment().updateState(Button.State.PRESSED);
            onSecondaryButtonClicked(jucyField);
        } else {
            jucyField.getMain().getAttachment().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(jucyField);
        }
    }

    private void setFlammable(boolean flammable) {
        if (flammable) {
            flammableField.getMain().getAttachment().updateState(Button.State.PRESSED);
            onSecondaryButtonClicked(flammableField);
        } else {
            flammableField.getMain().getAttachment().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(flammableField);
        }
    }

    void onJuiceButtonClicked(SimpleQuaternary<?> juiceButton) {
        Log.e("interface", juiceButton.getQuaternaryKey() + "/");

    }

    void onMaterialButtonClicked(SimpleSecondary<?> materialButton) {
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


        Material material = MaterialFactory.getInstance().getMaterialByIndex(secondaryKey);
        layerProperty.setProperties(new float[]{material.getDensity(), material.getRestitution(), material.getFriction(), material.getTenacity(), material.getJuicinessDensity(), material.getJuicinessLowerPressure(), material.getJuicinessUpperPressure()});
        layerProperty.getDefaultColor().set(material.getColor());
        layerProperty.setJuicy(material.isJuicy());
        layerProperty.setFlammable(material.isFlammable());
        layerProperty.setIgnitionTemperature(material.getIgnitionTemperature());
        layerProperty.setFlameTemperature(material.getFlameTemperature());
        layerProperty.setChemicalEnergy(material.getEnergy());
        if (material.getJuiceColor() != null)
            layerProperty.setJuiceColor(new Color(material.getJuiceColor()));

        setDensity(layerProperty.getDensity());
        setBounciness(layerProperty.getRestitution());
        setFriction(layerProperty.getFriction());
        setTenacity(layerProperty.getTenacity());
        setJuicy(layerProperty.isJuicy());
        setFlammable(layerProperty.isFlammable());
        setIgnitionTemperature(layerProperty.getIgnitionTemperature());
        setFlameTemperature(layerProperty.getFlameTemperature());
        setChemicalEnergy(layerProperty.getChemicalEnergy());
        setLayerColorSlot();
        setJuiceColorSlot();

        userInterface.getToolModel().updateMesh();

    }


    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        layerWindowController.onResume();
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        userInterface.getToolModel().updateMesh();
        layerWindowController.onResume();
        LayerPointsModel LayerModel = (LayerPointsModel) model;
        layerWindowController.changeLayerName(model.getModelName(), LayerModel.getBodyId(), LayerModel.getLayerId());
    }


    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }
}
