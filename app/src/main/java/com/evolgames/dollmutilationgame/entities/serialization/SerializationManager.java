package com.evolgames.dollmutilationgame.entities.serialization;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.GroupType;
import com.evolgames.dollmutilationgame.entities.basics.SpecialEntityType;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;
import com.evolgames.dollmutilationgame.entities.blocks.DecorationBlock;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.blocks.Polarity;
import com.evolgames.dollmutilationgame.entities.blocks.StainBlock;
import com.evolgames.dollmutilationgame.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.dollmutilationgame.entities.contact.Pair;
import com.evolgames.dollmutilationgame.entities.cut.CutPoint;
import com.evolgames.dollmutilationgame.entities.cut.PointsFreshCut;
import com.evolgames.dollmutilationgame.entities.cut.SegmentFreshCut;
import com.evolgames.dollmutilationgame.entities.hand.AngleChangerHandControl;
import com.evolgames.dollmutilationgame.entities.hand.Hand;
import com.evolgames.dollmutilationgame.entities.hand.HoldHandControl;
import com.evolgames.dollmutilationgame.entities.hand.MoveHandControl;
import com.evolgames.dollmutilationgame.entities.hand.MoveToSlashHandControl;
import com.evolgames.dollmutilationgame.entities.hand.MoveToStabHandControl;
import com.evolgames.dollmutilationgame.entities.hand.ParallelHandControl;
import com.evolgames.dollmutilationgame.entities.hand.PlayerAction;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.entities.hand.SwingHandControl;
import com.evolgames.dollmutilationgame.entities.hand.ThrowHandControl;
import com.evolgames.dollmutilationgame.entities.init.BodyInitImpl;
import com.evolgames.dollmutilationgame.entities.properties.BodyProperties;
import com.evolgames.dollmutilationgame.entities.properties.BodyUsageCategory;
import com.evolgames.dollmutilationgame.entities.properties.CoatingProperties;
import com.evolgames.dollmutilationgame.entities.properties.DecorationProperties;
import com.evolgames.dollmutilationgame.entities.properties.JointBlockProperties;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.dollmutilationgame.entities.properties.StainProperties;
import com.evolgames.dollmutilationgame.entities.serialization.infos.BombInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.CasingInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.DragInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.FireSourceInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.JointInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.LiquidSourceInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.ProjectileInfo;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.EnumSerializer;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.FloatArraySerializer;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.GameEntitySerializer;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.GameGroupSerializer;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.SceneSerializer;
import com.evolgames.dollmutilationgame.entities.serialization.serializers.WorldFacadeSerializer;
import com.evolgames.dollmutilationgame.entities.usage.Bow;
import com.evolgames.dollmutilationgame.entities.usage.Drag;
import com.evolgames.dollmutilationgame.entities.usage.FlameThrower;
import com.evolgames.dollmutilationgame.entities.usage.Heavy;
import com.evolgames.dollmutilationgame.entities.usage.ImpactBomb;
import com.evolgames.dollmutilationgame.entities.usage.LiquidContainer;
import com.evolgames.dollmutilationgame.entities.usage.Missile;
import com.evolgames.dollmutilationgame.entities.usage.MotorControl;
import com.evolgames.dollmutilationgame.entities.usage.Muzzle;
import com.evolgames.dollmutilationgame.entities.usage.Projectile;
import com.evolgames.dollmutilationgame.entities.usage.ProjectileType;
import com.evolgames.dollmutilationgame.entities.usage.Rocket;
import com.evolgames.dollmutilationgame.entities.usage.RocketLauncher;
import com.evolgames.dollmutilationgame.entities.usage.Shooter;
import com.evolgames.dollmutilationgame.entities.usage.Slasher;
import com.evolgames.dollmutilationgame.entities.usage.Smasher;
import com.evolgames.dollmutilationgame.entities.usage.Stabber;
import com.evolgames.dollmutilationgame.entities.usage.Throw;
import com.evolgames.dollmutilationgame.entities.usage.TimeBomb;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.scenes.PlayScene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SerializationManager {

    private final Kryo kryo;

    SerializationManager() {
        this.kryo = new Kryo();
        kryo.register(GameEntitySerializer.class);
        kryo.register(GameGroupSerializer.class);
        kryo.register(SceneSerializer.class);
        kryo.register(JointInfo.class);
        kryo.register(InitInfo.class);
        kryo.register(BombInfo.class);
        kryo.register(LiquidSourceInfo.class);
        kryo.register(DragInfo.class);
        kryo.register(ProjectileInfo.class);
        kryo.register(CasingInfo.class);
        kryo.register(WorldFacadeSerializer.class);
        kryo.register(FireSourceInfo.class);
        kryo.register(LayerBlock.class);
        kryo.register(CoatingBlock.class);
        kryo.register(StainBlock.class);
        kryo.register(JointBlock.class);
        kryo.register(DecorationBlock.class);
        kryo.register(Polarity.class, new EnumSerializer<Polarity>());
        kryo.register(JointBlock.Position.class, new EnumSerializer<JointBlock.Position>());
        kryo.register(BodyInitImpl.class);
        kryo.register(SpecialEntityType.class, new EnumSerializer<SpecialEntityType>());
        kryo.register(GroupType.class, new EnumSerializer<GroupType>());
        kryo.register(EntityDestructionCommand.class);
        kryo.register(HoldHandControl.class);
        kryo.register(AngleChangerHandControl.class);
        kryo.register(MoveHandControl.class);
        kryo.register(MoveToSlashHandControl.class);
        kryo.register(MoveToStabHandControl.class);
        kryo.register(ParallelHandControl.class);
        kryo.register(SwingHandControl.class);
        kryo.register(ThrowHandControl.class);
        kryo.register(Pair.class);
        kryo.register(PointsFreshCut.class);
        kryo.register(CutPoint.class);
        kryo.register(SegmentFreshCut.class);
        kryo.register(Slasher.class);
        kryo.register(Smasher.class);
        kryo.register(Heavy.class);
        kryo.register(Shooter.class);
        kryo.register(Stabber.class);
        kryo.register(Throw.class);
        kryo.register(Projectile.class);
        kryo.register(TimeBomb.class);
        kryo.register(ProjectileType.class, new EnumSerializer<ProjectileType>());
        kryo.register(FlameThrower.class);
        kryo.register(Rocket.class);
        kryo.register(Muzzle.class);
        kryo.register(Drag.class);
        kryo.register(ImpactBomb.class);
        kryo.register(Missile.class);
        kryo.register(MotorControl.class);
        kryo.register(LiquidContainer.class);
        kryo.register(RocketLauncher.class);
        kryo.register(Bow.class);

        kryo.register(BodyUsageCategory.class, new EnumSerializer<BodyUsageCategory>());
        kryo.register(LayerProperties.class);
        kryo.register(CoatingProperties.class);
        kryo.register(StainProperties.class);
        kryo.register(JointBlockProperties.class);
        kryo.register(DecorationProperties.class);
        kryo.register(BodyProperties.class);

        kryo.register(Hand.class);
        kryo.register(PlayerAction.class, new EnumSerializer<PlayerAction>());
        kryo.register(PlayerSpecialAction.class, new EnumSerializer<PlayerSpecialAction>());


        kryo.register(com.badlogic.gdx.physics.box2d.Filter.class);
        kryo.register(com.badlogic.gdx.physics.box2d.JointDef.JointType.class, new EnumSerializer<JointDef.JointType>());
        kryo.register(com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef.class);
        kryo.register(com.badlogic.gdx.physics.box2d.joints.WeldJointDef.class);
        kryo.register(com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef.class);
        kryo.register(com.badlogic.gdx.physics.box2d.joints.MouseJointDef.class);
        kryo.register(com.badlogic.gdx.physics.box2d.joints.DistanceJointDef.class);
        kryo.register(com.badlogic.gdx.physics.box2d.BodyDef.BodyType.class, new EnumSerializer<BodyDef.BodyType>());


        kryo.register(java.util.HashMap.class);
        kryo.register(java.util.Stack.class);
        kryo.register(org.andengine.util.adt.color.Color.class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.concurrent.CopyOnWriteArrayList.class);
        kryo.register(java.util.HashSet.class);
        kryo.register(float[].class, new FloatArraySerializer());
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

    public void deserialize(PhysicsScene scene, String fileName) throws FileNotFoundException, RuntimeException {
        FileInputStream fileInputStream = ResourceManager.getInstance().activity.openFileInput(fileName);
        Input input = new Input(fileInputStream);
        SceneSerializer sceneSerializer = kryo.readObject(input, SceneSerializer.class);
        sceneSerializer.create(scene);
        sceneSerializer.afterCreate(scene);
        input.close();
    }
}
