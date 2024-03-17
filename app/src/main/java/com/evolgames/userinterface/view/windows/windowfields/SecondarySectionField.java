package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController2;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class SecondarySectionField<C extends AbstractSectionedAdvancedWindowController2>
        extends SimpleSecondary<ButtonWithText<C>> {

    public SecondarySectionField(
            int primaryKey,
            int secondaryKey,
            String sectionName,
            TiledTextureRegion textureRegion,
            C controller) {
        super(
                primaryKey,
                secondaryKey,
                new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true));

        this.setHeight(getMain().getHeight());
        this.setWidth(getMain().getWidth());
        getMain()
                .setBehavior(
                        new ButtonBehavior<C>(controller, getMain()) {
                            @Override
                            public void informControllerButtonClicked() {
                                controller.onSecondaryButtonClicked(SecondarySectionField.this);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onSecondaryButtonReleased(SecondarySectionField.this);
                            }
                        });
    }
}
