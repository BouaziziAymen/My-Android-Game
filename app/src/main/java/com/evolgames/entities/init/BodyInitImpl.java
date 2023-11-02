package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.EntityWithBody;
import com.evolgames.entities.GameEntity;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyInitImpl implements BodyInit{
    private final Filter filter;
    private static short groupNumber;

    public BodyInitImpl(short category){
        this.filter  = new Filter();
        this.filter.categoryBits = category;
        this.filter.maskBits = category;
        this.filter.groupIndex = (short) -++groupNumber;
    }

    public BodyInitImpl(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void initialize(Body body) {
        EntityWithBody entity = (EntityWithBody) body.getUserData();
        entity.setGroupIndex(this.filter.groupIndex);
        for (Fixture f : body.getFixtureList()) {
            f.setFilterData(this.filter);
        }
    }
}
