package com.evolgames.scenes.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.HandControl;
import com.evolgames.entities.hand.HoldHandControl;
import com.evolgames.entities.hand.SwingHandControl;
import com.evolgames.physics.WorldFacade;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;

import java.util.Stack;

public class Hand {
    private final WorldFacade worldFacade;
    private int mousePointerId;
    Stack<HandControl> handControlStack = new Stack<>();
    private GameEntity grabbedEntity;
    private MouseJoint mouseJoint;
    private int step = 0;

    public Hand(WorldFacade worldFacade) {
        this.worldFacade = worldFacade;
    }

    public GameEntity getGrabbedEntity() {
        return grabbedEntity;
    }

    public void grab(GameEntity entity, TouchEvent touchEvent) {
            entity.setHanged(true);
            grabbedEntity = entity;

            final MouseJointDef mouseJointDef = new MouseJointDef();
            entity.setHangedPointerId(touchEvent.getPointerID());
            mouseJointDef.dampingRatio = 0.5f;
            mouseJointDef.frequencyHz = 10f;
            mouseJointDef.maxForce = 1000 * entity.getMassOfGroup();
            mouseJointDef.target.set(touchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
            mouseJointDef.collideConnected = true;
            mousePointerId = touchEvent.getPointerID();
            worldFacade.addJointToCreate(mouseJointDef, worldFacade.getGround().getGameEntityByIndex(0), entity);
        }
        public void holdHand(float degree){
                handControlStack.push(new HoldHandControl(grabbedEntity.getBody(), 0));
        }
        public void swingHand(){
        handControlStack.push(new SwingHandControl(grabbedEntity.getBody(),this.mouseJoint,30,10,5000));
        }
    public void onUpdate() {
        if (!handControlStack.isEmpty()) {
            HandControl top = handControlStack.peek();
            if (top.isDead()){
                handControlStack.pop();
            }
            else {
                top.run();
            }
        }
    }
    public void releaseGrabbedEntity(){
            Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), mouseJoint);
            if(!handControlStack.isEmpty()) {
                handControlStack.peek().setDead(true);
            }
            grabbedEntity.setHanged(false);
            this.grabbedEntity = null;
    }

    public void onSceneTouchEvent(TouchEvent touchEvent) {
        if (mouseJoint != null&&grabbedEntity!=null&&grabbedEntity.isHanged()) {
            if (touchEvent.isActionMove() && touchEvent.getPointerID() == mousePointerId&&grabbedEntity.isHanged()) {
                Vector2 vec = Vector2Pool
                        .obtain(touchEvent.getX()
                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                (touchEvent.getY())
                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

                this.mouseJoint.setTarget(vec);
                Vector2Pool.recycle(vec);
            }
        }
        if(!handControlStack.isEmpty()){
            if(handControlStack.peek() instanceof HoldHandControl){
                if(touchEvent.isActionDown()&&this.mouseJoint!=null){
                    swingHand();
                }
            }
        }
    }

    public MouseJoint getMouseJoint() {
        return mouseJoint;
    }

    public void setMouseJoint(MouseJoint joint) {
        this.mouseJoint = joint;
    }

    public void release() {
       releaseGrabbedEntity();
    }

    public void resumeGrab() {
        grabbedEntity.setHanged(true);
    }

    public void stopFollow() {
        grabbedEntity.setHanged(false);
    }
}
