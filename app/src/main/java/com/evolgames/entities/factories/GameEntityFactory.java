package com.evolgames.entities.factories;

import static com.evolgames.physics.CollisionConstants.OBJECTS_BACK_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_FRONT_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.EntityWithBody;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.DecorationBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.cut.CutPoint;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.PointsFreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.ragdoll.RagDoll;
import com.evolgames.entities.serialization.infos.InitInfo;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.entities.blockvisitors.utilities.BlockUtils;
import com.evolgames.entities.blockvisitors.utilities.GeometryUtils;
import com.evolgames.entities.blockvisitors.utilities.Utils;
import com.evolgames.scenes.PhysicsScene;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

public class GameEntityFactory {
  private static final GameEntityFactory INSTANCE = new GameEntityFactory();
  public Box hand;
  private PhysicsScene<?> scene;
  private PhysicsWorld physicsWorld;

  public static GameEntityFactory getInstance() {
    return INSTANCE;
  }

  public void create(PhysicsWorld physicsWorld, PhysicsScene<?> scene) {
    this.physicsWorld = physicsWorld;
    this.scene = scene;
  }

  public GameEntity createGameEntity(
      float x,
      float y,
      float rot,
      BodyInit bodyInit,
      List<LayerBlock> blocks,
      BodyDef.BodyType bodyType,
      String name) {
    Collections.sort(blocks);

    for (LayerBlock b : blocks) {
      b.getAssociatedBlocks().sort(BlockUtils.associatedBlockComparator);
    }

    MosaicMesh mesh = MeshFactory.getInstance().createMosaicMesh(x * 32, y * 32, rot, blocks);
   // mesh.setCullingEnabled(true);

    GameEntity entity = new GameEntity(mesh, scene, name, blocks);
    entity.setMesh(mesh);
    entity.setVisible(false);
    this.scene.attachChild(entity.getMesh());
    InitInfo initInfo = bodyInit.getInitInfo(new InitInfo());
    entity.setInitInfo(initInfo);
    entity.setBodyType(bodyType);
    Invoker.addBodyCreationCommand(entity, bodyType, bodyInit);
    return entity;
  }

  @SuppressWarnings("unused")
  private void drawFreshCuts(ArrayList<LayerBlock> blocks, MosaicMesh mesh) {
    for (LayerBlock b : blocks) {
      for (FreshCut fc : b.getFreshCuts()) {
        if (fc instanceof SegmentFreshCut) {
          SegmentFreshCut freshCut = (SegmentFreshCut) fc;
          Line line =
              new Line(
                  freshCut.first.x,
                  freshCut.first.y,
                  freshCut.second.x,
                  freshCut.second.y,
                  5,
                  ResourceManager.getInstance().vbom);
          line.setColor(Utils.getRandomColor());
          mesh.attachChild(line);
          float X = freshCut.second.x - freshCut.first.x;
          float Y = freshCut.second.y - freshCut.first.y;
          float midPointX = freshCut.first.x + X / 2;
          float midPointY = freshCut.first.y + Y / 2;
          Vector2 tangent = new Vector2(X, Y).nor();
          Vector2 normal = new Vector2(tangent.y, -tangent.x).mul(16);
          line =
              new Line(
                  midPointX,
                  midPointY,
                  midPointX + normal.x,
                  midPointY + normal.y,
                  1,
                  ResourceManager.getInstance().vbom);
          line.setColor(Color.GREEN);
          mesh.attachChild(line);
        }
      }
    }
  }

  @SuppressWarnings("unused")
  public GameEntity createGameEntityDirect(
      float x,
      float y,
      float rot,
      List<LayerBlock> blocks,
      BodyDef.BodyType bodyType,
      String name) {
    MosaicMesh mesh = MeshFactory.getInstance().createMosaicMesh(x, y, rot, blocks);
    GameEntity entity = new GameEntity(mesh, scene, name, blocks);
    Body body = BodyFactory.getInstance().createBody(blocks, bodyType);
    PhysicsConnector physicsConnector = new PhysicsConnector(mesh, body);
    physicsWorld.registerPhysicsConnector(physicsConnector);
    entity.setBody(body);
    body.setTransform(x / 32f, y / 32f, rot);
    return entity;
  }

