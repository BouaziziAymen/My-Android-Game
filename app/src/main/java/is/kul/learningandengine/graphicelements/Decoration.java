package is.kul.learningandengine.graphicelements;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.BlockImageSubType;
import is.kul.learningandengine.basics.BlockType;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.helpers.Utils;

public class Decoration extends Entity {
LayerShape shape;
public int ID;

private ArrayList<Vector2> points;
Color color;
DecorationType type;
public Color getColor(){return color;}
    public void createPolygonIndicator(Vector2 begin, Vector2 end, float scale, int polygonNumVertices) {
    shape.createPolygonIndicator(begin,end,scale,polygonNumVertices);
    }

    public void createRotationIndicator(Vector2 start, Vector2 end) {
    }

    public void updateRotationIndicator(Vector2 end) {
    }

    public enum DecorationType{
    Cut,Normal
}
enum DecorationState{
	SELECTED,NOTSELECTED
	
}
    public LayerPoints getDecorationPoints() {
        return shape.layerPoints;
    }


Decoration.DecorationState state;
public void setState(Decoration.DecorationState newState){
    state = newState;
if(newState==Decoration.DecorationState.NOTSELECTED) setVisible(false);
else setVisible(true);
}

Layer parent;
@Override
public void setColor(Color c){
    color = c;
}
Layer getLayerParent(){
    return this.parent;
}
public DecorationType getType(){
    return type;
}
public Decoration(int ID,DecorationType type,Layer parent){
    this.parent = parent;
this.type = type;
    this.shape = new LayerShape(this);

    attachChild(this.shape);
    setState(Decoration.DecorationState.SELECTED);
	this.ID = ID;
    setColor(Utils.getColor());

}
public void addPoint(float x, float y, float scale) {
    this.points = this.getLayerParent().generatePoints();

if(this.type==DecorationType.Cut||Utils.PointInPolygon(new Vector2(x,y), this.points))
    this.shape.addPoint(x,y, scale);

}
public Block generateDecorationBlock(){
	Block decoration = BasicFactory.getInstance().createBlock(getPoints(), 0, 0, 0);
	decoration.setID(-1);
	decoration.blockType = BlockType.IMAGE;
	decoration.blockSubType = BlockImageSubType.DECORATION;
	decoration.color = color;
	return decoration;
}
protected ArrayList<Vector2> getPoints() {
	// TODO Auto-generated method stub
	return this.shape.generatePoints();
}
public void insertPoint(float x, float y, float scale) {

    this.shape.insertPoint(x,y, scale);




}
public void unselect() {
    setState(Decoration.DecorationState.NOTSELECTED);

}
public void select() {
    setState(Decoration.DecorationState.SELECTED);
	
}
}
