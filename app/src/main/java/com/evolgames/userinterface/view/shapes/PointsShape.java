package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;

import org.andengine.entity.primitive.LineLoop;

import java.util.ArrayList;

public class PointsShape extends OutlineShape<PointsModel<?>> {
    private final ArrayList<ModelPointImage> pointImages;
    private ArrayList<ReferencePointImage> centerPointImages = new ArrayList<>();
    private boolean pointsVisible;
    private boolean outlineVisible;


    public PointsShape(UserInterface userInterface) {
        super(userInterface);
        pointImages = new ArrayList<>();
        setDepth(-10);
        setScale(0.5f / userInterface.getZoomFactor(), 0.5f / userInterface.getZoomFactor());
    }

    public ModelPointImage getPointImage(Vector2 p) {
        for (ModelPointImage pointImage : pointImages) {
            if (pointImage.getPoint() == p) {
                return pointImage;
            }
        }
        return null;
    }

    public ArrayList<PointImage> getMovablePointImages() {
        return new ArrayList<>(pointImages);
    }

    @Override
    public void onModelUpdated() {
        userInterface.getToolModel().updateMesh();
        updateOutlineShape();
    }

    public void detachPointImages() {
        for (ModelPointImage pointImage : pointImages) {
            removeElement(pointImage);
        }
        pointImages.clear();
    }

    @Override
    public void setScale(float pScaleX, float pScaleY) {
        super.setScale(pScaleX, pScaleY);
        for (ModelPointImage pointImage : pointImages) {
            pointImage.setScale(pScaleX, pScaleY);
        }
    }

    @Override
    public void updateOutlineShape() {
        Vector2[] points = outlineModel.getOutlinePoints();
        if (lineLoop != null) {
            lineLoop.detachSelf();
        }
        this.lineLoop = new LineLoop(0, 0, 4f, 100, ResourceManager.getInstance().vbom);
        lineLoop.setZIndex(2);
        lineLoop.setColor(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue());
        userInterface.getScene().attachChild(lineLoop);
        userInterface.getScene().sortChildren();

        for (Vector2 point : outlineModel.getModelPoints()) {
            if (getPointImage(point) == null) {
                ModelPointImage pointImage = new ModelPointImage(this, ResourceManager.getInstance().diskTextureRegion, point);
                pointImage.setScale(scaleX, scaleY);
                addElement(pointImage);
                this.pointImages.add(pointImage);
            }
        }

        if (points != null) {
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
        }

        setOutlineVisible(outlineVisible);
        setPointsVisible(pointsVisible);

        lineLoop.setColor(lineColor.getRed(),lineLoop.getGreen(),lineColor.getBlue());
        setUpdated(true);
    }

    @Override
    public void dispose() {
        centerPointImages.forEach(p-> userInterface.detachReference(p));


        if (lineLoop != null) {
            lineLoop.detachSelf();
            lineLoop.dispose();
        }
    }

    public void resume() {
        updateOutlineShape();
    }


    public ArrayList<ReferencePointImage> getCenterPointImages() {
        return centerPointImages;
    }


    public void setPointsVisible(boolean visible) {
        pointImages.forEach(e -> e.setVisible(visible));
        pointsVisible = visible;
    }
    public void setOutlineVisible(boolean visible) {
      if(lineLoop!=null){lineLoop.setVisible(visible);}
      outlineVisible = visible;
    }


    @Override
    public void setVisible(boolean visible) {
        pointImages.forEach(e -> e.setVisible(visible));
        setOutlineVisible(visible);
        outlineVisible = visible;
    }


}
