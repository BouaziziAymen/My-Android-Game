package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.controllers.Movable;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.scenes.EditorScene;

public abstract class VectorIndicator extends ArrowShape implements Movable {

    private final ControllerPointImage limit;

    public VectorIndicator(Vector2 begin, EditorScene scene, int size) {
        super(begin, scene, size);
        limit =
                new ControllerPointImage(ResourceManager.getInstance().diamondTextureRegion, end) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        VectorIndicator.this.onControllerMoved(dx, dy);
                    }
                };

        scene.getUserInterface().addElement(limit);
    }

    public void updateZoom(float zoom) {
        this.limit.updateZoom(zoom);
    }

    public ControllerPointImage getLimit() {
        return limit;
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        updateLimit(end.x, end.y);
    }

    @Override
    public void updateBegin(float x, float y) {
        super.updateBegin(x, y);
        drawLimit();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        limit.setVisible(b);
    }

    public void drawLimit() {
        updateLimit(end.x, end.y);
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        updateEnd(end.x + dx, end.y + dy);
    }

    public void updateLimit(float x, float y) {
        limit.setPosition(x, y);
        limit.setUpdated(true);
    }

    @Override
    public void detach() {
        super.detach();
        creationScene.getUserInterface().removeElement(limit);
    }
}
