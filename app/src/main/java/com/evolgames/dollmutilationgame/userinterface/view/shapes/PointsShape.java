package com.evolgames.dollmutilationgame.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ReferencePointImage;

import org.andengine.entity.primitive.LineLoop;

import java.util.ArrayList;

public class PointsShape extends OutlineShape<PointsModel<?>> {
    private final ArrayList<ModelPointImage> pointImages;
    private ReferencePointImage referencePointImage;
    private boolean pointsVisible;
    private boolean outlineVisible;

    public PointsShape(EditorUserInterface editorUserInterface) {
        super(editorUserInterface);
        pointImages = new ArrayList<>();
        referencePointImage = null;
        setDepth(-10);
        updateZoom(editorUserInterface.getZoomFactor());
    }

    public ReferencePointImage getReferencePointImage() {
      return referencePointImage;
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
        editorUserInterface.getToolModel().updateMesh();
        updateOutlineShape();
    }

    public void detachReferencePointImage() {
        if (referencePointImage!=null) {
            editorUserInterface.detachReference(referencePointImage);
        }
     referencePointImage = null;
    }

    public void detachPointImages() {
        for (ModelPointImage pointImage : pointImages) {
            removeElement(pointImage);
        }
        pointImages.clear();
    }

    @Override
    public void updateOutlineShape() {
        Vector2[] points = outlineModel.getOutlinePoints();
        if (lineLoop != null) {
            lineLoop.detachSelf();
        }
        this.lineLoop = new LineLoop(0, 0, 3f, 100, ResourceManager.getInstance().vbom);
        lineLoop.setZIndex(10);
        lineLoop.setColor(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue());
        editorUserInterface.getScene().attachChild(lineLoop);
        editorUserInterface.getScene().sortChildren();

        updatePointImages();

        if (points != null) {
            for (Vector2 point : points) {
                lineLoop.add(point.x, point.y);
            }
        }

        setOutlineVisible(outlineVisible);
        setPointsVisible(pointsVisible);

        lineLoop.setColor(lineColor.getRed(), lineLoop.getGreen(), lineColor.getBlue());
        setUpdated(true);
    }

    private void updatePointImages() {
        for (Vector2 point : outlineModel.getPoints()) {
            if (getPointImage(point) == null) {
                ModelPointImage pointImage =
                        new ModelPointImage(this, ResourceManager.getInstance().diskTextureRegion, point);
                addElement(pointImage);
                pointImage.setPointsShape(this);
                pointImage.setDepth(50);
                this.pointImages.add(pointImage);
            }
        }
        updateZoom(editorUserInterface.getZoomFactor());
    }

    public void createReferencePointImage(Vector2 center) {
        referencePointImage = new ReferencePointImage(center);
        referencePointImage.setScale(scaleX, scaleY);
        editorUserInterface.addReferencePoint(referencePointImage);
        referencePointImage.updateZoom(editorUserInterface.getZoomFactor());
    }

    @Override
    public void dispose() {
     if(referencePointImage!=null){
         editorUserInterface.detachReference(referencePointImage);
     }

        if (lineLoop != null) {
            lineLoop.detachSelf();
            lineLoop.dispose();
        }
    }

    public void resume() {
        updateOutlineShape();
    }

    public void setPointsVisible(boolean visible) {
        pointImages.forEach(e -> e.setVisible(visible));
        pointsVisible = visible;
    }

    public void setOutlineVisible(boolean visible) {
        if (lineLoop != null) {
            lineLoop.setVisible(visible);
        }
        outlineVisible = visible;
        editorUserInterface.getScene().sortChildren();
    }

    @Override
    public void setVisible(boolean visible) {
        pointImages.forEach(e -> e.setVisible(visible));
        setOutlineVisible(visible);
        outlineVisible = visible;
    }
}
