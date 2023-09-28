package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.properties.usage.AutomaticProperties;
import com.evolgames.entities.properties.usage.FuzeBombUsageProperties;
import com.evolgames.entities.properties.usage.ImpactBombUsageProperties;
import com.evolgames.entities.properties.usage.ManualProperties;
import com.evolgames.entities.properties.usage.SemiAutomaticProperties;
import com.evolgames.entities.properties.usage.SlashProperties;
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
        switch (type){
            case RANGED_MANUAL:
               this.properties = (T) new ManualProperties();
                break;
            case RANGED_SEMI_AUTOMATIC:
                this.properties = (T) new SemiAutomaticProperties();
                break;
            case RANGED_AUTOMATIC:
                this.properties = (T) new AutomaticProperties();
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
                break;
            case STABBER:
                this.properties = (T) new StabProperties();
                break;
            case THROWING:
                this.properties = (T) new ThrowProperties();
                break;
        }
        this.type = type;
    }

    public BodyUsageCategory getType() {
        return type;
    }

}
