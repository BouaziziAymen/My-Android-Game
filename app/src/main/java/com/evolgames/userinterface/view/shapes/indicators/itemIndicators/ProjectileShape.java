package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;

import org.andengine.entity.primitive.Line;

import java.util.ArrayList;

import is.kul.learningandengine.helpers.Utils;

public class ProjectileShape extends AngleIndicator implements MovablesContainer {
    private final UserInterface userInterface;
    private final ControllerPointImage originPoint;
    private final ControllerPointImage upperLimitPoint;
    private ProjectileModel model;
    private float upperLimit;
    private Line limitLine;

    public ProjectileShape(Vector2 begin, GameScene scene) {
        super(begin, scene, 32, 1);

        this.userInterface = scene.getUserInterface();
        originPoint = new ControllerPointImage(ResourceManager.getInstance().aimCircleTextureRegion, begin.cpy()) {
            @Override
            protected void performControl(float dx, float dy) {
                float x = ProjectileShape.this.begin.x;
                float y = ProjectileShape.this.begin.y;
                ProjectileShape.this.setBegin(x + dx, y + dy);
                ProjectileShape.this.drawSelf();
                ProjectileShape.this.updateUpperLimitPosition();
            }
        };
        userInterface.addElement(originPoint);

        upperLimitPoint = new ControllerPointImage(ResourceManager.getInstance().diskTextureRegion, begin.cpy()) {

            @Override
            protected void performControl(float dx, float dy) {
                upperLimit += dy;
                updateUpperLimitPosition();
            }
        };
        userInterface.addElement(upperLimitPoint);


        userInterface.setUpdated(true);
    }

    private void updateUpperLimitPosition() {
        Vector2 position = begin.cpy().add(dir.x * upperLimit, dir.y * upperLimit);
        upperLimitPoint.setPosition(position.x, position.y);
        updateLimitLine();
    }

    private void updateLimitLine() {
        if (limitLine != null) limitLine.detachSelf();
        limitLine = new Line(begin.x, begin.y, upperLimitPoint.getPoint().x, upperLimitPoint.getPoint().y, ResourceManager.getInstance().vbom);
        creationScene.attachChild(limitLine);
        limitLine.setZIndex(10);
        limitLine.setColor(1, 0, 0);
        limitLine.setLineWidth(3);
        creationScene.sortChildren();
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
        if (moveLimits)
            movables.add(upperLimitPoint);
        else {
            movables.add(getLimit());
            movables.add(originPoint);
        }
        return movables;
    }

    @Override
    public void detach() {
        super.detach();
        if(limitLine!=null)
        limitLine.detachSelf();
        userInterface.removeElement(originPoint);
        userInterface.removeElement(upperLimitPoint);
    }

    @Override
    public void onTurnAroundCommand(float dA) {
        turnAround(dA);
        updateUpperLimitPosition();
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        super.onControllerMoved(dx, dy);
    }

    public ProjectileModel getModel() {
        return model;
    }

    public void setModel(ProjectileModel model) {
        this.model = model;
    }

}
