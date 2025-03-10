package com.evolgames.dollmutilationgame.entities.serialization.infos;

import androidx.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.usage.Muzzle;

public class ProjectileInfo {

    private String projectileInfoUniqueId;
    private CasingInfo casingInfo;

    private transient Muzzle muzzle;
    private String muzzleEntityUniqueId;
    private Vector2 projectileOrigin;
    private Vector2 projectileEnd;
    private float muzzleVelocity;
    private float recoil;
    private String fireSound;
    private float fireRatio;
    private float smokeRatio;
    private float sparkRatio;
    private String missileFile;
    private int id;

    private String rocketEntityUniqueId;
    private boolean assetsMissile;
    private String arrowGroupUniqueId;
    private boolean updatedMuzzle;

    public CasingInfo getCasingInfo() {
        return casingInfo;
    }

    public void setCasingInfo(CasingInfo casingInfo) {
        this.casingInfo = casingInfo;
    }

    public String getProjectileInfoUniqueId() {
        return projectileInfoUniqueId;
    }

    public void setProjectileInfoUniqueId(String projectileInfoUniqueId) {
        this.projectileInfoUniqueId = projectileInfoUniqueId;
    }

    public String getMuzzleEntityUniqueId() {
        return muzzleEntityUniqueId;
    }

    public Muzzle getMuzzle() {
        return muzzle;
    }

    public void setMuzzle(Muzzle muzzle) {
        this.muzzle = muzzle;
        if(this.muzzle!=null&&muzzle.getTheMuzzleEntity()!=null) {
            this.muzzleEntityUniqueId = muzzle.getTheMuzzleEntity().getUniqueID();
        }
    }

    public Vector2 getProjectileOrigin() {
        return projectileOrigin;
    }

    public void setProjectileOrigin(Vector2 projectileOrigin) {
        this.projectileOrigin = projectileOrigin;
    }

    public Vector2 getProjectileEnd() {
        return projectileEnd;
    }

    public void setProjectileEnd(Vector2 projectileEnd) {
        this.projectileEnd = projectileEnd;
    }

    public float getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public void setMuzzleVelocity(float muzzleVelocity) {
        this.muzzleVelocity = muzzleVelocity;
    }

    public float getRecoil() {
        return recoil;
    }

    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    public String getFireSound() {
        return fireSound;
    }

    public void setFireSound(String fireSound) {
        this.fireSound = fireSound;
    }

    public float getFireRatio() {
        return fireRatio;
    }

    public void setFireRatio(float fireRatio) {
        this.fireRatio = fireRatio;
    }

    public float getSmokeRatio() {
        return smokeRatio;
    }

    public void setSmokeRatio(float smokeRatio) {
        this.smokeRatio = smokeRatio;
    }

    public float getSparkRatio() {
        return sparkRatio;
    }

    public void setSparkRatio(float sparkRatio) {
        this.sparkRatio = sparkRatio;
    }

    public String getMissileFile() {
        return missileFile;
    }

    public void setMissileFile(String missileFile) {
        this.missileFile = missileFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getRocketEntityUniqueId() {
        return rocketEntityUniqueId;
    }

    public void setRocketEntityUniqueId(String rocketEntityUniqueId) {
        this.rocketEntityUniqueId = rocketEntityUniqueId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ProjectileInfo)) {
            return false;
        }
        return id == ((ProjectileInfo) obj).id;
    }

    public boolean isAssetsMissile() {
        return assetsMissile;
    }

    public void setAssetsMissile(boolean assetsMissile) {
        this.assetsMissile = assetsMissile;
    }

    public String getArrowGroupUniqueId() {
        return arrowGroupUniqueId;
    }

    public void setArrowGroupUniqueId(String arrowGroupUniqueId) {
        this.arrowGroupUniqueId = arrowGroupUniqueId;
    }

    public void setUpdatedMuzzle(boolean updatedMuzzle) {
        this.updatedMuzzle = updatedMuzzle;
    }

    public boolean isUpdatedMuzzle() {
        return updatedMuzzle;
    }
}/**/
