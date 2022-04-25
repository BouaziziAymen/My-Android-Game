package com.evolgames.entities.properties;

import java.util.Arrays;

public class CollisionOption {
    private short category = 0x0001;
    private short mask = 0b111111111111111;
    private short order = 0;
    public static enum DefaultCategories{
        DEFAULT
    }

    public void setMask(short mask) {
        this.mask = mask;
    }

    public short getMask() {
        return mask;
    }
    public void setOrder(short order) {
        this.order = order;
    }

    public short getOrder() {
        return order;
    }

    public short getCategory() {
        return category;
    }

    public void setCategory(short order){

        this.order = order;
        this.category = pow2(order);
    }
    short pow2(short n){

        if(n==0)return 1;
        if(n>1) return (short)(2*pow2((short)(n-1)));
        else return 2;
    }
    public void collidesWith(short[] orders){
        mask = 0;
        for(int i=0;i<orders.length;i++)if(orders[i]!=15)mask+=pow2(orders[i]);
        else mask*=-1;
    }
    Power2 sup(short t){
        Power2 r = new Power2();
        for(short i=0;i<=14;i++){
            short pow = pow2(i);
            if(pow<=t){r.value = pow;r.power = i;}else break;
        }
        return r;
    }
    class Power2{
        short value;
        short power;

    }
    public short[] getCollisionList(){

        short n = (short)Math.abs(mask);
        short[] result = new short[16];
        short k = 0;
        while (n>0){

            Power2 m=sup(n);

            result[k] = m.power;
            n-=m.value;
            k++;

        };
        if(mask<0){result[k]=15; k++;}
        return Arrays.copyOfRange(result,0,k);
    }
}
