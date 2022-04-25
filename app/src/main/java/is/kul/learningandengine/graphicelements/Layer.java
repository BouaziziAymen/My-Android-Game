package is.kul.learningandengine.graphicelements;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.scene.GameScene;

public class Layer extends Entity {
    public LayerProperty properties;
    public LayerShape layerShape;
    public Entity decorations,centers;
    public int ID;
    boolean cuttingLayer;
    int ORDER;

    Layer.State state = Layer.State.NOTSELECTED;
    int selectedDecorationID = -1;

    Layer(int ID, int order) {
        this.ID = ID;
        ORDER = order;
        this.layerShape = new LayerShape(this);
        attachChild(this.layerShape);
        this.properties = new LayerProperty(0, "Layer" + ID, ID);
        this.decorations = new Entity();
        centers = new Entity();
        centers.setZIndex(2);
        attachChild(centers);
        this.decorations.setZIndex(1);
        attachChild(this.decorations);
        changeState(Layer.State.NOTSELECTED);
        sortChildren();
        setZIndex(9999);

    }



    public void removeDecoration(int decorationID) {
        for (int i = 0; i < this.decorations.getChildCount(); i++) {

            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decorationID == decoration.ID) {
                decoration.detachSelf();
                break;
            }
        }
    }

    public void updatePosition(DPoint movedElement, float dX, float dY) {

        layerShape.updatePosition(movedElement, dX, dY);
    }

    public void addPointSimple(float x, float y, float scale) {
        layerShape.layerPoints.insertPoint(x, y, scale);
    }

    public LayerPoints getLayerPoints() {
        return layerShape.layerPoints;
    }

    public DPoint getLastPoint() {
        return layerShape.getLastPoint();
    }

    public LayerPoints getPoints() {
        if (state == Layer.State.SELECTED)
            return getLayerPoints();
        else if (state == Layer.State.DECORATIONACTIVE)
            return getSelectedDecoration().getDecorationPoints();

        return null;
    }

    public void selectDecoration(int decorID) {
        setSelectedDecoration(decorID);
        if (decorID != -1)
            changeState(Layer.State.DECORATIONACTIVE);
        else
            changeState(Layer.State.SELECTED);
    }

    public void deselectDecoration(int ID) {
        Decoration decoration = getDecoration(ID);
        if (decoration != null) decoration.setState(Decoration.DecorationState.NOTSELECTED);
    }

    public void addDecoration(int ID, Decoration.DecorationType type) {
        this.decorations.attachChild(new Decoration(ID, type, this));

    }

    public void changeState(Layer.State state) {
        switch (state) {
            case HIDDEN:

                break;
            case HIGHLIGHTED:
                setState(state);
                break;
            case NOTSELECTED:
                setState(state);
                break;
            case SELECTED:
                if (this.state != Layer.State.HIGHLIGHTED) setState(state);
                break;
            case DECORATIONACTIVE:
                setState(Layer.State.DECORATIONACTIVE);
                break;
            default:
                break;

        }

    }

    public void setState(Layer.State state) {
        this.state = state;
        if (state != Layer.State.HIDDEN && !isVisible()) setVisible(true);

        switch (state) {
            case HIDDEN:
                setVisible(false);
                break;
            case HIGHLIGHTED:
                layerShape.highlight(Color.BLUE);
                break;
            case NOTSELECTED:
                layerShape.hide();
                break;
            case SELECTED:
                layerShape.show();
                break;
            case DECORATIONACTIVE:
                layerShape.highlight(Color.CYAN);
                break;
            default:
                break;

        }

    }

    public void decaleGeneral(float dx,float dy) {
        if (state == Layer.State.SELECTED) {
            this.layerShape.decale(dx,dy);

        } else if (state == Layer.State.DECORATIONACTIVE) {
            Decoration decoration = getSelectedDecoration();
            if (decoration != null) {
                decoration.shape.decale(dx,dy);
            }
        }
        Update();
    }

    public void rotateGeneral(Vector2 origin, float angle) {
        if (state == Layer.State.SELECTED) {
            this.layerShape.rotate(origin, angle);
        } else if (state == Layer.State.DECORATIONACTIVE) {
            Decoration decoration = getSelectedDecoration();
            if (decoration != null) {
                decoration.shape.rotate(origin,angle);
            }
        }
        Update();
    }

    public void rotate(Vector2 origin, float angle) {
        this.layerShape.rotate(origin, angle);
        Update();
    }
    public void decale(float dx, float dy) {
        for(int i=0;i<centers.getChildCount();i++){
            DCircleT center = (DCircleT)centers.getChildByIndex(i);
            center.decale(dx,dy);
        }
        this.layerShape.decale(dx, dy);
        Update();
    }

    public void addPoint(float x, float y, float scale) {

        if (state == Layer.State.SELECTED) {
            layerShape.addPoint(x, y, scale);
        } else if (state == Layer.State.DECORATIONACTIVE) {
            Decoration decoration = getSelectedDecoration();
            if (decoration != null) {
                decoration.addPoint(x, y, scale);
            }
        }


    }

    public void insertPoint(float x, float y, float scale, int index) {
        this.layerShape.insertPoint(x, y, scale);
    }

    boolean isActive() {
        return layerShape.layerPoints.getNumberOfPoints() >= 3;
    }


    public void updateShape() {
        layerShape.Update();
    }

    public void insertPoint(float x, float y, float scale) {
        if (state == Layer.State.SELECTED)
            layerShape.insertPoint(x, y, scale);
        else if (state == Layer.State.DECORATIONACTIVE)
            getSelectedDecoration().insertPoint(x, y, scale);
        else return;
    }
    public ArrayList<Vector2> getCenters() {
        ArrayList<Vector2> result = new ArrayList<Vector2>();

        for (int i = 0; i < this.centers.getChildCount(); i++) {
            DCircleT center = (DCircleT) this.centers.getChildByIndex(i);
            result.add(center.toVector());
        }
        return result;
    }

    public void updateScale(float scale) {
        layerShape.scalePoints(scale);
        for (int i = 0; i < this.centers.getChildCount(); i++) {
            DCircleT circle = (DCircleT) this.centers.getChildByIndex(i);
            circle.scale(scale);
        }

    }

    boolean isValid() {
        return (layerShape.getLastCoherent() >= 2);
    }

    public ArrayList<Vector2> generatePoints() {

        return this.layerShape.generatePoints();
    }


    public Block generateBlock() {

        Block block = new Block(generatePoints(), this.properties, this.properties.getColor(), this.ORDER, this.ID, true);

        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            decoration.shape.Update();
            block.associatedBlocks.add(decoration.generateDecorationBlock());
        }


        return block;
    }

    public boolean hasCuttingDecoration() {
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decoration.getType() == Decoration.DecorationType.Cut) return true;
        }
        return false;
    }

    public Decoration getCuttingDecoration() {
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decoration.getType() == Decoration.DecorationType.Cut) return decoration;
        }
        return null;
    }

    public Decoration getSelectedDecoration() {
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decoration.ID == this.selectedDecorationID) return decoration;
        }
        return null;

    }

    public void setSelectedDecoration(int decorID) {
        this.selectedDecorationID = decorID;
        if (this.selectedDecorationID != -1)
            getSelectedDecoration().select();
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decoration.ID != this.selectedDecorationID) decoration.unselect();
        }
    }

    public Decoration getDecoration(int ID) {
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            if (decoration.ID == ID) return decoration;
        }
        return null;

    }

    public void Update() {
        updateShape();
        for (int i = 0; i < this.decorations.getChildCount(); i++) {
            Decoration decoration = (Decoration) this.decorations.getChildByIndex(i);
            decoration.shape.Update();
        }

    }



    public void detachPoint(DPoint dpoint) {
        layerShape.detachPoint(dpoint);
        updateShape();
    }

    public int getNumberOfPoints() {
        return layerShape.getNumberOfPoints();
    }

    @Override
    public boolean detachSelf() {
        if (layerShape.getPolygonIndicator() != null) layerShape.getPolygonIndicator().detachSelf();
        return super.detachSelf();
    }

    public DPoint getClosestPoint(float x, float y, float maxD) {
        return layerShape.getClosestPoint(x, y, maxD);
    }

    public void applyMirror() {
        if (state == Layer.State.SELECTED) {
            GameScene.model.createNewLayer(GameScene.ui.in_signal_addLayer(0));
            this.layerShape.mirrorShape();
        } else if (state == Layer.State.DECORATIONACTIVE) {
            Decoration mirroredDecoration = this.getSelectedDecoration();
            int newDecorationID = GameScene.ui.in_signal_addDecoration(GameScene.model.activeBodyID, ID);
            addDecoration(newDecorationID, Decoration.DecorationType.Normal);
            selectDecoration(newDecorationID);
            mirroredDecoration.shape.mirrorShape();
        }
    }

    public void createMirrorIndicator(Vector2 start, float scale) {
        if (state == Layer.State.SELECTED)
            layerShape.createMirrorIndicator(start,scale);
        else if (state == Layer.State.DECORATIONACTIVE)
            getSelectedDecoration().shape.createMirrorIndicator(start,scale);
    }

    public void updatePolygonIndicator(float nx, float ny) {
        if (state == Layer.State.SELECTED)
            layerShape.updatePolygonIndicator(nx, ny);
        else if (state == Layer.State.DECORATIONACTIVE)
            getSelectedDecoration().shape.updatePolygonIndicator(nx, ny);
    }

    public void hidePolygonIndicator() {
        if (state == Layer.State.SELECTED)
            layerShape.hidePolygonIndicator();
        else if (state == Layer.State.DECORATIONACTIVE)
            getSelectedDecoration().shape.hidePolygonIndicator();
    }

    public void updateMirrorIndicator(Vector2 newPosition) {
        if (state == Layer.State.SELECTED)
            layerShape.updateMirrorIndicator(newPosition);
        else if (state == Layer.State.DECORATIONACTIVE)
            getSelectedDecoration().shape.updateMirrorIndicator(newPosition);
    }

    public void createPolygon(Vector2 begin, Vector2 end,float scale,int polygonNumVertices) {
        DCircleT center = new DCircleT(begin.x, begin.y, scale, 0, 2);


        if (state == Layer.State.SELECTED) {
            if (getNumberOfPoints() > 0) {

                GameScene.model.createNewLayer(GameScene.ui.in_signal_addLayer(0));
            }
            GameScene.model.getActiveLayer().centers.attachChild(center);
            GameScene.model.getActiveLayer().layerShape.createPolygonIndicator(begin, end, scale, polygonNumVertices);

        } else if (state == Layer.State.DECORATIONACTIVE) {
            getSelectedDecoration().createPolygonIndicator(begin, end, scale, polygonNumVertices);
        }
    }
    public void abortRotationIndicator(){
        layerShape.abortRotationIndicator();
    }

    public void abortPolygonIndicator() {
        if (state == Layer.State.SELECTED)
           layerShape.abortPolygonIndicator();
        else if (state == Layer.State.DECORATIONACTIVE)
           getSelectedDecoration().shape.abortPolygonIndicator();
    }

    public void createRotationIndicator(Vector2 start, Vector2 end) {

            GameScene.model.getActiveLayer().layerShape.createRotationIndicator(start, end);

    }

    public void updateRotationIndicator(Vector2 end) {

            GameScene.model.getActiveLayer().layerShape.updateRotationIndicator(end);

    }

    enum State {
        SELECTED, HIGHLIGHTED, NOTSELECTED, HIDDEN, DECORATIONACTIVE

    }


}
