package com.evolgames.entities.usage;

import static com.evolgames.physics.CollisionUtils.OBJECT;
import static com.evolgames.physics.CollisionUtils.OBJECTS_MIDDLE_CATEGORY;
import static com.evolgames.physics.PhysicsConstants.getEffectiveFireRate;
import static com.evolgames.physics.PhysicsConstants.getProjectileVelocity;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.properties.BodyUsageCategory;
import com.evolgames.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.ShooterProperties;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.Init;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.ToolUtils;

import org.andengine.audio.sound.Sound;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class Shooter extends Use {

    private List<ProjectileInfo> projectileInfoList;
    private float cyclicTime;
    private int maxRounds;
    private float reloadTime;
    private BodyUsageCategory type;
    private boolean loaded = true;
    private boolean readyToFire = false;
    private boolean loading = false;
    private int rounds;
    private float loadingTimer;
    private float readyTimer;
    private boolean continueFire;
    transient private List<ToolModel> missileModels;
    transient private Map<ProjectileInfo, ExplosiveParticleWrapper> projInfFireSourceMap;

    private boolean isHeavy;

    private int fireCountdown = 0;

    @SuppressWarnings("unused")
    public Shooter() {
    }

    public Shooter(UsageModel<?> rangedUsageModel, PhysicsScene physicsScene, boolean heavy, boolean mirrored) {
        this.type = rangedUsageModel.getType();
        this.isHeavy = heavy;
        RangedProperties rangedProperties = (RangedProperties) rangedUsageModel.getProperties();
        this.projectileInfoList =
                rangedProperties.getProjectileModelList().stream()
                        .map(m -> m.toProjectileInfo(mirrored))
                        .collect(Collectors.toList());
        fillMissileModels();
        this.projectileInfoList.forEach(projectileInfo -> projectileInfo.setUpdatedMuzzle(true));
        createFireSources(physicsScene.getWorldFacade());
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
        this.rounds = this.maxRounds;

    }

    public List<ProjectileInfo> getProjectileInfoList() {
        return projectileInfoList;
    }

    public void fillMissileModels() {
        this.missileModels = new ArrayList<>();
        for (ProjectileInfo projectileInfo : this.projectileInfoList) {
            try {
                ToolModel missileModel = ToolUtils.getProjectileModel(projectileInfo.getMissileFile(), projectileInfo.isAssetsMissile());
                this.missileModels.add(missileModel);
            } catch (PersistenceException | IOException | ParserConfigurationException |
                     SAXException e) {
               e.printStackTrace();
            }
        }
    }

    public void onTriggerPulled(PhysicsScene physicsScene) {
        if (readyToFire) {
            for (int i = 0, projectileInfoListSize = projectileInfoList.size(); i < projectileInfoListSize; i++) {
                fire(i, physicsScene);
            }

        }
    }

    private void startReload() {
        this.loadingTimer = 0;
        this.loading = true;
    }

    public void onTriggerReleased() {
        this.continueFire = false;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (fireCountdown > 0) {
            fireCountdown--;
        }
        if (this.projInfFireSourceMap != null) {
            for (ExplosiveParticleWrapper explosiveParticleWrapper : this.projInfFireSourceMap.values()) {
                explosiveParticleWrapper.setSpawnEnabled(fireCountdown > 0);
            }
        }
        boolean updatedMuzzles = projectileInfoList.stream().anyMatch(ProjectileInfo::isUpdatedMuzzle);
        if(updatedMuzzles){
            createFireSources(worldFacade);
        }
        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                ((PlayScene)worldFacade.getPhysicsScene()).onUsagesUpdated();
                this.loaded = true;
                this.rounds = this.maxRounds;
                this.loading = false;
                // Reload finished
                if (isHeavy) {
                    if (type == BodyUsageCategory.SHOOTER) {
                        ((PlayScene) worldFacade.getPhysicsScene()).onUsagesUpdated();
                    }
                }
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
                fire(i, worldFacade.getPhysicsScene());
            }
        }
    }

    private void decrementRounds() {
        this.rounds--;
        if (this.rounds <= 0) {
            this.loaded = false;
        }
    }


    private void fire(int index, PhysicsScene physicsScene) {
        ProjectileInfo projectileInfo = this.projectileInfoList.get(index);
        if (projectileInfo.getMuzzle() == null || !projectileInfo.getMuzzle().isActive()) {
            return;
        }
        ToolModel missileModel = this.missileModels.get(index);
        this.createProjectile(projectileInfo, missileModel, physicsScene);
        this.decrementRounds();
        this.readyToFire = false;

        if (this.type == BodyUsageCategory.SHOOTER_CONTINUOUS) {
            this.continueFire = true;
        }
        fireCountdown = 4;

        if (rounds <= 0) {
            startReload();
            ResourceManager.getInstance().gunEmptySound.setPriority(3);
            ResourceManager.getInstance().gunEmptySound.play();
        }
    }


    private void createProjectile(ProjectileInfo projectileInfo, ToolModel missileModel, PhysicsScene physicsScene) {
        GameEntity muzzleEntity = projectileInfo.getMuzzle().getTheMuzzleEntity();
        if(muzzleEntity.getBody()==null){
            return;
        }

        Vector2 begin = projectileInfo.getProjectileOrigin();
        Vector2 end = projectileInfo.getProjectileEnd();
        Vector2 endProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(end)
                        .cpy();
        Vector2 beginProjected =
                muzzleEntity
                        .getBody()
                        .getWorldPoint(begin)
                        .cpy();
        Vector2 directionProjected = endProjected.cpy().sub(beginProjected).nor();
        float muzzleVelocity = getProjectileVelocity(projectileInfo.getMuzzleVelocity());
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        Filter projectileFilter = new Filter();

        projectileFilter.categoryBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.maskBits = OBJECTS_MIDDLE_CATEGORY;
        projectileFilter.groupIndex = muzzleEntity.getGroupIndex();

        endProjected.mul(32f);
        Init bulletInit = new Init.Builder(endProjected.x, endProjected.y)
                .angle(GeometryUtils.calculateAngleRadians(directionProjected.x, directionProjected.y)+(float)(muzzleEntity.isMirrored()?0:Math.PI))
                .linearVelocity(muzzleVelocityVector)
                .filter(OBJECT, OBJECT, muzzleEntity.getGroupIndex())
                .isBullet(true)
                .recoil(muzzleEntity.getBody(), muzzleVelocityVector, beginProjected, projectileInfo.getRecoil())
                .build();
        if (missileModel.getBodies().size() == 0) {
            return;
        }
        missileModel.getBodies().get(0).setInit(bulletInit);

        if (missileModel.getBodies().size() > 1 && projectileInfo.getCasingInfo() != null) {
            Vector2 casingOrigin = projectileInfo.getCasingInfo().getAmmoOrigin();
            Vector2 casingDirection = projectileInfo.getCasingInfo().getAmmoDirection();
            Vector2 casingOriginProjected =
                    muzzleEntity
                            .getBody()
                            .getWorldPoint(casingOrigin)
                            .cpy();
            Vector2 casingDirectionProjected = muzzleEntity.getBody().getWorldVector(casingDirection).cpy();
            boolean clockwise = projectileInfo.getCasingInfo().isRotationOrientation();
            float angularVelocity =
                    clockwise ? (float) (projectileInfo.getCasingInfo().getRotationSpeed() * 5 * Math.PI * 2) :
                            (float) (-1 * projectileInfo.getCasingInfo().getRotationSpeed() * 5 * Math.PI * 2);
            float ejectionVelocity = projectileInfo.getCasingInfo().getLinearSpeed() * 10f;
            Vector2 ejectionVelocityVector = casingDirectionProjected.mul(ejectionVelocity);
            Init casingInit = new Init.Builder(casingOriginProjected.x * 32f, casingOriginProjected.y * 32f)
                    .linearVelocity(ejectionVelocityVector)
                    .angularVelocity(angularVelocity)
                    .angle(muzzleEntity.getBody().getAngle())
                    .filter(OBJECTS_MIDDLE_CATEGORY, OBJECTS_MIDDLE_CATEGORY, muzzleEntity.getGroupIndex()).build();
            missileModel.getBodies().get(1).setInit(casingInit);
        } else {
            if (missileModel.getBodies().size() > 1) {
                missileModel.getBodies().remove(1);
            }
        }

        GameGroup bulletGroup = physicsScene.createTool(missileModel, muzzleEntity.isMirrored());

        GameEntity projectile = bulletGroup.getGameEntityByIndex(0);
        physicsScene.getWorldFacade().scheduleGameEntityToDestroy(projectile, 60);
        if (bulletGroup.getEntities().size() > 1) {
            GameEntity casing = bulletGroup.getGameEntityByIndex(1);
            physicsScene.getWorldFacade().scheduleGameEntityToDestroy(casing, 120);
            for (GameEntity entity : muzzleEntity.getParentGroup().getEntities()) {
                physicsScene.getWorldFacade().addNonCollidingPair(entity, casing);
            }
        }
        Projectile projectileUse = new Projectile(ProjectileType.BULLET);
        projectileUse.setActive(true);
        projectile.getUseList().add(projectileUse);
        projectile.getUseList().forEach(
                use -> {
                    if (use instanceof TimeBomb) {
                        TimeBomb timeBomb = (TimeBomb) use;
                        timeBomb.onTriggered(physicsScene.getWorldFacade());
                    }
                }
        );

        Sound sound =  ResourceManager.getInstance().getProjectileSound(projectileInfo.getFireSound()).getSound();
        ResourceManager.getInstance().vibrate(50);
      ResourceManager.getInstance().tryPlaySound(sound,1f,3,muzzleEntity.getX(), muzzleEntity.getY());
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        if (!isHeavy) {
            List<PlayerSpecialAction> list = new ArrayList<>();
            list.add(PlayerSpecialAction.None);
            list.add(PlayerSpecialAction.FireLight);
            list.add(PlayerSpecialAction.AimLight);
            if(isLoaded().first) {
                list.add(PlayerSpecialAction.SingleShot);
            }
            return list;
        } else {
            List<PlayerSpecialAction> list = new ArrayList<>();
            list.add(PlayerSpecialAction.None);
            if (this.type == BodyUsageCategory.SHOOTER_CONTINUOUS) {
                list.add(PlayerSpecialAction.FireHeavy);
            } else {
                list.add(PlayerSpecialAction.AimHeavy);
                if (isLoaded().first) {
                    list.add(PlayerSpecialAction.Trigger);
                }
            }
            return list;
        }
    }

    public GameEntity getMuzzleEntity() {
        Optional<Muzzle> result = projectileInfoList.stream().map(ProjectileInfo::getMuzzle).findFirst();
        return result.map(Muzzle::getTheMuzzleEntity).orElse(null);
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        projectileInfoList.forEach(projectileInfo -> {
            projectileInfo.getProjectileOrigin().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileOrigin()));
            projectileInfo.getProjectileEnd().set(GeometryUtils.mirrorPoint(projectileInfo.getProjectileEnd()));
            if (projectileInfo.getCasingInfo() != null) {
                Vector2 ammoDir = projectileInfo.getCasingInfo().getAmmoDirection();
                Vector2 origin = projectileInfo.getCasingInfo().getAmmoOrigin();
                projectileInfo.getCasingInfo().setRotationOrientation(!projectileInfo.getCasingInfo().isRotationOrientation());
                origin.set(GeometryUtils.mirrorPoint(origin));
                ammoDir.set(-ammoDir.x, ammoDir.y);
            }
        });
        this.projInfFireSourceMap.values().forEach(
                ExplosiveParticleWrapper::detach
        );
        this.projectileInfoList.forEach(projectileInfo -> projectileInfo.setUpdatedMuzzle(true));
        createFireSources(physicsScene.getWorldFacade());
    }

    public Pair<Boolean,Float> isLoaded() {
        float loadedIn = Math.max(0,reloadTime-loadingTimer);
        return new Pair<>(loaded,loadedIn);
    }

    public void createFireSources(WorldFacade worldFacade) {
        this.projInfFireSourceMap = new HashMap<>();
        if(this.missileModels.isEmpty()){
            return;
        }
        this.projectileInfoList
                .forEach(
                        p -> {
                            if(p.getMuzzle()!=null&&p.isUpdatedMuzzle()) {
                                if (p.getFireRatio() >= 0.1f
                                        || p.getSmokeRatio() >= 0.1f
                                        || p.getSparkRatio() >= 0.1f) {
                                    Vector2 end = p.getProjectileEnd().cpy().mul(32f);
                                    Vector2 dir = end.cpy().sub(p.getProjectileOrigin()).nor();
                                    float nx = -dir.y;
                                    float ny = dir.x;
                                    Vector2 nor = new Vector2(nx,ny);
                                    int index = this.projectileInfoList.indexOf(p);
                                    float axisExtent = ToolUtils.getAxisExtent(this.missileModels.get(index), nor) / 2f;
                                    ExplosiveParticleWrapper fireSource =
                                            worldFacade
                                                    .createFireSource(
                                                            p.getMuzzle().getTheMuzzleEntity(),
                                                            end.cpy().sub(axisExtent * nor.x, axisExtent * nor.y),
                                                            end.cpy().add(axisExtent * nor.x, axisExtent * nor.y),
                                                            PhysicsConstants.getProjectileVelocity(p.getMuzzleVelocity())
                                                                    / 5f,
                                                            p.getFireRatio(),
                                                            p.getSmokeRatio(),
                                                            p.getSparkRatio(),
                                                            1f,
                                                            0.5f, 0.5f, 0f);
                                    fireSource.setSpawnEnabled(false);
                                    this.projInfFireSourceMap.put(p, fireSource);
                                    p.setUpdatedMuzzle(false);
                                }
                            }
                        });
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        this.projInfFireSourceMap.values().forEach(ExplosiveParticleWrapper::detach);
        if (ratio < 0.9f) {
            return false;
        }
        createFireSources(heir.getScene().getWorldFacade());
        return true;
    }
}
