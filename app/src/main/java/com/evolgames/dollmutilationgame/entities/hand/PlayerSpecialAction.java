package com.evolgames.dollmutilationgame.entities.hand;

import com.evolgames.dollmutilationgame.R;

public enum PlayerSpecialAction {

    None(R.drawable.none_icon, false, true,0),
    Drop(R.drawable.drop_icon, false, false,10),
    Unselect(R.drawable.deselect_icon, false, true,10),
    SingleShot(R.drawable.bullet_icon, false, false,true,9),
    Trigger(R.drawable.tap_icon, false, true,1),
    SwitchOn(R.drawable.on_icon, false, true,1),
    SwitchOff(R.drawable.off_icon, false, true,1),
    Slash(R.drawable.slash_icon, true, false,R.string.slash_hint,1),
    Stab(R.drawable.stab_icon, true, false,R.string.stab_hint,1),
    Throw(R.drawable.hatchet_icon, true, false,R.string.throw_hint,1),
    Smash(R.drawable.mace_icon, true, false,R.string.bludgeon_hint,1),
    FireLight(R.drawable.shooting_icon, true, false,R.string.fire_light_hint,1),
    AimLight(R.drawable.aim_icon, false, false,R.string.aim_light_hint,2),
    Guide(R.drawable.aim_icon, true, true,2),
    Shoot_Arrow(R.drawable.target_icon, true, false,1),
    FireHeavy(R.drawable.machine_icon, false, true,R.string.fire_heavy_hint,1),
    AimHeavy(R.drawable.aim_icon, false, true,R.string.aim_heavy_hint,2),
    motorStop(R.drawable.stop_icon, true, true,3),
    motorMoveForward(R.drawable.forward_icon, false, true,3),
    motorMoveBackward(R.drawable.backward_icon, false, true,3),

    effectCut(R.drawable.laser_cut_icon, false, false,R.string.effect_cut_hint,0),
    effectGlue(R.drawable.glue_icon, false, false,R.string.effect_glue_hint,1),
    effectFireBolt(R.drawable.fire_icon, false, false,R.string.effect_fire_hint,2),
    effectMeteor(R.drawable.meteor_icon, false, false,R.string.effect_meteor_hint,4),
    effectFrost(R.drawable.frost_icon, false, false,R.string.effect_frost_hint,3);
    public final int iconId;
    public final boolean selectableByDefault;
    public final boolean fromDistance;
    public final int hintString;
    public final boolean oneClick;
    private final int priority;

    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance, boolean oneClick, int priority) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = -1;
        this.oneClick = oneClick;
        this.priority = priority;
    }
    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance,int priority) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = -1;
        this.oneClick = false;
        this.priority = priority;
    }
    PlayerSpecialAction(int iconId, boolean selectableByDefault, boolean fromDistance, int hintString, int priority) {
        this.iconId = iconId;
        this.selectableByDefault = selectableByDefault;
        this.fromDistance = fromDistance;
        this.hintString = hintString;
        this.oneClick = false;
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }
}
