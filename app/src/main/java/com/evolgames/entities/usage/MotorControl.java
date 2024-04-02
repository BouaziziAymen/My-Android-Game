package com.evolgames.entities.usage;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.properties.usage.MotorControlProperties;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MotorControl extends Use {
    private List<String> jointBlockUniqueIds;
    private boolean brakes;
    private transient List<JointBlock> jointBlocks;
    private float forwardSpeed;
    private float backwardSpeed;
    private float power;
    private int motorState;

    @SuppressWarnings("unused")
    public MotorControl() {
    }

    public MotorControl(UsageModel<?> e, List<JointBlock> jointBlocks, boolean mirrored) {
        MotorControlProperties motorControlProperties = (MotorControlProperties) e.getProperties();
        this.forwardSpeed = PhysicsConstants.getRotationSpeedFromRatio(mirrored ? motorControlProperties.getForwardSpeed() : motorControlProperties.getBackwardSpeed());
        this.backwardSpeed = PhysicsConstants.getRotationSpeedFromRatio(mirrored ? motorControlProperties.getBackwardSpeed() : motorControlProperties.getForwardSpeed());
        this.brakes = motorControlProperties.isBrakes();
        this.power = motorControlProperties.getPower() * 735 * 4;
        this.jointBlocks = jointBlocks;
        this.jointBlockUniqueIds = jointBlocks.stream().map(t -> t.getJointUniqueId()).collect(Collectors.toList());
        this.motorState = 0;
    }


    public void setMotorState(int motorState) {
        this.motorState = motorState;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        for (JointBlock jointBlock : jointBlocks) {
            if (jointBlock != null && jointBlock.getBrother() != null &&
                    jointBlock.getJoint() != null
                    && jointBlock.getEntity() != null
                    && jointBlock.getBrother().getEntity() != null
                    && jointBlock.getEntity().isAlive()
                    && jointBlock.getBrother().getEntity().isAlive()
                    && JointCreationCommand.isJointDefReady(jointBlock.getEntity().getBody(), jointBlock.getBrother().getEntity().getBody())) {
                if (jointBlock.getJointType() == JointDef.JointType.RevoluteJoint) {
                    RevoluteJoint motor = (RevoluteJoint) jointBlock.getJoint();
                    if (motorState == 1) {
                        if (brakes) {
                            motor.enableLimit(false);
                        }
                        motor.enableMotor(true);
                        motor.setMotorSpeed(forwardSpeed);
                        motor.setMaxMotorTorque(power);
                    }
                    if (motorState == -1) {
                        if (brakes) {
                            motor.enableLimit(false);
                        }
                        motor.enableMotor(true);
                        motor.setMotorSpeed(-backwardSpeed);
                        motor.setMaxMotorTorque(power);
                    }
                    if (motorState == 0) {
                        if (brakes) {
                            motor.enableLimit(true);
                        }
                        motor.enableMotor(false);
                    }
                }
            }
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.motorStop);
        list.add(PlayerSpecialAction.motorMoveForward);
        list.add(PlayerSpecialAction.motorMoveBackward);
        return list;
    }

    public List<String> getJointBlockUniqueIds() {
        return jointBlockUniqueIds;
    }


    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {
        float backwardSpeed = this.backwardSpeed;
        this.backwardSpeed = this.forwardSpeed;
        this.forwardSpeed = backwardSpeed;
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return ratio > 0.5f;
    }

    public void setJointBlocks(List<JointBlock> jointBlocks) {
        this.jointBlocks = jointBlocks;
    }
}
