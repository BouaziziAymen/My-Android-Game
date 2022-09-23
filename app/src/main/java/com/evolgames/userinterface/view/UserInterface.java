package com.evolgames.userinterface.view;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.buttonboardcontrollers.DrawButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.ImageButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.JointButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.MainButtonBoardController;
import com.evolgames.userinterface.control.buttonboardcontrollers.ToolButtonBoardController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ColorSelector;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.Touchable;
import com.evolgames.userinterface.view.inputs.controllers.ControlElement;
import com.evolgames.userinterface.view.inputs.controllers.ControlPanel;
import com.evolgames.userinterface.view.inputs.controllers.ControllerAction;
import com.evolgames.userinterface.view.inputs.controllers.MyAnalogOnScreenControl;
import com.evolgames.userinterface.view.layouts.ButtonBoard;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.shapes.CreationZone;
import com.evolgames.userinterface.view.shapes.Grid;
import com.evolgames.userinterface.view.shapes.ImageShape;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;
import com.evolgames.userinterface.view.visitor.ContentTraverser;
import com.evolgames.userinterface.view.visitor.IsUpdatedVisitBehavior;
import com.evolgames.userinterface.view.visitor.StepVisitBehavior;
import com.evolgames.userinterface.view.visitor.TouchVisitBehavior;
import com.evolgames.userinterface.view.visitor.VisitBehavior;
import com.evolgames.userinterface.view.windows.gamewindows.BodySettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ColorSelectorWindow;
import com.evolgames.userinterface.view.windows.gamewindows.DecorationSettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ItemSaveWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ItemWindow;
import com.evolgames.userinterface.view.windows.gamewindows.JointOptionWindow;
import com.evolgames.userinterface.view.windows.gamewindows.JointsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.LayerSettingsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.LayersWindow;
import com.evolgames.userinterface.view.windows.gamewindows.OptionsWindow;
import com.evolgames.userinterface.view.windows.gamewindows.ProjectileOptionWindow;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

public class UserInterface extends Container implements Touchable {
    public final static SpriteBatch hudBatcher = new SpriteBatch(ResourceManager.getInstance().gameTextureAtlas, 10000, ResourceManager.getInstance().vbom);
    public final static SpriteBatch sceneBatcher = new SpriteBatch(ResourceManager.getInstance().gameTextureAtlas, 10000, ResourceManager.getInstance().vbom);

    private final ButtonBoard drawButtonBoard;
    private final ButtonBoard jointButtonBoard;
    private final ButtonBoard imageButtonBoard;
    private final ButtonBoard toolButtonBoard;
    private final ControlPanel panel;
    private final LayersWindow layersWindow;
    private final JointsWindow jointsWindow;
    private final JointOptionWindow jointOptionWindow;
    private final OptionsWindow optionsWindow;
    private final ItemWindow itemWindow;
    private final ItemSaveWindow itemSaveWindow;
    private final CreationZone creationZone;
    private final ColorSelectorWindow colorSelector;
    private final OptionsWindowController optionsWindowController;
    private final LayerWindowController layersWindowController;
    private final DecorationSettingsWindowController decorationSettingsWindowController;
    private final ToolButtonBoardController toolButtonBoardController;
    private final ItemWindowController itemWindowController;
    private final ProjectileOptionWindow projectileOptionWindow;
    private final DrawButtonBoardController drawButtonBoardController;
    private final JointButtonBoardController jointButtonBoardController;
    private final BodySettingsWindowController bodySettingsWindowController;
    private final ProjectileOptionController projectileOptionController;
    private final ImageButtonBoardController imageButtonBoardController;
    private final LayerSettingsWindowController layerSettingsWindowController;
    private final JointSettingsWindowController jointSettingsWindowController;
    private final CreationZoneController creationZoneController;
    private final ItemSaveWindowController itemSaveWindowController;
    private final GameActivity activity;
    private final Button<DrawButtonBoardController> triggerButton;
    private final ContentTraverser contentTraverser = new ContentTraverser();
    private final StepVisitBehavior updateVisitBehavior = new StepVisitBehavior();
    private final VisitBehavior resetUpdateVisitBehavior = new VisitBehavior() {
        @Override
        protected void visitElement(Element e) {
            e.setUpdated(false);
        }

        @Override
        protected boolean forkCondition(Element e) {
            return true;
        }

        @Override
        protected boolean carryOnCondition() {
            return true;
        }
    };
    private VisitBehavior drawVisitBehavior = new VisitBehavior() {
        @Override
        protected void visitElement(Element e) {

            if (e.isVisible()) {
                e.drawSelf();
            }
        }

        @Override
        protected boolean forkCondition(Element e) {
            return e.isVisible();
        }

        @Override
        protected boolean carryOnCondition() {
            return true;
        }
    };
    private final TouchVisitBehavior hudTouchVisitBehavior = new TouchVisitBehavior();
    private final IsUpdatedVisitBehavior isUpdatedVisitBehavior = new IsUpdatedVisitBehavior();
    private final GameScene scene;
    private ImageShape imageShape;
    private ToolModel toolModel;
    private float zoomFactor = 1f;

