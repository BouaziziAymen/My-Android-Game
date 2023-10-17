package com.evolgames.entities.ragdoll;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.physics.entities.callbacks.SimpleDetectionRayCastCallback;
import com.evolgames.scenes.GameScene;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import java.util.ArrayDeque;
import java.util.HashSet;

public class Ragdoll extends GameGroup {

    private final Grafcet brain;
    private final GameScene gameScene;
    public GameEntity head;
    public GameEntity upperTorso;
    public GameEntity upperArmR;
    public GameEntity upperLegR;
    public GameEntity lowerLegR;
    GameEntity lowerTorso;
    GameEntity lowerArmR;
    GameEntity upperArmL;
    GameEntity lowerArmL;
    GameEntity upperLegL;
    GameEntity lowerLegL;
    GameEntity rightHand;
    GameEntity leftHand;
   public GameEntity leftFoot;
    public GameEntity rightFoot;
    boolean alive = true;
    Balancer[] balancers = new Balancer[6];
    private boolean leftLegReadyToStand;
    private boolean rightLegReadyToStand;

    public Ragdoll(GameScene gameScene, GameEntity... entities) {
        super(true,entities);
        this.gameScene = gameScene;
        this.brain = new Grafcet
                (new int[][]{{0, 0}, {0, 0}, {20000}},
                        new int[][]{{10, -10}, {10, 10}, {10}},

                        new int[][]{{0, 1}, {2, 3}, {4}}, new int[]{2, 2, 1}, 3);


    }

    public void setBodyParts(GameEntity head, GameEntity
            upperTorso,
                             GameEntity upperArmR, GameEntity lowerArmR,
                             GameEntity upperArmL, GameEntity lowerArmL,
                             GameEntity upperLegR, GameEntity lowerLegR,
                             GameEntity upperLegL, GameEntity lowerLegL,
                             GameEntity rightHand, GameEntity leftHand,
                             GameEntity leftFoot, GameEntity rightFoot) {


        head.setType(SpecialEntityType.Head);
        upperTorso.setType(SpecialEntityType.UpperTorso);
        upperArmR.setType(SpecialEntityType.UpperArmRight);
        lowerArmR.setType(SpecialEntityType.LowerArmRight);
        upperArmL.setType(SpecialEntityType.UpperArmLeft);
        lowerArmL.setType(SpecialEntityType.LowerArmLeft);
        upperLegR.setType(SpecialEntityType.UpperLegRight);
        lowerLegR.setType(SpecialEntityType.LowerLegRight);
        upperLegL.setType(SpecialEntityType.UpperLegLeft);
        lowerLegL.setType(SpecialEntityType.LowerLegLeft);


        rightHand.setType(SpecialEntityType.RightHand);
        leftHand.setType(SpecialEntityType.LeftHand);
        leftFoot.setType(SpecialEntityType.LeftFoot);
        rightFoot.setType(SpecialEntityType.RightFoot);
        this.head = head;

        upperTorso.getMesh().setZIndex(3);
        upperLegR.getMesh().setZIndex(2);
        upperLegL.getMesh().setZIndex(2);
        lowerLegR.getMesh().setZIndex(1);
        lowerLegL.getMesh().setZIndex(1);
        this.gameScene.sortChildren();

        balancers[0] = new Balancer(head, (float) (Math.PI / 2), 0);
        balancers[1] = new Balancer(upperTorso, (float) (Math.PI / 4), 0);
        balancers[2] = new Balancer(upperLegR, (float) (Math.PI / 4), 0);
        balancers[3] = new Balancer(lowerLegR, (float) (Math.PI / 6), 0);
        balancers[4] = new Balancer(upperLegL, (float) (Math.PI / 4), 0);
        balancers[5] = new Balancer(lowerLegL, (float) (Math.PI / 6), 0);

    }

    private void turnRightFoot(float torque) {
        rightFoot.getBody().applyTorque(torque);
    }

