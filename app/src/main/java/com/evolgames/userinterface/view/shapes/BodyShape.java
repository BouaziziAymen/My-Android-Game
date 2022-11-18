package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.entity.primitive.LineLoop;

public class BodyShape extends OutlineShape<BodyModel> {


    public BodyShape(UserInterface userInterface) {
        super(userInterface);
    }

    @Override
    public void onModelUpdated() {
        updateOutlineShape();
    }

    @Override
    protected void updateOutlineShape() {
        Vector2[] points = outlineModel.getOutlinePoints();
        if (lineLoop != null) {
            lineLoop.detachSelf();
        }
        this.lineLoop = new LineLoop(0, 0, 2f, 100, ResourceManager.getInstance().vbom);
        lineLoop.setZIndex(2);
        lineLoop.setColor(r, 0, 0);
        userInterface.getScene().attachChild(lineLoop);
        userInterface.getScene().sortChildren();

        if (points != null) {
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
        }
        setUpdated(true);
    }

    @Override
    public void dispose() {
        if (lineLoop != null) {
            lineLoop.detachSelf();
            lineLoop.dispose();
        }
    }

}
