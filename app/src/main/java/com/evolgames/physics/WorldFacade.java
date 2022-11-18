package com.evolgames.physics;

import android.util.Log;
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
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blockvisitors.ClipVisitor;
import com.evolgames.entities.blockvisitors.ShatterVisitor;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.contact.ContactObserver;
import com.evolgames.entities.contact.GameEntityContactListener;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.CutType;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.PointsFreshCut;
import com.evolgames.entities.cut.Segment;

import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.particles.wrappers.ClusterLiquidParticleWrapper;
import com.evolgames.entities.particles.wrappers.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.particles.wrappers.LiquidParticleWrapper;
import com.evolgames.entities.particles.systems.Spark;
import com.evolgames.entities.particles.wrappers.SegmentLiquidParticleWrapper;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.ragdoll.Ragdoll;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
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
import com.evolgames.physics.entities.callbacks.GameEntityQueryCallBack;
import com.evolgames.physics.entities.callbacks.SimpleDetectionRayCastCallback;
import com.evolgames.scenes.GameScene;

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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.recycle;
import static java.lang.Math.min;

public class WorldFacade implements ContactObserver {
    public static final float BACKOFF = 0.05f;
    public static final int PRECISION = 50;
    private static final ClipVisitor clipVisitor = new ClipVisitor();
    private static final HashSet<LiquidParticleWrapper> liquidParticleWrappers = new HashSet<>();
    private static final ArrayList<ParticleSystem<?>> flames = new ArrayList<>();
    private static final SimpleDetectionRayCastCallback simpleDetectionRayCastCallback = new SimpleDetectionRayCastCallback();
    private static final ArrayList<TimedCommand> timedCommands = new ArrayList<>();
    private final GameEntityContactListener contactListener;
    private final GameEntityQueryCallBack queryCallBack = new GameEntityQueryCallBack();
    private final BlockQueryCallBack blockQueryCallBack = new BlockQueryCallBack();
    private final LinearRayCastCallback rayCastCallback = new LinearRayCastCallback();
    private final DetectionRayCastCallback detectionRayCastCallback = new DetectionRayCastCallback();
    private final PhysicsWorld physicsWorld;
    private final GameScene gameScene;
    private final ArrayList<Pair<GameEntity, GameEntity>> goThroughEntities = new ArrayList<>();
    private final CutRayCastCallback cutRayCastCallback = new CutRayCastCallback();
    private final HashSet<Touch> touches = new HashSet<>();
    Random rand = new Random();
    private GameGroup ground;

