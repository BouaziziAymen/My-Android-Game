package com.evolgames.dollmutilationgame.userinterface.control.behaviors;

import com.evolgames.dollmutilationgame.userinterface.control.Controller;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

public abstract class AdvancedClickableBehavior<C extends Controller> extends ClickableBehavior<C> {

    public AdvancedClickableBehavior(C controller) {
        super(controller);
    }

    public void onViewUpdated() {
        if (getController() instanceof LinearLayoutAdvancedWindowController) {
            ((LinearLayoutAdvancedWindowController<?>) getController()).onVisibleZoneUpdate();
        }
    }

    protected void updateWindowLayout() {
        if (getController() instanceof LinearLayoutAdvancedWindowController) {
            LinearLayoutAdvancedWindowController<?> linearLayoutAdvancedWindowController =
                    (LinearLayoutAdvancedWindowController<?>) getController();
            linearLayoutAdvancedWindowController.updateLayout();
        }
    }
}
