package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.actions.Condition;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Keyboard;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Quantity;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleQuaternary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.ColorSlot;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.FieldWithError;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.Sub2SectionField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledField;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.TitledTextField;
import com.evolgames.dollmutilationgame.entities.basics.Liquid;
import com.evolgames.dollmutilationgame.entities.basics.Material;
import com.evolgames.dollmutilationgame.entities.factories.MaterialFactory;
import com.evolgames.dollmutilationgame.entities.factories.Materials;
import com.evolgames.dollmutilationgame.entities.properties.ColoredProperties;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.gameengine.R;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.validators.AlphaNumericValidator;
import com.evolgames.dollmutilationgame.userinterface.control.validators.NumericValidator;
import com.evolgames.dollmutilationgame.userinterface.model.LayerModel;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;

import org.andengine.util.adt.color.Color;

public class LayerSettingsWindowController extends SettingsWindowController<LayerProperties> {

    private final NumericValidator densityValidator =
            new NumericValidator(false, true, 0.01f, 999f, 3, 2);
    private final NumericValidator ignitionTemperatureValidator =
            new NumericValidator(false, false, 0, 10000, 4, 1);
    private final NumericValidator flameTemperatureValidator =
            new NumericValidator(false, false, 0, 10000, 4, 1);
    private final NumericValidator energyValidator =
            new NumericValidator(false, false, 0, 100000000, 9, 1);
    private final AlphaNumericValidator layerNameValidator = new AlphaNumericValidator(8, 5);
    private LayerWindowController layerWindowController;
    private TextField<LayerSettingsWindowController> flameTemperatureTextField;
    private ColorSlot colorSlotForLayer;
    private ColorSlot colorSlotForJuice;
    private ColorSelectorWindowController colorSelectorController;
    private LayerProperties layerProperty;
    private TextField<LayerSettingsWindowController> layerNameTextField;
    private TextField<LayerSettingsWindowController> densityTextField;
    private Quantity<LayerSettingsWindowController> bouncinessQuantity;
    private Quantity<LayerSettingsWindowController> frictionQuantity;
    private Quantity<LayerSettingsWindowController> tenacityQuantity;
    private SimpleSecondary<TitledButton<LayerSettingsWindowController>> jucyField;
    private TextField<LayerSettingsWindowController> ignitionTextField;
    private TextField<LayerSettingsWindowController> energyTextField;
    private SimpleSecondary<TitledButton<LayerSettingsWindowController>> combustibleField;
    private Quantity<LayerSettingsWindowController> sharpnessQuantity;
    private Quantity<LayerSettingsWindowController> hardnessQuantity;
    private SimpleSecondary<TitledButton<LayerSettingsWindowController>> flammableField;
    private Quantity<LayerSettingsWindowController> flammabilityQuantity;
    private TitledQuantity<LayerSettingsWindowController> juiceLowerRateQuantity;
    private TitledQuantity<LayerSettingsWindowController> juicinessQuantity;
    private TitledQuantity<LayerSettingsWindowController> juiceUpperRateQuantity;
    private TitledQuantity<LayerSettingsWindowController> juiceFlammabilityQuantity;
    private Quantity<LayerSettingsWindowController> heatResistanceQuantity;
    private ColoredProperties copy;

    public void setColorSelectorController(ColorSelectorWindowController colorSelectorController) {
        this.colorSelectorController = colorSelectorController;
    }

    public void setLayerWindowController(LayerWindowController layerWindowController) {
        this.layerWindowController = layerWindowController;
    }

