package com.evolgames.entities.usage;

import com.evolgames.scenes.PlayerSpecialAction;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.view.UserInterface;

public abstract class Use {

    protected boolean active;

    public abstract void onStep(float deltaTime);

    public abstract PlayerSpecialAction getAction();
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
