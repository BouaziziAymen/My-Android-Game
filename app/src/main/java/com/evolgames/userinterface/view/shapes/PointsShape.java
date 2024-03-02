package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;
import java.util.ArrayList;
import org.andengine.entity.primitive.LineLoop;

public class PointsShape extends OutlineShape<PointsModel<?>> {
  private final ArrayList<ModelPointImage> pointImages;
  private final ArrayList<ReferencePointImage> referencePointImages;
  private boolean pointsVisible;
  private boolean outlineVisible;

  public PointsShape(EditorUserInterface editorUserInterface) {
    super(editorUserInterface);
    pointImages = new ArrayList<>();
    referencePointImages = new ArrayList<>();
    setDepth(-10);
   updateZoom(editorUserInterface.getZoomFactor());
  }

  public ModelPointImage getPointImage(Vector2 p) {
    for (ModelPointImage pointImage : pointImages) {
      if (pointImage.getPoint() == p) {
        return pointImage;
      }
    }
    return null;
  }

  public ReferencePointImage getReferencePointImage(Vector2 p) {
    for (ReferencePointImage pointImage : referencePointImages) {
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

  public void detachReferencePointImages() {
    for (ReferencePointImage pointImage : referencePointImages) {
      editorUserInterface.detachReference(pointImage);
    }
    referencePointImages.clear();
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
    this.lineLoop = new LineLoop(0, 0, 2f, 100, ResourceManager.getInstance().vbom);
    lineLoop.setZIndex(10);
    lineLoop.setColor(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue());
    editorUserInterface.getScene().attachChild(lineLoop);
    editorUserInterface.getScene().sortChildren();

    updatePointImages();
    updateReferencePointImages();

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
        this.pointImages.add(pointImage);
      }
    }
    updateZoom(editorUserInterface.getZoomFactor());
  }

  public void createReferencePointImage(Vector2 center) {
    ReferencePointImage centerPointImage = new ReferencePointImage(center);
    referencePointImages.add(centerPointImage);
    centerPointImage.setScale(scaleX, scaleY);
    editorUserInterface.addReferencePoint(centerPointImage);
  }

  private void updateReferencePointImages() {
    for (Vector2 point : outlineModel.getReferencePoints()) {
      if (getReferencePointImage(point) == null) {
        createReferencePointImage(point);
      }
    }
  }

  @Override
  public void dispose() {
    referencePointImages.forEach(p -> editorUserInterface.detachReference(p));

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
