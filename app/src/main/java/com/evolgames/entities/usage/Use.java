package com.evolgames.entities.usage;

import com.evolgames.scenes.PlayerSpecialAction;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.view.UserInterface;

public abstract class Use {

    public abstract void onStep(float deltaTime);

    public void createControls(UsageButtonsController usageButtonsController, UserInterface userInterface) {
    }
    public abstract float getUIWidth();

    public abstract void updateUIPosition(int row, int offset);
    public abstract void showUI();
    public abstract void hideUI();
    public abstract PlayerSpecialAction getAction();
}
