package is.kul.learningandengine.graphicelements;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.Inter;
import is.kul.learningandengine.helpers.MathUtils;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.GameScene;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;

public class LayerShape extends Entity {

    public int getNumberOfPoints() {
        return layerPoints.getNumberOfPoints();
    }

    public DPoint getClosestPoint(float x, float y, float maxD) {
        return layerPoints.getClosestPoint(x,y, maxD);
    }



    enum Type {DependentContained, DependentNonContained, Independent, None}

    Color pColor, lColor, flColor;
    Entity owner;

    Type getType() {
        if (owner instanceof Layer) return Type.Independent;
        else if (owner instanceof Decoration) {
            Decoration dec = (Decoration) owner;
            if (dec.getType() == Decoration.DecorationType.Normal) return Type.DependentContained;
            else return Type.DependentNonContained;
        }
        return Type.None;
    }

    public LayerShape(Entity owner) {
        this.owner = owner;
        this.layerPoints = new LayerPoints(this);
        attachChild(this.layerPoints);
        this.lines = new Entity();
        this.indicators = new Entity();
        attachChild(this.lines);
        attachChild(this.indicators);
        this.indicators.setZIndex(1000);
        sortChildren();
        this.pColor = Color.GREEN;
        this.lColor = Color.GREEN;
        this.flColor = Color.RED;
    }

    public LayerPoints layerPoints;
    Entity lines;
    Entity indicators;


    public void addPoint(float x, float y, float scale) {

        boolean condition = false;
        int n = layerPoints.getNumberOfPoints();
        if (n < 3) condition = true;
        else condition = testPoint(x, y);
        if (!condition) return;

        if (n >= 2) {
            int pi = n - 1;
            int ppi = n - 2;
            Vector2 v1 = layerPoints.getPointByOrder(pi).toVector();
            Vector2 v2 = layerPoints.getPointByOrder(ppi).toVector();
            Vector2 v3 = layerPoints.getPointByOrder(0).toVector();
//ALIGNMENT TEST need to add next line test
            if (Math.abs(v1.cpy().sub(x, y).cross(v2.cpy().sub(x, y))) > 1) {

                float dp = v1.dst(x, y);
                float dn = v3.dst(x, y);
                if (dp > 1 && dn > 1)
                    layerPoints.insertPoint(x, y, scale, n);


            } else {
                layerPoints.getPointByOrder(pi).setPosition(x, y);
                layerPoints.getPointByOrder(pi).setX(x);
                layerPoints.getPointByOrder(pi).setY(y);
            }
            if(rectifiable())
            rectification();
            Update();

        } else {
            layerPoints.insertPoint(x, y, scale, n);

            Update();
        }


    }
private boolean rectifiable(){
    if(getDecorationOwner()!=null)
        if(getDecorationOwner().getType()== Decoration.DecorationType.Cut)return true;
    return false;
}

    public void addPointWithoutTest(float x, float y, float scale, int index) {

        this.layerPoints.insertPoint(x, y, scale, index);
    }

    public void insertPoint(float x, float y, float scale) {

        ArrayList<Inter> inters = new ArrayList<Inter>();
        for (int i = 0; i < this.layerPoints.getNumberOfPoints(); i++) {
            Line line = (Line) this.lines.getChildByIndex(i);
            int[] orders = (int[]) line.getUserData();
            inters.add(Utils.intersectSegmentCircle(line, new Vector2(x, y), PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, orders));
        }
        Collections.sort(inters);
        if (inters.size() > 0)
            if (inters.get(0).intersection) {

                int insertOrder;
                if (inters.get(0).order[0] > inters.get(0).order[1]) {
                    insertOrder = 0;

                    if (this.testPosition(insertOrder, x, y)) {
                        this.layerPoints.insertPoint(x, y, scale, this.layerPoints.getNumberOfPoints());


                        this.Update();
                    }


                } else {
                    insertOrder = inters.get(0).order[0] + 1;
                    if (this.testPosition(insertOrder, x, y)) {


                        for (int i = 0; i < this.layerPoints.getNumberOfPoints(); i++)
                            if (this.layerPoints.getPoint(i).order >= insertOrder)
                                this.layerPoints.getPoint(i).order++;

                        this.layerPoints.insertPoint(x, y, scale, insertOrder);
                        this.Update();
                    }
                }

            }


    }


