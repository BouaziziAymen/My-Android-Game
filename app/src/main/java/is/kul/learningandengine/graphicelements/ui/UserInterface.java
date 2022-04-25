package is.kul.learningandengine.graphicelements.ui;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.Decoration;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.graphicelements.ui.ButtonBoard.Type;
import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

public class UserInterface extends Entity implements UIHandler {
    public static AtomicInteger IDnum = new AtomicInteger();
    public static ArrayList<Text> texts;
    static Keyboard cb;
    static NumericKeyboard kb;
    final ToolsWindow toolsWindow;
    private final CollisionWindow collisionWindow;
    private final ButtonBoard vchecker;
    private final ButtonBoard board1;
    private final LayersWindow layersWindow;
    private final JointsWindow jointWindow;
    private final ButtonBoard drawboard;
    private final ButtonBoard board2,imageboard;
    private final JointOptionWindow jointOptionWindow;
    private final PromptWindow promptWindow;
    public UserInterface.BoardType board;
    GameScene scene;
    ColorSelectorWindow colorSelectorWindow;
    boolean updated = false;
    private boolean VIRGIN;
    private LayerOptionWindow layerOptionWindow;
    private boolean verticalMagnet;
    private boolean horizontalMagnet;
    private boolean centerMagnet;
    private float range;
    private BodyOptionWindow bodyOptionWindow;

