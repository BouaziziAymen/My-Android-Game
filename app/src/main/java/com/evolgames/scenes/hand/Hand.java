package com.evolgames.scenes.hand;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.commandtemplate.Invoker;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.control.HandControl;
import com.evolgames.entities.control.HoldHandControl;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.GameScene;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;

import java.util.Stack;

public class Hand {
    GameEntity grabbedEntity;
    WorldFacade worldFacade;
    MouseJoint mouseJoint;
    int mousePointerId;
    Stack<HandControl> handControlStack = new Stack<>();

    public Hand(WorldFacade worldFacade) {
        this.worldFacade = worldFacade;
    }


    public void grab(GameEntity entity, TouchEvent touchEvent) {
        grabbedEntity = entity;
        handControlStack.push(new HoldHandControl(grabbedEntity.getBody(), 0));
        final MouseJointDef mouseJointDef = new MouseJointDef();
        entity.setHanged(true);
        entity.setHangedPointerId(touchEvent.getPointerID());
        mouseJointDef.dampingRatio = 1f;
        mouseJointDef.frequencyHz = 5f;
        mouseJointDef.maxForce = 1000 * entity.getMassOfGroup();
        mouseJointDef.target.set(touchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        mouseJointDef.collideConnected = true;
        mousePointerId = touchEvent.getPointerID();
        worldFacade.addJointToCreate(mouseJointDef, worldFacade.getGround().getGameEntityByIndex(0), entity, false);
    }

    public void onUpdate() {
        if(!handControlStack.isEmpty()) {
            HandControl top = handControlStack.peek();
            if (top.isDead()) handControlStack.pop();
            else top.run();
        }
        if(grabbedEntity!=null){
            if(GameScene.step%120==0) {
               // handControlStack.push(new SwingHandControl(grabbedEntity.getBody(),60,10,1000));
            }
        }

    }

    public void onSceneTouchEvent(TouchEvent touchEvent) {
        Log.e("grab hand","onSceneTouch"+touchEvent.getPointerID()+":"+mousePointerId);
        if(touchEvent.isActionUp())
            if(!handControlStack.isEmpty()) {
            HandControl handControl = handControlStack.peek();
            if(handControl instanceof HoldHandControl){
                if (grabbedEntity != null) {
                    Invoker.addMouseJointDestructionCommand(grabbedEntity, mouseJoint);
                    grabbedEntity.setHanged(false);
                    grabbedEntity = null;
                    handControl.setDead(true);
                }
            }
        }
        if (mouseJoint != null) {
            if (touchEvent.isActionMove() && touchEvent.getPointerID() == mousePointerId) {
                Vector2 vec = Vector2Pool
                        .obtain(touchEvent.getX()
                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
                                (touchEvent.getY()+32)
                                        / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

                this.mouseJoint.setTarget(vec);
                Log.e("grab hand","drag");
                Vector2Pool.recycle(vec);
            }
            if (touchEvent.isActionCancel() || touchEvent.isActionOutside() || touchEvent.isActionUp()) {
                if (grabbedEntity != null) {
                    Invoker.addMouseJointDestructionCommand(grabbedEntity, mouseJoint);
                    grabbedEntity.setHanged(false);
                    grabbedEntity = null;
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
}
