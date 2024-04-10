package com.evolgames.userinterface.view;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.properties.SquareProperties;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.control.CreationAction;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.behaviors.actions.ConfirmableAction;
import com.evolgames.userinterface.control.buttonboardcontrollers.DrawButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.ImageButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.JointButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.MainButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.ToolButtonBoardController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BombOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.CasingOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ConfirmWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DragOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.FireSourceOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LiquidSourceOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.ImageShapeModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.SpecialPointModel;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ColorSelector;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.controllers.ControlElement;
import com.evolgames.userinterface.view.inputs.controllers.ControlPanel;
import com.evolgames.userinterface.view.layouts.ButtonBoard;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.shapes.CreationZone;
import com.evolgames.userinterface.view.shapes.Grid;
import com.evolgames.userinterface.view.shapes.ImageShape;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.DragShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.SpecialPointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.DistanceJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.PrismaticJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.RevoluteJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.WeldJointShape;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;
import com.evolgames.userinterface.view.windows.gamewindows.AmmoOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.BodySettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.BombOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ColorSelectorWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ConfirmWindow;
import com.evolgames.userinterface.view.windows.gamewindows.DecorationSettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.DragOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.FireSourceOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ItemSaveWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.gamewindows.JointOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.JointsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.LayerSettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.LayersWindow;
import com.evolgames.userinterface.view.windows.gamewindows.LiquidSourceOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.OptionsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ProjectileOptionWindow;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.color.Color;

import java.io.FileNotFoundException;
import java.util.Comparator;

import javax.xml.transform.TransformerException;

public class EditorUserInterface extends UserInterface<EditorScene> {

    private final ControlPanel panel;
    private final CreationZone creationZone;
    // controllers
    private final ConfirmWindowController confirmWindowController;
    private final JointWindowController jointsWindowController;
    private final OutlineController outlineController;
    private final OptionsWindowController optionsWindowController;
    private final LayerWindowController layersWindowController;
    private final ToolButtonBoardController toolButtonBoardController;
    private final ItemWindowController itemWindowController;
    private final DrawButtonBoardController drawButtonBoardController;
    private final JointButtonBoardController jointButtonBoardController;
    private final ProjectileOptionController projectileOptionController;
    private final ImageButtonBoardController imageButtonBoardController;
    private final JointSettingsWindowController jointSettingsWindowController;
    private final CreationZoneController creationZoneController;
    private final ItemSaveWindowController itemSaveWindowController;
    private final ColorSelectorWindowController colorSelectorWindowController;
    private final ButtonBoard mainButtonBoard;
    private final MainButtonBoardController mainButtonBoardController;
    private ControlElement moveElementController;
    private ImageShape imageShape;
    private ToolModel toolModel;
    private boolean interactionLocked;
    private Screen selectedScreen;

