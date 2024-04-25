package com.evolgames.scenes;

import static com.evolgames.entities.factories.MaterialFactory.IGNIUM;
import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.physics.CollisionUtils.OBJECTS_MIDDLE_CATEGORY;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import android.opengl.GLES20;
import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.activity.GameActivity;
import com.evolgames.activity.NativeUIController;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.Plotter;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.basics.SpecialEntityType;
import com.evolgames.entities.blocks.DecorationBlock;
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
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.serialization.SavingBox;
import com.evolgames.entities.usage.Bomb;
import com.evolgames.entities.usage.Bow;
import com.evolgames.entities.usage.ImpactBomb;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.MotorControl;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.entities.usage.ProjectileType;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.physics.entities.Touch;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.MyColorUtils;
import com.evolgames.utilities.Vector2Utils;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class PlayScene extends PhysicsScene implements IAccelerationListener, ScrollDetector.IScrollDetectorListener, PinchZoomDetector.IPinchZoomDetectorListener {
    public static float groundY;
    public static boolean pause = false;
    public static Plotter plotter;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private final Random random = new Random();
    private final boolean[] motorSounds = new boolean[3];
    private final EnumSet<PlayerSpecialAction> aimSet = EnumSet.of(PlayerSpecialAction.SingleShot, PlayerSpecialAction.AimLight, PlayerSpecialAction.AimHeavy, PlayerSpecialAction.Shoot_Arrow, PlayerSpecialAction.FireHeavy, PlayerSpecialAction.FireLight, PlayerSpecialAction.Throw);
    private PlayerAction playerAction = PlayerAction.Drag;
    private PlayerSpecialAction specialAction = PlayerSpecialAction.None;
    private Vector2 point1, point2;
    private Line line;
    private ArrayList<Vector2> points;
    private SavingBox savingBox;
    private float mPinchZoomStartedCameraZoomFactor;
    private boolean chaseActive;
    private boolean usesActive = true;
    private boolean effectsActive;
    private boolean creating;
    private PhysicsConnector physicsConnector;
    private LayerBlock glueBlock;
    private GameEntity glueEntity;
    private Sprite aimSprite;
    private GameEntity usableEntity;

    public PlayScene(Camera pCamera) {
        super(pCamera, SceneType.PLAY);
        this.savingBox = new SavingBox(this);
        plotter = new Plotter();
        this.attachChild(plotter);
        this.mScrollDetector = new SurfaceScrollDetector(this);
        this.mPinchZoomDetector = new PinchZoomDetector(this);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        super.onManagedUpdate(1 / 60f);
        step++;
        checkMotorSounds();
        if (this.savingBox != null) {
            this.savingBox.onStep();
        }
        if (hand != null) {
            hand.onUpdate();
        }
        if (false) {
            cullTest();
        }

        if (false) {
            bluntDamageTest();
        }

        if (false) {
            pulverizationTest();
        }
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
        hand.onSceneTouchEvent(touchEvent);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        boolean result = super.onSceneTouchEvent(pSceneTouchEvent);
        this.processTouchEvent(pSceneTouchEvent);
        return false;
    }

    protected void processTouchEvent(TouchEvent touchEvent) {
        if (this.playerAction == PlayerAction.Create) {
            ItemMetaData itemToCreate = ResourceManager.getInstance().getSelectedItemMetaData();
            if (itemToCreate != null) {
                if (touchEvent.isActionDown()) {
                    creating = true;
                }
                if (touchEvent.isActionUp() && creating) {
                    float x = touchEvent.getX();
                    float y = touchEvent.getY();
                    ToolModel toolModel = loadToolModel(itemToCreate.getFileName(), false, !itemToCreate.isUserCreated());
                    toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(x, y).filter(OBJECT, OBJECT).build()));
                    boolean checkEmpty = checkEmpty(toolModel, false);
                    if (!checkEmpty) {
                        getActivity().getUiController().showHint(R.string.insufficient_space, NativeUIController.HintType.WARNING);
                    } else {
                        createTool(toolModel, false);
                        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().createSound, 0.5f, 10);
                    }
                    stopCreating();
                    ResourceManager.getInstance().activity.getUiController().onItemCreated();
                }
            }
        } else {

            if (this.specialAction == PlayerSpecialAction.effectMeteor) {
                if (touchEvent.isActionDown()) {
                    setScrollerEnabled(false);
                    createMeteorite(touchEvent.getX() / 32f, 1000f);
                    ResourceManager.getInstance().meteorSound.play();
                }
                if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
                    setScrollerEnabled(true);
                }
            }
            if (this.specialAction == PlayerSpecialAction.effectFrost) {
                processFrostEffect(touchEvent);
            }
            if (this.specialAction == PlayerSpecialAction.effectFireBolt) {
                processExplosionEffect(touchEvent);
            }
            if (this.specialAction == PlayerSpecialAction.effectCut) {
                processSlicing(touchEvent);
            }
            if (this.specialAction == PlayerSpecialAction.effectGlue) {
                processGluing(touchEvent);
            }
            if (!effectsActive) {
                if (playerAction == PlayerAction.Drag || playerAction == PlayerAction.Hold || playerAction == PlayerAction.Select) {
                    processHandling(touchEvent);
                }
            }

        }
        if (mPinchZoomDetector != null) {
            mPinchZoomDetector.onTouchEvent(touchEvent);

            mScrollDetector.onTouchEvent(touchEvent);
        } else {
            mScrollDetector.onTouchEvent(touchEvent);
        }
    }

    public void stopCreating() {
        creating = false;
       PlayerAction playerAction = ResourceManager.getInstance().activity.getUiController().getHoldDragState();
        setPlayerAction(playerAction);
    }

    private void processFrostEffect(TouchEvent touchEvent) {
        if (touchEvent.isActionDown()) {
            worldFacade.frostParticleWrapper(new Vector2(-16, 0), new Vector2(16, 0), 400, 600);
            worldFacade.getFrostParticleWrapper().update(touchEvent.getX(), touchEvent.getY());
            setScrollerEnabled(false);
            ResourceManager.getInstance().windSound.play();
        }
        if (touchEvent.isActionMove()) {
            worldFacade.getFrostParticleWrapper().update(touchEvent.getX(), touchEvent.getY());
        }
        if (touchEvent.isActionUp() || touchEvent.isActionOutside() || touchEvent.isActionCancel()) {
            worldFacade.getFrostParticleWrapper().detach();
            setScrollerEnabled(true);
            ResourceManager.getInstance().windSound.pause();
        }
    }

    @Override
    public void populate() {
        createRagDoll(400, 300);
        String map = ResourceManager.getInstance().getMapString();
        if ("open".equalsIgnoreCase(map)) {
            createOpenGround();
        }
        if ("marble".equalsIgnoreCase(map)) {
            createMarbleGround();
        }
        if ("wood".equalsIgnoreCase(map)) {
            createWoodGround();
        }
        this.hand = new Hand(this);
        onUsagesUpdated();


    }

    @Override
    public void detach() {
        super.detach();
        this.worldFacade.getTimedCommands().clear();
        ResourceManager.getInstance().activity.runOnUpdateThread(this::hideAimSpriteDirect);
        System.gc();
    }

    @Override
    public void onPause() {
        this.savingBox.onScenePause();
        this.savingBox.saveSelf();
        this.detach();
    }

    public void refresh() {
        this.detach();
        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
            this.populate();
            ((SmoothCamera) this.mCamera).setCenterDirect(400, 240);
        });


    }

    @Override
    public void onResume() {
        createUserInterface();
        this.savingBox = new SavingBox(this);
        this.savingBox.loadSelf();
        this.savingBox.onSceneResume();
    }


    @Override
    public void createUserInterface() {
    }

    private void bluntDamageTest() {
        if (step % 60 == 10) {
            GameEntity head = ragdoll.leftFoot;
            List<Vector2> pts = Vector2Utils.generateRandomPointsInsidePolygon(10, head.getBlocks().get(0).getVertices().get(0), head.getBlocks().get(0), head);
            for (Vector2 p : pts) {
                EditorScene.plotter.drawPointOnEntity(p, Color.PINK, head.getMesh());
                // 0.4f+ (float) (Math.random()*0.3f), (float) (Math.random()*0.3f) , (float)
                // (0.1f+Math.random()*0.2f)
                Color color = new Color(0.6f + (float) (Math.random() * 0.4f), 0f, (float) (0.2f + Math.random() * 0.4f));
                Color skin = new Color(head.getBlocks().get(0).getProperties().getDefaultColor());
                skin.setAlpha((float) (0.3f * Math.random()));
                MyColorUtils.blendColors(color, color, skin);
                this.worldFacade.applyStain(head, p.x, p.y, head.getBlocks().get(0), color, 0f, 14, true);
            }
            head.redrawStains();
        }
    }

    private void pulverizationTest() {
        if (step == 180) {
            ragdoll.getGameEntities().forEach(e -> {
                if (e.getType() == SpecialEntityType.Head) {
                    e.getBlocks().forEach(b -> this.worldFacade.pulverizeBlock(b, e));
                    this.worldFacade.destroyGameEntity(e, true);
                }
            });
        }
    }

    private void createMeteorite(float x, float y) {
        int n = 5 + random.nextInt(10);
        float size = (float) (Math.random() * 2 + 4);
        float angle = (float) (Math.PI * 2 * Math.random());
        List<Vector2> points = VerticesFactory.createPolygon(0, 0, angle, size, size, n);
        LayerProperties properties1 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(IGNIUM));
        properties1.setTenacity(0.1f);
        LayerBlock block = BlockFactory.createLayerBlock(points, properties1, 0);

        ArrayList<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block);
        Vector2 u = new Vector2(0, -1);
        BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), u.mul(500)), x, y, 0), true);
        GameEntity meteorite = GameEntityFactory.getInstance().createGameEntity(x / 32f, y / 32f, 0, false, bodyInit, blocks, BodyDef.BodyType.DynamicBody, "Meteorite");
        Projectile projectileUse = new Projectile(ProjectileType.METEOR);
        projectileUse.setActive(true);
        meteorite.getUseList().add(projectileUse);
        GameGroup proj = new GameGroup(GroupType.OTHER, meteorite);
        addGameGroup(proj);
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

            LayerProperties properties1 = PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1));
            properties1.setSharpness(0.5f);
            LayerBlock block1 = BlockFactory.createLayerBlock(vertices1, properties1, 1);

            ArrayList<LayerBlock> blocks = new ArrayList<>();
            blocks.add(block1);

            BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), u.mul(80f)), 400 / 32f, 480 / 32f, (float) (angle + Math.PI)), true);
            GameEntity gameEntity = GameEntityFactory.getInstance().createGameEntity(400 / 32f, 100 / 32f, (float) (angle + Math.PI), false, bodyInit, blocks, BodyDef.BodyType.DynamicBody, "Projectile");
            GameGroup proj = new GameGroup(GroupType.OTHER, gameEntity);
            gameEntity.getUseList().add(new Projectile(ProjectileType.SHARP_WEAPON));
            addGameGroup(proj);
        }
    }

    private void processExplosionEffect(TouchEvent touchEvent) {
        float x = touchEvent.getX();
        float y = touchEvent.getY();
        if (touchEvent.isActionDown()) {
            this.worldFacade.createExplosion(null, x / 32f, y / 32f, 1f, 0.3f, 0.3f, 10f, 0.001f, 1f, 0.25f, 1f, 0f);
        }
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
    }

    private void testMesh() {
        ArrayList<Vector2> list = VerticesFactory.createPolygon(0, 0, 0, 2, 2, 64);
        float[] data = MeshFactory.getInstance().computeData(list);
        Mesh theMesh = new Mesh(400, 240, data, data.length / 3, DrawMode.TRIANGLES, ResourceManager.getInstance().vbom);
        attachChild(theMesh);
    }

    private void createOpenGround() {
        ((SmoothCamera) ResourceManager.getInstance().firstCamera).setBounds(-Float.MAX_VALUE, 0, Float.MAX_VALUE, Float.MAX_VALUE);
        PlayScene.groundY = 30f;
        List<GameEntity> entities = new CopyOnWriteArrayList<>();
        for (int i = -100; i < 100; i++) {
            // for(int i=2;i<3;i++) {
            ArrayList<LayerBlock> blocks = new ArrayList<>();
            List<Vector2> vertices = new ArrayList<>();
            vertices.add(obtain(-200, -50));
            vertices.add(obtain(-200, 30));
            vertices.add(obtain(200, 30));
            vertices.add(obtain(200, -50));
            blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.ASPHALT)), 0, 0, false));

            vertices = new ArrayList<>();
            vertices.add(obtain(-100, 30));
            vertices.add(obtain(-100, 29));
            vertices.add(obtain(100, 29));
            vertices.add(obtain(100, 30));
            DecorationBlock decorationBlock = new DecorationBlock();
            DecorationProperties decorationProperties = new DecorationProperties();
            decorationProperties.setDefaultColor(new Color(0.8f, 0.8f, 0.8f));
            decorationBlock.initialization(vertices, decorationProperties, 0);
            blocks.get(0).addAssociatedBlock(decorationBlock);

            BodyInit bodyInit = new TransformInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), i * 400f / 32f, 0, 0);
            GameEntity entity = GameEntityFactory.getInstance().createGameEntity(i * 400f / 32f, 0, 0, false, bodyInit, blocks, BodyDef.BodyType.StaticBody, "Ground Part " + i);
            entities.add(entity);

        }
        GameGroup groundGroup = new GameGroup(GroupType.GROUND, entities);
        this.addGameGroup(groundGroup);
        this.worldFacade.setGroundGroup(groundGroup);
    }

    private void createWoodGround() {
        PlayScene.groundY = 20f;
        ((SmoothCamera) ResourceManager.getInstance().firstCamera).setBounds(-400, 0, 1200, 980);
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        List<Vector2> vertices = new ArrayList<>();
        vertices.add(obtain(-800, -50));
        vertices.add(obtain(-800, 20));
        vertices.add(obtain(800, 20));
        vertices.add(obtain(800, -50));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)), 0, 0, false));

        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 20));
        vertices.add(obtain(-780, 20));
        vertices.add(obtain(-780, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)), 1, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(800, 960));
        vertices.add(obtain(800, 20));
        vertices.add(obtain(780, 20));
        vertices.add(obtain(780, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)), 2, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 980));
        vertices.add(obtain(800, 980));
        vertices.add(obtain(800, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)), 3, 0, false));

        for (LayerBlock layerBlock : blocks) {
            layerBlock.getProperties().setDefaultColor(new Color(222f / 255f, 184f / 255f, 135 / 255f));
        }
        GameGroup groundGroup = GameEntityFactory.getInstance().createGameGroupTest(blocks, new Vector2(400 / 32f, 0), BodyDef.BodyType.StaticBody, "Ground", OBJECTS_MIDDLE_CATEGORY, GroupType.GROUND);
        this.worldFacade.setGroundGroup(groundGroup);
    }

    private void createMarbleGround() {
        PlayScene.groundY = 60f;

        ((SmoothCamera) ResourceManager.getInstance().firstCamera).setBounds(-440, 0, 1240, 980);
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        List<Vector2> vertices = new ArrayList<>();
        vertices.add(obtain(-800, -50));
        vertices.add(obtain(-800, 20));
        vertices.add(obtain(800, 20));
        vertices.add(obtain(800, -50));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 0, 0, false));
        vertices = new ArrayList<>();
        vertices.add(obtain(-780, 20));
        vertices.add(obtain(-780, 40));
        vertices.add(obtain(780, 40));
        vertices.add(obtain(780, 20));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 1, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 40));
        vertices.add(obtain(-760, 60));
        vertices.add(obtain(760, 60));
        vertices.add(obtain(760, 40));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 2, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 960));
        vertices.add(obtain(-760, 60));
        vertices.add(obtain(-720, 60));
        vertices.add(obtain(-720, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 3, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(760, 960));
        vertices.add(obtain(760, 60));
        vertices.add(obtain(720, 60));
        vertices.add(obtain(720, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 4, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 980));
        vertices.add(obtain(800, 980));
        vertices.add(obtain(800, 960));
        blocks.add(BlockFactory.createLayerBlock(vertices, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)), 5, 0, false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-780, 20));
        vertices.add(obtain(-780, 21));
        vertices.add(obtain(780, 21));
        vertices.add(obtain(780, 20));
        DecorationBlock decorationBlock = new DecorationBlock();
        DecorationProperties decorationProperties = new DecorationProperties();
        decorationProperties.setDefaultColor(new Color(0.8f, 0.8f, 0.8f));
        decorationBlock.initialization(vertices, decorationProperties, 0);
        blocks.get(1).addAssociatedBlock(decorationBlock);


        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 40));
        vertices.add(obtain(-760, 41));
        vertices.add(obtain(760, 41));
        vertices.add(obtain(760, 40));
        decorationBlock = new DecorationBlock();
        decorationProperties = new DecorationProperties();
        decorationProperties.setDefaultColor(new Color(0.8f, 0.8f, 0.8f));
        decorationBlock.initialization(vertices, decorationProperties, 0);
        blocks.get(2).addAssociatedBlock(decorationBlock);


        GameGroup groundGroup = GameEntityFactory.getInstance().createGameGroupTest(blocks, new Vector2(400 / 32f, 0), BodyDef.BodyType.StaticBody, "Ground", OBJECTS_MIDDLE_CATEGORY, GroupType.GROUND);
        this.worldFacade.setGroundGroup(groundGroup);
    }

    private void processSlicing(TouchEvent touchEvent) {
        if (touchEvent.isActionDown()) {
            point1 = new Vector2(touchEvent.getX(), touchEvent.getY());
            point2 = new Vector2(point1);
            line = new Line(point1.x, point1.y, point2.x, point2.y, 3, ResourceManager.getInstance().vbom);

            line.setColor(Color.RED);
            line.setZIndex(999);
            this.attachChild(line);
            this.sortChildren();
            setScrollerEnabled(false);
        }
        if (touchEvent.isActionMove()) {
            if (point2 != null) {
                point2.set(touchEvent.getX(), touchEvent.getY());
                line.setPosition(point1.x, point1.y, point2.x, point2.y);
            }
        }
        if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
            setScrollerEnabled(true);
            if (point1 != null && point2 != null) {
                this.worldFacade.performScreenCut(point1.mul(1 / 32f), point2.mul(1 / 32f));
                point1 = null;
                point2 = null;
                line.detachSelf();
            }
        }
    }

    private void processGluing(TouchEvent touchEvent) {

        if (touchEvent.isActionDown()) {
            setScrollerEnabled(false);
            Selection touchData = this.getTappedEntity(touchEvent);
            if (touchData != null) {
                glueEntity = touchData.gameEntity;
                point1 = touchData.anchor.cpy();
                glueBlock = touchData.layerBlock;
                point2 = new Vector2(point1);
                line = new Line(point1.x, point1.y, point2.x, point2.y, 3, ResourceManager.getInstance().vbom);
                line.setColor(Color.BLUE);
                line.setZIndex(999);
                this.attachChild(line);
                this.sortChildren();
            }
        }
        if (touchEvent.isActionMove()) {
            if (point2 != null) {
                point2.set(touchEvent.getX(), touchEvent.getY());
                line.setPosition(point1.x, point1.y, point2.x, point2.y);
            }
        }
        if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
            setScrollerEnabled(true);
            Selection touchData = this.getTappedEntity(touchEvent);
            if (point1 != null && point2 != null && glueEntity != null && touchData != null && touchData.gameEntity != null && glueEntity != touchData.gameEntity) {
                Touch touch = worldFacade.areTouching(glueEntity, touchData.gameEntity);
                if (touch != null) {
                    line.setPosition(point1.x, point1.y, touchData.anchor.x, touchData.anchor.y);
                    this.worldFacade.glueEntities(glueEntity, touchData.gameEntity, touch.getPoint());
                    ResourceManager.getInstance().glueSound.play();
                }
            }
            point1 = null;
            point2 = null;
            if (line != null) {
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
            if (!getHand().getGrabbedEntity().isMirrored()) {
                getHand().setHoldingAngle(90);
            } else {
                getHand().setHoldingAngle(-90);
            }
        }
        if (specialAction == PlayerSpecialAction.Slash) {
            getHand().setHoldingAngle(0);
        }
        if (specialAction == PlayerSpecialAction.FireLight) {
            getHand().setHoldingAngle(0);
        }
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
        if(this.playerAction==PlayerAction.Drag){
            this.hand.killTopOfStack();
        }
    }

    public void onUsagesUpdated() {
        getActivity().runOnUiThread(() -> {
            Set<PlayerSpecialAction> usageList = new HashSet<>();
            PlayerSpecialAction defaultAction = null;
            Hand h = getHand();
            if (usesActive) {
                if (h != null && h.hasSelectedEntity()) {
                    h.getSelectedEntity().getUseList().forEach(u -> {
                        if (u.getActions() != null && !u.getActions().isEmpty()) {
                            usageList.addAll(u.getActions().stream().filter(e -> e.fromDistance).collect(Collectors.toList()));
                        }
                    });
                } else if (playerAction == PlayerAction.Hold && h != null && h.getGrabbedEntity() != null) {
                    h.getGrabbedEntity().getUseList().forEach(u -> {
                        if (u.getActions() != null && !u.getActions().isEmpty()) {
                            usageList.addAll(u.getActions());
                        }
                    });
                }

                defaultAction = specialAction == null ? usageList.stream().filter(e -> e.selectableByDefault).findFirst().orElse(null) : specialAction;
                if (hand != null) {
                    if (hand.hasSelectedEntity()) {
                        usageList.add(PlayerSpecialAction.Unselect);
                    } else if (playerAction == PlayerAction.Hold && hand.getGrabbedEntity() != null) {
                        usageList.add(PlayerSpecialAction.Drop);
                    }
                }
                this.setSpecialAction(usageList.contains(defaultAction) ? defaultAction : PlayerSpecialAction.None);
            }
            if (effectsActive) {
                usageList.add(PlayerSpecialAction.effectCut);
                usageList.add(PlayerSpecialAction.effectGlue);
                usageList.add(PlayerSpecialAction.effectFireBolt);
                if (ResourceManager.getInstance().getMapString().equalsIgnoreCase("open")) {
                    usageList.add(PlayerSpecialAction.effectMeteor);
                }
                usageList.add(PlayerSpecialAction.effectFrost);
            }
            if (hand != null && usableEntity != hand.getUsableEntity() && hand.getUsableEntity() != null) {
                if (usageList.contains(PlayerSpecialAction.Trigger)) {
                    if (hand.getUsableEntity().hasUsage(TimeBomb.class)) {
                        getActivity().getUiController().showHint(R.string.trigger_time_bomb_hint, NativeUIController.HintType.HINT);
                    }
                    if (hand.getUsableEntity().hasUsage(ImpactBomb.class)) {
                        getActivity().getUiController().showHint(R.string.trigger_impact_bomb_hint, NativeUIController.HintType.HINT);
                    }
                    if (hand.getUsableEntity().hasUsage(RocketLauncher.class)) {
                        getActivity().getUiController().showHint(R.string.trigger_rocket_launcher_hint, NativeUIController.HintType.HINT);
                    }
                }
                if (usageList.contains(PlayerSpecialAction.motorStop)) {
                    getActivity().getUiController().showHint(R.string.motor_hint, NativeUIController.HintType.HINT);
                }
                if (usageList.contains(PlayerSpecialAction.SwitchOn)) {
                    if (hand.getUsableEntity().hasUsage(LiquidContainer.class)) {
                        getActivity().getUiController().showHint(R.string.unseal_hint, NativeUIController.HintType.HINT);
                    }
                    if (hand.getUsableEntity().hasUsage(Rocket.class)) {
                        getActivity().getUiController().showHint(R.string.rocket_launch_hint, NativeUIController.HintType.HINT);
                    }
                }
                usableEntity = hand.getUsableEntity();
            }
            getActivity().getUiController().onUsagesUpdated(new ArrayList<>(usageList), defaultAction, usesActive);
        });
    }

    public GameActivity getActivity() {
        return ResourceManager.getInstance().activity;
    }

    public Selection getSelectedEntity(TouchEvent touchEvent) {
        List<Selection> list = new ArrayList<>();
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (!entity.getMesh().isVisible() || entity.isNotInteractive()) {
                    continue;
                }
                if (entity.getBody() != null && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                    Pair<Vector2, LayerBlock> vector2LayerBlockPair = entity.computeTouch(touchEvent, false);
                    if (vector2LayerBlockPair != null) {
                        float dis = vector2LayerBlockPair.first.dst(touchEvent.getX(), touchEvent.getY());
                        if (dis < 32f) {
                            list.add(new Selection(entity, vector2LayerBlockPair.second, dis, vector2LayerBlockPair.first));
                        }
                    }
                }
            }
        }
        list.sort(new SelectionComparator());
        return list.stream().findFirst().orElse(null);
    }

    public Selection getHeldEntity(TouchEvent touchEvent) {
        List<Selection> list = new ArrayList<>();
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (!entity.getMesh().isVisible() || entity.isNotInteractive()) {
                    continue;
                }
                if (entity.getBody() != null && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                    Pair<Vector2, LayerBlock> vector2LayerBlockPair = entity.computeTouch(touchEvent, true);
                    if (vector2LayerBlockPair != null) {
                        float dis = vector2LayerBlockPair.first.dst(touchEvent.getX(), touchEvent.getY());
                        if (dis < 32f) {
                            list.add(new Selection(entity, vector2LayerBlockPair.second, dis, vector2LayerBlockPair.first));
                        }
                    }
                }
            }
        }
        list.sort(new HoldingComparator());
        return list.stream().findFirst().orElse(null);
    }

    public Selection getDraggedEntity(TouchEvent touchEvent) {
        List<Selection> list = new ArrayList<>();
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (!entity.getMesh().isVisible() || entity.isNotInteractive()) {
                    continue;
                }
                if (entity.getBody() != null && entity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                    Pair<Vector2, LayerBlock> vector2LayerBlockPair = entity.computeTouch(touchEvent, false);
                    if (vector2LayerBlockPair != null) {
                        float dis = vector2LayerBlockPair.first.dst(touchEvent.getX(), touchEvent.getY());
                        if (dis < 32f) {
                            list.add(new Selection(entity, vector2LayerBlockPair.second, dis, vector2LayerBlockPair.first));
                        }
                    }
                }
            }
        }
        list.sort(new HoldingComparator());
        return list.stream().findFirst().orElse(null);
    }

    public Selection getTappedEntity(TouchEvent touchEvent) {
        List<Selection> list = new ArrayList<>();
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (!entity.getMesh().isVisible()) {
                    continue;
                }
                if (entity.getBody() != null) {
                    Pair<Vector2, LayerBlock> vector2LayerBlockPair = entity.computeTouch(touchEvent, false);
                    if (vector2LayerBlockPair != null) {
                        float dis = vector2LayerBlockPair.first.dst(touchEvent.getX(), touchEvent.getY());
                        if (dis < 32f) {
                            list.add(new Selection(entity, vector2LayerBlockPair.second, dis, vector2LayerBlockPair.first));
                        }
                    }
                }
            }
        }
        list.sort(new TappingComparator());
        return list.stream().findFirst().orElse(null);
    }

    public void lockSaving() {
        this.savingBox.setLockedSave(true);
    }

    public void unlockSaving() {
        this.savingBox.setLockedSave(false);
    }

    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {
        if (playerSpecialAction == PlayerSpecialAction.None) {
            setScrollerEnabled(true);
            setZoomEnabled(true);
        }
        if (!aimSet.contains(playerSpecialAction)) {
            hideAimSprite();
        }
        if (playerSpecialAction == PlayerSpecialAction.effectMeteor || playerSpecialAction == PlayerSpecialAction.effectFrost || playerSpecialAction == PlayerSpecialAction.effectCut || playerSpecialAction == PlayerSpecialAction.effectGlue || playerSpecialAction == PlayerSpecialAction.effectFireBolt) {
            getActivity().getUiController().showHint(playerSpecialAction.hintString, NativeUIController.HintType.HINT);
        }
        if (playerSpecialAction == PlayerSpecialAction.Stab || playerSpecialAction == PlayerSpecialAction.Slash || playerSpecialAction == PlayerSpecialAction.Smash) {
            getActivity().getUiController().showHint(playerSpecialAction.hintString, NativeUIController.HintType.HINT);
        }

        if (playerSpecialAction == PlayerSpecialAction.AimHeavy || playerSpecialAction == PlayerSpecialAction.FireHeavy) {
            getActivity().getUiController().showHint(playerSpecialAction.hintString, NativeUIController.HintType.HINT);
            createAimSprite(hand.getHeldEntity());
        } else if (playerSpecialAction == PlayerSpecialAction.AimLight || playerSpecialAction == PlayerSpecialAction.FireLight || playerSpecialAction == PlayerSpecialAction.Throw) {
            getActivity().getUiController().showHint(playerSpecialAction.hintString, NativeUIController.HintType.HINT);
            createAimSprite(hand.getGrabbedEntity());
        }

        if (playerSpecialAction == PlayerSpecialAction.FireHeavy || playerSpecialAction == PlayerSpecialAction.AimHeavy) {
            if (this.hand != null) {
                hand.holdEntity();
            }
        }

        if (playerSpecialAction == PlayerSpecialAction.motorMoveForward || playerSpecialAction == PlayerSpecialAction.motorMoveBackward || playerSpecialAction == PlayerSpecialAction.motorStop) {
            if (hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(MotorControl.class)) {
                    MotorControl motorControl = usableEntity.getUsage(MotorControl.class);
                    int state = playerSpecialAction == PlayerSpecialAction.motorMoveBackward ? -1 : playerSpecialAction == PlayerSpecialAction.motorMoveForward ? 1 : 0;
                    motorControl.setMotorState(state);
                }
            }
        }
        if (playerSpecialAction == PlayerSpecialAction.Shoot_Arrow) {
            if (this.hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(Bow.class)) {
                    Bow bow = usableEntity.getUsage(Bow.class);
                    bow.onArrowsReleased(this);
                    onUsagesUpdated();
                }
            }
        } else if (playerSpecialAction == PlayerSpecialAction.Unselect) {
            if (this.hand != null) {
                setScrollerEnabled(true);
                ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
                    this.hand.deselectDirect(true);
                });
            }
        } else if (playerSpecialAction == PlayerSpecialAction.Drop) {
            if (this.hand != null) {
                this.hand.releaseGrabbedEntity(true, true);
            }
        } else if (playerSpecialAction == PlayerSpecialAction.Trigger) {
            if (this.hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(Bomb.class)) {
                    Bomb bomb = usableEntity.getUsage(Bomb.class);
                    bomb.onTriggered(worldFacade);
                    onUsagesUpdated();
                    resetTouchHold();
                }
                if (usableEntity != null && usableEntity.hasUsage(RocketLauncher.class)) {
                    RocketLauncher rocketLauncher = usableEntity.getUsage(RocketLauncher.class);
                    rocketLauncher.onTriggerPulled(this);
                    onUsagesUpdated();
                }
                if (usableEntity != null && usableEntity.hasUsage(Shooter.class)) {
                    Shooter shooter = usableEntity.getUsage(Shooter.class);
                    shooter.onTriggerPulled(this);
                    onUsagesUpdated();
                }
            }
        } else if (playerSpecialAction == PlayerSpecialAction.SingleShot) {
            if (usableEntity != null && usableEntity.hasUsage(Shooter.class)) {
                Shooter shooter = usableEntity.getUsage(Shooter.class);
                shooter.onTriggerPulled(this);
                shooter.onTriggerReleased();
                onUsagesUpdated();
            }
        } else if (playerSpecialAction == PlayerSpecialAction.SwitchOn) {
            if (this.hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(LiquidContainer.class)) {
                    LiquidContainer liquidContainer = usableEntity.getUsage(LiquidContainer.class);
                    liquidContainer.open();
                    onUsagesUpdated();
                }
                if (usableEntity != null && usableEntity.hasUsage(Rocket.class)) {
                    Rocket rocket = usableEntity.getUsage(Rocket.class);
                    if (!rocket.isOn()) {
                        hand.launchRocket();
                        if (hand.getMouseJoint() != null) {
                            Invoker.addJointDestructionCommand(usableEntity.getParentGroup(), hand.getMouseJoint());
                        }
                        onUsagesUpdated();
                    }
                }
            }
        } else if (playerSpecialAction == PlayerSpecialAction.SwitchOff) {
            if (this.hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(LiquidContainer.class)) {
                    LiquidContainer liquidContainer = usableEntity.getUsage(LiquidContainer.class);
                    liquidContainer.close();
                    onUsagesUpdated();
                }
            }
        } else {
            setSpecialAction(playerSpecialAction);
        }
    }

    private void createAimSprite(GameEntity entity) {
        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
            if (entity == null) {
                return;
            }
            if (aimSprite != null) {
                hideAimSpriteDirect();
            }
            aimSprite = new Sprite(entity.getX(), entity.getY(), ResourceManager.getInstance().focusTextureRegion, ResourceManager.getInstance().vbom);
            aimSprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            this.attachChild(aimSprite);
            aimSprite.setAlpha(0.01f);
            aimSprite.setWidth(32 * 16);
            aimSprite.setHeight(32 * 16);
            aimSprite.setZIndex(-1);
            if (entity.getBody() != null) {
                physicsConnector = new PhysicsConnector(aimSprite, entity.getBody());
                getPhysicsWorld().registerPhysicsConnector(physicsConnector);
            }
        });
    }

    public void resetTouchHold() {
        this.setPlayerAction(PlayerAction.Drag);
        ResourceManager.getInstance().activity.getUiController().resetTouchHold();
    }

    public void onMirrorButtonClicked() {
        if (hand != null && hand.getUsableEntity() != null) {
            Invoker.addBodyMirrorCommand(hand.getUsableEntity());
        }
    }

    @Override
    public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
        SmoothCamera cam = (SmoothCamera) this.mCamera;
        this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();
        this.mScrollDetector.setEnabled(false);
    }

    @Override
    public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        cam.setZoomFactor(zf);
    }

    @Override
    public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
        mScrollDetector.setEnabled(true);
        onScroll(mScrollDetector, pTouchEvent.getPointerID(), 0, 0);
    }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        mPinchZoomDetector.setEnabled(false);
    }

    @Override
    public void onScroll(ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;

        float zoomFactor = cam.getZoomFactor();

        float deltaX = -pDistanceX / zoomFactor;

        float deltaY = pDistanceY / zoomFactor;

        mCamera.offsetCenter(deltaX, deltaY);

    }

    @Override
    public void onScrollFinished(ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        mPinchZoomDetector.setEnabled(true);
    }

    public void setScrollerEnabled(boolean pScrollerEnabled) {
        mScrollDetector.setEnabled(pScrollerEnabled);
    }

    public void setZoomEnabled(boolean pZoomEnabled) {
        mPinchZoomDetector.setEnabled(pZoomEnabled);
    }

    public boolean isChaseActive() {
        return chaseActive;
    }

    public void setChaseActive(boolean chaseActive) {
        this.chaseActive = chaseActive;
    }

    public void setUsesActive(boolean usesActive) {
        this.usesActive = usesActive;
    }

    public boolean isEffectsActive() {
        return effectsActive;
    }

    public void setEffectsActive(boolean effectsActive) {
        this.effectsActive = effectsActive;
    }

    private void checkMotorSounds() {
        HashSet<Integer> sounds = new HashSet<>();
        HashMap<Integer, Float> soundValues = new HashMap<>();
        for (GameGroup gameGroup : gameGroups) {
            for (GameEntity gameEntity : gameGroup.getEntities()) {
                if (gameEntity.hasUsage(MotorControl.class)) {
                    MotorControl motorControl = gameEntity.getUsage(MotorControl.class);
                    if (motorControl.isRunning()) {
                        int motorSound = motorControl.getMotorSound();
                        sounds.add(motorSound);
                        float x = gameEntity.getMesh().getX();
                        float y = gameEntity.getMesh().getY();
                        float dis = new Vector2(x, y).dst(this.mCamera.getCenterX(), this.mCamera.getCenterY());
                        float soundFactor = dis <= 0 ? 1f : 800f * 800f / (dis * dis);
                        soundValues.compute(motorSound, (key, oldValue) -> {
                            if (oldValue == null || soundFactor > oldValue) {
                                return soundFactor;
                            } else {
                                return oldValue;
                            }
                        });
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (sounds.contains(i)) {
                ResourceManager.getInstance().motorSounds.get(i).setVolume(soundValues.get(i));
                if (!motorSounds[i]) {
                    motorSounds[i] = true;
                    ResourceManager.getInstance().motorSounds.get(i).play();
                }
            } else {
                if (motorSounds[i]) {
                    ResourceManager.getInstance().motorSounds.get(i).stop();
                    motorSounds[i] = false;
                }
            }
        }
    }

    public void onSelectedEntitySpared(GameEntity selectedEntity) {
        hideAimSprite();
        if (this.getChasedEntity() == selectedEntity) {
            this.resetChasedEntity();
            ResourceManager.getInstance().activity.runOnUiThread(() -> {
                ResourceManager.getInstance().activity.getUiController().resetCameraButton();
            });
        }
    }

    private void hideAimSprite() {
        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
            if (aimSprite != null && aimSprite.hasParent()) {
                aimSprite.detachSelf();
                if (this.physicsConnector != null) {
                    getPhysicsWorld().unregisterPhysicsConnector(this.physicsConnector);
                }
            }
        });
    }

    private void hideAimSpriteDirect() {
        if (aimSprite != null && aimSprite.hasParent()) {
            aimSprite.detachSelf();
            if (this.physicsConnector != null) {
                getPhysicsWorld().unregisterPhysicsConnector(this.physicsConnector);
            }
        }
    }

    public void onGrabbedEntityReleased(boolean usagesUpdated) {
        if (usagesUpdated) {
            hideAimSprite();
        }
    }

    private void cullTest() {
        int total = 0;
        int numberOfVisibleEntities = 0;
        for (GameGroup gameGroup : gameGroups) {
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                total++;
                if (!gameEntity.getMesh().isCulled(this.mCamera)) {
                    numberOfVisibleEntities++;
                }
            }
        }
        Log.e("numberOfVisible", total + ":" + numberOfVisibleEntities);
    }

    public static class Selection {
        public GameEntity gameEntity;
        public Vector2 anchor;
        LayerBlock layerBlock;
        float distance;
        int uses;
        int zIndex;

        public Selection(GameEntity gameEntity, LayerBlock layerBlock, float distance, Vector2 anchor) {
            this.gameEntity = gameEntity;
            this.layerBlock = layerBlock;
            this.distance = distance;
            this.anchor = anchor;
            this.uses = (int) gameEntity.getUseList().size();
            this.zIndex = gameEntity.getZIndex();
        }
    }

    static class SelectionComparator implements Comparator<Selection> {
        @Override
        public int compare(Selection s1, Selection s2) {
            // First compare by number of uses
            int compareByUses = Integer.compare(s2.uses, s1.uses);
            if (compareByUses != 0) {
                return compareByUses;
            }

            // If uses are equal, compare by z-index
            if (s2.distance < 8f && s1.distance < 8f) {
                int compareByZIndex = Integer.compare(s2.zIndex, s1.zIndex);
                if (compareByZIndex != 0) {
                    return compareByZIndex;
                }
            }

            // If z-index are equal, compare by distance
            return Float.compare(s1.distance, s2.distance);
        }
    }

    static class HoldingComparator implements Comparator<Selection> {
        @Override
        public int compare(Selection s1, Selection s2) {
            // First compare by number of uses
            int compareByUses = Integer.compare(s2.uses, s1.uses);
            if (compareByUses != 0) {
                return compareByUses;
            }

            // first compare by z-index only if the entities belong to different groups
            if (s1.gameEntity.getParentGroup() != s2.gameEntity.getParentGroup()) {
                int compareByZIndex = Integer.compare(s2.zIndex, s1.zIndex);
                if (compareByZIndex != 0) {
                    return compareByZIndex;
                }
            }

            // If z-index are equal, compare by distance
            return Float.compare(s1.distance, s2.distance);
        }
    }

    static class TappingComparator implements Comparator<Selection> {
        @Override
        public int compare(Selection s1, Selection s2) {
            // compare by distance
            return Float.compare(s1.distance, s2.distance);
        }
    }


}
