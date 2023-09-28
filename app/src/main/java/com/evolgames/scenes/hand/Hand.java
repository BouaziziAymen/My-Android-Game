package com.evolgames.scenes.hand;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import android.util.Log;
import android.util.Pair;

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
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.PlayerAction;
import com.evolgames.scenes.PlayerSpecialAction;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hand {
    public static final int PRECISION = 5;
    private static final float HAND_EXTENT = 2f;
    private final GameScene gameScene;
    Stack<HandControl> handControlStack = new Stack<>();
    private int mousePointerId;
    private GameEntity grabbedEntity;
    private MouseJoint mouseJoint;
    private boolean follow;
    private boolean isTouching = false;
    private Vector2 initialPoint;
    private Vector2 localPoint;

    public Hand(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void grab(GameEntity entity, TouchEvent touchEvent, Vector2 anchor) {
        this.follow = true;
        this.grabbedEntity = entity;
        final MouseJointDef mouseJointDef = new MouseJointDef();
        entity.setHangedPointerId(touchEvent.getPointerID());
        mouseJointDef.dampingRatio = 0.5f;
        mouseJointDef.frequencyHz = 10f;
        mouseJointDef.maxForce = 1000 * entity.getMassOfGroup();
        mouseJointDef.target.set(anchor.x / PIXEL_TO_METER_RATIO_DEFAULT, anchor.y / PIXEL_TO_METER_RATIO_DEFAULT);
        mouseJointDef.collideConnected = true;
        this.mousePointerId = touchEvent.getPointerID();
        this.gameScene.getWorldFacade().addJointToCreate(mouseJointDef, gameScene.getWorldFacade().getGround().getGameEntityByIndex(0), entity);
        this.grabbedEntity.getBody().setBullet(true);
        this.gameScene.onUsagesUpdated();
    }

    private void holdHand() {
        handControlStack.push(new HoldHandControl(grabbedEntity));
    }

    public void moveToSlash(Vector2 target, GameEntity targetEntity) {
        Log.e("stab","---------MOVE TO SLASH-----------");
        this.initialPoint = this.mouseJoint.getTarget().cpy();
        this.localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        this.grabbedEntity.getUsage(Slasher.class).getTargetGameEntities().add(targetEntity);

        final float[] values = new float[6];
        gameScene.getWorldFacade().performScanFlux(target, grabbedEntity, (block, entity, direction, source, point, angle) -> {
            float blockSharpness = block.getProperties().getSharpness();
            float dy = initialPoint.y - point.y;
            if (dy < values[4]) {
                values[4] = dy;
            }
            if (dy > values[5]) {
                values[5] = dy;
            }
            if (blockSharpness > 0.0f) {
                if (point.y > initialPoint.y) {
                    values[2] += blockSharpness;
                    values[3]++;
                }

                if (dy < values[0]) {
                    values[0] = dy;
                }
                if (dy > values[1]) {
                    values[1] = dy;
                }
            }
        }, PRECISION, false);


        float sharpLength = values[1] - values[0];
        float swordLength = values[5] - values[4];
        float sharpness = values[2] / values[3];

        float distance = this.mouseJoint.getTarget().dst(target);
        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        Vector2 handTarget = mouseJoint.getTarget().cpy().add(U.cpy().mul(Math.max(0, distance - sharpLength / 2)));

        if (distance > swordLength + HAND_EXTENT) {
            return;
        }
        HandControl handControl1 = new SwingHandControl(this, (int) (-Math.signum(U.x) * 20));
        MoveWithRevertHandControl handControl2 = new MoveWithRevertHandControl(this, handTarget);
        List<Vector2> path = new ArrayList<>();

        handControl2.setRunnable(() -> {
            Vector2 p = this.getGrabbedEntity().getBody().getWorldPoint(new Vector2(0, sharpLength / 2f)).cpy();
            path.add(p);
            if (path.size() >= 3 && Utils.PointInPolygon(target, path)) {
                Vector2 u = new Vector2(1, 0);
                GeometryUtils.rotateVectorDeg(u, (float) (Math.random() * 360));
                float cutHalfLength = 0.2f * sharpness * sharpLength;
                Vector2 point1 = target.cpy().add(cutHalfLength * u.x, cutHalfLength * u.y);
                Vector2 point2 = target.cpy().add(-cutHalfLength * u.x, -cutHalfLength * u.y);
                if (cutHalfLength > 0.05f) {
                    gameScene.getWorldFacade().performScreenCut(point1, point2, grabbedEntity);
                }
                path.clear();
            }
        });
        handControlStack.add(new ParallelHandControl(HandAction.MOVE_TO_SLASH, handControl2, handControl1));

        for (GameEntity gameEntity : targetEntity.getParentGroup().getGameEntities()) {
            gameScene.getWorldFacade().addNonCollidingPair(grabbedEntity, gameEntity);
        }

    }

    public void onUpdate() {
        List<GameEntity> targetEntities = new ArrayList<>();
        if(grabbedEntity!=null&&grabbedEntity.hasUsage(Stabber.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Stabber.class).getTargetGameEntities());
        }
        if(grabbedEntity!=null&&grabbedEntity.hasUsage(Slasher.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Slasher.class).getTargetGameEntities());
        }
        if (grabbedEntity != null && !targetEntities.isEmpty() && localPoint != null) {
            Vector2 point = grabbedEntity.getBody().getWorldPoint(localPoint).cpy();
            if (point.dst(initialPoint) < 0.01f && handControlStack.peek() instanceof HoldHandControl) {
                if(grabbedEntity.hasUsage(Stabber.class)) {
                    Stabber stabber = grabbedEntity.getUsage(Stabber.class);
                    stabber.getTargetGameEntities().clear();
                    stabber.setActive(false);
                }
                if(grabbedEntity.hasUsage(Slasher.class)) {
                    Slasher slasher = grabbedEntity.getUsage(Slasher.class);
                    slasher.getTargetGameEntities().clear();
                }
                for(GameEntity targetEntity:targetEntities) {
                        gameScene.getWorldFacade().removeNonCollidingPair(grabbedEntity, targetEntity);
                }
            }
        }

        if (!handControlStack.isEmpty()) {
            HandControl top = handControlStack.peek();
            if (top.isDead()) {
                handControlStack.pop();
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
        if(grabbedEntity.hasUsage(Stabber.class)){
            Stabber stabber = grabbedEntity.getUsage(Stabber.class);
            stabber.setActive(false);
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
                Pair<GameEntity, Vector2> touchData = gameScene.getTouchedEntity(touchEvent);
                if (touchData != null) {
                    grab(touchData.first, touchEvent, touchData.second);
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
                Pair<GameEntity, Vector2> touchData = gameScene.getTouchedEntity(touchEvent);
                if (touchData != null) {
                    if (gameScene.getSpecialAction() == PlayerSpecialAction.None) {
                        if (grabbedEntity != null) {
                            releaseGrabbedEntity();
                        }
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Slash) {
                        if (grabbedEntity == touchData.first) {
                            releaseGrabbedEntity();
                            grab(touchData.first, touchEvent, touchData.second);
                            holdHand();
                        } else {
                            if (grabbedEntity != null) {
                                Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                moveToSlash(target, touchData.first);
                            }
                        }
                    } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Stab) {
                        if (grabbedEntity == touchData.first) {
                            releaseGrabbedEntity();
                            grab(touchData.first, touchEvent, touchData.second);
                            holdHand();
                        } else {
                            if (grabbedEntity != null) {
                                Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                moveToStab(target, touchData.first);
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

    private void moveToStab(Vector2 target, GameEntity targetEntity) {
        this.initialPoint = this.mouseJoint.getTarget().cpy();
        this.localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        Vector2 p = this.mouseJoint.getTarget();
        Vector2 u = target.cpy().sub(p).nor();
        grabbedEntity.getUsage(Stabber.class).setActive(true);
        Log.e("stab","---------MOVE TO STAB-----------");
        HandControl handControl = new MoveWithRevertHandControl(this, new Vector2(p.x+u.x*2f,p.y+u.y*2f));

        handControlStack.add(handControl);
    }

    private void moveHand(TouchEvent touchEvent) {
        Vector2 vec = Vector2Pool.obtain(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, (touchEvent.getY()) / PIXEL_TO_METER_RATIO_DEFAULT);
        this.mouseJoint.setTarget(vec);
        Vector2Pool.recycle(vec);
    }

    public MouseJoint getMouseJoint() {
        return mouseJoint;
    }

    public void setMouseJoint(MouseJoint joint, GameEntity gameEntity) {
        this.mouseJoint = joint;
        this.grabbedEntity = gameEntity;
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
