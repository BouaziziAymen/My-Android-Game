package com.evolgames.entities.usage;

import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.view.UserInterface;

public abstract class Use {
    private boolean controlsCreated;

    public boolean isControlsCreated() {
        return controlsCreated;
    }

    public abstract void onStep(float deltaTime);

    public void createControls(UsageButtonsController usageButtonsController, UserInterface userInterface) {
        controlsCreated = true;
    }
    public abstract float getUIWidth();

    public abstract void updateUIPosition(int row, int offset);
    public abstract void showControlButtons();
    public abstract void hideControlButtons();
}
