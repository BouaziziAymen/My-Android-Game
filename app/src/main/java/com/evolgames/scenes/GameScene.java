package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.PROJECTILE_CATEGORY;
import static com.evolgames.physics.CollisionConstants.PROJECTILE_MASK;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.Plotter;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.DecorationBlock;
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
import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.ragdoll.Ragdoll;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.physics.CollisionConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.hand.Hand;
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
import com.evolgames.userinterface.view.inputs.controllers.Movable;

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
    public static int step;
    public static boolean pause = false;
    public static Plotter plotter;
    public static Plotter plotter2;
    private static final ArrayList<GameGroup> gameGroups = new ArrayList<>();
    private final WorldFacade worldFacade;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    public Ragdoll ragdoll;
    HashMap<Integer, Hand> hands = new HashMap<>();
    float x, y;
    private Vector2 point1;
    private Vector2 point2;
    private Line line;
    private PlayerAction action = PlayerAction.Slice;
    private UserInterface userInterface;
    private HUD hud;
    private boolean scroll = false;
    private float mPinchZoomStartedCameraZoomFactor;
    private Movable movable;
    private GameGroup gameGroup;
    private Mesh theMesh;
    private UsageButtonsController usageButtonsController;


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
        ToolModel toolModel = null;
        try {
            toolModel = PersistenceCaretaker.getInstance().loadToolModel("c2_grenade.xml");
            toolModel.setToolCategory(ItemCategoryFactory.getInstance().getItemCategoryByIndex(2));
        } catch (IOException | ParserConfigurationException | SAXException | PersistenceException e) {
            e.printStackTrace();
        }

        JointSettingsWindowController jointSettingsWindowController = new JointSettingsWindowController(this, keyboardController, outlineController, toolModel);
        JointWindowController jointWindowController = new JointWindowController(jointSettingsWindowController, outlineController);
        ProjectileOptionController projectileOptionController = new ProjectileOptionController(this, keyboardController, toolModel);
        CasingOptionController ammoOptionController = new CasingOptionController(keyboardController);
        BombOptionController bombOptionController = new BombOptionController(keyboardController);
        ItemWindowController itemWindowController = new ItemWindowController(projectileOptionController, ammoOptionController,bombOptionController, outlineController);
        LayerSettingsWindowController layerSettingsWindowController = new LayerSettingsWindowController(layerWindowController, keyboardController);
        BodySettingsWindowController bodySettingsWindowController = new BodySettingsWindowController(layerWindowController, keyboardController);
        ItemSaveWindowController itemSaveWindowController = new ItemSaveWindowController(keyboardController);
        DecorationSettingsWindowController decorationSettingsWindowController = new DecorationSettingsWindowController();
        OptionsWindowController optionsWindowController = new OptionsWindowController(keyboardController, itemSaveWindowController);

        userInterface = new UserInterface(activity, this, layerWindowController, jointWindowController, layerSettingsWindowController, bodySettingsWindowController, jointSettingsWindowController, itemWindowController, projectileOptionController, ammoOptionController, bombOptionController, itemSaveWindowController, decorationSettingsWindowController, optionsWindowController, outlineController, keyboardController);
        this.usageButtonsController = new UsageButtonsController(userInterface,this);
        this.usageButtonsController.init();
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

        //plotter2.drawLine2(new Vector2(0,240),new Vector2(400,240),Color.RED,3);
        ArrayList<Vector2> testPoints = new ArrayList<>();
        testPoints.add(new Vector2(350, 100));
        testPoints.add(new Vector2(400, 150));
        testPoints.add(new Vector2(450, 100));
        testPoints.add(new Vector2(400, 50));


        ArrayList<Vector2> triangles = new ArrayList<>();
        triangles.add(new Vector2(350, 100));
        triangles.add(new Vector2(400, 150));
        triangles.add(new Vector2(450, 100));
        triangles.add(new Vector2(350, 100));
        triangles.add(new Vector2(400, 50));
        triangles.add(new Vector2(450, 100));


        ArrayList<Vector2> ver = new ArrayList<>();
        ver.add(new Vector2(-100, -3));
        ver.add(new Vector2(-100, 3));
        ver.add(new Vector2(100, 3));
        ver.add(new Vector2(100, -3));

        //float[] data = MeshFactory.getInstance().createDataForTexturedMesh(400,240,0,region, ver, new Vector2(64, 64));
        //TexturedMeshBatch batch = new TexturedMeshBatch(ResourceManager.getInstance().texturedMesh, 10000, ResourceManager.getInstance().vbom);
        // batch.draw(region, data);
        // batch.redrawStains();
        //batch.setDepth(999);


        sortChildren();
        //  createAnalog();


        LayerBlock block1;

        ArrayList<Vector2> vertices = VerticesFactory.createPolygon(0, 0, 64, 64, 4);
        LayerProperties properties = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0));
        block1 = BlockFactory.createLayerBlock(vertices, properties, 0, 0);
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block1);
        // gameEntity1 = GameEntityFactory.getInstance().createGameEntity(11f, 4f, 0, blocks, BodyDef.BodyType.DynamicBody, "entity1");
        // attachChild(gameEntity1.getMesh());


        vertices = VerticesFactory.createPolygon(0, 0, 60, 60, 4);
        properties = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(11));
        block1 = BlockFactory.createLayerBlock(vertices, properties, 7, 0);
        blocks = new ArrayList<>();
        blocks.add(block1);


        float a = (float) (Math.sqrt(2) * 16);
        if (false)
            for (CoatingBlock g : block1.getBlockGrid().getCoatingBlocks()) {


                float x = g.position.x;
                float y = g.position.y;
                if (!Utils.PointInPolygon(new Vector2(x, y), block1.getVertices()) || GeometryUtils.isOnBorder(new Vector2(x, y), block1.getVertices()))
                    continue;

                ArrayList<Vector2> l = VerticesFactory.createRectangle(x, y, 16, 16);
                DecorationBlock blockB = BlockFactory.createDecorationBlock(l, new DecorationProperties(Color.PINK), 0, block1.getVertices(), new Vector2(x, y));
                block1.addAssociatedBlock(blockB);
                break;

            }

        if (true) {
            // UncoloredSprite uncoloredSprite = new UncoloredSprite(400, 240, ResourceManager.getInstance().pokemon, ResourceManager.getInstance().vbom);
            // this.attachChild(uncoloredSprite);

            gameGroup = GameEntityFactory.getInstance().createGameGroupTest(blocks, new Vector2(5f, 200 / 32f), BodyDef.BodyType.DynamicBody);
            getWorldFacade().applyLiquidStain(gameGroup.getGameEntityByIndex(0), 0, 0, block1, Color.RED, 0);
            gameGroup.getGameEntityByIndex(0).redrawStains();
            gameGroup.getGameEntityByIndex(0).setName("test");
            Vector2 v1 = gameGroup.getGameEntityByIndex(0).getBlocks().get(0).getVertices().get(0);
            // Vector2 v2 = gameGroup.getGameEntityByIndex(0).getBlocks().get(0).getVertices().get(1);
            //  PointExplosiveParticleWrapper pointExplosive = this.worldFacade.createPointFireSource(null, new Vector2(400,240), 3000f,1f, 0.1f, 0.1f, 10f, 2000f);
            //pointExplosive.detachFromParent();
            if (false)
                for (LayerBlock layerBlock : gameGroup.getGameEntityByIndex(0).getBlocks()) {
                    Collections.shuffle(layerBlock.getBlockGrid().getCoatingBlocks());
                    layerBlock.getBlockGrid().getCoatingBlocks().forEach(g -> g.setTemperature(10000));
                }
        }



        ArrayList<LayerBlock> blocks2 = new ArrayList<>();
        ArrayList<Vector2> vertices2 = new ArrayList<>();

        vertices2.add(obtain(-4000, 0));
        vertices2.add(obtain(-4000, 20));
        vertices2.add(obtain(4400, 20));
        vertices2.add(obtain(4400, 0));


        LayerBlock block3 = BlockFactory.createLayerBlock(vertices2, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0)), 0);
        //LayerBlock block4 = BlockFactory.createBlockA(vertices3, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0), groundFilter), 0);

        blocks2.clear();
