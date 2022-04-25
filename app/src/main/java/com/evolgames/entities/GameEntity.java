package com.evolgames.entities;

import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.JointZoneBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.joint.JointKey;
import com.evolgames.entities.particles.FireParticleWrapper;
import com.evolgames.entities.particles.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.mesh.batch.TexturedMeshBatch;
import com.evolgames.mesh.mosaic.MosaicMesh;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.JointBlueprint;
import com.evolgames.scenes.GameScene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;


public class GameEntity extends EntityWithBody{

    Color color = Utils.getRandomColor();
    private SpecialEntityType type = SpecialEntityType.Default;
    private GameGroup parentGroup;
    private boolean alive = true;
    private FireParticleWrapperWithPolygonEmitter fireParticleWrapperWithPolygonEmitter;
    private boolean isFireSetup;
    private boolean projectile;
    private TexturedMeshBatch batch;
    private String name;
    private ArrayList<BlockA> blocks;
    private ArrayList<GameEntity> children;
    private GameScene gameScene;
    private MosaicMesh mesh;
    private int stainDataLimit;
    private ArrayList<NonRotatingChild> nonRotatingChildren;
    private Vector2 center;
    public boolean changed = true;
    private boolean isBatcherSetup = false;
    private int hangedPointerId;

    public SpecialEntityType getType() {
        return type;
    }

    public void setType(SpecialEntityType type) {
        this.type = type;
    }

    public GameEntity(MosaicMesh mesh, GameScene scene, String entityName, ArrayList<BlockA> blocks) {
        super();
        this.mesh = mesh;
        this.gameScene = scene;
        this.name = entityName;
        this.blocks = blocks;
        for (BlockA block : blocks) block.setGameEntity(this);

        nonRotatingChildren = new ArrayList<>();
        /* #####
        for (BlockA b : blocks) {
            for (CoatingBlock g : b.getBlockGrid().getCoatingBlocks()) {
                if (g.getFireParticle() != null) {
                    g.getFireParticle().setParentGameEntity(this);
                    nonRotatingChildren.add(g.getFireParticle());
                }
            }
        }

         */
        for (BlockA block : blocks) {
            for (Block<?,?> decorationBlock : block.getAssociatedBlocks()) {
                if (decorationBlock instanceof CoatingBlock) {
                    CoatingBlock coatingBlock = ((CoatingBlock) decorationBlock);
                    coatingBlock.setMesh(mesh);
                }
            }
        }

        float area = 0;
        for (BlockA block : blocks) {
            area += block.getArea();
        }
        if (area >= 5)
            if (hasStains()) setupBatcher();
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



    public boolean isProjectile() {
        return projectile;
    }

    public void setProjectile(boolean projectile) {
        this.projectile = projectile;
    }

    public void setupFire() {
        isFireSetup = true;
        fireParticleWrapperWithPolygonEmitter = new FireParticleWrapperWithPolygonEmitter(this);
        gameScene.attachChild(fireParticleWrapperWithPolygonEmitter.getParticleSystem());
        gameScene.sortChildren();

    }

    private void setupBatcher() {
        isBatcherSetup = true;
        float area = 0;
        for (BlockA b : blocks) area += b.getArea();
        int stainLimit = (int) (area / 32);
        if (stainLimit < 1) stainLimit = 1;
        stainDataLimit = Math.min(3000*64,stainLimit * 64 * 8);


        batch = new TexturedMeshBatch(ResourceManager.getInstance().texturedMesh, stainDataLimit + 12, ResourceManager.getInstance().vbom);
        mesh.attachChild(batch);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BlockA> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<BlockA> blocks) {
        this.blocks = blocks;
    }


    public void update() {

        for (NonRotatingChild entity : this.nonRotatingChildren) {
            if (!entity.isIgnoreUpdate() && getBody() != null) {
                Vector2 worldPosition = getBody().getWorldPoint(new Vector2(entity.x0, entity.y0).mul(1 / 32f)).cpy().mul(32);
                if (entity.type == NRType.EMITTER) {
                    FireParticleWrapper system = (FireParticleWrapper) entity;
                    system.setPositionOfEmitter(worldPosition.x, worldPosition.y);

                } else entity.setPosition(worldPosition.x, worldPosition.y);

            }
        }
        if (isFireSetup) fireParticleWrapperWithPolygonEmitter.update();


    }

    public boolean computeTouch(TouchEvent touch) {

        float[] converted = mesh.convertSceneCoordinatesToLocalCoordinates(touch.getX(), touch.getY());
        for (BlockA block : blocks) {
            if (GeometryUtils.PointInPolygon(converted[0], converted[1], block.getVertices())) {
                Log.e("diagnose", "*********************************" + getName());
                for (BlockA blockA : blocks) {
                    // Log.e("diagnose","-------------------");
                    //Log.e("diagnose","blockA:"+blockA);
                    //for(Block decorationBlock:blockA.getAssociatedBlocks())
                    //  Log.e("diagnose","dec:"+decorationBlock);
                }


                return true;
            } else {
            }
        }
        return false;
    }

    private BlockA getNearestBlockA(Vector2 anchor) {
        float x = anchor.x * 32;
        float y = anchor.y * 32;
        float distance = Float.MAX_VALUE;
        BlockA result = null;
        for (BlockA block : blocks) {
            float d = GeometryUtils.distBetweenPointAndPolygon(x, y, block.getVertices());
            if (d < distance) {
                distance = d;
                result = block;
            }
        }
        return result;
    }

    public ArrayList<GameEntity> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<GameEntity> children) {
        this.children = children;
    }

