package com.evolgames.helpers;


public class ElementCouple<T> {
    T block1,block2;
    public ElementCouple(T b1, T b2){
        this.block1 = b1;
        this.block2 = b2;
    }
    public T getElement1() {
        return block1;
    }
    public T getElement2(){
        return block2;
    }

}
