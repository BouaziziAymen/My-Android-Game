package is.kul.learningandengine.helpers;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.scene.Cut;
import is.kul.learningandengine.scene.GameScene;


public class Utils {
static	boolean successive(int order1,int order2,int n){
		boolean result;
		result =(order1==n-1)?(order2==0)?true:false:(order2==order1+1)?true:false;
		return result;
	}
public static	boolean areNeighbors(int ord1,int ord2,int n){
		return (successive(ord1,ord2,n)||successive(ord2,ord1,n));
	}
	public static boolean collision(Line l1, Line l2,int order1, int order2, int n,String s){

if(areNeighbors(order1,order2,n))return false;

		if (l1.collidesWith(l2)){
			Log.e("collisionxxx",""+l1+"/"+l2+" "+s);
			return true;
		}
		else return false;
	}

	public static boolean collision(Line l1, Line l2){
		if(l1.getX1()==l2.getX1()&&l1.getY1()==l2.getY1())return false;
		if(l1.getX1()==l2.getX2()&&l1.getY1()==l2.getY2())return false;
		if(l1.getX2()==l2.getX1()&&l1.getY2()==l2.getY1())return false;
		if(l1.getX2()==l2.getX2()&&l1.getY2()==l2.getY2())return false;
		return l1.collidesWith(l2);
	}

	public static boolean vectorEquivalence(Vector2 v1, Vector2 v2){
        return Math.abs(v1.x - v2.x) < 0.0001f && Math.abs(v1.y - v2.y) < 0.0001f;
    }
	public static boolean vectorEquivalence3(Vector2 v1, Vector2 v2){
        return Math.abs(v1.x - v2.x) < 0.01f && Math.abs(v1.y - v2.y) < 0.01f;
    }
	public static boolean vectorEquivalence2(Vector2 v1, Vector2 v2){
        return v1.dst(v2) < 0.1f;
    }
	public static Color getColorTemp(float temp){
		float blue = (1000 - temp)/1000f;
		float red = temp/1000f;
		return new Color(red,0,blue);
	}
	
	
	public static Color blendColors(Color background,Color foreground){
	float outputRed = foreground.getRed() * foreground.getAlpha() + background.getRed() * (1 - foreground.getAlpha());
	float outputGreen = foreground.getGreen() * foreground.getAlpha() + background.getGreen() * (1 - foreground.getAlpha());
	float outputBlue = foreground.getBlue() * foreground.getAlpha() + background.getBlue() * (1 - foreground.getAlpha());
	
	return new Color(outputRed,outputGreen,outputBlue);
	}
	
	public static float shortestDistance(float x1,float y1,float x2,float y2,float x3,float y3)
    {
        float px=x2-x1;
        float py=y2-y1;
        float temp= px*px + py*py;
        float u=((x3 - x1) * px + (y3 - y1) * py) / temp;
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        float x = x1 + u * px;
        float y = y1 + u * py;

        float dx = x - x3;
        float dy = y - y3;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return dist;

    }

	public static float getArea(Vector2[] Vertices) {

		float A = 0;
		for(int i = 0; i< Vertices.length; i++){
			int ni = i== Vertices.length-1 ?0:i+1;
			A += Vertices[i].x* Vertices[ni].y- Vertices[ni].x* Vertices[i].y;
		}
		A = A/2;
		if(A>=0)	return A;else return -A;
	}


	public static float getArea(ArrayList<Vector2> Vertices){


		float A = 0;
		for(int i = 0; i< Vertices.size(); i++){
			int ni = i== Vertices.size()-1 ?0:i+1;
			A += Vertices.get(i).x* Vertices.get(ni).y- Vertices.get(ni).x* Vertices.get(i).y;
		}
		A = A/2;
		if(A>=0)	return A;else return -A;
	}
	
	public static boolean IsClockwise(ArrayList<Vector2> vertices)
	{
		
		
	    double sum = 0;
	    for (int i = 0; i < vertices.size(); i++) {
	        Vector2 v1 = vertices.get(i);
	        Vector2 v2 = vertices.get((i + 1) % vertices.size());
	        sum += (v2.x - v1.x) * (v2.y + v1.y);
	    }
	    return sum < 0;
	}

