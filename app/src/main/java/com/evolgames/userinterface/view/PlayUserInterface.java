package com.evolgames.userinterface.view;

import static com.evolgames.physics.CollisionConstants.OBJECTS_BACK_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_FRONT_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.andengine.input.touch.TouchEvent;

public class PlayUserInterface extends UserInterface<GameScene> {

  private final Switcher particularUsageSwitcher;
  private PlayerSpecialAction[] particularUsages;

  public PlayUserInterface(GameScene scene) {
    super(scene);

    particularUsageSwitcher =
        new Switcher(
            800f - 72f,
            300,
            ResourceManager.getInstance().particularUsages,
            32f,
            (index) -> scene.setSpecialAction(this.particularUsages[index]));
    particularUsageSwitcher.reset();
    addElement(particularUsageSwitcher);

    Switcher generalUsageSwitcher =
        new Switcher(
            800f - 72f,
            300f + 64f,
            ResourceManager.getInstance().generalUsages,
            32f,
            (index) -> scene.setAction(PlayerAction.values()[index]));
    generalUsageSwitcher.reset(0, 1, 2, 3, 4, 5);
    addElement(generalUsageSwitcher);

    //        Button<DrawButtonBoardController> createButton = new
    // Button<>(ResourceManager.getInstance().simpleButtonTextureRegion, Button.ButtonType.OneClick,
    // true);
    //        createButton.setBehavior(new
    // ButtonBehavior<DrawButtonBoardController>(drawButtonBoardController, createButton) {
    //            @Override
    //            public void informControllerButtonClicked() {
    //            }
    //
    //            @Override
    //            public void informControllerButtonReleased() {
    //                getToolModel().createTool();
    //            }
    //        });
    //        createButton.setPosition(0, 480 - createButton.getHeight());
    //        addElement(createButton);
    setUpdated(true);
  }

  @Override
  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {}

  @Override
  public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
    return super.onTouchHud(pTouchEvent, isTouched);
  }

  public void updateParticularUsageSwitcher(PlayerSpecialAction[] usages) {
    Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal));
    this.particularUsages = Arrays.copyOf(usages, usages.length);
    Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal).reversed());
    this.particularUsageSwitcher.reset(Arrays.stream(usages).mapToInt(e -> e.iconId).toArray());
  }

  public void createTool(ToolModel toolModel) {
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
      bodyModel.getBombModels().forEach(bombModel -> bombModel.setGameEntity(gameEntity));
      bodyModel
          .getProjectileModels()
          .forEach(
              p -> {
                ProjectileProperties properties = p.getProperties();
                if (properties.getFireRatio() >= 0.1f
                    || properties.getSmokeRatio() >= 0.1f
                    || properties.getSparkRatio() >= 0.1f) {
                  Vector2 end = properties.getProjectileEnd();
                  Vector2 dir = end.cpy().sub(properties.getProjectileOrigin()).nor();
                  Vector2 nor = new Vector2(-dir.y, dir.x);
                  Vector2 e = end.cpy().sub(gameEntity.getCenter());
                  float extent = ToolUtils.getAxisExtent(p.getMissileModel(), nor) / 2f;
                  ExplosiveParticleWrapper fireSource =
                      scene
                          .getWorldFacade()
                          .createFireSource(
                              gameEntity,
                              e.cpy().sub(extent * nor.x, extent * nor.y),
                              e.cpy().add(extent * nor.x, extent * nor.y),
                              PhysicsConstants.getProjectileVelocity(properties.getMuzzleVelocity())
                                  / 10f,
                              properties.getFireRatio(),
                              properties.getSmokeRatio(),
                              properties.getSparkRatio(),
                              0.1f,
                              2000);
                  fireSource.setSpawnEnabled(false);
                  p.setFireSource(fireSource);
                }
              });
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
                      Shooter shooter = new Shooter(e);
                      usageBodyModel.getGameEntity().getUseList().add(shooter);
                    }));

    bodies.forEach(
        usageBodyModel ->
            usageBodyModel.getUsageModels().stream()
                .filter(e -> e.getType() == BodyUsageCategory.TIME_BOMB)
                .forEach(
                    e -> {
                      TimeBomb timeBomb = new TimeBomb(e, scene.getWorldFacade());
                      usageBodyModel.getGameEntity().getUseList().add(timeBomb);
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

    // Create joints
    for (JointModel jointModel : joints) {
      createJointFromModel(jointModel);
    }
    // Create game group
    GameGroup gameGroup = new GameGroup(GroupType.OTHER,gameEntities);
    scene.addGameGroup(gameGroup);
    scene.sortChildren();
  }

  private void createJointFromModel(JointModel jointModel) {
    BodyModel bodyModel1 = jointModel.getBodyModel1();
    BodyModel bodyModel2 = jointModel.getBodyModel2();

    GameEntity entity1 = bodyModel1.getGameEntity();
    GameEntity entity2 = bodyModel2.getGameEntity();

    if (entity1 == null || entity2 == null) {
      return;
    }
    if (jointModel.getJointShape() != null) {
      Vector2 u1 = jointModel.getJointShape().getBegin().cpy().sub(entity1.getCenter());
      Vector2 u2 = jointModel.getJointShape().getEnd().cpy().sub(entity2.getCenter());

      if (jointModel.getJointDef() instanceof RevoluteJointDef) {
        RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointModel.getJointDef();
        revoluteJointDef.localAnchorA.set(u1.mul(1 / 32f));
        revoluteJointDef.localAnchorB.set(u2.mul(1 / 32f));

      } else if (jointModel.getJointDef() instanceof WeldJointDef) {
        WeldJointDef weldJointDef = (WeldJointDef) jointModel.getJointDef();
        weldJointDef.localAnchorA.set(u1.mul(1 / 32f));
        weldJointDef.localAnchorB.set(u2.mul(1 / 32f));
      } else if (jointModel.getJointDef() instanceof DistanceJointDef) {
        DistanceJointDef distanceJointDef = (DistanceJointDef) jointModel.getJointDef();
        distanceJointDef.localAnchorA.set(u1.mul(1 / 32f));
        distanceJointDef.localAnchorB.set(u2.mul(1 / 32f));

      } else if (jointModel.getJointDef() instanceof PrismaticJointDef) {
        PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointModel.getJointDef();
        prismaticJointDef.localAnchorA.set(u1.mul(1 / 32f));
        prismaticJointDef.localAnchorB.set(u2.mul(1 / 32f));
      }
    }
    scene.getWorldFacade().addJointToCreate(jointModel.getJointDef(), entity1, entity2);
  }
}
