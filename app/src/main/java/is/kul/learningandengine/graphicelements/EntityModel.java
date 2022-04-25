package is.kul.learningandengine.graphicelements;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.BlockImageSubType;
import is.kul.learningandengine.basics.BlockType;
import is.kul.learningandengine.basics.GameGroup;
import is.kul.learningandengine.basics.MCMesh;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.graphicelements.ui.JointProperty;
import is.kul.learningandengine.graphicelements.ui.UserInterface;
import is.kul.learningandengine.polygonarithmetic.FullCutter;
import is.kul.learningandengine.scene.GameScene;

public class EntityModel extends Entity {
    private final Entity indicators;
    public int activeBodyID;
    public int numberOfLayers;
    public float scale = 1;
    public ModelTrail modelTrail;
    public Entity bodies;
    public int cutLayerID;
    GameScene scene;

    int currentJointID;
    FullCutter cutter;
    int polygonNumVertices = 7;


    public EntityModel(GameScene scene) {
        this.scene = scene;
        this.bodies = new Entity();

        this.indicators = new Entity();
        this.indicators.setZIndex(9999);

        attachChild(this.indicators);
        createNewBody(0);

        setZIndex(9999);
        this.bodies.setZIndex(9999);
        this.modelTrail = new ModelTrail();
        attachChild(this.modelTrail);
        attachChild(this.bodies);
        sortChildren();
        attachChild(new DCircleT(400, 240, this.scale, 0, 2));


    }

    public Body getBody(int ID) {
        for (int i = 0; i < this.bodies.getChildCount(); i++) {
            Body body = (Body) this.bodies.getChildByIndex(i);
            if (body.bodyID == ID) return body;
        }
        return null;
    }

    Body getActiveBody() {
        return this.getBody(this.activeBodyID);
    }

    public void setActiveBody(int ID) {

        this.activeBodyID = ID;

        for (int i = 0; i < this.bodies.getChildCount(); i++) {
            Body body = (Body) this.bodies.getChildByIndex(i);

            if (body.bodyID == this.activeBodyID) {
                body.changeState(Body.BodyState.SELECTED);

            } else {
                body.changeState(Body.BodyState.NOTSELECTED);

            }
        }
    }

    public void createNewLayer(int ID) {
        Body body = getActiveBody();
        body.createNewLayer(ID);
    }

    public void swapLayers(int a, int b) {
        Layer layer1 = getLayerByOrder(a);
        Layer layer2 = getLayerByOrder(b);
        int joker = layer1.ORDER;
        layer1.ORDER = layer2.ORDER;
        layer2.ORDER = joker;
        Update();
    }

    public Layer getLayerByOrder(int ORDER) {
        Body body = getActiveBody();
        for (int i = 0; i < body.layers.getChildCount(); i++) {
            Layer layer = (Layer) body.layers.getChildByIndex(i);
            if (layer.ORDER == ORDER) {
                return layer;
            }
        }
        return null;
    }

    public void Decale(float dx, float dy,boolean multiple) {
        if(multiple)
        for (int i = 0; i < getActiveBody().decaledLayerIndexes.length; i++)
            getLayer(getActiveBody().decaledLayerIndexes[i]).decale(dx, dy);
        else
            getActiveLayer().decaleGeneral(dx,dy);
        Update();
    }
    public void Rotate(Vector2 origin, float angle,boolean multiple) {
        if(multiple)
        for (int i = 0; i < getActiveBody().decaledLayerIndexes.length; i++)
         getLayer(getActiveBody().decaledLayerIndexes[i]).rotate(origin, angle);
        else
            getActiveLayer().rotateGeneral(origin,angle);

        Update();
    }


    public void addPoint(float x, float y) {
        if (this.getActiveLayer() != null) {
            this.getActiveLayer().addPoint(x, y, this.scale);
Update();
        }
    }

