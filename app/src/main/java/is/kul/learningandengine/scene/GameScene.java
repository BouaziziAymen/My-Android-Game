package is.kul.learningandengine.scene;

import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.HashSet;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.GameGroup;
import is.kul.learningandengine.controls.MyAnalogOnScreenControl;
import is.kul.learningandengine.controls.MyBaseOnScreenControl;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.graphicelements.DElement;
import is.kul.learningandengine.graphicelements.DFactory;
import is.kul.learningandengine.graphicelements.DImage;
import is.kul.learningandengine.graphicelements.DPoint;
import is.kul.learningandengine.graphicelements.DynamicGrid;
import is.kul.learningandengine.graphicelements.EntityModel;
import is.kul.learningandengine.graphicelements.Plotter;
import is.kul.learningandengine.graphicelements.PrismaticLimitDPoint;
import is.kul.learningandengine.graphicelements.PrismaticPoint;
import is.kul.learningandengine.graphicelements.RevoluteLimitDPoint;
import is.kul.learningandengine.graphicelements.SizeLimitDImage;
import is.kul.learningandengine.graphicelements.ui.JointProperty;
import is.kul.learningandengine.graphicelements.ui.UserInterface;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.particlesystems.FireParticleSystem;


public class GameScene extends AbstractScene implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener, PinchZoomDetector.IPinchZoomDetectorListener,
        IOnSceneTouchListener {

    public static World world;
    public static ArrayList<Color> flameColors;
    public static GameGroup ground;
    public static UserInterface ui;
    public static Plotter plotter;
    public static EntityModel model;
    Object key = new Object();
    static boolean pause;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private final ControlPanel controlPanel;
    public PhysicsWorld physicsWorld;
    public BaseOnScreenControl.IOnScreenControlListener listener;
    public AnalogOnScreenControl analogOnScreenControl;
    public static GameScene.PlayerAction action;
    private DElement movedElement;
    float lastX;
    GameGroup test;
    int counter;
    boolean count = true;
    int k = 2;
    float pzf = 1;
    private DCGameEntity entity;
    private Entity background;
    private DynamicGrid grid;
    private ArrayList<Vector2> triangle;
    private int score;
    public static MouseJoint mouseJoint;
    private float time;
    private GameGroup bullets;
    private boolean DRAGGING;
    private float mPinchZoomStartedCameraZoomFactor;
    private boolean noZoom = true;
    private boolean noScroll = true;

    private Vector2 begin;
    private Vector2 origin;
    private Slash slash;
    private Vector2 reference;
    private Vector2 createdPoint;
    private GameGroup group;
    private int mousePointerId;
    private DCGameEntity bullet;
    private IUpdateHandler mouseUpdateHandler;


    public GameScene() {
        GameScene.pause = false;
        this.physicsWorld = new PhysicsWorld(new Vector2(0,
                -SensorManager.GRAVITY_EARTH), false);
        physicsWorld.setAutoClearForces(true);
        physicsWorld.setContinuousPhysics(true);

        BasicFactory.getInstance().create(this.physicsWorld, this.vbom, this);
        DFactory.getInstance().create();

        this.mScrollDetector = new SurfaceScrollDetector(this);

        this.mPinchZoomDetector = new PinchZoomDetector(this);
        GameScene.world = new World(this.physicsWorld);
        GameScene.world.setZIndex(999999999);
        controlPanel = new ControlPanel(this);
        ControlElement controlElementForMovablePoints = new ControlElement(ControlElement.Type.AnalogController,
        ControlElement.Function.ControlMoveableElement,0);
        controlPanel.addControlElements(controlElementForMovablePoints);


        attachChild(GameScene.world);
        sortChildren();

    }



    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
        if (Math.abs(pAccelerationData.getX() - this.lastX) > 0.5) {

            this.lastX = pAccelerationData.getX();

        }/*
         * final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX() *
         * 8, - SensorManager.GRAVITY_EARTH * 4);
         * this.physicsWorld.(gravity); Vector2Pool.recycle(gravity);
         */
    }

    private void createBackground() {
        this.background = new Entity();
        this.setBackground(new EntityBackground(1f, 1f, 1f, this.background));
        // Sprite cloud1 = new Sprite(200, 300, res.cloud1TextureRegion, vbom);

        this.grid = new DynamicGrid(this.vbom);


        attachChild(this.background);
        this.grid.setZIndex(0);
       // attachChild(this.grid);


        sortChildren();
    }
    void setElementToMove(DElement element){
        ui.bindToCoordinates(element);
        movedElement = element;
        if(movedElement instanceof PrismaticLimitDPoint){
            PrismaticLimitDPoint plp = (PrismaticLimitDPoint)movedElement;
            PrismaticPoint parent = (PrismaticPoint) plp.getParent().getParent();
            boolean isSecondLimit = parent.isSecondLimit(plp);
            if(isSecondLimit){
                ((MyAnalogOnScreenControl)controlPanel.getControlElementByID(0).associate).setType(MyBaseOnScreenControl.Type.Y);
            } else {
                ((MyAnalogOnScreenControl)controlPanel.getControlElementByID(0).associate).setType(MyBaseOnScreenControl.Type.XYD);
            }
        }
        if(movedElement instanceof RevoluteLimitDPoint){
            ((MyAnalogOnScreenControl)controlPanel.getControlElementByID(0).associate).setType(MyBaseOnScreenControl.Type.Y);
        }
        else if(movedElement instanceof PrismaticLimitDPoint){
            ((MyAnalogOnScreenControl)controlPanel.getControlElementByID(0).associate).setType(MyBaseOnScreenControl.Type.XYD);
        }
    else if(movedElement instanceof SizeLimitDImage) {
            ((MyAnalogOnScreenControl)controlPanel.getControlElementByID(0).associate).setType(MyBaseOnScreenControl.Type.XYD);
        }
    }

    public void resetElementToMove(){
        if(movedElement!=null)
        movedElement.resetColor();
        movedElement = null;
    }

    public  void ControlMoveableElementChange( float pValueX, float pValueY) {

        if (movedElement != null && !movedElement.dead) {

            boolean analog = ui.isAnalog();
            boolean fixedX = ui.isFixedX();
            boolean fixedY = ui.isFixedY();
            float step = 3 / pzf;

            float dX =  step * pValueX;
            float dY =  step * pValueY;
            model.moveElement(movedElement, dX, dY);
        }

    }



    private void createAnalog(){

        final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(480, 240, ResourceManager.getInstance().camera, ResourceManager.getInstance().mOnScreenControlBaseTextureRegion, ResourceManager.getInstance().mOnScreenControlKnobTextureRegion, 0.1f, 200, ResourceManager.getInstance().vbom, new AnalogOnScreenControl.IAnalogOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {

            }

            @Override
            public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {

            }
        });
        analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControl.getControlBase().setAlpha(0.5f);

        analogOnScreenControl.refreshControlKnobPosition();
        setChildScene(analogOnScreenControl);

    }

    @Override
    public void populate() {

        this.createHUD();
      //  DAngle angle = new DAngle(new Vector2(0,0),new Vector2(1,-2),new Vector2(1,-3),1,4);
createAnalog();

        this.setTouchAreaBindingOnActionDownEnabled(true);

        this.action = GameScene.PlayerAction.DRAG;
        this.model = new EntityModel(this);
        attachChild(this.model);

        /*
         * DebugRenderer dr = new DebugRenderer(physicsWorld, vbom);
         * dr.setDepth(999); attachChild(dr);
         */


        // createHUD();
        this.addGround(0, 0);
        this.createBackground();
        this.engine.enableAccelerationSensor(this.activity, this);
        this.registerUpdateHandler(this.physicsWorld);
        this.setOnSceneTouchListener(this);
        // physicsWorld.setContactListener(new MyContactListener(player));

        this.triangle = new ArrayList<Vector2>();
        this.triangle.add(new Vector2(0, 0));
        this.triangle.add(new Vector2(100, 0));
        this.triangle.add(new Vector2(150, 50));
        this.triangle.add(new Vector2(150, 100));
        this.triangle.add(new Vector2(50, 50));

        if (true) {


            GameScene.flameColors = new ArrayList<Color>();
            GameScene.flameColors.add(new Color(126 / 255f, 40 / 255f, 1 / 255f));
            GameScene.flameColors.add(new Color(241 / 255f, 60 / 255f, 0 / 255f));
            GameScene.flameColors.add(new Color(255 / 255f, 103 / 255f, 0 / 255f));
            GameScene.flameColors.add(new Color(253 / 255f, 206 / 255f, 6 / 255f));
            GameScene.flameColors.add(new Color(249 / 255f, 238 / 255f, 58 / 255f));
            GameScene.flameColors.add(new Color(255 / 255f, 255 / 255f, 255 / 255f));
            GameScene.flameColors.add(new Color(180 / 255f, 180 / 255f, 254 / 255f));
            GameScene.flameColors.add(new Color(131 / 255f, 131 / 255f, 255 / 255f));
            GameScene.flameColors.add(new Color(83 / 255f, 83 / 255f, 254 / 255f));
            GameScene.flameColors.add(new Color(10 / 255f, 10 / 255f, 254 / 255f));
            GameScene.flameColors.add(new Color(0 / 255f, 0 / 255f, 190 / 255f));
            GameScene.flameColors.add(new Color(0, 0, 0));


/*
            this.model.addPoint(350,200);
            this.model.addPoint(450,200);
            this.model.addPoint(450,300);
            this.model.addPoint(350,300);


*/



        }



        this.sortChildren();
    }


    private void addGround(float tx, float ty) {

        GameScene.ground = BasicFactory.getInstance().createGround();
        GameScene.world.addGroup(GameScene.ground);



        if (false)
            for (int i = 0; i < 1; i++) {
                this.test = BasicFactory.getInstance().createRagdoll(400 + i * 80, 340);
                GameScene.world.addGroup(this.test);
            }

    }

    private void createHUD() {
        HashSet<Vector2> points0 = new HashSet<Vector2>();
        points0.add(new Vector2(2, -1));
        points0.add(new Vector2(1, -1));


        HUD hud = new HUD();




      GameScene.ui = new UserInterface(this.vbom, this);
      hud.attachChild(GameScene.ui);
        this.camera.setHUD(hud);

        plotter = new Plotter();
        plotter.setZIndex(999999999);
        attachChild(plotter);


        ArrayList<Vector2> pts = new ArrayList<Vector2>();
        pts.add(new Vector2(0, 0));
        pts.add(new Vector2(0, 10));
        pts.add(new Vector2(10, 10));
        pts.add(new Vector2(10, 0));


    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (GameScene.pause) return;
        this.time += pSecondsElapsed;

        if (this.time > 1 / 60f) {
            this.time = this.time - 1 / 60f;

            super.onManagedUpdate(1 / 60f);

            GameScene.world.step();
        }

        this.counter++;
if(true)
        if (this.counter % 300 == 0) {
            if (test != null) for (DCGameEntity e : test.getEntities()) {
                e.detach();
            }
        }

        if (this.counter % 300 == 40) {
            this.test = BasicFactory.getInstance().createRagdoll(400, 440);
            GameScene.world.addGroup(this.test);
        }
if(false)
        if (this.counter % 360 == 0) {
    if(group!=null)for(DCGameEntity e:group.getEntities()){e.detach();

    }
plotter.detachChildren();
model.removeLayer(0);
model.createNewLayer(0);
int n= Utils.RAND.nextInt(14)+3;
            ArrayList<Vector2> polygon = Utils.generateRandomSpecialConvexPolygon(n);
            for(Vector2 v:polygon)this.model.addPoint(v.x,v.y);
 group = model.create();


           // Block testblock = model.getActiveLayer().generateBlock();
          //  testblock.fillGrid();
/*
            Log.e("print",""+ Arrays.toString(polygon.toArray()));
            for(Grain g:testblock.getGrains()){
                plotter.drawPoint(g.position,Color.RED,1,0);

            }
Vector2 start = testblock.startup;*/


        }
        if(false)
        if(counter>=3600){
            plotter.detachChildren();
            Block testblock = model.getActiveLayer().generateBlock();
            Vector2 u = new Vector2(1,0);
            Vector2 start = testblock.startup;
            int k = counter%360;
            Utils.rotateVector2(u,k);
            u.mul(500);


            Vector2 last1 = start.cpy().add(u);
            Vector2 last2 = start.cpy().sub(u);

            Cut cut = testblock.slice(start,last1,last2);

          plotter.drawPoint(cut.getP1(),Color.RED,1,0);
          plotter.drawPoint(cut.getP2(),Color.RED,1,0);
plotter.drawLine2(cut.getP1(),cut.getP2(),Color.BLUE,1);
            ArrayList<ArrayList<Vector2>> groups = Utils.splitFinal(cut, testblock.VERTICES);
            ArrayList<Vector2> chunk1 = groups.get(0);
            ArrayList<Vector2> chunk2 = groups.get(1);
            Vector2 dir = cut.getP2().cpy().sub(cut.getP1()).nor().mul(5);
            Vector2 nor = new Vector2(dir.y,-dir.x);
            for(Vector2 v:chunk1)v.add(-200,0);
            for(Vector2 v:chunk2)v.add(-200,0);
Utils.drawPath(chunk1,Color.BLUE);
            Utils.drawPath(chunk2,Color.YELLOW);

            for(int i=0;i<chunk1.size();i++){
                int ni = (i==chunk1.size()-1)?0:i+1;
                Vector2 p = chunk1.get(i);
                Vector2 np = chunk1.get(ni);
                float d = p.dst(np);
                if(d<2f)Log.e("print","issue");
            }
            for(int i=0;i<chunk2.size();i++){
                int ni = (i==chunk2.size()-1)?0:i+1;
                Vector2 p = chunk2.get(i);
                Vector2 np = chunk2.get(ni);
                float d = p.dst(np);
                if(d<2f)Log.e("print","issue");
            }

        }
if(bullet!=null)Log.e("speed",""+bullet.getBody().getLinearVelocity());
        if (this.counter % 60 == 0) {


        }
        if (count) {
            //world.getGroupByIndex(0).getEntityByIndex(0).blocks.get(0).getGrains().get(0).temperature=2000;
        }


        if (true)
            if (this.counter > 600) {
                ArrayList<Fire> fires = new ArrayList<Fire>();
                for (Fire fire : World.fires) fires.add(fire);
                for (Fire fire : fires) GameScene.world.removeFire(fire);
            }

            if (this.count)
                if (this.counter % 60 == 0) {
                    //Outline outline = new Outline();

                    if (true) {
                        if (false)
                            for (int i = 0; i < 50; i++) {
                                FireParticleSystem system = new FireParticleSystem(i % 10 * 32, 200 + i / 10 * 32, 0, 1, null, 5000);
                                attachChild(system);
                            }

                        if (this.bullets == null) {
                            this.bullets = new GameGroup("");
                            world.addGroup(this.bullets);
                        }
                         bullet = BasicFactory.getInstance().createBullet();
                        bullet.setZIndex(99999999);
                        this.bullets.addChild(bullet);

                        bullet.setDeathTimer(60);
//this.sortChildren();
//for(int i=0;i<1;i++)
//world.createFire(entity.blocks.get(0).grid.allGrains.get(i), entity);


                    }
                }


    }

    @Override
    public void onPause() {


    }

    @Override
    public void onResume() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent touchEvent) {

        float[] coord = this.camera.getSurfaceCoordinatesFromSceneCoordinates(
                touchEvent.getX(), touchEvent.getY());
        // normalize
        coord[0] = coord[0] / GameActivity.SCREEN_WIDTH
                * GameActivity.CAMERA_WIDTH;
        coord[1] = GameActivity.CAMERA_HEIGHT - coord[1]
                / GameActivity.SCREEN_HEIGHT * GameActivity.CAMERA_HEIGHT;

        TouchEvent hudTouch = TouchEvent.obtain(coord[0], coord[1],
                touchEvent.getAction(), touchEvent.getPointerID(),
                touchEvent.getMotionEvent());
        if (this.noZoom) GameScene.ui.OnTouchScene(hudTouch);

        // IF NOT VIRGIN THEN BlockA CLICKABLE ELEMENT WAS TOUCHED


        if (!GameScene.ui.isVirgin() || this.action == GameScene.PlayerAction.POLYGON || this.action == GameScene.PlayerAction.MIRROR)

        if (this.action == GameScene.PlayerAction.CREATESHAPE
                || this.action == GameScene.PlayerAction.MOVEELEMENT
                || this.action == GameScene.PlayerAction.POLYGON
                || this.action == GameScene.PlayerAction.INSERTPOINT
                || this.action == GameScene.PlayerAction.REMOVEPOINT
                || this.action == GameScene.PlayerAction.NONE) {

            if (mPinchZoomDetector != null) {

                mPinchZoomDetector.onTouchEvent(touchEvent);

                if (mPinchZoomDetector.isZooming()) {
                    mScrollDetector.setEnabled(false);
                } else {
                    if (touchEvent.isActionDown()) {
                            mScrollDetector.setEnabled(true);
                    }

                    mScrollDetector.onTouchEvent(touchEvent);

                }
            } else {

                mScrollDetector.onTouchEvent(touchEvent);
            }

        }

        if (this.action == GameScene.PlayerAction.SLICE) {

            if (this.slash != null) this.slash.updateTouch(touchEvent);
            if (touchEvent.isActionDown()) {

                this.slash = new Slash(new Vector2(touchEvent.getX(), touchEvent.getY()));
                attachChild(this.slash);
            }

            if (touchEvent.isActionUp()) {

                GameScene.world.computeCut(this.slash.getPoints());
                this.slash.detachSelf();

            }

        }

        if (this.noZoom&&ui.isVirgin()) {
            if (this.action == GameScene.PlayerAction.MOVEELEMENT) {
                if (touchEvent.isActionDown()) {


                    float distance = Float.MAX_VALUE;

                    ArrayList<DElement> delements = model.getMovableElements();
                    DElement movedElement = null;
                    Vector2 touch = new Vector2(touchEvent.getX(), touchEvent.getY());


                    for (int i = 0; i < delements.size(); i++) {

                        DElement delement = delements.get(i);
                        float[] points = delement.getSceneCenterCoordinates();
                        Vector2 point = new Vector2(points[0],points[1]);

                        if (touch.dst(point) < distance) {
                            distance = touch.dst(point);
                            movedElement = delement;
                        }

                    }


                    if (distance < 50 && movedElement != null) {

                        movedElement.setBlue();
                        for (DElement element : delements)
                            if (element != movedElement)element.resetColor();

                        this.setElementToMove(movedElement);


                    }
                }
            }

            if (this.action == GameScene.PlayerAction.DECALE) {
                if (touchEvent.isActionDown()) {
                    this.origin = new Vector2(touchEvent.getX(), touchEvent.getY());
                }
                if (touchEvent.isActionMove()) {
                    if (origin != null) {
                        Vector2 v = new Vector2(touchEvent.getX(), touchEvent.getY()).sub(this.origin);
                        this.origin = new Vector2(touchEvent.getX(), touchEvent.getY());
                        boolean multiple = ui.isMultiple();
                       this.model.Decale((ui.isFixedX())?0:v.x,(ui.isFixedY())?0:v.y,multiple);

                    }
                }

            }


            if (this.action == GameScene.PlayerAction.ROTATE) {
                if(touchEvent.getPointerID()==0) {
                    if (touchEvent.isActionDown()) {
                        this.origin = new Vector2(touchEvent.getX(), touchEvent.getY());

                    }
                    if(this.origin!=null)
                    if (touchEvent.isActionMove()) {
                        this.origin.set(touchEvent.getX(), touchEvent.getY());
                    }

                }
                if(origin!=null)
                if(touchEvent.getPointerID()==1)
                    if (touchEvent.isActionDown()) {
                        ui.disableButtonBoard();
                        this.reference = new Vector2(touchEvent.getX(), touchEvent.getY());
                        model.createRotationIndicator(origin,reference);
                    }
                if (touchEvent.isActionUp()&&touchEvent.getPointerID()==1) {
                    model.abortRotationIndicator();
                    ui.enableButtonBoard();
                    reference = null;
                }
                if (reference != null)
                if (touchEvent.isActionMove()&&touchEvent.getPointerID()==1) {
                        Vector2 v = new Vector2(touchEvent.getX(), touchEvent.getY());
                        model.updateRotationIndicator(v);
                       float angle = Utils.getAbsoluteAngle(reference.sub(origin),v.cpy().sub(origin));
                        this.model.Rotate(origin,angle,ui.isMultiple());
                        reference.set(v.x,v.y);
                }

            }


            if (this.action == GameScene.PlayerAction.PERPEND) {

                if (touchEvent.isActionUp()) {
                    DPoint point = this.model.getLastPoint();
                    if (point != null) {
                        float dy = Math.abs(touchEvent.getY() - point.getY());
                        float dx = Math.abs(touchEvent.getX() - point.getX());
                        if (dx >= dy)
                            this.model.addPoint(touchEvent.getX(), point.getY());
                        else
                            this.model.addPoint(point.getX(), touchEvent.getY());
                    } else {
                        this.model.addPoint(touchEvent.getX(), touchEvent.getY());

                    }

                }
            }

            if (this.action == GameScene.PlayerAction.INSERTPOINT && this.model.getActiveLayerID() != -1) {

                if (touchEvent.isActionUp()) {

                    // ADDING BlockA POINT AT THE END OF THE PATH
                    if (this.model.getActivePointsNumber() > 1)
                        this.model.insertPoint(touchEvent.getX(), touchEvent.getY());
                }

            }

            if (this.action == GameScene.PlayerAction.EXPLOSION) {
                if (touchEvent.isActionDown()) {
                    float size = 0.01f;
                    int number = 100;
                    //	Factory.getInstance().createInvisibleExplosion(
                    //touchEvent.getX(), touchEvent.getY(), size, number);
                }
            }
            if (this.action == GameScene.PlayerAction.MIRROR) {
                if (touchEvent.isActionDown()) {

                    Vector2 start = applyMagnets(touchEvent.getX(), touchEvent.getY());
                    model.createMirrorIndicator(start);

                }
                if (touchEvent.isActionMove()) {

                    Vector2 end =  applyMagnets(touchEvent.getX(), touchEvent.getY());
                    model.updateMirrorIndicator(end);
                }
                if (touchEvent.isActionUp()) {

                    model.applyMirror();

                }

            }


            if (this.action == GameScene.PlayerAction.REVOLUTE || this.action == GameScene.PlayerAction.WELD || this.action == GameScene.PlayerAction.PRISMATIC || this.action == GameScene.PlayerAction.DISTANCE) {
                if (touchEvent.isActionDown()) {
                    JointProperty.Joint_Type type = null;
                    if (this.action == GameScene.PlayerAction.REVOLUTE)
                        type = JointProperty.Joint_Type.REVOLUTE;
                    if (this.action == GameScene.PlayerAction.WELD)
                        type = JointProperty.Joint_Type.WELD;
                    if (this.action == GameScene.PlayerAction.DISTANCE)
                        type = JointProperty.Joint_Type.DISTANCE;
                    if (this.action == GameScene.PlayerAction.PRISMATIC)
                        type = JointProperty.Joint_Type.PRISMATIC;


                    Vector2 begin = applyMagnets(touchEvent.getX(), touchEvent.getY());
                    this.model.createJointIndicator(begin, begin, type,ui.isHasLimits());

                }
                if (touchEvent.isActionMove()) {
                    Vector2 end = applyMagnets(touchEvent.getX(), touchEvent.getY());

                    this.model.updateJointIndicator(end.x, end.y);
                }


            }


            if (this.action == GameScene.PlayerAction.POLYGON) {


                if (touchEvent.isActionDown()) {
                    begin = (ui.isFirstPointMagnet())?applyMagnets(touchEvent.getX(), touchEvent.getY()):new Vector2(touchEvent.getX(), touchEvent.getY());
                    this.model.createPolygon(begin);
                }

                if (touchEvent.isActionMove()) {
                    Vector2 move = (ui.isSecondPointMagnet()) ?
                            applyMagnets(touchEvent.getX(), touchEvent.getY()) : new Vector2(touchEvent.getX(), touchEvent.getY());
                    if (begin.dst(move) > 1)
                        this.model.updatePolygonIndicator(move.x, move.y);
                }

                if (this.begin != null && touchEvent.isActionUp()) {
                    this.model.hidePolygonIndicator();
                }

            }

            if (this.action == GameScene.PlayerAction.REMOVEPOINT) {
                if (touchEvent.isActionDown()) {
                    DPoint dpoint = this.model.getClosestPoint(touchEvent.getX(),
                            touchEvent.getY(), ui.getRange());
                    if (dpoint != null)
                        this.model.detachPoint(dpoint);

                }
            }
            if (this.action == PlayerAction.COLORPIPE) {
                Color color = sketch.getColorAt(touchEvent.getX(),touchEvent.getY());

                if(color!=null) {
                    ui.setPipeColor(color);
                }
            }



            if (this.action == GameScene.PlayerAction.CREATESHAPE) {


if(touchEvent.getX()>700)action = PlayerAction.DRAG;
                if (touchEvent.isActionDown()) {
                    createdPoint = new Vector2(touchEvent.getX(),touchEvent.getY());
                }

                    if (touchEvent.isActionUp()&&createdPoint!=null) {

                        // ADDING BlockA POINT AT THE END OF THE PATH
                        this.model.addPoint(applyMagnets(createdPoint.x, createdPoint.y));
                        createdPoint = null;
                    }

                }





        }


        if (this.action == GameScene.PlayerAction.DRAG) {

                if (touchEvent.isActionDown()&&mouseJoint==null) {

                    outerloup:
                    for (int k = 0; k < GameScene.world.getNumberOfGroups(); k++) {
                        GameGroup grouping = GameScene.world.getGroupByIndex(k);

                        for (int i = 0; i < grouping.getEntityCount(); i++) {
                            DCGameEntity entity = grouping.getEntityByIndex(i);

                            if (entity.testTouch(touchEvent.getX(), touchEvent.getY(), 16)) {
mousePointerId = touchEvent.getPointerID();

                                final MouseJointDef mouseJointDef = new MouseJointDef();
                                mouseJointDef.bodyA = ground.getEntityByIndex(0).getBody();
                                mouseJointDef.bodyB = entity.getBody();
                                mouseJointDef.dampingRatio = 1f;
                                mouseJointDef.frequencyHz = 10;
                                mouseJointDef.maxForce = 1000 * grouping.getMass();

                                final Vector2 vec = Vector2Pool
                                        .obtain(touchEvent.getX()
                                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                                touchEvent.getY()
                                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                                float x = entity.sail.x;
                                float y = entity.sail.y;

                                float[] newcoords = entity
                                        .convertLocalCoordinatesToSceneCoordinates(x, y);

                                Vector2 sail = new Vector2(
                                        newcoords[0]
                                                / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                        newcoords[1]
                                                / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                                mouseJointDef.target.set(sail);
                                mouseJointDef.collideConnected = true;


                                                                                                 mouseJoint = (MouseJoint) physicsWorld.createJoint(mouseJointDef);
                                                                                                 mouseJoint.setTarget(vec);
                                                                                                 Vector2Pool.recycle(vec);



                                break outerloup;
                            }

                        }
                    }
                }


                else if (touchEvent.getPointerID()==mousePointerId&&mouseJoint!=null) {

                    if (touchEvent.isActionMove()) {
                        Vector2 vec = Vector2Pool
                                .obtain(touchEvent.getX()
                                                / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                        touchEvent.getY()
                                                / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

                        this.mouseJoint.setTarget(vec);

                        Vector2Pool.recycle(vec);
                    } else if (touchEvent.isActionCancel() || touchEvent.isActionOutside() || touchEvent.isActionUp()) {
                        mouseJoint.setMaxForce(0);
mouseJoint.setFrequency(0);
mouseJoint=null;






                    }

                }
            }




        if (touchEvent.isActionUp()) {
            this.model.modelTrail.cutTrail();


        }



        TouchEvent.recycle(hudTouch);
        return true;

    }

    @Override
    public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
                                   TouchEvent pSceneTouchEvent) {
        SmoothCamera cam = (SmoothCamera) this.camera;
        this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();

        this.noZoom = false;
createdPoint = null;

    }

    @Override
    public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
                            TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.camera;

        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;

        if (zf >= 1 && zf <= 16) {
            this.model.setScale(zf);

        }

        if (zf < 1)
            zf = 1;
        if (zf > 16)
            zf = 16;
        if (zf >= 1 && zf <= 16)
            this.grid.update(zf, cam.getXMin(), cam.getXMax(), cam.getYMin(),
                    cam.getYMax());
        cam.setZoomFactor(zf);

        this.pzf = zf;
    }

    @Override
    public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
                                    TouchEvent pTouchEvent, float pZoomFactor) {
        noZoom = true;

        SmoothCamera cam = (SmoothCamera) this.camera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (zf < 1)
            zf = 1;
        if (zf > 16)
            zf = 16;
        cam.setZoomFactor(zf);

    }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
                                float pDistanceX, float pDistanceY) {
        this.noZoom = false;
        createdPoint = null;


    }

    @Override
    public void onScroll(ScrollDetector pScollDetector, int pPointerID,
                         float pDistanceX, float pDistanceY) {


        SmoothCamera cam = (SmoothCamera) this.camera;

        float zoomFactor = cam.getZoomFactor();

        float deltax = -pDistanceX / zoomFactor;

        float deltay = pDistanceY / zoomFactor;

        camera.offsetCenter(deltax, deltay);
        this.grid.update(this.pzf, cam.getXMin(), cam.getXMax(), cam.getYMin(),
                cam.getYMax());

    }

    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
                                 float pDistanceX, float pDistanceY) {
        this.noZoom = true;

    }
