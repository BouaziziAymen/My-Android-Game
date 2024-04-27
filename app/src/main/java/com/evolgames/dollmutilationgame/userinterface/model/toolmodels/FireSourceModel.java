package com.evolgames.dollmutilationgame.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.serialization.infos.FireSourceInfo;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.properties.FireSourceProperties;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.FireSourceField;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

public class FireSourceModel extends ProperModel<FireSourceProperties> {

    private final int bodyId;
    private final int fireSourceId;
    private FireSourceShape fireSourceShape;
    private FireSourceField fireSourceField;
    private GameEntity muzzleEntity;

    public FireSourceModel(int bodyId, int fireSourceId, String fireSourceName) {
        super(fireSourceName);
        this.bodyId = bodyId;
        this.fireSourceId = fireSourceId;
    }

    public FireSourceModel(int bodyId, int fireSourceId, FireSourceShape fireSourceShape) {
        super("Fire Source " + fireSourceId);
        this.properties =
                new FireSourceProperties(fireSourceShape.getBegin(), fireSourceShape.getDirection());
        this.fireSourceShape = fireSourceShape;
        this.bodyId = bodyId;
        this.fireSourceId = fireSourceId;
    }

    public void setMuzzleEntity(GameEntity muzzleEntity) {
        this.muzzleEntity = muzzleEntity;
    }

    public FireSourceShape getFireSourceShape() {
        return fireSourceShape;
    }

    public void setFireSourceShape(FireSourceShape fireSourceShape) {
        this.fireSourceShape = fireSourceShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getFireSourceId() {
        return fireSourceId;
    }

    public FireSourceProperties getFireSourceProperties() {
        return this.properties;
    }

    public FireSourceField getFireSourceField() {
        return fireSourceField;
    }

    public void setFireSourceField(FireSourceField fireSourceField) {
        this.fireSourceField = fireSourceField;
    }


    public FireSourceInfo toFireSourceInfo(boolean mirrored) {
        FireSourceInfo fireSourceInfo = new FireSourceInfo();

        Vector2 centredOrigin = this.properties
                .getFireSourceOrigin()
                .cpy()
                .sub(muzzleEntity.getCenter())
                .mul(1 / 32f);
        Vector2 direction = this.properties.getFireSourceDirection().cpy();
        if (mirrored) {
            centredOrigin.set(GeometryUtils.mirrorPoint(centredOrigin));
            direction.x = -direction.x;
        }
        fireSourceInfo.setFireSourceOrigin(centredOrigin);
        fireSourceInfo.setFireDirection(direction);
        fireSourceInfo.setFireRatio(this.properties.getFireRatio());
        fireSourceInfo.setSmokeRatio(this.properties.getSmokeRatio());
        fireSourceInfo.setSparkRatio(this.properties.getSparkRatio());
        fireSourceInfo.setHeat(this.properties.getHeatRatio());
        fireSourceInfo.setParticles(this.properties.getParticles());
        fireSourceInfo.setSpeed(this.properties.getSpeedRatio());
        fireSourceInfo.setMuzzleEntity(this.muzzleEntity);
        fireSourceInfo.setExtent(this.properties.getExtent());
        return fireSourceInfo;
    }
}