    public void addPoint(Vector2 p) {
        if (this.getActiveLayer() != null) {
                this.getActiveLayer().addPoint(p.x, p.y, this.scale);
            Update();
        }
    }

    public void addPointSimple(float x, float y) {
        this.getActiveLayer().addPointSimple(x, y, scale);

    }

    public void insertPoint(float x, float y) {

            this.getActiveLayer().insertPoint(x, y, this.scale);

        Update();
    }

    public Layer getLayer(int ID) {
        Body body = getActiveBody();
       return body.getLayer(ID);

    }

    public Layer getActiveLayer() {
        Body activeBodybody = getActiveBody();
        return this.getLayer(activeBodybody.activeLayer);
    }

    public void setActiveLayer(int ID) {
        Body body = getActiveBody();
        body.setActiveLayer(ID);


    }

    public LayerPoints getPoints() {
        Layer activeLayer = this.getActiveLayer();
        if(activeLayer!=null)
       return activeLayer.getPoints();
        else return null;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void setScale(float scale) {
        this.scale = Math.min(16, scale);


        for (int i = 0; i < this.indicators.getChildCount(); i++) {
            JointIndicator indicator = (JointIndicator) this.indicators.getChildByIndex(i);
            indicator.scale(scale);
        }


        if (getActiveLayer() != null)
            this.getActiveLayer().updateScale(scale);
    }

    public void Update() {

        GameGroup grouping = new GameGroup("");
        for (int i = 0; i < this.bodies.getChildCount(); i++) {
            Body body = (Body) this.bodies.getChildByIndex(i);
            MCMesh mesh = BasicFactory.getInstance().createMCMesh(body.generateBlocks(true), 0, 0, 0, true);

            grouping.attachChild(mesh);
        }
        this.modelTrail.addModel(grouping);
    }

    public void UpdateAll() {
        for (int i = 0; i < getActiveBody().decaledLayerIndexes.length; i++) {
            Layer layer = getLayer(getActiveBody().decaledLayerIndexes[i]);
            layer.updateShape();
        }

        GameGroup grouping = new GameGroup("");
        for (int i = 0; i < this.bodies.getChildCount(); i++) {
            Body body = (Body) this.bodies.getChildByIndex(i);
            MCMesh mesh = BasicFactory.getInstance().createMCMesh(body.generateBlocks(true), 0, 0, 0, true);
            grouping.attachChild(mesh);
        }
        this.modelTrail.addModel(grouping);
    }

    public void detachPoint(final DPoint dpoint) {
        ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {

                EntityModel.this.getActiveLayer().detachPoint(dpoint);
                Update();
            }


        });


    }

    public void detachMirrorIndicator(final MirrorIndicator indicator) {
        ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                indicator.detachSelf();
            }


        });

    }

    public void createJointIndicator(Vector2 begin, Vector2 v, JointProperty.Joint_Type type, boolean hasLimits) {
       int ID = GameScene.ui.in_signal_addJoint();
        this.indicators.attachChild(new JointIndicator(begin, v, this.scale, ID, type,hasLimits));
setActiveJointID(ID);
    }
    public void setActiveJointID(int ID){
        this.currentJointID = ID;
        JointIndicator current = this.getJointIndicator(currentJointID);
        current.select();
        for(int i=0;i<indicators.getChildCount();i++){
            JointIndicator ind = (JointIndicator) indicators.getChildByIndex(i);
            if(ind.getID()!=currentJointID)
            ind.disselect();
        }
    }

    public void updateJointIndicator(float nx, float ny) {
        getCurrentJointIndicator().updateIndicator(nx, ny);
    }

    public void copyLayer(int ID) {
        LayerPoints points = getActiveLayer().getLayerPoints();
        createNewLayer(GameScene.ui.in_signal_addLayer(this.activeBodyID));
        for (int i = 0; i < points.getChildCount(); i++) {
            DPoint point = ((DPoint) points.getChildByIndex(i)).copy();
            addPointSimple(point.getX(), point.getY());

        }

        Update();
    }

    public void applyMirror() {
      getActiveLayer().applyMirror();

    }

    JointIndicator getIndicator(int ID) {
        for (int i = 0; i < this.indicators.getChildCount(); i++) {
            if (this.indicators.getChildByIndex(i) instanceof JointIndicator) {
                JointIndicator indicator = (JointIndicator) this.indicators.getChildByIndex(i);
                if (indicator.ID == ID)
                    return indicator;
            }

        }
        return null;
    }

    JointIndicator getCurrentJointIndicator() {
        return this.getIndicator(currentJointID);
    }

    public void createPolygon(Vector2 begin) {
        if(this.getActiveLayer()==null)return;


     this.getActiveLayer().createPolygon(begin, begin,scale,polygonNumVertices);

    }
    public void abortRotationIndicator() {
        ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                getActiveLayer().abortRotationIndicator();
            }
        });
    }

    public void abortPolygonIndicator() {
        ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                getActiveLayer().abortPolygonIndicator();
            }
        });
    }

    public void hidePolygonIndicator() {
    this.getActiveLayer().hidePolygonIndicator();
    Update();
    }

    public ArrayList<Vector2> getCenters() {
        ArrayList<Vector2> result = new ArrayList<Vector2>();

        for (int i = 0; i < bodies.getChildCount(); i++) {
           Body body =(Body) bodies.getChildByIndex(i);
           result.addAll(body.getCenters());
        }
        return result;
    }

    public void updatePolygonIndicator(float nx, float ny) {
        getActiveLayer().updatePolygonIndicator(nx,ny);
        Update();
    }


    public void createRotationIndicator(Vector2 start,Vector2 end) {
        getActiveLayer().createRotationIndicator(start,end);
    }
    public void updateRotationIndicator(Vector2 end) {
        getActiveLayer().updateRotationIndicator(end);
    }


    public void createMirrorIndicator(Vector2 start) {
        getActiveLayer().createMirrorIndicator(start,scale);
    }

    public DPoint getClosestPoint(float x, float y, float d) {
        return getActiveLayer().getClosestPoint(x, y, d);
    }

    public void updateMirrorIndicator(Vector2 newPosition) {
        getActiveLayer().updateMirrorIndicator(newPosition);
    }

    public DPoint getLastPoint() {
        return getActiveLayer().getLastPoint();
    }

    public void undoCut() {
        Layer cutLayer = this.getLayer(cutLayerID);
        cutLayer.setState(Layer.State.SELECTED);
        cutLayer.removeDecoration(1000);
        GameScene.ui.in_signal_removeDecoration(0, cutLayerID, 1000);
    }

    public void performCut() {
        Layer cutLayer = this.getLayer(cutLayerID);
        this.cutter = new FullCutter(cutLayer.generatePoints(), cutLayer.getCuttingDecoration().getPoints());
        removeLayer(this.cutLayerID);
        GameScene.ui.in_signal_removeLayer(0, cutLayerID);
        if (this.cutter.DECOMPOSITION != null) {
            for (int i = 0; i < this.cutter.DECOMPOSITION.size(); i++) {
                createNewLayer(GameScene.ui.in_signal_addLayer(0));

                for (int j = 0; j < this.cutter.DECOMPOSITION.get(i).size(); j++) {
                    Vector2 point = this.cutter.DECOMPOSITION.get(i).get(j);
                    addPoint(point.x, point.y);
                }

            }
        }
        sortChildren();

    }
    public void removeDecoration(int decorationID) {
    Layer layer = getActiveLayer();
    layer.removeDecoration(decorationID);
    Update();
    }



    public void removeLayer(int layerID) {
        Body body = getActiveBody();

        int order = this.getLayer(layerID).ORDER;
        for (int i = 0; i < body.layers.getChildCount(); i++) {
            Layer layer = (Layer) body.layers.getChildByIndex(i);
            if (layer.ORDER > order) layer.ORDER--;
        }

        this.getLayer(layerID).detachSelf();
        this.numberOfLayers--;

        Update();
        modelTrail.cutTrail();
    }

    public ArrayList<DElement> getMovableElements() {
        ArrayList<DElement> elements = new ArrayList<DElement>();
        if (GameScene.ui.board == UserInterface.BoardType.DRAWBOARD) {

            LayerPoints raw = getPoints();
            if (raw != null)
                for (int i = 0; i < raw.getChildCount(); i++) {
                    elements.add((DElement) raw.getChildByIndex(i));
                }

        }
        else if (GameScene.ui.board == UserInterface.BoardType.IMAGEBOARD) {
            if(scene.getSketch()!=null)
          elements.add(scene.getSketch().xyLimit);
        }


        else if (GameScene.ui.board == UserInterface.BoardType.JOINTBOARD) {

            for (int i = 0; i < this.indicators.getChildCount(); i++) {
                if (indicators.getChildByIndex(i) instanceof JointIndicator) {
                    JointIndicator rev = (JointIndicator) indicators.getChildByIndex(i);
                    elements.add(rev.begin);
                    elements.add(rev.end);
                    if (rev.type == JointProperty.Joint_Type.PRISMATIC) {
                        DElement[] el = rev.getPrismaticMain();
                        elements.add(el[0]);
                        elements.add(el[1]);

                    } else    if (rev.type == JointProperty.Joint_Type.REVOLUTE) {
                        DElement[] el = rev.getRevoluteMain();
                        elements.add(el[0]);
                        elements.add(el[1]);
                        elements.add(el[2]);

                    }

                }

            }
        }
        return elements;
    }

    public int getActivePointsNumber() {
        if (getActiveLayer() != null)
            return getActiveLayer().getNumberOfPoints();
        else return 0;
    }

    public void createCuttingDecoration() {
        cutLayerID = this.getActiveLayerID();
        Body body = getActiveBody();
        this.addDecoration(1000, Decoration.DecorationType.Cut);
        GameScene.ui.in_signal_addCuttingDecoration(activeBodyID, cutLayerID);
    }

    public void updateProperty() {

        Update();

    }

    public void createNewBody(int ID) {

        this.bodies.attachChild(new Body(ID));
        setActiveBody(ID);
        createNewLayer(0);


    }

    public int getActiveLayerID() {
        Body body = getActiveBody();
        return body.activeLayer;
    }

    public GameGroup create() {


        ArrayList<ArrayList<Block>> blocks = new ArrayList<ArrayList<Block>>();
        ArrayList<Integer> zindexes = new ArrayList<Integer>();
        for (int i = 0; i < this.bodies.getChildCount(); i++) {
            Body body = (Body) this.bodies.getChildByIndex(i);
            blocks.add(body.generateBlocks(false));
            zindexes.add(body.ZINDEX);
        }


        ArrayList<Vector2> points1 = new ArrayList<Vector2>();
        points1.add(new Vector2(400, 280));
        points1.add(new Vector2(400, 200));
        points1.add(new Vector2(300, 280));
        Block decoration1 = BasicFactory.getInstance().createBlock(points1, 0, 0, 0);
        decoration1.setID(-1);
        decoration1.blockType = BlockType.IMAGE;
        decoration1.blockSubType = BlockImageSubType.DECORATION;
        decoration1.color = Color.GREEN;
        ArrayList<Vector2> points2 = new ArrayList<Vector2>();
        points2.add(new Vector2(450, 280));
        points2.add(new Vector2(400, 200));
        points2.add(new Vector2(300, 250));
        Block decoration2 = BasicFactory.getInstance().createBlock(points2, 0, 0, 0);
        decoration2.setID(-1);
        decoration2.blockType = BlockType.IMAGE;
        decoration2.blockSubType = BlockImageSubType.DECORATION;
        decoration2.color = Color.RED;

//blocks.get(0).get(0).associatedBlocks.add(decoration1);
//blocks.get(0).get(0).associatedBlocks.add(decoration2);

        GameGroup group = BasicFactory.getInstance().createGameGroup(blocks, 0, 0, 0, zindexes, "created");

		/*
int y =400;
	Vector2 dir = new Vector2(0,1);
	float random = 1f-2*(float)Math.random();
	Utils.rotateVector2(dir,180+random*20);
	Vector2 l = new Vector2(400/32f,y/32f);
	Utils.rotateVector2(l,180+random*20);
	l.sub(400/32f,y/32f);


	dir.mul(120);
com.badlogic.gdx.physics.box2d.Body body = group.getEntityByIndex(0).getBody();

	float desiredAngle = (float)Math.atan2( -dir.x, dir.y );

	body.setTransform(l.mul(-1), desiredAngle );

GameScene.plotter.detachChildren();

	body.setBullet(true);
	body.setLinearVelocity(dir.x,dir.y);

*/


        GameScene.world.addGroup(group);

        for (int j = 0; j < this.indicators.getChildCount(); j++) {
            JointIndicator rev = (JointIndicator) this.indicators.getChildByIndex(j);


            group.createJoint(rev);
        }
return group;

    }

    public void removeJointIndicator(int ID) {
        getIndicator(ID).detachSelf();

    }

    public void selectJointIndicator(int JointID) {

        this.currentJointID = JointID;
        getIndicator(this.currentJointID).select();

        for (int i = 0; i < this.indicators.getChildCount(); i++) {
            JointIndicator ind = (JointIndicator) this.indicators.getChildByIndex(i);
            if (ind.ID != JointID) {
                ind.disselect();
            }
        }

    }

    public JointIndicator getJointIndicator(int ID) {

        return getIndicator(ID);
    }

    public void setJointProperty(JointProperty property) {
        this.getJointIndicator(property.ID).setProperty(property);

    }

    public JointProperty getJointProperty(int ID) {

        return getJointIndicator(ID).getProperty();
    }

    public void setMovableLayers(int[] indexes) {
        getActiveBody().decaledLayerIndexes = indexes;
        if (getActiveBody().state == Body.BodyState.MOVED)
            getActiveBody().setMovableLayers();


    }

    public void setMovableLayers() {
        getActiveBody().changeState(Body.BodyState.MOVED);
        getActiveBody().setMovableLayers();

    }

    public void resetActiveBodyLayers() {
        getActiveBody().resetActiveLayer();
        getActiveBody().changeState(Body.BodyState.SELECTED);

    }

    public void addDecoration(int decorationID, Decoration.DecorationType type) {
        this.getActiveLayer().addDecoration(decorationID, type);
        selectDecoration(decorationID);
    }

    public void selectDecoration(int decorID) {
if(this.getActiveLayer()!=null){
    this.getActiveLayer().selectDecoration(decorID);
}
    }

    public void deselectDecoration(int ID) {
       if(this.getActiveLayer()!=null){
          this.getActiveLayer().deselectDecoration(ID);
       }
    }

    public void setPolygonNumVertices(int number) {
        this.polygonNumVertices = number;
    }


    public void moveElement(DElement movedElement, float dX, float dY) {

        if (movedElement instanceof DPoint) {
            if(movedElement instanceof PrismaticLimitDPoint)movedElement.updatePosition(dX,dY);
            if(movedElement instanceof SizeLimitDImage)movedElement.updatePosition(dX,dY);
            else if(movedElement instanceof RevoluteLimitDPoint)movedElement.updatePosition(dX,5*dY);
            else {
                this.getActiveLayer().updatePosition((DPoint) movedElement, dX, dY);
                Update();
            }
        } else {

            movedElement.updatePosition(dX, dY);
        }
    }


    public Color getColorOfDecoration(int decorationID) {
        return getActiveLayer().getDecoration(decorationID).getColor();
    }
}