    public WorldFacade(GameScene scene) {
        gameScene = scene;
        physicsWorld = new PhysicsWorld(obtain(0, PhysicsConstants.gravity), false);
        contactListener = new GameEntityContactListener(this);
        physicsWorld.setContactListener(contactListener);


        scene.registerUpdateHandler(physicsWorld);
        physicsWorld.setContinuousPhysics(true);
        physicsWorld.setAutoClearForces(true);
        //physicsWorld.setWarmStarting(false);
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

    public ArrayList<GameEntity> computeSplinters(ArrayList<LayerBlock> splinterBlocks, GameEntity parentEntity) {

        Body body = parentEntity.getBody();
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float rot = body.getAngle();

        ArrayList<GameEntity> splinters = GameEntityFactory.getInstance().createSplinterEntities(x, y, rot, BlockUtils.getDivisionGroups(splinterBlocks), body.getLinearVelocity(), body.getAngularVelocity(), parentEntity.getName(),parentEntity);


        for (GameEntity splinter : splinters) {
            ArrayList<LayerBlock> layerBlocks = splinter.getLayerBlocks();
            for(LayerBlock layerBlock:layerBlocks){
                for(Block<?,?> associated:layerBlock.getAssociatedBlocks()){
                    if(associated instanceof JointBlock){
                        JointBlock jointBlock = (JointBlock) associated;
                        this.recreateJoint(jointBlock,splinter,parentEntity);
                    }
                }
            }

            parentEntity.getParentGroup().addGameEntity(splinter);
            if (parentEntity.getType() == SpecialEntityType.Head) {
                if (GeometryUtils.PointInPolygon(0, 9, splinter.getLayerBlocks().get(0).getVertices())) {
                    splinter.setType(SpecialEntityType.Head);
                    ((Ragdoll) parentEntity.getParentGroup()).setHead(splinter);
                }
            } else splinter.setType(parentEntity.getType());
            if (splinter.getArea() < 25) {
                this.scheduleGameEntityToDestroy(splinter, (int) (splinter.getArea() * 10));
            }
        }

        return splinters;
    }


    public GameEntityContactListener getContactListener() {
        return contactListener;
    }

    public SimpleDetectionRayCastCallback getSimpleDetectionRayCastCallback() {
        return simpleDetectionRayCastCallback;
    }

    public Vector2 detect(Vector2 first, Vector2 second) {
        simpleDetectionRayCastCallback.reset();
        physicsWorld.rayCast(simpleDetectionRayCastCallback, first, second);
        return simpleDetectionRayCastCallback.getIntersectionPoint();
    }


    private void addFlame(FireParticleWrapperWithPolygonEmitter fireParticleWrapper) {
        ParticleSystem<?> fire = fireParticleWrapper.getParticleSystem();
        flames.add(fire);
    }

    private LiquidParticleWrapper liquidParticleWrapperFromFreshCut(GameEntity parentEntity, FreshCut freshCut, Color color, int lowerRate, int higherRate) {
        if (freshCut instanceof SegmentFreshCut) {
            SegmentFreshCut sfc = (SegmentFreshCut) freshCut;
            return new SegmentLiquidParticleWrapper(parentEntity, new float[]{sfc.first.x, sfc.first.y, sfc.second.x, sfc.second.y}, color, lowerRate, higherRate);

        } else if (freshCut instanceof PointsFreshCut) {
            PointsFreshCut pfc = (PointsFreshCut) freshCut;
            float[] data = Utils.mapPointsToArray(pfc.getPoints());
            return new ClusterLiquidParticleWrapper(parentEntity, color, data, pfc.getSplashVelocity(), lowerRate, higherRate);
        }
        return null;
    }

    public void createBloodSource(GameEntity parentEntity, LayerBlock layerBlock, final FreshCut freshCut) {
        int lowerRate = (int) (layerBlock.getProperties().getJuicinessLowerPressure() * freshCut.getLength());
        int higherRate = (int) (layerBlock.getProperties().getJuicinessUpperPressure() * freshCut.getLength());
        LiquidParticleWrapper particleWrapper =
                gameScene.getWorldFacade().createLiquidSource(parentEntity, freshCut, (layerBlock.getProperties().getJuiceColor() != null) ? layerBlock.getProperties().getJuiceColor() : Utils.getRandomColor(), lowerRate, higherRate);
        particleWrapper.setLimit(lowerRate);
        freshCut.setLiquidParticleWrapper(particleWrapper);

        particleWrapper.setSpawnAction(() -> {
            layerBlock.decrementLiquidQuantity();
            particleWrapper.decrementLimit();
            if (particleWrapper.getLimit() <= 0 || layerBlock.getLiquidQuantity() <= 0) {
                particleWrapper.finishSelf();
            }
        });
    }

    public LiquidParticleWrapper createLiquidSource(GameEntity parentEntity, final FreshCut freshCut, Color color, int lowerRate, int higherRate) {
        LiquidParticleWrapper liquidSource = liquidParticleWrapperFromFreshCut(parentEntity, freshCut, color, lowerRate, higherRate);
        if (liquidSource == null) {
            return null;
        }
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
            LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
            LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();
            if (touch.isEquivalent(block1, block2)) return contact;
        }
        return null;
    }

