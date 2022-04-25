package is.kul.learningandengine.graphicelements;

import android.util.Log;

import java.util.Arrays;

public class CollisionOption {
    public short getCategory() {
        return category;
    }

    public short getMask() {
        return mask;
    }

    public short getOrder() {
        return order;
    }

    public static enum DefaultCategories{
        DEFAULT
    }
    short category = 0x0001;
    short mask = 0b111111111111111;
    short order = 0;

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
        Log.e("MASK", "mask:"+mask);
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
