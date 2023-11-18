package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.mesh.batch.TexturedMeshBatch;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.particles.wrappers.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.serialization.InitInfo;
import com.evolgames.entities.usage.Use;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.PhysicsScene;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.andengine.entity.primitive.Line;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

public class GameEntity extends EntityWithBody {

  private PhysicsScene<?> scene;
  private final List<LayerBlock> layerBlocks;
  private List<Use> useList;
  public boolean changed = true;
  private String name;
  private GameGroup parentGroup;
  private Vector2 center;
  private FireParticleWrapperWithPolygonEmitter fireParticleWrapperWithPolygonEmitter;
  private boolean isFireSetup;
  private TexturedMeshBatch batch;
  private int stainDataLimit;
  private boolean isBatcherSetup = false;
  private SpecialEntityType type = SpecialEntityType.Default;
  private int hangedPointerId;
  private float sortingValue;
  private float area;
  private boolean alive = true;
  private MosaicMesh mesh;
  private BodyDef.BodyType bodyType;
  private InitInfo initInfo;
  private String uniqueID = UUID.randomUUID().toString();

  public GameEntity(
      MosaicMesh mesh, PhysicsScene<?> scene, String entityName, List<LayerBlock> layerBlocks) {
    super();
    this.mesh = mesh;
    this.scene = scene;
    this.name = entityName;
    this.layerBlocks = layerBlocks;
    this.useList = new ArrayList<>();

    for (LayerBlock layerBlock : this.layerBlocks) {
      for (Block<?, ?> decorationBlock : layerBlock.getAssociatedBlocks()) {
        if (decorationBlock instanceof CoatingBlock) {
          CoatingBlock coatingBlock = ((CoatingBlock) decorationBlock);
          coatingBlock.setMesh(mesh);
        }
      }
    }

    computeArea();
    if (area >= 5) {
      if (hasStains()) {
        setupBatcher();
      }
    }
  }

  public PhysicsScene<?> getScene() {
    return scene;
  }

  private void computeArea() {
    this.area = 0;
    for (LayerBlock layerBlock : layerBlocks) {
      area += layerBlock.getBlockArea();
    }
  }

  public GameGroup getParentGroup() {
    return parentGroup;
  }

  public void setParentGroup(GameGroup parentGroup) {
    this.parentGroup = parentGroup;
  }

  public FireParticleWrapperWithPolygonEmitter getFireParticleWrapperWithPolygonEmitter() {
    return fireParticleWrapperWithPolygonEmitter;
  }

  public boolean isFireSetup() {
    return isFireSetup;
  }

  public void setupFire() {
    isFireSetup = true;
    fireParticleWrapperWithPolygonEmitter = new FireParticleWrapperWithPolygonEmitter(this);
    scene.attachChild(fireParticleWrapperWithPolygonEmitter.getParticleSystem());
    scene.sortChildren();
  }

  private void setupBatcher() {
    isBatcherSetup = true;
    float area = 0;
    for (LayerBlock b : layerBlocks) {
      area += b.getBlockArea();
    }
    int stainLimit = (int) (area / 32);
    if (stainLimit < 1) {
      stainLimit = 1;
    }
    stainDataLimit = Math.min(3000 * 64, stainLimit * 64 * 8);
    batch =
        new TexturedMeshBatch(
            ResourceManager.getInstance().texturedMesh,
            stainDataLimit + 12,
            ResourceManager.getInstance().vbom);
    mesh.attachChild(batch);
  }

