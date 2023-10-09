package com.evolgames.entities.commandtemplate;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.commandtemplate.commands.BodyCreationCommand;
import com.evolgames.entities.commandtemplate.commands.BodyDestructionCommand;
import com.evolgames.entities.commandtemplate.commands.Command;
import com.evolgames.entities.commandtemplate.commands.CustomCommand;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.commandtemplate.commands.JointDestructionCommand;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;

import java.util.ArrayList;
import java.util.Iterator;

public class Invoker {
    public static GameScene gameScene;

    synchronized public static void onStep() {

        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
            for (GameGroup gameGroup : gameScene.getGameGroups()) {
                Iterator<GameEntity> entitiesIterator = gameGroup.getGameEntities().iterator();
                while (entitiesIterator.hasNext()) {
                    GameEntity gameEntity = entitiesIterator.next();
                    for (Command command : gameEntity.getCommands()) {
                        if (!command.isAborted()) {
                            command.execute();
                            if(command instanceof BodyDestructionCommand){
                                entitiesIterator.remove();
                            }
                        }
                    }
                }
                Iterator<Command> groupCommandsIterator = gameGroup.getCommands().iterator();
                while (groupCommandsIterator.hasNext()) {
                    Command command = groupCommandsIterator.next();
                    command.execute();
                    if(command instanceof JointDestructionCommand){
                        groupCommandsIterator.remove();
                    }
                }
            }
        });
    }

    public static void addBodyCreationCommand(GameEntity entity, BodyDef.BodyType type, BodyInit bodyInit) {

        for (Command c : entity.getCommands()) {
            if (c instanceof BodyDestructionCommand) {
                c.setAborted(true);
                return;
            }
        }

        BodyCreationCommand command = new BodyCreationCommand(entity, type, bodyInit);
        entity.getCommands().add(command);
    }

    public static void addBodyDestructionCommand(GameEntity entity) {
        if (!entity.isAlive()) {
            return;
        }
        for (Command command : entity.getCommands()) {
            if (command instanceof BodyDestructionCommand){
                return;
            }
        }

        Command command = new BodyDestructionCommand(entity);
        entity.getCommands().add(command);
        entity.setAlive(false);
    }

    public static void addJointDestructionCommand(GameGroup gameGroup, Joint joint) {
        ArrayList<Command> commandContainer = gameGroup.getCommands();
        commandContainer.add(new JointDestructionCommand(joint));
        Iterator<Command> iterator = commandContainer.iterator();
        while (iterator.hasNext()){
            Command command = iterator.next();
            if(command instanceof JointCreationCommand){
                JointCreationCommand jointCreationCommand = (JointCreationCommand) command;
                if(jointCreationCommand.getJoint()==joint){
                   iterator.remove();
                }
            }
        }
    }

    public static void addJointCreationCommand(JointDef jointDef, GameGroup gameGroup, GameEntity entity1, GameEntity entity2){
        JointCreationCommand command = new JointCreationCommand(jointDef, entity1, entity2);
        gameGroup.getCommands().add(command);
    }
    public static void addCustomCommand(GameEntity entity,Runnable runnable){
        entity.getCommands().add(new CustomCommand(runnable));
    }

}
