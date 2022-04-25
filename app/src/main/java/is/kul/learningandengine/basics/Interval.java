package is.kul.learningandengine.basics;


import android.util.Log;

import java.util.HashSet;

import is.kul.learningandengine.helpers.Inter;

public class Interval {
    public float start;
    public float end;
    float hardness;
    float energy;
    public float getValue(){
        return Math.abs(end-start);
    }
DCGameEntity entity;
    void setEntity(DCGameEntity e){
      this.entity = e;
    }
    DCGameEntity getEntity(){
        return entity;
    }
    float getEnergy(){
        float result= this.energy;
        energy = 0;
        for(Interval v: this.children)result+=v.getEnergy();

        return result;
    }
    public Interval set(float s, float e, float hardness){

        if(s<e) {
            this.start = s;
            this.end = e;
        } else
        {
            this.start = e;
            this.end = s;
        }
        this.children = new  HashSet<Interval>();
        this.hardness = hardness;
        return this;
    }
    public void recycle(){

        for(Interval i:this.children)i.recycle();
        this.start = 0;
        this.end = 0;
        this.hardness = 0;
        children.clear();
        IntervalPool.recycle(this);
    }



    HashSet<Interval> children;
    public Interval(){
        children = new  HashSet<Interval>();
    }
      public Interval(float s, float e, float hardness) {
          this.hardness = hardness;
          if(s<e) {
              start = s;
              end = e;
          } else
          {
              start = e;
              end = s;
          }
          this.children = new  HashSet<Interval>();
      }
   public String toString(){
      return (getEntity()==null)?"0":"1";
    }




    public HashSet<Interval> merge (Interval other) {
        HashSet<Interval> result = new HashSet<Interval>();
        boolean otherIn1 = this.inInterval(other.start);
        boolean otherIn2 = this.inInterval(other.end);
        if(other.start==other.end)return result;
//FIRST CASE BOTH OTHER POINTS INSIDE
        if (otherIn1 && otherIn2) {
            Interval i1,i2,i3;
            result.add(i1=IntervalPool.obtain(other.start, other.end,Math.max(hardness,other.hardness)));
            result.add(i2=IntervalPool.obtain(other.end, this.end, this.hardness));
            result.add(i3=IntervalPool.obtain(this.start, other.start, this.hardness));


        }
//SECOND CASE ONE INSIDE OTHER OUTSIDE
        else if (!otherIn1 && otherIn2) {
            Interval i1,i2,i3;
            result.add(i1=IntervalPool.obtain(this.start, other.end,Math.max(hardness,other.hardness)));
            result.add(i2=IntervalPool.obtain(other.start, this.start,other.hardness));
            result.add(i3=IntervalPool.obtain(other.end, this.end, this.hardness));

        }
        else if (otherIn1 && !otherIn2) {
            Interval i1,i2,i3;
            result.add(i1=IntervalPool.obtain(other.start, this.end,Math.max(hardness,other.hardness)));
            result.add(i2=IntervalPool.obtain(this.end, other.end,other.hardness));
            result.add(i3=IntervalPool.obtain(this.start, other.start, this.hardness));
        }
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION
        else {

            boolean thisIn1 = other.inInterval(this.start);

            if (thisIn1) {
                Interval i1,i2,i3;
                result.add(i1=IntervalPool.obtain(this.start, this.end,Math.max(hardness,other.hardness)));
                result.add(i2=IntervalPool.obtain(other.start, this.start,other.hardness));
                result.add(i3=IntervalPool.obtain(this.end, other.end,other.hardness));
            } else {
                Interval i1,i2;
                result.add(i1=IntervalPool.obtain(this.start, this.end, this.hardness));
                result.add(i2=IntervalPool.obtain(other.start, other.end,other.hardness));


            }

        }
        return result;
    }