    private void setLayerColorSlot() {
        Color color = layerProperty.getDefaultColor();
        colorSlotForLayer.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    private void setJuiceColorSlot() {
        Color color = layerProperty.getJuiceColor();
        if (color != null) {
            colorSlotForJuice.setColor(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    @Override
    void onModelUpdated(ProperModel<LayerProperties> model) {
        super.onModelUpdated(model);

        this.layerProperty = model.getProperties();
        this.copy = layerProperty.clone();

        for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
            SimplePrimary<?> element = window.getLayout().getPrimaryByIndex(i);
            if (element.getPrimaryKey() != 2) {
                element.getSection().setActive(false);
                if (element.getMain() instanceof ButtonWithText<?>) {
                    ((ButtonWithText<?>) element.getMain()).updateState(Button.State.NORMAL);
                }
            }
        }

        setMaterialNumber(layerProperty.getMaterialNumber());
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
        setHeatResistance(layerProperty.getHeatResistance());
        setSharpness(layerProperty.getSharpness());
        setHardness(layerProperty.getHardness());
        setJuiceIndex(layerProperty.getJuiceIndex());
        setJuicy(layerProperty.isJuicy());
        setJuiceFlammability(layerProperty.getJuiceFlammability());
        setJuiceLowerRate(layerProperty.getJuicinessLowerPressure());
        setJuiceUpperRate(layerProperty.getJuicinessUpperPressure());
        setJuiciness(layerProperty.getJuicinessDensity());
        setCombustible(layerProperty.isCombustible());
        setFlammable(layerProperty.isFlammable());
        setFlammability(layerProperty.getFlammability());

        updateLayout();
    }

    @Override
    public void init() {
        window.setVisible(false);
        SectionField<LayerSettingsWindowController> sectionField =
                new SectionField<>(
                        1, ResourceManager.getInstance().getString(R.string.general_settings_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(sectionField);
        TitledTextField<LayerSettingsWindowController> layerNameField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.layer_name_title), 10,0);
        layerNameTextField = layerNameField.getAttachment();

        layerNameTextField.setBehavior(
                new TextFieldBehavior<LayerSettingsWindowController>(
                        this, layerNameTextField, Keyboard.KeyboardType.AlphaNumeric, layerNameValidator) {
                    @Override
                    protected void informControllerTextFieldTapped() {
                        LayerSettingsWindowController.super.onTextFieldTapped(layerNameTextField);
                    }

                    @Override
                    protected void informControllerTextFieldReleased() {
                        LayerSettingsWindowController.super.onTextFieldReleased(layerNameTextField);
                    }
                });
        layerNameTextField
                .getBehavior()
                .setReleaseAction(() -> model.setModelName(layerNameTextField.getTextString()));

        FieldWithError fieldWithError = new FieldWithError(layerNameField);
        SimpleSecondary<FieldWithError> secondaryElement1 = new SimpleSecondary<>(1, 0, fieldWithError);
        window.addSecondary(secondaryElement1);
        // create the color selection field

        colorSlotForLayer = new ColorSlot();
        Button<LayerSettingsWindowController> colorSelectionButton =
                new Button<>(
                        ResourceManager.getInstance().smallButtonTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        LinearLayout linearLayout = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
        linearLayout.addToLayout(colorSelectionButton);
        linearLayout.addToLayout(colorSlotForLayer);
        TitledField<LinearLayout> colorSelectionField =
                new TitledField<>(ResourceManager.getInstance().getString(R.string.select_color_title), linearLayout);
        colorSelectionButton.setBehavior(
                new ButtonBehavior<LayerSettingsWindowController>(this, colorSelectionButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        ColoredProperties props = model.getProperties();
                        if (colorSelectorController != null) {
                            colorSelectorController.bindProperties(
                                    props);
                            colorSelectorController.setAcceptAction(() -> setLayerColorSlot());
                            colorSelectorController.openWindow();
                        }
                    }
                });
        SimpleSecondary<TitledField<?>> secondaryElement2 =
                new SimpleSecondary<>(1, 1, colorSelectionField);
        window.addSecondary(secondaryElement2);

        SectionField<LayerSettingsWindowController> materialsSection =
                new SectionField<>(
                        2, ResourceManager.getInstance().getString(R.string.material_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(materialsSection);

        for (int i = 0; i < MaterialFactory.getInstance().materials.size(); i++) {
            Material material = MaterialFactory.getInstance().materials.get(i);
            ButtonWithText<LayerSettingsWindowController> materialButton =
                    new ButtonWithText<>(
                            material.getName(),
                            2,
                            ResourceManager.getInstance().simpleButtonTextureRegion,
                            Button.ButtonType.Selector,
                            true);

            SimpleSecondary<ButtonWithText<?>> materialField =
                    new SimpleSecondary<>(2, material.getIndex(), materialButton);
            window.addSecondary(materialField);
            materialButton.setBehavior(
                    new ButtonBehavior<LayerSettingsWindowController>(this, materialButton) {
                        @Override
                        public void informControllerButtonClicked() {
                            onMaterialButtonClicked(materialField);
                        }

                        @Override
                        public void informControllerButtonReleased() {
                        }
                    });
        }

        SectionField<LayerSettingsWindowController> propertiesSection =
                new SectionField<>(
                        3, ResourceManager.getInstance().getString(R.string.properties_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(propertiesSection);

        TitledTextField<LayerSettingsWindowController> densityField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.density_title), 5, 5);
        densityTextField = densityField.getAttachment();

        densityField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<LayerSettingsWindowController>(
                                this,
                                densityField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                densityValidator,
                                true) {
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
        SimpleSecondary<FieldWithError> densityElement =
                new SimpleSecondary<>(3, 0, titleDensityFieldWithError);
        window.addSecondary(densityElement);
        densityTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float density = Float.parseFloat(densityTextField.getTextString());
                            layerProperty.setDensity(density);
                        });

        TitledQuantity<LayerSettingsWindowController> titledBouncinessQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.bounciness_title), 10, "b", 5, true);
        bouncinessQuantity = titledBouncinessQuantity.getAttachment();
        titledBouncinessQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledBouncinessQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<LayerSettingsWindowController>> bouncinessElement =
                new SimpleSecondary<>(3, 1, titledBouncinessQuantity);
        window.addSecondary(bouncinessElement);
        bouncinessQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setRestitution(bouncinessQuantity.getRatio()));

        TitledQuantity<LayerSettingsWindowController> titledFrictionQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.friction_title), 10, "r", 5, true);
        frictionQuantity = titledFrictionQuantity.getAttachment();
        titledFrictionQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledFrictionQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> frictionElement =
                new SimpleSecondary<>(3, 2, titledFrictionQuantity);
        window.addSecondary(frictionElement);
        frictionQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setFriction(frictionQuantity.getRatio()));

        TitledQuantity<LayerSettingsWindowController> titledTenacityQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.tenacity_title), 20, "t", 5, true);
        tenacityQuantity = titledTenacityQuantity.getAttachment();
        titledTenacityQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledTenacityQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> tenacityElement =
                new SimpleSecondary<>(3, 3, titledTenacityQuantity);
        window.addSecondary(tenacityElement);
        tenacityQuantity
                .getBehavior()
                .setChangeAction(
                        () -> {
                            float ratio = tenacityQuantity.getRatio();
                            layerProperty.setTenacity(PhysicsConstants.getTenacityFromRatio(ratio));
                        });

        TitledQuantity<LayerSettingsWindowController> titledHardnessQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.hardness_title), 20, "t", 5, true);
        hardnessQuantity = titledHardnessQuantity.getAttachment();
        titledHardnessQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledHardnessQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> hardnessElement =
                new SimpleSecondary<>(3, 4, titledHardnessQuantity);
        window.addSecondary(hardnessElement);
        hardnessQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setHardness(hardnessQuantity.getRatio() * 10));

        int secId = 4;

        secId++;
        TitledQuantity<LayerSettingsWindowController> titledSharpnessQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.sharpness_title), 10, "t", 5, true);
        sharpnessQuantity = titledSharpnessQuantity.getAttachment();
        titledSharpnessQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledSharpnessQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> sharpnessElement =
                new SimpleSecondary<>(3, secId, titledSharpnessQuantity);
        window.addSecondary(sharpnessElement);
        sharpnessQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setSharpness(sharpnessQuantity.getRatio()));

        secId++;
        TitledQuantity<LayerSettingsWindowController> titledHeatResistanceQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.heat_resistance_title), 10, "g", 5, true);
        heatResistanceQuantity = titledHeatResistanceQuantity.getAttachment();
        titledHeatResistanceQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledHeatResistanceQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleSecondary<TitledQuantity<?>> heatResistanceElement =
                new SimpleSecondary<>(3, secId, titledHeatResistanceQuantity);
        window.addSecondary(heatResistanceElement);
        heatResistanceQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setHeatResistance(heatResistanceQuantity.getRatio()));


        secId++;
        TitledButton<LayerSettingsWindowController> juicyButton =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(R.string.juicy_title),
                        ResourceManager.getInstance().onOffTextureRegion,
                        Button.ButtonType.Selector,
                        5f, true, 1);

        jucyField = new SimpleSecondary<>(3, secId, juicyButton);
        window.addSecondary(jucyField);
        juicyButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<LayerSettingsWindowController>(this, juicyButton.getAttachment()) {
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

        ButtonWithText<LayerSettingsWindowController> button = new ButtonWithText<>(ResourceManager.getInstance().getString(R.string.liquid_title), 2, ResourceManager.getInstance().mainButtonTextureRegion, Button.ButtonType.Selector, true);
        SimpleTertiary<ButtonWithText<LayerSettingsWindowController>> juiceTypeSection =
                new SimpleTertiary<>(
                        3, secId, 0, button);
        button.setBehavior(new ButtonBehavior<LayerSettingsWindowController>(this, button) {
            @Override
            public void informControllerButtonClicked() {
                onTertiaryButtonClicked(juiceTypeSection);
            }

            @Override
            public void informControllerButtonReleased() {
                onTertiaryButtonReleased(juiceTypeSection);
            }
        });

        window.addTertiary(juiceTypeSection);

        for (int i = 0; i < Materials.liquids.size(); i++) {

            ButtonWithText<LayerSettingsWindowController> juiceButton =
                    new ButtonWithText<>(
                            this.getLiquidNameByIndex(i),
                            2,
                            ResourceManager.getInstance().simpleButtonTextureRegion,
                            Button.ButtonType.Selector,
                            true);

            SimpleQuaternary<ButtonWithText<?>> juiceField =
                    new SimpleQuaternary<>(3, secId, 0, i, juiceButton);
            window.addQuaternary(juiceField);
            juiceButton.setBehavior(
                    new ButtonBehavior<LayerSettingsWindowController>(this, juiceButton) {
                        @Override
                        public void informControllerButtonClicked() {
                            onJuiceButtonClicked(juiceField);
                        }

                        @Override
                        public void informControllerButtonReleased() {
                        }
                    });
        }

        Sub2SectionField<LayerSettingsWindowController> juicePropertiesSection =
                new Sub2SectionField<>(
                        3, secId, 2, ResourceManager.getInstance().getString(R.string.properties_title), ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addTertiary(juicePropertiesSection);


        colorSlotForJuice = new ColorSlot();
        Button<LayerSettingsWindowController> juiceColorSelectionButton =
                new Button<>(
                        ResourceManager.getInstance().smallButtonTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        linearLayout = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
        linearLayout.addToLayout(juiceColorSelectionButton);
        linearLayout.addToLayout(colorSlotForJuice);
        TitledField<LinearLayout> juiceColorTitledLayout =
                new TitledField<>(ResourceManager.getInstance().getString(R.string.color_title), linearLayout);
        juiceColorSelectionButton.setBehavior(
                new ButtonBehavior<LayerSettingsWindowController>(this, juiceColorSelectionButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        if (colorSelectorController != null) {
                            Color juiceColor = ((LayerProperties) model.getProperties()).getJuiceColor();
                            ColoredProperties juiceProps = new ColoredProperties() {
                                @Override
                                public Color getDefaultColor() {
                                    return juiceColor;
                                }
                            };
                            colorSelectorController.bindProperties(juiceProps);
                            colorSelectorController.setAcceptAction(() -> setJuiceColorSlot());
                            colorSelectorController.openWindow();
                        }
                    }
                });
        SimpleQuaternary<TitledField<?>> juiceColorField =
                new SimpleQuaternary<>(3, secId, 2, -1, juiceColorTitledLayout);
        window.addQuaternary(juiceColorField);


        juicinessQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.juiciness_title), 10, "g", 5);
        juicinessQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, juicinessQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleQuaternary<TitledQuantity<?>> juicinessElement =
                new SimpleQuaternary<>(3, secId, 2, 0, juicinessQuantity);
        window.addQuaternary(juicinessElement);
        juicinessQuantity
                .getAttachment()
                .getBehavior()
                .setChangeAction(
                        () -> layerProperty.setJuicinessDensity(juicinessQuantity.getAttachment().getRatio()));

        juiceLowerRateQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.lower_rate_title), 10, "b", 5, true);
        juiceLowerRateQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, juiceLowerRateQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        FieldWithError lowerRateFieldWithError = new FieldWithError(juiceLowerRateQuantity);
        SimpleQuaternary<FieldWithError> lowerRateElement =
                new SimpleQuaternary<>(3, secId, 2, 1, lowerRateFieldWithError);
        window.addQuaternary(lowerRateElement);
        juiceLowerRateQuantity
                .getAttachment()
                .getBehavior()
                .setChangeAction(
                        () ->
                                layerProperty.setJuicinessLowerPressure(
                                        juiceLowerRateQuantity.getAttachment().getRatio()));

        juiceLowerRateQuantity
                .getAttachment()
                .getBehavior()
                .setCondition(
                        new Condition() {
                            @Override
                            public boolean isCondition(float value) {
                                return value <= layerProperty.getJuicinessUpperPressure();
                            }

                            @Override
                            public String getError() {
                                return ResourceManager.getInstance().getString(R.string.rate_validation_message);
                            }
                        });

        juiceUpperRateQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.upper_rate_title), 10, "r", 5, true);
        juiceUpperRateQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, juiceUpperRateQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        FieldWithError upperRateFieldWithError = new FieldWithError(juiceUpperRateQuantity);
        SimpleQuaternary<FieldWithError> upperRateElement =
                new SimpleQuaternary<>(3, secId, 2, 2, upperRateFieldWithError);
        window.addQuaternary(upperRateElement);

        juiceUpperRateQuantity
                .getAttachment()
                .getBehavior()
                .setChangeAction(
                        () ->
                                layerProperty.setJuicinessUpperPressure(
                                        juiceUpperRateQuantity.getAttachment().getRatio()));
        juiceUpperRateQuantity
                .getAttachment()
                .getBehavior()
                .setCondition(
                        new Condition() {
                            @Override
                            public boolean isCondition(float value) {
                                return value >= layerProperty.getJuicinessLowerPressure();
                            }

                            @Override
                            public String getError() {
                                return ResourceManager.getInstance().getString(R.string.rate_validation_message);
                            }
                        });


        juiceFlammabilityQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.flammability_title), 10, "r", 5, true);
        juiceFlammabilityQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, juiceFlammabilityQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        FieldWithError juiceFlammabilityFieldWithError = new FieldWithError(juiceFlammabilityQuantity);
        SimpleQuaternary<FieldWithError> juiceFlammabilityElement =
                new SimpleQuaternary<>(3, secId, 2, 3, juiceFlammabilityFieldWithError);
        window.addQuaternary(juiceFlammabilityElement);

        juiceFlammabilityQuantity
                .getAttachment()
                .getBehavior()
                .setChangeAction(
                        () ->
                                layerProperty.setJuiceFlammability(
                                        juiceFlammabilityQuantity.getAttachment().getRatio()));

