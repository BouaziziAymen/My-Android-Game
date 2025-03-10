package com.evolgames.dollmutilationgame.physics;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.particles.systems.SpawnAction;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.ClusterLiquidParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.DataExplosiveParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.ExplosiveParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.Fire;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.FrostParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.LiquidParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.PowderParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.SegmentExplosiveParticleWrapper;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.SegmentLiquidParticleWrapper;
import com.evolgames.dollmutilationgame.helpers.ElementCouple;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.BlockIntersectionCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.BlockQueryCallBack;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.CheckEmptyCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.CutRayCastCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.DebugCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.DetectionRayCastCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.FluxInnerRayCastCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.FluxRayCastCallback;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.GameEntityQueryCallBack;
import com.evolgames.dollmutilationgame.physics.entities.callbacks.SimpleDetectionRayCastCallback;
import com.evolgames.dollmutilationgame.physics.entities.explosions.Explosion;
import com.evolgames.dollmutilationgame.physics.entities.explosions.ImpactInterface;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.scenes.PlayScene;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.basics.GameGroup;
import com.evolgames.dollmutilationgame.entities.basics.GroupType;
import com.evolgames.dollmutilationgame.entities.basics.SpecialEntityType;
import com.evolgames.dollmutilationgame.entities.blocks.AssociatedBlock;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.blocks.StainBlock;
import com.evolgames.dollmutilationgame.entities.blockvisitors.ImpactData;
import com.evolgames.dollmutilationgame.entities.blockvisitors.ShatterVisitor;
import com.evolgames.dollmutilationgame.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;
import com.evolgames.dollmutilationgame.entities.commandtemplate.TimedCommand;
import com.evolgames.dollmutilationgame.entities.contact.ContactObserver;
import com.evolgames.dollmutilationgame.entities.contact.GameEntityContactListener;
import com.evolgames.dollmutilationgame.entities.contact.Pair;
import com.evolgames.dollmutilationgame.entities.cut.Cut;
import com.evolgames.dollmutilationgame.entities.cut.CutPoint;
import com.evolgames.dollmutilationgame.entities.cut.CutType;
import com.evolgames.dollmutilationgame.entities.cut.FreshCut;
import com.evolgames.dollmutilationgame.entities.cut.PointsFreshCut;
import com.evolgames.dollmutilationgame.entities.cut.Segment;
import com.evolgames.dollmutilationgame.entities.cut.SegmentFreshCut;
import com.evolgames.dollmutilationgame.entities.factories.BlockFactory;
import com.evolgames.dollmutilationgame.entities.factories.GameEntityFactory;
import com.evolgames.dollmutilationgame.entities.factories.MaterialFactory;
import com.evolgames.dollmutilationgame.entities.pools.ImpactDataPool;
import com.evolgames.dollmutilationgame.entities.properties.JointBlockProperties;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.dollmutilationgame.entities.ragdoll.RagDoll;
import com.evolgames.dollmutilationgame.entities.usage.ImpactBomb;
import com.evolgames.dollmutilationgame.entities.usage.Penetrating;
import com.evolgames.dollmutilationgame.entities.usage.Projectile;
import com.evolgames.dollmutilationgame.entities.usage.Smasher;
import com.evolgames.dollmutilationgame.entities.usage.Stabber;
import com.evolgames.dollmutilationgame.physics.entities.TopographyData;
import com.evolgames.dollmutilationgame.physics.entities.Touch;
import com.evolgames.dollmutilationgame.utilities.BlockUtils;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.utilities.MathUtils;
import com.evolgames.dollmutilationgame.utilities.MyColorUtils;
import com.evolgames.dollmutilationgame.utilities.PhysicsUtils;
import com.evolgames.dollmutilationgame.utilities.Utils;
import com.evolgames.dollmutilationgame.utilities.Vector2Utils;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorldFacade implements ContactObserver {
    public static Color frostColor = new Color(172f / 255f, 213f / 255f, 243f / 255f);
    public CheckEmptyCallback checkEmptyCallback;
    private final Set<LiquidParticleWrapper> liquidParticleWrappers = new HashSet<>();
    private final Set<PowderParticleWrapper> powderParticleWrappers = new HashSet<>();
    private final Set<ExplosiveParticleWrapper> explosivesParticleWrappers = new HashSet<>();

    public DebugCallback debugCallback = new DebugCallback();

    private final List<Explosion> explosions = new ArrayList<>();
    private final List<Fire> flames = new ArrayList<>();
    private final List<TimedCommand> timedCommands = new ArrayList<>();
    private final List<Touch> touches = new ArrayList<>();
    private final SimpleDetectionRayCastCallback simpleDetectionRayCastCallback = new SimpleDetectionRayCastCallback();
    private final FluxRayCastCallback fluxRayCastCallback = new FluxRayCastCallback();
    private final FluxInnerRayCastCallback fluxInnerRayCastCallback = new FluxInnerRayCastCallback();
    private final BlockIntersectionCallback blockIntersectionCallback = new BlockIntersectionCallback();
    private final GameEntityQueryCallBack queryCallBack = new GameEntityQueryCallBack();
    private final BlockQueryCallBack blockQueryCallBack = new BlockQueryCallBack();
    private final LinearRayCastCallback rayCastCallback = new LinearRayCastCallback();
    private final DetectionRayCastCallback detectionRayCastCallback = new DetectionRayCastCallback();
    private final CutRayCastCallback cutRayCastCallback = new CutRayCastCallback();
    private final GameEntityContactListener contactListener;
    private final PhysicsWorld physicsWorld;
    private final PhysicsScene scene;
    Random random = new Random();
    Vector2 result = new Vector2();
    Vector2 sum = new Vector2();
    private GameGroup ground;
    private final Set<FrostParticleWrapper> frostParticleWrappers = new HashSet<>();

    public WorldFacade(PhysicsScene scene) {
        this.scene = scene;
        physicsWorld = new PhysicsWorld(obtain(0, PhysicsConstants.gravity), false);
        contactListener = new GameEntityContactListener(this);
        physicsWorld.setContactListener(contactListener);

        scene.registerUpdateHandler(physicsWorld);
        physicsWorld.setVelocityIterations(20);
        physicsWorld.setPositionIterations(10);
        physicsWorld.setContinuousPhysics(true);
    }

    private static void processPenetrationSound(    MaterialFactory.MaterialAcousticType materialAcousticType, float collisionImpulse, float x, float y) {
      //  MaterialFactory.MaterialAcousticType materialAcousticType = MaterialFactory.getInstance().getMaterialAcousticType(layerBlock.getProperties().getMaterialNumber());
        if (materialAcousticType == null) {
            return;
        }
        Sound sound = null;
        switch (materialAcousticType) {
            case SOFT:
                sound = ResourceManager.getInstance().penetrationSounds.get(0).getSound();
                break;
            case METAL:
                if (Math.random() < 0.5f) {
                    sound = ResourceManager.getInstance().penetrationSounds.get(3).getSound();
                } else {
                    sound = ResourceManager.getInstance().penetrationSounds.get(6).getSound();
                }
                break;
            case HARD_METAL:
                if (Math.random() < 0.5f) {
                    sound = ResourceManager.getInstance().penetrationSounds.get(4).getSound();
                } else {
                    sound = ResourceManager.getInstance().penetrationSounds.get(5).getSound();
                }
                break;
            case WOOD:
                if (Math.random() < 0.5f) {
                    sound = ResourceManager.getInstance().penetrationSounds.get(11).getSound();
                } else {
                    sound = ResourceManager.getInstance().penetrationSounds.get(12).getSound();
                }
                break;
            case ROCK:
                if (Math.random() < 0.5f) {
                    sound = ResourceManager.getInstance().penetrationSounds.get(9).getSound();
                } else {
                    sound = ResourceManager.getInstance().penetrationSounds.get(10).getSound();
                }
                break;
            case GLASS:
                sound = ResourceManager.getInstance().penetrationSounds.get(8).getSound();
                break;
            default:
                break;
        }

        if (sound != null) {
            ResourceManager.getInstance().tryPlaySound(sound,calculateVolumeRatio(collisionImpulse),5,x,y);
        }
    }

    public static float calculateVolumeRatio(float impulse) {
        // Calculate the volume ratio using a logarithmic scale
        float volumeRatio = (float) (1 - Math.exp(-impulse / 1000f));

        // Ensure the volume ratio is within the valid range [0, 1]
        if (volumeRatio < 0) {
            volumeRatio = 0;
        } else if (volumeRatio > 1) {
            volumeRatio = 1;
        }

        return volumeRatio;
    }

    public void onStep() {
        for(TimedCommand timedCommand:timedCommands){
            timedCommand.update();
        }
        for (LiquidParticleWrapper liquidParticleWrapper : this.liquidParticleWrappers) {
            liquidParticleWrapper.update();
        }
        for (PowderParticleWrapper powderParticleWrapper : this.powderParticleWrappers) {
            powderParticleWrapper.update();
        }
        for (ExplosiveParticleWrapper explosiveParticleWrapper : this.explosivesParticleWrappers) {
            explosiveParticleWrapper.update();
        }
        for (FrostParticleWrapper frostParticleWrapper : this.frostParticleWrappers) {
            frostParticleWrapper.update();
        }
        for (Explosion explosion : this.explosions) {
            explosion.update();
        }

        this.timedCommands.removeIf(TimedCommand::isTimedOut);
        this.explosions.removeIf(e->!e.isAlive());
        this.frostParticleWrappers.removeIf(e->!e.isAlive());
        this.powderParticleWrappers.removeIf(e->!e.isAlive());


        computeConduction();
        computeConvection();
        computeFrosting();
        computeStaining();
    }
    public void removeFlame(Fire fireParticleWrapper) {
        flames.remove(fireParticleWrapper);
    }

    public void computeSplinters(List<LayerBlock> splinterBlocks, GameEntity parentEntity) {
        Body body = parentEntity.getBody();
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float rot = body.getAngle();
        ArrayList<GameEntity> splinters = GameEntityFactory.getInstance().createSplinterEntities(x, y, rot, BlockUtils.getDivisionGroups(splinterBlocks), body.getLinearVelocity(), body.getAngularVelocity(), parentEntity.getName(), parentEntity);
        HashSet<Pair<GameEntity>> setOfPairs = new HashSet<>();
        splinters.forEach(splinter -> {
            for (Pair<GameEntity> pair : this.contactListener.getNonCollidingEntities()) {
                if (pair.first == parentEntity) {
                    setOfPairs.add(new Pair<>(splinter, pair.second));
                } else if (pair.second == parentEntity) {
                    setOfPairs.add(new Pair<>(splinter, pair.first));
                }
            }

            for (LayerBlock layerBlock : splinter.getBlocks()) {
                Iterator<? extends AssociatedBlock<?, ?>> iterator = layerBlock.getAssociatedBlocks().iterator();
                while (iterator.hasNext()) {
                    AssociatedBlock<?, ?> associatedBlock = iterator.next();
                    if (associatedBlock instanceof JointBlock) {
                        JointBlock jointBlock = (JointBlock) associatedBlock;
                        if (jointBlock.isNotAborted()) {
                            if (jointBlock.getJointType() == JointDef.JointType.MouseJoint) {
                                Vector2 anchor = jointBlock.getVertices().get(0);
                                MouseJointDef jointDef = (MouseJointDef) jointBlock.getProperties().getJointDef();
                                Vector2 target = parentEntity.getBody().getWorldPoint(anchor.cpy().mul(1/32f)).cpy();
                                jointDef.target.set(target);
                                recreateJoint(jointBlock, splinter);
                                splinter.setDragged(parentEntity.isDragged());
                            } else {
                                recreateJoint(jointBlock, splinter);
                            }
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
            parentEntity.getParentGroup().addGameEntity(splinter);
            if (parentEntity.getType() == SpecialEntityType.Head) {
                if (GeometryUtils.isPointInPolygon(0, 9, splinter.getBlocks().get(0).getVertices())) {
                    splinter.setType(SpecialEntityType.Head);
                    ((RagDoll) parentEntity.getParentGroup()).setHead(splinter);
                }
            } else {
                splinter.setType(parentEntity.getType());
            }
            if (splinter.getArea() < PhysicsConstants.MINIMUM_STABLE_SPLINTER_AREA) {
                this.scheduleGameEntityToDestroy(splinter, (int) (splinter.getArea()));
            }
            splinter.setZIndex(parentEntity.getZIndex());
        });
        this.contactListener.getNonCollidingEntities().addAll(setOfPairs);
    }

    public void recreateJoint(JointBlock jointBlock, GameEntity splinter) {
        if (jointBlock.getBrother() == null) {
            jointBlock.setAborted(true);
            return;
        }
        GameEntity intact = jointBlock.getBrother().getEntity();
        jointBlock.setEntity(splinter);
        Invoker.addJointCreationCommand(jointBlock.prepareJointDef(), jointBlock.getJointType() == JointDef.JointType.MouseJoint ? splinter.getParentGroup() : intact.getParentGroup(), jointBlock.getPosition() == JointBlock.Position.A ? splinter : intact, jointBlock.getPosition() == JointBlock.Position.B ? splinter : intact, jointBlock);
    }

    public Vector2 detectFirstIntersectionPointWithLayerBlock(Vector2 first, Vector2 second, LayerBlock layerBlock) {
        this.blockIntersectionCallback.reset(layerBlock);
        physicsWorld.rayCast(blockIntersectionCallback, first, second);
        return blockIntersectionCallback.getIntersectionPoint();
    }

    public Vector2 detectFirstIntersectionPointWithExceptions(Vector2 first, Vector2 second, List<GameEntity> excepted) {
        simpleDetectionRayCastCallback.reset();
        simpleDetectionRayCastCallback.resetExcepted();
        excepted.forEach(simpleDetectionRayCastCallback::addExcepted);
        physicsWorld.rayCast(simpleDetectionRayCastCallback, first, second);
        return simpleDetectionRayCastCallback.getIntersectionPoint();
    }

    public ImpactData detectFirstIntersectionDataInner(Vector2 first, Vector2 second, GameEntity restricted) {
        fluxInnerRayCastCallback.reset();
        fluxInnerRayCastCallback.setRestricted(restricted);
        physicsWorld.rayCast(fluxInnerRayCastCallback, first, second);
        return fluxInnerRayCastCallback.getImpactData();
    }

    public ImpactData detectFirstIntersectionData(Vector2 first, Vector2 second, GameEntity excepted) {
        fluxRayCastCallback.reset();
        fluxRayCastCallback.setExcepted(excepted);
        physicsWorld.rayCast(fluxRayCastCallback, first, second);
        return fluxRayCastCallback.getImpactData();
    }

    public void addFlame(Fire fireParticleWrapper) {
        flames.add(fireParticleWrapper);
    }

    public void createJuiceSource(GameEntity parentEntity, LayerBlock layerBlock, final FreshCut freshCut) {
        if (layerBlock.getLiquidQuantity() <= 0) {
            return;
        }
        int lowerRate = (int) (PhysicsConstants.BLEEDING_CONSTANT * layerBlock.getProperties().getJuicinessLowerPressure() * freshCut.getLength());
        int higherRate = (int) (PhysicsConstants.BLEEDING_CONSTANT * layerBlock.getProperties().getJuicinessUpperPressure() * freshCut.getLength());
        if (lowerRate == 0 || higherRate == 0) {
            return;
        }
        LiquidParticleWrapper particleWrapper = scene.getWorldFacade().createLiquidParticleWrapper(parentEntity, freshCut, layerBlock.getProperties().getJuiceColor(), layerBlock.getProperties().getFlammability(), lowerRate, higherRate);
        SpawnAction spawnAction = (Particle<UncoloredSprite> p) -> {
            freshCut.decrementLimit();
            layerBlock.setLiquidQuantity(layerBlock.getLiquidQuantity()-1);
            if (freshCut.getLimit() <= 0||layerBlock.getLiquidQuantity()<=0) {
                particleWrapper.detach();
            }
        };
        particleWrapper.setFreshCut(freshCut);
        particleWrapper.getParticleSystem().setSpawnAction(spawnAction);
    }

    private LiquidParticleWrapper liquidParticleWrapperFromFreshCut(GameEntity parentEntity, FreshCut freshCut, Color color, float flammability, final int lowerRate, final int higherRate) {
        if (freshCut instanceof SegmentFreshCut) {
            SegmentFreshCut sfc = (SegmentFreshCut) freshCut;
            return new SegmentLiquidParticleWrapper(parentEntity, new float[]{sfc.first.x, sfc.first.y, sfc.second.x, sfc.second.y}, sfc.getSplashVelocity(), color, flammability, lowerRate, higherRate);

        } else {
            PointsFreshCut pfc = (PointsFreshCut) freshCut;
            float[] data = Utils.mapPointsToArray(pfc.getPoints().stream().map(CutPoint::getPoint).collect(Collectors.toList()));
            float[] weights = Utils.mapWeightsToArray(pfc.getPoints().stream().map(CutPoint::getWeight).collect(Collectors.toList()));
            return new ClusterLiquidParticleWrapper(parentEntity, color, flammability, data, weights, pfc.getSplashVelocity(), lowerRate, higherRate);
        }
    }

    public FrostParticleWrapper frostParticleWrapper(float touchX, float touchY,Vector2 begin, Vector2 end, final int lowerRate, final int higherRate) {
        FrostParticleWrapper frostParticleWrapper = new FrostParticleWrapper(new float[]{begin.x, begin.y, end.x, end.y}, frostColor, 0, lowerRate, higherRate);
        frostParticleWrappers.add(frostParticleWrapper);
        frostParticleWrapper.update(touchX,touchY);
        frostParticleWrapper.attachTo(scene);
        this.scene.sortChildren();
        return frostParticleWrapper;
    }

    public SegmentExplosiveParticleWrapper createFireSource(GameEntity entity, Vector2 v1, Vector2 v2, float velocityMeter, float fireRatio, float smokeRatio, float sparkRatio, float intensity, float heatRatio, float inFireSize, float finFireSize) {
        Vector2 dir = v1.cpy().sub(v2).nor();
        Vector2 normal = new Vector2(-dir.y, dir.x);
        SegmentExplosiveParticleWrapper explosiveParticleWrapper = new SegmentExplosiveParticleWrapper(entity, new float[]{v1.x, v1.y, v2.x, v2.y}, normal.cpy().mul(32f * velocityMeter), fireRatio, smokeRatio, sparkRatio, intensity, heatRatio, inFireSize, finFireSize);

        if (explosiveParticleWrapper.getFireParticleSystem() != null) {
            scene.attachChild(explosiveParticleWrapper.getFireParticleSystem());
            explosiveParticleWrapper.getFireParticleSystem().setZIndex(5);
            this.addFlame(explosiveParticleWrapper);
        }
        if (explosiveParticleWrapper.getSmokeParticleSystem() != null) {
            scene.attachChild(explosiveParticleWrapper.getSmokeParticleSystem());
            explosiveParticleWrapper.getSmokeParticleSystem().setZIndex(5);
        }
        if (explosiveParticleWrapper.getSparkParticleSystem() != null) {
            scene.attachChild(explosiveParticleWrapper.getSparkParticleSystem());
            explosiveParticleWrapper.getSparkParticleSystem().setZIndex(5);
        }
        this.explosivesParticleWrappers.add(explosiveParticleWrapper);
        scene.sortChildren();
        return explosiveParticleWrapper;
    }

    public DataExplosiveParticleWrapper createPointFireSource(GameEntity parent, float[] data, float velocity, float fireRatio, float smokeRatio, float sparkRatio, float particles, float temperature, float inFireSize, float finFireSize, boolean trackFireParticles) {

        DataExplosiveParticleWrapper explosiveParticleWrapper = new DataExplosiveParticleWrapper(parent, data, velocity, fireRatio, smokeRatio, sparkRatio, particles, temperature, inFireSize, finFireSize);

        explosiveParticleWrapper.attachTo(scene);
        if(trackFireParticles&&explosiveParticleWrapper.getFireParticleSystem()!=null){
            this.addFlame(explosiveParticleWrapper);
        }
        this.explosivesParticleWrappers.add(explosiveParticleWrapper);
        scene.sortChildren();
        return explosiveParticleWrapper;
    }

    public LiquidParticleWrapper createLiquidParticleWrapper(GameEntity parentEntity, final FreshCut freshCut, Color color, float flammability, int lowerRate, int higherRate) {
        LiquidParticleWrapper liquidSource = liquidParticleWrapperFromFreshCut(parentEntity, freshCut, color, flammability, lowerRate, higherRate);
        liquidParticleWrappers.add(liquidSource);
        liquidSource.attachTo(scene);
        scene.sortChildren();
        return liquidSource;
    }

    public HashSet<GameEntity> findEntitiesInZone(float rx, float ry, float halfWidth, float halfHeight) {
        queryCallBack.reset();
        physicsWorld.QueryAABB(queryCallBack, rx - halfWidth, ry - halfHeight, rx + halfWidth, ry + halfHeight);
        return queryCallBack.getEntities();
    }

    public boolean checkEmpty(float lx, float ux, float ly, float uy) {
        checkEmptyCallback = new CheckEmptyCallback();
        checkEmptyCallback.reset(new float[]{lx*32f,ux*32f,ly*32f,uy*32f});
        physicsWorld.QueryAABB(checkEmptyCallback, lx, ly, ux, uy);
        return checkEmptyCallback.isEmpty();
    }

    public Contact getConcernedContact(Touch touch) {
        for (Contact contact : physicsWorld.getContactList()) {
            if (!contact.isTouching()) {
                continue;
            }
            LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
            LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();
            if (touch.isEquivalent(block1, block2)) {
                return contact;
            }
        }
        return null;
    }

    public void  createExplosion(GameEntity source, float x, float y, float fireRatio, float smokeRatio, float sparkRatio, float particles, float force, float heat, float speedRatio, float inFirePartSize, float finFirePartSize) {
        Explosion explosion = new Explosion(scene, source, new Vector2(x, y), particles, force, speedRatio, heat, fireRatio, smokeRatio, sparkRatio, inFirePartSize, finFirePartSize);
        explosions.add(explosion);

        Sound sound = ResourceManager.getInstance().getProjectileSound("explosion1").getSound();
        Vector2 p = new Vector2(x,y);
        if(source!=null&&source.getBody()!=null){
          p = source.getBody().getWorldPoint(p).cpy();
        }
        ResourceManager.getInstance().tryPlaySound(sound, 1f,4,p.x*32f,p.y*32f);
        ResourceManager.getInstance().vibrate(100);
    }

    private void computeStaining() {
        HashSet<GameEntity> affectedEntities = new HashSet<>();
        for (LiquidParticleWrapper liquidSource : liquidParticleWrappers) {
            if (liquidSource.getParticleSystem().hasParent()) {
                liquidSource.getParticleSystem().getParticles();
                for (Particle<?> p : liquidSource.getParticleSystem().getParticles()) {
                    if (p != null && !p.isExpired() && Math.random() < PhysicsConstants.STAINING_PROBABILITY) {
                        Vector2 position = new Vector2(p.getEntity().getX(), p.getEntity().getY());
                        float halfWidth = p.getEntity().getWidth() / 32f / 2f;
                        float halfHeight = p.getEntity().getHeight() / 32f / 2f;
                        float rx = position.x / 32f;
                        float ry = position.y / 32f;
                        HashSet<GameEntity> entities = findEntitiesInZone(rx, ry, halfWidth, halfHeight);
                        Vector2 rPosition = new Vector2(rx, ry);
                        boolean onBoundaries = false;

                        for (GameEntity entity : entities) {
                            if (entity.getBody() != null) {
                                for (LayerBlock block : entity.getBlocks()) {
                                    if (block.testPoint(entity.getBody(), rx, ry)) {
                                        onBoundaries = true;
                                        break;
                                    }
                                }
                            }
                        }

                        boolean dead = false;
                        if (onBoundaries) {
                            for (GameEntity entity : entities) {
                                if (entity.getBody() != null) {
                                    boolean affected = false;
                                    Body body = entity.getBody();
                                    Vector2 localPoint = obtain(body.getLocalPoint(rPosition)).mul(32);
                                    List<LayerBlock> blocks = entity.getBlocks();
                                    for (int i = blocks.size() - 1; i >= 0; i--) {
                                        boolean applied = applyLiquidStain(entity, localPoint.x, localPoint.y, blocks.get(i), liquidSource.getColor(), liquidSource.getFlammability(), random.nextInt(14), false);
                                        if (applied) {
                                            affected = true;
                                            break;
                                        }
                                    }
                                    recycle(localPoint);
                                    if (affected) {
                                        dead = true;
                                        affectedEntities.add(entity);
                                    }
                                }
                            }
                        }

                        if (dead) {
                            p.setExpired(true);
                        }
                    }
                }
            }
        }
        for (GameEntity entity : affectedEntities) {
            entity.redrawStains();
        }

    }


    private void computeConduction() {
        for (GameGroup gameGroup : scene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (!gameEntity.getName().equals("Ground")) {
                    for (LayerBlock block : gameEntity.getBlocks()) {
                        if (block.getBlockGrid() == null) {
                            continue;
                        }
                        for (CoatingBlock coatingBlock : block.getBlockGrid().getCoatingBlocks()) {
                            computeInternalConduction(coatingBlock);
                        }
                    }
                }
            }
    }
    private void computeConvection() {
        // convection
        List<Fire> flamesCopy = new ArrayList<>(flames);
        Collections.shuffle(flamesCopy);
        for (Fire fire : flamesCopy) {
            for (Particle<UncoloredSprite> p : fire.getFireParticleSystem().getParticles()) {
                if (p == null || p.isExpired()) {
                    continue;
                }

                Entity spark = p.getEntity();

                float x = spark.getX();
                float y = spark.getY();
                float w = spark.getWidth() * spark.getScaleX();
                float h = spark.getHeight() * spark.getScaleY();
                Vector2 worldPosition = new Vector2(x / 32f, y / 32f);
                Vector2 lower = new Vector2(x - w / 2f, y - h / 2f).mul(1 / 32f);
                Vector2 upper = new Vector2(x + w / 2f, y + h / 2f).mul(1 / 32f);
                blockQueryCallBack.reset();
                physicsWorld.QueryAABB(blockQueryCallBack, lower.x, lower.y, upper.x, upper.y);
                List<LayerBlock> list = blockQueryCallBack.getBlocks();
                List<Body> bodyList = blockQueryCallBack.getBodies();
                if (list.size() == 0) {
                    continue;
                }

                for (LayerBlock block : list) {

                    Body body = bodyList.get(list.indexOf(block));

                    if (body == null) {
                        continue;
                    }
                    Vector2 localPosition = obtain(body.getLocalPoint(worldPosition)).mul(32);
                    CoatingBlock nearestCoatingBlock = block.getBlockGrid().getNearestCoatingBlockSimple(localPosition);
                    float dst = nearestCoatingBlock!=null?localPosition.dst(nearestCoatingBlock.getPosition()):0;
                    recycle(localPosition);
                    if (nearestCoatingBlock != null&& dst<32f) {
                        double sparkTemperature = fire.getParticleTemperature(p);
                        nearestCoatingBlock.onSpark(sparkTemperature);
                        PhysicsUtils.transferHeatByConvection(nearestCoatingBlock.getProperties().getHeatResistance(), sparkTemperature, nearestCoatingBlock);
                    }
                }
            }
        }
        for (GameGroup gameGroup : scene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (gameEntity.getName().equals("Ground")) {
                    continue;
                }
                for (LayerBlock block : gameEntity.getBlocks()) {
                    if (block.getBlockGrid() == null) {
                        continue;
                    }
                    for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                        PhysicsUtils.transferHeatByConvection(0.1f, PhysicsConstants.ambient_temperature, grain);
                    }
                }
            }
    }

    private void computeFrosting() {
        for(FrostParticleWrapper frostParticleWrapper:this.frostParticleWrappers) {
            if (frostParticleWrapper == null || !frostParticleWrapper.isAlive()) {
                return;
            }
            for (Particle<UncoloredSprite> p : frostParticleWrapper.getParticleSystem().getParticles()) {
                if (p == null || p.isExpired()) {
                    continue;
                }

                Entity frostParticle = p.getEntity();

                float x = frostParticle.getX();
                float y = frostParticle.getY();
                float w = frostParticle.getWidth() * frostParticle.getScaleX();
                float h = frostParticle.getHeight() * frostParticle.getScaleY();
                Vector2 worldPosition = new Vector2(x / 32f, y / 32f);
                Vector2 lower = new Vector2(x - w / 2f, y - h / 2f).mul(1 / 32f);
                Vector2 upper = new Vector2(x + w / 2f, y + h / 2f).mul(1 / 32f);
                blockQueryCallBack.reset();
                physicsWorld.QueryAABB(blockQueryCallBack, lower.x, lower.y, upper.x, upper.y);
                List<LayerBlock> list = blockQueryCallBack.getBlocks();
                List<Body> bodyList = blockQueryCallBack.getBodies();
                if (list.size() == 0) {
                    continue;
                }

                for (LayerBlock block : list) {

                    Body body = bodyList.get(list.indexOf(block));

                    if (body == null) {
                        continue;
                    }
                    Vector2 localPosition = obtain(body.getLocalPoint(worldPosition)).mul(32);
                    CoatingBlock nearestCoatingBlock = block.getBlockGrid().getNearestCoatingBlockSimple(localPosition);
                    recycle(localPosition);

                    if (nearestCoatingBlock != null) {
                        nearestCoatingBlock.onFrost();
                    }
                }
            }
        }
    }

    private void plotTouch() {
        for (Touch touch : touches) {
            Contact contact = getConcernedContact(touch);
            if (contact == null) continue;
            if (contact.getWorldManifold().getNumberOfContactPoints() == 2) {
                Vector2 first = contact.getWorldManifold().getPoints()[0];
                Vector2 second = contact.getWorldManifold().getPoints()[1];
                EditorScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
                EditorScene.plotter.drawPoint(second.cpy().mul(32), Color.RED, 1f, 0);
            } else {
                Vector2 first = contact.getWorldManifold().getPoints()[0];
                EditorScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
            }
        }
    }

    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }

    @Override
    public void processImpactAfterSolve(Contact contact, ContactImpulse impulse) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();

        GameEntity entity1 = (GameEntity) body1.getUserData();
        GameEntity entity2 = (GameEntity) body2.getUserData();
        if (entity1 == null || entity2 == null || !entity1.isAlive() || !entity2.isAlive()) {
            return;
        }

        float[] normalImpulses = impulse.getNormalImpulses();
        float[] tangentImpulses = impulse.getTangentImpulses();
        float impulseValue = (float) Math.sqrt(normalImpulses[0] * normalImpulses[0] + tangentImpulses[0] * tangentImpulses[0]);
        if (Float.isInfinite(impulseValue)) {
            return;
        }
        if (entity1.getParentGroup() == entity2.getParentGroup()) {
            return;
        }
        computeShatterImpact(contact, impulseValue, entity1, entity2);
    }

    @Override
    public void processImpactBeginContact(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        GameEntity entity1 = (GameEntity) body1.getUserData();
        GameEntity entity2 = (GameEntity) body2.getUserData();
        LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
        LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();
        boolean equivalentFound = false;

        for (Touch touch : touches) {
            if (touch.isEquivalent(block1, block2)) {
                equivalentFound = true;
            }
        }
        if (!equivalentFound && contact.getWorldManifold().getNumberOfContactPoints() > 0) {
            Touch newTouch = new Touch(block1, block2, body1, body2, contact.getWorldManifold().getPoints()[0].cpy());
            touches.add(newTouch);
        }

        if (entity1 == null || entity2 == null || !entity1.isAlive() || !entity2.isAlive()) {
            return;
        }

        if (entity1.getParentGroup() == entity2.getParentGroup()) {
            return;
        }
        Vector2 penetrationPoint = contact.getWorldManifold().getPoints()[0];
        if (Float.isInfinite(penetrationPoint.x) || Float.isInfinite(penetrationPoint.y) || Float.isNaN(penetrationPoint.x) || Float.isNaN(penetrationPoint.y)) {
            return;
        }


        boolean computePenetration = entity1.hasActiveUsage(Penetrating.class) != entity2.hasActiveUsage(Penetrating.class);


        boolean penetrationHappened = false;
        if (computePenetration) {
            penetrationHappened = computePenetrationImpact(contact, entity1, entity2, block1, block2);
        }

        if (!penetrationHappened) {
            if (entity1.hasActiveUsage(Stabber.class)) {
                Penetrating usage = entity1.getActiveUsage(Stabber.class);
                usage.onCancel();
            }
            if (entity2.hasActiveUsage(Stabber.class)) {
                Penetrating usage = entity2.getActiveUsage(Stabber.class);
                usage.onCancel();
            }
            if (entity1.hasActiveUsage(Projectile.class)) {
                Penetrating usage = entity1.getActiveUsage(Projectile.class);
                usage.onCancel();
            }
            if (entity2.hasActiveUsage(Projectile.class)) {
                Penetrating usage = entity2.getActiveUsage(Projectile.class);
                usage.onCancel();
            }
        }
    }

    @Override
    public void processImpactEndContact(Contact contact) {
        Iterator<Touch> touchIterator = touches.iterator();
        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        LayerBlock block1 = (LayerBlock) fixture1.getUserData();
        LayerBlock block2 = (LayerBlock) fixture2.getUserData();
        while (touchIterator.hasNext()) {
            Touch touch = touchIterator.next();
            if (touch.isEquivalent(block1, block2)) {
                touchIterator.remove();
            }
        }
    }

    @Override
    public void processImpactBeforeSolve(Contact contact) {
    }

    private float computeCollisionEnergy(Vector2 V1, Vector2 V2, Vector2 normal, float m1, float m2) {
        float v1 = V1.dot(normal);
        float v2 = V2.dot(normal);
        return m1 * v1 * v1 - m2 * v2 * v2;
    }

    private Vector2 detectIntersectionWithEntity(GameEntity entity, Vector2 worldBegin, Vector2 worldEnd) {
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity);
        physicsWorld.rayCast(detectionRayCastCallback, worldBegin, worldEnd);
        return detectionRayCastCallback.getIntersectionPoint();
    }

    private void drawData(TopographyData topographyDataTable, float factor, Vector2 normal, Color color) {
        float[][] data = topographyDataTable.getData();
        for (float[] datum : data) {
            float inf = datum[0];
            float sup = datum[1];
            Vector2 p1 = topographyDataTable.getBase().cpy().add(normal.x * inf * factor, normal.y * inf * factor).mul(32f);
            Vector2 p2 = topographyDataTable.getBase().cpy().add(normal.x * sup * factor, normal.y * sup * factor).mul(32f);

            PlayScene.plotter.drawLine2(p1, p2, color != null ? color : Utils.getRandomColor(), 3);
        }
    }

    private boolean computePenetrationImpact(Contact contact, GameEntity entity1, GameEntity entity2, LayerBlock block1, LayerBlock block2) {

        GameEntity penetrator = entity1.hasActiveUsage(Penetrating.class) ? entity1 : entity2;

        Penetrating penetration = penetrator.hasActiveUsage(Stabber.class) ? penetrator.getUsage(Stabber.class) : penetrator.hasActiveUsage(Projectile.class) ? penetrator.getActiveUsage(Projectile.class) : penetrator.getActiveUsage(Smasher.class);
        GameEntity penetrated = penetrator == entity1 ? entity2 : entity1;
        LayerBlock penetratorBlock = penetrator == entity1 ? block1 : block2;

        Vector2[] points = contact.getWorldManifold().getPoints();
        Vector2 penetrationPoint = points[0];

        Vector2 V1 = penetrator.getBody().getLinearVelocity();
        Vector2 V2 = penetrated.getBody().getLinearVelocity();
        float m1 = penetrator.getBody().getMass();
        float m2 = penetrated.getBody().getMass();
        Vector2 normal = obtain(V1.x, V1.y).nor();
        Vector2 tangent = obtain(-normal.y, normal.x);

        final float collisionEnergy = computeCollisionEnergy(V1, V2, normal, m1, m2);
        if (collisionEnergy < 10 || Float.isNaN(collisionEnergy) || Float.isInfinite(collisionEnergy)) {
            return false;
        }
        Log.d("Penetration", "-----------$ Begin penetration, energy:" +  collisionEnergy + "/ bullet:" + penetrator.getBody().isBullet());

        final float range = 10f;
        final float dL = 0.01f;
        final float dTx = dL * tangent.x;
        final float dTy = dL * tangent.y;
        final float dN = 0.05f;
        float pds0x = penetrationPoint.x + normal.x * range;
        float pds0y = penetrationPoint.y + normal.y * range;
        float pde0x = penetrationPoint.x - normal.x * range;
        float pde0y = penetrationPoint.y - normal.y * range;

        final Vector2 pDS = obtain();
        final Vector2 pDE = obtain();
        final Vector2 p1 = obtain();
        final Vector2 p2 = obtain();

        List<LayerBlock> leadingBlocks = new ArrayList<>();
        List<TopographyData> penetratorData = new ArrayList<>();
        List<TopographyData> environmentData = new ArrayList<>();
        int i = 0;
        int sign = 1;
        while (true) {
            pDS.set(pds0x + i * dTx * sign, pds0y + i * dTy * sign);
            pDE.set(pde0x + i * dTx * sign, pde0y + i * dTy * sign);
            Vector2 interPenetrator = i == 0 ? penetrationPoint.cpy() : detectIntersectionWithEntity(penetrator, pDS, pDE);

            if (interPenetrator != null) {
                p1.set(interPenetrator.x + normal.x * PhysicsConstants.BACKOFF, interPenetrator.y + normal.y * PhysicsConstants.BACKOFF);
                p2.set(interPenetrator.x - normal.x * range, interPenetrator.y - normal.y * range);
                TopographyData penTopographyData = findIntervalsExclusiveToOneEntity(penetrator, interPenetrator, normal, p1, p2);
                penTopographyData.setCenter(i == 0);
                if (sign == 1) {
                    penetratorData.add(penTopographyData);
                    if (i == 0) {
                        leadingBlocks.add(penetratorBlock);
                    } else {
                        leadingBlocks.add(detectionRayCastCallback.getLayerBlock());
                    }
                } else {
                    penetratorData.add(0, penTopographyData);
                    leadingBlocks.add(0, detectionRayCastCallback.getLayerBlock());
                }
                p1.set(interPenetrator.x - normal.x * PhysicsConstants.BACKOFF, interPenetrator.y - normal.y * PhysicsConstants.BACKOFF);
                p2.set(interPenetrator.x + normal.x * range, interPenetrator.y + normal.y * range);
                TopographyData envTopographyData = findIntervalsExceptingOneEntity(penetrator, interPenetrator, normal, p1, p2);
                if (sign == 1) {
                    environmentData.add(envTopographyData);
                } else {
                    environmentData.add(0, envTopographyData);
                }
                envTopographyData.setCenter(i == 0);
                i++;
            } else {
                if (sign == 1) {
                    sign = -1;
                    i = 1;
                } else {
                    break;
                }
            }
        }

        float correctionValue = -correctEnvironmentData(environmentData);
        penetrationPoint.sub(normal.x * correctionValue, normal.y * correctionValue);

        boolean debug = false;
        if (debug) {
            PlayScene.plotter.detachChildren();
            drawPenetrationData(penetrationPoint.cpy(), normal.cpy(), penetratorData, environmentData);
            if (contact.getWorldManifold().getNumberOfContactPoints() == 2) {
                PlayScene.plotter.drawPoint(contact.getWorldManifold().getPoints()[1].cpy().mul(32f), Color.GREEN, 4f);
            }
            // GameScene.pause = false;
        }
        if (environmentData.isEmpty()) {
            Log.d("Issue","Empty Environment Data");
            return false;
        }

        int step = 0;
        float penetrationEnergy;
        float advance;
        while (true) {
            advance = (step + 1) * dN;
            penetrationEnergy = 0;
            boolean depleted = false;
            for (int index = 0; index < environmentData.size(); index++) {
                if (environmentData.get(index) != null && penetratorData.get(index) != null) {
                    LayerProperties properties = leadingBlocks.get(index).getProperties();
                    float sharpness = properties.getSharpness();
                    float hardness = properties.getHardness();
                    Float dEnergy = environmentData.get(index).getImpulseForAdvance(advance, dN, sharpness, hardness);
                    if (dEnergy == null) {
                        depleted = true;
                        penetrationEnergy = collisionEnergy;
                        break;
                    } else {
                        penetrationEnergy += dEnergy;
                    }
                }
            }


            if (step > 100) {
                break;
            }

            if (depleted || collisionEnergy - penetrationEnergy < 0) {
                float actualAdvance = step * dN;
                penetration.onImpulseConsumed(this, contact, penetrationPoint.cpy(), normal, actualAdvance, penetrator, penetrated, environmentData, penetratorData, (float) Math.sqrt(collisionEnergy), penetratorBlock);
                Log.d("Penetration", "-----------$ On energy consumed, penetrationEnergy:" + penetrationEnergy + " advance:" + actualAdvance);
                return true;
            }
            step++;
        }
        Log.d("Penetration", "-----------$ On free, advance:" + advance+" : "+collisionEnergy);
        penetration.onFree(this, contact, penetrationPoint.cpy(), normal, advance, penetrator, penetrated, environmentData, penetratorData, (float) Math.sqrt(collisionEnergy), penetratorBlock);
        return true;
    }

    private void drawPenetrationData(Vector2 point, Vector2 normal, List<TopographyData> penetratorData, List<TopographyData> environmentData) {
        for (int j = 0; j < environmentData.size(); j++) {
            float ratio = j / (float) environmentData.size();
            Color c = new Color(ratio, 1f - ratio, 0f);
            TopographyData topographyData = environmentData.get(j);
            drawData(topographyData, 1, normal, c);
        }

        for (int j = 0; j < penetratorData.size(); j++) {
            float ratio = j / (float) penetratorData.size();
            Color c = new Color(0f, 1f - ratio, ratio);
            TopographyData topographyData = penetratorData.get(j);
            drawData(topographyData, 1, normal, c);
        }
        PlayScene.plotter.drawPoint(point.cpy().mul(32f), Color.CYAN, 4f);
        PlayScene.plotter.drawVector(point.cpy().mul(32f), normal.cpy().mul(32f), Color.CYAN);
    }

    private float correctEnvironmentData(List<TopographyData> environmentData) {
        float envError = Float.MAX_VALUE;
        for (TopographyData topographyData : environmentData) {
            float min = topographyData.getMin();
            if (min < envError) {
                envError = min;
            }
        }
        for (TopographyData data : environmentData) {
            for (int k = 0; k < data.getLength(); k++) {
                data.getData()[k][0] -= envError;
                data.getData()[k][1] -= envError;
            }
        }
        return envError;
    }

    public void freeze(GameEntity penetrator) {
        penetrator.getBody().setAngularVelocity(0);
        penetrator.getBody().setLinearVelocity(0, 0);
    }

    public void mergeEntities(GameEntity receiver, GameEntity traveler, Vector2 advance, Vector2 impactWorldPoint) {

        Vector2 localA = receiver.getBody().getLocalPoint(impactWorldPoint).cpy();
        Vector2 localB = traveler.getBody().getLocalPoint(impactWorldPoint.cpy().add(advance)).cpy();
        for (GameEntity gameEntity : receiver.getParentGroup().getGameEntities()) {
            this.addNonCollidingPair(gameEntity, traveler);
        }
        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = receiver.getBody();
        jointDef.bodyB = traveler.getBody();
        jointDef.localAnchorA.set(localA.cpy());
        jointDef.localAnchorB.set(localB.cpy());
        jointDef.collideConnected = false;
        jointDef.referenceAngle = -receiver.getBody().getAngle() + traveler.getBody().getAngle();
        addJointToCreate(jointDef, receiver, traveler, -2);
        traveler.setZIndex(receiver.getZIndex() - 1);
        scene.sortChildren();
    }

    public void glueEntities(GameEntity entity1, GameEntity entity2, Vector2 point) {
        Vector2 localA = entity1.getBody().getLocalPoint(point).cpy();
        Vector2 localB = entity2.getBody().getLocalPoint(point).cpy();

        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();
        jointDef.localAnchorA.set(localA.cpy());
        jointDef.localAnchorB.set(localB.cpy());
        jointDef.collideConnected = false;
        jointDef.referenceAngle = -entity1.getBody().getAngle() + entity2.getBody().getAngle();
        addJointToCreate(jointDef, entity1, entity2, -777777);
    }

    private void computeShatterImpact(Contact contact, float impulse, GameEntity entity1, GameEntity entity2) {


        final int numberOfContactPoints = contact.getWorldManifold().getNumberOfContactPoints();

        LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
        LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();

        Vector2[] points = contact.getWorldManifold().getPoints();
        Vector2 point;
        if (numberOfContactPoints == 1){
            point = points[0];
        }
        else {
            point = points[0].add(points[1]).mul(0.5f);
        }
        if (Float.isInfinite(point.x) || Float.isInfinite(point.y) || Float.isNaN(point.x) || Float.isNaN(point.y)) {
            return;
        }

        applyOnePointImpactToEntity(block1, impulse / 2, entity1,  obtain(point));
        applyOnePointImpactToEntity(block2, impulse / 2, entity2,  obtain(point));
    }

    private void linearTopographicScan(Vector2 begin, Vector2 end) {
        rayCastCallback.setDirection(true);
        physicsWorld.rayCast(rayCastCallback, begin, end);
        rayCastCallback.setDirection(false);
        physicsWorld.rayCast(rayCastCallback, end, begin);
    }

    private TopographyData findIntervalsExclusiveToOneEntity(GameEntity exclusive, Vector2 origin, Vector2 direction, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExclusive(exclusive);
        linearTopographicScan(begin, end);
        return computeIntervals(origin, direction, begin, end);
    }

    private TopographyData findIntervalsExceptingOneEntity(GameEntity excepted, Vector2 center, Vector2 direction, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExcepted(excepted);
        linearTopographicScan(begin, end);
        return computeIntervals(center, direction, begin, end);
    }

    private TopographyData computeIntervals(final Vector2 origin, Vector2 base, Vector2 begin, Vector2 end) {
        ArrayList<ArrayList<Flag>> flags = rayCastCallback.getFlags();
        ArrayList<LayerBlock> blocks = rayCastCallback.getCoveredBlocks();
        ArrayList<GameEntity> entities = rayCastCallback.getCoveredEntities();
        TopographyData topographyData = new TopographyData(blocks.size(), origin);
        for (ArrayList<Flag> list : flags) {
            Collections.sort(list);
            int i = flags.indexOf(list);
            LayerBlock layerBlock = blocks.get(i);
            GameEntity entity = entities.get(i);
            Body body = entity.getBody();

            boolean firstInside = layerBlock.testPoint(body, begin.x, begin.y);
            boolean secondInside = layerBlock.testPoint(body, end.x, end.y);

            if (firstInside && secondInside) {
                // inner cut
                float projection1 = GeometryUtils.projection(begin, origin, base);
                float projection2 = GeometryUtils.projection(end, origin, base);
                topographyData.add(projection1, projection2, entity, layerBlock);

            } else if (firstInside) {
                // half cut
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(begin, origin, base);
                float projection2 = GeometryUtils.projection(last.getPoint(), origin, base);
                topographyData.add(projection1, projection2, entity, layerBlock);
            } else if (secondInside) {
                // half cut
                Flag first = list.get(0);
                float projection1 = GeometryUtils.projection(first.getPoint(), origin, base);
                float projection2 = GeometryUtils.projection(end, origin, base);
                topographyData.add(projection1, projection2, entity, layerBlock);

            } else {
                // full cut
                Flag first = list.get(0);
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(first.getPoint(), origin, base);
                float projection2 = GeometryUtils.projection(last.getPoint(), origin, base);
                topographyData.add(projection1, projection2, entity, layerBlock);
            }
        }
        return topographyData;
    }

    public void destroyGameEntity(GameEntity entity, boolean finalDestruction) {
        if (entity == null || !entity.isAlive() || entity.getBody() == null) {
            return;
        }
        Invoker.addBodyDestructionCommand(entity,finalDestruction);
    }

    public void performScanFlux(Vector2 sourceWorldPoint, GameEntity gameEntity, FluxInterface fluxInterface, final int precision, boolean draw) {
        // find lower and higher angles
        float minAngle = Float.MAX_VALUE;
        float maxAngle = -Float.MAX_VALUE;
        Vector2 low = null;
        Vector2 high = null;
        LayerBlock lowBlock = null;
        LayerBlock highBlock = null;
        Vector2 joker = new Vector2();
        for (LayerBlock block : gameEntity.getBlocks()) {
            for (Vector2 v : block.getVertices()) {
                joker.set(v.x / 32f, v.y / 32f);
                Vector2 worldPoint = obtain(gameEntity.getBody().getWorldPoint(joker));
                float angle = (float) (MathUtils.radiansToDegrees * Math.atan2(worldPoint.x - sourceWorldPoint.x, worldPoint.y - sourceWorldPoint.y));
                if (angle < minAngle) {
                    minAngle = angle;
                    low = worldPoint;
                    lowBlock = block;
                }
                if (angle > maxAngle) {
                    maxAngle = angle;
                    high = worldPoint;
                    highBlock = block;
                }
            }
        }
        if (low != null && high != null) {
            float angleDifference = maxAngle - minAngle;
            float dAngle = angleDifference / precision;
            Vector2 lowDirection = obtain(high.x - sourceWorldPoint.x, high.y - sourceWorldPoint.y).nor();
            if (fluxInterface != null) {
                fluxInterface.computeFluxEffect(lowBlock, gameEntity, lowDirection, sourceWorldPoint, low, dAngle);
            }
            recycle(lowDirection);
            for (int i = 1; i < precision; i++) {
                Vector2 direction = obtain(low.x - sourceWorldPoint.x, low.y - sourceWorldPoint.y).nor();
                float angle = i * dAngle;
                GeometryUtils.rotateVectorDeg(direction, -angle);
                Vector2 detectionEnd = obtain(sourceWorldPoint.x + direction.x * 200, sourceWorldPoint.y + direction.y * 200);
                Vector2 target = detectIntersectionWithEntity(gameEntity, sourceWorldPoint, detectionEnd);
                recycle(detectionEnd);
                if (target != null && fluxInterface != null) {
                    fluxInterface.computeFluxEffect(detectionRayCastCallback.getLayerBlock(), gameEntity, direction, sourceWorldPoint, target, dAngle);
                    recycle(target);
                }
                recycle(direction);
                if (draw && target != null) {
                    EditorScene.plotter.drawPoint(target.cpy().mul(32f), Color.RED, 1, 1);
                }
            }
            Vector2 highDirection = obtain(high.x - sourceWorldPoint.x, high.y - sourceWorldPoint.y).nor();
            if (fluxInterface != null) {
                fluxInterface.computeFluxEffect(highBlock, gameEntity, highDirection, sourceWorldPoint, high, dAngle);
            }
            recycle(highDirection);
            if (draw) {
                EditorScene.plotter.drawLine2(sourceWorldPoint.cpy().mul(32f), low.mul(32f), Color.BLUE, 1);
                EditorScene.plotter.drawLine2(sourceWorldPoint.cpy().mul(32f), high.mul(32f), Color.RED, 1);
            }
            recycle(high);
            recycle(low);
        }
    }

    public void performFlux(Vector2 sourceWorldPoint, ImpactInterface impactInterface, GameEntity source) {
        Vector2 v = new Vector2(1, 0);
        List<ImpactData> list = new ArrayList<>();
        float phi = (float) (Math.random() * 360);
        for (int i = 0; i < PhysicsConstants.FLUX_PRECISION; i++) {
            v.set(1, 0);
            GeometryUtils.rotateVectorDeg(v, i * 360 / (float) PhysicsConstants.FLUX_PRECISION + phi);
            Vector2 end = obtain(sourceWorldPoint.x + v.x * 100, sourceWorldPoint.y + v.y * 100);
            ImpactData dataOuter = this.detectFirstIntersectionData(sourceWorldPoint, end, source);
            if (dataOuter != null) {
                list.add(dataOuter);
            }
            if (source != null) {
                ImpactData dataInner = this.detectFirstIntersectionDataInner(end, sourceWorldPoint, source);
                if (dataInner != null) {
                    dataInner.setInner(true);
                    list.add(dataInner);
                }
            }
        }
        Map<GameEntity, List<ImpactData>> res = list.stream().collect(Collectors.groupingBy(ImpactData::getGameEntity));
        if (impactInterface != null) {
            res.forEach(impactInterface::processImpacts);
        }
        list.forEach(ImpactDataPool::recycle);
    }

    public void createInnerCut(SegmentFreshCut innerCut, GameEntity gameEntity, LayerBlock block) {
        float x1 = innerCut.first.x;
        float y1 = innerCut.first.y;
        float x2 = innerCut.second.x;
        float y2 = innerCut.second.y;
        Line line = new Line(x1, y1, x2, y2, 2, ResourceManager.getInstance().vbom);
        line.setColor(block.getProperties().isJuicy() ? block.getProperties().getJuiceColor() : Color.BLACK);
        gameEntity.getMesh().attachChild(line);
        processPenetrationSound(block.getMaterialAcousticType(), innerCut.getLength() * 10,x1,y1);
        if (block.getProperties().isJuicy()) {
            scene.getWorldFacade().createJuiceSource(gameEntity, block, innerCut);
        }
    }

    public void performScreenCut(Vector2 worldPoint1, Vector2 worldPoint2) {
        this.performScreenCut(worldPoint1, worldPoint2, null, Float.MAX_VALUE);
    }

    public void performScreenCut(Vector2 worldPoint1, Vector2 worldPoint2, GameEntity excepted, float hardness) {
        Vector2 u = worldPoint2.cpy().sub(worldPoint1).nor();
        final float L = 10;
        Vector2 L1 = worldPoint1.cpy().sub(u.x * L, u.y * L);
        Vector2 L2 = worldPoint2.cpy().add(u.x * L, u.y * L);
        final float f1 = L / (2 * L + worldPoint1.dst(worldPoint2));
        final float f2 = 1 - f1;
        cutRayCastCallback.setExcepted(excepted);
        cutRayCastCallback.reset();
        cutRayCastCallback.setDirection(true);
        getPhysicsWorld().rayCast(cutRayCastCallback, L1, L2);
        cutRayCastCallback.setDirection(false);
        getPhysicsWorld().rayCast(cutRayCastCallback, L2, L1);
        ArrayList<ArrayList<Flag>> flags = cutRayCastCallback.getFlags();
        ArrayList<LayerBlock> blocks = cutRayCastCallback.getCoveredBlocks();
        ArrayList<Fixture> fixtures = cutRayCastCallback.getCoveredFixtures();
        ArrayList<ArrayList<Segment>> allSegments = new ArrayList<>();
        ArrayList<GameEntity> entities = new ArrayList<>();
        for (int i = 0; i < flags.size(); i++) {
            List<Flag> list = flags.get(i);
            Collections.sort(list);

            LayerBlock block = blocks.get(i);

            Body body = fixtures.get(i).getBody();
            GameEntity entity = (GameEntity) body.getUserData();
            if (!entities.contains(entity)) {
                entities.add(entity);
                allSegments.add(new ArrayList<>());
            }
            int entityIndex = entities.indexOf(entity);
            ArrayList<Segment> entitySegments = allSegments.get(entityIndex);

            boolean firstInside = block.testPoint(body, worldPoint1.x, worldPoint1.y);
            boolean secondInside = block.testPoint(body, worldPoint2.x, worldPoint2.y);
            Vector2 splashVelocity = worldPoint1.cpy().sub(worldPoint2).nor().mul(1000);
            Vector2 p1;
            Vector2 p2;
            if (firstInside && secondInside) {
                p1 = body.getLocalPoint(worldPoint1).cpy().mul(32f);
                p2 = body.getLocalPoint(worldPoint2).cpy().mul(32f);

                SegmentFreshCut fc = new SegmentFreshCut(p1, p2, true, block.getProperties().getJuicinessDensity(), splashVelocity);
                block.addFreshCut(fc);
                createInnerCut(fc, entity, block);
            } else if (firstInside) {
                p1 = body.getLocalPoint(worldPoint1).cpy().mul(32f);
                p2 = body.getLocalPoint(list.get(list.size() - 1).getPoint()).cpy().mul(32f);
                Vector2 V = p1.cpy().sub(p2);
                if (V.len() > 4) {
                    SegmentFreshCut fc = new SegmentFreshCut(p1, p2.add(V.nor()), true, block.getProperties().getJuicinessDensity(), splashVelocity);
                    block.addFreshCut(fc);
                    createInnerCut(fc, entity, block);
                }
            } else if (secondInside) {
                p1 = body.getLocalPoint(list.get(0).getPoint()).cpy().mul(32f);
                p2 = body.getLocalPoint(worldPoint2).cpy().mul(32f);
                Vector2 V = p2.cpy().sub(p1);
                if (V.len() > 4) {
                    SegmentFreshCut fc = new SegmentFreshCut(p1.add(V.nor()), p2, true, block.getProperties().getJuicinessDensity(), splashVelocity);
                    block.addFreshCut(fc);
                    createInnerCut(fc, entity, block);
                }
            } else {
                List<Flag> cutList = list.stream().filter(e -> e.getFraction() >= f1 && e.getFraction() <= f2).collect(Collectors.toList());
                // perform the cut
                if (cutList.size() >= 2) {
                    p1 = list.get(0).getPoint();
                    p2 = list.get(list.size() - 1).getPoint();
                    entitySegments.add(new Segment(p1, p2, block, CutType.fullCut));
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            GameEntity cutEntity = entities.get(i);
            ArrayList<Segment> segments = allSegments.get(i);
            if (cutEntity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                applySlice(cutEntity, segments);
            }
        }
    }

    public void applyBluntTrauma(float x, float y, float impulse, GameEntity gameEntity, LayerBlock layerBlock) {

        float trauma = impulse / (5f * PhysicsConstants.TENACITY_FACTOR * layerBlock.getTenacity());
        int numberOfPoints = Math.min(10, Math.round(trauma));

        List<Vector2> pts = Vector2Utils.generateRandomPointsInsidePolygon(numberOfPoints, new Vector2(x, y), layerBlock, gameEntity);

        Runnable onBlunt;
        if (gameEntity.getParentGroup().getGroupType() == GroupType.DOLL) {
            RagDoll ragDoll = ((RagDoll) gameEntity.getParentGroup());
            if (ragDoll.isBluntSensitiveBodyPart(gameEntity)) {
                onBlunt = () -> ragDoll.onBlunt(gameEntity,Math.round(trauma));
            } else {
                onBlunt = null;
            }
        } else {
            onBlunt = null;
        }


        boolean applied = false;
        for (Vector2 p : pts) {
            Color juiceColor = layerBlock.getProperties().getJuiceColor();
            Color color =
                   gameEntity.getParentGroup().getGroupType()==GroupType.DOLL?
                    new Color((float) (0.5f+Math.random() * 0.3f), (float) (0.2f + Math.random() * 0.2f), (float) (0.2f + Math.random() * 0.2f))
                    : new Color(juiceColor);
            Color skin = new Color(layerBlock.getProperties().getDefaultColor());
            skin.setAlpha((float) (0.5f*skin.getAlpha()));
            MyColorUtils.blendColors(color, color, skin);
            if (onBlunt != null) {
                onBlunt.run();
            }
            boolean res = this.applyStain(gameEntity, p.x, p.y, layerBlock, color, 0f, 14, false);
            if (res) {
                applied = true;
            }
        }
        if (applied) {
            gameEntity.redrawStains();
        }
    }

    private int getStainPriorityFromIndex(int index) {
        if (index == 14) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean applyLiquidStain(GameEntity gameEntity, float x, float y, LayerBlock concernedBlock, Color color, float flammability, int index, boolean superpose) {
        return applyStain(gameEntity, x, y, concernedBlock, color, flammability, index, superpose);
    }

    public boolean applyStain(GameEntity gameEntity, float x, float y, LayerBlock concernedBlock, Color color, float flammability, int index, boolean superpose) {
        Vector2 localPosition = new Vector2(x, y);

        float angle = random.nextInt(360);
        StainBlock stainBlock = BlockFactory.createStainBlock(localPosition, angle, concernedBlock.getVertices(), index, color, flammability);

        if (stainBlock != null && stainBlock.isNotAborted() && (superpose || !gameEntity.isNonValidStainPosition(concernedBlock, stainBlock))) {
            stainBlock.setId(index);
            stainBlock.setPriority(this.getStainPriorityFromIndex(index));
            gameEntity.addStain(concernedBlock, stainBlock);
            return true;
        } else {
            return false;
        }
    }

    private void applySlice(GameEntity gameEntity, ArrayList<Segment> entitySegments) {
        boolean cutPerformed = false;
        ArrayList<LayerBlock> splintersBlocks = new ArrayList<>();
        for (Segment segment : entitySegments) {
            if (segment.getType() != CutType.fullCut) {
                continue;
            }

            Vector2 p1 = gameEntity.getBody().getLocalPoint(segment.getP1()).cpy().mul(32);
            Vector2 p2 = gameEntity.getBody().getLocalPoint(segment.getP2()).cpy().mul(32);
            LayerBlock block = segment.getBlock();
            List<Vector2> vertices = block.getVertices();

            ElementCouple<Vector2> sides1 = BlockUtils.getSides(p1, vertices);
            ElementCouple<Vector2> sides2 = BlockUtils.getSides(p2, vertices);

            Vector2 lower1 = sides1 != null ? sides1.getElement1() : null;
            Vector2 higher1 = sides1 != null ? sides1.getElement2() : null;
            Vector2 lower2 = sides2 != null ? sides2.getElement1() : null;
            Vector2 higher2 = sides2 != null ? sides2.getElement2() : null;

            if (lower1 == null || higher1 == null || lower2 == null || higher2 == null) {
                continue;
            }
            Cut cut = BlockUtils.correctExtremitiesAndCreateCut(p1, p2, lower1, higher1, lower2, higher2);
            if (!cut.isValid(vertices)) {
                continue;
            }
            block.performCut(cut);
            cutPerformed = true;


            ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().slashSound, calculateVolumeRatio(10 * cut.getLength()),1,cut.getP1().x,cut.getP1().y);

            Iterator<LayerBlock> iterator = block.createIterator();
            while (iterator.hasNext()) {
                LayerBlock bl = iterator.next();
                if (bl.isNotAborted()) {
                    splintersBlocks.add(bl);
                }
            }
        }
        if (cutPerformed) {
            for (LayerBlock b : gameEntity.getBlocks()) {
                if (b.isNotAborted()) {
                    splintersBlocks.add(b);
                }
            }
            computeSplinters(splintersBlocks, gameEntity);

            gameEntity.getBody().setActive(false);
            scene.getWorldFacade().destroyGameEntity(gameEntity, false);
        }
    }

    public void computePenetrationSpecialEffects(Vector2 normal, Vector2 impactPoint, float advance, List<TopographyData> envData, float collisionImpulse) {
        List<PenetrationPoint> allPenPoints = new ArrayList<>();
        for (TopographyData topographyData : envData) {
            List<PenetrationPoint> dataPenPoints = new ArrayList<>();
            for (int i = 0; i < topographyData.getLength(); i++) {
                float inf = topographyData.getData()[i][0];
                float sup = topographyData.getData()[i][1];
                if (advance >= inf) {
                    Vector2 point = topographyData.getBase().cpy().add((inf + 0.02f) * normal.x, (inf + 0.02f) * normal.y);
                    PenetrationPoint pe = new PenetrationPoint(topographyData.getEntities()[i], topographyData.getBlocks()[i], point, inf, advance - inf, true);
                    dataPenPoints.add(pe);
                }
                if (advance >= sup) {
                    Vector2 point = topographyData.getBase().cpy().add((sup - 0.05f) * normal.x, (sup - 0.05f) * normal.y);
                    PenetrationPoint ps = new PenetrationPoint(topographyData.getEntities()[i], topographyData.getBlocks()[i], point, sup, sup - inf, false);
                    dataPenPoints.add(ps);
                }
            }
            Map<LayerBlock, List<PenetrationPoint>> penPointsGroupedByBlock = dataPenPoints.stream().collect(Collectors.groupingBy(PenetrationPoint::getBlock));
            for (List<PenetrationPoint> list : penPointsGroupedByBlock.values()) {
                Collections.sort(list);
                PenetrationPoint enteringPoint = list.get(0);
                allPenPoints.add(enteringPoint);
                if (list.size() > 1) {
                    PenetrationPoint leavingPoint = list.get(list.size() - 1);
                    allPenPoints.add(leavingPoint);
                }
            }
        }
        playCollisionSound(impactPoint, allPenPoints.stream().filter(PenetrationPoint::isEntering).collect(Collectors.groupingBy(PenetrationPoint::getBlock)).values(), collisionImpulse);
        Map<GameEntity, List<PenetrationPoint>> groupedByEntity = allPenPoints.stream().collect(Collectors.groupingBy(PenetrationPoint::getEntity));

        for (Map.Entry<GameEntity, List<PenetrationPoint>> entry : groupedByEntity.entrySet()) {
            Map<LayerBlock, List<PenetrationPoint>> res = entry.getValue().stream().collect(Collectors.groupingBy(PenetrationPoint::getBlock));
            GameEntity entity = entry.getKey();
            for (Map.Entry<LayerBlock, List<PenetrationPoint>> entryByBlock : res.entrySet()) {
                LayerBlock layerBlock = entryByBlock.getKey();


                List<CutPoint> enterBleedingPoints = entryByBlock.getValue().stream().filter(PenetrationPoint::isEntering).map(p -> new CutPoint(entity.getBody().getLocalPoint(p.getPoint()).cpy().mul(32f), p.getWeight())).collect(Collectors.toList());
                if (!enterBleedingPoints.isEmpty()) {
                    float length = (float)enterBleedingPoints.stream().mapToDouble(e->0.05f*32f*32*Math.min(0.2f,e.getWeight())).sum()*0.1f;
                    length = Math.min(10f,length);
                    int limit = (int) Math.ceil(length * PhysicsConstants.BLEEDING_CONSTANT * layerBlock.getProperties().getJuicinessDensity());
                    if (limit > 0 && layerBlock.getProperties().isJuicy()) {
                        FreshCut freshCut = new PointsFreshCut(enterBleedingPoints, length, limit, normal.cpy().mul(-collisionImpulse));
                        this.createJuiceSource(entity, layerBlock, freshCut);
                        layerBlock.addFreshCut(freshCut);
                    }
                }
                List<CutPoint> leavingBleedingPoints = entryByBlock.getValue().stream().filter(p -> !p.isEntering()).map(p -> new CutPoint(entity.getBody().getLocalPoint(p.getPoint()).cpy().mul(32f), p.getWeight())).collect(Collectors.toList());
                if (!leavingBleedingPoints.isEmpty()) {
                    float length = (float) (leavingBleedingPoints.size())*0.05f*0.05f*32f*32f*0.1f;
                    length = Math.min(length,5f);
                    int limit = (int) Math.ceil(length * layerBlock.getProperties().getJuicinessDensity() * PhysicsConstants.BLEEDING_CONSTANT);
                    if (limit > 0 && layerBlock.getProperties().isJuicy()) {
                        FreshCut freshCut = new PointsFreshCut(leavingBleedingPoints, length, limit, normal.cpy());
                        this.createJuiceSource(entity, layerBlock, freshCut);
                        layerBlock.addFreshCut(freshCut);
                    }
                }
            }
        }
    }

    private static void playCollisionSound(Vector2 impactPoint, Collection<List<PenetrationPoint>> envData, float collisionImpulse) {
        envData.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting())) // Group by AcousticType and count occurrences
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Find the entry with the maximum count
                .map(e -> e.getKey().get(0).getBlock().getMaterialAcousticType())
                .ifPresent(mostCommonAcousticType -> processPenetrationSound(mostCommonAcousticType, collisionImpulse, impactPoint.x * 32f, impactPoint.y * 32f));
    }

    public void applyPointImpact(Vector2 worldPoint, float energy, GameEntity gameEntity, LayerBlock layerBlock) {
        if (gameEntity.getBody().getType() != BodyDef.BodyType.DynamicBody || !gameEntity.isAlive() || gameEntity.getBody() == null) {
            return;
        }
        applyOnePointImpactToEntity(layerBlock, energy, gameEntity, worldPoint);
    }

    public void applyPointImpact(Vector2 worldPoint, float energy, GameEntity gameEntity) {
        if (gameEntity.getBody().getType() != BodyDef.BodyType.DynamicBody || !gameEntity.isAlive() || gameEntity.getBody() == null) {
            return;
        }
        LayerBlock nearest = BlockUtils.getNearestBlock(gameEntity.getBody().getLocalPoint(worldPoint).cpy().mul(32f), gameEntity.getBlocks());
        if (nearest != null) {
            applyOnePointImpactToEntity(nearest, energy, gameEntity, worldPoint);
        }
    }

    private void applyOnePointImpactToEntity(LayerBlock block, float impulse, GameEntity gameEntity, Vector2 worldPoint) {
        if (gameEntity.hasActiveUsage(Smasher.class)) {
            gameEntity.getActiveUsage(Smasher.class).onCancel();
        }
        if (impulse < 10) {
            return;
        }
        if (gameEntity.getBody().getType() != BodyDef.BodyType.DynamicBody) {
            return;
        }
        Vector2 localPoint = gameEntity.getBody().getLocalPoint(worldPoint).cpy().mul(32f);
        if (block.getProperties().isJuicy()) {
            this.applyBluntTrauma(localPoint.x, localPoint.y, impulse, gameEntity, block);
        }
        if (gameEntity.hasUsage(ImpactBomb.class)) {
            ImpactBomb impactBomb = gameEntity.getUsage(ImpactBomb.class);
            if (impactBomb.isActive()) {
                if (impactBomb.getSensitiveLayers().contains(block.getId())) {
                    impactBomb.onImpact(impulse, block.getId(),this);
                }
            }
        }
        for(Explosion explosion:explosions){
            if(explosion.getCarrierEntity()==gameEntity){
                return;
            }
        }
        if (impulse > PhysicsConstants.TENACITY_FACTOR) {
            List<ImpactData> impactData = new ArrayList<>();
            impactData.add(new ImpactData(gameEntity, block, worldPoint, impulse));
            this.applyImpacts(gameEntity, impactData);
        }
    }

    public void pulverizeBlock(LayerBlock layerBlock, GameEntity gameEntity) {
        Body body = gameEntity.getBody();
        for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
            coatingBlock.setPulverized(true);
            coatingBlock.getTriangles().forEach(vector2 -> {
                Vector2 worldVector = body.getWorldPoint(vector2.mul(1 / 32f)).cpy().mul(32f);
                vector2.set(worldVector);
            });
        }
        ArrayList<? extends AssociatedBlock<?, ?>> copy = new ArrayList<>(layerBlock.getAssociatedBlocks());
        copy.forEach(associatedBlock -> {
            if (associatedBlock instanceof JointBlock) {
                JointBlock jointBlock = (JointBlock) associatedBlock;
                jointBlock.setAborted(true);
            }
        });
        PowderParticleWrapper powderParticleWrapper = new PowderParticleWrapper(this,gameEntity, layerBlock, gameEntity.getBody().getLinearVelocity().cpy());
        powderParticleWrappers.add(powderParticleWrapper);
        powderParticleWrapper.attachTo(scene);
        scene.sortChildren();
    }

    public void applyImpacts(GameEntity gameEntity, List<ImpactData> impactDataList) {
        if(!gameEntity.isAlive()){
            return;
        }
        Iterator<LayerBlock> iterator = new ArrayList<>(gameEntity.getBlocks()).iterator();
        List<LayerBlock> allSplinters = new ArrayList<>();
        boolean shatterPerformed = false;
        while (iterator.hasNext()) {
            LayerBlock layerBlock = iterator.next();
            List<ImpactData> layerImpacts = impactDataList.stream()
                    .filter(impactData -> impactData.getImpactedBlock() == layerBlock)
                    .collect(Collectors.toList());
            final float impactEnergy = (float) layerImpacts.stream().mapToDouble(ImpactData::getImpactImpulse).sum();
            List<Vector2> localCenters = layerImpacts.stream().map(ImpactData::getLocalImpactPoint).collect(Collectors.toList());
            Vector2 localCenter = GeometryUtils.calculateCenterScatter(localCenters);
            ShatterVisitor shatterVisitor = new ShatterVisitor(impactEnergy,localCenter,scene.getWorldFacade(),gameEntity);
            List<LayerBlock> splinters = shatterVisitor.visitTheElement(layerBlock);
            allSplinters.addAll(splinters);
            if(shatterVisitor.isShatterPerformed()){
                shatterPerformed = true;
            } else {
                applyStrain(layerBlock,impactEnergy);
            }
        }


       if (shatterPerformed) {
           if (allSplinters.size() > 0) {
              computeSplinters(allSplinters, gameEntity);
           }
           this.destroyGameEntity(gameEntity, false);
        }
    }
    private void applyStrain(LayerBlock layerBlock,float energy) {
        LayerProperties properties = layerBlock.getProperties();
        float ratio =
                (float) Math.min(0.1f, 0.05f * energy / (Math.pow(layerBlock.getTenacity(), 10)));
        float newTenacity = layerBlock.getTenacity() * (1f - ratio);
        properties.setTenacity(newTenacity);
    }
    public void applyImpactHeat(float heatRatio, List<ImpactData> impactData) {
        impactData.forEach(impact -> {
            impact.getImpactedBlock().getBlockGrid().getCoatingBlocks().forEach((coatingBlock -> {
                coatingBlock.onHeatWave(heatRatio / impact.getDistanceFromSource());

            }));
        });
    }

    private void computeInternalConduction(CoatingBlock coatingBlock) {

        HashSet<CoatingBlock> neighbors = coatingBlock.getNeighbors();

        for (CoatingBlock other : neighbors) {
            float area1 = coatingBlock.getArea();
            float area2 = other.getArea();
            float length = (float) (area1 <= area2 ? Math.sqrt(area1) : Math.sqrt(area2));
            PhysicsUtils.transferHeatByConduction(length, coatingBlock, other);
        }
    }

    public void scheduleGameEntityToDestroy(GameEntity entity, int time) {
        TimedCommand command = new EntityDestructionCommand(time, entity, this);
        timedCommands.add(command);
    }

    public GameGroup getGround() {
        return ground;
    }

    public void setGroundGroup(GameGroup ground) {
        this.ground = ground;
    }

    private void addJointBlocks(GameEntity entity1, GameEntity entity2, JointBlock jointBlock1, JointBlock jointBlock2, JointDef jointDef, boolean mirror) {
        Vector2 anchorA = new Vector2();
        Vector2 anchorB = new Vector2();

        switch (jointDef.type) {
            case DistanceJoint:
                assert jointDef instanceof DistanceJointDef;
                anchorA.set(((DistanceJointDef) jointDef).localAnchorA.cpy().mul(32f));
                anchorB.set(((DistanceJointDef) jointDef).localAnchorB.cpy().mul(32f));
                break;
            case Unknown:
            case GearJoint:
            case MouseJoint:
                MouseJointDef mouseJointDef = ((MouseJointDef) jointDef);
                Vector2 anchor = mouseJointDef.target.cpy().mul(32f);
                float[] pos = entity2.getMesh().convertSceneCoordinatesToLocalCoordinates(anchor.x, anchor.y);
                anchorB.set(pos[0], pos[1]);

            case LineJoint:
            case PulleyJoint:
            case FrictionJoint:
                break;
            case RevoluteJoint:
                anchorA.set(((RevoluteJointDef) jointDef).localAnchorA.cpy().mul(32f));
                anchorB.set(((RevoluteJointDef) jointDef).localAnchorB.cpy().mul(32f));
                break;
            case PrismaticJoint:
                anchorA.set(((PrismaticJointDef) jointDef).localAnchorA.cpy().mul(32f));
                anchorB.set(((PrismaticJointDef) jointDef).localAnchorB.cpy().mul(32f));
                break;
            case WeldJoint:
                anchorA.set(((WeldJointDef) jointDef).localAnchorA.cpy().mul(32f));
                anchorB.set(((WeldJointDef) jointDef).localAnchorB.cpy().mul(32f));
                break;
        }
        String jointUniqueId = UUID.randomUUID().toString();

        jointBlock1.initialization(entity1, jointUniqueId, jointDef.type, new ArrayList<>(Collections.singletonList(anchorA)), new JointBlockProperties(jointDef), 0, JointBlock.Position.A, jointBlock2);
        LayerBlock layerBlock1 = BlockUtils.getNearestBlock(anchorA, entity1.getBlocks());
        layerBlock1.addAssociatedBlock(jointBlock1);

        jointBlock2.initialization(entity2, jointUniqueId, jointDef.type, new ArrayList<>(Collections.singletonList(anchorB)), new JointBlockProperties(jointDef), 0, JointBlock.Position.B, jointBlock1);
        LayerBlock layerBlock2 = BlockUtils.getNearestBlock(anchorB, entity2.getBlocks());
        layerBlock2.addAssociatedBlock(jointBlock2);
        if (mirror) {
            jointBlock1.mirrorJointDef();
        }
    }


    public void addJointToRecreate(JointDef jointDef, GameEntity entity1, GameEntity entity2, JointBlock jointBlock) {
        Invoker.addJointCreationCommand(jointDef, GroupType.GROUND.equals(entity1.getParentGroup().getGroupType()) ? entity2.getParentGroup() : entity1.getParentGroup(), entity1, entity2, jointBlock);
    }

    /**
     * @noinspection UnusedReturnValue
     */
    public JointBlock addJointToCreate(JointDef jointDef, GameEntity entity1, GameEntity entity2, int jointId, boolean mirror) {
        JointBlock jointBlock1 = new JointBlock();
        JointBlock jointBlock2 = new JointBlock();
        jointBlock1.setJointId(jointId);
        jointBlock2.setJointId(jointId);
        this.addJointBlocks(entity1, entity2, jointBlock1, jointBlock2, jointDef, mirror);
        Invoker.addJointCreationCommand(jointDef, entity2.getParentGroup(), entity1, entity2, jointBlock2);
        return jointBlock1;
    }


    public JointBlock addJointToCreate(JointDef jointDef, GameEntity entity1, GameEntity entity2, int jointId) {
        return addJointToCreate(jointDef, entity1, entity2, jointId, false);
    }

    public Vector2 getAirVelocity(Vector2 worldPoint) {
        sum.set(0, 0);
        for (Explosion explosion : explosions) {
            if (explosion.isAlive()) {
                float velocity = explosion.getVelocity();
                result = worldPoint.cpy().sub(explosion.getCenter()).nor().mul(velocity);
                sum.add(result);
            }
        }
        return sum;
    }

    public void addNonCollidingPair(GameEntity entity1, GameEntity entity2) {
        this.contactListener.addNonCollidingPair(entity1, entity2);
    }

    public void removeNonCollidingPair(GameEntity entity1, GameEntity entity2) {
        this.contactListener.removeNonCollidingPair(entity1, entity2);
    }

    public List<GameEntity> findOverlappingEntities(List<TopographyData> penData, List<TopographyData> envData, float actualAdvance) {

        HashSet<TopographyData.Overlap> overlaps = new HashSet<>();
        for (int i = 0; i < penData.size(); i++) {
            overlaps.addAll(envData.get(i).findOverlaps(penData.get(i), actualAdvance));
        }
        return new ArrayList<>(overlaps.stream().collect(Collectors.groupingBy(TopographyData.Overlap::getGameEntity)).keySet());
    }

    public List<GameEntity> findReachedEntities(List<TopographyData> penData, List<TopographyData> envData, float actualAdvance) {
        HashSet<GameEntity> entities = new HashSet<>();
        for (int i = 0; i < penData.size(); i++) {
            entities.addAll(envData.get(i).findReachedEntities(penData.get(i), actualAdvance));
        }
        return new ArrayList<>(entities);
    }

    public List<TimedCommand> getTimedCommands() {
        return timedCommands;
    }

    public Set<Pair<GameEntity>> getNonCollidingPairs() {
        return this.contactListener.getNonCollidingEntities();
    }

    public PhysicsScene getPhysicsScene() {
        return scene;
    }

    public Touch areTouching(GameEntity entity1, GameEntity entity2) {
        for (Touch touch : touches) {
            if (entity1 != null && entity2 != null && entity1.getBody() != null && entity2.getBody() != null) {
                if (touch.isEquivalent(entity1.getBody(), entity2.getBody())) {
                    return touch;
                }
            }
        }
        return null;
    }

    /** @noinspection unused*/
    public Touch areTouching(LayerBlock layerBlock1, LayerBlock layerBlock2) {
        for (Touch touch : touches) {
            if (layerBlock1 != null && layerBlock2 != null) {
                if (touch.isEquivalent(layerBlock1, layerBlock2)) {
                    return touch;
                }
            }
        }
        return null;
    }

    public void onEntityDetached(GameEntity gameEntity) {
        //This is run on UI Thread
        for(LiquidParticleWrapper liquidParticleWrapper:this.liquidParticleWrappers){
            if(liquidParticleWrapper.isAlive()) {
                if (liquidParticleWrapper.getParent() == gameEntity) {
                    liquidParticleWrapper.detachDirect();
                }
            }
        }
        if (gameEntity.getFireParticleWrapper() != null) {
            if(gameEntity.getFireParticleWrapper().isAlive()) {
                scene.getWorldFacade().removeFlame(gameEntity.getFireParticleWrapper());
                gameEntity.getFireParticleWrapper().detachDirect();
            }
        }
        explosivesParticleWrappers.forEach(element->{
            if(element.getParent()==gameEntity){
                element.detachDirect();
                removeFlame(element);
            }
        });
        explosivesParticleWrappers.removeIf(e->!e.isAlive());
        liquidParticleWrappers.removeIf(element -> !element.isAlive());
    }

    public void debugZone(float x, float y){
        physicsWorld.QueryAABB(debugCallback, x-1f,y-1,x+1,y+1);
    }

    public void clearFlames() {
        this.flames.clear();
    }

}