    public void onUpdate(float pSecondsElapsed) {
        if (head.getBody() == null || !head.isAlive()){
            return;
        }

        detectGround();

        this.brain.run();


        for (int i = 0; i < this.brain.numStat[this.brain.current]; i++) {
            this.performAction(this.brain.actions[this.brain.current][i], this.brain.speeds[this.brain.current][i]);
        }


    }

    private void performAction(int action, int amplitude) {


        if (leftLegReadyToStand && rightLegReadyToStand)
            for (Balancer balancer : balancers)
                if (balancer.getEntity() != null && ((GameEntity) balancer.getEntity()).isAlive())
                    balancer.equilibrate();

    }

    @Override
    public void onStep(float timeStep) {
        super.onStep(timeStep);
        findBodyParts();
    }

    public String toString() {
        String result;
        result = "uarml" + upperArmL + "|uarmr" + upperArmR + "|larml" + lowerArmL + "|larmr" + lowerArmR + "|head" + head + "|utorso" +
                upperTorso + "|ltorso" + lowerTorso + "|ulegl" + upperLegL + "|ulegr" + upperLegR + "|llegl" + lowerLegL + "llegr" + lowerLegR + "|fl" + leftFoot + "|fr"
                + rightFoot;
        return "{" + result + "}";

    }

    private void detectGround() {

        if (leftFoot == null) leftLegReadyToStand = false;
        if (rightFoot == null) rightLegReadyToStand = false;


        GameScene.plotter.detachChildren();
        if (leftFoot != null&&leftFoot.getBody()!=null) {
            Body leftFootBody = leftFoot.getBody();
            Vector2 tip1 = leftFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            GameScene.plotter.drawPoint(tip1, Color.PINK, 1, 0);
            Vector2 projection1 = gameScene.getWorldFacade().detectFirstIntersectionPointWithExceptions(tip1, tip1.cpy().add(0, -10f), getGameEntities());
            if (projection1 != null) {
                leftLegReadyToStand = tip1.dst(projection1) < 1f;

                GameScene.plotter.drawPoint(projection1.mul(32f), Color.YELLOW, 1, 0);
            }
        }
        if (rightFoot != null&&rightFoot.getBody()!=null) {
            Body rightFootBody = rightFoot.getBody();
            Vector2 tip2 = rightFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            GameScene.plotter.drawPoint(tip2, Color.CYAN, 1, 0);
            Vector2 projection2 = gameScene.getWorldFacade().detectFirstIntersectionPointWithExceptions(tip2, tip2.cpy().add(0, -10f), getGameEntities());
            if (projection2 != null) {
                rightLegReadyToStand = tip2.dst(projection2) < 1f;
                GameScene.plotter.drawPoint(projection2.mul(32f), Color.YELLOW, 1, 0);

            }
        }


    }

    public void setHead(GameEntity splinter) {
        head = splinter;
    }

    public void findBodyParts() {

        HashSet<GameEntity> entities = new HashSet<>();
        ArrayDeque<GameEntity> deque = new ArrayDeque<>();
        deque.push(head);
        while (!deque.isEmpty()) {
            GameEntity current = deque.pop();

            if (current.getBody() != null && current.isAlive()) {
                entities.add(current);
                for (JointEdge edge : current.getBody().getJointList()) {
                    GameEntity entity = (GameEntity) edge.other.getUserData();
                    if (entity != null)
                        if (!entities.contains(entity)) deque.push(entity);
                }
            }
        }
        alive = false;
        for (GameEntity e : entities) if (e.getType() == SpecialEntityType.UpperTorso) alive = true;
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
                    break;
                case UpperLegLeft:
                    upperLegL = e;
                    break;
                case LowerLegRight:
                    lowerLegR = e;
                    break;
                case LowerLegLeft:
                    lowerLegL = e;
                    break;
                case RightFoot:
                    rightFoot = e;
                    break;
                case LeftFoot:
                    leftFoot = e;
                    break;
            }
        }

    }

    public enum RagdollState {
        FALLENLEFT, FALLENRIGHT, DRAGGEDUP

    }

}
