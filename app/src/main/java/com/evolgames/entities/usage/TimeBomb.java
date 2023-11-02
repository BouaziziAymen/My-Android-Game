package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.properties.BombProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PlayerSpecialAction;
import com.evolgames.userinterface.control.buttonboardcontrollers.UsageButtonsController;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.List;

public class TimeBomb extends Use{

    private final List<BombModel> bombs;
    private final WorldFacade worldFacade;
    private float countDown;
    private boolean alive = true;
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

    public void onTriggerReleased() {
        count = true;
    }


    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Grenade;
    }


}
