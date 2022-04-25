package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.control.windowcontrollers.OneLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.PrismaticJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.RevoluteJointShape;
import com.evolgames.userinterface.view.windows.gamewindows.JointOptionWindow;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class JointSettingsWindowController extends OneLevelSectionedAdvancedWindowController<JointOptionWindow, SimplePrimary<?>, SimpleSecondary<?>> {
    DecimalFormat format = new DecimalFormat("##.##");
    @SuppressWarnings("unused")
    DecimalFormat format2 = new DecimalFormat("###.##");
    private ToolModel toolModel;
    private JointModel jointModel;
    private TextField<JointSettingsWindowController> prismaticDirectionAngleTextField;
    private TextField<JointSettingsWindowController> prismaticReferenceAngleTextField;
    private TextField<JointSettingsWindowController> revoluteUpperAngleTextField;
    private TextField<JointSettingsWindowController> revoluteLowerAngleTextField;
    private TextField<JointSettingsWindowController> revoluteReferenceAngleTextField;
    private NumericValidator revoluteReferenceAngleValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator revoluteLowerAngleValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator revoluteUpperAngleValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator revoluteMotorSpeedValidator = new NumericValidator(true, true, -120f, 120f, 3, 2);
    private NumericValidator revoluteMaxTorqueValidator = new NumericValidator(false, true, 0f, 99999f, 5, 2);
    private NumericValidator prismaticLowerLimitValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator prismaticUpperLimitValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator prismaticDirectionAngleValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private NumericValidator prismaticReferenceAngleValidator = new NumericValidator(true, true, -100f, 100f, 2, 2);
    private JointDef copyJointDef;
    private TextField<JointSettingsWindowController> prismaticLowerLimitTextField;
    private TextField<JointSettingsWindowController> prismaticUpperLimitTextField;
    private SimplePrimary<TitledButton<JointSettingsWindowController>> revoluteLimitsField;
    private SimplePrimary<TitledButton<JointSettingsWindowController>> revoluteHasMotorField;
    private TextField<JointSettingsWindowController> revoluteSpeedTextField;
    private TextField<JointSettingsWindowController> revoluteMaxTorqueTextField;
    private SimplePrimary<TitledButton<JointSettingsWindowController>> prismaticLimitsField;
    private SimplePrimary<TitledButton<JointSettingsWindowController>> prismaticHasMotorField;
    private TextField<JointSettingsWindowController> prismaticSpeedTextField;
    private TextField<JointSettingsWindowController> prismaticMaxForceTextField;

    public JointSettingsWindowController(KeyboardController keyboardController, ToolModel toolModel) {
        this.keyboardController = keyboardController;
        this.toolModel = toolModel;
        revoluteLowerAngleValidator.setValidationCondition(new Condition() {
            @Override
            public boolean isCondition(float value) {
                return value <= Float.parseFloat(revoluteUpperAngleTextField.getTextString());
            }

            @Override
            public String getError() {
                return "";
            }
        });
        revoluteUpperAngleValidator.setValidationCondition(new Condition() {
            @Override
            public boolean isCondition(float value) {
                return value >= Float.parseFloat(revoluteLowerAngleTextField.getTextString());
            }

            @Override
            public String getError() {
                return "";
            }
        });

        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(decimalFormatSymbols);


    }

    @Override
    public void onPrimaryButtonClicked(SimplePrimary<?> simplePrimary) {
        super.onPrimaryButtonClicked(simplePrimary);
        if (simplePrimary.getPrimaryKey() == 1 || simplePrimary.getPrimaryKey() == 2) {
            int key = simplePrimary.getPrimaryKey();
            window.getLayout().getSectionByKey(key == 1 ? 2 : 1).setActive(false);
            ((ButtonWithText<?>) window.getLayout().getSectionByKey(key == 1 ? 2 : 1).getPrimary().getMain()).updateState(Button.State.NORMAL);

            updateLayout();
        }
    }

    public void updateBodySelectionFields() {
        if (window.getLayout().getPrimariesSize() >= 2) {
            window.getLayout().removePrimary(1);
            window.getLayout().removePrimary(2);
        }
        SectionField<JointSettingsWindowController> bodyASection = new SectionField<>(1, "BodyA:", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyASection);

        SectionField<JointSettingsWindowController> bodyBSection = new SectionField<>(2, "BodyB:", ResourceManager.getInstance().mainButtonTextureRegion, this);
        window.addPrimary(bodyBSection);
        ArrayList<BodyModel> bodies = new ArrayList<>(toolModel.getBodies());
        bodies.add(0, ToolModel.groundBodyModel);

        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            ButtonWithText<JointSettingsWindowController> bodyButton = new ButtonWithText<>(bodyModel.getModelName(), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);
            SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField = new SimpleSecondary<>(1, i, bodyButton);
            window.addSecondary(bodyField);
            bodyButton.setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, bodyButton) {
                @Override
                public void informControllerButtonClicked() {
                    onFirstBodyButtonClicked(bodyModel, bodyField);
                }

                @Override
                public void informControllerButtonReleased() {

                }
            });
        }

        for (int i = 0; i < bodies.size(); i++) {
            BodyModel bodyModel = bodies.get(i);
            ButtonWithText<JointSettingsWindowController> bodyButton = new ButtonWithText<>(bodyModel.getModelName(), 2, ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.Selector, true);
            SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField = new SimpleSecondary<>(2, i, bodyButton);
            window.addSecondary(bodyField);
            bodyButton.setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, bodyButton) {
                @Override
                public void informControllerButtonClicked() {
                    onSecondBodyButtonClicked(bodyModel, bodyField);
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
        jointModel.setBodyModel1(bodyModel);

    }

    private void onSecondBodyButtonClicked(BodyModel bodyModel, SimpleSecondary<?> body2Field) {
        super.onSecondaryButtonClicked(body2Field);
        int size = window.getLayout().getSecondariesSize(2);
        for (int i = 0; i < size; i++) {
            SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
            if (body2Field != other) {
                Element main = other.getMain();
                if (main instanceof ButtonWithText) {
                    ((ButtonWithText<?>) main).updateState(Button.State.NORMAL);
                }
            }
        }
        jointModel.setBodyModel2(bodyModel);
    }

    @Override
    public void init() {
        updateBodySelectionFields();
    }

    void updateJointModel(JointModel jointModel) {
        //add the bodyA and bodyB elements
        resetLayout();
        this.jointModel = jointModel;

        if (jointModel.getJointType() == JointDef.JointType.RevoluteJoint) {
            RevoluteJointDef jointDef = (RevoluteJointDef) jointModel.getJointDef();
            RevoluteJointShape revoluteJointShape = (RevoluteJointShape) jointModel.getJointShape();


            TitledButton<JointSettingsWindowController> revoluteLimitsButton = new TitledButton<>("Has Limits:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);
            revoluteLimitsField = new SimplePrimary<>(3, revoluteLimitsButton);
            window.addPrimary(revoluteLimitsField);
            revoluteLimitsButton.getAttachment().setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, revoluteLimitsButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                    jointDef.enableLimit = true;
                    revoluteJointShape.showIndicators();
                    onPrimaryButtonClicked(revoluteLimitsField);
                }

                @Override
                public void informControllerButtonReleased() {
                    jointDef.enableLimit = false;
                    revoluteJointShape.hideIndicators();
                    onPrimaryButtonReleased(revoluteLimitsField);
                }
            });


            TitledTextField<JointSettingsWindowController> lowerAngleField = new TitledTextField<>("Lower Angle:", 6, 5, 85);
            revoluteLowerAngleTextField = lowerAngleField.getAttachment();

            revoluteLowerAngleTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, revoluteLowerAngleTextField, Keyboard.KeyboardType.Numeric, revoluteLowerAngleValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(revoluteLowerAngleTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(revoluteLowerAngleTextField);
                }
            });

            SimpleSecondary<TitledTextField<JointSettingsWindowController>> lowerAngleElement = new SimpleSecondary<>(3, 0, lowerAngleField);
            window.addSecondary(lowerAngleElement);
            revoluteLowerAngleTextField.getBehavior().setReleaseAction(() -> {
                float referenceAngle = revoluteJointShape.getReferenceAngle() * MathUtils.degreesToRadians;
                float rawValue = Float.parseFloat(revoluteLowerAngleTextField.getTextString());
                jointDef.lowerAngle = (float) (rawValue * 2 * Math.PI) + referenceAngle;
                revoluteJointShape.updateLowerAngleIndicator((float) (jointDef.lowerAngle / (2 * Math.PI) * 360));
            });


            TitledTextField<JointSettingsWindowController> referenceAngleField = new TitledTextField<>("Reference Angle:", 6, 5, 85);
            revoluteReferenceAngleTextField = referenceAngleField.getAttachment();

            revoluteReferenceAngleTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, revoluteReferenceAngleTextField, Keyboard.KeyboardType.Numeric, revoluteReferenceAngleValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(revoluteReferenceAngleTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(revoluteReferenceAngleTextField);
                }
            });


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> referenceAngleElement = new SimpleSecondary<>(3, 0, referenceAngleField);
            window.addSecondary(referenceAngleElement);
            revoluteReferenceAngleTextField.getBehavior().setReleaseAction(() -> {
                float rawValue = Float.parseFloat(revoluteReferenceAngleTextField.getTextString());
                //jointDef.referenceAngle = (float) (rawValue * 2 * Math.PI);
                revoluteJointShape.updateReferenceAngleIndicator(rawValue * 360);
            });


            TitledTextField<JointSettingsWindowController> upperAngleField = new TitledTextField<>("Upper Angle:", 6, 5, 85);
            revoluteUpperAngleTextField = upperAngleField.getAttachment();

            revoluteUpperAngleTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, revoluteUpperAngleTextField, Keyboard.KeyboardType.Numeric, revoluteUpperAngleValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(revoluteUpperAngleTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(revoluteUpperAngleTextField);
                }
            });

            SimpleSecondary<TitledTextField<JointSettingsWindowController>> upperAngleElement = new SimpleSecondary<>(3, 1, upperAngleField);
            window.addSecondary(upperAngleElement);
            revoluteUpperAngleTextField.getBehavior().setReleaseAction(() -> {
                float referenceAngle = revoluteJointShape.getReferenceAngle() * MathUtils.degreesToRadians;
                float rawValue = Float.parseFloat(revoluteUpperAngleTextField.getTextString());
                jointDef.upperAngle = (float) (rawValue * 2 * Math.PI) + referenceAngle;
                revoluteJointShape.updateUpperAngleIndicator((float) (jointDef.upperAngle / (2 * Math.PI) * 360));
            });


            TitledButton<JointSettingsWindowController> hasMotorButton = new TitledButton<>("Has Motor:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);

            revoluteHasMotorField = new SimplePrimary<>(4, hasMotorButton);
            window.addPrimary(revoluteHasMotorField);
            hasMotorButton.getAttachment().setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, hasMotorButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                    jointDef.enableMotor = true;
                    onPrimaryButtonClicked(revoluteHasMotorField);
                }

                @Override
                public void informControllerButtonReleased() {
                    jointDef.enableMotor = false;
                    onPrimaryButtonReleased(revoluteHasMotorField);

                }
            });

            TitledTextField<JointSettingsWindowController> speedField = new TitledTextField<>("Speed:", 7, 5, 85);
            revoluteSpeedTextField = speedField.getAttachment();
            revoluteSpeedTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, revoluteSpeedTextField, Keyboard.KeyboardType.Numeric, revoluteMotorSpeedValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(revoluteSpeedTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(revoluteSpeedTextField);
                }
            });
            revoluteSpeedTextField.getBehavior().setReleaseAction(()-> jointDef.motorSpeed = (float) (Float.parseFloat(revoluteSpeedTextField.getTextString()) * 2 * Math.PI)
            );


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> speedElement = new SimpleSecondary<>(4, 0, speedField);
            window.addSecondary(speedElement);


            TitledTextField<JointSettingsWindowController> torqueField = new TitledTextField<>("Torque:", 9, 5, 85);
            revoluteMaxTorqueTextField = torqueField.getAttachment();
            revoluteMaxTorqueTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, revoluteMaxTorqueTextField, Keyboard.KeyboardType.Numeric, revoluteMaxTorqueValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(revoluteMaxTorqueTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(revoluteMaxTorqueTextField);
                }
            });
            revoluteMaxTorqueTextField.getBehavior().setReleaseAction(() -> jointDef.maxMotorTorque = Float.parseFloat(revoluteMaxTorqueTextField.getTextString()));


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> torqueElement = new SimpleSecondary<>(4, 1, torqueField);
            window.addSecondary(torqueElement);
        }

        if (jointModel.getJointType() == JointDef.JointType.PrismaticJoint) {

            PrismaticJointDef jointDef = (PrismaticJointDef) jointModel.getJointDef();
            PrismaticJointShape prismaticJointShape = (PrismaticJointShape) jointModel.getJointShape();


            TitledTextField<JointSettingsWindowController> directionAngleField = new TitledTextField<>("Direction Angle:", 4, 5, 120);
            prismaticDirectionAngleTextField = directionAngleField.getAttachment();

            prismaticDirectionAngleTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticDirectionAngleTextField, Keyboard.KeyboardType.Numeric, prismaticDirectionAngleValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticDirectionAngleTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticDirectionAngleTextField);
                }
            });

            SimplePrimary<TitledTextField<JointSettingsWindowController>> directionAngleElement = new SimplePrimary<>(3, directionAngleField);
            window.addPrimary(directionAngleElement);
            prismaticDirectionAngleTextField.getBehavior().setReleaseAction(() -> {
                float rawValue = Float.parseFloat(prismaticDirectionAngleTextField.getTextString());
                float radAngle = (float) (rawValue * 2 * Math.PI);
                Vector2 u = new Vector2(1, 0);
                MathUtils.rotateVectorByRadianAngle(u, radAngle);
                jointDef.localAxis1.set(u.x, u.y);
                prismaticJointShape.updateDirectionAngleIndicator(rawValue * 360);
            });


            TitledTextField<JointSettingsWindowController> referenceAngleField = new TitledTextField<>("Reference Angle:", 4, 5, 120);
            prismaticReferenceAngleTextField = referenceAngleField.getAttachment();

            prismaticReferenceAngleTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticReferenceAngleTextField, Keyboard.KeyboardType.Numeric, prismaticReferenceAngleValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticReferenceAngleTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticReferenceAngleTextField);
                }
            });

            SimplePrimary<TitledTextField<JointSettingsWindowController>> referenceAngleElement = new SimplePrimary<>(4, referenceAngleField);
            window.addPrimary(referenceAngleElement);


            prismaticReferenceAngleTextField.getBehavior().setReleaseAction(() -> {
                float rawValue = Float.parseFloat(prismaticReferenceAngleTextField.getTextString());
                jointDef.referenceAngle = (float) (rawValue * 2 * Math.PI);

            });


            TitledButton<JointSettingsWindowController> prismaticLimitsButton = new TitledButton<>("Has Limits:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);
            prismaticLimitsField = new SimplePrimary<>(5, prismaticLimitsButton);
            window.addPrimary(prismaticLimitsField);
            prismaticLimitsButton.getAttachment().setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, prismaticLimitsButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                    jointDef.enableLimit = true;
                    onPrimaryButtonClicked(prismaticLimitsField);
                    prismaticJointShape.showIndicators();
                }

                @Override
                public void informControllerButtonReleased() {
                    jointDef.enableLimit = false;
                    onPrimaryButtonReleased(prismaticLimitsField);
                    prismaticJointShape.hideIndicators();
                }
            });


            TitledTextField<JointSettingsWindowController> lowerLimitField = new TitledTextField<>("Lower Limit :", 4, 5, 85);
            prismaticLowerLimitTextField = lowerLimitField.getAttachment();

            prismaticLowerLimitTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticLowerLimitTextField, Keyboard.KeyboardType.Numeric, prismaticLowerLimitValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticLowerLimitTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticLowerLimitTextField);
                }
            });


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> lowerLimitElement = new SimpleSecondary<>(5, 0, lowerLimitField);
            window.addSecondary(lowerLimitElement);
            prismaticLowerLimitTextField.getBehavior().setReleaseAction(() -> {
                jointDef.lowerTranslation = Float.parseFloat(prismaticLowerLimitTextField.getTextString());
                prismaticJointShape.updateLowerLimit(jointDef.lowerTranslation * 32);

            });


            TitledTextField<JointSettingsWindowController> upperLimitField = new TitledTextField<>("Upper Limit :", 4, 5, 85);
            prismaticUpperLimitTextField = upperLimitField.getAttachment();

            prismaticUpperLimitTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticUpperLimitTextField, Keyboard.KeyboardType.Numeric, prismaticUpperLimitValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticUpperLimitTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticUpperLimitTextField);
                }
            });


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> upperLimitElement = new SimpleSecondary<>(5, 1, upperLimitField);
            window.addSecondary(upperLimitElement);
            prismaticUpperLimitTextField.getBehavior().setReleaseAction(() -> {
                jointDef.upperTranslation = Float.parseFloat(prismaticUpperLimitTextField.getTextString());
                prismaticJointShape.updateUpperLimit(jointDef.upperTranslation * 32);
            });






            TitledButton<JointSettingsWindowController> hasMotorButton = new TitledButton<>("Has Motor:", ResourceManager.getInstance().onoffTextureRegion, Button.ButtonType.Selector, 5f);

            prismaticHasMotorField = new SimplePrimary<>(6, hasMotorButton);
            window.addPrimary(prismaticHasMotorField);
            hasMotorButton.getAttachment().setBehavior(new ButtonBehavior<JointSettingsWindowController>(this, hasMotorButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                    jointDef.enableMotor = true;
                    onPrimaryButtonClicked(prismaticHasMotorField);

                }

                @Override
                public void informControllerButtonReleased() {
                    jointDef.enableMotor = false;
                    onPrimaryButtonReleased(prismaticHasMotorField);

                }
            });

            TitledTextField<JointSettingsWindowController> speedField = new TitledTextField<>("Speed:", 7, 5, 85);
            prismaticSpeedTextField = speedField.getAttachment();
            prismaticSpeedTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticSpeedTextField, Keyboard.KeyboardType.Numeric, revoluteMotorSpeedValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticSpeedTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticSpeedTextField);
                }
            });
            prismaticSpeedTextField.getBehavior().setReleaseAction(() -> jointDef.motorSpeed = Float.parseFloat(prismaticSpeedTextField.getTextString()));


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> speedElement = new SimpleSecondary<>(6, 0, speedField);
            window.addSecondary(speedElement);


            TitledTextField<JointSettingsWindowController> forceField = new TitledTextField<>("Force:", 9, 5, 85);
            prismaticMaxForceTextField = forceField.getAttachment();
            prismaticMaxForceTextField.setBehavior(new TextFieldBehavior<JointSettingsWindowController>(this, prismaticMaxForceTextField, Keyboard.KeyboardType.Numeric, revoluteMaxTorqueValidator, true) {
                @Override
                protected void informControllerTextFieldTapped() {
                    JointSettingsWindowController.super.onTextFieldTapped(prismaticMaxForceTextField);
                }

                @Override
                protected void informControllerTextFieldReleased() {
                    JointSettingsWindowController.super.onTextFieldReleased(prismaticMaxForceTextField);
                }
            });
            prismaticMaxForceTextField.getBehavior().setReleaseAction(() -> jointDef.maxMotorForce = Float.parseFloat(prismaticMaxForceTextField.getTextString()));


            SimpleSecondary<TitledTextField<JointSettingsWindowController>> forceElement = new SimpleSecondary<>(6, 1, forceField);
            window.addSecondary(forceElement);


        }
        //settings the values of the textfields in accordance with the stored values
        switch (jointModel.getJointType()) {
            case RevoluteJoint:
                RevoluteJointShape jointShape = (RevoluteJointShape) jointModel.getJointShape();
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointModel.getJointDef();
                performCopyJointDef(revoluteJointDef);

                float lowerAngleInRevolutions = (float) (jointShape.getLowerAngleRelative() * MathUtils.degreesToRadians / (2 * Math.PI));
                float upperAngleInRevolutions = (float) (jointShape.getUpperAngleRelative() * MathUtils.degreesToRadians / (2 * Math.PI));
                float referenceAngleInRevolutions = (float) (jointShape.getReferenceAngle() * MathUtils.degreesToRadians / (2 * Math.PI));
                float motorSpeedInRevolutions = (float) (revoluteJointDef.motorSpeed / (2 * Math.PI));
                float maxTorque = revoluteJointDef.maxMotorTorque;

                setRevoluteHasMotor(revoluteJointDef.enableMotor);
                setRevoluteHasLimits(revoluteJointDef.enableLimit);
                setRevoluteLowerAngle(lowerAngleInRevolutions);
                setRevoluteUpperAngle(upperAngleInRevolutions);
                setRevoluteReferenceAngle(referenceAngleInRevolutions);
                setRevoluteMotorSpeed(motorSpeedInRevolutions);
                setRevoluteMaxTorque(maxTorque);
                setRevoluteHasLimits(revoluteJointDef.enableLimit);

                break;
            case WeldJoint:
                copyJointDef = new WeldJointDef();
                break;
            case PrismaticJoint:
                PrismaticJointShape prismaticJointShape = (PrismaticJointShape) jointModel.getJointShape();
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointModel.getJointDef();
                performCopyJointDef(prismaticJointDef);

                float lowerLimit = prismaticJointShape.getLowerLimit() / 32f;
                float upperLimit = prismaticJointShape.getUpperLimit() / 32f;
                float directionAngleInRevolutions = prismaticJointShape.getDirectionAngle() / 360f;
                setPrismaticHasLimits(prismaticJointDef.enableLimit);
                setPrismaticLowerTranslation(lowerLimit);
                setPrismaticUpperTranslation(upperLimit);
                setPrismaticDirectionAngle(directionAngleInRevolutions);
                setPrismaticHasMotor(prismaticJointDef.enableMotor);
                float motorSpeed = prismaticJointDef.motorSpeed;
                float maxForce = prismaticJointDef.maxMotorForce;
                setPrismaticMotorSpeed(motorSpeed);
                setPrismaticMaxForce(maxForce);

                break;
            case DistanceJoint:
                copyJointDef = new DistanceJointDef();
                break;
        }


        onUpdated();
    }

    private void performCopyJointDef(JointDef jointDef) {
        switch (jointDef.type) {

            case Unknown:
            case DistanceJoint:
            case PulleyJoint:
            case MouseJoint:
            case GearJoint:
            case LineJoint:
            case WeldJoint:
                break;
            case RevoluteJoint:
            {
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                copyJointDef = new RevoluteJointDef();
                RevoluteJointDef copyOfJointDef = (RevoluteJointDef) copyJointDef;
                copyOfJointDef.bodyA = revoluteJointDef.bodyA;
                copyOfJointDef.bodyB = revoluteJointDef.bodyB;
                copyOfJointDef.collideConnected = revoluteJointDef.collideConnected;
                copyOfJointDef.enableLimit = revoluteJointDef.enableLimit;
                copyOfJointDef.enableMotor = revoluteJointDef.enableMotor;
                copyOfJointDef.lowerAngle = revoluteJointDef.lowerAngle;
                copyOfJointDef.upperAngle = revoluteJointDef.upperAngle;
                copyOfJointDef.motorSpeed = revoluteJointDef.motorSpeed;
                copyOfJointDef.maxMotorTorque = revoluteJointDef.maxMotorTorque;
                copyJointDef = copyOfJointDef;
        }
                break;
            case PrismaticJoint:
                {
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                PrismaticJointDef copyOfJointDef = new PrismaticJointDef();
                copyOfJointDef.bodyA = prismaticJointDef.bodyA;
                copyOfJointDef.bodyB = prismaticJointDef.bodyB;
                copyOfJointDef.collideConnected = prismaticJointDef.collideConnected;
                copyOfJointDef.enableLimit = prismaticJointDef.enableLimit;
                copyOfJointDef.enableMotor = prismaticJointDef.enableMotor;
                copyOfJointDef.lowerTranslation = prismaticJointDef.lowerTranslation;
                copyOfJointDef.lowerTranslation = prismaticJointDef.upperTranslation;
                copyOfJointDef.motorSpeed = prismaticJointDef.motorSpeed;
                copyOfJointDef.maxMotorForce = prismaticJointDef.maxMotorForce;
                copyJointDef = copyOfJointDef;
            }
                break;
            case FrictionJoint:
                break;
        }
    }

    private void resetLayout() {
        for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
            if (simplePrimary.getPrimaryKey() != 1 && simplePrimary.getPrimaryKey() != 2)
                window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
        }
    }

    public void onSubmitSettings() {
        closeWindow();
    }

    public void onCancelSettings() {
        jointModel.setJointDef(copyJointDef);
        closeWindow();
    }

    public void setRevoluteMotorSpeed(float speed) {
        revoluteSpeedTextField.getBehavior().setTextValidated(format.format(speed));
    }

    public void setRevoluteMaxTorque(float maxTorque) {
        revoluteMaxTorqueTextField.getBehavior().setTextValidated(format.format(maxTorque));
    }


    public void setPrismaticMotorSpeed(float speed) {
        prismaticSpeedTextField.getBehavior().setTextValidated(format.format(speed));
    }

    public void setPrismaticMaxForce(float maxForce) {
        prismaticMaxForceTextField.getBehavior().setTextValidated(format.format(maxForce));
    }


    public void setPrismaticLowerTranslation(float lowerTranslation) {
        prismaticLowerLimitTextField.getBehavior().setTextValidated(format.format(lowerTranslation));
    }

    public void setPrismaticUpperTranslation(float upperTranslation) {
        prismaticUpperLimitTextField.getBehavior().setTextValidated(format.format(upperTranslation));
    }

    public void setRevoluteLowerAngle(float lowerAngle) {

        revoluteLowerAngleTextField.getBehavior().setTextValidated(format.format(lowerAngle));
    }

    public void setRevoluteUpperAngle(float upperAngle) {
        revoluteUpperAngleTextField.getBehavior().setTextValidated(format.format(upperAngle));
    }

    public void setRevoluteReferenceAngle(float referenceAngle) {
        revoluteReferenceAngleTextField.getBehavior().setTextValidated(format.format(referenceAngle));
    }

    @SuppressWarnings("unused")
    public void setPrismaticReferenceAngle(float referenceAngle) {
        prismaticReferenceAngleTextField.getBehavior().setTextValidated(format.format(referenceAngle));
    }

    public void setPrismaticDirectionAngle(float directionAngle) {
        prismaticDirectionAngleTextField.getBehavior().setTextValidated(format.format(directionAngle));
    }

    public void setToolModel(ToolModel toolModel) {
        this.toolModel = toolModel;
    }

    public void setRevoluteHasLimits(boolean hasLimits) {
        revoluteLimitsField.getMain().getAttachment().updateState(hasLimits ? Button.State.PRESSED : Button.State.NORMAL);
        revoluteLimitsField.getSection().setActive(hasLimits);
        window.getLayout().updateLayout();

    }

    public void setPrismaticHasLimits(boolean hasLimits) {
        prismaticLimitsField.getMain().getAttachment().updateState(hasLimits ? Button.State.PRESSED : Button.State.NORMAL);
        prismaticLimitsField.getSection().setActive(hasLimits);
        window.getLayout().updateLayout();
    }


    public void setRevoluteHasMotor(boolean hasMotor) {
        revoluteHasMotorField.getMain().getAttachment().updateState(hasMotor ? Button.State.PRESSED : Button.State.NORMAL);
        revoluteHasMotorField.getSection().setActive(hasMotor);
        window.getLayout().updateLayout();
    }

    public void setPrismaticHasMotor(boolean hasMotor) {
        prismaticHasMotorField.getMain().getAttachment().updateState(hasMotor ? Button.State.PRESSED : Button.State.NORMAL);
        prismaticHasMotorField.getSection().setActive(hasMotor);
        window.getLayout().updateLayout();
    }
}
