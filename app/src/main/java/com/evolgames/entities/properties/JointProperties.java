package com.evolgames.entities.properties;

import com.badlogic.gdx.physics.box2d.JointDef;

public class JointProperties extends Properties{

    private JointDef jointDef;

    @SuppressWarnings("unused")
    public JointProperties(){}

    public JointProperties(JointDef jointDef) {
        this.jointDef = jointDef;
    }

    @Override
    public Properties copy() {
        return null;
    }

    public JointDef getJointDef() {
        return jointDef;
    }
}
