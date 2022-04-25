package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ayem on 24/01/2018.
 */


public class ExtremityRaycastCallback implements RayCastCallback {
ArrayList<Flag> flags;
    boolean direction;
    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point,
                                  Vector2 normal, float fraction) {


Flag flag = new Flag(point,fraction, this.direction);
        this.flags.add(flag);
        return 1;
    }
    public Vector2 determineExtremity(Vector2 p0, Vector2 dir, PhysicsWorld world){
        this.flags = new ArrayList<Flag>();

        Vector2 pe = p0.cpy().add(dir.cpy().mul(10));
        this.direction = true;
        world.rayCast(this,p0,pe);
        this.direction = false;
        world.rayCast(this,pe,p0);
        Collections.sort(this.flags);
        int i = 0;
        for(; i< this.flags.size(); i++){
            if(this.flags.get(i).EorS)break;
        }
       if(i==0)return p0;
        else return this.flags.get(0).point;
    }


}
