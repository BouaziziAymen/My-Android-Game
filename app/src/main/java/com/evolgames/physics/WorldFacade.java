package com.evolgames.physics;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.commandtemplate.Invoker;
import com.evolgames.commandtemplate.TimedCommand;
import com.evolgames.entities.EntityWithBody;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.JointZoneBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blockvisitors.ClipVisitor;
import com.evolgames.entities.blockvisitors.ShatterVisitor;
import com.evolgames.entities.contact.ContactObserver;
import com.evolgames.entities.contact.GameEntityContactListener;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.CutType;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.Segment;

import com.evolgames.entities.joint.JointKey;
import com.evolgames.entities.particles.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.particles.LiquidParticleWrapper;
import com.evolgames.entities.particles.SegmentLiquidParticleWrapper;
import com.evolgames.entities.particles.Spark;
import com.evolgames.entities.properties.JointZoneProperties;
import com.evolgames.factories.BlockFactory;
import com.evolgames.factories.GameEntityFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.ElementCouple;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.PhysicsUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.physics.entities.Data;
import com.evolgames.physics.entities.JointBlueprint;
import com.evolgames.physics.entities.Touch;
import com.evolgames.physics.entities.callbacks.BlockQueryCallBack;
import com.evolgames.physics.entities.callbacks.CutRayCastCallback;
import com.evolgames.physics.entities.callbacks.DetectionRayCastCallback;
import com.evolgames.physics.entities.callbacks.GameEntityQueryCallBack;
import com.evolgames.physics.entities.callbacks.SimpleDetectionRayCastCallback;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.behaviors.actions.Action;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;

public class WorldFacade implements ContactObserver {
    private static ClipVisitor clipVisitor = new ClipVisitor();
    private static HashSet<LiquidParticleWrapper> liquidParticleWrappers = new HashSet<>();
    private static ArrayList<ParticleSystem<?>> flames = new ArrayList<>();
    private static SimpleDetectionRayCastCallback simpleDetectionRayCastCallback = new SimpleDetectionRayCastCallback();
    private static ArrayList<TimedCommand> timedCommands = new ArrayList<>();
    private final int precision = 50;
    private final float backoff = 0.1f;
    int i = 0;
    Random rand = new Random();
    private GameEntityQueryCallBack queryCallBack = new GameEntityQueryCallBack();
    private BlockQueryCallBack blockQueryCallBack = new BlockQueryCallBack();
    private LinearRayCastCallback rayCastCallback = new LinearRayCastCallback();
    private DetectionRayCastCallback detectionRayCastCallback = new DetectionRayCastCallback();
    private PhysicsWorld physicsWorld;
    private GameScene gameScene;
    private ArrayList<Pair<GameEntity, GameEntity>> goThroughEntities = new ArrayList<>();
    private CutRayCastCallback cutRayCastCallback = new CutRayCastCallback();
    private HashSet<Touch> touches = new HashSet<>();
    private ArrayList<JointBlueprint> jointBlueprints = new ArrayList<>();
    private GameGroup ground;

    public WorldFacade(GameScene scene) {
        gameScene = scene;
        physicsWorld = new PhysicsWorld(obtain(0, PhysicsConstants.gravity), false);
        physicsWorld.setContactListener(new GameEntityContactListener(this));

        scene.registerUpdateHandler(physicsWorld);
        physicsWorld.setContinuousPhysics(true);
        physicsWorld.setAutoClearForces(true);
        //physicsWorld.setWarmStarting(false);
        physicsWorld.setPositionIterations(3);
        physicsWorld.setVelocityIterations(8);
    }

    public static ClipVisitor getClipVisitor() {
        return clipVisitor;
    }

    public static void removeLiquidSource(LiquidParticleWrapper particleWrapper) {

        if (particleWrapper != null)
            if (particleWrapper.getParticleSystem() != null) {
                particleWrapper.getParticleSystem().detachSelf();
                liquidParticleWrappers.remove(particleWrapper);
                GameScene.numberOfLiquid--;
                //   Log.e("freshcuts","removed");
            }


    }

    public static void removeFireParticleWrapper(FireParticleWrapperWithPolygonEmitter fireParticleWrapper) {
        fireParticleWrapper.getParticleSystem().detachSelf();
        flames.remove(fireParticleWrapper.getParticleSystem());
    }

    public SimpleDetectionRayCastCallback getSimpleDetectionRayCastCallback() {
        return simpleDetectionRayCastCallback;
    }

    public Vector2 detect(Vector2 first, Vector2 second) {
        simpleDetectionRayCastCallback.reset();
        physicsWorld.rayCast(simpleDetectionRayCastCallback, first, second);
        return simpleDetectionRayCastCallback.getIntersectionPoint();
    }

    public void addJointBlueprint(JointBlueprint jointBlueprint) {
        jointBlueprints.add(jointBlueprint);
    }

    private void addFlame(FireParticleWrapperWithPolygonEmitter fireParticleWrapper) {
        ParticleSystem fire = fireParticleWrapper.getParticleSystem();

        flames.add(fire);

    }

    public LiquidParticleWrapper createSegmentLiquidSource(GameEntity parentEntity, final Vector2 first, final Vector2 second, Color color, int lowerRate, int higherRate) {

        LiquidParticleWrapper liquidSource = new SegmentLiquidParticleWrapper(color, first, second, lowerRate, higherRate);
        liquidParticleWrappers.add(liquidSource);
        liquidSource.getParticleSystem().setZIndex(5);
        gameScene.attachChild(liquidSource.getParticleSystem());
        gameScene.sortChildren();
        liquidSource.setParent(parentEntity);
        return liquidSource;
    }

    public HashSet<GameEntity> getEntitiesInZone(float rx, float ry, float halfWidth, float halfHeight) {
        queryCallBack.reset();
        physicsWorld.QueryAABB(queryCallBack, rx - halfWidth, ry - halfHeight, rx + halfWidth, ry + halfHeight);
        return queryCallBack.getEntities();
    }

    public Contact getConcernedContact(Touch touch) {
        for (Contact contact : physicsWorld.getContactList()) {
            if (!contact.isTouching()) continue;
            BlockA block1 = (BlockA) contact.getFixtureA().getUserData();
            BlockA block2 = (BlockA) contact.getFixtureB().getUserData();
            if (touch.isEquivalent(block1, block2)) return contact;
        }
        return null;
    }

    public void onStep(float deltaTime) {
        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity entity : gameGroup.getGameEntities()) {
                if (entity != null)
                    entity.test_draw();
            }
        for (TimedCommand timedCommand : timedCommands) timedCommand.update();

