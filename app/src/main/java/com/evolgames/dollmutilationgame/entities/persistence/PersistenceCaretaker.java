package com.evolgames.dollmutilationgame.entities.persistence;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.dollmutilationgame.activity.GameActivity;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.Material;
import com.evolgames.dollmutilationgame.entities.factories.MaterialFactory;
import com.evolgames.dollmutilationgame.entities.factories.Materials;
import com.evolgames.dollmutilationgame.entities.properties.BodyProperties;
import com.evolgames.dollmutilationgame.entities.properties.BodyUsageCategory;
import com.evolgames.dollmutilationgame.entities.properties.BombProperties;
import com.evolgames.dollmutilationgame.entities.properties.CasingProperties;
import com.evolgames.dollmutilationgame.entities.properties.DecorationProperties;
import com.evolgames.dollmutilationgame.entities.properties.DragProperties;
import com.evolgames.dollmutilationgame.entities.properties.Explosive;
import com.evolgames.dollmutilationgame.entities.properties.FireSourceProperties;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.dollmutilationgame.entities.properties.LiquidSourceProperties;
import com.evolgames.dollmutilationgame.entities.properties.ProjectileProperties;
import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.entities.properties.SpecialPointProperties;
import com.evolgames.dollmutilationgame.entities.properties.SquareProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.BombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.BowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.HeavyProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MissileProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MotorControlProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RangedProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.SlashProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.StabProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ThrowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.helpers.XmlHelper;
import com.evolgames.dollmutilationgame.scenes.AbstractScene;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.BombModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.CasingModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.DragModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileTriggerType;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.SpecialPointModel;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.utilities.ToolUtils;
import com.evolgames.dollmutilationgame.utilities.Utils;
import com.evolgames.dollmutilationgame.utilities.XmlUtils;
import com.evolgames.dollmutilationgame.userinterface.model.BodyModel;
import com.evolgames.dollmutilationgame.userinterface.model.DecorationModel;
import com.evolgames.dollmutilationgame.userinterface.model.ImageShapeModel;
import com.evolgames.dollmutilationgame.userinterface.model.ItemCategory;
import com.evolgames.dollmutilationgame.userinterface.model.LayerModel;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;