//        blocks2.add(block1);
        blocks2.add(block3);
        // blocks2.add(block4);
        BodyFactory.getInstance().create(worldFacade.getPhysicsWorld());


        GameGroup groundGroup = GameEntityFactory.getInstance().createGameGroupTest(blocks2, new Vector2(400 / 32f, 0), BodyDef.BodyType.StaticBody, "Ground", CollisionConstants.DOLL_CATEGORY, (short) -1);
        this.worldFacade.setGround(groundGroup);

        if (true) {
            ragdoll = GameEntityFactory.getInstance().createRagdoll(400 / 32f, 240 / 32f);
        }
//GameEntityFactory.getInstance().createTest();


        ArrayList<Vector2> list = VerticesFactory.createPolygon(0, 0, 0, 2, 2, 64);
        float[] data = MeshFactory.getInstance().computeData(list);
        theMesh = new Mesh(400, 240, data, data.length / 3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        attachChild(theMesh);

//Color(175 / 256f, 17 / 256f, 28 / 256f)) blood
        //     liquidSource = getWorldFacade().createSegmentLiquidSource(gameEntity1,new Vector2(400,240),new Vector2(450,240), new Color(0 / 255f, 255 / 255f, 255 / 255f),30,50);

        //      getWorldFacade().createSegmentLiquidSource(300,240,Utils.getColor());
        //    getWorldFacade().createSegmentLiquidSource(500,240,Utils.getColor());

        List<Vector2> verti1 = new ArrayList<>();
        verti1.add(new Vector2(0, 0));
        verti1.add(new Vector2(50, 0));
        verti1.add(new Vector2(50, 50));
        verti1.add(new Vector2(0, 50));
        List<Vector2> verti2 = new ArrayList<>();
        verti2.add(new Vector2(-50, 25));
        verti2.add(new Vector2(50, 20));
        verti2.add(new Vector2(25, -25));
        sortChildren();


    }

    @Override
    public void onPause() {
        Log.e("lifecycle", "pause");
        if (userInterface != null) {
            userInterface.dispose();
        }
    }

    @Override
    public void onResume() {
        Log.e("lifecycle", "resume");
        if (userInterface != null) {
            userInterface.resume();
        }
        // GameEntityFactory.getInstance().createRagdoll();
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        userInterface.step();

        for (Hand hand : hands.values()) {
            hand.onUpdate();
        }

        if (!pause) {
            super.onManagedUpdate(pSecondsElapsed);
        }

        // if (false && step == 60) GameEntityFactory.getInstance().createLinks();

        step++;

        Invoker.onStep();
        if (ragdoll != null) {
            ragdoll.onUpdate(pSecondsElapsed);
        }
        getWorldFacade().onStep();

        for (GameGroup gameGroup : gameGroups) {
            gameGroup.onStep(pSecondsElapsed);
        }
        if (false) {
            if (step == 120) {
                ragdoll.getGameEntities().forEach(e -> {
                    e.getBlocks().forEach(b -> worldFacade.pulverizeBlock(b, e));
                    worldFacade.addGameEntityToDestroy(e, false);
                });
            }
        }

        if (false)
            if (gameGroup.getGameEntityByIndex(0).getBody() != null) {
                gameGroup.getGameEntityByIndex(0).getBody().setLinearVelocity(30, 10 - step / 100f);
                mainCamera.setCenter(gameGroup.getGameEntityByIndex(0).getMesh().getX(), gameGroup.getGameEntityByIndex(0).getMesh().getY());
            }

        if (false)
            if (step % 15 == 0) {
                theMesh.setScale(1);
                theMesh.setColor(Color.WHITE);
            } else {
                theMesh.setScale(1 + (step % 15) * 10);
            }
        if (step == 180) {
            //getWorldFacade().pulverizeBlock(ragdoll.upperTorso.getBlocks().get(0), ragdoll.upperTorso);

        }

        if (false)
            if (step % 120 == 0) {
                Random rand = new Random();
                ArrayList<Vector2> list = VerticesFactory.createPolygon(0, 0, (float) (Math.random() * Math.PI), 100, 100, rand.nextInt(6) + 3);
                LayerProperties properties = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0));
                LayerBlock block = BlockFactory.createLayerBlock(list, properties, 0);
                properties.setDefaultColor(Utils.getRandomColor(0.5f));
            }


        if (false)
            if (step % 5 == 0) {

                LayerBlock layerBlock = new LayerBlock();
                Random random = new Random();
                //VerticesFactory.createPolygon(0,0, (float) (Math.random()*2*Math.PI), (float) (100+100*Math.random()),(float) (100+100*Math.random()),random.nextInt(1)+3);//
                ArrayList<Vector2> vertices = GeometryUtils.generateRandomSpecialConvexPolygon(random.nextInt(10) + 6);
                //vertices.clear();
                //Collections.addAll(vertices,new Vector2(0.7686557f,-113.54755f), new Vector2(170.17291f,57.157574f), new Vector2(-170.94154f,56.390015f));
                layerBlock.initialization(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().materials.get(0)), 0);
                BlockUtils.computeCoatingBlocks(layerBlock);
            }


        //projectiles test
        if (false)
            if (step % 60 == 0) {
                Filter projectileFilter = new Filter();
                projectileFilter.groupIndex = -1;
                Vector2 u = new Vector2(0, -1);
                float angle = (float) ((1 - 2 * Math.random()) * Math.PI / 4);

                GeometryUtils.rotateVectorRad(u, angle);
                ArrayList<Vector2> vertices1 = new ArrayList<>();
                vertices1.add(obtain(0, -10));
                vertices1.add(obtain(-6, -5));
                vertices1.add(obtain(0, 15));
                vertices1.add(obtain(6, -5));

                LayerProperties properties1 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
                LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1);

                ArrayList<LayerBlock> blocks = new ArrayList<>();
                blocks.add(block1);

                BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(PROJECTILE_CATEGORY, PROJECTILE_MASK), u.mul(60)), 400 / 32f, 480 / 32f, (float) (angle + Math.PI)), true);
                GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(400 / 32f, 480 / 32f, (float) (angle + Math.PI), bodyInit, blocks, BodyDef.BodyType.DynamicBody, "Projectile");
                GameGroup proj = new GameGroup(gameEntity);
                gameEntity.setProjectile(true);
                addGameGroup(proj);
            }

    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, final TouchEvent touchEvent) {
        this.x = touchEvent.getX() / 32f;
        this.y = touchEvent.getY() / 32f;
        if(false)
            if(touchEvent.isActionDown()){
                getWorldFacade().performFlux(new Vector2(x,y),null,gameGroup.getGameEntityByIndex(0));
            }
        if (false)
            if (touchEvent.isActionDown()) {
                getWorldFacade().createExplosion(null,x, y, 0.1f,0f,1f,1f,0f,0.1f,0.1f);
            }
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
                // userInterface.getCreationZoneController().resetScrollAndZoom();
                if (!hudTouched)
                    mScrollDetector.onTouchEvent(touchEvent);
            }
        } else {
            if (!hudTouched) {
                mScrollDetector.onTouchEvent(touchEvent);
                Log.e("touch", "scroll");
            }
        }


        Vector2 touch = obtain(touchEvent.getX(), touchEvent.getY());

        if (action == PlayerAction.Drag) {
            processGrabbing(touchEvent);
        }


        if (action == PlayerAction.Slice) {
            processSlicing(touchEvent);
        }
        if (touchEvent.isActionUp())
            if (new Vector2(400, 240).dst(touchEvent.getX(), touchEvent.getY()) < 16) {
                if (action == PlayerAction.Drag) {
                    action = PlayerAction.Slice;
                } else {
                    action = PlayerAction.Drag;
                }
            }

        recycle(touch);
        return false;
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

    private void processGrabbing(TouchEvent touchEvent) {
        int pointerID = touchEvent.getPointerID();
        if (!hands.containsKey(pointerID)) {
            hands.put(pointerID,  new Hand(worldFacade));
        }
        Hand hand = hands.get(pointerID);
        if (touchEvent.isActionDown()) {
            for (GameGroup gameGroup : gameGroups)
                for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                    GameEntity entity = gameGroup.getGameEntities().get(k);
                    if (entity.computeTouch(touchEvent) && entity.getBody() != null && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                        if(hand.getGrabbedEntity()==null||hand.getGrabbedEntity()!=entity) {
                            if(hand.getGrabbedEntity()!=null) {
                                usageButtonsController.removeGameEntityControls(hand.getGrabbedEntity());
                                hand.release();
                            }
                            hand.grab(entity, touchEvent, entity.shouldBeHeld());
                            usageButtonsController.addGameEntityControls(entity);
                        } else {
                            hand.resumeGrab();
                        }
                        break;
                    }
                }
        } else if (touchEvent.isActionCancel() || touchEvent.isActionOutside() || touchEvent.isActionUp()) {
            if (hand != null&&hand.getGrabbedEntity()!=null) {
                if(!hand.getGrabbedEntity().shouldBeHeld()) {
                    usageButtonsController.removeGameEntityControls(hand.getGrabbedEntity());
                    hand.release();
                } else {
                    hand.stopFollow();
                }
            }
        }
        for(Hand h:hands.values()){
            h.onSceneTouchEvent(touchEvent);
        }
    }

    public void onDestroyMouseJoint(MouseJoint j) {
        Optional<Hand> hand = hands.values().stream().filter(e -> e.getMouseJoint() == j).findFirst();
        hand.ifPresent(value -> value.setMouseJoint(null));
    }

    public void setMouseJoint(MouseJoint joint, int hangedPointerId) {
        if (hands.get(hangedPointerId) != null) {
            Objects.requireNonNull(hands.get(hangedPointerId)).setMouseJoint(joint);
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

    public void setMovable(Movable movable) {
        this.movable = movable;
    }

    public void ControlMovableElementChange(float valueX, float valueY) {
        if (movable != null) {
            movable.onControllerMoved(valueX, valueY);
        }
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

    public void controlReleased() {

    }

    public void controlClicked() {

    }
}

