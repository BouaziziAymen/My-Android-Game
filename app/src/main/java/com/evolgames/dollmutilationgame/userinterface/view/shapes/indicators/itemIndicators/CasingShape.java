package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.CasingModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;
import java.util.List;

public class CasingShape extends AngleIndicator implements MovablesContainer {
    private final EditorUserInterface editorUserInterface;
    private final ControllerPointImage originPoint;
    private CasingModel model;

    public CasingShape(Vector2 begin, EditorScene scene) {
        super(begin, scene, 32, 1);

        this.editorUserInterface = scene.getUserInterface();
        this.originPoint =
                new ControllerPointImage(
                        ResourceManager.getInstance().casingShapeTextureRegion, begin.cpy()) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        float x = CasingShape.this.begin.x;
                        float y = CasingShape.this.begin.y;
                        model.getProperties().getAmmoOrigin().set(x, y);
                        CasingShape.this.updateBegin(x + dx, y + dy);
                        CasingShape.this.drawSelf();
                    }
                };

        this.direction = new Vector2();
        this.editorUserInterface.addElement(originPoint);
        this.updateZoom(scene.getUserInterface().getZoomFactor());
        editorUserInterface.setUpdated(true);
    }

    @Override
    public void updateZoom(float zoom) {
        super.updateZoom(zoom);
        this.originPoint.updateZoom(zoom);
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
        super.setVisible(false);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        this.originPoint.setVisible(b);
    }

    @Override
    public List<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> movables = new ArrayList<>();
        movables.add(getLimit());
        movables.add(originPoint);
        return movables;
    }

    @Override
    public void detach() {
        super.detach();
        editorUserInterface.removeElement(originPoint);
    }

    @Override
    public void onTurnAroundCommand(float dA) {
        turnAround(dA);
        onTurnAround();
    }

    private void onTurnAround() {
        model.getProperties().getAmmoDirection().set(direction);
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        this.angle = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        model.getProperties().getAmmoDirection().set(direction);
    }

    public CasingModel getModel() {
        return model;
    }

    public void bindModel(CasingModel model) {
        this.model = model;
        model.setCasingShape(this);
        Vector2 begin = model.getProperties().getAmmoOrigin();
        updateBegin(begin.x, begin.y);
        updateDirection(model.getProperties().getAmmoDirection());
        this.drawSelf();
    }
}
