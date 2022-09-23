package com.evolgames.userinterface.view.shapes.indicators;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.shapes.ImageShape;

import java.util.Arrays;

public class ScaleImageShape  extends ArrowShape {


    private final ImageShape imageShape;

    public ScaleImageShape(Vector2 begin, ImageShape imageShape, GameScene scene) {
        super(begin, scene);
        this.imageShape = imageShape;
    }


    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x,y);
     float post[] =  imageShape.getSprite().convertSceneCoordinatesToLocalCoordinates(x,y);

        Log.e("post", "/"+Arrays.toString(post));
        float disX = post[0]>0?post[0]:0;
        float disY = post[1]>0?post[1]:1;

        imageShape.getSprite().setWidth(disX);
        imageShape.getSprite().setHeight(disY);
        imageShape.updateSelf();

    }


}