        ArrayList<JointBlueprint> createdBluePrints = new ArrayList<>();
        Iterator<Joint> iterator = physicsWorld.getJoints();
        if (true)
            while (iterator.hasNext()) {
                Joint j = iterator.next();
                Iterator<JointBlueprint> iter = jointBlueprints.iterator();
                while (iter.hasNext()) {
                    JointBlueprint jj = iter.next();
                    if (jj.getJoint() == j && jj.isValid()) {
                        if (jj.getEntity2().getHanged()) {
                            if (j instanceof WeldJoint) {
                                Vector2 r = j.getReactionForce(60f);
                                if (r.len() > 100000) {
                                    addJointToDestroy(jj);
                                    iter.remove();
                                    WeldJointDef weldJointDef = (WeldJointDef) jj.getJointDef();
                                    PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
                                    prismaticJointDef.collideConnected = false;
                                    prismaticJointDef.referenceAngle = weldJointDef.referenceAngle;
                                    prismaticJointDef.localAnchorA.set(weldJointDef.localAnchorA.cpy());
                                    prismaticJointDef.localAnchorB.set(weldJointDef.localAnchorB.cpy());
                                    if (jj.getAdvance() != null) {
                                        prismaticJointDef.localAxis1.set(jj.getAdvance().cpy().nor());
                                        prismaticJointDef.enableLimit = true;
                                        prismaticJointDef.upperTranslation = jj.getAdvance().len() + 0.01f;

                                        JointBlueprint prismaticJointBlueprint = new JointBlueprint(prismaticJointDef, jj.getEntity1(), jj.getEntity2(), false);
                                        createdBluePrints.add(prismaticJointBlueprint);

                                    }
                                }
                            } else if (j instanceof PrismaticJoint) {
                                PrismaticJoint prismaticJoint = (PrismaticJoint) j;
                                float upperLimit = prismaticJoint.getUpperLimit();
                                if (prismaticJoint.getJointTranslation() >= upperLimit && !jj.isDead()) {
                                    addJointToDestroy(jj);
                                    jj.setDead(true);
                                }
                            }
                        }
                    }
                }

            }
        createdBluePrints.stream().parallel().forEach(this::addJointBlueprint);
        jointBlueprints.stream().parallel().forEach(
                jbp -> {
                    jbp.update();
                    if (!jbp.isCreated() && jbp.isValid()) {
                        Invoker.addJointCreationCommand(jbp);
                        jbp.setCreated(true);
                    }
                }
        );


        if (false)
            for (Touch touch : touches) {
                Contact contact = getConcernedContact(touch);
                if (contact == null) continue;
                if (contact.getWorldManifold().getNumberOfContactPoints() == 2) {
                    Vector2 first = contact.getWorldManifold().getPoints()[0];
                    Vector2 second = contact.getWorldManifold().getPoints()[1];
                    GameScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
                    GameScene.plotter.drawPoint(second.cpy().mul(32), Color.RED, 1f, 0);
                    //GameScene.plotter.drawLine2(first.cpy().mul(32), second.cpy().mul(32), Color.RED, 2);
                } else {
                    Vector2 first = contact.getWorldManifold().getPoints()[0];
                    GameScene.plotter.drawPoint(first.cpy().mul(32), Color.RED, 1f, 0);
                }
            }