  private void updateGrains() {
    for (LayerBlock block : this.getBlocks()) {
      for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
        grain.update();
      }
    }
  }

  public void onStep(float timeStep) {
    if (!this.getName().equals("Ground")) {
      updateGrains();
    }
    for (Use use : useList) {
      use.onStep(timeStep);
    }

    if (isFireSetup) {
      fireParticleWrapperWithPolygonEmitter.update();
    }
  }

  public Vector2 computeTouch(TouchEvent touch, boolean withHold) {
    Vector2 t =
        this.body.getLocalPoint(new Vector2(touch.getX() / 32f, touch.getY() / 32f)).cpy().mul(32f);
    Vector2 result = null;
    float minDis = Float.MAX_VALUE;
    for (Block<?, ?> block : layerBlocks) {
      LayerProperties layerProperties = (LayerProperties) block.getProperties();
      if (layerProperties.getSharpness() > 0.0f && withHold) {
        continue;
      }
      Vector2 point = GeometryUtils.calculateProjection(t, block.getVertices());
      if (point != null) {
        float dis = t.dst(point);
        if (dis < minDis) {
          minDis = dis;
          result = point;
        }
      }
    }
    if (result != null) {
      return body.getWorldPoint(result.cpy().mul(1 / 32f)).cpy().mul(32f);
    }
    return null;
  }

  public MosaicMesh getMesh() {
    return mesh;
  }

  public void setMesh(MosaicMesh mesh) {
    this.mesh = mesh;
  }

  public void setVisible(boolean b) {
    mesh.setVisible(b);
  }

  public float getMassOfGroup() {

    HashSet<GameEntity> entities = new HashSet<>();
    ArrayDeque<GameEntity> deque = new ArrayDeque<>();
    deque.push(this);
    while (!deque.isEmpty()) {
      GameEntity current = deque.pop();
      entities.add(current);
      if (current.getBody() != null) {
        for (JointEdge edge : current.getBody().getJointList()) {
          GameEntity entity = (GameEntity) edge.other.getUserData();
          if (entity != null) if (!entities.contains(entity)) deque.push(entity);
        }
      }
    }

    float mass = 0;
    for (GameEntity e : entities) {
      if (e.getBody() != null) {
        mass += e.getBody().getMass();
      }
    }
    return mass;
  }

  public boolean isNonValidStainPosition(LayerBlock block, StainBlock stainBlock) {
    float x = stainBlock.getLocalCenterX();
    float y = stainBlock.getLocalCenterY();
    for (Block<?, ?> b : block.getAssociatedBlocks()) {
      if (b instanceof StainBlock) {
        StainBlock other = (StainBlock) b;
        float distance = GeometryUtils.dst(x, y, other.getLocalCenterX(), other.getLocalCenterY());
        if (distance < 2f) {
          return true;
        }
      }
    }
    return false;
  }

  public void addStain(LayerBlock block, StainBlock stainBlock) {
    block.addAssociatedBlock(stainBlock);
    changed = true;
    if (!isBatcherSetup) {
      setupBatcher();
    }
  }

  private boolean hasStains() {
    for (LayerBlock b : layerBlocks) {
      for (Block<?, ?> decorationBlock : b.getAssociatedBlocks()) {
        if (decorationBlock instanceof StainBlock) {
          return true;
        }
      }
    }
    return false;
  }

  private int getStainDataCount(ArrayList<StainBlock> stains) {
    int count = 0;
    for (StainBlock b : stains) {
      if (b.getData() == null) {
        b.computeData();
      }
      count += b.getData().length;
    }
    return count;
  }

  private ArrayList<StainBlock> getStains() {
    ArrayList<StainBlock> result = new ArrayList<>();
    for (LayerBlock b : layerBlocks) {
      for (Block<?, ?> decorationBlock : b.getAssociatedBlocks()) {
        if (decorationBlock instanceof StainBlock) {
          result.add((StainBlock) decorationBlock);
        }
      }
    }
    return result;
  }

  public void redrawStains() {
    if (batch == null || !changed) {
      return;
    }

    batch.reset();
    ArrayList<StainBlock> allStains = getStains();
    int stainDataCount = getStainDataCount(allStains);
    while (stainDataCount > stainDataLimit) {
      StainBlock removedStainBlock = allStains.get(0);
      for (LayerBlock b : layerBlocks) {
        if (b.getAssociatedBlocks().contains(removedStainBlock)) {
          b.getAssociatedBlocks().remove(removedStainBlock);
          break;
        }
      }
      stainDataCount -= removedStainBlock.getData().length;
      allStains.remove(removedStainBlock);
    }

    for (LayerBlock layerBlock : getBlocks()) {
      ArrayList<? extends Block<?, ?>> associatedBlocks = layerBlock.getAssociatedBlocks();
      List<StainBlock> stainBlocks =
          associatedBlocks.stream()
              .filter(e -> e instanceof StainBlock)
              .map(e -> (StainBlock) e)
              .collect(Collectors.toList());
      stainBlocks.sort(Comparator.comparing(StainBlock::getPriority));
      for (StainBlock stain : stainBlocks) {
        Color color = stain.getProperties().getColor();
        batch.draw(
            stain.getTextureRegion(),
            stain.getData(),
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            color.getAlpha());
      }
    }
    batch.submit();
    changed = false;
  }

  public void dispose() {
    if (batch != null) {
      batch.dispose();
      batch.detachSelf();
    }
  }

  public Vector2 getCenter() {
    return center;
  }

  public void setCenter(Vector2 center) {
    this.center = center;
  }

  public boolean isAlive() {
    return alive;
  }

  public void setAlive(boolean alive) {
    this.alive = alive;
  }

  public void detach() {
    mesh.detachSelf();
    if (fireParticleWrapperWithPolygonEmitter != null) {
      scene.getWorldFacade().removeFireParticleWrapper(fireParticleWrapperWithPolygonEmitter);
    }
    dispose();
  }

  public void createJuiceSources() {
    for (LayerBlock block : this.getBlocks()) {
      LayerProperties properties = block.getProperties();
      ArrayList<FreshCut> freshCuts = block.getFreshCuts();
      for (FreshCut freshCut : freshCuts) {
        if (freshCut.getLength() < 1f) {
          continue;
        }
        if (freshCut.getLimit() > 0 && properties.isJuicy()) {
          scene.getWorldFacade().createJuiceSource(this, block, freshCut);
        }
        if (freshCut instanceof SegmentFreshCut) {
          SegmentFreshCut segmentFreshCut = (SegmentFreshCut) freshCut;
          if (segmentFreshCut.isInner()) {
            Line line =
                new Line(
                    segmentFreshCut.first.x,
                    segmentFreshCut.first.y,
                    segmentFreshCut.second.x,
                    segmentFreshCut.second.y,
                    2,
                    ResourceManager.getInstance().vbom);
            line.setColor(Color.BLACK);
            mesh.attachChild(line);
          }
        }
      }
    }
  }

  @SafeVarargs
  public final <T> boolean hasUsage(Class<T>... targetTypes) {
    for (Use obj : this.useList) {
      for (Class<T> targetType : targetTypes) {
        if (targetType.isInstance(obj)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean hasActiveUsage(Class<?>... targetTypes) {
    for (Use obj : this.useList) {
      for (Class<?> targetType : targetTypes) {
        if (targetType.isInstance(obj) && obj.isActive()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameEntity that = (GameEntity) o;
    return Objects.equals(layerBlocks, that.layerBlocks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layerBlocks);
  }

  public float getSortingValue() {
    return sortingValue;
  }

  public void setSortingValue(float sortingValue) {
    this.sortingValue = sortingValue;
  }

  public List<GameEntity> getConnectedEntities() {
    List<GameEntity> list = new ArrayList<>();
    scene
        .getPhysicsWorld()
        .getJoints()
        .forEachRemaining(
            e -> {
              if (e.getBodyA() == this.getBody()) {
                list.add((GameEntity) e.getBodyB().getUserData());
              } else if (e.getBodyB() == this.getBody()) {
                list.add((GameEntity) e.getBodyA().getUserData());
              }
            });
    return list;
  }

  public SpecialEntityType getType() {
    return type;
  }

  public void setType(SpecialEntityType type) {
    this.type = type;
  }

  public <T extends Use> T getActiveUsage(Class<T> targetType) {
    for (Use obj : this.useList) {
      if (targetType.isInstance(obj) && obj.isActive()) {
        return (T) obj;
      }
    }
    return null;
  }

  public <T extends Use> T getUsage(Class<T> targetType) {
    for (Use obj : this.useList) {
      if (targetType.isInstance(obj)) {
        return (T) obj;
      }
    }
    return null;
  }

  public int getHangedPointerId() {
    return hangedPointerId;
  }

  public void setHangedPointerId(int hangedPointerId) {
    this.hangedPointerId = hangedPointerId;
  }

  public List<Use> getUseList() {
    return useList;
  }

  public float getArea() {
    return area;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<LayerBlock> getBlocks() {
    return layerBlocks;
  }

  public BodyDef.BodyType getBodyType() {
    return bodyType;
  }

  public void setBodyType(BodyDef.BodyType bodyType) {
    this.bodyType = bodyType;
  }

  public void setInitInfo(InitInfo initInfo) {
    this.initInfo = initInfo;
  }

  public InitInfo getCreationInitInfo() {
    if (getBody() == null) {
      return this.initInfo;
    }
    InitInfo initInfo = new InitInfo();
    initInfo.setLinearVelocity(this.body.getLinearVelocity());
    initInfo.setBullet(this.body.isBullet());
    initInfo.setAngularVelocity(this.body.getAngularVelocity());
    initInfo.setX(this.body.getPosition().x);
    initInfo.setY(this.body.getPosition().y);
    initInfo.setAngle(this.body.getAngle());
    initInfo.setFilter(this.initInfo.getFilter());
    return initInfo;
  }

  public String getUniqueID() {
    return uniqueID;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueID = uniqueId;
  }

  public void setScene(PhysicsScene<?> scene) {
    this.scene = scene;
  }

  public void setUseList(List<Use> useList) {
    this.useList = useList;
  }
}
