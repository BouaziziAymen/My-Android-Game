package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.Plotter;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.ItemCategoryFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.particles.persistence.PersistenceCaretaker;
import com.evolgames.entities.particles.persistence.PersistenceException;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.ragdoll.Ragdoll;
import com.evolgames.entities.serialization.SerializationManager;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MyColorUtils;
import com.evolgames.helpers.utilities.Vector2Utils;
import com.evolgames.physics.WorldFacade;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BombOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.CasingOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;


public class GameScene extends AbstractScene implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener, PinchZoomDetector.IPinchZoomDetectorListener,
        IOnSceneTouchListener {
    private final ArrayList<GameGroup> gameGroups = new ArrayList<>();
    public static int step;
    public static boolean pause = false;
    public static Plotter plotter;
    public static Plotter plotter2;
    private final WorldFacade worldFacade;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    public Ragdoll ragdoll;
    private final HashMap<Integer, Hand> hands = new HashMap<>();
    float x, y;
    private Vector2 point1, point2;
    private Line line;
    private UserInterface userInterface;
    private HUD hud;
    private boolean scroll = false;
    private float mPinchZoomStartedCameraZoomFactor;
    private GameGroup gameGroup1;
    private PlayerAction action = PlayerAction.Drag;
    private PlayerSpecialAction specialAction = PlayerSpecialAction.None;
    private ArrayList<Vector2> points;


    public GameScene() {
        super();
        Invoker.gameScene = this;
        setOnSceneTouchListener(this);
        this.setCullingEnabled(false);
        worldFacade = new WorldFacade(this);
        BodyFactory.getInstance().create(worldFacade.getPhysicsWorld());
        GameEntityFactory.getInstance().create(worldFacade.getPhysicsWorld(), this);
        try {
            PersistenceCaretaker.getInstance().create(this);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        this.mScrollDetector = new SurfaceScrollDetector(this);

        this.mPinchZoomDetector = new PinchZoomDetector(this);
        createHUD();

    }

    public HUD getHud() {
        return hud;
    }

    public WorldFacade getWorldFacade() {
        return worldFacade;
    }

    @Override
    public void populate() {

        Entity background = new Entity();
        this.setBackground(new EntityBackground(0, 0, 0, background));

        plotter = new Plotter();
        plotter.setZIndex(200);

        plotter2 = new Plotter();
        plotter2.setZIndex(200);


        Color color = new Color(1, 1, 1);
        color.setBlue(0);
        KeyboardController keyboardController = new KeyboardController();

        OutlineController outlineController = new OutlineController();
        LayerWindowController layerWindowController = new LayerWindowController(outlineController);
        ToolModel toolModel = loadToolModel("");

        JointSettingsWindowController jointSettingsWindowController = new JointSettingsWindowController(this, keyboardController, outlineController, toolModel);
        JointWindowController jointWindowController = new JointWindowController(jointSettingsWindowController, outlineController);
        ProjectileOptionController projectileOptionController = new ProjectileOptionController(this, keyboardController, toolModel);
        CasingOptionController ammoOptionController = new CasingOptionController(keyboardController);
        BombOptionController bombOptionController = new BombOptionController(keyboardController);
        ItemWindowController itemWindowController = new ItemWindowController(projectileOptionController, ammoOptionController, bombOptionController, outlineController);
        LayerSettingsWindowController layerSettingsWindowController = new LayerSettingsWindowController(layerWindowController, keyboardController);
        BodySettingsWindowController bodySettingsWindowController = new BodySettingsWindowController(layerWindowController, keyboardController);
        ItemSaveWindowController itemSaveWindowController = new ItemSaveWindowController(keyboardController);
        DecorationSettingsWindowController decorationSettingsWindowController = new DecorationSettingsWindowController();
        OptionsWindowController optionsWindowController = new OptionsWindowController(keyboardController, itemSaveWindowController);
        userInterface = new UserInterface(activity, this, layerWindowController, jointWindowController, layerSettingsWindowController, bodySettingsWindowController, jointSettingsWindowController, itemWindowController, projectileOptionController, ammoOptionController, bombOptionController, itemSaveWindowController, decorationSettingsWindowController, optionsWindowController, outlineController, keyboardController);
        UsageButtonsController usageButtonsController = new UsageButtonsController();
        usageButtonsController.init();
        optionsWindowController.setUserInterface(userInterface);
        itemSaveWindowController.setUserInterface(userInterface);
        jointWindowController.setUserInterface(userInterface);
        projectileOptionController.setUserInterface(userInterface);
        layerSettingsWindowController.setUserInterface(userInterface);
        bodySettingsWindowController.setUserInterface(userInterface);
        decorationSettingsWindowController.setUserInterface(userInterface);
        outlineController.setUserInterface(userInterface);
        userInterface.bindToolModel(toolModel);

        this.attachChild(plotter);
        this.attachChild(plotter2);


        sortChildren();


        createTestUnit();


        createGround();

        if (false) {
            ragdoll = GameEntityFactory.getInstance().createRagdoll(400 / 32f, 240 / 32f);
        }

        testMesh();
    }

    private void testMesh() {
        ArrayList<Vector2> list = VerticesFactory.createPolygon(0, 0, 0, 2, 2, 64);
        float[] data = MeshFactory.getInstance().computeData(list);
        Mesh theMesh = new Mesh(400, 240, data, data.length / 3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        attachChild(theMesh);
    }

    private void createGround() {
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        blocks.add(BlockFactory.createLayerBlock(new ArrayList<Vector2>() {{
            add(obtain(-900, 0));
            add(obtain(-900, 20));
            add(obtain(400, 20));
            add(obtain(400, 0));
        }}, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0)), 0,0,false));
        blocks.add(BlockFactory.createLayerBlock(new ArrayList<Vector2>() {{
            add(obtain(200, 15));
            add(obtain(200, 20));
            add(obtain(340, 20));
            add(obtain(340, 15));
        }}, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1)), 0,1, false));

        blocks.add(BlockFactory.createLayerBlock(new ArrayList<Vector2>() {{
            add(obtain(100, 18));
            add(obtain(100, 20));
            add(obtain(50, 20));
            add(obtain(50, 18));
        }}, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1)), 0,2,false));
        blocks.add(BlockFactory.createLayerBlock(new ArrayList<Vector2>() {{
            add(obtain(140, 18));
            add(obtain(140, 16));
            add(obtain(50, 16));
            add(obtain(50, 18));
        }}, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(3)), 0,3,false));
        BodyFactory.getInstance().create(worldFacade.getPhysicsWorld());


        GameGroup groundGroup = GameEntityFactory.getInstance().createGameGroupTest(blocks, new Vector2(400 / 32f, 0), BodyDef.BodyType.StaticBody, "Ground", OBJECTS_MIDDLE_CATEGORY);
        this.worldFacade.setGround(groundGroup);
    }

    private void createTestUnit() {
        List<Vector2> vertices1 = VerticesFactory.createRectangle(60, 60);
        LayerProperties properties1 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(2));
        LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1, 0);
        List<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block1);


        gameGroup1 = GameEntityFactory.getInstance().createGameGroupTest(blocks, new Vector2(100f / 32f, 200 / 32f), BodyDef.BodyType.DynamicBody);
        getWorldFacade().applyStain(gameGroup1.getGameEntityByIndex(0), 0, 0, block1, Color.RED, 0, false);
        GameEntity gameEntity = gameGroup1.getGameEntityByIndex(0);
        gameEntity.setCenter(new Vector2());
        gameEntity.redrawStains();
        gameEntity.setName("test");

        List<Vector2> vertices2 = VerticesFactory.createRectangle(20, 100);
        LayerProperties properties2 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
        LayerBlock block2 = BlockFactory.createLayerBlock(vertices2, properties2, 1, 0);
        List<LayerBlock> blocks2 = new ArrayList<>();
        blocks2.add(block2);
        BodyInit bodyInit = new TransformInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY),100f / 32f,200 / 32f, 0);
        GameEntity gameEntity2 = GameEntityFactory.getInstance().createGameEntity(100f / 32f, 200 / 32f,0f,bodyInit,blocks2, BodyDef.BodyType.DynamicBody,"test 2");
        gameEntity2.setCenter(new Vector2());
        gameGroup1.addGameEntity(gameEntity2);
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.localAnchorA.set(0,0);
        revoluteJointDef.localAnchorB.set(0,0);
        revoluteJointDef.collideConnected = false;
        getWorldFacade().addJointToCreate(revoluteJointDef,gameEntity,gameEntity2);


        List<Vector2> vertices3 = VerticesFactory.createRectangle(90, 60);
        LayerProperties properties3 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(4));
        LayerBlock block3 = BlockFactory.createLayerBlock(vertices3, properties3, 1, 0);
        List<LayerBlock> blocks3 = new ArrayList<>();
        blocks3.add(block3);
        GameEntityFactory.getInstance().createGameGroupTest(blocks3, new Vector2(300f / 32f, 200 / 32f), BodyDef.BodyType.DynamicBody);

        if (false)
            for (LayerBlock layerBlock : gameGroup1.getGameEntityByIndex(0).getBlocks()) {
                Collections.shuffle(layerBlock.getBlockGrid().getCoatingBlocks());
                layerBlock.getBlockGrid().getCoatingBlocks().forEach(g -> g.setTemperature(10000));
            }
    }

    public ToolModel loadToolModel(String file) {
        ToolModel toolModel = null;
        try {
            toolModel = PersistenceCaretaker.getInstance().loadToolModel(file);
            toolModel.setToolCategory(ItemCategoryFactory.getInstance().getItemCategoryByIndex(2));
        } catch (IOException | ParserConfigurationException | SAXException | PersistenceException e) {
            e.printStackTrace();
        }
        return toolModel;
    }

    @Override
    public void onPause() {
        try {
            SerializationManager.getInstance().serialize(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (userInterface != null) {
            userInterface.onPause();
        }
    }

    @Override
    public void onResume() {
        if (userInterface != null) {
            userInterface.resume();
        }

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        userInterface.step();
        if (!pause) {
            super.onManagedUpdate(pSecondsElapsed);
        }
        step++;

        for (Hand hand : hands.values()) {
            hand.onUpdate();
        }

        Invoker.onStep();
        if (ragdoll != null) {
            ragdoll.onUpdate(pSecondsElapsed);
        }
        getWorldFacade().onStep();

        for (GameGroup gameGroup : gameGroups) {
            gameGroup.onStep(pSecondsElapsed);
        }

        if (false) {
            bluntDamageTest();
        }

        if (false) {
            pulverizationTest();
        }


        if (false)
            coatingTest();


        //projectiles test
        if (false) {
            projectileTest();
        }


    }

    private void bluntDamageTest() {
        if (step % 60 == 10) {
            GameEntity head = ragdoll.leftFoot;
            List<Vector2> pts = Vector2Utils.generateRandomPointsInsidePolygon(10, head.getBlocks().get(0).getVertices().get(0), head.getBlocks().get(0), head);
            for(Vector2 p: pts) {
                GameScene.plotter2.drawPointOnEntity(p, Color.PINK, head.getMesh());
                //0.4f+ (float) (Math.random()*0.3f), (float) (Math.random()*0.3f) , (float) (0.1f+Math.random()*0.2f)
                Color color = new Color(0.6f + (float) (Math.random() * 0.4f), 0f, (float) (0.2f + Math.random() * 0.4f));
                Color skin = new Color(head.getBlocks().get(0).getProperties().getDefaultColor());
                skin.setAlpha((float) (0.2f + 0.5f * Math.random()));
                MyColorUtils.blendColors(color, color, skin);
                this.worldFacade.applyStain(head, p.x, p.y, head.getBlocks().get(0), color, 14, true);
            }
            head.redrawStains();
        }
    }

    private void pulverizationTest() {
        if (step == 180) {
                    ragdoll.getGameEntities().forEach(e -> {
                        e.getBlocks().forEach(b -> worldFacade.pulverizeBlock(b, e));
                        worldFacade.addGameEntityToDestroy(e, false);
                    });
                }
            }


    private void coatingTest() {
        if (step % 5 == 0) {

            LayerBlock layerBlock = new LayerBlock();
            Random random = new Random();
            //VerticesFactory.createPolygon(0,0, (float) (Math.random()*2*Math.PI), (float) (100+100*Math.random()),(float) (100+100*Math.random()),random.nextInt(1)+3);//
            ArrayList<Vector2> vertices = GeometryUtils.generateRandomSpecialConvexPolygon(random.nextInt(10) + 6, 400, 240, 50);
            //vertices.clear();
            //Collections.addAll(vertices,new Vector2(0.7686557f,-113.54755f), new Vector2(170.17291f,57.157574f), new Vector2(-170.94154f,56.390015f));
            layerBlock.initialization(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().materials.get(0)), 0);
            BlockUtils.computeCoatingBlocks(layerBlock);
        }
    }

    private void projectionTest(TouchEvent touchEvent) {
        if (this.points == null) {
            this.points = GeometryUtils.generateRandomSpecialConvexPolygon(9, 400, 240, 90);
        }
        GameScene.plotter2.drawPolygon(this.points, Color.RED);
        Vector2 p = new Vector2(touchEvent.getX(), touchEvent.getY());
        Vector2 proj = GeometryUtils.calculateProjection(p, this.points);
        if (proj != null) {
            GameScene.plotter2.drawPoint(proj, Color.GREEN, 3);
            GameScene.plotter2.drawPoint(p, Color.YELLOW, 3);
            GameScene.plotter2.drawLine2(proj, p, Color.GREEN, 3);
        }
    }

    private void projectileTest() {
        if (step % 60 == 0) {

            Vector2 u = new Vector2(0, -1);
            float angle = (float) ((1 - 2 * Math.random()) * Math.PI / 4);

            GeometryUtils.rotateVectorRad(u, angle);
            ArrayList<Vector2> vertices1 = new ArrayList<>();
            vertices1.add(obtain(0, -20));
            vertices1.add(obtain(-3, -5));
            vertices1.add(obtain(0, 15));
            vertices1.add(obtain(3, -5));

            LayerProperties properties1 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
            properties1.setSharpness(0.5f);
            LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1);

            ArrayList<LayerBlock> blocks = new ArrayList<>();
            blocks.add(block1);

            BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), u.mul(80f)), 400 / 32f, 480 / 32f, (float) (angle + Math.PI)), true);
            GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(400 / 32f, 100 / 32f, (float) (angle + Math.PI), bodyInit, blocks, BodyDef.BodyType.DynamicBody, "Projectile");
            GameGroup proj = new GameGroup(gameEntity);
            gameEntity.getUseList().add(new Projectile(gameEntity));
            addGameGroup(proj);
        }
    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, final TouchEvent touchEvent) {
        this.x = touchEvent.getX() / 32f;
        this.y = touchEvent.getY() / 32f;
        if (false)
            projectionTest(touchEvent);
        if (false)
            if (touchEvent.isActionDown()) {
                getWorldFacade().performFlux(new Vector2(x, y), null, gameGroup1.getGameEntityByIndex(0));
            }
        if (false)
            explosionTest(touchEvent);
        float[] cameraSceneCoordinatesFromSceneCoordinates = mainCamera.getCameraSceneCoordinatesFromSceneCoordinates(touchEvent.getX(), touchEvent.getY());


        TouchEvent hudTouch = TouchEvent.obtain(cameraSceneCoordinatesFromSceneCoordinates[0], cameraSceneCoordinatesFromSceneCoordinates[1], touchEvent.getAction(), touchEvent.getPointerID(),
                touchEvent.getMotionEvent());


        boolean hudTouched = userInterface.onTouchHud(hudTouch, scroll);
        if (!hudTouched) {
            userInterface.onTouchScene(touchEvent, scroll);
        }


        if (mPinchZoomDetector != null) {
            mPinchZoomDetector.onTouchEvent(touchEvent);
            if (mPinchZoomDetector.isZooming()) {
                mScrollDetector.setEnabled(false);
            } else {
                if (!hudTouched)
                    mScrollDetector.onTouchEvent(touchEvent);
            }
        } else {
            if (!hudTouched) {
                mScrollDetector.onTouchEvent(touchEvent);
            }
        }


        Vector2 touch = obtain(touchEvent.getX(), touchEvent.getY());
        if (!hudTouched) {
            if (action == PlayerAction.Drag || action == PlayerAction.Hold) {
                processHandling(touchEvent);
            }
        }
        if (action == PlayerAction.Slice) {
            processSlicing(touchEvent);
        }

        recycle(touch);
        return false;
    }

    private void explosionTest(TouchEvent touchEvent) {
        if (touchEvent.isActionDown()) {
            getWorldFacade().createExplosion(null, x, y, 1f, 0.3f, 0.3f, 0.2f, 100f, 0.1f, 1f);
        }
    }

    private void processSlicing(TouchEvent touchEvent) {
        if (touchEvent.isActionDown()) {
            point1 = new Vector2(touchEvent.getX(), touchEvent.getY());
            point2 = new Vector2(point1);
            line = new Line(point1.x, point1.y, point2.x, point2.y, ResourceManager.getInstance().vbom);
            line.setColor(Color.PINK);
            line.setZIndex(999);
            this.attachChild(line);
            this.sortChildren();
        }
        if (touchEvent.isActionMove()) {
            if (point2 != null) {
                point2.set(touchEvent.getX(), touchEvent.getY());
                line.setPosition(point1.x, point1.y, point2.x, point2.y);
            }
        }
        if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
            if (point1 != null && point2 != null) {
                worldFacade.performScreenCut(point1.mul(1 / 32f), point2.mul(1 / 32f));
                point1 = null;
                point2 = null;
                line.detachSelf();
            }
        }
    }

    public Pair<GameEntity, Vector2> getTouchedEntity(TouchEvent touchEvent, boolean withHold) {
        GameEntity result = null;
        Vector2 anchor = null;
        float minDis = 12f;
        for (GameGroup gameGroup : gameGroups) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (entity.getBody() != null && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                    Vector2 proj = entity.computeTouch(touchEvent, withHold);
                    if (proj != null) {
                        float dis = proj.dst(touchEvent.getX(), touchEvent.getY());
                        if (dis < minDis) {
                            minDis = dis;
                            result = entity;
                            anchor = proj;
                        }
                    }
                }
            }
        }
        if (result != null) {
            return new Pair<>(result, anchor);
        }
        return null;
    }


    private void processHandling(TouchEvent touchEvent) {
        int pointerID = touchEvent.getPointerID();

        if (!hands.containsKey(pointerID)) {
            hands.put(pointerID, new Hand(this));
        }
        Hand hand = hands.get(pointerID);
        assert hand != null;
        if (touchEvent.getPointerID() == hand.getMousePointerId()) {
            hand.onSceneTouchEvent(touchEvent);
        }
    }


    public void onDestroyMouseJoint(MouseJoint j) {
        Optional<Hand> hand = hands.values().stream().filter(e -> e.getMouseJoint() == j).findFirst();
        hand.ifPresent(Hand::onMouseJointDestroyed);
    }

    public void setMouseJoint(MouseJoint joint, GameEntity gameEntity) {
        if (hands.get(gameEntity.getHangedPointerId()) != null) {
            Objects.requireNonNull(hands.get(gameEntity.getHangedPointerId())).setMouseJoint(joint, gameEntity);
        }
    }

    public PhysicsWorld getPhysicsWorld() {
        return getWorldFacade().getPhysicsWorld();
    }


    private void createHUD() {
        hud = new HUD();

        this.mainCamera.setHUD(hud);
        //this.secondCamera.setHUD(hud);


    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {

    }

    @Override
    public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
        SmoothCamera cam = (SmoothCamera) this.mainCamera;
        this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();
        userInterface.getCreationZoneController().setUpLocked(true);
    }

    @Override
    public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mainCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (zf >= 1) {
            cam.setZoomFactor(zf);
            userInterface.updateZoom(zf);
        }

    }

    @Override
    public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mainCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (zf >= 1) {
            cam.setZoomFactor(zf);
            userInterface.updateZoom(zf);
        }

    }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        scroll = true;
    }

    @Override
    public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

        SmoothCamera cam = (SmoothCamera) this.mainCamera;

        float zoomFactor = cam.getZoomFactor();

        float deltax = -pDistanceX / zoomFactor;

        float deltay = pDistanceY / zoomFactor;

        mainCamera.offsetCenter(deltax, deltay);
    }

    public HashMap<Integer, Hand> getHands() {
        return hands;
    }

    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        scroll = false;
    }

    public void setScrollerEnabled(boolean pScrollerEnabled) {
        mScrollDetector.setEnabled(pScrollerEnabled);
        Log.e("scroller", "enabled:" + pScrollerEnabled);
    }

    public void setZoomEnabled(boolean pZoomEnabled) {
        mPinchZoomDetector.setEnabled(pZoomEnabled);
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }


    public ArrayList<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public void addGameGroup(GameGroup gameGroup) {
        gameGroups.add(gameGroup);
    }

    public GameActivity getActivity() {
        return activity;
    }

    public void setAction(PlayerAction action) {
        this.action = action;
        if (action != PlayerAction.Hold) {
            this.specialAction = PlayerSpecialAction.None;
        }
        if (action == PlayerAction.Drag) {
            this.hands.forEach((key, h) -> {
                if (h.getMouseJoint() != null) {
                    h.getMouseJoint().setMaxForce(h.getGrabbedEntity().getMassOfGroup() * 1000);
                }
            });
        }
    }

    public PlayerSpecialAction getSpecialAction() {
        return specialAction;
    }

    public void setSpecialAction(PlayerSpecialAction action) {
        this.specialAction = action;
        final float forceFactor;
        switch (action) {
            case None:
                forceFactor = 1000f;
                break;
            case Slash:
                forceFactor = 2000f;
                break;
            case Stab:
            case Throw:
            case Grenade:
            case Shoot:
                forceFactor = 3000f;
                break;
            case Smash:
                forceFactor = 4000f;
                break;
            default:
                forceFactor = 0;
        }
        this.hands.forEach((key, h) -> {
                    if (h.getMouseJoint() != null) {
                        h.setForceFactor(forceFactor);
                    }
                }
        );
    }

    public PlayerAction getPlayerAction() {
        return action;
    }

    public void onUsagesUpdated() {
        List<PlayerSpecialAction> usageList = new ArrayList<>();
        this.hands.forEach((key, h) -> {
            if (h.getGrabbedEntity() != null) {
                h.getGrabbedEntity().getUseList().forEach(u -> {
                    if (this.action == PlayerAction.Hold) {
                        if (u.getAction() != null && u.getAction().iconId != -1) {
                            usageList.add(u.getAction());
                        }
                    }
                });
            }
        });
        if(this.action==PlayerAction.Hold&&!usageList.isEmpty()) {
            usageList.add(0, PlayerSpecialAction.None);
        }
        if (!usageList.contains(specialAction)) {
            this.setSpecialAction(PlayerSpecialAction.None);
        }
        this.userInterface.updateParticularUsageSwitcher(usageList.toArray(new PlayerSpecialAction[0]));
    }
}