    public Interval intersection (Interval other) {
        HashSet<Interval> result = new HashSet<Interval>();
        boolean otherIn1 = this.inInterval(other.start);
        boolean otherIn2 = this.inInterval(other.end);
//FIRST CASE BOTH OTHER POINTS INSIDE
        if (otherIn1 && otherIn2) {
           return IntervalPool.obtain(other.start, other.end,Math.max(hardness,other.hardness));


        }
//SECOND CASE ONE INSIDE OTHER OUTSIDE
        else if (!otherIn1 && otherIn2) {
            return IntervalPool.obtain(this.start, other.end,Math.max(hardness,other.hardness));

        }
        else if (otherIn1 && !otherIn2) {
            return IntervalPool.obtain(other.start, this.end,Math.max(hardness,other.hardness));


        }
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION
        else {

            boolean thisIn1 = other.inInterval(this.start);

            if (thisIn1) {
                return IntervalPool.obtain(this.start, this.end,Math.max(hardness,other.hardness));

            } else {
                return IntervalPool.obtain();
            }

        }

    }





    public boolean intersect (Interval other) {
//FIRST CASE BOTH OTHER POINTS INSIDE
        boolean otherIn1 = this.inInterval(other.start);
        if (otherIn1)return true;


        boolean otherIn2 = this.inInterval(other.end);
        if(otherIn2)return true;
//SECOND
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION


            boolean thisIn1 = other.inInterval(this.start);

            if (thisIn1) {
                return true;

            } else {
                return false;
            }



    }



    public boolean isIncludedIn(Interval other){
        return other.inInterval(start) && other.inInterval(end);
    }
public HashSet<Interval> getChildren(){
    HashSet<Interval> result = new HashSet<Interval>();
    if(this.children.size()==0)result.add(this);
   else for(Interval v: this.children)result.addAll(v.getChildren());
    return result;
}

public void applyCut(Interval other){
    if(this.children.size()>0)
        for(Interval i: this.children)i.applyCut(other);
    else {
        if(other.start==other.end)return;
        boolean otherIn1 = this.inInterval(other.start);
        boolean otherIn2 = this.inInterval(other.end);
//FIRST CASE BOTH OTHER POINTS INSIDE
        if (otherIn1 && otherIn2) {
float d = (other.end-other.start);
            this.energy = d*d* this.calculateFactor(this.hardness, other.hardness);

            this.children.add(IntervalPool.obtain(other.end, this.end, this.hardness));
            this.children.add(IntervalPool.obtain(this.start, other.start, this.hardness));

        }
//SECOND CASE ONE INSIDE OTHER OUTSIDE
        else if (!otherIn1 && otherIn2) {
float d = (other.end-other.start);
            this.energy = d*d * this.calculateFactor(this.hardness, other.hardness);

            this.children.add(IntervalPool.obtain(other.end, this.end, this.hardness));
        } else if (otherIn1 && !otherIn2) {
float d =this.end - other.start;
            this.energy = d*d* this.calculateFactor(this.hardness, other.hardness);

            this.children.add(IntervalPool.obtain(this.start, other.start, this.hardness));

        }
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION
        else {

            boolean thisIn1 = other.inInterval(this.start);

            if (thisIn1) {
float d = (this.end - this.start);
                this.energy = d*d* this.calculateFactor(this.hardness, other.hardness);

                this.children.add(IntervalPool.obtain());

            } else {

            }

        }
    }

}


    public float intersectionValue (Interval other){
        boolean otherIn1 = this.inInterval(other.start);
        boolean otherIn2 = this.inInterval(other.end);
//FIRST CASE BOTH OTHER POINTS INSIDE
        if(otherIn1&&otherIn2)return other.end-other.start;
//SECOND CASE ONE INSIDE OTHER OUTSIDE
        if(!otherIn1&&otherIn2)return other.end - this.start;
        if(otherIn1&&!otherIn2)return this.end -other.start;
//THIRD CASE BOTH OUTSIDE 1 OUTSIDE NO INTERSECTION 2 BOTH OUTSIDE WITH INTERSECTION
        else {

            boolean thisIn1 = other.inInterval(this.start);

            if(thisIn1)return this.end - this.start;

            else return 0;
        }
    }






    boolean inInterval(float x){
        return x >= this.start && x <= this.end;
    }
float calculateFactor(float h1, float h2){

    if(Math.abs(h1-h2)<0.1f){


        return Float.MAX_VALUE;}
    return 100000f/Math.abs(h1-h2);
}
//public String toString(){
 // return ""+ this.hardness;
//}

}