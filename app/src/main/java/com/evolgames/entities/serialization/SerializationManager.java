package com.evolgames.entities.serialization;

import android.content.Context;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.entities.serialization.infos.CasingInfo;
import com.evolgames.entities.serialization.infos.InitInfo;
import com.evolgames.entities.serialization.infos.JointInfo;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SerializationManager {

  private final Kryo kryo;

  SerializationManager() {
    this.kryo = new Kryo();
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
    kryo.register(JointInfo.class);
    kryo.register(InitInfo.class);
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
    kryo.register(com.evolgames.entities.cut.PointsFreshCut.class);
    kryo.register(com.evolgames.entities.cut.CutPoint.class);
    kryo.register(com.evolgames.entities.usage.Slasher.class);
    kryo.register(com.evolgames.entities.hand.HandControl[].class);
    kryo.register(com.evolgames.entities.usage.Smasher.class);
    kryo.register(com.evolgames.entities.usage.Shooter.class);
    kryo.register(ProjectileInfo.class);
    kryo.register(CasingInfo.class);
    kryo.register(com.evolgames.userinterface.model.BodyUsageCategory.class);
    kryo.register(com.evolgames.entities.usage.TimeBomb.class);
    kryo.register(BombInfo.class);
    kryo.register(com.evolgames.entities.usage.ProjectileType.class);
    kryo.register(com.evolgames.entities.usage.FlameThrower.class);
    kryo.register(com.evolgames.entities.serialization.infos.FireSourceInfo.class);
    kryo.register(com.evolgames.entities.usage.Rocket.class);
    kryo.register(com.evolgames.entities.usage.Drag.class);
    kryo.register(com.evolgames.entities.serialization.infos.DragInfo.class);
    kryo.register(com.evolgames.entities.usage.ImpactBomb.class);
    kryo.register(com.evolgames.entities.usage.Missile.class);
    kryo.setReferences(false);
  }

  public void serialize(PlayScene playScene, String fileName) throws FileNotFoundException, RuntimeException {

    FileOutputStream fileOutputStream =
        playScene.getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
    Output output = new Output(fileOutputStream);
    SceneSerializer sceneSerializer = new SceneSerializer(playScene);
    kryo.writeObject(output, sceneSerializer);
    output.close();
  }

  public void deserialize(PhysicsScene<?> scene, String fileName) throws FileNotFoundException, RuntimeException {
    FileInputStream fileInputStream = ResourceManager.getInstance().activity.openFileInput(fileName);
    Input input = new Input(fileInputStream);
    SceneSerializer sceneSerializer = kryo.readObject(input, SceneSerializer.class);
    sceneSerializer.create(scene);
    sceneSerializer.afterCreate(scene);
    input.close();
  }
}
