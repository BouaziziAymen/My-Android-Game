package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.joint.JointKey;
import com.evolgames.entities.properties.JointZoneProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.physics.GameEntityTransformation;
import java.util.ArrayList;


public final class JointZoneBlock extends InvisibleBlock<JointZoneBlock, JointZoneProperties> {

    private JointZoneBlock twin;
    private JointKey.KeyType jointKeyType;
    private WeldJointDef jointDef;
    private final GameEntityTransformation transformation;
    private int jointId;
    private Vector2 advance;
    private float area;
    private LayerBlock parent;

    @Override
    public LayerBlock getParent() {
        return parent;
    }

    @Override
    public void setParent(LayerBlock parent) {
        this.parent = parent;
    }

    public JointZoneBlock(GameEntityTransformation transformation) {
        this.transformation = transformation;
    }

    public void setJointId(int jointId) {
        this.jointId = jointId;
    }

    @Override
    public void performCut(Cut cut) {
        super.performCut(cut);
        if (twin==null||!twin.isNotAborted()) {
            return;
        }

        JointZoneBlock child1 = getChildren().get(0);
        JointZoneBlock child2 = getChildren().get(1);

        child1.setJointKeyType(jointKeyType);
        child2.setJointKeyType(jointKeyType);

        child1.setJointId(jointId);
        child2.setJointId(jointId);
        child1.setJointDef(jointDef);
        child2.setJointDef(jointDef);
        child1.setParent(getParent());
        child2.setParent(getParent());
        //create twin children
        ArrayList<Vector2> twinChild1Vertices = GameEntityTransformation.interpolateScene(transformation, child1.getVertices(), jointKeyType == JointKey.KeyType.A);
        ArrayList<Vector2> twinChild2Vertices = GameEntityTransformation.interpolateScene(transformation, child2.getVertices(), jointKeyType == JointKey.KeyType.A);

        JointZoneBlock twinChild1 = new JointZoneBlock(transformation);
        JointZoneBlock twinChild2 = new JointZoneBlock(transformation);
        twinChild1.initialization(twinChild1Vertices, getProperties(), 0, true);
        twinChild2.initialization(twinChild2Vertices, getProperties(), 0, true);

        child1.setAdvance(advance);
        child2.setAdvance(advance);
        twinChild1.setAdvance(advance);
        twinChild2.setAdvance(advance);
        twinChild1.setJointKeyType(jointKeyType == JointKey.KeyType.A ? JointKey.KeyType.B : JointKey.KeyType.A);
        twinChild2.setJointKeyType(jointKeyType == JointKey.KeyType.A ? JointKey.KeyType.B : JointKey.KeyType.A);
        twinChild1.setJointId(jointId);
        twinChild2.setJointId(jointId);
        twinChild1.setJointDef(jointDef);
        twinChild2.setJointDef(jointDef);

        twin.setAborted(true);


/*
            twin.getParent().getAssociatedBlocks().remove(twin);

            if(twinChild1.isNotAborted())
            twin.getParent().addAssociatedBlock(twinChild1);
            if(twinChild2.isNotAborted())
            twin.getParent().addAssociatedBlock(twinChild2);
twinChild1.setSkip(true);
twinChild2.setSkip(true);
*/

        child1.setTwin(twinChild1);
        child2.setTwin(twinChild2);

        twin.getChildren().add(twinChild1);
        twin.getChildren().add(twinChild2);

        child1.getTwin().setTwin(child1);
        child2.getTwin().setTwin(child2);
    }

    @Override
    protected void checkShape() {
        if (getVertices().size() < 3 || area < 9) {
            setAborted(true);
        } else {
            setAborted(false);
        }
    }


    @Override
    protected void calculateArea() {
        area = GeometryUtils.getArea(getVertices());
    }

    @Override
    protected boolean calcArea() {
        return true;
    }

    public JointKey.KeyType getJointKeyType() {
        return jointKeyType;
    }

    public void setJointKeyType(JointKey.KeyType jointKeyType) {
        this.jointKeyType = jointKeyType;
    }


    @Override
    protected JointZoneBlock createChildBlock() {
        return new JointZoneBlock(transformation);
    }

    public JointZoneBlock getTwin() {
        return twin;
    }


    public void setTwin(JointZoneBlock twin) {
        this.twin = twin;
    }

    public WeldJointDef getJointDef() {
        return jointDef;
    }

    public void setJointDef(WeldJointDef jointDef) {
        this.jointDef = jointDef;
    }



    @Override
    protected boolean rectify() {
        return true;
    }

    @Override
    protected void rectifyVertices() {
        BlockUtils.bruteForceRectificationDecoration(getVertices());
    }

    public Vector2 getAdvance() {
        return advance;
    }

    public void setAdvance(Vector2 advance) {
        this.advance = advance;
    }

    @Override
    protected JointZoneBlock getThis() {
        return this;
    }
}
