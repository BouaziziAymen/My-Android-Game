package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.PlayerSpecialAction;

public class HoldHandControl extends HandControl {
    private final GameEntity weapon;

    public HoldHandControl(GameEntity weapon) {
        super();
        this.weapon = weapon;

    }


    @Override
    public void run() {
        super.run();
        if(weapon==null||weapon.getBody()==null){
            return;
        }
        float angle = getAngle();

        Body body = weapon.getBody();
        if(!isDead()) {
            assert body != null;
            float rot = body.getAngle();
            float error = angle - rot;

            while (error < -Math.PI) error += 2 * Math.PI;
            while (error > Math.PI) error -= 2 * Math.PI;
            if (Math.abs(error) > 0.005f) body.setAngularVelocity(10 * error);
        }
    }

    private float getAngle() {
        PlayerSpecialAction action = weapon.getGameScene().getSpecialAction();
        float angle = 0;
        if(action ==PlayerSpecialAction.Slash|| action ==PlayerSpecialAction.None){
            angle = 0 * MathUtils.degreesToRadians;
        } else if(action ==PlayerSpecialAction.Stab){
            angle = 90 * MathUtils.degreesToRadians;
        }
        return angle;
    }
}
