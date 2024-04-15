package com.evolgames.utilities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.GameActivity;
import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.entities.persistence.VersioningHelper;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class ToolUtils {

    public static List<String> getToolNamesByCategory(GameActivity activity, String categoryPrefix) {
        return Arrays.stream(activity.fileList())
                .filter(f -> f.startsWith(categoryPrefix))
                .collect(Collectors.toList());
    }

    public static ToolModel getProjectileModel(String fileName, boolean assets)
            throws PersistenceException, IOException, ParserConfigurationException, SAXException {
        return PersistenceCaretaker.getInstance().loadToolModel(fileName, false, assets);
    }

    public static float getAxisExtent(ToolModel toolModel, Vector2 axis) {
        float max = -Float.MAX_VALUE;
        float min = Float.MAX_VALUE;
        for (BodyModel bodyModel : toolModel.getBodies()) {
            for (LayerModel layerModel : bodyModel.getLayers()) {
                for (Vector2 v : layerModel.getPoints()) {
                    float dot = v.dot(axis);
                    max = Math.max(dot, max);
                    min = Math.min(dot, min);
                }
            }
        }
        return max - min;
    }


    private static void scaleBodyModel(BodyModel bodyModel, float scale) {
        List<List<Vector2>> list = new ArrayList<>();
        for (LayerModel layerModel : bodyModel.getLayers()) {
            list.add(layerModel.getPoints());
        }
        Vector2 center = GeometryUtils.calculateCenter(list);
        for (LayerModel layerModel : bodyModel.getLayers()) {
            scalePoints(center, layerModel.getPoints(), scale);
            if(layerModel.getCenter()!=null) {
                scalePoint(center, layerModel.getCenter(), scale);
            }
            for (DecorationModel decorationModel : layerModel.getDecorations()) {
                scalePoints(center, decorationModel.getPoints(), scale);
                if(decorationModel.getCenter()!=null) {
                    scalePoint(center, decorationModel.getCenter(), scale);
                }
            }
        }
    }

    public static void scalePoints(Vector2 center, List<Vector2> vertices, float scale) {
        for (int i = 0; i < vertices.size(); i++) {
            float originalX = vertices.get(i).x;
            float originalY = vertices.get(i).y;

            float scaledX = center.x + (originalX - center.x) * scale;
            float scaledY = center.y + (originalY - center.y) * scale;
            // Update vertex positions
            vertices.get(i).set(scaledX, scaledY);
        }
    }

    public static void scalePoint(Vector2 center,Vector2 p, float scale) {
            float scaledX = center.x + (p.x - center.x) * scale;
            float scaledY = center.y + (p.y - center.y) * scale;
            // Update vertex positions
            center.set(scaledX, scaledY);
    }
    public static void scaleTool(ToolModel toolModel, float scale) {
                for (BodyModel bodyModel : toolModel.getBodies()) {
                    scaleBodyModel(bodyModel, scale);
                }
    }

}
