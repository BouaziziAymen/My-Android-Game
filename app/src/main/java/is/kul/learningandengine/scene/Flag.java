package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;

import is.kul.learningandengine.helpers.Utils;

/**
 * Created by ayem on 24/01/2018.
 */

public class Flag implements Comparable<Flag> {

    float intersectionFraction;
    boolean EorS;
    Vector2 point;

    public Flag(Vector2 point, float fraction, boolean es){

        intersectionFraction = es ?fraction:1-fraction;
        EorS = es;
        this.point = point;
    }


    @Override
    public int compareTo(Flag another) {
        if(Utils.vectorEquivalence(point,another.point)){
            if(EorS)return 1;else return -1;
        } else {

            if(intersectionFraction <another.intersectionFraction)return -1;else return 1;

        }
    }
}
