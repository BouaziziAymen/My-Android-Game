package com.evolgames.scenes.entities;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blockvisitors.utilities.GeometryUtils;
import com.evolgames.entities.blockvisitors.utilities.MathUtils;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.HandControl;
import com.evolgames.entities.hand.HoldHandControl;
import com.evolgames.entities.hand.MoveHandControl;
import com.evolgames.entities.hand.MoveToSlashHandControl;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.entities.hand.ParallelHandControl;
import com.evolgames.entities.hand.SwingHandControl;
import com.evolgames.entities.hand.ThrowHandControl;
import com.evolgames.entities.usage.FlameThrower;
import com.evolgames.entities.usage.LiquidContainer;
import com.evolgames.entities.usage.Penetrating;
import com.evolgames.entities.usage.Rocket;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.scenes.PlayScene;

import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Hand {

    public static final int PRECISION = 50;
    public static final float STAB_ADVANCE = 3f;
    public static final float HAND_EXTENT = 2f;
    Stack<HandControl> handControlStack = new Stack<>();
    private transient PlayScene playScene;
    private transient GameEntity grabbedEntity;
    private transient MouseJoint mouseJoint;
    private int mousePointerId;
    private boolean follow;
    private boolean holding;
    private boolean onAction;

    private MouseJointDef mouseJointDef;
    private boolean dragging;

    public Hand() {
    }

    public Hand(PlayScene playScene) {
        this.playScene = playScene;
    }

    public void setPlayScene(PlayScene playScene) {
        this.playScene = playScene;
    }

    public Stack<HandControl> getHandControlStack() {
        return handControlStack;
    }

    public void grab(GameEntity entity, TouchEvent touchEvent, Vector2 anchor) {
        this.follow = true;
        boolean grabbedOtherEntity = this.grabbedEntity != entity;
        releaseGrabbedEntity(false);
        this.grabbedEntity = entity;
        if (grabbedOtherEntity) {
            this.playScene.onUsagesUpdated();
        }

        mouseJointDef = new MouseJointDef();
        entity.setHangedPointerId(touchEvent.getPointerID());
        mouseJointDef.dampingRatio = 0.5f;
        mouseJointDef.frequencyHz = 10f;
        mouseJointDef.maxForce = 1000 * entity.getMassOfGroup();
        mouseJointDef.target.set(
                anchor.x / PIXEL_TO_METER_RATIO_DEFAULT, anchor.y / PIXEL_TO_METER_RATIO_DEFAULT);
        mouseJointDef.collideConnected = true;
        this.mousePointerId = touchEvent.getPointerID();

        this.playScene
                .getWorldFacade()
                .addJointToCreate(
                        mouseJointDef, playScene.getWorldFacade().getGround().getGameEntityByIndex(0), entity);
        this.grabbedEntity.getBody().setBullet(true);
        this.holding = playScene.getPlayerAction() == PlayerAction.Hold;
    }

    public boolean isHolding() {
        return holding;
    }

    private void holdHand() {
        handControlStack.push(new HoldHandControl(this));
    }

    public void onUpdate() {
        List<GameEntity> targetEntities = new ArrayList<>();
        if (grabbedEntity != null && grabbedEntity.hasUsage(Stabber.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Stabber.class).getTargetGameEntities());
        }
        if (grabbedEntity != null && grabbedEntity.hasUsage(Slasher.class)) {
            targetEntities.addAll(grabbedEntity.getUsage(Slasher.class).getTargetGameEntities());
        }
        if (onAction) {
            if (grabbedEntity != null && !grabbedEntity.hasActiveUsage(Penetrating.class)) {
                if (grabbedEntity.getBody().getLinearVelocity().len() < 0.1f
                        && !this.handControlStack.isEmpty()
                        && handControlStack.peek() instanceof HoldHandControl) {
                    grabbedEntity.setZIndex(0);
                    playScene.sortChildren();
                    if (grabbedEntity.hasUsage(Stabber.class)) {
                        Stabber stabber = grabbedEntity.getUsage(Stabber.class);
                        stabber.getTargetGameEntities().clear();
                        stabber.setActive(false);
                        grabbedEntity.getBody().setFixedRotation(false);
                        this.onAction = false;
                        this.playScene.unlockSaving();
                    }
                    if (grabbedEntity.hasUsage(Slasher.class)) {
                        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
                        slasher.getTargetGameEntities().clear();
                        slasher.setActive(false);
                        this.onAction = false;
                        this.playScene.unlockSaving();
                    }
                    for (GameEntity targetEntity : targetEntities) {
                        playScene.getWorldFacade().removeNonCollidingPair(grabbedEntity, targetEntity);
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
        if (grabbedEntity == null) {
            return;
        }
        if (grabbedEntity.getBody() != null) {
            grabbedEntity.getBody().setBullet(false);
        }
        grabbedEntity.getUseList().forEach(u -> u.setActive(false));
        Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), mouseJoint);
        if (!handControlStack.isEmpty()) {
            handControlStack.peek().setDead(true);
        }
        this.grabbedEntity = null;
        this.mouseJoint = null;
        this.onAction = false;
        this.playScene.unlockSaving();
        if (updateUsages) {
            playScene.onUsagesUpdated();
        }
    }

    public boolean onSceneTouchEvent(TouchEvent touchEvent) {
        if (this.playScene.getPlayerAction() == PlayerAction.Drag) {
            if (mouseJoint != null && touchEvent.isActionMove()) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown()) {
                Pair<GameEntity, Vector2> touchData = this.playScene.getTouchedEntity(touchEvent, false);
                if (touchData != null) {
                    grab(touchData.first, touchEvent, touchData.second);
                    this.dragging = true;
                }
            }
            if (touchEvent.isActionUp()) {
                if (grabbedEntity != null) {
                    releaseGrabbedEntity(true);
                    this.dragging = false;
                    return true;
                } else {
                    if (this.dragging) {
                        throw new RuntimeException("Issue release entity");
                    }
                }
            }
        }
        if (this.playScene.getPlayerAction() == PlayerAction.Hold) {
            if (mouseJoint != null) {
                if (touchEvent.isActionMove() && follow) {
                    moveHand(touchEvent);
                }
            }
            if (touchEvent.isActionDown()) {
                Pair<GameEntity, Vector2> touchData = this.playScene.getTouchedEntity(touchEvent, true);
                if (this.playScene.getSpecialAction() == PlayerSpecialAction.None) {
                    if (touchData != null) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Slash) {
                    if (touchData != null) {
                        if (grabbedEntity == touchData.first) {
                            grab(touchData.first, touchEvent, touchData.second);
                            holdHand();
                        } else {
                            if (grabbedEntity != null) {
                                Vector2 target =
                                        new Vector2(
                                                touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                                touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                moveToSlash(target, touchData.first);
                            }
                        }
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Stab) {
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
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Throw) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else {
                        if (grabbedEntity != null) {
                            Vector2 target =
                                    new Vector2(
                                            touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                            touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            moveToThrow(target);
                        }
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Smash) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else {
                        if (grabbedEntity != null && touchData != null && touchData.first != null) {
                            Vector2 target =
                                    new Vector2(
                                            touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                            touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            doSmash(target);
                        }
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Shoot) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        Shooter shooter = grabbedEntity.getUsage(Shooter.class);

                        if (!shooter.isLoaded()) {
                            shooter.startReload();
                            setHoldingAngle(0);
                        } else {
                            doShoot();
                        }
                    } else {
                        if (grabbedEntity != null) {
                            if (touchData != null) {
                                // doShoot();
                            }
                        }
                    }
                }
                else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Pouring||this.playScene.getSpecialAction() == PlayerSpecialAction.Sealed) {
                  if (touchData != null && grabbedEntity == touchData.first) {
                    LiquidContainer liquidContainer = this.grabbedEntity.getUsage(LiquidContainer.class);
                   if(this.playScene.getSpecialAction() == PlayerSpecialAction.Pouring){
                     liquidContainer.open();
                   } else {
                     liquidContainer.close();
                   }
                    playScene.onUsagesUpdated();
                  }
                }
                else if (this.playScene.getSpecialAction() == PlayerSpecialAction.ThrowFire) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        FlameThrower flameThrower = this.grabbedEntity.getUsage(FlameThrower.class);
                        if (!flameThrower.isOn()) {
                            flameThrower.onTriggerPulled();
                        } else {
                            flameThrower.onTriggerReleased();
                        }
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Rocket || this.playScene.getSpecialAction() == PlayerSpecialAction.Missile) {
                    if (touchData != null && grabbedEntity == touchData.first) {
                        grab(touchData.first, touchEvent, touchData.second);
                        holdHand();
                    } else {
                        if (grabbedEntity != null) {
                            Vector2 target =
                                    new Vector2(
                                            touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                            touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            Rocket rocket = this.grabbedEntity.getUsage(Rocket.class);
                            if (!rocket.isOn()) {
                                launchRocket(target);
                                Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), this.getMouseJoint());
                            }
                        }
                    }
                } else if (this.playScene.getSpecialAction() == PlayerSpecialAction.Grenade) {
                    if (grabbedEntity != null) {
                        TimeBomb timeBomb = grabbedEntity.getUsage(TimeBomb.class);
                        timeBomb.onTriggerReleased();
                        releaseGrabbedEntity(true);
                    }
                }
            } else if (touchEvent.isActionUp() && grabbedEntity != null) {
                this.follow = false;
            } else if (touchEvent.isActionMove() && grabbedEntity != null) {
                Pair<GameEntity, Vector2> touchData = this.playScene.getTouchedEntity(touchEvent, true);
                if (this.playScene.getSpecialAction() == PlayerSpecialAction.Shoot) {
                    if (touchData == null || touchData.first != grabbedEntity) {
                        Shooter shooter = this.grabbedEntity.getUsage(Shooter.class);
                        Vector2 target =
                                new Vector2(
                                        touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                        touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                        Vector2 dir = target.cpy().sub(grabbedEntity.getBody().getPosition());
                        if (dir.len() > 1f && dir.len() < 5f && !shooter.isLoading()) {
                            Vector2 U = target.cpy().sub(grabbedEntity.getBody().getPosition()).nor();
                            float angle = GeometryUtils.calculateAngle(U.x, U.y);
                            setHoldingAngle(angle);
                        }
                    }
                }
                if (this.playScene.getSpecialAction() == PlayerSpecialAction.ThrowFire) {
                    if (touchData == null || touchData.first != grabbedEntity) {
                        Vector2 target =
                                new Vector2(
                                        touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                        touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                        Vector2 dir = target.cpy().sub(grabbedEntity.getBody().getPosition());
                        if (dir.len() > 1f && dir.len() < 5f) {
                            Vector2 U = target.cpy().sub(grabbedEntity.getBody().getPosition()).nor();
                            float angle = GeometryUtils.calculateAngle(U.x, U.y);
                            setHoldingAngle(angle);
                        }
                    }
                }
            }
        }
        return false;
    }

    private void launchRocket(Vector2 touch) {
        if (!(handControlStack.peek() instanceof HoldHandControl)) {
            return;
        }
        float touchDistance = touch.cpy().sub(mouseJoint.getTarget()).len();
        if (touchDistance > 20f) {
            return;
        }
        Rocket rocket = grabbedEntity.getUsage(Rocket.class);
        Vector2 u = touch.cpy().sub(mouseJoint.getTarget()).nor();
        float angle = (float) Math.atan2(-u.x, u.y);
        rocket.onLaunch(angle);
    }

    public void setHoldingAngle(float angleDeg) {
        this.getHandControlStack().stream()
                .filter(e -> e instanceof HoldHandControl)
                .forEach(e -> ((HoldHandControl) e).setAngle(angleDeg * MathUtils.degreesToRadians));
    }

    private void doShoot() {
        Shooter shooter = this.grabbedEntity.getUsage(Shooter.class);
        if (shooter.isLoaded()) {
            shooter.onTriggerPulled();
        }
    }

    public boolean isFollow() {
        return follow;
    }

    private void doSmash(Vector2 target) {
        Smasher smasher = grabbedEntity.getUsage(Smasher.class);
        smasher.setActive(true);
        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        SwingHandControl handControl =
                new SwingHandControl(this, (int) (-Math.signum(U.x) * 30), (0.4f * 600));
        smasher.setHand(this);
        handControlStack.add(handControl);
    }

    public void moveToSlash(Vector2 target, GameEntity targetEntity) {
        if (this.onAction) {
            return;
        }
        Vector2 localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
        slasher.setActive(true);
        grabbedEntity.setZIndex(20);
        this.playScene.sortChildren();
        this.onAction = true;

        final float[] values =
                new float[]{
                        Float.MAX_VALUE, -Float.MAX_VALUE, 0f, 0f, Float.MAX_VALUE, -Float.MAX_VALUE, 0f
                };
        this.playScene
                .getWorldFacade()
                .performScanFlux(
                        target,
                        grabbedEntity,
                        (block, entity, direction, source, point, angle) -> {
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
                        },
                        PRECISION,
                        false);
        float sharpLength = values[1] - values[0];
        float swordLength = values[5] - values[4];
        float sharpness = values[2] / values[3];
        float hardness = values[6] / values[3];
        if (Float.isInfinite(sharpLength)) {
            return;
        }

        float distance = this.mouseJoint.getTarget().dst(target);
        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        Vector2 handTarget =
                mouseJoint.getTarget().cpy().add(U.cpy().mul(Math.max(0, distance - sharpLength / 2)));

        if (distance > swordLength + HAND_EXTENT) {
            return;
        }
        HandControl handControl1 =
                new SwingHandControl(this, (int) (-Math.signum(U.x) * 20), (0.3f * 600));
        MoveToSlashHandControl handControl2 = new MoveToSlashHandControl(this, handTarget, localPoint);
        slasher.reset(targetEntity, target, sharpLength, sharpness, hardness);
        handControlStack.add(new ParallelHandControl(handControl2, handControl1));
    }

    public void doSash() {
        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
        slasher.doSlash(this);
    }

    private void moveToThrow(Vector2 touch) {
        if (!(handControlStack.peek() instanceof HoldHandControl)) {
            return;
        }
        float touchDistance = touch.cpy().sub(mouseJoint.getTarget()).len();
        if (touchDistance > 5f) {
            return;
        }
        Throw throwable = grabbedEntity.getUsage(Throw.class);
        Vector2 u = touch.cpy().sub(mouseJoint.getTarget()).nor();
        float angle = GeometryUtils.calculateAngle(u.y, -u.x);
        Vector2 initialPoint = this.mouseJoint.getTarget().cpy();
        Vector2 handTarget = initialPoint.cpy().add(u.x * HAND_EXTENT, u.y * HAND_EXTENT);
        HandControl handControl2 = new ThrowHandControl(this, angle);
        HandControl handControl1 = new MoveHandControl(this, handTarget);
        throwable.reset(angle, 100f * touchDistance / 5f);
        ParallelHandControl throwHandControl = new ParallelHandControl(handControl1, handControl2);
        handControlStack.push(throwHandControl);
    }

    public void doThrow() {
        Throw throwable = grabbedEntity.getUsage(Throw.class);
        throwable.processThrow(this);
    }

    private void moveToStab() {
        if (!(handControlStack.peek() instanceof HoldHandControl) || onAction) {
            return;
        }
        this.playScene.lockSaving();
        this.onAction = true;
        Vector2 p = this.mouseJoint.getTarget();
        float grabbedEntityAngle = grabbedEntity.getBody().getAngle();
        Vector2 v = new Vector2(0, 1);
        GeometryUtils.rotateVectorRad(v, grabbedEntityAngle);
        Vector2 localPoint = this.grabbedEntity.getBody().getLocalPoint(mouseJoint.getTarget()).cpy();
        grabbedEntity.setZIndex(-20);
        this.playScene.sortChildren();
        MoveToStabHandControl handControl =
                new MoveToStabHandControl(
                        this, new Vector2(p.x + v.x * STAB_ADVANCE, p.y + v.y * STAB_ADVANCE), localPoint);
        Stabber stabber = grabbedEntity.getUsage(Stabber.class);
        stabber.setActive(true);
        stabber.setHand(this);
        stabber.setHandLocalPosition(localPoint);
        handControlStack.add(handControl);
    }

    private void moveHand(TouchEvent touchEvent) {
        Vector2 vec =
                Vector2Pool.obtain(
                        touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                        (touchEvent.getY()) / PIXEL_TO_METER_RATIO_DEFAULT);
        updateTarget(vec);
        Vector2Pool.recycle(vec);
    }

    public void updateTarget(Vector2 vec) {
        this.mouseJoint.setTarget(vec);
        this.mouseJointDef.target.set(vec);
    }

    public MouseJoint getMouseJoint() {
        return mouseJoint;
    }

    public void setMouseJoint(MouseJoint joint, MouseJointDef jointDef, GameEntity gameEntity) {
        this.mouseJoint = joint;
        this.mouseJointDef = jointDef;
        this.grabbedEntity = gameEntity;
        this.playScene.onUsagesUpdated();
    }

    public void onMouseJointDestroyed() {
        this.mouseJoint = null;
        this.mouseJointDef = null;
        this.grabbedEntity = null;
        this.playScene.onUsagesUpdated();
        this.onAction = false;
        this.playScene.unlockSaving();
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
        this.mouseJoint.setMaxForce(grabbedEntity.getMassOfGroup() * forceFactor);
    }

    public PlayScene getGameScene() {
        return playScene;
    }

    public boolean isDragging() {
        return dragging;
    }
}
