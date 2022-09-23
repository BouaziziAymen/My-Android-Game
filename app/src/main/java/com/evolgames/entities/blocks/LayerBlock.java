package com.evolgames.entities.blocks;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.grid.BlockGrid;
import com.evolgames.entities.joint.JointKey;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.physics.PhysicsConstants;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import is.kul.learningandengine.helpers.Utils;

public class LayerBlock extends Block<LayerBlock, LayerProperties> implements Comparable<LayerBlock> {
    private ArrayList<Vector2> bodyVertices;
    private ArrayList<Block<?, ?>> associatedBlocks;
    private Polarity polarity = Polarity.NEUTRAL;
    private HashSet<JointKey> keys;
    private BlockGrid blockGrid;
    private HashSet<Fixture> fixtures;
    private boolean Dead;
    private float Area;
    private Body body;
    private final ArrayList<FreshCut> freshCuts = new ArrayList<>();
    private GameEntity gameEntity;


    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity entity) {
        this.gameEntity = entity;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public ArrayList<Vector2> getBodyVertices() {
        if (bodyVertices == null) BlockUtils.setBodyVertices(this);
        return bodyVertices;
    }

    public void setBodyVertices(ArrayList<Vector2> vertices) {
        this.bodyVertices = vertices;
    }

    public int getOrder() {
        return getProperties().getOrder();
    }

    public void setOrder(int order) {
        getProperties().setOrder(order);
    }

    public boolean isDead() {
        return Dead;
    }

    public void setDead(boolean dead) {
        Dead = dead;
    }

    public BlockGrid getBlockGrid() {
        return blockGrid;
    }

    @Override
    protected boolean arrange() {
        return true;
    }

    @Override
    protected boolean check() {
        return true;
    }


    private void createKeys() {
        if (keys == null)
            keys = new HashSet<>();

    }

    private void createFixtureSet() {
        if (fixtures == null)
            fixtures = new HashSet<>();
    }

    private void createAssociated() {
        if (associatedBlocks == null)
            associatedBlocks = new ArrayList<>();
    }

    private void createGrid() {
        blockGrid = new BlockGrid();
    }

    private void fillGrid() {
        BlockUtils.computeCoatingBlocks(this);

        for (CoatingBlock coatingBlock : blockGrid.getCoatingBlocks()) {
            HashSet<CoatingBlock> neighbors = blockGrid.findNeighbors(coatingBlock);
            coatingBlock.setNeighbors(neighbors);
            if (neighbors.size() < 8) {
                coatingBlock.setBorder(true);
                getBlockGrid().addBorderGrain(coatingBlock);
            }
        }
    }

    @Override
    public void performCut(Cut cut) {
        super.performCut(cut);
        Pair<LayerBlock, LayerBlock> result = BlockUtils.cutBlockA(this, cut);
        addBlock(result.first);
        if (result.first.getArea() < PhysicsConstants.MINIMUM_SPLINTER_AREA){
            result.first.setAborted(true);
        }
        addBlock(result.second);
        if (result.second.getArea() < PhysicsConstants.MINIMUM_SPLINTER_AREA) {
            result.second.setAborted(true);
        }
    }

    @Override
    protected void calculateArea() {
        Area = GeometryUtils.getArea(getVertices());
    }

    @Override
    protected boolean calcArea() {
        return true;
    }

    @Override
    protected void particularInitialization(boolean firstTime) {
        createGrid();
        createKeys();
        createFixtureSet();
        createAssociated();

        if (firstTime) {
            fillGrid();
        }
    }

    public void refillGrid() {
        for (Block<?, ?> decorationBlock : associatedBlocks) {
            if (decorationBlock instanceof CoatingBlock) {
                CoatingBlock coatingBlock = (CoatingBlock) decorationBlock;
                getBlockGrid().addCoatingBlock(coatingBlock);
            }
        }

        for (CoatingBlock coatingBlock : getBlockGrid().getCoatingBlocks()) {
            coatingBlock.setNeighbors(blockGrid.findNeighbors(coatingBlock));
        }


    }

    public void addFreshCut(FreshCut freshCut) {
        freshCuts.add(freshCut);
    }

    public Polarity getPolarity() {
        return this.polarity;
    }

    public void setPolarity(Polarity polarity) {
        this.polarity = polarity;
    }

    public void addKey(JointKey key) {
        keys.add(key);
    }

    public float getArea() {
        return Area;
    }

    @Override
    protected LayerBlock createChildBlock() {
        return new LayerBlock();
    }

    @Override
    public void checkShape() {

        if (getVertices().size() < 3) {
            setAborted(true);
            setDead(true);
        } else {
            setAborted(false);
        }
    }

    @Override
    protected boolean rectify() {
        return true;
    }

    @Override
    protected LayerBlock getThis() {
        return this;
    }

    public List<LayerBlock> getChildren() {
        return children;
    }

    public HashSet<JointKey> getKeys() {
        return keys;
    }

    @SuppressWarnings("unused")
    public void addFixture(Fixture fixture) {
        fixtures.add(fixture);
    }

    public boolean testPoint(Body body, float x, float y) {
        Vector2 p = Vector2Pool.obtain(x, y);
        p = Vector2Pool.obtain(body.getLocalPoint(p)).mul(32);
        boolean result = Utils.PointInPolygon(p, getVertices());
        Vector2Pool.recycle(p);
        return result;
    }

    public HashSet<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(HashSet<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    @Override
    public int compareTo(LayerBlock o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

    public <T extends Block<?, ?>> void addAssociatedBlock(T block) {
        if (associatedBlocks != null) {
            associatedBlocks.add(block);
            block.setParent(this);
        }
    }

    public ArrayList<? extends Block<?, ?>> getAssociatedBlocks() {
        return associatedBlocks;
    }

    public ArrayList<FreshCut> getFreshCuts() {
        return freshCuts;
    }


    public void decrementLiquidQuantity() {
        liquidQuantity--;
    }

    private int liquidQuantity;

    public int getLiquidQuantity() {
        return liquidQuantity;
    }

    public void setLiquidQuantity(int liquidQuantity) {
        this.liquidQuantity = liquidQuantity;
    }


}