    public UserInterface(VertexBufferObjectManager vbom, GameScene scene) {
        this.VIRGIN = false;
        this.scene = scene;
        UserInterface.texts = new ArrayList<Text>();

        Button insertButton = new Button(0, 0,
                ResourceManager.getInstance().insertPointTextureRegion, vbom, Button.ButtonType.Selector, SignalName.INSERTPOINTBUTTON);


        Button pathButton = new Button(0, 0,
                ResourceManager.getInstance().createPathTextureRegion, vbom, Button.ButtonType.Selector, SignalName.PATHBUTTON);

        Button moveButton = new Button(0, 0,
                ResourceManager.getInstance().movePointTextureRegion, vbom, Button.ButtonType.Selector, SignalName.MOVEELEMENTBUTTON);
        Button removeButton = new Button(0, 0,
                ResourceManager.getInstance().removePointTextureRegion, vbom, Button.ButtonType.Selector, SignalName.REMOVEPOINTBUTTON);

        Button polygonButton = new Button(0, 0,
                ResourceManager.getInstance().polygonTextureRegion, vbom, Button.ButtonType.Selector, SignalName.POLYGONBUTTON);
        Button mirrorButton = new Button(0, 0,
                ResourceManager.getInstance().mirrorTextureRegion, vbom, Button.ButtonType.Selector, SignalName.MIRRORBUTTON);
        Button perpendButton = new Button(0, 0,
                ResourceManager.getInstance().perpendTextureRegion, vbom, Button.ButtonType.Selector, SignalName.PERPENDBUTTON);
        Button cutButton = new Button(0, 0,
                ResourceManager.getInstance().cutTextureRegion, vbom, Button.ButtonType.Selector, SignalName.CUTBUTTON);
        Button rotateButton = new Button(0, 0,
                ResourceManager.getInstance().rotateTextureRegion, vbom, Button.ButtonType.Selector, SignalName.ROTATEBUTTON);

        Button decaleButton = new Button(0, 0,
                ResourceManager.getInstance().dacaleButtonTextureRegion, vbom, Button.ButtonType.Selector, SignalName.DECALEBUTTON);


        Button acceptvButton = new Button(0, 0,
                ResourceManager.getInstance().acceptvTextureRegion, vbom, Button.ButtonType.OneClick, SignalName.ACCEPTBUTTON);
        Button refusevButton = new Button(0, 0 - ResourceManager.getInstance().acceptvTextureRegion.getHeight() + 1,
                ResourceManager.getInstance().refusevTextureRegion, vbom, Button.ButtonType.OneClick, SignalName.REFUSEBUTTON);
        acceptvButton.setZIndex(999);
        refusevButton.setZIndex(999);

//TextPlace quantity = new TextPlace(400, 240,true);
//QuantitySelector quantity = new QuantitySelector(1,400, 240,20,2, 0f,1f);
//quantity.setDepth(999);
//this.attachChild(quantity);


        Button drawButton = new Button(0, 0,
                ResourceManager.getInstance().drawTextureRegion, vbom, Button.ButtonType.Selector, SignalName.DRAWBOARDBUTTON);
        drawButton.click();
        Button jointButton = new Button(0, 0,
                ResourceManager.getInstance().jointTextureRegion, vbom, Button.ButtonType.Selector, SignalName.JOINTBOARDBUTTON);
        float a = ResourceManager.getInstance().drawTextureRegion.getWidth();

        Button collisionButton = new Button(0, 0,
                ResourceManager.getInstance().collisionTextureRegion, vbom, Button.ButtonType.Selector, SignalName.COLLISIONBOARDBUTTON);
        Button imageButton = new Button(0, 0,
                ResourceManager.getInstance().imageButtonTextureRegion, vbom, Button.ButtonType.Selector, SignalName.IMAGEBOARDBUTTON);

        this.drawboard = new ButtonBoard(a / 2, GameActivity.CAMERA_HEIGHT / 2+128, 0, Board.Type.Vertical, Type.Dependant, drawButton, jointButton, collisionButton,imageButton);
        this.drawboard.setZIndex(999);
        attachChild(this.drawboard);


//ColorPanel panel = new ColorPanel(400,240);
    //this.attachChild(panel);






        UserInterface.cb = new Keyboard
                (0, 250, vbom);
        UserInterface.cb.setZIndex(1500);
        UserInterface.cb.setVisible(false);
        attachChild(UserInterface.cb);


        insertButton.setZIndex(999);
        pathButton.setZIndex(999);
        moveButton.setZIndex(999);
        removeButton.setZIndex(999);

        this.board1 = new ButtonBoard(GameActivity.CAMERA_WIDTH * 0.4f, GameActivity.CAMERA_HEIGHT - ResourceManager.getInstance().insertPointTextureRegion.getHeight() / 2 * 0.9f, 0, Board.Type.Horizental, Type.Dependant, pathButton, insertButton, moveButton, removeButton, polygonButton, mirrorButton, perpendButton, cutButton, rotateButton, decaleButton);
        this.board1.setZIndex(999);
        attachChild(this.board1);

        this.board1.setVisible(false);


        Button rotateImageButton = new Button(0, 0,
                ResourceManager.getInstance().rotateTextureRegion, vbom, Button.ButtonType.Selector, SignalName.ROTATEIMAGEBUTTON);
        Button moveImageButton = new Button(0, 0,
                ResourceManager.getInstance().moveImageTextureRegion, vbom, Button.ButtonType.Selector, SignalName.MOVEIMAGEBUTTON);

        Button scaleImageButton = new Button(0, 0,
                ResourceManager.getInstance().scaleButtonTextureRegion, vbom, Button.ButtonType.Selector, SignalName.SCALEIMAGEBUTTON);

        Button pipeButton = new Button(0, 0,
                ResourceManager.getInstance().pipeButtonTextureRegion, vbom, Button.ButtonType.Selector, SignalName.PIPEBUTTON);


        imageboard = new ButtonBoard(GameActivity.CAMERA_WIDTH * 0.4f, GameActivity.CAMERA_HEIGHT - ResourceManager.getInstance().insertPointTextureRegion.getHeight() / 2 * 0.9f, 0, Board.Type.Horizental, Type.Dependant, moveImageButton,rotateImageButton,scaleImageButton,pipeButton);
        this.imageboard.setZIndex(999);
        attachChild(this.imageboard);
        this.imageboard.setVisible(false);






        Button revButton = new Button(0, 0,
                ResourceManager.getInstance().revoluteTextureRegion, vbom, Button.ButtonType.Selector, SignalName.revjointButton);
        Button weldButton = new Button(0, 0,
                ResourceManager.getInstance().weldTextureRegion, vbom, Button.ButtonType.Selector, SignalName.weldjointButton);
        Button disButton = new Button(0, 0,
                ResourceManager.getInstance().distanceTextureRegion, vbom, Button.ButtonType.Selector, SignalName.disjointButton);
        Button prisButton = new Button(0, 0,
                ResourceManager.getInstance().prismaticTextureRegion, vbom, Button.ButtonType.Selector, SignalName.prisjointButton);


        Button movButton = new Button(0, 0,
                ResourceManager.getInstance().movePointTextureRegion2, vbom, Button.ButtonType.Selector, SignalName.movejointButton);

        this.board2 = new ButtonBoard(GameActivity.CAMERA_WIDTH * 0.4f, GameActivity.CAMERA_HEIGHT - ResourceManager.getInstance().insertPointTextureRegion.getHeight() / 2 * 0.9f, 0, Board.Type.Horizental, Type.Dependant, revButton, weldButton, disButton, prisButton, movButton);
        this.board2.setZIndex(999);
        this.board2.setVisible(true);
        attachChild(this.board2);


        this.vchecker = new ButtonBoard(this.board1.getX() + cutButton.getWidth() * 3.5f, this.board1.getY() - cutButton.getHeight() / 2 - acceptvButton.getHeight(), -1, Board.Type.Vertical, Type.Independant, acceptvButton, refusevButton);
        this.vchecker.setZIndex(999);
        this.vchecker.setVisible(false);
        attachChild(this.vchecker);


        this.layersWindow = new LayersWindow(800 - 2 * 64, 240 + 80 - 56 + 8);
        this.layersWindow.setZIndex(999);
        attachChild(this.layersWindow);
        this.setLayerOptionWindow(new LayerOptionWindow(400, 240));
        this.getLayerOptionWindow().setZIndex(1000);
        attachChild(this.getLayerOptionWindow());


        this.setBodyOptionWindow(new BodyOptionWindow(400, 240));
        this.getBodyOptionWindow().setZIndex(1000);
        attachChild(this.getBodyOptionWindow());
        bodyOptionWindow.setVisible(false);


        promptWindow = new PromptWindow();
        promptWindow.setZIndex(1000);
        attachChild(promptWindow);
        promptWindow.setVisible(false);

        toolsWindow = new ToolsWindow();
        toolsWindow.setZIndex(99999999);
        attachChild(toolsWindow);



        this.jointOptionWindow = new JointOptionWindow(400, 240, this);
        this.jointOptionWindow.setZIndex(1000);
        attachChild(this.jointOptionWindow);


        this.jointWindow = new JointsWindow(800 - 2 * 64, 240 + 80 - 56 + 8);
        this.jointWindow.setZIndex(999);
        attachChild(this.jointWindow);
        this.jointWindow.setVisible(false);


        this.collisionWindow = new CollisionWindow(800 - 2 * 64, 240 + 80 - 56 + 8);
        this.collisionWindow.setZIndex(999);
        attachChild(this.collisionWindow);
        this.collisionWindow.setVisible(false);


        this.colorSelectorWindow = new ColorSelectorWindow(0, 0);
        this.colorSelectorWindow.setZIndex(1001);
        attachChild(this.colorSelectorWindow);
        colorSelectorWindow.setPosition(800-colorSelectorWindow.getWidth()/2,240 - 56 + 8+64);
        this.colorSelectorWindow.setVisible(false);


        UserInterface.kb = new NumericKeyboard(400, 240);
        UserInterface.kb.setZIndex(1999);
        UserInterface.kb.setVisible(false);
        attachChild(UserInterface.kb);

        sortChildren();

    }

