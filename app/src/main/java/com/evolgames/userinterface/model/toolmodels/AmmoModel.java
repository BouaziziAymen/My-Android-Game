package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.AmmoProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.AmmoShape;

public class AmmoModel extends ProperModel<AmmoProperties> {

    private AmmoShape ammoShape;
    private final int bodyId;
    private final int ammoId;
    private float rotationSpeed;
    private boolean rotationOrientation;
    private float linearSpeed;

    public AmmoModel(int bodyId, int ammoId,String ammoName) {
        super(ammoName);
        this.bodyId = bodyId;
        this.ammoId = ammoId;
    }
    public AmmoModel(int bodyId, int ammoId, AmmoShape ammoShape) {
        super("Ammo "+ammoId);
        this.properties = new AmmoProperties(ammoShape.getBegin(),ammoShape.getDirection());
        this.ammoShape = ammoShape;
        this.bodyId = bodyId;
        this.ammoId = ammoId;
    }


    public void setAmmoShape(AmmoShape ammoShape) {
        this.ammoShape = ammoShape;
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

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationOrientation(boolean rotationOrientation) {
        this.rotationOrientation = rotationOrientation;
    }

    public boolean getRotationOrientation() {
        return rotationOrientation;
    }

    public float getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(float linearSpeed) {
        this.linearSpeed = linearSpeed;
    }
}
