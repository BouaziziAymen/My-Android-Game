package com.evolgames.physics;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointZoneBlock;
import com.evolgames.entities.joint.JointKey;

public class JointReset {
    public JointKey.KeyType getKeyType() {
        return keyType;
    }

    public JointDef getJointDef() {
        return jointDef;
    }


    public GameEntity getFirst() {
        return first;
    }


    public GameEntity getSecond() {
        return second;
    }


    public JointZoneBlock getBlock() {
        return block;
    }

    public void setBlock(JointZoneBlock block) {
        this.block = block;
    }

    private JointKey.KeyType keyType;
    private JointDef jointDef;
    private GameEntity first;
    private GameEntity second;
    private JointZoneBlock block;

    public JointReset(JointKey.KeyType keyType, JointDef jointDef, GameEntity first, GameEntity second, JointZoneBlock block) {
        this.keyType = keyType;
        this.jointDef = jointDef;
        this.first = first;
        this.second = second;
        this.block = block;
    }
}