    public UserInterface(GameActivity gameActivity, GameScene pGameScene, LayerWindowController layerWindowController, JointWindowController jointWindowController, LayerSettingsWindowController layerSettingsController, BodySettingsWindowController bodySettingsWindowController, JointSettingsWindowController jointSettingsWindowController, ItemWindowController itemWindowController, ProjectileOptionController projectileOptionController, ItemSaveWindowController itemSaveWindowController, DecorationSettingsWindowController decorationSettingsWindowController, OptionsWindowController optionsWindowController, KeyboardController keyboardController) {
        this.activity = gameActivity;
        pGameScene.getHud().attachChild(hudBatcher);
        pGameScene.attachChild(sceneBatcher);


        hudBatcher.setZIndex(1);
        sceneBatcher.setZIndex(1);


        new Grid(pGameScene);
        this.scene = pGameScene;
        this.layerSettingsWindowController = layerSettingsController;
        this.bodySettingsWindowController = bodySettingsWindowController;
        this.jointSettingsWindowController = jointSettingsWindowController;
        this.layersWindowController = layerWindowController;
        this.itemSaveWindowController = itemSaveWindowController;
        this.optionsWindowController = optionsWindowController;


        layerWindowController.setUserInterface(this);
        layerSettingsController.setUserInterface(this);

        Keyboard keyboard = new Keyboard(0, 0, keyboardController);
        keyboardController.setKeyboard(keyboard);
        addElement(keyboard);
        keyboard.setDepth(2);

        layersWindow = new LayersWindow(0, 0, layerWindowController);
        layersWindow.setPosition(800 - layersWindow.getWidth() - 12, 480 - layersWindow.getHeight());
        addElement(layersWindow);

        itemWindow = new ItemWindow(0, 0, itemWindowController);
        itemWindow.setPosition(800 - itemWindow.getWidth() - 12, 480 - itemWindow.getHeight());
        addElement(itemWindow);
        itemWindow.setVisible(false);
        itemWindowController.setUserInterface(this);
        this.itemWindowController = itemWindowController;

        LayerSettingsWindow layerSettingsWindow = new LayerSettingsWindow(0, 0, layerSettingsController);
        addElement(layerSettingsWindow);
        layerSettingsWindow.setPosition(800 - layerSettingsWindow.getWidth() - 12, 480 - layerSettingsWindow.getHeight());


        layerWindowController.setLayerSettingsWindowController(layerSettingsWindowController);


        BodySettingsWindow bodySettingsWindow = new BodySettingsWindow(0, 0, bodySettingsWindowController);
        addElement(bodySettingsWindow);
        bodySettingsWindow.setPosition(800 - bodySettingsWindow.getWidth() - 12, 480 - bodySettingsWindow.getHeight());


        layerWindowController.setBodySettingsWindowController(bodySettingsWindowController);

        this.decorationSettingsWindowController = decorationSettingsWindowController;

        layerWindowController.setDecorationSettingsWindowController(decorationSettingsWindowController);


        DecorationSettingsWindow decorationSettingsWindow = new DecorationSettingsWindow(0, 0, decorationSettingsWindowController);
        addElement(decorationSettingsWindow);
        decorationSettingsWindow.setPosition(800 - layerSettingsWindow.getWidth() - 12, 480 - decorationSettingsWindow.getHeight());

        decorationSettingsWindow.setVisible(false);


        jointsWindow = new JointsWindow(0, 0, jointWindowController);
        jointsWindow.setPosition(800 - jointsWindow.getWidth(), 480 - jointsWindow.getHeight());
        addElement(jointsWindow);
        jointsWindow.setVisible(false);


        optionsWindow = new OptionsWindow(0, 0, optionsWindowController);
        addElement(optionsWindow);


        jointOptionWindow = new JointOptionWindow(0, 0, jointSettingsWindowController);
        jointOptionWindow.setPosition(800 - jointOptionWindow.getWidth() - jointsWindow.getWidth(), 480 - jointOptionWindow.getHeight() - 64);
        jointOptionWindow.setVisible(false);
        addElement(jointOptionWindow);


        projectileOptionWindow = new ProjectileOptionWindow(0, 0, projectileOptionController);
        projectileOptionWindow.setPosition(800 - projectileOptionWindow.getWidth() - itemWindow.getWidth() - 16, 480 - projectileOptionWindow.getHeight());
        addElement(projectileOptionWindow);
        this.projectileOptionController = projectileOptionController;


        itemSaveWindow = new ItemSaveWindow(0, 0, itemSaveWindowController);
        itemSaveWindow.setPosition(800 - itemSaveWindow.getPanel().getWidth(), 480 - itemSaveWindow.getHeight());
        itemSaveWindow.setVisible(false);
        addElement(itemSaveWindow);

        ColorSelectorWindowController colorSelectorWindowController = new ColorSelectorWindowController(this);
        colorSelector = new ColorSelectorWindow(400, 0, colorSelectorWindowController);
        colorSelector.setVisible(false);
        addElement(colorSelector);
        ColorSelector selector = colorSelector.getSelector();
        selector.getMesh().setPosition(selector.getAbsoluteX() + 103, selector.getAbsoluteY() - 25 + 128);

        layerSettingsController.setColorSelectorController(colorSelectorWindowController);
        decorationSettingsWindowController.setColorSelectorController(colorSelectorWindowController);

        creationZoneController = new CreationZoneController(pGameScene, layerWindowController, jointWindowController, optionsWindowController, itemWindowController);
        creationZoneController.setUserInterface(this);
        creationZone = new CreationZone(creationZoneController);
        creationZoneController.setCreationZone(creationZone);
        creationZoneController.setAction(CreationZoneController.CreationAction.NONE);

        hudBatcher.attachChild(colorSelector.getSelector().getMesh());


        ButtonBoard mainButtonBoard = new ButtonBoard(0, 400, LinearLayout.Direction.Vertical, 0);
        MainButtonBoardController controller = new MainButtonBoardController(mainButtonBoard, this);
        addElement(mainButtonBoard);
        Button<MainButtonBoardController> button10 = new Button<>(ResourceManager.getInstance().drawBigButton, Button.ButtonType.Selector, true);
        button10.setBehavior(new ButtonBehavior<MainButtonBoardController>(controller, button10) {
            @Override
            public void informControllerButtonClicked() {
                getController().onDrawOptionClicked(button10);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onDrawOptionReleased(button10);
            }
        });

        Button<MainButtonBoardController> button11 = new Button<>(ResourceManager.getInstance().jointBigButton, Button.ButtonType.Selector, true);
        button11.setBehavior(new ButtonBehavior<MainButtonBoardController>(controller, button11) {
            @Override
            public void informControllerButtonClicked() {
                getController().onJointOptionClicked(button11);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onJointOptionReleased(button11);
            }
        });

        Button<MainButtonBoardController> button12 = new Button<>(ResourceManager.getInstance().collisionBigButton, Button.ButtonType.Selector, true);
        button12.setBehavior(new ButtonBehavior<MainButtonBoardController>(controller, button12) {
            @Override
            public void informControllerButtonClicked() {
                getController().onToolOptionClicked(button12);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onToolOptionReleased(button12);
            }
        });

        Button<MainButtonBoardController> button13 = new Button<>(ResourceManager.getInstance().imageBigButton, Button.ButtonType.Selector, true);
        button13.setBehavior(new ButtonBehavior<MainButtonBoardController>(controller, button13) {
            @Override
            public void informControllerButtonClicked() {
                getController().onImageOptionClicked(button13);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onImageOptionReleased(button13);
            }
        });


        Button<MainButtonBoardController> button14 = new Button<>(ResourceManager.getInstance().saveBigButton, Button.ButtonType.Selector, true);
        button14.setBehavior(new ButtonBehavior<MainButtonBoardController>(controller, button14) {
            @Override
            public void informControllerButtonClicked() {
                getController().onSaveOptionClicked(button14);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onSaveOptionReleased(button14);
            }
        });
        mainButtonBoard.addToButtonBoard(button10);
        mainButtonBoard.addToButtonBoard(button11);
        mainButtonBoard.addToButtonBoard(button12);
        mainButtonBoard.addToButtonBoard(button13);
        mainButtonBoard.addToButtonBoard(button14);

        drawButtonBoard = new ButtonBoard(200, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        drawButtonBoardController = new DrawButtonBoardController(drawButtonBoard, creationZoneController, optionsWindowController);

        addElement(drawButtonBoard);
        Button<DrawButtonBoardController> button00 = new Button<>(ResourceManager.getInstance().addPolygonTextureRegion, Button.ButtonType.Selector, true);
        button00.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button00) {
            @Override
            public void informControllerButtonClicked() {
                getController().onPolygonCreationButtonClicked(button00);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onPolygonCreationButtonReleased(button00);
            }
        });


