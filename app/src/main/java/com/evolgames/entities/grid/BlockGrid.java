package com.evolgames.entities.grid;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.CoatingBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BlockGrid {

    private final List<CoatingBlock> coatingBlocks;

    public BlockGrid(){
        coatingBlocks = new ArrayList<>();
    }
   
    public CoatingBlock getCoatingBlock(int nx, int ny) {
        for (CoatingBlock g : coatingBlocks)
                if (g.getNx() == nx && g.getNy() == ny) return g;
        return null;
    }

    public void addCoatingBlock(CoatingBlock g) {
        coatingBlocks.add(g);
    }

    public Iterator<CoatingBlock> getIterator(){
        return coatingBlocks.iterator();
    }

    public List<CoatingBlock> getCoatingBlocks() {
        return coatingBlocks;
    }

    public HashSet<CoatingBlock> findNeighbors(CoatingBlock grain) {
        HashSet<CoatingBlock> neighbors = new HashSet<>();
        CoatingBlock temp;
        if((temp = getCoatingBlock(grain.getNx()-1,grain.getNy()-1))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx()-1,grain.getNy()))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx()-1,grain.getNy()+1))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx(),grain.getNy()+1))!=null)neighbors.add(temp);

        if((temp = getCoatingBlock(grain.getNx()+1,grain.getNy()+1))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx()+1,grain.getNy()))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx()+1,grain.getNy()-1))!=null)neighbors.add(temp);
        if((temp = getCoatingBlock(grain.getNx(),grain.getNy()-1))!=null)neighbors.add(temp);

    return neighbors;
    }



//TODO
    public CoatingBlock getNearestCoatingBlockSimple(Vector2 center) {
        if(this.getCoatingBlocks().size()==0||Float.isInfinite(center.x)||Float.isInfinite(center.y)){
            return null;
        }
        float distance = Float.POSITIVE_INFINITY;
        CoatingBlock result = null;
            for (CoatingBlock g : this.getCoatingBlocks()) {
                float d = g.distance(center);
                if (d < distance) {
                    distance = d;
                    result = g;
                }
            }
        return result;
    }
}