	public static ArrayList<ArrayList<Vector2>> splitFinal(Cut cut, ArrayList<Vector2> vertices) {
		ArrayList<Vector2> chunk1 = new ArrayList<Vector2>();
		ArrayList<Vector2> chunk2 = new ArrayList<Vector2>();
		ArrayList<Vector2> list = new ArrayList<Vector2>();
		ArrayList<ArrayList<Vector2>> groups = new ArrayList<ArrayList<Vector2>>();
		list.addAll(vertices);

		int ip2 = cut.ip2;

		int iq2 = cut.iq2;

		list.addAll(vertices);
		for(int i=ip2;i<list.size();i++){
			chunk1.add(list.get(i).cpy());
			if(list.get(i)==cut.x2a)break;
		}

		for(int i=iq2;i<list.size();i++){
			chunk2.add(list.get(i).cpy());
			if(list.get(i)==cut.x1a)break;
		}
      if(cut.getP1()!=cut.x1b)chunk1.add(0,cut.getP1().cpy());
	  if(cut.getP2()!=cut.x2a)chunk1.add(cut.getP2().cpy());
		//CHECK ADDED IF ADDED P1 ALONE MAKE SURE NO ALIGNMENT
		//IF ADDED P2 ALONE
		//IF ADDED P2 AND P1
		//IF ADDED NEITHER

		if(cut.getP2()!=cut.x2b)chunk2.add(0,cut.getP2().cpy());
		if(cut.getP1()!=cut.x1a)chunk2.add(cut.getP1().cpy());



bruteForceRectification(chunk1);
bruteForceRectification(chunk2);

groups.add(chunk1);
		groups.add(chunk2);
		return groups;
	}
	public static void bruteForceRectification(ArrayList<Vector2> polygon){
		boolean repeat;
	do {
		 repeat = false;
		for (int i = 0; i < polygon.size(); i++) {
			int pi = (i == 0) ? polygon.size() - 1 : i - 1;
			int ni = (i == polygon.size() - 1) ? 0 : i + 1;
			Vector2 p = polygon.get(i);
			Vector2 pp = polygon.get(pi);
			Vector2 pn = polygon.get(ni);
			Vector2 u = p.cpy().sub(pp).nor();
			Vector2 v = p.cpy().sub(pn).nor();
			if (Math.abs(u.cross(v)) < 0.1f) {
				repeat = true;
				Log.e("rectification","rectification");
				polygon.remove(p);
				break;
			}
		}
	} while (repeat);

	}

	public static void rotatePolygon(float angle, ArrayList<Vector2> vertices,float cX,float cY) {
	for(Vector2 p: vertices){
		Vector2 dir = p.sub(cX,cY);
		rotateVector2(dir,angle);
		p.add(cX,cY);
	}
	}


    public static class Normal{
		public Vector2 N;
		public Vector2 origin;

		public Normal(Vector2 n,Vector2 lower){
            this.N = n;
            origin = lower;
		}
	}
	public static float getMaxProjection(ArrayList<Vector2> inners,Utils.Normal normal){
	ArrayList<Float> floats = new ArrayList<Float>();
		for(Vector2 p:inners){
			float proj = normal.N.dot(p.cpy().sub(normal.origin));
			floats.add(proj);
		}
		return Collections.max(floats);

	}

	public static float getMinProjection(ArrayList<Vector2> inners,Utils.Normal normal){
		ArrayList<Float> floats = new ArrayList<Float>();
			for(Vector2 p:inners){
				float proj = normal.N.dot(p.cpy().sub(normal.origin));
				floats.add(proj);
			}
			return Collections.min(floats);

		}


	public static Utils.Normal getNormal(Vector2 pLower,Vector2 pUpper){
		Vector2 nor = new Vector2(-(pUpper.y-pLower.y), pUpper.x-pLower.x).nor();
		//Vector2 midPoint = new Vector2(pLower.x+(pUpper.x-pLower.x)/2,pLower.y+(pUpper.y-pLower.y)/2);
		return new Utils.Normal(nor,pLower);
	}
	
	public static boolean segmentsOverlap(Vector2 pA1, Vector2 pA2, Vector2 pB1, Vector2 pB2){
		
	boolean A1In = Utils.PointOnLineSegment( pB1, pB2,pA1, 0.00001f);
	boolean A2In = Utils.PointOnLineSegment( pB1, pB2,pA2, 0.00001f);
	boolean B1In = Utils.PointOnLineSegment( pA1, pA2,pB1, 0.00001f);
	boolean B2In = Utils.PointOnLineSegment( pA1, pA2,pB2, 0.00001f);
	if(A1In&&A2In)return true;
	if(B1In&&B2In)return true;
	if(A1In&&B1In)return true;
	if(A1In&&B2In)return true;
	if(A2In&&B1In)return true;
        return A2In && B2In;

    }


	
public static boolean isOnBorder(Vector2 point, ArrayList<Vector2> layer){		
for(int i=0;i<layer.size();i++){
            int ni = i==layer.size()-1 ?0:i+1;
			Vector2 p = layer.get(i);
			Vector2 q = layer.get(ni);
		if(PointOnLineSegment(p, q, point, 0.001f))return true;
}
return false;
}

public static int isOnBorderP(Vector2 point, ArrayList<Vector2> layer){		
for(int i=0;i<layer.size();i++){
            int ni = i==layer.size()-1 ?0:i+1;
			Vector2 p = layer.get(i);
			Vector2 q = layer.get(ni);
		if(PointOnLineSegment(p, q, point, 0.1f))return i;
}
return -1;
}

