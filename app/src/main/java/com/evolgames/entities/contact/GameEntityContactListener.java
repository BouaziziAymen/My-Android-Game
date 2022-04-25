package com.evolgames.entities.contact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameEntityContactListener implements ContactListener {
    private ContactObserver observer;

    public GameEntityContactListener(ContactObserver observer) {
        this.observer = observer;
    }

    @Override
    public void beginContact(Contact contact) {
        observer.processImpactBeginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        observer.processImpactEndContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        observer.processImpactBeforeSolve(contact);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

        observer.processImpactAfterSolve(contact, impulse);
    }


}
