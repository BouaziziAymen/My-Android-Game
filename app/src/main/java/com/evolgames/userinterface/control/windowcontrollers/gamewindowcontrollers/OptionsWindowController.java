package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.TextFieldBehavior;
import com.evolgames.userinterface.control.validators.IntegerValidator;
import com.evolgames.userinterface.control.validators.NumericValidator;
import com.evolgames.userinterface.control.windowcontrollers.TwoLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.ImageShapeModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;
import com.evolgames.userinterface.view.layouts.ButtonBoard;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.shapes.ImageShape;
import com.evolgames.userinterface.view.windows.gamewindows.OptionsWindow;
import com.evolgames.userinterface.view.windows.windowfields.ColorSlot;
import com.evolgames.userinterface.view.windows.windowfields.TitledButton;
import com.evolgames.userinterface.view.windows.windowfields.TitledField;
import com.evolgames.userinterface.view.windows.windowfields.TitledTextField;

import org.andengine.util.adt.color.Color;

import java.util.function.BooleanSupplier;

public class OptionsWindowController
        extends TwoLevelSectionedAdvancedWindowController<
        OptionsWindow, SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>> {
    private final IntegerValidator polygonNumberValidator = new IntegerValidator(3, 30);
    private final NumericValidator polygonRadiusValidator =
            new NumericValidator(false, true, 0, 32 * 5, 3, 1);
    private ItemSaveWindowController itemSaveController;
    private EditorUserInterface editorUserInterface;
    private ColorSlot pipeColorSlot;
    private TextField<OptionsWindowController> polygonRadiusTextField;
    private CreationZoneController creationZoneController;
    private TextField<OptionsWindowController> polygonPointNumberTextField;
    private Text dimensionsText;
    private SettingsType settingsType;

    public void setCreationZoneController(CreationZoneController creationZoneController) {
        this.creationZoneController = creationZoneController;
    }

    public void setItemSaveController(ItemSaveWindowController itemSaveController) {
        this.itemSaveController = itemSaveController;
    }


    public void selectSettingsType(SettingsType settingsType) {
        resetLayout();
        this.settingsType = settingsType;
        switch (settingsType) {
            case TOOL_SAVE_SETTINGS:
            case NONE:
                closeWindow();
                break;
            case PROJECTILE_SETTINGS:
                break;
            case MOVE_TOOL_POINT_SETTING:
            case MOVE_JOINT_POINT_SETTINGS:
                openWindow();
                window.addPrimary(createMoveLimitsOption(0));
                break;
            case ROTATE_IMAGE_SETTINGS:
            case MOVE_IMAGE_SETTINGS:
                openWindow();
                dimensionsText = new Text("",2);
                window.addPrimary( new SimplePrimary<>(0,this.dimensionsText));
                break;
            case SCALE_IMAGE_SETTINGS:
                openWindow();
                window.addPrimary(createOnOffField(0, "Fixed Ratio :", (b) -> creationZoneController.setImageFixedRatio(b), () -> creationZoneController.isImageFixedRatio()));
                dimensionsText = new Text("!!!",2);
                window.addPrimary( new SimplePrimary<>(1,this.dimensionsText));
                break;
            case REMOVE_POINT_SETTINGS:
                openWindow();
                window.addPrimary(createReferenceOption(0));
                break;
            case PIPE_SETTINGS:
                openWindow();
                pipeColorSlot = new ColorSlot();
                Button<OptionsWindowController> colorValidatedButton =
                        new Button<>(
                                ResourceManager.getInstance().smallButtonTextureRegion,
                                Button.ButtonType.OneClick,
                                true);
                LinearLayout linearLayout = new LinearLayout(LinearLayout.Direction.Horizontal, 5);
                linearLayout.addToLayout(colorValidatedButton);
                linearLayout.addToLayout(pipeColorSlot);
                pipeColorSlot.setColor(0, 0, 0);
                TitledField<LinearLayout> colorSelectionField =
                        new TitledField<>("Piped Color:", linearLayout);
                colorValidatedButton.setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, colorValidatedButton) {
                            @Override
                            public void informControllerButtonClicked() {
                                editorUserInterface.getColorSelectorWindowController().addColorToPanel(new Color(pipeColorSlot.getColor()), true);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                            }
                        });
                SimplePrimary<TitledField<?>> primary = new SimplePrimary<>(1, colorSelectionField);
                window.addPrimary(primary);
                break;
            case INSERT_POINT_SETTINGS:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                window.addPrimary(createReferenceOption(1));

                break;
            case MOVE_POINT_SETTINGS:
                openWindow();
                window.addPrimary(createReferenceOption(0));
                ButtonWithText<OptionsWindowController> createReferenceButton =
                        new ButtonWithText<>(
                                "Reference",
                                2,
                                ResourceManager.getInstance().simpleButtonTextureRegion,
                                Button.ButtonType.OneClick,
                                true);
                createReferenceButton.setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, createReferenceButton) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                creationZoneController.createReferencePoint();
                            }
                        });
                SimplePrimary<ButtonWithText<OptionsWindowController>> referenceElement =
                        new SimplePrimary<>(1, createReferenceButton);
                window.addPrimary(referenceElement);
                break;
            case ROTATE_SETTINGS:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                break;
            case MIRROR_SETTINGS:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                window.addPrimary(createDirectionBoard(1));
                window.addPrimary(createOnOffField(2, "Same :", (b) -> creationZoneController.setSameShape(b), () -> creationZoneController.isSameShape()));
                break;
            case SHIFT_SETTINGS:
                openWindow();
                window.addPrimary(createDirectionBoard(0));
                break;
            case DISTANCE_JOINT_SETTINGS:
            case REVOLUTE_JOINT_SETTINGS:
            case PRISMATIC_JOINT_SETTINGS:
            case WELD_JOINT_SETTINGS:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                window.addPrimary(createCongruenceOption(1));
                break;
            case POLYGON_CREATION_SETTINGS:
                openWindow();
                TitledTextField<OptionsWindowController> polygonPointNumberField =
                        new TitledTextField<>("Number Of Points:", 4, 5, 120);
                polygonPointNumberTextField = polygonPointNumberField.getAttachment();

                polygonPointNumberTextField.setBehavior(
                        new TextFieldBehavior<OptionsWindowController>(
                                this,
                                polygonPointNumberTextField,
                                Keyboard.KeyboardType.Numeric,
                                polygonNumberValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                OptionsWindowController.super.onTextFieldTapped(polygonPointNumberTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                OptionsWindowController.super.onTextFieldReleased(polygonPointNumberTextField);
                            }
                        });
                polygonPointNumberTextField
                        .getBehavior()
                        .setReleaseAction(
                                () -> {
                                    int numberOfPoints =
                                            Integer.parseInt(polygonPointNumberTextField.getTextString());
                                    creationZoneController.setNumberOfPointsForPolygon(numberOfPoints);
                                });
                setPolygonPointNumber(creationZoneController.getNumberOfPointsForPolygon());

                SimplePrimary<TitledTextField<?>> polygonPointsNumberElement =
                        new SimplePrimary<>(0, polygonPointNumberField);
                window.addPrimary(polygonPointsNumberElement);

                TitledButton<OptionsWindowController> fixedRadiusButton =
                        new TitledButton<>(
                                "Fixed Radius:",
                                ResourceManager.getInstance().onoffTextureRegion,
                                Button.ButtonType.Selector,
                                5f);
                SimplePrimary<TitledButton<?>> fixedRadiusField = new SimplePrimary<>(1, fixedRadiusButton);
                window.addPrimary(fixedRadiusField);
                fixedRadiusButton
                        .getAttachment()
                        .setBehavior(
                                new ButtonBehavior<OptionsWindowController>(
                                        this, fixedRadiusButton.getAttachment()) {
                                    @Override
                                    public void informControllerButtonClicked() {
                                        creationZoneController.setFixedRadiusEnabled(true);
                                        onPrimaryButtonClicked(fixedRadiusField);
                                    }

                                    @Override
                                    public void informControllerButtonReleased() {
                                        creationZoneController.setFixedRadiusEnabled(false);
                                        onPrimaryButtonReleased(fixedRadiusField);
                                    }
                                });
                if (creationZoneController.isFixedRadiusForPolygonEnabled()) {
                    fixedRadiusButton.getAttachment().updateState(Button.State.PRESSED);
                    onPrimaryButtonClicked(fixedRadiusField);
                } else {
                    fixedRadiusButton.getAttachment().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(fixedRadiusField);
                }

                TitledTextField<OptionsWindowController> polygonRadiusField =
                        new TitledTextField<>("Radius:", 4, 5, 48);
                polygonRadiusTextField = polygonRadiusField.getAttachment();

                polygonRadiusTextField.setBehavior(
                        new TextFieldBehavior<OptionsWindowController>(
                                this,
                                polygonRadiusTextField,
                                Keyboard.KeyboardType.Numeric,
                                polygonRadiusValidator,
                                true) {
                            @Override
                            protected void informControllerTextFieldTapped() {
                                OptionsWindowController.super.onTextFieldTapped(polygonRadiusTextField);
                            }

                            @Override
                            protected void informControllerTextFieldReleased() {
                                OptionsWindowController.super.onTextFieldReleased(polygonRadiusTextField);
                            }
                        });
                polygonRadiusTextField
                        .getBehavior()
                        .setReleaseAction(
                                () -> {
                                    float radius = Integer.parseInt(polygonRadiusTextField.getTextString());
                                    creationZoneController.setRadiusForPolygon(radius);
                                });
                setPolygonRadius(creationZoneController.getRadiusForPolygon());

                SimpleSecondary<TitledTextField<?>> polygonRadiusElement =
                        new SimpleSecondary<>(1, 0, polygonRadiusField);
                window.addSecondary(polygonRadiusElement);

                window.addPrimary(createMagnetBoard(2));

                break;
            case IMAGE_SETTINGS:
                openWindow();
                ButtonWithText<OptionsWindowController> addImageButton =
                        new ButtonWithText<>(
                                "Add Image",
                                2,
                                ResourceManager.getInstance().simpleButtonTextureRegion,
                                Button.ButtonType.OneClick,
                                true);

                SimplePrimary<ButtonWithText<?>> addImageField = new SimplePrimary<>(0, addImageButton);
                window.addPrimary(addImageField);

                addImageButton.setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, addImageButton) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                editorUserInterface.onAddImageButtonClicked();
                            }
                        });


                ButtonWithText<OptionsWindowController> mirrorImageButton =
                        new ButtonWithText<>(
                                "Mirror Image",
                                2,
                                ResourceManager.getInstance().simpleButtonTextureRegion,
                                Button.ButtonType.OneClick,
                                true);

                SimplePrimary<ButtonWithText<?>> mirrorImageField = new SimplePrimary<>(0, mirrorImageButton);
                window.addPrimary(mirrorImageField);

                mirrorImageButton.setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, mirrorImageButton) {
                            @Override
                            public void informControllerButtonClicked() {
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                editorUserInterface.onMirrorImageButtonClicked();
                            }
                        });
                break;
            case AMMO_TOOL_POINT_SETTING:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                break;
            case BOMB_TOOL_POINT_SETTING:
                openWindow();
                window.addPrimary(createMagnetBoard(0));
                break;

        }
        updateLayout();
    }

    @Override
    public void init() {
        super.init();
        setBodyOnly();
        updateLayout();
        selectSettingsType(SettingsType.NONE);
    }

    private void resetLayout() {
        for (SimplePrimary<?> simplePrimary : window.getLayout().getPrimaries()) {
            window.getLayout().removePrimary(simplePrimary.getPrimaryKey());
        }
    }

    private void setPolygonPointNumber(int pointNumber) {
        polygonPointNumberTextField.getBehavior().setTextValidated(String.valueOf(pointNumber));
    }

    private void setPolygonRadius(float polygonRadius) {
        polygonRadiusTextField.getBehavior().setTextValidated(String.valueOf(polygonRadius));
    }

    public void updatePipeColor(Color color) {
        pipeColorSlot.setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public SimplePrimary<TitledField<ButtonBoard>> createMagnetBoard(int primaryId) {

        ButtonBoard magnetBoard = new ButtonBoard(0, 0, LinearLayout.Direction.Horizontal, 1);
        Button<OptionsWindowController> button1 =
                new Button<>(
                        ResourceManager.getInstance().optionsMagnetTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        Button<OptionsWindowController> button2 =
                new Button<>(
                        ResourceManager.getInstance().optionsLinesTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        Button<OptionsWindowController> button3 =
                new Button<>(
                        ResourceManager.getInstance().optionsCenterTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        magnetBoard.addToButtonBoard(button1);
        magnetBoard.addToButtonBoard(button2);
        magnetBoard.addToButtonBoard(button3);
        button1.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, button1) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setMagnet(true);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        editorUserInterface.getCreationZoneController().setMagnet(false);
                    }
                });
        button2.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, button2) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setMagnetLines(true);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        editorUserInterface.getCreationZoneController().setMagnetLines(false);
                    }
                });

        button3.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, button3) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setMagnetCenters(true);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        editorUserInterface.getCreationZoneController().setMagnetCenters(false);
                    }
                });
        TitledField<ButtonBoard> centerMagnetField = new TitledField<>("Magnet:", magnetBoard, 8);

        return new SimplePrimary<>(primaryId, centerMagnetField);
    }

    public SimplePrimary<TitledField<ButtonBoard>> createDirectionBoard(int primaryId) {
        ButtonBoard magnetBoard = new ButtonBoard(0, 0, LinearLayout.Direction.Horizontal, 1);
        Button<OptionsWindowController> horizontalButton =
                new Button<>(
                        ResourceManager.getInstance().optionsHorTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        Button<OptionsWindowController> verticalButton =
                new Button<>(
                        ResourceManager.getInstance().optionsVerTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        Button<OptionsWindowController> directionButton =
                new Button<>(
                        ResourceManager.getInstance().optionsDirTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        magnetBoard.addToButtonBoard(horizontalButton);
        magnetBoard.addToButtonBoard(verticalButton);
        magnetBoard.addToButtonBoard(directionButton);
        horizontalButton.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, horizontalButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setDirection(Direction.HORIZONTAL);
                        verticalButton.updateState(Button.State.NORMAL);
                        directionButton.updateState(Button.State.NORMAL);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });
        verticalButton.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, verticalButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setDirection(Direction.VERTICAL);
                        horizontalButton.updateState(Button.State.NORMAL);
                        directionButton.updateState(Button.State.NORMAL);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });

        directionButton.setBehavior(
                new ButtonBehavior<OptionsWindowController>(this, directionButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        editorUserInterface.getCreationZoneController().setDirection(Direction.OTHER);
                        horizontalButton.updateState(Button.State.NORMAL);
                        verticalButton.updateState(Button.State.NORMAL);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });
        TitledField<ButtonBoard> directionField = new TitledField<>("Direction:", magnetBoard, 8);

        return new SimplePrimary<>(primaryId, directionField);
    }

    private SimplePrimary<TitledButton<?>> createReferenceOption(int primaryKey) {
        TitledButton<OptionsWindowController> referenceButton =
                new TitledButton<>(
                        "Reference :",
                        ResourceManager.getInstance().onoffTextureRegion,
                        Button.ButtonType.Selector,
                        5f);
        SimplePrimary<TitledButton<?>> referenceField =
                new SimplePrimary<>(primaryKey, referenceButton);
        referenceButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, referenceButton.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                creationZoneController.setReference(true);
                                onPrimaryButtonClicked(referenceField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                creationZoneController.setReference(false);
                                onPrimaryButtonReleased(referenceField);
                            }
                        });

        if (creationZoneController.isReferenceEnabled()) {
            referenceButton.getAttachment().updateState(Button.State.PRESSED);
            if (referenceField.getSection() != null) onPrimaryButtonClicked(referenceField);
        } else {
            referenceButton.getAttachment().updateState(Button.State.NORMAL);
            if (referenceField.getSection() != null) onPrimaryButtonReleased(referenceField);
        }
        return referenceField;
    }

    private SimplePrimary<TitledButton<?>> createOnOffField(int primaryKey, String title, BooleanProcessor processor, BooleanSupplier tester) {
        TitledButton<OptionsWindowController> onOffButton =
                new TitledButton<>(
                        title,
                        ResourceManager.getInstance().onoffTextureRegion,
                        Button.ButtonType.Selector,
                        5f);
        SimplePrimary<TitledButton<?>> onOffField =
                new SimplePrimary<>(primaryKey, onOffButton);
        onOffButton
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, onOffButton.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                processor.process(true);
                                onPrimaryButtonClicked(onOffField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                processor.process(false);
                                onPrimaryButtonReleased(onOffField);
                            }
                        });

        if (tester.getAsBoolean()) {
            onOffButton.getAttachment().updateState(Button.State.PRESSED);
            if (onOffField.getSection() != null) onPrimaryButtonClicked(onOffField);
        } else {
            onOffButton.getAttachment().updateState(Button.State.NORMAL);
            if (onOffField.getSection() != null) onPrimaryButtonReleased(onOffField);
        }
        return onOffField;
    }

    private SimplePrimary<TitledButton<?>> createMoveLimitsOption(int primaryKey) {
        TitledButton<OptionsWindowController> button =
                new TitledButton<>(
                        "Limits :",
                        ResourceManager.getInstance().onoffTextureRegion,
                        Button.ButtonType.Selector,
                        5f);
        SimplePrimary<TitledButton<?>> moveLimitsField = new SimplePrimary<>(primaryKey, button);
        button
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, button.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                creationZoneController.setMoveLimits(true);
                                onPrimaryButtonClicked(moveLimitsField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                creationZoneController.setMoveLimits(false);
                                onPrimaryButtonReleased(moveLimitsField);
                            }
                        });

        if (creationZoneController.getMoveLimits()) {
            button.getAttachment().updateState(Button.State.PRESSED);
        } else {
            button.getAttachment().updateState(Button.State.NORMAL);
        }
        return moveLimitsField;
    }

    private SimplePrimary<TitledButton<?>> createCongruenceOption(int primaryKey) {
        TitledButton<OptionsWindowController> button =
                new TitledButton<>(
                        "Congruent Anchors :",
                        ResourceManager.getInstance().onoffTextureRegion,
                        Button.ButtonType.Selector,
                        5f);
        SimplePrimary<TitledButton<?>> congruenceField = new SimplePrimary<>(primaryKey, button);
        button
                .getAttachment()
                .setBehavior(
                        new ButtonBehavior<OptionsWindowController>(this, button.getAttachment()) {
                            @Override
                            public void informControllerButtonClicked() {
                                creationZoneController.setCongruentAnchors(true);
                                onPrimaryButtonClicked(congruenceField);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                creationZoneController.setCongruentAnchors(false);
                                onPrimaryButtonReleased(congruenceField);
                            }
                        });

        if (creationZoneController.getCongruentAnchors()) {
            button.getAttachment().updateState(Button.State.PRESSED);
        } else {
            button.getAttachment().updateState(Button.State.NORMAL);
        }
        return congruenceField;
    }

    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    public void onUpdatedImageDimensions(ImageShape imageShape) {
        if(imageShape==null){
            return;
        }
        float x = imageShape.getX();
        float y = imageShape.getY();
        float rot = imageShape.getRotation();
        float width = imageShape.getWidth();
        float height = imageShape.getHeight();
        String text = "";
        if(settingsType==SettingsType.SCALE_IMAGE_SETTINGS) {
            text = String.format("Width=%.2f, Height=%.2f", width, height);
        }
        if(settingsType==SettingsType.ROTATE_IMAGE_SETTINGS) {
            text = String.format("Rot=%.2f",rot);
        }
        if(settingsType==SettingsType.MOVE_IMAGE_SETTINGS) {
            text = String.format("(X=%.2f, Y=%.2f)", x, y);
        }
        if(dimensionsText!=null) {
            dimensionsText.updateText(text);
        }
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL,
        OTHER
    }

    @FunctionalInterface
    interface BooleanProcessor {
        void process(boolean b);
    }
}
