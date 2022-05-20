package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;

import org.andengine.entity.primitive.LineLoop;
import org.andengine.entity.scene.Scene;

import java.util.ArrayList;

public class PointsShape extends Container {
    private final UserInterface userInterface;
    private final Scene creationScene;
    private LineLoop lineLoop;
    private PointsModel shapePointsModel;
    private final ArrayList<ModelPointImage> pointImages;
    private ReferencePointImage centerPointImage;
    private boolean selected;
    private float r = 1f, g = 1f, b = 1f;


    public PointsShape(UserInterface userInterface) {
        creationScene = userInterface.getScene();
        this.userInterface = userInterface;
        pointImages = new ArrayList<>();
        setDepth(-10);
        setScale( 0.5f/ userInterface.getZoomFactor(), 0.5f / userInterface.getZoomFactor());
    }

    public void setLineLoopColor(float r, float g, float b) {
        if (lineLoop != null) lineLoop.setColor(r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setLineLoopColor(Color c) {
        setLineLoopColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    public PointsModel getShapePointsModel() {
        return shapePointsModel;
    }

    public void setShapePointsModel(PointsModel shapePointsModel) {
        this.shapePointsModel = shapePointsModel;
    }

    public ModelPointImage getPointImage(Vector2 p) {
        for (ModelPointImage pointImage : pointImages)
            if (pointImage.getPoint() == p) return pointImage;
        return null;
    }

    public ArrayList<PointImage> getMovablePointImages() {
        return new ArrayList<>(pointImages);
    }

    public void onModelUpdated() {
        shapePointsModel.onShapeUpdated();
        userInterface.getToolModel().updateMesh();
        updateSelf();
    }

    public void detachPointImages() {
        for (ModelPointImage pointImage : pointImages) removeElement(pointImage);

        pointImages.clear();
    }

    @Override
    public void setScale(float pScaleX, float pScaleY) {
        super.setScale(pScaleX, pScaleY);
        for (ModelPointImage pointImage : pointImages) pointImage.setScale(pScaleX, pScaleY);
    }

    public void updateSelf() {
        Vector2[] points = shapePointsModel.getOutlinePoints();
        if (lineLoop != null) {
            lineLoop.detachSelf();
        }
        this.lineLoop = new LineLoop(0, 0, 4, 100, ResourceManager.getInstance().vbom);
        lineLoop.setZIndex(2);
        lineLoop.setColor(r, g, b);
        creationScene.attachChild(lineLoop);
        creationScene.sortChildren();

        for (Vector2 point : shapePointsModel.getPoints()) {
            if (getPointImage(point) == null) {
                ModelPointImage pointImage = new ModelPointImage(this, ResourceManager.getInstance().diskTextureRegion, point);
                pointImage.setScale(scaleX, scaleY);
                addElement(pointImage);
                this.pointImages.add(pointImage);
            }
        }

        if (points != null)
            for (Vector2 point : points) {

                lineLoop.add(point.x, point.y);
            }

        setUpdated(true);
    }

    public void dispose() {
        if (centerPointImage != null) userInterface.detachReference(centerPointImage);
        if (lineLoop != null) {
            lineLoop.detachSelf();
            lineLoop.dispose();

        }
    }

    public void resume() {
        updateSelf();
    }


    public ReferencePointImage getCenterPointImage() {
        return centerPointImage;
    }

    public void addCenterPointImage(ReferencePointImage centerPointImage) {
        this.centerPointImage = centerPointImage;
    }
    public void setVisible(boolean visible){
        pointImages.forEach(e->e.setVisible(visible));
        if(lineLoop!=null)lineLoop.setVisible(visible);
    }
    public void select() {
        selected = true;
    }
    public void deselect(){
        selected = false;
    }
    public boolean isSelected(){
        return selected;
    }
}
