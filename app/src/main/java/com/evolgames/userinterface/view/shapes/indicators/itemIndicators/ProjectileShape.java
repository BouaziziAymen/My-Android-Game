package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import org.andengine.entity.primitive.Line;
import java.util.ArrayList;

public class ProjectileShape extends AngleIndicator implements MovablesContainer {
    private final UserInterface userInterface;
    private final ControllerPointImage originPoint;
    private ProjectileModel  model;

    public ProjectileShape(Vector2 begin, GameScene scene) {
        super(begin, scene, 32, 1);

        this.userInterface = scene.getUserInterface();
        this.originPoint = new ControllerPointImage(ResourceManager.getInstance().aimCircleTextureRegion, begin.cpy()) {
            @Override
            protected void performControl(float dx, float dy) {
                float x = ProjectileShape.this.begin.x;
                float y = ProjectileShape.this.begin.y;
                model.getProperties().getProjectileOrigin().set(x,y);
                ProjectileShape.this.updateBegin(x + dx, y + dy);
                ProjectileShape.this.drawSelf();
            }
        };
        this.direction = new Vector2();
        this.userInterface.addElement(originPoint);


        userInterface.setUpdated(true);
    }


    public Vector2 getBegin(){
        return this.begin;
    }
    public Vector2 getDirection(){
        return this.direction;
    }

    @Override
    public void select() {
        super.select();
        originPoint.select();
    }

    @Override
    public void release() {
        super.release();
        originPoint.release();
    }


    @Override
    public ArrayList<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> movables = new ArrayList<>();

            movables.add(getLimit());
            movables.add(originPoint);

        return movables;
    }

    @Override
    public void detach() {
        super.detach();
        userInterface.removeElement(originPoint);
    }

    @Override
    public void onTurnAroundCommand(float dA) {
        turnAround(dA);
        onTurnAround();
    }

    private void onTurnAround(){
        model.getProperties().getProjectileDirection().set(direction);
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        super.onControllerMoved(dx, dy);
    }

    public ProjectileModel getModel() {
        return model;
    }

    public void bindModel(ProjectileModel model) {
        this.model = model;
        updateDirection(model.getProperties().getProjectileDirection());
        model.setProjectileShape(this);
        onTurnAround();
    }

}
