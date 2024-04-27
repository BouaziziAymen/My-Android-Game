package com.evolgames.dollmutilationgame.entities.usage;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.evolgames.dollmutilationgame.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.entities.properties.usage.MotorControlProperties;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.UsageModel;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;

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
    private int motorSound;

    private boolean mirrored;

    @SuppressWarnings("unused")
    public MotorControl() {
    }

    public MotorControl(UsageModel<?> e, List<JointBlock> jointBlocks, boolean mirrored) {
        MotorControlProperties motorControlProperties = (MotorControlProperties) e.getProperties();
        this.forwardSpeed = PhysicsConstants.getRotationSpeedFromRatio(!mirrored ? motorControlProperties.getForwardSpeed() : motorControlProperties.getBackwardSpeed());
        this.backwardSpeed = PhysicsConstants.getRotationSpeedFromRatio(!mirrored ? motorControlProperties.getBackwardSpeed() : motorControlProperties.getForwardSpeed());
        this.brakes = motorControlProperties.isBrakes();
        this.power = motorControlProperties.getPower() * 735 * 4;
        this.jointBlocks = jointBlocks;
        this.jointBlockUniqueIds = jointBlocks.stream().map(JointBlock::getJointUniqueId).collect(Collectors.toList());
        this.motorState = 0;
        if(power<10){
            motorSound = 0;
        } else if (power<50){
            motorSound = 1;
        } else {
            motorSound = 2;
        }
        this.mirrored = mirrored;
    }


    public void setMotorState(int motorState) {
        this.motorState = motorState;
    }
    public boolean isRunning(){
        return this.motorState != 0;
    }
    public int getMotorSound(){
        return motorSound;
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
                    if (motorState == (mirrored?-1:1)) {
                        if (brakes) {
                            motor.enableLimit(false);
                        }
                        motor.enableMotor(true);
                        motor.setMotorSpeed(forwardSpeed);
                        motor.setMaxMotorTorque(power);
                    }
                    if (motorState == (mirrored?1:-1)) {
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
    public void dynamicMirror(PhysicsScene physicsScene) {
        float backwardSpeed = this.backwardSpeed;
        this.backwardSpeed = this.forwardSpeed;
        this.forwardSpeed = backwardSpeed;
        this.mirrored = !mirrored;
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return ratio > 0.5f;
    }

    public void setJointBlocks(List<JointBlock> jointBlocks) {
        this.jointBlocks = jointBlocks;
    }
}
