package com.evolgames.userinterface.view;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.scenes.PlayScene;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.controllers.ControlPanel;

import org.andengine.input.touch.TouchEvent;

import java.util.Arrays;
import java.util.Comparator;

public class PlayUserInterface extends UserInterface<PlayScene> {

    private final ControlPanel panel;

    public PlayUserInterface(PlayScene scene) {
        super(scene);

        this.panel = new ControlPanel(scene);

        setUpdated(true);
    }

    @Override
    public void onTouchScene(TouchEvent pTouchEvent) {
    }

    public void updateParticularUsageSwitcher(PlayerSpecialAction[] usages) {
        Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal));
        PlayerSpecialAction[] particularUsages = Arrays.copyOf(usages, usages.length);
        Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal).reversed());
    }


    public ControlPanel getPanel() {
        return panel;
    }
}
