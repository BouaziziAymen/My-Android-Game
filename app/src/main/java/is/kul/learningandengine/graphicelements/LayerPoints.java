package is.kul.learningandengine.graphicelements;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.GameScene;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class LayerPoints extends Entity {

    LayerShape layerShape;
    boolean updated;
    ArrayList<Vector2> points;

    LayerPoints(LayerShape shape) {

        this.layerShape = shape;
        points = new ArrayList<Vector2>();


    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    DPoint getPoint(int index) {
        return (DPoint) getChildByIndex(index);
    }

    DPoint getPointByOrder(int order) {
        if (order < this.getNumberOfPoints() && order >= 0) {
            for (int i = 0; i < this.getNumberOfPoints(); i++)
                if (this.getPoint(i).order == order) return this.getPoint(i);
        }
        return null;
    }

    public void detachPoint(DPoint point) {


        point.detachSelf();

        for (int i = 0; i < this.getNumberOfPoints(); i++)
            if (this.getPoint(i).order > point.order) {
                if (this.getPoint(i).order > 0) this.getPoint(i).order--;
                else this.getPoint(i).order = this.getNumberOfPoints() - 1;
            }



    }

    public int getNumberOfPoints() {
        return getChildCount();
    }

    public void scale(float scale) {
        for (int i = 0; i < this.getNumberOfPoints(); i++)
            this.getPoint(i).scale(scale);

    }

    public ArrayList<Vector2> generatePoints() {
        if (updated) {
            updated = false;
            points.clear();
            if (layerShape.getLastCoherent() > 1) {
                for (int i = 0; i <= layerShape.getLastCoherent(); i++) {

                    DPoint dpoint = getPointByOrder(i);
                    Vector2 point = new Vector2(dpoint.getX(), dpoint.getY());
                    points.add(point);
                }
            }
        }
        return points;

    }

    public void clearAllPoints() {


        if (getChildCount() > 0) {
            detachChildren();

            this.layerShape.lines.detachChildren();
        }

    }

    public DPoint getClosestPoint(float x, float y, float maxD) {

        float distance = Float.MAX_VALUE;
        DPoint result = null;
        for (int i = 0; i < this.getNumberOfPoints(); i++) {
            DPoint dpoint = (DPoint) getChildByIndex(i);
            Vector2 point = new Vector2(dpoint.getX(), dpoint.getY());
            Vector2 touch = new Vector2(x, y);


            if (touch.dst(point) < distance) {
                distance = touch.dst(point);
                result = dpoint;
            }
        }
        if (distance < maxD) {

            return result;

        } else return null;

    }

    public void rotate(Vector2 origin, float angle) {
        for (int i = 0; i < getChildCount(); i++) {

            getPoint(i).rotate(origin, angle);
        }

    }

    public void decale(float dx, float dy) {
        for (int i = 0; i < getChildCount(); i++) {

            getPoint(i).decale(dx, dy);
        }

    }

    public void insertPoint(float x, float y, float scale) {
        insertPoint(x, y, scale, getNumberOfPoints());
    }

    void insertPoint(float x, float y, float scale, int index) {

        DPoint dpoint = new DPoint(x, y, scale, index, DFactory.DrawingType.TYPEPOINT);
        this.attachChild(dpoint);


    }


}
