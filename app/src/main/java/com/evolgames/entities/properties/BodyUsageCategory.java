package com.evolgames.entities.properties;

import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;

public enum BodyUsageCategory {

    HEAVY(R.string.heavy_usage, "Heavy Options", 10),
    SHOOTER(R.string.shooter_usage, "Ranged Options", 1),
    SHOOTER_CONTINUOUS(R.string.shooter_continuous, "Ranged Options", 1),
    BOW(R.string.bow_usage, "Bow Options", 1),
    TIME_BOMB(R.string.time_bomb_usage, "Bomb Options", 2),
    IMPACT_BOMB(R.string.impact_bomb_usage, "Bomb Options", 2),
    SLASHER(R.string.slashing_usage, "Slashing Options", 3),
    BLUNT(R.string.blunt_usage, "Blunt Options", 4),
    STABBER(R.string.stabbing_usage, "Stabbing Options", 5),
    THROWING(R.string.throwing_usage, "Throwing Options", 6),
    FLAME_THROWER(R.string.flame_thrower_usage, "Flame Thr. Options", 7),
    ROCKET(R.string.rocket_usage, "Rocket. Options", 8),
    MISSILE(R.string.missile_usage, "Missile. Options", 8),
    LIQUID_CONTAINER(R.string.liquid_container_usage, "Container. Options", 9),
    ROCKET_LAUNCHER(R.string.rocket_launcher_usage, "Rock. Launch. Options", 7),
    MOTOR_CONTROL(R.string.motor_control_usage,"Motor. Cont. Options",11);
    public final int nameStringId;
    final int group;
    private final String optionsTitle;

    BodyUsageCategory(int nameStringId, String optionsTitle, int group) {
        this.nameStringId = nameStringId;
        this.group = group;
        this.optionsTitle = optionsTitle;
    }
    public int getGroup() {
        return group;
    }

    public String getOptionsTitle() {
        return optionsTitle;
    }

}
