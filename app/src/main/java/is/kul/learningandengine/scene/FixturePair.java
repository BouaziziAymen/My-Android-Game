package is.kul.learningandengine.scene;


import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

public class FixturePair {
    Fixture fixtureA, fixtureB;
    Contact pendingContact;
    public FixturePair(Fixture f1, Fixture f2){
        fixtureA = f1;
        fixtureB = f2;


    }
    public void setPendingContact(Contact pending){
        this.pendingContact = pending;
    }

    public boolean equivalent(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA==this.fixtureA&&fixtureB==this.fixtureB)return true;
        if(fixtureA==this.fixtureB&&fixtureB==this.fixtureA)return true;
        return false;
    }
}
