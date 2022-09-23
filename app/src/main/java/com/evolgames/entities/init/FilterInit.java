package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class FilterInit extends BodyInitDecorator {
    final Filter filter;

    public FilterInit(BodyInit bodyInit, Filter filter) {
        super(bodyInit);
        this.filter = filter;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if(filter!=null) {
            for (Fixture f : body.getFixtureList()) {
                f.setFilterData(filter);
            }
        }
    }
}