        Button<DrawButtonBoardController> button01 = new Button<>(ResourceManager.getInstance().addPointTextureRegion, Button.ButtonType.Selector, true);
        button01.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button01) {
            @Override
            public void informControllerButtonClicked() {
                getController().onAddPointButtonButtonClicked(button01);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onAddPointButtonReleased(button01);
            }
        });


        Button<DrawButtonBoardController> button02 = new Button<>(ResourceManager.getInstance().movePointTextureRegion, Button.ButtonType.Selector, true);
        button02.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button02) {
            @Override
            public void informControllerButtonClicked() {
                getController().onMovePointButtonClicked(button02);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onMovePointButtonReleased(button02);
            }
        });


        Button<DrawButtonBoardController> button03 = new Button<>(ResourceManager.getInstance().removePointTextureRegion, Button.ButtonType.Selector, true);
        button03.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button03) {
            @Override
            public void informControllerButtonClicked() {
                getController().onRemovePointButtonClicked(button03);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onRemovePointButtonReleased(button03);
            }
        });


        Button<DrawButtonBoardController> button04 = new Button<>(ResourceManager.getInstance().mirrorTextureRegion, Button.ButtonType.Selector, true);
        button04.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button04) {
            @Override
            public void informControllerButtonClicked() {
                getController().onMirrorButtonClicked(button04);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onMirrorButtonReleased(button04);
            }
        });

        Button<DrawButtonBoardController> button05 = new Button<>(ResourceManager.getInstance().rotateTextureRegion, Button.ButtonType.Selector, true);
        button05.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button05) {
            @Override
            public void informControllerButtonClicked() {
                getController().onRotateButtonClicked(button05);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onRotateButtonReleased(button05);
            }
        });


        Button<DrawButtonBoardController> button06 = new Button<>(ResourceManager.getInstance().decaleTextureRegion, Button.ButtonType.Selector, true);
        button06.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, button06) {
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


        jointButtonBoard = new ButtonBoard(200, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        jointButtonBoard.setVisible(false);
        jointButtonBoardController = new JointButtonBoardController(jointButtonBoard, creationZoneController, optionsWindowController);

        addElement(jointButtonBoard);
        Button<JointButtonBoardController> button20 = new Button<>(ResourceManager.getInstance().revoluteTextureRegion, Button.ButtonType.Selector, true);
        button20.setBehavior(new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button20) {
            @Override
            public void informControllerButtonClicked() {
                getController().onRevoluteButtonClicked(button20);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onRevoluteButtonReleased(button20);
            }
        });


        Button<JointButtonBoardController> button21 = new Button<>(ResourceManager.getInstance().weldTextureRegion, Button.ButtonType.Selector, true);
        button21.setBehavior(new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button21) {
            @Override
            public void informControllerButtonClicked() {
                getController().onWeldButtonClicked(button21);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onWeldButtonReleased(button21);
            }
        });


        Button<JointButtonBoardController> button22 = new Button<>(ResourceManager.getInstance().distanceTextureRegion, Button.ButtonType.Selector, true);
        button22.setBehavior(new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button22) {
            @Override
            public void informControllerButtonClicked() {
                getController().onDistanceButtonClicked(button22);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onDistanceButtonReleased(button22);
            }
        });

        Button<JointButtonBoardController> button23 = new Button<>(ResourceManager.getInstance().prismaticTextureRegion, Button.ButtonType.Selector, true);
        button23.setBehavior(new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button23) {
            @Override
            public void informControllerButtonClicked() {
                getController().onPrismaticButtonClicked(button23);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onPrismaticButtonReleased(button23);
            }
        });


        Button<JointButtonBoardController> button24 = new Button<>(ResourceManager.getInstance().moveJointTextureRegion, Button.ButtonType.Selector, true);
        button24.setBehavior(new ButtonBehavior<JointButtonBoardController>(jointButtonBoardController, button24) {
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
        //addElement(new PointImage(ResourceManager.getInstance().centedDiskTextureRegion,new Vector2(100,100)));


        addElement(new PointImage(ResourceManager.getInstance().centeredDiskTextureRegion, new Vector2(400, 240)));


        imageButtonBoard = new ButtonBoard(0, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        imageButtonBoard.setVisible(false);

        imageButtonBoardController = new ImageButtonBoardController(imageButtonBoard, this);

        addElement(imageButtonBoard);


        Button<ImageButtonBoardController> button30 = new Button<>(ResourceManager.getInstance().moveImageButtonTextureRegion, Button.ButtonType.Selector, true);
        button30.setBehavior(new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button30) {
            @Override
            public void informControllerButtonClicked() {
                getController().onMoveImageButtonClicked(button30);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onMoveImageButtonReleased(button30);
            }
        });

        Button<ImageButtonBoardController> button31 = new Button<>(ResourceManager.getInstance().rotateImageButtonTextureRegion, Button.ButtonType.Selector, true);
        button31.setBehavior(new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button31) {
            @Override
            public void informControllerButtonClicked() {
                getController().onRotateImageButtonClicked(button31);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onRotateImageButtonReleased(button31);
            }
        });


        Button<ImageButtonBoardController> button32 = new Button<>(ResourceManager.getInstance().scaleButtonTextureRegion, Button.ButtonType.Selector, true);
        button32.setBehavior(new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button32) {
            @Override
            public void informControllerButtonClicked() {
                getController().onScaleImageButtonClicked(button32);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onScaleImageButtonReleased(button32);
            }
        });


        Button<ImageButtonBoardController> button33 = new Button<>(ResourceManager.getInstance().pipeButtonTextureRegion, Button.ButtonType.Selector, true);
        button33.setBehavior(new ButtonBehavior<ImageButtonBoardController>(imageButtonBoardController, button33) {
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
        imageButtonBoardController.disableButtons();
        imageButtonBoard.setLowerBottomX(400 - imageButtonBoard.getWidth() / 2);

        Button<DrawButtonBoardController> createButton = new Button<>(ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.OneClick, true);
        createButton.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, createButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                getToolModel().createTool();
            }
        });
        createButton.setPosition(0, 480 - createButton.getHeight());
        addElement(createButton);


        triggerButton = new Button<>(ResourceManager.getInstance().trigger1, Button.ButtonType.OneClick, true);
        triggerButton.setBehavior(new ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, triggerButton) {
            @Override
            public void informControllerButtonClicked() {
                if(pGameScene.getHand().getGrabbedEntity()!=null && pGameScene.getHand().getGrabbedEntity().hasTriggers()){
                    pGameScene.getHand().getGrabbedEntity().onTriggerPushed();
                }
            }

            @Override
            public void informControllerButtonReleased() {
                if(pGameScene.getHand().getGrabbedEntity()!=null && pGameScene.getHand().getGrabbedEntity().hasTriggers()){
                    pGameScene.getHand().getGrabbedEntity().onTriggerReleased();
                }
            }
        });
        triggerButton.setPosition(800 - triggerButton.getWidth(), 0);
        addElement(triggerButton);

        toolButtonBoard = new ButtonBoard(0, 480 - 41, LinearLayout.Direction.Horizontal, 0);
        toolButtonBoard.setVisible(false);
        toolButtonBoardController = new ToolButtonBoardController(toolButtonBoard, creationZoneController, optionsWindowController);
        addElement(toolButtonBoard);


        Button<ToolButtonBoardController> button40 = new Button<>(ResourceManager.getInstance().targetButtonTextureRegion, Button.ButtonType.Selector, true);
        button40.setBehavior(new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button40) {
            @Override
            public void informControllerButtonClicked() {
                getController().onTargetButtonClicked(button40);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onTargetButtonReleased(button40);
            }
        });


        Button<ToolButtonBoardController> button41 = new Button<>(ResourceManager.getInstance().handleTextureRegion, Button.ButtonType.Selector, true);
        button41.setBehavior(new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button41) {
            @Override
            public void informControllerButtonClicked() {
                getController().onHandleButtonClicked(button41);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onHandleButtonReleased(button41);
            }
        });


        Button<ToolButtonBoardController> button42 = new Button<>(ResourceManager.getInstance().movePointTextureRegion, Button.ButtonType.Selector, true);
        button42.setBehavior(new ButtonBehavior<ToolButtonBoardController>(toolButtonBoardController, button42) {
            @Override
            public void informControllerButtonClicked() {
                getController().onMovePointButtonClicked(button42);
            }

            @Override
            public void informControllerButtonReleased() {
                getController().onMovePointButtonReleased(button42);
            }
        });
        toolButtonBoard.addToButtonBoard(button40);
        toolButtonBoard.addToButtonBoard(button41);
        toolButtonBoard.addToButtonBoard(button42);
        toolButtonBoard.setLowerBottomX(400 - toolButtonBoard.getWidth() / 2);


        panel = new ControlPanel(pGameScene);

        ControlElement controlElementForMovablePoints = new ControlElement(ControlElement.Type.AnalogController, 0, new ControllerAction() {
            @Override
            public void controlMoved(float pX, float pY) {
                pGameScene.ControlMoveableElementChange(pX, pY);
            }

            @Override
            public void controlClicked() {
                pGameScene.controlClicked();
            }

            @Override
            public void controlReleased() {
                pGameScene.controlReleased();
            }
        });
        panel.addControlElements(controlElementForMovablePoints);
        //panel.showControlElement(0);

        setUpdated(true);

    }

    public ToolModel getToolModel() {
        return toolModel;
    }

    public void bindToolModel(ToolModel toolModel) {
        if (this.toolModel != null) {
            for (BodyModel bodyModel : this.toolModel.getBodies()) {
                for (LayerModel layerModel : bodyModel.getLayers()) {
                    removeElement(layerModel.getPointsShape());
                    for (PointsShape p : layerModel.getPointsShapes())
                        removeElement(p);
                }
            }
        }
        itemSaveWindowController.onModelUpdated(toolModel);
        jointSettingsWindowController.setToolModel(toolModel);
        this.toolModel = toolModel;

        for (BodyModel bodyModel : toolModel.getBodies()) {
            for (LayerModel layerModel : bodyModel.getLayers()) {
                if (layerModel.getPointsShape() == null) {
                    PointsShape pointsShape = new PointsShape(this);
                    layerModel.setPointsShape(pointsShape);
                }
                layerModel.getPointsShape().updateSelf();
                addElement(layerModel.getPointsShape());
                for (DecorationModel decorationModel : layerModel.getDecorations()) {
                    if (decorationModel.getPointsShape() == null) {
                        PointsShape pointsShape = new PointsShape(this);
                        decorationModel.setPointsShape(pointsShape);
                    }
                    decorationModel.getPointsShape().updateSelf();
                    addElement(decorationModel.getPointsShape());
                }
            }
            for(ProjectileModel projectileModel:bodyModel.getProjectiles()){
                ProjectileShape projectileShape = new ProjectileShape(projectileModel.getProperties().getProjectileOrigin(), scene);
                projectileShape.updateDirection(projectileModel.getProperties().getProjectileDirection());
                projectileModel.setProjectileShape(projectileShape);
                projectileShape.bindModel(projectileModel);
            }
        }
        toolModel.updateMesh();
        layersWindowController.init();
        itemWindowController.init();
    }

    public CreationZoneController getCreationZoneController() {
        return creationZoneController;
    }

    @Override
    public void drawSelf() {
        contentTraverser.setBehavior(drawVisitBehavior);
        contentTraverser.traverse(this, false);
        hudBatcher.submit();
        sceneBatcher.submit();

    }

    public void step() {
        contentTraverser.setBehavior(updateVisitBehavior);
        contentTraverser.traverse(this, false);
        checkUpdated();
        if (isUpdated()) {
            drawSelf();
        }

        resetUpdate();


    }

    private void resetUpdate() {
        contentTraverser.setBehavior(resetUpdateVisitBehavior);
        contentTraverser.traverse(this, true);

    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
        if (creationZone.isTouchLocked()) return false;
        hudTouchVisitBehavior.setSceneTouchEvent(pTouchEvent);
        hudTouchVisitBehavior.setTouched(false);
        contentTraverser.setBehavior(hudTouchVisitBehavior);
        contentTraverser.traverse(this, false, true);
        return hudTouchVisitBehavior.isTouched();
    }

    public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {
        creationZone.onTouchScene(pTouchEvent, scroll);

        if (creationZoneController.getAction() == CreationZoneController.CreationAction.PIPING && imageShape != null) {
            Color color = imageShape.getColorAt(pTouchEvent.getX(), pTouchEvent.getY(), 10);
            if (color != null)
                optionsWindowController.updatePipeColor(color);
            Log.e("pipe", "color:" + color);
        }

    }

    public void resume() {
        for (Element e : getContents()) {
            if (e instanceof PointsShape) {
                ((PointsShape) e).resume();
            }
        }
    }

    public void dispose() {
        if (false)
            for (Element e : getContents()) {
                if (e instanceof PointsShape) {
                    Log.e("dispose", "outside:" + e.hashCode());
                    ((PointsShape) e).dispose();
                }
            }
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public void updateZoom(float pZoomFactor) {
        zoomFactor = pZoomFactor;
        for (Element e : getContents())
            if (e instanceof PointsShape) {
                PointsShape pointsShape = (PointsShape) e;
                pointsShape.setScale(0.5f / pZoomFactor, 0.5f / pZoomFactor);
            }
    }

    private void checkUpdated() {
        contentTraverser.setBehavior(isUpdatedVisitBehavior);
        isUpdatedVisitBehavior.setUpdated(false);
        contentTraverser.traverse(this, true);
        setUpdated(isUpdatedVisitBehavior.isUpdated());

    }

    public Scene getScene() {
        return scene;
    }

    public LayerSettingsWindowController getLayerSettingsWindowController() {
        return layerSettingsWindowController;
    }

    private void resetSelection() {
        if (toolModel != null)
            toolModel.resetSelection();
    }

    public void setSaveWindowVisible(boolean b) {
        itemSaveWindow.setVisible(b);
    }

    public void setLayersWindowVisible(boolean b) {
        layersWindow.setVisible(b);
        resetSelection();
    }

    public void setJointsWindowVisible(boolean b) {
        jointsWindow.setVisible(b);
        resetSelection();
    }

    public void setItemWindowVisible(boolean b) {
        itemWindow.setVisible(b);
        resetSelection();
    }

    public void setDrawButtonBoardVisible(boolean b) {
        drawButtonBoard.setVisible(b);
    }

    public void setImageButtonBoardVisible(boolean b) {
        imageButtonBoard.setVisible(b);
    }

    public void setJointButtonBoardVisible(boolean b) {
        jointButtonBoard.setVisible(b);
    }

    public void setItemButtonBoardVisible(boolean b) {
        toolButtonBoard.setVisible(b);
    }


    public JointSettingsWindowController getJointSettingsWindowController() {
        return jointSettingsWindowController;
    }

    public void updateOptionsWindow(SettingsType settingsType) {
        optionsWindowController.selectSettingsType(settingsType);
    }

    public void onAddImageButtonClicked() {
        activity.startLoadPictureIntent();
    }

    public void addImage() {
        imageShape = new ImageShape(getScene());
        addElement(imageShape);
        imageButtonBoardController.enableButtons();
    }

    public ImageShape getImageShape() {
        return imageShape;
    }

    public DecorationSettingsWindowController getDecorationSettingsWindowController() {
        return decorationSettingsWindowController;
    }

    public ControlPanel getPanel() {
        return panel;
    }

    public void onActionChanged(CreationZoneController.CreationAction action) {
        if (panel != null) {
            MyAnalogOnScreenControl analog = (MyAnalogOnScreenControl) panel.getControlElementByID(0).getAssociate();
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
                    panel.hideControlElement(0);
                    analog.setOnControlClickEnabled(false);
                    break;
                case MOVE_TOOL_POINT:
                case MOVE_POINT:
                case MOVE_JOINT_POINT:
                    panel.showControlElement(0);
                    analog.setOnControlClickEnabled(true);
                    break;
            }
        }
    }

    public void addReferencePoint(ReferencePointImage centerPointImage) {
        addElement(centerPointImage);
        creationZone.referencePointImageArrayList.add(centerPointImage);
    }

    public void detachReference(ReferencePointImage centerPointImage) {
        removeElement(centerPointImage);
        creationZone.referencePointImageArrayList.remove(centerPointImage);
    }

    public ItemWindowController getItemWindowController() {
        return itemWindowController;
    }


    public ToolButtonBoardController getToolButtonBoardController() {
        return toolButtonBoardController;
    }

    public OptionsWindowController getOptionsWindowController() {
        return optionsWindowController;
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

    public ProjectileOptionController getProjectileOptionsController() {
        return projectileOptionController;
    }

    public LayerWindowController getLayersWindowController() {
        return layersWindowController;
    }

}
