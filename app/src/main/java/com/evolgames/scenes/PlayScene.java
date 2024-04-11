package com.evolgames.scenes;

import static com.evolgames.entities.factories.MaterialFactory.IGNIUM;
import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.physics.CollisionUtils.OBJECTS_MIDDLE_CATEGORY;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.Plotter;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
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
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.MotorControl;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.entities.usage.ProjectileType;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.RocketLauncher;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.physics.entities.Touch;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.MyColorUtils;
import com.evolgames.utilities.Vector2Utils;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayScene extends PhysicsScene<UserInterface<?>>
        implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener,
        PinchZoomDetector.IPinchZoomDetectorListener {

    public static boolean pause = false;
    public static Plotter plotter;
    public static float groundY;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private GameGroup gameGroup1;
    private PlayerAction playerAction = PlayerAction.Drag;
    private PlayerSpecialAction specialAction = PlayerSpecialAction.None;
    private Vector2 point1, point2;
    private Line line;
    private ArrayList<Vector2> points;
    private boolean scroll;
    private SavingBox savingBox;
    private float mPinchZoomStartedCameraZoomFactor;
    private boolean chaseActive;
    private boolean usesActive = true;
    private boolean effectsActive;
    private boolean creating;

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
//        int n = 0;
//        int c = 0;
//        for(GameGroup gameGroup:getGameGroups()){
//            for(GameEntity gameEntity:gameGroup.getEntities()){
//                n++;
//                boolean culled = gameEntity.getMesh().isCulled(this.mCamera);
//                if(culled)c++;
//            }
//        }
//        Log.e("Culling",n+":"+c);

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
        if(this.playerAction==PlayerAction.Create){
            ItemMetaData itemToCreate = ResourceManager.getInstance().getSelectedItemMetaData();
            if(itemToCreate!=null) {
                if (touchEvent.isActionDown()) {
                      creating = true;
                }
                if(touchEvent.isActionUp()&&creating){
                    float x = touchEvent.getX();
                    float y = touchEvent.getY();
                    ToolModel toolModel = loadToolModel(itemToCreate.getFileName(), false, !itemToCreate.isUserCreated());
                    toolModel.getBodies().forEach(bodyModel -> bodyModel.setInit(new Init.Builder(x, y).filter(OBJECT, OBJECT).build()));
                    float[] bounds = toolModel.getBounds(x,y,false);
                    plotter.detachChildren();
                    boolean checkEmpty = worldFacade.checkEmpty(bounds[0]/32f,bounds[1]/32f,bounds[2]/32f,bounds[3]/32f);
                    Color color;
                    if(!checkEmpty) {
                        Log.e("CheckEmpty",worldFacade.checkEmptyCallback.getGameEntity().getName());
                        color = Color.RED;
                        plotter.drawLine2(new Vector2(bounds[0], bounds[3]), new Vector2(bounds[0], bounds[2]), color, 1);
                        plotter.drawLine2(new Vector2(bounds[1], bounds[3]), new Vector2(bounds[1], bounds[2]), color, 1);
                    }
                    if(checkEmpty) {
                        createTool(toolModel, false);
                    }
                    creating = false;
                    this.playerAction = PlayerAction.Drag;
                    ResourceManager.getInstance().activity.getUiController().onItemCreated();
                }
            }
        } else {

            if (this.specialAction == PlayerSpecialAction.effectMeteor) {
                if (touchEvent.isActionDown()) {
                    setScrollerEnabled(false);
                    createMeteorite(touchEvent.getX() / 32f, 100f);
                }
                if (touchEvent.isActionUp() || touchEvent.isActionCancel() || touchEvent.isActionOutside()) {
                    setScrollerEnabled(true);
                }
            } else if (this.specialAction == PlayerSpecialAction.effectFrost) {
                processFrostEffect(touchEvent);
            } else if (this.specialAction == PlayerSpecialAction.effectFireBolt) {
                processExplosionEffect(touchEvent);
            } else if (this.specialAction == PlayerSpecialAction.effectCut) {
                processSlicing(touchEvent);
            }
            else if (this.specialAction == PlayerSpecialAction.effectGlue) {
                processGluing(touchEvent);
            }
            else {
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

    private void processFrostEffect(TouchEvent touchEvent) {
        if (touchEvent.isActionDown()) {
            worldFacade.frostParticleWrapper(new Vector2(-16, 0), new Vector2(16, 0), 400, 600);
            worldFacade.getFrostParticleWrapper().update(touchEvent.getX(), touchEvent.getY());
            setScrollerEnabled(false);
        }
        if (touchEvent.isActionMove()) {
            worldFacade.getFrostParticleWrapper().update(touchEvent.getX(), touchEvent.getY());
        }
        if (touchEvent.isActionUp() || touchEvent.isActionOutside() || touchEvent.isActionCancel()) {
            worldFacade.getFrostParticleWrapper().setAlive(false);
            worldFacade.getFrostParticleWrapper().setSpawnEnabled(false);
            setScrollerEnabled(true);
        }
    }

    @Override
    public void populate() {
        createRagDoll(400, 420);
        createTestUnit();
        String map = ResourceManager.getInstance().getMapString();
        if("open".equals(map.toLowerCase())) {
            createOpenGround();
        }
        if("marble".equals(map.toLowerCase())) {
            createMarbleGround();
        }
        if("wood".equals(map.toLowerCase())) {
            createWoodGround();
        }
        //testMesh();
        onUsagesUpdated();
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
    private Random random = new Random();
    private void createMeteorite(float x, float y){
        int n = 5+random.nextInt(10);
        float size = (float) (Math.random()*5 + 5);
        float angle = (float) (Math.PI * 2 * Math.random());
       List<Vector2> points = VerticesFactory.createPolygon(0,0,angle,size,size,n);
        LayerProperties properties1 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(IGNIUM));
        properties1.setTenacity(0.1f);
        LayerBlock block = BlockFactory.createLayerBlock(points, properties1, 0);

        ArrayList<LayerBlock> blocks = new ArrayList<>();
        blocks.add(block);
        Vector2 u = new Vector2(0,-1);
        BodyInit bodyInit =
                new BulletInit(
                        new TransformInit(
                                new LinearVelocityInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), u.mul(500)),
                                x,
                                y,
                                0),
                        true);
        GameEntity meteorite =
                GameEntityFactory.getInstance()
                        .createGameEntity(
                                x / 32f,
                                y / 32f,
                                0,
                                false,
                                bodyInit,
                                blocks,
                                BodyDef.BodyType.DynamicBody,
                                "Meteorite",null);
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
                                    "Projectile",null);
            GameGroup proj = new GameGroup(GroupType.OTHER, gameEntity);
            gameEntity.getUseList().add(new Projectile(ProjectileType.SHARP_WEAPON));
            addGameGroup(proj);
        }
    }

    private void processExplosionEffect(TouchEvent touchEvent) {
        float x = touchEvent.getX();
        float y = touchEvent.getY();
        if (touchEvent.isActionDown()) {
            this.worldFacade.createExplosion(null, x / 32f, y / 32f, 1f, 0.3f, 0.3f, 10f, 0.01f, 1f, 0.25f, 1f, 0f);
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

    private void createOpenGround() {
        PlayScene.groundY = 30f;
        List<GameEntity> entities = new ArrayList<>();
        for(int i=-100;i<100;i++) {
       // for(int i=2;i<3;i++) {
            ArrayList<LayerBlock> blocks = new ArrayList<>();
            List<Vector2> vertices = new ArrayList<>();
            vertices.add(obtain(-200, -50));
            vertices.add(obtain(-200, 30));
            vertices.add(obtain(200, 30));
            vertices.add(obtain(200, -50));
            blocks.add(
                    BlockFactory.createLayerBlock(
                            vertices,
                            PropertiesFactory.getInstance()
                                    .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.ASPHALT)),
                            0,
                            0,
                            false));

            BodyInit bodyInit = new TransformInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), i*400f/32f, 0, 0);
            GameEntity entity =
                    GameEntityFactory.getInstance().createGameEntity(i*400f/32f, 0, 0, false, bodyInit, blocks, BodyDef.BodyType.StaticBody, "Ground Part "+i,null);
            entities.add(entity);
        }
        GameGroup groundGroup = new GameGroup(GroupType.GROUND,entities);
        this.addGameGroup(groundGroup);
        this.worldFacade.setGroundGroup(groundGroup);
    }


    private void createWoodGround() {
        PlayScene.groundY = 20f;
        ((SmoothCamera)ResourceManager.getInstance().firstCamera)
                .setBounds(-400,0,1200,980);
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        List<Vector2> vertices = new ArrayList<>();
        vertices.add(obtain(-800, -50));
        vertices.add(obtain(-800, 20));
        vertices.add(obtain(800, 20));
        vertices.add(obtain(800, -50));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)),
                        0,
                        0,
                        false));

        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 20));
        vertices.add(obtain(-780, 20));
        vertices.add(obtain(-780, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)),
                        1,
                        0,
                        false));



        vertices = new ArrayList<>();
        vertices.add(obtain(800, 960));
        vertices.add(obtain(800, 20));
        vertices.add(obtain(780, 20));
        vertices.add(obtain(780, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)),
                        2,
                        0,
                        false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 980));
        vertices.add(obtain(800, 980));
        vertices.add(obtain(800, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.WOOD)),
                        3,
                        0,
                        false));

