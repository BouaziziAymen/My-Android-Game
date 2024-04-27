package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;
import com.evolgames.dollmutilationgame.physics.CollisionUtils;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

public class BodyInitImpl implements BodyInit {

    private Filter filter;

    @SuppressWarnings("unused")
    public BodyInitImpl() {
    }

    public BodyInitImpl(short category) {
        this.filter = new Filter();
        this.filter.categoryBits = category;
        this.filter.maskBits = category;
        this.filter.groupIndex = CollisionUtils.groupIndex();
    }

    public BodyInitImpl(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void initialize(Body body) {
        GameEntity entity = (GameEntity) body.getUserData();
        entity.setGroupIndex(this.filter.groupIndex);
        for (Fixture f : body.getFixtureList()) {
            f.setFilterData(this.filter);
        }
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setFilter(filter);
        return initInfo;
    }
}
