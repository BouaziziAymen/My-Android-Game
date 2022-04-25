package com.evolgames.entities.grid;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.Block;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.helpers.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class BlockGrid {
    private ArrayList<CoatingBlock> coatingBlocks;
    private HashSet<CoatingBlock> borderGrains;


    public BlockGrid(){
        coatingBlocks = new ArrayList<>();
        borderGrains = new HashSet<>();
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

    public ArrayList<CoatingBlock> getCoatingBlocks() {
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




    public CoatingBlock getNearestCoatingBlockSimple(Vector2 center) {
        float distance = Float.MAX_VALUE;
        CoatingBlock result = null;
        while(result==null) {
            for (CoatingBlock g : this.getCoatingBlocks()) {
                float d = g.distance(center);
                if (d < distance) {
                    distance = d;
                    result = g;
                }
            }
        }

        return result;
    }










    public HashSet<CoatingBlock> getBorderGrains() {
        return borderGrains;
    }

    public void addBorderGrain(CoatingBlock coatingBlock) {
        borderGrains.add(coatingBlock);
    }
}
