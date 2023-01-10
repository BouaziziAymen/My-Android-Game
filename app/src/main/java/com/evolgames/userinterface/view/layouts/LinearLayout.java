package com.evolgames.userinterface.view.layouts;

import com.evolgames.userinterface.view.basics.Element;

import java.util.ArrayList;

public class LinearLayout extends Layout {
    protected ArrayList<Element> layoutElements = new ArrayList<>();
    private final Direction direction;


    public enum Direction {Horizontal, Vertical}

    public LinearLayout(float pX, float pY, Direction direction) {
        super(pX, pY, 0);
        this.direction = direction;
    }

    public LinearLayout(float pX, float pY, Direction direction, float margin) {
        super(pX, pY, margin);
        this.direction = direction;
    }

    public LinearLayout(Direction direction) {
        super(0, 0, 0);
        this.direction = direction;
    }

    public LinearLayout(Direction direction, float margin) {
        super(0, 0, margin);
        this.direction = direction;
    }


    public void updateLayout() {
        clearContents();
        layoutElements.clear();
        mHeight = 0;
        mWidth = 0;
        setUpdated(true);
    }

    public void addToLayout(Element element) {
        float position = 0;
        for (Element e : layoutElements) {
            final float step = (direction == Direction.Horizontal) ? e.getWidth() : e.getHeight();
            position += (direction == Direction.Horizontal) ? step + margin + e.getPadding() : -step - margin - e.getPadding();
        }

        if (direction == Direction.Horizontal) {
            element.setLowerBottomX(position);
            mWidth = position + element.getWidth();
            mHeight = Math.max(element.getHeight(), mHeight);
        } else {
            element.setLowerBottomY(position - element.getHeight());
            mHeight += element.getHeight() + element.getPadding();
            mWidth = Math.max(element.getWidth(), mWidth);
        }

        addElement(element);
        layoutElements.add(element);
    }


}
