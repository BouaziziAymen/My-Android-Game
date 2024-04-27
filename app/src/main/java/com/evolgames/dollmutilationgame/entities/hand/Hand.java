package com.evolgames.dollmutilationgame.entities.hand;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import android.annotation.SuppressLint;
import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.dollmutilationgame.activity.NativeUIController;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.PlayScene;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.utilities.MathUtils;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;
import com.evolgames.dollmutilationgame.entities.usage.Bow;
import com.evolgames.dollmutilationgame.entities.usage.FlameThrower;
import com.evolgames.dollmutilationgame.entities.usage.Penetrating;
import com.evolgames.dollmutilationgame.entities.usage.Projectile;
import com.evolgames.dollmutilationgame.entities.usage.Rocket;
import com.evolgames.dollmutilationgame.entities.usage.RocketLauncher;
import com.evolgames.dollmutilationgame.entities.usage.Shooter;
import com.evolgames.dollmutilationgame.entities.usage.Slasher;
import com.evolgames.dollmutilationgame.entities.usage.Smasher;
import com.evolgames.dollmutilationgame.entities.usage.Stabber;
import com.evolgames.dollmutilationgame.entities.usage.Throw;
import com.evolgames.gameengine.R;

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
    private transient GameEntity selectedEntity;
    private transient MouseJoint mouseJoint;
    private boolean onAction;

    private MouseJointDef mouseJointDef;
    private int zIndex;


    @SuppressWarnings("unused")
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

    public void grab(GameEntity entity, Vector2 anchor) {
        this.grabbedEntity = entity;
        mouseJointDef = new MouseJointDef();
        mouseJointDef.dampingRatio = 0.5f;
        mouseJointDef.frequencyHz = 5f;
        mouseJointDef.maxForce = 100000;
        mouseJointDef.target.set(anchor.x / PIXEL_TO_METER_RATIO_DEFAULT, anchor.y / PIXEL_TO_METER_RATIO_DEFAULT);
        mouseJointDef.collideConnected = true;

        this.playScene
                .getWorldFacade()
                .addJointToCreate(
                        mouseJointDef, playScene.getWorldFacade().getGround().getGameEntityByIndex(0), entity, -1);
        if(this.grabbedEntity.getBody()!=null) {
            this.grabbedEntity.getBody().setBullet(true);
        }
    }

    public void holdEntity() {
        if(handControlStack.isEmpty()||!(handControlStack.peek() instanceof HoldHandControl)) {
            handControlStack.push(new HoldHandControl(this));
        }
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
                if (Math.abs(grabbedEntity.getBody().getLinearVelocity().x) < 0.1f
                        && !this.handControlStack.isEmpty()
                        && handControlStack.peek() instanceof HoldHandControl) {
                    grabbedEntity.setZIndex(zIndex);
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

    public void releaseGrabbedEntity(boolean updateUsages, boolean deactivateProjectiles) {
        if (grabbedEntity == null || mouseJoint == null) {
            return;
        }
        this.playScene.onGrabbedEntityReleased(updateUsages);
        Invoker.addJointDestructionCommand(grabbedEntity.getParentGroup(), mouseJoint);
        onGrabbedEntityReleased(updateUsages, deactivateProjectiles);
    }

    private void onGrabbedEntityReleased(boolean updateUsages, boolean deactivateProjectiles) {
        if (grabbedEntity.getBody() != null && deactivateProjectiles) {
            grabbedEntity.getBody().setBullet(false);
        }
        grabbedEntity.getUseList().forEach(u -> {
                    if ((deactivateProjectiles && u instanceof Projectile) || u instanceof Stabber) {
                        u.setActive(false);
                    }
                }
        );

        killTopOfStack();
        this.onAction = false;
        this.playScene.unlockSaving();
        this.updateUsagesOnEntityReleased();
        if (updateUsages) {
            this.playScene.onUsagesUpdated();
        }
        this.mouseJoint = null;
        this.mouseJointDef = null;
        this.grabbedEntity = null;
    }

    public void killTopOfStack() {
        if (!handControlStack.isEmpty()) {
            handControlStack.peek().setDead(true);
        }
    }


    public boolean onSceneTouchEvent(TouchEvent touchEvent) {
        if (onAction) {
            return false;
        }
        if (selectedEntity == null) {
            if (this.playScene.getPlayerAction() == PlayerAction.Drag) {
                PlayScene.Selection touchData = this.playScene.getDraggedEntity(touchEvent);
                if (touchEvent.isActionMove()) {
                    if (mouseJoint != null && grabbedEntity != null &&grabbedEntity.isAlive()&&grabbedEntity.isDragged() &&grabbedEntity.getBody() != null) {
                        moveHand(touchEvent);
                    }
                } else if (touchEvent.isActionUp() || touchEvent.isActionOutside() || touchEvent.isActionCancel()) {
                    if (grabbedEntity != null) {
                        this.grabbedEntity.setDragged(false);
                        releaseGrabbedEntity(true, true);
                        playScene.setScrollerEnabled(true);
                        return true;
                    }
                } else if (touchEvent.isActionDown()) {
                    if (touchData != null && touchData.gameEntity != null) {
                        grab(touchData.gameEntity, touchData.anchor);
                        grabbedEntity.setDragged(true);
                        playScene.setScrollerEnabled(false);
                        playScene.setZoomEnabled(false);
                    }
                }
            } else if (this.playScene.getPlayerAction() == PlayerAction.Hold) {
                PlayScene.Selection selection = this.playScene.getHeldEntity(touchEvent);
                if (touchEvent.isActionMove()) {
                    if (grabbedEntity != null && grabbedEntity.isDragged()) {
                        if (mouseJoint != null) {
                            moveHand(touchEvent);
                        }
                    } else {
                        if (grabbedEntity != null) {
                            if (this.playScene.getSpecialAction() == PlayerSpecialAction.FireLight) {
                                if ((this.grabbedEntity.hasUsage(Shooter.class, FlameThrower.class) && (selection == null || selection.gameEntity != grabbedEntity))) {
                                    doAim(touchEvent, grabbedEntity.isMirrored());
                                }
                            }
                            if (this.playScene.getSpecialAction() == PlayerSpecialAction.AimLight) {
                                if (this.grabbedEntity.hasUsage(RocketLauncher.class, Bow.class, Shooter.class, Rocket.class)) {
                                    doAim(touchEvent, grabbedEntity.isMirrored());
                                }
                            }

                        }
                    }
                } else if (touchEvent.isActionUp()) {
                    if(grabbedEntity!=null){
                        this.grabbedEntity.setDragged(false);
                    }
                    playScene.setScrollerEnabled(true);
                    switch (this.playScene.getSpecialAction()) {
                        case None:
                            break;
                        case Trigger:
                            break;
                        case SwitchOn:
                            break;
                        case SwitchOff:
                            break;
                        case Slash:
                            break;
                        case Stab:
                            break;
                        case Throw:
                            break;
                        case Smash:
                            break;
                        case FireLight:
                            if (grabbedEntity != null && grabbedEntity.hasUsage(Shooter.class)) {
                                Shooter shooter = grabbedEntity.getUsage(Shooter.class);
                                shooter.onTriggerReleased();
                            }
                            if (grabbedEntity != null && grabbedEntity.hasUsage(FlameThrower.class)) {
                                FlameThrower flameThrower = this.grabbedEntity.getUsage(FlameThrower.class);
                                if (flameThrower.isOn()) {
                                    flameThrower.onTriggerReleased();
                                }
                            }
                            break;
                        case Guide:
                            break;
                        case Shoot_Arrow:
                            break;
                    }
                } else if (touchEvent.isActionDown()) {
                    if (selection != null && selection.gameEntity != null) {
                        if (selection.gameEntity == grabbedEntity || playScene.getSpecialAction() == PlayerSpecialAction.None) {
                            GameEntity old = this.grabbedEntity;
                            grab(selection.gameEntity, selection.anchor);
                            if (old!= selection.gameEntity) {
                                this.playScene.onUsagesUpdated();
                            }
                            this.grabbedEntity.setDragged(true);
                            holdEntity();
                            onEntityHeld();
                            playScene.setScrollerEnabled(false);
                            return false;
                        }
                    }
                    switch (this.playScene.getSpecialAction()) {
                        case None:
                            break;
                        case Trigger:
                            //Triggers are handled in the level of action
                        case SwitchOn:
                        case SwitchOff:
                            break;
                        case Slash:
                            if (selection != null && selection.gameEntity != grabbedEntity) {
                                Vector2 target =
                                        new Vector2(
                                                touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                                touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                moveToSlash(target, selection.gameEntity);
                            }
                            break;
                        case Stab:
                            if (selection != null && selection.gameEntity != grabbedEntity) {
                                moveToStab();
                            }
                            break;
                        case Throw:
                            Vector2 throwTarget =
                                    new Vector2(
                                            touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                            touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                            moveToThrow(throwTarget);

                            break;
                        case Smash:
                            if (selection != null && selection.gameEntity != grabbedEntity) {
                                Vector2 smashTarget =
                                        new Vector2(
                                                touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                                touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                doSmash(smashTarget);
                            }
                            break;
                        case FireLight:
                            if (grabbedEntity != null) {
                                if (grabbedEntity.hasUsage(Shooter.class)) {
                                    Shooter shooter = grabbedEntity.getUsage(Shooter.class);
                                    Pair<Boolean, Float> loaded = shooter.isLoaded();
                                    if (loaded.first) {
                                        shooter.onTriggerPulled(playScene);
                                    } else {
                                        float reloadTime = loaded.second; // Assuming the second element represents the reload time
                                        @SuppressLint("DefaultLocale")
                                        String reloadHint = String.format(ResourceManager.getInstance().activity.getString(R.string.gun_reloading_warning), reloadTime);
                                        ResourceManager.getInstance().activity.getUiController().showHint(reloadHint, NativeUIController.HintType.WARNING);
                                        ResourceManager.getInstance().gunEmptySound.play();
                                    }
                                }
                                if (grabbedEntity.hasUsage(FlameThrower.class)) {
                                    FlameThrower flameThrower = this.grabbedEntity.getUsage(FlameThrower.class);
                                    if (!flameThrower.isOn()) {
                                        flameThrower.onTriggerPulled(touchEvent.getX(), touchEvent.getY());
                                    }
                                }
                            }
                            break;
                        case Guide:
                            break;
                        case Shoot_Arrow:
                            break;
                    }

                }
            }
        }

        if (this.playScene.getPlayerAction() == PlayerAction.Select) {
            PlayScene.Selection touchData = this.playScene.getSelectedEntity(touchEvent);
            if (touchEvent.isActionMove()) {

            } else if (touchEvent.isActionDown()) {

            } else if (touchEvent.isActionUp()) {
                playScene.setScrollerEnabled(true);
                if (touchData != null && touchData.gameEntity != null) {
                    if (selectedEntity != touchData.gameEntity) {
                        if (selectedEntity != null) {
                            if (!handControlStack.isEmpty()) {
                                killTopOfStack();
                            }
                            deselectDirect(false);
                        }
                        select(touchData.gameEntity);
                    } else {
                        ResourceManager.getInstance().activity.runOnUpdateThread(() -> {
                            deselectDirect(true);
                        });
                        if (!handControlStack.isEmpty()) {
                            killTopOfStack();
                        }
                    }
                    ResourceManager.getInstance().activity.getUiController().resetSelectButton();
                }
            }
        } else {
            //Handle selection actions
            if (selectedEntity != null) {
                if (touchEvent.isActionDown()) {
                    if (playScene.getSpecialAction() == PlayerSpecialAction.FireHeavy) {
                        if (selectedEntity != null && selectedEntity.hasUsage(Shooter.class)) {
                            Shooter shooter = selectedEntity.getUsage(Shooter.class);
                            Pair<Boolean, Float> loaded = shooter.isLoaded();
                            if (loaded.first) {
                                shooter.onTriggerPulled(playScene);
                            } else {
                                float reloadTime = loaded.second; // Assuming the second element represents the reload time
                                @SuppressLint("DefaultLocale")
                                String reloadHint = String.format(ResourceManager.getInstance().activity.getString(R.string.gun_reloading_warning), reloadTime);
                                ResourceManager.getInstance().activity.getUiController().showHint(reloadHint, NativeUIController.HintType.WARNING);
                                ResourceManager.getInstance().gunEmptySound.play();
                            }
                        }
                    }
                }
                if (touchEvent.isActionMove()) {
                    if (selectedEntity.getBody() == null) {
                        return false;
                    }
                    if (playScene.getSpecialAction() == PlayerSpecialAction.FireHeavy || playScene.getSpecialAction() == PlayerSpecialAction.AimHeavy) {
                        if (selectedEntity != null && selectedEntity.hasUsage(Shooter.class)) {
                            playScene.setScrollerEnabled(false);
                            GameEntity muzzleEntity = getHeldEntity();
                            if (muzzleEntity != null && muzzleEntity.getBody() != null) {
                                Vector2 target =
                                        new Vector2(
                                                touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                                                touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
                                Vector2 dir = target.cpy().sub(muzzleEntity.getBody().getPosition());
                                if (dir.len() < 8f) {
                                    Vector2 U = dir.nor();
                                    if (!muzzleEntity.isMirrored()) {
                                        U.mul(-1f);
                                    }
                                    float angle = GeometryUtils.calculateAngleDegrees(U.x, U.y);
                                    setHoldingAngle(angle);
                                }
                            }
                        }
                    }
                }


                if (touchEvent.isActionUp()) {
                    if (playScene.getSpecialAction() == PlayerSpecialAction.FireHeavy) {
                        if (selectedEntity != null && selectedEntity.hasUsage(Shooter.class)) {
                            Shooter shooter = selectedEntity.getUsage(Shooter.class);
                            shooter.onTriggerReleased();
                        }
                    }
                }
            }
        }
        return false;
    }

    public void deselectDirect(boolean updateUsages) {
        if (selectedEntity == null) {
            return;
        }
        this.selectedEntity.hideOutline();
        if (selectedEntity.hasUsage(Shooter.class)) {
            Shooter shooter = selectedEntity.getUsage(Shooter.class);
            shooter.onTriggerReleased();
        }
        this.playScene.onSelectedEntitySpared(selectedEntity);
        this.selectedEntity = null;
        if (updateUsages) {
            this.playScene.onUsagesUpdated();
        }
        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().unselectSound, 1f, 5);
    }

    private void select(GameEntity entity) {
        releaseGrabbedEntity(false, true);
        entity.outlineEntity();
        this.selectedEntity = entity;
        this.playScene.onUsagesUpdated();
        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().selectSound, 1f, 5);
    }

    private void updateUsagesOnEntityReleased() {
        if (this.grabbedEntity == null || this.grabbedEntity.getBody() == null) {
            return;
        }
        if (this.grabbedEntity.hasUsage(Bow.class)) {
            Bow bow = this.grabbedEntity.getUsage(Bow.class);
            bow.destroyArrows(playScene.getWorldFacade());
        }
    }

    private void onEntityHeld() {
        if (this.grabbedEntity == null || this.grabbedEntity.getBody() == null) {
            return;
        }
        if (this.grabbedEntity.hasUsage(Bow.class)) {
            Bow bow = this.grabbedEntity.getUsage(Bow.class);
            if (!bow.isLoaded() && !bow.isLoading()) {
                bow.startReloading();
            }
        }
    }

    private void doAim(TouchEvent touchEvent, boolean isMirrored) {
        playScene.setScrollerEnabled(false);
        if (grabbedEntity.getBody() == null) {
            return;
        }
        Vector2 target =
                new Vector2(
                        touchEvent.getX() / PIXEL_TO_METER_RATIO_DEFAULT,
                        touchEvent.getY() / PIXEL_TO_METER_RATIO_DEFAULT);
        Vector2 dir = target.cpy().sub(grabbedEntity.getBody().getPosition());
        if (dir.len() < 8f) {
            Vector2 U = dir.nor();
            if (!isMirrored) {
                U.mul(-1f);
            }
            float angleDegrees = GeometryUtils.calculateAngleDegrees(U.x, U.y);
            setHoldingAngle(angleDegrees);
        }
    }


    public void launchRocket() {
        Rocket rocket = getUsableEntity().getUsage(Rocket.class);
        releaseGrabbedEntity(true, false);
        rocket.onLaunch(playScene, true);
    }

    public void setHoldingAngle(float angleDeg) {
        this.getHandControlStack().stream()
                .filter(e -> e instanceof HoldHandControl)
                .forEach(e -> ((HoldHandControl) e).setAngle(angleDeg * MathUtils.degreesToRadians));
    }


    private void doSmash(Vector2 target) {
        Smasher smasher = grabbedEntity.getUsage(Smasher.class);
        smasher.setActive(true);
        Vector2 U = target.cpy().sub(mouseJoint.getTarget()).nor();
        SwingHandControl handControl =
                new SwingHandControl(this, (int) (-Math.signum(U.x) * 200), (600));
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
        this.zIndex = grabbedEntity.getZIndex();
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
                            float blockHardness = block.getTenacity();

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

    public void doSlash() {
        Slasher slasher = grabbedEntity.getUsage(Slasher.class);
        slasher.doSlash(this);
    }

    private void moveToThrow(Vector2 touch) {
        if (handControlStack.isEmpty() || !(handControlStack.peek() instanceof HoldHandControl)) {
            return;
        }
        float touchDistance = touch.cpy().sub(mouseJoint.getTarget()).len();
        if (touchDistance > 7f) {
            return;
        }
        Throw throwable = grabbedEntity.getUsage(Throw.class);
        Vector2 u = touch.cpy().sub(mouseJoint.getTarget()).nor();
        float angle = GeometryUtils.calculateAngleDegrees(u.y, -u.x);
        Vector2 initialPoint = this.mouseJoint.getTarget().cpy();
        Vector2 handTarget = initialPoint.cpy().add(u.x * HAND_EXTENT, u.y * HAND_EXTENT);
        HandControl handControl2 = new ThrowHandControl(this, angle);
        HandControl handControl1 = new MoveHandControl(this, handTarget);
        throwable.reset(angle, 100f * touchDistance / 7f);
        ParallelHandControl throwHandControl = new ParallelHandControl(handControl1, handControl2);
        handControlStack.push(throwHandControl);
    }

    public void doThrow() {
        if (grabbedEntity == null || grabbedEntity.getBody() == null) {
            return;
        }
        Throw throwable = grabbedEntity.getUsage(Throw.class);
        throwable.processThrow(this);
        releaseGrabbedEntity(true, false);
        playScene.resetTouchHold();
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
        this.zIndex = grabbedEntity.getZIndex();
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
        this.playScene.unlockSaving();
    }

//    public void onMouseJointDestroyed(MouseJoint mouseJoint) {
//        if (this.onAction) {
//            onGrabbedEntityReleased(true,true);
//            this.onAction = false;
//        } else {
//            if(this.mouseJoint == mouseJoint){
//               this.mouseJoint = null;
//               this.mouseJointDef = null;
//               this.grabbedEntity = null;
//            }
//            this.playScene.onUsagesUpdated();
//            this.playScene.unlockSaving();
//        }
//    }

    public GameEntity getGrabbedEntity() {
        return this.grabbedEntity;
    }

    public void setGrabbedEntity(GameEntity grabbedEntity) {
        this.grabbedEntity = grabbedEntity;
    }


    public void clearStack() {
        this.handControlStack.clear();
    }

    public PlayScene getGameScene() {
        return playScene;
    }

    public GameEntity getUsableEntity() {
        if (selectedEntity != null) {
            return selectedEntity;
        }
        if (playScene.getPlayerAction() == PlayerAction.Hold) {
            return grabbedEntity;
        }
        return null;
    }

    public GameEntity getHeldEntity() {
        if (selectedEntity != null) {
            if (selectedEntity.hasUsage(Shooter.class)) {
                Shooter shooter = selectedEntity.getUsage(Shooter.class);
                return shooter.getMuzzleEntity();
            }
        } else {
            return grabbedEntity;
        }
        return null;
    }

    public boolean hasSelectedEntity() {
        return selectedEntity != null;
    }

    public void inheritSelectedEntity(GameEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
        this.selectedEntity.setOutlined(true);
        playScene.onUsagesUpdated();
    }

    public GameEntity getSelectedEntity() {
        return this.selectedEntity;
    }

    public void setSelectedEntity(GameEntity selectedEntity) {
        if (selectedEntity != null) {
            this.selectedEntity = selectedEntity;
            this.selectedEntity.setOutlined(true);
        }
    }

    public boolean isOnAction() {
        return onAction;
    }
}