    void Update() {//UPDATES THE LINES AND SHAPE OF LAYER

        if (this.layerPoints.getNumberOfPoints() > 0) {
            layerPoints.setUpdated(true);
            this.lines.detachChildren();
            for (int i = 0; i < this.layerPoints.getNumberOfPoints() - 1; i++) {
                DPoint dpoint1 = this.layerPoints.getPointByOrder(i);
                DPoint dpoint2 = this.layerPoints.getPointByOrder(i + 1);
                Line line = new Line(dpoint1.getX(), dpoint1.getY(), dpoint2.getX(), dpoint2.getY(), 1, ResourceManager.getInstance().vbom);

                line.setColor(this.pColor);

//TO WORK ON CREATING MYLINE WHICH HAS ORDER
                int[] orders = {i, i + 1};
                line.setUserData(orders);
                line.setColor(this.lColor);
                this.lines.attachChild(line);

            }

            Line fline =
                    new Line(this.layerPoints.getPointByOrder(this.layerPoints.getNumberOfPoints() - 1).getX(), this.layerPoints.getPointByOrder(this.layerPoints.getNumberOfPoints() - 1).getY(),
                            this.layerPoints.getPointByOrder(0).getX(), this.layerPoints.getPointByOrder(0).getY(), 1, ResourceManager.getInstance().vbom);
            fline.setUserData(this.layerPoints.getNumberOfPoints() - 1);


            fline.setColor(this.flColor);


            this.lines.attachChild(fline);
            fline.setUserData(new int[]{this.layerPoints.getNumberOfPoints() - 1, 0});
        }

    }


    Line getLineByOrder(int order) {
        for (int i = 0; i < this.layerPoints.getNumberOfPoints(); i++) {
            int[] orders = (int[]) this.lines.getChildByIndex(i).getUserData();
            if (orders[0] == order) return (Line) this.lines.getChildByIndex(i);
        }
        return null;
    }

    public void scalePoints(float scale) {
        layerPoints.scale(scale);

    }

    public ArrayList<Vector2> generatePoints() {
        return this.layerPoints.generatePoints();

    }

    public void detachPoint(DPoint dpoint) {
        layerPoints.detachPoint(dpoint);
    }

    public boolean createPolygonDPoints(int n, float teta0, float w, float h, float scale, Vector2 begin) {

        float teta = teta0;
        ArrayList<Vector2> shape = new ArrayList<Vector2>();
        for (int i = 0; i < n; i++) {
            shape.add(new Vector2(begin.x + w * (float) Math.cos(teta), begin.y + h * (float) Math.sin(teta)));
            teta += 2 * Math.PI / n;
        }

        layerPoints.clearAllPoints();
        for (int i = 0; i < n; i++) {
            addPointWithoutTest(shape.get(i).x, shape.get(i).y, scale, i);
        }
        Update();
        return true;


    }
    public RotationIndicator getRotationIndicator() {

        for (int i = 0; i < this.indicators.getChildCount(); i++)
            if (this.indicators.getChildByIndex(i) instanceof RotationIndicator)
                return (RotationIndicator) this.indicators.getChildByIndex(i);

        return null;


    }


    public MirrorIndicator getMirrorIndicator() {

        for (int i = 0; i < this.indicators.getChildCount(); i++)
            if (this.indicators.getChildByIndex(i) instanceof MirrorIndicator)
                return (MirrorIndicator) this.indicators.getChildByIndex(i);

        return null;


    }
    public void createRotationIndicator(Vector2 start, Vector2 end) {
        this.indicators.attachChild(new RotationIndicator(start, end));
    }

    public void updateRotationIndicator(Vector2 end) {
        this.getRotationIndicator().update(end);
    }
    public void createPolygonIndicator(Vector2 begin, Vector2 end, float scale, int n) {
        Vector2 dir = end.cpy().sub(begin);
        float w = dir.len();
        float angle = (float) Math.atan2(dir.y, dir.x);
        this.createPolygonDPoints(n, angle, w, w, scale, begin);
        this.indicators.attachChild(new PolygonIndicator(begin, end, n));
    }
    public PolygonIndicator getPolygonIndicator() {
        for (int i = 0; i < this.indicators.getChildCount(); i++)
        if (this.indicators.getChildByIndex(i) instanceof PolygonIndicator)return (PolygonIndicator) this.indicators.getChildByIndex(i);
        return null;
    }
    public void updatePolygonIndicator(float nx, float ny) {
        PolygonIndicator indicator = this.getPolygonIndicator();
        Vector2 dir = new Vector2(nx, ny).cpy().sub(indicator.begin.toVector());
        float w = dir.len();
        float angle = (float) Math.atan2(dir.y, dir.x);
        this.createPolygonDPoints(indicator.n, angle, w, w, indicator.scale, indicator.begin.toVector());
        indicator.updateIndicator(nx, ny, ResourceManager.getInstance().vbom);

    }

