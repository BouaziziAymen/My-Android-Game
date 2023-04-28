package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.GameEntity;

public class BodyInitImpl implements BodyInit{
    private final Filter filter;
    private static short groupIndex = 0;
    public BodyInitImpl(){
        this.filter = new Filter();
        this.filter.groupIndex = (short) ~(++groupIndex - 1);
    }
    public BodyInitImpl(Filter filter){
        this.filter = filter;

    }
    @Override
    public void initialize(Body body) {
        if(filter!=null) {
            GameEntity gameEntity = (GameEntity) body.getUserData();
            gameEntity.setInitialFilter(filter);
            for (Fixture f : body.getFixtureList()) {
                f.setFilterData(filter);
            }
        }
    }
}