	public static boolean getRelativePositionOfLayers(ArrayList<Vector2> layer1,ArrayList<Vector2> layer2 ){
		for(Vector2 point:layer1){
			if(PointInPolygon(point,layer2)){return true;}
		}
		for(Vector2 point:layer2){
			if(PointInPolygon(point,layer1)){return true;}
		}
		for(int i=0;i<layer1.size();i++){
			int ni = i==layer1.size()-1 ?0:i+1;
			Vector2 p1 = layer1.get(i);
			Vector2 q1 = layer1.get(ni);
			for(int j=0;j<layer2.size();j++){
				int nj = j==layer2.size()-1 ?0:j+1;
				Vector2 p2 = layer2.get(j);
				Vector2 q2 = layer2.get(nj);
				if(lineIntersect(p1.x, p1.y, q1.x, q1.y, p2.x, p2.y, q2.x, q2.y)){return true;}
				
			}
		}
		return false;
	}
	boolean isAligned(Grain begin, Grain end, Grain g){
		Vector2 v = end.position.cpy().sub(begin.position).nor();
		Vector2 u = g.position.cpy().sub(begin.position).nor();
		return Math.abs(u.cross(v))<0.01f;
	}



	static Random rand = new Random();
	public static Color getColor(){

		return new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
	}
	public static Color getColorWithAlpha(){

		return new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
	}


	public static Color getColor(float alpha){

		return new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),alpha);
	}
	public static float getAngle(Vector2 A, Vector2 B, Vector2 C){
		Vector2 BA = A.cpy().sub(B);
		Vector2 BC = C.cpy().sub(B);
		return (float) Math.acos(BA.dot(BC)/ (BA.len()*BC.len()));
	}
	
	public static boolean isContainedAInB(ArrayList<Grain> A,ArrayList<Grain> B){
		if(B.containsAll(A))return true;
		
			boolean inside = true;
			ArrayList<Vector2> points = new ArrayList<Vector2>();
			for(Grain grain:B)points.add(grain.position);
			for(Grain g:A){
			
				if(!B.contains(g)&&!PointInPolygon(g.position,points )){inside=false;break;}
			}
			return inside;
		
	}
	
	
	
	
	
	public static Vector2 getTriangleCentroid(Vector2 v1, Vector2 v2, Vector2 v3){
		return new Vector2((v1.x + v2.x + v3.x) / 3
		,(v1.y + v2.y + v3.y) / 3);
	}
	
	public static boolean convex(Vector2 v1,Vector2 v2, 
		       Vector2 v3)
		{
            return Utils.area(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y) < 0;
		}


		/* area:  determines area of triangle formed by three points
		 */
	public static float area(float x1, float y1, float x2, float y2,
		    float x3, float y3)
		{
		float areaSum = 0;

		areaSum += x1 * (y3 - y2);
		areaSum += x2 * (y1 - y3);
		areaSum += x3 * (y2 - y1);

		/* for actual area, we need to multiple areaSum * 0.5, but we are
		     * only interested in the sign of the area (+/-)
		     */

		return areaSum;
		}
	
	
	
	
	
	
	
	
	public static int normalise(  float f) 
	{
		return (int) (f + Math.ceil( -f / 360 ) * 360);

	}
	
	
	static ArrayList<Vector2> copyExts(int a, int b,ArrayList<Vector2> vectors){
		int n= vectors.size();
		ArrayList<Vector2> nvectors = new ArrayList<Vector2>();
		if(a<b){

			for(int i=a;i<=b;i++){
			nvectors.add(vectors.get(i).cpy());
		
			}
			
			
		} else{
			
			for(int i=a;i<n;i++){
				nvectors.add(vectors.get(i).cpy());
			
				}
			nvectors.add(vectors.get(0).cpy());
		}
	
		return nvectors;
	}
	
public static ArrayList<ArrayList<Vector2>> decompose(ArrayList<Vector2> vectors){
ArrayList<ArrayList<Vector2>> result = new 	ArrayList<ArrayList<Vector2>>();
int N = vectors.size();
if(N<=8)result.add(vectors);
else {
	ArrayList<Vector2> part1 = new ArrayList<Vector2>();
	ArrayList<Vector2> part2 = new ArrayList<Vector2>();
int index = (N-1)/2 + 1;

for(int i=0;i<=index;i++)part1.add(vectors.get(i).cpy());
part2.add(vectors.get(index).cpy());
for(int i=index+1;i<vectors.size();i++)part2.add(vectors.get(i).cpy());
part2.add(vectors.get(0).cpy());

result.addAll(Utils.decompose(part1));
result.addAll(Utils.decompose(part2));
}


return result;
}




public static boolean isInside(ArrayList<Vector2> associated,ArrayList<Vector2> layer){


	for(Vector2 point:associated){
		if(!PointInPolygon(point,layer)&&!isOnBorder(point, layer))return false;
	}
	

	
	return true;
	
}




