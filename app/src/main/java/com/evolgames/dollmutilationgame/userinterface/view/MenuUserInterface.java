package com.evolgames.dollmutilationgame.userinterface.view;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.MenuScene;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.actions.Action;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Dummy;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.ScaleClickable;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;

public class MenuUserInterface extends UserInterface<MenuScene> {
    public MenuUserInterface(MenuScene scene) {
        super(scene);
        Controller controller = new Controller() {
            @Override
            public void init() {

            }
        };

        Element imaginary = new Dummy();
        imaginary.setLowerBottomX(400 - 120);
        imaginary.setLowerBottomY(100 - 40);
        RectangularBounds rectangularBounds = new RectangularBounds(imaginary, 240, 80);
        createScaleButton(400, 100, controller, ResourceManager.getInstance().playTextureRegion, rectangularBounds, () -> scene.goToScene(SceneType.PLAY));


        setUpdated(true);

    }

    private void createScaleButton(float x, float y, Controller controller, TextureRegion region, RectangularBounds bounds, Action action) {
        float xl = x - (region.getWidth() / 2f);
        float yl = y - (region.getHeight() / 2f);
        ScaleClickable<Controller> scaleClickable = new ScaleClickable<>(xl, yl, region, controller, bounds
                , action);
        this.addElement(scaleClickable);
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        return super.onTouchHud(pTouchEvent);
    }

    @Override
    public void onTouchScene(TouchEvent pTouchEvent) {
    }
}
