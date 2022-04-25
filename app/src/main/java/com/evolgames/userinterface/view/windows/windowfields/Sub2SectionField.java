package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController2;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class Sub2SectionField<C extends AbstractSectionedAdvancedWindowController2> extends SimpleTertiary<ButtonWithText<C>> {


    public Sub2SectionField(int primaryKey,int secondaryKey,int tertiaryKey, String sectionName, TiledTextureRegion textureRegion, C controller) {
        super(primaryKey, secondaryKey,tertiaryKey, new ButtonWithText<>(sectionName, 2, textureRegion, Button.ButtonType.Selector, true));

        this.setHeight(getMain().getHeight());
        this.setWidth(getMain().getWidth());
        getMain().setBehavior(new ButtonBehavior<C>(controller, getMain()) {
            @Override
            public void informControllerButtonClicked() {
                controller.onSecondaryButtonClicked(Sub2SectionField.this);
            }

            @Override
            public void informControllerButtonReleased() {
                controller.onSecondaryButtonReleased(Sub2SectionField.this);
            }
        });





    }


}