public static int indexOf(int a, int[] base){
	for(int i=0;i<base.length;i++){
		if(base[i]==a)return i;
	}
	return -1;
}
	
	
	public static Inter intersectSegmentCircle(Line line,Vector2 P,float radius, int[] is){
		
		float x1 = line.getX1()-P.x;
		float x2 = line.getX2()-P.x;
		float y1 = line.getY1()-P.y;
		float y2 = line.getY2()-P.y;
		

		Vector2 v1 = new Vector2(x1,y1), v2=new Vector2(x2,y2);
		
		P = new Vector2(0,0);
		if(Utils.FindDistanceToSegment(P,v1,v2)<radius){
			
		
			
			return new Inter(true, Utils.FindDistanceToSegment(P,v1,v2),is);
			
		
			
		} else {
			
			return new Inter(false, 0,is);
		}
		
		
	}
	
	// Calculate the distance between
	// point pt and the segment p1 --> p2.
public static float FindDistanceToSegment(
	    Vector2 pt, Vector2 p1, Vector2 p2)
	{
		Vector2 closest;
	    float dx = p2.x - p1.x;
	    float dy = p2.y - p1.y;
	    if (dx == 0 && dy == 0)
	    {
	        // It's a point not a line segment.
	        closest = p1;
	        dx = pt.x - p1.x;
	        dy = pt.y - p1.y;
	        return (float) Math.sqrt(dx * dx + dy * dy);
	    }

	    // Calculate the t that minimizes the distance.
	    float t = ((pt.x - p1.x) * dx + (pt.y - p1.y) * dy) /
	        (dx * dx + dy * dy);

	    // See if this represents one of the segment's
	    // end points or a point in the middle.
	    if (t < 0)
	    {
	        closest = new Vector2(p1.x, p1.y);
	        dx = pt.x - p1.x;
	        dy = pt.y - p1.y;
	    }
	    else if (t > 1)
	    {
	        closest = new Vector2(p2.x, p2.y);
	        dx = pt.x - p2.x;
	        dy = pt.y - p2.y;
	    }
	    else
	    {
	        closest = new Vector2(p1.x + t * dx, p1.y + t * dy);
	        dx = pt.x - closest.x;
	        dy = pt.y - closest.y;
	    }

	    return (float) Math.sqrt(dx * dx + dy * dy);
	}
	static int sgn(float f){
		if(f<0)return -1; else return 1;
	}
	
	
	
	
	
	
	public static boolean circleLineIntersect(Vector2 p1, Vector2 p2, float 

	        cx, float cy, float R ) {
	      float dx = p2.x - p1.x;
	      float dy = p2.y - p1.y;
	      float a = dx * dx + dy * dy;
	      float b = 2 * (dx * (p1.x - cx) + dy * (p1.y - cy));
	      float c = cx * cx + cy * cy;
	      c += p1.x * p1.x + p1.y * p1.y;
	      c -= 2 * (cx * p1.x + cy * p1.y);
	      c -= R * R;
	      float bb4ac = b * b - 4 * a * c;

        return bb4ac >= 0;
	}

	
	public static Vector2 midPoint(float p1x,float p1y, float p2x, float p2y) {
	     float mx = (p1x + p2x)/2;
	     float my = (p1y + p2y)/2;
	     return new Vector2(mx,my);
	}
	public static  Vector2 FindLineSegmentCircleIntersection(
		    float cx, float cy, float radius,
		    Vector2 point1, Vector2 point2){
		
		Vector2 inter = FindLineCircleIntersection(cx, cy, radius, point1, point2);
		if(inter!=null)
		if(PointOnLineSegment(point1,point2,inter, 0.01f))return inter;
		return null;
		
	}
	

	
	public static boolean PointOnLineSegment(Vector2 pt1, Vector2 pt2, Vector2 pt, float epsilon)
	{
	  if (pt.x - Math.max(pt1.x, pt2.x) > epsilon || 
	      Math.min(pt1.x, pt2.x) - pt.x > epsilon || 
	      pt.y - Math.max(pt1.y, pt2.y) > epsilon || 
	      Math.min(pt1.y, pt2.y) - pt.y > epsilon)
	    return false;

	  if (Math.abs(pt2.x - pt1.x) < epsilon)
	    return Math.abs(pt1.x - pt.x) < epsilon || Math.abs(pt2.x - pt.x) < epsilon;
	  if (Math.abs(pt2.y - pt1.y) < epsilon)
	    return Math.abs(pt1.y - pt.y) < epsilon || Math.abs(pt2.y - pt.y) < epsilon;

	  double x = pt1.x + (pt.y - pt1.y) * (pt2.x - pt1.x) / (pt2.y - pt1.y);
	  double y = pt1.y + (pt.x - pt1.x) * (pt2.y - pt1.y) / (pt2.x - pt1.x);

	  return Math.abs(pt.x - x) < epsilon || Math.abs(pt.y - y) < epsilon;
	}
	
	
	public static  Vector2 FindLineCircleIntersection(
		    float cx, float cy, float radius,
		    Vector2 point1, Vector2 point2)
		{
		    float dx, dy, A, B, C, det;

		    dx = point2.x - point1.x;
		    dy = point2.y - point1.y;

		    A = dx * dx + dy * dy;
		    B = 2 * (dx * (point1.x - cx) + dy * (point1.y - cy));
		    C = (point1.x - cx) * (point1.x - cx) +
		        (point1.y - cy) * (point1.y - cy) -
		        radius * radius;

		    det = B * B - 4 * A * C;
		    if (A <= 0.0000001 || det < 0)
		    {
		        // No real solutions.
		        
		        return null;
		    }
		    else if (det == 0)
		    {
		        // One solution.
		       float t1 = -B / (2 * A);
		
		       Vector2 v = new Vector2(point1.x + t1 * dx, point1.y + t1 * dy);
		     
		        return v;
		   
		    }
		    else
		    {
		        // Two solutions.
		     float   t1 = (float)((-B + Math.sqrt(det)) / (2 * A));
		   
		       float t2 = (float)((-B - Math.sqrt(det)) / (2 * A));
		     
		        return Utils.midPoint(point1.x + t1 * dx, point1.y + t1 * dy,point1.x + t2 * dx, point1.y + t2 * dy);
		    }
		}
		    
	
	
		    
		    
		    
		    public static  boolean EdgeInterCircle(
				    float cx, float cy, float radius,
				    Vector2 point1, Vector2 point2)
				{
				    float dx, dy, A, B, C, det;

				    dx = point2.x - point1.x;
				    dy = point2.y - point1.y;

				    A = dx * dx + dy * dy;
				    B = 2 * (dx * (point1.x - cx) + dy * (point1.y - cy));
				    C = (point1.x - cx) * (point1.x - cx) +
				        (point1.y - cy) * (point1.y - cy) -
				        radius * radius;

				    det = B * B - 4 * A * C;
				    if (A <= 0.0000001 || det < 0)
				    {
				        // No real solutions.
				        
				        return false;
				    }
				    else if (det == 0)
				    {
				        // One solution.
				       float t1 = -B / (2 * A);
				
				       Vector2 v = new Vector2(point1.x + t1 * dx, point1.y + t1 * dy);
						
						return PointOnLineSegment(point1,point2,v, 0.01f);
				   
				    }
				    else
				    {
				        // Two solutions.
				     float   t1 = (float)((-B + Math.sqrt(det)) / (2 * A));
				   Vector2 v1 = new Vector2(point1.x + t1 * dx, point1.y + t1 * dy);
				       float t2 = (float)((-B - Math.sqrt(det)) / (2 * A));
				       Vector2 v2 = new Vector2(point1.x + t2 * dx, point1.y + t2 * dy);
				      boolean b1 = PointOnLineSegment(point1,point2,v1, 0.01f);
				      boolean b2 = PointOnLineSegment(point1,point2,v2, 0.01f);
				  	return b1||b2;
				    }
				    
		    
		    
		    
		  
		    
		    
		}
	
	
	
	
	
	
	public static Vector2 orthProjection(float xB, float yB, float cx, float cy,float xA, float yA){
		Vector2 v = new Vector2(xA-xB,yA-yB).nor();
		float bh = (cx-xB) *v.x+ (cy-yB) *v.y;
	return new Vector2(xB+bh*v.x,yB+bh*v.y);
	}
	
	
	
	public static Vector2 transformVector(Vector2 pp, Vector2 p, Vector2 np, ArrayList<Vector2> layer){
		Vector2 v = pp.cpy().sub(p);
		Vector2 u = np.cpy().sub(p);
		
		
		Vector2 n = v.add(u).nor().mul(0.5f*(1+(float)Math.random()));
		Vector2 r1 = p.cpy().add(n);
		if(PointInPolygon(r1, layer))return r1; else return p.cpy().sub(n);
	}
	
	
	
	
	
	public static boolean PointInPolygon(Vector2 point, ArrayList<Vector2> points) {
		
		  int i, j, nvert = points.size();
		  boolean c = false;

		  for(i = 0, j = nvert - 1; i < nvert; j = i++) {
		    if( points.get(i).y >= point.y != points.get(j).y >= point.y &&
                    point.x <= (points.get(j).x - points.get(i).x) * (point.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x
		      )
		      c = !c;
		  }

		  return c;
		}

	
	
	
	
	public static boolean PointInPolygon(float x, float y, ArrayList<Vector2> points) {
		
		  int i, j, nvert = points.size();
		  boolean c = false;

		  for(i = 0, j = nvert - 1; i < nvert; j = i++) {
		    if( points.get(i).y >= y != points.get(j).y >= y &&
                    x <= (points.get(j).x - points.get(i).x) * (y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x
		      )
		      c = !c;
		  }

		  return c;
		}


	public static boolean PointInPolygon(float x, float y, Vector2[] points) {

		int i, j, nvert = points.length;
		boolean c = false;

		for(i = 0, j = nvert - 1; i < nvert; j = i++) {
			
			if( points[i].y >= y != points[j].y >= y &&
					x <= (points[j].x - points[i].x) * (y - points[i].y) / (points[j].y - points[i].y) + points[i].x
					)
				c = !c;
		}

		return c;
	}
	
	
	//THIS IS APPLICABLE TO SEGMENTS
	
	public static Vector2 intersectionPoint(Vector2 InitialPoint1,Vector2 TerminalPoint1, Vector2 InitialPoint2,Vector2 TerminalPoint2)
	{
		
	    return Utils.lineIntersectPoint(InitialPoint1.x,InitialPoint1.y,TerminalPoint1.x,TerminalPoint1.y,
	    		InitialPoint2.x,InitialPoint2.y,TerminalPoint2.x,TerminalPoint2.y
	    		);
	}


	public static  Vector2 lineIntersectPoint(Vector2 s1, Vector2 e1, Vector2 s2, Vector2 e2) {
		return  lineIntersectPoint(s1.x,s1.y,e1.x,e1.y,s2.x,s2.y,e2.x,e2.y);

	}













	public static  boolean lineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		  double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		  if (denom == 0.0) { // Lines are parallel.
		     return false;
		  }
		  double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		  double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
        return ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f;

    }



	public static boolean doLinesIntersect(Vector2 InitialPoint1,Vector2 TerminalPoint1, Vector2 InitialPoint2,Vector2 TerminalPoint2)
	{
	    return Utils.lineIntersect(InitialPoint1.x,InitialPoint1.y,TerminalPoint1.x,TerminalPoint1.y,
	    		InitialPoint2.x,InitialPoint2.y,TerminalPoint2.x,TerminalPoint2.y
	    		);
	}


	
	
	
	
	
	
	
	
	

	
	public static boolean liesOnSegment( Vector2 a, Vector2 b, Vector2  c) {

	    double dotProduct = (c.x - a.x) * (c.x - b.x) + (c.y - a.y) * (c.y - b.y);
        return dotProduct < 0;
    }
	
	
	

	
	
	
	
	
	
	

	

	



	public static int findNearestLayer(ArrayList<ArrayList<Vector2>> dividedVertices,
			Vector2 Point) {
		Vector2 result = new Vector2(1000, 1000);
		int layer = -1;
		for (int k = 0; k < dividedVertices.size(); k++) {
			if (Utils.PointInPolygon(Point, dividedVertices.get(k))) {
				layer = k;
				break;
			}
		}
	
		if (layer == -1) {
			for (int k = 0; k < dividedVertices.size(); k++) {
	
				for (int m = 0; m < dividedVertices.get(k).size(); m++) {
	
					Vector2 va = dividedVertices.get(k).get(m);
	
					if (va.dst(Point) < result.dst(Point)) {
						result = va.cpy();
						layer = k;
					}
	
				}
	
			}
		}
		return layer;
	}


	public static boolean isAligned(Vector2 point, ArrayList<Vector2> vertices) {
		int N = vertices.size();
			float minCross = Float.MAX_VALUE;
			
			for (int i = 0; i < vertices.size(); i++) {

				int ni = i==N-1 ?0:i+1;

				Vector2 v1 = vertices.get(i).cpy().sub(vertices.get(ni).cpy());
				Vector2 v2 = point.cpy().sub(vertices.get(i).cpy());
				float cross = Math.abs(v1.cross(v2));
				boolean test = v2.len() < v1.len();
				if (cross < minCross && test) {
					minCross = cross;
				}

			}

        return Math.abs(minCross) < 1f;
		
	}





	  public static  int getIndex(Vector2 pt, ArrayList<Vector2> points){

			 for(int i=0;i<points.size();i++){
				 int ni = i==points.size()-1 ?0:i+1;
					Vector2 v1 = points.get(i);
					Vector2 v2 = points.get(ni); 
					if(PointOnLineSegment(v1, v2, pt, 0.01f))return i;
		 }
			 
			
			 return -1;
	 }


	public static float getAbsoluteAngleRad(Vector2 v1, Vector2 v2) {
		return (float)Math.atan2( v1.x*v2.y - v1.y*v2.x, v1.x*v2.x + v1.y*v2.y );
	}

	public static float getAbsoluteAngle(Vector2 v1, Vector2 v2) {
		return (float) Math.toDegrees( Math.atan2( v1.x*v2.y - v1.y*v2.x, v1.x*v2.x + v1.y*v2.y ));
	}
	public static float getAngleRad(Vector2 v) {
		return  (float)Math.atan2( - v.y, v.x  );
	}


	public static void drawPath(ArrayList<Vector2> path, Color color) {
	for(int i=0;i<path.size();i++){
		int ni = i==path.size()-1 ?0:i+1;
		GameScene.plotter.drawLine2(path.get(i), path.get(ni), color, 1);
	}
		
	}
	public static boolean isInReach(Vector2 position,
			ArrayList<Vector2> path) {
		for(int i=0;i<path.size();i++){
			int ni = i==path.size()-1 ?0:i+1;
			Vector2 p1 = path.get(i);
			Vector2 p2 = path.get(ni);
			if(circleLineIntersect(p1, p2, position.x, position.y,1))return true;
		}
		return false;
	}
	public static ArrayList<ArrayList<Vector2>> split(Vector2 p1, Vector2 p2,
			ArrayList<Vector2> points) {
		ArrayList<ArrayList<Vector2>> result = new ArrayList<ArrayList<Vector2>>();
		
		
		
		
		
		int index1 = getIndex(p1,points);
		int index2 = getIndex(p2,points);

		if(index1!=-1&&index2!=-1){
		if(index1!=index2){	

			int nindex1 = index1==points.size()-1 ?0:index1+1;
			int nindex2 = index2==points.size()-1 ?0:index2+1;
		Vector2 a1 = points.get(index1);	
		Vector2 a2 = points.get(nindex2);	
			
		Vector2 b1 = points.get(nindex1);	
		Vector2 b2 = points.get(index2);

		boolean p1Ona1 = p1.dst(a1)<0.01f;

		boolean p2Ona2 = p2.dst(a2)<0.01f;

		boolean p1Onb1 = p1.dst(b1)<0.01f;
		boolean p2Onb2 = p2.dst(b2)<0.01f;
			

		ArrayList<Vector2> list = new ArrayList<Vector2>();
		ArrayList<Vector2> group1 = new ArrayList<Vector2>();
		//nindex2 to index1
		if(!p1Ona1)
		group1.add(p1.cpy());
	

		if(!p2Ona2)
		group1.add(p2.cpy());
	
		for(int i=nindex2;i<points.size();i++)list.add(points.get(i));
		list.addAll(points);
		for(int i=0;i<list.size();i++){
			group1.add(list.get(i).cpy());
			if(list.get(i)==a1)break;
		}
		list.clear();

		ArrayList<Vector2> group2 = new ArrayList<Vector2>();
		if(!p1Onb1)
		group2.add(p1.cpy());
		

		if(!p2Onb2)
		group2.add(p2.cpy());
	

		Collections.reverse(group2);
		for(int i=nindex1;i<points.size();i++)list.add(points.get(i));
		list.addAll(points);
		for(int i=0;i<list.size();i++){
			group2.add(list.get(i).cpy());
			if(list.get(i)==b2)break;
		}
	


result.add(group1);
result.add(group2);
		} 



		}


		
		
		
		
		return result;
	}

	
	public static ArrayList<ArrayList<Vector2>> splitPrime(Vector2 p1, Vector2 p2,
			ArrayList<Vector2> points,int index1, int index2) {
		ArrayList<ArrayList<Vector2>> result = new ArrayList<ArrayList<Vector2>>();
		
		
		
		
		
	

		if(index1!=-1&&index2!=-1){
		if(index1!=index2){	

			int nindex1 = index1==points.size()-1 ?0:index1+1;
			int nindex2 = index2==points.size()-1 ?0:index2+1;
		Vector2 a1 = points.get(index1);		

		Vector2 b2 = points.get(index2);


		ArrayList<Vector2> list = new ArrayList<Vector2>();
		ArrayList<Vector2> group1 = new ArrayList<Vector2>();
		//nindex2 to index1
	
		group1.add(p1.cpy());


		group1.add(p2.cpy());
	
		for(int i=nindex2;i<points.size();i++)list.add(points.get(i));
		list.addAll(points);
		for(int i=0;i<list.size();i++){
			group1.add(list.get(i).cpy());
			if(list.get(i)==a1)break;
		}
		list.clear();

		ArrayList<Vector2> group2 = new ArrayList<Vector2>();
	
		group2.add(p1.cpy());
		

		group2.add(p2.cpy());
	

		Collections.reverse(group2);
		for(int i=nindex1;i<points.size();i++)list.add(points.get(i));
		list.addAll(points);
		for(int i=0;i<list.size();i++){
			group2.add(list.get(i).cpy());
			if(list.get(i)==b2)break;
		}
	
		
result.add(group1);
result.add(group2);
		}  else {
			
			ArrayList<Vector2> group1 = new ArrayList<Vector2>();
			
			boolean reverse = p1.dst(points.get(index1)) >= p2.dst(points.get(index1));
			
			group1.add(p1.cpy());
			


					group1.add(p2.cpy());
			
			
			ArrayList<Vector2> group2 = new ArrayList<Vector2>();
			
			ArrayList<Vector2> list = new ArrayList<Vector2>();
		
		
			list.addAll(points);
			list.addAll(points);
			
			ArrayList<Vector2> outline = new ArrayList<Vector2>();
			outline.add(p1);

			outline.add(p2);
			if(!reverse)Collections.reverse(outline);
			//next of base index
			int ni = index1==points.size()-1 ?0:index1+1;
			for(int i=ni;i<list.size();i++){
				//FROM NEXTOFBASE TO BASE
				group2.add(list.get(i));
				if(list.get(i)==points.get(index1))break;
				
			}
			group2.addAll(outline);

						
				result.add(group1);
				result.add(group2);
			
		}



		}


		
		
		
		
		return result;
	}
	public static  Vector2 lineIntersectPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
		if (ua > 0.0f && ua < 1.0f && ub > 0.0f && ub < 1.0f) {
			// Get the intersection point.
			return new Vector2((float)(x1 + ua*(x2 - x1)),(float)(y1 + ua*(y2 - y1)));
		}

		return null;
	}




	public static boolean included(ArrayList<Vector2> main,
			ArrayList<Vector2> other) {
		for(Vector2 v:main){
			if(!PointInPolygon(v, other))return false;
		}
		
		return true;
	}
	public static void rotateVector2(Vector2 v, float angle) {
		float r = (float) (angle/360 * 2*Math.PI);
	
		    float rx = (float) (v.x * Math.cos(r) - v.y * Math.sin(r));
		    float ry = (float) (v.x * Math.sin(r) + v.y * Math.cos(r));
		    v.x = rx;
		    v.y = ry;
		}
		
	public static float linearInterpolate(float ex1, float ex2, float value){
		return Math.min(ex1+(ex2-ex1)*value,1f);
	}
	public static Color getFlameColor(float temperature){
		float r = temperature/6000;
		return new Color(1,0,0);
		
	}


	public static final Random RAND = new Random();
	public static ArrayList<Vector2> generateRandomSpecialConvexPolygon(int n) {
		ArrayList<Vector2> polygon;
		do {
			polygon = generateRandomConvexPolygon(n);
		} while (!testPolygon(polygon));
		return polygon;
	}
