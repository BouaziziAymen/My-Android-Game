package com.evolgames.entities.contact;

import android.util.Pair;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.evolgames.entities.GameEntity;

import java.util.HashSet;

public class GameEntityContactListener implements ContactListener {
    private final ContactObserver observer;
    private HashSet<Pair<GameEntity,GameEntity>> nonCollidingEntities;
    public void addNonCollidingPair(GameEntity entity1, GameEntity entity2){
        nonCollidingEntities.add(new Pair<>(entity1,entity2));
    }

    public GameEntityContactListener(ContactObserver observer) {
        this.observer = observer;
        this.nonCollidingEntities = new HashSet<>();
    }

    @Override
    public void beginContact(Contact contact) {
        if(shouldNotCollide(contact.getFixtureA(),contact.getFixtureB())){
            contact.setEnabled(false);
            return;
        }
        observer.processImpactBeginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        observer.processImpactEndContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if(shouldNotCollide(contact.getFixtureA(),contact.getFixtureB())){
            contact.setEnabled(false);
            return;
        }
        observer.processImpactBeforeSolve(contact);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

        observer.processImpactAfterSolve(contact, impulse);
    }

    private boolean shouldNotCollide(Fixture fixture1, Fixture fixture2){
        Body body1 = fixture1.getBody();
        Body body2 = fixture2.getBody();
        GameEntity entity1 = (GameEntity) body1.getUserData();
        GameEntity entity2 = (GameEntity) body2.getUserData();
        for(Pair<GameEntity,GameEntity> pair:nonCollidingEntities){
            if((pair.first==entity1&&pair.second==entity2)||(pair.second==entity1&&pair.first==entity2))return true;
        }
        return false;
    }
}
