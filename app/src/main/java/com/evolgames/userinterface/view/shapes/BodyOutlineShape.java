package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.entity.primitive.LineLoop;
import org.andengine.entity.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class BodyOutlineShape {
    private List<LineLoop> lineLoops;
    private final Scene gameScene;
    private float r = 1f, g = 0f, b = 0f;
    private final BodyModel bodyModel;
    private boolean selected;

    public BodyOutlineShape(UserInterface userInterface, BodyModel bodyModel) {
        this.gameScene = userInterface.getScene();
        this.bodyModel = bodyModel;
    }


    public void setLineLoopColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        if (lineLoops != null)
            for (LineLoop lineLoop : lineLoops) {
                lineLoop.setColor(r, g, b);
            }
    }

    public void setLineLoopColor(Color c) {
        setLineLoopColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    public BodyModel getBodyModel() {
        return bodyModel;
    }


    public void onModelUpdated() {
        updateSelf();
    }

    public void updateSelf() {

        if (lineLoops == null) {
            lineLoops = new ArrayList<>();
        }
        for (LineLoop lineLoop : lineLoops) {
            lineLoop.detachSelf();
        }
        List<List<Vector2>> polygons = bodyModel.getPolygons();
        if(polygons==null)return;
        List<List<Vector2>> result = GeometryUtils.mergePolygons(polygons);
        for (List<Vector2> points : result) {
            LineLoop lineLoop = new LineLoop(0, 0, 4, 1000, ResourceManager.getInstance().vbom);
            lineLoop.setZIndex(3);
            lineLoop.setColor(r, g, b);
            gameScene.attachChild(lineLoop);
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
            lineLoops.add(lineLoop);
            lineLoop.setVisible(selected);
        }
        gameScene.sortChildren();
    }

    public void select(Color color) {
        selected = true;
        if (lineLoops != null) {
            for (LineLoop lineLoop : lineLoops) {
                lineLoop.setVisible(true);
            }
        }

        setLineLoopColor(color);
    }

    public void deselect() {
        selected = false;
        if (lineLoops != null) {
            for (LineLoop lineLoop : lineLoops) {
                lineLoop.setVisible(false);
            }
        }
    }
}
