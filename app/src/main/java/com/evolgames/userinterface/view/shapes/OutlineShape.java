package com.evolgames.userinterface.view.shapes;

import com.evolgames.userinterface.model.OutlineModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Container;

import org.andengine.entity.primitive.LineLoop;

public abstract class OutlineShape<M extends OutlineModel<?>> extends Container {
    protected M outlineModel;
    protected LineLoop lineLoop;
    protected UserInterface userInterface;
    protected float r = 1f, g = 1f, b = 1f;

    public OutlineShape(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public M getOutlineModel() {
        return outlineModel;
    }

    public void setOutlineModel(M outlineModel) {
        this.outlineModel = outlineModel;
    }

    public void setLineLoopColor(float r, float g, float b) {
        if (lineLoop != null) {
            lineLoop.setColor(r, g, b);
        }
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setLineLoopColor(Color c) {
        setLineLoopColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    public abstract void onModelUpdated();

    protected abstract void updateOutlineShape();

    public abstract void dispose();


}
