package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.entities.blockvisitors.utilities.MathUtils;
import java.util.ArrayList;
import java.util.List;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class Plotter extends Entity {

  private final VertexBufferObjectManager vbom;
  public Entity sheet;
  public Entity vectors;
  float UNIT = 5;

  public Plotter() {
    vbom = ResourceManager.getInstance().vbom;
    setZIndex(9999999);
    this.sheet = new Entity();
    attachChild(this.sheet);
    this.sheet.setZIndex(1000);
    this.vectors = new Entity();
    attachChild(this.vectors);
    this.vectors.setZIndex(1000);
  }

  public void drawLineOnEntity(Vector2 v1, Vector2 v2, Color color, float width, Entity e) {
    Line line = new Line(v1.x, v1.y, v2.x, v2.y, width, this.vbom);
    line.setColor(color);
    line.setAlpha(1f);
    line.setZIndex(99999);
    e.attachChild(line);
  }

  public void drawPointOnEntity(Vector2 v, Color color, Entity e) {
    Rectangle rect = new Rectangle(v.x, v.y, 1f, 1f, this.vbom);
    rect.setColor(color);
    rect.setZIndex(999999999);
    e.attachChild(rect);
  }

  public void drawPoint(Vector2 v, Color color, Mesh mesh) {

    Rectangle rect = new Rectangle(v.x, v.y, 1f, 1f, this.vbom);
    rect.setColor(color);
    rect.setZIndex(999999999);
    mesh.attachChild(rect);
  }

  public void drawPoint(Vector2 v, Color color, float width) {

    Rectangle rect = new Rectangle(v.x, v.y, width, width, this.vbom);
    rect.setColor(color);
    rect.setAlpha(1f);
    rect.setZIndex(999999999);
    attachChild(rect);
  }

  public void drawPoint(Vector2 v, Color color, float alpha, float width) {
    Rectangle rect = new Rectangle(v.x, v.y, width, width, this.vbom);
    rect.setColor(color);
    rect.setAlpha(alpha);
    rect.setZIndex(999999999);
    attachChild(rect);
  }

  public void drawLine2(Vector2 v1, Vector2 v2, Color color, float width) {
    Line line = new Line(v1.x, v1.y, v2.x, v2.y, width, this.vbom);
    line.setColor(color);
    line.setAlpha(1f);
    line.setZIndex(99999);

    this.attachChild(line);
  }

  public void drawLine4(Vector2 v1, Vector2 v2, Color color, int width) {

    Line line = new Line(v1.x, v1.y, v2.x, v2.y, 2, this.vbom);
    line.setColor(color);
    line.setAlpha(1f);
    line.setZIndex(99999);

    sheet.attachChild(line);
  }

  public void drawLine3(Vector2 v1, Vector2 v2, Color color) {

    Line line = new Line(v1.x, v1.y, v2.x, v2.y, 2, this.vbom);
    line.setColor(color);
    line.setAlpha(1f);
    line.setZIndex(99999);

    attachChild(line);
  }

  public void drawVector(Vector2 position, Vector2 vector, Color color) {

    Vector2 u = vector.cpy();
    Vector2 endPoint = position.cpy().add(u);
    Line line = new Line(position.x, position.y, endPoint.x, endPoint.y, 1, this.vbom);
    line.setColor(color);
    attachChild(line);

    Vector2 unit1 =
        MathUtils.getRotatedVectorByRadianAngle(u.cpy().mul(-1), (float) (-Math.PI / 6)).nor();
    Vector2 unit2 =
        MathUtils.getRotatedVectorByRadianAngle(u.cpy().mul(-1), (float) (Math.PI / 6)).nor();

    Line stroke1 =
        new Line(
            endPoint.x,
            endPoint.y,
            endPoint.x + this.UNIT * unit1.x,
            endPoint.y + this.UNIT * unit1.y,
            1,
            this.vbom);
    Line stroke2 =
        new Line(
            endPoint.x,
            endPoint.y,
            endPoint.x + this.UNIT * unit2.x,
            endPoint.y + this.UNIT * unit2.y,
            1,
            this.vbom);
    stroke1.setColor(color);
    attachChild(stroke1);
    stroke2.setColor(color);
    attachChild(stroke2);
  }

  public void drawPolygon(Vector2 p1, Vector2 p2, Vector2 p3, Color color) {

    drawLine2(p1, p2, color, 1);
    drawLine2(p2, p3, color, 1);
    drawLine2(p3, p1, color, 1);
  }

  public void drawPolygon(List<Vector2> polygon, Color color, int width, float dx, float dy) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      Vector2 p1 = polygon.get(i).cpy().add(dx, dy);
      Vector2 p2 = polygon.get(ni).cpy().add(dx, dy);
      drawLine2(p1, p2, color, width);
    }
  }

  public void drawPolygon(List<Vector2> polygon, Color color, int width) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      Vector2 p1 = polygon.get(i);
      Vector2 p2 = polygon.get(ni);
      drawLine2(p1, p2, color, width);
    }
  }

  public void drawPolygon(List<Vector2> polygon, Vector2 dv, Color color) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      Vector2 p1 = polygon.get(i).cpy().add(dv);
      Vector2 p2 = polygon.get(ni).cpy().add(dv);
      drawLine2(p1, p2, color, 6);
    }
  }

  public void drawPolygon(List<Vector2> polygon, Color color) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      Vector2 p1 = polygon.get(i);
      Vector2 p2 = polygon.get(ni);
      drawLine2(p1, p2, color, 3);
    }
  }

  public void drawPolygonWithPoints(ArrayList<Vector2> polygon, Color color) {
    for (int i = 0; i < polygon.size(); i++) {
      int ni = (i == polygon.size() - 1) ? 0 : i + 1;
      Vector2 p1 = polygon.get(i);
      Vector2 p2 = polygon.get(ni);
      drawLine2(p1, p2, color, 1);
      drawPoint(p1, Color.RED, 1, 0);
    }
  }
}
