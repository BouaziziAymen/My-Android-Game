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

    private final PhysicsWorld physicsWorld;
    private final Grafcet brain;
    private final GameScene gameScene;
    public GameEntity head;
    public GameEntity upperTorso;
    public GameEntity upperArmR;
    GameEntity lowerTorso;
    GameEntity lowerArmR;
    GameEntity upperArmL;
    GameEntity lowerArmL;
    public GameEntity upperLegR;
    public GameEntity lowerLegR;
    GameEntity upperLegL;
    GameEntity lowerLegL;
    GameEntity rightHand;
    GameEntity leftHand;
    GameEntity leftFoot;
    GameEntity rightFoot;
    float t;
    boolean alive = true;
    private boolean footTouchingR;
    private boolean footTouchingL;
    private int ORBIT;
    private boolean leftLegReadyToStand;
    private boolean rightLegReadyToStand;
    private SimpleDetectionRayCastCallback simpleDetectionCallback;


    public Ragdoll(GameScene gameScene, PhysicsWorld physicsWorld, GameEntity... entities) {
        super(entities);
        this.gameScene = gameScene;
        this.physicsWorld = physicsWorld;
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


        balancers[0] = new Balancer(head, (float) (Math.PI / 2),0);
        balancers[1] = new Balancer(upperTorso, (float) (Math.PI / 4),0);
        balancers[2] = new Balancer(upperLegR, (float) (Math.PI / 4),0);
        balancers[3] = new Balancer(lowerLegR, (float) (Math.PI / 6),0);
        balancers[4] = new Balancer(upperLegL, (float) (Math.PI / 4),0);
        balancers[5] = new Balancer(lowerLegL, (float) (Math.PI / 6),0);

        simpleDetectionCallback = gameScene.getWorldFacade().getSimpleDetectionRayCastCallback();


    }

    private void turnRightFoot(float torque) {
        rightFoot.getBody().applyTorque(torque);
    }

    public void onUpdate(float pSecondsElapsed) {
        if (head.getBody() == null || !head.isAlive()) return;

        detectGround();

//Log.e("feet",lowerLegR.getMesh().getRotation()+"/"+lowerLegL.getMesh().getRotation()+"/"+rightFoot.getMesh().getRotation()+"/"+leftFoot.getMesh().getRotation());
        this.t += pSecondsElapsed;
        this.brain.run();





            for (int i = 0; i < this.brain.numStat[this.brain.current]; i++) {
                this.performAction(this.brain.actions[this.brain.current][i], this.brain.speeds[this.brain.current][i]);
            }



    }
Balancer[] balancers = new Balancer[6];
    private void performAction(int action, int amplitude) {


if(leftLegReadyToStand&&rightLegReadyToStand)
                for(Balancer balancer : balancers)
                   if(balancer.getEntity()!=null&&((GameEntity) balancer.getEntity()).isAlive())
                    balancer.equilibrate();

    }

    @Override
    public void update() {
        super.update();
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
        if (leftFoot != null) {
            Body leftFootBody = leftFoot.getBody();
            Vector2 tip1 = leftFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            GameScene.plotter.drawPoint(tip1, Color.PINK, 1, 0);
            Vector2 projection1 = gameScene.getWorldFacade().detect(tip1, tip1.cpy().add(0, -10f));
            if (projection1 != null) {
                leftLegReadyToStand = tip1.dst(projection1) < 1f;

                GameScene.plotter.drawPoint(projection1.mul(32f), Color.YELLOW, 1, 0);
            }
        }
        if (rightFoot != null) {
            Body rightFootBody = rightFoot.getBody();
            Vector2 tip2 = rightFootBody.getWorldPoint(new Vector2(0, 0).mul(1 / 32f)).cpy();
            GameScene.plotter.drawPoint(tip2, Color.CYAN, 1, 0);
            Vector2 projection2 = gameScene.getWorldFacade().detect(tip2, tip2.cpy().add(0, -10f));
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
                    if(entity!=null)
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


            simpleDetectionCallback.resetExcepted();
            simpleDetectionCallback.addExcepted(leftFoot);
            simpleDetectionCallback.addExcepted(rightFoot);
            simpleDetectionCallback.addExcepted(lowerLegL);
            simpleDetectionCallback.addExcepted(lowerLegR);
            simpleDetectionCallback.addExcepted(upperLegR);
            simpleDetectionCallback.addExcepted(upperLegL);
            simpleDetectionCallback.addExcepted(head);
            simpleDetectionCallback.addExcepted(upperTorso);
            simpleDetectionCallback.addExcepted(lowerTorso);
            simpleDetectionCallback.addExcepted(upperArmR);
            simpleDetectionCallback.addExcepted(upperArmL);
            simpleDetectionCallback.addExcepted(lowerArmR);
            simpleDetectionCallback.addExcepted(lowerArmL);
            simpleDetectionCallback.addExcepted(leftHand);
            simpleDetectionCallback.addExcepted(rightHand);
        }

    }

    public enum RagdollState {
        FALLENLEFT, FALLENRIGHT, DRAGGEDUP

    }

}
