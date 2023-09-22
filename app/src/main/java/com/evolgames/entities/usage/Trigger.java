package com.evolgames.entities.usage;

import static com.evolgames.physics.PhysicsConstants.*;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.properties.usage.AutomaticProperties;
import com.evolgames.entities.properties.usage.ManualProperties;
import com.evolgames.entities.properties.usage.RangedProperties;
import com.evolgames.entities.properties.usage.SemiAutomaticProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.physics.CollisionConstants;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.ArrayList;
import java.util.List;

public class Trigger extends Use{
    private Button<UsageButtonsController> reloadButton;
    private Button<UsageButtonsController> triggerButton;
    private final List<ProjectileModel> projectileModels;
    private final UsageModel<?> usageModel;
    private final float cyclicTime;
    private boolean loaded = false;
    private boolean readyToFire = false;
    private boolean loading = false;
    private final int maxRounds;
    private int rounds;
    private float loadingTimer;
    private final float reloadTime;
    private float readyTimer;
    private boolean continueFire;

    public Trigger(UsageModel<?> rangedUsageModel) {
        this.usageModel = rangedUsageModel;
        RangedProperties rangedProperties = (RangedProperties) rangedUsageModel.getProperties();
        this.projectileModels = rangedProperties.getProjectileModelList();
        switch (usageModel.getType()) {
            case RANGED_MANUAL:
                ManualProperties manualProperties = (ManualProperties) rangedProperties;
                this.reloadTime = manualProperties.getReloadTime();
                this.maxRounds = manualProperties.getNumberOfRounds();
                this.cyclicTime = 0;
                break;
            case RANGED_SEMI_AUTOMATIC:
                SemiAutomaticProperties semiAutomaticProperties = (SemiAutomaticProperties) rangedProperties;
                this.cyclicTime = 1 / getEffectiveFireRate(semiAutomaticProperties.getFireRate());
                this.maxRounds = semiAutomaticProperties.getNumberOfRounds();
                this.reloadTime = semiAutomaticProperties.getReloadTime();
                break;
            case RANGED_AUTOMATIC:
                AutomaticProperties automaticProperties = (AutomaticProperties) rangedProperties;
                this.cyclicTime = 1 / getEffectiveFireRate(automaticProperties.getFireRate());
                this.maxRounds = automaticProperties.getNumberOfRounds();
                this.reloadTime = automaticProperties.getReloadTime();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + usageModel.getType());
        }
    }

    private void onTriggerPulled() {
        if (!this.loaded) {
            return;
        }
        for (ProjectileModel projectileModel : projectileModels) {
            fire(projectileModel);
        }
    }

    private void startReload() {
        this.loadingTimer = 0;
        this.loading = true;
    }

    private void onTriggerReleased() {
        this.continueFire = false;
    }

    @Override
    public void onStep(float deltaTime) {
        for (ProjectileModel projectileModel : this.projectileModels) {
            if (projectileModel.getFireSource() != null) {
                projectileModel.getFireSource().setSpawnEnabled(false);
            }
        }
        if (this.loading) {
            this.loadingTimer += deltaTime;
            if (this.loadingTimer > this.reloadTime) {
                this.loaded = true;
                this.rounds = this.maxRounds;
                this.loading = false;
                this.reloadButton.setEnabled(true);
                this.reloadButton.updateState(Button.State.NORMAL);
               //Reload finished
            }
            return;
        }

        if (!this.readyToFire) {
            this.readyTimer += deltaTime;
            if (this.readyTimer > this.cyclicTime) {
                this.readyTimer = 0;
                this.readyToFire = true;
            }
        }

        if (this.loaded && this.readyToFire && this.continueFire) {
            for (ProjectileModel projectileModel : this.projectileModels) {

                fire(projectileModel);
            }
        }
    }

    private void decrementRounds() {
        this.rounds--;
        if (this.rounds == 0) {
            this.loaded = false;
        }
    }

    private void fire(ProjectileModel projectileModel) {
        this.createBullet(projectileModel);
        this.decrementRounds();
        this.readyToFire = false;
        if (this.usageModel.getType() == BodyUsageCategory.RANGED_AUTOMATIC || this.usageModel.getType() == BodyUsageCategory.RANGED_SEMI_AUTOMATIC) {
            if (projectileModel.getAmmoModel() != null && projectileModel.getMissileModel().getBodies().size() >= 2) {
                createBulletCasing(projectileModel);
            }
            if (this.usageModel.getType() == BodyUsageCategory.RANGED_AUTOMATIC) {
                this.continueFire = true;
            }
        } else {
            this.loaded = false;
        }
        if (projectileModel.getFireSource() != null) {
            projectileModel.getFireSource().setSpawnEnabled(true);
        }
    }

    private void createBulletCasing(ProjectileModel projectileModel) {
        GameEntity muzzleEntity = projectileModel.getMuzzleEntity();
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(1));
        Vector2 begin = projectileModel.getAmmoModel().getProperties().getAmmoOrigin();
        Vector2 direction = projectileModel.getAmmoModel().getProperties().getAmmoDirection();
        Vector2 beginProjected = muzzleEntity.getBody().getWorldPoint(begin.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f)).cpy();
        Vector2 directionProjected = muzzleEntity.getBody().getWorldVector(direction).cpy();
        boolean clockwise = projectileModel.getAmmoModel().getAmmoProperties().isRotationOrientation();
        float angularVelocity = clockwise ? 1 : -1 * projectileModel.getAmmoModel().getAmmoProperties().getRotationSpeed() * 10;
        float ejectionVelocity = projectileModel.getAmmoModel().getAmmoProperties().getLinearSpeed() * 10;
        Vector2 ejectionVelocityVector = directionProjected.mul(ejectionVelocity);
        BodyInit bodyInit = new TransformInit(new AngularVelocityInit(new LinearVelocityInit(new BodyInitImpl(CollisionConstants.CASING_CATEGORY, CollisionConstants.CASING_MASK), ejectionVelocityVector), angularVelocity), beginProjected.x, beginProjected.y, muzzleEntity.getBody().getAngle());
        GameEntityFactory.getInstance().createIndependentGameEntity(muzzleEntity.getParentGroup(), blocks, beginProjected, muzzleEntity.getBody().getAngle(), bodyInit, false, "shell");
    }

    private void createBullet(ProjectileModel projectileModel) {
        GameEntity muzzleEntity = projectileModel.getMuzzleEntity();
        ArrayList<LayerBlock> blocks = BlockUtils.createBlocks(projectileModel.getMissileModel().getBodies().get(0));
        Vector2 begin = projectileModel.getProperties().getProjectileOrigin();
        Vector2 end = projectileModel.getProperties().getProjectileEnd();
        Vector2 beginProjected = muzzleEntity.getBody().getWorldPoint(begin.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f)).cpy();
        Vector2 endProjected = muzzleEntity.getBody().getWorldPoint(end.cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f)).cpy();
        Vector2 direction = end.cpy().sub(begin).nor();
        Vector2 directionProjected = muzzleEntity.getBody().getWorldVector(direction).cpy();
        float muzzleVelocity = getProjectileVelocity(projectileModel.getProperties().getMuzzleVelocity());
        Vector2 muzzleVelocityVector = directionProjected.mul(muzzleVelocity);
        BodyInit bodyInit = new BulletInit(new TransformInit(new LinearVelocityInit(new BodyInitImpl(CollisionConstants.PROJECTILE_CATEGORY, CollisionConstants.PROJECTILE_MASK), muzzleVelocityVector), endProjected.x, endProjected.y, muzzleEntity.getBody().getAngle()), true);
        GameEntityFactory.getInstance().createIndependentGameEntity(muzzleEntity.getParentGroup(), blocks, endProjected, muzzleEntity.getBody().getAngle(), new RecoilInit(bodyInit, muzzleEntity.getBody(), projectileModel.getProperties().getRecoil(), muzzleVelocityVector, beginProjected), true, "bullet");
        ResourceManager.getInstance().gunshotSounds.get(projectileModel.getProperties().getFireSound()).getSoundList().get(0).play();
    }

    private void onReloadPushed() {
        startReload();
        reloadButton.setEnabled(false);
    }

    @Override
    public void createControls(UsageButtonsController usageButtonsController, UserInterface userInterface) {
       super.createControls(usageButtonsController,userInterface);
        triggerButton = new Button<>(ResourceManager.getInstance().arcadeRedTextureRegion, Button.ButtonType.OneClick, true);
        triggerButton.setBehavior(new ButtonBehavior<UsageButtonsController>(usageButtonsController, triggerButton) {
            @Override
            public void informControllerButtonClicked() {
                onTriggerPulled();
            }

            @Override
            public void informControllerButtonReleased() {
                onTriggerReleased();
            }
        });
        triggerButton.setPosition(800 - triggerButton.getWidth(), 0);

        reloadButton = new Button<>(ResourceManager.getInstance().arcadeRedTextureRegion, Button.ButtonType.OneClick, true);
        reloadButton.setBehavior(new ButtonBehavior<UsageButtonsController>(usageButtonsController, reloadButton) {
            @Override
            public void informControllerButtonClicked() {
                onReloadPushed();
            }

            @Override
            public void informControllerButtonReleased() {
            }
        });
        reloadButton.setPosition(800 - reloadButton.getWidth() - triggerButton.getWidth(), 0);
        userInterface.addElement(reloadButton);
        userInterface.addElement(triggerButton);
    }

    @Override
    public float getUIWidth() {
        return triggerButton.getWidth()+reloadButton.getWidth();
    }

    @Override
    public void updateUIPosition(int row, int offset) {
        reloadButton.setPosition(800 -offset- reloadButton.getWidth() - triggerButton.getWidth(), row * 32f);
        triggerButton.setPosition(800-offset - triggerButton.getWidth(), 0);
    }

    @Override
    public void showUI() {
        triggerButton.setVisible(true);
        reloadButton.setVisible(true);
    }
    @Override
    public void hideUI() {
        triggerButton.setVisible(false);
        reloadButton.setVisible(false);
    }

    @Override
    public int getUseId() {
        return 3;
    }
}
