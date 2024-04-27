package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.BombModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;

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