    public void abortPolygonIndicator() {
        layerPoints.clearAllPoints();
        if (this.getPolygonIndicator() != null) this.getPolygonIndicator().detachSelf();

    }

    public void abortRotationIndicator() {

        if (this.getRotationIndicator() != null) this.getRotationIndicator().detachSelf();
    }

    public void hidePolygonIndicator() {
        if (this.getPolygonIndicator() != null) this.getPolygonIndicator().hide();
    }

    public void createMirrorIndicator(Vector2 start, float scale) {
            this.indicators.attachChild(new MirrorIndicator(start));
    }

    private void mirrorPoints(DPoint begin, DPoint end) {

        Vector2 direction = new Vector2(end.getX() - begin.getX(), end.getY() - begin.getY());


        for (int i = 0; i < this.layerPoints.getChildCount(); i++) {
            DPoint dpoint = this.layerPoints.getPointByOrder(i);
            Vector2 v = dpoint.toVector().sub(begin.toVector());
            Vector2 pos = begin.toVector().add(this.imageMirror(direction, v));
            GameScene.model.addPoint(pos.x, pos.y);
        }
        Update();
    }

    public void updateMirrorIndicator(Vector2 position) {
        MirrorIndicator indicator = this.getMirrorIndicator();

        if (indicator != null) {

            indicator.update(position);

        }

    }

    Vector2 imageMirror(Vector2 mirror, Vector2 vector) {
        float angle = (float) (-Math.atan2(mirror.y, mirror.x) + Math.atan2(vector.y, vector.x));
        return MathUtils.rotateZ(vector, -2 * angle);
    }

    public void mirrorShape() {

        MirrorIndicator indicator = this.getMirrorIndicator();


        this.mirrorPoints(indicator.begin, indicator.end);
        GameScene.model.detachMirrorIndicator(indicator);

    }

    public DPoint getLastPoint() {
        if (this.layerPoints.getNumberOfPoints() > 0)
            return this.layerPoints.getPointByOrder(this.layerPoints.getNumberOfPoints() - 1);
        else return null;
    }
    public void rotate(Vector2 origin,float angle) {
        this.layerPoints.rotate(origin, angle);

    }
    public void decale(float x, float y) {
        this.layerPoints.decale(x, y);

    }

    private void setLinesColor(Color c) {
        for (int i = 0; i < this.lines.getChildCount(); i++) {
            Line line = (Line) this.lines.getChildByIndex(i);
            line.setColor(c);
        }
        lColor = c;
        flColor = c;
    }

    public void highlight(Color color) {
        setVisible(true);
        lines.setVisible(true);
        layerPoints.setVisible(false);
        this.setLinesColor(color);
    }

    public void show() {
        setVisible(true);
        lines.setVisible(true);
        layerPoints.setVisible(true);
        this.setLinesColor(Color.GREEN);
        flColor = Color.RED;
    }

    public void hide() {

        setVisible(false);
    }

    public Line getLine(int i) {
        return (Line) this.lines.getChildByIndex(i);

    }

    public void updatePosition(DPoint dpoint, float dX, float dY) {
        if (testPosition(dpoint.order, dpoint.getX()+dX, dpoint.getY()+dY)) {

            dpoint.updatePosition(dX, dY);
            Update();
        }
    }

    public boolean testPosition(int order, float newX, float newY) {
        int n = layerPoints.getNumberOfPoints();
        int pOrder, nOrder;
        if (order == 0) pOrder = n - 1;
        else pOrder = order - 1;
        if (order == n - 1) nOrder = 0;
        else nOrder = order + 1;
        DPoint point = layerPoints.getPointByOrder(order);


        DPoint previous = layerPoints.getPointByOrder(pOrder);
        DPoint next = layerPoints.getPointByOrder(nOrder);

        if (previous != null && next != null) {
            //FIRST TEST IF TOO CLOSE OR ALIGNMENT
            if (Math.abs(previous.toVector().cpy().sub(newX, newY).cross(next.toVector().cpy().sub(newX, newY))) < 5)
                return false;
            if (previous.toVector().dst(newX, newY) < 1 || next.toVector().dst(newX, newY) < 1)
                return false;

            Line potentialpLine = new Line(previous.getX(), previous.getY(), newX, newY, 1, ResourceManager.getInstance().vbom);
            Line potentialnLine = new Line(newX, newY, next.getX(), next.getY(), 1, ResourceManager.getInstance().vbom);
            for (int i = 0; i < n; i++) {
                Line line = this.getLineByOrder(i);
if(i!=order&&i!=pOrder) {
    if (Utils.collision(line, potentialpLine, i, pOrder, n,"first")) return false;

    if (Utils.collision(line, potentialnLine, i, order, n,"second")) return false;
}
                }
            }


        return true;
    }

