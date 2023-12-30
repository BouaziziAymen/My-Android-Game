package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.entities.blockvisitors.utilities.GeometryUtils;
import com.evolgames.entities.blockvisitors.utilities.MathUtils;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.QuantityBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Condition;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.control.windowcontrollers.OneLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Quantity;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.PrismaticJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.RevoluteJointShape;
import com.evolgames.userinterface.view.windows.gamewindows.JointOptionWindow;
import com.evolgames.userinterface.view.windows.windowfields.SectionField;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledQuantity;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class JointSettingsWindowController
    extends OneLevelSectionedAdvancedWindowController<
        JointOptionWindow, SimplePrimary<?>, SimpleSecondary<?>> {
  private final NumericValidator revoluteLowerAngleValidator =
      new NumericValidator(true, true, -100f, 100f, 2, 2);
  private final NumericValidator revoluteUpperAngleValidator =
      new NumericValidator(true, true, -100f, 100f, 2, 2);
  private final NumericValidator revoluteMotorSpeedValidator =
      new NumericValidator(true, true, -120f, 120f, 3, 2);
  private final NumericValidator revoluteMaxTorqueValidator =
      new NumericValidator(false, true, 0f, 99999f, 5, 2);
  private final NumericValidator prismaticLowerLimitValidator =
      new NumericValidator(true, true, -100f, 100f, 2, 2);
  private final NumericValidator prismaticUpperLimitValidator =
      new NumericValidator(true, true, -100f, 100f, 2, 2);
  private final NumericValidator prismaticDirectionAngleValidator =
      new NumericValidator(true, true, -100f, 100f, 2, 2);
  private final NumericValidator frequencyValidator =
      new NumericValidator(false, true, 0.1f, 999f, 3, 1);
  private final EditorScene editorScene;
  DecimalFormat format = new DecimalFormat("##.##");
  @SuppressWarnings("unused")
  DecimalFormat format2 = new DecimalFormat("###.##");
  private OutlineController outlineController;
  private JointWindowController jointWindowController;
  private ToolModel toolModel;
  private JointModel jointModel;
  private TextField<JointSettingsWindowController> prismaticDirectionAngleTextField;
  private TextField<JointSettingsWindowController> revoluteUpperAngleTextField;
  private TextField<JointSettingsWindowController> revoluteLowerAngleTextField;
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
  private TextField<JointSettingsWindowController> frequencyTextField;
  private Quantity<JointSettingsWindowController> dampingQuantity;
  private TextField<JointSettingsWindowController> lengthTextField;

  public JointSettingsWindowController(EditorScene editorScene) {
    this.editorScene = editorScene;
    revoluteLowerAngleValidator.setValidationCondition(
        new Condition() {
          @Override
          public boolean isCondition(float value) {
            return value <= Float.parseFloat(revoluteUpperAngleTextField.getTextString());
          }

          @Override
          public String getError() {
            return "";
          }
        });
    revoluteUpperAngleValidator.setValidationCondition(
        new Condition() {
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

  public void setOutlineController(OutlineController outlineController) {
    this.outlineController = outlineController;
  }

  @Override
  public void onPrimaryButtonClicked(SimplePrimary<?> simplePrimary) {
    super.onPrimaryButtonClicked(simplePrimary);
    if (simplePrimary.getPrimaryKey() == 1 || simplePrimary.getPrimaryKey() == 2) {
      int key = simplePrimary.getPrimaryKey();
      window.getLayout().getSectionByKey(key == 1 ? 2 : 1).setActive(false);
      ((ButtonWithText<?>)
              window.getLayout().getSectionByKey(key == 1 ? 2 : 1).getPrimary().getMain())
          .release();

      updateLayout();
    }
  }

  public void selectBodyFields() {
    if (jointModel.getBodyModel1() != null) {
      for (int i = 0; i < window.getLayout().getSecondariesSize(1); i++) {
        SimpleSecondary<ButtonWithText<?>> secondary =
            (SimpleSecondary<ButtonWithText<?>>) window.getLayout().getSecondaryByIndex(1, i);
        if (secondary.getSecondaryKey() == jointModel.getBodyModel1().getBodyId()) {
          secondary.getMain().click();
        } else {
          secondary.getMain().release();
        }
      }
    }
    if (jointModel.getBodyModel2() != null) {
      for (int i = 0; i < window.getLayout().getSecondariesSize(2); i++) {
        SimpleSecondary<ButtonWithText<?>> secondary =
            (SimpleSecondary<ButtonWithText<?>>) window.getLayout().getSecondaryByIndex(2, i);
        if (secondary.getSecondaryKey() == jointModel.getBodyModel2().getBodyId()) {
          secondary.getMain().click();
        } else {
          secondary.getMain().release();
        }
      }
    }
  }

  public void updateBodySelectionFields() {
    if (window.getLayout().getPrimariesSize() >= 2) {
      window.getLayout().removePrimary(1);
      window.getLayout().removePrimary(2);
    }
    SectionField<JointSettingsWindowController> bodyASection =
        new SectionField<>(
            1, "BodyA:", ResourceManager.getInstance().mainButtonTextureRegion, this);
    Color bodyAColor = Colors.palette1_joint_a_color;
    bodyASection.getMain().setTextColor(bodyAColor);
    window.addPrimary(bodyASection);

    SectionField<JointSettingsWindowController> bodyBSection =
        new SectionField<>(
            2, "BodyB:", ResourceManager.getInstance().mainButtonTextureRegion, this);
    Color bodyBColor = Colors.palette1_joint_b_color;
    bodyBSection.getMain().setTextColor(bodyBColor);
    window.addPrimary(bodyBSection);
    if (toolModel != null) {
      ArrayList<BodyModel> bodies = new ArrayList<>(toolModel.getBodies());
      bodies.add(0, editorScene.getGroundModel());

      for (int i = 0; i < bodies.size(); i++) {
        BodyModel bodyModel = bodies.get(i);
        ButtonWithText<JointSettingsWindowController> bodyButton =
            new ButtonWithText<>(
                bodyModel.getModelName(),
                2,
                ResourceManager.getInstance().simpleButtonTextureRegion,
                Button.ButtonType.Selector,
                true);
        SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField =
            new SimpleSecondary<>(1, bodyModel.getBodyId(), bodyButton);
        window.addSecondary(bodyField);
        bodyButton.setBehavior(
            new ButtonBehavior<JointSettingsWindowController>(this, bodyButton) {
              @Override
              public void informControllerButtonClicked() {
                onFirstBodyButtonClicked(bodyModel, bodyField);
              }

              @Override
              public void informControllerButtonReleased() {
                onFirstBodyButtonReleased(bodyModel, bodyField);
              }
            });
      }

      for (int i = 0; i < bodies.size(); i++) {
        BodyModel bodyModel = bodies.get(i);
        ButtonWithText<JointSettingsWindowController> bodyButton =
            new ButtonWithText<>(
                bodyModel.getModelName(),
                2,
                ResourceManager.getInstance().simpleButtonTextureRegion,
                Button.ButtonType.Selector,
                true);
        SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField =
            new SimpleSecondary<>(2, bodyModel.getBodyId(), bodyButton);
        window.addSecondary(bodyField);
        bodyButton.setBehavior(
            new ButtonBehavior<JointSettingsWindowController>(this, bodyButton) {
              @Override
              public void informControllerButtonClicked() {
                onSecondBodyButtonClicked(bodyModel, bodyField);
              }

              @Override
              public void informControllerButtonReleased() {
                onSecondBodyButtonReleased(bodyModel, bodyField);
              }
            });
      }
    }
    onUpdated();
  }

  private void onFirstBodyButtonReleased(
      BodyModel bodyModel,
      SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField) {
    bodyField.getMain().release();
    outlineController.onJointBodySelectionUpdated(null, this.jointModel.getBodyModel2());
  }

  private void onSecondBodyButtonReleased(
      BodyModel bodyModel,
      SimpleSecondary<ButtonWithText<JointSettingsWindowController>> bodyField) {
    bodyField.getMain().release();
    outlineController.onJointBodySelectionUpdated(this.jointModel.getBodyModel1(), null);
  }

  public void onUpdated() {
    updateLayout();
  }

  private void onFirstBodyButtonClicked(BodyModel bodyModel, SimpleSecondary<?> body1Field) {
    if (jointModel.getBodyModel2() == bodyModel) {
      ((ButtonWithText<?>) body1Field.getMain()).release();
      return;
    }
    super.onSecondaryButtonClicked(body1Field);
    ((ButtonWithText<?>) body1Field.getMain()).click();
    int size = window.getLayout().getSecondariesSize(1);
    for (int i = 0; i < size; i++) {
      SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(1, i);
      if (body1Field != other) {
        Element main = other.getMain();
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).release();
        }
      }
    }
    jointModel.setBodyModel1(bodyModel);
    outlineController.onJointBodySelectionUpdated(
        this.jointModel.getBodyModel1(), this.jointModel.getBodyModel2());
  }

  private void onSecondBodyButtonClicked(BodyModel bodyModel, SimpleSecondary<?> body2Field) {
    if (jointModel.getBodyModel1() == bodyModel) {
      ((ButtonWithText<?>) body2Field.getMain()).release();
      return;
    }
    super.onSecondaryButtonClicked(body2Field);
    ((ButtonWithText<?>) body2Field.getMain()).click();
    int size = window.getLayout().getSecondariesSize(2);
    for (int i = 0; i < size; i++) {
      SimpleSecondary<?> other = window.getLayout().getSecondaryByIndex(2, i);
      if (body2Field != other) {
        Element main = other.getMain();
        if (main instanceof ButtonWithText) {
          ((ButtonWithText<?>) main).release();
        }
      }
    }
    jointModel.setBodyModel2(bodyModel);
    outlineController.onJointBodySelectionUpdated(
        this.jointModel.getBodyModel1(), this.jointModel.getBodyModel2());
  }

  @Override
  public void init() {
    updateBodySelectionFields();
  }

  void updateJointModel(JointModel jointModel) {
    // add the bodyA and bodyB elements
    resetLayout();
    this.jointModel = jointModel;
    selectBodyFields();
    if (jointModel.getJointType() == JointDef.JointType.RevoluteJoint) {
      RevoluteJointShape revoluteJointShape = (RevoluteJointShape) jointModel.getJointShape();

      TitledButton<JointSettingsWindowController> revoluteLimitsButton =
          new TitledButton<>(
              "Has Limits:",
              ResourceManager.getInstance().onoffTextureRegion,
              Button.ButtonType.Selector,
              5f);
      revoluteLimitsField = new SimplePrimary<>(3, revoluteLimitsButton);
      window.addPrimary(revoluteLimitsField);
      revoluteLimitsButton
          .getAttachment()
          .setBehavior(
              new ButtonBehavior<JointSettingsWindowController>(
                  this, revoluteLimitsButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                  jointModel.setEnableLimit(true);
                  revoluteJointShape.showLimitsElements();
                  onPrimaryButtonClicked(revoluteLimitsField);
                }

                @Override
                public void informControllerButtonReleased() {
                    jointModel.setEnableLimit(false);
                  revoluteJointShape.hideLimitsElements();
                  onPrimaryButtonReleased(revoluteLimitsField);
                }
              });

      TitledTextField<JointSettingsWindowController> lowerAngleField =
          new TitledTextField<>("Lower Angle:", 6, 5, 85);
      revoluteLowerAngleTextField = lowerAngleField.getAttachment();

      revoluteLowerAngleTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              revoluteLowerAngleTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteLowerAngleValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(revoluteLowerAngleTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(revoluteLowerAngleTextField);
            }
          });

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> lowerAngleElement =
          new SimpleSecondary<>(3, 0, lowerAngleField);
      window.addSecondary(lowerAngleElement);
      revoluteLowerAngleTextField
          .getBehavior()
          .setReleaseAction(
              () -> {
                float rawValue = Float.parseFloat(revoluteLowerAngleTextField.getTextString());
               jointModel.setLowerAngle ((float) (rawValue * 2 * Math.PI));
                revoluteJointShape.updateLowerAngleIndicator(
                    (float) (jointModel.getLowerAngle() / (2 * Math.PI) * 360));
              });

      TitledTextField<JointSettingsWindowController> upperAngleField =
          new TitledTextField<>("Upper Angle:", 6, 5, 85);
      revoluteUpperAngleTextField = upperAngleField.getAttachment();

      revoluteUpperAngleTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              revoluteUpperAngleTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteUpperAngleValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(revoluteUpperAngleTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(revoluteUpperAngleTextField);
            }
          });

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> upperAngleElement =
          new SimpleSecondary<>(3, 1, upperAngleField);
      window.addSecondary(upperAngleElement);
      revoluteUpperAngleTextField
          .getBehavior()
          .setReleaseAction(
              () -> {
                float rawValue = Float.parseFloat(revoluteUpperAngleTextField.getTextString());
                jointModel.setUpperAngle((float) (rawValue * 2 * Math.PI));
                revoluteJointShape.updateUpperAngleIndicator(
                    (float) (jointModel.getUpperAngle() / (2 * Math.PI) * 360));
              });

      TitledButton<JointSettingsWindowController> hasMotorButton =
          new TitledButton<>(
              "Has Motor:",
              ResourceManager.getInstance().onoffTextureRegion,
              Button.ButtonType.Selector,
              5f);

      revoluteHasMotorField = new SimplePrimary<>(4, hasMotorButton);
      window.addPrimary(revoluteHasMotorField);
      hasMotorButton
          .getAttachment()
          .setBehavior(
              new ButtonBehavior<JointSettingsWindowController>(
                  this, hasMotorButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                  jointModel.setEnableMotor(true);
                  onPrimaryButtonClicked(revoluteHasMotorField);
                }

                @Override
                public void informControllerButtonReleased() {
                 jointModel.setEnableMotor(false);
                  onPrimaryButtonReleased(revoluteHasMotorField);
                }
              });

      TitledTextField<JointSettingsWindowController> speedField =
          new TitledTextField<>("Speed:", 7, 5, 85);
      revoluteSpeedTextField = speedField.getAttachment();
      revoluteSpeedTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              revoluteSpeedTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteMotorSpeedValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(revoluteSpeedTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(revoluteSpeedTextField);
            }
          });
      revoluteSpeedTextField
          .getBehavior()
          .setReleaseAction(
              () ->
                 jointModel.setMotorSpeed(
                      (float)
                          (Float.parseFloat(revoluteSpeedTextField.getTextString()) * 2 * Math.PI)));

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> speedElement =
          new SimpleSecondary<>(4, 0, speedField);
      window.addSecondary(speedElement);

      TitledTextField<JointSettingsWindowController> torqueField =
          new TitledTextField<>("Torque:", 9, 5, 85);
      revoluteMaxTorqueTextField = torqueField.getAttachment();
      revoluteMaxTorqueTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              revoluteMaxTorqueTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteMaxTorqueValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(revoluteMaxTorqueTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(revoluteMaxTorqueTextField);
            }
          });
      revoluteMaxTorqueTextField
          .getBehavior()
          .setReleaseAction(
              () ->
                  jointModel.setMaxMotorTorque(
                      Float.parseFloat(revoluteMaxTorqueTextField.getTextString())));

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> torqueElement =
          new SimpleSecondary<>(4, 1, torqueField);
      window.addSecondary(torqueElement);
    }
    if (jointModel.getJointType() == JointDef.JointType.DistanceJoint) {
      TitledQuantity<JointSettingsWindowController> titledDampingQuantity =
          new TitledQuantity<>("Damping:", 10, "b", 5, 76);
      dampingQuantity = titledDampingQuantity.getAttachment();
      titledDampingQuantity
          .getAttachment()
          .setBehavior(
              new QuantityBehavior<JointSettingsWindowController>(
                  this, titledDampingQuantity.getAttachment()) {
                @Override
                public void informControllerQuantityUpdated(Quantity<?> quantity) {}
              });
      SimplePrimary<TitledQuantity<JointSettingsWindowController>> dampingElement =
          new SimplePrimary<>(3, titledDampingQuantity);
      window.addPrimary(dampingElement);
      dampingQuantity
          .getBehavior()
          .setChangeAction(() -> jointModel.setDampingRatio(dampingQuantity.getRatio()));

      TitledTextField<JointSettingsWindowController> frequencyField =
          new TitledTextField<>("Frequency:", 4, 5, 120);
      frequencyTextField = frequencyField.getAttachment();

      frequencyTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this, frequencyTextField, Keyboard.KeyboardType.Numeric, frequencyValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(frequencyTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(frequencyTextField);
            }
          });

      SimplePrimary<TitledTextField<JointSettingsWindowController>> frequencyElement =
          new SimplePrimary<>(4, frequencyField);
      window.addPrimary(frequencyElement);
      frequencyTextField
          .getBehavior()
          .setReleaseAction(
              () -> jointModel.setFrequencyHz(Float.parseFloat(frequencyTextField.getTextString())));
      //
      TitledTextField<JointSettingsWindowController> lengthField =
          new TitledTextField<>("Length:", 4, 5, 120);
      lengthTextField = lengthField.getAttachment();

      lengthTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this, lengthTextField, Keyboard.KeyboardType.Numeric, frequencyValidator, true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(lengthTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(lengthTextField);
            }
          });

      SimplePrimary<TitledTextField<JointSettingsWindowController>> lengthElement =
          new SimplePrimary<>(5, lengthField);
      window.addPrimary(lengthElement);
      lengthTextField
          .getBehavior()
          .setReleaseAction(
              () -> jointModel.setLength(Float.parseFloat(lengthTextField.getTextString())));
    }

    if (jointModel.getJointType() == JointDef.JointType.PrismaticJoint) {
      PrismaticJointShape prismaticJointShape = (PrismaticJointShape) jointModel.getJointShape();

      TitledTextField<JointSettingsWindowController> directionAngleField =
          new TitledTextField<>("Direction Angle:", 4, 5, 120);
      prismaticDirectionAngleTextField = directionAngleField.getAttachment();

      prismaticDirectionAngleTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              prismaticDirectionAngleTextField,
              Keyboard.KeyboardType.Numeric,
              prismaticDirectionAngleValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(
                  prismaticDirectionAngleTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(
                  prismaticDirectionAngleTextField);
            }
          });

      SimplePrimary<TitledTextField<JointSettingsWindowController>> directionAngleElement =
          new SimplePrimary<>(3, directionAngleField);
      window.addPrimary(directionAngleElement);
      prismaticDirectionAngleTextField
          .getBehavior()
          .setReleaseAction(
              () -> {
                Vector2 dir = new Vector2(1, 0);
                float angle =
                    360 * Float.parseFloat(prismaticDirectionAngleTextField.getTextString());
                GeometryUtils.rotateVectorDeg(dir, angle);
                jointModel.getLocalAxis1().set(dir);
                prismaticJointShape.updateDirectionAngleIndicator(angle);
              });

      TitledButton<JointSettingsWindowController> prismaticLimitsButton =
          new TitledButton<>(
              "Has Limits:",
              ResourceManager.getInstance().onoffTextureRegion,
              Button.ButtonType.Selector,
              5f);
      prismaticLimitsField = new SimplePrimary<>(5, prismaticLimitsButton);
      window.addPrimary(prismaticLimitsField);
      prismaticLimitsButton
          .getAttachment()
          .setBehavior(
              new ButtonBehavior<JointSettingsWindowController>(
                  this, prismaticLimitsButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                  jointModel.setEnableLimit(true);
                  onPrimaryButtonClicked(prismaticLimitsField);
                  prismaticJointShape.showLimitsElements();
                }

                @Override
                public void informControllerButtonReleased() {
                    jointModel.setEnableLimit(false);
                  onPrimaryButtonReleased(prismaticLimitsField);
                  prismaticJointShape.hideLimitsElements();
                }
              });

      TitledTextField<JointSettingsWindowController> lowerLimitField =
          new TitledTextField<>("Lower Limit :", 4, 5, 85);
      prismaticLowerLimitTextField = lowerLimitField.getAttachment();

      prismaticLowerLimitTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              prismaticLowerLimitTextField,
              Keyboard.KeyboardType.Numeric,
              prismaticLowerLimitValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(prismaticLowerLimitTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(prismaticLowerLimitTextField);
            }
          });

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> lowerLimitElement =
          new SimpleSecondary<>(5, 0, lowerLimitField);
      window.addSecondary(lowerLimitElement);
      prismaticLowerLimitTextField
          .getBehavior()
          .setReleaseAction(
              () -> {
                jointModel.setLowerTranslation(
                    Float.parseFloat(prismaticLowerLimitTextField.getTextString()));
                prismaticJointShape.updateLowerLimit(jointModel.getLowerTranslation() * 32);
              });

      TitledTextField<JointSettingsWindowController> upperLimitField =
          new TitledTextField<>("Upper Limit :", 4, 5, 85);
      prismaticUpperLimitTextField = upperLimitField.getAttachment();

      prismaticUpperLimitTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              prismaticUpperLimitTextField,
              Keyboard.KeyboardType.Numeric,
              prismaticUpperLimitValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(prismaticUpperLimitTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(prismaticUpperLimitTextField);
            }
          });

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> upperLimitElement =
          new SimpleSecondary<>(5, 1, upperLimitField);
      window.addSecondary(upperLimitElement);
      prismaticUpperLimitTextField
          .getBehavior()
          .setReleaseAction(
              () -> {
                jointModel.setUpperTranslation(
                    Float.parseFloat(prismaticUpperLimitTextField.getTextString()));
                prismaticJointShape.updateUpperLimit(jointModel.getUpperTranslation() * 32);
              });

      TitledButton<JointSettingsWindowController> hasMotorButton =
          new TitledButton<>(
              "Has Motor:",
              ResourceManager.getInstance().onoffTextureRegion,
              Button.ButtonType.Selector,
              5f);

      prismaticHasMotorField = new SimplePrimary<>(6, hasMotorButton);
      window.addPrimary(prismaticHasMotorField);
      hasMotorButton
          .getAttachment()
          .setBehavior(
              new ButtonBehavior<JointSettingsWindowController>(
                  this, hasMotorButton.getAttachment()) {
                @Override
                public void informControllerButtonClicked() {
                  jointModel.setEnableLimit(true);
                  onPrimaryButtonClicked(prismaticHasMotorField);
                }

                @Override
                public void informControllerButtonReleased() {
                    jointModel.setEnableLimit(false);
                  onPrimaryButtonReleased(prismaticHasMotorField);
                }
              });

      TitledTextField<JointSettingsWindowController> speedField =
          new TitledTextField<>("Speed:", 7, 5, 85);
      prismaticSpeedTextField = speedField.getAttachment();
      prismaticSpeedTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              prismaticSpeedTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteMotorSpeedValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(prismaticSpeedTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(prismaticSpeedTextField);
            }
          });
      prismaticSpeedTextField
          .getBehavior()
          .setReleaseAction(
              () ->
                  jointModel.setMotorSpeed(Float.parseFloat(prismaticSpeedTextField.getTextString())));

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> speedElement =
          new SimpleSecondary<>(6, 0, speedField);
      window.addSecondary(speedElement);

      TitledTextField<JointSettingsWindowController> forceField =
          new TitledTextField<>("Force:", 9, 5, 85);
      prismaticMaxForceTextField = forceField.getAttachment();
      prismaticMaxForceTextField.setBehavior(
          new TextFieldBehavior<JointSettingsWindowController>(
              this,
              prismaticMaxForceTextField,
              Keyboard.KeyboardType.Numeric,
              revoluteMaxTorqueValidator,
              true) {
            @Override
            protected void informControllerTextFieldTapped() {
              JointSettingsWindowController.super.onTextFieldTapped(prismaticMaxForceTextField);
            }

            @Override
            protected void informControllerTextFieldReleased() {
              JointSettingsWindowController.super.onTextFieldReleased(prismaticMaxForceTextField);
            }
          });
      prismaticMaxForceTextField
          .getBehavior()
          .setReleaseAction(
              () ->
                 jointModel.setMaxMotorForce(
                      Float.parseFloat(prismaticMaxForceTextField.getTextString())));

      SimpleSecondary<TitledTextField<JointSettingsWindowController>> forceElement =
          new SimpleSecondary<>(6, 1, forceField);
      window.addSecondary(forceElement);
    }
    // setting the values of the textfields in accordance with the stored values
    switch (jointModel.getJointType()) {
      case RevoluteJoint:
        RevoluteJointShape jointShape = (RevoluteJointShape) jointModel.getJointShape();

        float lowerAngleInRevolutions =
            (float)
                (jointShape.getLowerAngleRelative() * MathUtils.degreesToRadians / (2 * Math.PI));
        float upperAngleInRevolutions =
            (float)
                (jointShape.getUpperAngleRelative() * MathUtils.degreesToRadians / (2 * Math.PI));
        float motorSpeedInRevolutions = (float) (jointModel.getMotorSpeed() / (2 * Math.PI));
        float maxTorque = jointModel.getMaxMotorTorque();

        setRevoluteHasMotor(jointModel.isEnableMotor());
        setRevoluteHasLimits(jointModel.isEnableLimit());
        setRevoluteLowerAngle(lowerAngleInRevolutions);
        setRevoluteUpperAngle(upperAngleInRevolutions);
        setRevoluteMotorSpeed(motorSpeedInRevolutions);
        setRevoluteMaxTorque(maxTorque);
        setRevoluteHasLimits(jointModel.isEnableLimit());

        break;
      case WeldJoint:
        break;
      case PrismaticJoint:
        PrismaticJointShape prismaticJointShape = (PrismaticJointShape) jointModel.getJointShape();

        float lowerLimit = prismaticJointShape.getLowerLimit() / 32f;
        float upperLimit = prismaticJointShape.getUpperLimit() / 32f;
        float directionAngleInRevolutions = prismaticJointShape.getDirectionAngleDegrees() / 360f;
        setPrismaticHasLimits(jointModel.isEnableLimit());
        setPrismaticLowerTranslation(lowerLimit);
        setPrismaticUpperTranslation(upperLimit);
        setPrismaticDirectionAngle(directionAngleInRevolutions * 360);
        setPrismaticHasMotor(jointModel.isEnableMotor());
        float motorSpeed = jointModel.getMotorSpeed();
        float maxForce = jointModel.getMaxForce();
        setPrismaticMotorSpeed(motorSpeed);
        setPrismaticMaxForce(maxForce);

        break;
      case DistanceJoint:
        setDistanceDamping(jointModel.getDampingRatio());
        setDistanceFrequency(jointModel.getFrequencyHz());
        setDistanceLength(jointModel.getLength());
        break;
    }
    onUpdated();
  }

  private void resetLayout() {
    for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
      if (simplePrimary.getPrimaryKey() != 1 && simplePrimary.getPrimaryKey() != 2)
        window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
    }
  }

  public void onSubmitSettings() {
    closeWindow();
    jointWindowController.onResume();
  }

  public void onCancelSettings() {
   // jointModel.setJointDef(copyJointDef);
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

  public void setDistanceFrequency(float frequency) {
    jointModel.setFrequencyHz(frequency);
    frequencyTextField.getBehavior().setTextValidated(format.format(frequency));
  }

  public void setDistanceLength(float length) {
    jointModel.setLength(length);
    lengthTextField.getBehavior().setTextValidated(format.format(length));
  }

  public void setDistanceDamping(float damping) {
    jointModel.setDampingRatio(damping);
    dampingQuantity.updateRatio(damping);
  }

  public void setRevoluteLowerAngle(float lowerAngleInRevolutions) {
    jointModel.setLowerAngle((float) (lowerAngleInRevolutions * 2 * Math.PI));
    revoluteLowerAngleTextField
        .getBehavior()
        .setTextValidated(format.format(lowerAngleInRevolutions));
  }

  public void setRevoluteUpperAngle(float upperAngle) {
    jointModel.setUpperAngle((float) (upperAngle * 2 * Math.PI));
    revoluteUpperAngleTextField.getBehavior().setTextValidated(format.format(upperAngle));
  }

  public void setPrismaticDirectionAngle(float directionAngle) {
    prismaticDirectionAngleTextField.getBehavior().setTextValidated(format.format(directionAngle));
    Vector2 dir = new Vector2(1, 0);
    GeometryUtils.rotateVectorDeg(dir, directionAngle);
    jointModel.getLocalAxis1().set(dir);
  }

  public void setToolModel(ToolModel toolModel) {
    this.toolModel = toolModel;
  }

  public void setRevoluteHasLimits(boolean hasLimits) {
    revoluteLimitsField
        .getMain()
        .getAttachment()
        .updateState(hasLimits ? Button.State.PRESSED : Button.State.NORMAL);
    revoluteLimitsField.getSection().setActive(hasLimits);
    updateLayout();
  }

  public void setPrismaticHasLimits(boolean hasLimits) {
    prismaticLimitsField
        .getMain()
        .getAttachment()
        .updateState(hasLimits ? Button.State.PRESSED : Button.State.NORMAL);
    prismaticLimitsField.getSection().setActive(hasLimits);
    updateLayout();
  }

  public void setRevoluteHasMotor(boolean hasMotor) {
    revoluteHasMotorField
        .getMain()
        .getAttachment()
        .updateState(hasMotor ? Button.State.PRESSED : Button.State.NORMAL);
    revoluteHasMotorField.getSection().setActive(hasMotor);
    updateLayout();
  }

  public void setPrismaticHasMotor(boolean hasMotor) {
    prismaticHasMotorField
        .getMain()
        .getAttachment()
        .updateState(hasMotor ? Button.State.PRESSED : Button.State.NORMAL);
    prismaticHasMotorField.getSection().setActive(hasMotor);
    updateLayout();
  }

  public void setJointWindowController(JointWindowController jointWindowController) {
    this.jointWindowController = jointWindowController;
  }

  public BodyModel getBodyModelA() {
    if (jointModel == null) {
      return null;
    }
    return jointModel.getBodyModel1();
  }

  public BodyModel getBodyModelB() {
    if (jointModel == null) {
      return null;
    }
    return jointModel.getBodyModel2();
  }
}
