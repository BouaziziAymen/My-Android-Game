package com.evolgames.entities.hand;

import com.evolgames.gameengine.R;

public enum PlayerSpecialAction {
    None(R.drawable.none_icon, false, false, false),
    Trigger(R.drawable.tap_icon, false, false, true),
    SwitchOn(R.drawable.on_icon, true, false, true),
    SwitchOff(R.drawable.off_icon, true, false, true),
    Slash(R.drawable.slash_icon, false, true, false),
    Stab(R.drawable.stab_icon, false, true, false),
    Throw(R.drawable.hatchet_icon, false, true, false),
    Smash(R.drawable.mace_icon, false, true, false),
    Fire(R.drawable.shooting_icon, false, true, false),
    AimLight(R.drawable.aim_icon, false, false, false),
    Guide(R.drawable.home_icon, false, true, true),
    Shoot_Arrow(R.drawable.target_icon, false, true, false),
    FireHeavy(R.drawable.machine_icon, false, false, true),
    AimHeavy(R.drawable.aim_icon, false, false, true),
    motorStop(R.drawable.stop_icon, false, true, true),
    motorMoveForward(R.drawable.forward_icon, false, false, true),
    motorMoveBackward(R.drawable.backward_icon, false, false, true),

    effectCut(R.drawable.laser_cut_icon, false, false, false),
    effectGlue(R.drawable.glue_icon, false, false, false),
    effectFireBolt(R.drawable.fire_icon, false, false, false),
    effectMeteor(R.drawable.meteor_icon, false, false, false),
    effectFrost(R.drawable.frost_icon, false, false, false);
    public final int iconId;
    public final boolean doubleState;
    public final boolean selectableByDefault;
    public final boolean fromDistance;

    PlayerSpecialAction(int iconId, boolean doubleState, boolean selectableByDefault, boolean fromDistance) {
        this.iconId = iconId;
        this.doubleState = doubleState;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
    }
}