        //Internal conduction


        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (!gameEntity.getName().equals("Ground")) {
                    for (BlockA block : gameEntity.getBlocks()) {
                        for (CoatingBlock coatingBlock : block.getBlockGrid().getCoatingBlocks())
                            computeInternalConduction(coatingBlock);
                    }
                }
            }


        //convection
        if (true) {
            Collections.shuffle(flames);
            for (ParticleSystem<?> fire : flames) {
                Particle<?>[] particles = fire.getParticles();
                for (Particle<?> p : particles) {
                    if (p == null || p.isExpired()) continue;

                    Spark spark = (Spark) p.getEntity();

                    float x = spark.getX();
                    float y = spark.getY();
                    float w = 3;
                    float h = 3;
                    // GameScene.plotter.drawPoint(new Vector2(x,y),Color.CYAN,1,0);

                    Vector2 worldPosition = new Vector2(x / 32f, y / 32f);
                    Vector2 lower = new Vector2(x - w / 2f, y - h / 2f).mul(1 / 32f);
                    Vector2 upper = new Vector2(x + w / 2f, y + h / 2f).mul(1 / 32f);
                    blockQueryCallBack.reset();
                    physicsWorld.QueryAABB(blockQueryCallBack, lower.x, lower.y, upper.x, upper.y);
                    HashSet<BlockA> list = blockQueryCallBack.getBlocks();
                    if (list.size() == 0) continue;

                    for (BlockA block : list) {
                        if (block.getFixtures().size() == 0) continue;
                        Body body = block.getBody();

                        if (body == null) continue;
                        Vector2 localPosition = obtain(body.getLocalPoint(worldPosition)).mul(32);
                        CoatingBlock nearestCoatingBlock = block.getBlockGrid().getNearestCoatingBlockSimple(localPosition);
                        recycle(localPosition);

                        if (nearestCoatingBlock != null) {
                            double sparkTemperature = (double) spark.getUserData();

                            PhysicsUtils.transferHeatByConvection(0.05f, 1f, 1f, sparkTemperature, nearestCoatingBlock);
                        }
                        // Vector2 grainWorldPosition = body.getWorldPoint(nearestCoatingBlock.position.cpy().mul(1/32f)).cpy().mul(32f);
                        //  GameScene.plotter.drawPoint(grainWorldPosition,Color.RED,1,0);

                    }
                }
            }
        }


        if (true)
            for (GameGroup gameGroup : gameScene.getGameGroups())
                for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                    if (gameEntity.getName().equals("Ground")) continue;
                    for (BlockA block : gameEntity.getBlocks()) {
                        for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                            PhysicsUtils.transferHeatByConvection(0.0001f, 100f, 0.00125f, PhysicsConstants.ambient_temperature, grain);
                        }
                    }
                }


        //Update grains
        if (true)
            for (GameGroup gameGroup : gameScene.getGameGroups())
                for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                    if (gameEntity.getName().equals("Ground")) continue;
                    for (BlockA block : gameEntity.getBlocks()) {
                        for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                            grain.update();
                        }
                    }
                }


        if (true)
            for (GameGroup gameGroup : gameScene.getGameGroups())
                for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                    if (!gameEntity.isAlive() || gameEntity.getName().equals("Ground")) continue;
                    for (BlockA block : gameEntity.getBlocks()) {
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

        //Liquid staining
        Iterator<LiquidParticleWrapper> liquidParticleIterator = liquidParticleWrappers.iterator();
        while (liquidParticleIterator.hasNext()) {
            LiquidParticleWrapper liquidParticleWrapper = liquidParticleIterator.next();
            liquidParticleWrapper.update();
            if (!liquidParticleWrapper.isAlive() && liquidParticleWrapper.isAllParticlesExpired()) {
                liquidParticleWrapper.recycleParticles();
                liquidParticleWrapper.getParticleSystem().detachSelf();
                liquidParticleIterator.remove();
            }
        }
        HashSet<GameEntity> affectedEntities = new HashSet<>();

        for (LiquidParticleWrapper liquidSource : liquidParticleWrappers)
            if (liquidSource.getParticleSystem().hasParent())
                if (liquidSource.getParticleSystem().getParticles().length > 0) {
                    for (Particle<?> p : liquidSource.getParticleSystem().getParticles()) {
                        if (p != null && !p.isExpired() && Math.random() < 0.05f) {
                            Vector2 position = new Vector2(p.getEntity().getX(), p.getEntity().getY());
                            float halfWidth = p.getEntity().getWidth() / 32f / 2f;
                            float halfHeight = p.getEntity().getHeight() / 32f / 2f;
                            //find concerned entities

                            float rx = position.x / 32f;
                            float ry = position.y / 32f;
                            HashSet<GameEntity> entities = getEntitiesInZone(rx, ry, halfWidth, halfHeight);
                            Vector2 rPosition = new Vector2(rx, ry);
                            boolean onBoundaries = false;

                            for (GameEntity entity : entities)
                                if (entity.getBody() != null)
                                    for (BlockA block : entity.getBlocks())
                                        if (block.testPoint(entity.getBody(), rx, ry)) {
                                            onBoundaries = true;
                                            break;
                                        }

                            boolean dead = false;
                            if (onBoundaries) {
                                int index = rand.nextInt(14);
                                for (GameEntity entity : entities) {
                                    if (entity.getBody() != null) {
                                        boolean affected = false;
                                        Body body = entity.getBody();
                                        Vector2 localPoint = obtain(body.getLocalPoint(rPosition)).mul(32);
                                        ArrayList<BlockA> blocks = entity.getBlocks();
                                        for (int i = blocks.size() - 1; i >= 0; i--) {
                                            BlockA block = blocks.get(i);
                                            boolean applied = applyLiquidStain(entity, localPoint.x, localPoint.y, block, liquidSource.getColor(), index);
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
        for (GameEntity entity : affectedEntities) entity.redrawStains();

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
        if (entity1 == null || entity2 == null) return;
        float[] normalImpulses = impulse.getNormalImpulses();
        float[] tangentImpulses = impulse.getTangentImpulses();
        float normalImpulse = normalImpulses[0];
        float impulseValue = (float) Math.sqrt(normalImpulses[0] * normalImpulses[0] + tangentImpulses[0] * tangentImpulses[0]);

        if (normalImpulse < 10) return;

        if (!entity1.isAlive() || !entity2.isAlive()) return;

        if (true)
            computeShatterImpact(contact, impulseValue, entity1, entity2);


    }

    @Override
    public void processImpactBeginContact(Contact contact) {


        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        GameEntity entity1 = (GameEntity) body1.getUserData();
        GameEntity entity2 = (GameEntity) body2.getUserData();
        BlockA block1 = (BlockA) contact.getFixtureA().getUserData();
        BlockA block2 = (BlockA) contact.getFixtureB().getUserData();


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

        if (entity1 == null || entity2 == null) return;
        if (!entity1.isAlive() || !entity2.isAlive()) return;

        if (true) computePenetrationImpact(contact, entity1, entity2);

    }

    @Override
    public void processImpactEndContact(Contact contact) {

        Iterator<Touch> touchIterator = touches.iterator();
        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        BlockA block1 = (BlockA) fixture1.getUserData();
        BlockA block2 = (BlockA) fixture2.getUserData();
        while (touchIterator.hasNext()) {
            Touch touch = touchIterator.next();
            if (touch.isEquivalent(block1, block2)) touchIterator.remove();
        }


    }

    @Override
    public void processImpactBeforeSolve(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        GameEntity entity1 = (GameEntity) body1.getUserData();
        GameEntity entity2 = (GameEntity) body2.getUserData();
        if (true)
            for (Pair<GameEntity, GameEntity> pair : goThroughEntities) {
                if (pair.first == entity1 && pair.second == entity2) contact.setEnabled(false);
                else if (pair.first == entity2 && pair.second == entity1) contact.setEnabled(false);
            }
    }

    private void computePenetrationImpact(Contact contact, GameEntity entity1, GameEntity entity2) {
        //if(true)return;
        if (!entity1.isProjectile() && !entity2.isProjectile()) return;
        Vector2[] points = contact.getWorldManifold().getPoints();
        final int numberOfContactPoints = contact.getWorldManifold().getNumberOfContactPoints();

        Vector2 point;
        if (numberOfContactPoints == 1)
            point = points[0];
        else
            point = points[0].add(points[1]).mul(0.5f);

        BlockA block1 = (BlockA) contact.getFixtureA().getUserData();
        BlockA block2 = (BlockA) contact.getFixtureB().getUserData();
        Vector2 V1 = entity1.getBody().getLinearVelocity();
        Vector2 V2 = entity2.getBody().getLinearVelocity();
        Vector2 normal = obtain(V1.x - V2.x, V1.y - V2.y).nor();
        Vector2 tangent = obtain(-normal.y, normal.x);
        Vector2 V1Useful = obtain(normal).mul(V1.dot(normal));
        Vector2 V2Useful = obtain(normal).mul(V2.dot(normal));
        float m1 = entity1.getBody().getMass();
        float m2 = entity2.getBody().getMass();
        float energy = (float) (0.5 * (m1 * V1Useful.dot(V1Useful) + m2 * V2Useful.dot(V2Useful)));
        recycle(V1Useful);
        recycle(V2Useful);
        Log.e("penetration", "PENETRATION:" + energy);
        if (energy < 500) return;

        Vector2 normallBackoff = obtain(normal).mul(backoff);
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity1);
        physicsWorld.rayCast(detectionRayCastCallback, point.cpy().sub(normallBackoff), point.cpy().add(normal));
        float f1 = detectionRayCastCallback.getFraction();
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity1);
        physicsWorld.rayCast(detectionRayCastCallback, point.cpy().add(normallBackoff), point.cpy().sub(normal));
        float f2 = detectionRayCastCallback.getFraction();
        recycle(normallBackoff);

        boolean normalPointsToEntity1 = false;
        boolean normalPointsToEntity2 = false;
        if (f1 < f2) {
            //normal points to entity1
            normalPointsToEntity1 = true;
        } else {
            normalPointsToEntity2 = true;
        }
        //GameScene.plotter.drawVector(point.cpy().mul(32),normal.cpy().mul(32),Color.RED);


        float[] bounds = BlockUtils.getBounds(entity1.getBlocks(), entity1.getBody(), point, tangent, normal);
        float infX1 = bounds[0];
        float infY1 = bounds[1];
        float supX1 = bounds[2];
        float supY1 = bounds[3];

        bounds = BlockUtils.getBounds(entity2.getBlocks(), entity2.getBody(), point, tangent, normal);
        float infX2 = bounds[0];
        float infY2 = bounds[1];
        float supX2 = bounds[2];
        float supY2 = bounds[3];

//DETERMINE PENETRATOR VS PENETRATED
        //IF NORMAL POINTS TO ENTITY2 THEN TAKE THE SUP ELSE TAKE INF
//IF NORMAL POINTS TO ENTITY1 THEN TAKE THE SUP ELSE TAKE INF
        Log.e("penetration", "flag4");
        GameEntity penetrator = entity1.isProjectile() ? entity1 : entity2;
        GameEntity penetrated = penetrator == entity1 ? entity2 : entity1;

        Vector2 traversalCenter1, traversalCenter2;
        Vector2 detectionDirection;
        Vector2 oppPoint1, oppPoint2;
//-----------------------------
        //  GameScene.pause = true;      //  GameScene.plotter2.drawPoint(point.cpy().mul(32f),Color.RED,1f,0);

        if (penetrator == entity1) {
            if (normalPointsToEntity1) {
                traversalCenter1 = obtain(point).add(normal.x * infY1, normal.y * infY1);
                traversalCenter2 = obtain(point).add(normal.x * supY2, normal.y * supY2);
                oppPoint1 = obtain(point).add(normal.x * supY1, normal.y * supY1);
                oppPoint2 = obtain(point).add(normal.x * infY2, normal.y * infY2);
                detectionDirection = obtain(normal);

            } else {
                traversalCenter1 = obtain(point).add(normal.x * supY1, normal.y * supY1);
                traversalCenter2 = obtain(point).add(normal.x * infY2, normal.y * infY2);
                oppPoint1 = obtain(point).add(normal.x * infY1, normal.y * infY1);
                oppPoint2 = obtain(point).add(normal.x * supY2, normal.y * supY2);
                detectionDirection = obtain(normal).mul(-1);
            }
        } else {
            if (normalPointsToEntity2) {
                traversalCenter1 = obtain(point).add(normal.x * infY2, normal.y * infY2);
                traversalCenter2 = obtain(point).add(normal.x * supY1, normal.y * supY1);
                oppPoint1 = obtain(point).add(normal.x * supY2, normal.y * supY2);
                oppPoint2 = obtain(point).add(normal.x * infY1, normal.y * infY1);
                detectionDirection = obtain(normal);
            } else {
                traversalCenter1 = obtain(point).add(normal.x * supY2, normal.y * supY2);
                traversalCenter2 = obtain(point).add(normal.x * infY1, normal.y * infY1);
                oppPoint1 = obtain(point).add(normal.x * infY2, normal.y * infY2);
                oppPoint2 = obtain(point).add(normal.x * supY1, normal.y * supY1);
                detectionDirection = obtain(normal).mul(-1);
            }

        }

        //GameScene.plotter2.drawVector(point.cpy().mul(32f),detectionDirection.cpy().mul(32f),Color.YELLOW);
        float infX = Math.max(infX1, infX2);
        float infY = Math.min(infY1, infY2);
        float supX = Math.min(supX1, supX2);
        float supY = Math.max(supY1, supY2);
//find place to start looking for edge


        Vector2 midPoint1 = obtain(traversalCenter1).add(-detectionDirection.x * backoff, -detectionDirection.y * backoff);
        Vector2 midPoint2 = obtain(traversalCenter2).add(detectionDirection.x * backoff, detectionDirection.y * backoff);
        recycle(traversalCenter1);
        recycle(traversalCenter2);

        float dT = Math.abs(supX - infX) / precision;


        int n;
        n = 0;
        while (n * dT < supX) {
            n++;
        }
        int m;
        m = 0;
        while (-m * dT > infX) {
            m++;
        }
        Log.e("penetration", infX + " inf " + supX + " sup");

        int counter = 0;
        Vector2 midPointM1 = obtain();
        Vector2 oppPointM1 = obtain();
        Vector2 midPointM2 = obtain();
        Vector2 oppPointM2 = obtain();

        int limit1 = Math.max(m, n);
        int limit2 = Math.min(m, n);
        float sign = (m >= n) ? -1 : 1;
        Log.e("penetration", m + "/" + n);
        final int p = m + n + 1;
        Data[] topography1 = new Data[p];
        Data[] topography2 = new Data[p];


        int index = 0;
        while (counter <= limit1) {
            //Log.e("data2","-------------------------------------------------");
            Data data1, data2;
            Vector2 intersection1, intersection2;
            oppPointM1.set(oppPoint1.x + counter * tangent.x * sign * dT, oppPoint1.y + counter * tangent.y * sign * dT);
            midPointM1.set(midPoint1.x + counter * tangent.x * sign * dT, midPoint1.y + counter * tangent.y * sign * dT);
            oppPointM2.set(oppPoint2.x + counter * tangent.x * sign * dT, oppPoint2.y + counter * tangent.y * sign * dT);
            midPointM2.set(midPoint2.x + counter * tangent.x * sign * dT, midPoint2.y + counter * tangent.y * sign * dT);

            detectionRayCastCallback.setEntity(penetrator);
            detectionRayCastCallback.reset();
            physicsWorld.rayCast(detectionRayCastCallback, midPointM1, oppPointM1);
            intersection1 = detectionRayCastCallback.getIntersectionPoint();
            if (intersection1 != null) {
                Vector2 begin = intersection1.cpy().add(backoff * detectionDirection.x, backoff * detectionDirection.y);
                Vector2 end = intersection1.cpy().add(detectionDirection.cpy().mul(-supY + infY));
                data1 = findIntervalsExceptingOneEntity(penetrator, intersection1, detectionDirection.cpy().mul(-1), begin, end);
                topography1[index] = data1;
            }
            if (intersection1 != null) {
                detectionRayCastCallback.setEntity(penetrated);
                detectionRayCastCallback.reset();
                physicsWorld.rayCast(detectionRayCastCallback, intersection1, oppPointM2);
                intersection2 = detectionRayCastCallback.getIntersectionPoint();
                if (intersection2 != null) {
                    Vector2 begin = intersection1.cpy().add(-backoff * detectionDirection.x, -backoff * detectionDirection.y);
                    Vector2 end = intersection1.cpy().add(detectionDirection.cpy().mul(supY - infY));
                    data2 = findIntervalsExclusiveToOneEntity(penetrator, intersection1, detectionDirection.cpy().mul(-1), begin, end);
                    topography2[index] = data2;
                }
            }
            if (counter >= 0) counter++;
            if (counter <= limit2) counter *= -1;
            index++;
        }

        recycle(midPoint1);
        recycle(oppPoint1);
        recycle(midPoint2);
        recycle(oppPoint2);
        recycle(midPointM1);
        recycle(oppPointM1);
        recycle(midPointM2);
        recycle(oppPointM2);
        float dN = 0;
        for (int i = 0; i < p; i++) {
            Data data1 = topography1[i];
            Data data2 = topography2[i];
            if (data1 != null && data2 != null) {
                if (Arrays.asList(data1.getEntities()).contains(penetrated)) {
                    float max = data1.getMax(penetrated);
                    float min = data2.getMin();
                    float delta = max - min;
                    //                  Log.e("data", ":" + max + "/" + min+"/"+delta);
                    if (delta > dN) dN = delta;
                }
            }
        }


        float da = dN / precision;

        Log.e("penetration", "flag5");
        int step = 0;
        while (step < precision) {

            float consumedEnergy = 0;
            final float advance = (step + 1) * da;

            for (index = 0; index < topography1.length; index++) {
                Data data1 = topography1[index];
                Data data2 = topography2[index];
                if (data1 != null && data2 != null) {
                    float dE = data1.getEnergyForAdvance(advance, dT);
                    consumedEnergy += dE;
                }
            }
            //Log.e("projectileinfo","step "+step+"/ consumed:"+consumedEnergy);

            //test penetration break if zero energy
            if (energy - consumedEnergy < 0) {
                if (step >= precision / 10) {
                    //find overlapping entities with penetrator

                    contact.setEnabled(false);
                    penetrator.getBody().setLinearVelocity(new Vector2());
                    penetrator.getBody().setAngularVelocity(0);
                    penetrated.getBody().applyLinearImpulse(detectionDirection.cpy().mul(-energy / 100), point);
                    //addGameEntityToDestroy(penetrator, 1200);
                    HashSet<JointZoneBlock> zones = findWeldZones(penetrator, penetrated, detectionDirection.cpy().mul(-step * da));
                    mergeEntities(penetrated, penetrator, detectionDirection.cpy().mul(step * da), point, zones);

                    // for (Fixture f : penetrator.getBody().getFixtureList()) {
                    //   f.setSensor(true);

                    // }
                }
                //move to compute shatter
                return;
            }
            step++;
        }

//go through
        Pair<GameEntity, GameEntity> pair = new Pair<>(penetrator, penetrated);
        goThroughEntities.add(pair);
        BlockA penetratedBlock = penetrated.getBlocks().contains(block1) ? block1 : block2;
        //applyImpact(penetratedBlock,point,energy/5000,penetrated);
        //penetrated.getBody().applyLinearImpulse(tangent.cpy().mul(5),point);
        Log.e("gothrough", "go through");


    }

    public void mergeEntities(GameEntity receiver, GameEntity traveler, Vector2 advance, Vector2 impactWorldPoint, HashSet<JointZoneBlock> zones) {

        Vector2 localA = receiver.getBody().getLocalPoint(impactWorldPoint).cpy();
        Vector2 localB = traveler.getBody().getLocalPoint(impactWorldPoint.cpy().add(advance)).cpy();

        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = receiver.getBody();
        jointDef.bodyB = traveler.getBody();
        jointDef.localAnchorA.set(localA.cpy());
        jointDef.localAnchorB.set(localB.cpy());
        jointDef.collideConnected = false;
        jointDef.referenceAngle = -receiver.getBody().getAngle() + traveler.getBody().getAngle();
        traveler.getBody().getFixtureList().forEach(f -> {
            Filter filter = new Filter();
            filter.maskBits = 0;
            f.setFilterData(filter);
        });
        JointBlueprint blueprint = addJointToCreate(jointDef, receiver, traveler, false);
        blueprint.setAdvance(advance);
        for (JointZoneBlock jointZoneBlock : zones) {
            jointZoneBlock.setJointDef(jointDef);
            jointZoneBlock.setJointId(blueprint.getId());
            jointZoneBlock.setAdvance(advance);
        }

        receiver.getMesh().setZIndex(1);
        traveler.getMesh().setZIndex(0);
        gameScene.sortChildren();


    }

    public HashSet<JointZoneBlock> findWeldZones(GameEntity penetrator, GameEntity penetrated, Vector2 advanceVector) {


        HashSet<JointZoneBlock> result = new HashSet<>();
        Vector2 position1 = penetrator.getBody().getPosition();
        Vector2 position2 = penetrated.getBody().getPosition();
        float angle1 = penetrator.getBody().getAngle();
        float angle2 = penetrated.getBody().getAngle();

        ArrayList<ArrayList<ArrayList<Vector2>>> penetratorInterpolatedLists = new ArrayList<>();
        for (int i = 0; i < penetrator.getBlocks().size(); i++) {
            ArrayList<ArrayList<Vector2>> bigList = new ArrayList<>();
            ArrayList<Vector2> penetratorBlockVertices = penetrator.getBlocks().get(i).getVertices();
            ArrayList<Vector2> interpolated = GameEntityTransformation.interpolate(position1.cpy().add(advanceVector), angle1, position2, angle2, penetratorBlockVertices);
            for (Vector2 v : interpolated) v.mul(32f);
            bigList.add(interpolated);
            for (int j = 0; j < penetrated.getBlocks().size() - 1; j++) {
                ArrayList<Vector2> copy = new ArrayList<>();
                for (int k = 0; k < penetratorBlockVertices.size(); k++)
                    copy.add(penetratorBlockVertices.get(k).cpy());
                bigList.add(copy);
            }
            penetratorInterpolatedLists.add(bigList);
        }

        for (int i = 0; i < penetratorInterpolatedLists.size(); i++) {
            ArrayList<ArrayList<Vector2>> bigList = penetratorInterpolatedLists.get(i);
            //  GameScene.plotter.drawPolygon(bigList.get(0),Color.RED);
            for (int j = 0; j < penetrated.getBlocks().size(); j++) {
                ArrayList<Vector2> path = penetrated.getBlocks().get(j).getVertices();
                //    if(i==0)     GameScene.plotter.drawPolygon(path,Color.GREEN);
                ArrayList<Vector2> overlapZone1 = BlockUtils.applyClip(bigList.get(j), path);
                if (overlapZone1 != null) {
                    //   GameScene.plotter.drawPolygon(overlapZone1,Color.BLUE);
                    //project back to penetrator
                    ArrayList<Vector2> overlapZone2 = GameEntityTransformation.interpolate(position2, angle2, position1.cpy().add(advanceVector), angle1, overlapZone1);
                    for (Vector2 v : overlapZone2) v.mul(32f);
                    JointZoneBlock zone1 = new JointZoneBlock(new GameEntityTransformation(position1, position2, angle1, angle2));
                    JointZoneBlock zone2 = new JointZoneBlock(new GameEntityTransformation(position1, position2, angle1, angle2));
                    zone1.initialization(overlapZone1, new JointZoneProperties(), -1, true);
                    zone2.initialization(overlapZone2, new JointZoneProperties(), -1, true);
                    penetrated.getBlocks().get(j).addAssociatedBlock(zone1);
                    penetrator.getBlocks().get(i).addAssociatedBlock(zone2);
                    zone1.setJointKeyType(JointKey.KeyType.A);
                    zone2.setJointKeyType(JointKey.KeyType.B);
                    zone1.setTwin(zone2);
                    zone2.setTwin(zone1);
                    result.add(zone1);
                    result.add(zone2);

                    //  GameScene.plotter.drawPolygon(overlapZone2,Color.YELLOW);
                }
            }
        }
        return result;
    }

    public HashSet<ArrayList<Vector2>> findOverlap(GameEntity penetrator, GameEntity penetrated) {
        HashSet<ArrayList<Vector2>> result = new HashSet<>();
        ArrayList<ArrayList<Vector2>> penetratorInterpolatedLists = new ArrayList<>();
        for (int i = 0; i < penetrator.getBlocks().size(); i++) {
            ArrayList<Vector2> penetratorBlockVertices = new ArrayList<>();
            ArrayList<Vector2> vertices = penetrator.getBlocks().get(i).getBodyVertices();
            for (Vector2 v : vertices) {
                penetratorBlockVertices.add(penetrator.getBody().getWorldPoint(v).cpy().mul(32f));
            }
            penetratorInterpolatedLists.add(penetratorBlockVertices);
        }

        ArrayList<ArrayList<Vector2>> penetratedInterpolatedLists = new ArrayList<>();
        for (int i = 0; i < penetrated.getBlocks().size(); i++) {
            ArrayList<Vector2> penetratedInterpolatedVertices = new ArrayList<>();
            ArrayList<Vector2> vertices = penetrated.getBlocks().get(i).getBodyVertices();
            for (Vector2 v : vertices) {
                penetratedInterpolatedVertices.add(penetrated.getBody().getWorldPoint(v).cpy().mul(32f));
            }
            penetratedInterpolatedLists.add(penetratedInterpolatedVertices);
        }

        for (int i = 0; i < penetrated.getBlocks().size(); i++) {
            ArrayList<Vector2> list1 = penetratedInterpolatedLists.get(i);

            for (int j = 0; j < penetrator.getBlocks().size(); j++) {
                ArrayList<Vector2> list2 = penetratorInterpolatedLists.get(i);
                ArrayList<Vector2> clipped = BlockUtils.applyClip(list1, list2);
                if (clipped != null) {
                    result.add(clipped);
                }

            }
        }


        return result;
    }

    private void computeShatterImpact(Contact contact, float impulse, GameEntity entity1, GameEntity entity2) {

        Vector2[] points = contact.getWorldManifold().getPoints();
        final int numberOfContactPoints = contact.getWorldManifold().getNumberOfContactPoints();


        BlockA block1 = (BlockA) contact.getFixtureA().getUserData();
        BlockA block2 = (BlockA) contact.getFixtureB().getUserData();
        Vector2 point;
        if (numberOfContactPoints == 1)
            point = points[0];
        else {
            point = points[0].add(points[1]).mul(0.5f);
        }
        if (Float.isInfinite(point.x) || Float.isInfinite(point.y)) return;
        if (Float.isNaN(point.x) || Float.isNaN(point.y)) return;

        Vector2 impactPoint1 = obtain(point);
        Vector2 impactPoint2 = obtain(point);

        applyImpact(block1, impactPoint1, impulse / 2, entity1);
        applyImpact(block2, impactPoint2, impulse / 2, entity2);
    }

    private void linearTopographicScan(Vector2 begin, Vector2 end) {
        rayCastCallback.setDirection(true);
        physicsWorld.rayCast(rayCastCallback, begin, end);
        rayCastCallback.setDirection(false);
        physicsWorld.rayCast(rayCastCallback, end, begin);
    }

    private Data findIntervalsExclusiveToOneEntity(GameEntity exclusive, Vector2 center, Vector2 eVector, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExclusive(exclusive);
        linearTopographicScan(begin, end);
        return computeIntervals(center, eVector, begin, end);
    }

    private Data findIntervalsExceptingOneEntity(GameEntity excepted, Vector2 center, Vector2 eVector, Vector2 begin, Vector2 end) {
        rayCastCallback.reset();
        rayCastCallback.setExcepted(excepted);
        linearTopographicScan(begin, end);
        return computeIntervals(center, eVector, begin, end);
    }

    private Data computeIntervals(Vector2 center, Vector2 eVector, Vector2 begin, Vector2 end) {

        ArrayList<ArrayList<Flag>> flags = rayCastCallback.getFlags();
        ArrayList<BlockA> blocks = rayCastCallback.getCoveredBlocks();
        ArrayList<GameEntity> entities = rayCastCallback.getCoveredEntities();
        Data data = new Data(blocks.size());

        for (ArrayList<Flag> list : flags) {

            Collections.sort(list);
            int i = flags.indexOf(list);
            BlockA blockA = blocks.get(i);
            GameEntity entity = entities.get(i);
            Body body = entity.getBody();

            float density = blockA.getProperties().getDensity();

            boolean firstInside = blockA.testPoint(body, begin.x, begin.y);
            boolean secondInside = blockA.testPoint(body, end.x, end.y);

            if (firstInside && secondInside) {
                //inneer cut
                float projection1 = GeometryUtils.projection(begin, center, eVector);
                float projection2 = GeometryUtils.projection(end, center, eVector);
                data.add(projection1, projection2, density, entity);

            } else if (firstInside) {
                //half cut
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(begin, center, eVector);
                float projection2 = GeometryUtils.projection(last.getPoint(), center, eVector);
                data.add(projection1, projection2, density, entity);
            } else if (secondInside) {
                //half cut
                Flag first = list.get(0);
                float projection1 = GeometryUtils.projection(first.getPoint(), center, eVector);
                float projection2 = GeometryUtils.projection(end, center, eVector);
                data.add(projection1, projection2, density, entity);

            } else {
                //full cut
                Flag first = list.get(0);
                Flag last = list.get(list.size() - 1);
                float projection1 = GeometryUtils.projection(first.getPoint(), center, eVector);
                float projection2 = GeometryUtils.projection(last.getPoint(), center, eVector);
                data.add(projection1, projection2, density, entity);
            }
        }

        return data;
    }

    private void addGameEntityToDestroy(GameEntity entity, boolean recycle) {
        if (!entity.isAlive()) return;
        ArrayList<JointEdge> jointsEdges = entity.getBody().getJointList();
        for (JointEdge jointedge : jointsEdges) gameScene.onDestroyMouseJoint(jointedge.joint);
        Invoker.addBodyDestructionCommand(entity);
        Iterator<JointBlueprint> iterator = jointBlueprints.iterator();
        while (iterator.hasNext()) {
            JointBlueprint j = iterator.next();
            if (j.getEntity1() == entity || j.getEntity2() == entity)
                iterator.remove();
        }

        if (recycle) {
            for (BlockA blockA : entity.getBlocks()) {
                for (Block b : blockA.getAssociatedBlocks()) b.recycleSelf();
                blockA.recycleSelf();
            }

        }

        entity.setAlive(false);
    }

    public void addJointToDestroy(JointBlueprint jointBlueprint) {
        Invoker.addJointDestructionCommand(jointBlueprint);
    }

    public JointBlueprint addJointToCreate(JointDef jointDef, EntityWithBody entity1, EntityWithBody entity2, boolean connected) {

        JointBlueprint blueprint = new JointBlueprint(jointDef, entity1, entity2, connected);
        addJointBlueprint(blueprint);
        switch (jointDef.type) {

            case Unknown:
                break;
            case RevoluteJoint: {
                RevoluteJointDef jDef = (RevoluteJointDef) jointDef;
                if (entity1 instanceof GameEntity)
                    ((GameEntity) entity1).putKey(blueprint, JointKey.KeyType.A, jDef.localAnchorA);
                if (entity2 instanceof GameEntity)
                    ((GameEntity) entity2).putKey(blueprint, JointKey.KeyType.B, jDef.localAnchorB);
            }
            break;
            case PrismaticJoint: {
                PrismaticJointDef jDef = (PrismaticJointDef) jointDef;
                if (entity1 instanceof GameEntity)
                    ((GameEntity) entity1).putKey(blueprint, JointKey.KeyType.A, jDef.localAnchorA);
                if (entity2 instanceof GameEntity)
                    ((GameEntity) entity2).putKey(blueprint, JointKey.KeyType.B, jDef.localAnchorB);
            }
            break;
            case DistanceJoint: {
                DistanceJointDef jDef = (DistanceJointDef) jointDef;
                if (entity1 instanceof GameEntity)
                    ((GameEntity) entity1).putKey(blueprint, JointKey.KeyType.A, jDef.localAnchorA);
                if (entity2 instanceof GameEntity)
                    ((GameEntity) entity2).putKey(blueprint, JointKey.KeyType.B, jDef.localAnchorB);
            }
            break;
            case WeldJoint:
                WeldJointDef jDef = (WeldJointDef) jointDef;
                if (entity1 instanceof GameEntity)
                    ((GameEntity) entity1).putKey(blueprint, JointKey.KeyType.A, jDef.localAnchorA);
                if (entity2 instanceof GameEntity)
                    ((GameEntity) entity2).putKey(blueprint, JointKey.KeyType.B, jDef.localAnchorB);
                break;
            case PulleyJoint:
                break;
            case MouseJoint:

                break;
            case GearJoint:
                break;
            case LineJoint:
                break;

            case FrictionJoint:
                break;
        }
        return blueprint;
    }

    public void resetWeldJoints(ArrayList<GameEntity> allSplinters) {
        ArrayList<JointReset> pairs = new ArrayList<>();
        for (GameEntity gameEntity : allSplinters)
            for (BlockA block : gameEntity.getBlocks()) {
                for (Block b : block.getAssociatedBlocks()) {
                    if (b instanceof JointZoneBlock) {
                        JointZoneBlock jzb = (JointZoneBlock) b;
                        if (jzb.getTwin() == null) continue;
                        if (jzb.getParent() == null || jzb.getTwin().getParent() == null) continue;
                        GameEntity gameEntity1 = jzb.getParent().getGameEntity();
                        GameEntity gameEntity2 = jzb.getTwin().getParent().getGameEntity();
                        if (gameEntity1 == null || gameEntity2 == null) continue;
                        boolean found = false;
                        for (JointReset p : pairs) {
                            GameEntity e1 = p.getFirst();
                            GameEntity e2 = p.getSecond();
                            if ((e1 == gameEntity1 && e2 == gameEntity2) || (e1 == gameEntity2 && e2 == gameEntity1)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            JointReset pair = new JointReset(jzb.getJointKeyType(), jzb.getJointDef(), gameEntity1, gameEntity2, jzb);
                            pairs.add(pair);
                        }
                    }
                }
            }

        for (JointReset p : pairs) {
            GameEntity e1 = p.getFirst();
            GameEntity e2 = p.getSecond();
            JointKey.KeyType type = p.getKeyType();
            JointDef def = Utils.copyJointDef(p.getJointDef());
            JointBlueprint blueprint;
            if (type == JointKey.KeyType.A)
                blueprint = new JointBlueprint(def, e1, e2, false);
            else
                blueprint = new JointBlueprint(def, e2, e1, false);
            if (p.getBlock().getAdvance() != null)
                blueprint.setAdvance(p.getBlock().getAdvance());


            addJointBlueprint(blueprint);
        }

    }

    public void performScreenCut(Vector2 worldPoint1, Vector2 worldPoint2) {
        cutRayCastCallback.reset();
        cutRayCastCallback.setDirection(true);
        getPhysicsWorld().rayCast(cutRayCastCallback, worldPoint1, worldPoint2);
        cutRayCastCallback.setDirection(false);
        getPhysicsWorld().rayCast(cutRayCastCallback, worldPoint2, worldPoint1);
        ArrayList<ArrayList<Flag>> flags = cutRayCastCallback.getFlags();
        ArrayList<BlockA> blocks = cutRayCastCallback.getCoveredBlocks();
        ArrayList<Fixture> fixtures = cutRayCastCallback.getCoveredFixtures();
        ArrayList<ArrayList<Segment>> cuts = new ArrayList<>();
        ArrayList<GameEntity> entities = new ArrayList<>();
        for (ArrayList<Flag> list : flags) {
            Collections.sort(list);
            int i = flags.indexOf(list);
            BlockA block = blocks.get(i);
            Body body = fixtures.get(i).getBody();
            GameEntity entity = (GameEntity) body.getUserData();
            if (!entities.contains(entity)) {
                entities.add(entity);
                cuts.add(new ArrayList<>());
            }
            int entityIndex = entities.indexOf(entity);
            ArrayList<Segment> entityCutsList = cuts.get(entityIndex);

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
                entityCutsList.add(new Segment(p1, p2, block, CutType.fullCut));
            }
        }

        ArrayList<GameEntity> allSplinters = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            GameEntity cutEntity = entities.get(i);
            ArrayList<Segment> entityCuts = cuts.get(i);
            if (cutEntity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
                ArrayList<GameEntity> splinters = applySlice(cutEntity, entityCuts);
                if (splinters != null) allSplinters.addAll(splinters);
            }
        }
        resetWeldJoints(allSplinters);

    }

    public boolean applyLiquidStain(GameEntity gameEntity, float x, float y, BlockA concernedBlock, Color color, int index) {
        Vector2 localPosition = new Vector2(x, y);

        float angle = rand.nextInt(360);

        ITextureRegion textureRegion = ResourceManager.getInstance().imageTextureRegion.getTextureRegion(index);

        StainBlock stainBlock = BlockFactory.createBlockC(localPosition, angle, concernedBlock.getVertices(), textureRegion, color);

        if (stainBlock != null && stainBlock.isNotAborted() && !gameEntity.stainHasTwin(concernedBlock, stainBlock)) {
            stainBlock.setID(index);
            gameEntity.addStain(concernedBlock, stainBlock);
            return true;
        } else
            return false;
    }

    private ArrayList<GameEntity> applySlice(GameEntity gameEntity, ArrayList<Segment> entitySegments) {
        boolean cutPerformed = false;
        ArrayList<BlockA> splintersBlocks = new ArrayList<>();
        HashSet<BlockA> abortedBlocks;
        abortedBlocks = new HashSet<>();
        for (Segment segment : entitySegments) {
            if (segment.getType() != CutType.fullCut) continue;

            Vector2 p1 = gameEntity.getBody().getLocalPoint(segment.getP1()).cpy().mul(32);
            Vector2 p2 = gameEntity.getBody().getLocalPoint(segment.getP2()).cpy().mul(32);
            BlockA block = segment.getBlock();
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
            if (!cut.isValid(vertices)) continue;
            block.performCut(cut);
            cutPerformed = true;

            Iterator<BlockA> iterator = block.createIterator();
            while (iterator.hasNext()) {
                BlockA bl = iterator.next();
                if (bl.isNotAborted()){
                    splintersBlocks.add(bl);
                }
                else {
                    abortedBlocks.add(bl);
                }
            }
        }
        if (cutPerformed) {
            for (BlockA block : gameEntity.getBlocks()) {
                for (FreshCut freshCut : block.getFreshCuts()) {
                    // if(liquidParticleWrappers.contains(freshCut.getLiquidParticleWrapper()))
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
                }
            }

            for (BlockA b : gameEntity.getBlocks()) {
                if (b.isNotAborted()) splintersBlocks.add(b);
            }
            ArrayList<GameEntity> splinters = GameEntityFactory.getInstance().computeSplinters(splintersBlocks, gameEntity);

            gameEntity.getBody().setActive(false);
            gameScene.getWorldFacade().addGameEntityToDestroy(gameEntity, false);
            return splinters;
        } else {
            return null;
        }

    }

    private void applyImpact(BlockA block, Vector2 worldPoint, float energy, GameEntity gameEntity) {

        ArrayList<GameEntity> allSplinters;
        if (gameEntity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
            Vector2 point = gameEntity.getBody().getLocalPoint(worldPoint);
            Vector2 localPoint = obtain(point.x * 32, point.y * 32);

            ArrayDeque<BlockA> deque = new ArrayDeque<>();
            deque.push(block);
            ShatterVisitor visitor = new ShatterVisitor(energy, localPoint);
            while (!deque.isEmpty()) {
                BlockA current = deque.peek();
                if (current == null) {
                    break;
                }

                if (current.isNotAborted()) {
                    visitor.visitTheElement(current); //visit
                }

                if (visitor.isExhausted() || visitor.isError()) {
                    break;
                }
                for (BlockA component : current.getChildren()) {
                    if (component.isNotAborted()) deque.addLast(component);
                }


                deque.pop();
            }
            recycle(localPoint);

            ArrayList<BlockA> splintersBlocks = new ArrayList<>();
            HashSet<BlockA> abortedBlocks = new HashSet<>();

            Iterator<BlockA> iterator = block.createIterator();

            while (iterator.hasNext()) {
                BlockA bl = iterator.next();
                if (bl.isNotAborted()) splintersBlocks.add(bl);
                else abortedBlocks.add(bl);
            }


            if (visitor.isShatterPerformed()) {
                for (FreshCut freshCut : block.getFreshCuts()) {
                    //if(liquidParticleWrappers.contains(freshCut.getLiquidParticleWrapper()))
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
                }

                gameEntity.getBlocks().remove(block);
                splintersBlocks.addAll(gameEntity.getBlocks());
                if (splintersBlocks.size() > 0) {
                    allSplinters = GameEntityFactory.getInstance().computeSplinters(splintersBlocks, gameEntity);
                    resetWeldJoints(allSplinters);
                }

                gameScene.getWorldFacade().addGameEntityToDestroy(gameEntity, false);


            }
        }

    }

    private void computeInternalConduction(CoatingBlock coatingBlock) {

        HashSet<CoatingBlock> neighbors = coatingBlock.getNeighbors();


        for (CoatingBlock other : neighbors) {
            float density = coatingBlock.getParent().getProperties().getDensity();
            float area1 = coatingBlock.getArea();
            float area2 = other.getArea();
            float length = (float) (area1 <= area2 ? Math.sqrt(area1) : Math.sqrt(area2));
            PhysicsUtils.transferHeatByConduction(density, density, length, coatingBlock, other, 0.01f, 0.01f, 1f, 1f);
        }


    }

    private ArrayList<CoatingBlock> getLineCoatingBlocks(Vector2 begin, Vector2 end, BlockA block) {
        ArrayList<CoatingBlock> result = new ArrayList<>();
        Vector2 dir = end.cpy().sub(begin);
        float length = dir.len();
        dir.nor();
        int nsteps = 12;
        float step = length / nsteps;
        dir.nor().mul(step);
        Vector2 other = begin.cpy().add(dir);
        block.getGameEntity().getMesh().detachChildren();
        for (int i = 0; i < nsteps; i++) {
            CoatingBlock g = block.getBlockGrid().getNearestCoatingBlockSimple(begin);

            //GameScene.plotter.drawPoint(g.getPosition(), Color.BLUE, block.getGameEntity().getMesh());
            //Log.e("points", i + ":" + g.getPosition());
            result.add(g);
            begin.add(dir);
            other.add(dir);

        }
        return result;
    }

    public void addGameEntityToDestroy(GameEntity entity, int time) {
        for (BlockA blockA : entity.getBlocks()) {
            for (FreshCut freshCut : blockA.getFreshCuts()) {
                if (liquidParticleWrappers.contains(freshCut.getLiquidParticleWrapper()))
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
            }
        }

        TimedCommand command = new TimedCommand(time, new Action() {
            @Override
            public void performAction() {
                addGameEntityToDestroy(entity, true);
            }
        });

        timedCommands.add(command);


    }

    public void addJointToDestroy(JointBlueprint jointBlueprint, int time) {
        TimedCommand command = new TimedCommand(time, () -> addJointToDestroy(jointBlueprint));
        timedCommands.add(command);
    }

    public GameGroup getGround() {
        return ground;
    }

    public void setGround(GameGroup ground) {
        this.ground = ground;
    }
}