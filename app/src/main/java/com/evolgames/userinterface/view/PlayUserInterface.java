package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.PlayerAction;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.inputs.controllers.ControlPanel;

import org.andengine.input.touch.TouchEvent;

import java.util.Arrays;
import java.util.Comparator;

public class PlayUserInterface extends UserInterface<PlayScene> {

    private final ControlPanel panel;
    private final Switcher particularUsageSwitcher;
    private final Switcher generalUsageSwitcher;
    private PlayerSpecialAction[] particularUsages;

    public PlayUserInterface(PlayScene scene) {
        super(scene);
        // Grid grid = new Grid(scene);
        particularUsageSwitcher =
                new Switcher(
                        800f - 72f,
                        300,
                        ResourceManager.getInstance().particularUsages,
                        32f,
                        (index) -> scene.setSpecialAction(this.particularUsages[index]));
        particularUsageSwitcher.reset();
        addElement(particularUsageSwitcher);
        this.panel = new ControlPanel(scene);

        generalUsageSwitcher =
                new Switcher(
                        800f - 72f,
                        300f + 64f,
                        ResourceManager.getInstance().generalUsages,
                        32f,
                        (index) -> scene.setPlayerAction(PlayerAction.values()[index]));
        generalUsageSwitcher.reset(0, 1, 2, 3, 4, 5);
        addElement(generalUsageSwitcher);

        createBackToMenuButton();
        createLastItemButton();
        String[] items = new String[]{"melee$sword.xml", "rocket$guided_rocket.xml", "rocket$bazooka_rocket.xml", "melee$knife.xml", "grenade$grenade.xml", "gun$revolver.xml", "melee$morning_star.xml", "object$petrol_container.xml"};
        int t = 0;
        for (String item : items) {
            createItemButton(t, item);
            t++;
        }
        setUpdated(true);
    }

    private void createBackToMenuButton() {
        Button<Controller> backToMenu =
                new Button<>(
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        backToMenu.setBehavior(
                new ButtonBehavior<Controller>(
                        new Controller() {
                            @Override
                            public void init() {
                            }
                        },
                        backToMenu) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        scene.onPause();
                        scene.goToScene(SceneType.MENU);
                    }
                });
        backToMenu.setPosition(0, 480 - backToMenu.getHeight());
        addElement(backToMenu);
    }

    private String formatName(String filename) {
        String token = filename.split("\\$")[1];
        return token.substring(0, token.length() - 4);
    }

    private void createItemButton(int i, String name) {
        float height = ResourceManager.getInstance().simpleButtonTextureRegion.getHeight();
        ButtonWithText<Controller> createItemButton =
                new ButtonWithText<>(formatName(name), 2,
                        ResourceManager.getInstance().simpleButtonTextureRegion,
                        Button.ButtonType.OneClick,
                        true);
        createItemButton.setBehavior(
                new ButtonBehavior<Controller>(
                        new Controller() {
                            @Override
                            public void init() {
                            }
                        },
                        createItemButton) {
                    @Override
                    public void informControllerButtonClicked() {
                    }

                    @Override
                    public void informControllerButtonReleased() {
                        scene.createItem(name);
                    }
                });
        createItemButton.setPosition(0, 480 - (3 + i) * height);
        addElement(createItemButton);
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
        this.particularUsages = Arrays.copyOf(usages, usages.length);
        Arrays.sort(usages, Comparator.comparingInt(PlayerSpecialAction::ordinal).reversed());
        this.particularUsageSwitcher.reset(Arrays.stream(usages).mapToInt(e -> e.iconId).toArray());
    }

    public void updatePlayerSpecialActionOnSwitcher(PlayerSpecialAction playerSpecialAction) {
        while (scene.getSpecialAction() != playerSpecialAction) {
            this.particularUsageSwitcher.search();
        }
    }

    public void updatePlayerSpecialActionOnSwitcher(PlayerAction playerAction) {
        while (scene.getPlayerAction() != playerAction) {
            this.generalUsageSwitcher.search();
        }
    }

    public ControlPanel getPanel() {
        return panel;
    }
}
