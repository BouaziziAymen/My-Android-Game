package is.kul.learningandengine.polygonarithmetic;

public class Position {
int NORTH;//POSITIVE YANG
int SOUTH;
int EAST;//NEGATIVE YIN
int WEST;
Position(int n, int s, int e, int w){
	NORTH = n;
	SOUTH = s;
    EAST = e;
	WEST = w;
}
public String toString(){
	return ("n"+NORTH+"s"+SOUTH+"e"+EAST+"w"+WEST);
}

}
