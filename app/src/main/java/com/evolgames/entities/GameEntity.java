package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.mesh.batch.TexturedMeshBatch;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.particles.wrappers.FireParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.entities.usage.Use;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;

import org.andengine.entity.primitive.Line;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GameEntity extends EntityWithBody {

    private final GameScene gameScene;
    public boolean changed = true;
    private float area;
    private SpecialEntityType type = SpecialEntityType.Default;
    private GameGroup parentGroup;
    private boolean alive = true;
    private TexturedMeshBatch batch;
    private String name;
    private final ArrayList<LayerBlock> layerBlocks;
    private final List<Use> useList;
    private MosaicMesh mesh;
    private int stainDataLimit;
    private Vector2 center;
    private boolean isBatcherSetup = false;
    private int hangedPointerId;
    private FireParticleWrapperWithPolygonEmitter fireParticleWrapperWithPolygonEmitter;
    private boolean isFireSetup;
    private GameEntity parentGameEntity;


    public GameScene getGameScene() {
        return gameScene;
    }

    public GameEntity(MosaicMesh mesh, GameScene scene, String entityName, ArrayList<LayerBlock> layerBlocks) {
        super();
        this.mesh = mesh;
        this.gameScene = scene;
        this.name = entityName;
        this.layerBlocks = layerBlocks;
        this.useList = new ArrayList<>();

        for (LayerBlock layerBlock : this.layerBlocks) {
            layerBlock.setGameEntity(this);
        }

        for (LayerBlock layerBlock : this.layerBlocks) {
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
        for (LayerBlock layerBlock : layerBlocks) {
            area += layerBlock.getBlockArea();
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

    public void setupFire() {
        isFireSetup = true;
        fireParticleWrapperWithPolygonEmitter = new FireParticleWrapperWithPolygonEmitter(this);
        gameScene.attachChild(fireParticleWrapperWithPolygonEmitter.getParticleSystem());
        gameScene.sortChildren();

    }


    private void setupBatcher() {
        isBatcherSetup = true;
        float area = 0;
        for (LayerBlock b : layerBlocks) {
            area += b.getBlockArea();
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
        return layerBlocks;
    }


    private void updateGrains() {
        for (LayerBlock block : this.getBlocks()) {
            for (CoatingBlock grain : block.getBlockGrid().getCoatingBlocks()) {
                grain.update();
            }
        }
    }

    public void onStep(float timeStep) {
        if (!this.getName().equals("Ground")) {
            updateGrains();
        }
        for (Use use : useList) {
            use.onStep(timeStep);
        }

        if (isFireSetup) {
            fireParticleWrapperWithPolygonEmitter.update();
        }
    }


    public Vector2 computeTouch(TouchEvent touch) {
        Vector2 t = this.body.getLocalPoint(new Vector2(touch.getX()/32f, touch.getY()/32f)).cpy().mul(32f);
        Vector2 result = null;
        float minDis = Float.MAX_VALUE;
        for (Block<?, ?> block : layerBlocks) {
            Vector2 point = GeometryUtils.calculateProjection(t, block.getVertices());
            if(point!=null) {
                float dis = t.dst(point);
                if(dis<minDis){
                    minDis = dis;
                    result = point;
                }
            }
        }
        if(result!=null) {
            return body.getWorldPoint(result.cpy().mul(1/32f)).cpy().mul(32f);
        }
        return null;
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
            if (current.getBody() != null) {
                for (JointEdge edge : current.getBody().getJointList()) {
                    GameEntity entity = (GameEntity) edge.other.getUserData();
                    if (entity != null)
                        if (!entities.contains(entity)) deque.push(entity);
                }
            }
        }

        float mass = 0;
        for (GameEntity e : entities) {
            if (e.getBody() != null) {
                mass += e.getBody().getMass();
            }
        }
        return mass;

    }


    public boolean isNonValidStainPosition(LayerBlock block, StainBlock stainBlock) {
        float x = stainBlock.getLocalCenterX();
        float y = stainBlock.getLocalCenterY();
        for (Block<?, ?> b : block.getAssociatedBlocks()) {
            if (b instanceof StainBlock) {
                StainBlock other = (StainBlock) b;
                float distance = GeometryUtils.dst(x, y, other.getLocalCenterX(), other.getLocalCenterY());
                if (distance < 2f) {
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
        for (LayerBlock b : layerBlocks) {
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
        for (LayerBlock b : layerBlocks) {
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
        while (stainDataCount > stainDataLimit) {
            StainBlock removedStainBlock = allStains.get(0);
            for (LayerBlock b : layerBlocks) {
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


    public void detach() {
        mesh.detachSelf();
        if (fireParticleWrapperWithPolygonEmitter != null) {
            gameScene.getWorldFacade().removeFireParticleWrapper(fireParticleWrapperWithPolygonEmitter);
        }
        dispose();
    }

    public int getHangedPointerId() {
        return hangedPointerId;
    }

    public void setHangedPointerId(int hangedPointerId) {
        this.hangedPointerId = hangedPointerId;
    }


    public List<Use> getUseList() {
        return useList;
    }

    public void setParentGameEntity(GameEntity parentGameEntity) {
        this.parentGameEntity = parentGameEntity;
    }

    public GameEntity getParentGameEntity() {
        return parentGameEntity;
    }

    public void createJuiceSources() {
        for (LayerBlock block : this.getBlocks()) {
            LayerProperties properties = block.getProperties();
            if (properties.isJuicy()) {
                ArrayList<FreshCut> freshCuts = block.getFreshCuts();
                for (FreshCut freshCut : freshCuts) {
                    if (freshCut.getLength() < 1f) {
                        continue;
                    }
                    if(freshCut.getLimit()>0) {
                        gameScene.getWorldFacade().createJuiceSource(this, block, freshCut);
                    }
                    if (freshCut instanceof SegmentFreshCut) {
                        SegmentFreshCut segmentFreshCut = (SegmentFreshCut) freshCut;
                        if (segmentFreshCut.isInner()) {
                            Line line = new Line(segmentFreshCut.first.x, segmentFreshCut.first.y, segmentFreshCut.second.x, segmentFreshCut.second.y, 2, ResourceManager.getInstance().vbom);
                            line.setColor(Color.RED);
                            mesh.attachChild(line);
                        }
                    }
                }
            }
        }
    }

    public  <T extends Use> T getUsage(Class<T> targetType) {
        for (Use obj : this.useList) {
            if (targetType.isInstance(obj)) {
                return (T) obj;
            }
        }
        return null;
    }
    public  <T> boolean hasUsage(Class<T> targetType) {
        for (Use obj : this.useList) {
            if (targetType.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }
}
