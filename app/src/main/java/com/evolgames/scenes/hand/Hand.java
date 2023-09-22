package com.evolgames.scenes.hand;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.HandAction;
import com.evolgames.entities.hand.HandControl;
import com.evolgames.entities.hand.HoldHandControl;
import com.evolgames.entities.hand.MoveWithRevertHandControl;
import com.evolgames.entities.hand.ParallelHandControl;
import com.evolgames.entities.hand.SwingHandControl;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.PlayerAction;
import com.evolgames.scenes.PlayerSpecialAction;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hand {
    private final GameScene gameScene;
    Stack<HandControl> handControlStack = new Stack<>();
    private int mousePointerId;
    private GameEntity grabbedEntity;
    private MouseJoint mouseJoint;
    private boolean follow;
    private Vector2 target;
    private boolean isTouching = false;

    public Hand(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void grab(GameEntity entity, TouchEvent touchEvent) {
        this.follow = true;
        this.grabbedEntity = entity;
        final MouseJointDef mouseJointDef = new MouseJointDef();
        entity.setHangedPointerId(touchEvent.getPointerID());
        mouseJointDef.dampingRatio = 0.5f;
        mouseJointDef.frequencyHz = 10f;
        mouseJointDef.maxForce = 1000 * entity.getMassOfGroup();
        mouseJointDef.target.set(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
        mouseJointDef.collideConnected = true;
        this.mousePointerId = touchEvent.getPointerID();
        this.gameScene.getWorldFacade().addJointToCreate(mouseJointDef, gameScene.getWorldFacade().getGround().getGameEntityByIndex(0), entity);
        this.grabbedEntity.getBody().setBullet(true);
        this.gameScene.onUsagesUpdated();
    }

    private void holdHand(float degrees) {
        handControlStack.push(new HoldHandControl(grabbedEntity.getBody(), degrees));
    }

    public void moveToSlash(Vector2 target, float angle) {
        grabbedEntity.getBody().setTransform(grabbedEntity.getBody().getPosition(), angle);
        HandControl handControl1 = new SwingHandControl(this, 20);
        MoveWithRevertHandControl handControl2 = new MoveWithRevertHandControl(this, target);
        List<Vector2> path = new ArrayList<>();
        handControl2.setRunnable(()->{
            Vector2 p = this.getGrabbedEntity().getBody().getWorldPoint(new Vector2(0,3f)).cpy();
            path.add(p);
                if(path.size()>=3&& Utils.PointInPolygon(target,path)) {
                    Vector2 u = new Vector2(1, 0);
                    GeometryUtils.rotateVectorDeg(u, (float) (Math.random() * 360));
                    Vector2 point1 = target.cpy().add(0.2f * u.x, 0.2f * u.y);
                    Vector2 point2 = target.cpy().add(-0.2f * u.x, -0.2f * u.y);
                    gameScene.getWorldFacade().performScreenCut(point1, point2, grabbedEntity);
                    path.clear();
                }
        });
        handControlStack.add(new ParallelHandControl(HandAction.MOVE_TO_SLASH, handControl2, handControl1));
    }

    public void onUpdate() {
        if (!handControlStack.isEmpty()) {
            HandControl top = handControlStack.peek();
            if (top.isDead()) {
                handControlStack.pop();
                if (top.getHandAction() == HandAction.MOVE_TO_SLASH ) {

                }
            } else {
                top.run();
            }
        }
    }

    private void releaseGrabbedEntity() {
        Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), mouseJoint);
        if (!handControlStack.isEmpty()) {
            handControlStack.peek().setDead(true);
        }
        if (this.grabbedEntity.getBody() != null) {
            this.grabbedEntity.getBody().setBullet(false);
        }
        this.grabbedEntity = null;
        this.mouseJoint = null;
        this.gameScene.onUsagesUpdated();
    }

    public void onSceneTouchEvent(TouchEvent touchEvent) {
        if (touchEvent.getPointerID() != this.mousePointerId) {
            return;
        }
        if (touchEvent.isActionDown()) {
            this.isTouching = true;
        }
        if (touchEvent.isActionUp()) {
            this.isTouching = false;
        }
        if (gameScene.getPlayerAction() == PlayerAction.Drag) {
            if (mouseJoint != null) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown() && mouseJoint == null) {
                GameEntity entity = gameScene.getTouchedEntity(touchEvent);
                if (entity != null) {
                    grab(entity, touchEvent);
                }
            }
            if (touchEvent.isActionUp() && grabbedEntity != null) {
                releaseGrabbedEntity();
            }
        }
        if (gameScene.getPlayerAction() == PlayerAction.Hold) {
            if (mouseJoint != null) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown()) {
                GameEntity entity = gameScene.getTouchedEntity(touchEvent);

                if (entity != null) {
                    if (gameScene.getSpecialAction() == PlayerSpecialAction.None) {
                        if (grabbedEntity != null) {
                            releaseGrabbedEntity();
                        }
                        grab(entity, touchEvent);
                        holdHand(0);
                    } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Slash) {
                        float distance = mouseJoint.getTarget().dst(touchEvent.getX() / 32f, touchEvent.getY() / 32f);
                        if (grabbedEntity == entity) {
                            releaseGrabbedEntity();
                            grab(entity, touchEvent);
                            holdHand(0);
                        } else {
                            if (grabbedEntity != null && distance < 30f) {
                                Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                Vector2 U = target.cpy().sub(mouseJoint.getTarget());
                                this.target = target.cpy();

                                Vector2 u = U.nor();

                                float angle = (float) Math.atan(u.y / u.x);

                                for (GameEntity gameEntity : entity.getParentGroup().getGameEntities()) {
                                    gameScene.getWorldFacade().addNonCollidingPair(grabbedEntity, gameEntity);
                                }
                                moveToSlash(target, angle);
                            }
                        }
                    }
                }
            }

            if (touchEvent.isActionUp() && grabbedEntity != null) {
                this.follow = false;
            }
        }
    }

    private void moveHand(TouchEvent touchEvent) {
        Vector2 vec = Vector2Pool.obtain(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, (touchEvent.getY()) / PIXEL_TO_METER_RATIO_DEFAULT);
        this.mouseJoint.setTarget(vec);
        Vector2Pool.recycle(vec);
    }

    public MouseJoint getMouseJoint() {
        return mouseJoint;
    }

    public void setMouseJoint(MouseJoint joint) {
        this.mouseJoint = joint;
    }

    public void onMouseJointDestroyed() {
        this.mouseJoint = null;
        this.grabbedEntity = null;
        this.gameScene.onUsagesUpdated();
    }

    public GameEntity getGrabbedEntity() {
        return this.grabbedEntity;
    }


    public boolean isTouching() {
        return isTouching;
    }
}