for(LayerBlock layerBlock:blocks){
    layerBlock.getProperties().setDefaultColor(new Color(222f/255f,184f/255f,135/255f));
}
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

    private void createMarbleGround() {
        PlayScene.groundY = 30f;

        ((SmoothCamera)ResourceManager.getInstance().firstCamera)
                .setBounds(-440,0,1240,980);
        ArrayList<LayerBlock> blocks = new ArrayList<>();
        List<Vector2> vertices = new ArrayList<>();
        vertices.add(obtain(-800, -50));
        vertices.add(obtain(-800, 10));
        vertices.add(obtain(800, 10));
        vertices.add(obtain(800, -50));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        0,
                        0,
                        false));
        vertices = new ArrayList<>();
        vertices.add(obtain(-780, 10));
        vertices.add(obtain(-780, 20));
        vertices.add(obtain(780, 20));
        vertices.add(obtain(780, 10));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        1,
                        0,
                        false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 20));
        vertices.add(obtain(-760, 30));
        vertices.add(obtain(760, 30));
        vertices.add(obtain(760, 20));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        2,
                        0,
                        false));




        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 960));
        vertices.add(obtain(-760, 30));
        vertices.add(obtain(-720, 30));
        vertices.add(obtain(-720, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        3,
                        0,
                        false));



        vertices = new ArrayList<>();
        vertices.add(obtain(760, 960));
        vertices.add(obtain(760, 30));
        vertices.add(obtain(720, 30));
        vertices.add(obtain(720, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        4,
                        0,
                        false));


        vertices = new ArrayList<>();
        vertices.add(obtain(-800, 960));
        vertices.add(obtain(-800, 980));
        vertices.add(obtain(800, 980));
        vertices.add(obtain(800, 960));
        blocks.add(
                BlockFactory.createLayerBlock(
                        vertices,
                        PropertiesFactory.getInstance()
                                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.MARBLE)),
                        5,
                        0,
                        false));




        vertices = new ArrayList<>();
        vertices.add(obtain(-780, 10));
        vertices.add(obtain(-780, 11));
        vertices.add(obtain(780, 11));
        vertices.add(obtain(780, 10));
        DecorationBlock decorationBlock = new DecorationBlock();
        DecorationProperties decorationProperties = new DecorationProperties();
        decorationProperties.setDefaultColor(new Color(0.8f,0.8f,0.8f));
        decorationBlock.initialization(vertices,decorationProperties,0);
     blocks.get(1).addAssociatedBlock(decorationBlock);


        vertices = new ArrayList<>();
        vertices.add(obtain(-760, 20));
        vertices.add(obtain(-760, 21));
        vertices.add(obtain(760, 21));
        vertices.add(obtain(760, 20));
        decorationBlock = new DecorationBlock();
        decorationProperties = new DecorationProperties();
        decorationProperties.setDefaultColor(new Color(0.8f,0.8f,0.8f));
        decorationBlock.initialization(vertices,decorationProperties,0);
        blocks.get(2).addAssociatedBlock(decorationBlock);


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
/*        List<Vector2> vertices1 = VerticesFactory.createRectangle(60, 60);
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
         gameEntity1.setCenter(new Vector2());
        */
        // this.worldFacade.addNonCollidingPair(gameEntity1,gameEntity2);

        List<Vector2> vertices3 = VerticesFactory.createRectangle(80, 80);
        LayerProperties properties3 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.IRON));
        LayerBlock block3 = BlockFactory.createLayerBlock(vertices3, properties3, 1, 0);
        List<LayerBlock> blocks3 = new ArrayList<>();
        blocks3.add(block3);
        GameGroup gameGroup =
                GameEntityFactory.getInstance()
                        .createGameGroupTest(
                                blocks3,
                                new Vector2(700 / 32f, 200 / 32f),
                                BodyDef.BodyType.DynamicBody,
                                GroupType.OTHER);
        GameEntity gameEntity3 = gameGroup.getGameEntityByIndex(0);
        gameEntity3.setName("small rectangle");


        List<Vector2> vertices4 = VerticesFactory.createRectangle(80, 80);
        LayerProperties properties4 =
                PropertiesFactory.getInstance()
                        .createProperties(MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.IRON));
        LayerBlock block4 = BlockFactory.createLayerBlock(vertices4, properties4, 1, 0);
        List<LayerBlock> blocks4 = new ArrayList<>();
        blocks4.add(block4);
        GameGroup gameGroup2 =
                GameEntityFactory.getInstance()
                        .createGameGroupTest(
                                blocks4,
                                new Vector2(100 / 32f, 200 / 32f),
                                BodyDef.BodyType.DynamicBody,
                                GroupType.OTHER);
        GameEntity gameEntity4 = gameGroup2.getGameEntityByIndex(0);
        gameEntity4.setName("small rectangle");

        if (false) {
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
    private GameEntity glueEntity;
    private void processGluing(TouchEvent touchEvent) {

        if (touchEvent.isActionDown()) {
            point1 = new Vector2(touchEvent.getX(), touchEvent.getY());
            point2 = new Vector2(point1);
            line = new Line(point1.x, point1.y, point2.x, point2.y, 3, ResourceManager.getInstance().vbom);

            line.setColor(Color.BLUE);
            line.setZIndex(999);
            this.attachChild(line);
            this.sortChildren();
            setScrollerEnabled(false);
            Pair<GameEntity, Vector2> touchData = this.getTouchedEntity(touchEvent, false,true);
           if(touchData!=null) {
               glueEntity = touchData.first;
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
            Pair<GameEntity, Vector2> touchData = this.getTouchedEntity(touchEvent, false,true);
            if (point1 != null && point2 != null&&glueEntity!=null && touchData!=null&&touchData.first!=null && glueEntity!=touchData.first) {
              Touch touch = worldFacade.areTouching(glueEntity,touchData.first);
                if(touch!=null){
                  this.worldFacade.glueEntities(glueEntity,touchData.first,touch.getPoint());
              }
            }
            point1 = null;
            point2 = null;
            line.detachSelf();
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

//        Missile missile = null;
//
//        Hand h = PlayScene.this.getHand();
//        if (h != null && h.getMouseJoint() != null) {
//            if (h.getGrabbedEntity().hasUsage(Missile.class)) {
//                missile = h.getGrabbedEntity().getUsage(Missile.class);
//            }
        //Missile finalMissile = missile;
//            userInterface
//                    .getPanel()
//                    .allocateController(
//                            800 - 64 / 2f,
//                            64 / 2f,
//                            ControlElement.Type.AnalogController,
//                            new ControllerAction() {
//                                @Override
//                                public void controlMoved(float pX, float pY) {
//                                    if (finalMissile != null) {
//                                        finalMissile.setSteerValue(pY);
//                                    }
//                                }
//
//                                @Override
//                                public void controlClicked() {
//                                }
//
//                                @Override
//                                public void controlReleased() {
//                                    if (finalMissile != null) {
//                                        finalMissile.setSteerValue(0);
//                                    }
//                                }
//                            });
//        }

    }


    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public void onUsagesUpdated() {
        getActivity().runOnUiThread(() -> {
            List<PlayerSpecialAction> usageList = new ArrayList<>();
            PlayerSpecialAction defaultAction = null;
            if (usesActive) {
                Hand h = getHand();
                if (h != null && h.getUsableEntity() != null) {
                    h.getUsableEntity()
                            .getUseList()
                            .forEach(
                                    u -> {
                                        if (this.playerAction == PlayerAction.Hold || hand.hasSelectedEntity()) {
                                            if (u.getActions() != null && !u.getActions().isEmpty()) {
                                                usageList.addAll(u.getActions());
                                                if (this.playerAction == PlayerAction.Select) {
                                                    usageList.removeIf(e -> !e.fromDistance);
                                                }
                                            }
                                        }
                                    });
                }

                defaultAction = specialAction == null ? usageList.stream().filter(e -> e.selectableByDefault).findFirst().orElse(null) : specialAction;
                this.setSpecialAction(usageList.contains(defaultAction) ? defaultAction : PlayerSpecialAction.None);
            }
            if (effectsActive) {
                usageList.add(PlayerSpecialAction.effectCut);
                usageList.add(PlayerSpecialAction.effectGlue);
                usageList.add(PlayerSpecialAction.effectFireBolt);
                usageList.add(PlayerSpecialAction.effectMeteor);
                usageList.add(PlayerSpecialAction.effectFrost);
            }
            getActivity().getUiController().onUsagesUpdated(usageList, defaultAction, usesActive);
        });
    }

    public GameActivity getActivity() {
        return ResourceManager.getInstance().activity;
    }

    public Pair<GameEntity, Vector2> getTouchedEntity(TouchEvent touchEvent, boolean withHold, boolean includeStatic) {
        GameEntity result = null;
        Vector2 anchor = null;
        float minDis = 32f;
        for (GameGroup gameGroup : getGameGroups()) {
            for (int k = 0; k < gameGroup.getGameEntities().size(); k++) {
                GameEntity entity = gameGroup.getGameEntities().get(k);
                if (entity.getBody() != null
                        && (includeStatic||entity.getBody().getType() == BodyDef.BodyType.DynamicBody)) {
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
        createItemFromFile("editor_auto_save.mut", false, false);
    }

    public void onOptionSelected(PlayerSpecialAction playerSpecialAction) {

        if (playerSpecialAction == PlayerSpecialAction.FireHeavy || playerSpecialAction == PlayerSpecialAction.AimHeavy) {
            if (this.hand != null) {
                hand.holdEntity();
            }
        }
        if (playerSpecialAction == PlayerSpecialAction.motorMoveForward ||
                playerSpecialAction == PlayerSpecialAction.motorMoveBackward || playerSpecialAction == PlayerSpecialAction.motorStop) {
            if (hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(MotorControl.class)) {
                    MotorControl motorControl = usableEntity.getUsage(MotorControl.class);
                    int state = playerSpecialAction == PlayerSpecialAction.motorMoveBackward ? -1 :
                            playerSpecialAction == PlayerSpecialAction.motorMoveForward ? 1 : 0;
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
        } else if (playerSpecialAction == PlayerSpecialAction.Trigger) {
            if (this.hand != null) {
                GameEntity usableEntity = hand.getUsableEntity();
                if (usableEntity != null && usableEntity.hasUsage(Bomb.class)) {
                    Bomb bomb = usableEntity.getUsage(Bomb.class);
                    bomb.onTriggered(worldFacade);
                    //  hand.releaseGrabbedEntity(true);
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

    private void resetTouchHold() {
        this.setPlayerAction(PlayerAction.Drag);
        ResourceManager.getInstance().activity.getUiController().resetTouchHold();
    }

    public void onMirrorButtonClicked() {
        if (hand != null && hand.getUsableEntity() != null) {
            Invoker.addBodyMirrorCommand(hand.getUsableEntity());
        }
    }


    @Override
    public void onPinchZoomStarted(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
        SmoothCamera cam = (SmoothCamera) this.mCamera;
        this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();
        this.mScrollDetector.setEnabled(false);
    }

    @Override
    public void onPinchZoom(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        cam.setZoomFactor(zf);
    }

    @Override
    public void onPinchZoomFinished(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
        mScrollDetector.setEnabled(true);
        onScroll(mScrollDetector, pTouchEvent.getPointerID(), 0, 0);
    }

    @Override
    public void onScrollStarted(
            ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        mPinchZoomDetector.setEnabled(false);
    }

    @Override
    public void onScroll(
            ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;

        float zoomFactor = cam.getZoomFactor();

        float deltaX = -pDistanceX / zoomFactor;

        float deltaY = pDistanceY / zoomFactor;

        mCamera.offsetCenter(deltaX, deltaY);

    }

    @Override
    public void onScrollFinished(
            ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
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

    public boolean isUsesActive() {
        return usesActive;
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
}
