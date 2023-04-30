package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;

import java.util.ArrayList;

public class Trigger {
    private final GameEntity projectileOwner;
    float timer = 0;
    boolean continueFire = false;
    private final ProjectileModel projectileModel;
    public Trigger(ProjectileModel projectileModel, GameEntity entity) {
        this.projectileModel = projectileModel;
        this.projectileOwner = entity;
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
        if(projectileModel.getMissileModel()==null){
            return;
        }
        timer = 0;
        createBullet();
        createBulletCasing();
    }

    private void createBulletCasing() {
        if(projectileModel.getMissileModel().getBodies().size()<2){
            return;
        }
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(1));
        Vector2 begin = projectileModel.getAmmoModel().getProperties().getAmmoOrigin();
        Vector2 direction =  projectileModel.getAmmoModel().getProperties().getAmmoDirection();
        Vector2 beginProjected = projectileOwner.getBody().getWorldPoint(begin.cpy().sub(projectileOwner.getCenter()).mul(1/32f)).cpy();
        Vector2 directionProjected = projectileOwner.getBody().getWorldVector(direction).cpy();
        float muzzleVelocity = 6;
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        Filter filter = projectileOwner.getInitialFilter();
        BodyInit bodyInit = new TransformInit(new LinearVelocityInit(new BodyInitImpl(filter),muzzleVelocityVector),beginProjected.x,beginProjected.y, projectileOwner.getBody().getAngle());
       GameEntity shell = GameEntityFactory.getInstance().createIndependentGameEntity(projectileOwner.getParentGroup(), blocks, beginProjected, projectileOwner.getBody().getAngle(),bodyInit,false);
        projectileOwner.getGameScene().getWorldFacade().addNonCollidingPair(projectileOwner,shell);
    }

    private void createBullet() {
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(0));
        Vector2 begin = projectileModel.getProperties().getProjectileOrigin();
        Vector2 direction = projectileModel.getProperties().getProjectileDirection();
        Vector2 beginProjected = projectileOwner.getBody().getWorldPoint(begin.cpy().sub(projectileOwner.getCenter()).mul(1/32f)).cpy();
        Vector2 directionProjected = projectileOwner.getBody().getWorldVector(direction).cpy();
        float muzzleVelocity = projectileModel.getProperties().getMuzzleVelocity();
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        Filter filter = projectileOwner.getInitialFilter();
        BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(filter),muzzleVelocityVector),beginProjected.x,beginProjected.y, projectileOwner.getBody().getAngle()),true);
        GameEntity projectile = GameEntityFactory.getInstance().createIndependentGameEntity(projectileOwner.getParentGroup(), blocks, beginProjected, projectileOwner.getBody().getAngle(), new RecoilInit(bodyInit, projectileOwner.getBody(), projectileModel.getProperties().getRecoil(), muzzleVelocityVector, beginProjected), true);
        ResourceManager.getInstance().gunshotSounds.get(projectileModel.getProperties().getFireSound()).getSoundList().get(0).play();
        projectileOwner.getGameScene().getWorldFacade().addNonCollidingPair(projectileOwner,projectile);

    }


    public ProjectileModel getProjectileModel() {
        return projectileModel;
    }
}