public static boolean testPolygon(ArrayList<Vector2> polygon){
		for(int i=0;i<polygon.size();i++){
			int ni = (i==polygon.size()-1)?0:i+1;
			if(polygon.get(i).dst(polygon.get(ni))<2)return false;
		}
		return true;
}

	public static ArrayList<Vector2> generateRandomConvexPolygon(int n) {
		// Generate two lists of random X and Y coordinates
		ArrayList<Float> xPool = new ArrayList<>(n);
		ArrayList<Float> yPool = new ArrayList<>(n);

		for (int i = 0; i < n; i++) {
			xPool.add(RAND.nextFloat()*100+350);
			yPool.add(RAND.nextFloat()*100+190);
		}

		// Sort them
		Collections.sort(xPool);
		Collections.sort(yPool);

		// Isolate the extreme points
		Float minX = xPool.get(0);
		Float maxX = xPool.get(n - 1);
		Float minY = yPool.get(0);
		Float maxY = yPool.get(n - 1);

		// Divide the interior points into two chains & Extract the vector components
		ArrayList<Float> xVec = new ArrayList<>(n);
		ArrayList<Float> yVec = new ArrayList<>(n);

		float lastTop = minX, lastBot = minX;

		for (int i = 1; i < n - 1; i++) {
			float x = xPool.get(i);

			if (RAND.nextBoolean()) {
				xVec.add(x - lastTop);
				lastTop = x;
			} else {
				xVec.add(lastBot - x);
				lastBot = x;
			}
		}

		xVec.add(maxX - lastTop);
		xVec.add(lastBot - maxX);

		float lastLeft = minY, lastRight = minY;

		for (int i = 1; i < n - 1; i++) {
			float y = yPool.get(i);

			if (RAND.nextBoolean()) {
				yVec.add(y - lastLeft);
				lastLeft = y;
			} else {
				yVec.add(lastRight - y);
				lastRight = y;
			}
		}

		yVec.add(maxY - lastLeft);
		yVec.add(lastRight - maxY);

		// Randomly pair up the X- and Y-components
		Collections.shuffle(yVec);

		// Combine the paired up components into vectors
		ArrayList<Vector2> vec = new ArrayList<>(n);

		for (int i = 0; i < n; i++) {
			vec.add(new Vector2(xVec.get(i), yVec.get(i)));
		}

		// Sort the vectors by angle
		Collections.sort(vec,new  VectorComparator());

		// Lay them end-to-end
		float x = 0, y = 0;
		float minPolygonX = 0;
		float minPolygonY = 0;
		ArrayList<Vector2> points = new ArrayList<>(n);

		for (int i = 0; i < n; i++) {
			points.add(new Vector2(x, y));

			x += vec.get(i).x;
			y += vec.get(i).y;

			minPolygonX = Math.min(minPolygonX, x);
			minPolygonY = Math.min(minPolygonY, y);
		}

		// Move the polygon to the original min and max coordinates
		float xShift = minX - minPolygonX;
		float yShift = minY - minPolygonY;

		for (int i = 0; i < n; i++) {
			Vector2 p = points.get(i);
			points.set(i, new Vector2(p.x + xShift, p.y + yShift));
		}

		return points;
	}

}
