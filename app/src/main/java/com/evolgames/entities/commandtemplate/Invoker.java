package com.evolgames.entities.commandtemplate;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.entities.commandtemplate.commands.BodyCreationCommand;
import com.evolgames.entities.commandtemplate.commands.BodyDestructionCommand;
import com.evolgames.entities.commandtemplate.commands.Command;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.commandtemplate.commands.JointDestructionCommand;
import com.evolgames.entities.commandtemplate.commands.MouseJointDestructionCommand;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.physics.entities.JointBlueprint;
import com.evolgames.scenes.GameScene;

import java.util.Iterator;

public class Invoker {
    public static GameScene gameScene;

    synchronized public static void onStep() {

        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
            for (GameGroup gameGroup : gameScene.getGameGroups()) {
                Iterator<GameEntity> iterator = gameGroup.getGameEntities().iterator();
                while (iterator.hasNext()) {
                    GameEntity gameEntity = iterator.next();
                    for (Command command : gameEntity.getCommandContainer().getCommands()) {
                        if (command != null && !command.isAborted()) {
                            command.execute();
                            command.setAborted(true);
                            if (command instanceof JointCreationCommand) {
                                JointCreationCommand j = (JointCreationCommand) command;
                            }
                            if (command instanceof BodyDestructionCommand) {
                                iterator.remove();
                            }
                            if (command instanceof BodyCreationCommand) {


                            }

                        }
                    }
                }
            }
        });
    }

    public static BodyCreationCommand addBodyCreationCommand(GameEntity entity, BodyDef.BodyType type, BodyInit bodyInit) {

        GameEntityCommands commandContainer = entity.getCommandContainer();
        for (Command c : commandContainer.getCommands()) {
            if (c instanceof BodyDestructionCommand) {
                c.setAborted(true);
                return null;
            }
        }

        BodyCreationCommand command = new BodyCreationCommand(entity, type, bodyInit);
        commandContainer.getCommands().add(command);
        return command;
    }

    public static void addBodyDestructionCommand(GameEntity entity) {
        GameEntityCommands commandContainer = entity.getCommandContainer();
        if (commandContainer.isAborted()) return;
        for (Command command : commandContainer.getCommands()) {
            if (command instanceof BodyDestructionCommand) return;
        }
        Command command = new BodyDestructionCommand(entity);
        commandContainer.getCommands().add(command);
        commandContainer.setAborted(true);

    }


    public static void addJointCreationCommand(JointBlueprint jointBlueprint) {

        GameEntityCommands commandContainer1 = jointBlueprint.getEntity1().getCommandContainer();
        GameEntityCommands commandContainer2 = jointBlueprint.getEntity2().getCommandContainer();
        if (!commandContainer1.isAborted() && !commandContainer2.isAborted()) {
            Command command = new JointCreationCommand(jointBlueprint);
            commandContainer1.getCommands().add(command);
            commandContainer2.getCommands().add(command);
        }
    }

    public static void addJointDestructionCommand(JointBlueprint jointBlueprint) {

        GameEntityCommands commandContainer1 = jointBlueprint.getEntity1().getCommandContainer();
        GameEntityCommands commandContainer2 = jointBlueprint.getEntity2().getCommandContainer();
        if (!commandContainer1.isAborted() && !commandContainer2.isAborted()) {
            Command command = new JointDestructionCommand(jointBlueprint);
            commandContainer1.getCommands().add(command);
            commandContainer2.getCommands().add(command);
        }
    }

    public static void addMouseJointDestructionCommand(GameEntity hangedEntity, Joint mouseJoint) {
        GameEntityCommands commandContainer = hangedEntity.getCommandContainer();
        if (!commandContainer.isAborted()) {
            Command command = new MouseJointDestructionCommand(mouseJoint);
            commandContainer.getCommands().add(command);

        }
    }


}
