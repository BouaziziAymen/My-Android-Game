package com.evolgames.entities.persistence;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.helpers.utilities.XmlUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;

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
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class PersistenceCaretaker {
    public static final String LAYER_TAG = "layer";
    public static final String DECORATION_TAG = "decoration";
    public static final String PROPERTIES_TAG = "properties";
    public static final String VERTICES_TAG = "vertices";
    public static final String OUTLINE_TAG = "outline";
    public static final String BODY_TAG = "body";
    public static final String PROJECTILES_TAG = "projectiles";
    public static final String TOOL_TAG = "tool";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String VECTOR_TAG = "v";
    public static final String BODIES_TAG = "bodies";
    public static final String LAYERS_TAG = "layers";
    public static final String PROJECTILE_TAG = "projectile";
    public static final String PROJECTILE_FILE_TAG = "missileFile";
    private static final PersistenceCaretaker INSTANCE = new PersistenceCaretaker();
    public static final String PROJECTILE_CAT_PREFIX = "c0_";
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
        toolDocument.appendChild(toolElement);
        XmlUtils.writeXml(toolDocument, fos);
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
        return bodyElement;
    }

    private Element createProjectileElement(Document document, ProjectileModel projectileModel) {
        Element projectileElement = document.createElement(PROJECTILE_TAG);
        projectileElement.setAttribute(ID, String.valueOf(projectileModel.getProjectileId()));
        projectileElement.setIdAttribute(ID, true);
        projectileElement.setAttribute(NAME, String.valueOf(projectileModel.getModelName()));
        Element propertiesElement = createPropertiesElement(document, projectileModel.getProperties());
        projectileElement.appendChild(propertiesElement);
        projectileElement.setAttribute(PROJECTILE_FILE_TAG, PROJECTILE_CAT_PREFIX +projectileModel.getModelName()+".xml");
        return projectileElement;
    }

    private Element createLayerElement(Document document, LayerModel layerModel) {
        Element layerElement = createElementFromPointsModel(document, layerModel,layerModel.getLayerId(), LAYER_TAG);
        layerModel.getDecorations().forEach(decorationModel -> layerElement.appendChild(createDecorationElement(document, decorationModel)));
        return layerElement;
    }

    private Element createDecorationElement(Document document, DecorationModel decorationModel) {
        return createElementFromPointsModel(document, decorationModel,decorationModel.getDecorationId(), DECORATION_TAG);
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
        Vector2[] outline = pointsModel.getOutlinePoints();
        Element outlineElement = document.createElement(OUTLINE_TAG);
        for (Vector2 v : outline) {
            Element vectorElement = XmlUtils.createVectorElement(document, VECTOR_TAG, v);
            outlineElement.appendChild(vectorElement);
        }
        element.appendChild(verticesElement);
        element.appendChild(outlineElement);

        Element propertiesElement = createPropertiesElement(document, pointsModel.getProperties());
        element.appendChild(propertiesElement);
        return element;
    }


    private <T extends Properties> T loadProperties(Element propertiesElement, Class<T> theClass) {
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
                            value = Float.parseFloat(propertiesElement.getAttribute(field.getName()));
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
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
            return properties;

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            return null;
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
                if ((field.getType().isPrimitive() || field.getType() == Color.class || field.getType() == Vector2.class || field.getType() == String.class)) {
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
                    } else {
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


    public ToolModel loadToolModel(String toolFileName) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fis = gameActivity.openFileInput(toolFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document xml = docBuilder.parse(fis);
        fis.close();
        Element toolElement = xml.getDocumentElement();
        Element bodiesElement = (Element) toolElement.getElementsByTagName(BODIES_TAG).item(0);
        List<BodyModel> bodies = new ArrayList<>();
        for (int i = 0; i < bodiesElement.getChildNodes().getLength(); i++) {
            Element bodyElement = (Element) bodiesElement.getChildNodes().item(i);
            BodyModel bodyModel = readBodyModel(bodyElement, Integer.parseInt(bodyElement.getAttribute(ID)));
            bodies.add(bodyModel);
        }
        ToolModel toolModel = new ToolModel(gameScene, 0);
        toolModel.setModelName(toolFileName.substring(3,toolFileName.length()-4));
        toolModel.getBodies().addAll(bodies);
        this.initializeCounters(toolModel);
        return toolModel;
    }

    LayerModel readLayerModel(Element element, BodyModel bodyModel, int layerId) {
        Element verticesElement = (Element) element.getElementsByTagName(VERTICES_TAG).item(0);
        Element outlineElement = (Element) element.getElementsByTagName(OUTLINE_TAG).item(0);

        List<Vector2> vertices = readVectors(verticesElement);
        List<Vector2> outlinePoints = readVectors(outlineElement);

        LayerModel layerModel = new LayerModel(bodyModel.getBodyId(), layerId, element.getAttribute(NAME), new LayerProperties(), bodyModel);
        layerModel.setPoints(vertices);
        layerModel.setOutlinePoints(outlinePoints.toArray(new Vector2[0]));
        Element propertiesElement = (Element) element.getElementsByTagName(PROPERTIES_TAG).item(0);
        LayerProperties layerProperties = loadProperties(propertiesElement, LayerProperties.class);
        layerModel.setProperties(layerProperties);
        return layerModel;
    }

    private void initializeCounters(ToolModel toolModel) {
        if (toolModel.getBodies().size() > 0) {
            toolModel.getBodyCounter().set(toolModel.getBodies().stream().mapToInt(BodyModel::getBodyId).max().getAsInt() + 1);
        }
        if(toolModel.getJointModels().size()>0){
            toolModel.getJointCounter().set(toolModel.getJointModels().stream().mapToInt(JointModel::getJointId).max().getAsInt()+1);
        }

        for (BodyModel body : toolModel.getBodies()) {
            if(body.getProjectiles().size()>0){
                body.getProjectileCounter().set(body.getProjectiles().stream().mapToInt(ProjectileModel::getProjectileId).max().getAsInt()+1);
            }
            if (body.getLayers().size() > 0) {
                body.getLayerCounter().set(body.getLayers().stream().mapToInt(LayerModel::getLayerId).max().getAsInt()+1);
            }
            for (LayerModel layerModel : body.getLayers()) {
                if (layerModel.getDecorations().size() > 0) {
                    layerModel.getDecorationCounter().set(layerModel.getDecorations().stream().mapToInt(DecorationModel::getDecorationId).max().getAsInt()+1);
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

    BodyModel readBodyModel(Element element, int bodyId) {
        BodyModel bodyModel = new BodyModel(bodyId);
        Element layersElement = (Element) element.getElementsByTagName(LAYERS_TAG).item(0);
        List<LayerModel> layers = new ArrayList<>();
        NodeList childNodes = layersElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element layerElement = (Element) childNodes.item(i);
            LayerModel layerModel = readLayerModel(layerElement, bodyModel, Integer.parseInt(layerElement.getAttribute(ID)));
            layers.add(layerModel);
        }
        bodyModel.getLayers().addAll(layers);
        Element projectilesElement = (Element) element.getElementsByTagName(PROJECTILES_TAG).item(0);
        if (projectilesElement != null) {
            List<ProjectileModel> projectiles = bodyModel.getProjectiles();
            for (int i = 0; i < projectilesElement.getChildNodes().getLength(); i++) {
                Element projectileElement = (Element) projectilesElement.getChildNodes().item(i);
                ProjectileModel projectileModel = readProjectileModel(projectileElement, bodyId, Integer.parseInt(projectileElement.getAttribute(ID)));
                projectiles.add(projectileModel);
            }
        }
        return bodyModel;
    }

    private ProjectileModel readProjectileModel(Element projectileElement, int bodyId, int projectileId) {
        Element propertiesElement = (Element) projectileElement.getElementsByTagName(PROPERTIES_TAG).item(0);
        ProjectileModel projectileModel = new ProjectileModel(bodyId, projectileId, projectileElement.getAttribute(NAME));
        ProjectileProperties projectileProperties = loadProperties(propertiesElement, ProjectileProperties.class);
        projectileModel.setProperties(projectileProperties);
        String fileName = projectileElement.getAttribute(PROJECTILE_FILE_TAG);
        ToolModel missile = ToolUtils.getProjectileModel(fileName);
        projectileModel.setMissileModel(missile);
        return projectileModel;
    }
}