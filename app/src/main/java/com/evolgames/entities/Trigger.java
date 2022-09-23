package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.factories.GameEntityFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;

import java.util.ArrayList;

public class Trigger {
    private final GameEntity gameEntity;
    float timer = 0;
    boolean continueFire = false;
    private final ProjectileModel projectileModel;
    public Trigger(ProjectileModel projectileModel, GameEntity entity) {
        this.projectileModel = projectileModel;
        this.gameEntity = entity;
    }

    void onTriggerPulled(){
        fire();
        if(projectileModel.getProperties().getProjectileTriggerType()== ProjectileTriggerType.AUTOMATIC){
            continueFire = true;
        }
    }
    void onTriggerReleased(){
        continueFire = false;
    }
    public void onStep(float deltaTime){
        if(continueFire) {
            timer += deltaTime;
            if (timer > 60f / projectileModel.getProperties().getFireRate()) {
                fire();
            }
        }
    }
    private void fire(){
        timer = 0;
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(0));
        Vector2 begin = projectileModel.getProperties().getProjectileOrigin();
        Vector2 direction = projectileModel.getProperties().getProjectileDirection();
        Vector2 beginProjected = gameEntity.getBody().getWorldPoint(begin.cpy().sub(gameEntity.getCenter()).mul(1/32f)).cpy();
        Vector2 directionProjected = gameEntity.getBody().getWorldVector(direction).cpy();
        float muzzleVelocity = projectileModel.getProperties().getMuzzleVelocity();
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        BodyInit bodyInit = new TransformInit(new LinearVelocityInit(new BodyInitImpl(),muzzleVelocityVector),beginProjected.x,beginProjected.y,gameEntity.getBody().getAngle());
        GameEntityFactory.getInstance().createProjectile(gameEntity.getParentGroup(), blocks, beginProjected,gameEntity.getBody().getAngle(),new RecoilInit(bodyInit,gameEntity.getBody(),projectileModel.getProperties().getRecoil(),muzzleVelocityVector,beginProjected));
        ResourceManager.getInstance().gunshotSounds.get(projectileModel.getProperties().getFireSound()).getSoundList().get(0).play();
    }


    public ProjectileModel getProjectileModel() {
        return projectileModel;
    }
}
