package com.evolgames.dollmutilationgame.userinterface.view.layouts;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

import java.util.ArrayList;

public class LinearLayout extends Layout {
    private final Direction direction;
    private final float y0;
    protected ArrayList<Element> layoutElements = new ArrayList<>();

    public LinearLayout(float pX, float pY, Direction direction) {
        super(pX, pY, 0);
        y0 = pY;
        this.direction = direction;
    }

    public LinearLayout(float pX, float pY, Direction direction, float margin) {
        super(pX, pY, margin);
        y0 = pY;
        this.direction = direction;
    }

    public LinearLayout(Direction direction) {
        super(0, 0, 0);
        y0 = 0;
        this.direction = direction;
    }

    public LinearLayout(Direction direction, float margin) {
        super(0, 0, margin);
        y0 = 0;
        this.direction = direction;
    }

    public float getY0() {
        return y0;
    }

    public void updateLayout() {
        clearContents();
        layoutElements.clear();
        mHeight = 0;
        setUpdated(true);
    }

    public void addToLayout(Element element) {
        float d = 0;
        for (Element e : layoutElements) {

            if (direction == Direction.Horizontal) {
                final float step = e.getWidth();
                d = d + step + margin + e.getPadding();
            } else {
                final float step = e.getHeight();
                d = d - step - margin - e.getPadding();
            }
        }

        if (direction == Direction.Horizontal) {
            mWidth += element.getWidth() + margin;
            element.setLowerBottomX(d);
        } else {
            mHeight += element.getHeight() + margin + element.getPadding();
            element.setLowerBottomY(d - element.getHeight());
        }

        addElement(element);
        layoutElements.add(element);
        refreshOppositeDimension();
    }

    private void refreshOppositeDimension() {
        if (direction == Direction.Horizontal) {
            float maxy = 0;
            float miny = 0;
            for (Element e : layoutElements) {
                if (e.getLowerBottomY() < miny) miny = e.getLowerBottomY();
                if (e.getLowerBottomY() + e.getHeight() > maxy)
                    maxy = e.getLowerBottomY() + e.getHeight();
            }
            setHeight(maxy - miny);
        }
    }

    public enum Direction {
        Horizontal,
        Vertical
    }
}
