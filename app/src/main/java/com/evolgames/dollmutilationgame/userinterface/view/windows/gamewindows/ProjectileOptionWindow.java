package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SimpleTitleField;
import com.evolgames.gameengine.R;

public class ProjectileOptionWindow extends SettingsWindow {

    public ProjectileOptionWindow(float pX, float pY, ProjectileOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.projectile_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
