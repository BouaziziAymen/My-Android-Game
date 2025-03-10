package com.evolgames.dollmutilationgame.entities.blocks;

import android.util.Pair;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.cut.Cut;
import com.evolgames.dollmutilationgame.entities.cut.FreshCut;
import com.evolgames.dollmutilationgame.entities.grid.BlockGrid;
import com.evolgames.dollmutilationgame.utilities.BlockUtils;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.utilities.Utils;
import com.evolgames.dollmutilationgame.entities.factories.MaterialFactory;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;
import com.evolgames.dollmutilationgame.entities.properties.Properties;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LayerBlock extends Block<LayerBlock, LayerProperties>
        implements Comparable<LayerBlock> {

    private final ArrayList<FreshCut> freshCuts = new ArrayList<>();
    private ArrayList<AssociatedBlock<?, ?>> associatedBlocks;
    private Polarity polarity = Polarity.NEUTRAL;
    private transient BlockGrid blockGrid;
    private boolean dead;
    private float blockArea;
    private boolean fillGrid;
    private float liquidQuantity;

    public void initialization(
            List<Vector2> vertices, Properties properties, int id, boolean fillGrid) {
        this.fillGrid = fillGrid;
        super.initialization(vertices, properties, id);
    }

    public int getOrder() {
        return getProperties().getOrder();
    }

    public void setOrder(int order) {
        getProperties().setOrder(order);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public BlockGrid getBlockGrid() {
        return blockGrid;
    }

    @Override
    protected boolean shouldArrangeVertices() {
        return true;
    }

    @Override
    protected boolean shouldCheckShape() {
        return true;
    }

    private void createAssociated() {
        if (associatedBlocks == null) associatedBlocks = new ArrayList<>();
    }

    private void createGrid() {
        blockGrid = new BlockGrid();
    }

    private void fillGrid() {
        BlockUtils.computeCoatingBlocks(this);
        for (CoatingBlock coatingBlock : blockGrid.getCoatingBlocks()) {
            HashSet<CoatingBlock> neighbors = blockGrid.findNeighbors(coatingBlock);
            coatingBlock.setNeighbors(neighbors);
        }
    }

    @Override
    public void performCut(Cut cut) {
        super.performCut(cut);
        Pair<LayerBlock, LayerBlock> result = BlockUtils.cutLayerBlock(this, cut);
        addBlock(result.first);
        addBlock(result.second);
    }

    @Override
    protected void calculateArea() {
        blockArea = GeometryUtils.getArea(getVertices());
    }

    @Override
    protected boolean shouldCalculateArea() {
        return true;
    }

    @Override
    protected void specificInitialization() {
        createGrid();
        createAssociated();
        if (this.fillGrid) {
            fillGrid();
        }
    }

    public void refillGrid() {
        if (this.blockGrid == null) {
            createGrid();
        }
        for (Block<?, ?> decorationBlock : associatedBlocks) {
            if (decorationBlock instanceof CoatingBlock) {
                CoatingBlock coatingBlock = (CoatingBlock) decorationBlock;
                coatingBlock.setParent(this);
                getBlockGrid().addCoatingBlock(coatingBlock);
            }
        }
        if (this.getBlockGrid().getCoatingBlocks().isEmpty()) {
            setAborted(true);
            return;
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

    public float getBlockArea() {
        return blockArea;
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
    protected boolean shouldRectify() {
        return true;
    }

    @Override
    protected LayerBlock getThis() {
        return this;
    }

    public List<LayerBlock> getChildren() {
        return children;
    }

    public boolean testPoint(Body body, float x, float y) {
        Vector2 p = Vector2Pool.obtain(x, y);
        p = Vector2Pool.obtain(body.getLocalPoint(p)).mul(32);
        boolean result = Utils.PointInPolygon(p, getVertices());
        Vector2Pool.recycle(p);
        return result;
    }

    @Override
    public int compareTo(LayerBlock o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

    public <T extends AssociatedBlock<?, ?>> void addAssociatedBlock(T block) {
        if (associatedBlocks != null) {
            associatedBlocks.add(block);
            if (block instanceof CoatingBlock) {
                CoatingBlock coatingBlock = (CoatingBlock) block;
                coatingBlock.setParent(this);
            }
        }
    }

    public ArrayList<? extends AssociatedBlock<?, ?>> getAssociatedBlocks() {
        return associatedBlocks;
    }

    public ArrayList<FreshCut> getFreshCuts() {
        return freshCuts;
    }

    @Override
    public void mirror() {
        super.mirror();
        for (AssociatedBlock<?, ?> associatedBlock : associatedBlocks) {
            associatedBlock.mirror();
        }
    }

    public boolean isFrozen() {
        if(!getProperties().isJuicy()){
            return false;
        }
        if(this.blockGrid==null){
            return false;
        }
        int n = 0;
        for(CoatingBlock coatingBlock:blockGrid.getCoatingBlocks()){
            if(coatingBlock.getTemperature()<=-30){
                n++;
            }
        }
        return n/(float)blockGrid.getCoatingBlocks().size()>0.5f;
    }
    public float getTenacity(){
        if(isFrozen()){
            return MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.CLAY).getTenacity();
        }
        return getProperties().getTenacity();
    }

    public void setLiquidQuantity(float liquidQuantity) {
        this.liquidQuantity = liquidQuantity;
    }

    public float getLiquidQuantity() {
        return liquidQuantity;
    }

    public MaterialFactory.MaterialAcousticType getMaterialAcousticType() {
        return MaterialFactory.getInstance().getMaterialAcousticType(getProperties().getMaterialNumber());
    }
}
