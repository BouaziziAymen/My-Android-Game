package com.evolgames.scenes;

public enum PlayerSpecialAction {
   None(-1), Slash(5),Stab(3),Throw(1);
   public final int iconId;

   PlayerSpecialAction(int iconId) {
      this.iconId = iconId;
   }
}
