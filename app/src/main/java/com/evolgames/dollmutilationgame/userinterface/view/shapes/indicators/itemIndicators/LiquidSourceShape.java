package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;
import java.util.List;

public class LiquidSourceShape extends AngleIndicator implements MovablesContainer {
    private final EditorUserInterface editorUserInterface;
    private final ControllerPointImage originPoint;
    private final ControllerPointImage extentPoint;
    private LiquidSourceModel model;
    private float extent;

    public LiquidSourceShape(Vector2 begin, EditorScene scene) {
        super(begin, scene, 32, 1);

        this.editorUserInterface = scene.getUserInterface();
        this.originPoint =
                new ControllerPointImage(
                        ResourceManager.getInstance().liquidShapeTextureRegion, begin.cpy()) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        float x = LiquidSourceShape.this.begin.x;
                        float y = LiquidSourceShape.this.begin.y;
                        LiquidSourceShape.this.updateBegin(x + dx, y + dy);
                        LiquidSourceShape.this.drawSelf();
                    }
                };

        this.direction = new Vector2(1, 0);
        this.editorUserInterface.addElement(originPoint);
        this.extent = 0f;
        this.extentPoint =
                new ControllerPointImage(
                        ResourceManager.getInstance().diamondTextureRegion, new Vector2(begin)) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        extent += dy;
                        if (extent < 0) {
                            extent = 0f;
                        }
                        if (extent > 8f) {
                            extent = 8f;
                        }
                        LiquidSourceShape.this.updateExtent();
                        model.getProperties().setExtent(extent);
                        LiquidSourceShape.this.drawSelf();
                    }
                };
        originPoint.setDepth(1);
        extentPoint.setDepth(2);
        this.editorUserInterface.addElement(extentPoint);
        updateZoom(this.editorUserInterface.getZoomFactor());
        editorUserInterface.setUpdated(true);
    }

    @Override
    public void updateZoom(float zoom) {
        super.updateZoom(zoom);
        this.extentPoint.updateZoom(zoom);
        this.originPoint.updateZoom(zoom);
    }

    @Override
    public void updateBegin(float x, float y) {
        super.updateBegin(x, y);
        this.model.getProperties().getLiquidSourceOrigin().set(x, y);
        updateExtent();
    }

    public Vector2 getBegin() {
        return this.begin;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    @Override
    public void select() {
        super.select();
        originPoint.select();
        originPoint.setDepth(2);
        setVisible(true);
    }

    @Override
    public void release() {
        super.release();
        originPoint.release();
        originPoint.setDepth(1);
        this.setVisible(false);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        this.extentPoint.setVisible(b);
    }

    @Override
    public List<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> movables = new ArrayList<>();
        if (moveLimits) {
            movables.add(extentPoint);
        } else {
            movables.add(getLimit());
            movables.add(originPoint);
        }
        return movables;
    }

    @Override
    public void detach() {
        super.detach();
        editorUserInterface.removeElement(originPoint);
        editorUserInterface.removeElement(extentPoint);
    }

    @Override
    public void onTurnAroundCommand(float dA) {
        turnAround(dA);
        onTurnAround();
    }

    @Override
    public void updateDirection(Vector2 direction) {
        super.updateDirection(direction);
        model.getLiquidSourceProperties().setLiquidSourceDirection(direction);
        updateExtent();
    }

    private void updateExtent() {
        extentPoint.setPosition(begin.x - extent * direction.y, begin.y + extent * direction.x);
        extentPoint.setUpdated(true);
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        updateExtent();
        this.angle = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        this.model.getProperties().setLiquidSourceDirection(direction);
    }

    @Override
    public void drawLine() {
        while (lineStrip.getIndex() != 0) {
            lineStrip.shift();
            lineStrip.setIndex(lineStrip.getIndex() - 1);
        }
        lineStrip.add(extentPoint.getPoint().x, extentPoint.getPoint().y);
        lineStrip.add(begin.x, begin.y);
        lineStrip.add(end.x, end.y);
        lineStrip.setColor(mRed, mGreen, mBlue);
        lineStrip.setVisible(visible);
    }

    private void onTurnAround() {
        model.getProperties().getLiquidSourceDirection().set(direction);
    }

    public LiquidSourceModel getModel() {
        return model;
    }

    public void bindModel(LiquidSourceModel model) {
        this.model = model;
        updateDirection(model.getProperties().getLiquidSourceDirection());
        Vector2 begin = model.getProperties().getLiquidSourceOrigin();
        updateBegin(begin.x, begin.y);
        this.extent = model.getProperties().getExtent();
        updateExtent();
        this.drawSelf();
        model.setLiquidSourceShape(this);
    }
}
