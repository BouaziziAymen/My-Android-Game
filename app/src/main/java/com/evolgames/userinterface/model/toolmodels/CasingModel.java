package com.evolgames.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.CasingProperties;
import com.evolgames.entities.serialization.infos.CasingInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.CasingField;
import com.evolgames.utilities.GeometryUtils;

public class CasingModel extends ProperModel<CasingProperties> {

    private final int bodyId;
    private final int casingId;
    private CasingShape casingShape;
    private CasingField casingField;

    public CasingModel(int bodyId, int casingId, String ammoName) {
        super(ammoName);
        this.bodyId = bodyId;
        this.casingId = casingId;
    }

    public CasingModel(int bodyId, int casingId, CasingShape casingShape) {
        super("Casing " + casingId);
        this.properties = new CasingProperties(casingShape.getBegin(), casingShape.getDirection());
        this.casingShape = casingShape;
        this.bodyId = bodyId;
        this.casingId = casingId;
    }

    public CasingShape getCasingShape() {
        return casingShape;
    }

    public void setCasingShape(CasingShape ammoShape) {
        this.casingShape = ammoShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getCasingId() {
        return casingId;
    }

    public CasingProperties getAmmoProperties() {
        return this.properties;
    }

    public CasingField getCasingField() {
        return casingField;
    }

    public void setCasingField(CasingField casingField) {
        this.casingField = casingField;
    }

    public CasingInfo toCasingInfo(boolean mirrored, GameEntity muzzleEntity) {

        Vector2 originProjected = this.properties.getAmmoOrigin().cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f);
        Vector2 ammoDir = new Vector2(this.properties.getAmmoDirection());
        boolean orientation = this.properties.isRotationOrientation();
        if (mirrored) {
            originProjected = GeometryUtils.mirrorPoint(originProjected);
            ammoDir.x = -ammoDir.x;
            orientation = !orientation;
        }
        CasingInfo casingInfo = new CasingInfo();
        casingInfo.setAmmoOrigin(originProjected);
        casingInfo.setLinearSpeed(this.properties.getLinearSpeed());
        casingInfo.setRotationOrientation(orientation);
        casingInfo.setAmmoDirection(ammoDir);
        casingInfo.setRotationSpeed(this.properties.getRotationSpeed());
        return casingInfo;
    }
}
