package com.evolgames.userinterface.view.inputs;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Image;
import com.evolgames.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.userinterface.view.layouts.LinearLayout;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

public class TypeABoard extends Container {

    private float usefulWidth;

    public TypeABoard(float pX, float pY, int mLength, boolean hasLeftHole) {
        super(pX, pY);
        LinearLayout layout = new LinearLayout(LinearLayout.Direction.Horizontal, -1);
        addElement(layout);

        ArrayList<ITextureRegion> baseRegions = ResourceManager.getInstance().buttonRegionsA;
        float width = 0;
        Image image = new Image(0, 0, baseRegions.get(hasLeftHole ? 3 : 2));
        width += baseRegions.get(hasLeftHole ? 3 : 2).getWidth();
        layout.addToLayout(image);

        for (int i = 0; i < mLength - 2; i++) {
            image = new Image(0, 0, baseRegions.get(0));
            layout.addToLayout(image);
            width += baseRegions.get(0).getWidth();
            usefulWidth += baseRegions.get(0).getWidth();
        }
        image = new Image(0, 0, baseRegions.get(1));
        width += baseRegions.get(1).getWidth();
        setWidth(width);
        setHeight(baseRegions.get(0).getHeight());

        RectangularBounds bounds = new RectangularBounds(this, getWidth(), getHeight());
        setBounds(bounds);
        layout.addToLayout(image);
        usefulWidth -= 8;
    }

    public float getUsefulWidth() {
        return usefulWidth;
    }
}
