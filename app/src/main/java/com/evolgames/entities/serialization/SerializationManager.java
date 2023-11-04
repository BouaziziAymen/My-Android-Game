package com.evolgames.entities.serialization;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.Polarity;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.StainProperties;
import com.evolgames.scenes.GameScene;

import org.andengine.util.adt.color.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import is.kul.learningandengine.graphicelements.Layer;

public class SerializationManager {
    private static final SerializationManager INSTANCE = new SerializationManager();
    private final Kryo kryo = new Kryo();

    private SerializationManager() {
        kryo.register(LayerBlock.class);
        kryo.register(ArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(CoatingBlock.class);
        kryo.register(StainBlock.class);
        kryo.register(JointBlock.class);
        kryo.register(Polarity.class);
        kryo.register(LayerProperties.class);
        kryo.register(CoatingProperties.class);
        kryo.register(StainProperties.class);
        kryo.register(JointProperties.class);
        kryo.register(Color.class);
        kryo.register(Vector2.class);
        kryo.register(float[].class);
        kryo.register(GameEntitySerializer.class);
    }

    public static SerializationManager getInstance() {
        return INSTANCE;
    }

    public void serialize(GameScene gameScene) throws FileNotFoundException {
       GameEntity gameEntity = gameScene.getGameGroups().get(0).getGameEntityByIndex(0);
        FileOutputStream fileOutputStream = gameScene.getActivity().openFileOutput("autoSave.mut", Context.MODE_PRIVATE);
        Output output = new Output(fileOutputStream);
        GameEntitySerializer gameEntitySerializer = new GameEntitySerializer(gameEntity);
        kryo.writeObject(output,gameEntitySerializer);
        output.close();

        FileInputStream fileInputStream = gameScene.getActivity().openFileInput("autoSave.mut");
        Input input = new Input(fileInputStream);
        GameEntitySerializer readGameEntitySerializer = kryo.readObject(input, GameEntitySerializer.class);
        readGameEntitySerializer.afterLoad();
        input.close();
    }
}
