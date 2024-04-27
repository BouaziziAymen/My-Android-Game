package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.DragModel;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;
import java.util.List;

public class DragShape extends AngleIndicator implements MovablesContainer {
    private final EditorUserInterface editorUserInterface;
    private final ControllerPointImage originPoint;
    private final ControllerPointImage extentPoint1;
    private final ControllerPointImage extentPoint2;
    private DragModel model;
    private float extent1, extent2;

    public DragShape(Vector2 begin, EditorScene scene) {
        super(begin, scene, 32, 1);

        this.editorUserInterface = scene.getUserInterface();
        this.originPoint =
                new ControllerPointImage(
                        ResourceManager.getInstance().projectileDragTextureRegion, begin.cpy()) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        float x = DragShape.this.begin.x;
                        float y = DragShape.this.begin.y;
                        DragShape.this.updateBegin(x + dx, y + dy);
                        DragShape.this.drawSelf();
                    }
                };

        this.direction = new Vector2(1, 0);
        this.editorUserInterface.addElement(originPoint);
        this.extent1 = 0f;
        this.extentPoint1 =
                new ControllerPointImage(
                        ResourceManager.getInstance().diamondTextureRegion, new Vector2(begin)) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        extent1 += dy;
                        if (extent1 < 0) {
                            extent1 = 0f;
                        }
                        DragShape.this.updateExtent();
                        model.getProperties().setExtent1(extent1);
                        DragShape.this.drawSelf();
                    }
                };

        this.extent2 = 0f;
        this.extentPoint2 =
                new ControllerPointImage(
                        ResourceManager.getInstance().diamondTextureRegion, new Vector2(begin)) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        extent2 += dy;
                        if (extent2 > 0) {
                            extent2 = 0f;
                        }
                        DragShape.this.updateExtent();
                        model.getProperties().setExtent2(extent2);
                        DragShape.this.drawSelf();
                    }
                };
        originPoint.setDepth(1);
        extentPoint1.setDepth(2);
        extentPoint2.setDepth(2);

        this.editorUserInterface.addElement(extentPoint1);
        this.editorUserInterface.addElement(extentPoint2);

        this.updateZoom(scene.getUserInterface().getZoomFactor());
        editorUserInterface.setUpdated(true);
    }

    @Override
    public void updateZoom(float zoom) {
        super.updateZoom(zoom);
        this.originPoint.updateZoom(zoom);
        this.extentPoint1.updateZoom(zoom);
        this.extentPoint2.updateZoom(zoom);
    }

    @Override
    public void updateBegin(float x, float y) {
        super.updateBegin(x, y);
        this.model.getProperties().getDragOrigin().set(x, y);
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
        this.extentPoint1.setVisible(b);
        this.extentPoint2.setVisible(b);
    }

    @Override
    public List<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> movables = new ArrayList<>();
        if (moveLimits) {
            movables.add(extentPoint1);
            movables.add(extentPoint2);
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
        editorUserInterface.removeElement(extentPoint1);
        editorUserInterface.removeElement(extentPoint2);
    }

    @Override
    public void onTurnAroundCommand(float dA) {
        turnAround(dA);
        onTurnAround();
    }

    @Override
    public void updateDirection(Vector2 direction) {
        super.updateDirection(direction);
        model.getDragProperties().setDragNormal(direction);
        updateExtent();
    }

    private void updateExtent() {
        extentPoint1.setPosition(begin.x - extent1 * direction.y, begin.y + extent1 * direction.x);
        extentPoint1.setUpdated(true);
        extentPoint2.setPosition(begin.x - extent2 * direction.y, begin.y + extent2 * direction.x);
        extentPoint2.setUpdated(true);
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        updateExtent();
        this.angle = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        this.model.getProperties().setDragNormal(direction);
    }

    @Override
    public void drawLine() {
        while (lineStrip.getIndex() != 0) {
            lineStrip.shift();
            lineStrip.setIndex(lineStrip.getIndex() - 1);
        }
        lineStrip.add(extentPoint2.getPoint().x, extentPoint2.getPoint().y);
        lineStrip.add(extentPoint1.getPoint().x, extentPoint1.getPoint().y);
        lineStrip.add(begin.x, begin.y);
        lineStrip.add(end.x, end.y);
        lineStrip.setColor(mRed, mGreen, mBlue);
        lineStrip.setVisible(visible);
    }

    private void onTurnAround() {
        model.getProperties().getDragNormal().set(direction);
    }

    public DragModel getModel() {
        return model;
    }

    public void bindModel(DragModel model) {
        this.model = model;
        model.setDragShape(this);
        Vector2 begin = model.getProperties().getDragOrigin();
        updateBegin(begin.x, begin.y);
        updateDirection(model.getProperties().getDragNormal());
        this.extent1 = model.getProperties().getExtent1();
        this.extent2 = model.getProperties().getExtent2();
        updateExtent();
        this.drawSelf();
    }
}
