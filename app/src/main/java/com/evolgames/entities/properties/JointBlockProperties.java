package com.evolgames.entities.properties;

import com.badlogic.gdx.physics.box2d.JointDef;

public class JointBlockProperties extends Properties {

    private JointDef jointDef;
@SuppressWarnings("Unused")
    public JointBlockProperties(){}
    public JointBlockProperties(JointDef jointDef) {
        this.jointDef = jointDef;
    }

    public JointDef getJointDef() {
        return jointDef;
    }

}