    public EditorUserInterface(
            EditorScene editorScene,
            LayerWindowController layerWindowController,
            JointWindowController jointWindowController,
            LayerSettingsWindowController layerSettingsController,
            BodySettingsWindowController bodySettingsWindowController,
            JointSettingsWindowController jointSettingsWindowController,
            ItemWindowController itemWindowController,
            ProjectileOptionController projectileOptionController,
            CasingOptionController casingOptionController,
            BombOptionController bombOptionController,
            FireSourceOptionController fireSourceOptionController,
            LiquidSourceOptionController liquidSourceOptionController, DragOptionController dragOptionController,
            ItemSaveWindowController itemSaveWindowController,
            DecorationSettingsWindowController decorationSettingsWindowController,
            OptionsWindowController optionsWindowController,
            OutlineController outlineController,
            CreationZoneController creationZoneController,
            KeyboardController keyboardController) {
        super(editorScene);

        ResourceManager.getInstance().hudBatcher.setZIndex(10000000);
        ResourceManager.getInstance().sceneBatcher.setZIndex(1000000);


        Grid grid = new Grid(editorScene);

        this.jointSettingsWindowController = jointSettingsWindowController;
        this.layersWindowController = layerWindowController;
        this.itemSaveWindowController = itemSaveWindowController;
        this.optionsWindowController = optionsWindowController;
        this.jointsWindowController = jointWindowController;
        this.outlineController = outlineController;
        this.itemWindowController = itemWindowController;
        this.projectileOptionController = projectileOptionController;
        this.creationZoneController = creationZoneController;
        liquidSourceOptionController.setUserInterface(this);

        creationZone = new CreationZone(creationZoneController);
        creationZoneController.setCreationZone(creationZone);

        Keyboard keyboard = new Keyboard(0, 0, keyboardController);
        keyboardController.setKeyboard(keyboard);
        addElement(keyboard);
        keyboard.setDepth(2);

        // windows
        LayersWindow layersWindow = new LayersWindow(0, 0, layerWindowController);
        layersWindow.setPosition(800 - layersWindow.getWidth() - 12, 480 - layersWindow.getHeight());
        addElement(layersWindow);

        ItemWindow itemWindow = new ItemWindow(0, 0, itemWindowController);
        itemWindow.setPosition(800 - itemWindow.getWidth() - 12, 480 - itemWindow.getHeight());
        addElement(itemWindow);
        itemWindow.setVisible(false);

        LayerSettingsWindow layerSettingsWindow =
                new LayerSettingsWindow(0, 0, layerSettingsController);
        addElement(layerSettingsWindow);
        layerSettingsWindow.setPosition(
                800 - layerSettingsWindow.getWidth() - 12, 480 - layerSettingsWindow.getHeight());

        layerWindowController.setLayerSettingsWindowController(layerSettingsController);

        BodySettingsWindow bodySettingsWindow =
                new BodySettingsWindow(0, 0, bodySettingsWindowController);
        addElement(bodySettingsWindow);
        bodySettingsWindow.setPosition(
                800 - bodySettingsWindow.getWidth() - 12, 480 - bodySettingsWindow.getHeight());

        layerWindowController.setBodySettingsWindowController(bodySettingsWindowController);

        layerWindowController.setDecorationSettingsWindowController(decorationSettingsWindowController);

        DecorationSettingsWindow decorationSettingsWindow =
                new DecorationSettingsWindow(0, 0, decorationSettingsWindowController);
        addElement(decorationSettingsWindow);
        decorationSettingsWindow.setPosition(
                800 - layerSettingsWindow.getWidth() - 12, 480 - decorationSettingsWindow.getHeight());

        decorationSettingsWindow.setVisible(false);

        JointsWindow jointsWindow = new JointsWindow(0, 0, jointWindowController);
        jointsWindow.setPosition(800 - jointsWindow.getWidth(), 480 - jointsWindow.getHeight());
        addElement(jointsWindow);
        jointsWindow.setVisible(false);

        OptionsWindow optionsWindow = new OptionsWindow(0, 0, optionsWindowController);
        addElement(optionsWindow);

        JointOptionWindow jointOptionWindow =
                new JointOptionWindow(0, 0, jointSettingsWindowController);
        jointOptionWindow.setPosition(
                800 - jointOptionWindow.getWidth() - 12, 480 - jointOptionWindow.getHeight());
        jointOptionWindow.setVisible(false);
        addElement(jointOptionWindow);

        ProjectileOptionWindow projectileOptionWindow =
                new ProjectileOptionWindow(0, 0, projectileOptionController);
        projectileOptionWindow.setPosition(
                800 - projectileOptionWindow.getWidth() - 12,
                480 - projectileOptionWindow.getHeight() - 32);
        addElement(projectileOptionWindow);

        AmmoOptionWindow ammoOptionWindow = new AmmoOptionWindow(0, 0, casingOptionController);
        ammoOptionWindow.setPosition(
                800 - ammoOptionWindow.getWidth() - 12, 480 - ammoOptionWindow.getHeight() - 32);
        addElement(ammoOptionWindow);

        BombOptionWindow bombOptionWindow = new BombOptionWindow(0, 0, bombOptionController);
        bombOptionWindow.setPosition(
                800 - bombOptionWindow.getWidth() - 12, 480 - bombOptionWindow.getHeight() - 32);
        addElement(bombOptionWindow);

        FireSourceOptionWindow fireSourceOptionWindow =
                new FireSourceOptionWindow(0, 0, fireSourceOptionController);
        fireSourceOptionWindow.setPosition(
                800 - fireSourceOptionWindow.getWidth() - 12,
                480 - fireSourceOptionWindow.getHeight() - 32);
        addElement(fireSourceOptionWindow);


        LiquidSourceOptionWindow liquidSourceOptionWindow =
                new LiquidSourceOptionWindow(0, 0, liquidSourceOptionController);
        liquidSourceOptionWindow.setPosition(
                800 - liquidSourceOptionWindow.getWidth() - 12,
                480 - liquidSourceOptionWindow.getHeight() - 32);
        addElement(liquidSourceOptionWindow);

        DragOptionWindow dragOptionWindow = new DragOptionWindow(0, 0, dragOptionController);
        dragOptionWindow.setPosition(
                800 - dragOptionWindow.getWidth() - 12, 480 - dragOptionWindow.getHeight() - 32);
        addElement(dragOptionWindow);

        ItemSaveWindow itemSaveWindow = new ItemSaveWindow(0, 0, itemSaveWindowController);
        itemSaveWindow.setPosition(
                400 - itemSaveWindow.getWidth() / 2f, 240);
        itemSaveWindow.setVisible(false);
        addElement(itemSaveWindow);

        colorSelectorWindowController =
                new ColorSelectorWindowController(this);
        ColorSelectorWindow colorSelector =
                new ColorSelectorWindow(0, 0, colorSelectorWindowController);
        colorSelector.setLowerBottomX(400 - colorSelector.getWidth());
        colorSelector.setLowerBottomY(240);
        colorSelector.setVisible(false);
        addElement(colorSelector);
        ColorSelector selector = colorSelector.getSelector();
        selector
                .getMesh()
                .setPosition(selector.getAbsoluteX() + 103, selector.getAbsoluteY() - 25 + 128);

        layerSettingsController.setColorSelectorController(colorSelectorWindowController);
        decorationSettingsWindowController.setColorSelectorController(colorSelectorWindowController);

        ResourceManager.getInstance().hudBatcher.attachChild(colorSelector.getSelector().getMesh());


        mainButtonBoard = new ButtonBoard(0, 460, LinearLayout.Direction.Vertical, 0);
        mainButtonBoardController = new MainButtonBoardController(mainButtonBoard, this);
        addElement(mainButtonBoard);
        Button<MainButtonBoardController> button10 =
                new Button<>(ResourceManager.getInstance().drawBigButton, Button.ButtonType.Selector, true);
        button10.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button10) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onDrawOptionClicked(button10);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onDrawOptionReleased(button10);
                    }
                });

        Button<MainButtonBoardController> button11 =
                new Button<>(
                        ResourceManager.getInstance().jointBigButton, Button.ButtonType.Selector, true);
        button11.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button11) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onJointOptionClicked(button11);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onJointOptionReleased(button11);
                    }
                });

        Button<MainButtonBoardController> button12 =
                new Button<>(
                        ResourceManager.getInstance().collisionBigButton, Button.ButtonType.Selector, true);
        button12.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button12) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onToolOptionClicked(button12);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onToolOptionReleased(button12);
                    }
                });

        Button<MainButtonBoardController> button13 =
                new Button<>(
                        ResourceManager.getInstance().imageBigButton, Button.ButtonType.Selector, true);
        button13.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button13) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onImageOptionClicked(button13);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onImageOptionReleased(button13);
                    }
                });

        Button<MainButtonBoardController> button14 =
                new Button<>(ResourceManager.getInstance().saveBigButton, Button.ButtonType.Selector, true);
        button14.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button14) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onSaveOptionClicked(button14);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onSaveOptionReleased(button14);
                    }
                });

        Button<MainButtonBoardController> button15 =
                new Button<>(ResourceManager.getInstance().homeBigButton, Button.ButtonType.OneClick, true);
        button15.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button15) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onHomeButtonClicked(button15);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onHomeButtonReleased(button15);
                    }
                });

        Button<MainButtonBoardController> button16 =
                new Button<>(ResourceManager.getInstance().helpBigButton, Button.ButtonType.OneClick, true);
        button16.setBehavior(
                new ButtonBehavior<MainButtonBoardController>(mainButtonBoardController, button16) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onHelpButtonClicked(button16);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onHelpButtonReleased(button16);
                    }
                });
        mainButtonBoard.addToButtonBoard(button10);
        mainButtonBoard.addToButtonBoard(button11);
        mainButtonBoard.addToButtonBoard(button12);
        mainButtonBoard.addToButtonBoard(button13);
        mainButtonBoard.addToButtonBoard(button14);
        mainButtonBoard.addToButtonBoard(button15);
        mainButtonBoard.addToButtonBoard(button16);
        // boards
        ButtonBoard drawButtonBoard =
                new ButtonBoard(160, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        drawButtonBoardController =
                new DrawButtonBoardController(
                        drawButtonBoard, creationZoneController, optionsWindowController);

        addElement(drawButtonBoard);
        Button<DrawButtonBoardController> button00 =
                new Button<>(
                        ResourceManager.getInstance().addPolygonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button00.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button00) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onPolygonCreationButtonClicked(button00);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onPolygonCreationButtonReleased(button00);
                    }
                });

        Button<DrawButtonBoardController> button01 =
                new Button<>(
                        ResourceManager.getInstance().addPointTextureRegion, Button.ButtonType.Selector, true);
        button01.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button01) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onAddPointButtonButtonClicked(button01);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onAddPointButtonReleased(button01);
                    }
                });

        Button<DrawButtonBoardController> button02 =
                new Button<>(
                        ResourceManager.getInstance().movePointTextureRegion, Button.ButtonType.Selector, true);
        button02.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button02) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onMovePointButtonClicked(button02);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onMovePointButtonReleased(button02);
                    }
                });

        Button<DrawButtonBoardController> button03 =
                new Button<>(
                        ResourceManager.getInstance().removePointTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button03.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button03) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onRemovePointButtonClicked(button03);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onRemovePointButtonReleased(button03);
                    }
                });

        Button<DrawButtonBoardController> button04 =
                new Button<>(
                        ResourceManager.getInstance().mirrorTextureRegion, Button.ButtonType.Selector, true);
        button04.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button04) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onMirrorButtonClicked(button04);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onMirrorButtonReleased(button04);
                    }
                });

        Button<DrawButtonBoardController> button05 =
                new Button<>(
                        ResourceManager.getInstance().rotateTextureRegion, Button.ButtonType.Selector, true);
        button05.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button05) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onRotateButtonClicked(button05);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onRotateButtonReleased(button05);
                    }
                });

        Button<DrawButtonBoardController> button06 =
                new Button<>(
                        ResourceManager.getInstance().decaleTextureRegion, Button.ButtonType.Selector, true);
        button06.setBehavior(
                new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button06) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onShiftButtonClicked(button06);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onShiftButtonReleased(button06);
                    }
                });

        drawButtonBoard.addToButtonBoard(button00);
        drawButtonBoard.addToButtonBoard(button01);
        drawButtonBoard.addToButtonBoard(button02);
        drawButtonBoard.addToButtonBoard(button03);
        drawButtonBoard.addToButtonBoard(button04);
        drawButtonBoard.addToButtonBoard(button05);
        drawButtonBoard.addToButtonBoard(button06);

        ButtonBoard jointButtonBoard =
                new ButtonBoard(200, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        jointButtonBoard.setVisible(false);
        jointButtonBoardController =
                new JointButtonBoardController(
                        jointButtonBoard, creationZoneController, optionsWindowController);
        jointButtonBoardController.setActive(true);
        addElement(jointButtonBoard);
        Button<JointButtonBoardController> button20 =
                new Button<>(
                        ResourceManager.getInstance().revoluteTextureRegion, Button.ButtonType.Selector, true);
        button20.setBehavior(
                new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button20) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onRevoluteButtonClicked(button20);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onRevoluteButtonReleased(button20);
                    }
                });

        Button<JointButtonBoardController> button21 =
                new Button<>(
                        ResourceManager.getInstance().weldTextureRegion, Button.ButtonType.Selector, true);
        button21.setBehavior(
                new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button21) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onWeldButtonClicked(button21);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onWeldButtonReleased(button21);
                    }
                });

        Button<JointButtonBoardController> button22 =
                new Button<>(
                        ResourceManager.getInstance().distanceTextureRegion, Button.ButtonType.Selector, true);
        button22.setBehavior(
                new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button22) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onDistanceButtonClicked(button22);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onDistanceButtonReleased(button22);
                    }
                });

        Button<JointButtonBoardController> button23 =
                new Button<>(
                        ResourceManager.getInstance().prismaticTextureRegion, Button.ButtonType.Selector, true);
        button23.setBehavior(
                new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button23) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onPrismaticButtonClicked(button23);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onPrismaticButtonReleased(button23);
                    }
                });

        Button<JointButtonBoardController> button24 =
                new Button<>(
                        ResourceManager.getInstance().moveJointTextureRegion, Button.ButtonType.Selector, true);
        button24.setBehavior(
                new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button24) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onMoveButtonClicked(button24);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onMoveButtonReleased(button24);
                    }
                });

        jointButtonBoard.addToButtonBoard(button20);
        jointButtonBoard.addToButtonBoard(button21);
        jointButtonBoard.addToButtonBoard(button22);
        jointButtonBoard.addToButtonBoard(button23);
        jointButtonBoard.addToButtonBoard(button24);

        ButtonBoard imageButtonBoard =
                new ButtonBoard(0, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        imageButtonBoard.setVisible(false);

        imageButtonBoardController = new ImageButtonBoardController(imageButtonBoard, this);

        addElement(imageButtonBoard);

        Button<ImageButtonBoardController> button30 =
                new Button<>(
                        ResourceManager.getInstance().moveImageButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button30.setBehavior(
                new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button30) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onMoveImageButtonClicked(button30);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onMoveImageButtonReleased(button30);
                    }
                });

        Button<ImageButtonBoardController> button31 =
                new Button<>(
                        ResourceManager.getInstance().rotateImageButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button31.setBehavior(
                new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button31) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onRotateImageButtonClicked(button31);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onRotateImageButtonReleased(button31);
                    }
                });

        Button<ImageButtonBoardController> button32 =
                new Button<>(
                        ResourceManager.getInstance().scaleButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button32.setBehavior(
                new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button32) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onScaleImageButtonClicked(button32);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onScaleImageButtonReleased(button32);
                    }
                });

        Button<ImageButtonBoardController> button33 =
                new Button<>(
                        ResourceManager.getInstance().pipeButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button33.setBehavior(
                new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button33) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onPipeButtonClicked(button33);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onPipeButtonReleased(button33);
                    }
                });

        imageButtonBoard.addToButtonBoard(button30);
        imageButtonBoard.addToButtonBoard(button31);
        imageButtonBoard.addToButtonBoard(button32);
        imageButtonBoard.addToButtonBoard(button33);
        imageButtonBoardController.setActive(false);
        imageButtonBoard.setLowerBottomX(400 - imageButtonBoard.getWidth() / 2);

        ButtonBoard toolButtonBoard =
                new ButtonBoard(0, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        toolButtonBoard.setVisible(false);
        toolButtonBoardController =
                new ToolButtonBoardController(
                        toolButtonBoard, creationZoneController, optionsWindowController);
        addElement(toolButtonBoard);

        Button<ToolButtonBoardController> button40 =
                new Button<>(
                        ResourceManager.getInstance().targetButtonTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button40.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button40) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onTargetButtonClicked(button40);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onTargetButtonReleased(button40);
                    }
                });

        Button<ToolButtonBoardController> button41 =
                new Button<>(
                        ResourceManager.getInstance().ammoTextureRegion, Button.ButtonType.Selector, true);
        button41.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button41) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onAmmoButtonClicked(button41);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onAmmoButtonReleased(button41);
                    }
                });

        Button<ToolButtonBoardController> button42 =
                new Button<>(
                        ResourceManager.getInstance().bombTextureRegion, Button.ButtonType.Selector, true);
        button42.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button42) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onBombButtonClicked(button42);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onBombButtonReleased(button42);
                    }
                });

        Button<ToolButtonBoardController> button44 =
                new Button<>(
                        ResourceManager.getInstance().movePointTextureRegion, Button.ButtonType.Selector, true);
        button44.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button44) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onMovePointButtonClicked(button44);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onMovePointButtonReleased(button44);
                    }
                });

        Button<ToolButtonBoardController> button43 =
                new Button<>(
                        ResourceManager.getInstance().fireSourceTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button43.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button43) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onFireSourceButtonClicked(button43);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onFireSourceButtonReleased(button43);
                    }
                });

        Button<ToolButtonBoardController> button45 =
                new Button<>(
                        ResourceManager.getInstance().projDragTextureRegion, Button.ButtonType.Selector, true);
        button45.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button45) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onDragButtonClicked(button45);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onDragButtonReleased(button45);
                    }
                });
        Button<ToolButtonBoardController> button46 =
                new Button<>(
                        ResourceManager.getInstance().liquidSourceTextureRegion,
                        Button.ButtonType.Selector,
                        true);
        button46.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button46) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onLiquidButtonClicked(button46);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onLiquidButtonReleased(button46);
                    }
                });

        Button<ToolButtonBoardController> button47 =
                new Button<>(
                        ResourceManager.getInstance().specialPointTextureRegion, Button.ButtonType.Selector, true);
        button47.setBehavior(
                new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button47) {
                    @Override
                    public void informControllerButtonClicked() {
                        getController().onSpecialPointClicked(button47);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        getController().onSpecialPointReleased(button47);
                    }
                });
        toolButtonBoard.addToButtonBoard(button40);
        toolButtonBoard.addToButtonBoard(button41);
        toolButtonBoard.addToButtonBoard(button42);
        toolButtonBoard.addToButtonBoard(button43);
        toolButtonBoard.addToButtonBoard(button46);
        toolButtonBoard.addToButtonBoard(button45);
        toolButtonBoard.addToButtonBoard(button47);
        toolButtonBoard.addToButtonBoard(button44);
        toolButtonBoard.setLowerBottomX(320 - toolButtonBoard.getWidth() / 2);

        panel = new ControlPanel(editorScene);

        confirmWindowController = new ConfirmWindowController(this);
        ConfirmWindow confirmWindow = new ConfirmWindow(400, 240, 1, confirmWindowController);
        addElement(confirmWindow);
        setUpdated(true);
    }

    public Screen getSelectedScreen() {
        return selectedScreen;
    }

    public void doWithConfirm(String prompt, Action action) {
        confirmWindowController.bindAction(new ConfirmableAction(prompt, action));
    }

    public ToolModel getToolModel() {
        return toolModel;
    }

    public void bindToolModel(ToolModel toolModel) {
        if (this.toolModel != null) {
            for (BodyModel bodyModel : this.toolModel.getBodies()) {
                for (LayerModel layerModel : bodyModel.getLayers()) {
                    removeElement(layerModel.getPointsShape());
                    for (PointsShape p : layerModel.getPointsShapes()) {
                        removeElement(p);
                    }
                }
                for (JointModel jointModel : this.toolModel.getJoints()) {
                    jointModel.getJointShape().detach();
                }
            }
        }
        this.itemSaveWindowController.onModelUpdated(toolModel);
        this.jointSettingsWindowController.setToolModel(toolModel);
        this.colorSelectorWindowController.setColorPanelProperties(toolModel.getColorPanelProperties());
        this.toolModel = toolModel;

        for (BodyModel bodyModel : toolModel.getBodies()) {
            for (LayerModel layerModel : bodyModel.getLayers()) {
                if (layerModel.getPointsShape() == null) {
                    PointsShape pointsShape = new PointsShape(this);
                    layerModel.setPointsShape(pointsShape);
                }
                layerModel.getPointsShape().updateOutlineShape();
                layerModel.getReferencePoints().forEach(refPoint -> layerModel.getPointsShape().createReferencePointImage(refPoint));
                addElement(layerModel.getPointsShape());
                for (DecorationModel decorationModel : layerModel.getDecorations()) {
                    if (decorationModel.getPointsShape() == null) {
                        PointsShape pointsShape = new PointsShape(this);
                        decorationModel.setPointsShape(pointsShape);
                    }
                    decorationModel.getPointsShape().updateOutlineShape();
                    addElement(decorationModel.getPointsShape());
                }
            }
            for (ProjectileModel projectileModel : bodyModel.getProjectileModels()) {
                ProjectileShape projectileShape =
                        new ProjectileShape(projectileModel.getProperties().getProjectileOrigin(), scene);
                projectileShape.bindModel(projectileModel);
            }

            for (CasingModel casingModel : bodyModel.getCasingModels()) {
                CasingShape casingShape =
                        new CasingShape(casingModel.getProperties().getAmmoOrigin(), scene);
                casingShape.bindModel(casingModel);
            }

            for (BombModel bombModel : bodyModel.getBombModels()) {
                BombShape bombShape = new BombShape(bombModel.getProperties().getBombPosition(), scene);
                bombShape.bindModel(bombModel);
            }
            for (SpecialPointModel specialPointModel : bodyModel.getSpecialPointModels()) {
                SpecialPointShape specialPointShape = new SpecialPointShape(specialPointModel.getProperties().getPosition(), scene);
                specialPointShape.bindModel(specialPointModel);
            }
            for (FireSourceModel fireSourceModel : bodyModel.getFireSourceModels()) {
                FireSourceShape fireSourceShape =
                        new FireSourceShape(fireSourceModel.getProperties().getFireSourceOrigin(), scene);
                fireSourceShape.bindModel(fireSourceModel);
            }
            for (LiquidSourceModel liquidSourceModel : bodyModel.getLiquidSourceModels()) {
                LiquidSourceShape liquidSourceShape =
                        new LiquidSourceShape(liquidSourceModel.getProperties().getLiquidSourceOrigin(), scene);
                liquidSourceShape.bindModel(liquidSourceModel);
            }

            for (DragModel dragModel : bodyModel.getDragModels()) {
                DragShape dragShape = new DragShape(dragModel.getProperties().getDragOrigin(), scene);
                dragShape.bindModel(dragModel);
            }
        }
        for (JointModel jointModel : toolModel.getJoints()) {
            Vector2 begin = jointModel.getProperties().getLocalAnchorA();
            switch (jointModel.getProperties().getJointType()) {
                case WeldJoint:
                    WeldJointShape weldJointShape = new WeldJointShape(scene, begin);
                    weldJointShape.bindModel(jointModel);
                    break;
                case RevoluteJoint:
                    RevoluteJointShape revoluteJointShape = new RevoluteJointShape(scene, begin);
                    revoluteJointShape.bindModel(jointModel);
                    break;
                case PrismaticJoint:
                    PrismaticJointShape prismaticJointShape = new PrismaticJointShape(scene, begin);
                    prismaticJointShape.bindModel(jointModel);
                    break;
                case DistanceJoint:
                    DistanceJointShape distanceJointShape = new DistanceJointShape(scene, begin);
                    distanceJointShape.bindModel(jointModel);
                    break;
                case PulleyJoint:
                case MouseJoint:
                case GearJoint:
                case LineJoint:
                case FrictionJoint:
                    break;
            }
        }
        toolModel.updateMesh();
        for (SquareProperties squareProperties : toolModel.getColorPanelProperties().getSquarePropertiesList()) {
            colorSelectorWindowController.addColorToPanel(squareProperties.getColor(), squareProperties.getSquareId(), false);
        }
        Integer maxId = toolModel.getColorPanelProperties().getSquarePropertiesList().stream().map(SquareProperties::getSquareId).max(Comparator.naturalOrder()).orElse(0);
        colorSelectorWindowController.getColorSlotCounter().set(maxId + 1);
        layersWindowController.init();
        itemWindowController.init();
        jointsWindowController.init();
        // Outline call needs to be called last as it sets the outline
        outlineController.init();
        itemSaveWindowController.onModelUpdated(toolModel);
        addImage();
    }

    public CreationZoneController getCreationZoneController() {
        return creationZoneController;
    }

    @Override
    public void onTouchScene(TouchEvent pTouchEvent) {
        if (interactionLocked) {
            return;
        }
        creationZone.onTouchScene(pTouchEvent);
        if (creationZoneController.getAction() == CreationAction.PIPING
                && imageShape != null) {
            Color color = imageShape.getColorAt(pTouchEvent.getX(), pTouchEvent.getY(), (int) (10 / getZoomFactor()));
            if (color != null) {
                optionsWindowController.updatePipeColor(color);
            }
        }
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        if (!interactionLocked) {
            return super.onTouchHud(pTouchEvent);
        }
        return false;
    }

    public EditorScene getScene() {
        return scene;
    }

    public JointSettingsWindowController getJointSettingsWindowController() {
        return jointSettingsWindowController;
    }

    public ItemSaveWindowController getItemSaveWindowController() {
        return itemSaveWindowController;
    }

    public JointWindowController getJointsWindowController() {
        return jointsWindowController;
    }

    public void updateOptionsWindow(SettingsType settingsType) {
        optionsWindowController.selectSettingsType(settingsType);
    }

    public OptionsWindowController getOptionsWindowController() {
        return optionsWindowController;
    }

    public void onAddImageButtonClicked() {
        ResourceManager.getInstance().activity.requestImagePermission();
    }

    public void onMirrorImageButtonClicked() {
        ResourceManager.getInstance().mirrorSketch();
        if (imageShape != null) {
            imageShape.updateSprite();
            ImageShapeModel model = toolModel.getImageShapeModel();
            imageShape.updateWidth(model.getWidth());
            imageShape.updateHeight(model.getHeight());
            imageShape.updatePosition(model.getX(), model.getY());
            imageShape.updateRotation(model.getRotation());
            getOptionsWindowController().onUpdatedImageDimensions(imageShape);
        }
    }

    public void addImage() {
        TextureRegion region = ResourceManager.getInstance().sketchTextureRegion;
        if (region != null) {
            if (toolModel.getImageShapeModel() == null) {
                toolModel.setImageShapeModel(new ImageShapeModel(0, 400, 240, region.getWidth(), region.getHeight()));
            }
            imageShape = new ImageShape(scene, toolModel.getImageShapeModel());
            addElement(imageShape);
            imageButtonBoardController.setActive(true);
            ImageShapeModel model = toolModel.getImageShapeModel();
            imageShape.updateWidth(model.getWidth());
            imageShape.updateHeight(model.getHeight());
            imageShape.updatePosition(model.getX(), model.getY());
            imageShape.updateRotation(model.getRotation());
            imageShape.updateSelf();
            this.optionsWindowController.onUpdatedImageDimensions(imageShape);
        }
    }

    public ImageShape getImageShape() {
        return imageShape;
    }

    public ControlPanel getPanel() {
        return panel;
    }

    public void onActionChanged(CreationAction action) {
        if (panel != null) {
            switch (action) {
                case ADD_POINT:
                case REMOVE_POINT:
                case NONE:
                case MIRROR:
                case ROTATE:
                case ADD_POLYGON:
                case SHIFT:
                case PRISMATIC:
                case REVOLUTE:
                case WELD:
                case MOVE_IMAGE:
                case DISTANCE:
                case SCALE_IMAGE:
                case ROTATE_IMAGE:
                    if (this.moveElementController != null) {
                        getPanel().hideControlElement(moveElementController.getID());
                    }
                    break;
                case MOVE_TOOL_POINT:
                case MOVE_POINT:
                case MOVE_JOINT_POINT:
                    break;
            }
        }
    }

    public void hideMoveElementController() {
        getPanel().hideControlElement(moveElementController.getID());
    }

    public void addReferencePoint(ReferencePointImage centerPointImage) {
        addElement(centerPointImage);
        creationZone.getReferencePointImageArrayList().add(centerPointImage);
    }

    public void detachReference(ReferencePointImage centerPointImage) {
        removeElement(centerPointImage);
        creationZone.getReferencePointImageArrayList().remove(centerPointImage);
    }

    public ItemWindowController getItemWindowController() {
        return itemWindowController;
    }

    public DrawButtonBoardController getDrawButtonBoardController() {
        return drawButtonBoardController;
    }

    public JointButtonBoardController getJointButtonBoardController() {
        return jointButtonBoardController;
    }

    public ImageButtonBoardController getImageButtonBoardController() {
        return imageButtonBoardController;
    }

    public ToolButtonBoardController getItemButtonBoardController() {
        return toolButtonBoardController;
    }

    public LayerWindowController getLayersWindowController() {
        return layersWindowController;
    }

    public void changeSelectedScreen(Screen selectedScreen) {
        this.selectedScreen = selectedScreen;
        outlineController.onScreenChanged(selectedScreen);
    }

    public void setMoveElementController(ControlElement moveElementController) {
        this.moveElementController = moveElementController;
    }

    public void saveToolModel() {
        try {
            PersistenceCaretaker.getInstance().saveToolModel(this.toolModel);
        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public ColorSelectorWindowController getColorSelectorWindowController() {
        return colorSelectorWindowController;
    }

    public void lockInteraction() {
        this.interactionLocked = true;
    }

    public void unlockInteraction() {
        this.interactionLocked = false;
    }

    public void setBoardsState(Screen screen, CreationAction creationAction) {
        if (screen != Screen.NONE) {
            mainButtonBoard.getButtonAtIndex(screen.ordinal()).updateState(Button.State.PRESSED);

            switch (screen) {
                case DRAW_SCREEN:
                    mainButtonBoardController.onDrawOptionClicked((Button<MainButtonBoardController>) mainButtonBoard.getButtonAtIndex(screen.ordinal()));
                    break;
                case JOINTS_SCREEN:
                    mainButtonBoardController.onJointOptionClicked((Button<MainButtonBoardController>) mainButtonBoard.getButtonAtIndex(screen.ordinal()));
                    break;
                case ITEMS_SCREEN:
                    mainButtonBoardController.onToolOptionClicked((Button<MainButtonBoardController>) mainButtonBoard.getButtonAtIndex(screen.ordinal()));
                    break;
                case IMAGE_SCREEN:
                    mainButtonBoardController.onImageOptionClicked((Button<MainButtonBoardController>) mainButtonBoard.getButtonAtIndex(screen.ordinal()));
                    break;
                case SAVE_SCREEN:
                    mainButtonBoardController.onSaveOptionClicked((Button<MainButtonBoardController>) mainButtonBoard.getButtonAtIndex(screen.ordinal()));
                    break;
            }
        }
    }

}
