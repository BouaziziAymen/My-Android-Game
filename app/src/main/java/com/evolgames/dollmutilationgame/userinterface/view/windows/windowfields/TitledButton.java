package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class TitledButton<C extends AdvancedWindowController<?>> extends TitledField<Button<C>> {
    public TitledButton(
            String titleString,
            TiledTextureRegion textureRegion,
            Button.ButtonType buttonType,
            float margin) {
        super(titleString, new Button<>(textureRegion, buttonType, true), margin);
    }

    public TitledButton(
            String titleString,
            TiledTextureRegion textureRegion,
            Button.ButtonType buttonType,
            float margin,
            boolean attachmentFirst) {
        super(titleString, new Button<>(textureRegion, buttonType, true), margin, attachmentFirst);
    }

    public TitledButton(
            String titleString,
            TiledTextureRegion textureRegion,
            Button.ButtonType buttonType,
            float margin,
            boolean attachmentFirst, int fontId) {
        super(titleString, new Button<>(textureRegion, buttonType, true), margin, attachmentFirst, fontId);
    }
}
