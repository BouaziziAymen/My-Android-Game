package com.evolgames.entities.properties;

import com.evolgames.gameengine.R;

public enum BodyUsageCategory {

    HEAVY(R.string.heavy_usage, 10,false),
    SHOOTER(R.string.shooter_usage, 1,true),
    SHOOTER_CONTINUOUS(R.string.shooter_continuous, 1,true),
    BOW(R.string.bow_usage, 1,true),
    ROCKET_LAUNCHER(R.string.rocket_launcher_usage, 1,true),
    TIME_BOMB(R.string.time_bomb_usage, 2,true),
    IMPACT_BOMB(R.string.impact_bomb_usage, 2,true),
    SLASHER(R.string.slashing_usage, 3,false),
    BLUNT(R.string.blunt_usage, 4,false),
    STABBER(R.string.stabbing_usage, 5,false),
    THROWING(R.string.throwing_usage, 6,false),
    FLAME_THROWER(R.string.flame_thrower_usage, 1,true),
    ROCKET(R.string.rocket_usage, 8,true),
    MISSILE(R.string.missile_usage, 8,true),
    LIQUID_CONTAINER(R.string.liquid_container_usage, 9,true),
    MOTOR_CONTROL(R.string.motor_control_usage, 11,true);
    public final int nameStringId;
    final int group;
    public final boolean hasOptions;

    BodyUsageCategory(int nameStringId, int group, boolean hasOptions) {
        this.nameStringId = nameStringId;
        this.group = group;
        this.hasOptions = hasOptions;
    }
    public int getGroup() {
        return group;
    }


}
