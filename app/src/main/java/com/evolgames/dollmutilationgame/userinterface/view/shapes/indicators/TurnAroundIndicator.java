package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.controllers.Movable;
import com.evolgames.dollmutilationgame.scenes.EditorScene;

public abstract class TurnAroundIndicator extends FixedLengthArrowShape implements Movable {

    protected TurnAroundIndicator(Vector2 begin, EditorScene scene, float length, int size) {
        super(begin, scene, length, size);
    }

    public abstract void onTurnAroundCommand(float dA);
}
