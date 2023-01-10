package is.kul.learningandengine.factory;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import is.kul.learningandengine.MyConstants;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.BlockGrid;
import is.kul.learningandengine.basics.BlockImageSubType;
import is.kul.learningandengine.basics.BlockType;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.GameEntityContactListener;
import is.kul.learningandengine.basics.GameGroup;
import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.basics.LayerInfo;
import is.kul.learningandengine.basics.MCMesh;
import is.kul.learningandengine.basics.MeshInfo;
import is.kul.learningandengine.basics.Polarity;
import is.kul.learningandengine.entity.Ragdoll;
import is.kul.learningandengine.graphicelements.CollisionOption;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.helpers.UnionFind;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.GameScene;


public class BasicFactory {
    private static final BasicFactory INSTANCE = new BasicFactory();
    public static PhysicsWorld physicsWorld;
    public static EarClippingTriangulator triangulator = new EarClippingTriangulator();
    public AtomicInteger jointIds;
    GameScene scene;
    private VertexBufferObjectManager vbom;

    private BasicFactory() {
    }

    public static BasicFactory getInstance() {
        return BasicFactory.INSTANCE;
    }

    public static float[] generateData(List<Vector2> triangles) {

        float[] layerData = new float[3 * triangles.size()];

        for (int i = 0; i < triangles.size(); i++) {
            layerData[i * 3] = triangles.get(i).x;
            layerData[i * 3 + 1] = triangles.get(i).y;
            layerData[i * 3 + 2] = 10;
        }
        return layerData;
    }

    public static List<Vector2> triangulate(ArrayList<Vector2> points) {
        return BasicFactory.triangulator.computeTriangles(points);
    }

    public static void createJointAndRegister(JointDef def, GameGroup group) {

        group.registerJoint(def);

        BasicFactory.physicsWorld.createJoint(def);
    }

    public static void createJoint(JointDef def) {

        BasicFactory.physicsWorld.createJoint(def);
    }

    public MCMesh createMCMesh(ArrayList<Block> blocks, float x, float y, float rot, boolean initialVisibility) {
        ArrayList<LayerInfo> infos = new ArrayList<LayerInfo>();
        for (Block block : blocks) {//EACH BLOCK IS BlockA MESH LAYER


            float[] layerData = block.getLayerData();

            Integer[] temp = block.getVertexCount();
            int[] layersVertexCount = new int[temp.length];
            for (int i = 0; i < temp.length; i++)
                layersVertexCount[i] = temp[i];
            Color[] colors = block.getColors();


            LayerInfo li = new LayerInfo(layerData, colors, layersVertexCount, block.getID());
            infos.add(li);


        }


        MeshInfo info = new MeshInfo(infos);
        MCMesh mesh = new DCGameEntity(x, y, rot, info, "", initialVisibility);


        return mesh;
    }

    public DCGameEntity createGameEntity(ArrayList<Block> blocks, float x, float y, float rot) {


        return createGameEntity(blocks, x, y, rot, BodyDef.BodyType.DynamicBody, true);

    }

    public DCGameEntity createGameEntity(ArrayList<Block> blocks, float x, float y, float rot, boolean initialVisibility) {


        return createGameEntity(blocks, x, y, rot, BodyDef.BodyType.DynamicBody, initialVisibility);

    }