    public boolean isAnalog() {
        return toolsWindow.isAnalog();
    }

    public boolean isFixedX() {
        return toolsWindow.isFixedX();
    }

    public boolean isFixedY() {
        return toolsWindow.isFixedY();
    }

    public void in_signal_removeDecoration(int bodyID, int layerID, int decorationID) {
        layersWindow.removeDecorationFromOutside(bodyID, layerID, decorationID);
    }

    public boolean isVirgin() {
        return this.VIRGIN;
    }

    @Override
    public void diffuse(UISignal signal) {


        switch (signal.signalName) {
            case revjointButton:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.REVOLUTE;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);
                break;
            case weldjointButton:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.WELD;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);
                break;
            case disjointButton:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.DISTANCE;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);
                break;


            case prisjointButton:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.PRISMATIC;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);
                break;
            case movejointButton:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.MOVEELEMENT;
                    scene.showControlElement(0);
                }
                else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    scene.hideControlElement(0);
                    scene.resetElementToMove();
                } else  if (signal.event == UISignal.Event.Released) {
                    scene.hideControlElement(0);
                    scene.resetElementToMove();
                }
                toolsWindow.diffuse(signal);
                break;

            case MOVEELEMENTBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.MOVEELEMENT;
                    scene.showControlElement(0);
                }
                else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    scene.hideControlElement(0);
                    scene.resetElementToMove();
                }
                else if (signal.event == UISignal.Event.Released) {
                    scene.hideControlElement(0);
                    scene.resetElementToMove();
                }
                toolsWindow.diffuse(signal);
                break;
            case INSERTPOINTBUTTON:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.INSERTPOINT;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                // this.scene.action = GameScene.PlayerAction.SLICE;
                toolsWindow.diffuse(signal);
                break;


            case MIRRORBUTTON:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.MIRROR;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);

                break;
            case REMOVEPOINTBUTTON:

                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.REMOVEPOINT;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);

                break;
            case PATHBUTTON:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.CREATESHAPE;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);

                break;
            case StartCutting:

                scene.model.createCuttingDecoration();
                vchecker.setVisible(true);

                break;
            case CUTBUTTON:
                toolsWindow.diffuse(signal);
                break;
            case ACCEPTBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    scene.model.performCut();
                    scene.action = GameScene.PlayerAction.NONE;
                    board1.releaseAll();
                    vchecker.setVisible(false);
                    this.update();
                }
                break;
            case REFUSEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    scene.model.undoCut();
                    this.update();
                    vchecker.setVisible(false);
                }
                break;
            case POLYGONBUTTON:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.POLYGON;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);
                break;
            case PERPENDBUTTON:
                if (signal.event == UISignal.Event.Clicked)
                    this.scene.action = GameScene.PlayerAction.PERPEND;
                else if (signal.event == UISignal.Event.Unclicked)
                    this.scene.action = GameScene.PlayerAction.NONE;
                toolsWindow.diffuse(signal);

                break;
            case COLLISIONBOARDBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.board1.setVisible(false);
                    this.board2.setVisible(false);
                    this.layersWindow.setVisible(false);
                    this.jointWindow.setVisible(false);
                    this.collisionWindow.setVisible(true);
                    this.board = UserInterface.BoardType.DRAWBOARD;
                } else {

                    this.collisionWindow.setVisible(false);
                    this.board = UserInterface.BoardType.NONE;
                }
                break;


            case IMAGEBOARDBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.board1.setVisible(false);
                    this.board2.setVisible(false);
                    this.imageboard.setVisible(true);
                    this.layersWindow.setVisible(false);
                    this.jointWindow.setVisible(false);
                    this.collisionWindow.setVisible(false);
                    this.board = BoardType.IMAGEBOARD;
                    toolsWindow.diffuse(signal);

                } else {

                    this.imageboard.hide();
                    this.board = UserInterface.BoardType.NONE;
                }
                break;
            case DRAWBOARDBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.board1.setVisible(true);
                    this.board2.setVisible(false);
                    this.layersWindow.setVisible(true);
                    this.jointWindow.setVisible(false);
                    this.collisionWindow.setVisible(false);
                    this.board = UserInterface.BoardType.DRAWBOARD;
                    toolsWindow.diffuse(signal);
                } else {
                    this.board1.hide();

                    this.layersWindow.setVisible(false);
                    this.board = UserInterface.BoardType.NONE;
                }


                break;

            case JOINTBOARDBUTTON:

                if (signal.event == UISignal.Event.Clicked) {
                    this.board1.setVisible(false);
                    this.board2.setVisible(true);
                    this.layersWindow.setVisible(false);
                    this.jointWindow.setVisible(true);
                    this.collisionWindow.setVisible(false);
                    this.board = UserInterface.BoardType.JOINTBOARD;
                } else {
                    this.board2.hide();
                    this.jointWindow.setVisible(false);
                    this.board = UserInterface.BoardType.NONE;
                }
                break;

            case DECALEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.DECALE;
                    this.scene.model.setMovableLayers();
                } else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    this.scene.model.resetActiveBodyLayers();
                }

                toolsWindow.diffuse(signal);
                break;
            case MOVEIMAGEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.MOVEIMAGE;
                    scene.showControlElement(0);
                } else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    scene.hideControlElement(0);
                }
                toolsWindow.diffuse(signal);

                break;
            case SCALEIMAGEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.MOVEELEMENT;
                    scene.showControlElement(0);
                } else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    scene.hideControlElement(0);
                }
                toolsWindow.diffuse(signal);

                break;
            case PIPEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.COLORPIPE;
                   // scene.showControlElement(0);
                } else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    //scene.hideControlElement(0);
                }
                toolsWindow.diffuse(signal);

                break;


            case ROTATEBUTTON:
                if (signal.event == UISignal.Event.Clicked) {
                    this.scene.action = GameScene.PlayerAction.ROTATE;
                    if(this.isMultiple())
                    this.scene.model.setMovableLayers();
                } else if (signal.event == UISignal.Event.Unclicked) {
                    this.scene.action = GameScene.PlayerAction.NONE;
                    this.scene.model.resetActiveBodyLayers();
                }
                toolsWindow.diffuse(signal);
                break;
            case Updated:
                this.updated = true;
                break;


        }


    }

    @Override
    public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
        boolean touched = false;
        Log.e("touched","begin");

        if (pSceneTouchEvent.isActionUp() && updated) {
            updated = false;
        }
        if (updated) return true;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            if (getChildByIndex(i) instanceof UIHandler) {

                UIHandler handler = (UIHandler) getChildByIndex(i);
                if (handler.OnTouchScene(pSceneTouchEvent)) {
                    touched = true;
                    Log.e("touched","t"+handler.getClass());
break;

                }

            }
        }

        this.VIRGIN = !touched;

        return touched;
    }

    public void in_signal_clickLayer(int layerID) {
        layersWindow.clickLayer(layerID);

    }

    public void out_signal_selectLayer(int layerID) {
        Log.e("signal", "" + layerID);
        this.scene.model.setActiveLayer(layerID);

    }

    public int in_signal_addJoint() {
        return jointWindow.addJointFromOutside();

    }

    public int in_signal_addLayer(int activeBodyID) {
        return layersWindow.addLayerFromOutside(activeBodyID);

    }

    public int in_signal_addDecoration(int bodyID, int layerID) {

        return layersWindow.addDecorationFromOutside(bodyID, layerID);

    }
    public void in_signal_addCuttingDecoration(int bodyID, int layerID) {

         layersWindow.addCuttingDecorationFromOutside(bodyID, layerID);

    }

    public void in_signal_removeLayer(int activeBodyID, int layerID) {
        layersWindow.removeLayerFromOutside(activeBodyID, layerID);

    }

    public ArrayList<BodyElement> in_signal_getBodyElements() {
        // TODO Auto-generated method stub
        return layersWindow.getBodies();
    }

    public void out_signal_addLayer(int layerID) {
        this.scene.model.createNewLayer(layerID);
    }

    public void out_signal_addBody(int bodyID) {
        this.scene.model.createNewBody(bodyID);
    }

    public void out_signal_selectBody(int bodyID) {
        this.scene.model.setActiveBody(bodyID);

    }

    public void out_signal_addDecoration(int decorationID) {
        this.scene.model.addDecoration(decorationID, Decoration.DecorationType.Normal);

    }

    public void out_signal_selectDecoration(int decorationID) {
        this.scene.model.selectDecoration(decorationID);

    }

    public void out_signal_swapLayers(int order, int no) {
        this.scene.model.swapLayers(order, no);
    }

    public void out_signal_updateDecaledLayers(int[] moveableLayers) {
        this.scene.model.setMovableLayers(moveableLayers);
    }

    public void out_signal_removeLayer(int layerID) {
        this.scene.model.removeLayer(layerID);
    }
    public void out_signal_removeDecoration(int decorationID) {
        this.scene.model.removeDecoration(decorationID);
    }
    public LayerOptionWindow getLayerOptionWindow() {
        return this.layerOptionWindow;
    }

    public void setLayerOptionWindow(LayerOptionWindow layerOptionWindow) {
        this.layerOptionWindow = layerOptionWindow;
    }

    public void out_signal_updateProperty(LayerProperty lproperty) {
        this.scene.model.updateProperty();
        //layersWindow.renameLayer(signal.specific1,0,signal.properties.layerName);


    }

    public void out_select_Joint(int jointID) {
        this.scene.model.selectJointIndicator(jointID);
    }

    public void out_remove_Joint(int jointID) {
        this.scene.model.removeJointIndicator(jointID);

    }

    public ArrayList<CategoryElement> getCategories() {
        return collisionWindow.getCategories();
    }

    public void in_signal_start_Joint_Option_Window(int jointID) {
        jointOptionWindow.setCurrentJoint(this.scene.model.getJointProperty(jointID));

    }

    public void out_set_Joint_Property(JointProperty property) {
        this.scene.model.setJointProperty(property);
    }

    public void signal_keyboard_On(AlphaNumericInput place) {
        UserInterface.cb.setText(place);
        UserInterface.cb.setVisible(true);
    }

    public void signal_keyboard_Off(AlphaNumericInput place) {
        UserInterface.cb.detachText();
        UserInterface.cb.setVisible(false);
    }

    public void signal_numericKeyboard_On(NumericInput numericInput) {
        UserInterface.kb.setText(numericInput);
        UserInterface.kb.setVisible(true);
    }

    public void signal_numericKeyboard_Off() {
        UserInterface.kb.detachText();
        UserInterface.kb.setVisible(false);
    }

    public void in_signal_OpenColorSelector(Color color, Window caller) {
        caller.unfoldWindow();
        colorSelectorWindow.setCaller(caller);
        colorSelectorWindow.setColor(color.getARGBPackedInt());
        colorSelectorWindow.selector.initialize(color);
        showColorSelector();

    }

    public void signal_updateLayerColor(Color color) {
        Window caller = colorSelectorWindow.getCaller();
   caller.updateColor(color);

            caller.foldWindow();

        this.scene.model.Update();

    }

    public void out_signal_setPolygonNumVertices(int value) {
        this.scene.model.setPolygonNumVertices(value);

    }

    public boolean isCenterMagnet() {
        return this.toolsWindow.getCenterMagnet();
    }

    public boolean isVerticalMagnet() {
        return this.toolsWindow.getVerticalMagnet();
    }

    public float getRange() {
        return toolsWindow.getRange();
    }

    public boolean isHorizontalMagnet() {
        return this.toolsWindow.getHorizontalMagnet();
    }

    public boolean isFirstPointMagnet() {
        return this.toolsWindow.isFirstPointMagnet();
    }
    public boolean isHasLimits() {
        return this.toolsWindow.isHasLimits();
    }
    public boolean isSecondPointMagnet() {
        return this.toolsWindow.isSecondPointMagnet();
    }

    public void update() {
        board1.resetButtons();
        toolsWindow.update();
    }

    public Color in_signal_GetColorOfDecoration(int decorationID) {
        return scene.model.getColorOfDecoration(decorationID);
    }

    public boolean isMultiple() {
        return toolsWindow.isMultiple();
    }

    public void out_signal_setMovable() {
        this.scene.model.setMovableLayers();
    }
    public void out_signal_resetActiveLayers() {
        this.scene.model.resetActiveBodyLayers();
    }

    public void setActive(boolean active) {
        this.active = active;
    }
