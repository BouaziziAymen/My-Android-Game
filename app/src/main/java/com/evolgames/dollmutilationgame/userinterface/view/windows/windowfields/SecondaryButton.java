package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.userinterface.control.Controller;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SecondaryInterface;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class SecondaryButton<C extends Controller> extends LinearLayout
        implements SecondaryInterface {
    private final ButtonWithText<C> mainControl;
    private final int primaryKey;
    private final int secondaryKey;

    public SecondaryButton(
            int primaryKey, int secondaryKey, String sectionName, TiledTextureRegion textureRegion) {
        super(Direction.Horizontal, 4);
        this.primaryKey = primaryKey;
        this.secondaryKey = secondaryKey;
        mainControl =
                new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true);
        addToLayout(mainControl);
        this.setHeight(mainControl.getHeight());
        this.setWidth(mainControl.getWidth());
    }

    public void setMainBehavior(ButtonBehavior<C> behavior) {
        mainControl.setBehavior(behavior);
    }

    public Button<C> getMainControl() {
        return mainControl;
    }

    @Override
    public int getSecondaryKey() {
        return secondaryKey;
    }

    @Override
    public int getPrimaryKey() {
        return primaryKey;
    }
}
