package is.kul.learningandengine.polygonarithmetic;

import is.kul.learningandengine.helpers.Box2DSeparator;
import is.kul.learningandengine.helpers.Utils;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class FullCutter {

	public ArrayList<Vector2> YANGS;
	public ArrayList<Vector2> YINS;
	public ArrayList<ArrayList<Vector2>> DECOMPOSITION;
	
	public boolean IsClockwise(ArrayList<Vector2> vertices) {
		double sum = 0;
		for (int i = 0; i < vertices.size(); i++) {
			Vector2 v1 = vertices.get(i);
			Vector2 v2 = vertices.get((i + 1) % vertices.size());
			sum += (v2.x - v1.x) * (v2.y + v1.y);
		}
			
		return sum < 0;
	}

	public FullCutter(ArrayList<Vector2> yangs, ArrayList<Vector2> yins) {
		this.YANGS = yangs;
		this.YINS = yins;
		boolean INSIDE = true;
		
		for (int i = 0; i < yangs.size(); i++) {
			for (int j = 0; j < yins.size(); j++) {
				int ii = (i == yangs.size() - 1) ? 0 : i + 1;
				int jj = (j == yins.size() - 1) ? 0 : j + 1;
				if (Utils.doLinesIntersect(yangs.get(i), yangs.get(ii),
						yins.get(j), yins.get(jj))){
					INSIDE=false;break;}
			}
		}
		if(!INSIDE){

			Cutter cutter = new Cutter(yangs, yins);
			DECOMPOSITION = cutter.DECOMPOSITION;
			for(int i=0;i<DECOMPOSITION.size();i++){
				if(!IsClockwise(DECOMPOSITION.get(i)))
					Collections.reverse(DECOMPOSITION.get(i));
			}
		} else {
			
			ArrayList<Vector2> chosen = null;
			if(!IsClockwise(YINS))Collections.reverse(YINS);
ArrayList<ArrayList<Vector2>> polygons = Box2DSeparator.separate(YINS, 30);	
int min = Integer.MAX_VALUE;
for(int i=0;i<polygons.size();i++){
	if(polygons.get(i).size()<min){
		chosen = polygons.get(i);
	min = chosen.size();	
	}
}


Tunnel tunnel = new Tunnel(getCentroid(chosen));
Cutter cutter1 = new Cutter(YANGS, tunnel.list);
Cutter cutter2 = new Cutter(cutter1.DECOMPOSITION.get(0), YINS);
DECOMPOSITION = cutter2.DECOMPOSITION;

	}

	}
float polygonSurface(ArrayList<Vector2> polygon){
	float A = 0;
	for(int i=0;i<polygon.size();i++){
		int ni = (i==polygon.size()-1)?0:i+1;
		A +=(polygon.get(i).x*polygon.get(ni).y-polygon.get(ni).x*polygon.get(i).y);
	}
	A = A/2;
	return A;
}
Vector2 getCentroid(ArrayList<Vector2> polygon){
	float A6 = 6*polygonSurface(polygon);
	float Cx = 0;
	for(int i=0;i<polygon.size();i++){
		int ni = (i==polygon.size()-1)?0:i+1;
		Cx +=(polygon.get(i).x+polygon.get(ni).x)*(polygon.get(i).x*polygon.get(ni).y-polygon.get(ni).x*polygon.get(i).y);
	}
	Cx = Cx/A6;
	
	
	float Cy = 0;
	for(int i=0;i<polygon.size();i++){
		int ni = (i==polygon.size()-1)?0:i+1;
		Cy +=(polygon.get(i).y+polygon.get(ni).y)*(polygon.get(i).x*polygon.get(ni).y-polygon.get(ni).x*polygon.get(i).y);
	}
	Cy = Cy/A6;
	
	return new Vector2(Cx,Cy);
}



}




