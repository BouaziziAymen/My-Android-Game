package is.kul.learningandengine.basics;

import is.kul.learningandengine.factory.VerticesFactory;
import is.kul.learningandengine.helpers.UnionFind;
import is.kul.learningandengine.helpers.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.andengine.util.adt.color.Color;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class Expandable {

HashSet<Grain> usedGrains;
Color color;

ArrayList<Block> main;

private static final int numberOfVertices = 4;
private static final float radius = (float) (16*Math.sqrt(2));

Block affectedBlock;
ArrayList<Vector2> surround;
Block createShunk(Grain g){
	
	if(!this.testPoint(g.position))return null;
	ArrayList<Vector2> path = VerticesFactory.createPolygon(Expandable.numberOfVertices,(float)Math.PI/4, Expandable.radius, Expandable.radius,g.position.x, g.position.y);
    ArrayList<Vector2> sur = new ArrayList<Vector2>();
	for(int k = 0; k< this.surround.size(); k++)sur.add(this.surround.get(k).cpy());
	Block b = new Block(path);
	b.applyClipByPath(sur, g.position);
	return b;
}
boolean colorUpdated;
boolean shapeUpdated;
public void update(){
    this.colorUpdated = false;
    this.shapeUpdated = false;
	outer:
	for(Grain g: this.affectedBlock.grid.allGrains){
	if(g.hasLayers()){
		boolean found = false;
			for(Block b: this.main){
		
				if(b.GRAIN==g){
					found = true;
				
					b.color = g.getColorResult();
                    this.colorUpdated = true;
					
					
				}
				
			}
		if(!found){
			Block b = createShunk(g);
			if(b!=null){
			b.computeMeshTriangles();
			b.blockType = BlockType.IMAGE;
			b.blockSubType = BlockImageSubType.EXPANDABLE;
			b.color = g.getColorResult();
			b.setID(-1);
			this.main.add(b);
			b.GRAIN = g;
			this.shapeUpdated = true;
			}
			}	
	}
	}
}
public Expandable(Block affectedBlock, ArrayList<Block> already){
	this.affectedBlock = affectedBlock;

    surround = affectedBlock.VERTICES;
	
	if(already==null)
        this.main = new ArrayList<Block>();
	else this.main = already;



}


public ArrayList<Block> getBlocks(){
	return this.main;
}







public boolean testPoint(Vector2 position) {

	 for(int i = 0; i< surround.size(); i++){
		 int ni = i== this.surround.size()-1 ?0:i+1;
			Vector2 v1 = this.surround.get(i);
			Vector2 v2 = this.surround.get(ni);
		
	
	if(Utils.EdgeInterCircle( position.x, position.y,0.1f,v1, v2)){
		return false;
	}
		 

}
	 return true;
}








}