boolean active = true;

    public void disableButtonBoard() {
        if(board==BoardType.DRAWBOARD)
        board1.disable();
        if(board==BoardType.JOINTBOARD)
        board2.disable();
    }
    public void enableButtonBoard() {
        if(board==BoardType.DRAWBOARD)
            board1.enable();
        if(board==BoardType.JOINTBOARD)
            board2.enable();
    }

    public BodyOptionWindow getBodyOptionWindow() {
        return bodyOptionWindow;
    }

    public void setBodyOptionWindow(BodyOptionWindow bodyOptionWindow) {
        this.bodyOptionWindow = bodyOptionWindow;
    }

    public void in_signal_setPrompt(UISignal signal, UIHandler source) {
        promptWindow.setVisible(true);
        promptWindow.setSignal(signal,source);
    }

    public void setPipeColor(Color color) {
         toolsWindow.setPipeColor(color);
    }

    public Color getPipeColor() {
        return toolsWindow.getPipeColor();
    }

    public void showColorSelector() {
        colorSelectorWindow.setVisible(true);
        toolsWindow.setVisible(false);

        layersWindow.setVisible(false);
    }
    public void hideColorSelector() {
        colorSelectorWindow.setVisible(false);
        toolsWindow.setVisible(true);

        layersWindow.setVisible(true);
    }

    public void addColorToPanel(Color color) {
        colorSelectorWindow.addColorToPanel(color);
    }

    public enum BoardType {
        DRAWBOARD, JOINTBOARD, IMAGEBOARD, NONE
    }
    public void bindToCoordinates(Coordinated bounded){
     toolsWindow.setCoordinated(bounded);
    }

}
