package com.evolgames.scenes.entities;

public enum PlayerSpecialAction {
  None(10),
  Grenade(3),
  Slash(9),
  Stab(7),
  Throw(5),
  Smash(8),
  Shoot(6), ThrowFire(2), Rocket(1),Missile(1), GuidedRocket(0);
  public final int iconId;

  PlayerSpecialAction(int iconId) {
    this.iconId = iconId;
  }
}
