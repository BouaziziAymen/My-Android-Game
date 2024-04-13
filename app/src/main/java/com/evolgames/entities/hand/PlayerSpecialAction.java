package com.evolgames.entities.hand;

import com.evolgames.gameengine.R;

public enum PlayerSpecialAction {
    None(R.drawable.none_icon, false, false, true),
    Drop(R.drawable.drop_icon, false, false, false),
    Unselect(R.drawable.deselect_icon, false, false, true),
    Trigger(R.drawable.tap_icon, false, false, true),
    SwitchOn(R.drawable.on_icon, true, false, true),
    SwitchOff(R.drawable.off_icon, true, false, true),
    Slash(R.drawable.slash_icon, false, true, false),
    Stab(R.drawable.stab_icon, false, true, false),
    Throw(R.drawable.hatchet_icon, false, true, false,R.string.throw_hint),
    Smash(R.drawable.mace_icon, false, true, false),
    FireLight(R.drawable.shooting_icon, false, true, false,R.string.fire_light_hint),
    AimLight(R.drawable.aim_icon, false, false, false,R.string.aim_light_hint),
    Guide(R.drawable.home_icon, false, true, true),
    Shoot_Arrow(R.drawable.target_icon, false, true, false),
    FireHeavy(R.drawable.machine_icon, false, false, true,R.string.fire_heavy_hint),
    AimHeavy(R.drawable.aim_icon, false, false, true,R.string.aim_heavy_hint),
    motorStop(R.drawable.stop_icon, false, true, true),
    motorMoveForward(R.drawable.forward_icon, false, false, true),
    motorMoveBackward(R.drawable.backward_icon, false, false, true),

    effectCut(R.drawable.laser_cut_icon, false, false, false,R.string.effect_cut_hint),
    effectGlue(R.drawable.glue_icon, false, false, false,R.string.effect_glue_hint),
    effectFireBolt(R.drawable.fire_icon, false, false, false,R.string.effect_fire_hint),
    effectMeteor(R.drawable.meteor_icon, false, false, false,R.string.effect_meteor_hint),
    effectFrost(R.drawable.frost_icon, false, false, false,R.string.effect_frost_hint);
    public final int iconId;
    public final boolean doubleState;
    public final boolean selectableByDefault;
    public final boolean fromDistance;
    public final int hintString;

    PlayerSpecialAction(int iconId, boolean doubleState, boolean selectableByDefault, boolean fromDistance) {
        this.iconId = iconId;
        this.doubleState = doubleState;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = -1;
    }
    PlayerSpecialAction(int iconId, boolean doubleState, boolean selectableByDefault, boolean fromDistance, int hintString) {
        this.iconId = iconId;
        this.doubleState = doubleState;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = hintString;
    }
}
