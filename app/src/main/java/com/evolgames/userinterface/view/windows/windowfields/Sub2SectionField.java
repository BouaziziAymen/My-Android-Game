package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.ThreeLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class Sub2SectionField<C extends ThreeLevelSectionedAdvancedWindowController>
        extends SimpleTertiary<ButtonWithText<C>> {

    public Sub2SectionField(
            int primaryKey,
            int secondaryKey,
            int tertiaryKey,
            String sectionName,
            TiledTextureRegion textureRegion,
            C controller) {
        super(
                primaryKey,
                secondaryKey,
                tertiaryKey,
                new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true));

        this.setHeight(getMain().getHeight());
        this.setWidth(getMain().getWidth());
        getMain()
                .setBehavior(
                        new ButtonBehavior<C>(controller, getMain()) {
                            @Override
                            public void informControllerButtonClicked() {
                                controller.onTertiaryButtonClicked(Sub2SectionField.this);
                            }

                            @Override
                            public void informControllerButtonReleased() {
                                controller.onTertiaryButtonReleased(Sub2SectionField.this);
                            }
                        });
    }
}
