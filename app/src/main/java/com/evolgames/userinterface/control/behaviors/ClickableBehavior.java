package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

import org.andengine.input.touch.TouchEvent;

public abstract class ClickableBehavior<C extends Controller> extends Behavior<C> {

    public ClickableBehavior(C controller) {
        super(controller);
    }
    public abstract boolean processTouch(TouchEvent touchEvent, boolean touched);

    public void onViewUpdated() {
        if(getController() instanceof LinearLayoutAdvancedWindowController){
            ((LinearLayoutAdvancedWindowController<?>) getController()).onVisibleZoneUpdate();
        }
    }
     protected  void updateWindowLayout(){
        if(getController() instanceof LinearLayoutAdvancedWindowController){

            LinearLayoutAdvancedWindowController<?> linearLayoutAdvancedWindowController = (LinearLayoutAdvancedWindowController<?>)getController() ;
            linearLayoutAdvancedWindowController.updateLayout();
        }
    }
}
