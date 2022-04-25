package is.kul.learningandengine.basics;

import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.GameScene;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.andengine.util.adt.color.Color;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;

public class Outline {
enum NodeType{
	O,I,B
}
	

    ArrayList<Vector2> points;
	private ArrayList<Cut> cuts;
	public String string;


	public Outline(ArrayList<Vector2> outline, ArrayList<Vector2> path){


        this.points = outline;
//Utils.drawPath(outline, Color.BLUE);
//Utils.drawPath(path, Color.RED);

ArrayList<Outline.Flag>all = new ArrayList<Outline.Flag>();
for(int i=0;i<path.size();i++){
	int ni = i==path.size()-1 ?0:i+1;
	Vector2 p1 = path.get(i);
	Vector2 p2 = path.get(ni);
	boolean log = false;

	ArrayList<Outline.Flag> result = getLineIntersections(p1, p2);

if(result.size()>0){
if(!result.get(0).onBorder)
   all.add(new Outline.Flag(p1, this.getType(p1), false, false, false));
} else
   all.add(new Outline.Flag(p1, this.getType(p1), false, false, false));
all.addAll(result);

}

int N = all.size();
all.addAll(all);
        this.setCuts(new ArrayList<Cut>());
int k = 0;
for(int i=0;i<N;i++){

	Outline.Flag first = all.get(i);

	if(first.type==Outline.NodeType.B){
		k++;
		ArrayList<Vector2> inners = new ArrayList<Vector2>();
		for(int j=i+1;j<all.size();j++){
		Outline.Flag second = all.get(j);
		if(second.type==Outline.NodeType.O)break;
		if(second.type==Outline.NodeType.I)inners.add(second.center);
		if(second.type==Outline.NodeType.B){
			Cut cut = new Cut(first.center, second.center, 0);
            this.getCuts().add(cut);
			cut.inners = inners;
			cut.setDead(false);
			break;
		}
	}

	}


}
        this.string = Arrays.toString(all.toArray())+"%"+ this.cuts.size()+"?k"+k;

Iterator<Cut> iterator = this.getCuts().iterator();
while(iterator.hasNext()){
	Cut cut = iterator.next();
if(!testCut(cut))iterator.remove();
}



}

	public boolean testCut(Cut cut){
		for(int i = 0; i< this.points.size(); i++){
			int ni = i== this.points.size()-1 ?0:i+1;
			if(cut.inners.size()==0)
			if(Utils.PointOnLineSegment(this.points.get(i), this.points.get(ni), cut.getP1(), 0.01f)&&
			Utils.PointOnLineSegment(this.points.get(i), this.points.get(ni), cut.getP2(), 0.01f))
			return false;
		}
		return true;

	}


public Outline.NodeType getType(Vector2 p){
	for(int i = 0; i< this.points.size(); i++){
		int ni = i== this.points.size()-1 ?0:i+1;
		if(Utils.PointOnLineSegment(this.points.get(i), this.points.get(ni), p, 0.01f))return Outline.NodeType.B;
	}
if(Utils.PointInPolygon(p, this.points))return Outline.NodeType.I; else return Outline.NodeType.O;
}



	public ArrayList<Outline.Flag> getLineIntersections(Vector2 p1, Vector2 p2){
		ArrayList<Outline.Flag>result = new ArrayList<Outline.Flag>();
		for(int i = 0; i< this.points.size(); i++){
			int ni = i== this.points.size()-1 ?0:i+1;
			Outline.Flag flag = getLineToLineTest(this.points.get(i), this.points.get(ni), p1, p2);


			if(flag!=null){
				flag.setIndex(i);
				flag.setValue(flag.center.dst(p1));
			if(!flag.onExB)
			result.add(flag);

			}
		}

		Collections.sort(result);
		return result;
	}




class Flag implements Comparable<Outline.Flag>{
		Vector2 center;
		Outline.NodeType type;
		boolean onExA, onExB;
		float value;
		boolean onBorder;
		int index;
    Flag(Vector2 center,Outline.NodeType type, boolean onexA, boolean onexB, boolean onBorder){
	this.center = center;
	this.type = type;
        onExA = onexA;
        onExB = onexB;
	this.onBorder = onBorder;
}
		public void setIndex(int i) {
            index =i;

	}
		public void setValue(float value) {
		this.value = value;

	}
		@Override
		public int compareTo(Outline.Flag another) {
			if(value <another.value)return -1; else return 1;
		}
		public String toString(){
			return "{"+ this.center + this.type +"}";
		}

	}


	public Outline.Flag getLineToLineTest(Vector2 ex1, Vector2 ex2, Vector2 p1, Vector2 p2){
		Vector2 intersection = this.getIntersection(ex1,ex2, p1 ,p2);
		if(intersection==null){ // NO INTERSECTION
		boolean p1InBorder = Utils.PointOnLineSegment(ex1, ex2, p1, 0.01f);
		boolean p2InBorder = Utils.PointOnLineSegment(ex1, ex2, p2, 0.01f);

		if(p1InBorder&&p2InBorder){
			//TWO FLAGS THAT DONT COUNT DO NOTHING
		} else if(p1InBorder){
			//ONE FLAG THAT CONTAINS P1
			boolean onExtremityA = false,onExtremityB = false;
			intersection = p1;
			if(p1.dst(ex1)<0.01f){
			onExtremityA = true;
			intersection = ex1;

			}
			else if(p1.dst(ex2)<0.01f){
			onExtremityB = true;
			intersection = ex2;
			}

			return new Outline.Flag(intersection,Outline.NodeType.B,onExtremityA,onExtremityB,true);

		} else if(p2InBorder){
			//ONE FLAG THAT CONTAINS P2
return null;

		} else {

		}

		} else {
			boolean onExtremityA = false,onExtremityB = false;

			if(intersection.dst(ex1)<0.01f){
			onExtremityA = true;
			intersection = ex1;
			}
			else if(intersection.dst(ex2)<0.01f){
			onExtremityB = false;
			intersection = ex2;
			}

			return new Outline.Flag(intersection,Outline.NodeType.B,onExtremityA,onExtremityB,false);

		//ONE FLAG THAT CONTAINS INTERSECTION AND INFO IF EX
		}
		
		
		return null;
	}
	
	
	Vector2 getNearPoint(Vector2 v){
		for(Vector2 p: this.points)if(v.dst(p)<=0.01f)return p;
		return null;
	}
	
	
	
	public  Vector2 getIntersection(Vector2 fixedA, Vector2 fixedB, Vector2 cutterA, Vector2 cutterB){
if(areColinearInter(fixedA, fixedB, cutterA, cutterB))return null;
	Vector2 u = fixedB.cpy().sub(fixedA).nor().mul(0.01f);
	
	Vector2 vB = fixedB.cpy().add(u);
	Vector2 vA = fixedA.cpy().sub(u);
	Vector2 intersection = Utils.lineIntersectPoint(vA.x, vA.y,vB.x, vB.y, cutterA.x, cutterA.y, cutterB.x, cutterB.y);
	
	return intersection;
	}
	
	
	
	boolean areColinearInter(Vector2 fixedA, Vector2 fixedB, Vector2 cutterA, Vector2 cutterB){
	Vector2 U = fixedB.cpy().sub(fixedA).nor();
	
	Vector2 V = cutterB.cpy().sub(cutterA).nor();
        return Math.abs(U.x * V.y - U.y * V.x) < 0.001f;


    }

	public ArrayList<Cut> getCuts() {
		return this.cuts;
	}

	public void setCuts(ArrayList<Cut> cuts) {
		this.cuts = cuts;
	}
	
	

	
	
	
	
	
}
