package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import java.util.List;
import org.andengine.util.adt.color.Color;

public class DecorationModel extends PointsModel<DecorationProperties> {
  private static final float MINIMAL_DISTANCE_BETWEEN_VERTICES = 4f;
  private final LayerModel layerModel;
  private final int decorationId;
  private DecorationField field;

  public DecorationModel(LayerModel layerModel, int decorationId) {
    super("Decoration" + decorationId);
    this.decorationId = decorationId;
    this.layerModel = layerModel;
    properties = new DecorationProperties(new Color(Color.WHITE));
  }

  public String toString() {
    return "Decoration" + getDecorationId() + ": \n";
  }

  public int getDecorationId() {
    return decorationId;
  }

  @Override
  public boolean test(Vector2 movedPoint, float dx, float dy) {
    boolean inside =
        GeometryUtils.isPointInPolygon(movedPoint.x + dx, movedPoint.y + dy, layerModel.getOutlinePoints());
    if (!inside) return false;
    for (Vector2 p : getPoints())
      if (p != movedPoint)
        if (p.dst(movedPoint.x + dx, movedPoint.y + dy) < MINIMAL_DISTANCE_BETWEEN_VERTICES)
          return false;
    return true;
  }

  @Override
  public boolean test(List<Vector2> points) {
    for (Vector2 p : points) {
      if (!GeometryUtils.isPointInPolygon(p.x, p.y, layerModel.getOutlinePoints())) return false;
    }

    return true;
  }

  @Override
  public boolean test(float x, float y) {

    boolean inside = GeometryUtils.isPointInPolygon(x, y, layerModel.getOutlinePoints());
    if (!inside) return false;
    for (Vector2 p : getPoints()) if (p.dst(x, y) < MINIMAL_DISTANCE_BETWEEN_VERTICES) return false;
    return true;
  }

  public DecorationField getField() {
    return field;
  }

  public void setField(DecorationField field) {
    this.field = field;
  }

  public LayerModel getLayerModel() {
    return layerModel;
  }
}