    public void onStep(float deltaTime) {

        for (TimedCommand timedCommand : timedCommands) timedCommand.update();

/*
        ArrayList<JointBlueprint> createdBluePrints = new ArrayList<>();
        Iterator<Joint> iterator = physicsWorld.getJoints();
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
*/


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
                    for (Block<?, ?> block : gameEntity.getLayerBlocks()) {
                        LayerBlock layerBlock = (LayerBlock) block;
                        for (CoatingBlock coatingBlock : layerBlock.getBlockGrid().getCoatingBlocks()) {
                            computeInternalConduction(coatingBlock);
                        }
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
                            double sparkTemperature = (double) spark.getUserData();

                            PhysicsUtils.transferHeatByConvection(0.05f, 1f, 1f, sparkTemperature, nearestCoatingBlock);
                        }
                        // Vector2 grainWorldPosition = body.getWorldPoint(nearestCoatingBlock.position.cpy().mul(1/32f)).cpy().mul(32f);
                        //  GameScene.plotter.drawPoint(grainWorldPosition,Color.RED,1,0);

                    }
                }
            }
        }


        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (gameEntity.getName().equals("Ground")) {
                    continue;
                }
                for (Block<?, ?> block : gameEntity.getLayerBlocks()) {
                    LayerBlock layerBlock = (LayerBlock) block;
                    for (CoatingBlock grain : layerBlock.getBlockGrid().getCoatingBlocks()) {
                        PhysicsUtils.transferHeatByConvection(0.0001f, 100f, 0.00125f, PhysicsConstants.ambient_temperature, grain);
                    }
                }
            }


        //Update grains
        for (GameGroup gameGroup : gameScene.getGameGroups())
            for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                if (gameEntity.getName().equals("Ground")) continue;
                for (Block<?, ?> block : gameEntity.getLayerBlocks()) {
                    LayerBlock layerBlock = (LayerBlock) block;
                    for (CoatingBlock grain : layerBlock.getBlockGrid().getCoatingBlocks()) {
                        grain.update();
                    }
                }
            }


        if (true)
            for (GameGroup gameGroup : gameScene.getGameGroups())
                for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                    if (!gameEntity.isAlive() || gameEntity.getName().equals("Ground")) continue;
                    for (LayerBlock block : gameEntity.getLayerBlocks()) {
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
        Iterator<LiquidParticleWrapper> wrapperIterator = liquidParticleWrappers.iterator();
        while (wrapperIterator.hasNext()) {
            LiquidParticleWrapper liquidParticleWrapper = wrapperIterator.next();
            liquidParticleWrapper.update();
            if (!liquidParticleWrapper.isAlive() && liquidParticleWrapper.isAllParticlesExpired()) {
                liquidParticleWrapper.recycleParticles();
                liquidParticleWrapper.getParticleSystem().detachSelf();
                wrapperIterator.remove();
            }
        }
        HashSet<GameEntity> affectedEntities = new HashSet<>();

        for (LiquidParticleWrapper liquidSource : liquidParticleWrappers)
            if (liquidSource.getParticleSystem().hasParent())
                if (liquidSource.getParticleSystem().getParticles().length > 0) {
                    for (Particle<?> p : liquidSource.getParticleSystem().getParticles()) {
                        if (p != null && !p.isExpired() && Math.random() < 0.1f) {
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
                                    for (Block<?, ?> block : entity.getLayerBlocks()) {
                                        LayerBlock layerBlock = (LayerBlock) block;
                                        if (layerBlock.testPoint(entity.getBody(), rx, ry)) {
                                            onBoundaries = true;
                                            break;
                                        }
                                    }

                            boolean dead = false;
                            if (onBoundaries) {
                                int index = rand.nextInt(14);
                                for (GameEntity entity : entities) {
                                    if (entity.getBody() != null) {
                                        boolean affected = false;
                                        Body body = entity.getBody();
                                        Vector2 localPoint = obtain(body.getLocalPoint(rPosition)).mul(32);
                                        ArrayList<LayerBlock> blocks = entity.getLayerBlocks();
                                        for (int i = blocks.size() - 1; i >= 0; i--) {
                                            boolean applied = applyLiquidStain(entity, localPoint.x, localPoint.y, blocks.get(i), liquidSource.getColor(), index);
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

        if (normalImpulse < 1000) return;

        if (!entity1.isAlive() || !entity2.isAlive()) return;

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

        if (entity1 == null || entity2 == null) return;
        if (!entity1.isAlive() || !entity2.isAlive()) return;
        if (!(entity1.isProjectile() || entity2.isProjectile())) return;

        computePenetrationImpact(contact, entity1, entity2);

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

    private float computeCollisionEnergy(Vector2 V1, Vector2 V2, Vector2 normal, float m1, float m2) {
        Vector2 V1Useful = obtain(normal).mul(V1.dot(normal));
        Vector2 V2Useful = obtain(normal).mul(V2.dot(normal));
        final float energy = (float) (0.5 * (m1 * V1Useful.dot(V1Useful) + m2 * V2Useful.dot(V2Useful)));
        recycle(V1Useful);
        recycle(V2Useful);
        return energy;
    }

    private boolean checkVectorPointsToEntity(Vector2 worldPoint, Vector2 vector, GameEntity entity) {
        Vector2 normalBackoff = obtain(vector).mul(BACKOFF);
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

    private Vector2 detectIntersectionWithEntity(GameEntity entity, Vector2 begin, Vector2 end) {
        detectionRayCastCallback.reset();
        detectionRayCastCallback.setEntity(entity);
        physicsWorld.rayCast(detectionRayCastCallback, begin, end);
        return detectionRayCastCallback.getIntersectionPoint();
    }

    private void drawData(Data data1, float DIRECTION, Vector2 normal, Color color) {
        float[][] data = data1.getData();
        for (float[] datum : data) {
            float inf = datum[0];
            float sup = datum[1];
            Vector2 p1 = data1.getBase().cpy().add(normal.x * inf * DIRECTION, normal.y * inf * DIRECTION).mul(32f);
            Vector2 p2 = data1.getBase().cpy().add(normal.x * sup * DIRECTION, normal.y * sup * DIRECTION).mul(32f);
            GameScene.plotter2.drawLine2(p1, p2, color, 1);
        }
    }

    private void computePenetrationImpact(Contact contact, GameEntity entity1, GameEntity entity2) {
        Vector2[] points = contact.getWorldManifold().getPoints();
        Vector2 point;
        if (contact.getWorldManifold().getNumberOfContactPoints() == 1)
            point = points[0];
        else
            point = points[0].add(points[1]).mul(0.5f);

        LayerBlock block1 = (LayerBlock) contact.getFixtureA().getUserData();
        LayerBlock block2 = (LayerBlock) contact.getFixtureB().getUserData();

        Vector2 V1 = entity1.getBody().getLinearVelocity();
        Vector2 V2 = entity2.getBody().getLinearVelocity();
        float m1 = entity1.getBody().getMass();
        float m2 = entity2.getBody().getMass();
        Vector2 normal = obtain(V1.x - V2.x, V1.y - V2.y).nor();
        Vector2 tangent = obtain(-normal.y, normal.x);

        final float energy = computeCollisionEnergy(V1, V2, normal, m1, m2);


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
        if (infTPenetrator < infTPenetrated || supNPenetrator > supTPenetrated) return;

        boolean normalPointsToPenetrated = checkVectorPointsToEntity(point, normal, penetrated);


        //GameScene.pause = true;


        float xN1 = (normalPointsToPenetrated) ? (supNPenetrator) : (infNPenetrator);
        Vector2 penetratorDetectionOriginStart = obtain(point.x + normal.x * xN1, point.y + normal.y * xN1);
        float xN2 = (!normalPointsToPenetrated) ? (supNPenetrator) : (infNPenetrator);
        Vector2 penetratorDetectionOriginEnd = obtain(point.x + normal.x * xN2, point.y + normal.y * xN2);

        float xN3 = (normalPointsToPenetrated) ? (supNPenetrated) : (infNPenetrated);
        Vector2 penetratedDetectionOriginEnd = obtain(point.x + normal.x * xN3, point.y + normal.y * xN3);


        float infX = Math.max(infTPenetrator, infTPenetrated);
        float supX = min(supTPenetrator, supTPenetrated);
        float dT = Math.abs(supX - infX) / PRECISION;


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

        final int DIRECTION = normalPointsToPenetrated ? -1 : 1;
        final float xBackoff = normal.x * BACKOFF * DIRECTION;
        final float yBackoff = normal.y * BACKOFF * DIRECTION;
        while (counter <= limit1) {
            Data data1, data2;
            movingPenetratorDetectionOriginStart.set(penetratorDetectionOriginStart.x + counter * tangent.x * sign * dT, penetratorDetectionOriginStart.y + counter * tangent.y * sign * dT);
            movingPenetratorDetectionOriginEnd.set(penetratorDetectionOriginEnd.x + counter * tangent.x * sign * dT, penetratorDetectionOriginEnd.y + counter * tangent.y * sign * dT);
            movingPenetratedDetectionOriginEnd.set(penetratedDetectionOriginEnd.x + counter * tangent.x * sign * dT, penetratedDetectionOriginEnd.y + counter * tangent.y * sign * dT);

            Vector2 intersection1 = detectIntersectionWithEntity(penetrator, movingPenetratorDetectionOriginStart.cpy().sub(xBackoff, yBackoff), movingPenetratorDetectionOriginEnd.cpy().add(xBackoff, yBackoff));
            if (intersection1 != null) {
                data1 = findIntervalsExclusiveToOneEntity(penetrator, intersection1, normal.cpy().mul(DIRECTION), intersection1.cpy().sub(xBackoff, yBackoff), movingPenetratorDetectionOriginEnd.cpy().add(xBackoff, yBackoff));
                penetratorTopography[index] = data1;
                Vector2 intersection2 = detectIntersectionWithEntity(penetrated, intersection1.cpy().add(xBackoff, yBackoff), movingPenetratedDetectionOriginEnd.cpy().sub(xBackoff, yBackoff));
                if (intersection2 != null) {
                    data2 = findIntervalsExceptingOneEntity(penetrator, intersection1, normal.cpy().mul(-DIRECTION), intersection1.cpy().add(xBackoff, yBackoff), movingPenetratedDetectionOriginEnd.cpy().sub(xBackoff, yBackoff));
                    penetratedTopography[index] = data2;

                }
            }


            if (counter >= 0) counter++;
            if (counter <= limit2) counter *= -1;
            index++;
        }
        recycle(movingPenetratorDetectionOriginStart);
        recycle(movingPenetratorDetectionOriginEnd);
        recycle(movingPenetratedDetectionOriginEnd);
        recycle(penetratorDetectionOriginStart);
        recycle(penetratorDetectionOriginEnd);
        recycle(penetratedDetectionOriginEnd);

        float penetrationLength = getPenetrationLength(penetrated, p, penetratedTopography, penetratorTopography);
        LayerBlock penetratedBlock = penetrated.getLayerBlocks().contains(block1) ? block1 : block2;
        float da = penetrationLength / PRECISION;

        int step = 0;

        while (step < PRECISION) {

            final float advance = (step + 1) * da;

            float consumedEnergy = 0;
            for (index = 0; index < penetratedTopography.length; index++) {
                if (penetratedTopography[index] != null && penetratorTopography[index] != null) {
                    float dE = penetratedTopography[index].getEnergyForAdvance(advance, dT);
                    consumedEnergy += dE;
                }
            }

            if (energy - consumedEnergy < 0) {
                //create penetration effects
                createPenetrationEffects(normal, energy, dT, penetratedTopography, DIRECTION, advance);
                if (step >= PRECISION / 10) {
                 /*   HashSet<JointZoneBlock> zones = findWeldZones(penetrator, penetrated, normal.cpy().mul(-step * da));
                    float weldedArea = (float) zones.stream().mapToDouble(t -> GeometryUtils.getArea(t.getVertices())).sum() / 2;
                    if (!zones.isEmpty() && weldedArea > penetrator.getArea() / 10) {
                        zones.forEach(zone -> zone.getParent().addAssociatedBlock(zone));
                        penetrated.getBody().applyLinearImpulse(normal.cpy().mul(DIRECTION * energy / 500), point);
                        penetrator.setProjectile(false);
                        mergeEntities(penetrated, penetrator, normal.cpy().mul(step * da), point, zones);
                        for (Fixture fixture : penetrator.getBody().getFixtureList()) {
                            fixture.setSensor(true);
                        }
                    }*/
                    applyImpact(penetratedBlock, point, energy / 60, penetrated);
                }
                return;
            }
            step++;
        }

        //go through
        Log.e("Go through", "go through:" + energy);
        Pair<GameEntity, GameEntity> pair = new Pair<>(penetrator, penetrated);
        penetrated.getBody().applyLinearImpulse(normal.cpy().mul(DIRECTION * energy / 500), point);
        applyImpact(penetratedBlock, point, energy / 10, penetrated);
        createPenetrationEffects(normal, energy, dT, penetratedTopography, DIRECTION, PRECISION * da);
        goThroughEntities.add(pair);
    }

    private void createPenetrationEffects(Vector2 normal, float energy, float dT, Data[] penetratedTopography, int DIRECTION, float advance) {
        int index;
        Hashtable<LayerBlock, ArrayList<Vector2>> table = new Hashtable<>();
        for (index = 0; index < penetratedTopography.length; index++) {
            Vector2 dir = normal.cpy().mul(-DIRECTION);
            Data data = penetratedTopography[index];
            if (data != null) {
                int i = 0;
                for (float[] datum : data.getData()) {
                    float inf = datum[0];
                    Vector2 worldPoint = data.getBase().cpy().add(dir.x * inf * DIRECTION, dir.y * inf * DIRECTION);
                    Vector2 localPoint = data.getEntities()[i].getBody().getLocalPoint(worldPoint).cpy().mul(32f);

                    LayerBlock layerBlock = data.getBlocks()[i];
                    if (table.containsKey(layerBlock)) {
                        ArrayList<Vector2> list = table.get(layerBlock);
                        assert list != null;
                        list.add(localPoint);
                    } else {
                        ArrayList<Vector2> newList = new ArrayList<>();
                        newList.add(localPoint);
                        table.put(layerBlock, newList);
                    }
                    i++;
                }
            }
        }
        table.forEach((key, pts) -> {
            if (key.getProperties().isJuicy()) {
                final float length = dT * advance * 32f * 32f * pts.size();
                if (length > 3) {
                    FreshCut freshCut = new PointsFreshCut(pts, length, normal.cpy().mul(energy * 32f));
                    createBloodSource(key.getGameEntity(), key, freshCut);
                    key.addFreshCut(freshCut);
                }
            } else {
                //TODO Implement hard impact effects
            }
        });
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

    /*public void mergeEntities(GameEntity receiver, GameEntity traveler, Vector2 advance, Vector2 impactWorldPoint, HashSet<JointZoneBlock> zones) {

        Vector2 localA = receiver.getBody().getLocalPoint(impactWorldPoint).cpy();
        Vector2 localB = traveler.getBody().getLocalPoint(impactWorldPoint.cpy().add(advance)).cpy();

        WeldJointDef jointDef = new WeldJointDef();
        jointDef.bodyA = receiver.getBody();
        jointDef.bodyB = traveler.getBody();
        jointDef.localAnchorA.set(localA.cpy());
        jointDef.localAnchorB.set(localB.cpy());
        jointDef.collideConnected = false;
        jointDef.referenceAngle = -receiver.getBody().getAngle() + traveler.getBody().getAngle();

        JointBlueprint blueprint = addJointToCreate(jointDef, receiver, traveler, false);
        blueprint.setAdvance(advance);
       *//* for (JointZoneBlock jointZoneBlock : zones) {
            jointZoneBlock.setJointDef(jointDef);
            jointZoneBlock.setJointId(blueprint.getId());
            jointZoneBlock.setAdvance(advance);
        }*//*

        receiver.getMesh().setZIndex(1);
        traveler.getMesh().setZIndex(0);
        gameScene.sortChildren();
    }*/


  /*  public HashSet<JointBlock> findWeldZones(GameEntity penetrator, GameEntity penetrated, Vector2 advanceVector) {

        HashSet<JointBlock> result = new HashSet<>();
        Vector2 position1 = penetrator.getBody().getPosition();
        Vector2 position2 = penetrated.getBody().getPosition();
        float angle1 = penetrator.getBody().getAngle();
        float angle2 = penetrated.getBody().getAngle();

        ArrayList<ArrayList<ArrayList<Vector2>>> penetratorInterpolatedLists = new ArrayList<>();
        for (int i = 0; i < penetrator.getLayerBlocks().size(); i++) {
            ArrayList<ArrayList<Vector2>> bigList = new ArrayList<>();
            ArrayList<Vector2> penetratorBlockVertices = penetrator.getLayerBlocks().get(i).getVertices();
            ArrayList<Vector2> interpolated = GameEntityTransformation.interpolate(position1.cpy().add(advanceVector), angle1, position2, angle2, penetratorBlockVertices);
            for (Vector2 v : interpolated) v.mul(32f);
            bigList.add(interpolated);
            for (int j = 0; j < penetrated.getLayerBlocks().size() - 1; j++) {
                ArrayList<Vector2> copy = new ArrayList<>();
                for (int k = 0; k < penetratorBlockVertices.size(); k++) {
                    copy.add(penetratorBlockVertices.get(k).cpy());
                }
                bigList.add(copy);
            }
            penetratorInterpolatedLists.add(bigList);
        }


        for (int i = 0; i < penetratorInterpolatedLists.size(); i++) {
            ArrayList<ArrayList<Vector2>> bigList = penetratorInterpolatedLists.get(i);
            //  GameScene.plotter.drawPolygon(bigList.get(0),Color.RED);
            for (int j = 0; j < penetrated.getLayerBlocks().size(); j++) {
                ArrayList<Vector2> path = penetrated.getLayerBlocks().get(j).getVertices();
                //    if(i==0)     GameScene.plotter.drawPolygon(path,Color.GREEN);
                ArrayList<Vector2> overlapZone1 = BlockUtils.applyClip(bigList.get(j), path);
                if (overlapZone1 != null) {
                    //project back to penetrator
                    ArrayList<Vector2> overlapZone2 = GameEntityTransformation.interpolate(position2, angle2, position1.cpy().add(advanceVector), angle1, overlapZone1);
                    for (Vector2 v : overlapZone2) v.mul(32f);
                    JointZoneBlock zone1 = new JointZoneBlock(new GameEntityTransformation(position1, position2, angle1, angle2));
                    JointZoneBlock zone2 = new JointZoneBlock(new GameEntityTransformation(position1, position2, angle1, angle2));
                    zone1.initialization(overlapZone1, new JointZoneProperties(), -1, true);
                    zone2.initialization(overlapZone2, new JointZoneProperties(), -1, true);

                    zone1.setParent(penetrated.getBlocks().get(j));
                    zone2.setParent(penetrator.getBlocks().get(i));
//                    zone1.setJointKeyType(KeyType.A);
//                    zone2.setJointKeyType(KeyType.B);
                    zone1.setTwin(zone2);
                    zone2.setTwin(zone1);
                    result.add(zone1);
                    result.add(zone2);

                    //  GameScene.plotter.drawPolygon(overlapZone2,Color.YELLOW);
                }
            }
        }
        return result;
    }*/


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

    private void addGameEntityToDestroy(GameEntity entity, boolean recycle) {
        if (!entity.isAlive()) return;
        ArrayList<JointEdge> jointsEdges = entity.getBody().getJointList();
        for (JointEdge jointedge : jointsEdges) {
            if (jointedge.joint instanceof MouseJoint) {
                gameScene.onDestroyMouseJoint((MouseJoint) jointedge.joint);
            }
        }
        Invoker.addBodyDestructionCommand(entity);
        // jointBlueprints.removeIf(j -> j.getEntity1() == entity || j.getEntity2() == entity);

        if (recycle) {
            for (LayerBlock layerBlock : entity.getLayerBlocks()) {
                for (Block<?, ?> b : layerBlock.getAssociatedBlocks()) {
                    b.recycleSelf();
                }
                layerBlock.recycleSelf();
            }

        }

        entity.setAlive(false);
    }

   /* public void resetWeldJoints(ArrayList<GameEntity> allSplinters) {
        ArrayList<JointReset> pairs = new ArrayList<>();
        for (GameEntity gameEntity : allSplinters)
            for (LayerBlock block : gameEntity.getBlocks()) {
                for (Block<?, ?> b : block.getAssociatedBlocks()) {
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

  *//*      for (JointReset p : pairs) {
            GameEntity e1 = p.getFirst();
            GameEntity e2 = p.getSecond();
//            JointKey.KeyType type = p.getKeyType();
            JointDef def = Utils.copyJointDef(p.getJointDef());
            JointBlueprint blueprint;
            if (type == JointKey.KeyType.A) {
                blueprint = new JointBlueprint(def, e1, e2, false);
            } else {
                blueprint = new JointBlueprint(def, e2, e1, false);
            }
            if (p.getBlock().getAdvance() != null)
                blueprint.setAdvance(p.getBlock().getAdvance());

            Invoker.addJointCreationCommand(blueprint);
            //  addJointBlueprint(blueprint);
        }*//*

    }*/

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

        float angle = rand.nextInt(360);

        ITextureRegion textureRegion = ResourceManager.getInstance().imageTextureRegion.getTextureRegion(index);

        StainBlock stainBlock = BlockFactory.createStainBlock(localPosition, angle, concernedBlock.getVertices(), textureRegion, color);

        if (stainBlock != null && stainBlock.isNotAborted() && !gameEntity.stainHasTwin(concernedBlock, stainBlock)) {
            stainBlock.setId(index);
            gameEntity.addStain(concernedBlock, stainBlock);
            return true;
        } else
            return false;
    }

    private ArrayList<GameEntity> applySlice(GameEntity gameEntity, ArrayList<Segment> entitySegments) {
        boolean cutPerformed = false;
        ArrayList<LayerBlock> splintersBlocks = new ArrayList<>();
        HashSet<LayerBlock> abortedBlocks;
        abortedBlocks = new HashSet<>();
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
                } else {
                    abortedBlocks.add(bl);
                }
            }
        }
        if (cutPerformed) {
            for (LayerBlock layerBlock : gameEntity.getLayerBlocks()) {
                for (FreshCut freshCut : layerBlock.getFreshCuts()) {
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
                }
            }

            for (LayerBlock b : gameEntity.getLayerBlocks()) {
                if (b.isNotAborted()) {
                    splintersBlocks.add(b);
                }
            }
            ArrayList<GameEntity> splinters = computeSplinters(splintersBlocks, gameEntity);

            gameEntity.getBody().setActive(false);
            gameScene.getWorldFacade().addGameEntityToDestroy(gameEntity, false);
            return splinters;
        } else {
            return null;
        }

    }

    private void applyImpact(LayerBlock block, Vector2 worldPoint, float energy, GameEntity gameEntity) {
        if (energy < PhysicsConstants.TENACITY_FACTOR * 3) {
            return;
        }

        if (gameEntity.getBody().getType() == BodyDef.BodyType.DynamicBody) {
            Vector2 point = gameEntity.getBody().getLocalPoint(worldPoint);
            Vector2 localPoint = obtain(point.x * 32, point.y * 32);

            ArrayDeque<LayerBlock> deque = new ArrayDeque<>();
            deque.push(block);
            ShatterVisitor visitor = new ShatterVisitor(energy, localPoint);
            while (!deque.isEmpty()) {
                LayerBlock current = deque.peek();
                if (current == null) {
                    break;
                }

                if (current.isNotAborted()) {
                    visitor.visitTheElement(current); //visit
                }

                if (visitor.isExhausted() || visitor.isError()) {
                    break;
                }
                for (LayerBlock component : current.getChildren()) {
                    if (component.isNotAborted()) deque.addLast(component);
                }


                deque.pop();
            }
            recycle(localPoint);

            ArrayList<LayerBlock> splintersBlocks = new ArrayList<>();
            HashSet<LayerBlock> abortedBlocks = new HashSet<>();

            Iterator<LayerBlock> iterator = block.createIterator();

            while (iterator.hasNext()) {
                LayerBlock bl = iterator.next();
                if (bl.isNotAborted()) splintersBlocks.add(bl);
                else abortedBlocks.add(bl);
            }
            System.out.println("shatter performed?" + visitor.isShatterPerformed());

            if (visitor.isShatterPerformed()) {
                for (FreshCut freshCut : block.getFreshCuts()) {
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
                }

                gameEntity.getLayerBlocks().remove(block);
                splintersBlocks.addAll(gameEntity.getLayerBlocks());
                if (splintersBlocks.size() > 0) {
                    computeSplinters(splintersBlocks, gameEntity);
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

    private ArrayList<CoatingBlock> getLineCoatingBlocks(Vector2 begin, Vector2 end, LayerBlock block) {
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

    public void scheduleGameEntityToDestroy(GameEntity entity, int time) {
        for (LayerBlock layerBlock : entity.getLayerBlocks()) {
            for (FreshCut freshCut : layerBlock.getFreshCuts()) {
                if (liquidParticleWrappers.contains(freshCut.getLiquidParticleWrapper())) {
                    removeLiquidSource(freshCut.getLiquidParticleWrapper());
                }
            }
        }

        TimedCommand command = new TimedCommand(time, () -> addGameEntityToDestroy(entity, true));
        timedCommands.add(command);
    }


    public GameGroup getGround() {
        return ground;
    }

    public void setGround(GameGroup ground) {
        this.ground = ground;
    }


    public void recreateJoint(JointBlock jointBlock,GameEntity splinter,GameEntity parentEntity) {
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
                break;
            case RevoluteJoint:
                anchorA = ((RevoluteJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((RevoluteJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case PrismaticJoint:
                anchorA = ((PrismaticJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((PrismaticJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case DistanceJoint:
                break;
            case PulleyJoint:
                break;
            case MouseJoint:
                break;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                anchorA = ((WeldJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((WeldJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case FrictionJoint:
                break;
        }

        if(anchorA!=null) {
            jointBlock1.initialization(new ArrayList<>(Collections.singletonList(anchorA)),new JointProperties(jointDef),0, JointBlock.JointZoneType.PUNCTUAL, JointBlock.Position.A);
            entity1.getLayerBlocks().get(0).addAssociatedBlock(jointBlock1);
        }
        if(anchorB!=null){
            jointBlock2.initialization(new ArrayList<>(Collections.singletonList(anchorB)),new JointProperties(jointDef),0, JointBlock.JointZoneType.PUNCTUAL, JointBlock.Position.B);
            entity2.getLayerBlocks().get(0).addAssociatedBlock(jointBlock2);
        }
    }

}