package com.evolgames.scenes;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.AngleHandControl;
import com.evolgames.entities.hand.HandAction;
import com.evolgames.entities.hand.HandControl;
import com.evolgames.entities.hand.HoldHandControl;
import com.evolgames.entities.hand.MoveHandControl;
import com.evolgames.entities.hand.MoveToSlashHandControl;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.entities.hand.ParallelHandControl;
import com.evolgames.entities.hand.SwingHandControl;
import com.evolgames.entities.usage.Penetrating;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.helpers.utilities.GeometryUtils;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hand {
    public static final int PRECISION = 50;
    public static final float STAB_ADVANCE = 3f;
    private static final float HAND_EXTENT = 2f;
    private final GameScene gameScene;
    Stack<HandControl> handControlStack = new Stack<>();
    private int mousePointerId;
    private GameEntity grabbedEntity;
    private MouseJoint mouseJoint;
    private boolean follow;
    private Vector2 localPoint;
    private boolean onAction = false;

    public Hand(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void grab(GameEntity entity, TouchEvent touchEvent, Vector2 anchor) {
        this.follow = true;
        boolean grabbedOtherEntity = this.grabbedEntity!=entity;
        releaseGrabbedEntity(false);
        this.grabbedEntity = entity;
        if(grabbedOtherEntity) {
            this.gameScene.onUsagesUpdated();
        }

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

    }

    private void holdHand() {
        handControlStack.push(new HoldHandControl(grabbedEntity));
    }


    public void onUpdate() {
        List<GameEntity> targetEntities = new ArrayList<>();
        if (grabbedEntity != null && grabbedEntity.hasUsage(Stabber.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Stabber.class).getTargetGameEntities());
        }
        if (grabbedEntity != null && grabbedEntity.hasUsage(Slasher.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Slasher.class).getTargetGameEntities());
        }
        if(onAction) {
            if (grabbedEntity != null && !grabbedEntity.hasActiveUsage(Penetrating.class)) {
                if (grabbedEntity.getBody().getLinearVelocity().len() < 0.1f && !this.handControlStack.isEmpty() && handControlStack.peek() instanceof HoldHandControl) {
                    grabbedEntity.getMesh().setZIndex(0);
                    gameScene.sortChildren();
                    if (grabbedEntity.hasUsage(Stabber.class)) {
                        Stabber stabber = grabbedEntity.getUsage(Stabber.class);
                        stabber.getTargetGameEntities().clear();
                        stabber.setActive(false);
                        grabbedEntity.getBody().setFixedRotation(false);
                        this.onAction = false;
                    }
                    if (grabbedEntity.hasUsage(Slasher.class)) {
                        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
                        slasher.getTargetGameEntities().clear();
                        slasher.setActive(false);
                        this.onAction = false;
                    }
                    for (GameEntity targetEntity : targetEntities) {
                        gameScene.getWorldFacade().removeNonCollidingPair(grabbedEntity, targetEntity);
                    }
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

    private void releaseGrabbedEntity(boolean updateUsages) {
        if(grabbedEntity==null){
            return;
        }
        grabbedEntity.getBody().setBullet(false);
        grabbedEntity.getUseList().forEach(u->u.setActive(false));
        Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), mouseJoint);
        if (!handControlStack.isEmpty()) {
            handControlStack.peek().setDead(true);
        }
        this.grabbedEntity = null;
        this.mouseJoint = null;
        this.onAction = false;
        if(updateUsages){
            gameScene.onUsagesUpdated();
        }
    }

    public void onSceneTouchEvent(TouchEvent touchEvent) {
        if(onAction){
            return;
        }

        if (gameScene.getPlayerAction() == PlayerAction.Drag) {
            if (mouseJoint != null) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown() && mouseJoint == null) {
                Pair<GameEntity, Vector2> touchData = gameScene.getTouchedEntity(touchEvent, false);
                if (touchData != null) {
                    grab(touchData.first, touchEvent, touchData.second);
                }
            }
            if (touchEvent.isActionUp() && grabbedEntity != null) {
                releaseGrabbedEntity(true);
            }
        }
        if (gameScene.getPlayerAction() == PlayerAction.Hold) {
            if (mouseJoint != null) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown()) {
                Pair<GameEntity, Vector2> touchData = gameScene.getTouchedEntity(touchEvent, true);
                if (gameScene.getSpecialAction() == PlayerSpecialAction.None) {
                    if (touchData != null) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    }
                } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Slash) {
                    if (touchData != null) {
                        if (grabbedEntity == touchData.first) {
                            grab(touchData.first, touchEvent, touchData.second);
                            holdHand();
                        } else {
                            if (grabbedEntity != null) {
                                Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                moveToSlash(target, touchData.first);
                            }
                        }
                    }
                } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Stab) {
                    if (touchData != null) {
                        if (grabbedEntity == touchData.first) {
                            grab(touchData.first, touchEvent, touchData.second);
                            holdHand();
                        } else {
                            if (grabbedEntity != null) {
                                moveToStab();
                            }
                        }
                    }
                } else if (gameScene.getSpecialAction() == PlayerSpecialAction.Throw) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else {
                        if (grabbedEntity != null) {
                            Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            doThrow(target);
                        }
                    }
                }  else if (gameScene.getSpecialAction() == PlayerSpecialAction.Smash) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else {
                        if (grabbedEntity != null&& touchData!=null&&touchData.first!=null) {
                            Vector2 target = new Vector2(touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT, touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            doSmash(target);
                        }
                    }


                }
            }

            if (touchEvent.isActionUp() && grabbedEntity != null) {
                this.follow = false;
            }
        }
    }

    public boolean isFollow() {
        return follow;
    }

    private void doSmash(Vector2 target) {
        Smasher smasher = grabbedEntity.getUsage(Smasher.class);
        smasher.setActive(true);

        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        this.localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        SwingHandControl smashHandControl = new SwingHandControl(this, (int) (-Math.signum(U.x) * 30),(0.4f*600));
        handControlStack.add(smashHandControl);
        smasher.reset(smashHandControl);
    }

    public void moveToSlash(Vector2 target, GameEntity targetEntity) {
        if(this.onAction){
            return;
        }
        this.localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
        slasher.setActive(true);
        grabbedEntity.getMesh().setZIndex(20);
        gameScene.sortChildren();
        this.onAction = true;

        final float[] values = new float[]{Float.MAX_VALUE, -Float.MAX_VALUE, 0f, 0f, Float.MAX_VALUE, -Float.MAX_VALUE, 0f};
        gameScene.getWorldFacade().performScanFlux(target, grabbedEntity, (block, entity, direction, source, point, angle) -> {
            Vector2 local = grabbedEntity.getBody().getLocalPoint(point);
            float dy = local.y - localPoint.y;
            if (dy < values[4]) {
                values[4] = dy;
            }
            if (dy > values[5]) {
                values[5] = dy;
            }
            float blockSharpness = block.getProperties().getSharpness();
            float blockHardness = block.getProperties().getTenacity();

            if (local.y > localPoint.y && blockSharpness > 0f) {
                values[6] += blockHardness;
                values[2] += blockSharpness;
                values[3]++;
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
        float hardness = values[6] / values[3];
        if (Float.isInfinite(sharpLength)) {
            return;
        }

        float distance = this.mouseJoint.getTarget().dst(target);
        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        Vector2 handTarget = mouseJoint.getTarget().cpy().add(U.cpy().mul(Math.max(0, distance - sharpLength / 2)));

        if (distance > swordLength + HAND_EXTENT) {
            return;
        }
        HandControl handControl1 = new SwingHandControl(this, (int) (-Math.signum(U.x) * 20),(0.3f*600));
        MoveToSlashHandControl handControl2 = new MoveToSlashHandControl(this, handTarget, this.localPoint);
        List<Vector2> path = new ArrayList<>();
        handControl2.setRunnable(() -> slasher.doSlash(gameScene, this, targetEntity, path, target, sharpLength, sharpness, hardness));

        handControlStack.add(new ParallelHandControl(HandAction.MOVE_TO_SLASH, handControl2, handControl1));
    }

    private void doThrow(Vector2 touch) {
        if (!(handControlStack.peek() instanceof HoldHandControl)) {
            return;
        }
        float touchDistance = touch.cpy().sub(mouseJoint.getTarget()).len();
        if(touchDistance>5f){
            return;
        }
        Throw throwable = grabbedEntity.getUsage(Throw.class);
        Vector2 U = touch.cpy().sub(mouseJoint.getTarget()).nor();
        float angle = GeometryUtils.calculateAngle(U.y, -U.x);
        Vector2 initialPoint = this.mouseJoint.getTarget().cpy();
        Vector2 handTarget = initialPoint.cpy().add(U.x * HAND_EXTENT, U.y * HAND_EXTENT);
        Body body = this.grabbedEntity.getBody();
        this.localPoint = body.getLocalPoint(mouseJoint.getTarget()).cpy();
        HandControl handControl2 = new AngleHandControl(this, angle);
        handControl2.setRunnable(() -> throwable.processThrow(gameScene, this, angle, touchDistance/5f));
        HandControl handControl1 = new MoveHandControl(this, handTarget);
        ParallelHandControl throwHandControl = new ParallelHandControl(HandAction.MOVE_TO_THROW, handControl1, handControl2);
        handControlStack.push(throwHandControl);
    }

    private void moveToStab() {
        if (!(handControlStack.peek() instanceof HoldHandControl)||onAction) {
            return;
        }
        this.onAction = true;
        Vector2 p = this.mouseJoint.getTarget();
        float grabbedEntityAngle = grabbedEntity.getBody().getAngle();
        Vector2 v = new Vector2(0, 1);
        GeometryUtils.rotateVectorRad(v, grabbedEntityAngle);
        this.localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        grabbedEntity.getMesh().setZIndex(-20);
        gameScene.sortChildren();
        MoveToStabHandControl handControl = new MoveToStabHandControl(this, new Vector2(p.x + v.x * STAB_ADVANCE, p.y + v.y * STAB_ADVANCE), this.localPoint);
        grabbedEntity.getUsage(Stabber.class).setActive(true);
        grabbedEntity.getUsage(Stabber.class).reset(localPoint, handControl);
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
        this.onAction = false;
    }

    public GameEntity getGrabbedEntity() {
        return this.grabbedEntity;
    }


    public int getMousePointerId() {
        return mousePointerId;
    }

    public void clearStack() {
        this.handControlStack.clear();
    }

    public void setForceFactor(float forceFactor) {
        this.mouseJoint.setMaxForce(grabbedEntity.getMassOfGroup()*forceFactor);
    }
}
