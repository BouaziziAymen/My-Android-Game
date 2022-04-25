package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MathUtils;

import org.andengine.entity.Entity;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

public class Plotter extends Entity {

    private final VertexBufferObjectManager vbom;
    float UNIT = 5;
    public Entity sheet;

    public void plot(Vector2[] path, Vector2 A1, Vector2 A2, Vector2 B1, Vector2 B2) {


    }


    public Plotter() {
        vbom = ResourceManager.getInstance().vbom;
        setZIndex(9999999);
        this.sheet = new Entity();
        attachChild(this.sheet);
        this.sheet.setZIndex(1000);
        this.vectors = new Entity();
        attachChild(this.vectors);
        this.vectors.setZIndex(1000);
//MEDIAN VECTOR
/*
	Vector2 dirA1 = u2.cpy().sub(u1).nor();
	Vector2 dirA2 = u3.cpy().sub(u1).nor();

	Vector2 median1 = MathUtils.getRotatedVectorByRadianAngle(dirA1.cpy().nor(), (float) ((-Math.atan2(dirA1.y, dirA1.x)+ Math.atan2(dirA2.y, dirA2.x))/2)).mul(2*UNIT);
	drawVector(u1,median1,Color.YELLOW);

	Vector2 mAPoint1 = u1.cpy().add(median1);

	Vector2 NmA1 = Vector2Pool.obtain(-median1.y,median1.x).mul(0.5f);
	Vector2 NmA2 = Vector2Pool.obtain(median1.y,-median1.x).mul(0.5f);
	drawPoint(mAPoint1, Color.RED);
	//drawLine(mAPoint1.cpy().add(NmA1),mAPoint1.cpy().add(NmA1).cpy().add(median1),Color.YELLOW);
	//drawLine(mAPoint1.cpy().add(NmA2),mAPoint1.cpy().add(NmA2).cpy().add(median1),Color.CYAN);
	//drawLine(u1.cpy().add(median1),u1.cpy().add(median1).cpy().add(NmA1),Color.GREEN);

	Vector2 dirB1 = v2.cpy().sub(v1).nor();
	Vector2 dirB2 = v3.cpy().sub(v1).nor();




	Vector2 median2 = MathUtils.getRotatedVectorByRadianAngle(dirB1.cpy().nor(), (float) ((-Math.atan2(dirB1.y, dirB1.x)+ Math.atan2(dirB2.y, dirB2.x))/2+Math.PI)).mul(2*UNIT);
	drawVector(v1,median2,Color.YELLOW);

	Vector2 mBPoint1 = v1.cpy().add(median2);

	Vector2 NmB1 = Vector2Pool.obtain(-median2.y,median2.x).mul(0.5f);
	Vector2 NmB2 = Vector2Pool.obtain(median2.y,-median2.x).mul(0.5f);
	drawPoint(mBPoint1, Color.RED);
	drawLine(mBPoint1.cpy().add(NmB1),mBPoint1.cpy().add(NmB1).cpy().add(median2),Color.PINK);
	drawLine(mBPoint1.cpy().add(NmB2),mBPoint1.cpy().add(NmB2).cpy().add(median2),Color.BLUE);
	drawLine(v1.cpy().add(median2),v1.cpy().add(median2).cpy().add(NmB1),Color.RED);




	LineEquation equation1 = new LineEquation(v1.cpy().add(median2),v1.cpy().add(median2).cpy().add(NmB1));//RED
	LineEquation equation2 = new LineEquation(mBPoint1.cpy().add(NmB1),mBPoint1.cpy().add(NmB1).cpy().add(median2));//PINK
	LineEquation equation3 = new LineEquation(mBPoint1.cpy().add(NmB2),mBPoint1.cpy().add(NmB2).cpy().add(median2));//BLUE

	LineEquation equ1 = new LineEquation(u1.cpy().add(N2),u1.cpy().add(N2).cpy().add(porte));//CYAN
	LineEquation equ2 = new LineEquation(u1.cpy().add(N1),u1.cpy().add(N1).cpy().add(porte));//WHITE

	LineEquation equation4 = new LineEquation(mAPoint1.cpy().add(NmA1),mAPoint1.cpy().add(NmA1).cpy().add(median1));//YELLOW
	LineEquation equation5 = new LineEquation(u1.cpy().add(median1),u1.cpy().add(median1).cpy().add(NmA1));//GREEN
	LineEquation equation6 = new LineEquation(mAPoint1.cpy().add(NmA2),mAPoint1.cpy().add(NmA2).cpy().add(median1));//CYAN

	Vector2 P1 = equation1.IntersectsWithLine(equation2);
	Vector2 P2 = equation1.IntersectsWithLine(equation3);
Vector2 P3 = equation3.IntersectsWithLine(equ1);
Vector2 P4 = equation4.IntersectsWithLine(equ1);
Vector2 P5 = equation4.IntersectsWithLine(equation5);
Vector2 P6 = equation5.IntersectsWithLine(equation6);
Vector2 P7 = equation6.IntersectsWithLine(equ2);
Vector2 P8 = equation2.IntersectsWithLine(equ2);

	drawPoint(P1,Color.YELLOW);
	drawPoint(P2,Color.YELLOW);
	drawPoint(P3,Color.YELLOW);
	drawPoint(P4,Color.YELLOW);
	drawPoint(P5,Color.YELLOW);
	drawPoint(P6,Color.YELLOW);
	drawPoint(P7,Color.YELLOW);
	drawPoint(P8,Color.YELLOW);
	ArrayList<Vector2> list = new ArrayList<Vector2>();
	list.add(P1);
	list.add(P2);
	list.add(P3);
	list.add(P4);
	list.add(P5);
	list.add(P6);
	list.add(P7);
	list.add(P8);
	for(int i=0;i<list.size();i++){
		Vector2 P = list.get(i);
		Text text = new Text(P.x,P.y,ResourceManager.getInstance().font2,""+i,vbom);
		text.setColor(Color.RED);
		this.attachChild(text);
		}
		*/

    }

