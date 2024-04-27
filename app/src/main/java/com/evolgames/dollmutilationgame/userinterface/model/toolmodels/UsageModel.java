package com.evolgames.dollmutilationgame.userinterface.model.toolmodels;

import com.evolgames.dollmutilationgame.entities.properties.usage.BowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.HeavyProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MissileProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.MotorControlProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.RocketProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ShooterProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.SlashProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.SmashProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.StabProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.ThrowProperties;
import com.evolgames.dollmutilationgame.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.dollmutilationgame.entities.properties.BodyUsageCategory;
import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;

public class UsageModel<T extends Properties> extends ProperModel<T> {
    final BodyUsageCategory type;

    @SuppressWarnings("unchecked")
    public UsageModel(String name, BodyUsageCategory type) {
        super(name);
        switch (type) {
            case HEAVY:
                this.properties = (T) new HeavyProperties();
                break;
            case SHOOTER:
                this.properties = (T) new ShooterProperties();
                break;
            case SHOOTER_CONTINUOUS:
                this.properties = (T) new ContinuousShooterProperties();
                break;
            case BOW:
                this.properties = (T) new BowProperties();
                break;
            case TIME_BOMB:
                this.properties = (T) new TimeBombUsageProperties();
                break;
            case IMPACT_BOMB:
                this.properties = (T) new ImpactBombUsageProperties();
                break;
            case SLASHER:
                this.properties = (T) new SlashProperties();
                break;
            case BLUNT:
                this.properties = (T) new SmashProperties();
                break;
            case STABBER:
                this.properties = (T) new StabProperties();
                break;
            case THROWING:
                this.properties = (T) new ThrowProperties();
                break;
            case FLAME_THROWER:
                this.properties = (T) new FlameThrowerProperties();
                break;
            case ROCKET:
                this.properties = (T) new RocketProperties();
                break;
            case MISSILE:
                this.properties = (T) new MissileProperties();
                break;
            case LIQUID_CONTAINER:
                this.properties = (T) new LiquidContainerProperties();
                break;
            case ROCKET_LAUNCHER:
                this.properties = (T) new RocketLauncherProperties();
                break;
            case MOTOR_CONTROL:
                this.properties = (T) new MotorControlProperties();
                break;
        }
        this.type = type;
    }

    public BodyUsageCategory getType() {
        return type;
    }
}
