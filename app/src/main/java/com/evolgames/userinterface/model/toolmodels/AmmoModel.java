package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.AmmoProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.AmmoShape;

public class AmmoModel extends ProperModel<AmmoProperties> {

    private final AmmoShape ammoShape;
    private final int bodyId;
    private final int ammoId;

    public AmmoModel(int bodyId, int ammoId, AmmoShape ammoShape) {
        super("Ammo "+ammoId);
        this.properties = new AmmoProperties(ammoShape.getBegin(),ammoShape.getDirection());
        this.ammoShape = ammoShape;
        this.bodyId = bodyId;
        this.ammoId = ammoId;
    }

    public AmmoShape getAmmoShape() {
        return ammoShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getAmmoId() {
        return ammoId;
    }
}
