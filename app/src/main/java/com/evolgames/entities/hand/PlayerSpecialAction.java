package com.evolgames.entities.hand;

import com.evolgames.gameengine.R;

public enum PlayerSpecialAction {
    None(R.drawable.none_icon, false, true),
    Drop(R.drawable.drop_icon, false, false),
    Unselect(R.drawable.deselect_icon, false, true),
    SingleShot(R.drawable.bullet_icon, false, false,true),
    Trigger(R.drawable.tap_icon, false, true),
    SwitchOn(R.drawable.on_icon, false, true),
    SwitchOff(R.drawable.off_icon, false, true),
    Slash(R.drawable.slash_icon, true, false,R.string.slash_hint),
    Stab(R.drawable.stab_icon, true, false,R.string.stab_hint),
    Throw(R.drawable.hatchet_icon, true, false,R.string.throw_hint),
    Smash(R.drawable.mace_icon, true, false,R.string.bludgeon_hint),
    FireLight(R.drawable.shooting_icon, true, false,R.string.fire_light_hint),
    AimLight(R.drawable.aim_icon, false, false,R.string.aim_light_hint),
    Guide(R.drawable.aim_icon, true, true),
    Shoot_Arrow(R.drawable.target_icon, true, false),
    FireHeavy(R.drawable.machine_icon, false, true,R.string.fire_heavy_hint),
    AimHeavy(R.drawable.aim_icon, false, true,R.string.aim_heavy_hint),
    motorStop(R.drawable.stop_icon, true, true),
    motorMoveForward(R.drawable.forward_icon, false, true),
    motorMoveBackward(R.drawable.backward_icon, false, true),

    effectCut(R.drawable.laser_cut_icon, false, false,R.string.effect_cut_hint),
    effectGlue(R.drawable.glue_icon, false, false,R.string.effect_glue_hint),
    effectFireBolt(R.drawable.fire_icon, false, false,R.string.effect_fire_hint),
    effectMeteor(R.drawable.meteor_icon, false, false,R.string.effect_meteor_hint),
    effectFrost(R.drawable.frost_icon, false, false,R.string.effect_frost_hint);
    public final int iconId;
    public final boolean selectableByDefault;
    public final boolean fromDistance;
    public final int hintString;
    public final boolean oneClick;

    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance, boolean oneClick) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = -1;
        this.oneClick = oneClick;
    }
    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = -1;
        this.oneClick = false;
    }
    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance, int hintString) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = hintString;
        this.oneClick = false;
    }
}
