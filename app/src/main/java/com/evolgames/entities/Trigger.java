package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.physics.CollisionConstants;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;

import java.util.ArrayList;
import java.util.List;

public class Trigger {
    private final GameEntity triggerEntity;
    float timer = 0;
    boolean continueFire = false;
    private final List<ProjectileModel> projectileModels;
    private final ProjectileTriggerType projectileTriggerType = ProjectileTriggerType.AUTOMATIC;
    public Trigger(GameEntity triggerEntity, List<ProjectileModel> projectileModels) {
        this.projectileModels = projectileModels;
        this.triggerEntity = triggerEntity;
    }

    void onTriggerPulled(){
        if(this.projectileTriggerType== ProjectileTriggerType.AUTOMATIC){
            continueFire = true;
        }
    }
    void onTriggerReleased(){
        continueFire = false;
    }

    public void onStep(float deltaTime){
        if(continueFire) {
            timer += deltaTime;
            for(ProjectileModel projectileModel:projectileModels) {
                if (timer > 60f) {
                    fire(projectileModel);
                }
            }
        }
    }
    private void fire(ProjectileModel projectileModel){
        timer = 0;
        createBullet(projectileModel);
        if(projectileModel.getAmmoModel()!=null&&projectileModel.getMissileModel().getBodies().size()>=2) {
            createBulletCasing(projectileModel);
        }
    }

    private void createBulletCasing(ProjectileModel projectileModel) {
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(1));
        Vector2 begin = projectileModel.getAmmoModel().getProperties().getAmmoOrigin();
        Vector2 direction =  projectileModel.getAmmoModel().getProperties().getAmmoDirection();
        Vector2 beginProjected = triggerEntity.getBody().getWorldPoint(begin.cpy().sub(triggerEntity.getCenter()).mul(1/32f)).cpy();
        Vector2 directionProjected = triggerEntity.getBody().getWorldVector(direction).cpy();
        boolean clockwise = projectileModel.getAmmoModel().getAmmoProperties().isRotationOrientation();
        float angularVelocity = clockwise?1:-1 * projectileModel.getAmmoModel().getAmmoProperties().getRotationSpeed()*10;
        float ejectionVelocity = projectileModel.getAmmoModel().getAmmoProperties().getLinearSpeed()*10;
        Vector2 ejectionVelocityVector = directionProjected.mul(ejectionVelocity);
        BodyInit bodyInit = new TransformInit(new AngularVelocityInit(new LinearVelocityInit(new BodyInitImpl(CollisionConstants.CASING_CATEGORY,CollisionConstants.CASING_MASK),ejectionVelocityVector),angularVelocity),beginProjected.x,beginProjected.y, triggerEntity.getBody().getAngle());
       GameEntity shell = GameEntityFactory.getInstance().createIndependentGameEntity(triggerEntity.getParentGroup(), blocks, beginProjected, triggerEntity.getBody().getAngle(),bodyInit,false,"shell");
    }

    private void createBullet(ProjectileModel projectileModel) {
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(0));
        Vector2 begin = projectileModel.getProperties().getProjectileOrigin();
        Vector2 direction = projectileModel.getProperties().getProjectileDirection();
        Vector2 beginProjected = triggerEntity.getBody().getWorldPoint(begin.cpy().sub(triggerEntity.getCenter()).mul(1/32f)).cpy();
        Vector2 directionProjected = triggerEntity.getBody().getWorldVector(direction).cpy();
        float muzzleVelocity = projectileModel.getProperties().getMuzzleVelocity();
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(CollisionConstants.PROJECTILE_CATEGORY,CollisionConstants.PROJECTILE_MASK),muzzleVelocityVector),beginProjected.x,beginProjected.y, triggerEntity.getBody().getAngle()),true);
        GameEntity projectile = GameEntityFactory.getInstance().createIndependentGameEntity(triggerEntity.getParentGroup(), blocks, beginProjected, triggerEntity.getBody().getAngle(), new RecoilInit(bodyInit, triggerEntity.getBody(), projectileModel.getProperties().getRecoil(), muzzleVelocityVector, beginProjected), true,"bullet");
        ResourceManager.getInstance().gunshotSounds.get(projectileModel.getProperties().getFireSound()).getSoundList().get(0).play();
    }

}