    public void drawPointOnEntity(Vector2 v, Color color, Entity e) {
        Rectangle rect = new Rectangle(v.x, v.y, 4f, 4f, this.vbom);
        rect.setColor(color);
        rect.setZIndex(999999999);
        e.attachChild(rect);
    }

    public void drawPoint(Vector2 v, Color color, Mesh mesh) {
        Rectangle rect = new Rectangle(v.x, v.y, 3f, 3f, this.vbom);
        rect.setColor(color);
        rect.setZIndex(999999999);
        mesh.attachChild(rect);

    }

    public void drawPoint(Vector2 v, Color color, float alpha, float width) {
        Rectangle rect = new Rectangle(v.x, v.y, width, width, this.vbom);
        rect.setColor(color);
        rect.setAlpha(alpha);
        rect.setZIndex(999999999);
        attachChild(rect);


    }

    public void drawLine2(Vector2 v1, Vector2 v2, Color color, int width) {

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


        Vector2 unit1 = MathUtils.getRotatedVectorByRadianAngle(u.cpy().mul(-1), (float) (-Math.PI / 6)).nor();
        Vector2 unit2 = MathUtils.getRotatedVectorByRadianAngle(u.cpy().mul(-1), (float) (Math.PI / 6)).nor();


        Line stroke1 = new Line(endPoint.x, endPoint.y, endPoint.x + this.UNIT * unit1.x, endPoint.y + this.UNIT * unit1.y, 1, this.vbom);
        Line stroke2 = new Line(endPoint.x, endPoint.y, endPoint.x + this.UNIT * unit2.x, endPoint.y + this.UNIT * unit2.y, 1, this.vbom);
        stroke1.setColor(color);
        attachChild(stroke1);
        stroke2.setColor(color);
        attachChild(stroke2);
    }

    public Entity vectors;

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