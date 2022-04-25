package is.kul.learningandengine.basics;

/**
 * Created by ayem on 19/01/2018.
 */

public class DCGameEntityPair {
    DCGameEntity first;
    DCGameEntity second;
    DCGameEntityPair(DCGameEntity f, DCGameEntity s){
        this.first = f;
        this.second = s;
    }

    public boolean equal(DCGameEntity f, DCGameEntity s){
        if(this.first ==f&& this.second ==s)return true;
        return this.first == s && this.second == f;

    }

    public DCGameEntity contains(DCGameEntity e){
        if(this.first ==e)return second;
        if(this.second ==e)return first;
        return null;

    }
}
