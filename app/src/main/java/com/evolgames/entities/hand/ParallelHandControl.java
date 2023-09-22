package com.evolgames.entities.hand;

public class ParallelHandControl extends HandControl{
    private final HandControl[] controls;
    public ParallelHandControl(HandAction handAction,HandControl ... list) {
        super(handAction);
       this.controls = list;
    }

    public HandControl getControl(int index){
        return controls[index];
    }
    @Override
    public void run() {
        super.run();
        if(!isDead()) {
            for (HandControl handControl : controls) {
                handControl.run();
            }
        }
    }

    @Override
    public boolean isDead() {
       for(HandControl handControl:controls){
           if(!handControl.isDead()){
               return false;
           }
       }
       return true;
    }

}
