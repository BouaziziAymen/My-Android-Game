package is.kul.learningandengine.basics;

import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.GameScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Crack {
	class Pair {
		Vector2 v1, v2;
		
		Pair(Vector2 l1, Vector2 l2){

            this.v1 = l1;
            this.v2 = l2;
	

		}

		
	}

class Edge{
ArrayList<Crack.Pair> pairs;

	Edge(){

        this.pairs = new ArrayList<Crack.Pair>();

        this.pairs.add(new Crack.Pair(this.modify(0, 20), this.modify(0, -20)));
        this.pairs.add(new Crack.Pair(this.modify(20, 0), this.modify(-20, 0)));
        this.pairs.add(new Crack.Pair(this.modify(-20, -20), this.modify(20, 20)));
        this.pairs.add(new Crack.Pair(this.modify(20, -20), this.modify(-20, 20)));
	}
	Vector2 modify(float x, float y){
		Vector2 v = Crack.this.body.getLocalVector(new Vector2(x,y)).cpy().mul(32);
		Vector2 inter  =  Crack.this.localCenter.cpy().add(v);
		return inter;
	}

}

Body body;
Vector2 localCenter;

    public  Crack(DCGameEntity entity,Block block,Grain nearest,float energy, int order,Vector2 point){
	//HashSet<Grain> grains = new HashSet<Grain>();
	//float brittleness = 0.4f;
	//for(Grain g:block.grid.allGrains){
		//if(Math.random()<brittleness)grains.add(g);
	//}

        this.body = entity.getBody();
    this.localCenter = nearest.position;

	Crack.Edge edge = new Crack.Edge();
	ArrayList<Cut> cuts = new ArrayList<Cut>();
	for(Crack.Pair pair:edge.pairs){
		Cut cut  = block.slice(nearest.position,pair.v1,pair.v2);

		if(cut!=null)
		cuts.add(cut);
	}
	if(cuts.size()>0){
	Collections.sort(cuts);
	
	Cut chosen = cuts.get(0);


	energy-=chosen.getValue()*entity.qe;
	if(energy>0){
	
	cuts.clear();
	cuts.add(chosen);
	chosen.blockId = block.getID();
	
	ArrayList<DCGameEntity> pieces = entity.applyCuts(cuts);
	
	if(pieces!=null&&order<5&&chosen.isDead()){
		order++;
	Iterator<DCGameEntity> iterator = pieces.iterator();
	while(iterator.hasNext()){
		DCGameEntity e = iterator.next();
		if(e.getBlock(block.getID())==null)iterator.remove();
	}
	for(DCGameEntity e:pieces){
		
		e.applyImpact(e.getBlock(block.getID()), point.cpy(), energy,order);
	}
	}
	
	}
	}
}
	
}