    public void putKey(JointBlueprint command, JointKey.KeyType type, Vector2 anchor) {
        BlockA nearest = getNearestBlockA(anchor);
        Vector2 anchorConverted = obtain(anchor.x * 32, anchor.y * 32);
        nearest.addKey(new JointKey(command, type, anchorConverted));
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
            for (JointEdge edge : current.getBody().getJointList()) {
                GameEntity entity = (GameEntity) edge.other.getUserData();
                if(entity!=null)
                if (!entities.contains(entity)) deque.push(entity);
            }
        }

        float mass = 0;
        for (GameEntity e : entities) mass += e.getBody().getMass();
        Log.e("mass", "" + mass);
        return mass;

    }


    public boolean stainHasTwin(BlockA block, StainBlock stainBlock){
        float x = stainBlock.getLocalCenterX();
        float y = stainBlock.getLocalCenterY();
        for(Block<?,?> b:block.getAssociatedBlocks()){
            if(b instanceof StainBlock){
                StainBlock sblock = (StainBlock)b;
                    float distance = GeometryUtils.dst(x, y, sblock.getLocalCenterX(), sblock.getLocalCenterY());
                    if (distance < 1f) {
                        return true;
                }
            }
        }
        return false;
    }

    public void addStain(BlockA block, StainBlock stainBlock) {
        block.addAssociatedBlock(stainBlock);
        changed = true;
        if (!isBatcherSetup) setupBatcher();
    }

    private boolean hasStains() {
        for (BlockA b : blocks) {
            for (Block decorationBlock : b.getAssociatedBlocks()) {
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
            if (b.getData() == null) b.computeData();
            count += b.getData().length;
        }
        return count;
    }

    private ArrayList<StainBlock> getStains() {
        ArrayList<StainBlock> result = new ArrayList<>();
        for (BlockA b : blocks) {
            for (Block decorationBlock : b.getAssociatedBlocks()) {
                if (decorationBlock instanceof StainBlock) {
                    result.add((StainBlock) decorationBlock);
                }
            }
        }
        return result;
    }

    public void redrawStains() {
        if (batch == null || !changed) return;
        changed = false;

        batch.reset();
        ArrayList<StainBlock> allStains = getStains();
        while (getStainDataCount(allStains) >= stainDataLimit) {
            StainBlock removedStainBlock = allStains.get(0);
            for (BlockA b : blocks) {
                if (b.getAssociatedBlocks().contains(removedStainBlock)) {
                    b.getAssociatedBlocks().remove(removedStainBlock);
                    break;
                }
            }
            allStains.remove(removedStainBlock);
        }


        for (BlockA blockA : getBlocks()) {
            ArrayList<? extends Block<?,?>> associatedBlocks = blockA.getAssociatedBlocks();
            for (Block<?,?> decorationBlock : associatedBlocks) {
                if (decorationBlock instanceof StainBlock) {
                    StainBlock stain = (StainBlock) decorationBlock;
                    Color color = stain.getProperties().getColor();
                    batch.draw(stain.getTextureRegion(), stain.getData(), color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }
            }
        }

        batch.submit();
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

    public ArrayList<NonRotatingChild> getNonRotatingChildren() {
        return nonRotatingChildren;
    }

    public void test_draw() {
        if (true) return;
        getMesh().detachChildren();
        for (BlockA b : blocks) {
            for (Block c : b.getAssociatedBlocks())
                if (c instanceof JointZoneBlock)
                    Utils.drawPathOnEntity(c.getVertices(), Color.RED, getMesh());
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Color getColor() {
        return color;
    }



    public void detach() {
        mesh.detachSelf();
        if (fireParticleWrapperWithPolygonEmitter != null)
            WorldFacade.removeFireParticleWrapper(fireParticleWrapperWithPolygonEmitter);
        dispose();
    }


    public void setHangedPointerId(int hangedPointerId) {
        this.hangedPointerId = hangedPointerId;
    }

    public int getHangedPointerId() {
        return hangedPointerId;
    }
}