//-------------------------
        TitledButton<LayerSettingsWindowController> combustibleButton =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(R.string.combustible_title),
                        ResourceManager.getInstance().onOffTextureRegion,
                        Button.ButtonType.Selector,
                        5f, true, 1);

        secId++;
        combustibleField = new SimpleSecondary<>(3, secId, combustibleButton);
        window.addSecondary(combustibleField);
        combustibleButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<LayerSettingsWindowController>(
                                this, combustibleButton.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {

                                onSecondaryButtonClicked(combustibleField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                onSecondaryButtonReleased(combustibleField);
                            }
                        });
        combustibleButton
                .getAttachment()
                .getBehavior()
                .setPushAction(() -> layerProperty.setCombustible(true));
        combustibleButton
                .getAttachment()
                .getBehavior()
                .setReleaseAction(() -> layerProperty.setCombustible(false));

        TitledTextField<LayerSettingsWindowController> ignitionTemperatureField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.ignition_temperature_title), 5, 5);
        ignitionTextField = ignitionTemperatureField.getAttachment();
        ignitionTemperatureField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<LayerSettingsWindowController>(
                                this,
                                ignitionTemperatureField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                ignitionTemperatureValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                LayerSettingsWindowController.super.onTextFieldTapped(ignitionTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                LayerSettingsWindowController.super.onTextFieldReleased(ignitionTextField);
                            }
                        });
        FieldWithError ignitionFieldWithError = new FieldWithError(ignitionTemperatureField);
        SimpleTertiary<FieldWithError> ignitionElement =
                new SimpleTertiary<>(3, secId, 0, ignitionFieldWithError);
        window.addTertiary(ignitionElement);
        ignitionTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float ignitionTemperature = Float.parseFloat(ignitionTextField.getTextString());
                            layerProperty.setIgnitionTemperature(ignitionTemperature);
                        });

        TitledTextField<LayerSettingsWindowController> energyField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.energy_title), 8, 5);
        energyTextField = energyField.getAttachment();

        energyField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<LayerSettingsWindowController>(
                                this,
                                energyField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                energyValidator,
                                true) {
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
        SimpleTertiary<FieldWithError> energyElement =
                new SimpleTertiary<>(3, secId, 1, energyFieldWithError);
        window.addTertiary(energyElement);

        energyTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float energy = Float.parseFloat(energyTextField.getTextString());
                            layerProperty.setChemicalEnergy(energy);
                        });

        TitledTextField<LayerSettingsWindowController> flameTemperatureField =
                new TitledTextField<>(ResourceManager.getInstance().getString(R.string.flame_temperature_title), 5, 5);
        flameTemperatureTextField = flameTemperatureField.getAttachment();

        flameTemperatureField
                .getAttachment()
                .setBehavior(
                        new TextFieldBehavior<LayerSettingsWindowController>(
                                this,
                                flameTemperatureField.getAttachment(),
                                Keyboard.KeyboardType.Numeric,
                                flameTemperatureValidator,
                                true) {
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
        SimpleTertiary<FieldWithError> flameTemperatureElement =
                new SimpleTertiary<>(3, secId, 2, flameTemperatureFieldWithError);
        window.addTertiary(flameTemperatureElement);
        flameTemperatureTextField
                .getBehavior()
                .setReleaseAction(
                        () -> {
                            float flameTemperature = Float.parseFloat(flameTemperatureTextField.getTextString());
                            layerProperty.setFlameTemperature(flameTemperature);
                        });

        //--------------------------

        TitledButton<LayerSettingsWindowController> flammableButton =
                new TitledButton<>(
                        ResourceManager.getInstance().getString(R.string.flammable_title),
                        ResourceManager.getInstance().onOffTextureRegion,
                        Button.ButtonType.Selector,
                        5f, true, 1);

        secId++;
        this.flammableField = new SimpleSecondary<>(3, secId, flammableButton);
        window.addSecondary(this.flammableField);
        flammableButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<LayerSettingsWindowController>(
                                this, flammableButton.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                onSecondaryButtonClicked(flammableField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                onSecondaryButtonReleased(flammableField);
                            }
                        });
        flammableButton
                .getAttachment()
                .getBehavior()
                .setPushAction(() -> layerProperty.setFlammable(true));
        flammableButton
                .getAttachment()
                .getBehavior()
                .setReleaseAction(() -> layerProperty.setFlammable(false));

        TitledQuantity<LayerSettingsWindowController> titledFlammabilityQuantity =
                new TitledQuantity<>(ResourceManager.getInstance().getString(R.string.flammability_title), 10, "r", 5, true);
        flammabilityQuantity = titledFlammabilityQuantity.getAttachment();
        titledFlammabilityQuantity
                .getAttachment()
                .setBehavior(
                        new QuantityBehavior<LayerSettingsWindowController>(
                                this, titledFlammabilityQuantity.getAttachment()) {
                            @Override
                            public void informControllerQuantityUpdated(Quantity<?> quantity) {
                            }
                        });
        SimpleTertiary<TitledQuantity<LayerSettingsWindowController>> flammabilityElement =
                new SimpleTertiary<>(3, secId, 1, titledFlammabilityQuantity);
        window.addTertiary(flammabilityElement);
        flammabilityQuantity
                .getBehavior()
                .setChangeAction(() -> layerProperty.setFlammability(flammabilityQuantity.getRatio()));

        window.createScroller();
        updateLayout();
    }

    private String getLiquidNameByIndex(int i) {
        return Materials.getLiquidByIndex(i).getJuiceName();
    }

    private void setLayerName(String layerName) {
        layerNameTextField.getBehavior().setTextValidated(layerName);
    }

    private void setBounciness(float bounciness) {
        bouncinessQuantity.updateRatio(bounciness);
    }

    private void setDensity(float density) {
        densityTextField.getBehavior().setTextValidated(String.valueOf(density));
    }

    private void setIgnitionTemperature(double ignitionTemperature) {
        ignitionTextField.getBehavior().setTextValidated(String.valueOf((long) ignitionTemperature));
    }

    private void setFlameTemperature(double flameTemperature) {
        flameTemperatureTextField.getBehavior().setTextValidated(String.valueOf((long) flameTemperature));
    }

    private void setChemicalEnergy(double chemicalEnergy) {
        energyTextField.getBehavior().setTextValidated(String.valueOf((long) chemicalEnergy));
    }

    private void setFriction(float friction) {
        frictionQuantity.updateRatio(friction);
    }

    private void setTenacity(float tenacity) {
        tenacityQuantity.updateRatio(PhysicsConstants.getTenacityRatio(tenacity));
    }

    private void setHeatResistance(float heatResistance) {
        heatResistanceQuantity.updateRatio(heatResistance <= 1.0 ? heatResistance : 0);
    }

    private void setFlammability(float flammability) {
        flammabilityQuantity.updateRatio(flammability);
    }

    private void setJuiceFlammability(float flammability) {
        juiceFlammabilityQuantity.getAttachment().updateRatio(flammability);
    }

    private void setJuiceLowerRate(float juiceLowerRate) {
        juiceLowerRateQuantity.getAttachment().updateRatio(juiceLowerRate);
    }

    private void setJuiceUpperRate(float juiceUpperRate) {
        juiceUpperRateQuantity.getAttachment().updateRatio(juiceUpperRate);
    }

    private void setJuiciness(float juiciness) {
        juicinessQuantity.getAttachment().updateRatio(juiciness);
    }

    private void setSharpness(float sharpness) {
        sharpnessQuantity.updateRatio(sharpness);
    }

    private void setHardness(float hardness) {
        hardnessQuantity.updateRatio(hardness / 10f);
    }

    private void setMaterialNumber(int index) {
        for (int i = 0; i < window.getLayout().getSecondariesSize(2); i++) {
            SimpleSecondary<?> element = window.getLayout().getSecondaryByIndex(2, i);
            Element main = element.getMain();
            if (element.getSecondaryKey() == index) {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.PRESSED);
                }
            } else {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
    }

    private void setLiquidNumber(int index) {
        for (int i = 0; i < window.getLayout().getQuaternatiesSize(3, 6, 0); i++) {
            SimpleSecondary<?> element = window.getLayout().getQuaternaryByIndex(3, 6, 0, i);
            Element main = element.getMain();
            if (element.getSecondaryKey() == index) {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.PRESSED);
                }
            } else {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
    }

    private void setJuiceIndex(int index) {
        if (index == -1) {
            return;
        }
        for (int i = 0; i < window.getLayout().getQuaternatiesSize(3, 7, 0); i++) {
            SimpleQuaternary<?> element = window.getLayout().getQuaternaryByIndex(3, 7, 0, i);
            Element main = element.getMain();
            if (element.getQuaternaryKey() == index) {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.PRESSED);
                }
            } else {
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
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

    private void setCombustible(boolean flammable) {
        if (flammable) {
            combustibleField.getMain().getAttachment().updateState(Button.State.PRESSED);
            onSecondaryButtonClicked(combustibleField);
        } else {
            combustibleField.getMain().getAttachment().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(combustibleField);
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

        for (int i = 0; i < window.getLayout().getQuaternatiesSize(juiceButton.getPrimaryKey(), juiceButton.getSecondaryKey(), juiceButton.getTertiaryKey()); i++) {
            SimpleQuaternary<?> element = window.getLayout().getQuaternaryByIndex(juiceButton.getPrimaryKey(), juiceButton.getSecondaryKey(), juiceButton.getTertiaryKey(), i);
            if (element.getQuaternaryKey() != juiceButton.getQuaternaryKey()) {
                Element main = element.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
        layerProperty.setJuiceIndex(juiceButton.getQuaternaryKey());
        Liquid liquid = Materials.getLiquidByIndex(juiceButton.getQuaternaryKey());
        layerProperty.getJuiceColor().set(new Color(liquid.getDefaultColor()));
        colorSlotForJuice.setColor(layerProperty.getJuiceColor());
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
        Liquid liquid;
        if (material.getJuiceIndex() != -1) {
            liquid = Materials.getLiquidByIndex(material.getJuiceIndex());
            layerProperty.setJuiceColor(liquid.getDefaultColor());
        }
        layerProperty.setMaterialNumber(material.getIndex());

        layerProperty.setDensity(material.getDensity());
        layerProperty.setRestitution(material.getRestitution());
        layerProperty.setFriction(material.getFriction());
        layerProperty.setTenacity(material.getTenacity());
        layerProperty.setHeatResistance(material.getHeatResistance());
        layerProperty.setHardness(material.getHardness());
        layerProperty.setJuicy(material.isJuicy());
        layerProperty.setJuiceIndex(material.getJuiceIndex());
        layerProperty.setJuicinessDensity(material.getJuicinessDensity());
        layerProperty.setJuicinessLowerPressure(material.getJuicinessLowerPressure());
        layerProperty.setJuicinessUpperPressure(material.getJuicinessUpperPressure());
        layerProperty.setJuiceFlammability(material.getJuiceFlammability());
        layerProperty.getDefaultColor().set(material.getColor());
        layerProperty.setCombustible(material.isCombustible());
        layerProperty.setIgnitionTemperature(material.getIgnitionTemperature());
        layerProperty.setFlameTemperature(material.getFlameTemperature());
        layerProperty.setFlammable(material.isFlammable());
        layerProperty.setFlammability(material.getFlammability());
        layerProperty.setChemicalEnergy(material.getEnergy());

        setDensity(layerProperty.getDensity());
        setBounciness(layerProperty.getRestitution());
        setFriction(layerProperty.getFriction());
        setTenacity(layerProperty.getTenacity());
        setHeatResistance(layerProperty.getHeatResistance());
        setHardness(layerProperty.getHardness());
        setJuicy(layerProperty.isJuicy());
        setJuiceIndex(layerProperty.getJuiceIndex());
        setJuiceLowerRate(layerProperty.getJuicinessLowerPressure());
        setJuiceUpperRate(layerProperty.getJuicinessUpperPressure());
        setJuiceFlammability(layerProperty.getJuiceFlammability());
        setCombustible(layerProperty.isCombustible());
        setFlammable(layerProperty.isFlammable());
        setFlammability(layerProperty.getFlammability());
        setIgnitionTemperature(layerProperty.getIgnitionTemperature());
        setFlameTemperature(layerProperty.getFlameTemperature());
        setChemicalEnergy(layerProperty.getChemicalEnergy());
        setLayerColorSlot();
        setJuiceColorSlot();

        editorUserInterface.getToolModel().updateMesh();
    }

    @Override
    public void onCancelSettings() {
        super.onCancelSettings();
        model.setProperties(copy);
        editorUserInterface.getToolModel().updateMesh();
        layerWindowController.onResume();
    }

    @Override
    public void onSubmitSettings() {
        super.onSubmitSettings();
        editorUserInterface.getToolModel().updateMesh();
        layerWindowController.onResume();
        LayerModel LayerModel = (com.evolgames.dollmutilationgame.userinterface.model.LayerModel) model;
        layerWindowController.changeLayerName(
                model.getModelName(), LayerModel.getBodyId(), LayerModel.getLayerId());
    }

}
