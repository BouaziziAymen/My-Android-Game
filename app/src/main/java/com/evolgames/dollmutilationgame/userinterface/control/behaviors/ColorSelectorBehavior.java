package com.evolgames.dollmutilationgame.userinterface.control.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ColorSelector;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import org.andengine.input.touch.TouchEvent;

public class ColorSelectorBehavior extends AdvancedClickableBehavior<ColorSelectorWindowController> {

    private final ColorSelector mSelector;

    public ColorSelectorBehavior(ColorSelectorWindowController controller, ColorSelector selector) {
        super(controller);
        mSelector = selector;
    }

    @Override
    public boolean processTouch(TouchEvent touchEvent) {

        float x = touchEvent.getX();
        float y = touchEvent.getY();
        Vector2 vector =
                new Vector2(mSelector.getAbsoluteX() + 64, mSelector.getAbsoluteY() + 64).sub(x, y);
        float distance = vector.len();
        if (distance < 47) {
            float saturation = distance / 47;
            mSelector.setSaturation(saturation); // FROM 0 TO 1

            float hue = (float) Math.atan2(vector.y, -vector.x) * GeometryUtils.TO_DEGREES + 90;
            if (hue < 0) hue += 360;
            mSelector.setHue(hue);
            mSelector.setValue(1f);
            mSelector.setAlpha(1f);

            getController().onHueAndSaturationUpdated();
            getController().resetSlots();

            return true;
        }
        return false;
    }
}
