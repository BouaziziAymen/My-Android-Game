package com.evolgames.entities.cut;

public abstract class FreshCut {
    private final float length;
    private int limit;

    public FreshCut(float length, int limit) {
        this.length = length;
        this.limit = limit;
    }

    public float getLength() {
        return length;
    }

    public void decrementLimit(){
        limit--;
    }

    public int getLimit() {
        return limit;
    }

}
