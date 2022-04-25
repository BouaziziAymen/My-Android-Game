package is.kul.learningandengine.basics;

public class PressureInterval {

    PressureInterval(float n1, float n2){
        if(n1<n2) {
            a1 = n1;
            a2 = n2;
        } else
        {
            a1 = n2;
            a2 = n1;
        }
    }
    float a1 , a2;//A1<A2
public void merge(PressureInterval other){
    boolean otherIn1 = this.inInterval(other.a1);
    boolean otherIn2 = this.inInterval(other.a2);
//FIRST CASE BOTH OTHER POINTS INSIDE
    if(otherIn1&&otherIn2);
//SECOND CASE ONE INSIDE OTHER OUTSIDE
    if(!otherIn1&&otherIn2);
    if(otherIn1&&!otherIn2);
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION
    else {
       if(other.inInterval(this.a1));
        else ;
   }
}
boolean inInterval(float x){
    return x >= this.a1 && x <= this.a2;
}

}
