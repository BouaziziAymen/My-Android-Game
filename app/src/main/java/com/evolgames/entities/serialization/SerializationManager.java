package com.evolgames.entities.serialization;

import android.content.Context;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.DecorationBlock;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blocks.Polarity;
import com.evolgames.entities.blocks.StainBlock;
import com.evolgames.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.entities.cut.SegmentFreshCut;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.properties.CoatingProperties;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.properties.StainProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.PhysicsScene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.andengine.util.adt.color.Color;

public class SerializationManager {
  private static final SerializationManager INSTANCE = new SerializationManager();
  private final Kryo kryo = new Kryo();

  private SerializationManager() {
    kryo.register(LayerBlock.class);
    kryo.register(CoatingBlock.class);
    kryo.register(StainBlock.class);
    kryo.register(JointBlock.class);
    kryo.register(DecorationBlock.class);
    kryo.register(Polarity.class);
    kryo.register(LayerProperties.class);
    kryo.register(CoatingProperties.class);
    kryo.register(StainProperties.class);
    kryo.register(JointProperties.class);
    kryo.register(DecorationProperties.class);
    kryo.register(Color.class);
    kryo.register(Vector2.class);
    kryo.register(ArrayList.class);
    kryo.register(HashSet.class);
    kryo.register(float[].class);
    kryo.register(BodyInitImpl.class);
    kryo.register(Filter.class);
    kryo.register(JointDef.JointType.class);
    kryo.register(JointBlock.Position.class);
    kryo.register(RevoluteJointDef.class);
    kryo.register(WeldJointDef.class);
    kryo.register(PrismaticJointDef.class);
    kryo.register(MouseJointDef.class);
    kryo.register(DistanceJointDef.class);
    kryo.register(BodyDef.BodyType.class);
    kryo.register(GameEntitySerializer.class);
    kryo.register(GameGroupSerializer.class);
    kryo.register(SceneSerializer.class);
    kryo.register(JointInfo.class);
    kryo.register(InitInfo.class);
    kryo.register(GroupType.class);
    kryo.register(SegmentFreshCut.class);
    kryo.register(SpecialEntityType.class);
    kryo.register(WorldFacadeSerializer.class);
    kryo.register(EntityDestructionCommand.class);
  }

  public static SerializationManager getInstance() {
    return INSTANCE;
  }

  public void serialize(GameScene gameScene) throws FileNotFoundException {
    FileOutputStream fileOutputStream =
        gameScene.getActivity().openFileOutput("autoSave.mut", Context.MODE_PRIVATE);
    Output output = new Output(fileOutputStream);
    SceneSerializer sceneSerializer = new SceneSerializer(gameScene);
    kryo.writeObject(output, sceneSerializer);
    output.close();
  }

  public void deserialize(PhysicsScene<?> scene) throws FileNotFoundException {
    FileInputStream fileInputStream = ResourceManager.getInstance().activity.openFileInput("autoSave.mut");
    Input input = new Input(fileInputStream);
    SceneSerializer sceneSerializer = kryo.readObject(input, SceneSerializer.class);
    sceneSerializer.create(scene);
    sceneSerializer.afterCreate(scene);
    input.close();
  }
}
