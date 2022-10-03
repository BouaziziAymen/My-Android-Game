package com.evolgames.entities;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.particles.wrappers.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.entities.mesh.batch.TexturedMeshBatch;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.GameScene;

import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import is.kul.learningandengine.graphicelements.Layer;


public class GameEntity extends EntityWithBody {

    private final GameScene gameScene;
    public boolean changed = true;
    Color color = Utils.getRandomColor();
    private float area;
    private SpecialEntityType type = SpecialEntityType.Default;
    private GameGroup parentGroup;
    private boolean alive = true;
    private FireParticleWrapperWithPolygonEmitter fireParticleWrapperWithPolygonEmitter;
    private boolean isFireSetup;
    private boolean projectile;
    private TexturedMeshBatch batch;
    private String name;
    private ArrayList<LayerBlock> blocks;
    private ArrayList<GameEntity> children;
    private List<Trigger> triggers;
    private MosaicMesh mesh;
    private int stainDataLimit;
    private Vector2 center;
    private boolean isBatcherSetup = false;
    private int hangedPointerId;


    public GameEntity(MosaicMesh mesh, GameScene scene, String entityName, ArrayList<LayerBlock> blocks) {
        super();
        this.mesh = mesh;
        this.gameScene = scene;
        this.name = entityName;
        this.blocks = blocks;

        for (LayerBlock layerBlock : this.blocks) {
                layerBlock.setGameEntity(this);
        }

        for (LayerBlock layerBlock : this.blocks) {
                for (Block<?, ?> decorationBlock : layerBlock.getAssociatedBlocks()) {
                    if (decorationBlock instanceof CoatingBlock) {
                        CoatingBlock coatingBlock = ((CoatingBlock) decorationBlock);
                        coatingBlock.setMesh(mesh);
                    }
                }
        }

        computeArea();
        if (area >= 5) {
            if (hasStains()) {
                setupBatcher();
            }
        }
    }

    public float getArea() {
        return area;
    }

    private void computeArea() {
        this.area = 0;
        for (LayerBlock layerBlock: blocks) {
                area += layerBlock.getArea();
        }
    }

    public SpecialEntityType getType() {
        return type;
    }

    public void setType(SpecialEntityType type) {
        this.type = type;
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
        for (LayerBlock b : blocks){
                area += b.getArea();
        }
        int stainLimit = (int) (area / 32);
        if (stainLimit < 1) {
            stainLimit = 1;
        }
        stainDataLimit = Math.min(3000 * 64, stainLimit * 64 * 8);


        batch = new TexturedMeshBatch(ResourceManager.getInstance().texturedMesh, stainDataLimit + 12, ResourceManager.getInstance().vbom);
        mesh.attachChild(batch);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<LayerBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<LayerBlock> blocks) {
        this.blocks = blocks;
    }


    public void onStep(float timeStep) {
        if (triggers != null) {
            this.triggers.forEach(t -> t.onStep(timeStep));
        }

       /* for (NonRotatingChild entity : this.nonRotatingChildren) {
            if (!entity.isIgnoreUpdate() && getBody() != null) {
                Vector2 worldPosition = getBody().getWorldPoint(new Vector2(entity.x0, entity.y0).mul(1 / 32f)).cpy().mul(32);
                if (entity.type == NRType.EMITTER) {
                    FireParticleWrapper system = (FireParticleWrapper) entity;
                    system.setPositionOfEmitter(worldPosition.x, worldPosition.y);

                } else entity.setPosition(worldPosition.x, worldPosition.y);

            }
        }*/
        if (isFireSetup) fireParticleWrapperWithPolygonEmitter.update();

    }


    public boolean computeTouch(TouchEvent touch) {

        float[] converted = mesh.convertSceneCoordinatesToLocalCoordinates(touch.getX(), touch.getY());
        for (Block<?,?> block : blocks) {
            if (GeometryUtils.PointInPolygon(converted[0], converted[1], block.getVertices())) {
                return true;
            }
        }
        return false;
    }

    private LayerBlock getNearestBlockA(Vector2 anchor) {
        float x = anchor.x * 32;
        float y = anchor.y * 32;
        float distance = Float.MAX_VALUE;
        LayerBlock result = null;
        for (LayerBlock block : blocks) {
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
                if (entity != null)
                    if (!entities.contains(entity)) deque.push(entity);
            }
        }

        float mass = 0;
        for (GameEntity e : entities) mass += e.getBody().getMass();
        Log.e("mass", "" + mass);
        return mass;

    }


    public boolean stainHasTwin(LayerBlock block, StainBlock stainBlock) {
        float x = stainBlock.getLocalCenterX();
        float y = stainBlock.getLocalCenterY();
        for (Block<?, ?> b : block.getAssociatedBlocks()) {
            if (b instanceof StainBlock) {
                StainBlock sblock = (StainBlock) b;
                float distance = GeometryUtils.dst(x, y, sblock.getLocalCenterX(), sblock.getLocalCenterY());
                if (distance < 1f) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addStain(LayerBlock block, StainBlock stainBlock) {
        block.addAssociatedBlock(stainBlock);
        changed = true;
        if (!isBatcherSetup) {
            setupBatcher();
        }
    }

    private boolean hasStains() {
        for (LayerBlock b : blocks) {
                for (Block<?, ?> decorationBlock : b.getAssociatedBlocks()) {
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
            if (b.getData() == null) {
                b.computeData();
            }
            count += b.getData().length;
        }
        return count;
    }

    private ArrayList<StainBlock> getStains() {
        ArrayList<StainBlock> result = new ArrayList<>();
        for (LayerBlock b : blocks) {
                for (Block<?, ?> decorationBlock : b.getAssociatedBlocks()) {
                    if (decorationBlock instanceof StainBlock) {
                        result.add((StainBlock) decorationBlock);
                    }
                }
        }
        return result;
    }

    public void redrawStains() {
        if (batch == null || !changed) {
            return;
        }

        batch.reset();
        ArrayList<StainBlock> allStains = getStains();
        int stainDataCount = getStainDataCount(allStains);
        while (stainDataCount >= stainDataLimit) {
            StainBlock removedStainBlock = allStains.get(0);
            for (LayerBlock b : blocks) {
                    if (b.getAssociatedBlocks().contains(removedStainBlock)) {
                        b.getAssociatedBlocks().remove(removedStainBlock);
                        break;
                    }
            }
            stainDataCount -= removedStainBlock.getData().length;
            allStains.remove(removedStainBlock);
        }


        for (LayerBlock b : getBlocks()) {
                ArrayList<? extends Block<?, ?>> associatedBlocks = b.getAssociatedBlocks();
                for (Block<?, ?> decorationBlock : associatedBlocks) {
                    if (decorationBlock instanceof StainBlock) {
                        StainBlock stain = (StainBlock) decorationBlock;
                        Color color = stain.getProperties().getColor();
                        batch.draw(stain.getTextureRegion(), stain.getData(), color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                    }
                }
        }
        batch.submit();
        changed = false;
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

    public int getHangedPointerId() {
        return hangedPointerId;
    }

    public void setHangedPointerId(int hangedPointerId) {
        this.hangedPointerId = hangedPointerId;
    }


    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public boolean hasTriggers() {
        if (this.triggers == null) {
            return false;
        }
        return this.triggers.size() > 0;
    }

    public void onTriggerPushed() {
        if (!hasTriggers()) return;
        Trigger trigger = triggers.get(0);
        trigger.onTriggerPulled();
    }

    public void onTriggerReleased() {
        if (!hasTriggers()) return;
        Trigger trigger = triggers.get(0);
        trigger.onTriggerReleased();
    }

}
