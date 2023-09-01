package com.evolgames.physics;


import static com.evolgames.physics.PhysicsConstants.FLUX_PRECISION;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;
import static java.lang.Math.min;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blockvisitors.BreakVisitor;
import com.evolgames.entities.blockvisitors.GameEntityMultiShatterVisitor;
import com.evolgames.entities.blockvisitors.ImpactData;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.contact.ContactObserver;
import com.evolgames.entities.contact.GameEntityContactListener;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.CutType;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.PointsFreshCut;
import com.evolgames.entities.cut.Segment;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.particles.systems.SpawnAction;
import com.evolgames.entities.particles.wrappers.ClusterLiquidParticleWrapper;
import com.evolgames.entities.particles.wrappers.Fire;
import com.evolgames.entities.particles.wrappers.LiquidParticleWrapper;
import com.evolgames.entities.particles.wrappers.PointExplosiveParticleWrapper;
import com.evolgames.entities.particles.wrappers.PulverizationParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.particles.wrappers.SegmentLiquidParticleWrapper;
import com.evolgames.entities.particles.wrappers.SegmentExplosiveParticleWrapper;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.ragdoll.Ragdoll;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.ElementCouple;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.PhysicsUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.physics.entities.Data;
import com.evolgames.physics.entities.Touch;
import com.evolgames.physics.entities.callbacks.BlockQueryCallBack;
import com.evolgames.physics.entities.callbacks.CutRayCastCallback;
import com.evolgames.physics.entities.callbacks.DetectionRayCastCallback;
import com.evolgames.physics.entities.callbacks.FluxInnerRayCastCallback;
import com.evolgames.physics.entities.callbacks.FluxRayCastCallback;
import com.evolgames.physics.entities.callbacks.GameEntityQueryCallBack;
import com.evolgames.physics.entities.callbacks.SimpleDetectionRayCastCallback;
import com.evolgames.physics.entities.explosions.Explosion;
import com.evolgames.physics.entities.explosions.ImpactInterface;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.BodyModel;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldFacade implements ContactObserver {
    private final HashSet<LiquidParticleWrapper> liquidParticleWrappers = new HashSet<>();
    private final HashSet<PulverizationParticleWrapperWithPolygonEmitter> powderParticleWrappers = new HashSet<>();
    private final ArrayList<Fire> flames = new ArrayList<>();
    private final SimpleDetectionRayCastCallback simpleDetectionRayCastCallback = new SimpleDetectionRayCastCallback();
    private final FluxRayCastCallback fluxRayCastCallback = new FluxRayCastCallback();
    private final FluxInnerRayCastCallback fluxInnerRayCastCallback = new FluxInnerRayCastCallback();
    private final ArrayList<TimedCommand> timedCommands = new ArrayList<>();
    private final GameEntityContactListener contactListener;
    private final GameEntityQueryCallBack queryCallBack = new GameEntityQueryCallBack();
    private final BlockQueryCallBack blockQueryCallBack = new BlockQueryCallBack();
    private final LinearRayCastCallback rayCastCallback = new LinearRayCastCallback();
    private final DetectionRayCastCallback detectionRayCastCallback = new DetectionRayCastCallback();
    private final PhysicsWorld physicsWorld;
    private final GameScene gameScene;
    private final CutRayCastCallback cutRayCastCallback = new CutRayCastCallback();
    private final HashSet<Touch> touches = new HashSet<>();
    private final HashSet<Explosion> explosions = new HashSet<>();
    private final HashSet<ExplosiveParticleWrapper> explosives = new HashSet<>();
    Random random = new Random();
    Vector2 result = new Vector2();
    Vector2 sum = new Vector2();
    private GameGroup ground;
    private final BodyModel groundModel;

    public WorldFacade(GameScene scene) {
        gameScene = scene;
        physicsWorld = new PhysicsWorld(obtain(0, PhysicsConstants.gravity), false);
        contactListener = new GameEntityContactListener(this);
        physicsWorld.setContactListener(contactListener);


        scene.registerUpdateHandler(physicsWorld);
        physicsWorld.setVelocityIterations(8);
        physicsWorld.setPositionIterations(3);
        physicsWorld.setContinuousPhysics(true);

        this.groundModel = new BodyModel(-1);
        this.groundModel.setModelName("Ground");
    }

    public void onStep(float deltaTime) {

        for (TimedCommand timedCommand : timedCommands) {
            timedCommand.update();
        }
        for (Explosion explosion : explosions) {
            explosion.update();
        }
        this.contactListener.getNonCollidingEntities().removeIf(pair -> !pair.first.isAlive() || !pair.second.isAlive());

        clearPulverizationWrappers();
        clearLiquidWrappers();
        for (ExplosiveParticleWrapper explosiveParticleWrapper : explosives) {
            explosiveParticleWrapper.update();
        }


        if (false) {
            plotTouch();
        }
        computeConduction();
        computeConvection();
        createFires();
        computeLiquidStaining();
    }


    public void removeFireParticleWrapper(Fire fireParticleWrapper) {
        fireParticleWrapper.getFireParticleSystem().detachSelf();
        flames.remove(fireParticleWrapper);
    }

    public void computeSplinters(List<LayerBlock> splinterBlocks, GameEntity parentEntity) {

        Body body = parentEntity.getBody();
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float rot = body.getAngle();
        ArrayList<GameEntity> splinters = GameEntityFactory.getInstance().createSplinterEntities(x, y, rot, BlockUtils.getDivisionGroups(splinterBlocks), body.getLinearVelocity(), body.getAngularVelocity(), parentEntity.getName(), parentEntity);
        HashSet<Pair<GameEntity, GameEntity>> setOfPairs = new HashSet<>();
        splinters.forEach(splinter -> {
            for (Pair<GameEntity, GameEntity> pair : this.contactListener.getNonCollidingEntities()) {
                if (pair.first == parentEntity) {
                    setOfPairs.add(new Pair<>(splinter, pair.second));
                } else if (pair.second == parentEntity) {
                    setOfPairs.add(new Pair<>(splinter, pair.first));
                }
            }
            splinter.setParentGameEntity(parentEntity);
            ArrayList<LayerBlock> layerBlocks = splinter.getBlocks();
            layerBlocks.stream().flatMap(layerBlock -> layerBlock.getAssociatedBlocks().stream()).filter(associated -> associated instanceof JointBlock).map(associated -> (JointBlock) associated).forEachOrdered(jointBlock -> this.recreateJoint(jointBlock, splinter));
            parentEntity.getParentGroup().addGameEntity(splinter);
            if (parentEntity.getType() == SpecialEntityType.Head) {
                if (GeometryUtils.PointInPolygon(0, 9, splinter.getBlocks().get(0).getVertices())) {
                    splinter.setType(SpecialEntityType.Head);
                    ((Ragdoll) parentEntity.getParentGroup()).setHead(splinter);
                }
            } else splinter.setType(parentEntity.getType());
            if (splinter.getArea() < PhysicsConstants.MINIMUM_STABLE_SPLINTER_AREA) {
                this.scheduleGameEntityToDestroy(splinter, (int) (splinter.getArea()));
            }
        });
        this.contactListener.getNonCollidingEntities().addAll(setOfPairs);
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


    private void addFlame(Fire fireParticleWrapper) {
        flames.add(fireParticleWrapper);
    }

    public void createJuiceSource(GameEntity parentEntity, LayerBlock layerBlock, final FreshCut freshCut) {
        if (layerBlock.getLiquidQuantity() <= 0 || !freshCut.isAlive()) {
            return;
        }
        int lowerRate = (int) (layerBlock.getProperties().getJuicinessLowerPressure() * freshCut.getLength());
        int higherRate = (int) (layerBlock.getProperties().getJuicinessUpperPressure() * freshCut.getLength());
        if (lowerRate == 0 || higherRate == 0) {
            return;
        }
        LiquidParticleWrapper particleWrapper =
                gameScene.getWorldFacade().createLiquidParticleWrapper(parentEntity, freshCut, (layerBlock.getProperties().getJuiceColor() != null) ? layerBlock.getProperties().getJuiceColor() : Utils.getRandomColor(), lowerRate, higherRate);
        SpawnAction spawnAction = (Particle<UncoloredSprite> p) -> {
            layerBlock.decrementLiquidQuantity(freshCut.getLength());
            freshCut.decrementLimit();
            if (layerBlock.getLiquidQuantity() <= 0 || freshCut.getLimit() <= 0) {
                particleWrapper.finishSelf();
                freshCut.setAlive(false);
            }
        };
        particleWrapper.getParticleSystem().setSpawnAction(spawnAction);
    }

    private LiquidParticleWrapper liquidParticleWrapperFromFreshCut(GameEntity parentEntity, FreshCut freshCut, Color color, int lowerRate, int higherRate) {
        if (freshCut instanceof SegmentFreshCut) {
            SegmentFreshCut sfc = (SegmentFreshCut) freshCut;
            return new SegmentLiquidParticleWrapper(parentEntity, new float[]{sfc.first.x, sfc.first.y, sfc.second.x, sfc.second.y}, color, lowerRate, higherRate);

        } else {
            PointsFreshCut pfc = (PointsFreshCut) freshCut;
            float[] data = Utils.mapPointsToArray(pfc.getPoints());
            return new ClusterLiquidParticleWrapper(parentEntity, color, data, pfc.getSplashVelocity(), lowerRate, higherRate);
        }
    }

    public SegmentExplosiveParticleWrapper createFireSource(GameEntity entity, Vector2 v1, Vector2 v2, float velocity, float fireRatio, float smokeRatio, float sparkRatio, float intensity, float temperature) {
        Vector2 dir = v1.cpy().sub(v2).nor();
        Vector2 normal = new Vector2(-dir.y, dir.x);
        SegmentExplosiveParticleWrapper explosiveParticleWrapper = new SegmentExplosiveParticleWrapper(entity,
                new float[]{v1.x, v1.y, v2.x, v2.y}, normal.cpy().mul(32f * velocity), fireRatio, smokeRatio, sparkRatio, intensity, temperature);

        explosiveParticleWrapper.setParent(entity);
        if (explosiveParticleWrapper.getFireParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getFireParticleSystem());
            explosiveParticleWrapper.getFireParticleSystem().setZIndex(5);
            this.addFlame(explosiveParticleWrapper);
        }
        if (explosiveParticleWrapper.getSmokeParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getSmokeParticleSystem());
            explosiveParticleWrapper.getSmokeParticleSystem().setZIndex(5);
        }
        if (explosiveParticleWrapper.getSparkParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getSparkParticleSystem());
            explosiveParticleWrapper.getSparkParticleSystem().setZIndex(5);
        }
        this.explosives.add(explosiveParticleWrapper);
        gameScene.sortChildren();
        return explosiveParticleWrapper;
    }

    public PointExplosiveParticleWrapper createPointFireSource(GameEntity entity, Vector2 p, float velocity, float fireRatio, float smokeRatio, float sparkRatio, float particles, float temperature, boolean trackFireParticles) {

        PointExplosiveParticleWrapper explosiveParticleWrapper = new PointExplosiveParticleWrapper(entity,
                new float[]{p.x, p.y, p.x, p.y}, 32f * velocity / 10f, fireRatio, smokeRatio, sparkRatio, particles, temperature);

        explosiveParticleWrapper.setParent(entity);
        if (explosiveParticleWrapper.getFireParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getFireParticleSystem());
            explosiveParticleWrapper.getFireParticleSystem().setZIndex(5);
            if (trackFireParticles) {
                this.addFlame(explosiveParticleWrapper);
            }
        }
        if (explosiveParticleWrapper.getSmokeParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getSmokeParticleSystem());
            explosiveParticleWrapper.getSmokeParticleSystem().setZIndex(5);
        }
        if (explosiveParticleWrapper.getSparkParticleSystem() != null) {
            gameScene.attachChild(explosiveParticleWrapper.getSparkParticleSystem());
            explosiveParticleWrapper.getSparkParticleSystem().setZIndex(5);
        }
        this.explosives.add(explosiveParticleWrapper);
        gameScene.sortChildren();
        return explosiveParticleWrapper;
    }

    public LiquidParticleWrapper createLiquidParticleWrapper(GameEntity parentEntity, final FreshCut freshCut, Color color, int lowerRate, int higherRate) {
        LiquidParticleWrapper liquidSource = liquidParticleWrapperFromFreshCut(parentEntity, freshCut, color, lowerRate, higherRate);
        liquidParticleWrappers.add(liquidSource);
        liquidSource.getParticleSystem().setZIndex(5);
        gameScene.attachChild(liquidSource.getParticleSystem());
        gameScene.sortChildren();
        liquidSource.setParent(parentEntity);
        return liquidSource;
    }

    public HashSet<GameEntity> findEntitiesInZone(float rx, float ry, float halfWidth, float halfHeight) {
        queryCallBack.reset();
        physicsWorld.QueryAABB(queryCallBack, rx - halfWidth, ry - halfHeight, rx + halfWidth, ry + halfHeight);
        return queryCallBack.getEntities();
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

    public void createExplosion(GameEntity source, float x, float y, float fireRatio, float smokeRatio, float sparkRatio, float particles, float force, float heat, float speed) {
        Explosion explosion = new Explosion(gameScene, source, new Vector2(x, y), particles, force, speed, heat, fireRatio, smokeRatio, sparkRatio);
        explosions.add(explosion);
    }

    private void clearLiquidWrappers() {
        Iterator<LiquidParticleWrapper> wrapperIterator = liquidParticleWrappers.iterator();
        while (wrapperIterator.hasNext()) {
            LiquidParticleWrapper liquidParticleWrapper = wrapperIterator.next();
            liquidParticleWrapper.update();
            if (!liquidParticleWrapper.isAlive() && liquidParticleWrapper.isAllParticlesExpired()) {
                liquidParticleWrapper.getParticleSystem().detachSelf();
                wrapperIterator.remove();
            }
        }
    }

    private void clearPulverizationWrappers() {
        Iterator<PulverizationParticleWrapperWithPolygonEmitter> iterator = powderParticleWrappers.iterator();
        while (iterator.hasNext()) {
            PulverizationParticleWrapperWithPolygonEmitter particleWrapper = iterator.next();
            particleWrapper.update();
            if (!particleWrapper.isAlive() && particleWrapper.allExpired()) {
                iterator.remove();
                particleWrapper.finishSelf();
            }
        }
    }

    private void computeConduction() {
        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (!gameEntity.getName().equals("Ground")) {
                    for (LayerBlock block : gameEntity.getBlocks()) {
                        for (CoatingBlock coatingBlock : block.getBlockGrid().getCoatingBlocks()) {
                            computeInternalConduction(coatingBlock);
                        }
                    }
                }
            }
    }

    private void computeLiquidStaining() {
        HashSet<GameEntity> affectedEntities = new HashSet<>();

        for (LiquidParticleWrapper liquidSource : liquidParticleWrappers)
            if (liquidSource.getParticleSystem().hasParent())
                if (liquidSource.getParticleSystem().getParticles().length > 0) {
                    for (Particle<?> p : liquidSource.getParticleSystem().getParticles()) {
                        if (p != null && !p.isExpired() && Math.random() < PhysicsConstants.STAINING_PROBABILITY) {
                            Vector2 position = new Vector2(p.getEntity().getX(), p.getEntity().getY());
                            float halfWidth = p.getEntity().getWidth() / 32f / 2f;
                            float halfHeight = p.getEntity().getHeight() / 32f / 2f;
                            //find concerned entities

                            float rx = position.x / 32f;
                            float ry = position.y / 32f;
                            HashSet<GameEntity> entities = findEntitiesInZone(rx, ry, halfWidth, halfHeight);
                            Vector2 rPosition = new Vector2(rx, ry);
                            boolean onBoundaries = false;

                            for (GameEntity entity : entities)
                                if (entity.getBody() != null)
                                    for (LayerBlock block : entity.getBlocks()) {
                                        if (block.testPoint(entity.getBody(), rx, ry)) {
                                            onBoundaries = true;
                                            break;
                                        }
                                    }

                            boolean dead = false;
                            if (onBoundaries) {
                                int index = random.nextInt(14);
                                for (GameEntity entity : entities) {
                                    if (entity.getBody() != null) {
                                        boolean affected = false;
                                        Body body = entity.getBody();
                                        Vector2 localPoint = obtain(body.getLocalPoint(rPosition)).mul(32);
                                        ArrayList<LayerBlock> blocks = entity.getBlocks();
                                        for (int i = blocks.size() - 1; i >= 0; i--) {
                                            boolean applied = applyLiquidStain(entity, localPoint.x, localPoint.y, blocks.get(i), liquidSource.getLiquidColor(), index);
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
        for (GameEntity entity : affectedEntities) {
            entity.redrawStains();
        }
    }

    private void createFires() {
        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (!gameEntity.isAlive() || gameEntity.getName().equals("Ground")) {
                    continue;
                }
                for (LayerBlock block : gameEntity.getBlocks()) {
                    for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                        if (grain.isOnFire()) {
                            if (!gameEntity.isFireSetup()) {
                                gameEntity.setupFire();
                                addFlame(gameEntity.getFireParticleWrapperWithPolygonEmitter());
                            }
                        }
                    }
                }
            }
    }


    private void computeConvection() {
        //convection
        Collections.shuffle(flames);
        for (Fire fire : flames) {
            Particle<UncoloredSprite>[] particles = fire.getFireParticleSystem().getParticles();
            for (Particle<UncoloredSprite> p : particles) {
                if (p == null || p.isExpired()) continue;

                Entity spark = p.getEntity();

                float x = spark.getX();
                float y = spark.getY();
                float w = 3;
                float h = 3;
                Vector2 worldPosition = new Vector2(x / 32f, y / 32f);
                Vector2 lower = new Vector2(x - w / 2f, y - h / 2f).mul(1 / 32f);
                Vector2 upper = new Vector2(x + w / 2f, y + h / 2f).mul(1 / 32f);
                blockQueryCallBack.reset();
                physicsWorld.QueryAABB(blockQueryCallBack, lower.x, lower.y, upper.x, upper.y);
                HashSet<LayerBlock> list = blockQueryCallBack.getBlocks();
                if (list.size() == 0) continue;

                for (LayerBlock block : list) {
                    if (block.getFixtures().size() == 0) continue;
                    Body body = block.getBody();

                    if (body == null) continue;
                    Vector2 localPosition = obtain(body.getLocalPoint(worldPosition)).mul(32);
                    CoatingBlock nearestCoatingBlock = block.getBlockGrid().getNearestCoatingBlockSimple(localPosition);
                    recycle(localPosition);

                    if (nearestCoatingBlock != null) {
                        double sparkTemperature = fire.getParticleTemperature(p);
                        PhysicsUtils.transferHeatByConvection(0.05f, 1f, 1f, sparkTemperature, nearestCoatingBlock, true);
                    }
                }
            }
        }


        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (gameEntity.getName().equals("Ground")) {
                    continue;
                }
                for (LayerBlock block : gameEntity.getBlocks()) {
                    for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                        PhysicsUtils.transferHeatByConvection(0.0001f, 100f, 0.00125f, PhysicsConstants.ambient_temperature, grain, (gameEntity.getName().equals("test")));
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
                GameScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
                GameScene.plotter.drawPoint(second.cpy().mul(32), Color.RED, 1f, 0);
            } else {
                Vector2 first = contact.getWorldManifold().getPoints()[0];
                GameScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
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
        float normalImpulse = normalImpulses[0];
        float impulseValue = (float) Math.sqrt(normalImpulses[0] * normalImpulses[0] + tangentImpulses[0] * tangentImpulses[0]);

        if (normalImpulse < PhysicsConstants.TENACITY_FACTOR * 6) {
            return;
        }
        if (entitiesAreInvolvedInPenetration(entity1, entity2)) {
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
        if (!equivalentFound) {
            Touch newTouch = new Touch(block1, block2, body1, body2);
            touches.add(newTouch);
        }

        if (entity1 == null || entity2 == null) {
            return;
        }
        if (!entity1.isAlive() || !entity2.isAlive()) {
            return;
        }
        if (!(entity1.isProjectile() || entity2.isProjectile())) {
            return;
        }

        if (entitiesAreInvolvedInPenetration(entity1, entity2)) {
            return;
        }

        computePenetrationImpact(contact, entity1, entity2);

    }

    private boolean entitiesAreInvolvedInPenetration(GameEntity entity1, GameEntity entity2) {

        if (this.contactListener.shouldNotCollide(entity1, entity2.getParentGameEntity())) {
            return true;
        }

        return this.contactListener.shouldNotCollide(entity2, entity1.getParentGameEntity());
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

    private float computeCollisionImpulse(Vector2 V1, Vector2 V2, Vector2 normal, float m1, float m2) {
        return Math.abs(m1 * V1.dot(normal) - m2 * V2.dot(normal));
    }

    private boolean checkVectorPointsToEntity(Vector2 worldPoint, Vector2 vector, GameEntity entity) {
        Vector2 normalBackoff = obtain(vector).mul(PhysicsConstants.BACKOFF);
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity);
        physicsWorld.rayCast(detectionRayCastCallback, worldPoint.cpy().sub(normalBackoff), worldPoint.cpy().add(vector));
        float f1 = detectionRayCastCallback.getFraction();
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity);
        physicsWorld.rayCast(detectionRayCastCallback, worldPoint.cpy().add(normalBackoff), worldPoint.cpy().sub(vector));
        float f2 = detectionRayCastCallback.getFraction();
        detectionRayCastCallback.reset();
        recycle(normalBackoff);

        return f1 < f2;
    }

    private Vector2 detectIntersectionWithEntity(GameEntity entity, Vector2 worldBegin, Vector2 worldEnd) {
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity);
        physicsWorld.rayCast(detectionRayCastCallback, worldBegin, worldEnd);
        return detectionRayCastCallback.getIntersectionPoint();
    }

    public BodyModel getGroundModel() {
        return this.groundModel;
    }

    enum EPointType {
        ENTERING, LEAVING
    }

    class EPoint {
        GameEntity gameEntity;
        LayerBlock layerBlock;
        Vector2 localPoint;
        EPointType ePointType;

        public LayerBlock getLayerBlock() {
            return layerBlock;
        }

        public EPoint(GameEntity gameEntity, LayerBlock layerBlock, Vector2 localPoint, EPointType ePointType) {
            this.gameEntity = gameEntity;
            this.layerBlock = layerBlock;
            this.localPoint = localPoint;
            this.ePointType = ePointType;
        }

        @Override
        public String toString() {
            return "EPoint{" +
                    "layerBlock=" + layerBlock.hashCode() +
                    ", localPoint=" + localPoint +
                    ", ePointType=" + ePointType +
                    '}';
        }
    }

    private HashSet<EPoint> findEPoints(GameEntity penetrated, Data data1, float DIRECTION, Vector2 normal) {
        float[][] data = data1.getData();
        HashSet<EPoint> result = new HashSet<>();
        for (int i = 0; i < data1.getSize(); i++) {
            if (data1.getEntities()[i] != penetrated) {
                continue;
            }
            float[] datum = data1.getData()[i];
            boolean found1 = false;
            boolean found2 = false;
            float inf = datum[0];
            float sup = datum[1];
            for (float[] other : data) {
                if (other != datum) {
                    if (inf - 0.01f < other[1] && inf - 0.01f > other[0]) {
                        found1 = true;
                        break;
                    }
                }
            }
            for (float[] other : data) {
                if (other != datum) {
                    if (sup + 0.01f < other[1] && sup + 0.01f > other[0]) {
                        found2 = true;
                        break;
                    }
                }
            }
            if (!found1) {
                Vector2 worldPoint = data1.getBase().cpy().add(normal.x * inf * DIRECTION, normal.y * inf * DIRECTION);
                Vector2 localPoint = data1.getEntities()[i].getBody().getLocalPoint(worldPoint).cpy().mul(32f);
                result.add(new EPoint(data1.getBlocks()[i].getGameEntity(), data1.getBlocks()[i], localPoint, EPointType.ENTERING));
            }
            if (!found2) {
                Vector2 worldPoint = data1.getBase().cpy().add(normal.x * sup * DIRECTION, normal.y * sup * DIRECTION);
                Vector2 localPoint = data1.getEntities()[i].getBody().getLocalPoint(worldPoint).cpy().mul(32f);
                result.add(new EPoint(data1.getBlocks()[i].getGameEntity(), data1.getBlocks()[i], localPoint, EPointType.LEAVING));
            }
        }
        return result;
    }

    private void drawData(Data data1, float DIRECTION, Vector2 normal, Color color) {
        float[][] data = data1.getData();
        for (float[] datum : data) {
            float inf = datum[0];
            float sup = datum[1];
            Vector2 p1 = data1.getBase().cpy().add(normal.x * inf * DIRECTION, normal.y * inf * DIRECTION).mul(32f);
            Vector2 p2 = data1.getBase().cpy().add(normal.x * sup * DIRECTION, normal.y * sup * DIRECTION).mul(32f);
            GameScene.plotter2.drawLine2(p1, p2, Utils.getRandomColor(), 1);

            GameScene.plotter2.drawPoint(p1, Color.RED, 1);

            GameScene.plotter2.drawPoint(p2, Color.CYAN, 1);
        }
    }

    private void computePenetrationImpact(Contact contact, GameEntity entity1, GameEntity entity2) {

        Vector2[] points = contact.getWorldManifold().getPoints();
        Vector2 point;
        if (contact.getWorldManifold().getNumberOfContactPoints() == 1) {
            point = points[0];
        } else {
            point = points[0].add(points[1]).mul(0.5f);
        }


        LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
        LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();

        Vector2 V1 = entity1.getBody().getLinearVelocity();
        Vector2 V2 = entity2.getBody().getLinearVelocity();
        float m1 = entity1.getBody().getMass();
        float m2 = entity2.getBody().getMass();
        Vector2 normal = obtain(V1.x - V2.x, V1.y - V2.y).nor();
        Vector2 tangent = obtain(-normal.y, normal.x);

        final float collisionImpulse = computeCollisionImpulse(V1, V2, normal, m1, m2);

        GameEntity penetrator = entity1.isProjectile() ? entity1 : entity2;
        GameEntity penetrated = penetrator == entity1 ? entity2 : entity1;


        float[] bounds = BlockUtils.getBounds(penetrator, penetrator.getBody(), point, tangent, normal);
        float infTPenetrator = bounds[0];
        float infNPenetrator = bounds[1];
        float supTPenetrator = bounds[2];
        float supNPenetrator = bounds[3];

        bounds = BlockUtils.getBounds(penetrated, penetrated.getBody(), point, tangent, normal);
        float infTPenetrated = bounds[0];
        float infNPenetrated = bounds[1];
        float supTPenetrated = bounds[2];
        float supNPenetrated = bounds[3];

        boolean normalPointsToPenetrated = checkVectorPointsToEntity(point, normal, penetrated);

        float xN1 = (normalPointsToPenetrated) ? supNPenetrator : infNPenetrator;
        Vector2 penetratorDetectionOriginStart = obtain(point.x + normal.x * xN1, point.y + normal.y * xN1);

        float xN2 = (!normalPointsToPenetrated) ? supNPenetrator : infNPenetrator;
        Vector2 penetratorDetectionOriginEnd = obtain(point.x + normal.x * xN2, point.y + normal.y * xN2);

        float xN3 = (normalPointsToPenetrated) ? supNPenetrated : infNPenetrated;
        Vector2 penetratedDetectionOriginEnd = obtain(point.x + normal.x * xN3, point.y + normal.y * xN3);

        float infX = Math.max(infTPenetrator, infTPenetrated);
        float supX = Math.min(supTPenetrator, supTPenetrated);
        float dT = Math.abs(supX - infX) / PhysicsConstants.PENETRATION_PRECISION;


        int n = 0;
        while (n * dT < supX) {
            n++;
        }

        int m = 0;
        while (-m * dT > infX) {
            m++;
        }


        int limit1 = Math.max(m, n);
        int limit2 = min(m, n);
        float sign = (m >= n) ? -1 : 1;
        final int p = m + n + 1;
        Data[] penetratedTopography = new Data[p];
        Data[] penetratorTopography = new Data[p];

        int counter = 0;


        int index = 0;
        Vector2 movingPenetratorDetectionOriginStart = obtain();
        Vector2 movingPenetratorDetectionOriginEnd = obtain();

        Vector2 movingPenetratedDetectionOriginEnd = obtain();

        final int direction = normalPointsToPenetrated ? -1 : 1;
        final float xBackoff = normal.x * PhysicsConstants.BACKOFF * direction;
        final float yBackoff = normal.y * PhysicsConstants.BACKOFF * direction;
        final Set<EPoint> ePoints = new HashSet<>();
        while (counter <= limit1) {
            Data data1, data2;
            movingPenetratorDetectionOriginStart.set(penetratorDetectionOriginStart.x + counter * tangent.x * sign * dT, penetratorDetectionOriginStart.y + counter * tangent.y * sign * dT);
            movingPenetratorDetectionOriginEnd.set(penetratorDetectionOriginEnd.x + counter * tangent.x * sign * dT, penetratorDetectionOriginEnd.y + counter * tangent.y * sign * dT);
            movingPenetratedDetectionOriginEnd.set(penetratedDetectionOriginEnd.x + counter * tangent.x * sign * dT, penetratedDetectionOriginEnd.y + counter * tangent.y * sign * dT);

            Vector2 intersection1 = detectIntersectionWithEntity(penetrator, movingPenetratorDetectionOriginStart.cpy().sub(xBackoff, yBackoff), movingPenetratorDetectionOriginEnd.cpy().add(xBackoff, yBackoff));
            if (intersection1 != null) {
                data1 = findIntervalsExclusiveToOneEntity(penetrator, intersection1, normal.cpy().mul(direction), intersection1.cpy().sub(xBackoff, yBackoff), movingPenetratorDetectionOriginEnd.cpy().add(xBackoff, yBackoff));
                penetratorTopography[index] = data1;
                Vector2 intersection2 = detectIntersectionWithEntity(penetrated, intersection1.cpy().add(xBackoff, yBackoff), movingPenetratedDetectionOriginEnd.cpy().sub(xBackoff, yBackoff));
                if (intersection2 != null) {
                    data2 = findIntervalsExceptingOneEntity(penetrator, intersection1, normal.cpy().mul(-direction), intersection1.cpy().add(xBackoff, yBackoff), movingPenetratedDetectionOriginEnd.cpy().sub(xBackoff, yBackoff));
                    penetratedTopography[index] = data2;
                    //drawData(data2, -DIRECTION, normal, Color.YELLOW);
                    ePoints.addAll(this.findEPoints(penetrated, data2, -direction, normal));
                }
            }
            if (counter >= 0) {
                counter++;
            }
            if (counter <= limit2) {
                counter *= -1;
            }
            index++;
        }

        recycle(movingPenetratorDetectionOriginStart);
        recycle(movingPenetratorDetectionOriginEnd);
        recycle(movingPenetratedDetectionOriginEnd);
        recycle(penetratorDetectionOriginStart);
        recycle(penetratorDetectionOriginEnd);
        recycle(penetratedDetectionOriginEnd);

        float penetrationLength = getPenetrationLength(penetrated, p, penetratedTopography, penetratorTopography);
        LayerBlock penetratedBlock = penetrated.getBlocks().contains(block1) ? block1 : block2;
        float stepAdvance = penetrationLength / PhysicsConstants.PENETRATION_PRECISION;

        float massFraction = penetrator.getBody().getMass() / (penetrator.getBody().getMass() + penetrator.getBody().getMass());

        int step = 0;
        float consumedEnergy = 0;
        while (step < PhysicsConstants.PENETRATION_PRECISION) {
            final float advance = (step + 1) * stepAdvance;
            consumedEnergy = 0;
            for (index = 0; index < penetratedTopography.length; index++) {
                if (penetratedTopography[index] != null && penetratorTopography[index] != null) {
                    float dE = penetratedTopography[index].getEnergyForAdvance(advance, dT);
                    consumedEnergy += dE;
                }
            }
            if (collisionImpulse - consumedEnergy < 0) {
                if (step >= PhysicsConstants.PENETRATION_PRECISION / 10) {
                    handlePenetrationEffects(normal, direction, penetrated, dT, ePoints, advance);
                    penetrated.getBody().applyLinearImpulse(normal.cpy().mul(-direction * consumedEnergy * massFraction), point);
                    checkOnePointImpact(penetratedBlock, obtain(point), collisionImpulse * massFraction, penetrated);
                }
                return;
            }
            step++;
        }

        handlePenetrationEffects(normal, direction, penetrated, dT, ePoints, PhysicsConstants.PENETRATION_PRECISION * stepAdvance);

        if (penetrated.getParentGroup().isLiving()) {
            for (GameEntity other : penetrated.getParentGroup().getGameEntities()) {
                this.contactListener.addNonCollidingPair(penetrator, other);
            }
        } else {
            this.contactListener.addNonCollidingPair(penetrator, penetrated);
        }
        penetrated.getBody().applyLinearImpulse(normal.cpy().mul(-direction * consumedEnergy * massFraction), point);
        checkOnePointImpact(penetratedBlock, obtain(point), 5 * collisionImpulse * massFraction, penetrated);
    }

    private void handlePenetrationEffects(Vector2 normal, int direction, GameEntity penetrated, float dT, Set<EPoint> ePoints, float advance) {

        ePoints.stream().collect(Collectors.groupingBy(EPoint::getLayerBlock)).forEach((layerBlock, exPoints) -> {
            if (!layerBlock.getProperties().isJuicy()) {
                return;
            }
            List<Vector2> bleedingPoints = exPoints.stream().filter(v -> v.ePointType == EPointType.ENTERING).map(v -> v.localPoint).collect(Collectors.toList());
            if (!bleedingPoints.isEmpty()) {
                float length = dT * 32f * advance * 32f * bleedingPoints.size();
                if (length > 1) {
                    FreshCut freshCut = new PointsFreshCut(bleedingPoints, length, normal.cpy().mul(direction * 60f * layerBlock.getProperties().getJuicinessUpperPressure()), layerBlock);
                    this.createJuiceSource(layerBlock.getGameEntity(), layerBlock, freshCut);
                    layerBlock.addFreshCut(freshCut);
                }
            }
            bleedingPoints = exPoints.stream().filter(v -> v.ePointType == EPointType.LEAVING).map(v -> v.localPoint).collect(Collectors.toList());
            if (!bleedingPoints.isEmpty()) {
                float length = dT * 32f * bleedingPoints.size();
                if (length > 1) {
                    FreshCut freshCut = new PointsFreshCut(bleedingPoints, length, new Vector2(), layerBlock);
                    this.createJuiceSource(layerBlock.getGameEntity(), layerBlock, freshCut);
                    layerBlock.addFreshCut(freshCut);
                }
            }
        });
    }

    private void mergeEntities(GameEntity receiver, GameEntity traveler, Vector2 advance, Vector2 impactWorldPoint) {

        Vector2 localA = receiver.getBody().getLocalPoint(impactWorldPoint).cpy();
        Vector2 localB = traveler.getBody().getLocalPoint(impactWorldPoint.cpy().add(advance)).cpy();

        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = receiver.getBody();
        jointDef.bodyB = traveler.getBody();
        jointDef.localAnchorA.set(localA.cpy());
        jointDef.localAnchorB.set(localB.cpy());
        jointDef.collideConnected = false;
        jointDef.referenceAngle = -receiver.getBody().getAngle() + traveler.getBody().getAngle();
        addJointToCreate(jointDef, receiver, traveler);
    }


    private float getPenetrationLength(GameEntity penetrated, int topographyLength, Data[] penetratedTopography, Data[] penetratorTopography) {
        float penetrationLength = 0;
        for (int i = 0; i < topographyLength; i++) {
            Data data1 = penetratedTopography[i];
            Data data2 = penetratorTopography[i];
            if (data1 != null && data2 != null) {
                if (Arrays.asList(data1.getEntities()).contains(penetrated)) {
                    float max = data1.getMax(penetrated);
                    float min = data2.getMin();
                    float delta = max - min;

                    if (delta > penetrationLength) penetrationLength = delta;
                }
            }
        }
        return penetrationLength;
    }

    private void computeShatterImpact(Contact contact, float impulse, GameEntity entity1, GameEntity entity2) {

        Vector2[] points = contact.getWorldManifold().getPoints();
        final int numberOfContactPoints = contact.getWorldManifold().getNumberOfContactPoints();


        LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
        LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();
        Vector2 point;
        if (numberOfContactPoints == 1)
            point = points[0];
        else {
            point = points[0].add(points[1]).mul(0.5f);
        }
        if (Float.isInfinite(point.x) || Float.isInfinite(point.y)) {
            return;
        }
        if (Float.isNaN(point.x) || Float.isNaN(point.y)) {
            return;
        }

        Vector2 impactPoint1 = obtain(point);
        Vector2 impactPoint2 = obtain(point);

        checkOnePointImpact(block1, impactPoint1, impulse / 2, entity1);
        checkOnePointImpact(block2, impactPoint2, impulse / 2, entity2);
    }

    private void linearTopographicScan(Vector2 begin, Vector2 end) {
        rayCastCallback.setDirection(true);
        physicsWorld.rayCast(rayCastCallback, begin, end);
        rayCastCallback.setDirection(false);
        physicsWorld.rayCast(rayCastCallback, end, begin);
    }

    private Data findIntervalsExclusiveToOneEntity(GameEntity exclusive, Vector2 center, Vector2 direction, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExclusive(exclusive);
        linearTopographicScan(begin, end);
        return computeIntervals(center, direction, begin, end);
    }

    private Data findIntervalsExceptingOneEntity(GameEntity excepted, Vector2 center, Vector2 direction, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExcepted(excepted);
        linearTopographicScan(begin, end);
        return computeIntervals(center, direction, begin, end);
    }

    private Data computeIntervals(Vector2 center, Vector2 eVector, Vector2 begin, Vector2 end) {

        ArrayList<ArrayList<Flag>> flags = rayCastCallback.getFlags();
        ArrayList<LayerBlock> blocks = rayCastCallback.getCoveredBlocks();
        ArrayList<GameEntity> entities = rayCastCallback.getCoveredEntities();
        Data data = new Data(blocks.size(), center);

        for (ArrayList<Flag> list : flags) {
            Collections.sort(list);
            int i = flags.indexOf(list);
            LayerBlock layerBlock = blocks.get(i);
            GameEntity entity = entities.get(i);
            Body body = entity.getBody();

            float density = layerBlock.getProperties().getDensity();

            boolean firstInside = layerBlock.testPoint(body, begin.x, begin.y);
            boolean secondInside = layerBlock.testPoint(body, end.x, end.y);

            if (firstInside && secondInside) {
                //inneer cut
                float projection1 = GeometryUtils.projection(begin, center, eVector);
                float projection2 = GeometryUtils.projection(end, center, eVector);
                data.add(projection1, projection2, density, entity, layerBlock);

            } else if (firstInside) {
                //half cut
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(begin, center, eVector);
                float projection2 = GeometryUtils.projection(last.getPoint(), center, eVector);
                data.add(projection1, projection2, density, entity, layerBlock);
            } else if (secondInside) {
                //half cut
                Flag first = list.get(0);
                float projection1 = GeometryUtils.projection(first.getPoint(), center, eVector);
                float projection2 = GeometryUtils.projection(end, center, eVector);
                data.add(projection1, projection2, density, entity, layerBlock);

            } else {
                //full cut
                Flag first = list.get(0);
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(first.getPoint(), center, eVector);
                float projection2 = GeometryUtils.projection(last.getPoint(), center, eVector);
                data.add(projection1, projection2, density, entity, layerBlock);
            }
        }

        return data;
    }

    public void addGameEntityToDestroy(GameEntity entity, boolean recycle) {
        if (!entity.isAlive()) {
            return;
        }
        ArrayList<JointEdge> jointsEdges = entity.getBody().getJointList();
        for (JointEdge jointedge : jointsEdges) {
            if (jointedge.joint instanceof MouseJoint) {
                gameScene.onDestroyMouseJoint((MouseJoint) jointedge.joint);
            }
        }
        Invoker.addBodyDestructionCommand(entity);

        if (recycle) {
            for (LayerBlock layerBlock : entity.getBlocks()) {
                for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                    b.recycleSelf();
                }
                layerBlock.recycleSelf();
            }

        }

        entity.setAlive(false);
    }

    public void performFlux(Vector2 sourceWorldPoint, ImpactInterface impactInterface, GameEntity source) {
        GameScene.plotter2.detachChildren();
        Vector2 v = new Vector2(1, 0);
        List<ImpactData> list = new ArrayList<>();
        for (int i = 0; i < FLUX_PRECISION; i++) {
            v.set(1, 0);
            GeometryUtils.rotateVectorDeg(v, i * 360 / (float) FLUX_PRECISION);
            Vector2 end = new Vector2(sourceWorldPoint.x + v.x * 100, sourceWorldPoint.y + v.y * 100);
            ImpactData dataOuter = this.detectFirstIntersectionData(sourceWorldPoint, end, source);
            if (dataOuter != null) {
                list.add(dataOuter);
            }
            if (source != null) {
                ImpactData dataInner = this.detectFirstIntersectionDataInner(end, sourceWorldPoint, source);
                if (dataInner != null) {
                    list.add(dataInner);
                }
            }
        }
        Map<GameEntity, List<ImpactData>> res = list.stream().collect(Collectors.groupingBy(ImpactData::getGameEntity));
        if (impactInterface != null) {
            res.forEach(impactInterface::processImpacts);
        }
    }

    public void performScreenCut(Vector2 worldPoint1, Vector2 worldPoint2) {
        cutRayCastCallback.reset();
        cutRayCastCallback.setDirection(true);
        getPhysicsWorld().rayCast(cutRayCastCallback, worldPoint1, worldPoint2);
        cutRayCastCallback.setDirection(false);
        getPhysicsWorld().rayCast(cutRayCastCallback, worldPoint2, worldPoint1);
        ArrayList<ArrayList<Flag>> flags = cutRayCastCallback.getFlags();
        ArrayList<LayerBlock> blocks = cutRayCastCallback.getCoveredBlocks();
        ArrayList<Fixture> fixtures = cutRayCastCallback.getCoveredFixtures();
        ArrayList<ArrayList<Segment>> allSegments = new ArrayList<>();
        ArrayList<GameEntity> entities = new ArrayList<>();
        for (ArrayList<Flag> list : flags) {
            Collections.sort(list);
            int i = flags.indexOf(list);
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
            Vector2 p1;
            Vector2 p2;
            if (firstInside && secondInside) {
                p1 = body.getLocalPoint(worldPoint1);
                p2 = body.getLocalPoint(worldPoint2);
                // entityCutsList.add(new Segment(p1,p2,block,CutType.InnerCut));
            } else if (firstInside) {
                p1 = body.getLocalPoint(worldPoint1);
                p2 = list.get(list.size() - 1).getPoint();
                //entityCutsList.add(new Segment(p1,p2,block,CutType.halfCutFirstInside));
            } else if (secondInside) {
                p1 = list.get(0).getPoint();
                p2 = body.getLocalPoint(worldPoint2);
                // entityCutsList.add(new Segment(p1,p2,block,CutType.halfCutSecondInside));
            } else {
                //perform the cut
                p1 = list.get(0).getPoint();
                p2 = list.get(list.size() - 1).getPoint();
                entitySegments.add(new Segment(p1, p2, block, CutType.fullCut));
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

    public boolean applyLiquidStain(GameEntity gameEntity, float x, float y, LayerBlock concernedBlock, Color color, int index) {
        Vector2 localPosition = new Vector2(x, y);

        float angle = random.nextInt(360);

        ITextureRegion textureRegion = ResourceManager.getInstance().imageTextureRegion.getTextureRegion(index);

        StainBlock stainBlock = BlockFactory.createStainBlock(localPosition, angle, concernedBlock.getVertices(), textureRegion, color);

        if (stainBlock != null && stainBlock.isNotAborted() && !gameEntity.stainHasTwin(concernedBlock, stainBlock)) {
            stainBlock.setId(index);
            gameEntity.addStain(concernedBlock, stainBlock);
            return true;
        } else
            return false;
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
            ArrayList<Vector2> vertices = block.getVertices();

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
            gameScene.getWorldFacade().addGameEntityToDestroy(gameEntity, false);
        }

    }

    private void checkOnePointImpact(LayerBlock block, Vector2 worldPoint, float energy, GameEntity gameEntity) {
        if (gameEntity.getBody().getType() == BodyDef.BodyType.DynamicBody && energy > 3 * PhysicsConstants.TENACITY_FACTOR) {
            applyOnePointImpactToEntity(block, energy, gameEntity, worldPoint);
        }
    }

    private void applyOnePointImpactToEntity(LayerBlock block, float energy, GameEntity gameEntity, Vector2 worldPoint) {
        List<ImpactData> impactData = new ArrayList<>();
        impactData.add(new ImpactData(gameEntity, block, worldPoint, energy));
        applyImpacts(gameEntity, impactData);
    }

    public void pulverizeBlock(LayerBlock layerBlock, GameEntity gameEntity) {
        Body body = gameEntity.getBody();
        if (body == null) {
            return;
        }
        for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
            coatingBlock.setPulverized(true);
            coatingBlock.getTriangles().forEach(vector2 -> {
                Vector2 worldVector = body.getWorldPoint(vector2.mul(1 / 32f)).cpy().mul(32f);
                vector2.set(worldVector);
            });
        }
        float v = gameEntity.getBody().getLinearVelocity().len();
        Vector2 particleVelocity = v < PhysicsConstants.PARTICLE_TERMINAL_VELOCITY ? gameEntity.getBody().getLinearVelocity().cpy() : gameEntity.getBody().getLinearVelocity().cpy().nor().mul(PhysicsConstants.PARTICLE_TERMINAL_VELOCITY);
        PulverizationParticleWrapperWithPolygonEmitter pulverizationParticleWrapper = new PulverizationParticleWrapperWithPolygonEmitter(this, layerBlock, particleVelocity);
        powderParticleWrappers.add(pulverizationParticleWrapper);
        gameScene.attachChild(pulverizationParticleWrapper.getParticleSystem());
    }

    public void applyImpacts(GameEntity gameEntity, List<ImpactData> impactData) {
        BreakVisitor<GameEntity> visitor = new GameEntityMultiShatterVisitor(impactData, this);
        visitor.visitTheElement(gameEntity);
        if (visitor.isShatterPerformed()) {
            if (visitor.getSplintersBlocks().size() > 0) {
                computeSplinters(visitor.getSplintersBlocks(), gameEntity);
            }
            this.addGameEntityToDestroy(gameEntity, false);
        }
    }

    public void applyImpactHeat(float heat, List<ImpactData> impactData) {
        impactData.forEach(impact -> {
            CoatingBlock coatingCenter = impact.getImpactedBlock().getBlockGrid().getNearestCoatingBlockSimple(impact.getLocalImpactPoint());
            coatingCenter.applyDeltaTemperature(10 * heat);
        });
    }

    private void computeInternalConduction(CoatingBlock coatingBlock) {

        HashSet<CoatingBlock> neighbors = coatingBlock.getNeighbors();


        for (CoatingBlock other : neighbors) {
            float density = coatingBlock.getParent().getProperties().getDensity();
            float area1 = coatingBlock.getArea();
            float area2 = other.getArea();
            float length = (float) (area1 <= area2 ? Math.sqrt(area1) : Math.sqrt(area2));
            PhysicsUtils.transferHeatByConduction(density, density, length, coatingBlock, other, 0.1f, 0.1f, 10f, 10f);
        }
    }

    public void scheduleGameEntityToDestroy(GameEntity entity, int time) {
        TimedCommand command = new TimedCommand(time, () -> addGameEntityToDestroy(entity, true));
        timedCommands.add(command);
    }

    public GameGroup getGround() {
        return ground;
    }

    public void setGround(GameGroup ground) {
        this.ground = ground;
        this.groundModel.setGameEntity(ground.getGameEntityByIndex(0));
    }

    public void recreateJoint(JointBlock jointBlock, GameEntity splinter) {
        jointBlock.substitute(splinter);
    }

    public void addJointToCreate(JointDef jointDef, GameEntity entity1, GameEntity entity2) {
        JointCreationCommand jointCreationCommand = Invoker.addJointCreationCommand(jointDef, entity1.getParentGroup(), entity1, entity2);
        JointBlock jointBlock1 = new JointBlock();
        JointBlock jointBlock2 = new JointBlock();
        jointBlock1.setCommand(jointCreationCommand);
        jointBlock2.setCommand(jointCreationCommand);
        Vector2 anchorA = null;
        Vector2 anchorB = null;

        switch (jointDef.type) {
            case Unknown:
            case GearJoint:
            case MouseJoint:
            case LineJoint:
            case PulleyJoint:
            case DistanceJoint:
            case FrictionJoint:
                break;
            case RevoluteJoint:
                anchorA = ((RevoluteJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((RevoluteJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case PrismaticJoint:
                anchorA = ((PrismaticJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((PrismaticJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case WeldJoint:
                anchorA = ((WeldJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((WeldJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
        }

        if (anchorA != null) {
            jointBlock1.initialization(new ArrayList<>(Collections.singletonList(anchorA)), new JointProperties(jointDef), 0, JointBlock.JointZoneType.PUNCTUAL, JointBlock.Position.A);
            entity1.getBlocks().get(0).addAssociatedBlock(jointBlock1);
        }
        if (anchorB != null) {
            jointBlock2.initialization(new ArrayList<>(Collections.singletonList(anchorB)), new JointProperties(jointDef), 0, JointBlock.JointZoneType.PUNCTUAL, JointBlock.Position.B);
            entity2.getBlocks().get(0).addAssociatedBlock(jointBlock2);
        }
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
}
