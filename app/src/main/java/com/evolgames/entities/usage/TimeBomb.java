package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.properties.BombProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.physics.WorldFacade;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.List;

public class TimeBomb extends Use{

    private final List<BombModel> bombs;
    private final WorldFacade worldFacade;
    private float countDown;
    private boolean alive = true;
    private Button<UsageButtonsController> triggerButton;
    private boolean count;

    public TimeBomb(UsageModel usageModel, WorldFacade worldFacade){
        TimeBombUsageProperties properties = (TimeBombUsageProperties) usageModel.getProperties();
        this.countDown = properties.getDelay();
        this.bombs = properties.getBombModelList();
        this.worldFacade = worldFacade;
    }
    @Override
    public void onStep(float deltaTime) {
       if(!this.alive){
           return;
       }
       if(count){
        countDown-= deltaTime;
        if(countDown<0) {
            for (BombModel bombModel : bombs) {
                Body body = bombModel.getGameEntity().getBody();
                Vector2 pos = bombModel.getProperties().getBombPosition().cpy().sub(bombModel.getGameEntity().getCenter()).mul(1 / 32f);
                Vector2 worldPos = body.getWorldPoint(pos).cpy();
                BombProperties bp = bombModel.getProperties();
               worldFacade.createExplosion(bombModel.getGameEntity(),worldPos.x, worldPos.y,bp.getFireRatio(),bp.getSmokeRatio(),bp.getSparkRatio(),
                       bp.getParticles(),bp.getForce(),bp.getHeat(),bp.getSpeed());
            }
            this.alive = false;
        }
        }
    }

    @Override
    public void createControls(UsageButtonsController usageButtonsController, UserInterface userInterface) {
        super.createControls(usageButtonsController, userInterface);
        triggerButton = new Button<>(ResourceManager.getInstance().arcadeRedTextureRegion, Button.ButtonType.OneClick, true);
        triggerButton.setBehavior(new ButtonBehavior<UsageButtonsController>(usageButtonsController, triggerButton) {
            @Override
            public void informControllerButtonClicked() {
            }

            @Override
            public void informControllerButtonReleased() {
                TimeBomb.this.onTriggerReleased();
            }
        });
        triggerButton.setPosition(800 - triggerButton.getWidth(), 0);
        userInterface.addElement(triggerButton);
        triggerButton.setScale(2,2);
    }

    private void onTriggerReleased() {
        count = true;
    }

    @Override
    public float getUIWidth() {
        return triggerButton.getWidth();
    }

    @Override
    public void updateUIPosition(int row, int offset) {
        triggerButton.setPosition(800 -offset- triggerButton.getWidth(), row*32f);
    }
    @Override
    public void showControlButtons() {
        triggerButton.setVisible(true);
    }
    @Override
    public void hideControlButtons() {
        triggerButton.setVisible(false);
    }
}
