package com.evolgames.entities.serialization;

import android.content.Context;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SerializationManager {
  private static final SerializationManager INSTANCE = new SerializationManager();
  private final Kryo kryo = new Kryo();

  private SerializationManager() {
    kryo.register(com.evolgames.entities.blocks.LayerBlock.class);
    kryo.register(com.evolgames.entities.blocks.CoatingBlock.class);
    kryo.register(com.evolgames.entities.blocks.StainBlock.class);
    kryo.register(com.evolgames.entities.blocks.JointBlock.class);
    kryo.register(com.evolgames.entities.blocks.DecorationBlock.class);
    kryo.register(com.evolgames.entities.blocks.Polarity.class);
    kryo.register(com.evolgames.entities.properties.LayerProperties.class);
    kryo.register(com.evolgames.entities.properties.CoatingProperties.class);
    kryo.register(com.evolgames.entities.properties.StainProperties.class);
    kryo.register(com.evolgames.entities.properties.JointProperties.class);
    kryo.register(com.evolgames.entities.properties.DecorationProperties.class);
    kryo.register(org.andengine.util.adt.color.Color.class);
    kryo.register(com.badlogic.gdx.math.Vector2.class);
    kryo.register(java.util.ArrayList.class);
    kryo.register(java.util.HashSet.class);
    kryo.register(float[].class);
    kryo.register(com.evolgames.entities.init.BodyInitImpl.class);
    kryo.register(com.badlogic.gdx.physics.box2d.Filter.class);
    kryo.register(com.badlogic.gdx.physics.box2d.JointDef.JointType.class);
    kryo.register(com.evolgames.entities.blocks.JointBlock.Position.class);
    kryo.register(com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef.class);
    kryo.register(com.badlogic.gdx.physics.box2d.joints.WeldJointDef.class);
    kryo.register(com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef.class);
    kryo.register(com.badlogic.gdx.physics.box2d.joints.MouseJointDef.class);
    kryo.register(com.badlogic.gdx.physics.box2d.joints.DistanceJointDef.class);
    kryo.register(com.badlogic.gdx.physics.box2d.BodyDef.BodyType.class);
    kryo.register(com.evolgames.entities.serialization.GameEntitySerializer.class);
    kryo.register(com.evolgames.entities.serialization.GameGroupSerializer.class);
    kryo.register(com.evolgames.entities.serialization.SceneSerializer.class);
    kryo.register(com.evolgames.entities.serialization.JointInfo.class);
    kryo.register(com.evolgames.entities.serialization.InitInfo.class);
    kryo.register(com.evolgames.entities.GroupType.class);
    kryo.register(com.evolgames.entities.cut.SegmentFreshCut.class);
    kryo.register(com.evolgames.entities.SpecialEntityType.class);
    kryo.register(com.evolgames.entities.serialization.WorldFacadeSerializer.class);
    kryo.register(com.evolgames.entities.commandtemplate.EntityDestructionCommand.class);
    kryo.register(java.util.HashMap.class);
    kryo.register(com.evolgames.scenes.entities.Hand.class);
    kryo.register(java.util.Stack.class);
    kryo.register(com.evolgames.entities.hand.HoldHandControl.class);
    kryo.register(com.evolgames.entities.hand.AngleChangerHandControl.class);
    kryo.register(com.evolgames.entities.hand.MoveHandControl.class);
    kryo.register(com.evolgames.entities.hand.MoveToSlashHandControl.class);
    kryo.register(com.evolgames.entities.hand.MoveToStabHandControl.class);
    kryo.register(com.evolgames.entities.hand.ParallelHandControl.class);
    kryo.register(com.evolgames.entities.hand.SwingHandControl.class);
    kryo.register(com.evolgames.entities.hand.ThrowHandControl.class);
    kryo.register(com.evolgames.entities.contact.Pair.class);
    kryo.register(com.evolgames.entities.usage.Stabber.class);
    kryo.register(com.evolgames.entities.usage.Throw.class);
    kryo.register(com.evolgames.entities.usage.Projectile.class);
    kryo.register(com.evolgames.scenes.entities.PlayerAction.class);
    kryo.register(com.evolgames.scenes.entities.PlayerSpecialAction.class);
  }

  public static SerializationManager getInstance() {
    return INSTANCE;
  }

  public void serialize(PlayScene playScene) throws FileNotFoundException {
    FileOutputStream fileOutputStream =
        playScene.getActivity().openFileOutput("autoSave.mut", Context.MODE_PRIVATE);
    Output output = new Output(fileOutputStream);
    SceneSerializer sceneSerializer = new SceneSerializer(playScene);
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
