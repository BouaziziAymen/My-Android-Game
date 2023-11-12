package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import android.util.Pair;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.entities.serialization.SerializationManager;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MyColorUtils;
import com.evolgames.helpers.utilities.Vector2Utils;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.PlayUserInterface;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.adt.color.Color;

public class GameScene extends PhysicsScene<PlayUserInterface>
    implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener,
        PinchZoomDetector.IPinchZoomDetectorListener,
        IOnSceneTouchListener {

  public static boolean pause = false;
  private RagDoll ragdoll;
  private GameGroup gameGroup1;
  private PlayerAction action = PlayerAction.Drag;
  private PlayerSpecialAction specialAction = PlayerSpecialAction.None;
  private Vector2 point1, point2;
  private Line line;
  private ArrayList<Vector2> points;
  private int step = 0;
  private boolean scroll;

  public GameScene(Camera pCamera) {
    super(pCamera, SceneType.PLAY);
    setOnSceneTouchListener(this);
  }

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    step++;
    if (!pause) {
      super.onManagedUpdate(pSecondsElapsed);
    }
    this.worldFacade.onStep();
    Invoker.onStep();


    for (GameGroup gameGroup : getGameGroups()) {
      gameGroup.onStep(pSecondsElapsed);
    }
    for (Hand hand : hands.values()) {
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

  @Override
  public boolean onSceneTouchEvent(TouchEvent touchEvent) {
    super.onSceneTouchEvent(touchEvent);
    float[] cameraSceneCoordinatesFromSceneCoordinates =
        mCamera.getCameraSceneCoordinatesFromSceneCoordinates(touchEvent.getX(), touchEvent.getY());

    TouchEvent hudTouch =
        TouchEvent.obtain(
            cameraSceneCoordinatesFromSceneCoordinates[0],
            cameraSceneCoordinatesFromSceneCoordinates[1],
            touchEvent.getAction(),
            touchEvent.getPointerID(),
            touchEvent.getMotionEvent());

    boolean hudTouched = this.userInterface.onTouchHud(hudTouch, scroll);
    if (!hudTouched) {
      //   userInterface.onTouchScene(touchEvent, scroll);
    }

    if (!hudTouched) {
      if (action == PlayerAction.Drag || action == PlayerAction.Hold) {
        processHandling(touchEvent);
      }
    }
    if (action == PlayerAction.Slice) {
      processSlicing(touchEvent);
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

    return true;
  }

  @Override
  public void populate() {
    createRagDoll();
    createTestUnit();
    createGround();
    testMesh();
  }

  @Override
  public void detach() {}

  private void createRagDoll() {
    this.ragdoll = GameEntityFactory.getInstance().createRagdoll(400 / 32f, 240 / 32f);
  }

  @Override
  public void onPause() {
    // save scene
    try {
      SerializationManager.getInstance().serialize(this);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    this.hands.clear();
    if (this.userInterface != null) {
      this.userInterface.detachSelf();
    }
    // destroy all groups
    for (Iterator<GameGroup> iterator = this.getGameGroups().iterator(); iterator.hasNext(); ) {
      GameGroup gameGroup = iterator.next();
      iterator.remove();
      for (GameEntity gameEntity : gameGroup.getGameEntities()) {
        this.worldFacade.destroyGameEntity(gameEntity, false);
      }
    }
    this.worldFacade.getTimedCommands().clear();
  }

  @Override
  public void onResume() {
    this.userInterface = new PlayUserInterface(this);
    try {
      SerializationManager.getInstance().deserialize(this);
    } catch (FileNotFoundException e) {

    }
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
        this.worldFacade.applyStain(head, p.x, p.y, head.getBlocks().get(0), color, 14, true);
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
                this.worldFacade.destroyGameEntity(e, false);
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
                  bodyInit,
                  blocks,
                  BodyDef.BodyType.DynamicBody,
                  "Projectile");
      GameGroup proj = new GameGroup(GroupType.OTHER,gameEntity);
      gameEntity.getUseList().add(new Projectile(gameEntity));
      addGameGroup(proj);
    }
  }

  private void explosionTest(TouchEvent touchEvent) {
    float x = touchEvent.getX();
    float y = touchEvent.getY();
    if (touchEvent.isActionDown()) {
      this.worldFacade.createExplosion(null, x, y, 1f, 0.3f, 0.3f, 0.2f, 100f, 0.1f, 1f);
    }
  }

  @Override
  public boolean onSceneTouchEvent(Scene pScene, final TouchEvent touchEvent) {
    float x = touchEvent.getX() / 32f;
    float y = touchEvent.getY() / 32f;
    if (false) {
      projectionTest(touchEvent);
    }
    if (false) {
      if (touchEvent.isActionDown()) {
        this.worldFacade.performFlux(new Vector2(x, y), null, gameGroup1.getGameEntityByIndex(0));
      }
    }
    if (false) {
      explosionTest(touchEvent);
    }
    return true;
  }

  @Override
  public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {}

  @Override
  public void onAccelerationChanged(AccelerationData pAccelerationData) {}

  @Override
  public void onPinchZoomStarted(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {}

  @Override
  public void onPinchZoom(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {}

  @Override
  public void onPinchZoomFinished(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {}

  @Override
  public void onScrollStarted(
      ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {}

  @Override
  public void onScroll(
      ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {}

  @Override
  public void onScrollFinished(
      ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {}

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
    vertices1.add(obtain(-900, 0));
    vertices1.add(obtain(-900, 20));
    vertices1.add(obtain(400, 20));
    vertices1.add(obtain(400, 0));

    List<Vector2> vertices2 = new ArrayList<>();
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
    vertices4.add(obtain(50, 18));

    blocks.add(
        BlockFactory.createLayerBlock(
            vertices1,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(0)),
            0,
            0,
            false));
    blocks.add(
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

    GameGroup groundGroup =
        GameEntityFactory.getInstance()
            .createGameGroupTest(
                blocks,
                new Vector2(400 / 32f, 0),
                BodyDef.BodyType.StaticBody,
                "Ground",
                OBJECTS_MIDDLE_CATEGORY,GroupType.GROUND);
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
                blocks, new Vector2(100f / 32f, 200 / 32f), BodyDef.BodyType.DynamicBody,GroupType.OTHER);
    this.worldFacade.applyStain(
        gameGroup1.getGameEntityByIndex(0), 0, 10, block1, Color.RED, 0, false);
    this.worldFacade.applyStain(
        gameGroup1.getGameEntityByIndex(0), 0, 15, block1, Color.RED, 0, false);
    GameEntity gameEntity = gameGroup1.getGameEntityByIndex(0);
    gameEntity.setCenter(new Vector2());
    gameEntity.redrawStains();
    gameEntity.setName("test");

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
    this.worldFacade.addJointToCreate(revoluteJointDef, gameEntity, gameEntity2);

    List<Vector2> vertices3 = VerticesFactory.createRectangle(90, 60);
    LayerProperties properties3 =
        PropertiesFactory.getInstance()
            .createProperties(MaterialFactory.getInstance().getMaterialByIndex(4));
    LayerBlock block3 = BlockFactory.createLayerBlock(vertices3, properties3, 1, 0);
    List<LayerBlock> blocks3 = new ArrayList<>();
    blocks3.add(block3);
    GameEntityFactory.getInstance()
        .createGameGroupTest(
            blocks3, new Vector2(300f / 32f, 200 / 32f), BodyDef.BodyType.DynamicBody,GroupType.OTHER);

    if (false) {
      for (LayerBlock layerBlock : gameGroup1.getGameEntityByIndex(0).getBlocks()) {
        Collections.shuffle(layerBlock.getBlockGrid().getCoatingBlocks());
        layerBlock.getBlockGrid().getCoatingBlocks().forEach(g -> g.setTemperature(10000));
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

  public void setAction(PlayerAction action) {
    this.action = action;
    if (action != PlayerAction.Hold) {
      this.specialAction = PlayerSpecialAction.None;
    }
    if (action == PlayerAction.Drag) {
      this.hands.forEach(
          (key, h) -> {
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
    this.hands.forEach(
        (key, h) -> {
          if (h.getMouseJoint() != null) {
            h.setForceFactor(forceFactor);
          }
        });
  }

  public PlayerAction getPlayerAction() {
    return action;
  }

  public void onUsagesUpdated() {
    List<PlayerSpecialAction> usageList = new ArrayList<>();
    this.hands.forEach(
        (key, h) -> {
          if (h.getGrabbedEntity() != null) {
            h.getGrabbedEntity()
                .getUseList()
                .forEach(
                    u -> {
                      if (this.action == PlayerAction.Hold) {
                        if (u.getAction() != null && u.getAction().iconId != -1) {
                          usageList.add(u.getAction());
                        }
                      }
                    });
          }
        });
    if (this.action == PlayerAction.Hold && !usageList.isEmpty()) {
      usageList.add(0, PlayerSpecialAction.None);
    }
    if (!usageList.contains(specialAction)) {
      this.setSpecialAction(PlayerSpecialAction.None);
    }
    this.userInterface.updateParticularUsageSwitcher(usageList.toArray(new PlayerSpecialAction[0]));
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
}
