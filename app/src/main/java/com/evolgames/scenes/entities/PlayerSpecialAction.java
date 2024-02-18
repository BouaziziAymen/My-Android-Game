package com.evolgames.scenes.entities;

public enum PlayerSpecialAction {
  None(12,false),
  Grenade(5,false),
  Slash(11,true),
  Stab(9,true),
  Throw(7,false),
  Smash(10,false),
  Shoot(8,false), ThrowFire(4,false), Rocket(3,false),Missile(3,false), GuidedRocket(2,false), Pouring(1,false), Sealed(0,false);
  public final int iconId;
  public final boolean hasNone;

  PlayerSpecialAction(int iconId, boolean hasNone) {
    this.iconId = iconId;
    this.hasNone = hasNone;
  }
}
