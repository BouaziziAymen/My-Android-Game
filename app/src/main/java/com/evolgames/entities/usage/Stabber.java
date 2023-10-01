package com.evolgames.entities.usage;

import com.evolgames.entities.hand.MoveWithRevertHandControl;
import com.evolgames.scenes.PlayerSpecialAction;

public class Stabber extends MeleeUse{
    private MoveWithRevertHandControl handControl;

    @Override
    public void onStep(float deltaTime) {

    }


    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Stab;
    }

    public void setHandControl(MoveWithRevertHandControl handControl) {
        this.handControl = handControl;
    }

    public MoveWithRevertHandControl getHandControl() {
        return handControl;
    }
}
