package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;
import java.util.List;

public class BombShape extends ControllerPointImage  implements MovablesContainer {
    private BombModel bombModel;
    private UserInterface userInterface;

    public BombShape(Vector2 point, GameScene scene) {
        super(ResourceManager.getInstance().bombShapeTextureRegion,point);
        this.userInterface = scene.getUserInterface();
        this.userInterface.addElement(this);
    }

    @Override
    protected void performControl(float dx, float dy) {
        this.setPosition(this.point.x+dx,this.point.y+dy);
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
    public void detach(){
        userInterface.removeElement(this);
    }
}