    public DCGameEntity createGameEntity(ArrayList<Block> blocks, float x, float y, float rot, BodyDef.BodyType type, boolean initialVisibility) {

        int IDCOUNT = 0;
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {

            Block block = iterator.next();
            block.setID(IDCOUNT);
            for (Block b : block.associatedBlocks)
                if (b.blockType == BlockType.IMAGE)
                    if (b.blockSubType == BlockImageSubType.DECORATION) {
                        b.secondaryID = IDCOUNT;
                    }


            IDCOUNT++;
        }
        final DCGameEntity entity = (DCGameEntity) createMCMesh(blocks, x, y, rot, initialVisibility);
        entity.ID = GameScene.world.entityIDS.getAndIncrement();

        Body body = createBody(entity, blocks, type, x, y, rot);

        entity.setBody(body);
        body.setUserData(entity);
        entity.setBlocks(blocks);

        //HALF CHILDEN
        PhysicsConnector physconnector = new PhysicsConnector(entity, body);
        GameEntityContactListener listener = new GameEntityContactListener(entity);
        BasicFactory.physicsWorld.registerPhysicsConnector(physconnector);

        entity.connector = physconnector;
        BasicFactory.physicsWorld.setContactListener(listener);
        entity.listener = listener;


        for (Block block : blocks) {
            for (Block associated : block.associatedBlocks) {
                associated.setPolarity(Polarity.NEUTRAL);

                if (associated.blockType == BlockType.IMAGE) {
                    if (associated.blockSubType == BlockImageSubType.STAIN)
                        entity.applyStain(associated);


                }

            }
        }


        return entity;

    }

    public Body createBody(DCGameEntity entity, ArrayList<Block> blocks, BodyDef.BodyType type, float x, float y, float rot) {

        Body body = BasicFactory.physicsWorld.createBody(new BodyDef());

        body.setType(type);

        for (Block block : blocks) {//EACH BLOCK IS BlockA MESH LAYER

            float density = block.getPROPERTIES().getDensity();
            float elasticity = block.getPROPERTIES().getRestitution();
            float friction = block.getPROPERTIES().getFriction();

            CollisionOption option = block.getPROPERTIES().getCollisionOptions();
            block.fixtures.clear();
            ArrayList<ArrayList<Vector2>> triangles = block.computeBodyTriangles();
            for (ArrayList<Vector2> fix : triangles) {

                FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction, false,
                        option.getCategory(), option.getMask(), (short) 0);

                PolygonShape fixtureShape = new PolygonShape();
                Vector2[] points = fix.toArray(new Vector2[fix.size()]);
                fixtureShape.set(points);


                fixtureDef.shape = fixtureShape;


                Fixture fixture = body.createFixture(fixtureDef);

                block.fixtures.add(fixture);
                fixture.setUserData(block.getID());


            }


            if (!block.hasGrid)
                block.finish();
            else {
                if (block.parentgrid != null) {


                    ArrayList<Grain> grains = new ArrayList<Grain>();
                    Iterator<Grain> iterator = block.parentgrid.allGrains.iterator();
                    while (iterator.hasNext()) {
                        Grain g = iterator.next();

                        if (block.testPoint(g.position.x / 32f, g.position.y / 32f)) {
                            grains.add(g);

                            g.setBlock(block);
                            iterator.remove();


                        }


                    }


                    //	Color color = Utils.getColor();
                    //		for(Grain g:grains)GameScene.plotter.drawPoint(g.position, color, 1, 1);
                    if (grains.size() > 0)
                        block.grid = new BlockGrid(grains, block.VERTICES, block.parentgrid.startup);
                    else {
                        block.finish();

                        for (Grain g : block.grid.allGrains) {
                            Grain nearest = getNearestGrain(g.position, block.parentgrid.reservoir);
                            g.copyValues(nearest);
                            g.setBlock(block);
                            for (Block b : block.associatedBlocks) b.GRAIN = g;
                        }


                    }

                    block.hasGrid = true;
                    block.parentgrid = null;

                } else {
                    //SHOULD BE IMPOSSIBLE
                }
            }
        }

