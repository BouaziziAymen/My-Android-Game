package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.properties.usage.ContinuousShooterProperties;
import com.evolgames.entities.properties.usage.FlameThrowerProperties;
import com.evolgames.entities.properties.usage.FuzeBombUsageProperties;
import com.evolgames.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.entities.properties.usage.LiquidContainerProperties;
import com.evolgames.entities.properties.usage.MissileProperties;
import com.evolgames.entities.properties.usage.RocketLauncherProperties;
import com.evolgames.entities.properties.usage.RocketProperties;
import com.evolgames.entities.properties.usage.ShooterProperties;
import com.evolgames.entities.properties.usage.SlashProperties;
import com.evolgames.entities.properties.usage.SmashProperties;
import com.evolgames.entities.properties.usage.StabProperties;
import com.evolgames.entities.properties.usage.ThrowProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.ProperModel;

public class UsageModel<T extends Properties> extends ProperModel<T> {
    final BodyUsageCategory type;

    @SuppressWarnings("unchecked")
    public UsageModel(String name, BodyUsageCategory type) {
        super(name);
        switch (type) {
            case SHOOTER:
                this.properties = (T) new ShooterProperties();
                break;
            case SHOOTER_CONTINUOUS:
                this.properties = (T) new ContinuousShooterProperties();
                break;
            case TIME_BOMB:
                this.properties = (T) new TimeBombUsageProperties();
                break;
            case FUZE_BOMB:
                this.properties = (T) new FuzeBombUsageProperties();
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
        }
        this.type = type;
    }

    public BodyUsageCategory getType() {
        return type;
    }
}
