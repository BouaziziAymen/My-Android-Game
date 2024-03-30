package com.evolgames.entities.basics;

import android.util.Log;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.blocks.AssociatedBlock;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.mesh.batch.TexturedMeshBatch;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.particles.wrappers.FireParticleWrapper;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.serialization.infos.InitInfo;
import com.evolgames.entities.usage.Use;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.utilities.GeometryUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.LineStrip;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.util.adt.color.Color;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameEntity extends EntityWithBody {

    private final List<LayerBlock> layerBlocks;
    public boolean changed = true;
    // Define the vertex shader
// Define the vertex shader
    String vertexShader =
            "uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
                    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "void main() {\n" +
                    "    gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
                    "}\n";

    // Define the fragment shader
    String fragmentShader =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0); // Green color\n" +
                    "}\n";
    private Body mirrorBody;
    private boolean mirrorCreated = false;
    private PhysicsScene<?> scene;
    private List<Use> useList;
    private String name;
    private GameGroup parentGroup;
    private Vector2 center;
    private FireParticleWrapper fireParticleWrapper;
    private boolean isFireSetup;
    private TexturedMeshBatch batch;
    private int stainDataLimit;
    private boolean isBatcherSetup = false;
    private SpecialEntityType type = SpecialEntityType.Default;
    private float sortingValue;
    private float area;
    private boolean alive = true;
    private MosaicMesh mesh, mirrorMesh;
    private BodyDef.BodyType bodyType;
    private InitInfo initInfo;
    private String uniqueID = UUID.randomUUID().toString();
    private boolean mirrored;
    private Entity outline;
    private int zIndex;
    private boolean outlined;

    public GameEntity(
            MosaicMesh mesh, PhysicsScene<?> scene, String entityName, List<LayerBlock> layerBlocks) {
        super();
        this.mesh = mesh;
        this.scene = scene;
        this.name = entityName;
        this.layerBlocks = layerBlocks;
        this.useList = new ArrayList<>();

        computeArea();
        if (area >= 5) {
            if (hasStains()) {
                setupBatcher();
            }
        }
    }

    public boolean isOutlined() {
        return outlined;
    }

    public void outlineEntity() {
        this.outlined = true;
        if(this.outline==null) {
            this.outline = new Entity();
            scene.attachChild(this.outline);
            PhysicsConnector physicsConnector = new PhysicsConnector(outline, body);
            scene.getPhysicsWorld().registerPhysicsConnector(physicsConnector);
        }
        for(LayerBlock layerBlock:layerBlocks){
         drawPolygon(layerBlock.getVertices(),Color.WHITE);
        }

        mesh.setZIndex(10000);
        outline.setZIndex(mesh.getZIndex() - 1);
        outline.setVisible(true);
        scene.sortChildren();
    }

    public void drawPolygon(List<Vector2> polygon, Color color) {
        LineStrip lineStrip = new LineStrip(0,0,polygon.size()+1,ResourceManager.getInstance().vbom);
        lineStrip.setColor(color);
        lineStrip.setLineWidth(3f);
       for(Vector2 v:polygon) {
           lineStrip.add(v.x, v.y);
       }
        lineStrip.add(polygon.get(0).x, polygon.get(0).y);
       outline.attachChild(lineStrip);
    }

    public void hideOutline() {
        outlined = false;
        if (outline == null) {
            return;
        }
        outline.setVisible(false);
        mesh.setZIndex(zIndex);
        scene.sortChildren();
    }

    public PhysicsScene<?> getScene() {
        return scene;
    }

    public void setScene(PhysicsScene<?> scene) {
        this.scene = scene;
    }

    private void computeArea() {
        this.area = 0;
        for (LayerBlock layerBlock : layerBlocks) {
            area += layerBlock.getBlockArea();
        }
    }

    public GameGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(GameGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public FireParticleWrapper getFireParticleWrapperWithPolygonEmitter() {
        return fireParticleWrapper;
    }

    public boolean isFireSetup() {
        return isFireSetup;
    }

    public void setupFire() {
        isFireSetup = true;
        fireParticleWrapper = new FireParticleWrapper(this);
        scene.attachChild(fireParticleWrapper.getParticleSystem());
        scene.sortChildren();
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
        batch =
                new TexturedMeshBatch(
                        ResourceManager.getInstance().texturedMesh,
                        stainDataLimit + 12,
                        ResourceManager.getInstance().vbom);
        mesh.attachChild(batch);
    }

    private void updateAssociatedBlocks() {
        for (LayerBlock block : this.getBlocks()) {
            for (AssociatedBlock<?, ?> associatedBlock : block.getAssociatedBlocks()) {
                associatedBlock.onStep(this);
            }
        }
    }

    public void onStep(float timeStep) {
        if (!this.getName().equals("Ground")) {
            updateAssociatedBlocks();
        }
        for (Use use : useList) {
            use.onStep(timeStep, scene.getWorldFacade());
        }

        if (isFireSetup) {
            fireParticleWrapper.update();
        }
    }

    public Vector2 computeTouch(TouchEvent touch, boolean withHold) {
        Vector2 t =
                this.body.getLocalPoint(new Vector2(touch.getX() / 32f, touch.getY() / 32f)).cpy().mul(32f);
        Vector2 result = null;
        float minDis = Float.MAX_VALUE;
        for (Block<?, ?> block : layerBlocks) {
            LayerProperties layerProperties = (LayerProperties) block.getProperties();
            if (layerProperties.getSharpness() > 0.0f && withHold) {
                continue;
            }
            Vector2 point = GeometryUtils.calculateProjection(t, block.getVertices());
            if (point != null) {
                float dis = t.dst(point);
                if (dis < minDis) {
                    minDis = dis;
                    result = point;
                }
            }
        }
        if (result != null) {
            return body.getWorldPoint(result.cpy().mul(1 / 32f)).cpy().mul(32f);
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
                ArrayList<JointEdge> list = new ArrayList<>(current.getBody().getJointList());
                for (JointEdge edge : list) {
                    GameEntity entity = (GameEntity) edge.other.getUserData();
                    if (entity != null) if (!entities.contains(entity)) deque.push(entity);
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

        for (LayerBlock layerBlock : getBlocks()) {
            ArrayList<? extends Block<?, ?>> associatedBlocks = layerBlock.getAssociatedBlocks();
            List<StainBlock> stainBlocks =
                    associatedBlocks.stream()
                            .filter(e -> e instanceof StainBlock)
                            .map(e -> (StainBlock) e).sorted(Comparator.comparing(StainBlock::getPriority)).collect(Collectors.toList());
            for (StainBlock stain : stainBlocks) {
                Color color = stain.getProperties().getColor();
                batch.draw(
                        stain.getTextureRegion(),
                        stain.getData(),
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        color.getAlpha());
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
        if (mirrorMesh != null) {
            mirrorMesh.detachSelf();
        }
        if (fireParticleWrapper != null) {
            scene.getWorldFacade().removeFireParticleWrapper(fireParticleWrapper);
        }
        dispose();
    }

    public void createJuiceSources() {
        for (LayerBlock block : this.getBlocks()) {
            LayerProperties properties = block.getProperties();
            ArrayList<FreshCut> freshCuts = block.getFreshCuts();
            for (FreshCut freshCut : freshCuts) {
                if (freshCut.getLength() < 5f) {
                    continue;
                }
                if (freshCut.getLimit() > 0 && properties.isJuicy()) {
                    scene.getWorldFacade().createJuiceSource(this, block, freshCut);
                }
                if (freshCut instanceof SegmentFreshCut) {
                    SegmentFreshCut segmentFreshCut = (SegmentFreshCut) freshCut;
                    if (segmentFreshCut.isInner()) {
                        Line line =
                                new Line(
                                        segmentFreshCut.first.x,
                                        segmentFreshCut.first.y,
                                        segmentFreshCut.second.x,
                                        segmentFreshCut.second.y,
                                        2,
                                        ResourceManager.getInstance().vbom);
                        line.setColor(block.getProperties().isJuicy() ? block.getProperties().getJuiceColor() : Color.BLACK);
                        mesh.attachChild(line);
                    }
                }
            }
        }
    }

    @SafeVarargs
    public final <T> boolean hasUsage(Class<T>... targetTypes) {
        for (Use obj : this.useList) {
            for (Class<T> targetType : targetTypes) {
                if (targetType.isInstance(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasActiveUsage(Class<?>... targetTypes) {
        for (Use obj : this.useList) {
            for (Class<?> targetType : targetTypes) {
                if (targetType.isInstance(obj) && obj.isActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return Objects.equals(layerBlocks, that.layerBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layerBlocks);
    }

    public float getSortingValue() {
        return sortingValue;
    }

    public void setSortingValue(float sortingValue) {
        this.sortingValue = sortingValue;
    }

    public List<GameEntity> getConnectedEntities() {
        List<GameEntity> list = new ArrayList<>();
        scene
                .getPhysicsWorld()
                .getJoints()
                .forEachRemaining(
                        e -> {
                            if (e.getBodyA() == this.getBody()) {
                                list.add((GameEntity) e.getBodyB().getUserData());
                            } else if (e.getBodyB() == this.getBody()) {
                                list.add((GameEntity) e.getBodyA().getUserData());
                            }
                        });
        return list;
    }

    public SpecialEntityType getType() {
        return type;
    }

    public void setType(SpecialEntityType type) {
        this.type = type;
    }

    public <T extends Use> T getActiveUsage(Class<T> targetType) {
        for (Use obj : this.useList) {
            if (targetType.isInstance(obj) && obj.isActive()) {
                return (T) obj;
            }
        }
        return null;
    }

    public <T extends Use> T getUsage(Class<T> targetType) {
        for (Use obj : this.useList) {
            if (targetType.isInstance(obj)) {
                return (T) obj;
            }
        }
        return null;
    }

    public void setHangedPointerId(int hangedPointerId) {
    }

    public List<Use> getUseList() {
        return useList;
    }

    public void setUseList(List<Use> useList) {
        this.useList = useList;
    }

    public float getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LayerBlock> getBlocks() {
        return layerBlocks;
    }

    public BodyDef.BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyDef.BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
    }

    public InitInfo getCreationInitInfo() {
        if (getBody() == null) {
            return this.initInfo;
        }
        InitInfo initInfo = new InitInfo();
        initInfo.setLinearVelocity(this.body.getLinearVelocity());
        initInfo.setBullet(this.body.isBullet());
        initInfo.setAngularVelocity(this.body.getAngularVelocity());
        initInfo.setX(this.body.getPosition().x);
        initInfo.setY(this.body.getPosition().y);
        initInfo.setAngle(this.body.getAngle());
        initInfo.setFilter(this.initInfo.getFilter());
        return initInfo;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueID = uniqueId;
    }

    public boolean isMirrored() {
        return mirrored;
    }

    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    public Pair<List<GameEntity>, Boolean> directAttachedEntities() {
        if (body == null) {
            return new Pair<>(null, false);
        }
        final List<GameEntity> attachedEntities = new ArrayList<>();
        body.getJointList().forEach(jointEdge -> {
            if (jointEdge.joint.getType() != JointDef.JointType.MouseJoint) {
                GameEntity other = (GameEntity) jointEdge.other.getUserData();
                attachedEntities.add(other);
            }
        });
        return new Pair<>(attachedEntities, true);
    }

    private void destroyJoints() {
        ArrayList<Joint> jointsToDestroy = new ArrayList<>();
        for (JointEdge je : body.getJointList()) {
            Joint joint = je.joint;
            jointsToDestroy.add(joint);
        }
        for (Joint joint : jointsToDestroy) {
            scene.getPhysicsWorld().destroyJoint(joint);
            Log.e("Mirror", "Destroy :" + joint.getType());
            if (joint.getType() == JointDef.JointType.MouseJoint) {
                scene.onDestroyMouseJoint((MouseJoint) joint);
            }
        }
    }

    private BodyInit getMirrorBodyInit() {
        return new AngularVelocityInit(new LinearVelocityInit(new BulletInit(new TransformInit(new BodyInitImpl(initInfo.getFilter()), body.getPosition().x, body.getPosition().y, body.getAngle()), body.isBullet()), body.getLinearVelocity()), body.getAngularVelocity());
    }

    private synchronized void mirror() {
        if (body == null) {
            return;
        }
        //mirror blocks and usages
        for (LayerBlock layerBlock : layerBlocks) {
            layerBlock.mirror();
        }
        for (Use use : useList) {
            use.dynamicMirror(scene);
        }

        // Iterate over the joints attached to the body and collect them
        destroyJoints();

        body.setActive(false);
        mesh.detachSelf();
        if (isBatcherSetup) {
            batch.detachSelf();
        }
        if (mirrorCreated) {
            mirrorBody.setActive(true);
            mirrorBody.setTransform(body.getPosition(), body.getAngle());
            mirrorBody.setLinearVelocity(body.getLinearVelocity());
            mirrorBody.setAngularVelocity(body.getAngularVelocity());
            Body joker = body;
            body = mirrorBody;
            mirrorBody = joker;

            MosaicMesh jokerMesh = mesh;
            mesh = mirrorMesh;
            mirrorMesh = jokerMesh;
            scene.attachChild(mesh);

        } else {
            mirrorBody = body;
            mirrorMesh = mesh;
            //create new mirrored body and assign it to body
            Invoker.addBodyCreationCommand(this, bodyType, getMirrorBodyInit());
            //create new mirrored mesh and assign it to mesh
            mesh = MeshFactory.getInstance().createMosaicMesh(mesh.getX(), mesh.getY(), mesh.getRotation(), layerBlocks);
            mesh.setZIndex(mirrorMesh.getZIndex());
            mesh.setVisible(false);
            scene.attachChild(mesh);
            mirrorCreated = true;
        }
        scene.sortChildren();
        if (isBatcherSetup) {
            mesh.attachChild(batch);
        }
        changed = true;
        redrawStains();
        mirrored = !mirrored;
        for (Use use : useList) {
            use.onAfterMirror(scene);
        }
    }

    public void tryDynamicMirror() {
        Log.e("Mirror", this + "---------------------------Begin Try Dynamic Mirror---------------------------");
        Queue<GameEntity> deque = new ArrayDeque<>();
        deque.add(this);
        HashSet<GameEntity> visited = new HashSet<>();
        while (!deque.isEmpty()) {
            GameEntity current = deque.poll();
            assert current != null;
            Pair<List<GameEntity>, Boolean> res = current.directAttachedEntities();
            visited.add(current);
            if (!res.second) {
                continue;
            }
            deque.addAll(res.first.stream().filter(e -> !visited.contains(e)).collect(Collectors.toList()));
        }
        for (GameEntity gameEntity : visited) {
            gameEntity.mirror();
        }
        HashSet<JointBlock> used = new HashSet<>();
        List<JointBlock> list = visited.stream().flatMap(e -> e.layerBlocks.stream().flatMap(layerBlock -> layerBlock.getAssociatedBlocks().stream().filter(a -> a instanceof JointBlock).map(jb -> (JointBlock) jb))).collect(Collectors.toList());
        for (JointBlock jointBlock : list) {
            if (used.contains(jointBlock)) {
                continue;
            }
            Log.e("Mirror", "Schedule recreate:" + jointBlock.getJointType() + " /" + jointBlock);
            scene.getWorldFacade().recreateJoint(jointBlock, jointBlock.getEntity());
            used.add(jointBlock);
            used.add(jointBlock.getBrother());
        }

        // mirror each entity and mirror their joints
    }

    public void removeJointBlock(JointBlock jointBlock) {
        for (LayerBlock layerBlock : layerBlocks) {
            layerBlock.getAssociatedBlocks().remove(jointBlock);
        }
    }

    public Body getMirrorBody() {
        return mirrorBody;
    }

    public void setMirrorBody(Body body) {
        mirrorBody = body;
    }

    public float getMass() {
        return body.getMass();
    }

    public float getX() {
        return this.mesh.getX();
    }

    public float getY() {
        return this.mesh.getY();
    }

    public float getRotation() {
        return this.mesh.getRotation();
    }

    public int getZIndex() {
        return this.mesh.getZIndex();
    }

    public void setZIndex(int i) {
        this.mesh.setZIndex(i);
        this.zIndex = i;
    }
}
