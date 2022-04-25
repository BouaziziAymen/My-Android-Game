package is.kul.learningandengine.helpers;

public class Inter implements Comparable<Inter>{
public boolean intersection;
public float distance;
public int[] order;
Inter(boolean intersection, float distance, int[] is){
	this.intersection = intersection;
	this.distance = distance;
    order =is;
}
@Override
public int compareTo(Inter inter2) {

	if(this.intersection &&!inter2.intersection) return -1;
	if(!this.intersection &&inter2.intersection) return 1;
	if(!this.intersection &&!inter2.intersection) return 0;
	if(inter2.distance> distance)return -1; else return 1;
}
}
