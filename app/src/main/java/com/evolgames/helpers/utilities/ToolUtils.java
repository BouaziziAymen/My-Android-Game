package com.evolgames.helpers.utilities;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.properties.BlockAProperties;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationPointsModel;
import com.evolgames.userinterface.model.LayerPointsModel;
import com.evolgames.userinterface.model.ToolModel;

import org.andengine.util.adt.color.Color;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolUtils {
    public static Optional<List<ToolModel>> loadFile(GameScene scene, String fileName) {
        List<ToolModel> toolModelList = null;
        try {
            FileInputStream fis = scene.getActivity().openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);
            toolModelList = new ArrayList<>();
            ToolModel loaded;
            do {
                loaded = loadModel(scene, reader);
                toolModelList.add(loaded);
            } while (loaded != null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(toolModelList);
    }

    public static ToolModel loadModel(GameScene scene, BufferedReader reader) {
        ToolModel toolModel = null;
        try {
            String line = reader.readLine();
            if (line.contains("@Tool")) {
                toolModel = new ToolModel(scene, 0);
                String[] mainTokens = line.split(":");
                int bodyCounter = Integer.parseInt(mainTokens[1]);
                String modelName = mainTokens[2];
                toolModel.setModelName(modelName);
                toolModel.initBodyCounter(bodyCounter);
                while (!line.contains("$Tool")) {
                    if (line.contains("@Body")) {
                        String[] bodyTokens = line.split(":");
                        int bodyId = Integer.parseInt(bodyTokens[1]);
                        int layerCounter = Integer.parseInt(bodyTokens[2]);

                        BodyModel bodyModel = readBodyModel(bodyId, reader);
                        //  if(line.contains("#"))bodyModel.setSelected(true);
                        bodyModel.initLayerCounter(layerCounter);
                        toolModel.getBodies().add(bodyModel);

                    }
                    line = reader.readLine();
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tool model.");
        }

        return toolModel;
    }

    public static void saveToolModel(String filename, ToolModel toolModel, GameActivity gameActivity) throws IOException {
        FileOutputStream fOut = gameActivity.openFileOutput(filename, Context.MODE_PRIVATE);
        fOut.write(("@Tool:" + toolModel.getBodyCount() + ":" + toolModel.getModelName() + "\n").getBytes());
        for (BodyModel bodyModel : toolModel.getBodies()) saveBodyModel(bodyModel, fOut);
        fOut.write("$Tool\n".getBytes());
        fOut.close();
    }

    private static void saveBodyModel(BodyModel bodyModel, FileOutputStream outputStream) throws IOException {
        outputStream.write(("@Body:" + bodyModel.getBodyId() + ":" + bodyModel.getLayersCounterValue() + "\n").getBytes());
        for (LayerPointsModel layerModel : bodyModel.getLayers())
            saveLayerModel(layerModel, outputStream);
        outputStream.write("$Body\n".getBytes());
    }

    private static BlockAProperties readBlockAProperties(BufferedReader reader) throws IOException {
        BlockAProperties properties = new BlockAProperties();
        Filter filter = new Filter();
        properties.setFilter(filter);
        String line = reader.readLine();
        String[] tokens = line.split(";");

        properties.setMaterialNumber(Integer.parseInt(tokens[0]));
        properties.setOrder(Integer.parseInt(tokens[1]));
        properties.setDefaultColor(new Color(Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]), Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5])));
        properties.setProperties(new float[7]);
        for (int i = 0; i < 7; i++) properties.getProperties()[i] = Float.parseFloat(tokens[6 + i]);
        return properties;
    }

    private static void saveBlockAProperties(BlockAProperties properties, FileOutputStream outputStream) throws IOException {
        String line = properties.getMaterialNumber() + ";"
                + properties.getOrder() + ";"
                + properties.getDefaultColor().getRed() + ";"
                + properties.getDefaultColor().getGreen() + ";"
                + properties.getDefaultColor().getBlue() + ";"
                + properties.getDefaultColor().getAlpha() + ";"
                + properties.getProperties()[0] + ";"
                + properties.getProperties()[1] + ";"
                + properties.getProperties()[2] + ";"
                + properties.getProperties()[3] + ";"
                + properties.getProperties()[4] + ";"
                + properties.getProperties()[5] + ";"
                + properties.getProperties()[6] + ";\n";
        outputStream.write(line.getBytes());


    }

    private static void saveDecorationModel(DecorationPointsModel decorationModel, FileOutputStream outputStream) throws IOException {
        ArrayList<Vector2> points = decorationModel.getPoints();
        Vector2[] outline = decorationModel.getOutlinePoints();

        int decorationId = decorationModel.getDecorationId();
        outputStream.write(("@Decoration:" + decorationId + "\n").getBytes());
        StringBuilder temp = new StringBuilder();
        for (Vector2 v : points) {
            temp.append(v.x).append(";").append(v.y).append(";");
        }
        temp.append("\n");
        outputStream.write(temp.toString().getBytes());
        temp = new StringBuilder();
        for (Vector2 v : outline) {
            temp.append(v.x).append(";").append(v.y).append(";");
        }
        temp.append("\n");
        outputStream.write(temp.toString().getBytes());

        outputStream.write("$Decoration\n".getBytes());
    }

    private static void saveLayerModel(LayerPointsModel layerModel, FileOutputStream outputStream) throws IOException {
        ArrayList<Vector2> points = layerModel.getPoints();
        Vector2[] outline = layerModel.getOutlinePoints();
        BlockAProperties properties = (BlockAProperties) layerModel.getProperty();
        int layerId = layerModel.getLayerId();
        outputStream.write(("@Layer:" + layerId + ":" + layerModel.getNewDecorationId() + ":" + layerModel.getModelName() + "\n").getBytes());
        StringBuilder temp = new StringBuilder();
        temp.append("@").append(";");
        for (Vector2 v : points) {
            temp.append(v.x).append(";").append(v.y).append(";");
        }
        temp.append("\n");
        outputStream.write(temp.toString().getBytes());
        temp.delete(2, temp.length());
        if (outline != null)
            for (Vector2 v : outline) {
                temp.append(v.x).append(";").append(v.y).append(";");
            }
        temp.append("\n");
        outputStream.write(temp.toString().getBytes());


        saveBlockAProperties(properties, outputStream);

        for (DecorationPointsModel decorationModel : layerModel.getDecorations())
            saveDecorationModel(decorationModel, outputStream);
        outputStream.write("$Layer\n".getBytes());

    }

    private static LayerPointsModel readLayerModel(int layerId, int bodyId, BodyModel bodyModel, String layerName, BufferedReader reader) throws IOException {
        //this points shape needs to be attached to user interface
        //read points
        String line = reader.readLine();
        String[] tokens = line.split(";");
        ArrayList<Vector2> points = new ArrayList<>();
        for (int i = 1; i < tokens.length; i += 2) {
            float x = Float.parseFloat(tokens[i]);
            float y = Float.parseFloat(tokens[i + 1]);
            points.add(new Vector2(x, y));
        }
        line = reader.readLine();
        tokens = line.split(";");
        Vector2[] outlinePoints = new Vector2[tokens.length / 2];
        for (int i = 1; i < tokens.length; i += 2) {
            float x = Float.parseFloat(tokens[i]);
            float y = Float.parseFloat(tokens[i + 1]);
            outlinePoints[i / 2] = new Vector2(x, y);
        }

        //read properties

        BlockAProperties properties = readBlockAProperties(reader);

        LayerPointsModel layerModel = new LayerPointsModel(bodyId, layerId, layerName, properties, bodyModel);

        layerModel.setPoints(points);
        layerModel.setOutlinePoints(outlinePoints);

        //read decorations
        while (!(line = reader.readLine()).contains("$Layer")) {
            if (line.contains("@Decoration")) {
                tokens = line.split(":");
                int decorationId = Integer.parseInt(tokens[1]);
                DecorationPointsModel decorationModel = readDecorationModel(layerModel, decorationId, reader);
                layerModel.getDecorations().add(decorationModel);
            }
        }

        return layerModel;
    }

    private static DecorationPointsModel readDecorationModel(LayerPointsModel layerModel, int decorationId, BufferedReader reader) throws IOException {

        DecorationPointsModel decorationModel = new DecorationPointsModel(layerModel, decorationId);

        String line = reader.readLine();
        String[] tokens = line.split(";");
        ArrayList<Vector2> points = new ArrayList<>();
        for (int i = 0; i < tokens.length; i += 2) {
            float x = Float.parseFloat(tokens[i]);
            float y = Float.parseFloat(tokens[i + 1]);
            points.add(new Vector2(x, y));
        }
        line = reader.readLine();
        tokens = line.split(";");
        Vector2[] outlinePoints = new Vector2[tokens.length / 2];
        for (int i = 0; i < tokens.length; i += 2) {
            float x = Float.parseFloat(tokens[i]);
            float y = Float.parseFloat(tokens[i + 1]);
            outlinePoints[i / 2] = new Vector2(x, y);
        }
        decorationModel.setPoints(points);
        decorationModel.setOutlinePoints(outlinePoints);


        return decorationModel;
    }


    private static BodyModel readBodyModel(int bodyId, BufferedReader reader) throws IOException {
        BodyModel bodyModel = new BodyModel(bodyId);
        String line = reader.readLine();
        while (line != null && !line.contains("$Body")) {
            if (line.contains("@Layer")) {
                String[] mainTokens = line.split(":");
                int layerId = Integer.parseInt(mainTokens[1]);
                int decorationCounter = Integer.parseInt((mainTokens[2]));
                String layerName = mainTokens[3];

                LayerPointsModel layerModel = readLayerModel(layerId, bodyId, bodyModel, layerName, reader);
                Log.e("load", "layer:" + layerModel.getModelName());
                layerModel.setDecorationCounter(decorationCounter);
                bodyModel.getLayers().add(layerModel);
            }
            line = reader.readLine();
        }
        return bodyModel;
    }


}
