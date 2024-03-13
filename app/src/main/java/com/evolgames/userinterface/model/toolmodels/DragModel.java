package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.DragProperties;
import com.evolgames.entities.serialization.infos.DragInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.DragShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.DragField;

public class DragModel extends ProperModel<DragProperties> {

  private final int bodyId;
  private final int dragId;
  private DragShape dragShape;
  private DragField dragField;
  private GameEntity draggedEntity;

  public DragModel(int bodyId, int dragId) {
    super("Drag " + dragId);
    this.bodyId = bodyId;
    this.dragId = dragId;
  }

  public DragModel(int bodyId, int dragId, DragShape dragShape) {
    super("Drag " + dragId);
    this.properties = new DragProperties(dragShape.getBegin(), dragShape.getDirection());
    this.dragShape = dragShape;
    this.bodyId = bodyId;
    this.dragId = dragId;
  }

  public int getBodyId() {
    return bodyId;
  }

  public int getDragId() {
    return dragId;
  }

  public DragShape getDragShape() {
    return dragShape;
  }

  public void setDragShape(DragShape dragShape) {
    this.dragShape = dragShape;
  }

  public DragField getDragField() {
    return dragField;
  }

  public void setDragField(DragField dragField) {
    this.dragField = dragField;
  }

  public GameEntity getDraggedEntity() {
    return draggedEntity;
  }

  public void setDraggedEntity(GameEntity draggedEntity) {
    this.draggedEntity = draggedEntity;
  }

  public DragProperties getDragProperties() {
    return properties;
  }

  public DragInfo toDragInfo() {
    DragInfo dragInfo = new DragInfo();
    dragInfo.setDragOrigin(this.properties.getDragOrigin());
    dragInfo.setDragNormal(this.properties.getDragNormal());
    dragInfo.setExtent(Math.abs(this.properties.getExtent1())+Math.abs(this.properties.getExtent2()));
    dragInfo.setSymmetrical(this.properties.isSymmetrical());
    dragInfo.setMagnitude(this.properties.getMagnitude());
    return dragInfo;
  }
}
