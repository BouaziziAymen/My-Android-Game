package com.evolgames.entities.ragdoll;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.basics.SpecialEntityType;
import com.evolgames.scenes.PhysicsScene;

import java.util.ArrayDeque;
import java.util.HashSet;

public class RagDoll extends GameGroup {

    transient private final PhysicsScene<?> scene;
    transient public GameEntity head;
    transient public GameEntity upperTorso;
    transient public GameEntity upperArmR;
    transient public GameEntity upperLegR;
    transient public GameEntity lowerLegR;
    transient public GameEntity leftFoot;
    transient public GameEntity rightFoot;
    transient GameEntity lowerTorso;
    transient GameEntity lowerArmR;
    transient GameEntity upperArmL;
    transient GameEntity lowerArmL;
    transient GameEntity upperLegL;
    transient GameEntity lowerLegL;
    transient GameEntity rightHand;
    transient GameEntity leftHand;
    boolean alive = true;
    transient Balancer[] balancers = new Balancer[8];
    private boolean leftLegReadyToStand;
    private boolean rightLegReadyToStand;

    public RagDoll(PhysicsScene<?> scene, GameEntity... entities) {
        super(GroupType.DOLL, entities);
        this.scene = scene;
    }


    private void turnRightFoot(float torque) {
        rightFoot.getBody().applyTorque(torque);
    }

    private void onUpdate(float pSecondsElapsed) {
        if (head == null || head.getBody() == null || !head.isAlive()) {
            return;
        }

        detectGround();
        this.performAction();
    }

    private void performAction() {
        if (leftLegReadyToStand && rightLegReadyToStand)
            for (Balancer balancer : balancers)
                if (balancer.getEntity() != null && ((GameEntity) balancer.getEntity()).isAlive())
                    balancer.equilibrate();
    }

    @Override
    public void onStep(float timeStep) {
        super.onStep(timeStep);
        findBodyParts();
        onUpdate(timeStep);
    }

    private void detectGround() {

        if (leftFoot == null) {
            leftLegReadyToStand = false;
        }
        if (rightFoot == null) {
            rightLegReadyToStand = false;
        }

        if (leftFoot != null && leftFoot.getBody() != null) {
            Body leftFootBody = leftFoot.getBody();
            Vector2 tip1 = leftFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            Vector2 projection1 =
                    scene
                            .getWorldFacade()
                            .detectFirstIntersectionPointWithExceptions(
                                    tip1, tip1.cpy().add(0, -10f), getGameEntities());
            if (projection1 != null) {
                leftLegReadyToStand = tip1.dst(projection1) < 3f;
            }
        }
        if (rightFoot != null && rightFoot.getBody() != null) {
            Body rightFootBody = rightFoot.getBody();
            Vector2 tip2 = rightFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            Vector2 projection2 =
                    scene
                            .getWorldFacade()
                            .detectFirstIntersectionPointWithExceptions(
                                    tip2, tip2.cpy().add(0, -10f), getGameEntities());
            if (projection2 != null) {
                rightLegReadyToStand = tip2.dst(projection2) < 3f;
            }
        }
    }

    public void setHead(GameEntity splinter) {
        head = splinter;
        if (balancers[0] == null) {
            balancers[0] = new Balancer(head, (float) (Math.PI / 2), 0);
        }
    }

    public void findBodyParts() {
        HashSet<GameEntity> entities = new HashSet<>();
        ArrayDeque<GameEntity> deque = new ArrayDeque<>();
        if (this.head == null) {
            return;
        }
        deque.push(head);
        while (!deque.isEmpty()) {
            GameEntity current = deque.pop();

            if (current.getBody() != null && current.isAlive()) {
                entities.add(current);
                for (JointEdge edge : current.getBody().getJointList()) {
                    GameEntity entity = (GameEntity) edge.other.getUserData();
                    if (entity != null) if (!entities.contains(entity)) deque.push(entity);
                }
            }
        }
        alive = false;
        for (GameEntity e : entities) {
            if (e.getType() == SpecialEntityType.UpperTorso) {
                alive = true;
                break;
            }
        }
        lowerTorso = null;
        upperTorso = null;
        upperLegR = null;
        upperLegL = null;
        lowerLegR = null;
        lowerLegL = null;
        rightFoot = null;
        leftFoot = null;
        upperArmR = null;
        upperArmL = null;
        lowerArmR = null;
        lowerArmL = null;
        rightHand = null;
        leftHand = null;

        for (GameEntity e : entities) {
            switch (e.getType()) {
                case Default:
                    break;
                case Head:
                    break;
                case UpperTorso:
                    upperTorso = e;
                    if (balancers[1] == null) {
                        balancers[1] = new Balancer(upperTorso, (float) (Math.PI / 4), 0);
                    }
                    break;
                case LowerTorso:
                    lowerTorso = e;
                    break;
                case UpperArmRight:
                    upperArmR = e;
                    break;
                case UpperArmLeft:
                    upperArmL = e;
                    break;
                case LowerArmRight:
                    lowerArmR = e;
                    break;
                case LowerArmLeft:
                    lowerArmL = e;
                    break;
                case RightHand:
                    rightHand = e;
                    break;
                case LeftHand:
                    leftHand = e;
                    break;
                case UpperLegRight:
                    upperLegR = e;
                    if (balancers[2] == null) {
                        balancers[2] = new Balancer(upperLegR, (float) (Math.PI / 10), 0);
                    }
                    break;
                case UpperLegLeft:
                    upperLegL = e;
                    if (balancers[4] == null) {
                        balancers[4] = new Balancer(upperLegL, (float) (Math.PI / 10), 0);
                    }
                    break;
                case LowerLegRight:
                    lowerLegR = e;
                    if (balancers[3] == null) {
                        balancers[3] = new Balancer(lowerLegR, (float) (Math.PI / 10), 0);
                    }
                    break;
                case LowerLegLeft:
                    lowerLegL = e;
                    if (balancers[5] == null) {
                        balancers[5] = new Balancer(lowerLegL, (float) (Math.PI / 10), 0);
                    }
                    break;
                case RightFoot:
                    rightFoot = e;
                    if (balancers[6] == null) {
                        balancers[6] = new Balancer(rightFoot, 0, 0);
                    }
                    break;
                case LeftFoot:
                    leftFoot = e;
                    if (balancers[7] == null) {
                        balancers[7] = new Balancer(leftFoot, 0, 0);
                    }
                    break;
            }
        }
    }

}
