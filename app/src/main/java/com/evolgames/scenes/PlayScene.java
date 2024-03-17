package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.Plotter;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.PlayerAction;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.serialization.SavingBox;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.Missile;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.entities.usage.ProjectileType;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.PlayUserInterface;
import com.evolgames.userinterface.view.inputs.controllers.ControlElement;
import com.evolgames.userinterface.view.inputs.controllers.ControllerAction;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.MyColorUtils;
import com.evolgames.utilities.Vector2Utils;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayScene extends PhysicsScene<PlayUserInterface>
        implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener,
        PinchZoomDetector.IPinchZoomDetectorListener {

    public static boolean pause = false;
    public static Plotter plotter;
    private GameGroup gameGroup1;
    private PlayerAction playerAction = PlayerAction.Drag;
    private PlayerSpecialAction specialAction = PlayerSpecialAction.None;
    private Vector2 point1, point2;
    private Line line;
    private ArrayList<Vector2> points;
    private boolean scroll;
    private SavingBox savingBox;

    public PlayScene(Camera pCamera) {
        super(pCamera, SceneType.PLAY);
        this.savingBox = new SavingBox(this);
        plotter = new Plotter();
        this.attachChild(plotter);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(1 / 60f);
        if (this.savingBox != null) {
            this.savingBox.onStep();
        }
        if (hand != null) {
            hand.onUpdate();
        }

        if (false) {
            bluntDamageTest();
        }

        if (false) {
            pulverizationTest();
        }

        if (false) coatingTest();

        // projectiles test
        if (false) {
            projectileTest();
        }
    }


    private void processHandling(TouchEvent touchEvent) {
        int pointerID = touchEvent.getPointerID();
        if (pointerID != 0) {
            return;
        }
        if (hand == null) {
            hand = new Hand(this);
        }
        hand.onSceneTouchEvent(touchEvent);
    }

    @Override
    protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
        boolean hudTouched = this.userInterface.onTouchHud(hudTouchEvent);
        if (false) {
            projectionTest(touchEvent);
        }
        if (false) {
            if (touchEvent.isActionDown()) {
                this.worldFacade.performFlux(
                        new Vector2(touchEvent.getX(), touchEvent.getY()),
                        null,
                        gameGroup1.getGameEntityByIndex(0));
            }
        }
        if (false) {
            explosionTest(touchEvent);
        }

        if (playerAction == PlayerAction.Slice) {
            processSlicing(touchEvent);
        }
        Vector2 touch = obtain(touchEvent.getX(), touchEvent.getY());
        if (!hudTouched) {
            if (playerAction == PlayerAction.Drag || playerAction == PlayerAction.Hold) {
                processHandling(touchEvent);
            }
        }
        if (playerAction == PlayerAction.Slice) {
            processSlicing(touchEvent);
        }
    }

    @Override
    public void populate() {
        createRagDoll(400, 420);
        // createItemFromFile("RPG.xml",true,true);
        createTestUnit();
        createGround();
        testMesh();
        // createTool(loadToolModel("c1_knife.xml"));
        //  createTool(loadToolModel("c1_sword.xml"));
        //  createTool(loadToolModel("c1_morning_star.xml"));
        //  createTool(loadToolModel("c2_revolver_latest.xml"));
    }

    @Override
    public void detach() {
        destroyEntities();
        if (this.userInterface != null) {
            this.userInterface.detachSelf();
        }
        this.hand = null;
        this.worldFacade.getTimedCommands().clear();
    }

    @Override
    public void onPause() {
        this.savingBox.onScenePause();
        this.savingBox.saveSelf();
        this.detach();
    }

    @Override
    public void onResume() {
        createUserInterface();
        this.savingBox = new SavingBox(this);
        this.savingBox.loadSelf();
        this.savingBox.onSceneResume();
    }

    private void destroyEntities() {
        for (GameGroup gameGroup : this.getGameGroups()) {
            this.getGameGroups().remove(gameGroup);
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                this.worldFacade.destroyGameEntity(gameEntity, true, false);
            }
        }
    }

    @Override
    public void createUserInterface() {
        this.userInterface = new PlayUserInterface(this);
    }

    private void bluntDamageTest() {
        if (step % 60 == 10) {
            GameEntity head = ragdoll.leftFoot;
            List<Vector2> pts =
                    Vector2Utils.generateRandomPointsInsidePolygon(
                            10, head.getBlocks().get(0).getVertices().get(0), head.getBlocks().get(0), head);
            for (Vector2 p : pts) {
                EditorScene.plotter.drawPointOnEntity(p, Color.PINK, head.getMesh());
                // 0.4f+ (float) (Math.random()*0.3f), (float) (Math.random()*0.3f) , (float)
                // (0.1f+Math.random()*0.2f)
                Color color =
                        new Color(
                                0.6f + (float) (Math.random() * 0.4f), 0f, (float) (0.2f + Math.random() * 0.4f));
                Color skin = new Color(head.getBlocks().get(0).getProperties().getDefaultColor());
                skin.setAlpha((float) (0.2f + 0.5f * Math.random()));
                MyColorUtils.blendColors(color, color, skin);
                this.worldFacade.applyStain(head, p.x, p.y, head.getBlocks().get(0), color, 0f, 14, true);
            }
            head.redrawStains();
        }
    }

    private void pulverizationTest() {
        if (step == 180) {
            ragdoll
                    .getGameEntities()
                    .forEach(
                            e -> {
                                e.getBlocks().forEach(b -> this.worldFacade.pulverizeBlock(b, e));
                                this.worldFacade.destroyGameEntity(e, true, false);
                            });
        }
    }

    private void coatingTest() {
        if (step % 5 == 0) {

            LayerBlock layerBlock = new LayerBlock();
            Random random = new Random();
            // VerticesFactory.createPolygon(0,0, (float) (Math.random()*2*Math.PI), (float)
            // (100+100*Math.random()),(float) (100+100*Math.random()),random.nextInt(1)+3);//
            ArrayList<Vector2> vertices =
                    GeometryUtils.generateRandomSpecialConvexPolygon(random.nextInt(10) + 6, 400, 240, 50);
            // vertices.clear();
            // Collections.addAll(vertices,new Vector2(0.7686557f,-113.54755f), new
            // Vector2(170.17291f,57.157574f), new Vector2(-170.94154f,56.390015f));
            layerBlock.initialization(
                    vertices,
                    PropertiesFactory.getInstance()
                            .createProperties(MaterialFactory.getInstance().materials.get(0)),
                    0);
            BlockUtils.computeCoatingBlocks(layerBlock);
        }
    }

    private void projectionTest(TouchEvent touchEvent) {
        if (this.points == null) {
            this.points = GeometryUtils.generateRandomSpecialConvexPolygon(9, 400, 240, 90);
        }
        EditorScene.plotter.drawPolygon(this.points, Color.RED);
        Vector2 p = new Vector2(touchEvent.getX(), touchEvent.getY());
        Vector2 proj = GeometryUtils.calculateProjection(p, this.points);
        if (proj != null) {
            EditorScene.plotter.drawPoint(proj, Color.GREEN, 3);
            EditorScene.plotter.drawPoint(p, Color.YELLOW, 3);
            EditorScene.plotter.drawLine2(proj, p, Color.GREEN, 3);
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

            LayerProperties properties1 =
                    PropertiesFactory.getInstance()
                            .createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
            properties1.setSharpness(0.5f);
            LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1);

            ArrayList<LayerBlock> blocks = new ArrayList<>();
            blocks.add(block1);

            BodyInit bodyInit =
                    new BulletInit(
                            new TransformInit(
                                    new LinearVelocityInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), u.mul(80f)),
                                    400 / 32f,
                                    480 / 32f,
                                    (float) (angle + Math.PI)),
                            true);
            GameEntity gameEntity =
                    GameEntityFactory.getInstance()
                            .createGameEntity(
                                    400 / 32f,
                                    100 / 32f,
                                    (float) (angle + Math.PI),
                                    false,
                                    bodyInit,
                                    blocks,
                                    BodyDef.BodyType.DynamicBody,
                                    "Projectile");
            GameGroup proj = new GameGroup(GroupType.OTHER, gameEntity);
            gameEntity.getUseList().add(new Projectile(ProjectileType.SHARP_WEAPON));
            addGameGroup(proj);
        }
    }

    private void explosionTest(TouchEvent touchEvent) {
        float x = touchEvent.getX();
        float y = touchEvent.getY();
        if (touchEvent.isActionDown()) {
            this.worldFacade.createExplosion(null, x, y, 1f, 0.3f, 0.3f, 0.2f, 100f, 0.1f, 1f, 1f, 0f);
        }
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onPinchZoomStarted(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
    }

    @Override
    public void onPinchZoom(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
    }

    @Override
    public void onPinchZoomFinished(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
    }

    @Override
    public void onScrollStarted(
            ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
    }

    @Override
    public void onScroll(
            ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
    }

    @Override
    public void onScrollFinished(
            ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
    }

    private void testMesh() {
        ArrayList<Vector2> list = VerticesFactory.createPolygon(0, 0, 0, 2, 2, 64);
        float[] data = MeshFactory.getInstance().computeData(list);
        Mesh theMesh =
                new Mesh(
                        400,
                        240,
                        data,
                        data.length / 3,
                        DrawMode.TRIANGLES,
                        ResourceManager.getInstance().vbom);
        attachChild(theMesh);
    }

    private void createGround() {
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        List<Vector2> vertices1 = new ArrayList<>();
        vertices1.add(obtain(-400, 0));
        vertices1.add(obtain(-400, 20));
        vertices1.add(obtain(400, 20));
        vertices1.add(obtain(400, 0));

   /* List<Vector2> vertices2 = new ArrayList<>();
    vertices2.add(obtain(200, 15));
    vertices2.add(obtain(200, 20));
    vertices2.add(obtain(340, 20));
    vertices2.add(obtain(340, 15));

    List<Vector2> vertices3 = new ArrayList<>();
    vertices3.add(obtain(100, 18));
    vertices3.add(obtain(100, 20));
    vertices3.add(obtain(50, 20));
    vertices3.add(obtain(50, 18));

    List<Vector2> vertices4 = new ArrayList<>();
    vertices4.add(obtain(140, 18));
    vertices4.add(obtain(140, 16));
    vertices4.add(obtain(50, 16));
    vertices4.add(obtain(50, 18));*/

        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices1,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(13)),
                        0,
                        0,
                        false));
   /* blocks.add(
        BlockFactory.createLayerBlock(
            vertices2,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(1)),
            0,
            1,
            false));

    blocks.add(
        BlockFactory.createLayerBlock(
            vertices3,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(1)),
            0,
            2,
            false));
    blocks.add(
        BlockFactory.createLayerBlock(
            vertices4,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(3)),
            0,
            3,
            false));
*/
        GameGroup groundGroup =
                GameEntityFactory.getInstance()
                        .createGameGroupTest(
                                blocks,
                                new Vector2(400 / 32f, 0),
                                BodyDef.BodyType.StaticBody,
                                "Ground",
                                OBJECTS_MIDDLE_CATEGORY,
                                GroupType.GROUND);
        this.worldFacade.setGroundGroup(groundGroup);
    }

    private void createTestUnit() {
        List<Vector2> vertices1 = VerticesFactory.createRectangle(60, 60);
        LayerProperties properties1 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(2));
        LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1, 0);
        List<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block1);

        gameGroup1 =
                GameEntityFactory.getInstance()
                        .createGameGroupTest(
                                blocks,
                                new Vector2(100f / 32f, 200 / 32f),
                                BodyDef.BodyType.DynamicBody,
                                GroupType.OTHER);

        GameEntity gameEntity1 = gameGroup1.getGameEntityByIndex(0);
        gameEntity1.setCenter(new Vector2());
        gameEntity1.setName("test");

        List<Vector2> vertices2 = VerticesFactory.createRectangle(20, 200);
        LayerProperties properties2 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
        LayerBlock block2 = BlockFactory.createLayerBlock(vertices2, properties2, 1, 0);
        List<LayerBlock> blocks2 = new ArrayList<>();
        blocks2.add(block2);
        BodyInit bodyInit =
                new TransformInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), 100f / 32f, 200 / 32f, 0);
        GameEntity gameEntity2 =
                GameEntityFactory.getInstance()
                        .createGameEntity(
                                100f / 32f,
                                200 / 32f,
                                0f,
                                false,
                                bodyInit,
                                blocks2,
                                BodyDef.BodyType.DynamicBody,
                                "test 2");
        gameEntity2.setCenter(new Vector2());
        gameGroup1.addGameEntity(gameEntity2);
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.localAnchorA.set(0, 0);
        revoluteJointDef.localAnchorB.set(0, 2f);
        revoluteJointDef.collideConnected = false;
        this.worldFacade.addJointToCreate(revoluteJointDef, gameEntity1, gameEntity2);
        // this.worldFacade.addNonCollidingPair(gameEntity1,gameEntity2);

        List<Vector2> vertices3 = VerticesFactory.createRectangle(40, 20);
        LayerProperties properties3 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(0));
        LayerBlock block3 = BlockFactory.createLayerBlock(vertices3, properties3, 1, 0);
        List<LayerBlock> blocks3 = new ArrayList<>();
        blocks3.add(block3);
        GameGroup gameGroup =
                GameEntityFactory.getInstance()
                        .createGameGroupTest(
                                blocks3,
                                new Vector2(300f / 32f, 200 / 32f),
                                BodyDef.BodyType.DynamicBody,
                                GroupType.OTHER);
        GameEntity gameEntity3 = gameGroup.getGameEntityByIndex(0);
        gameEntity3.setName("small rectangle");
        gameEntity3.getUseList().add(new Stabber());
        gameEntity3.setCenter(new Vector2());
        gameEntity1.setCenter(new Vector2());
        if (true) {
            for (LayerBlock layerBlock : gameEntity3.getBlocks()) {
                Collections.shuffle(layerBlock.getBlockGrid().getCoatingBlocks());
                layerBlock.getBlockGrid().getCoatingBlocks().forEach(g -> g.setTemperature(4000));
            }
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
                this.worldFacade.performScreenCut(point1.mul(1 / 32f), point2.mul(1 / 32f));
                point1 = null;
                point2 = null;
                line.detachSelf();
            }
        }
    }

    public PlayerSpecialAction getSpecialAction() {
        return specialAction;
    }

    public void setSpecialAction(PlayerSpecialAction action) {
        this.specialAction = action;
        if (specialAction == PlayerSpecialAction.Stab) {
            getHand().setHoldingAngle(90);
        }
        if (specialAction == PlayerSpecialAction.Slash) {
            getHand().setHoldingAngle(0);
        }
        if (specialAction == PlayerSpecialAction.Fire) {
            getHand().setHoldingAngle(0);
        }
        float forceFactor = 3000f;
        Missile missile = null;

        Hand h = PlayScene.this.getHand();
        if (h != null && h.getMouseJoint() != null) {
            if (h.getGrabbedEntity().hasUsage(Missile.class)) {
                missile = h.getGrabbedEntity().getUsage(Missile.class);
            }

            Missile finalMissile = missile;
            userInterface
                    .getPanel()
                    .allocateController(
                            800 - 64 / 2f,
                            64 / 2f,
                            ControlElement.Type.AnalogController,
                            new ControllerAction() {
                                @Override
                                public void controlMoved(float pX, float pY) {
                                    if (finalMissile != null) {
                                        finalMissile.setSteerValue(pY);
                                    }
                                }

                                @Override
                                public void controlClicked() {
                                }

                                @Override
                                public void controlReleased() {
                                    if (finalMissile != null) {
                                        finalMissile.setSteerValue(0);
                                    }
                                }
                            });
        }

        if (getHand() != null) {
            getHand().setForceFactor(forceFactor);
        }
    }


    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
        if (playerAction == PlayerAction.Drag) {
            Hand h = getHand();
            if (h != null && h.getMouseJoint() != null) {
                h.getMouseJoint().setMaxForce(h.getGrabbedEntity().getMassOfGroup() * 1000);
            }
        }
    }

    public void onUsagesUpdated() {
        getActivity().runOnUiThread(() -> {
            List<PlayerSpecialAction> usageList = new ArrayList<>();
            Hand h = getHand();
            if (h != null && h.getGrabbedEntity() != null) {
                h.getGrabbedEntity()
                        .getUseList()
                        .forEach(
                                u -> {
                                    if (this.playerAction == PlayerAction.Hold) {
                                        if (u.getActions() != null && !u.getActions().isEmpty()) {
                                            usageList.addAll(u.getActions());
                                        }
                                    }
                                });
            }

            PlayerSpecialAction defaultAction = specialAction == null ? usageList.stream().filter(e -> e.selectableByDefault).findFirst().orElse(null) : specialAction;
            this.setSpecialAction(usageList.contains(defaultAction) ? defaultAction : PlayerSpecialAction.None);

            getActivity().getUiController().onUsagesUpdated(usageList, defaultAction);
        });
    }

    public GameActivity getActivity() {
        return ResourceManager.getInstance().activity;
    }

    public Pair<GameEntity, Vector2> getTouchedEntity(TouchEvent touchEvent, boolean withHold) {
        GameEntity result = null;
        Vector2 anchor = null;
        float minDis = 12f;
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (entity.getBody() != null
                        && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
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

    public void lockSaving() {
        this.savingBox.setLockedSave(true);
    }

    public void unlockSaving() {
        this.savingBox.setLockedSave(false);
    }

    public void goToScene(SceneType sceneType) {
        ((MainScene) this.mParentScene).goToScene(sceneType);
    }

    public void createLastItem() {
        createItemFromFile(EditorScene.SAVE_MUT, false, false);
    }

    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {
        if (playerSpecialAction == PlayerSpecialAction.Trigger) {
            if (this.hand != null) {
                if (hand.getGrabbedEntity() != null && hand.getGrabbedEntity().hasUsage(TimeBomb.class)) {
                    TimeBomb timeBomb = hand.getGrabbedEntity().getUsage(TimeBomb.class);
                    timeBomb.onTriggerReleased();
                    hand.releaseGrabbedEntity(true);
                }
                if (hand.getGrabbedEntity() != null && hand.getGrabbedEntity().hasUsage(RocketLauncher.class)) {
                    RocketLauncher rocketLauncher = hand.getGrabbedEntity().getUsage(RocketLauncher.class);
                    rocketLauncher.onTriggerPulled();
                    onUsagesUpdated();
                }
            }
        } else if (playerSpecialAction == PlayerSpecialAction.SwitchOn) {
            if (this.hand != null) {
                if (hand.getGrabbedEntity() != null && hand.getGrabbedEntity().hasUsage(LiquidContainer.class)) {
                    LiquidContainer liquidContainer = hand.getGrabbedEntity().getUsage(LiquidContainer.class);
                    liquidContainer.open();
                    onUsagesUpdated();
                }
                if (hand.getGrabbedEntity() != null && hand.getGrabbedEntity().hasUsage(Rocket.class)) {
                    Rocket rocket = hand.getGrabbedEntity().getUsage(Rocket.class);
                    if (!rocket.isOn()) {
                        hand.launchRocket();
                        Invoker.addJointDestructionCommand(hand.getGrabbedEntity().getParentGroup(), hand.getMouseJoint());
                    }
                }
            }
        } else if (playerSpecialAction == PlayerSpecialAction.SwitchOff) {
            if (this.hand != null) {
                if (hand.getGrabbedEntity().hasUsage(LiquidContainer.class)) {
                    LiquidContainer liquidContainer = hand.getGrabbedEntity().getUsage(LiquidContainer.class);
                    liquidContainer.close();
                    onUsagesUpdated();
                }
            }
        } else {
            setSpecialAction(playerSpecialAction);
        }
    }

    public void onMirrorButtonClicked() {
        if (hand != null && hand.getGrabbedEntity() != null) {
            Invoker.addBodyMirrorCommand(hand.getGrabbedEntity());
            //  hand.getGrabbedEntity().tryDynamicMirror();
        }
    }
}
