package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Button;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class TitledButton<C extends AdvancedWindowController<?>> extends TitledField<Button<C>> {
    public TitledButton(String titleString, TiledTextureRegion textureRegion, Button.ButtonType buttonType, float margin) {
        super(titleString, new Button<>(textureRegion,buttonType,true), margin);
    }
    public TitledButton(String titleString, TiledTextureRegion textureRegion, Button.ButtonType buttonType, float margin, boolean attachmentFirst) {
        super(titleString, new Button<>(textureRegion,buttonType,true), margin,attachmentFirst);
    }
}
