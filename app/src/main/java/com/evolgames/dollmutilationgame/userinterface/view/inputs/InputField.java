package com.evolgames.dollmutilationgame.userinterface.view.inputs;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Image;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.AdvancedClickableBehavior;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.bounds.RectangularBounds;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

public class InputField<C extends Controller, B extends AdvancedClickableBehavior<C>>
        extends ClickableContainer<C, B> {

    protected final LinearLayout layout;
    private final boolean leftSlot;
    private final boolean rightSlot;
    protected int inputLength;

    public InputField(float pX, float pY, int mLength) {
        this(pX, pY, mLength, false, false);
    }

    public InputField(float pX, float pY, int mLength, boolean leftSlot, boolean rightSlot) {
        super(pX, pY);
        this.inputLength = mLength;
        this.layout = new LinearLayout(LinearLayout.Direction.Horizontal, -1);
        this.rightSlot = rightSlot;
        this.leftSlot = leftSlot;
        addElement(layout);
        createBase(leftSlot, rightSlot);
    }

    public boolean isLeftSlot() {
        return leftSlot;
    }

    public boolean isRightSlot() {
        return rightSlot;
    }

    private void createBase(boolean left, boolean right) {
        ArrayList<ITextureRegion> baseRegions = ResourceManager.getInstance().quantity.get("a");
        ArrayList<ITextureRegion> altBaseRegions = ResourceManager.getInstance().quantity.get("c");
        Image image = new Image(0, 0, left ? altBaseRegions.get(0) : baseRegions.get(0));
        layout.addToLayout(image);
        for (int i = 0; i < inputLength - 2; i++) {
            image = new Image(0, 0, baseRegions.get(1));
            layout.addToLayout(image);
        }
        image = new Image(0, 0, right ? altBaseRegions.get(2) : baseRegions.get(2));
        layout.addToLayout(image);
        setWidth(layout.getWidth());
        setHeight(layout.getHeight());
        RectangularBounds bounds =
                new RectangularBounds(
                        this, getWidth() - (right ? 27 : 0) - (left ? 27 : 0), getHeight(), left ? 27 : 0, 0);
        setBounds(bounds);
    }
}
