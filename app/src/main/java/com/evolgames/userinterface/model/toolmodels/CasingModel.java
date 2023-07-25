package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.CasingProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;

public class CasingModel extends ProperModel<CasingProperties> {

    private CasingShape casingShape;
    private final int bodyId;
    private final int casingId;

    public CasingModel(int bodyId, int casingId, String ammoName) {
        super(ammoName);
        this.bodyId = bodyId;
        this.casingId = casingId;
    }
    public CasingModel(int bodyId, int casingId, CasingShape casingShape) {
        super("Casing "+casingId);
        this.properties = new CasingProperties(casingShape.getBegin(),casingShape.getDirection());
        this.casingShape = casingShape;
        this.bodyId = bodyId;
        this.casingId = casingId;
    }


    public void setCasingShape(CasingShape ammoShape) {
        this.casingShape = ammoShape;
    }

    public CasingShape getCasingShape() {
        return casingShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getCasingId() {
        return casingId;
    }

    public CasingProperties getAmmoProperties(){
        return this.properties;
    }


}
