package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.mesh.mosaic.MosaicMesh;
import com.evolgames.entities.properties.ColorPanelProperties;
import com.evolgames.entities.properties.ToolProperties;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.helpers.XmlHelper;
import com.evolgames.scenes.AbstractScene;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.SpecialPointModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.DragShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.SpecialPointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.utilities.BlockUtils;
import com.evolgames.utilities.GeometryUtils;

import org.andengine.util.adt.color.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ToolModel extends ProperModel<ToolProperties> implements Serializable {

    private final AbstractScene<?> scene;
    private final AtomicInteger bodyCounter = new AtomicInteger();
    private final AtomicInteger jointCounter = new AtomicInteger();
    private final AtomicInteger projectileCounter = new AtomicInteger();
    private final AtomicInteger ammoCounter = new AtomicInteger();
    private final AtomicInteger bombCounter = new AtomicInteger();
    private final AtomicInteger dragCounter = new AtomicInteger();
    private final AtomicInteger specialPointCounter = new AtomicInteger();
    private final AtomicInteger fireSourceCounter = new AtomicInteger();
    private final AtomicInteger liquidSourceCounter = new AtomicInteger();
    private final ArrayList<BodyModel> bodies;
    private final ArrayList<JointModel> joints;
    private final ArrayList<MosaicMesh> meshes = new ArrayList<>();
    private final ColorPanelProperties colorPanelProperties = new ColorPanelProperties();
    private ImageShapeModel imageShapeModel;
    private ItemCategory category;

    public ToolModel(AbstractScene<?> gameScene) {
        super("");
        scene = gameScene;
        bodies = new ArrayList<>();
        joints = new ArrayList<>();
        this.properties = new ToolProperties();
    }

    @Override
    public String getModelName() {
        throw new UnsupportedOperationException("This model doesn't have a direct model name");
    }

    public ColorPanelProperties getColorPanelProperties() {
        return colorPanelProperties;
    }

    public BodyModel createNewBody() {
        BodyModel bodyModel = new BodyModel(bodyCounter.getAndIncrement());
        bodies.add(bodyModel);
        return bodyModel;
    }

    private BodyModel getBodyById(int bodyId) {
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getBodyId() == bodyId) return bodyModel;
        }
        return null;
    }

    public void swapLayers(int bodyId, int index1, int index2) {
        Objects.requireNonNull(getBodyById(bodyId)).swapLayers(index1, index2);
        updateMesh();
    }

    public ProjectileModel createNewProjectile(ProjectileShape projectileShape, int bodyId) {
        int projectileId = projectileCounter.getAndIncrement();
        ProjectileModel projectileModel = new ProjectileModel(bodyId, projectileId, projectileShape);
        getBodyModelById(bodyId).getProjectileModels().add(projectileModel);
        return projectileModel;
    }

    public JointModel createJointModel(JointShape jointShape, JointDef.JointType jointType) {
        int jointId = jointCounter.getAndIncrement();
        JointModel jointModel = new JointModel(jointId, jointType, jointShape);
        joints.add(jointModel);
        return jointModel;
    }

    public void removeJoint(int jointId) {
        JointModel jointModel = null;
        for (JointModel j : joints) {
            if (j.getJointId() == jointId) {
                jointModel = j;
                break;
            }
        }
        if (jointModel != null) {
            joints.remove(jointModel);
        }
    }

    public LayerModel createNewLayer(int bodyId) {
        return Objects.requireNonNull(getBodyById(bodyId), "invalid bodyId").createLayer();
    }

    public LayerModel getLayerModelById(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).getLayerModelById(layerId);
    }

    public ArrayList<BodyModel> getBodies() {
        return bodies;
    }

    public void selectJoint(int jointId) {
        getJointById(jointId).selectJoint();
        for (JointModel jointModel : joints) {
            if (jointId != jointModel.getJointId()) {
                jointModel.deselect();
            }
        }
    }

    public JointModel getJointById(int jointId) {
        for (JointModel jointModel : joints) {
            if (jointModel.getJointId() == jointId) {
                return jointModel;
            }
        }
        return null;
    }

    public DecorationModel createNewDecoration(int bodyId, int layerId) {
        return Objects.requireNonNull(getBodyById(bodyId)).createNewDecoration(layerId);
    }

    public void removeBody(int bodyId) {
        bodies.remove(getBodyById(bodyId));
    }

    public void removeLayer(int bodyId, int layerId) {
        Objects.requireNonNull(getBodyById(bodyId)).removeLayer(layerId);
    }

    public void removeAmmo(int bodyId, int ammoId) {
        BodyModel body = getBodyModelById(bodyId);
        body.getCasingModels().remove(body.getCasingModelById(ammoId));
    }

    public DecorationModel removeDecoration(int bodyId, int layerId, int decorationId) {
        return Objects.requireNonNull(getBodyById(bodyId)).removeDecoration(layerId, decorationId);
    }

    public DecorationModel getDecorationModelById(int primaryKey, int secondaryKey, int tertiaryKey) {
        return getLayerModelById(primaryKey, secondaryKey).getDecorationById(tertiaryKey);
    }

    public void updateMesh() {
        for (MosaicMesh mesh : meshes) {
            mesh.detachSelf();
        }
        meshes.clear();
        for (BodyModel bodyModel : bodies) {
            List<LayerModel> layers = bodyModel.getLayers().stream().filter(layerModel -> layerModel.getPoints().size() >= 3).collect(Collectors.toList());
            if (layers.size() == 0) {
                continue;
            }
            Vector2 center = GeometryUtils.calculateCentroid(layers.get(0).getPoints());
            ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(layers, center);
            MosaicMesh mesh = MeshFactory.getInstance().createMosaicMesh(center.x, center.y, 0, blocks);
            scene.attachChild(mesh);
            meshes.add(mesh);
        }
        scene.sortChildren();
    }

    public void deselectJoint(int jointId) {
        Objects.requireNonNull(getJointById(jointId)).deselect();
    }

    public ArrayList<JointModel> getJoints() {
        return joints;
    }

    public BodyModel getBodyModelById(int bodyId) {
        for (BodyModel bodyModel : bodies) {
            if (bodyModel.getBodyId() == bodyId) {
                return bodyModel;
            }
        }
        return null;
    }

    public CasingModel getAmmoById(int primaryKey, int secondaryKey) {
        return getBodyModelById(primaryKey).getCasingModels().stream()
                .filter(e -> e.getCasingId() == secondaryKey)
                .findAny()
                .orElse(null);
    }

    public LiquidSourceModel getLiquidSourceById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getLiquidSourceModels().stream()
                .filter(e -> e.getLiquidSourceId() == modelId)
                .findAny()
                .orElse(null);
    }

    public FireSourceModel getFireSourceById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getFireSourceModels().stream()
                .filter(e -> e.getFireSourceId() == modelId)
                .findAny()
                .orElse(null);
    }

    public ProjectileModel getProjectileById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getProjectileModels().stream()
                .filter(e -> e.getProjectileId() == modelId)
                .findFirst()
                .orElse(null);
    }

    public BombModel getBombById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getBombModels().stream()
                .filter(e -> e.getBombId() == modelId)
                .findAny()
                .orElse(null);
    }

    public DragModel getDragModelById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getDragModels().stream()
                .filter(e -> e.getDragId() == modelId)
                .findAny()
                .orElse(null);
    }

    public void removeProjectile(int primaryKey, int modelId) {
        getBodyModelById(primaryKey)
                .getProjectileModels()
                .removeIf(e -> e.getProjectileId() == modelId);
    }

    public void removeFireSource(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getFireSourceModels().removeIf(e -> e.getFireSourceId() == secondaryKey);
    }

    public void removeLiquidSource(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getLiquidSourceModels().removeIf(e -> e.getLiquidSourceId() == secondaryKey);
    }
    public void removeSpecialPoint(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getSpecialPointModels().removeIf(e -> e.getPointId() == secondaryKey);
    }


    public void removeBomb(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getBombModels().removeIf(e -> e.getBombId() == secondaryKey);
    }

    public void removeDrag(int primaryKey, int secondaryKey) {
        getBodyModelById(primaryKey).getDragModels().removeIf(e -> e.getDragId() == secondaryKey);
    }

    public ItemCategory getToolCategory() {
        return category;
    }

    public AtomicInteger getProjectileCounter() {
        return projectileCounter;
    }

    public AtomicInteger getAmmoCounter() {
        return ammoCounter;
    }

    public AtomicInteger getBodyCounter() {
        return bodyCounter;
    }

    public AtomicInteger getJointCounter() {
        return jointCounter;
    }

    public AtomicInteger getBombCounter() {
        return bombCounter;
    }

    public AtomicInteger getDragCounter() {
        return dragCounter;
    }

    public AtomicInteger getFireSourceCounter() {
        return fireSourceCounter;
    }

    public AtomicInteger getLiquidSourceCounter() {
        return liquidSourceCounter;
    }


    public CasingModel createNewAmmo(CasingShape ammoShape, int bodyId) {
        int ammoId = ammoCounter.getAndIncrement();
        CasingModel ammoModel = new CasingModel(bodyId, ammoId, ammoShape);
        getBodyModelById(bodyId).getCasingModels().add(ammoModel);
        return ammoModel;
    }

    public FireSourceModel createNewFireSource(FireSourceShape fireSourceShape, int bodyId) {
        int fireSourceId = fireSourceCounter.getAndIncrement();
        FireSourceModel fireSourceModel = new FireSourceModel(bodyId, fireSourceId, fireSourceShape);
        getBodyModelById(bodyId).getFireSourceModels().add(fireSourceModel);
        return fireSourceModel;
    }

    public LiquidSourceModel createNewLiquidSource(LiquidSourceShape liquidSourceShape, int bodyId) {
        int liquidSourceId = liquidSourceCounter.getAndIncrement();
        LiquidSourceModel liquidSourceModel =
                new LiquidSourceModel(bodyId, liquidSourceId, liquidSourceShape);
        getBodyModelById(bodyId).getLiquidSourceModels().add(liquidSourceModel);
        return liquidSourceModel;
    }

    public BombModel createNewBomb(BombShape bombShape, int bodyId) {
        int bombId = bombCounter.getAndIncrement();
        BombModel bombModel = new BombModel(bodyId, bombId, bombShape);
        getBodyModelById(bodyId).getBombModels().add(bombModel);
        return bombModel;
    }

    public DragModel createNewDrag(DragShape dragShape, int bodyId) {
        int dragId = dragCounter.getAndIncrement();
        DragModel dragModel = new DragModel(bodyId, dragId, dragShape);
        getBodyModelById(bodyId).getDragModels().add(dragModel);
        return dragModel;
    }


    public ImageShapeModel getImageShapeModel() {
        return this.imageShapeModel;
    }

    public void setImageShapeModel(ImageShapeModel imageShapeModel) {
        this.imageShapeModel = imageShapeModel;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public SpecialPointModel createNewSpecialPoint(SpecialPointShape specialPointShape, int selectedBodyId) {
        int pointId = specialPointCounter.getAndIncrement();
        SpecialPointModel specialPointModel = new SpecialPointModel(selectedBodyId, pointId, specialPointShape);
        getBodyModelById(selectedBodyId).getSpecialPointModels().add(specialPointModel);
        return specialPointModel;
    }

    public SpecialPointModel getSpecialPointById(int primaryKey, int modelId) {
        return getBodyModelById(primaryKey).getSpecialPointModels().stream()
                .filter(e -> e.getPointId() == modelId)
                .findAny()
                .orElse(null);
    }
}
