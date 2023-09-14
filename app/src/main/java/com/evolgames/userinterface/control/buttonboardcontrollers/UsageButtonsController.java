package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.usage.Use;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.UserInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UsageButtonsController extends Controller {
    private final UserInterface userInterface;
    private final GameScene gameScene;
    private final List<UIElement> uiElements;

    public UsageButtonsController(UserInterface userInterface, GameScene gameScene) {
        this.userInterface = userInterface;
        this.gameScene = gameScene;
        this.uiElements = new ArrayList<>();
    }

    public void addGameEntityControls(GameEntity gameEntity) {
        int col = 0;
        for (Use use : gameEntity.getUseList()) {
                if (use.isControlsCreated()) {
                    use.showUI();
                } else {
                    use.createControls(this, userInterface);
                }
            uiElements.add(new UIElement(gameEntity, use, col++));
            }
        updateLayout();
    }

    private void updateLayout() {
        List<GameEntity> entityList = uiElements.stream().map(e -> e.key).distinct().collect(Collectors.toList());
        int offset = 0;
        for (int r = 0; r < 10; r++) {
            int finalR = r;
            List<UIElement> list = uiElements.stream().filter(e -> {
                int row = entityList.indexOf(e.key);
                return row == finalR;
            }).sorted(Comparator.comparing(UIElement::getColumn)).collect(Collectors.toList());
            for (UIElement uiElement : list) {
                uiElement.use.updateUIPosition(r, offset);
                offset += uiElement.use.getUIWidth();
            }
        }
    }

    public void removeGameEntityControls(GameEntity gameEntity) {
        for (Use use : gameEntity.getUseList()) {
         use.hideUI();
        }
        uiElements.removeIf(e -> e.key == gameEntity);
        updateLayout();
    }

    @Override
    public void init() {

    }

    static class UIElement {
        int column;
        GameEntity key;
        Use use;

        UIElement(GameEntity g, Use u, int c) {
            this.key = g;
            this.use = u;
            this.column = c;
        }

        public int getColumn() {
            return column;
        }

    }


}