import org.andengine.BuildConfig;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.color.ColorUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class PersistenceCaretaker {

    public static final String LAYER_TAG = "layer";
    public static final String DECORATION_TAG = "decoration";
    public static final String BODIES_TAG = "bodies";
    public static final String LAYERS_TAG = "layers";
    public static final String DECORATIONS_TAG = "decorations";
    public static final String PROPERTIES_TAG = "properties";
    public static final String VERTICES_TAG = "vertices";
    public static final String REF_VERTICES_TAG = "refVertices";
    public static final String OUTLINE_TAG = "outline";
    public static final String BODY_TAG = "body";
    public static final String PANEL_COLORS_TAG = "panelColors";
    public static final String PANEL_COLOR_TAG = "color";
    public static final String PROJECTILES_TAG = "projectiles";
    public static final String AMMO_LIST_TAG = "ammoList";
    public static final String FIRE_SOURCE_LIST_TAG = "fireSourceList";
    public static final String LIQUID_SOURCE_LIST_TAG = "liquidSourceList";
    public static final String USAGE_LIST_TAG = "usageList";
    public static final String DRAG_LIST_TAG = "dragList";
    public static final String USAGE_TAG = "usage";
    public static final String USAGE_PROPERTIES_JOINTS_TAG = "motorJoints";
    public static final String USAGE_PROPERTIES_PROJECTILES_TAG = "projectiles";
    public static final String USAGE_PROPERTIES_FIRE_SOURCES_TAG = "fireSources";
    public static final String USAGE_PROPERTIES_LIQUID_SOURCES_TAG = "liquidSources";
    public static final String USAGE_PROPERTIES_BOMBS_TAG = "bombs";
    public static final String USAGE_PROPERTIES_SENSITIVE_LAYERS_TAG = "sensitiveLayers";
    public static final String USAGE_TYPE_TAG = "type";
    public static final String AMMO_TAG = "ammo";
    public static final String AMMO_ID_TAG = "ammoId";
    public static final String TOOL_TAG = "tool";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String VECTOR_TAG = "v";
    public static final String PROJECTILE_TAG = "projectile";
    public static final String FIRE_SOURCE_TAG = "fireSource";
    public static final String LIQUID_SOURCE_TAG = "liquidSource";
    public static final String JOINT_COLLIDE_CONNECTED_ATTRIBUTE = "collideConnected";
    public static final String JOINT_TYPE_ATTRIBUTE = "type";
    public static final String BODY_A_ID_JOINT_ATTRIBUTE = "bodyAId";
    public static final String BODY_B_ID_JOINT_ATTRIBUTE = "bodyBId";
    public static final String XML_FOLDER = "xml";
    public static final String JOINT = "joint";
    public static final String SPACIAL_POINT_TAG = "spacialPoint";
    private static final PersistenceCaretaker INSTANCE = new PersistenceCaretaker();
    private static final String IMAGE_SHAPE_TAG = "imageShape";
    private static final String JOINTS_TAG = "joints";
    private static final String BOMB_LIST_TAG = "bombList";
    private static final String SPECIAL_POINTS_LIST_TAG = "specialPointsList";
    private static final String BOMB_TAG = "bomb";
    private static final String CATEGORY = "category";
    private DocumentBuilder docBuilder;
    private GameActivity gameActivity;
    private AbstractScene scene;
    private File versionDir;

    private PersistenceCaretaker() {
    }

    public static PersistenceCaretaker getInstance() {
        return INSTANCE;
    }

    public static String convertIntListToString(List<Integer> list) {
        return list.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }

    public static List<Integer> convertStringToIntList(String input) {
        if (input.length() == 0) {
            return new ArrayList<>();
        }
        String[] numberStrings = input.split(" ");
        List<Integer> numbers = new ArrayList<>();

        for (String numberString : numberStrings) {
            int number = Integer.parseInt(numberString);
            numbers.add(number);
        }

        return numbers;
    }


    public void create(AbstractScene editorScene) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        this.scene = editorScene;
        this.gameActivity = ResourceManager.getInstance().activity;
        this.docBuilder = docFactory.newDocumentBuilder();
        if (BuildConfig.DEBUG) {
            File internalStorageDir = gameActivity.getFilesDir();
            versionDir = new File(internalStorageDir, "version");
            if (!versionDir.exists()) {
                versionDir.mkdir();
            }
            runVersionUpdates();
        }
    }

    private void runVersionUpdates() {
        //resetMaterialsBasicValues();
        //addSymbolToTheEndOfItems();
     // scaleToolBody("Main Battle Tank#",2f,13);
//     scaleTool("Archery Target#",1.333f);
       // mirrorTool("Rocket 2#");
        // moveLayersTransformation();
       //asIsTransformation();
    }

    private void resetMaterialsBasicValues() {
        VersioningHelper.applyTreatmentToLayers(layerModel -> {
            LayerProperties props = layerModel.getProperties();
            int materialNumber = props.getMaterialNumber();
            Material material = MaterialFactory.getInstance().getMaterialByIndex(materialNumber);
            props.setHardness(material.getHardness());
            props.setTenacity(material.getTenacity());
            props.setDensity(material.getDensity());
            props.setRestitution(material.getRestitution());
            props.setFriction(material.getFriction());
        }, e -> true);
    }

    private void moveLayersTransformation() {
        VersioningHelper.applyTreatment(toolModel -> {
            String name = toolModel.getProperties().getToolName();
            if (name.equals("fra")) {
                toolModel.getBodies().get(1).getLayers().addAll(toolModel.getBodies().get(0).getLayers().stream().filter(l -> l.getLayerId() >= 7).collect(Collectors.toList()));
                toolModel.getBodies().get(1).getLayerCounter().set(toolModel.getBodies().get(0).getLayerCounter().get());
                toolModel.getBodies().get(0).getLayers().removeIf(e -> e.getLayerId() >= 7);
            }
        }, (e) -> true);
    }

    private void scaleBodyModel(BodyModel bodyModel, float scale) {
        List<List<Vector2>> list = new ArrayList<>();
        for (LayerModel layerModel : bodyModel.getLayers()) {
            list.add(layerModel.getPoints());
        }
        Vector2 center = GeometryUtils.calculateCenter(list);
        for (LayerModel layerModel : bodyModel.getLayers()) {
            ToolUtils.scalePoints(center, layerModel.getPoints(), scale);
            ToolUtils.scalePoint(center, layerModel.getCenter(), scale);
            for (DecorationModel decorationModel : layerModel.getDecorations()) {
                ToolUtils.scalePoints(center, decorationModel.getPoints(), scale);
                ToolUtils.scalePoint(center, decorationModel.getCenter(), scale);
            }
        }
    }
    private void asIsTransformation() {
        VersioningHelper.applyTreatment(toolModel -> {
         for(BodyModel bodyModel:toolModel.getBodies()){
             for(LayerModel layerModel:bodyModel.getLayers()){
                 LayerProperties properties = layerModel.getProperties();
                 if(properties.isJuicy()){
                     properties.getJuiceColor().setAlpha(Materials.getLiquidByIndex(properties.getJuiceIndex()).getDefaultColor().getAlpha());
                 }
             }
         }
        }, (ItemMetaData e) ->e.getItemCategory()==ItemCategory.PRODUCE||e.getItemCategory()==ItemCategory.OTHER);
    }

    private void scaleToolBody(String toolName, float scale, int bodyId) {
        VersioningHelper.applyTreatment(toolModel -> {
            String name = toolModel.getProperties().getToolName();
            if (name.equals(toolName)) {
                for (BodyModel bodyModel : toolModel.getBodies()) {
                    if(bodyModel.getBodyId()!=bodyId){
                        continue;
                    }
                    scaleBodyModel(bodyModel, scale);
                }
            }
        }, (ItemMetaData e) -> e.getName().equals(toolName));
    }


    private void scaleTool(String toolName, float scale) {
        VersioningHelper.applyTreatment(toolModel -> {
            String name = toolModel.getProperties().getToolName();
            if (name.equals(toolName)) {
                for (BodyModel bodyModel : toolModel.getBodies()) {
                    scaleBodyModel(bodyModel, scale);
                }
                float h = toolModel.getImageShapeModel().getHeight();
                float w = toolModel.getImageShapeModel().getWidth();
                toolModel.getImageShapeModel().setHeight(scale*h);
                toolModel.getImageShapeModel().setWidth(scale*w);
                toolModel.getJoints().clear();
            }
        }, (ItemMetaData e) -> e.getName().equals(toolName));
    }


    private void mirrorTool(String toolName) {
        VersioningHelper.applyTreatment(toolModel -> {
            String name = toolModel.getProperties().getToolName();
            if (name.equals(toolName)) {
                for (BodyModel bodyModel : toolModel.getBodies()) {
                   for(LayerModel layerModel:bodyModel.getLayers()){
                      layerModel.setPoints(GeometryUtils.mirrorPointsWithShift(layerModel.getPoints()));
                       layerModel.setCenter(GeometryUtils.mirrorPointWithShift(layerModel.getCenter()));
                       for(DecorationModel decorationModel: layerModel.getDecorations()){
                           decorationModel.setPoints(GeometryUtils.mirrorPointsWithShift(decorationModel.getPoints()));
                           decorationModel.setCenter(GeometryUtils.mirrorPointWithShift(decorationModel.getCenter()));
                       }
                   }
                }
                for(JointModel jointModel:toolModel.getJoints()){
                    jointModel.getProperties().getLocalAnchorA().set(GeometryUtils.mirrorPointWithShift(jointModel.getProperties().getLocalAnchorA()));
                    jointModel.getProperties().getLocalAnchorB().set(GeometryUtils.mirrorPointWithShift(jointModel.getProperties().getLocalAnchorB()));
                }
            }
        }, (ItemMetaData e) -> e.getName().equals(toolName));
    }
    public void saveToolModel(ToolModel toolModel)
            throws FileNotFoundException, TransformerException {
        this.saveToolModel(
                toolModel,
                XmlHelper.convertToXmlFormat(toolModel.getProperties().getToolName()));
    }

    public void saveToolModel(ToolModel toolModel, String fileName) throws FileNotFoundException, TransformerException {
        saveToolModel(toolModel, fileName, "");
    }

    public void saveToolModel(ToolModel toolModel, String fileName, String pathname)
            throws FileNotFoundException, TransformerException {
        Document toolDocument = docBuilder.newDocument();
        FileOutputStream fos;
        if (pathname == null || pathname.isEmpty()) {
            fos = gameActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
        } else {
            File file = new File(versionDir, fileName);
            fos = new FileOutputStream(file);
        }
        Element toolElement = toolDocument.createElement(TOOL_TAG);
        toolElement.setAttribute(NAME, toolModel.getProperties().getToolName());
        toolElement.setAttribute(CATEGORY, toolModel.getToolCategory().name());
        Element bodiesElement = toolDocument.createElement(BODIES_TAG);
        for (BodyModel bodyModel : toolModel.getBodies()) {
            Element bodyElement = createBodyElement(toolDocument, bodyModel);
            bodiesElement.appendChild(bodyElement);
        }
        toolElement.appendChild(bodiesElement);
        //Saving joints
        Element jointsElement = toolDocument.createElement(JOINTS_TAG);
        for (JointModel jointModel : toolModel.getJoints()) {
            Element jointElement = createJointElement(toolDocument, jointModel);
            jointsElement.appendChild(jointElement);
        }
        toolElement.appendChild(jointsElement);
        //Saving color panel
        Element colorPanelElement = createPanelColorsElement(toolDocument, toolModel.getColorPanelProperties().getSquarePropertiesList());
        toolElement.appendChild(colorPanelElement);


        if (toolModel.getImageShapeModel() != null) {
            Element imageShapeElement = toolDocument.createElement(IMAGE_SHAPE_TAG);
            Element imageShapePropertiesElement = createPropertiesElement(toolDocument, toolModel.getImageShapeModel());
            imageShapeElement.appendChild(imageShapePropertiesElement);
            toolElement.appendChild(imageShapeElement);
        }

        toolDocument.appendChild(toolElement);
        XmlUtils.writeXml(toolDocument, fos);
    }

    public Element createJointElement(Document document, JointModel jointModel) {
        Element jointElement = document.createElement(JOINT);
        jointElement.setAttribute(ID, String.valueOf(jointModel.getJointId()));
        jointElement.setIdAttribute(ID, true);
        jointElement.setAttribute(
                JOINT_COLLIDE_CONNECTED_ATTRIBUTE, String.valueOf(jointModel.getProperties().isCollideConnected()));
        jointElement.setAttribute(
                JOINT_TYPE_ATTRIBUTE, String.valueOf(jointModel.getProperties().getJointType().getValue()));
        if (jointModel.getBodyModel1() != null) {
            int bodyId1 = jointModel.getBodyModel1().getBodyId();
            jointElement.setAttribute(BODY_A_ID_JOINT_ATTRIBUTE, String.valueOf(bodyId1));
        }
        if (jointModel.getBodyModel2() != null) {
            int bodyId2 = jointModel.getBodyModel2().getBodyId();
            jointElement.setAttribute(BODY_B_ID_JOINT_ATTRIBUTE, String.valueOf(bodyId2));
        }

        Element localAnchorAElement;
        Element localAnchorBElement;
        switch (jointModel.getProperties().getJointType()) {
            case Unknown:
            case FrictionJoint:
            case LineJoint:
            case GearJoint:
            case MouseJoint:
            case PulleyJoint:
                break;
            case RevoluteJoint:
                localAnchorAElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorA());
                localAnchorBElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorB());
                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("enableMotor", String.valueOf(jointModel.getProperties().isEnableMotor()));
                jointElement.setAttribute("maxMotorTorque", String.valueOf(jointModel.getProperties().getMaxMotorTorque()));
                jointElement.setAttribute("motorSpeed", String.valueOf(jointModel.getProperties().getMotorSpeed()));
                jointElement.setAttribute("enableLimit", String.valueOf(jointModel.getProperties().isEnableLimit()));
                jointElement.setAttribute("lowerAngle", String.valueOf(jointModel.getProperties().getLowerAngle()));
                jointElement.setAttribute("upperAngle", String.valueOf(jointModel.getProperties().getUpperAngle()));
                jointElement.setAttribute("referenceAngle", String.valueOf(jointModel.getProperties().getReferenceAngle()));
                break;
            case PrismaticJoint:
                localAnchorAElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorA());
                localAnchorBElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorB());
                Element localAxisElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAxis1());
                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.appendChild(localAxisElement);
                jointElement.setAttribute("enableMotor", String.valueOf(jointModel.getProperties().isEnableMotor()));
                jointElement.setAttribute("maxMotorForce", String.valueOf(jointModel.getProperties().getMaxMotorForce()));
                jointElement.setAttribute("motorSpeed", String.valueOf(jointModel.getProperties().getMotorSpeed()));
                jointElement.setAttribute("enableLimit", String.valueOf(jointModel.getProperties().isEnableLimit()));
                jointElement.setAttribute(
                        "lowerTranslation", String.valueOf(jointModel.getProperties().getLowerTranslation()));
                jointElement.setAttribute(
                        "upperTranslation", String.valueOf(jointModel.getProperties().getUpperTranslation()));
                jointElement.setAttribute("referenceAngle", String.valueOf(jointModel.getProperties().getReferenceAngle()));
                break;
            case DistanceJoint:
                localAnchorAElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorA());
                localAnchorBElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorB());

                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("dampingRatio", String.valueOf(jointModel.getProperties().getDampingRatio()));
                jointElement.setAttribute("length", String.valueOf(jointModel.getProperties().getLength()));
                jointElement.setAttribute("frequencyHz", String.valueOf(jointModel.getProperties().getFrequencyHz()));
                break;
            case WeldJoint:
                localAnchorAElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorA());
                localAnchorBElement =
                        XmlUtils.createVectorElement(document, VECTOR_TAG, jointModel.getProperties().getLocalAnchorB());

                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("referenceAngle", String.valueOf(jointModel.getProperties().getReferenceAngle()));
                break;
        }
        return jointElement;
    }

    private Element createBodyElement(Document document, BodyModel bodyModel) {
        Element bodyElement = document.createElement(BODY_TAG);
        bodyElement.setAttribute(ID, String.valueOf(bodyModel.getBodyId()));
        bodyElement.setIdAttribute(ID, true);
        Element bodyPropertiesElement = createPropertiesElement(document, bodyModel.getProperties());
        bodyElement.appendChild(bodyPropertiesElement);

        Element layersElement = document.createElement(LAYERS_TAG);
        bodyModel
                .getLayers()
                .forEach(
                        layerModel -> {
                            Element layerElement = createLayerElement(document, layerModel);
                            layersElement.appendChild(layerElement);
                        });
        bodyElement.appendChild(layersElement);

        Element projectilesElement = document.createElement(PROJECTILES_TAG);
        for (ProjectileModel projectileModel : bodyModel.getProjectileModels()) {
            projectilesElement.appendChild(createProjectileElement(document, projectileModel));
        }
        bodyElement.appendChild(projectilesElement);

        Element fireSourcesElement = document.createElement(FIRE_SOURCE_LIST_TAG);
        for (FireSourceModel fireSourceModel : bodyModel.getFireSourceModels()) {
            fireSourcesElement.appendChild(createFireSourceElement(document, fireSourceModel));
        }
        bodyElement.appendChild(fireSourcesElement);

        Element liquidSourcesElement = document.createElement(LIQUID_SOURCE_LIST_TAG);
        for (LiquidSourceModel liquidSourceModel : bodyModel.getLiquidSourceModels()) {
            liquidSourcesElement.appendChild(createLiquidSourceElement(document, liquidSourceModel));
        }
        bodyElement.appendChild(liquidSourcesElement);

        Element ammoListElement = document.createElement(AMMO_LIST_TAG);
        for (CasingModel ammoModel : bodyModel.getCasingModels()) {
            ammoListElement.appendChild(createAmmoElement(document, ammoModel));
        }
        bodyElement.appendChild(ammoListElement);

        Element bombListElement = document.createElement(BOMB_LIST_TAG);
        for (BombModel bombModel : bodyModel.getBombModels()) {
            bombListElement.appendChild(createBombElement(document, bombModel));
        }
        bodyElement.appendChild(bombListElement);


        Element specialPointsListElement = document.createElement(SPECIAL_POINTS_LIST_TAG);
        for (SpecialPointModel specialPointModel : bodyModel.getSpecialPointModels()) {
            specialPointsListElement.appendChild(createSpecialPointElement(document, specialPointModel));
        }
        bodyElement.appendChild(specialPointsListElement);

        Element dragListElement = document.createElement(DRAG_LIST_TAG);
        for (DragModel dragModel : bodyModel.getDragModels()) {
            dragListElement.appendChild(createDragElement(document, dragModel));
        }
        bodyElement.appendChild(dragListElement);

        Element usageListElement = document.createElement(USAGE_LIST_TAG);
        for (UsageModel<?> usageModel : bodyModel.getUsageModels()) {
            Element usageElement = document.createElement(USAGE_TAG);
            Properties props = bodyModel.getUsageModelProperties(usageModel.getType());
            if (usageModel.getType() == BodyUsageCategory.BOW) {
                BowProperties bowProperties = (BowProperties) props;
                if (bodyModel.getSpecialPointModels().size() == 3) {
                    List<SpecialPointModel> sp = bodyModel.getSpecialPointModels();
                    bowProperties.setUpper(sp.get(0).getProperties().getPosition());
                    bowProperties.setMiddle(sp.get(1).getProperties().getPosition());
                    bowProperties.setLower(sp.get(2).getProperties().getPosition());
                }
            }
            Element propertiesElement = createPropertiesElement(document, Objects.requireNonNull(props));
            if (usageModel.getType() == BodyUsageCategory.SHOOTER_CONTINUOUS
                    || usageModel.getType() == BodyUsageCategory.SHOOTER || usageModel.getType() == BodyUsageCategory.BOW || usageModel.getType() == BodyUsageCategory.ROCKET_LAUNCHER) {
                RangedProperties rangedProperties = bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_PROJECTILES_TAG,
                        convertIntListToString(
                                rangedProperties.getProjectileIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.TIME_BOMB) {
                TimeBombUsageProperties timeBombUsageProperties =
                        bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_BOMBS_TAG,
                        convertIntListToString(timeBombUsageProperties.getBombIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.IMPACT_BOMB) {
                ImpactBombUsageProperties impactBombUsageProperties =
                        bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_BOMBS_TAG,
                        convertIntListToString(impactBombUsageProperties.getBombIds()));
                propertiesElement.setAttribute(USAGE_PROPERTIES_SENSITIVE_LAYERS_TAG, convertIntListToString(
                        impactBombUsageProperties.getSensitiveLayers()
                ));
            }
            if (usageModel.getType() == BodyUsageCategory.FLAME_THROWER) {
                FlameThrowerProperties flameThrowerProperties =
                        bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_FIRE_SOURCES_TAG,
                        convertIntListToString(flameThrowerProperties.getFireSourceIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.ROCKET) {
                RocketProperties rocketProperties = bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_FIRE_SOURCES_TAG,
                        convertIntListToString(rocketProperties.getFireSourceIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.LIQUID_CONTAINER) {
                LiquidContainerProperties liquidContainerProperties = bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_LIQUID_SOURCES_TAG,
                        convertIntListToString(
                                liquidContainerProperties.getLiquidSourceIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.MISSILE) {
                MissileProperties missileProperties =
                        bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_FIRE_SOURCES_TAG,
                        convertIntListToString(missileProperties.getFireSourceIds()));
            }
            if (usageModel.getType() == BodyUsageCategory.MOTOR_CONTROL) {
                MotorControlProperties motorControlProperties =
                        bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(
                        USAGE_PROPERTIES_JOINTS_TAG,
                        convertIntListToString(
                                motorControlProperties.getJointIds()));
            }
            usageElement.appendChild(propertiesElement);
            usageElement.setAttribute(USAGE_TYPE_TAG, usageModel.getType().name());
            usageListElement.appendChild(usageElement);
        }
        bodyElement.appendChild(usageListElement);
        return bodyElement;
    }

    private Element createDragElement(Document document, DragModel dragModel) {
        Element dragElement = document.createElement(BOMB_TAG);
        dragElement.setAttribute(ID, String.valueOf(dragModel.getDragId()));
        dragElement.setIdAttribute(ID, true);
        dragElement.setAttribute(NAME, String.valueOf(dragModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, dragModel.getProperties());
        dragElement.appendChild(propertiesElement);
        return dragElement;
    }

    private Element createSpecialPointElement(Document document, SpecialPointModel specialPointModel) {
        Element element = document.createElement(SPACIAL_POINT_TAG);
        element.setAttribute(ID, String.valueOf(specialPointModel.getPointId()));
        element.setIdAttribute(ID, true);
        Element propertiesElement = createPropertiesElement(document, specialPointModel.getProperties());
        element.appendChild(propertiesElement);
        return element;
    }

    private Element createBombElement(Document document, BombModel bombModel) {
        Element bombElement = document.createElement(BOMB_TAG);
        bombElement.setAttribute(ID, String.valueOf(bombModel.getBombId()));
        bombElement.setIdAttribute(ID, true);
        bombElement.setAttribute(NAME, String.valueOf(bombModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, bombModel.getProperties());
        bombElement.appendChild(propertiesElement);
        return bombElement;
    }

    private Element createAmmoElement(Document document, CasingModel ammoModel) {
        Element ammoElement = document.createElement(AMMO_TAG);
        ammoElement.setAttribute(ID, String.valueOf(ammoModel.getCasingId()));
        ammoElement.setIdAttribute(ID, true);
        ammoElement.setAttribute(NAME, String.valueOf(ammoModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, ammoModel.getProperties());
        ammoElement.appendChild(propertiesElement);
        return ammoElement;
    }

    private Element createLiquidSourceElement(
            Document document, LiquidSourceModel liquidSourceModel) {
        Element liquidSourceElement = document.createElement(LIQUID_SOURCE_TAG);
        liquidSourceElement.setAttribute(ID, String.valueOf(liquidSourceModel.getLiquidSourceId()));
        liquidSourceElement.setIdAttribute(ID, true);
        liquidSourceElement.setAttribute(NAME, String.valueOf(liquidSourceModel.getModelName()));
        Element propertiesElement =
                createPropertiesElement(document, liquidSourceModel.getProperties());
        liquidSourceElement.appendChild(propertiesElement);
        return liquidSourceElement;
    }

    private Element createFireSourceElement(Document document, FireSourceModel fireSourceModel) {
        Element fireSourceElement = document.createElement(FIRE_SOURCE_TAG);
        fireSourceElement.setAttribute(ID, String.valueOf(fireSourceModel.getFireSourceId()));
        fireSourceElement.setIdAttribute(ID, true);
        fireSourceElement.setAttribute(NAME, String.valueOf(fireSourceModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, fireSourceModel.getProperties());
        fireSourceElement.appendChild(propertiesElement);
        return fireSourceElement;
    }

    private Element createProjectileElement(Document document, ProjectileModel projectileModel) {
        Element projectileElement = document.createElement(PROJECTILE_TAG);
        projectileElement.setAttribute(ID, String.valueOf(projectileModel.getProjectileId()));
        projectileElement.setIdAttribute(ID, true);
        projectileElement.setAttribute(NAME, String.valueOf(projectileModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, projectileModel.getProperties());
        projectileElement.appendChild(propertiesElement);
        if (projectileModel.getCasingModel() != null) {
            projectileElement.setAttribute(
                    AMMO_ID_TAG, String.valueOf(projectileModel.getCasingModel().getCasingId()));
        }
        return projectileElement;
    }

    private Element createLayerElement(Document document, LayerModel layerModel) {
        Element layerElement =
                createElementFromPointsModel(document, layerModel, layerModel.getLayerId(), LAYER_TAG);
        Element decorationsElement = document.createElement(DECORATIONS_TAG);
        layerModel
                .getDecorations()
                .forEach(
                        decorationModel ->
                                decorationsElement.appendChild(createDecorationElement(document, decorationModel)));
        layerElement.appendChild(decorationsElement);
        return layerElement;
    }

    private Element createDecorationElement(Document document, DecorationModel decorationModel) {
        return createElementFromPointsModel(
                document, decorationModel, decorationModel.getDecorationId(), DECORATION_TAG);
    }

    private Element createElementFromPointsModel(
            Document document, PointsModel<?> pointsModel, int id, String tag) {
        Element element = document.createElement(tag);
        element.setAttribute(ID, String.valueOf(id));
        element.setIdAttribute(ID, true);
        element.setAttribute(NAME, pointsModel.getModelName());
        // save vertices
        List<Vector2> points = pointsModel.getPoints();

        Element verticesElement = document.createElement(VERTICES_TAG);
        for (Vector2 v : points) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
            verticesElement.appendChild(vectorElement);
        }
        element.appendChild(verticesElement);

        Vector2[] outline = pointsModel.getOutlinePoints();

        Element outlineElement = document.createElement(OUTLINE_TAG);
        if (outline != null) {
            for (Vector2 v : outline) {
                Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
                outlineElement.appendChild(vectorElement);
            }
        }
        element.appendChild(outlineElement);

        Element referenceVerticesElement = document.createElement(REF_VERTICES_TAG);
        if(pointsModel.getCenter()!=null) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, pointsModel.getCenter());
            referenceVerticesElement.appendChild(vectorElement);
        }
        element.appendChild(referenceVerticesElement);

        Element propertiesElement = createPropertiesElement(document, pointsModel.getProperties());
        element.appendChild(propertiesElement);
        return element;
    }

    private <T extends Properties> T loadProperties(Element propertiesElement, Class<T> theClass)
            throws PersistenceException {
        try {
            Constructor<T> constructor = theClass.getConstructor();
            T properties = constructor.newInstance();
            Field[] allFields = theClass.getDeclaredFields();

            if (theClass.getSuperclass() != null) {
                allFields = Utils.concatWithStream(allFields, theClass.getSuperclass().getDeclaredFields());
            }
            Arrays.stream(allFields)
                    .forEach(
                            field -> {
                                String methodName = "set" + StringUtils.capitalize(field.getName());
                                try {
                                    Method method = theClass.getMethod(methodName, field.getType());
                                    Object value = null;
                                    if (field.getType().isPrimitive()) {
                                        if (field.getType() == float.class) {
                                            try {
                                                value = Float.parseFloat(propertiesElement.getAttribute(field.getName()));
                                            } catch (Throwable t) {
                                                Log.e("Field Not Found", field.getName());
                                            }
                                        } else if (field.getType() == double.class) {
                                            value = Double.parseDouble(propertiesElement.getAttribute(field.getName()));
                                        } else if (field.getType() == int.class) {
                                            value = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                                        } else if (field.getType() == short.class) {
                                            value = Short.parseShort(propertiesElement.getAttribute(field.getName()));
                                        } else if (field.getType() == boolean.class) {
                                            value = Boolean.parseBoolean(propertiesElement.getAttribute(field.getName()));
                                        }
                                        assert (value != null) : "conversion failed";
                                        method.invoke(properties, value);
                                    } else if (field.getType() == String.class) {
                                        String stringValue = propertiesElement.getAttribute(field.getName());
                                        method.invoke(properties, stringValue);
                                    } else if (field.getType() == Color.class) {
                                        int packedColor =
                                                Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                                        Color color = ColorUtils.convertARGBPackedIntToColor(packedColor);
                                        method.invoke(properties, color);
                                    } else if (field.getType() == Vector2.class) {
                                        float x =
                                                Float.parseFloat(propertiesElement.getAttribute(field.getName() + "X"));
                                        float y =
                                                Float.parseFloat(propertiesElement.getAttribute(field.getName() + "Y"));
                                        method.invoke(properties, new Vector2(x, y));
                                    } else if (field.getType() == ProjectileTriggerType.class) {
                                        int v = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                                        method.invoke(properties, ProjectileTriggerType.getFromValue(v));
                                    } else if (field.getType() == Explosive.class) {
                                        int v = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                                        method.invoke(properties, Explosive.values()[v]);
                                    } else if (field.getType() == ItemCategory.class) {
                                        String v = propertiesElement.getAttribute(field.getName());
                                        method.invoke(properties, ItemCategory.fromName(v));
                                    }
                                } catch (Throwable ignored) {
                                }
                            });
            return properties;

        } catch (InvocationTargetException
                 | NoSuchMethodException
                 | IllegalAccessException
                 | InstantiationException e) {
            throw new PersistenceException("Error reading properties:" + theClass);
        }
    }

    private Element createPanelColorsElement(Document document, List<SquareProperties> squarePropertiesList) {
        Element panelColorsElement = document.createElement(PANEL_COLORS_TAG);
        for (SquareProperties squareProperties : squarePropertiesList) {
            Element panelColorElement = document.createElement(PANEL_COLOR_TAG);
            panelColorElement.setAttribute(ID, String.valueOf(squareProperties.getSquareId()));
            Color color = squareProperties.getColor();
            int packedIntColor = color.getARGBPackedInt();
            panelColorElement.setAttribute(
                    "color", String.valueOf(packedIntColor));
            panelColorsElement.appendChild(panelColorElement);

        }
        return panelColorsElement;
    }

    private List<SquareProperties> readPanelColorsElement(Element panelColorsElement) {
        NodeList childNodes = panelColorsElement.getChildNodes();
        List<SquareProperties> result = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element colorElement = (Element) childNodes.item(i);
            int packedColor =
                    Integer.parseInt(colorElement.getAttribute("color"));
            Color color = ColorUtils.convertARGBPackedIntToColor(packedColor);
            int id = Integer.parseInt(colorElement.getAttribute(ID));
            SquareProperties squareProperties = new SquareProperties(id, color);
            result.add(squareProperties);
        }
        return result;
    }

    private Element createPropertiesElement(Document document, Object properties) {
        Element propertiesElement = document.createElement(PROPERTIES_TAG);
        Field[] allFields = properties.getClass().getDeclaredFields();
        if (properties.getClass().getSuperclass() != null) {
            allFields =
                    Utils.concatWithStream(
                            allFields, properties.getClass().getSuperclass().getDeclaredFields());
        }
        Arrays.stream(allFields)
                .forEach(
                        field -> {
                            try {
                                if (field.getType() == ProjectileModel.class
                                        || field.getType().isPrimitive()
                                        || field.getType() == Color.class
                                        || field.getType() == ProjectileTriggerType.class
                                        || field.getType() == ItemCategory.class
                                        || field.getType() == Explosive.class
                                        || field.getType() == Vector2.class
                                        || field.getType() == String.class) {
                                    String methodName =
                                            (field.getType() == boolean.class ? "is" : "get")
                                                    + StringUtils.capitalize(field.getName());
                                    Method method = properties.getClass().getMethod(methodName);
                                    if (field.getType().isPrimitive() || field.getType() == String.class) {
                                        Object value = method.invoke(properties);
                                        if (value != null) {
                                            propertiesElement.setAttribute(field.getName(), value.toString());
                                        }
                                    } else if (field.getType() == Color.class) {
                                        Color color = (Color) method.invoke(properties);
                                        if (color != null) {
                                            int packedIntColor = color.getARGBPackedInt();
                                            propertiesElement.setAttribute(
                                                    field.getName(), String.valueOf(packedIntColor));
                                        }
                                    } else if (field.getType() == ProjectileTriggerType.class) {
                                        ProjectileTriggerType projectileTriggerType =
                                                (ProjectileTriggerType) method.invoke(properties);
                                        if (projectileTriggerType != null) {
                                            int value = projectileTriggerType.getValue();
                                            propertiesElement.setAttribute(field.getName(), String.valueOf(value));
                                        }
                                    } else if (field.getType() == ItemCategory.class) {
                                        ItemCategory itemCategory =
                                                (ItemCategory) method.invoke(properties);
                                        if (itemCategory != null) {

                                            propertiesElement.setAttribute(field.getName(), itemCategory.toString());
                                        }
                                    } else if (field.getType() == Explosive.class) {
                                        Explosive explosive = (Explosive) method.invoke(properties);
                                        if (explosive != null) {
                                            int value = explosive.ordinal();
                                            propertiesElement.setAttribute(field.getName(), String.valueOf(value));
                                        }
                                    } else if (field.getType() == Vector2.class) {
                                        Vector2 vector = (Vector2) method.invoke(properties);
                                        if (vector != null) {
                                            propertiesElement.setAttribute(
                                                    field.getName() + "X", String.valueOf(vector.x));
                                            propertiesElement.setAttribute(
                                                    field.getName() + "Y", String.valueOf(vector.y));
                                        }
                                    }
                                }
                            } catch (NoSuchMethodException
                                     | IllegalAccessException
                                     | InvocationTargetException ignored) {

                            }
                        });
        return propertiesElement;
    }

    public ToolModel loadToolModel(String toolFileName, boolean editor, boolean assets)
            throws IOException, ParserConfigurationException, SAXException, PersistenceException {
        if (toolFileName.isEmpty()) {
            return new ToolModel(scene);
        }
        InputStream fis = assets ? gameActivity.getAssets().open(String.format("%s/%s", XML_FOLDER, toolFileName)) : gameActivity.openFileInput(toolFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document xml = docBuilder.parse(fis);
        fis.close();
        Element toolElement = xml.getDocumentElement();
        String toolName = toolElement.getAttribute(NAME);
        ItemCategory category = ItemCategory.fromName(toolElement.getAttribute(CATEGORY));
        ToolModel toolModel = new ToolModel(scene);
        toolModel.getProperties().setToolName(toolName);
        toolModel.setCategory(category);
        Element bodiesElement = (Element) toolElement.getElementsByTagName(BODIES_TAG).item(0);
        List<BodyModel> bodyModels = new ArrayList<>();
        for (int i = 0; i < bodiesElement.getChildNodes().getLength(); i++) {
            Element bodyElement = (Element) bodiesElement.getChildNodes().item(i);
            BodyModel bodyModel;
            try {
                bodyModel = readBodyModel(bodyElement, Integer.parseInt(bodyElement.getAttribute(ID)));
                bodyModels.add(bodyModel);
            } catch (PersistenceException e) {
                System.err.println(e.getMessage());
                throw new PersistenceException("Error reading body model:" + ID, e);
            }
        }

        List<JointModel> jointModels = new ArrayList<>();
        Element jointsElement = (Element) toolElement.getElementsByTagName(JOINTS_TAG).item(0);
        if (jointsElement != null) {
            for (int i = 0; i < jointsElement.getChildNodes().getLength(); i++) {
                Element jointElement = (Element) jointsElement.getChildNodes().item(i);
                JointModel jointModel;
                try {
                    jointModel =
                            readJointModel(
                                    Integer.parseInt(jointElement.getAttribute(ID)), jointElement, bodyModels);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading joint:" + ID, e);
                }
                jointModels.add(jointModel);
            }
            toolModel.getJoints().addAll(jointModels);
        }

        populateSpecialModels(bodyModels,jointModels);

        toolModel.getBodies().addAll(bodyModels);


        if (editor) {
            Element panelColorsElement = (Element) toolElement.getElementsByTagName(PANEL_COLORS_TAG).item(0);
            if (panelColorsElement != null) {
                List<SquareProperties> list = readPanelColorsElement(panelColorsElement);
                toolModel.getColorPanelProperties().getSquarePropertiesList().addAll(list);
            }

            Element imageShapeElement = (Element) toolElement.getElementsByTagName(IMAGE_SHAPE_TAG).item(0);
            if (imageShapeElement != null) {
                Element propertiesElement = (Element) imageShapeElement.getElementsByTagName(PROPERTIES_TAG).item(0);
                ImageShapeModel imageShapeModel = loadProperties(propertiesElement, ImageShapeModel.class);
                toolModel.setImageShapeModel(imageShapeModel);
            }
        }
        this.initializeCounters(toolModel);
        return toolModel;
    }

    private static void populateSpecialModels(List<BodyModel> bodyModels, List<JointModel> jointModels) {
        List<FireSourceModel> allFireSources = new ArrayList<>();
        for (BodyModel model : bodyModels) {
            ArrayList<FireSourceModel> fireSourceModels = model.getFireSourceModels();
            allFireSources.addAll(fireSourceModels);
        }
        List<LiquidSourceModel> allLiquidSources = new ArrayList<>();
        for (BodyModel model : bodyModels) {
            ArrayList<LiquidSourceModel> liquidSourceModels = model.getLiquidSourceModels();
            allLiquidSources.addAll(liquidSourceModels);
        }
        List<ProjectileModel> allProjectiles = new ArrayList<>();
        for (BodyModel model : bodyModels) {
            ArrayList<ProjectileModel> projectiles = model.getProjectileModels();
            allProjectiles.addAll(projectiles);
        }
        List<BombModel> allBombs =
                bodyModels.stream()
                        .map(BodyModel::getBombModels)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        for (BodyModel bodyModel : bodyModels) {
            for (UsageModel<?> e : bodyModel.getUsageModels()) {
                if (e.getType() == BodyUsageCategory.ROCKET
                        || e.getType() == BodyUsageCategory.MISSILE) {
                    RocketProperties rocketProperties = (RocketProperties) e.getProperties();
                    rocketProperties
                            .getFireSourceModelList()
                            .addAll(
                                    allFireSources.stream()
                                            .filter(
                                                    p ->
                                                            rocketProperties
                                                                    .getFireSourceIds()
                                                                    .contains(p.getFireSourceId()))
                                            .collect(Collectors.toList()));
                } else if (e.getType() == BodyUsageCategory.FLAME_THROWER) {
                    FlameThrowerProperties flameThrowerProperties =
                            (FlameThrowerProperties) e.getProperties();
                    flameThrowerProperties
                            .getFireSources()
                            .addAll(
                                    allFireSources.stream()
                                            .filter(
                                                    p ->
                                                            flameThrowerProperties
                                                                    .getFireSourceIds()
                                                                    .contains(p.getFireSourceId()))
                                            .collect(Collectors.toList()));
                } else if (e.getType()==BodyUsageCategory.SHOOTER||e.getType()==BodyUsageCategory.SHOOTER_CONTINUOUS || e.getType() == BodyUsageCategory.ROCKET_LAUNCHER || e.getType() == BodyUsageCategory.BOW) {
                    RangedProperties rangedProperties = (RangedProperties) e.getProperties();
                    rangedProperties
                            .getProjectileModelList()
                            .addAll(
                                    allProjectiles.stream()
                                            .filter(
                                                    p -> rangedProperties.getProjectileIds().contains(p.getProjectileId()))
                                            .collect(Collectors.toList()));
                } else if (e.getType() == BodyUsageCategory.LIQUID_CONTAINER) {
                    LiquidContainerProperties liquidContainerProperties =
                            (LiquidContainerProperties) e.getProperties();
                    liquidContainerProperties
                            .getLiquidSourceModelList()
                            .addAll(
                                    allLiquidSources.stream()
                                            .filter(
                                                    p ->
                                                            liquidContainerProperties
                                                                    .getLiquidSourceIds()
                                                                    .contains(p.getLiquidSourceId()))
                                            .collect(Collectors.toList()));
                } else if (e.getType()==BodyUsageCategory.TIME_BOMB||e.getType()==BodyUsageCategory.IMPACT_BOMB) {
                    BombUsageProperties bombUsageProperties = (BombUsageProperties) e.getProperties();
                    bombUsageProperties
                            .getBombModelList()
                            .addAll(
                                    allBombs.stream()
                                            .filter(p -> bombUsageProperties.getBombIds().contains(p.getBombId()))
                                            .collect(Collectors.toList()));
                } else if(e.getType()==BodyUsageCategory.MOTOR_CONTROL){
                    MotorControlProperties motorControlProperties = (MotorControlProperties) e.getProperties();
                List<JointModel> list =    jointModels.stream().filter(jointModel -> motorControlProperties.getJointIds().contains(jointModel.getJointId())).collect(Collectors.toList());
                motorControlProperties.setJointModels(list);
                }
            }
        }
    }

    DecorationModel readDecorationModel(Element element, LayerModel layerModel, int bodyId, int layerId, int decorationId)
            throws PersistenceException {
        Element verticesElement = (Element) element.getElementsByTagName(VERTICES_TAG).item(0);
        Element refVerticesElement = (Element) element.getElementsByTagName(REF_VERTICES_TAG).item(0);
        List<Vector2> vertices = readVectors(verticesElement);
        List<Vector2> referencePoints;
        if (refVerticesElement != null) {
            referencePoints = readVectors(refVerticesElement);
        } else {
            referencePoints = new ArrayList<>();
        }
        Element propertiesElement = (Element) element.getElementsByTagName(PROPERTIES_TAG).item(0);
        DecorationProperties decorationProperties = loadProperties(propertiesElement, DecorationProperties.class);
        DecorationModel decorationModel =
                new DecorationModel(
                        bodyId,
                        layerId,
                        decorationId,
                        element.getAttribute(NAME),
                        decorationProperties,
                        layerModel);
        decorationModel.setPoints(vertices);
        if(referencePoints!=null&&referencePoints.size()>0) {
            decorationModel.setCenter(referencePoints.get(0));
        }
        decorationModel.setProperties(decorationProperties);
        return decorationModel;
    }

    LayerModel readLayerModel(Element element, BodyModel bodyModel, int bodyId, int layerId)
            throws PersistenceException {
        Element verticesElement = (Element) element.getElementsByTagName(VERTICES_TAG).item(0);
        Element refVerticesElement = (Element) element.getElementsByTagName(REF_VERTICES_TAG).item(0);
        List<Vector2> vertices = readVectors(verticesElement);
        List<Vector2> referencePoints;
        if (refVerticesElement != null) {
            referencePoints = readVectors(refVerticesElement);
        } else {
            referencePoints = new ArrayList<>();
        }
        Element propertiesElement = (Element) element.getElementsByTagName(PROPERTIES_TAG).item(0);
        LayerProperties layerProperties = loadProperties(propertiesElement, LayerProperties.class);
        LayerModel layerModel =
                new LayerModel(
                        bodyModel.getBodyId(),
                        layerId,
                        element.getAttribute(NAME),
                        layerProperties,
                        bodyModel);
        layerModel.setPoints(vertices);
        if(referencePoints!=null&&referencePoints.size()>0) {
            layerModel.setCenter(referencePoints.get(0));
        }
        layerModel.setProperties(layerProperties);

        Element decorationsElement = (Element) element.getElementsByTagName(DECORATIONS_TAG).item(0);
        List<DecorationModel> decorations = new ArrayList<>();
        if (decorationsElement != null) {
            NodeList childNodes = decorationsElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Element decorationElement = (Element) childNodes.item(i);
                DecorationModel decorationModel;
                try {
                    decorationModel =
                            readDecorationModel(
                                    decorationElement, layerModel, bodyId, layerId, Integer.parseInt(decorationElement.getAttribute(ID)));
                    decorations.add(decorationModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading layer model", e);
                }
            }
        }
        layerModel.getDecorations().addAll(decorations);

        return layerModel;
    }

    private void initializeCounters(ToolModel toolModel) {
        if (toolModel.getBodies().size() > 0) {
            toolModel
                    .getBodyCounter()
                    .set(toolModel.getBodies().stream().mapToInt(BodyModel::getBodyId).max().getAsInt() + 1);
        }
        if (toolModel.getJoints().size() > 0) {
            toolModel
                    .getJointCounter()
                    .set(
                            toolModel.getJoints().stream().mapToInt(JointModel::getJointId).max().getAsInt() + 1);
        }

        for (BodyModel body : toolModel.getBodies()) {
            if (body.getCasingModels().size() > 0) {
                toolModel
                        .getAmmoCounter()
                        .set(
                                body.getCasingModels().stream().mapToInt(CasingModel::getCasingId).max().orElse(-1)
                                        + 1);
            }
            if (body.getBombModels().size() > 0) {
                toolModel
                        .getBombCounter()
                        .set(body.getBombModels().stream().mapToInt(BombModel::getBombId).max().orElse(-1) + 1);
            }
            if (body.getProjectileModels().size() > 0) {
                toolModel
                        .getProjectileCounter()
                        .set(
                                body.getProjectileModels().stream()
                                        .mapToInt(ProjectileModel::getProjectileId)
                                        .max()
                                        .orElse(-1)
                                        + 1);
            }
            if (body.getFireSourceModels().size() > 0) {
                toolModel
                        .getFireSourceCounter()
                        .set(
                                body.getFireSourceModels().stream()
                                        .mapToInt(FireSourceModel::getFireSourceId)
                                        .max()
                                        .orElse(-1)
                                        + 1);
            }
            if (body.getLiquidSourceModels().size() > 0) {
                toolModel
                        .getLiquidSourceCounter()
                        .set(
                                body.getLiquidSourceModels().stream()
                                        .mapToInt(LiquidSourceModel::getLiquidSourceId)
                                        .max()
                                        .orElse(-1)
                                        + 1);
            }
            if (body.getDragModels().size() > 0) {
                toolModel
                        .getDragCounter()
                        .set(body.getDragModels().stream().mapToInt(DragModel::getDragId).max().orElse(-1) + 1);
            }
            if (body.getLayers().size() > 0) {
                body.getLayerCounter()
                        .set(body.getLayers().stream().mapToInt(LayerModel::getLayerId).max().orElse(-1) + 1);
            }
            for (LayerModel layerModel : body.getLayers()) {
                if (layerModel.getDecorations().size() > 0) {
                    layerModel
                            .getDecorationCounter()
                            .set(
                                    layerModel.getDecorations().stream()
                                            .mapToInt(DecorationModel::getDecorationId)
                                            .max()
                                            .orElse(-1)
                                            + 1);
                }
            }
        }
    }

    List<Vector2> readVectors(Element element) {
        NodeList childNodes = element.getChildNodes();
        List<Vector2> vertices = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element vectorElement = (Element) childNodes.item(i);
            Vector2 v = XmlUtils.readVector(vectorElement);
            vertices.add(v);
        }
        return vertices;
    }

    BodyModel readBodyModel(Element element, int bodyId) throws PersistenceException {
        BodyModel bodyModel = new BodyModel(bodyId);
        Element propertiesElement = (Element) element.getElementsByTagName(PROPERTIES_TAG).item(0);
        BodyProperties bodyProperties = loadProperties(propertiesElement, BodyProperties.class);
        bodyModel.setProperties(bodyProperties);

        Element layersElement = (Element) element.getElementsByTagName(LAYERS_TAG).item(0);
        List<LayerModel> layers = new ArrayList<>();
        NodeList childNodes = layersElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element layerElement = (Element) childNodes.item(i);
            LayerModel layerModel;
            try {
                layerModel =
                        readLayerModel(
                                layerElement, bodyModel, bodyId, Integer.parseInt(layerElement.getAttribute(ID)));
                layers.add(layerModel);
            } catch (PersistenceException e) {
                throw new PersistenceException("Error reading layer model", e);
            }
        }
        bodyModel.getLayers().addAll(layers);

        Element ammoListElement = (Element) element.getElementsByTagName(AMMO_LIST_TAG).item(0);
        List<CasingModel> ammoModels = bodyModel.getCasingModels();
        if (ammoListElement != null) {
            for (int i = 0; i < ammoListElement.getChildNodes().getLength(); i++) {
                Element ammoElement = (Element) ammoListElement.getChildNodes().item(i);
                CasingModel ammoModel;
                try {
                    ammoModel =
                            readAmmoModel(ammoElement, bodyId, Integer.parseInt(ammoElement.getAttribute(ID)));
                    ammoModels.add(ammoModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading ammo model", e);
                }
            }
        }

        Element fireSourceListElement =
                (Element) element.getElementsByTagName(FIRE_SOURCE_LIST_TAG).item(0);
        List<FireSourceModel> fireSourceModels = bodyModel.getFireSourceModels();
        if (fireSourceListElement != null) {
            for (int i = 0; i < fireSourceListElement.getChildNodes().getLength(); i++) {
                Element fireSourceElement = (Element) fireSourceListElement.getChildNodes().item(i);
                FireSourceModel fireSourceModel;
                try {
                    fireSourceModel =
                            readFireSourceModel(
                                    fireSourceElement, bodyId, Integer.parseInt(fireSourceElement.getAttribute(ID)));
                    fireSourceModels.add(fireSourceModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading fire source model", e);
                }
            }
        }

        Element liquidSourceListElement =
                (Element) element.getElementsByTagName(LIQUID_SOURCE_LIST_TAG).item(0);
        List<LiquidSourceModel> liquidSourceModels = bodyModel.getLiquidSourceModels();
        if (liquidSourceListElement != null) {
            for (int i = 0; i < liquidSourceListElement.getChildNodes().getLength(); i++) {
                Element liquidSourceElement = (Element) liquidSourceListElement.getChildNodes().item(i);
                LiquidSourceModel liquidSourceModel;
                try {
                    liquidSourceModel =
                            readLiquidSourceModel(
                                    liquidSourceElement,
                                    bodyId,
                                    Integer.parseInt(liquidSourceElement.getAttribute(ID)));
                    liquidSourceModels.add(liquidSourceModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading liquid source model", e);
                }
            }
        }

        Element projectilesElement = (Element) element.getElementsByTagName(PROJECTILES_TAG).item(0);
        if (projectilesElement != null) {
            List<ProjectileModel> projectiles = bodyModel.getProjectileModels();
            for (int i = 0; i < projectilesElement.getChildNodes().getLength(); i++) {
                Element projectileElement = (Element) projectilesElement.getChildNodes().item(i);
                ProjectileModel projectileModel;
                try {
                    projectileModel =
                            readProjectileModel(
                                    projectileElement, bodyId, Integer.parseInt(projectileElement.getAttribute(ID)));
                    projectiles.add(projectileModel);
                    if (!projectileElement.getAttribute(AMMO_ID_TAG).isEmpty()) {
                        int ammoId = Integer.parseInt(projectileElement.getAttribute(AMMO_ID_TAG));
                        projectileModel.setCasingModel(
                                ammoModels.stream()
                                        .filter(am -> am.getCasingId() == ammoId)
                                        .findFirst()
                                        .orElseGet(null));
                    }
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading projectile model", e);
                }
            }
        }

        Element bombsElement = (Element) element.getElementsByTagName(BOMB_LIST_TAG).item(0);
        if (bombsElement != null) {
            List<BombModel> bombModels = bodyModel.getBombModels();
            for (int i = 0; i < bombsElement.getChildNodes().getLength(); i++) {
                Element bombElement = (Element) bombsElement.getChildNodes().item(i);
                BombModel bombModel;
                try {
                    bombModel =
                            readBombModel(bombElement, bodyId, Integer.parseInt(bombElement.getAttribute(ID)));
                    bombModels.add(bombModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading bomb model", e);
                }
            }
        }

        Element specialPointsElement = (Element) element.getElementsByTagName(SPECIAL_POINTS_LIST_TAG).item(0);
        if (specialPointsElement != null) {
            List<SpecialPointModel> specialPointModels = bodyModel.getSpecialPointModels();
            for (int i = 0; i < specialPointsElement.getChildNodes().getLength(); i++) {
                Element specialPointElement = (Element) specialPointsElement.getChildNodes().item(i);
                SpecialPointModel specialPointModel;
                try {
                    specialPointModel =
                            readSpecialPointModel(specialPointElement, bodyId, Integer.parseInt(specialPointElement.getAttribute(ID)));
                    specialPointModels.add(specialPointModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading special point model", e);
                }
            }
        }


        Element dragListElement = (Element) element.getElementsByTagName(DRAG_LIST_TAG).item(0);
        if (dragListElement != null) {
            List<DragModel> dragModels = bodyModel.getDragModels();
            for (int i = 0; i < dragListElement.getChildNodes().getLength(); i++) {
                Element dragElement = (Element) dragListElement.getChildNodes().item(i);
                DragModel dragModel;
                try {
                    dragModel =
                            readDragModel(dragElement, bodyId, Integer.parseInt(dragElement.getAttribute(ID)));
                    dragModels.add(dragModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading drag model", e);
                }
            }
        }

        Element usageListElement = (Element) element.getElementsByTagName(USAGE_LIST_TAG).item(0);
        if (usageListElement != null) {
            for (int i = 0; i < usageListElement.getChildNodes().getLength(); i++) {
                Element usageElement = (Element) usageListElement.getChildNodes().item(i);
                UsageModel<?> usageModel = readUsageModel(usageElement);
                bodyModel.getUsageModels().add(usageModel);
            }
        }
        return bodyModel;
    }

    private UsageModel<?> readUsageModel(Element usageElement) throws PersistenceException {
        Element propertiesElement = (Element) usageElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        try {
            BodyUsageCategory bodyUsageCategory =
                    BodyModel.allCategories.stream()
                            .filter(bm -> bm.name().equals(usageElement.getAttribute(USAGE_TYPE_TAG)))
                            .findFirst()
                            .get();
            UsageModel<Properties> usageModel = new UsageModel<>("", bodyUsageCategory);
            List<Integer> usageProjectileIds;
            List<Integer> usageBombIds;
            List<Integer> usageSensitiveLayersIds;
            List<Integer> usageFireSourceIds;
            List<Integer> usageLiquidSourceIds;
            List<Integer> usageMotorJointIds;
            switch (bodyUsageCategory) {
                case SHOOTER:
                    usageProjectileIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                    ShooterProperties rangedShooterProperties =
                            loadProperties(propertiesElement, ShooterProperties.class);
                    rangedShooterProperties.setProjectileIds(usageProjectileIds);
                    usageModel.setProperties(rangedShooterProperties);
                    break;
                case SHOOTER_CONTINUOUS:
                    usageProjectileIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                    ContinuousShooterProperties rangedAutomaticProperties =
                            loadProperties(propertiesElement, ContinuousShooterProperties.class);
                    rangedAutomaticProperties.setProjectileIds(usageProjectileIds);
                    usageModel.setProperties(rangedAutomaticProperties);
                    break;
                case BOW:
                    usageProjectileIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                    BowProperties bowProperties =
                            loadProperties(propertiesElement, BowProperties.class);
                    bowProperties.setProjectileIds(usageProjectileIds);
                    usageModel.setProperties(bowProperties);
                    break;
                case TIME_BOMB:
                    TimeBombUsageProperties timeBombUsageProperties =
                            loadProperties(propertiesElement, TimeBombUsageProperties.class);
                    usageModel.setProperties(timeBombUsageProperties);
                    usageBombIds =
                            convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_BOMBS_TAG));
                    timeBombUsageProperties.setBombIds(usageBombIds);
                    break;
                case IMPACT_BOMB:
                    ImpactBombUsageProperties impactBombUsageProperties =
                            loadProperties(propertiesElement, ImpactBombUsageProperties.class);
                    usageModel.setProperties(impactBombUsageProperties);
                    usageBombIds =
                            convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_BOMBS_TAG));
                    usageSensitiveLayersIds =
                            convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_SENSITIVE_LAYERS_TAG));
                    impactBombUsageProperties.setBombIds(usageBombIds);
                    impactBombUsageProperties.setSensitiveLayers(usageSensitiveLayersIds);
                    break;
                case SLASHER:
                    SlashProperties slashProperties = loadProperties(propertiesElement, SlashProperties.class);
                    usageModel.setProperties(slashProperties);
                    break;
                case BLUNT:
                    break;
                case STABBER:
                    StabProperties stabProperties = loadProperties(propertiesElement, StabProperties.class);
                    usageModel.setProperties(stabProperties);
                    break;
                case THROWING:
                    ThrowProperties throwProperties = loadProperties(propertiesElement, ThrowProperties.class);
                    usageModel.setProperties(throwProperties);
                    break;
                case FLAME_THROWER:
                    FlameThrowerProperties flameThrowerProperties =
                            loadProperties(propertiesElement, FlameThrowerProperties.class);
                    usageFireSourceIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_FIRE_SOURCES_TAG));
                    flameThrowerProperties.setFireSourceIds(usageFireSourceIds);
                    usageModel.setProperties(flameThrowerProperties);
                    break;
                case ROCKET:
                    RocketProperties rocketProperties =
                            loadProperties(propertiesElement, RocketProperties.class);
                    usageFireSourceIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_FIRE_SOURCES_TAG));
                    rocketProperties.setFireSourceIds(usageFireSourceIds);
                    usageModel.setProperties(rocketProperties);
                    break;
                case MISSILE:
                    MissileProperties missileProperties =
                            loadProperties(propertiesElement, MissileProperties.class);
                    usageFireSourceIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_FIRE_SOURCES_TAG));
                    missileProperties.setFireSourceIds(usageFireSourceIds);
                    usageModel.setProperties(missileProperties);
                    break;
                case LIQUID_CONTAINER:
                    LiquidContainerProperties liquidContainerProperties =
                            loadProperties(propertiesElement, LiquidContainerProperties.class);
                    usageLiquidSourceIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_LIQUID_SOURCES_TAG));
                    liquidContainerProperties.setLiquidSourceIds(usageLiquidSourceIds);
                    usageModel.setProperties(liquidContainerProperties);
                    break;
                case ROCKET_LAUNCHER:
                    usageProjectileIds =
                            convertStringToIntList(
                                    propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                    RocketLauncherProperties rocketLauncherProperties =
                            loadProperties(propertiesElement, RocketLauncherProperties.class);
                    rocketLauncherProperties.setProjectileIds(usageProjectileIds);
                    usageModel.setProperties(rocketLauncherProperties);
                    break;
                case HEAVY:
                    HeavyProperties heavyProperties =
                            loadProperties(propertiesElement, HeavyProperties.class);
                    usageModel.setProperties(heavyProperties);
                    break;
                case MOTOR_CONTROL:
                    MotorControlProperties motorControlProperties =
                            loadProperties(propertiesElement, MotorControlProperties.class);
                    usageMotorJointIds =
                            convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_JOINTS_TAG));
                    motorControlProperties.setJointIds(usageMotorJointIds);
                    usageModel.setProperties(motorControlProperties);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + bodyUsageCategory);
            }
            return usageModel;
        } catch (Throwable t){
            System.out.println("Hello");
            return null;
        }
    }

    private CasingModel readAmmoModel(Element ammoElement, int bodyId, int ammoId)
            throws PersistenceException {
        Element propertiesElement = (Element) ammoElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        CasingModel ammoModel = new CasingModel(bodyId, ammoId, ammoElement.getAttribute(NAME));
        CasingProperties casingProperties = loadProperties(propertiesElement, CasingProperties.class);
        ammoModel.setProperties(casingProperties);
        return ammoModel;
    }

    private FireSourceModel readFireSourceModel(
            Element fireSourceElement, int bodyId, int fireSourceId) throws PersistenceException {
        Element propertiesElement =
                (Element) fireSourceElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        FireSourceModel fireSourceModel =
                new FireSourceModel(bodyId, fireSourceId, fireSourceElement.getAttribute(NAME));
        FireSourceProperties fireSourceProperties =
                loadProperties(propertiesElement, FireSourceProperties.class);
        fireSourceModel.setProperties(fireSourceProperties);
        return fireSourceModel;
    }

    private LiquidSourceModel readLiquidSourceModel(
            Element liquidSourceElement, int bodyId, int liquidSourceId) throws PersistenceException {
        Element propertiesElement =
                (Element) liquidSourceElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        LiquidSourceModel liquidSourceModel =
                new LiquidSourceModel(bodyId, liquidSourceId, liquidSourceElement.getAttribute(NAME));
        LiquidSourceProperties liquidSourceProperties =
                loadProperties(propertiesElement, LiquidSourceProperties.class);
        liquidSourceModel.setProperties(liquidSourceProperties);
        return liquidSourceModel;
    }

    private DragModel readDragModel(Element dragElement, int bodyId, int dragId)
            throws PersistenceException {
        Element propertiesElement = (Element) dragElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        DragModel dragModel = new DragModel(bodyId, dragId);
        DragProperties dragProperties = loadProperties(propertiesElement, DragProperties.class);
        dragModel.setProperties(dragProperties);
        return dragModel;
    }

    private BombModel readBombModel(Element bombElement, int bodyId, int bombId)
            throws PersistenceException {
        Element propertiesElement = (Element) bombElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        BombModel bombModel = new BombModel(bodyId, bombId);
        BombProperties bombProperties = loadProperties(propertiesElement, BombProperties.class);
        bombModel.setProperties(bombProperties);
        return bombModel;
    }

    private SpecialPointModel readSpecialPointModel(Element specialPointElement, int bodyId, int id)
            throws PersistenceException {
        Element propertiesElement = (Element) specialPointElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        SpecialPointModel specialPointModel = new SpecialPointModel(bodyId, id);
        SpecialPointProperties specialPointProperties = loadProperties(propertiesElement, SpecialPointProperties.class);
        specialPointModel.setProperties(specialPointProperties);
        return specialPointModel;
    }

    private ProjectileModel readProjectileModel(
            Element projectileElement, int bodyId, int projectileId) throws PersistenceException {
        Element propertiesElement =
                (Element) projectileElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        ProjectileModel projectileModel =
                new ProjectileModel(bodyId, projectileId, projectileElement.getAttribute(NAME));
        ProjectileProperties projectileProperties =
                loadProperties(propertiesElement, ProjectileProperties.class);
        projectileModel.setProperties(projectileProperties);
        return projectileModel;
    }

    private JointModel readJointModel(int jointId, Element jointElement, List<BodyModel> bodyModels)
            throws PersistenceException {
        JointDef.JointType jointType =
                JointDef.JointType.valueTypes[
                        Integer.parseInt(jointElement.getAttribute(JOINT_TYPE_ATTRIBUTE))];

        JointModel jointModel = new JointModel(jointId, jointType);

        jointModel.getProperties().setCollideConnected(
                Boolean.parseBoolean(jointElement.getAttribute(JOINT_COLLIDE_CONNECTED_ATTRIBUTE)));
        jointModel
                .getProperties().getLocalAnchorA()
                .set(XmlUtils.readVector((Element) jointElement.getElementsByTagName(VECTOR_TAG).item(0)));
        jointModel
                .getProperties().getLocalAnchorB()
                .set(XmlUtils.readVector((Element) jointElement.getElementsByTagName(VECTOR_TAG).item(1)));
        try {
            int bodyAId = Integer.parseInt(jointElement.getAttribute(BODY_A_ID_JOINT_ATTRIBUTE));
            int bodyBId = Integer.parseInt(jointElement.getAttribute(BODY_B_ID_JOINT_ATTRIBUTE));
            BodyModel bodyModelA =
                    bodyModels.stream().filter(bodyModel -> bodyModel.getBodyId() == bodyAId).findFirst().get();
            BodyModel bodyModelB =
                    bodyModels.stream().filter(bodyModel -> bodyModel.getBodyId() == bodyBId).findFirst().get();
            jointModel.setBodyModel1(bodyModelA);
            jointModel.setBodyModel2(bodyModelB);
        } catch (Throwable t) {
        }
        switch (jointType) {
            case WeldJoint:
                break;
            case RevoluteJoint:
                jointModel.getProperties().setEnableMotor(Boolean.parseBoolean(jointElement.getAttribute("enableMotor")));
                jointModel.getProperties().setMaxMotorTorque(Float.parseFloat(jointElement.getAttribute("maxMotorTorque")));
                jointModel.getProperties().setMotorSpeed(Float.parseFloat(jointElement.getAttribute("motorSpeed")));
                jointModel.getProperties().setEnableLimit(Boolean.parseBoolean(jointElement.getAttribute("enableLimit")));
                jointModel.getProperties().setLowerAngle(Float.parseFloat(jointElement.getAttribute("lowerAngle")));
                jointModel.getProperties().setUpperAngle(Float.parseFloat(jointElement.getAttribute("upperAngle")));
                jointModel.getProperties().setReferenceAngle(Float.parseFloat(jointElement.getAttribute("referenceAngle")));
                break;
            case PrismaticJoint:
                jointModel.getProperties().setEnableLimit(Boolean.parseBoolean(jointElement.getAttribute("enableLimit")));
                jointModel.getProperties().setEnableMotor(Boolean.parseBoolean(jointElement.getAttribute("enableMotor")));
                jointModel.getProperties().setMaxMotorForce(Float.parseFloat(jointElement.getAttribute("maxMotorForce")));
                jointModel.getProperties().setMotorSpeed(Float.parseFloat(jointElement.getAttribute("motorSpeed")));
                jointModel.getProperties().setLowerTranslation(
                        Float.parseFloat(jointElement.getAttribute("lowerTranslation")));
                jointModel.getProperties().setUpperTranslation(
                        Float.parseFloat(jointElement.getAttribute("upperTranslation")));
                jointModel.getProperties().setReferenceAngle(Float.parseFloat(jointElement.getAttribute("referenceAngle")));
                jointModel
                        .getProperties().getLocalAxis1()
                        .set(XmlUtils.readVector((Element) jointElement.getElementsByTagName(VECTOR_TAG).item(2)));
                break;
            case DistanceJoint:
                jointModel.getProperties().setDampingRatio(Float.parseFloat(jointElement.getAttribute("dampingRatio")));
                jointModel.getProperties().setLength(Float.parseFloat(jointElement.getAttribute("length")));
                jointModel.getProperties().setFrequencyHz(Float.parseFloat(jointElement.getAttribute("frequencyHz")));
                break;
            case PulleyJoint:
            case MouseJoint:
            case GearJoint:
            case LineJoint:
            case FrictionJoint:
                break;
        }

        return jointModel;
    }
}
