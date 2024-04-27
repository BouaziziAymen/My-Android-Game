package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController2;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ButtonWithText;
import com.evolgames.dollmutilationgame.userinterface.view.sections.basic.SimplePrimary;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class SectionField<C extends AbstractSectionedAdvancedWindowController2>
        extends SimplePrimary<ButtonWithText<C>> {

    public SectionField(
            int primaryKey, String sectionName, TiledTextureRegion textureRegion, C controller) {
        super(
                primaryKey,
                new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true));

        this.setHeight(getMain().getHeight());
        this.setWidth(getMain().getWidth());
        getMain()
                .setBehavior(
                        new ButtonBehavior<C>(controller, getMain()) {
                            @Override
                            public void informControllerButtonClicked() {
                                controller.onPrimaryButtonClicked(SectionField.this);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onPrimaryButtonReleased(SectionField.this);
                            }
                        });
    }
}
