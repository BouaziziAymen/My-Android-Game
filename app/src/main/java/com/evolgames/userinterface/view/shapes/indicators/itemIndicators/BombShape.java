package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;
import java.util.List;

public class BombShape extends ControllerPointImage implements MovablesContainer {
    private final EditorUserInterface editorUserInterface;
    private BombModel bombModel;

    public BombShape(Vector2 point, EditorScene scene) {
        super(ResourceManager.getInstance().bombShapeTextureRegion, point);
        this.editorUserInterface = scene.getUserInterface();
        this.editorUserInterface.addElement(this);
        this.updateZoom(scene.getUserInterface().getZoomFactor());
    }

    @Override
    protected void performControl(float dx, float dy) {
        this.setPosition(this.point.x + dx, this.point.y + dy);
        bombModel.getProperties().setBombPosition(point);
    }

    @Override
    public List<PointImage> getMovables(boolean moveLimits) {
        List<PointImage> pointImages = new ArrayList<>();
        pointImages.add(this);
        return pointImages;
    }

    public void bindModel(BombModel bombModel) {
        this.bombModel = bombModel;
        this.bombModel.getProperties().setBombPosition(point);
        bombModel.setBombShape(this);
    }

    public void detach() {
        editorUserInterface.removeElement(this);
    }
}
