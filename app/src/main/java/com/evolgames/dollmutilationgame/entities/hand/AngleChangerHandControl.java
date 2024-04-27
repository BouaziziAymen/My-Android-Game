package com.evolgames.dollmutilationgame.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.utilities.MathUtils;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

public class AngleChangerHandControl extends HandControl {

    private float target;
    private float step;

    @SuppressWarnings("unused")
    public AngleChangerHandControl() {
    }

    AngleChangerHandControl(Hand hand, float targetAngleDegrees) {
        super(hand);
        this.target = targetAngleDegrees;
        float rot = this.hand.getGrabbedEntity().getBody().getAngle();
        float dis =
                GeometryUtils.calculateShortestDirectedDistance(
                        rot * MathUtils.radiansToDegrees, targetAngleDegrees);
        this.step = dis * MathUtils.degreesToRadians / 15f;
        this.hand.getGrabbedEntity().getBody().setFixedRotation(true);
    }

    public void run() {
        GameEntity gameEntity = this.hand.getGrabbedEntity();
        Vector2 position = gameEntity.getBody().getPosition();
        float rot = gameEntity.getBody().getAngle();
        float dis =
                GeometryUtils.calculateShortestDirectedDistance(rot * MathUtils.radiansToDegrees, target);
        if (Math.abs(dis) > 0) {
            gameEntity.getBody().setTransform(position, gameEntity.getBody().getAngle() + step);
        }
    }
}