  public ArrayList<GameEntity> createSplinterEntities(
      float x,
      float y,
      float rot,
      ArrayList<ArrayList<LayerBlock>> splinters,
      Vector2 linearVelocity,
      float angularVelocity,
      String name,
      GameEntity parent) {
    ArrayList<GameEntity> entities = new ArrayList<>();
    int splinterId = 0;
    for (ArrayList<LayerBlock> group : splinters) {
      Vector2 newCenter =
          GeometryUtils.calculateCenter(
              group.stream().map(LayerBlock::getVertices).collect(Collectors.toList()));
      for (LayerBlock b : group) {
        b.translate(newCenter);
        for (Block<?, ?> a : b.getAssociatedBlocks()) {
          a.translate(newCenter);
        }
        for (FreshCut freshCut : b.getFreshCuts()) {
          if (freshCut instanceof SegmentFreshCut) {
            SegmentFreshCut segmentFreshCut = (SegmentFreshCut) freshCut;
            if (segmentFreshCut.isInner()) {
              Utils.translatePoint(segmentFreshCut.first, newCenter);
              Utils.translatePoint(segmentFreshCut.second, newCenter);
            }
          }
          if (freshCut instanceof PointsFreshCut) {
            PointsFreshCut pointsFreshCut = (PointsFreshCut) freshCut;
            Utils.translatePoints(
                pointsFreshCut.getPoints().stream()
                    .map(CutPoint::getPoint)
                    .collect(Collectors.toList()),
                newCenter);
          }
        }
      }
      Filter filter =
          parent.getBody().getFixtureList().stream()
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Filter should exist"))
              .getFilterData();

      assert newCenter != null;
      GeometryUtils.rotateVectorRad(newCenter, rot);
      BodyInit bodyInit =
          new TransformInit(
              new LinearVelocityInit(
                  new AngularVelocityInit(new BodyInitImpl(filter.categoryBits), angularVelocity),
                  linearVelocity),
              x + newCenter.x / 32f,
              y + newCenter.y / 32f,
              rot);
      GameEntity e =
          GameEntityFactory.getInstance()
              .createGameEntity(
                  x + newCenter.x / 32f,
                  y + newCenter.y / 32f,
                  rot,
                  bodyInit,
                  group,
                  BodyDef.BodyType.DynamicBody,
                  name + splinterId);

      e.redrawStains();
      e.setCenter(newCenter);
      scene.sortChildren();
      entities.add(e);
      e.setParentGroup(parent.getParentGroup());
    }
    return entities;
  }

  public GameGroup createGameGroupTest(
          ArrayList<LayerBlock> groupBlocks,
          Vector2 position,
          BodyDef.BodyType bodyType,
          String name,
          short category, GroupType
          groupType) {
    GameGroup gameGroup = new GameGroup(groupType);
    BodyInit bodyInit = new TransformInit(new BodyInitImpl(category), position.x, position.y, 0);
    GameEntity entity =
        createGameEntity(position.x, position.y, 0, bodyInit, groupBlocks, bodyType, name);
    gameGroup.addGameEntity(entity);
    scene.addGameGroup(gameGroup);
    return gameGroup;
  }

  public GameEntity createIndependentGameEntity(
      GameGroup parentGroup,
      ArrayList<LayerBlock> parentBlocks,
      Vector2 position,
      float angle,
      BodyInit bodyInit,
      String name) {
    GameEntity entity =
        createGameEntity(
            position.x,
            position.y,
            angle,
            bodyInit,
            parentBlocks,
            BodyDef.BodyType.DynamicBody,
            name);
    parentGroup.addGameEntity(entity);
    return entity;
  }

