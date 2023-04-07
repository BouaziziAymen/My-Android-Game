package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.Flag;
import com.evolgames.physics.FlagType;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;

public class CutRayCastCallback implements RayCastCallback {
    boolean direction = true;
    private final ArrayList<ArrayList<Flag>> flags;
    private final ArrayList<LayerBlock> coveredBlocks;
    private final ArrayList<Fixture> coveredFixtures;


    public CutRayCastCallback() {
        flags = new ArrayList<>();
        coveredBlocks = new ArrayList<>();
        coveredFixtures = new ArrayList<>();
    }



    public ArrayList<ArrayList<Flag>> getFlags() {
        return flags;
    }

    public ArrayList<LayerBlock> getCoveredBlocks() {
        return coveredBlocks;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        LayerBlock block = (LayerBlock) fixture.getUserData();
        GameEntity gameEntity = (GameEntity) fixture.getBody().getUserData();
        if(!gameEntity.isAlive()){
            return 1;
        }
        Vector2 Point = Vector2Pool.obtain(point);
        FlagType type = (direction) ? FlagType.Entering : FlagType.Leaving;
        if (coveredBlocks.contains(block)) {
            int index = coveredBlocks.indexOf(block);
            flags.get(index).add(new Flag(block, Point, fraction, type));

        } else {
            ArrayList<Flag> list = new ArrayList<>();
            list.add(new Flag(block, Point, fraction, type));
            flags.add(list);
            coveredBlocks.add(block);
            coveredFixtures.add(fixture);
        }


        return 1;
    }

    public void reset() {
        coveredFixtures.clear();
        coveredBlocks.clear();
        flags.clear();
    }

    public ArrayList<Fixture> getCoveredFixtures() {
        return coveredFixtures;
    }
}