        body.setTransform(x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, rot);
        return body;

    }

    public void updateEntity(final DCGameEntity entity, final DCGameEntity added, final Vector2 d) {
        final Body updatedBody = entity.getBody();
        added.getBody().setLinearVelocity(0, 0);
        added.getBody().setAngularVelocity(0);
        for (Block b : added.blocks)
            b.interpolate(added.getBody(), entity.getBody(), d);
        ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
                                                                     @Override
                                                                     public void run() {

                                                                         entity.updateBlocks(added.blocks);

                                                                         //updatedBody.applyLinearImpulse(d.cpy().mul(1000),p0);
                                                                         added.getBody().setActive(false);
                                                                         try {
                                                                             updateBody(updatedBody, added.blocks);

                                                                         } catch (Throwable t) {
                                                                             Log.e("Error", "issue");
                                                                         }


                                                                         added.detachSelf();


                                                                     }
                                                                 }

        );


    }

    public void updateBody(Body body, ArrayList<Block> blocks) throws Throwable {


        for (Block block : blocks) {//EACH BLOCK IS BlockA MESH LAYER
            block.done = true;
            float density = block.getPROPERTIES().getDensity();
            float elasticity = block.getPROPERTIES().getRestitution();
            float friction = block.getPROPERTIES().getFriction();
            block.fixtures.clear();

            ArrayList<ArrayList<Vector2>> triangles = block.computeBodyTrianglesThrow();

            for (ArrayList<Vector2> fix : triangles) {

                FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction);

                PolygonShape fixtureShape = new PolygonShape();
                Vector2[] points = fix.toArray(new Vector2[fix.size()]);
                fixtureShape.set(points);
                fixtureDef.shape = fixtureShape;

                Fixture fixture = body.createFixture(fixtureDef);

                block.fixtures.add(fixture);

//GameScene.world.registerFilter(fixture,null);
                fixture.setUserData(block.getID());
            }


        }


    }

    public Grain getNearestGrain(Vector2 v, HashSet<Grain> allGrains) {

        float mind = Float.MAX_VALUE;
        Grain result = null;
        for (Grain g : allGrains) {
            float distance = g.distance(v);
            if (distance < mind) {
                mind = distance;
                result = g;
            }
        }
        return result;
    }

    public void create(PhysicsWorld physicsWorld, VertexBufferObjectManager vbom, GameScene gameScene) {
        BasicFactory.physicsWorld = physicsWorld;
        this.vbom = vbom;
        this.scene = gameScene;
    }

    public GameGroup createGameGroup(ArrayList<ArrayList<Block>> blocks, float x, float y, float rot, ArrayList<Integer> zindexes, String string) {
        GameGroup group = new GameGroup(string);
        for (int I = 0; I < blocks.size(); I++) {
            ArrayList<Block> bodyBlocks = blocks.get(I);
            ArrayList<DCGameEntity> list = this.createGameGroup(bodyBlocks, x, y, rot, I, zindexes.get(I));

            group.addAll(list);
        }

        return group;
    }

    public ArrayList<DCGameEntity> createGameGroup(ArrayList<Block> blocks, float x, float y, float rot, int BODYID, int z) {

        int N = blocks.size();
        UnionFind unionFinder = new UnionFind(N);
        for (int i = 0; i < blocks.size(); i++) {
            for (int j = i + 1; j < blocks.size(); j++) {

                if (Utils.getRelativePositionOfLayers(blocks.get(i).getBlockPoints(), blocks.get(j).getBlockPoints()))
                    unionFinder.union(i, j);

            }
        }

        unionFinder.compute();

        Iterator<HashSet<Integer>> iterator = unionFinder.myDict.values().iterator();
        ArrayList<ArrayList<Block>> blockGrouping = new ArrayList<ArrayList<Block>>();
        int INDEX = 0;
        while (iterator.hasNext()) {
            blockGrouping.add(new ArrayList<Block>());
            HashSet<Integer> set = iterator.next();
            Iterator<Integer> setIterator = set.iterator();
            while (setIterator.hasNext()) {
                blockGrouping.get(INDEX).add(blocks.get(setIterator.next()));
            }
            INDEX++;
        }
        ArrayList<DCGameEntity> entities = new ArrayList<DCGameEntity>();

        for (int i = 0; i < blockGrouping.size(); i++) {
            DCGameEntity entity = createGameEntity(blockGrouping.get(i), x, y, rot);
            entity.getBody().setLinearVelocity(0, 0);
            entity.setInitial(BODYID);
            entity.setZIndex(z + i);

            entities.add(entity);

        }

        return entities;
    }

    public Block createBlock(ArrayList<Vector2> points, int MaterialID, int order, int ID) {
        LayerProperty layerProperty = new LayerProperty(MaterialID);
        return new Block(points, layerProperty, MaterialFactory.getInstance().materials.get(MaterialID).color, order, ID, true);
    }

    Block createBlock(ArrayList<Vector2> points, int MaterialID, int order, int ID, Color color) {
        LayerProperty layerProperty = new LayerProperty(MaterialID);
        return new Block(points, layerProperty, color, order, ID, true);
    }

    public Ragdoll createRagdoll(float cX, float cY) {

        Ragdoll ragdoll = new Ragdoll(BasicFactory.physicsWorld);
        float pixel = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
        float G = MyConstants.GOLDEN_RATIO;

        float HEAD_RAY = 6f;
        float ARM_LENGTH = 25 / 2f;
        float UPPERARM_CIR1 = 7 / 2f;
        float UPPERARM_CIR2 = 5 / 2f;
        float LOWERARM_CIR1 = 5 / 2f;
        float LOWERARM_CIR2 = 6 / 2f;
        float HAND_SIDE = 10 / 2f;
        float NECK_LENGTH = (G - 1) * HEAD_RAY;
        float TORSO_HEIGHT = G * (NECK_LENGTH + 2 * HEAD_RAY);
        float SHOULDER_WIDTH = 60 / 2f;
        float WAIST_WIDTH = 50 / 2f;
        float TORSO_ANGLE = (float) (Math.PI / 14);
        float TORSO_D = (float) (WAIST_WIDTH / 2 * Math.sin(TORSO_ANGLE));
        float UPPERLEG_CIR1 = 13 / 2f;
        float UPPERLEG_CIR2 = (G * G * HEAD_RAY) / (2 * (G + 1));
        float UPPERLEG_L1 = G * G * G * HEAD_RAY / (G + 1);
        float UPPERLEG_L2 = UPPERLEG_L1;

        float LOWERLEG_LENGTH1 = G * G * HEAD_RAY;
        float LOWERLEG_LENGTH2 = (G * G * HEAD_RAY) / (G + 1);
        float LOWERLEG_THICKNESS = 13 / 2f;
        float LOWERLEG_CIR1 = 7 / 2f;
        float LOWERLEG_CIR2 = (G * HEAD_RAY) / (G + 1);
        float FOOT_LENGTH = 18 / 2f;
        float RIGHT = SHOULDER_WIDTH / 2 - UPPERLEG_CIR1;


        float lowerLegHalfLength = (LOWERLEG_LENGTH1 + LOWERLEG_LENGTH2 + LOWERLEG_CIR2 + LOWERLEG_CIR1) / 2;
        float hLegLength = (UPPERLEG_L1 + UPPERLEG_L2 + UPPERLEG_CIR1 + UPPERLEG_CIR2) / 2;
        Color pullColor = new Color(0.63f, 0.63f, 0.63f);
        Color PantColor = new Color(0.06f, 0.2f, 0.6f);

        ArrayList<Vector2> points = VerticesFactory.createPolygon(0, 0, HEAD_RAY, HEAD_RAY, 4);
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));

        DCGameEntity head = createGameEntity(blocks, cX, cY + 4, 0);


        points = VerticesFactory.createSquare(HAND_SIDE);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));

        DCGameEntity rightHand = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 + 2 * ARM_LENGTH + UPPERARM_CIR1 + UPPERARM_CIR2 + LOWERARM_CIR1 + LOWERARM_CIR2, cY - HEAD_RAY - UPPERARM_CIR1, 0);

        points = VerticesFactory.createSquare(HAND_SIDE);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));

        DCGameEntity leftHand = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 - 2 * ARM_LENGTH - UPPERARM_CIR1 - UPPERARM_CIR2 - LOWERARM_CIR1 - LOWERARM_CIR2, cY - HEAD_RAY - UPPERARM_CIR1, 0);


        points = VerticesFactory.createShape6(LOWERLEG_CIR2, FOOT_LENGTH);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));

        DCGameEntity leftFoot = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 + UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - 2 * hLegLength - 2 * lowerLegHalfLength, 0);

        points = VerticesFactory.createShape6(LOWERLEG_CIR2, FOOT_LENGTH);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));
        DCGameEntity rightFoot = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 - UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - 2 * hLegLength - 2 * lowerLegHalfLength, (float) Math.PI);


        points = VerticesFactory.createShape1(ARM_LENGTH, UPPERARM_CIR1, UPPERARM_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, pullColor));
        DCGameEntity upperArmR = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 + ARM_LENGTH / 2 + UPPERARM_CIR1, cY - HEAD_RAY - UPPERARM_CIR1, (float) Math.PI / 2);


        points = VerticesFactory.createShape1(ARM_LENGTH, UPPERARM_CIR1, UPPERARM_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, pullColor));
        DCGameEntity upperArmL = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 - ARM_LENGTH / 2 - UPPERARM_CIR1, cY - HEAD_RAY - UPPERARM_CIR1, (float) -Math.PI / 2);


        points = VerticesFactory.createShape2(ARM_LENGTH, LOWERARM_CIR1, LOWERARM_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));
        DCGameEntity lowerArmR = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 + 3 * ARM_LENGTH / 2 + UPPERARM_CIR1 + UPPERARM_CIR2, cY - HEAD_RAY - UPPERARM_CIR1, (float) Math.PI / 2);

        points = VerticesFactory.createShape2(ARM_LENGTH, LOWERARM_CIR1, LOWERARM_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0));
        DCGameEntity lowerArmL = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 - 3 * ARM_LENGTH / 2 - UPPERARM_CIR1 - UPPERARM_CIR2, cY - HEAD_RAY - UPPERARM_CIR1, (float) -Math.PI / 2);


        points = VerticesFactory.createDistorted(SHOULDER_WIDTH, WAIST_WIDTH, TORSO_HEIGHT, 0, 0);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, pullColor));
        DCGameEntity upperTorso = createGameEntity(blocks, cX, cY - HEAD_RAY - TORSO_HEIGHT / 2, 0);

        points = VerticesFactory.createDistorted(WAIST_WIDTH, SHOULDER_WIDTH, TORSO_HEIGHT, 0, 0);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, pullColor));

        Vector2 v0 = new Vector2(-SHOULDER_WIDTH / 2, -TORSO_HEIGHT / 2);
        Vector2 v1 = new Vector2(SHOULDER_WIDTH / 2, -TORSO_HEIGHT / 2);
        Vector2 v2 = new Vector2(7 * SHOULDER_WIDTH / 16, 0);
        Vector2 v3 = new Vector2(-7 * SHOULDER_WIDTH / 16, 0);


        ArrayList<Vector2> points1 = new ArrayList<Vector2>();
        points1.add(v0);
        points1.add(v1);
        points1.add(v2);
        points1.add(v3);
        Block decoration1 = getInstance().createBlock(points1, 0, 0, 0);
        decoration1.setID(-1);
        decoration1.blockType = BlockType.IMAGE;
        decoration1.blockSubType = BlockImageSubType.DECORATION;
        decoration1.color = PantColor;

        blocks.get(0).associatedBlocks.add(decoration1);

        DCGameEntity lowerTorso = createGameEntity(blocks, cX, cY - HEAD_RAY - 3 * TORSO_HEIGHT / 2 + TORSO_D, 0);


        points = VerticesFactory.createShape4(UPPERLEG_L1, UPPERLEG_L2, UPPERLEG_CIR1, UPPERLEG_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, PantColor));
        DCGameEntity upperLegR = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 - UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - hLegLength, 0);

        points = VerticesFactory.createShape4(UPPERLEG_L1, UPPERLEG_L2, UPPERLEG_CIR1, UPPERLEG_CIR2);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, PantColor));
        DCGameEntity upperLegL = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 + UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - hLegLength, 0);


        points = VerticesFactory.createShape5(LOWERLEG_LENGTH1, LOWERLEG_LENGTH2, LOWERLEG_THICKNESS, LOWERLEG_CIR2, LOWERLEG_CIR1);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, PantColor));
        DCGameEntity lowerLegR = createGameEntity(blocks, cX + SHOULDER_WIDTH / 2 - UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - 2 * hLegLength - lowerLegHalfLength, 0);
        points = VerticesFactory.createShape5(LOWERLEG_LENGTH1, LOWERLEG_LENGTH2, LOWERLEG_THICKNESS, LOWERLEG_CIR2, LOWERLEG_CIR1);
        blocks = new ArrayList<Block>();
        blocks.add(createBlock(points, 10, 0, 0, PantColor));
        DCGameEntity lowerLegL = createGameEntity(blocks, cX - SHOULDER_WIDTH / 2 + UPPERLEG_CIR1, cY - HEAD_RAY - 2 * TORSO_HEIGHT - 2 * hLegLength - lowerLegHalfLength, 0);


        float sleeve = UPPERARM_CIR1 / 2 - ARM_LENGTH / 2 - LOWERARM_CIR2;


        RevoluteJointDef revoluteJointDef;

        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerArmR.getBody();
        revoluteJointDef.bodyB = rightHand.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, sleeve / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, HAND_SIDE / 2 / pixel));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (-Math.PI / 6);
        revoluteJointDef.upperAngle = (float) (Math.PI / 6);
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerArmL.getBody();
        revoluteJointDef.bodyB = leftHand.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, sleeve / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, HAND_SIDE / 2 / pixel));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (-Math.PI / 6);
        revoluteJointDef.upperAngle = (float) (Math.PI / 6);
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerLegR.getBody();
        revoluteJointDef.bodyB = rightFoot.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-lowerLegHalfLength - UPPERLEG_CIR2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2((-FOOT_LENGTH + 2 * LOWERLEG_CIR2) / 2 / pixel, 0));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) Math.PI;
        revoluteJointDef.upperAngle = (float) Math.PI;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerLegL.getBody();
        revoluteJointDef.bodyB = leftFoot.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-lowerLegHalfLength - UPPERLEG_CIR2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2((-FOOT_LENGTH + 2 * LOWERLEG_CIR2) / 2 / pixel, 0));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (Math.PI / 4);
        revoluteJointDef.upperAngle = (float) (Math.PI / 4);
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperLegR.getBody();
        revoluteJointDef.bodyB = lowerLegR.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-hLegLength + UPPERLEG_CIR2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, (lowerLegHalfLength - LOWERLEG_CIR1) / pixel));
        revoluteJointDef.collideConnected = false;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperLegL.getBody();
        revoluteJointDef.bodyB = lowerLegL.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-hLegLength + UPPERLEG_CIR2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, (lowerLegHalfLength - LOWERLEG_CIR1) / pixel));
        revoluteJointDef.collideConnected = false;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerTorso.getBody();
        revoluteJointDef.bodyB = upperLegR.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(RIGHT / pixel, -(TORSO_HEIGHT / 2 - UPPERLEG_CIR1 / 2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, (UPPERLEG_L1 + UPPERLEG_L2 + UPPERLEG_CIR1) / 2 / pixel));
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (-Math.PI / 2);
        revoluteJointDef.upperAngle = (float) (3 * Math.PI / 4);
        revoluteJointDef.collideConnected = false;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = lowerTorso.getBody();
        revoluteJointDef.bodyB = upperLegL.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(-RIGHT / pixel, -(TORSO_HEIGHT / 2 - UPPERLEG_CIR1 / 2) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, (UPPERLEG_L1 + UPPERLEG_L2 + UPPERLEG_CIR1) / 2 / pixel));
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (-3 / 4f * Math.PI);
        revoluteJointDef.upperAngle = (float) (Math.PI / 2);
        revoluteJointDef.collideConnected = false;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = head.getBody();
        revoluteJointDef.bodyB = upperTorso.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-HEAD_RAY - NECK_LENGTH) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, TORSO_HEIGHT / 2 / pixel));
        revoluteJointDef.collideConnected = true;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperTorso.getBody();
        revoluteJointDef.bodyB = lowerTorso.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, (-TORSO_HEIGHT / 2 + TORSO_D) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, TORSO_HEIGHT / 2 / pixel));
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = -TORSO_ANGLE;
        revoluteJointDef.upperAngle = TORSO_ANGLE;
        revoluteJointDef.collideConnected = false;


        float limitAy1 = -TORSO_HEIGHT / 2;
        float limitAy2 = limitAy1;
        float ratioA = (TORSO_HEIGHT - TORSO_D) / TORSO_HEIGHT;
        float limitAx1 = WAIST_WIDTH / 2 * ratioA;
        float limitAx2 = -limitAx1;
        float limitBy1 = TORSO_HEIGHT / 2;

        Vector2 limitA1 = new Vector2(limitAx1, limitAy1);
        Vector2 limitA2 = new Vector2(limitAx2, limitAy2);
        Vector2 limitB1 = new Vector2(limitAx1, limitBy1);
        Vector2 limitB2 = new Vector2(limitAx2, limitBy1);


        createJointAndRegister(revoluteJointDef, ragdoll, limitA1, limitA2, limitB1, limitB2);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperTorso.getBody();
        revoluteJointDef.bodyB = upperArmR.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(SHOULDER_WIDTH / 2 / pixel, (TORSO_HEIGHT / 2 - 6 / 2f) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, ARM_LENGTH / 2 / pixel));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) 0;
        revoluteJointDef.upperAngle = (float) Math.PI;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperArmR.getBody();
        revoluteJointDef.bodyB = lowerArmR.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, -ARM_LENGTH / 2 / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, ARM_LENGTH / 2 / pixel));
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) 0;
        revoluteJointDef.upperAngle = (float) Math.PI;
        revoluteJointDef.collideConnected = false;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperTorso.getBody();
        revoluteJointDef.bodyB = upperArmL.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(-SHOULDER_WIDTH / 2 / pixel, (TORSO_HEIGHT / 2 - 6 / 2f) / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, ARM_LENGTH / 2 / pixel));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) -Math.PI;
        revoluteJointDef.upperAngle = (float) 0;
        createJointAndRegister(revoluteJointDef, ragdoll);


        revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = upperArmL.getBody();
        revoluteJointDef.bodyB = lowerArmL.getBody();
        revoluteJointDef.localAnchorA.set(new Vector2(0, -ARM_LENGTH / 2 / pixel));
        revoluteJointDef.localAnchorB.set(new Vector2(0, ARM_LENGTH / 2 / pixel));
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) -Math.PI;
        revoluteJointDef.upperAngle = (float) 0;
        createJointAndRegister(revoluteJointDef, ragdoll);


        ragdoll.setBodyParts(head, upperTorso, lowerTorso, upperArmR, lowerArmR, upperArmL, lowerArmL, upperLegR, lowerLegR, upperLegL, lowerLegL, rightHand, leftHand, leftFoot, rightFoot);

        ragdoll.addChild(head);
        ragdoll.addChild(upperArmR);
        ragdoll.addChild(lowerArmR);
        ragdoll.addChild(upperArmL);
        ragdoll.addChild(lowerArmL);
        ragdoll.addChild(upperTorso);
        ragdoll.addChild(lowerTorso);
        ragdoll.addChild(upperLegR);
        ragdoll.addChild(upperLegL);
        ragdoll.addChild(lowerLegR);
        ragdoll.addChild(lowerLegL);
        ragdoll.addChild(leftFoot);
        ragdoll.addChild(rightFoot);
        ragdoll.addChild(rightHand);
        ragdoll.addChild(leftHand);
        head.name = "Doll";
        upperArmR.name = "Doll";
        lowerArmR.name = "Doll";
        upperArmL.name = "Doll";
        lowerArmL.name = "Doll";
        upperTorso.name = "Doll";
        lowerTorso.name = "Doll";
        upperLegR.name = "Doll";
        upperLegL.name = "Doll";
        lowerLegR.name = "Doll";
        lowerLegL.name = "Doll";
        leftFoot.name = "Doll";
        rightFoot.name = "Doll";
        rightHand.name = "Doll";
        leftHand.name = "Doll";


        ragdoll.setZIndex(9999);
        return ragdoll;


    }

    private void createJointAndRegister(JointDef def,
                                        GameGroup group, Vector2 limitA1, Vector2 limitA2, Vector2 limitB1,
                                        Vector2 limitB2) {
        group.registerJoint(def, limitA1, limitA2, limitB1, limitB2);
        BasicFactory.physicsWorld.createJoint(def);
    }

    public DCGameEntity createPo() {
        ArrayList<Vector2> points = new ArrayList<Vector2>();
        ArrayList<Block> blocks = new ArrayList<Block>();
        points = VerticesFactory.createRectangle(50, 50);

        blocks.add(createBlock(points, 0, 0, 0));


        DCGameEntity entity = createGameEntity(blocks, 400, 15, 0, BodyDef.BodyType.DynamicBody, true);


        return entity;

    }

    public GameGroup createGround() {
        ArrayList<Vector2> points = new ArrayList<Vector2>();
        ArrayList<Block> blocks = new ArrayList<Block>();
        points = VerticesFactory.createRectangle(32 * 8, 50);

        blocks.add(createBlock(points, 3, 0, 0));
        points = VerticesFactory.createRectangle(32 * 6, 40);
        blocks.add(createBlock(points, 0, 0, 0));
        DCGameEntity entity = createGameEntity(blocks, 400, -10, 0, BodyDef.BodyType.StaticBody, true);

        entity.name = "Gstatic";


        GameGroup group = new GameGroup("");
        group.addChild(entity);


        return group;
    }

    public GameGroup createGround2() {


        ArrayList<Vector2> points2 = new ArrayList<Vector2>();
        ArrayList<Block> blocks2 = new ArrayList<Block>();
        points2 = VerticesFactory.createRectangle(32 * 12, 40);
        blocks2.add(createBlock(points2, 10, 0, 0));

        points2 = VerticesFactory.createRectangle(32 * 10, 20);
        //blocks2.add(createBlock(points2,0, 1, 1));
        DCGameEntity entity2 = createGameEntity(blocks2, 400, 100, 0, BodyDef.BodyType.DynamicBody, true);


        entity2.name = "Gdynamic";
        GameGroup group = new GameGroup("");

        group.addChild(entity2);


        return group;
    }


    public DCGameEntity createBullet() {
        ArrayList<Vector2> points = new ArrayList<Vector2>();
        ArrayList<Block> blocks = new ArrayList<Block>();
        points.add(new Vector2(-145, 203));
        points.add(new Vector2(-135, 203));
        points.add(new Vector2(-132, 201));
        points.add(new Vector2(-135, 199));
        points.add(new Vector2(-145, 199));

        blocks.add(createBlock(points, 5, 0, 0));


        DCGameEntity entity = createGameEntity(blocks, 0, 0, 0, BodyDef.BodyType.DynamicBody, true);
        entity.smart = 99;
        entity.getBody().setLinearVelocity(20 * 60, 0);
        entity.getBody().setBullet(true);
        entity.qe = 100000;

        return entity;
    }


    public DCGameEntity createTest() {
        ArrayList<Vector2> points = new ArrayList<Vector2>();
        ArrayList<Block> blocks = new ArrayList<Block>();
        points.add(new Vector2(0, 0));
        points.add(new Vector2(0, 100));
        points.add(new Vector2(100, 80));
        points.add(new Vector2(120, 100));
        points.add(new Vector2(130, 0));

        blocks.add(createBlock(points, 1, 0, 0));
        return createGameEntity(blocks, 400, 480, 0, BodyDef.BodyType.DynamicBody, true);

    }


}
