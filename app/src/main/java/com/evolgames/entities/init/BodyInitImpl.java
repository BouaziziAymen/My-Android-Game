package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.GameEntity;

public class BodyInitImpl implements BodyInit{
    private final Filter filter;

    public BodyInitImpl(short category, short mask){
        this.filter  = new Filter();
        this.filter.categoryBits = category;
        this.filter.maskBits = mask;
    }

    public BodyInitImpl(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void initialize(Body body) {
        for (Fixture f : body.getFixtureList()) {
            f.setFilterData(filter);
        }
    }
}
