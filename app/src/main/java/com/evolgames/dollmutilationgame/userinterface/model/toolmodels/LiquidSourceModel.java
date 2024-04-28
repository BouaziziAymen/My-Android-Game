package com.evolgames.dollmutilationgame.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.serialization.infos.LiquidSourceInfo;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.properties.LiquidSourceProperties;
import com.evolgames.dollmutilationgame.entities.usage.Seal;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.LiquidSourceField;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.UUID;

public class LiquidSourceModel extends ProperModel<LiquidSourceProperties> {

    private final int bodyId;
    private final int liquidSourceId;
    private LiquidSourceShape liquidSourceShape;
    private LiquidSourceField liquidSourceField;
    private GameEntity containerEntity;
    private GameEntity sealEntity;

    public LiquidSourceModel(int bodyId, int liquidSourceId, String liquidSourceName) {
        super(liquidSourceName);
        this.bodyId = bodyId;
        this.liquidSourceId = liquidSourceId;
    }

    public LiquidSourceModel(int bodyId, int liquidSourceId, LiquidSourceShape liquidSourceShape) {
        super("Liquid Source " + liquidSourceId);
        this.properties =
                new LiquidSourceProperties(liquidSourceShape.getBegin(), liquidSourceShape.getDirection());
        this.liquidSourceShape = liquidSourceShape;
        this.bodyId = bodyId;
        this.liquidSourceId = liquidSourceId;
    }

    public void setContainerEntity(GameEntity containerEntity) {
        this.containerEntity = containerEntity;
    }

    public void setSealEntity(GameEntity sealEntity) {
        this.sealEntity = sealEntity;
    }

    public LiquidSourceShape getLiquidSourceShape() {
        return liquidSourceShape;
    }

    public void setLiquidSourceShape(LiquidSourceShape liquidSourceShape) {
        this.liquidSourceShape = liquidSourceShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getLiquidSourceId() {
        return liquidSourceId;
    }

    public LiquidSourceProperties getLiquidSourceProperties() {
        return this.properties;
    }

    public LiquidSourceField getLiquidSourceField() {
        return liquidSourceField;
    }

    public void setLiquidSourceField(LiquidSourceField liquidSourceField) {
        this.liquidSourceField = liquidSourceField;
    }

    public LiquidSourceInfo toLiquidSourceInfo(boolean mirrored) {
        LiquidSourceInfo lsi = new LiquidSourceInfo();
        Vector2 centredOrigin = this.properties
                .getLiquidSourceOrigin()
                .cpy()
                .sub(containerEntity.getCenter())
                .mul(1 / 32f);
        Vector2 direction = this.properties.getLiquidSourceDirection().cpy();
        if (mirrored) {
            centredOrigin = GeometryUtils.mirrorPoint(centredOrigin);
            direction.x = -direction.x;
        }
        lsi.setLiquidSourceInfoUniqueId(UUID.randomUUID().toString());
        lsi.setLiquidSourceOrigin(centredOrigin);
        lsi.setLiquidDirection(direction);
        lsi.setExtent(this.properties.getExtent());
        lsi.setContainerEntity(this.containerEntity);
        Seal seal = new Seal(sealEntity,lsi);
        this.sealEntity.getUseList().add(seal);
        lsi.setSeal(seal);
        lsi.setLiquid(this.properties.getLiquid());
        return lsi;
    }
}
