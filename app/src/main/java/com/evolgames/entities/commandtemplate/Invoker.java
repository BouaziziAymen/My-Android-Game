package com.evolgames.entities.commandtemplate;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.commands.BodyCreationCommand;
import com.evolgames.entities.commandtemplate.commands.BodyDestructionCommand;
import com.evolgames.entities.commandtemplate.commands.BodyMirrorCommand;
import com.evolgames.entities.commandtemplate.commands.Command;
import com.evolgames.entities.commandtemplate.commands.CustomCommand;
import com.evolgames.entities.commandtemplate.commands.GameGroupDestructionCommand;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.commandtemplate.commands.JointDestructionCommand;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;

public class Invoker {

    public static PhysicsScene scene;

    public static synchronized void onStep() {

        ResourceManager.getInstance()
                .activity
                .runOnUpdateThread(
                        () -> {
                            for (GameGroup gameGroup : scene.getGameGroups()) {
                                for (GameEntity gameEntity : gameGroup.getGameEntities()) {
                                    for (Command command : gameEntity.getCommands()) {
                                        if (!command.isAborted()) {
                                            command.execute();
                                            if (command instanceof BodyDestructionCommand) {
                                                gameGroup.getGameEntities().remove(gameEntity);
                                            }
                                        }
                                    }
                                }
                            }
                            // after creating the bodies we can create the joints
                            for (GameGroup gameGroup : scene.getGameGroups()) {
                                gameGroup.getCommands().sort((a, b) -> {
                                    if (a instanceof JointDestructionCommand) return 1;
                                    else if (b instanceof JointDestructionCommand) return -1;
                                    return 0;
                                });
                                for (Command command : gameGroup.getCommands()) {
                                    if (!(command instanceof BodyMirrorCommand)) {
                                        command.execute();
                                        if (command.isAborted()) {
                                            gameGroup.getCommands().remove(command);
                                        }
                                    }
                                }
                            }
                            for (GameGroup gameGroup : scene.getGameGroups()) {
                                for (Command command : gameGroup.getCommands()) {
                                    if ((command instanceof BodyMirrorCommand)) {
                                        command.execute();
                                        if (command.isAborted()) {
                                            gameGroup.getCommands().remove(command);
                                        }
                                    }
                                }
                            }
                            scene.getGameGroups().removeIf(gameGroup -> gameGroup.isDestroyed()||gameGroup.isReadyToDestroy());
                        });
    }

    public static void addGroupDestructionCommand(
            GameGroup gameGroup) {
        GameGroupDestructionCommand command = new GameGroupDestructionCommand(gameGroup);
        gameGroup.getCommands().add(command);
    }

    public static void addBodyMirrorCommand(
            GameEntity entity) {
        BodyMirrorCommand command = new BodyMirrorCommand(entity);
        entity.getParentGroup().getCommands().add(command);
    }


    public static void addBodyCreationCommand(
            GameEntity entity, BodyDef.BodyType type, BodyInit bodyInit) {

        for (Command c : entity.getCommands()) {
            if (c instanceof BodyDestructionCommand) {
                c.setAborted(true);
                return;
            }
        }

        BodyCreationCommand command = new BodyCreationCommand(entity, type, bodyInit);
        entity.getCommands().add(command);
    }

    public static void addBodyDestructionCommand(GameEntity entity, boolean finalDestruction) {
        for (Command command : entity.getCommands()) {
            if (command instanceof BodyDestructionCommand) {
                return;
            }
        }

        Command command = new BodyDestructionCommand(entity,finalDestruction);
        entity.getCommands().add(command);
        entity.setAlive(false);
    }

    public static void addJointDestructionCommand(GameGroup gameGroup, Joint joint) {
        List<Command> commandContainer = gameGroup.getCommands();
        commandContainer.add(new JointDestructionCommand(joint));
    }

    public static void addJointCreationCommand(
            JointDef jointDef, GameGroup gameGroup, GameEntity entity1, GameEntity entity2, JointBlock main) {
        JointCreationCommand command = new JointCreationCommand(jointDef, entity1, entity2, main);
        gameGroup.getCommands().add(command);
    }

    public static void addCustomCommand(GameEntity entity, Runnable runnable) {
        entity.getCommands().add(new CustomCommand(runnable));
    }

    public static void setScene(PhysicsScene scene) {
        Invoker.scene = scene;
    }

}
