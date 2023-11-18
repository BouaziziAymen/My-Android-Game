package com.evolgames.entities.usage;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;
import static com.evolgames.physics.PhysicsConstants.getEffectiveFireRate;
import static com.evolgames.physics.PhysicsConstants.getProjectileVelocity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.particles.persistence.PersistenceException;
import com.evolgames.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.ShooterProperties;
import com.evolgames.entities.usage.infos.ProjectileInfo;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Shooter extends Use {

  private final List<ProjectileInfo> projectileInfoList;
  private final float cyclicTime;
  private final int maxRounds;
  private final float reloadTime;
  private final BodyUsageCategory type;
  private boolean loaded = false;
  private boolean readyToFire = false;
  private boolean loading = false;
  private int rounds;
  private float loadingTimer;
  private float readyTimer;
  private boolean continueFire;
  private Vector2 target;
  transient private List<ToolModel> missileModels;

  public Shooter(UsageModel<?> rangedUsageModel) {
    this.type = rangedUsageModel.getType();
    RangedProperties rangedProperties = (RangedProperties) rangedUsageModel.getProperties();
    this.projectileInfoList =
        rangedProperties.getProjectileModelList().stream()
            .map(ProjectileModel::toProjectileInfo)
            .collect(Collectors.toList());
   fillMissileModels();
    switch (rangedUsageModel.getType()) {
      case SHOOTER:
        ShooterProperties shooterProperties = (ShooterProperties) rangedProperties;
        this.reloadTime = shooterProperties.getReloadTime();
        this.maxRounds = shooterProperties.getNumberOfRounds();
        this.cyclicTime = 0;
        break;
      case SHOOTER_CONTINUOUS:
        ContinuousShooterProperties automaticProperties =
            (ContinuousShooterProperties) rangedProperties;
        this.cyclicTime = 1 / getEffectiveFireRate(automaticProperties.getFireRate());
        this.maxRounds = automaticProperties.getNumberOfRounds();
        this.reloadTime = automaticProperties.getReloadTime();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + rangedUsageModel.getType());
    }
  }

  private void fillMissileModels() {
    this.missileModels = new ArrayList<>();
    for (ProjectileInfo projectileInfo : this.projectileInfoList) {
      try {
        ToolModel toolModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile());
        missileModels.add(toolModel);
      } catch (PersistenceException | ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void onTriggerPulled() {
    for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
      fire(i);
    }
  }

  public void startReload() {
    this.loadingTimer = 0;
    this.loading = true;
  }

  public void onTriggerReleased() {
    this.continueFire = false;
  }

  @Override
  public void onStep(float deltaTime) {
    for (ProjectileInfo projectileInfo : this.projectileInfoList) {
      if (projectileInfo.getFireSource() != null) {
        projectileInfo.getFireSource().setSpawnEnabled(false);
      }
    }
    if (this.loading) {
      this.loadingTimer += deltaTime;
      if (this.loadingTimer > this.reloadTime) {
        this.loaded = true;
        this.rounds = this.maxRounds;
        this.loading = false;
        // Reload finished
      }
      return;
    }

    if (!this.readyToFire) {
      this.readyTimer += deltaTime;
      if (this.readyTimer > this.cyclicTime) {
        this.readyTimer = 0;
        this.readyToFire = true;
      }
    }

    if (this.loaded && this.readyToFire && this.continueFire) {
      for (int i = 0; i < this.projectileInfoList.size(); i++) {
        fire(i);
      }
    }
  }

  private void decrementRounds() {
    this.rounds--;
    if (this.rounds == 0) {
      this.loaded = false;
    }
  }

  private void fire(int index) {
    ProjectileInfo projectileInfo = this.projectileInfoList.get(index);
    ToolModel missileModel = this.missileModels.get(index);
    this.createBullet(projectileInfo,missileModel);
    this.decrementRounds();
    this.readyToFire = false;

    if (projectileInfo.getCasingInfo() != null) {
      createBulletCasing(projectileInfo,missileModel);
    }
    if (this.type == BodyUsageCategory.SHOOTER_CONTINUOUS) {
      this.continueFire = true;
    }

    if (projectileInfo.getFireSource() != null) {
      projectileInfo.getFireSource().setSpawnEnabled(true);
    }
  }

  private void createBulletCasing(ProjectileInfo projectileInfo, ToolModel missileModel) {
    GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();
    ArrayList<LayerBlock> blocks =
        BlockUtils.createBlocks(missileModel.getBodies().get(1));
    Vector2 begin = projectileInfo.getCasingInfo().getAmmoOrigin();
    Vector2 direction = projectileInfo.getCasingInfo().getAmmoDirection();
    Vector2 beginProjected =
        muzzleEntity
            .getBody()
            .getWorldPoint(begin.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f))
            .cpy();
    Vector2 directionProjected = muzzleEntity.getBody().getWorldVector(direction).cpy();
    boolean clockwise = projectileInfo.getCasingInfo().isRotationOrientation();
    float angularVelocity =
        clockwise ? 1 : -1 * projectileInfo.getCasingInfo().getRotationSpeed() * 10;
    float ejectionVelocity = projectileInfo.getCasingInfo().getLinearSpeed() * 10;
    Vector2 ejectionVelocityVector = directionProjected.mul(ejectionVelocity);
    BodyInit bodyInit =
        new TransformInit(
            new AngularVelocityInit(
                new LinearVelocityInit(
                    new BodyInitImpl(OBJECTS_MIDDLE_CATEGORY), ejectionVelocityVector),
                angularVelocity),
            beginProjected.x,
            beginProjected.y,
            muzzleEntity.getBody().getAngle());
    GameEntityFactory.getInstance()
        .createIndependentGameEntity(
            muzzleEntity.getParentGroup(),
            blocks,
            beginProjected,
            muzzleEntity.getBody().getAngle(),
            bodyInit,
            "shell");
  }

  private void createBullet(ProjectileInfo projectileInfo, ToolModel missileModel) {
    GameEntity muzzleEntity = projectileInfo.getMuzzleEntity();
    ArrayList<LayerBlock> blocks =
        BlockUtils.createBlocks(missileModel.getBodies().get(0));
    Vector2 begin = projectileInfo.getProjectileOrigin();
    Vector2 end = projectileInfo.getProjectileEnd();

    Vector2 endProjected =
        muzzleEntity
            .getBody()
            .getWorldPoint(end.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f))
            .cpy();
    Vector2 beginProjected =
        muzzleEntity
            .getBody()
            .getWorldPoint(begin.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f))
            .cpy();
    Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
    float muzzleVelocity = getProjectileVelocity(projectileInfo.getMuzzleVelocity());
    Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
    Filter filter = new Filter();
    filter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
    filter.maskBits = OBJECTS_MIDDLE_CATEGORY;
    filter.groupIndex = muzzleEntity.getGroupIndex();
    BodyInit bodyInit =
        new BulletInit(
            new TransformInit(
                new LinearVelocityInit(new BodyInitImpl(filter), muzzleVelocityVector),
                endProjected.x,
                endProjected.y,
                muzzleEntity.getBody().getAngle()),
            true);
    GameEntity bullet =
        GameEntityFactory.getInstance()
            .createIndependentGameEntity(
                muzzleEntity.getParentGroup(),
                blocks,
                endProjected,
                GeometryUtils.calculateAngleRad(directionProjected.x, directionProjected.y),
                new RecoilInit(
                    bodyInit,
                    muzzleEntity.getBody(),
                    projectileInfo.getRecoil(),
                    muzzleVelocityVector,
                    beginProjected),
                "bullet");
    Projectile projectile = new Projectile();
    projectile.setActive(true);

    bullet.getUseList().add(projectile);
    ResourceManager.getInstance()
        .gunshotSounds
        .get(projectileInfo.getFireSound())
        .getSoundList()
        .get(0)
        .play();
  }

  @Override
  public PlayerSpecialAction getAction() {
    return PlayerSpecialAction.Shoot;
  }

  public boolean isLoaded() {
    return loaded;
  }

  public Vector2 getTarget() {
    return this.target;
  }

  public void setTarget(Vector2 target) {
    this.target = target;
  }

  public boolean isLoading() {
    return loading;
  }
}
