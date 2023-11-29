package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_BACK_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_FRONT_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public abstract class PhysicsScene<T extends UserInterface<?>> extends AbstractScene<T> {

  protected final WorldFacade worldFacade;
  protected final List<GameGroup> gameGroups = new ArrayList<>();
  protected HashMap<Integer, Hand> hands = new HashMap<>();

  public PhysicsScene(Camera pCamera, SceneType sceneName) {
    super(pCamera, sceneName);
    this.worldFacade = new WorldFacade(this);
    BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
    GameEntityFactory.getInstance().create(this.worldFacade.getPhysicsWorld(), this);
    BodyFactory.getInstance().create(this.worldFacade.getPhysicsWorld());
    Invoker.setScene(this);
  }

  protected void createTool(ToolModel toolModel) {
    ArrayList<BodyModel> bodies = toolModel.getBodies();
    ArrayList<JointModel> joints = toolModel.getJoints();
    ArrayList<GameEntity> gameEntities = new ArrayList<>();
    for (BodyModel bodyModel : bodies) {
      if (bodyModel.getLayers().size() == 0) {
        continue;
      }
      List<List<Vector2>> list = new ArrayList<>();
      for (LayerModel layerModel : bodyModel.getLayers()) {
        list.add(layerModel.getPoints());
      }

      Vector2 center = GeometryUtils.calculateCenter(list);
      ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(bodyModel, center);
      if (blocks.size() == 0 || center == null) {
        return;
      }
      BodyInit bodyInit =
          new BulletInit(
              new TransformInit(
                  new BodyInitImpl(
                      (short)
                          (OBJECTS_MIDDLE_CATEGORY
                              | OBJECTS_BACK_CATEGORY
                              | OBJECTS_FRONT_CATEGORY)),
                  center.x / 32F,
                  center.y / 32F,
                  0),
              false);
      GameEntity gameEntity =
          GameEntityFactory.getInstance()
              .createGameEntity(
                  center.x / 32F,
                  center.y / 32F,
                  0,
                  bodyInit,
                  blocks,
                  BodyDef.BodyType.DynamicBody,
                  "weapon");
      gameEntities.add(gameEntity);
      bodyModel.setGameEntity(gameEntity);
      gameEntity.setCenter(center);
    }
    // Handle usage
    List<ProjectileModel> projectileModels =
        bodies.stream()
            .map(BodyModel::getProjectileModels)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    projectileModels.forEach(
        projectileModel ->
            projectileModel.setMuzzleEntity(
                bodies.stream()
                    .filter(e -> e.getBodyId() == projectileModel.getBodyId())
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Body not found!"))
                    .getGameEntity()));

    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(
                    e ->
                        e.getType() == BodyUsageCategory.SHOOTER
                            || e.getType() == BodyUsageCategory.SHOOTER_CONTINUOUS)
                .forEach(
                    e -> {
                      Shooter shooter = new Shooter(e,this);
                      usageBodyModel.getGameEntity().getUseList().add(shooter);
                    }));

    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.TIME_BOMB)
                .forEach(
                    e -> {
                      TimeBomb timeBomb = new TimeBomb(e);
                      usageBodyModel.getGameEntity().getUseList().add(timeBomb);
                      timeBomb.setGameEntity(usageBodyModel.getGameEntity());
                    }));
    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.SLASHER)
                .forEach(
                    e -> {
                      Slasher slasher = new Slasher();
                      usageBodyModel.getGameEntity().getUseList().add(slasher);
                    }));
    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.STABBER)
                .forEach(
                    e -> {
                      Stabber stabber = new Stabber();
                      usageBodyModel.getGameEntity().getUseList().add(stabber);
                    }));
    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.BLUNT)
                .forEach(
                    e -> {
                      Smasher smasher = new Smasher();
                      usageBodyModel.getGameEntity().getUseList().add(smasher);
                    }));
    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.THROWING)
                .forEach(
                    e -> {
                      Throw throwable = new Throw();
                      usageBodyModel.getGameEntity().getUseList().add(throwable);
                    }));

    // Create game group
    GameGroup gameGroup = new GameGroup(GroupType.OTHER, gameEntities);
    this.addGameGroup(gameGroup);
    this.sortChildren();
    // Create joints
    for (JointModel jointModel : joints) {
      createJointFromModel(jointModel);
    }

  }

  protected void createJointFromModel(JointModel jointModel) {
    BodyModel bodyModel1 = jointModel.getBodyModel1();
    BodyModel bodyModel2 = jointModel.getBodyModel2();

    GameEntity entity1 = bodyModel1.getGameEntity();
    GameEntity entity2 = bodyModel2.getGameEntity();

    if (entity1 == null || entity2 == null) {
      return;
    }
      JointDef jointDef = jointModel.createJointDef(entity1.getCenter(),entity2.getCenter());

    getWorldFacade().addJointToCreate(jointDef, entity1, entity2);
  }

  public WorldFacade getWorldFacade() {
    return worldFacade;
  }

  public PhysicsWorld getPhysicsWorld() {
    return getWorldFacade().getPhysicsWorld();
  }

  @Override
  public void populate() {}

  @Override
  public void detach() {}

  @Override
  public void onPause() {}

  @Override
  public void onResume() {}

  public List<GameGroup> getGameGroups() {
    return gameGroups;
  }

  public void addGameGroup(GameGroup gameGroup) {
    gameGroups.add(gameGroup);
  }

  public HashMap<Integer, Hand> getHands() {
    return hands;
  }

  public void setMouseJoint(MouseJoint joint, GameEntity gameEntity, MouseJointDef jointDef) {
    if (hands.get(gameEntity.getHangedPointerId()) != null) {
      Objects.requireNonNull(hands.get(gameEntity.getHangedPointerId()))
          .setMouseJoint(joint,jointDef, gameEntity);
    }
  }

  public void onDestroyMouseJoint(MouseJoint j) {
    Optional<Hand> hand = hands.values().stream().filter(e -> e.getMouseJoint() == j).findFirst();
    hand.ifPresent(Hand::onMouseJointDestroyed);
  }

  public GameEntity getGameEntityByUniqueId(String uniqueId) {
    for (GameGroup gameGroup : gameGroups) {
      for (GameEntity gameEntity : gameGroup.getGameEntities()) {
        if (gameEntity.getUniqueID().equals(uniqueId)) {
          return gameEntity;
        }
      }
    }
    return null;
  }

  public void setHands(HashMap<Integer, Hand> hands) {
    this.hands = hands;
  }
}
