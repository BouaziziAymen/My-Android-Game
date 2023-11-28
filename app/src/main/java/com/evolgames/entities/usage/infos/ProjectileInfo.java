package com.evolgames.entities.usage.infos;

import android.support.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;

public class ProjectileInfo {

  private CasingInfo casingInfo;
  private transient GameEntity muzzleEntity;
  private String muzzleEntityUniqueId;
  private Vector2 projectileOrigin;
  private Vector2 projectileEnd;
  private float muzzleVelocity;
  private float recoil;
  private int fireSound;
  private float fireRatio;
  private float smokeRatio;
  private float sparkRatio;
  private String missileFile;

  public CasingInfo getCasingInfo() {
    return casingInfo;
  }

  public void setCasingInfo(CasingInfo casingInfo) {
    this.casingInfo = casingInfo;
  }

  public GameEntity getMuzzleEntity() {
    return muzzleEntity;
  }

  public void setMuzzleEntity(@Nullable GameEntity muzzleEntity) {
    this.muzzleEntity = muzzleEntity;
    if (this.muzzleEntity != null) {
      this.muzzleEntityUniqueId = muzzleEntity.getUniqueID();
    }
  }

  public String getMuzzleEntityUniqueId() {
    return muzzleEntityUniqueId;
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

  public int getFireSound() {
    return fireSound;
  }

  public void setFireSound(int fireSound) {
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
}
