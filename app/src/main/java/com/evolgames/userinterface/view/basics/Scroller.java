package com.evolgames.userinterface.view.basics;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.Touchable;
import com.evolgames.userinterface.view.layouts.LinearLayout;

import org.andengine.input.touch.TouchEvent;

public class Scroller extends LinearLayout implements Touchable {
    public static final float UPPER_WIDTH = 24f;
    private static final float UPPER_HEIGHT = 27f;
    private static final float SPACE_MARGIN = -1f;
    private static final float SLOT_WIDTH = 24f;
    private static final float TRAIL_WIDTH = 10f;
    private static final float TRAIL_HEIGHT = 32f;
    private final Image knob;

    private final LinearLayoutAdvancedWindowController<?> mController;
    private final float SCROLLABLE_HEIGHT;
    private final Button<LinearLayoutAdvancedWindowController<?>> upArrowButton;
    private final Button<LinearLayoutAdvancedWindowController<?>> downArrowButton;
    private final float visibilityLength;
    private float knobHeight;
    private boolean isTouched;

    public Scroller(
            float pX,
            float pY,
            int rows,
            LinearLayoutAdvancedWindowController<?> controller,
            float visibilityLength) {
        super(pX, pY, Direction.Vertical, SPACE_MARGIN);
        this.mController = controller;
        this.visibilityLength = visibilityLength;

        SCROLLABLE_HEIGHT = (TRAIL_HEIGHT + SPACE_MARGIN) * rows;

        Image upper = new Image(-SLOT_WIDTH / 2, 0, ResourceManager.getInstance().upperTextureRegion);
        addToLayout(upper);

        for (int i = 0; i < rows; i++) {
            Image middle =
                    new Image(-TRAIL_WIDTH / 2, 0, ResourceManager.getInstance().trailTextureRegion);
            addToLayout(middle);
        }
        Image lower = new Image(-SLOT_WIDTH / 2, 0, ResourceManager.getInstance().lowerTextureRegion);
        addToLayout(lower);

        upArrowButton =
                new Button<>(
                        upper.getLowerBottomX() + upper.getWidth() / 2 - 8,
                        upper.getLowerBottomY() + 15 - 7,
                        ResourceManager.getInstance().upButtonTextureRegions.get(0),
                        Button.ButtonType.OneClick,
                        true);
        addElement(upArrowButton);
        upArrowButton.setBehavior(
                new ButtonBehavior<LinearLayoutAdvancedWindowController<?>>(controller, upArrowButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        incrementAdvance(-0.1f);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });

        downArrowButton =
                new Button<>(
                        lower.getLowerBottomX() + lower.getWidth() / 2 - 8,
                        lower.getLowerBottomY() + 12 - 7,
                        ResourceManager.getInstance().downButtonTextureRegions.get(0),
                        Button.ButtonType.OneClick,
                        true);
        addElement(downArrowButton);

        downArrowButton.setBehavior(
                new ButtonBehavior<LinearLayoutAdvancedWindowController<?>>(controller, downArrowButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        incrementAdvance(0.1f);
                    }

                    @Override
                    public void informControllerButtonReleased() {
                    }
                });
        downArrowButton.setDepth(-1);
        upArrowButton.setDepth(-1);

        knob = new Image(-TRAIL_WIDTH / 2, 0, ResourceManager.getInstance().scrollerKnobTextureRegion);
        addElement(knob);
        knob.setColor(143 / 255f, 86 / 255f, 59 / 255f);
        knob.setDepth(1);
    }

    private float getEffectiveSup() {
        return getAbsoluteY() - UPPER_HEIGHT;
    }

    private float getEffectiveInf() {
        return getEffectiveSup() - SCROLLABLE_HEIGHT;
    }

    public void onLayoutHeightUpdated(float layoutHeight) {
        float ratio = layoutHeight == 0.0 ? 0 : visibilityLength / layoutHeight;
        float advance = 0;
        if (ratio >= 1) {
            upArrowButton.updateState(Button.State.DISABLED);
            downArrowButton.updateState(Button.State.DISABLED);
            knob.setVisible(false);
        } else {
            upArrowButton.updateState(Button.State.NORMAL);
            downArrowButton.updateState(Button.State.NORMAL);
            knob.setVisible(true);
            updateKnobSize(ratio);
            correctKnob();
            advance = getAdvance();
        }

        mController.onScrolled(advance);
    }

    private void incrementAdvance(float dA) {
        float advance = getAdvance();
        updateKnobPosition(advance + dA);
        knob.setUpdated(true);
        mController.onScrolled(advance + dA);
    }

    private float getAdvance() {
        return -(knob.getLowerBottomY() + knobHeight + UPPER_HEIGHT) / SCROLLABLE_HEIGHT;
    }

    private void updateKnobPosition(float newAdvance) {
        float newY = -newAdvance * SCROLLABLE_HEIGHT - knobHeight - UPPER_HEIGHT;
        knob.setLowerBottomY(newY);
        correctKnob();
    }


    private void updateKnobSize(float ratio) {
        knobHeight = SCROLLABLE_HEIGHT * ratio;
        knob.setScale(1, knobHeight / 100f);
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        if (!knob.isVisible()) return false;
        if (pTouchEvent.isActionDown()) {
            if (Math.abs(pTouchEvent.getX() - getAbsoluteX()) < UPPER_WIDTH / 2) {
                if (pTouchEvent.getY() < getEffectiveSup() && pTouchEvent.getY() > getEffectiveInf()) {
                    this.isTouched = true;
                }
            }
        }

        if (this.isTouched) {
            if (pTouchEvent.isActionOutside()
                    || pTouchEvent.isActionCancel()
                    || pTouchEvent.isActionUp()) {
                this.isTouched = false;
                return true;
            }
        }
        if (this.isTouched) {
            float newY = pTouchEvent.getY() - getAbsoluteY() - knobHeight / 2;
            knob.setLowerBottomY(newY);
            correctKnob();
            knob.setUpdated(true);
            mController.onScrolled(getAdvance());
            return true;
        }

        return false;
    }

    private void correctKnob() {
        float newY = knob.getLowerBottomY();
        if (newY > -UPPER_HEIGHT - knobHeight) newY = -knobHeight - UPPER_HEIGHT;
        if (newY < -UPPER_HEIGHT - SCROLLABLE_HEIGHT) newY = -UPPER_HEIGHT - SCROLLABLE_HEIGHT;
        knob.setLowerBottomY(newY);
    }
}