private DImage sketch;
    public void addImage() {
        TextureRegion region = ResourceManager.getInstance().sketchTextureRegion;
        Bitmap bitmap = ResourceManager.getInstance().sketchBitmap;
        sketch = new DImage(400, 240,region.getWidth(),region.getHeight(), region,bitmap);
        sketch.setZIndex(1000);
        this.grid.attachChild(sketch);
        grid.sortChildren();

    }

    public Vector2 applyMagnets(float x, float y) {
        Vector2 closest = null;
        float X = x;
        float Y = y;
        float range = GameScene.ui.getRange();
        boolean centerFound = false;
        if (GameScene.ui.isCenterMagnet()) {
            ArrayList<Vector2> list = this.model.getCenters();
            for (Vector2 v : list) {
                if (v.dst(x, y) < range) closest = v;
            }
        }
        if (closest != null) centerFound = true;
        if (!centerFound) {
            if (GameScene.ui.isHorizontalMagnet()) {
                int K = this.grid.getK(this.pzf);
                int nx1 = (int) (K * Math.floor(x / K));
                int nx2 = nx1 + K;
                if (Math.abs(x - nx1) < range || Math.abs(x - nx2) < range) {
                    if (Math.abs(x - nx1) > Math.abs(x - nx2)) X = nx2;
                    else X = nx1;
                }
            }

            if (GameScene.ui.isVerticalMagnet()) {
                int K = this.grid.getK(this.pzf);
                int ny1 = (int) (K * Math.floor(y / K));
                int ny2 = ny1 + K;
                if (Math.abs(y - ny1) < range || Math.abs(y - ny2) < range) {
                    if (Math.abs(y - ny1) > Math.abs(y - ny2)) Y = ny2;
                    else Y = ny1;
                } else Y = y;
            }
            closest = new Vector2(X, Y);
        }

        return closest;
    }


    public static void ControlMoveableElementClick() {
    }

    public  void showControlElement(int ID) {
        controlPanel.showControlElement(ID);
    }
    public  void hideControlElement(int ID) {
        controlPanel.hideControlElement(ID);
    }

    public DImage getSketch() {
        return sketch;
    }


    public enum PlayerAction {
       COLORPIPE, PRISMATIC, REVOLUTE, DRAG, SLICE, CREATESHAPE, MOVEELEMENT, INSERTPOINT, REMOVEPOINT, POLYGON, MIRROR, EXPLOSION, PERPEND, NONE, WELD, DISTANCE, ROTATE, MOVEIMAGE, DECALE
    }
}
