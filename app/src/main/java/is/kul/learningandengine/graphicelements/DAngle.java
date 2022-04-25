package is.kul.learningandengine.graphicelements;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.factory.VerticesFactory;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.Flag;

public class DAngle extends DElement {
    ArrayList<Vector2> points;
    float radius;
    Entity background, foreground;
    boolean drawn;

    public DAngle(float x, float y, float lowerAngleDeg, float upperAngleDeg, float radius, int n) {
        super(x, y);
        this.radius = radius;
        background = new Entity();
        foreground = new Entity();
        this.attachChild(background);
        this.attachChild(foreground);
        background.setZIndex(0);
        foreground.setZIndex(1);
        sortChildren();
        points = VerticesFactory.createPolygon(x, y, radius, radius, n);
        Collections.reverse(points);

        update(lowerAngleDeg, upperAngleDeg);
    }

    public void update(float lowerAngleDeg, float upperAngleDeg) {

        if (upperAngleDeg > 360 + lowerAngleDeg) {
            if (!drawn) {
                drawArc(points, new Color(0.5f,0.5f,0), background, true);
                drawn = true;
            }

        } else {
            background.detachChildren();
            drawn = false;
        }


        foreground.detachChildren();
        drawArc(generateArc(lowerAngleDeg, upperAngleDeg), Color.YELLOW, foreground, false);


    }

    ArrayList<Vector2> generateArc(float lowerAngleDeg, float upperAngleDeg) {


        Vector2 l1 = new Vector2(1, 0);
        Utils.rotateVector2(l1, -lowerAngleDeg);

        Vector2 l2 = new Vector2(1, 0);
        Utils.rotateVector2(l2, -upperAngleDeg);

        Vector2 u1 = l1.sub(getX(), getY()).nor().mul(2 * radius).add(getX(), getY());
        Vector2 u2 = l2.sub(getX(), getY()).nor().mul(2 * radius).add(getX(), getY());
        int k1 = -1, k2 = -1;
        Flag flag1 = null, flag2 = null;
        inter inter1 = this.getIntersection(u1);
        inter inter2 = this.getIntersection(u2);
        ArrayList<Vector2> arc = new ArrayList<Vector2>();
        if (inter1.v.dst(inter2.v) > 0.1f) {
            k1 = inter1.order;
            k2 = inter2.order;

            flag1 = new Flag(inter1.v, flagType.begin);
            flag2 = new Flag(inter2.v, flagType.end);

            ArrayList<Flag> flags = new ArrayList<Flag>();
            for (int i = 0; i < points.size(); i++) {
                Vector2 end = points.get(i);
                if (k1 == i && k2 != i) flags.add(flag1);
                else if (k2 == i && k1 != i) flags.add(flag2);
                else if (k2 == i && k1 == i) {

                    if (flag1.position.dst(end) <= flag2.position.dst(end)) {
                        flags.add(i, flag1);
                        flags.add(i, flag2);
                    } else {
                        flags.add(i, flag2);
                        flags.add(i, flag1);
                    }
                }
                flags.add(new Flag(points.get(i), flagType.point));
            }


            flags.addAll(flags);


            boolean firstAdded = false;
            int e = 0;
            for (int i = 0; i < flags.size(); i++) {
                if (flags.get(i).type == flagType.begin) {
                    e = i;
                    break;
                }

            }
            for (int j = e; j < flags.size(); j++) {
                Flag F = flags.get(j);
                arc.add(F.position);
                if (F.type == flagType.end) break;
            }

        }


        return arc;
    }

    void drawArc(ArrayList<Vector2> arc, Color color, Entity parent, boolean round) {

        for (int i = 0; i < arc.size() + ((round) ? 0 : -1); i++) {
            int ni = (i == arc.size() - 1) ? 0 : i + 1;
            Vector2 p1 = arc.get(i);
            Vector2 p2 = arc.get(ni);
            Line line = new Line(p1.x, p1.y, p2.x, p2.y, 5, ResourceManager.getInstance().vbom);
            line.setColor(color);
            parent.attachChild(line);
        }
    }

    inter getIntersection(Vector2 limit) {
        ArrayList<inter> intersections = new ArrayList<inter>();
        for (int i = 0; i < points.size(); i++) {
            int ni = i == points.size() - 1 ? 0 : i + 1;
            Vector2 v1 = points.get(i);
            Vector2 v2 = points.get(ni);
            Vector2 u = new Vector2(v1.x - v2.x, v1.y - v2.y).nor().mul(0.01f);

            Vector2 V1 = new Vector2(v1.x + u.x, v1.y + u.y);
            Vector2 V2 = new Vector2(v2.x - u.x, v2.y - u.y);
            Vector2 v = Utils.lineIntersectPoint(getX(), getY(),
                    limit.x, limit.y, V1.x, V1.y, V2.x, V2.y);
            if (v != null) {
                if (Utils.PointOnLineSegment(V1, V2, v, 0.001f)) {
                    inter inter = new inter(v, ni);
                    inter.value = v.dst(getX(), getY());

                    intersections.add(inter);
                }
            }
        }
        Collections.sort(intersections);
        if (intersections.size() > 0) return intersections.get(0);
        else return null;
    }

    @Override
    void scale(float scale) {
setScale(1/scale);
    }



    @Override
    public void setBlue() {

    }

    @Override
    public void setGreen() {

    }

    @Override
    public void setRed() {

    }

    enum flagType {point, begin, end}

    class Flag {
        flagType type;
        Vector2 position;

        Flag(Vector2 position, flagType type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public String toString() {
            return "{" + position + ":" + type + "]";
        }
    }

    class inter implements Comparable<inter> {
        Vector2 v;
        float value;
        int order;

        inter(Vector2 v, int order) {
            this.v = v;
            this.order = order;
        }

        @Override
        public int compareTo(inter another) {
            if (value > another.value) return 1;
            else return -1;
        }
    }
}
