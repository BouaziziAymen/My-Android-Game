package com.evolgames.userinterface.view.inputs.bounds;

import com.evolgames.userinterface.view.basics.Element;

public abstract class Bounds {
    protected Element element;
    public abstract boolean isInBounds(float pX, float pY);
    public Bounds(Element e){
        this.element = e;
    }
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
