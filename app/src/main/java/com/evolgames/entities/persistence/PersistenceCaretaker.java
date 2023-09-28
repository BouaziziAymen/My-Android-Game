package com.evolgames.entities.persistence;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.properties.BombProperties;
import com.evolgames.entities.properties.CasingProperties;
import com.evolgames.entities.properties.Explosive;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.properties.usage.AutomaticProperties;
import com.evolgames.entities.properties.usage.BombUsageProperties;
import com.evolgames.entities.properties.usage.FuzeBombUsageProperties;
import com.evolgames.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.entities.properties.usage.ManualProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.SemiAutomaticProperties;
import com.evolgames.entities.properties.usage.SlashProperties;
import com.evolgames.entities.properties.usage.StabProperties;
import com.evolgames.entities.properties.usage.ThrowProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.helpers.utilities.XmlUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.color.ColorUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class PersistenceCaretaker {

    public static final String LAYER_TAG = "layer";
    public static final String DECORATION_TAG = "decoration";
    public static final String PROPERTIES_TAG = "properties";
    public static final String VERTICES_TAG = "vertices";
    public static final String REF_VERTICES_TAG = "refVertices";
    public static final String OUTLINE_TAG = "outline";
    public static final String BODY_TAG = "body";
    public static final String PROJECTILES_TAG = "projectiles";
    public static final String AMMO_LIST_TAG = "ammoList";
    public static final String USAGE_LIST_TAG = "usageList";
    public static final String USAGE_TAG = "usage";
    public static final String USAGE_PROPERTIES_PROJECTILES_TAG = "projectiles";
    public static final String USAGE_PROPERTIES_BOMBS_TAG = "bombs";
    public static final String USAGE_TYPE_TAG = "type";
    public static final String AMMO_TAG = "ammo";
    public static final String AMMO_ID_TAG = "ammoId";
    public static final String TOOL_TAG = "tool";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String VECTOR_TAG = "v";
    public static final String BODIES_TAG = "bodies";
    public static final String LAYERS_TAG = "layers";
    public static final String PROJECTILE_TAG = "projectile";
    public static final String PROJECTILE_FILE_TAG = "missileFile";
    public static final String PROJECTILE_CAT_PREFIX = "c0_";
    public static final String JOINT_COLLIDE_CONNECTED_ATTRIBUTE = "collideConnected";
    public static final String JOINT_TYPE_ATTRIBUTE = "type";
    private static final PersistenceCaretaker INSTANCE = new PersistenceCaretaker();
    private static final String JOINTS_TAG = "joints";
    public static final String BODY_A_ID_JOINT_ATTRIBUTE = "bodyAId";
    public static final String BODY_B_ID_JOINT_ATTRIBUTE = "bodyBId";
    private static final String BOMB_LIST_TAG = "bombList";
    private static final String BOMB_TAG = "bomb";
    private DocumentBuilder docBuilder;
    private GameActivity gameActivity;
    private GameScene gameScene;

    private PersistenceCaretaker() {
    }

    public static PersistenceCaretaker getInstance() {
        return INSTANCE;
    }

    public void create(GameScene gameScene) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        this.gameScene = gameScene;
        this.gameActivity = gameScene.getActivity();
        this.docBuilder = docFactory.newDocumentBuilder();
    }

    public void saveToolModel(ToolModel toolModel) throws FileNotFoundException, TransformerException {
        Document toolDocument = docBuilder.newDocument();
        FileOutputStream fos = gameActivity.openFileOutput(toolModel.getToolCategory().getPrefix() + "_" + toolModel.getModelName().toLowerCase(Locale.ROOT) + ".xml", Context.MODE_PRIVATE);
        Element toolElement = toolDocument.createElement(TOOL_TAG);
        toolElement.setAttribute(NAME, toolModel.getModelName());
        Element bodiesElement = toolDocument.createElement(BODIES_TAG);
        for (BodyModel bodyModel : toolModel.getBodies()) {
            Element bodyElement = createBodyElement(toolDocument, bodyModel);
            bodiesElement.appendChild(bodyElement);
        }
        toolElement.appendChild(bodiesElement);

        Element jointsElement = toolDocument.createElement(JOINTS_TAG);
        for (JointModel jointModel : toolModel.getJoints()) {
            Element jointElement = createJointElement(toolDocument, jointModel);
            jointsElement.appendChild(jointElement);
        }
        toolElement.appendChild(jointsElement);
        toolDocument.appendChild(toolElement);
        XmlUtils.writeXml(toolDocument, fos);
    }

    public Element createJointElement(Document document, JointModel jointModel) {
        Element jointElement = document.createElement("joint");
        JointDef jointDef = jointModel.getJointDef();
        jointElement.setAttribute(ID, String.valueOf(jointModel.getJointId()));
        jointElement.setIdAttribute(ID, true);
        jointElement.setAttribute(JOINT_COLLIDE_CONNECTED_ATTRIBUTE, String.valueOf(jointDef.collideConnected));
        jointElement.setAttribute(JOINT_TYPE_ATTRIBUTE, String.valueOf(jointDef.type.getValue()));
        int bodyId1 = jointModel.getBodyModel1().getBodyId();
        int bodyId2 = jointModel.getBodyModel2().getBodyId();
        jointElement.setAttribute(BODY_A_ID_JOINT_ATTRIBUTE, String.valueOf(bodyId1));
        jointElement.setAttribute(BODY_B_ID_JOINT_ATTRIBUTE, String.valueOf(bodyId2));
        Element localAnchorAElement;
        Element localAnchorBElement;
        switch (jointDef.type) {
            case Unknown:
            case FrictionJoint:
            case LineJoint:
            case GearJoint:
            case MouseJoint:
            case PulleyJoint:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                localAnchorAElement = XmlUtils.createVectorElement(document, VECTOR_TAG, revoluteJointDef.localAnchorA);
                localAnchorBElement = XmlUtils.createVectorElement(document, VECTOR_TAG, revoluteJointDef.localAnchorB);
                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("enableMotor", String.valueOf(revoluteJointDef.enableMotor));
                jointElement.setAttribute("maxMotorTorque", String.valueOf(revoluteJointDef.maxMotorTorque));
                jointElement.setAttribute("motorSpeed", String.valueOf(revoluteJointDef.motorSpeed));
                jointElement.setAttribute("enableLimit", String.valueOf(revoluteJointDef.enableLimit));
                jointElement.setAttribute("lowerAngle", String.valueOf(revoluteJointDef.lowerAngle));
                jointElement.setAttribute("upperAngle", String.valueOf(revoluteJointDef.upperAngle));
                jointElement.setAttribute("referenceAngle", String.valueOf(revoluteJointDef.referenceAngle));
                break;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                localAnchorAElement = XmlUtils.createVectorElement(document, VECTOR_TAG, prismaticJointDef.localAnchorA);
                localAnchorBElement = XmlUtils.createVectorElement(document, VECTOR_TAG, prismaticJointDef.localAnchorB);
                Element localAxisElement = XmlUtils.createVectorElement(document, VECTOR_TAG, prismaticJointDef.localAxis1);
                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.appendChild(localAxisElement);
                jointElement.setAttribute("enableMotor", String.valueOf(prismaticJointDef.enableMotor));
                jointElement.setAttribute("maxMotorForce", String.valueOf(prismaticJointDef.maxMotorForce));
                jointElement.setAttribute("motorSpeed", String.valueOf(prismaticJointDef.motorSpeed));
                jointElement.setAttribute("enableLimit", String.valueOf(prismaticJointDef.enableLimit));
                jointElement.setAttribute("lowerTranslation", String.valueOf(prismaticJointDef.lowerTranslation));
                jointElement.setAttribute("upperTranslation", String.valueOf(prismaticJointDef.upperTranslation));
                jointElement.setAttribute("referenceAngle", String.valueOf(prismaticJointDef.referenceAngle));
                break;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointDef;
                localAnchorAElement = XmlUtils.createVectorElement(document, VECTOR_TAG, distanceJointDef.localAnchorA);
                localAnchorBElement = XmlUtils.createVectorElement(document, VECTOR_TAG, distanceJointDef.localAnchorB);

                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("dampingRatio", String.valueOf(distanceJointDef.dampingRatio));
                jointElement.setAttribute("length", String.valueOf(distanceJointDef.length));
                jointElement.setAttribute("frequencyHz", String.valueOf(distanceJointDef.frequencyHz));
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = (WeldJointDef) jointDef;
                localAnchorAElement = XmlUtils.createVectorElement(document, VECTOR_TAG, weldJointDef.localAnchorA);
                localAnchorBElement = XmlUtils.createVectorElement(document, VECTOR_TAG, weldJointDef.localAnchorB);

                jointElement.appendChild(localAnchorAElement);
                jointElement.appendChild(localAnchorBElement);
                jointElement.setAttribute("referenceAngle", String.valueOf(weldJointDef.referenceAngle));
                break;
        }
        return jointElement;
    }

    private Element createBodyElement(Document document, BodyModel bodyModel) {
        Element bodyElement = document.createElement(BODY_TAG);
        bodyElement.setAttribute(ID, String.valueOf(bodyModel.getBodyId()));
        bodyElement.setIdAttribute(ID, true);
        Element layersElement = document.createElement(LAYERS_TAG);
        bodyModel.getLayers().forEach(layerModel -> {
            Element layerElement = createLayerElement(document, layerModel);
            layersElement.appendChild(layerElement);
        });
        bodyElement.appendChild(layersElement);
        Element projectilesElement = document.createElement(PROJECTILES_TAG);
        for (ProjectileModel projectileModel : bodyModel.getProjectiles()) {
            projectilesElement.appendChild(createProjectileElement(document, projectileModel));
        }
        bodyElement.appendChild(projectilesElement);

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

        Element usageListElement = document.createElement(USAGE_LIST_TAG);
        for (UsageModel<?> usageModel : bodyModel.getUsageModels()) {
            Element usageElement = document.createElement(USAGE_TAG);
            Properties props = bodyModel.getUsageModelProperties(usageModel.getType());
            Element propertiesElement = createPropertiesElement(document, Objects.requireNonNull(props));
            if (usageModel.getType() == BodyUsageCategory.RANGED_AUTOMATIC || usageModel.getType() == BodyUsageCategory.RANGED_MANUAL || usageModel.getType() == BodyUsageCategory.RANGED_SEMI_AUTOMATIC) {
                RangedProperties rangedProperties = bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(USAGE_PROPERTIES_PROJECTILES_TAG, convertIntListToString(rangedProperties.getProjectileModelList().stream().map(ProjectileModel::getProjectileId).collect(Collectors.toList())));
            }
            if (usageModel.getType() == BodyUsageCategory.TIME_BOMB) {
                TimeBombUsageProperties timeBombUsageProperties = bodyModel.getUsageModelProperties(usageModel.getType());
                propertiesElement.setAttribute(USAGE_PROPERTIES_BOMBS_TAG, convertIntListToString(timeBombUsageProperties.getBombModelList().stream().map(BombModel::getBombId).collect(Collectors.toList())));
            }
            usageElement.appendChild(propertiesElement);
            usageElement.setAttribute(USAGE_TYPE_TAG, usageModel.getType().getName());
            usageListElement.appendChild(usageElement);
        }
        bodyElement.appendChild(usageListElement);
        return bodyElement;
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

    private Element createProjectileElement(Document document, ProjectileModel projectileModel) {
        Element projectileElement = document.createElement(PROJECTILE_TAG);
        projectileElement.setAttribute(ID, String.valueOf(projectileModel.getProjectileId()));
        projectileElement.setIdAttribute(ID, true);
        projectileElement.setAttribute(NAME, String.valueOf(projectileModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, projectileModel.getProperties());
        projectileElement.appendChild(propertiesElement);
        projectileElement.setAttribute(PROJECTILE_FILE_TAG, PROJECTILE_CAT_PREFIX + projectileModel.getMissileModel().getModelName() + ".xml");
        projectileElement.setAttribute(AMMO_ID_TAG, String.valueOf(projectileModel.getAmmoModel().getCasingId()));
        return projectileElement;
    }


    private Element createLayerElement(Document document, LayerModel layerModel) {
        Element layerElement = createElementFromPointsModel(document, layerModel, layerModel.getLayerId(), LAYER_TAG);
        layerModel.getDecorations().forEach(decorationModel -> layerElement.appendChild(createDecorationElement(document, decorationModel)));
        return layerElement;
    }

    private Element createDecorationElement(Document document, DecorationModel decorationModel) {
        return createElementFromPointsModel(document, decorationModel, decorationModel.getDecorationId(), DECORATION_TAG);
    }

    private Element createElementFromPointsModel(Document document, PointsModel<?> pointsModel, int id, String tag) {
        Element element = document.createElement(tag);
        element.setAttribute(ID, String.valueOf(id));
        element.setIdAttribute(ID, true);
        element.setAttribute(NAME, pointsModel.getModelName());
        //save vertices
        List<Vector2> points = pointsModel.getPoints();

        Element verticesElement = document.createElement(VERTICES_TAG);
        for (Vector2 v : points) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
            verticesElement.appendChild(vectorElement);
        }
        element.appendChild(verticesElement);

        Vector2[] outline = pointsModel.getOutlinePoints();
        Element outlineElement = document.createElement(OUTLINE_TAG);
        for (Vector2 v : outline) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
            outlineElement.appendChild(vectorElement);
        }
        element.appendChild(outlineElement);

        Element referenceVerticesElement = document.createElement(REF_VERTICES_TAG);
        for (Vector2 v : pointsModel.getReferencePoints()) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
            referenceVerticesElement.appendChild(vectorElement);
        }
        element.appendChild(referenceVerticesElement);

        Element propertiesElement = createPropertiesElement(document, pointsModel.getProperties());
        element.appendChild(propertiesElement);
        return element;
    }


    private <T extends Properties> T loadProperties(Element propertiesElement, Class<T> theClass) throws PersistenceException {
        try {
            Constructor<T> constructor = theClass.getConstructor();
            T properties = constructor.newInstance();
            Field[] allFields = theClass.getDeclaredFields();

            if (theClass.getSuperclass() != null) {
                allFields = Utils.concatWithStream(allFields, theClass.getSuperclass().getDeclaredFields());
            }
            Arrays.stream(allFields).forEach(field -> {
                String methodName = "set" + StringUtils.capitalize(field.getName());
                try {
                    Method method = theClass.getMethod(methodName, field.getType());
                    Object value = null;
                    if (field.getType().isPrimitive()) {
                        if (field.getType() == float.class) {
                            try {
                                value = Float.parseFloat(propertiesElement.getAttribute(field.getName()));
                            } catch (Throwable t) {
                                Log.e("Field Not Found", "" + field.getName());
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
                    } else if (field.getType() == Color.class) {
                        int packedColor = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                        Color color = ColorUtils.convertARGBPackedIntToColor(packedColor);
                        method.invoke(properties, color);
                    } else if (field.getType() == Vector2.class) {
                        float x = Float.parseFloat(propertiesElement.getAttribute(field.getName() + "X"));
                        float y = Float.parseFloat(propertiesElement.getAttribute(field.getName() + "Y"));
                        method.invoke(properties, new Vector2(x, y));
                    } else if (field.getType() == ProjectileTriggerType.class) {
                        int v = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                        method.invoke(properties, ProjectileTriggerType.getFromValue(v));
                    } else if (field.getType() == Explosive.class) {
                        int v = Integer.parseInt(propertiesElement.getAttribute(field.getName()));
                        method.invoke(properties, Explosive.values()[v]);
                    }
                } catch (Throwable ignored) {
                }
            });
            return properties;

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new PersistenceException("Error reading properties:" + theClass);
        }
    }


    private Element createPropertiesElement(Document document, Properties properties) {
        Element propertiesElement = document.createElement(PROPERTIES_TAG);
        Field[] allFields = properties.getClass().getDeclaredFields();
        if (properties.getClass().getSuperclass() != null) {
            allFields = Utils.concatWithStream(allFields, properties.getClass().getSuperclass().getDeclaredFields());
        }
        Arrays.stream(allFields).forEach(field -> {
            try {
                if (field.getType() == ProjectileModel.class || field.getType().isPrimitive() || field.getType() == Color.class || field.getType() == ProjectileTriggerType.class || field.getType() == Explosive.class || field.getType() == Vector2.class || field.getType() == String.class) {
                    String methodName = (field.getType() == boolean.class ? "is" : "get") + StringUtils.capitalize(field.getName());
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
                            propertiesElement.setAttribute(field.getName(), String.valueOf(packedIntColor));
                        }
                    } else if (field.getType() == ProjectileTriggerType.class) {
                        ProjectileTriggerType projectileTriggerType = (ProjectileTriggerType) method.invoke(properties);
                        if (projectileTriggerType != null) {
                            int value = projectileTriggerType.getValue();
                            propertiesElement.setAttribute(field.getName(), String.valueOf(value));
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
                            propertiesElement.setAttribute(field.getName() + "X", String.valueOf(vector.x));
                            propertiesElement.setAttribute(field.getName() + "Y", String.valueOf(vector.y));
                        }
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {

            }
        });
        return propertiesElement;
    }


    public ToolModel loadToolModel(String toolFileName) throws IOException, ParserConfigurationException, SAXException, PersistenceException {
        if (toolFileName.isEmpty()) {
            return new ToolModel(gameScene, 0);
        }
        FileInputStream fis = gameActivity.openFileInput(toolFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document xml = docBuilder.parse(fis);
        fis.close();
        Element toolElement = xml.getDocumentElement();
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
        List<ProjectileModel> allProjectiles = new ArrayList<>();
        for (BodyModel model : bodyModels) {
            ArrayList<ProjectileModel> projectiles = model.getProjectiles();
            allProjectiles.addAll(projectiles);
        }
        List<BombModel> allBombs = bodyModels.stream().map(BodyModel::getBombModels).flatMap(Collection::stream).collect(Collectors.toList());

        for (BodyModel bodyModel : bodyModels) {
            for (UsageModel<?> e : bodyModel.getUsageModels()) {
                if (e.getType().toString().startsWith("Ranged")) {
                    RangedProperties rangedProperties = (RangedProperties) e.getProperties();
                    rangedProperties.getProjectileModelList().addAll(allProjectiles.stream().filter(p -> rangedProperties.getProjectileIds().contains(p.getProjectileId())).collect(Collectors.toList()));
                } else if(e.getType().toString().contains("Bomb")){
                    BombUsageProperties bombUsageProperties = (BombUsageProperties) e.getProperties();
                    bombUsageProperties.getBombModelList().addAll(allBombs.stream().filter(p -> bombUsageProperties.getBombIds().contains(p.getBombId())).collect(Collectors.toList()));
                }
            }
        }

        ToolModel toolModel = new ToolModel(gameScene, 0);
        toolModel.setModelName(toolFileName.substring(3, toolFileName.length() - 4));
        toolModel.getBodies().addAll(bodyModels);

        List<JointModel> jointModels = new ArrayList<>();
        Element jointsElement = (Element) toolElement.getElementsByTagName(JOINTS_TAG).item(0);
        if (jointsElement != null) {
            for (int i = 0; i < jointsElement.getChildNodes().getLength(); i++) {
                Element jointElement = (Element) jointsElement.getChildNodes().item(i);
                JointModel jointModel = null;
                try {
                    jointModel = readJointModel(Integer.parseInt(jointElement.getAttribute(ID)), jointElement, bodyModels);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading joint:" + ID, e);
                }
                jointModels.add(jointModel);
            }
            toolModel.getJoints().addAll(jointModels);
        }
        this.initializeCounters(toolModel);
        return toolModel;
    }

    LayerModel readLayerModel(Element element, BodyModel bodyModel, int layerId) throws PersistenceException {
        Element verticesElement = (Element) element.getElementsByTagName(VERTICES_TAG).item(0);
        Element outlineElement = (Element) element.getElementsByTagName(OUTLINE_TAG).item(0);
        Element refVerticesElement = (Element) element.getElementsByTagName(REF_VERTICES_TAG).item(0);
        List<Vector2> vertices = readVectors(verticesElement);
        List<Vector2> referencePoints;
        if (refVerticesElement != null) {
            referencePoints = readVectors(refVerticesElement);
        } else {
            referencePoints = new ArrayList<>();
        }

        LayerModel layerModel = new LayerModel(bodyModel.getBodyId(), layerId, element.getAttribute(NAME), new LayerProperties(), bodyModel);
        layerModel.setPoints(vertices);
        layerModel.setReferencePoints(referencePoints);
        Element propertiesElement = (Element) element.getElementsByTagName(PROPERTIES_TAG).item(0);
        LayerProperties layerProperties = loadProperties(propertiesElement, LayerProperties.class);
        layerModel.setProperties(layerProperties);
        return layerModel;
    }

    private void initializeCounters(ToolModel toolModel) {
        if (toolModel.getBodies().size() > 0) {
            toolModel.getBodyCounter().set(toolModel.getBodies().stream().mapToInt(BodyModel::getBodyId).max().getAsInt() + 1);
        }
        if (toolModel.getJoints().size() > 0) {
            toolModel.getJointCounter().set(toolModel.getJoints().stream().mapToInt(JointModel::getJointId).max().getAsInt() + 1);
        }

        for (BodyModel body : toolModel.getBodies()) {
            if (body.getCasingModels().size() > 0) {
                toolModel.getAmmoCounter().set(body.getCasingModels().stream().mapToInt(CasingModel::getCasingId).max().getAsInt() + 1);
            }
            if (body.getBombModels().size() > 0) {
                toolModel.getBombCounter().set(body.getBombModels().stream().mapToInt(BombModel::getBombId).max().getAsInt() + 1);
            }
            if (body.getProjectiles().size() > 0) {
                toolModel.getProjectileCounter().set(body.getProjectiles().stream().mapToInt(ProjectileModel::getProjectileId).max().getAsInt() + 1);
            }
            if (body.getLayers().size() > 0) {
                body.getLayerCounter().set(body.getLayers().stream().mapToInt(LayerModel::getLayerId).max().getAsInt() + 1);
            }
            for (LayerModel layerModel : body.getLayers()) {
                if (layerModel.getDecorations().size() > 0) {
                    layerModel.getDecorationCounter().set(layerModel.getDecorations().stream().mapToInt(DecorationModel::getDecorationId).max().getAsInt() + 1);
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
        Element layersElement = (Element) element.getElementsByTagName(LAYERS_TAG).item(0);
        List<LayerModel> layers = new ArrayList<>();
        NodeList childNodes = layersElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element layerElement = (Element) childNodes.item(i);
            LayerModel layerModel;
            try {
                layerModel = readLayerModel(layerElement, bodyModel, Integer.parseInt(layerElement.getAttribute(ID)));
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
                CasingModel ammoModel = null;
                try {
                    ammoModel = readAmmoModel(ammoElement, bodyId, Integer.parseInt(ammoElement.getAttribute(ID)));
                    ammoModels.add(ammoModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading ammo model", e);
                }

            }
        }

        Element projectilesElement = (Element) element.getElementsByTagName(PROJECTILES_TAG).item(0);
        if (projectilesElement != null) {
            List<ProjectileModel> projectiles = bodyModel.getProjectiles();
            for (int i = 0; i < projectilesElement.getChildNodes().getLength(); i++) {
                Element projectileElement = (Element) projectilesElement.getChildNodes().item(i);
                ProjectileModel projectileModel;
                try {
                    projectileModel = readProjectileModel(projectileElement, bodyId, Integer.parseInt(projectileElement.getAttribute(ID)));
                    projectiles.add(projectileModel);
                    if (!projectileElement.getAttribute(AMMO_ID_TAG).isEmpty()) {
                        int ammoId = Integer.parseInt(projectileElement.getAttribute(AMMO_ID_TAG));
                        projectileModel.setAmmoModel(ammoModels.stream().filter(am -> am.getCasingId() == ammoId).findFirst().orElseGet(null));
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
                    bombModel = readBombModel(bombElement, bodyId, Integer.parseInt(bombElement.getAttribute(ID)));
                    bombModels.add(bombModel);
                } catch (PersistenceException e) {
                    throw new PersistenceException("Error reading bomb model", e);
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

    private UsageModel readUsageModel(Element usageElement) throws PersistenceException {
        Element propertiesElement = (Element) usageElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        BodyUsageCategory bodyUsageCategory = BodyModel.allCategories.stream().filter(bm -> bm.getName().equals(usageElement.getAttribute(USAGE_TYPE_TAG))).findFirst().get();
        UsageModel<Properties> usageModel = new UsageModel<>("", bodyUsageCategory);
        List<Integer> usageProjectileIds;
        List<Integer> usageBombIds;
        switch (bodyUsageCategory) {
            case RANGED_MANUAL:
                usageProjectileIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                ManualProperties rangedManualProperties = loadProperties(propertiesElement, ManualProperties.class);
                rangedManualProperties.setProjectileIds(usageProjectileIds);
                usageModel.setProperties(rangedManualProperties);
                break;
            case RANGED_SEMI_AUTOMATIC:
                usageProjectileIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                SemiAutomaticProperties rangedSemiAutomaticProperties = loadProperties(propertiesElement, SemiAutomaticProperties.class);
                rangedSemiAutomaticProperties.setProjectileIds(usageProjectileIds);
                usageModel.setProperties(rangedSemiAutomaticProperties);
                break;
            case RANGED_AUTOMATIC:
                usageProjectileIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_PROJECTILES_TAG));
                AutomaticProperties rangedAutomaticProperties = loadProperties(propertiesElement, AutomaticProperties.class);
                rangedAutomaticProperties.setProjectileIds(usageProjectileIds);
                usageModel.setProperties(rangedAutomaticProperties);
                break;
            case TIME_BOMB:
                TimeBombUsageProperties timeBombUsageProperties = loadProperties(propertiesElement, TimeBombUsageProperties.class);
                usageModel.setProperties(timeBombUsageProperties);
                usageBombIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_BOMBS_TAG));
                timeBombUsageProperties.setBombIds(usageBombIds);
                break;
            case FUZE_BOMB:
                FuzeBombUsageProperties fuzeBombUsageProperties = loadProperties(propertiesElement, FuzeBombUsageProperties.class);
                usageModel.setProperties(fuzeBombUsageProperties);
                usageBombIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_BOMBS_TAG));
                fuzeBombUsageProperties.setBombIds(usageBombIds);
                break;
            case IMPACT_BOMB:
                ImpactBombUsageProperties impactBombUsageProperties = loadProperties(propertiesElement, ImpactBombUsageProperties.class);
                usageModel.setProperties(impactBombUsageProperties);
                usageBombIds = convertStringToIntList(propertiesElement.getAttribute(USAGE_PROPERTIES_BOMBS_TAG));
                impactBombUsageProperties.setBombIds(usageBombIds);
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
            default:
                throw new IllegalStateException("Unexpected value: " + bodyUsageCategory);
        }
        return usageModel;
    }

    public static String convertIntListToString(List<Integer> list) {
        return list.stream().map(Objects::toString).collect(Collectors.joining(" "));
    }

    public static List<Integer> convertStringToIntList(String input) {
        if(input.length()==0){
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


    private CasingModel readAmmoModel(Element ammoElement, int bodyId, int ammoId) throws PersistenceException {
        Element propertiesElement = (Element) ammoElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        CasingModel ammoModel = new CasingModel(bodyId, ammoId, ammoElement.getAttribute(NAME));
        CasingProperties casingProperties = loadProperties(propertiesElement, CasingProperties.class);
        ammoModel.setProperties(casingProperties);
        return ammoModel;
    }

    private BombModel readBombModel(Element bombElement, int bodyId, int bombId) throws PersistenceException {
        Element propertiesElement = (Element) bombElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        BombModel bombModel = new BombModel(bodyId, bombId);
        BombProperties bombProperties = loadProperties(propertiesElement, BombProperties.class);
        bombModel.setProperties(bombProperties);
        return bombModel;
    }

    private ProjectileModel readProjectileModel(Element projectileElement, int bodyId, int projectileId) throws PersistenceException {
        Element propertiesElement = (Element) projectileElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        ProjectileModel projectileModel = new ProjectileModel(bodyId, projectileId, projectileElement.getAttribute(NAME));
        ProjectileProperties projectileProperties = loadProperties(propertiesElement, ProjectileProperties.class);
        projectileModel.setProperties(projectileProperties);
        String fileName = projectileElement.getAttribute(PROJECTILE_FILE_TAG);
        ToolModel missile;
        try {
            missile = ToolUtils.getProjectileModel(fileName);
            projectileModel.setMissileModel(missile);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            throw new PersistenceException("Error loading projectile model's missile");
        }
        return projectileModel;
    }

    private JointModel readJointModel(int jointId, Element jointElement, List<BodyModel> bodyModels) throws PersistenceException {
        JointDef.JointType jointType = JointDef.JointType.valueTypes[Integer.parseInt(jointElement.getAttribute(JOINT_TYPE_ATTRIBUTE))];
        JointDef jointDef = null;
        switch (jointType) {
            case WeldJoint:
                jointDef = new WeldJointDef();
                break;
            case RevoluteJoint:
                jointDef = new RevoluteJointDef();
                break;
            case PrismaticJoint:
                jointDef = new PrismaticJointDef();
                break;
            case DistanceJoint:
                jointDef = new DistanceJointDef();
                break;
            case PulleyJoint:
            case MouseJoint:
            case GearJoint:
            case LineJoint:
            case FrictionJoint:
                break;
        }
        JointModel jointModel = new JointModel(jointId, jointDef);
        assert jointDef != null;
        jointDef.collideConnected = Boolean.parseBoolean(jointElement.getAttribute(JOINT_COLLIDE_CONNECTED_ATTRIBUTE));
        Vector2 localAnchorA = XmlUtils.readVector((Element) jointElement.getElementsByTagName(VECTOR_TAG).item(0));
        Vector2 localAnchorB = XmlUtils.readVector((Element) jointElement.getElementsByTagName(VECTOR_TAG).item(1));
        int bodyAId = Integer.parseInt(jointElement.getAttribute(BODY_A_ID_JOINT_ATTRIBUTE));
        int bodyBId = Integer.parseInt(jointElement.getAttribute(BODY_B_ID_JOINT_ATTRIBUTE));
        BodyModel bodyModelA = bodyModels.stream().filter(bodyModel -> bodyModel.getBodyId() == bodyAId).findFirst().get();
        BodyModel bodyModelB = bodyModels.stream().filter(bodyModel -> bodyModel.getBodyId() == bodyBId).findFirst().get();
        jointModel.setBodyModel1(bodyModelA);
        jointModel.setBodyModel2(bodyModelB);
        switch (jointType) {
            case WeldJoint:
                WeldJointDef weldJointDef = (WeldJointDef) jointDef;
                weldJointDef.localAnchorA.set(localAnchorA);
                weldJointDef.localAnchorB.set(localAnchorB);
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                revoluteJointDef.localAnchorA.set(localAnchorA);
                revoluteJointDef.localAnchorB.set(localAnchorB);
                revoluteJointDef.enableMotor = Boolean.parseBoolean(jointElement.getAttribute("enableMotor"));
                revoluteJointDef.maxMotorTorque = Float.parseFloat(jointElement.getAttribute("maxMotorTorque"));
                revoluteJointDef.motorSpeed = Float.parseFloat(jointElement.getAttribute("motorSpeed"));
                revoluteJointDef.enableLimit = Boolean.parseBoolean(jointElement.getAttribute("enableLimit"));
                revoluteJointDef.lowerAngle = Float.parseFloat(jointElement.getAttribute("lowerAngle"));
                revoluteJointDef.upperAngle = Float.parseFloat(jointElement.getAttribute("upperAngle"));
                revoluteJointDef.referenceAngle = Float.parseFloat(jointElement.getAttribute("referenceAngle"));
                break;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                prismaticJointDef.localAnchorA.set(localAnchorA);
                prismaticJointDef.localAnchorB.set(localAnchorB);
                prismaticJointDef.enableLimit = Boolean.parseBoolean(jointElement.getAttribute("enableLimit"));
                prismaticJointDef.enableMotor = Boolean.parseBoolean(jointElement.getAttribute("enableMotor"));
                prismaticJointDef.maxMotorForce = Float.parseFloat(jointElement.getAttribute("maxMotorForce"));
                prismaticJointDef.motorSpeed = Float.parseFloat(jointElement.getAttribute("motorSpeed"));
                prismaticJointDef.lowerTranslation = Float.parseFloat(jointElement.getAttribute("lowerTranslation"));
                prismaticJointDef.upperTranslation = Float.parseFloat(jointElement.getAttribute("upperTranslation"));
                prismaticJointDef.referenceAngle = Float.parseFloat(jointElement.getAttribute("referenceAngle"));
                break;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointDef;
                distanceJointDef.localAnchorA.set(localAnchorA);
                distanceJointDef.localAnchorB.set(localAnchorB);
                distanceJointDef.dampingRatio = Float.parseFloat(jointElement.getAttribute("dampingRatio"));
                distanceJointDef.length = Float.parseFloat(jointElement.getAttribute("length"));
                distanceJointDef.frequencyHz = Float.parseFloat(jointElement.getAttribute("frequencyHz"));
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