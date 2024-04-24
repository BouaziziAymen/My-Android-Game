package com.evolgames.entities.ragdoll;

import static com.evolgames.utilities.GeometryUtils.normalizeAngleRad;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.basics.GroupType;
import com.evolgames.entities.basics.SpecialEntityType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.scenes.PhysicsScene;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RagDoll extends GameGroup {

    transient private final PhysicsScene scene;
    transient public GameEntity head;
    transient public GameEntity upperTorso;
    transient public GameEntity upperArmR;
    transient public GameEntity upperLegR;
    transient public GameEntity lowerLegR;
    transient public GameEntity leftFoot;
    transient public GameEntity rightFoot;
    transient GameEntity lowerArmR;
    transient GameEntity upperArmL;
    transient GameEntity lowerArmL;
    transient GameEntity upperLegL;
    transient GameEntity lowerLegL;
    transient GameEntity rightHand;
    transient GameEntity leftHand;
    boolean alive = true;
    transient Balancer[] balancers = new Balancer[6];
    private boolean leftLegReadyToStand;
    private boolean rightLegReadyToStand;
    private int bluntTrauma;
    private boolean torsoReadyToStand;
    private boolean leftUpperLegStraight,rightUpperLegStraight;

    public RagDoll(PhysicsScene scene, GameEntity... entities) {
        super(GroupType.DOLL, entities);
        this.scene = scene;
    }


    private void turnRightFoot(float torque) {
        rightFoot.getBody().applyTorque(torque);
    }


    private void performAction() {
        boolean[] active = new boolean[]{true, true, true, true, true, true};
        for (int i = 0; i < balancers.length; i++) {
            Balancer balancer = balancers[i];
            if (balancer == null || balancer.getEntity() == null || !((GameEntity) balancer.getEntity()).isAlive() || balancer.getEntity().isFrozen()) {
                active[i] = false;
                if (i == 1) {
                    Arrays.fill(active, 1, active.length, false);
                    break;
                }
                if (i == 2) {
                    active[4] = false;
                    active[1] = false;
                }
                if (i == 3) {
                    active[5] = false;
                    active[1] = false;
                }
            }
        }
        if (leftLegReadyToStand && rightLegReadyToStand && torsoReadyToStand && leftUpperLegStraight && rightUpperLegStraight) {
            for (int i = 0; i < balancers.length; i++) {
                Balancer balancer = balancers[i];
                if (active[i]) {
                    balancer.equilibrate();
                }
            }
        }

    }

    @Override
    public void onStep(float timeStep) {
        super.onStep(timeStep);

            HashSet<GameEntity> entities = findBodyParts();
            if (head == null || upperTorso == null) {
               return;
            }
        if (alive) {
            checkBurn(entities);
            checkBlood(entities);
            detectGround();
            this.performAction();
        }
    }
    private void checkBlood(HashSet<GameEntity> entities) {

        BloodResults result = getBloodResults(entities);
        for(GameEntity e:entities){
            for(LayerBlock layerBlock:e.getBlocks()){
              float ratio = layerBlock.getBlockArea()/ result.totalArea;
              float layerBlood = result.blood *ratio;
              layerBlock.setLiquidQuantity(layerBlood);
            }
        }
        if(result.blood <1200){
            alive = false;
        }
    }

    private static BloodResults getBloodResults(HashSet<GameEntity> entities) {
        float blood = 0;
        float totalArea = 0;
        for(GameEntity e: entities){
            for(LayerBlock layerBlock:e.getBlocks()){
                blood+=layerBlock.getLiquidQuantity();
                totalArea+=layerBlock.getBlockArea();
            }
        }
        return new BloodResults(blood, totalArea);
    }

    private static class BloodResults {
        public final float blood;
        public final float totalArea;

        public BloodResults(float blood, float totalArea) {
            this.blood = blood;
            this.totalArea = totalArea;
        }
    }

    private void checkBurn(HashSet<GameEntity> entities) {
        boolean burned = true;
        for (GameEntity e : entities) {
            if (e.getType() != null && e.getType().vital && e.checkBurnDamage() < 0.5f) {
                burned = false;
            }
        }
        if (burned) {
            alive = false;
        }
    }

    private void detectGround() {

        if (leftFoot == null) {
            leftLegReadyToStand = false;
        }
        if (rightFoot == null) {
            rightLegReadyToStand = false;
        }


        if (upperTorso != null && upperTorso.getBody() != null) {
            Body upperTorsoBody = upperTorso.getBody();
            torsoReadyToStand = Math.abs(normalizeAngleRad(upperTorsoBody.getAngle())) < Math.PI / 6;
        }
        if (upperLegR != null && upperLegR.getBody() != null) {
            Body upperLegRBody = upperLegR.getBody();
            rightUpperLegStraight = Math.abs(normalizeAngleRad(upperLegRBody.getAngle())) < Math.PI / 6;
        }

        if (upperLegL != null && upperLegL.getBody() != null) {
            Body upperLegLBody = upperLegL.getBody();
            leftUpperLegStraight = Math.abs(normalizeAngleRad(upperLegLBody.getAngle())) < Math.PI / 6;
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

    public HashSet<GameEntity> findBodyParts() {
        HashSet<GameEntity> entities = new HashSet<>();
        ArrayDeque<GameEntity> deque = new ArrayDeque<>();
        if (this.head == null) {
            return entities;
        }
        deque.push(head);
        while (!deque.isEmpty()) {
            GameEntity current = deque.pop();
            if (current.getBody() != null && current.isAlive()&&current.getType()!=SpecialEntityType.Default) {
                entities.add(current);
                for (JointEdge edge : current.getBody().getJointList()) {
                    GameEntity entity = (GameEntity) edge.other.getUserData();
                    if (entity != null && !entities.contains(entity)) {
                        deque.push(entity);
                    }
                }
            }
        }
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
                    if (balancers[3] == null) {
                        balancers[3] = new Balancer(upperLegL, (float) (Math.PI / 10), 0);
                    }
                    break;
                case LowerLegRight:
                    lowerLegR = e;
                    if (balancers[4] == null) {
                        balancers[4] = new Balancer(lowerLegR, (float) (Math.PI / 10), 0);
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
                    break;
                case LeftFoot:
                    leftFoot = e;
                    break;
            }
        }
        return entities;
    }

    public boolean isBodyPart(GameEntity parentEntity) {
        return Arrays.asList(rightFoot, leftFoot, lowerLegR, lowerLegL, upperLegR, upperLegL, upperTorso, upperArmR, upperArmL,
                lowerArmR, lowerArmL, rightHand, leftHand, head).contains(parentEntity);
    }

    public boolean isBluntSensitiveBodyPart(GameEntity parentEntity) {
        return Arrays.asList(upperTorso, head).contains(parentEntity);
    }

    public void onBlunt(GameEntity gameEntity, int finalNumberOfPoints) {
        if (gameEntity != null) {
        gameEntity.bluntTrauma += finalNumberOfPoints;
        if (gameEntity.bluntTrauma > gameEntity.getArea()) {
                gameEntity.setType(SpecialEntityType.Default);
            }
        }
    }

}
