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
import java.util.Arrays;
import java.util.List;

public class BodyOutlineShape {
    private List<LineLoop> lineLoops;
    private Scene gameScene;
    private float r = 1f, g = 0f, b = 0f;
    private BodyModel bodyModel;

    public BodyOutlineShape(UserInterface userInterface, BodyModel bodyModel) {
        this.gameScene = userInterface.getScene();
        this.bodyModel = bodyModel;
    }


    public void setLineLoopColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        if(lineLoops!=null)
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

        List<List<Vector2>> polygons = bodyModel.getPolygons();
        System.out.println("Update Body Outline Shape:" + polygons.size());
        List<List<Vector2>> result = GeometryUtils.mergePolygons(polygons);

        if (lineLoops == null) {
            lineLoops = new ArrayList<>();
        }
        for (LineLoop lineLoop : lineLoops) {
            lineLoop.detachSelf();
        }

        for (List<Vector2> points : result) {
            System.out.println("---------------");
            System.out.println(Arrays.toString(points.toArray()));
            LineLoop lineLoop = new LineLoop(0, 0, 4, 1000, ResourceManager.getInstance().vbom);
            lineLoop.setZIndex(3);
            lineLoop.setColor(r, g, b);
            gameScene.attachChild(lineLoop);
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
            lineLoops.add(lineLoop);
        }

        gameScene.sortChildren();
    }

    public void select() {
        if (lineLoops != null)
            for (LineLoop lineLoop : lineLoops) {
                lineLoop.setVisible(true);
            }

        setLineLoopColor(Colors.palette1_gold);
    }

    public void deselect() {
        if (lineLoops != null)
            for (LineLoop lineLoop : lineLoops) {
                lineLoop.setVisible(false);
            }
    }
}