    public boolean testPoint(float x, float y) {

        int n = layerPoints.getNumberOfPoints();
        for (int i = 0; i < n - 1; i++) {

            Line line = (Line) this.lines.getChildByIndex(i);

            if (Utils.PointOnLineSegment(new Vector2(line.getX1(), line.getY1()), new Vector2(line.getX2(), line.getY2()), new Vector2(x, y), 0.01f))
                return false;
        }

        DPoint previous = layerPoints.getPointByOrder(n - 1);
        if (previous != null) {
            Line potentialLine = new Line(previous.getX(), previous.getY(), x, y, 1, ResourceManager.getInstance().vbom);
            for (int i = 0; i < n; i++) {
                Line line = (Line) this.lines.getChildByIndex(i);
                if (Utils.collision(line, potentialLine)) return false;

            }
            if (this.getType() == Type.DependentContained) {

                for (int i = 0; i < getDecorationOwner().getLayerParent().layerShape.layerPoints.getNumberOfPoints(); i++) {
                    Line line = (Line) getDecorationOwner().getLayerParent().layerShape.lines.getChildByIndex(i);
                    if (Utils.collision(line, potentialLine)) return false;

                }
            }
        }


        return true;
    }

    void rectification() {
        while (rectifyAccordingToParent()) {
            decale(3, 0);
        }
    }

    private boolean rectifyAccordingToParent() {

        if (this.getType() == Type.DependentContained) {
            int n = layerPoints.getNumberOfPoints();
            for (int order = 0; order < this.getChildCount(); order++) {
                int pOrder, nOrder;
                pOrder = (order == 0) ? n - 1 : order - 1;

                nOrder = (order == n - 1) ? 0 : order + 1;

                DPoint previous = layerPoints.getPointByOrder(pOrder);
                DPoint current = layerPoints.getPointByOrder(order);
                boolean test = intersectionWithParent(previous.toVector(), current.toVector());

                if (!test) {
                    return true;
                }

            }
        }
        return false;
    }

    Decoration getDecorationOwner() {
        if (owner instanceof Decoration) {
            Decoration dec = (Decoration) owner;
            return dec;
        }
        return null;
    }

    private boolean intersectionWithParent(Vector2 s, Vector2 e) {

        LayerPoints points = getDecorationOwner().getLayerParent().layerShape.layerPoints;
        for (int order = 0; order < points.getChildCount(); order++) {
            int pOrder, nOrder;
            if (order == 0) pOrder = points.getNumberOfPoints() - 1;
            else pOrder = order - 1;
            nOrder = (pOrder + 1) % points.getNumberOfPoints();

            Vector2 previous = points.getPointByOrder(pOrder).toVector();
            Vector2 current = points.getPointByOrder(order).toVector();
            Vector2 intersection = Utils.intersectionPoint(e, s, current, previous);
            if (intersection != null) {
                for (int j = 0; j < points.getChildCount(); j++) {
                    Vector2 testPoint = points.getPoint(j).toVector();
                    if (testPoint.dst(intersection) < 3) return false;
                }
            }
        }
        return true;
    }

    public int getLastCoherent() {
int index = getNumberOfPoints()-1;
do {
    DPoint first = layerPoints.getPointByOrder(0);
    DPoint potentialLastPoint = layerPoints.getPointByOrder(index);
    Line potentialLastLine = new Line(potentialLastPoint.getX(), potentialLastPoint.getY(), first.getX(), first.getY(), 1, ResourceManager.getInstance().vbom);
boolean problem = false;
    for (int i = 0; i < index; i++) {
        if (Utils.collision(potentialLastLine, getLineByOrder(i))){problem = true;break;}
    }
    if(!problem)break;
    index--;
} while(index>=2);

return index;
}

}