  public GameGroup createGameGroupTest(
      List<LayerBlock> groupBlocks, Vector2 position, BodyDef.BodyType bodyType, GroupType groupType) {
    GameGroup gameGroup = new GameGroup(groupType);
    BodyInit bodyInit =
        new TransformInit(new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), position.x, position.y, 0);
    GameEntity entity =
        createGameEntity(position.x, position.y, 0, bodyInit, groupBlocks, bodyType, "");
    gameGroup.addGameEntity(entity);
    scene.addGameGroup(gameGroup);
    return gameGroup;
  }

  GameEntity createDollPart(
      float x, float y, float rot, ArrayList<LayerBlock> blocks, String name) {
    BodyInit bodyInit =
        new BulletInit(
            new TransformInit(
                new BodyInitImpl(
                    (short)
                        (OBJECTS_MIDDLE_CATEGORY | OBJECTS_FRONT_CATEGORY | OBJECTS_BACK_CATEGORY)),
                x,
                y,
                rot),
            false);
    return this.createGameEntity(x, y, rot, bodyInit, blocks, BodyDef.BodyType.DynamicBody, name);
  }

  GameEntity createDollPart(
      float x, float y, Filter filter, ArrayList<LayerBlock> blocks, String name) {
    BodyInit bodyInit =
        new BulletInit(new TransformInit(new BodyInitImpl(filter), x, y, (float) 0), false);
    return this.createGameEntity(
        x, y, (float) 0, bodyInit, blocks, BodyDef.BodyType.DynamicBody, name);
  }

  public RagDoll createRagdoll(float x, float y) {

    Color pullColor = new Color(0.7f, 0.7f, 0.7f);
    Color pantColor = new Color(0.06f, 0.2f, 0.6f);
    final float UPPERARM_CIR1 = 8f;
    final float UPPERARM_CIR2 = 6f;
    final float LOWERARM_CIR1 = 6f;
    final float LOWERARM_CIR2 = 5f;
    final float G = 1.618f;
    final float HEAD_RAY = 12;
    final float SHOULDER_WIDTH = 44;

    final float NECK_LENGTH = (G - 1) * HEAD_RAY;
    final float TORSO_HEIGHT = 1.5f * G * (NECK_LENGTH + 2 * HEAD_RAY);

    final float ARM_LENGTH = 32f;

    final float HAND_SIDE = 10f;

    final float UPPERLEG_CIR1 = 11f;
    final float UPPERLEG_CIR2 = 9f;
    final float UPPERLEG_L1 = G * G * G * HEAD_RAY / (G + 1);
    final float LOWERLEG_LENGTH1 = G * G * HEAD_RAY;
    final float LOWERLEG_LENGTH2 = (G * G * HEAD_RAY) / (G + 1);
    final float LOWERLEG_THICKNESS = 7f;
    final float LOWERLEG_CIR1 = 6f;
    final float LOWERLEG_CIR2 = 8f;
    final float RIGHT = SHOULDER_WIDTH / 2 - UPPERLEG_CIR1;
    final float FOOT_LENGTH = 16f;

    float level0 = y - (HEAD_RAY + NECK_LENGTH + TORSO_HEIGHT / 2) / 32f;

    ArrayList<Vector2> points =
        VerticesFactory.createPolygon(0, 0, 1.25f * HEAD_RAY, 1.25f * HEAD_RAY, 20);
    LayerBlock block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(12)),
            0);
    ArrayList<LayerBlock> blocks = new ArrayList<>();
    blocks.add(block);
    GameEntity head = createDollPart(x, y, 0, blocks, "head");

    blocks = new ArrayList<>();
    points = VerticesFactory.createDistorted(SHOULDER_WIDTH, SHOULDER_WIDTH, TORSO_HEIGHT, 0, 0);
    Vector2 A = points.get(0).cpy();
    Vector2 B = points.get(1).cpy();

    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);

    float alpha1 = SHOULDER_WIDTH / 2f;
    float alpha2 = SHOULDER_WIDTH / 2f;
    float b = UPPERLEG_CIR1 * (alpha2 - alpha1) / TORSO_HEIGHT;

    Vector2 C =
        new Vector2(0, 0)
            .sub(0, TORSO_HEIGHT / 2f)
            .add(alpha2, 0)
            .sub(b, 0)
            .add(0, 2 * UPPERLEG_CIR1);
    Vector2 Cprime = new Vector2(-C.x, C.y);

    points = new ArrayList<>();
    points.add(A);
    points.add(B);
    points.add(C);
    points.add(Cprime);

    DecorationBlock stripBlock =
        BlockFactory.createDecorationBlock(points, new DecorationProperties(pantColor), 0);
    block.addAssociatedBlock(stripBlock);

    blocks.add(block);
    block.getProperties().setDefaultColor(pullColor);
    GameEntity upperTorso = createDollPart(x, level0, 0, blocks, "upperTorso");

    points = VerticesFactory.createShape1(ARM_LENGTH, UPPERARM_CIR1, UPPERARM_CIR2);
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks = new ArrayList<>();
    blocks.add(block);
    block.getProperties().setDefaultColor(pullColor);
    GameEntity upperArmRight =
        createDollPart(
            x + (SHOULDER_WIDTH / 2 + ARM_LENGTH / 2 + UPPERARM_CIR1) / 32f,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            (float) 0,
            blocks,
            "head");

    points = VerticesFactory.createShape1(ARM_LENGTH, UPPERARM_CIR1, UPPERARM_CIR2);
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks = new ArrayList<>();
    blocks.add(block);
    block.getProperties().setDefaultColor(pullColor);
    GameEntity upperArmLeft =
        createDollPart(
            x - (SHOULDER_WIDTH / 2 + ARM_LENGTH / 2 + UPPERARM_CIR1) / 32f,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            (float) 0,
            blocks,
            "head");

    points = VerticesFactory.createShape2(ARM_LENGTH, LOWERARM_CIR1, LOWERARM_CIR2);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks.add(block);
    block.getProperties().setDefaultColor(pullColor);
    float shoulderPositionX =
        (SHOULDER_WIDTH / 2 + 3 * ARM_LENGTH / 2 + UPPERARM_CIR1 + UPPERARM_CIR2) / 32f;
    GameEntity lowerArmR =
        createDollPart(
            x + shoulderPositionX,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            (float) Math.PI / 2,
            blocks,
            "head");

    points = VerticesFactory.createShape2(ARM_LENGTH, LOWERARM_CIR1, LOWERARM_CIR2);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks.add(block);
    block.getProperties().setDefaultColor(pullColor);
    GameEntity lowerArmL =
        createDollPart(
            x - shoulderPositionX,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            (float) -Math.PI / 2,
            blocks,
            "head");

    points = VerticesFactory.createSquare(HAND_SIDE);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(12)),
            0);
    blocks.add(block);
    GameEntity rightHand =
        createDollPart(
            x
                + (SHOULDER_WIDTH / 2
                        + 2 * ARM_LENGTH
                        + UPPERARM_CIR1
                        + UPPERARM_CIR2
                        + LOWERARM_CIR1
                        + LOWERARM_CIR2)
                    / 32f,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            0,
            blocks,
            "head");

    points = VerticesFactory.createSquare(HAND_SIDE);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(12)),
            0);
    blocks.add(block);
    GameEntity leftHand =
        createDollPart(
            x
                - (SHOULDER_WIDTH / 2
                        + 2 * ARM_LENGTH
                        + UPPERARM_CIR1
                        + UPPERARM_CIR2
                        + LOWERARM_CIR1
                        + LOWERARM_CIR2)
                    / 32f,
            y - (HEAD_RAY + UPPERARM_CIR1) / 32f,
            0,
            blocks,
            "head");

    // problem here!
    float hLegLength = (UPPERLEG_L1 + UPPERLEG_L1 + UPPERLEG_CIR1 + UPPERLEG_CIR2) / 2;
    points = VerticesFactory.createShape4(UPPERLEG_L1, UPPERLEG_L1, UPPERLEG_CIR1, UPPERLEG_CIR2);
    Vector2[] vertices = GeometryUtils.hullFinder.findConvexHull(points.toArray(new Vector2[0]));
    points.clear();
    points.addAll(Arrays.asList(vertices));
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks = new ArrayList<>();
    blocks.add(block);
    block.getProperties().setDefaultColor(pantColor);

    GameEntity upperLegR =
        createDollPart(
            x + (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y - (HEAD_RAY + TORSO_HEIGHT + hLegLength) / 32f,
            0,
            blocks,
            "upperLeg");

    points = VerticesFactory.createShape4(UPPERLEG_L1, UPPERLEG_L1, UPPERLEG_CIR1, UPPERLEG_CIR2);
    vertices = GeometryUtils.hullFinder.findConvexHull(points.toArray(new Vector2[0]));
    points.clear();
    points.addAll(Arrays.asList(vertices));
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks = new ArrayList<>();
    blocks.add(block);
    block.getProperties().setDefaultColor(pantColor);
    GameEntity upperLegL =
        createDollPart(
            x - (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y - (HEAD_RAY + TORSO_HEIGHT + hLegLength) / 32f,
            0,
            blocks,
            "head");

    float lowerLegHalfLength =
        (LOWERLEG_LENGTH1 + LOWERLEG_LENGTH2 + LOWERLEG_CIR2 + LOWERLEG_CIR1) / 2;
    points =
        VerticesFactory.createShape5(
            LOWERLEG_LENGTH1, LOWERLEG_LENGTH2, LOWERLEG_THICKNESS, LOWERLEG_CIR2, LOWERLEG_CIR1);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks.add(block);
    block.getProperties().setDefaultColor(pantColor);
    GameEntity lowerLegR =
        createDollPart(
            x + (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y - (HEAD_RAY + TORSO_HEIGHT + 2 * hLegLength + lowerLegHalfLength) / 32f,
            0,
            blocks,
            "head");

    points =
        VerticesFactory.createShape5(
            LOWERLEG_LENGTH1, LOWERLEG_LENGTH2, LOWERLEG_THICKNESS, LOWERLEG_CIR2, LOWERLEG_CIR1);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(11)),
            0);
    blocks.add(block);
    block.getProperties().setDefaultColor(pantColor);
    GameEntity lowerLegL =
        createDollPart(
            x - (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y - (HEAD_RAY + TORSO_HEIGHT + 2 * hLegLength + lowerLegHalfLength) / 32f,
            0,
            blocks,
            "head");

    points = VerticesFactory.createShape6(LOWERLEG_CIR2, FOOT_LENGTH);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(12)),
            0);
    blocks.add(block);
    GameEntity leftFoot =
        createDollPart(
            x - (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y
                - (HEAD_RAY
                        + TORSO_HEIGHT
                        + 2 * hLegLength
                        + 2 * lowerLegHalfLength
                        + LOWERLEG_CIR2)
                    / 32f,
            (float) 0,
            blocks,
            "head");

    points = VerticesFactory.mirrorShapeX(points);
    blocks = new ArrayList<>();
    block =
        BlockFactory.createLayerBlock(
            points,
            PropertiesFactory.getInstance()
                .createProperties(MaterialFactory.getInstance().getMaterialByIndex(12)),
            0);
    blocks.add(block);
    GameEntity rightFoot =
        createDollPart(
            x + (SHOULDER_WIDTH / 2 - UPPERLEG_CIR1) / 32f,
            y
                - (HEAD_RAY
                        + TORSO_HEIGHT
                        + 2 * hLegLength
                        + 2 * lowerLegHalfLength
                        + LOWERLEG_CIR2)
                    / 32f,
            0,
            blocks,
            "head");

    RagDoll ragdoll =
        new RagDoll(
            scene,
            head,
            upperTorso,
            upperArmRight,
            lowerArmR,
            upperArmLeft,
            lowerArmL,
            upperLegR,
            lowerLegR,
            upperLegL,
            lowerLegL,
            rightHand,
            leftHand,
            leftFoot,
            rightFoot);
    scene.addGameGroup(ragdoll);
    head.setType(SpecialEntityType.Head);
    upperTorso.setType(SpecialEntityType.UpperTorso);
    upperArmRight.setType(SpecialEntityType.UpperArmRight);
    lowerArmR.setType(SpecialEntityType.LowerArmRight);
    upperArmLeft.setType(SpecialEntityType.UpperArmLeft);
    lowerArmL.setType(SpecialEntityType.LowerArmLeft);
    upperLegR.setType(SpecialEntityType.UpperLegRight);
    lowerLegR.setType(SpecialEntityType.LowerLegRight);
    upperLegL.setType(SpecialEntityType.UpperLegLeft);
    lowerLegL.setType(SpecialEntityType.LowerLegLeft);
    rightHand.setType(SpecialEntityType.RightHand);
    leftHand.setType(SpecialEntityType.LeftHand);
    leftFoot.setType(SpecialEntityType.LeftFoot);
    rightFoot.setType(SpecialEntityType.RightFoot);
    upperTorso.setZIndex(3);
    upperLegR.setZIndex(2);
    upperLegL.setZIndex(2);
    lowerLegR.setZIndex(1);
    lowerLegL.setZIndex(1);
    this.scene.sortChildren();
    ragdoll.setHead(head);
    head.setName("Head");
    upperTorso.setName("upperTorso");
    upperArmRight.setName("upperArmRight");
    lowerArmR.setName("lowerArmR");
    upperArmLeft.setName("upperArmLeft");
    lowerArmL.setName("lowerArmL");
    upperLegR.setName("upperLegR");
    lowerLegR.setName("lowerLegR");
    upperLegL.setName("upperLegL");
    lowerLegL.setName("lowerLegL");
    rightHand.setName("rightHand");
    leftHand.setName("leftHand");
    rightFoot.setName("rightFoot");
    leftFoot.setName("leftFoot");

    RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, (-HEAD_RAY - NECK_LENGTH) / 32));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, TORSO_HEIGHT / 2 / 32));
    revoluteJointDef.collideConnected = true;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, head, upperTorso);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(
            (SHOULDER_WIDTH / 2 - UPPERARM_CIR1 / 2) / 32f, (TORSO_HEIGHT / 2 - 16 / 2f) / 32f));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, ARM_LENGTH / 2 / 32f));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = 0;
    revoluteJointDef.upperAngle = (float) ((float) Math.PI);

    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperTorso, upperArmRight);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(
            (-SHOULDER_WIDTH / 2 + UPPERARM_CIR1 / 2) / 32f, (TORSO_HEIGHT / 2 - 16 / 2f) / 32f));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, ARM_LENGTH / 2 / 32f));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) ((float) -Math.PI);
    revoluteJointDef.upperAngle = (float) 0;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperTorso, upperArmLeft);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, -ARM_LENGTH / 2 / 32));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, ARM_LENGTH / 2 / 32));
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) 0;
    revoluteJointDef.upperAngle = (float) Math.PI;
    revoluteJointDef.collideConnected = false;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperArmRight, lowerArmR);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, -ARM_LENGTH / 2 / 32));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, ARM_LENGTH / 2 / 32));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) -Math.PI;
    revoluteJointDef.upperAngle = (float) 0;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperArmLeft, lowerArmL);

    float sleeve = UPPERARM_CIR1 / 2 - ARM_LENGTH / 2 - LOWERARM_CIR2;

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, sleeve / 32));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, HAND_SIDE / 2 / 32));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (-Math.PI / 6);
    revoluteJointDef.upperAngle = (float) (Math.PI / 6);
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, lowerArmR, rightHand);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, sleeve / 32));
    revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, HAND_SIDE / 2 / 32));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (-Math.PI / 6);
    revoluteJointDef.upperAngle = (float) (Math.PI / 6);
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, lowerArmL, leftHand);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(RIGHT / 32f, -(TORSO_HEIGHT / 2 - UPPERLEG_CIR1 / 2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain(0, (UPPERLEG_L1 + UPPERLEG_L1 + UPPERLEG_CIR1) / 2 / 32f));
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (float) -(Math.PI / 4);
    revoluteJointDef.upperAngle = (float) (Math.PI / 2);
    revoluteJointDef.collideConnected = false;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperTorso, upperLegR);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(-RIGHT / 32f, -(TORSO_HEIGHT / 2 - UPPERLEG_CIR1 / 2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain(0, (UPPERLEG_L1 + UPPERLEG_L1 + UPPERLEG_CIR1) / 2 / 32f));
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (-Math.PI / 2);
    revoluteJointDef.upperAngle = (float) (float) (Math.PI / 4);
    revoluteJointDef.collideConnected = false;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperTorso, upperLegL);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, (-hLegLength + UPPERLEG_CIR2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain(0, (lowerLegHalfLength - LOWERLEG_CIR1) / 32f));
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (-Math.PI / 2);
    revoluteJointDef.upperAngle = (float) (0);
    revoluteJointDef.collideConnected = false;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperLegR, lowerLegR);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, (-hLegLength + UPPERLEG_CIR2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain(0, (lowerLegHalfLength - LOWERLEG_CIR1) / 32f));
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) (0);
    revoluteJointDef.upperAngle = (float) (Math.PI / 2);
    revoluteJointDef.collideConnected = false;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperLegL, lowerLegL);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(0, (-lowerLegHalfLength - UPPERLEG_CIR2 / 2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain((-FOOT_LENGTH + 2 * LOWERLEG_CIR2) / 2 / 32f, 0));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.upperAngle = (float) Math.PI / 5;
    revoluteJointDef.lowerAngle = (float) -Math.PI / 4;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, lowerLegR, rightFoot);

    revoluteJointDef = new RevoluteJointDef();
    revoluteJointDef.localAnchorA.set(
        Vector2Pool.obtain(0, (-lowerLegHalfLength - UPPERLEG_CIR2 / 2) / 32f));
    revoluteJointDef.localAnchorB.set(
        Vector2Pool.obtain((-FOOT_LENGTH + 2 * LOWERLEG_CIR2) / 2 / 32f, 0));
    revoluteJointDef.collideConnected = false;
    revoluteJointDef.enableLimit = true;
    revoluteJointDef.lowerAngle = (float) -Math.PI / 5;
    revoluteJointDef.upperAngle = (float) Math.PI / 4;
    scene.getWorldFacade().addJointToCreate(revoluteJointDef, lowerLegL, leftFoot);

    return ragdoll;
  }

  /*    public void createTest() {


          ArrayList<Vector2> points = VerticesFactory.createPolygon(0, 0, 40, 40, 4);
          LayerBlock block = BlockFactory.createBlockA(points, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(2), new Filter()), 0);
          ArrayList<LayerBlock> blocks = new ArrayList<>();
          points = VerticesFactory.createPolygon(0, 0, 10, 60, 4);
          LayerBlock block2 = BlockFactory.createBlockA(points, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1), new Filter()), 1);
          blocks.add(block);
          blocks.add(block2);
          entity1 = createGameEntity(480 / 32f, 4f, 0, blocks, BodyDef.BodyType.DynamicBody, "entity1", true);
          scene.attachChild(entity1.getMesh());


          points = VerticesFactory.createPolygon(0, 0, 80, 80, 4);
          block = BlockFactory.createBlockA(points, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(0), new Filter()), 0);
          blocks = new ArrayList<>();
          blocks.add(block);

          entity2 = createGameEntity(600 / 32f, 3f, 0, blocks, BodyDef.BodyType.DynamicBody, "entity2", true);
          scene.attachChild(entity2.getMesh());


          points = VerticesFactory.createPolygon(0, 0, 10, 30, 4);
          block = BlockFactory.createBlockA(points, PropertiesFactory.getInstance().createProperties(MaterialFactory.getInstance().getMaterialByIndex(1), new Filter()), 0);
          blocks = new ArrayList<>();
          blocks.add(block);
          entity3 = createGameEntity(480 / 32f, 3f, 0, blocks, BodyDef.BodyType.DynamicBody, "entity3", true);
          scene.attachChild(entity3.getMesh());

  */
  /*
          RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
          revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, (-HEAD_RAY - NECK_LENGTH) / 32));
          revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, TORSO_HEIGHT / 2 / 32));
          revoluteJointDef.collideConnected = true;
          scene.getWorldFacade().addJointToCreate(revoluteJointDef, head, upperTorso);


          revoluteJointDef = new RevoluteJointDef();
          revoluteJointDef.localAnchorA.set(Vector2Pool.obtain(0, (-TORSO_HEIGHT / 2 + TORSO_D) / 32));
          revoluteJointDef.localAnchorB.set(Vector2Pool.obtain(0, TORSO_HEIGHT / 2 / 32));
          revoluteJointDef.enableLimit = true;
          revoluteJointDef.lowerAngle = -TORSO_ANGLE;
          revoluteJointDef.upperAngle = TORSO_ANGLE;
          revoluteJointDef.collideConnected = false;
          scene.getWorldFacade().addJointToCreate(revoluteJointDef, upperTorso, lowerTorso);
  */
  /*
  }*/

  /*    public void createLinks() {
      HashSet<JointZoneBlock> zones = scene.getWorldFacade().findWeldZones(entity1, entity2, new Vector2(2, 2));
      float x = entity1.getBody().getPosition().x;
      float y = entity1.getBody().getPosition().y;
      float x1 = entity2.getBody().getPosition().x;
      float y1 = entity2.getBody().getPosition().y;
      scene.getWorldFacade().mergeEntities(entity2, entity1, new Vector2(-2, -2), new Vector2(x, y), zones);

      Vector2 advance = new Vector2(2, 0);
      zones = scene.getWorldFacade().findWeldZones(entity3, entity2, advance.cpy());
      scene.getWorldFacade().mergeEntities(entity2, entity3, advance.cpy().mul(-1), new Vector2(x1, y1), zones);
  }*/

  public class Box extends EntityWithBody {

    private final float x0;
    private final float y0;
    private final float w;
    private final float h;
    private Rectangle rectangle;

    public Box(float x0, float y0, ITextureRegion region, boolean visible) {
      this.x0 = x0;
      this.y0 = y0;

      Sprite sprite = new Sprite(x0, y0, region, ResourceManager.getInstance().vbom);
      this.w = sprite.getWidth();
      this.h = sprite.getHeight();
      sprite.setVisible(visible);
      scene.attachChild(sprite);

      //  rectangle = new Rectangle(x0,y0,w,h,ResourceManager.getInstance().vbom);
      //  scene.attachChild(rectangle);
      //  rectangle.setColor(Color.RED);

      // sprite.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_DEFAULT,IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
      body = BodyFactory.getInstance().createBoxBody(x0, y0, w, h, BodyDef.BodyType.DynamicBody);
      physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body));
      // physicsWorld.registerPhysicsConnector( new PhysicsConnector(rectangle, body));

    }

    public float getX0() {
      return x0;
    }

    public float getY0() {
      return y0;
    }

    public float getW() {
      return w;
    }

    public float getH() {
      return h;
    }

    public Rectangle getRectangle() {
      return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
      this.rectangle = rectangle;
    }
  }
}
