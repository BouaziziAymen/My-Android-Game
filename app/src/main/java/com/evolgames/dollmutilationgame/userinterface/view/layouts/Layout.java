package com.evolgames.dollmutilationgame.userinterface.view.layouts;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Container;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public abstract class Layout extends Container {
    final float margin;

    Layout(float pX, float pY, float margin) {
        super(pX, pY);
        this.margin = margin;
    }

    Layout(float margin) {
        super();
        this.margin = margin;
    }

    abstract void addToLayout(Element e);
}
