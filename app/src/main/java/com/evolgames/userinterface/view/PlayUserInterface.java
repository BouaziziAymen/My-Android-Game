package com.evolgames.userinterface.view;

import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;
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

        createLastItemButton();

        setUpdated(true);
    }

    private void createLastItemButton() {
        Button<Controller> createLastItem =
                new Button<>(
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        createLastItem.setBehavior(
                new ButtonBehavior<Controller>(
                        new Controller() {
                            @Override
                            public void init() {
                            }
                        },
                        createLastItem) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        scene.createLastItem();
                    }
                });
        createLastItem.setPosition(0, 480 - 2 * ResourceManager.getInstance().simpleButtonTextureRegion.getHeight());
        addElement(createLastItem);
    }

    @Override
    public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {
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
